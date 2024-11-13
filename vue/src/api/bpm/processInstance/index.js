import request from '@/utils/httpRequest'

export const getProcessInstanceMyPage = async (params) => {
  return request.get({ url: '/bpm/process-instance/my-page', params })
}

export const getProcessInstanceManagerPage = async (params) => {
  return request.get({ url: '/bpm/process-instance/manager-page', params })
}

export const createProcessInstance = async (data) => {
  return request.post({ url: '/bpm/process-instance/create', data: data })
}

export const cancelProcessInstanceByStartUser = async (id, reason) => {
  const data = {
    id: id,
    reason: reason
  }
  return request.delete({ url: '/bpm/process-instance/cancel-by-start-user', data: data })
}

export const cancelProcessInstanceByAdmin = async (id, reason) => {
  const data = {
    id: id,
    reason: reason
  }
  return request.delete({ url: '/bpm/process-instance/cancel-by-admin', data: data })
}

export const getProcessInstance = async (id) => {
  return request.get({ url: '/bpm/process-instance/get?id=' + id })
}

export const getProcessInstanceCopyPage = async (params) => {
  return request.get({ url: '/bpm/process-instance/copy/page', params })
}
