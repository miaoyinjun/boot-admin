<template>
  <div>
    <!--工具栏-->
    <div class="head-container">
      <div>
        <!-- 搜索 -->
        <el-input
          v-model="query.blurry"
          size="small"
          clearable
          placeholder="输入名称搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <rrOperation />
      </div>

      <div class="crud-opts">
        <span class="crud-opts-left">
          <!--左侧插槽-->
          <slot name="left" />
          <el-button
            class="filter-item"
            size="mini"
            type="primary"
            icon="el-icon-plus"
            @click="toAdd"
          >
            新增
          </el-button>
          <el-button
            slot="reference"
            class="filter-item"
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :loading="crud.delAllLoading"
            :disabled="crud.selections.length === 0"
            @click="doDeleteSelect(crud.selections)"
          >
            删除
          </el-button>
          <!--右侧-->
          <slot name="right" />
        </span>
      </div>
    </div>
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :visible.sync="add.dialog"
      title="选择用户"
      width="620px"
    >
      <!--工具栏-->
      <div class="head-container">
        <!-- 搜索 -->
        <el-input
          v-model="add.blurry"
          size="small"
          clearable
          placeholder="输入名称搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="toAddQuery"
        />
        <el-button
          class="filter-item"
          size="mini"
          type="success"
          icon="el-icon-search"
          @click="toAddQuery"
        >搜索</el-button>
      </div>
      <!--表格渲染-->
      <el-table
        ref="table"
        v-loading="add.dataLoading"
        stripe
        :data="add.data"
        highlight-current-row
        style="width: 100%"
        @selection-change="addSelectionChangeHandler"
      >
        <el-table-column
          type="selection"
          width="55"
        />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickName" label="昵称" />
        <el-table-column prop="gender" label="性别" />
        <el-table-column prop="phone" label="电话" width="100px" />
        <el-table-column prop="email" label="邮箱" width="100px" />
        <el-table-column prop="enabled" label="状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled">激活</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <el-pagination
        background
        :page-size.sync="add.page.pageSize"
        :total="add.page.total"
        :current-page.sync="add.page.pageIndex"
        style="margin-top: 8px"
        layout="sizes, total,prev, pager, next"
        @size-change="addSizeChangeHandler($event)"
        @current-change="addPageChangeHandler"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="add.dialog=false">取消</el-button>
        <el-button
          :loading="add.loading"
          :disabled="add.selections.length === 0"
          type="primary"
          @click="doAdd"
        >确认</el-button>
      </div>
    </el-dialog>
    <div>
      <!--表格渲染-->
      <el-table
        ref="table"
        v-loading="crud.loading"
        stripe
        :data="crud.data"
        highlight-current-row
        style="width: 100%"
        @selection-change="crud.selectionChangeHandler"
      >
        <el-table-column
          type="selection"
          width="55"
        />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickName" label="昵称" />
        <el-table-column prop="gender" label="性别" />
        <el-table-column prop="phone" label="电话" width="100px" />
        <el-table-column prop="email" label="邮箱" width="100px" />
        <el-table-column prop="enabled" label="状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled">激活</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          v-permission="['roles:edit']"
          label="操作"
          align="center"
          fixed="right"
        >
          <template v-slot="scope">
            <el-popover
              v-model="pop"
              v-permission="['roles:edit']"
              placement="top"
              width="180"
              trigger="manual"
              @show="onPopoverShow"
              @hide="onPopoverHide"
            >
              <p>确定删除本条数据吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="doCancel">取消</el-button>
                <el-button
                  type="primary"
                  size="mini"
                  @click="doDelete(scope.row.id)"
                >确定</el-button>
              </div>
              <el-button
                slot="reference"
                type="danger"
                icon="el-icon-delete"
                size="mini"
                @click="toDelete"
              />
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import roleUser from '@/api/system/roleUser'
import Crud, { presenter, header } from '@crud/crud'
import pagination from '@crud/Pagination'
import rrOperation from '@crud/RR.operation'
export default {
  components: { pagination, rrOperation },
  cruds() {
    return [
      Crud({
        title: '数据规则详情',
        url: 'sys/roles/users',
        query: { roleId: '' },
        crudMethod: { ...roleUser },
        optShow: {
          add: true,
          edit: false,
          del: true,
          reset: false
        },
        queryOnPresenterCreated: false
      })
    ]
  },
  mixins: [presenter(), header()],
  data() {
    return {
      pop: false,
      add: {
        dialog: false,
        dataLoading: false,
        loading: false,
        data: [],
        selections: [],
        blurry: '',
        page: {
          // 页码
          pageIndex: 1,
          // 每页数据条数
          pageSize: 10,
          // 总数据条数
          total: 0
        }
      },
      permission: {
        add: ['roles:add'],
        edit: ['roles:edit'],
        del: ['roles:del']
      }
    }
  },
  methods: {
    doDeleteSelect(datas) {
      this.$confirm(`确认删除选中的${datas.length}条数据?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.crud.delAllLoading = true
          const params = []
          datas.forEach((data) => {
            params.push({ roleId: this.query.roleId, userId: data.id })
          })
          roleUser.del(params).then(() => {
            this.crud.delAllLoading = false
            this.crud.toQuery()
          }).catch(() => {
            this.crud.delAllLoading = false
          })
        })
        .catch(() => {})
    },
    doDelete(id) {
      this.pop = false
      const params = [{ roleId: this.query.roleId, userId: id }]
      roleUser.del(params).then(() => {
        this.pop = false
        this.crud.toQuery()
      }).catch(() => {
        this.pop = false
      })
    },
    doCancel() {
      this.pop = false
    },
    toDelete() {
      this.pop = true
    },
    onPopoverShow() {
      setTimeout(() => {
        document.addEventListener('click', this.handleDocumentClick)
      }, 0)
    },
    onPopoverHide() {
      document.removeEventListener('click', this.handleDocumentClick)
    },
    handleDocumentClick(event) {
      this.pop = false
    },
    toAdd() {
      this.add.dialog = true
      this.add.page.pageIndex = 1
      this.toAddQuery()
    },
    toAddQuery() {
      this.add.dataLoading = true
      const params = { pageIndex: this.add.page.pageIndex, pageSize: this.add.page.pageSize,
        roleId: this.query.roleId, blurry: this.add.blurry }
      roleUser.notUsers(params).then((data) => {
        this.add.page.total = data.total
        this.add.data = data.records
        this.add.dataLoading = false
      }).catch(() => {
        this.add.dataLoading = false
      })
    },
    addSelectionChangeHandler(val) {
      this.add.selections = val
    },
    doAdd() {
      const params = []
      this.add.selections.forEach((data) => {
        params.push({ roleId: this.query.roleId, userId: data.id })
      })
      roleUser.add(params).then(() => {
        this.add.dialog = false
        this.crud.toQuery()
      }).catch(() => {
        this.add.dialog = false
      })
    },
    // 每页条数改变
    addSizeChangeHandler(e) {
      this.add.page.pageSize = e
      this.add.page.pageIndex = 1
      this.toAddQuery()
    },
    // 当前页改变
    addPageChangeHandler(e) {
      this.add.page.pageIndex = e
      this.toAddQuery()
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
