<template>
  <div class="mod-config">
    <el-form :inline="true" :model="myQueryParam" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="myQueryParam.topicName" placeholder="课题名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="myQueryParam.keywords" placeholder="课题关键字" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>

        <el-button v-if="isAuth('qcSubject:registration:save')" type="primary"
          @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('qcSubject:registration:save')" type="warning" @click="reuseHandle()"
          :disabled="dataListSelections.length != 1">课题重用</el-button>
        <el-button type="danger" @click="toIssue()">问题添加</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;" stripe>
      <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column>
      <el-table-column prop="topicName" header-align="center" align="center" label="课题名称">
      </el-table-column>
      <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
      </el-table-column>
      <el-table-column prop="groupName" header-align="center" align="center" label="小组名称">
      </el-table-column>
      <!-- <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
      </el-table-column> -->
      <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
      </el-table-column>
      <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
      </el-table-column>
      <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述/摘要">
      </el-table-column>
      <el-table-column prop="topicType" header-align="center" align="center" label="课题类型">
      </el-table-column>
      <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
      </el-table-column>
      <el-table-column prop="activityPlan" header-align="center" align="center" label="活动计划开始日期">
      </el-table-column>
      <el-table-column prop="activityPlanEnd" header-align="center" align="center" label="活动计划结束日期">
      </el-table-column>
      <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字">
      </el-table-column>
      <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态">
      </el-table-column>
      <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
      </el-table-column>
      <el-table-column prop="note" header-align="center" align="center" label="备注">
      </el-table-column>
      <el-table-column prop="topicReviewStatus" label="课题审核状态" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.topicReviewStatus === 0" style="color: #f43628;">未通过</span>
          <span v-else-if="scope.row.topicReviewStatus === 1" style="color: gray;">未开始</span>
          <span v-else-if="scope.row.topicReviewStatus === 2" style="color: #3f9ccb;">审核中</span>
          <span v-else-if="scope.row.topicReviewStatus === 3" style="color: #8dc146;">已通过</span>
          <span v-else>-</span> <!-- 处理未知状态 -->
        </template>
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.qcsrId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.qcsrId)">删除</el-button>
          <el-button type="text" size="small" @click="checkHandle(scope.row.qcsrId)">审核</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './qcSubjectRegistration-add-or-update'
export default {
  data() {
    return {
      dataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      groupMemberList: [],
      myQueryParam: {
        topicName: '',
        keywords: '',
      },
      reuseStepId: ''
    }
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList()
    this.getGroupMemberData().then(groupList => {
      this.groupMemberList = groupList;
    });
    // console.log('+++++++++++++++++++++++++++++++')
    // console.log(this.isAuth('qcExamine:interested:technology'))
    // console.log('+++++++++++++++++++++++++++++++')
  },
  methods: {

    parseTime(time) {
      return new Date(time).toLocaleString();
    },
    // 获取数据列表
    async getDataList() {
      this.dataListLoading = true
      await this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/list'),
        // url: this.$http.adornUrl('/qcPlan/step/reuse'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'topicName': this.myQueryParam.topicName,
          'keywords': this.myQueryParam.keywords
          // 'key': 1,
          // 'reuseStepId': 5
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }

        this.dataListLoading = false
      })
    },

    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.groupMemberList = this.groupMemberList
        this.$refs.addOrUpdate.init(id)
      })
    },
    //审核
    checkHandle(id) {
      this.dataForm.qcsrId = id;
      this.dataForm.topicReviewStatus = 2;
      this.$http({
        url: this.$http.adornUrl(`/qcSubject/registration/update`),
        method: 'post',
        data: this.$http.adornData({
          'qcsrId': this.dataForm.qcsrId,
          'topicReviewStatus': this.dataForm.topicReviewStatus
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: '提交审核成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        } else {
          this.$message.error(data.msg)
        }
      })
      this.getDataList();
    },
    //获取成员小组数据
    async getGroupMemberData() {
      return new Promise((resolve, reject) => {
        let groupList = [];
        this.$http({
          url: this.$http.adornUrl('/qcMembers/qcGroupMember/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 10000,
            'key': ''
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.groupMemberList = data.page.list;
            // this.totalPage = data.page.totalCount;
            groupList = this.groupMemberList;
            // 分组
            const map = {};
            groupList.forEach(item => {
              if (item.parentId === null) {
                map[item.qcgmId] = {
                  id: item.qcgmId,
                  date: item.participationDate,
                  name: item.name,
                  number: item.number,
                  groupName: item.groupName,
                  roleInTopic: '组长',
                  children: []
                };
              }
            });
            groupList.forEach(item => {
              if (item.parentId !== null && map[item.parentId]) {
                map[item.parentId].children.push({
                  id: item.qcgmId,
                  date: item.participationDate,
                  name: item.name,
                  number: item.number,
                  roleInTopic: item.roleInTopic,
                  parentId: item.parentId,
                });
              }
            });
            groupList = map
            this.groupMemberList = groupList;
          } else {
            this.groupMemberList = [];
            this.totalPage = 0;
          }
          resolve(groupList);
        }).catch(error => {
          console.error('Error fetching data:', error);
          // this.dataList = [];
          this.totalPage = 0;
          this.dataListLoading = false;
          reject(error);
        });
      });
    },
    toIssue() {
      this.$router.push(
        {
          name: 'otherToIssue',
          // query: {
          //   data: JSON.stringify(filteredArray)
          // }
        });
    },
    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.qcsrId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/qcSubject/registration/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    },
    // 课题重用
    async reuseHandle() {
      var ids = this.dataListSelections.map(item => {
        return item.qcsrId
      })
      const newDataForm = {
        qcsrId: 0,
        topicName: '',
        topicNumber: '',
        topicLeader: '',
        topicConsultant: '',
        teamNumberIds: [],
        createDate: '',
        creator: '',
        modificationDate: '',
        modifier: '',
        startDate: '',
        endDate: '',
        topicReviewStatus: '',
        topicDescription: '',
        topicType: '',
        activityCharacteristics: '',
        activityPlan: [],
        activityPlanEnd: '',
        keywords: '',
        topicActivityStatus: '',
        topicActivityResult: '',
        groupName: '',
        deleteFlag: '',
        note: ''
      }
      if (ids) {

        await this.$http({
          url: this.$http.adornUrl(`/qcSubject/registration/info/${ids}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          if (data && data.code === 0) {
            newDataForm.topicName = data.qcSubjectRegistration.topicName
            newDataForm.topicNumber = data.qcSubjectRegistration.topicNumber
            newDataForm.topicLeader = data.qcSubjectRegistration.topicLeader
            newDataForm.topicConsultant = data.qcSubjectRegistration.topicConsultant
            newDataForm.teamNumberIds = data.qcSubjectRegistration.teamNumberIds
            newDataForm.topicReviewStatus = data.qcSubjectRegistration.topicReviewStatus
            newDataForm.topicDescription = data.qcSubjectRegistration.topicDescription
            newDataForm.topicType = data.qcSubjectRegistration.topicType
            newDataForm.activityCharacteristics = data.qcSubjectRegistration.activityCharacteristics
            newDataForm.activityPlan = [new Date(data.qcSubjectRegistration.activityPlan), new Date(data.qcSubjectRegistration.activityPlanEnd)]
            newDataForm.keywords = data.qcSubjectRegistration.keywords
            newDataForm.topicActivityStatus = data.qcSubjectRegistration.topicActivityStatus
            newDataForm.topicActivityResult = data.qcSubjectRegistration.topicActivityResult
            newDataForm.deleteFlag = 0
            newDataForm.note = data.qcSubjectRegistration.note
          }
        })

        await this.$http({
          url: this.$http.adornUrl(`/qcSubject/registration/save`),
          method: 'post',
          data: this.$http.adornData({
            'qcsrId': this.dataForm.qcsrId || undefined,
            'topicName': `${newDataForm.topicName}(重用)`,
            'topicNumber': newDataForm.topicNumber,
            'topicLeader': newDataForm.topicLeader,
            'topicConsultant': newDataForm.topicConsultant,
            'teamNumberIds': newDataForm.teamNumberIds,
            'topicReviewStatus': '1',
            'topicDescription': newDataForm.topicDescription,
            'topicType': newDataForm.topicType,
            'activityCharacteristics': newDataForm.activityCharacteristics,
            'activityPlan': newDataForm.activityPlan[0],
            'activityPlanEnd': newDataForm.activityPlan[1],
            'keywords': newDataForm.keywords,
            'topicActivityStatus': '',
            'topicActivityResult': '',
            'deleteFlag': 0,
            'note': newDataForm.note
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '课题重用成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
            console.log(data)
            this.reuseStepId = data.id
          } else {
            this.$message.error(data.msg)
          }
        })

        await this.$http({
          url: this.$http.adornUrl('/qcPlan/step/reuse'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 10000,
            'key': '',
            'subjectID': ids[0],
            'reuseStepId': this.reuseStepId
          })
        })
      }
    },
  }
}
</script>
