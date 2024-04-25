import request from '@/utils/request'

export function getDepts(params) {
  return request({
    url: 'sys/dept',
    method: 'get',
    params
  })
}

export function getDeptSuperior(ids) {
  const data = ids
  return request({
    url: 'sys/dept/superior',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: 'sys/dept',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/dept',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/dept',
    method: 'put',
    data
  })
}

// 获取部门精简信息列表
export function listSimpleDepts() {
  return request({
    url: 'sys/dept/list-all-simple',
    method: 'get'
  })
}

export default { add, edit, del, getDepts, getDeptSuperior, listSimpleDepts }
