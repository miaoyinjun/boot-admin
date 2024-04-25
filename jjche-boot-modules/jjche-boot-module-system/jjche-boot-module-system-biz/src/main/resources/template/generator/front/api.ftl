import request from '@/utils/request'

export function add(data) {
  return request({
    url: '${controllerBaseUrl}',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: '${controllerBaseUrl}/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: '${controllerBaseUrl}',
    method: 'put',
    data
  })
}

export function get(id) {
  return request({
    url: '${controllerBaseUrl}/' + id,
    method: 'get'
  })
}

export default { add, edit, del, get }
