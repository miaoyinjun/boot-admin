import request from '@/utils/request'

// 获取所有的Role
export function getAll() {
  return request({
    url: 'sys/roles/all',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'sys/roles',
    method: 'post',
    data
  })
}

export function get(id) {
  return request({
    url: 'sys/roles/' + id,
    method: 'get'
  })
}

export function getLevel() {
  return request({
    url: 'sys/roles/level',
    method: 'get'
  })
}

export function del(ids) {
  return request({
    url: 'sys/roles',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/roles',
    method: 'put',
    data
  })
}

export function editMenu(data) {
  return request({
    url: 'sys/roles/menu',
    method: 'put',
    data
  })
}

// 查询角色（精简)列表
export function listSimpleRoles() {
  return request({
    url: 'sys/roles/list-all-simple',
    method: 'get'
  })
}


export default { add, edit, del, get, editMenu, getLevel, listSimpleRoles }
