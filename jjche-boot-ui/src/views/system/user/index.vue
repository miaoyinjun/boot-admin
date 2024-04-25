<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--侧边部门数据-->
      <el-col :xs="9" :sm="6" :md="5" :lg="4" :xl="4">
        <div class="head-container">
          <el-input
            v-model="deptName"
            clearable
            size="small"
            placeholder="输入部门名称搜索"
            prefix-icon="el-icon-search"
            class="filter-item"
            @input="getDeptDatas"
          />
        </div>
        <el-tree
          :data="deptDatas"
          :load="getDeptDatas"
          :props="defaultProps"
          :expand-on-click-node="false"
          lazy
          @node-click="handleNodeClick"
        />
      </el-col>
      <!--用户数据-->
      <el-col :xs="15" :sm="18" :md="19" :lg="20" :xl="20">
        <!--工具栏-->
        <div class="head-container">
          <div v-if="crud.props.searchToggle">
            <!-- 搜索 -->
            <el-input
              v-model="query.blurry"
              clearable
              size="small"
              placeholder="输入名称或者邮箱搜索"
              style="width: 200px"
              class="filter-item"
              @keyup.enter.native="crud.toQuery"
            />
            <date-range-picker v-model="query.gmtCreate" class="date-item" />
            <el-select
              v-model="query.enabled"
              clearable
              size="small"
              placeholder="状态"
              class="filter-item"
              style="width: 90px"
              @change="crud.toQuery"
            >
              <el-option
                v-for="item in enabledTypeOptions"
                :key="item.key"
                :label="item.display_name"
                :value="item.key"
              />
            </el-select>
            <rrOperation />
          </div>
          <crudOperation show="" :permission="permission" />
        </div>
        <!--表单渲染-->
        <el-dialog
          append-to-body
          :close-on-click-modal="false"
          :before-close="crud.cancelCU"
          :visible.sync="crud.status.cu > 0"
          :title="crud.status.title"
          width="570px"
        >
          <el-form
            ref="form"
            :inline="true"
            :model="form"
            :rules="rules"
            size="small"
            label-width="66px"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model.trim="form.username" />
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input v-model.number.trim="form.phone" />
            </el-form-item>
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model.trim="form.nickName" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model.trim="form.email" />
            </el-form-item>
            <el-form-item label="部门" prop="dept.id">
              <treeselect
                v-model="form.dept.id"
                :options="depts"
                :load-options="loadDepts"
                style="width: 178px"
                placeholder="选择部门"
              />
            </el-form-item>
            <el-form-item label="岗位" prop="jobIds">
              <el-select
                v-model="form.jobIds"
                style="width: 178px"
                multiple
                placeholder="请选择"
              >
                <el-option
                  v-for="item in jobs"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender" style="width: 178px">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="状态">
              <el-radio-group
                v-model="form.enabled"
                :disabled="form.id === user.id"
              >
                <el-radio
                  v-for="item in dict.user_status"
                  :key="item.id"
                  :label="item.value"
                >{{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item style="margin-bottom: 0" label="角色" prop="roleIds">
              <el-select
                v-model="form.roleIds"
                style="width: 437px"
                multiple
                placeholder="请选择"
              >
                <el-option
                  v-for="item in roles"
                  :key="item.name"
                  :disabled="level !== 1 && item.level <= level"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="crud.cancelCU">取消</el-button>
            <el-button
              :loading="crud.status.cu === 2"
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
          style="width: 100%"
          @selection-change="crud.selectionChangeHandler"
        >
          <el-table-column
            :selectable="checkboxT"
            type="selection"
            width="55"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="username"
            label="用户名"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="nickName"
            label="昵称"
          />
          <el-table-column prop="gender" label="性别" />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="phone"
            width="100"
            label="电话"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            width="135"
            prop="email"
            label="邮箱"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            width="135"
            prop="role"
            label="角色"
          >
            <template v-slot="scope">
              <span v-for="role in scope.row.roles" :key="role.id">
                {{ role.name }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="dept"
            label="部门"
          >
            <template v-slot="scope">
              <div>{{ scope.row.dept.name }}</div>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" prop="enabled">
            <template v-slot="scope">
              <el-switch
                v-model="scope.row.enabled"
                :disabled="!checkPermission(permission.edit)"
                active-color="#409EFF"
                inactive-color="#F56C6C"
                @change="changeEnabled(scope.row, scope.row.enabled)"
              />
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="gmtCreate"
            width="135"
            label="创建日期"
          >
            <template v-slot="scope">
              <span>{{ scope.row.gmtCreate }}</span>
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="lastLoginTime"
            width="135"
            label="最后一次登陆时间"
          >
            <template v-slot="scope">
              <span>{{ scope.row.lastLoginTime }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-permission="['admin', 'user:edit', 'user:del']"
            label="操作"
            width="160"
            align="center"
            fixed="right"
          >
            <template v-slot="scope">
              <el-button
                v-permission="['admin']"
                :disabled="scope.row.username === 'admin'"
                size="mini"
                type="warning"
                icon="el-icon-key"
                style="display: inline-block"
                @click="handleResetPwd(scope.row)"
              />
              <div style="display: inline-block">
                <udOperation
                  :data="scope.row"
                  :permission="permission"
                  :disabled-dle="scope.row.id === user.id"
                />
              </div>
            </template>
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <pagination />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import crudUser from '@/api/system/user'
import { resetPass } from '@/api/system/user'
import { isvalidPhone } from '@/utils/validate'
import { getDepts, getDeptSuperior } from '@/api/system/dept'
import { getAll, getLevel } from '@/api/system/role'
import { getAllJob } from '@/api/system/job'
import Crud, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import DateRangePicker from '@/components/DateRangePicker'
import Treeselect from '@riophae/vue-treeselect'
import { mapGetters } from 'vuex'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { LOAD_CHILDREN_OPTIONS } from '@riophae/vue-treeselect'
import { Notification } from 'element-ui'
import checkPermission from '@/utils/permission'

const defaultForm = {
  id: null,
  username: null,
  nickName: null,
  gender: '男',
  email: null,
  enabled: 'true',
  roleIds: [],
  jobIds: [],
  jobs: [],
  roles: [],
  dept: { id: null },
  phone: null
}
export default {
  name: 'User',
  components: {
    Treeselect,
    crudOperation,
    rrOperation,
    udOperation,
    pagination,
    DateRangePicker
  },
  cruds() {
    return Crud({
      title: '用户',
      url: 'sys/users',
      crudMethod: { ...crudUser }
    })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  // 数据字典
  dicts: ['user_status'],
  data() {
    // 自定义验证
    const validPhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入电话号码'))
      } else if (!isvalidPhone(value)) {
        callback(new Error('请输入正确的11位手机号码'))
      } else {
        callback()
      }
    }
    return {
      height: document.documentElement.clientHeight - 180 + 'px;',
      deptName: '',
      depts: [],
      deptDatas: [],
      jobs: [],
      level: 3,
      roles: [],
      defaultProps: { children: 'children', label: 'name', isLeaf: 'leaf' },
      permission: {
        add: ['admin', 'user:add'],
        edit: ['admin', 'user:edit'],
        del: ['admin', 'user:del']
      },
      enabledTypeOptions: [
        { key: 'true', display_name: '激活' },
        { key: 'false', display_name: '锁定' }
      ],
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        phone: [{ required: true, trigger: 'blur', validator: validPhone }],
        'dept.id': [{ required: true, message: '请选择部门', trigger: 'blur' }],
        jobIds: [
          {
            type: 'array',
            required: true,
            message: '请选择岗位',
            trigger: 'blur'
          }
        ],
        roleIds: [
          {
            type: 'array',
            required: true,
            message: '请选择角色',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['user'])
  },
  created() {
    this.crud.msg.add = '新增成功'
  },
  mounted: function() {
    const that = this
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 180 + 'px;'
    }
  },
  methods: {
    checkPermission,
    // 新增与编辑前做的操作
    [Crud.HOOK.afterToCU](crud, form) {
      this.getRoles()
      if (form.id == null) {
        this.getDepts()
      } else {
        this.getSupDepts(form.dept.id)
      }
      this.getRoleLevel()
      this.getJobs()
      form.enabled = form.enabled.toString()
    },
    // 新增前将多选的值设置为空
    [Crud.HOOK.beforeToAdd]() {},
    // 初始化编辑时候的角色与岗位
    [Crud.HOOK.beforeToEdit](crud, form) {
      form.jobIds = []
      form.jobs.forEach((data) => {
        form.jobIds.push(data.id)
      })
      form.roleIds = []
      form.roles.forEach((data) => {
        form.roleIds.push(data.id)
      })
      this.getJobs(this.form.dept.id)
    },
    // 提交前做的操作
    [Crud.HOOK.afterValidateCU](crud) {
      if (!crud.form.dept.id) {
        this.$message({
          message: '部门不能为空',
          type: 'warning'
        })
        return false
      }
      return true
    },
    // 获取左侧部门数据
    getDeptDatas(node, resolve) {
      const sort = 'id desc'
      const params = { sort: sort }
      if (typeof node !== 'object') {
        if (node) {
          params['name'] = node
        }
      } else if (node.level !== 0) {
        params['pid'] = node.data.id
      }
      setTimeout(() => {
        getDepts(params).then((res) => {
          if (resolve) {
            resolve(res.records)
          } else {
            this.deptDatas = res.records
          }
        })
      }, 100)
    },
    getDepts() {
      getDepts({ enabled: true }).then((res) => {
        this.depts = res.records.map(function(obj) {
          if (obj.hasChildren) {
            obj.children = null
          }
          return obj
        })
      })
    },
    getSupDepts(deptId) {
      getDeptSuperior(Array.of(deptId)).then((res) => {
        const date = res.records
        this.buildDepts(date)
        this.depts = date
      })
    },
    buildDepts(depts) {
      depts.forEach((data) => {
        if (data.children) {
          this.buildDepts(data.children)
        }
        if (data.hasChildren && !data.children) {
          data.children = null
        }
      })
    },
    // 获取弹窗内部门数据
    loadDepts({ action, parentNode, callback }) {
      if (action === LOAD_CHILDREN_OPTIONS) {
        getDepts({ enabled: true, pid: parentNode.id }).then((res) => {
          parentNode.children = res.records.map(function(obj) {
            if (obj.hasChildren) {
              obj.children = null
            }
            return obj
          })
          setTimeout(() => {
            callback()
          }, 200)
        })
      }
    },
    // 切换部门
    handleNodeClick(data) {
      if (data.pid === 0) {
        this.query.deptId = null
      } else {
        this.query.deptId = data.id
      }
      this.crud.toQuery()
    },
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm(
        '此操作将 "' +
          this.dict.label.user_status[val] +
          '" ' +
          data.username +
          ', 是否继续？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(() => {
          crudUser
            .edit(data)
            .then((res) => {
              this.crud.notify(
                this.dict.label.user_status[val] + '成功',
                Crud.NOTIFICATION_TYPE.SUCCESS
              )
            })
            .catch(() => {
              data.enabled = !data.enabled
            })
        })
        .catch(() => {
          data.enabled = !data.enabled
        })
    },
    // 获取弹窗内角色数据
    getRoles() {
      getAll()
        .then((res) => {
          this.roles = res
        })
        .catch(() => {})
    },
    // 获取弹窗内岗位数据
    getJobs() {
      getAllJob()
        .then((res) => {
          this.jobs = res.records
        })
        .catch(() => {})
    },
    // 获取权限级别
    getRoleLevel() {
      getLevel()
        .then((res) => {
          this.level = res.level
        })
        .catch(() => {})
    },
    checkboxT(row, rowIndex) {
      return row.id !== this.user.id
    },
    handleResetPwd(row) {
      this.$prompt('请输入用户"' + row.username + '"的新密码', '提示', {
        inputValidator: function(val) {
          return val !== null && val !== ''
        },
        inputErrorMessage: '请输入密码',
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            const value = instance.getInputElement().value
            resetPass(row.username, value).then((response) => {
              done()
              Notification.success({
                title: '修改成功，新密码是：' + value,
                duration: 2500
              })
            })
          } else {
            done()
          }
        }
      }).catch(() => {})
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .vue-treeselect__control,
::v-deep .vue-treeselect__placeholder,
::v-deep .vue-treeselect__single-value {
  height: 30px;
  line-height: 30px;
}
</style>
