import request from '@/utils/axios'

// BPM 流程分类 API
export const CategoryApi = {
  // 查询流程分类分页
  getCategoryPage: async (params) => {
    return request.get({ url: `/bpm/category/list`, params })
  },

  // 查询流程分类列表
  getCategorySimpleList: async () => {
    return request.get({ url: `/bpm/category/simple-list` })
  },

  // 查询流程分类详情
  getCategory: async (id) => {
    return request.get({ url: `/bpm/category/get?id=` + id })
  },

  // 新增流程分类
  createCategory: async (data) => {
    return request.post({ url: `/bpm/category/create`, data })
  },

  // 修改流程分类
  updateCategory: async (data) => {
    return request.put({ url: `/bpm/category/update`, data })
  },

  // 删除流程分类
  deleteCategory: async (id) => {
    return request.delete({ url: `/bpm/category/delete?id=` + id })
  }
}
