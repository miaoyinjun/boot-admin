<template>
  <div className="app-container">
    <p className="warn-content">
      Markdown 基于
      <el-link type="primary" href="https://github.com/hinesboy/mavonEditor" target="_blank">MavonEditor</el-link>
    </p>
    <mavon-editor ref="md" :style="'height:' + height" @imgAdd="imgAdd"/>
  </div>
</template>

<script>
import { upload } from '@/utils/upload'
import { mapGetters } from 'vuex'
import { mavonEditor } from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

export default {
  name: 'Markdown',
  components: {
    mavonEditor
  },
  data() {
    return {
      height: document.documentElement.clientHeight - 200 + 'px'
    }
  },
  computed: {
    ...mapGetters([
      'imagesUploadApi',
      'baseApi'
    ])
  },
  mounted() {
    const that = this
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 200 + 'px'
    }
  },
  methods: {
    imgAdd(pos, $file) {
      upload(this.imagesUploadApi, $file).then(res => {
        const data = res.data
        const url = this.baseApi + '/file/' + data.type + '/' + data.realName
        this.$refs.md.$img2Url(pos, url)
      })
    }
  }
}
</script>

<style scoped>
.v-note-wrapper.shadow {
  z-index: 5;
}
</style>
