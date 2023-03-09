<template>
  <div class="login-container">
    <el-row>
      <el-col :xs="24" :sm="24" :md="12" :lg="16" :xl="16">
        <div style="color: transparent">占位符</div>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          class="login-form"
          label-position="left"
        >
          <div class="title-tips">欢迎访问{{ title }}</div>
          <el-form-item style="margin-top: 40px" prop="username">
            <span class="svg-container svg-container-admin">
              <vab-icon :icon="['fas', 'user']" />
            </span>
            <el-input
              v-model.trim="form.username"
              v-focus
              placeholder="请输入用户名"
              tabindex="1"
              type="text"
            />
          </el-form-item>
          <el-form-item prop="password">
            <span class="svg-container">
              <vab-icon :icon="['fas', 'lock']" />
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model.trim="form.password"
              :type="passwordType"
              tabindex="2"
              placeholder="请输入密码"
              @keyup.enter.native="handleLogin"
            />
            <span
              v-if="passwordType === 'password'"
              class="show-password"
              @click="handlePassword"
            >
              <vab-icon :icon="['fas', 'eye-slash']" />
            </span>
            <span v-else class="show-password" @click="handlePassword">
              <vab-icon :icon="['fas', 'eye']" />
            </span>
          </el-form-item>
          <el-form-item prop="code" hidden>
            <span class="svg-container">
              <vab-icon :icon="['fas', 'shield-alt']" />
            </span>
            <el-input
              ref="code"
              v-model="form.code"
              auto-complete="off"
              placeholder="验证码"
              style="width: 63%"
              @keyup.enter.native="handleLogin"
            />
            <div class="login-code">
              <img :src="codeUrl" @click="getCode">
            </div>
          </el-form-item>
          <el-button
            :loading="loading"
            class="login-btn"
            type="primary"
            @click="handleLogin"
          >
            登录
          </el-button>
          <Verify
            ref="verify"
            :img-size="{ width: '330px', height: '155px' }"
            :mode="'pop'"
            :captcha-type="'blockPuzzle'"
            @success="loginCaptchaVerification"
          />
        </el-form>
      </el-col>
    </el-row>
    <!--  底部  -->
    <div v-if="$store.state.settings.showFooter" id="el-login-footer">
      <span v-html="$store.state.settings.footerTxt" />
      <span> ⋅ </span>
      <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">{{
        $store.state.settings.caseNumber
      }}</a>
      <span>v:{{ $store.state.settings.versionNumber }}</span>
    </div>
  </div>
</template>

<script>
import '../components/icon/vabIcon'
import { getCodeImg } from '@/api/login'
import { encrypt } from '@/utils/rsaEncrypt'
import Cookies from 'js-cookie'
import qs from 'qs'
const defaultSettings = require('../settings.js')
import { MessageBox } from 'element-ui'
import store from '@/store'
import Verify from '../components/verifition/Verify'

export default {
  name: 'Login',
  components: { Verify },
  directives: {
    focus: {
      inserted(el) {
        el.querySelector('input').focus()
      }
    }
  },
  data() {
    return {
      nodeEnv: process.env.NODE_ENV,
      title: defaultSettings.title,
      codeUrl: '',
      form: {
        username: '',
        password: '',
        code: '',
        uuid: '',
        captchaVerification: ''
      },
      rules: {
        username: [
          { required: true, trigger: 'blur', message: '用户名不能为空' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '密码不能为空' }
        ],
        code: [{ required: false, trigger: 'change', message: '验证码不能为空' }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const data = route.query
        if (data && data.redirect) {
          this.redirect = data.redirect
          delete data.redirect
          if (JSON.stringify(data) !== '{}') {
            this.redirect = this.redirect + '&' + qs.stringify(data, { indices: false })
          }
        } else {
          this.redirect = '/'
        }
      },
      immediate: true
    }
  },
  created() {
    document.body.style.overflow = 'hidden'
    // 获取验证码
    this.getCode()
    // token 过期提示
    this.point()
  },
  beforeDestroy() {
    document.body.style.overflow = 'auto'
  },
  methods: {
    loginCaptchaVerification(params) {
      // params 返回的二次验证参数, 和登录参数一起回传给登录接口，方便后台进行二次验证
      this.form.captchaVerification = params.captchaVerification
      this.doLogin()
    },
    getCode() {
      getCodeImg().then((res) => {
        this.codeUrl = res.img
        this.form.uuid = res.uuid
        this.form.code = null
        this.$refs.code.focus()
      })
    },
    handlePassword() {
      this.passwordType === 'password'
        ? (this.passwordType = '')
        : (this.passwordType = 'password')
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.$refs.verify) {
            this.$refs.verify.show()
          } else {
            this.doLogin()
          }
        } else {
          return false
        }
      })
    },
    doLogin() {
      const user = {
        username: this.form.username,
        password: this.form.password,
        code: this.form.code,
        uuid: this.form.uuid,
        captchaVerification: this.form.captchaVerification
      }
      user.password = encrypt(user.password)
      this.loading = true
      this.$store
        .dispatch('Login', user)
        .then(() => {
          this.loading = false
          const hour = new Date().getHours()
          const thisTime =
                hour < 8
                  ? '早上好'
                  : hour <= 11
                    ? '上午好'
                    : hour <= 13
                      ? '中午好'
                      : hour < 18
                        ? '下午好'
                        : '晚上好'
          this.$notify({
            title: `${thisTime}！`,
            message: `欢迎登录${this.title}`,
            type: 'success'
          })
          const isTipResetPwd = store.getters.user.isTipResetPwd
          if (isTipResetPwd) {
            MessageBox.confirm(
              '登录密码即将过期，是否去修改密码',
              '系统提示',
              {
                confirmButtonText: '修改密码',
                cancelButtonText: '取消',
                type: 'warning'
              }
            )
              .then(() => {
                this.$router.push({ path: '/user/center' })
              })
              .catch(() => {
                this.$router.push({ path: this.redirect || '/' })
              })
          } else {
            this.$router.push({ path: this.redirect || '/' })
          }
        })
        .catch(() => {
          this.loading = false
          this.getCode()
        })
    },
    point() {
      const point = Cookies.get('point') !== undefined
      if (point) {
        this.$notify({
          title: '提示',
          message: '当前登录状态已过期，请重新登录！',
          type: 'warning',
          duration: 5000
        })
        Cookies.remove('point')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  background: url('~@/assets/images/background.jpg') center center fixed
    no-repeat;
  background-size: cover;

  .title {
    font-size: 54px;
    font-weight: 500;
    color: rgba(14, 18, 26, 1);
  }

  .title-tips {
    margin-top: 29px;
    font-size: 26px;
    font-weight: 400;
    color: rgba(14, 18, 26, 1);
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .login-btn {
    display: inherit;
    width: 220px;
    height: 60px;
    margin-top: 5px;
    border: 0;

    &:hover {
      opacity: 0.9;
    }
  }

  .login-form {
    position: relative;
    max-width: 100%;
    margin: calc((100vh - 425px) / 2) 10% 10%;
    overflow: hidden;

    .forget-password {
      width: 100%;
      margin-top: 40px;
      text-align: left;

      .forget-pass {
        width: 129px;
        height: 19px;
        font-size: 20px;
        font-weight: 400;
        color: rgba(92, 102, 240, 1);
      }
    }
  }

  .tips {
    margin-bottom: 10px;
    font-size: 14px;
    color: #fff;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .title-container {
    position: relative;

    .title {
      margin: 0 auto 40px auto;
      font-size: 34px;
      font-weight: bold;
      color: #1890ff;
      text-align: center;
    }
  }

  .svg-container {
    position: absolute;
    top: 14px;
    left: 15px;
    z-index: 999;
    font-size: 16px;
    color: #d7dee3;
    cursor: pointer;
    user-select: none;
  }

  .show-password {
    position: absolute;
    top: 14px;
    right: 25px;
    font-size: 16px;
    color: #d7dee3;
    cursor: pointer;
    user-select: none;
  }

  ::v-deep {
    .el-form-item {
      padding-right: 0;
      margin: 20px 0;
      color: #454545;
      background: transparent;
      border: 1px solid transparent;
      border-radius: 2px;

      &__content {
        min-height: 32px;
        line-height: 32px;
      }

      &__error {
        position: absolute;
        top: 100%;
        left: 18px;
        font-size: 12px;
        line-height: 18px;
        color: #ff4d4f;
      }
    }

    .el-input {
      box-sizing: border-box;

      input {
        height: 58px;
        padding-left: 45px;
        font-size: 14px;
        line-height: 58px;
        color: #606266;
        background: #f6f4fc;
        border: 0;
        caret-color: #606266;
      }
    }

    .login-code {
      width: 33%;
      display: inline-block;
      height: 58px;
      line-height: 58px;
      float: right;

      img {
        cursor: pointer;
        vertical-align: middle;
      }
    }
  .verifybox {
    left: 81%;
    top: 62%;
    }
  }
}
</style>
