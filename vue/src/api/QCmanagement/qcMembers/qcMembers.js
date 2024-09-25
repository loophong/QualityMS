
import axios from 'axios';


// 获取QC人员列表
export function getList(pageIndex, pageSize, key) {
  return this.$http({
    url: this.$http.adornUrl('/qcSubject/registration/list'),
    method: 'get',
    params: this.$http.adornParams({
      'page': pageIndex,
      'limit': pageSize,
      'key': key
    })
  })
}


