<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.blurry"
          size="small"
          clearable
          placeholder="输入名称或者描述搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <date-range-picker v-model="query.gmtCreate" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!-- 表单渲染 -->
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible.sync="crud.status.cu > 0"
      :title="crud.status.title"
      width="520px"
    >
      <el-form
        ref="form"
        :inline="true"
        :model="form"
        :rules="rules"
        size="small"
        label-width="80px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" style="width: 380px" />
        </el-form-item>
        <el-form-item label="角色标识" prop="code">
          <el-input
            v-model="form.code"
            style="width: 380px"
            :disabled="form.id > 0"
          />
        </el-form-item>
        <el-form-item label="角色级别" prop="level">
          <el-input-number
            v-model.number="form.level"
            :min="1"
            controls-position="right"
            style="width: 145px"
          />
        </el-form-item>
        <el-form-item label="数据范围" prop="dataScope">
          <el-select
            v-model="form.dataScope"
            style="width: 140px"
            placeholder="请选择数据范围"
            @change="changeScope"
          >
            <el-option
              v-for="item in dateScopes"
              :key="item.k"
              :label="item.v"
              :value="item.k"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="form.dataScope === 'DATA_SCOPE_CUSTOM'"
          label="数据权限"
          prop="depts"
        >
          <treeselect
            v-model="deptDatas"
            :load-options="loadDepts"
            :options="depts"
            multiple
            style="width: 380px"
            placeholder="请选择"
          />
        </el-form-item>
        <el-form-item label="描述信息" prop="description">
          <el-input
            v-model="form.description"
            style="width: 380px"
            rows="5"
            type="textarea"
          />
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
    <el-row :gutter="15">
      <!--角色管理-->
      <el-col
        :xs="24"
        :sm="24"
        :md="16"
        :lg="16"
        :xl="17"
        style="margin-bottom: 10px"
      >
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">角色列表</span>
          </div>
          <el-table
            ref="table"
            v-loading="crud.loading"
            stripe
            highlight-current-row
            style="width: 100%"
            :data="crud.data"
            @selection-change="crud.selectionChangeHandler"
            @current-change="handleCurrentChange"
          >
            <el-table-column
              :selectable="checkboxT"
              type="selection"
              width="55"
            />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="code" label="角色标识" width="100px" />
            <el-table-column prop="dataScopeValue" label="数据范围" />
            <el-table-column prop="level" label="角色级别" />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="description"
              label="描述"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              width="135px"
              prop="gmtCreate"
              label="创建日期"
            >
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.gmtCreate) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              v-permission="['admin', 'roles:edit', 'roles:del']"
              label="操作"
              width="300px"
              align="center"
              fixed="right"
            >
              <template slot-scope="scope">
                <div style="display: inline-block">
                  <udOperation
                    v-if="scope.row.level >= level"
                    :data="scope.row"
                    :permission="permission"
                  />
                </div>
                <el-button
                  v-permission="['roles:edit', 'roles:del']"
                  size="mini"
                  type="warning"
                  icon="el-icon-user"
                  style="display: inline-block"
                  @click="handleUsers(scope.row)"
                />
              </template>
            </el-table-column>
          </el-table>
          <!--分页组件-->
          <pagination />
        </el-card>
      </el-col>
      <!-- 菜单授权 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="7">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <el-tooltip
              class="item"
              effect="dark"
              content="选择指定角色分配菜单"
              placement="top"
            >
              <span class="role-span">菜单分配</span>
            </el-tooltip>
            <el-button
              v-permission="['admin', 'roles:edit']"
              :disabled="!showButton"
              :loading="menuLoading"
              icon="el-icon-check"
              size="mini"
              style="float: right; padding: 6px 9px"
              type="primary"
              @click="saveMenu"
            >保存
            </el-button>
          </div>
          <el-tree
            ref="menu"
            lazy
            :data="menus"
            :default-checked-keys="menuIds"
            :load="getMenuDatas"
            :props="defaultProps"
            check-strictly
            accordion
            show-checkbox
            :expand-on-click-node="false"
            node-key="id"
            @check="menuChange"
            @node-click="handleNodeClick"
          >
            <span
              slot-scope="{ node, data }"
              class="data-permission-filed-tree-node"
            >
              <span>{{ node.label }}</span>
              <span
                v-if="data.isDataPermission"
                class="el-icon-s-fold"
                style="color: red"
              />
            </span>
          </el-tree>
        </el-card>
      </el-col>
    </el-row>

    <el-drawer
      title="数据权限"
      :visible.sync="dataPermissionVisible"
      size="20%"
      @open="handleDataPermissionOpen()"
    >
      <div style="margin-left: 20px">
        <el-tabs
          v-model="DataPermissionActiveName"
          @tab-click="handleTabDataPermissionClick"
        >
          <el-tab-pane label="数据规则" name="dataPermissionRule">
            <dataPermissionRule
              ref="dataPermissionRule"
              :permission="permission"
            />
          </el-tab-pane>

          <el-tab-pane label="数据字段" name="dataPermissionField">
            <dataPermissionField
              ref="dataPermissionField"
              :permission="permission"
            />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <el-drawer
      :title="userTitle"
      :visible.sync="userVisible"
      size="40%"
      @open="handleUserOpen()"
    >
      <div style="margin-left: 20px">
        <User
          ref="User"
          :permission="permission"
        />
      </div>
    </el-drawer>
  </div>
</template>

<script>
import crudRoles from '@/api/system/role'
import { getDepts, getDeptSuperior } from '@/api/system/dept'
import { getMenusTree, getChild } from '@/api/system/menu'
import Crud, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { LOAD_CHILDREN_OPTIONS } from '@riophae/vue-treeselect'
import DateRangePicker from '@/components/DateRangePicker'
import dataPermissionRule from './dataPermissionRule'
import dataPermissionField from './dataPermissionField'
import User from './user'

const defaultForm = {
  id: null,
  name: null,
  code: null,
  depts: [],
  description: null,
  dataScope: 'DATA_SCOPE_ALL',
  level: 3
}
export default {
  name: 'Role',
  components: {
    Treeselect,
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
    dataPermissionRule,
    dataPermissionField,
    User
  },
  cruds() {
    return Crud({
      title: '角色',
      url: 'sys/roles',
      crudMethod: { ...crudRoles }
    })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      userTitle: '',
      userVisible: false,
      DataPermissionActiveName: 'dataPermissionRule',
      nodeMenuId: null,
      dataPermissionVisible: false,
      defaultProps: { children: 'children', label: 'label', isLeaf: 'leaf' },
      dateScopes: [
        { k: 'DATA_SCOPE_ALL', v: '全部' },
        {
          k: 'DATA_SCOPE_DEPT_AND_CHILD',
          v: '所在机构及以下'
        },
        { k: 'DATA_SCOPE_DEPT', v: '所在机构' },
        { k: 'DATA_SCOPE_SELF', v: '本人' },
        { k: 'DATA_SCOPE_CUSTOM', v: '自定义' }
      ],
      level: 3,
      currentId: 0,
      menuLoading: false,
      showButton: false,
      menus: [],
      menuIds: [],
      depts: [],
      deptDatas: [], // 多选时使用
      permission: {
        add: ['admin', 'roles:add'],
        edit: ['admin', 'roles:edit'],
        del: ['admin', 'roles:del']
      },
      rules: {
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        code: [{ required: true, message: '请输入标识', trigger: 'blur' }],
        permission: [{ required: true, message: '请输入权限', trigger: 'blur' }]
      }
    }
  },
  created() {
    crudRoles.getLevel().then((data) => {
      this.level = data.level
    })
  },
  methods: {
    getMenuDatas(node, resolve) {
      setTimeout(() => {
        getMenusTree(node.data.id ? node.data.id : 0, this.currentId).then(
          (res) => {
            resolve(res)
          }
        )
      }, 100)
    },
    [Crud.HOOK.afterRefresh]() {
      this.$refs.menu.setCheckedKeys([])
    },
    // 新增前初始化部门信息
    [Crud.HOOK.beforeToAdd]() {
      this.deptDatas = []
    },
    // 编辑前初始化自定义数据权限的部门信息
    [Crud.HOOK.beforeToEdit](crud, form) {
      this.deptDatas = []
      if (form.dataScope === 'DATA_SCOPE_CUSTOM') {
        this.getSupDepts(form.depts)
      }
      const _this = this
      form.depts.forEach(function(dept) {
        _this.deptDatas.push(dept.id)
      })
    },
    // 提交前做的操作
    [Crud.HOOK.afterValidateCU](crud) {
      if (
        crud.form.dataScope === 'DATA_SCOPE_CUSTOM' &&
        this.deptDatas.length === 0
      ) {
        this.$message({
          message: '自定义数据权限不能为空',
          type: 'warning'
        })
        return false
      } else if (crud.form.dataScope === 'DATA_SCOPE_CUSTOM') {
        const depts = []
        this.deptDatas.forEach(function(data) {
          const dept = { id: data }
          depts.push(dept)
        })
        crud.form.depts = depts
      } else {
        crud.form.depts = []
      }
      return true
    },
    // 触发单选
    handleCurrentChange(val) {
      if (val) {
        const _this = this
        // 清空菜单的选中
        this.$refs.menu.setCheckedKeys([])
        // 保存当前的角色id
        this.currentId = val.id
        // 初始化默认选中的key
        this.menuIds = []
        val.menus.forEach(function(data) {
          _this.menuIds.push(data.id)
        })
        this.showButton = true
      }
    },
    menuChange(menu) {
      // 获取该节点的所有子节点，id 包含自身
      getChild(menu.id).then((childIds) => {
        // 判断是否在 menuIds 中，如果存在则删除，否则添加
        if (this.menuIds.indexOf(menu.id) !== -1) {
          for (let i = 0; i < childIds.length; i++) {
            const index = this.menuIds.indexOf(childIds[i])
            if (index !== -1) {
              this.menuIds.splice(index, 1)
            }
          }
        } else {
          for (let i = 0; i < childIds.length; i++) {
            const index = this.menuIds.indexOf(childIds[i])
            if (index === -1) {
              this.menuIds.push(childIds[i])
            }
          }
        }
        this.$refs.menu.setCheckedKeys(this.menuIds)
      })
    },
    // 保存菜单
    saveMenu() {
      this.menuLoading = true
      const role = { id: this.currentId, menus: [] }
      // 得到已选中的 key 值
      this.menuIds.forEach(function(id) {
        const menu = { id: id }
        role.menus.push(menu)
      })
      crudRoles
        .editMenu(role)
        .then(() => {
          this.crud.notify('保存成功', Crud.NOTIFICATION_TYPE.SUCCESS)
          this.menuLoading = false
          this.update()
        })
        .catch((err) => {
          this.menuLoading = false
          console.log(err.response.data.message)
        })
    },
    // 改变数据
    update() {
      // 无刷新更新 表格数据
      crudRoles.get(this.currentId).then((res) => {
        for (let i = 0; i < this.crud.data.length; i++) {
          if (res.id === this.crud.data[i].id) {
            this.crud.data[i] = res
            break
          }
        }
      })
    },
    // 获取部门数据
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
    getSupDepts(depts) {
      const ids = []
      depts.forEach((dept) => {
        ids.push(dept.id)
      })
      getDeptSuperior(ids).then((res) => {
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
    // 如果数据权限为自定义则获取部门数据
    changeScope() {
      if (this.form.dataScope === 'DATA_SCOPE_CUSTOM') {
        this.getDepts()
      }
    },
    checkboxT(row) {
      return row.level >= this.level
    },
    handleNodeClick(data) {
      if (this.currentId) {
        this.dataPermissionVisible = true
        this.DataPermissionActiveName = 'dataPermissionRule'
        this.nodeMenuId = data.id
      }
    },
    getDataPermissionRuleData() {
      this.$refs.dataPermissionRule.query.menuId = this.nodeMenuId
      this.$refs.dataPermissionRule.query.roleId = this.currentId
      this.$refs.dataPermissionRule.crud.toQuery()
    },
    handleDataPermissionOpen() {
      this.$nextTick(() => {
        if (this.$refs.dataPermissionRule) {
          this.getDataPermissionRuleData()
        }
      })
    },
    handleTabDataPermissionClick(tab, event) {
      if (tab.name === 'dataPermissionField') {
        this.$refs.dataPermissionField.query.menuId = this.nodeMenuId
        this.$refs.dataPermissionField.query.roleId = this.currentId
        this.$refs.dataPermissionField.crud.toQuery()
      } else {
        this.getDataPermissionRuleData()
      }
    },
    handleUsers(data) {
      this.userTitle = data.name
      this.userVisible = true
      this.currentId = data.id
    },
    handleUserOpen() {
      this.$nextTick(() => {
        if (this.$refs.User) {
          this.$refs.User.query.roleId = this.currentId
          this.$refs.User.crud.toQuery()
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.role-span {
  font-weight: bold;
  color: #303133;
  font-size: 15px;
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}

::v-deep .vue-treeselect__multi-value {
  margin-bottom: 0;
}

::v-deep .vue-treeselect__multi-value-item {
  border: 0;
  padding: 0;
}

.data-permission-filed-tree-node {
  flex: 1;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
