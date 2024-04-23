<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <doc-alert title="工作流" url="https://www.yuque.com/miaoyj/nsln4r/wfakr6frz33peu15"/>

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
        <el-table-column label="任务编号" align="center" prop="id" width="320" fixed />
        <el-table-column label="任务名称" align="center" prop="name" width="200" />
        <el-table-column label="所属流程" align="center" prop="processInstance.name" width="200" />
        <el-table-column label="流程发起人" align="center" prop="processInstance.startUserNickname" width="120" />
        <el-table-column label="结果" align="center" prop="result">
          <template v-slot="scope">
            <el-tag v-if="scope.row.result">{{ scope.row.resultLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审批意见" align="center" prop="reason" width="200" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="审批时间" align="center" prop="endTime" width="180">
          <template v-slot="scope">
            <span>{{ scope.row.endTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="耗时" align="center" prop="durationInMillis" width="180">
          <template v-slot="scope">
            <span>{{ getDateStar(scope.row.durationInMillis) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
          <template v-slot="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleAudit(scope.row)"
                       v-permission="['bpm:task:query']">详情</el-button>
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
import {getDate} from "@/utils/dateUtils";

export default {
  name: 'Done',
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
      title: '已办任务',
      url: '/bpm/task/done-page',
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
    getDateStar(ms) {
      return getDate(ms);
    },
    /** 处理审批按钮 */
    handleAudit(row) {
      this.$router.push({ path: '/bpm/process-instance/detail', query: { id: row.processInstance.id } })
    }
  }
}
</script>
