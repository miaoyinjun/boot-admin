<template>

  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.name"
          clearable
          placeholder="请输入组名"
          style="width: 185px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <date-range-picker
          v-model="query.gmtCreate"
          start-placeholder="创建日期开始"
          end-placeholder="创建日期结束"
          class="date-item"
        />
        <rrOperation :crud="crud"/>
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission"/>
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
          <el-form-item label="组名" prop="name">
            <el-input v-model="form.name" style="width: 370px"/>
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description"/>
          </el-form-item>
          <el-form-item label="成员" prop="memberUserIds">
            <el-select v-model="form.memberUserIds" multiple placeholder="请选择成员">
              <el-option v-for="user in users" :key="parseInt(user.id)" :label="user.nickName" :value="parseInt(user.id)"/>
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio v-for="item in dict.CommonStatusEnum"
                        :key="item.value" :label="item.value"
              >{{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button
            :loading="crud.status.cu == 2"
            type="primary"
            @click="crud.submitCU"
          >确认
          </el-button>
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
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="name" label="组名"/>
        <el-table-column prop="description" label="描述"/>
        <el-table-column prop="status" label="状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status == 1">开启</el-tag>
            <el-tag v-else type="info">关闭</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="gmtCreate" label="创建时间"/>
        <el-table-column
          v-permission="['bpm:user-group:update', 'bpm:user-group:delete']"
          label="操作"
          width="300px"
          align="center"
        >
          <template v-slot="scope">
            <div style="display: inline-block">
              <udOperation :data="scope.row" :permission="permission"/>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination/>
    </div>
  </div>
</template>

<script>
import crudBpmUserGroup from '@/api/bpm/userGroup'
import { listSimpleUsers } from '@/api/system/user'
import CRUD, { crud, form, header, presenter } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = {
  id: null,
  name: null,
  description: null,
  memberUserIds: [],
  status: '1'
}
export default {
  name: 'BpmUserGroup',
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['CommonStatusEnum'],
  cruds() {
    return CRUD({
      title: '用户分组',
      url: 'sys/bpm/user-group',
      idField: 'id',
      crudMethod: { ...crudBpmUserGroup },
      optShow: {
        add: true,
        del: true
      }
    })
  },
  data() {
    return {
      logVisible: false,
      permission: {
        add: ['bpm:user-group:create'],
        edit: ['bpm:user-group:update'],
        del: ['bpm:user-group:delete']
      },
      rules: {
        name: [{ required: true, message: '组名不能为空', trigger: 'blur' }],
        description: [{ required: true, message: '描述不能为空', trigger: 'blur' }],
        memberUserIds: [{ required: true, message: '成员不能为空', trigger: 'change' }],
        status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
      },
      // 用户列表
      users: []
    }
  },
  created() {
    // 获得用户列表
    listSimpleUsers().then(data => {
      this.users = data
    })
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    // 编辑界面获取单条数据详情之后
    [CRUD.HOOK.afterToEdit](crud, form) {
    }
  }
}
</script>

<style scoped></style>
