<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <doc-alert title="工作流" url="https://doc.iocoder.cn/bpm"/>

      <!-- 搜索工作栏 -->
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.name"
          clearable
          placeholder="流程名"
          style="width: 185px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <date-range-picker
          v-model="query.createTime"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          class="date-item"
        />
        <rrOperation :crud="crud"/>
      </div>
      <crudOperation/>

      <!-- 列表 -->
      <el-table
        ref="table"
        v-loading="crud.loading"
        highlight-current-row
        stripe
        :data="crud.data"
        size="small"
        style="width: 100%"
      >
        <el-table-column label="任务编号" align="center" prop="id" width="320"/>
        <el-table-column label="任务名称" align="center" prop="name"/>
        <el-table-column label="所属流程" align="center" prop="processInstance.name"/>
        <el-table-column label="流程发起人" align="center" prop="processInstance.startUserNickname"/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
          <el-table-column label="状态" align="center" prop="version" width="80">
            <template v-slot="scope">
              <el-tag type="success" v-if="scope.row.suspensionState === 1">激活</el-tag>
              <el-tag type="warning" v-if="scope.row.suspensionState === 2">挂起</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template v-slot="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleAudit(scope.row)"
                         v-permission="['bpm:task:update']"
              >审批
              </el-button>
            </template>
          </el-table-column>
      </el-table>
      <!-- 分页组件 -->
      <pagination/>
    </div>

  </div>
</template>

<script>
import pagination from '@crud/Pagination.vue'
import crudOperation from '@crud/CRUD.operation.vue'
import rrOperation from '@crud/RR.operation.vue'
import udOperation from '@crud/UD.operation.vue'
import DateRangePicker from '@/components/DateRangePicker/index.vue'
import CRUD, { crud, header, presenter } from '@crud/crud'
import crudModel from '@/api/bpm/model'

export default {
  name: 'Todo',
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker
  },
  mixins: [presenter(), header(), crud()],
  cruds() {
    return CRUD({
      title: '待办任务',
      url: 'sys/bpm/task/todo-page',
      idField: 'id',
      crudMethod: { ...crudModel },
      optShow: {
        add: false,
        edit: false,
        del: false,
        download: false,
        reset: true
      },
    })
  },
  methods: {
    /** 处理审批按钮 */
    handleAudit(row) {
      this.$router.push({ path: '/bpm/process-instance/detail', query: { id: row.processInstance.id } })
    }
  }
}
</script>
