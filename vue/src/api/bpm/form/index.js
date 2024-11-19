import request from '@/utils/httpRequest'

// 创建工作流的表单定义
export const createForm = async (data) => {
  return request.post({
    url: '/bpm/form/create',
    data: data
  })
}

// 更新工作流的表单定义
export const updateForm = async (data) => {
  return request.put({
    url: '/bpm/form/update',
    data: data
  })
}

// 删除工作流的表单定义
export const deleteForm = async (id) => {
  return request.delete({
    url: '/bpm/form/delete?id=' + id
  })
}

// 获得工作流的表单定义
export const getForm = async (id) => {
  return request.get({
    url: '/bpm/form/get?id=' + id
  })
}

// 获得工作流的表单定义分页
export const getFormPage = async (params) => {
  return request.get({
    url: '/bpm/form/page',
    params
  })
}

// 获得动态表单的精简列表
export const getFormSimpleList = async () => {
  return request.get({
    url: '/bpm/form/simple-list'
  })
}
