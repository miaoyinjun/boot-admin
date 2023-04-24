<template>

  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.name"
          clearable
          placeholder="表单名"
          style="width: 185px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
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
        <el-table-column prop="name" label="表单名" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="gmtCreate" label="创建时间" />
        <el-table-column
          v-permission="['admin', 'student:edit', 'student:del']"
          label="操作"
          width="300px"
          align="center"
        >
          <template v-slot="scope">
            <el-button
              v-permission="['student:list']"
              size="mini"
              type="success"
              icon="el-icon-view"
              style="display: inline-block"
              @click="handlePreView(scope.row)"
            />
            <div style="display: inline-block">
              <udOperation :data="scope.row" :permission="permission" />
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
    <el-dialog title="表单详情" :visible.sync="formVisible" width="50%">
      <formRender :form-json="formJson" :form-data="formData" :option-data="optionData">
      </formRender>
    </el-dialog>
  </div>
</template>

<script>
import crudBpmForm from '@/api/bpm/form'
import CRUD, { crud, form, header, presenter } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import formRender from '@/components/FormRender'

export default {
  name: 'Form',
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    formRender
  },
  mixins: [presenter(), header(), form({}), crud()],
  cruds() {
    return CRUD({
      title: '表单',
      url: 'sys/bpm/form',
      idField: 'id',
      crudMethod: { ...crudBpmForm },
      optShow: {
        add: true,
        del: true
      }
    })
  },
  data() {
    return {
      formVisible: false,
      permission: {
        add: ['admin', 'student:add'],
        edit: ['admin', 'student:edit'],
        del: ['admin', 'student:del']
      },
      formJson: {"widgetList":[],"formConfig":{"labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":"","functions":"","layoutType":"PC","onFormCreated":"","onFormMounted":"","onFormDataChange":""}},
      formData: {},
      optionData: {}
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    [CRUD.HOOK.afterToAdd](crud, form) {
      this.$router.push({
        path:"/bpm/manager/form/edit"
      })
    },
    [CRUD.HOOK.afterToEdit](crud, form) {
      this.$router.push({
        path:"/bpm/manager/form/edit",
        query: {
          formId: form.id
        }
      })
    },
    handlePreView(row) {
      crudBpmForm.get(row.id).then(data => {
        // 设置值
        this.formVisible = true
        this.formJson = JSON.parse(data.content)
      })
    }
  }
}
</script>

<style scoped></style>
