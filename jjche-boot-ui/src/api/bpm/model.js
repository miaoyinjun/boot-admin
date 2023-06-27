import request from '@/utils/request'

export function get(id) {
  return request({
    url: '/sys/bpm/model/' + id,
    method: 'get'
  })
}

export function edit(data) {
  return request({
    url: '/sys/bpm/model',
    method: 'PUT',
    data: data
  })
}

// 任务状态修改
export function updateModelState(id, state) {
  return request({
    url: '/sys/bpm/model/state',
    method: 'put',
    data: {
      id,
      state
    }
  })
}

export function add(data) {
  return request({
    url: '/sys/bpm/model',
    method: 'POST',
    data: data
  })
}

export function del(ids) {
  return request({
    url: '/sys/bpm/model',
    method: 'DELETE',
    data: ids
  })
}

export function deployModel(id) {
  return request({
    url: '/sys/bpm/model/deploy?id=' + id,
    method: 'POST'
  })
}

export default { add, edit, del, get }
