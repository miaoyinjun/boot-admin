<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.name"
          clearable
          placeholder="版本号名称"
          style="width: 185px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
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
        width="500px"
      >
        <el-form
          ref="form"
          v-loading="crud.editLoading"
          :model="form"
          :rules="rules"
          size="small"
          label-width="80px"
        >
          <el-form-item label="版本号" prop="name">
            <el-input v-model="form.name" style="width: 370px" />
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              :rows="3"
              style="width: 370px"
            />
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
        :row-class-name="tableRowClassName"
        @selection-change="crud.selectionChangeHandler"
      >
        <el-table-column label="序号" type="index" width="50" />
        <el-table-column prop="name" label="版本号" />
        <el-table-column
          prop="remark"
          :show-overflow-tooltip="true"
          label="备注"
        />
        <el-table-column prop="gmtCreate" label="创建时间">
          <template v-slot="scope">
            <span>{{ scope.row.gmtCreate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建者" />
        <el-table-column
          v-permission="['admin', 'version:edit', 'version:del']"
          label="操作"
          align="center"
        >
          <template v-slot="scope">
            <el-popconfirm
              title="确认激活该版本吗？"
              @confirm="handleIsActivated(scope.row.id)"
            >
              <el-button
                slot="reference"
                :disabled="
                  scope.row.isActivated ||
                    !checkPermission(['admin', 'version:edit', 'version:del'])
                "
                size="mini"
                type="success"
                icon="el-icon-check"
                style="display: inline-block"
              />
            </el-popconfirm>
            <el-button
              v-permission="permission.edit"
              :disabled="
                scope.row.isActivated ||
                  !checkPermission(['admin', 'version:edit', 'version:del'])
              "
              size="mini"
              type="primary"
              icon="el-icon-edit"
              @click="crud.toEdit(scope.row)"
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
import crudVersion, { activated } from '@/api/system/version'
import CRUD, { crud, form, header, presenter } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import checkPermission from '@/utils/permission'

const defaultForm = { id: null, name: null, remark: null }
export default {
  name: 'Version',
  components: {
    pagination,
    crudOperation,
    rrOperation
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({
      title: '版本',
      url: 'sys/versions',
      idField: 'id',
      crudMethod: { ...crudVersion },
      optShow: { add: true }
    })
  },
  data() {
    return {
      permission: {
        add: ['admin', 'version:add'],
        edit: ['admin', 'version:edit'],
        del: ['admin', 'version:del']
      },
      rules: {
        name: [{ required: true, message: '版本号不能为空', trigger: 'blur' }]
      },
      queryTypeOptions: [{ key: 'name', display_name: '版本号' }]
    }
  },
  methods: {
    checkPermission,
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    tableRowClassName({ row, rowIndex }) {
      return row.isActivated ? 'success-row' : ''
    },
    handleIsActivated(id) {
      activated(id).then((response) => {
        this.crud.toQuery()
      })
    }
  }
}
</script>

<style>
.el-table .success-row {
  background: oldlace;
}
</style>
