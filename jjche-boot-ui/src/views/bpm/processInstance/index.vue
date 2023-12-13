<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <doc-alert title="工作流" url="https://doc.iocoder.cn/bpm"/>
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
        <el-input
          v-model="query.processDefinitionId"
          clearable
          placeholder="所属流程"
          style="width: 185px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-select
          v-model="query.category"
          filterable
          clearable
          placeholder="流程分类"
          class="filter-item"
        >
          <el-option
            v-for="item in dict.bpm_model_category"
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
          v-model="query.status"
          filterable
          clearable
          placeholder="状态"
          class="filter-item"
        >
          <el-option
            v-for="item in dict.bpm_process_instance_status"
            :key="item.id"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
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
        <rrOperation :crud="crud"/>
      </div>
      <crudOperation >
        <template slot="right">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                     v-permission="['bpm:process-instance:query']"
          >发起流程
          </el-button>
        </template>
      </crudOperation>

      <!-- 列表 -->
      <el-table v-loading="loading" :data="list">
        <el-table-column label="编号" align="center" prop="id" width="320"/>
        <el-table-column label="流程名" align="center" prop="name"/>
        <el-table-column label="流程分类" align="center" prop="category">
          <template v-slot="scope">
            <el-tag v-if="scope.row.category">{{ scope.row.categoryLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前审批任务" align="center" prop="tasks">
          <template v-slot="scope">
            <el-button v-for="task in scope.row.tasks" :key="task.id" type="text">
              <span>{{ task.name }}</span>
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status">
          <template v-slot="scope">
            <el-tag v-if="scope.row.category">{{ scope.row.statusLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="结果" align="center" prop="result">
          <template v-slot="scope">
            <el-tag v-if="scope.row.category">{{ scope.row.resultLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" align="center" prop="gmtCreate" width="180"/>
        <el-table-column label="结束时间" align="center" prop="endTime" width="180"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template v-slot="scope">
            <el-button type="text" size="small" icon="el-icon-delete" v-if="scope.row.result === 1"
                       v-hasPermi="['bpm:process-instance:cancel']" @click="handleCancel(scope.row)"
            >取消
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleDetail(scope.row)"
                       v-hasPermi="['bpm:process-instance:query']"
            >详情
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
import { cancelProcessInstance, getMyProcessInstancePage } from '@/api/bpm/processInstance'
import pagination from '@crud/Pagination'
import CRUD, { crud, header, presenter } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import crudModel from '@/api/bpm/model'
import DateRangePicker from '@/components/DateRangePicker'

export default {
  name: 'ProcessInstance',
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    DateRangePicker
  },
  mixins: [presenter(), header(), crud()],
  dicts: ['bpm_model_category', 'bpm_process_instance_status', 'bpm_process_instance_result'],
  cruds() {
    return CRUD({
      title: '我的流程',
      url: 'sys/bpm/process-instance/my-page',
      idField: 'id',
      crudMethod: { ...crudModel }
    })
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 工作流的流程实例的拓展列表
      list: [],
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        name: null,
        processDefinitionId: null,
        category: null,
        status: null,
        result: null,
        gmtCreate: []
      },
      permission: {

      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      // 执行查询
      getMyProcessInstancePage(this.queryParams).then(data => {
        this.list = data.list
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 新增按钮操作 **/
    handleAdd() {
      this.$router.push({ path: '/bpm/process-instance/create' })
    },
    /** 取消按钮操作 */
    handleCancel(row) {
      const id = row.id
      this.$prompt('请输入取消原因？', '取消流程', {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[\s\S]*.*\S[\s\S]*$/, // 判断非空，且非空格
        inputErrorMessage: '取消原因不能为空'
      }).then(({ value }) => {
        return cancelProcessInstance(id, value)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('取消成功')
      })
    },
    /** 处理详情按钮 */
    handleDetail(row) {
      this.$router.push({ path: '/bpm/process-instance/detail', query: { id: row.id } })
    }
  }
}
</script>
