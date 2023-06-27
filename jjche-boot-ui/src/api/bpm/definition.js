import request from '@/utils/request'

export function getProcessDefinitionPage(query) {
  return request({
    url: '/sys/bpm/process-definition/page',
    method: 'get',
    params: query
  })
}

export function getProcessDefinitionList(query) {
  return request({
    url: '/sys/bpm/process-definition/list',
    method: 'get',
    params: query
  })
}

export function getProcessDefinitionBpmnXML(id) {
  return request({
    url: '/sys/bpm/process-definition/get-bpmn-xml?id=' + id,
    method: 'get'
  })
}
