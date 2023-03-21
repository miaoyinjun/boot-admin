import request from '@/utils/request'

// 创建工作流的表单定义
export function add(data) {
  return request({
    url: '/sys/bpm/form',
    method: 'post',
    data: data
  })
}

// 更新工作流的表单定义
export function edit(data) {
  return request({
    url: '/sys/bpm/form',
    method: 'put',
    data: data
  })
}

// 删除工作流的表单定义
export function del(ids) {
  return request({
    url: '/sys/bpm/form',
    method: 'delete',
    data: ids
  })
}

// 获得工作流的表单定义
export function get(id) {
  return request({
    url: '/sys/bpm/form/' + id,
    method: 'get'
  })
}

export default { add, edit, del, get }
