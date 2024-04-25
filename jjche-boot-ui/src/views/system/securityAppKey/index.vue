<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.appId"
          clearable
          placeholder="应用id"
          style="width: 185px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-select
          v-model="query.enabled"
          filterable
          clearable
          placeholder="状态"
          class="filter-item"
        >
          <el-option
            v-for="item in enabled_status"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog
        :close-on-click-modal="false"
        :before-close="crud.cancelCU"
        :visible.sync="crud.status.cu > 0"
        :title="crud.status.title"
        width="600px"
      >
        <el-form ref="form" v-loading="crud.editLoading" :model="form" :rules="rules" size="small" label-width="150px">
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" style="width: 370px" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.comment" style="width: 370px" />
          </el-form-item>
          <el-form-item label="appId" prop="appId">
            <el-input v-model="form.appId" style="width: 370px" />
          </el-form-item>
          <el-form-item label="appSecret" prop="appSecret">
            <el-input v-model="form.appSecret" style="width: 370px">
              <el-button slot="append" icon="el-icon-refresh-right" @click="refreshAppSecret" />
            </el-input>
          </el-form-item>
          <el-form-item label="aesKey" prop="encKey">
            <el-input v-model="form.encKey" style="width: 370px">
              <el-button slot="append" icon="el-icon-refresh-right" @click="refreshEncKey" />
            </el-input>
          </el-form-item>
          <el-form-item label="地址，多个换行" prop="urls">
            <el-input v-model="form.urls" type="textarea" style="width: 370px" />
          </el-form-item>
          <el-form-item label="白名单，多个换行">
            <el-input v-model="form.whiteIp" type="textarea" style="width: 370px" />
          </el-form-item>
          <el-form-item label="限速, 每秒可请求">
            <el-input-number v-model="form.limitCount" :min="0" placeholder="0不限制" />/次
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button
            :loading="crud.status.cu == 2"
            type="primary"
            @click="crud.submitCU"
          >确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table
        ref="table"
        v-loading="crud.loading"
        highlight-current-row
        stripe
        :data="crud.data"
        size="small"
        style="width: 100%"
        @selection-change="crud.selectionChangeHandler"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="appId" label="appId" />
        <el-table-column prop="enabled" label="状态">
          <template v-slot="scope">
            <el-switch
              v-model="scope.row.enabled"
              @change="changeEnabled(scope.row, scope.row.enabled)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="gmtCreate" label="创建时间" />
        <el-table-column
          v-permission="['admin','securityAppKey:edit','securityAppKey:del']"
          label="操作"
          width="150px"
          align="center"
        >
          <template v-slot="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudSecurityAppKey from '@/api/securityAppKey/api'
import CRUD, { crud, form, header, presenter } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = {
  id: null,
  name: null,
  comment: null,
  appId: null,
  appSecret: null,
  encKey: null,
  urls: null,
  whiteIp: null,
  limitCount: 0
}
export default {
  name: 'SecurityAppKey',
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({
      title: '应用密钥',
      url: 'sys/security_app_keys',
      idField: 'id',
      sort: 'id DESC',
      crudMethod: { ...crudSecurityAppKey }
    })
  },
  data() {
    return {
      enabled_status: [{ label: '激活', value: true }, { label: '禁用', value: false }],
      permission: {
        add: ['admin', 'securityAppKey:add'],
        edit: ['admin', 'securityAppKey:edit'],
        del: ['admin', 'securityAppKey:del']
      },
      rules: {
        name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
        appId: [{ required: true, message: 'appId不能为空', trigger: 'blur' }],
        appSecret: [{ required: true, message: 'appSecret不能为空', trigger: 'blur' }],
        encKey: [{ required: true, message: 'aesKey不能为空', trigger: 'blur' }],
        urls: [{ required: true, message: '地址不能为空', trigger: 'blur' }]
      }
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    randomString(len) {
      len = len || 32
      const $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678' /** **默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
      const maxPos = $chars.length
      let pwd = ''
      for (let i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos))
      }
      return pwd
    },
    refreshEncKey() {
      this.form.encKey = this.randomString(16)
    },
    refreshAppSecret() {
      this.form.appSecret = this.randomString(20)
    },
    // 改变状态
    changeEnabled(data, val) {
      const str = val ? '激活' : '禁用'
      this.$confirm(
        str + '密钥, 是否继续？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(() => {
          data.enabled = val
          crudSecurityAppKey
            .edit(data)
            .then((res) => {
              this.crud.notify(
                '操作成功',
                CRUD.NOTIFICATION_TYPE.SUCCESS
              )
            })
            .catch(() => {
              data.enabled = !data.enabled
            })
        })
        .catch(() => {
          data.enabled = !data.enabled
        })
    }
  }
}
</script>

<style scoped></style>
