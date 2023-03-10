<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.blurry"
          clearable
          size="small"
          placeholder="模糊搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <date-range-picker v-model="query.gmtCreate" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible.sync="crud.status.cu > 0"
      :title="crud.status.title"
      width="580px"
    >
      <el-form
        ref="form"
        :inline="true"
        :model="form"
        :rules="rules"
        size="small"
        label-width="80px"
      >
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="form.type" size="mini" style="width: 178px">
            <el-radio-button label="0">目录</el-radio-button>
            <el-radio-button label="1">菜单</el-radio-button>
            <el-radio-button label="2">按钮</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上级类目" prop="pid">
          <treeselect
            v-model="form.pid"
            :options="menus"
            :load-options="loadMenus"
            style="width: 450px"
            placeholder="选择上级类目"
          />
        </el-form-item>
        <el-form-item
          v-show="form.type.toString() !== '2'"
          label="菜单图标"
          prop="icon"
        >
          <el-popover
            placement="bottom-start"
            width="450"
            trigger="click"
            @show="$refs['iconSelect'].reset()"
          >
            <IconSelect ref="iconSelect" @selected="selected" />
            <el-input
              slot="reference"
              v-model="form.icon"
              style="width: 450px"
              placeholder="点击选择图标"
              readonly
            >
              <svg-icon
                v-if="form.icon"
                slot="prefix"
                :icon-class="form.icon"
                class="el-input__icon"
                style="height: 32px; width: 16px"
              />
              <i v-else slot="prefix" class="el-icon-search el-input__icon" />
            </el-input>
          </el-popover>
        </el-form-item>
        <el-form-item
          v-show="form.type.toString() !== '2'"
          label="外链菜单"
          prop="iframe"
        >
          <el-radio-group v-model="form.iframe" size="mini">
            <el-radio-button label="true">是</el-radio-button>
            <el-radio-button label="false">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          v-show="form.type.toString() === '1'"
          label="菜单缓存"
          prop="cache"
        >
          <el-radio-group v-model="form.cache" size="mini">
            <el-radio-button label="true">是</el-radio-button>
            <el-radio-button label="false">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          v-show="form.type.toString() !== '2'"
          label="菜单可见"
          prop="hidden"
        >
          <el-radio-group v-model="form.hidden" size="mini">
            <el-radio-button label="false">是</el-radio-button>
            <el-radio-button label="true">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          v-if="form.type.toString() !== '2'"
          label="菜单标题"
          prop="title"
        >
          <el-input
            v-model="form.title"
            :style="
              form.type.toString() === '0' ? 'width: 450px' : 'width: 178px'
            "
            placeholder="菜单标题"
          />
        </el-form-item>
        <el-form-item
          v-if="form.type.toString() === '2'"
          label="按钮名称"
          prop="title"
        >
          <el-input
            v-model="form.title"
            placeholder="按钮名称"
            style="width: 178px"
          />
        </el-form-item>
        <el-form-item
          v-show="form.type.toString() !== '0'"
          label="权限标识"
          prop="permission"
        >
          <el-input
            v-model="form.permission"
            :disabled="form.iframe.toString() === 'true'"
            placeholder="权限标识"
            style="width: 178px"
          />
        </el-form-item>
        <el-form-item
          v-if="form.type.toString() !== '2'"
          label="路由地址"
          prop="path"
        >
          <el-input
            v-model="form.path"
            placeholder="路由地址"
            style="width: 178px"
          />
        </el-form-item>
        <el-form-item label="菜单排序" prop="menuSort">
          <el-input-number
            v-model.number="form.menuSort"
            :min="0"
            :max="999"
            controls-position="right"
            style="width: 178px"
          />
        </el-form-item>
        <el-form-item
          v-show="form.iframe.toString() !== 'true' && form.type.toString() === '1'"
          label="组件名称"
          prop="componentName"
        >
          <el-input
            v-model="form.componentName"
            style="width: 178px"
            placeholder="匹配组件内Name字段"
          />
        </el-form-item>
        <el-form-item
          v-show="form.iframe.toString() !== 'true' && form.type.toString() === '1'"
          label="组件路径"
          prop="component"
        >
          <el-input
            v-model="form.component"
            style="width: 178px"
            placeholder="组件路径"
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
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      highlight-current-row
      stripe
      lazy
      :load="getMenus"
      :data="crud.data"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      row-key="id"
      @select="crud.selectChange"
      @select-all="crud.selectAllChange"
      @selection-change="crud.selectionChangeHandler"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        :show-overflow-tooltip="true"
        label="菜单标题"
        width="125px"
        prop="title"
      />
      <el-table-column prop="icon" label="图标" align="center" width="60px">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon ? scope.row.icon : ''" />
        </template>
      </el-table-column>
      <el-table-column prop="menuSort" align="center" label="排序">
        <template slot-scope="scope">
          {{ scope.row.menuSort }}
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="permission"
        label="权限标识"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="component"
        label="组件路径"
      />
      <el-table-column prop="iframe" label="外链" width="75px">
        <template slot-scope="scope">
          <span v-if="scope.row.iframe">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="cache" label="缓存" width="75px">
        <template slot-scope="scope">
          <span v-if="scope.row.cache">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="hidden" label="可见" width="75px">
        <template slot-scope="scope">
          <span v-if="scope.row.hidden">否</span>
          <span v-else>是</span>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建日期" width="135px">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.gmtCreate) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-permission="['admin', 'menu:edit', 'menu:del']"
        label="操作"
        width="300px"
        align="center"
        fixed="right"
      >
        <template slot-scope="scope">
          <div style="display: inline-block">
            <udOperation
              :data="scope.row"
              :permission="permission"
              msg="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！"
            />
          </div>
          <el-tooltip content="数据权限" effect="dark" placement="top">
            <el-button
              v-permission="['menu:edit', 'menu:del']"
              size="mini"
              type="warning"
              icon="el-icon-s-grid"
              style="display: inline-block"
              @click="handleDataPermission(scope.row)"
            />
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <el-drawer
      title="数据权限"
      :visible.sync="dataPermissionVisible"
      size="40%"
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
  </div>
</template>

<script>
import crudMenu from '@/api/system/menu'
import IconSelect from '@/components/IconSelect'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { LOAD_CHILDREN_OPTIONS } from '@riophae/vue-treeselect'
import Crud, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import DateRangePicker from '@/components/DateRangePicker'
import dataPermissionRule from './dataPermissionRule'
import dataPermissionField from './dataPermissionField'

// crud交由presenter持有
const defaultForm = {
  id: null,
  title: null,
  menuSort: 999,
  path: null,
  component: null,
  componentName: null,
  iframe: false,
  roles: [],
  pid: 0,
  icon: null,
  cache: false,
  hidden: false,
  type: 0,
  permission: null
}
export default {
  name: 'Menu',
  components: {
    Treeselect,
    IconSelect,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
    dataPermissionRule,
    dataPermissionField
  },
  cruds() {
    return Crud({
      title: '菜单',
      url: 'sys/menus',
      crudMethod: { ...crudMenu }
    })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      DataPermissionActiveName: 'dataPermissionRule',
      dataPermissionVisible: false,
      menuId: null,
      menus: [],
      permission: {
        add: ['admin', 'menu:add'],
        edit: ['admin', 'menu:edit'],
        del: ['admin', 'menu:del']
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        path: [{ required: true, message: '请输入地址', trigger: 'blur' }]
      }
    }
  },
  methods: {
    // 新增与编辑前做的操作
    [Crud.HOOK.afterToCU](crud, form) {
      this.menus = []
      if (form.id != null) {
        if (form.pid === null) {
          form.pid = 0
        }
        this.getSupDepts(form.id)
      } else {
        this.menus.push({ id: 0, label: '顶级类目', children: null })
      }
    },
    getMenus(tree, treeNode, resolve) {
      const params = { pid: tree.id }
      setTimeout(() => {
        crudMenu.getMenus(params).then((res) => {
          resolve(res.records)
        })
      }, 100)
    },
    getSupDepts(id) {
      crudMenu.getMenuSuperior(id).then((res) => {
        const children = res.map(function(obj) {
          if (!obj.leaf && (!obj.children || obj.children.length == 0)) {
            obj.children = null
          }
          return obj
        })
        this.menus = [{ id: 0, label: '顶级类目', children: children }]
      })
    },
    loadMenus({ action, parentNode, callback }) {
      if (action === LOAD_CHILDREN_OPTIONS) {
        crudMenu.getMenusTree(parentNode.id, 0).then((res) => {
          parentNode.children = res.map(function(obj) {
            if (!obj.leaf) {
              obj.children = null
            } else {
              delete obj.children
            }
            return obj
          })
          setTimeout(() => {
            callback()
          }, 100)
        })
      }
    },
    // 选中图标
    selected(name) {
      this.form.icon = name
    },
    handleDataPermission(data) {
      this.dataPermissionVisible = true
      this.DataPermissionActiveName = 'dataPermissionRule'
      this.menuId = data.id
    },
    getDataPermissionRuleData() {
      this.$refs.dataPermissionRule.query.menuId = this.menuId
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
        this.$refs.dataPermissionField.query.menuId = this.menuId
        this.$refs.dataPermissionField.crud.toQuery()
      } else {
        this.getDataPermissionRuleData()
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}

::v-deep .vue-treeselect__control,
::v-deep .vue-treeselect__placeholder,
::v-deep .vue-treeselect__single-value {
  height: 30px;
  line-height: 30px;
}
.el-button + .el-button {
  margin-left: 0px;
}
</style>
