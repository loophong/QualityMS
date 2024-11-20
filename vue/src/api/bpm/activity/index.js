import request from '@/utils/httpRequest'

export const getActivityList = async (params) => {
  return request.get({
    url: '/bpm/activity/list',
    params
  })
}
