import request from '@/utils/request'

export function getMenusTree(pid, roleId) {
  return request({
    url: 'sys/menus/lazy?pid=' + pid + '&roleId=' + roleId,
    method: 'get'
  })
}

export function getMenus(params) {
  return request({
    url: 'sys/menus',
    method: 'get',
    params
  })
}

export function getMenuSuperior(ids) {
  const data = Array.isArray(ids) || ids.length === 0 ? ids : Array.of(ids)
  return request({
    url: 'sys/menus/superior',
    method: 'post',
    data
  })
}

export function getChild(id) {
  return request({
    url: 'sys/menus/child?id=' + id,
    method: 'get'
  })
}

export function buildMenus() {
  return request({
    url: 'sys/menus/build',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'sys/menus',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/menus',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/menus',
    method: 'put',
    data
  })
}

export default { add, edit, del, getMenusTree, getMenuSuperior, getMenus, getChild }
