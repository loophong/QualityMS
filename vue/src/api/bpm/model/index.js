import request from '@/utils/axios/index'

// getExtensionForm(instanceId, taskId) {
//   return request({
//     url: `/wx/xtkd/pjtapply/extension/approvalForm?instanceId=${instanceId}&taskId=${taskId}`,
//     method: 'get'
//   })
// },

// export const getModelPage = async (params) => {
//   return request.get({ url: '/bpm/model/list', params })
// }

export const getModelPage = async (params) => {
  return request.get({ url: '/bpm/model/page', params })
}

export const getModel = async (id) => {
  return request.get({ url: '/bpm/model/get?id=' + id })
}

export const updateModel = async (data) => {
  console.log('updateModel', data)
  return request.put({ url: '/bpm/model/update', data: data })
}

// 任务状态修改
export const updateModelState = async (id, state) => {
  const data = {
    id: id,
    state: state
  }
  return request.put({ url: '/bpm/model/update-state', data: data })
}

export const createModel = async (data) => {
  return request.post({ url: '/bpm/model/create', data: data })
}

export const deleteModel = async (id) => {
  return request.delete({ url: '/bpm/model/delete?id=' + id })
}

// 发布流程
export const deployModel = async (id) => {
  return request.post({ url: '/bpm/model/deploy?id=' + id })
}
