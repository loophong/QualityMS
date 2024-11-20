import request from '@/utils/httpRequest'

export const getTaskTodoPage = async (params) => {
  return request.get({ url: '/bpm/task/todo-page', params })
}

export const getTaskDonePage = async (params) => {
  return request.get({ url: '/bpm/task/done-page', params })
}

export const getTaskManagerPage = async (params) => {
  return request.get({ url: '/bpm/task/manager-page', params })
}

export const approveTask = async (data) => {
  return request.put({ url: '/bpm/task/approve', data })
}

export const rejectTask = async (data) => {
  return request.put({ url: '/bpm/task/reject', data })
}

export const getTaskListByProcessInstanceId = async (processInstanceId) => {
  return request.get({
    url: '/bpm/task/list-by-process-instance-id?processInstanceId=' + processInstanceId
  })
}

// 获取所有可回退的节点
export const getTaskListByReturn = async (id) => {
  return request.get({ url: '/bpm/task/list-by-return', params: { id } })
}

// 回退
export const returnTask = async (data) => {
  return request.put({ url: '/bpm/task/return', data })
}

// 委派
export const delegateTask = async (data) => {
  return request.put({ url: '/bpm/task/delegate', data })
}

// 转派
export const transferTask = async (data) => {
  return request.put({ url: '/bpm/task/transfer', data })
}

// 加签
export const signCreateTask = async (data) => {
  return request.put({ url: '/bpm/task/create-sign', data })
}

// 减签
export const signDeleteTask = async (data) => {
  return request.delete({ url: '/bpm/task/delete-sign', data })
}

// 获取减签任务列表
export const getChildrenTaskList = async (id) => {
  return request.get({ url: '/bpm/task/list-by-parent-task-id?parentTaskId=' + id })
}
