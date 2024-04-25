<template>

  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.name"
          clearable
          placeholder="姓名"
          style="width: 185px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-select
          v-model="query.course"
          filterable
          clearable
          placeholder="课程类型"
          class="filter-item"
        >
          <el-option
            v-for="item in dict.course_status"
            :key="item.id"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <date-range-picker
          v-model="query.gmtCreate"
          start-placeholder="创建日期开始"
          end-placeholder="创建日期结束"
          class="date-item"
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
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" style="width: 370px" />
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input-number v-model="form.age" :min="1" :max="200" />
          </el-form-item>
          <el-form-item label="课程类型">
            <el-select v-model="form.course" filterable placeholder="请选择">
              <el-option
                v-for="item in dict.course_status"
                :key="item.id"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
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
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="age" label="年龄" />
        <el-table-column prop="course" label="课程类型">
                  <template v-slot="scope">
                    {{ dict.label.course_status[scope.row.course] }}
                  </template>
                </el-table-column>
        <el-table-column prop="gmtCreate" label="创建时间">
          <template v-slot="scope">
            <span>{{ scope.row.gmtCreate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建者" />
        <el-table-column
          v-permission="['admin', 'student:edit', 'student:del']"
          label="操作"
          width="300px"
          align="center"
        >
          <template v-slot="scope">
            <div style="display: inline-block">
              <udOperation :data="scope.row" :permission="permission" />
            </div>
            <el-tooltip content="日志" effect="dark" placement="top">
              <el-button
                v-permission="['student:list']"
                size="mini"
                type="warning"
                icon="el-icon-notebook-2"
                style="display: inline-block"
                @click="handleLog(scope.row)"
              />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
    <el-dialog title="操作日志" :visible.sync="logVisible" width="50%">
      <log ref="log" />
    </el-dialog>
  </div>
</template>

<script>
import crudStudent from '@/api/demo/student'
import CRUD, { crud, form, header, presenter } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import DateRangePicker from '@/components/DateRangePicker'
import log from '@/components/Log'

const defaultForm = {
  id: null,
  name: null,
  age: null,
  deptId: null,
  course: '102'
}
export default {
  name: 'Student',
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker,
    log
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['course_status'],
  cruds() {
    return CRUD({
      title: '用户',
      url: 'demo/students',
      idField: 'id',
      crudMethod: { ...crudStudent }
    })
  },
  data() {
    return {
      logVisible: false,
      permission: {
        add: ['admin', 'student:add'],
        edit: ['admin', 'student:edit'],
        del: ['admin', 'student:del']
      },
      rules: {
        name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
        age: [{ required: true, message: '年龄不能为空', trigger: 'blur' }]
      },
      queryTypeOptions: [
        { key: 'name', display_name: '姓名' },
        { key: 'course', display_name: '课程类型' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    // 编辑界面获取单条数据详情之后
    [CRUD.HOOK.afterToEdit](crud, form) {
       //form.course = form.course.value
    },
    handleLog(data) {
      this.logVisible = true
      this.$nextTick(() => {
        if (this.$refs.log) {
          this.$refs.log.query.bizKey = 'students'
          this.$refs.log.query.bizNo = data.id
          this.$refs.log.crud.toQuery()
        }
      })
    }
  }
}
</script>

<style scoped></style>
