import request from '@/utils/httpRequest'

export const getProcessDefinition = async (id, key) => {
  return request.get({
    url: '/bpm/process-definition/get',
    params: { id, key }
  })
}

export const getProcessDefinitionPage = async (params) => {
  return request.get({
    url: '/bpm/process-definition/page',
    params
  })
}

export const getProcessDefinitionList = async (params) => {
  return request.get({
    url: '/bpm/process-definition/list',
    params
  })
}
