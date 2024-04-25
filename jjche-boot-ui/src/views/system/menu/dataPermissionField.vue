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
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" style="width: 370px" />
          </el-form-item>
          <el-form-item label="字段" prop="code">
            <el-input v-model="form.code" style="width: 370px" />
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number
              v-model.number="form.sort"
              :min="0"
              :max="999"
              controls-position="right"
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
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="code" label="字段" />
        <el-table-column prop="sort" label="排序" />
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
import crudPermissionData from '@/api/system/dataPermissionField'
import Crud, { presenter, header, form } from '@crud/crud'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'

const defaultForm = {
  id: null,
  menuId: null,
  name: null,
  code: null,
  sort: 999,
  isActivated: true
}

export default {
  components: { pagination, udOperation, rrOperation, crudOperation },
  mixins: [presenter(), header(), form(defaultForm)],
  data() {
    return {
      menuName: null,
      rules: {
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        code: [{ required: true, message: '请输入字段', trigger: 'blur' }],
        sort: [
          {
            required: true,
            message: '请输入排序',
            trigger: 'blur',
            type: 'number'
          }
        ]
      },
      permission: {
        add: ['admin', 'menu:add'],
        edit: ['admin', 'menu:edit'],
        del: ['admin', 'menu:del']
      }
    }
  },
  methods: {
    // 新增前合并menuId
    [Crud.HOOK.beforeToAdd]() {
      this.crud.form.menuId = this.query.menuId
    }
  },
  cruds() {
    return [
      Crud({
        title: '列数据详情',
        url: 'sys/data_permission_fields',
        query: { menuId: '' },
        sort: ['id desc'],
        crudMethod: { ...crudPermissionData },
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
