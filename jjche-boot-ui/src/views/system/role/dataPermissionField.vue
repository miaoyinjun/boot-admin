<template>
  <div>
    <div>
      <el-button
        v-permission="['admin', 'roles:edit']"
        class="filter-item"
        size="mini"
        style="float: right; margin-right: 10px; padding: 4px 10px"
        type="primary"
        icon="el-icon-check"
        :loading="dataPermissionFieldLoading"
        @click="saveChecked"
      >保存
      </el-button>
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
          :selectable="checkSelectable"
        />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="name" label="可见">
          <template v-slot="scope">
            <el-switch
              v-model="scope.row.isAccessible"
              active-color="#13ce66"
              inactive-color="#ff4949"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="可编辑">
          <template v-slot="scope">
            <el-switch
              v-model="scope.row.isEditable"
              active-color="#13ce66"
              inactive-color="#ff4949"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudDataPermissionField from '@/api/system/dataPermissionFieldRole'
import Crud, { presenter, header } from '@crud/crud'
import pagination from '@crud/Pagination'

export default {
  components: { pagination },
  dicts: ['dataPermissionField_condition'],
  cruds() {
    return [
      Crud({
        title: '数据字段详情',
        url: 'sys/data_permission_field_roles',
        query: { menuId: '', roleId: '' },
        crudMethod: { ...crudDataPermissionField },
        optShow: {
          add: true,
          edit: true,
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
      dataPermissionFieldLoading: false
    }
  },
  methods: {
    // 新增前合并dictId
    [Crud.HOOK.afterRefresh]() {
      // 获取选中
      this.crud.data.forEach((val) => {
        if (val.isSelected) {
          this.crud.getTable().toggleRowSelection(val)
        }
      })
    },
    saveChecked() {
      this.dataPermissionFieldLoading = true
      const selectedDataList = []

      this.crud.selections.forEach((val) => {
        const pushData = {
          id: val.id,
          isAccessible: val.isAccessible,
          isEditable: val.isEditable
        }
        selectedDataList.push(pushData)
      })
      const params = {
        roleId: this.query.roleId,
        menuId: this.query.menuId,
        dataPermissionFieldSelectedList: selectedDataList
      }
      crudDataPermissionField
        .add(params)
        .then(() => {
          this.crud.notify('保存成功', Crud.NOTIFICATION_TYPE.SUCCESS)
          this.dataPermissionFieldLoading = false
        })
        .catch((err) => {
          this.dataPermissionFieldLoading = false
          console.log(err.response.data.message)
        })
    },
    checkSelectable(row) {
      return row.isActivated === true
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
