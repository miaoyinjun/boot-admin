<template>
  <div>
    <v-form-render :form-json="formJson" :form-data="formData" :option-data="optionData" ref="vFormRef">
    </v-form-render>
    <div align="center">
      <el-button type="primary" @click=submitForm>提交</el-button>
      <el-button @click=resetForm>重置</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FormRender',
  props: {
    formJson: {
      required: true
    },
    formData: {
      required: true
    },
    optionData: {
      required: true
    }
  },
  methods: {
    submitForm() {
      this.$refs.vFormRef.getFormData().then(formData => {
        // Form Validation OK
        console.log(JSON.stringify(formData))
        this.$emit('submit', {
          formData: formData
        })
      }).catch(error => {
        // Form Validation failed
        //this.$message.error(error)
      })
    },
    resetForm() {
      this.$refs.vFormRef.resetForm()
    }
  }
}
</script>
