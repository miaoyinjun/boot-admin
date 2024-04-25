<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <eHeader :dict="dict" :permission="permission" />
      <crudOperation :permission="permission" />
    </div>
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      highlight-current-row
      stripe
      :data="crud.data"
      style="width: 100%"
      @selection-change="crud.selectionChangeHandler"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="jobSort" label="排序">
        <template v-slot="scope">
          {{ scope.row.jobSort }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" align="center">
        <template v-slot="scope">
          <el-switch
            v-model="scope.row.enabled"
            :disabled="!checkPermission(['admin', 'job:edit', 'job:del'])"
            active-color="#409EFF"
            inactive-color="#F56C6C"
            @change="changeEnabled(scope.row, scope.row.enabled)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建日期">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.gmtCreate) }}</span>
        </template>
      </el-table-column>
      <!--   编辑与删除   -->
      <el-table-column
        v-permission="['admin', 'job:edit', 'job:del']"
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
    <!--表单渲染-->
    <eForm :job-status="dict.job_status" />
  </div>
</template>

<script>
import crudJob from '@/api/system/job'
import eHeader from './module/header'
import eForm from './module/form'
import Crud, { presenter } from '@crud/crud'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
import checkPermission from '@/utils/permission'
export default {
  name: 'Job',
  components: { eHeader, eForm, crudOperation, pagination, udOperation },
  cruds() {
    return Crud({
      title: '岗位',
      url: 'sys/job',
      crudMethod: { ...crudJob }
    })
  },
  mixins: [presenter()],
  // 数据字典
  dicts: ['job_status'],
  data() {
    return {
      permission: {
        add: ['admin', 'job:add'],
        edit: ['admin', 'job:edit'],
        del: ['admin', 'job:del']
      }
    }
  },
  methods: {
    checkPermission,
    // 改变状态
    changeEnabled(data, val) {
      this.$confirm(
        '此操作将 "' +
          this.dict.label.job_status[val] +
          '" ' +
          data.name +
          '岗位, 是否继续？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(() => {
          // eslint-disable-next-line no-undef
          crudJob
            .edit(data)
            .then(() => {
              // eslint-disable-next-line no-undef
              this.crud.notify(
                this.dict.label.job_status[val] + '成功',
                'success'
              )
            })
            .catch((err) => {
              data.enabled = !data.enabled
              console.log(err.data.message)
            })
        })
        .catch(() => {
          data.enabled = !data.enabled
        })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
