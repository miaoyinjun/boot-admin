<template>
  <div class="app-container">
    <div class="head-container">
      <Search />
      <crudOperation>
        <el-button
          slot="left"
          :disabled="!checkPermission(['admin', 'log:del'])"
          class="filter-item"
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :loading="crud.delAllLoading"
          @click="confirmDelAll()"
        >
          清空
        </el-button>
      </crudOperation>
    </div>
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      highlight-current-row
      :data="crud.data"
      style="width: 100%"
      :row-class-name="tableRowClassName"
      @selection-change="crud.selectionChangeHandler"
    >
      <el-table-column
        prop="appName"
        label="系统"
        :show-overflow-tooltip="true"
      />
      <el-table-column type="expand" label="参数">
        <template v-slot="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="请求地址">
              <span>{{ props.row.url }}</span>
            </el-form-item>
            <el-form-item label="请求id">
              <span>{{ props.row.requestId }}</span>
            </el-form-item>
            <el-form-item label="请求方法">
              <span>{{ props.row.method }}</span>
            </el-form-item>
            <el-form-item label="请求参数">
              <pre v-highlightjs="props.row.params"><code class="json" /></pre>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        prop="module"
        label="模块"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="category"
        label="分类"
      >
        <template v-slot="scope">
          <span>{{ parseCategory(scope.row.category) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="logType" label="类型">
        <template v-slot="scope">
          <span>{{ parseLogType(scope.row.logType) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="description"
        label="描述"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="bizKey"
        label="业务主键"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="bizNo"
        label="业务编号"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="detail"
        label="详情"
      />
      <el-table-column prop="isSuccess" label="状态">
        <template v-slot="scope">
          <el-tag v-if="scope.row.isSuccess">成功</el-tag>
          <el-button
            v-if="scope.row.isSuccess == false"
            size="mini"
            type="text"
            @click="info(scope.row.id)"
          >异常详情</el-button>
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="result"
        label="结果"
      />
      <el-table-column prop="time" label="请求耗时" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.time <= 300">{{ scope.row.time }}ms</el-tag>
          <el-tag
            v-else-if="scope.row.time <= 1000"
            type="warning"
          >{{ scope.row.time }}ms</el-tag>
          <el-tag v-else type="danger">{{ scope.row.time }}ms</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="browser" label="浏览器" />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="os"
        label="操作系统"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="requestIp"
        label="IP"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="address"
        label="IP来源"
      />
      <el-table-column prop="username" label="操作人" />
      <el-table-column prop="gmtCreate" label="操作时间" width="180px">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.gmtCreate) }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      :visible.sync="dialog"
      title="详情"
      append-to-body
      top="30px"
      width="85%"
    >
      <pre v-highlightjs="errorInfo"><code class="java" /></pre>
    </el-dialog>
    <!--分页组件-->
    <pagination />
  </div>
</template>

<script>
import Search from './search'
import { delAllInfo, getErrDetail } from '@/api/monitor/log'
import Crud, { presenter } from '@crud/crud'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import checkPermission from '@/utils/permission'

export default {
  name: 'Log',
  components: { Search, crudOperation, pagination },
  cruds() {
    return Crud({ title: '日志', url: 'sys/logs' })
  },
  mixins: [presenter()],
  data() {
    return {
      errorInfo: '',
      dialog: false
    }
  },
  created() {
    this.crud.optShow = {
      add: false,
      edit: false,
      del: false,
      download: true
    }
  },
  methods: {
    checkPermission,
    // 获取异常详情
    info(id) {
      this.dialog = true
      getErrDetail(id).then((res) => {
        this.errorInfo = res.exception
      })
    },
    confirmDelAll() {
      this.$confirm(`确认清空所有操作日志吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.crud.delAllLoading = true
          delAllInfo()
            .then((res) => {
              this.crud.delAllLoading = false
              this.crud.dleChangePage(1)
              this.crud.delSuccessNotify()
              this.crud.toQuery()
            })
            .catch((err) => {
              this.crud.delAllLoading = false
              console.log(err.response.data.message)
            })
        })
        .catch(() => {})
    },
    parseLogType(logType) {
      if (logType != null && logType !== '') {
        if (logType === 'ADD') {
          return '新增'
        } else if (logType === 'DELETE') {
          return '删除'
        } else if (logType === 'UPDATE') {
          return '更新'
        } else {
          return '查询'
        }
      }
    },
    parseCategory(category) {
      if (category != null && category !== '') {
        if (category === 'MANAGER') {
          return '管理员'
        } else if (category === 'OPERATING') {
          return '运营'
        } else {
          return '其它'
        }
      }
    },
    tableRowClassName({ row, rowIndex }) {
      return row.isSuccess === false ? 'warning-row' : ''
    }
  }
}
</script>

<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 70px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
.demo-table-expand .el-form-item__content {
  font-size: 12px;
}

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>
