import request from '@/utils/request'

export function get(id) {
  return request({
    url: '/bpm/model/' + id,
    method: 'get'
  })
}

export function edit(data) {
  return request({
    url: '/bpm/model',
    method: 'PUT',
    data: data
  })
}

// 任务状态修改
export function updateModelState(id, state) {
  return request({
    url: '/bpm/model/state',
    method: 'put',
    data: {
      id,
      state
    }
  })
}

export function add(data) {
  return request({
    url: '/bpm/model',
    method: 'POST',
    data: data
  })
}

export function del(ids) {
  return request({
    url: '/bpm/model',
    method: 'DELETE',
    data: ids
  })
}

export function deployModel(id) {
  return request({
    url: '/bpm/model/deploy?id=' + id,
    method: 'POST'
  })
}

export default { add, edit, del, get }
