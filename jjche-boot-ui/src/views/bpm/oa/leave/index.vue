<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <doc-alert title="工作流" url="https://www.yuque.com/miaoyj/nsln4r/wfakr6frz33peu15"/>
      <!-- 搜索工作栏 -->
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-select v-model="query.type" placeholder="请选择请假类型" clearable>
          <el-option
            v-for="item in dict.bpm_oa_leave_type"
            :key="item.id"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <date-range-picker
          v-model="query.gmtCreate"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          class="date-item"
        />
        <el-select
          v-model="query.result"
          filterable
          clearable
          placeholder="结果"
          class="filter-item"
        >
          <el-option
            v-for="item in dict.bpm_process_instance_result"
            :key="item.id"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-input
          v-model="query.reason"
          clearable
          placeholder="原因"
          style="width: 185px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <rrOperation :crud="crud"/>
      </div>
      <crudOperation>
        <template slot="right">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                     v-permission="['bpm:oa-leave:create']"
          >发起请假
          </el-button>
        </template>
      </crudOperation>
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
        <el-table-column label="申请编号" align="center" prop="id" />
        <el-table-column label="状态" align="center" prop="result">
          <template v-slot="scope">
            <el-tag v-if="scope.row.result">{{ scope.row.resultLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="开始时间" align="center" prop="startTime" width="180">
          <template v-slot="scope">
            {{ parseTime(scope.row.startTime, '{y}-{m}-{d}')}}
          </template>
        </el-table-column>
        <el-table-column label="结束时间" align="center" prop="endTime" width="180">
          <template v-slot="scope">
            {{ parseTime(scope.row.endTime, '{y}-{m}-{d}')}}
          </template>
        </el-table-column>
        <el-table-column label="请假类型" align="center" prop="type">
          <template v-slot="scope">
            <el-tag v-if="scope.row.type">{{ scope.row.typeLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="原因" align="center" prop="reason" />
        <el-table-column label="申请时间" align="center" prop="gmtCreate" width="180" />

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
          <template v-slot="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleCancel(scope.row)"
                       v-permission="['bpm:oa-leave:create']" v-if="scope.row.result === 1">取消请假</el-button>
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)"
                       v-permission="['bpm:oa-leave:query']">详情</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleProcessDetail(scope.row)">审批进度</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页组件 -->
      <pagination/>
    </div>
  </div>
</template>

<script>
import {cancelProcessInstance} from "@/api/bpm/processInstance";
import pagination from '@crud/Pagination.vue'
import crudOperation from '@crud/CRUD.operation.vue'
import rrOperation from '@crud/RR.operation.vue'
import udOperation from '@crud/UD.operation.vue'
import DateRangePicker from '@/components/DateRangePicker/index.vue'
import CRUD, { crud, header, presenter } from '@crud/crud'

export default {
  name: "Leave",
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker
  },
  mixins: [presenter(), header(), crud()],
  dicts: ['bpm_oa_leave_type','bpm_process_instance_result'],
  cruds() {
    return CRUD({
      title: '请假',
      url: '/bpm/oa/leave/page',
      idField: 'id',
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
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({ path: "/bpm/oa/leave/create"});
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.$router.push({ path: "/bpm/oa/leave/detail", query: { id: row.id}});
    },
    /** 查看审批进度的操作 */
    handleProcessDetail(row) {
      this.$router.push({ path: "/bpm/process-instance/detail", query: { id: row.processInstanceId}});
    },
    /** 取消请假 */
    handleCancel(row) {
      const id = row.processInstanceId;
      this.$prompt('请输入取消原因？', "取消流程", {
        type: 'warning',
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /^[\s\S]*.*\S[\s\S]*$/, // 判断非空，且非空格
        inputErrorMessage: "取消原因不能为空",
      }).then(({ value }) => {
        return cancelProcessInstance(id, value);
      }).then(() => {
        this.crud.toQuery()
        this.$modal.msgSuccess("取消成功");
      })
    }
  }
};
</script>
