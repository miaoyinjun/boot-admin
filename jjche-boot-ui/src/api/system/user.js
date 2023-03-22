import request from '@/utils/request'
import { encrypt } from '@/utils/rsaEncrypt'

export function add(data) {
  return request({
    url: 'sys/users',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/users',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/users',
    method: 'put',
    data
  })
}

export function editUser(data) {
  return request({
    url: 'sys/users/center',
    method: 'put',
    data
  })
}

export function updatePass(user) {
  const data = {
    oldPass: encrypt(user.oldPass),
    newPass: encrypt(user.newPass)
  }
  return request({
    url: 'sys/users/updatePass/',
    method: 'post',
    data
  })
}

export function resetPass(username, newPass) {
  const data = {
    username: username,
    newPass: encrypt(newPass)
  }
  return request({
    url: 'sys/users/resetPass/',
    method: 'put',
    data
  })
}

export function updateEmail(form) {
  const data = {
    password: encrypt(form.pass),
    email: form.email
  }
  return request({
    url: 'sys/users/updateEmail/' + form.code,
    method: 'post',
    data
  })
}

export function listSimpleUsers() {
  return request({
    url: 'sys/users/sample',
    method: 'get'
  })
}

export default { add, edit, del, listSimpleUsers }

