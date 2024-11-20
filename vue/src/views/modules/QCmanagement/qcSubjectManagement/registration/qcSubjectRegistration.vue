<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="所有课题" name="1" v-if="isAuth('qcManagement:group:admin')">
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
          style="width: 100%;" stripe highlight-current-row>
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称">
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
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
          </el-table-column>
          <el-table-column prop="groupName" header-align="center" align="center" label="小组名称">
          </el-table-column>
          <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column>
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
            <template slot-scope="scope">
              <span v-if="scope.row.topicActivityResult && 85 <= scope.row.topicActivityResult">一等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 75 <= scope.row.topicActivityResult < 85">二等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 65 <= scope.row.topicActivityResult < 75">三等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 55 <= scope.row.topicActivityResult < 65">四等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 45 <= scope.row.topicActivityResult < 55">鼓励奖</span>
              <span v-else>--</span>
            </template>
          </el-table-column>
          <el-table-column prop="note" header-align="center" align="center" label="备注">
          </el-table-column>

          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.qcsrId)">修改</el-button>
              <el-button type="text" size="small" @click="deleteHandle(scope.row.qcsrId)">删除</el-button>
              <el-button type="text" size="small" v-if="scope.row.topicReviewStatus != 3"
                @click="checkHandle(scope.row.qcsrId)">提交审核</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <!-- <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update> -->
      </div>
    </el-tab-pane>
    <el-tab-pane label="我创办的课题" name="2">
      <div class="mod-config">
        <el-form :inline="true" :model="myQueryParamLead" @keyup.enter.native="getLeadList()">
          <el-form-item>
            <el-input v-model="myQueryParamLead.topicName" placeholder="课题名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="myQueryParamLead.keywords" placeholder="课题关键字" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getLeadList()">查询</el-button>
            <el-button v-if="isAuth('qcSubject:registration:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button>
            <el-button v-if="isAuth('qcSubject:registration:save')" type="warning" @click="reuseHandle()"
              :disabled="dataListSelections.length != 1">课题重用</el-button>
            <el-button type="danger" @click="toIssue()">问题添加</el-button>
            <!-- <el-badge :value="superScriptNumber" class="item">
              <el-button type="warning" @click="dialogMessageVisible = true">消息详情</el-button>
            </el-badge> -->
          </el-form-item>
        </el-form>
        <el-table :data="subjectLeadList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%" stripe highlight-current-row>
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" fixed>
          </el-table-column>
          <el-table-column prop="topicReviewStatus" label="课题审核状态" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.topicReviewStatus === 0" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.topicReviewStatus === 1" style="color: gray;">未开始</span>
              <span v-else-if="scope.row.topicReviewStatus === 2" style="color: #3f9ccb;">审核中</span>
              <span v-else-if="scope.row.topicReviewStatus === 3" style="color: #8dc146;">已通过</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
          </el-table-column>
          <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column>
          <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
          </el-table-column>
          <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
          </el-table-column>
          <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述/摘要" width="150">
          </el-table-column>
          <el-table-column prop="topicType" header-align="center" align="center" label="课题类型" width="160">
          </el-table-column>
          <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
          </el-table-column>
          <el-table-column prop="activityPlan" header-align="center" align="center" label="活动计划开始日期" width="100">
          </el-table-column>
          <el-table-column prop="activityPlanEnd" header-align="center" align="center" label="活动计划结束日期" width="100">
          </el-table-column>
          <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字">
          </el-table-column>
          <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态" width="120">
            <template slot-scope="scope">
              <span>{{
                toStatus(scope.row.topicActivityStatus, scope.row.topicType)
                }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
          </el-table-column>
          <el-table-column prop="resultType" header-align="center" align="center" label="提交类型">
          </el-table-column>
          <el-table-column prop="note" header-align="center" align="center" label="备注">
          </el-table-column>

          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.qcsrId)">修改</el-button>
              <el-button type="text" size="small" @click="deleteHandle(scope.row.qcsrId)">删除</el-button>
              <el-button type="text" size="small" @click="checkHandle(scope.row.qcsrId)">提交审核</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandleLead" @current-change="currentChangeHandleLead"
          :current-page="pageIndexLead" :page-sizes="[10, 20, 50, 100]" :page-size="pageSizeLead" :total="totalPageLead"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <!-- <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshLeadList="getLeadList()"></add-or-update> -->
      </div>
    </el-tab-pane>
    <el-tab-pane label="我参与的课题" name="3">
      <div class="mod-config">
        <el-form :inline="true" :model="myQueryParamJoin" @keyup.enter.native="getJoinList()">
          <el-form-item>
            <el-input v-model="myQueryParamJoin.topicName" placeholder="课题名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="myQueryParamJoin.keywords" placeholder="课题关键字" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getJoinList()">查询</el-button>
            <el-button v-if="isAuth('qcSubject:registration:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button>
            <el-button v-if="isAuth('qcSubject:registration:save')" type="warning" @click="reuseHandle()"
              :disabled="dataListSelections.length != 1">课题重用</el-button>
            <el-button type="danger" @click="toIssue()">问题添加</el-button>

          </el-form-item>
        </el-form>
        <el-table :data="subjectJoinList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%" stripe highlight-current-row>
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" fixed>
          </el-table-column>
          <el-table-column prop="topicReviewStatus" label="课题审核状态" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.topicReviewStatus === 0" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.topicReviewStatus === 1" style="color: gray;">未开始</span>
              <span v-else-if="scope.row.topicReviewStatus === 2" style="color: #3f9ccb;">审核中</span>
              <span v-else-if="scope.row.topicReviewStatus === 3" style="color: #8dc146;">已通过</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
          </el-table-column>
          <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column>
          <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
          </el-table-column>
          <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
          </el-table-column>
          <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述/摘要" width="150">
          </el-table-column>
          <el-table-column prop="topicType" header-align="center" align="center" label="课题类型" width="160">
          </el-table-column>
          <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
          </el-table-column>
          <el-table-column prop="activityPlan" header-align="center" align="center" label="活动计划开始日期" width="100">
          </el-table-column>
          <el-table-column prop="activityPlanEnd" header-align="center" align="center" label="活动计划结束日期" width="100">
          </el-table-column>
          <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字">
          </el-table-column>
          <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态" width="120">
            <template slot-scope="scope">
              <span>{{
                toStatus(scope.row.topicActivityStatus, scope.row.topicType)
                }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
          </el-table-column>
          <el-table-column prop="resultType" header-align="center" align="center" label="提交类型">
          </el-table-column>
          <el-table-column prop="note" header-align="center" align="center" label="备注">
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.qcsrId)">修改</el-button>
              <el-button type="text" size="small" @click="deleteHandle(scope.row.qcsrId)">删除</el-button>
              <el-button type="text" size="small" @click="checkHandle(scope.row.qcsrId)">提交审核</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandleJoin" @current-change="currentChangeHandleJoin"
          :current-page="pageIndexJoin" :page-sizes="[10, 20, 50, 100]" :page-size="pageSizeJoin" :total="totalPageJoin"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

      </div>
    </el-tab-pane>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList()"
      @refreshJoinList="getJoinList()" @refreshLeadList="getLeadList()"></add-or-update>
  </el-tabs>
</template>

<script>
import AddOrUpdate from './qcSubjectRegistration-add-or-update'
export default {
  name: 'qcSubjectRegistration',
  data() {
    return {
      activeName: '2',
      dataForm: {
        key: ''
      },
      dataList: [],

      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,

      pageIndexLead: 1,
      pageSizeLead: 10,
      totalPageLead: 0,

      pageIndexJoin: 1,
      pageSizeJoin: 10,
      totalPageJoin: 0,

      dataListLoading: false,
      dataListSelections: [],
      subjectDataList: [],
      subjectLeadList: [],
      subjectJoinList: [],
      membersOptions: [],
      addOrUpdateVisible: false,
      groupMemberList: [],
      myQueryParam: {
        topicName: '',
        keywords: '',
      },
      myQueryParamLead: {
        topicName: '',
        keywords: '',
      },
      myQueryParamJoin: {
        topicName: '',
        keywords: '',
      },
      reuseStepId: ''
    }
  },
  components: {
    AddOrUpdate
  },
  async activated() {
    this.getDataList()
    this.getJoinList();
    this.getLeadList();
    this.getGroupMemberData().then(groupList => {
      this.groupMemberList = Object.values(groupList).filter(item => item.examineStatus === "通过").reduce((acc, item) => {
        acc[item.id] = item;
        return acc;
      }, {});
    });
    // 获取分组后的员工数据
    await this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
      method: 'get',
    }).then(({ data }) => {
      this.membersOptions = data;
    });
  },
  methods: {

    parseTime(time) {
      return new Date(time).toLocaleString();
    },
    // 获取我参与课题数据列表
    async getJoinList() {
      this.dataListLoading = true;
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/myList"),
        method: "get",
        params: this.$http.adornParams({
          'page': this.pageIndexJoin,
          'limit': this.pageSizeJoin,
          'key': this.myQueryParamJoin,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.subjectJoinList = data.page.list;
          // this.dataList = resultList
          this.totalPageJoin = data.page.totalCount;
        } else {
          this.subjectJoinList = [];
          this.totalPageSubject = 0;
        }

        this.dataListLoading = false;
      });
    },
    // 获取我创办课题数据列表
    async getLeadList() {
      this.dataListLoading = true;
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/leadList"),
        method: "get",
        params: this.$http.adornParams({
          'page': this.pageIndexLead,
          'limit': this.pageSizeLead,
          'key': this.myQueryParamLead,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.subjectLeadList = data.page.list;
          // this.dataList = resultList
          this.totalPageLead = data.page.totalCount;
        } else {
          this.subjectLeadList = [];
          this.totalPageSubject = 0;
        }

        this.dataListLoading = false;
      });
    },

    // 获取数据列表
    async getDataList() {
      this.dataListLoading = true
      await this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.myQueryParam,
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
    sizeChangeHandleLead(val) {
      this.pageSizeLead = val
      this.pageIndexLead = 1
      this.getLeadList()
    },
    sizeChangeHandleJoin(val) {
      this.pageSizeJoin = val
      this.pageIndexJoin = 1
      this.getJoinList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    currentChangeHandleJoin(val) {
      this.pageIndexJoin = val
      this.getJoinList()
    },
    currentChangeHandleLead(val) {
      this.pageIndexLead = val
      this.getLeadList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.membersOptions = this.membersOptions;
        this.$refs.addOrUpdate.groupMemberList = this.groupMemberList
        this.$refs.addOrUpdate.updateOptions()
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
              this.getDataList();
              this.visible = false
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
            this.groupMemberList = data.page.list
            this.groupMemberList.filter(item => {
              item.examineStatus == '通过'
            });
            // this.totalPage = data.page.totalCount;
            groupList = this.groupMemberList;
            // 分组
            const map = {};
            groupList.forEach(item => {
              if (item.parentId === null) {
                map[item.qcgmId] = {
                  id: item.qcgmId,
                  date: item.participationDate,
                  name: item.memberName,
                  number: item.number,
                  groupName: item.groupName,
                  roleInTopic: '组长',
                  examineStatus: item.examineStatus,
                  children: []
                };
              }
            });
            groupList.forEach(item => {
              if (item.parentId !== null && map[item.parentId]) {
                map[item.parentId].children.push({
                  id: item.qcgmId,
                  date: item.participationDate,
                  name: item.memberName,
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
    toStatus(num, type) {
      if (type == '创新型') {
        if (num == '1') {
          return '设定目标'
        } else if (num == '2') {
          return '提出方案确定最佳方案'
        } else if (num == '3') {
          return '制定对策'
        } else if (num == '4') {
          return '对策实施'
        } else if (num == '5') {
          return '检查效果'
        } else if (num == '6') {
          return '标准化'
        } else if (num == '7') {
          return '总结和下一步打算'
        } else if (num == '8') {
          return '完成'
        } else {
          return '选择课题'
        }
      } else if (type === '问题解决型') {
        if (num === '1') {
          return '现状调查'
        } else if (num === '2') {
          return '设定目标'
        } else if (num === '3') {
          return '原因分析'
        } else if (num === '4') {
          return '要因确定'
        } else if (num === '5') {
          return '制定对策'
        } else if (num === '6') {
          return '对策实施'
        } else if (num === '7') {
          return '检查效果'
        } else if (num === '8') {
          return '巩固措施'
        } else if (num === '9') {
          return '总结和下一步打算'
        } else if (num === '10') {
          return '完成'
        } else {
          return '选择课题'
        }
      } else if (type === '问题解决型(指令型)') {
        if (num === '1') {
          return '设定目标'
        } else if (num === '2') {
          return '可靠性分析'
        } else if (num === '3') {
          return '原因分析'
        } else if (num === '4') {
          return '要因确定'
        } else if (num === '5') {
          return '制定对策'
        } else if (num === '6') {
          return '对策实施'
        } else if (num === '7') {
          return '检查效果'
        } else if (num === '8') {
          return '巩固措施'
        } else if (num === '9') {
          return '总结和下一步打算'
        } else if (num === '10') {
          return '完成'
        } else {
          return '选择课题'
        }
      } else {
        return '-'
      }
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
<style scoped>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>
