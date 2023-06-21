import Settings from '../../settings'

const baseApiUrl = Settings.apiPrefix
const baseSbaUrl = Settings.sbaPrefix
const api = {
  state: {
    // 根路径
    baseApiUrl: baseApiUrl,
    // 部署包上传
    deployUploadApi: baseApiUrl + '/sys/deploy/upload',
    // SQL脚本上传
    databaseUploadApi: baseApiUrl + '/sys/database/upload',
    // 实时控制台
    socketApi: baseApiUrl + '/sys/websocket?token=kl',
    // 图片上传
    imagesUploadApi: baseApiUrl + '/sys/localStorage/pictures',
    // 修改头像
    updateAvatarApi: baseApiUrl + '/sys/users/updateAvatar',
    // 上传文件到七牛云
    qiNiuUploadApi: baseApiUrl + '/sys/qiNiuContent',
    // Sql 监控
    sqlApi: baseSbaUrl + '/druid/index.html',
    // sba 监控
    sbaApi: baseSbaUrl,
    // swagger
    swaggerApi: baseSbaUrl + '/api/doc.html',
    // 文件上传
    fileUploadApi: baseApiUrl + '/sys/localStorage'
  }
}

export default api
