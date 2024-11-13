import request from '@/utils/httpRequest'

// 获得部门岗位分组分页
export const getUserPostDeptGroupPage = async (params) => {
  return request.get({ url: '/bpm/user-post-dept-group/page', params })
}

// 创建部门岗位分组
export const createUserPostDeptGroup = async (data) => {
  return request.post({
    url: '/bpm/user-post-dept-group/create',
    data
  })
}

// 获取部门岗位分组详情
export const getUserPostDept = async (params) => {
  return request.get({
    url: '/bpm/user-post-dept-group/get',
    params
  })
}

// 更新部门岗位分组
export const updateUserPostDeptGroup = async (data) => {
  return request.put({
    url: '/bpm/user-post-dept-group/update',
    data: data
  })
}

// 删除部门岗位分组
export const deleteUserPostDeptGroup = async (id) => {
  return request.delete({ url: '/bpm/user-post-dept-group/delete?id=' + id })
}
