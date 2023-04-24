<template>

  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.key"
          clearable
          placeholder="流程标识"
          style="width: 185px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-input
          v-model="query.name"
          clearable
          placeholder="流程名称"
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
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog
        :close-on-click-modal="false"
        :before-close="crud.cancelCU"
        :visible.sync="crud.status.cu > 0"
        :title="crud.status.title"
        width="500px"
      >
        <el-form
          ref="form"
          v-loading="crud.editLoading"
          :model="form"
          :rules="rules"
          size="small"
          label-width="80px"
        >
          <el-form-item label="流程标识" prop="key">
            <el-input v-model="form.key" placeholder="请输入流标标识" style="width: 330px;" :disabled="!!form.id" />
            <el-tooltip v-if="!form.id" class="item" effect="light" content="新建后，流程标识不可修改！" placement="top">
              <i style="padding-left: 5px;" class="el-icon-question" />
            </el-tooltip>
            <el-tooltip v-else class="item" effect="light" content="流程标识不可修改！" placement="top">
              <i style="padding-left: 5px;" class="el-icon-question" />
            </el-tooltip>
          </el-form-item>
          <el-form-item label="流程名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入流程名称" :disabled="!!form.id" clearable />
          </el-form-item>
          <el-form-item v-if="form.id" label="流程分类" prop="category">
            <el-select v-model="form.category" placeholder="请选择流程分类" clearable style="width: 100%">
              <el-option v-for="d in dict.bpm_model_category" :key="d.value" :label="d.label" :value="d.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="流程描述" prop="description">
            <el-input v-model="form.description" type="textarea" clearable />
          </el-form-item>
          <div v-if="form.id">
            <el-form-item label="表单类型" prop="formType">
              <el-radio-group v-model="form.formType">
                <el-radio v-for="d in dict.bpm_model_form_type" :key="parseInt(d.value)" :label="parseInt(d.value)">
                  {{ d.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item v-if="form.formType === 1" label="流程表单" prop="formId">
              <el-select v-model="form.formId" clearable style="width: 100%">
                <el-option v-for="f in forms" :key="f.id" :label="f.name" :value="f.id" />
              </el-select>
            </el-form-item>
            <el-form-item v-if="form.formType === 2" label="表单提交路由" prop="formCustomCreatePath">
              <el-input v-model="form.formCustomCreatePath" placeholder="请输入表单提交路由" style="width: 330px;" />
              <el-tooltip class="item" effect="light" content="自定义表单的提交路径，使用 Vue 的路由地址，例如说：bpm/oa/leave/create" placement="top">
                <i style="padding-left: 5px;" class="el-icon-question" />
              </el-tooltip>
            </el-form-item>
            <el-form-item v-if="form.formType === 2" label="表单查看路由" prop="formCustomViewPath">
              <el-input v-model="form.formCustomViewPath" placeholder="请输入表单查看路由" style="width: 330px;" />
              <el-tooltip class="item" effect="light" content="自定义表单的查看路径，使用 Vue 的路由地址，例如说：bpm/oa/leave/view" placement="top">
                <i style="padding-left: 5px;" class="el-icon-question" />
              </el-tooltip>
            </el-form-item>
          </div>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button
            :loading="crud.status.cu == 2"
            type="primary"
            @click="crud.submitCU"
          >确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table
        ref="table"
        v-loading="crud.loading"
        highlight-current-row
        stripe
        :data="crud.data"
        size="small"
        style="width: 100%"
        @selection-change="crud.selectionChangeHandler"
      >
        <el-table-column label="流程标识" align="center" prop="key" />
        <el-table-column label="流程名称" align="center" prop="name" width="200" >
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
        <el-table-column label="表单信息" align="center" prop="formType" width="200" >
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
        <el-table-column prop="createTime" label="创建时间" width="180"/>
        <el-table-column label="最新部署的流程定义" align="center">
          <el-table-column label="流程版本" align="center" prop="processDefinition.version" width="80">
            <template v-slot="scope">
              <el-tag size="medium" v-if="scope.row.processDefinition">v{{ scope.row.processDefinition.version }}</el-tag>
              <el-tag size="medium" type="warning" v-else>未部署</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="激活状态" align="center" prop="processDefinition.suspensionState" width="80">
            <template v-slot="scope">
              <el-switch v-if="scope.row.processDefinition" v-model="scope.row.processDefinition.suspensionState"
                         :active-value="1" :inactive-value="2" @change="handleChangeState(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="部署时间" align="center" prop="deploymentTime" width="180">
            <template v-slot="scope">
              <span v-if="scope.row.processDefinition">{{scope.row.processDefinition.deploymentTime}}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column
          v-permission="['admin', 'student:edit', 'student:del']"
          label="操作"
          width="450"
          align="center"
          fixed="right"
        >
          <template v-slot="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="crud.toEdit(scope.row)"
                       v-permission="['bpm:model:update']">修改流程</el-button>
            <el-button size="mini" type="text" icon="el-icon-setting" @click="handleDesign(scope.row)"
                       v-permission="['bpm:model:update']">设计流程</el-button>
            <el-button size="mini" type="text" icon="el-icon-s-custom" @click="handleAssignRule(scope.row)"
                       v-permission="['bpm:task-assign-rule:query']">分配规则</el-button>
            <el-button size="mini" type="text" icon="el-icon-thumb" @click="handleDeploy(scope.row)"
                       v-permission="['bpm:model:deploy']">发布流程</el-button>
            <el-button size="mini" type="text" icon="el-icon-ice-cream-round" @click="handleDefinitionList(scope.row)"
                       v-permission="['bpm:process-definition:query']">流程定义</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                       v-permission="['bpm:model:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
    <el-dialog title="表单详情" :visible.sync="formVisible" width="50%">
      <formRender :form-json="formJson" :form-data="formData" :option-data="optionData">
      </formRender>
    </el-dialog>
    <!-- 流程模型图的预览 -->
    <el-dialog title="流程图" :visible.sync="showBpmnOpen" width="80%" append-to-body>
      <my-process-viewer key="designer" v-model="bpmnXML" v-bind="bpmnControlForm" />
    </el-dialog>
  </div>
</template>

<script>
import crudModel from '@/api/bpm/model'
import { getForm, listFormAllSimple } from '@/api/bpm/form'
import CRUD, { crud, form, header, presenter } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import formRender from '@/components/FormRender'
const defaultForm = {
  id: null,
  key: null,
  name: null,
  description: null,
  formType: null,
  formId: null,
  formCustomCreatePath: null,
  formCustomViewPath: null,
  category: null
}
export default {
  name: 'BpmModel',
  components: {
    pagination,
    crudOperation,
    rrOperation,
    udOperation,
    formRender
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['bpm_model_category', 'bpm_model_form_type'],
  cruds() {
    return CRUD({
      title: '流程模型',
      url: 'sys/bpm/model',
      idField: 'id',
      crudMethod: { ...crudModel },
      optShow: {
        add: true
      }
    })
  },
  data() {
    return {
      formVisible: false,
      formJson: {"widgetList":[],"formConfig":{"labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":"","functions":"","layoutType":"PC","onFormCreated":"","onFormMounted":"","onFormDataChange":""}},
      formData: {},
      optionData: {},
      // 流程表单的下拉框的数据
      forms: [],
      id: null,
      permission: {
        add: ['admin', 'student:add'],
        edit: ['admin', 'student:edit'],
        del: ['admin', 'student:del']
      },
      // BPMN 数据
      showBpmnOpen: false,
      bpmnXML: null,
      bpmnControlForm: {
        prefix: 'flowable'
      },
      rules: {
        key: [{ required: true, message: '流程标识不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '流程名称不能为空', trigger: 'blur' }],
        formType: [{ required: true, message: '流程名称不能为空', trigger: 'blur' }],
        formId: [{ required: true, message: '业务表单不能为空', trigger: 'blur' }],
        category: [{ required: true, message: '流程分类不能为空', trigger: 'blur' }],
        formCustomCreatePath: [{ required: true, message: '表单提交路由不能为空', trigger: 'blur' }],
        formCustomViewPath: [{ required: true, message: '表单查看路由不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    // 获得流程表单的下拉框的数据
    listFormAllSimple().then(response => {
      this.forms = response
    })
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    [CRUD.HOOK.afterToCU](crud, form) {
      this.id = form.id
    },
    // 提交之后
    [CRUD.HOOK.afterSubmit]() {
      if (this.id === null) {
        this.$alert('<strong>新建模型成功！</strong>后续需要执行如下 4 个步骤：' +
          '<div>1. 点击【修改流程】按钮，配置流程的分类、表单信息</div>' +
          '<div>2. 点击【设计流程】按钮，绘制流程图</div>' +
          '<div>3. 点击【分配规则】按钮，设置每个用户任务的审批人</div>' +
          '<div>4. 点击【发布流程】按钮，完成流程的最终发布</div>' +
          '另外，每次流程修改后，都需要点击【发布流程】按钮，才能正式生效！！！',
        '重要提示', {
          dangerouslyUseHTMLString: true,
          type: 'success'
        })
      }
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
        this.$router.push({ path: row.formCustomCreatePath})
      }
    },
    /** 流程图的详情按钮操作 */
    handleBpmnDetail(row) {
      crudModel.get(row.id).then(data => {
        this.bpmnXML = data.bpmnXml
        // 弹窗打开
        this.showBpmnOpen = true
      })
    },
    /** 处理任务分配规则列表的按钮操作 */
    handleAssignRule(row) {
      this.$refs['taskAssignRuleDialog'].initModel(row.id);
    },
    /** 设计按钮操作 */
    handleDesign(row) {
      this.$router.push({
        path:"/bpm/manager/model/design",
        query:{
          modelId: row.id
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const _this = this
      this.$modal.confirm('是否删除该流程！！').then(function() {
        _this.crud.doDelete(row)
      }).catch((e) => {console.log(e)});
    }
  }
}
</script>

<style lang="scss">
.my-process-designer {
  height: calc(100vh - 200px);
}
</style>
