<template>
  <div class="app-container">
    <!-- 第一步，通过流程定义的列表，选择对应的流程 -->
    <div v-if="!selectProcessInstance">
      <el-table v-loading="loading" :data="list">
        <el-table-column label="流程名称" align="center" prop="name" width="200">
          <template v-slot="scope">
            <el-button type="text" @click="handleBpmnDetail(scope.row)">
              <span>{{ scope.row.name }}</span>
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="流程分类" align="center" prop="category" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.category">{{ scope.row.categoryLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="流程版本" align="center" prop="processDefinition.version" width="80">
          <template v-slot="scope">
            <el-tag size="medium" v-if="scope.row">v{{ scope.row.version }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="流程描述" align="center" prop="description" width="300" show-overflow-tooltip />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template v-slot="scope">
            <el-button type="text" size="small" icon="el-icon-plus" @click="handleSelect(scope.row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 第二步，填写表单，进行流程的提交 -->
    <div v-else>
      <el-card class="box-card" >
        <div slot="header" class="clearfix">
          <span class="el-icon-document">申请信息【{{ selectProcessInstance.name }}】</span>
          <el-button style="float: right;" type="primary" @click="selectProcessInstance = undefined">选择其它流程</el-button>
        </div>
        <el-col :span="16" :offset="6">
          <formRender :form-json="formJson" :form-data="formData" :option-data="optionData" @submit="submitForm">
          </formRender>
        </el-col>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span class="el-icon-picture-outline">流程图</span>
        </div>
        <my-process-viewer key="designer" v-model="bpmnXML" v-bind="bpmnControlForm" />
      </el-card>
    </div>

  </div>
</template>

<script>
import {getProcessDefinitionBpmnXML, getProcessDefinitionList} from "@/api/bpm/definition";
import formRender from '@/components/FormRender'
import {createProcessInstance} from "@/api/bpm/processInstance";

// 流程实例的发起
export default {
  name: "ProcessInstanceCreate",
  components: {
    formRender
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 表格数据
      list: [],

      // 流程表单详情
      formJson: {"widgetList":[],"formConfig":{"labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":"","functions":"","layoutType":"PC","onFormCreated":"","onFormMounted":"","onFormDataChange":""}},
      formData: {},
      optionData: {},

      // BPMN 数据
      bpmnXML: null,
      bpmnControlForm: {
        prefix: "flowable"
      },

      // 流程表单
      selectProcessInstance: undefined, // 选择的流程实例
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程定义列表 */
    getList() {
      this.loading = true;
      getProcessDefinitionList({
        suspensionState: 1
      }).then(response => {
          this.list = response
          this.loading = false
        }
      );
    },
    /** 处理选择流程的按钮操作 **/
    handleSelect(row) {
      // 设置选择的流程
      this.selectProcessInstance = row;

      // 流程表单
      if (row.formId) {
        this.formJson = JSON.parse(row.formConf)
        // 加载流程图
        getProcessDefinitionBpmnXML(row.id).then(response => {
          this.bpmnXML = response
        })
      } else if (row.formCustomCreatePath) {
        this.$router.push({ path: row.formCustomCreatePath});
        // 这里暂时无需加载流程图，因为跳出到另外个 Tab；
      }
    },
    /** 提交按钮 */
    submitForm(params) {
      if (!params) {
        return;
      }
      params.disableForm(true);// 表单禁用
      params.hiddenFormBtns(true); // 按钮隐藏

      // 提交表单，创建流程
      const variables = params.formData;
      createProcessInstance({
        processDefinitionId: this.selectProcessInstance.id,
        variables: variables
      }).then(response => {
        this.$modal.msgSuccess("发起流程成功");
        // 关闭当前窗口
        this.$tab.closeOpenPage();
        this.$router.go(-1);
      }).catch(() => {
        params.disableForm(false); // 表单开启
        params.hiddenFormBtns(false); // 按钮展示
      })
    },
  }
};
</script>

<style lang="scss">
.my-process-designer {
  height: calc(100vh - 200px);
}

.box-card {
  width: 100%;
  margin-bottom: 20px;
}
</style>
