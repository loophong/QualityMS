import request from '@/utils/httpRequest'

// 创建用户组
export const createUserGroup = async (data) => {
  return request.post({
    url: '/bpm/user-group/create',
    data: data
  })
}

// 更新用户组
export const updateUserGroup = async (data) => {
  return request.put({
    url: '/bpm/user-group/update',
    data: data
  })
}

// 删除用户组
export const deleteUserGroup = async (id) => {
  return request.delete({ url: '/bpm/user-group/delete?id=' + id })
}

// 获得用户组
export const getUserGroup = async (id) => {
  return request.get({ url: '/bpm/user-group/get?id=' + id })
}

// 获得用户组分页
export const getUserGroupPage = async (params) => {
  return request.get({ url: '/bpm/user-group/page', params })
}

// 获取用户组精简信息列表
export const getUserGroupSimpleList = async () => {
  return request.get({ url: '/bpm/user-group/simple-list' })
}
