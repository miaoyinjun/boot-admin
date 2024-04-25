<template>
  <div class="app-container">
    <v-form-designer ref="vfDesigner" :designer-config="designerConfig" id="vformabc">
      <template #customToolButtons>
        <el-button type="text" @click="saveFormVisible = true">
          <i class="el-icon-finished"/>保存</el-button>
      </template>
    </v-form-designer>
    <!--表单组件-->
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="saveFormVisible"
      title="保存"
      width="500px"
    >
      <el-form
        ref="form"
        v-loading="saveFormLoading"
        :model="form"
        :rules="rules"
        size="small"
        label-width="80px"
      >
        <el-form-item label="表单名称" prop="name">
          <el-input v-model="form.name" style="width: 370px"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" style="width: 370px"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text"
                   @click="saveFormVisible = false">取消</el-button>
        <el-button
          :loading="saveFormLoading"
          type="primary"
          @click="saveForm"
        >确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import crudBpmForm from '@/api/bpm/form'

export default {
  data() {
    return {
      saveFormVisible: false,
      saveFormLoading: false,
      form: {
        id: null,
        name: null,
        remark: null,
        content: null
      },
      rules: {
        name: [{ required: true, message: '表单名称不能为空', trigger: 'blur' }]
      },
      designerConfig: {
        exportCodeButton: false,
        generateSFCButton: false,
        languageMenu: false,
        externalLink: false
      }
    }
  },
  created() {
    const _this = this
    this.$nextTick(() => {
      _this.$refs.vfDesigner.clearDesigner()
    })
    // 读取表单配置
    const formId = this.$route.query && this.$route.query.formId
    if (formId) {
      // const loading = this.$loading({
      //   lock: true,
      //   fullscreen: false,
      //   text: 'Loading',
      //   spinner: 'el-icon-loading',
      //   background: 'rgba(0, 0, 0, 0.7)',
      //   target: 'vformabc'
      // })
      crudBpmForm.get(formId).then(data => {
        this.form = data
        this.$refs.vfDesigner.setFormJson(JSON.parse(data.content))
        // loading.close()
      })
    }
  },
  methods: {
    saveForm() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.saveFormVisible = true
          this.saveFormLoading = true
          const formJson = this.$refs.vfDesigner.getFormJson()
          this.form.content = JSON.stringify(formJson)
          this.form.id = this.$route.query && this.$route.query.formId
          if (this.form.id != null) {
            crudBpmForm.edit(this.form).then(() => {
              this.saveFormAfter()
            }).catch((err) => {
              console.log(err)
              this.saveFormVisible = false
              this.saveFormLoading = false
            })
          } else {
            crudBpmForm.add(this.form).then(() => {
              this.saveFormAfter()
            }).catch((err) => {
              console.log(err)
              this.saveFormVisible = false
              this.saveFormLoading = false
            })
          }
        } else {
          return false
        }
      })
    },
    saveFormAfter() {
      this.$notify({
        title: '保存成功',
        type: 'success',
        duration: 2500
      })
      this.saveFormVisible = false
      this.saveFormLoading = false
      this.$router.push({
        path: '/bpm/manager/form'
      })
    }

  }
}
</script>

<style lang="scss">
.el-container.main-container {
  margin-left: 0 !important;
}
</style>
