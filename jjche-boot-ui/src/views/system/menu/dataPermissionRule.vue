<template>
  <div>
    <div>
      <!--工具栏-->
      <div class="head-container">
        <!-- 搜索 -->
        <el-input
          v-model="query.name"
          clearable
          size="small"
          placeholder="名称"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="toQuery"
        />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog
        append-to-body
        :close-on-click-modal="false"
        :before-close="crud.cancelCU"
        :visible="crud.status.cu > 0"
        :title="crud.status.title"
        width="500px"
      >
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          size="small"
          label-width="80px"
        >
          <el-form-item label="规则名称" prop="name">
            <el-input
              v-model="form.name"
              placeholder="请输入规则名称"
              style="width: 370px"
            />
          </el-form-item>
          <el-form-item label="规则字段" prop="column">
            <el-input
              v-model="form.column"
              placeholder="请输入规则字段"
              style="width: 370px"
            />
          </el-form-item>
          <el-form-item label="规则条件" prop="condition">
            <el-select v-model="form.condition" filterable placeholder="请选择">
              <el-option
                v-for="item in dict.dataPermissionRule_condition"
                :key="item.id"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="规则值" prop="value">
            <el-input
              v-model="form.value"
              placeholder="请输入规则值"
              style="width: 370px"
            />
          </el-form-item>
          <el-form-item label="状态" prop="isActivated">
            <el-switch
              v-model="form.isActivated"
              active-color="#13ce66"
              inactive-color="#ff4949"
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
        stripe
        :data="crud.data"
        highlight-current-row
        style="width: 100%"
        @selection-change="crud.selectionChangeHandler"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="名称" show-overflow-tooltip />
        <el-table-column prop="column" label="字段" />
        <el-table-column prop="condition" label="条件">
          <template v-slot="scope">
            {{ dict.label.dataPermissionRule_condition[scope.row.condition] }}
          </template>
        </el-table-column>
        <el-table-column prop="value" label="值" />
        <el-table-column prop="isActivated" label="状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.isActivated" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          v-permission="['admin', 'menu:add', 'menu:edit']"
          label="操作"
          width="130px"
          align="center"
          fixed="right"
        >
          <template v-slot="scope">
            <udOperation :data="scope.row" :permission="permission" />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudDataPermissionRule from '@/api/system/dataPermissionRule'
import Crud, { presenter, header, form } from '@crud/crud'
import pagination from '@crud/Pagination'
import rrOperation from '@crud/RR.operation'
import udOperation from '@crud/UD.operation'
import crudOperation from '@crud/CRUD.operation'

const defaultForm = {
  menuId: null,
  id: null,
  name: null,
  condition: 'EQUAL',
  column: null,
  value: null,
  isActivated: true
}

export default {
  components: { pagination, rrOperation, udOperation, crudOperation },

  mixins: [presenter(), header(), form(defaultForm)],
  data() {
    return {
      rules: {
        name: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        condition: [
          { required: true, message: '请输入规则条件', trigger: 'blur' }
        ],
        value: [{ required: true, message: '请输入规则值', trigger: 'blur' }]
      },
      permission: {
        add: ['admin', 'menu:add'],
        edit: ['admin', 'menu:edit'],
        del: ['admin', 'menu:del']
      }
    }
  },
  methods: {
    // 新增前合并dictId
    [Crud.HOOK.beforeToAdd]() {
      this.crud.form.menuId = this.query.menuId
    }
  },
  dicts: ['dataPermissionRule_condition'],
  cruds() {
    return [
      Crud({
        title: '数据规则详情',
        url: 'sys/data_permission_rules',
        query: { menuId: '' },
        crudMethod: { ...crudDataPermissionRule },
        optShow: {
          add: true,
          edit: true,
          del: true,
          reset: false
        },
        queryOnPresenterCreated: false
      })
    ]
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
