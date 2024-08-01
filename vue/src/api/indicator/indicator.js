// import request from '@/utils/httpRequest'
//
// //获取指标统计列表
// export function listIndicatorSummary(query) {
//   return request({
//     url: '/indicator/indicatorindicatorsummary/list',
//     method: 'get',
//     params: query
//   });
// }

import request from '@/utils/httpRequest'
import http from "../../utils/httpRequest";

// 获取列表
export function listIndicatorSummary(params) {
  return request({
    url: http.adornUrl('/indicator/indicatorindicatorsummary/list'),
    method: 'get',
    params: params
  });
}
//excel导入
export function uploadFile(data, aimUrl) {
  return request({
    url: aimUrl,
    method: 'post',
    data: data
  })
}
