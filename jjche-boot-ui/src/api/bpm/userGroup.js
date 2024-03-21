import request from '@/utils/request'

// 创建用户组
export function add(data) {
  return request({
    url: '/bpm/user-group',
    method: 'post',
    data: data
  })
}

// 更新用户组
export function edit(data) {
  return request({
    url: '/bpm/user-group',
    method: 'put',
    data: data
  })
}

// 删除用户组
export function del(ids) {
  return request({
    url: '/bpm/user-group',
    method: 'delete',
    data: ids
  })
}

// 获得用户组
export function get(id) {
  return request({
    url: '/bpm/user-group/' + id,
    method: 'get'
  })
}


// 获取用户组精简信息列表
export function listSimpleUserGroups() {
  return request({
    url: '/bpm/user-group/list-all-simple',
    method: 'get'
  })
}

export default { add, edit, del, get, listSimpleUserGroups }
