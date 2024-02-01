<template>
  <div>
    <v-form-render :form-json="formJson" :form-data="formData" :option-data="optionData" ref="vFormRef">
    </v-form-render>
    <div align="center" v-if="formBtns">
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
  data() {
    return {
      formBtns: true,
    }
    },
  methods: {
    submitForm() {
      this.$refs.vFormRef.getFormData().then(formData => {
        // Form Validation OK
        this.$emit('submit', {
          formData: formData,
          disableForm: (val) => {
            this.disableForm(val)
          },
          hiddenFormBtns: (val) => {
            this.hiddenFormBtns(val)
          }
        })
      }).catch(error => {
        // Form Validation failed
        this.$message.error(error)
      })
    },
    resetForm() {
      this.$refs.vFormRef.resetForm()
    },
    disableForm(val) {
      if(val){
        this.$refs.vFormRef.disableForm()
      }else{
        this.$refs.vFormRef.enableForm()
      }
    },
    hiddenFormBtns(val) {
      this.formBtns=!val
    }
  }
}
</script>
