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

export function uploadImports(formData,url) {
    return request({
      url: url,
      method: 'post',
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      withCredentials: true,
      data: formData,
      onUploadProgress: (progressEvent) => {
        let progress = Math.round(
          (progressEvent.loaded * 100) / progressEvent.total
        );
        // 这里可以处理上传进度，例如通过事件发射给外部组件
        // this.$emit('progress', progress);
      },
    });
  }
