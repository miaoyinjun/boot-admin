import request from '@/utils/request'

export function getTodoTaskPage(query) {
  return request({
    url: '/sys/bpm/task/todo-page',
    method: 'get',
    params: query
  })
}

export function getDoneTaskPage(query) {
  return request({
    url: '/sys/bpm/task/done-page',
    method: 'get',
    params: query
  })
}

export function completeTask(data) {
  return request({
    url: '/sys/bpm/task/complete',
    method: 'PUT',
    data: data
  })
}

export function approveTask(data) {
  return request({
    url: '/sys/bpm/task/approve',
    method: 'PUT',
    data: data
  })
}

export function rejectTask(data) {
  return request({
    url: '/sys/bpm/task/reject',
    method: 'PUT',
    data: data
  })
}
export function backTask(data) {
  return request({
    url: '/sys/bpm/task/back',
    method: 'PUT',
    data: data
  })
}

export function updateTaskAssignee(data) {
  return request({
    url: '/sys/bpm/task/update-assignee',
    method: 'PUT',
    data: data
  })
}

export function getTaskListByProcessInstanceId(processInstanceId) {
  return request({
    url: '/sys/bpm/task/list-by-process-instance-id?processInstanceId=' + processInstanceId,
    method: 'get',
  })
}
