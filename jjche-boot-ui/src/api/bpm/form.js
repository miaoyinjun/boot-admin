import request from '@/utils/request'

// 创建工作流的表单定义
export function add(data) {
  return request({
    url: '/bpm/form',
    method: 'post',
    data: data
  })
}

// 更新工作流的表单定义
export function edit(data) {
  return request({
    url: '/bpm/form',
    method: 'put',
    data: data
  })
}

// 删除工作流的表单定义
export function del(ids) {
  return request({
    url: '/bpm/form',
    method: 'delete',
    data: ids
  })
}

// 获得工作流的表单定义
export function get(id) {
  return request({
    url: '/bpm/form/' + id,
    method: 'get'
  })
}

export function getForm(id) {
  return get(id)
}

export function listFormAllSimple() {
  return request({
    url: '/bpm/form/list-all-simple',
    method: 'get'
  })
}
export default { add, edit, del, get, listFormAllSimple, getForm }
