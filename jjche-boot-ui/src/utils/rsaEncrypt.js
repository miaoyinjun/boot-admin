import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair
// 为对一次打包docker镜像，各环境运行同一包，不使用各环境独立的publicKey，与后端保持一致
// const publicKey = process.env.VUE_APP_RSA_PUBLIC_KEY
const publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDK4b3DKtM26QeA++8vElist/F3Nc1+cDnJwk1LJGLmssjzLTaSA2HGHyyQdGQz8J8+KUJDN0SMPUVyy8bTH5FzSfGvKbVG4/wg3CWDqx09uP4KL8TBBnrcnjWdnYmftJGp3xDbCoSxNzCnxhvn/G/0LIYlGt0lyhOKnLjyPFoQsQIDAQAB'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对需要加密的数据进行加密
}

// 解密
// export function decrypt(txt) {
//   const encryptor = new JSEncrypt()
//   encryptor.setPrivateKey(privateKey)
//   return encryptor.decrypt(txt)
// }

