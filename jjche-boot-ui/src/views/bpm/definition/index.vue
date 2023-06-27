<template>
  <div class="app-container">
    <doc-alert title="工作流" url="https://doc.iocoder.cn/bpm"/>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="定义编号" align="center" prop="id" width="400"/>
      <el-table-column label="定义名称" align="center" prop="name" width="100">
        <template v-slot="scope">
          <el-button type="text" @click="handleBpmnDetail(scope.row)">
            <span>{{ scope.row.name }}</span>
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="定义分类" align="center" prop="category" width="100">
        <template v-slot="scope">
          <el-tag v-if="scope.row.category">{{ scope.row.categoryLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="表单信息" align="center" prop="formType" width="200">
        <template v-slot="scope">
          <el-button v-if="scope.row.formId" type="text" @click="handleFormDetail(scope.row)">
            <span>{{ scope.row.formName }}</span>
          </el-button>
          <el-button v-else-if="scope.row.formCustomCreatePath" type="text" @click="handleFormDetail(scope.row)">
            <span>{{ scope.row.formCustomCreatePath }}</span>
          </el-button>
          <label v-else>暂无表单</label>
        </template>
      </el-table-column>
      <el-table-column label="流程版本" align="center" prop="processDefinition.version" width="80">
        <template v-slot="scope">
          <el-tag size="medium" v-if="scope.row">v{{ scope.row.version }}</el-tag>
          <el-tag size="medium" type="warning" v-else>未部署</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="version" width="80">
        <template v-slot="scope">
          <el-tag type="success" v-if="scope.row.suspensionState === 1">激活</el-tag>
          <el-tag type="warning" v-if="scope.row.suspensionState === 2">挂起</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="部署时间" align="center" prop="deploymentTime" width="180">
        <template v-slot="scope">
          <span>{{ scope.row.deploymentTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="定义描述" align="center" prop="description" width="300" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" width="150" fixed="right">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-s-custom" @click="handleAssignRule(scope.row)"
                     v-permission="['bpm:task-assign-rule:update']"
          >分配规则
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 流程表单配置详情 -->
    <el-dialog title="表单详情" :visible.sync="formVisible" width="50%">
      <formRender :form-json="formJson" :form-data="formData" :option-data="optionData">
      </formRender>
    </el-dialog>

    <!-- 流程模型图的预览 -->
    <el-dialog title="流程图" :visible.sync="showBpmnOpen" width="80%" append-to-body>
      <my-process-viewer key="designer" v-model="bpmnXML" v-bind="bpmnControlForm"/>
    </el-dialog>

    <!-- 分页组件 -->
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- ========== 流程任务分配规则 ========== -->
    <taskAssignRuleDialog ref="taskAssignRuleDialog"/>
  </div>
</template>

<script>
import { getProcessDefinitionBpmnXML, getProcessDefinitionPage } from '@/api/bpm/definition'
import formRender from '@/components/FormRender'
import taskAssignRuleDialog from '../taskAssignRule/taskAssignRuleDialog'
import { getForm } from '@/api/bpm/form'
import pagination from '@/components/Pagination'

export default {
  name: 'processDefinition',
  components: {
    taskAssignRuleDialog,
    formRender,
    pagination
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10
      },
      // BPMN 数据
      showBpmnOpen: false,
      bpmnXML: null,
      bpmnControlForm: {
        prefix: 'flowable'
      },
      formVisible: false,
      formJson: {
        'widgetList': [],
        'formConfig': {
          'labelWidth': 80,
          'labelPosition': 'left',
          'size': '',
          'labelAlign': 'label-left-align',
          'cssCode': '',
          'customClass': '',
          'functions': '',
          'layoutType': 'PC',
          'onFormCreated': '',
          'onFormMounted': '',
          'onFormDataChange': ''
        }
      },
      formData: {},
      optionData: {}
    }
  },
  created() {
    const key = this.$route.query && this.$route.query.key
    if (key) {
      this.queryParams['key'] = key
    }
    this.getList()
  },
  methods: {
    /** 查询流程定义列表 */
    getList() {
      this.loading = true
      getProcessDefinitionPage(this.queryParams).then(data => {
          this.list = data.records
          this.total = data.total
          this.loading = false
        }
      )
    },
    /** 流程表单的详情按钮操作 */
    handleFormDetail(row) {
      // 流程表单
      if (row.formId) {
        getForm(row.formId).then(data => {
          // 设置值
          this.formVisible = true
          this.formJson = JSON.parse(data.content)
        })
        // 业务表单
      } else if (row.formCustomCreatePath) {
        this.$router.push({ path: row.formCustomCreatePath })
      }
    },
    /** 流程图的详情按钮操作 */
    handleBpmnDetail(row) {
      getProcessDefinitionBpmnXML(row.id).then(data => {
        this.bpmnXML = data
        // 弹窗打开
        this.showBpmnOpen = true
      })
    },
    /** 处理任务分配规则列表的按钮操作 */
    handleAssignRule(row) {
      this.$refs['taskAssignRuleDialog'].initProcessDefinition(row.id)
    }
  }
}
</script>

<style lang="scss">
.my-process-designer {
  height: calc(100vh - 200px);
}
</style>
