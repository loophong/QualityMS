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
            <!-- <el-button v-if="isAuth('qcSubject:plan:submit')" type="primary"
          @click="newPlanHandle(dataListSelections[0].qcsrId)"
          :disabled="dataListSelections.length != 1">关联计划</el-button>
        <el-button v-if="isAuth('qcSubject:plan:submit')" type="primary"
          @click="addOrUpdateHandle(dataListSelections[0].qcsrId)"
          :disabled="dataListSelections.length != 1">提交计划</el-button>
        <el-button v-if="isAuth('qcSubject:plan:submit')" type="primary"
          @click="examineStatus(dataListSelections[0].qcsrId)"
          :disabled="dataListSelections.length != 1">审核状态</el-button> -->
            <el-button type="danger" @click="toIssue()">问题添加</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="filteredDataList" :row-class-name="tableRowClassName" border v-loading="dataListLoading"
          @selection-change="selectionChangeHandle" style="width: 100%;"
          :default-sort="{ prop: 'qcsrId', order: 'descending' }" highlight-current-row>
          <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column> -->
          <el-table-column prop="qcsrId" header-align="center" align="center" label="课题ID" fixed sortable width="100">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" width="160" fixed>
          </el-table-column>
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
          </el-table-column>
          <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column>
          <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
          </el-table-column>
          <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
          </el-table-column>
          <el-table-column prop="startDate" header-align="center" align="center" label="开始日期" width="120">
          </el-table-column>
          <el-table-column prop="endDate" header-align="center" align="center" label="结束日期" width="120">
          </el-table-column>
          <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述">
          </el-table-column>
          <el-table-column prop="topicType" header-align="center" align="center" label="课题类型" width="160">
          </el-table-column>
          <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
          </el-table-column>
          <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字" width="120">
          </el-table-column>
          <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态">
            <template slot-scope="scope">
              <span>{{ toStatus(scope.row.topicActivityStatus,
                scope.row.topicType) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
            <template slot-scope="scope">
              <span v-if="scope.row.topicActivityResult && 85 <= scope.row.topicActivityResult">一等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 75 <= scope.row.topicActivityResult < 85">二等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 65 <= scope.row.topicActivityResult < 75">三等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 55 <= scope.row.topicActivityResult < 65">四等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 45 <= scope.row.topicActivityResult < 55">鼓励奖</span>
              <span v-else>--</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>

          <el-table-column prop="resultType" header-align="center" align="center" label="提交类型">
          </el-table-column>
          <el-table-column prop="note" header-align="center" align="center" label="备注">
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" v-if="isAuth('qcPlan:step:list')"
                @click="newPlanHandle(scope.row.qcsrId)">关联计划</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcSubject:plan:submit')"
                @click="addOrUpdateHandle(scope.row.qcsrId, scope.row.topicActivityStatus, scope.row.topicType)">成果提交</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')"
                @click="examineStatus(scope.row.qcsrId)">审核状态</el-button>
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
        <el-table :data="filteredLeadList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%" stripe :default-sort="{ prop: 'qcsrId', order: 'descending' }" highlight-current-row>
          <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column> -->
          <el-table-column prop="qcsrId" header-align="center" align="center" label="课题ID" fixed sortable width="100">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" width="160" fixed>
          </el-table-column>
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
          </el-table-column>
          <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column>
          <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
          </el-table-column>
          <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
          </el-table-column>
          <el-table-column prop="startDate" header-align="center" align="center" label="开始日期" width="120">
          </el-table-column>
          <el-table-column prop="endDate" header-align="center" align="center" label="结束日期" width="120">
          </el-table-column>
          <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述">
          </el-table-column>
          <el-table-column prop="topicType" header-align="center" align="center" label="课题类型" width="160">
          </el-table-column>
          <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
          </el-table-column>
          <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字" width="120">
          </el-table-column>
          <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态">
            <template slot-scope="scope">
              <span>{{ toStatus(scope.row.topicActivityStatus,
                scope.row.topicType) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
            <template slot-scope="scope">
              <span v-if="scope.row.topicActivityResult && 85 <= scope.row.topicActivityResult">一等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 75 <= scope.row.topicActivityResult < 85">二等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 65 <= scope.row.topicActivityResult < 75">三等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 55 <= scope.row.topicActivityResult < 65">四等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 45 <= scope.row.topicActivityResult < 55">鼓励奖</span>
              <span v-else>--</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>

          <el-table-column prop="resultType" header-align="center" align="center" label="提交类型">
          </el-table-column>
          <el-table-column prop="note" header-align="center" align="center" label="备注">
          </el-table-column>

          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" v-if="isAuth('qcPlan:step:list')"
                @click="newPlanHandle(scope.row.qcsrId)">关联计划</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcSubject:plan:submit')"
                @click="addOrUpdateHandle(scope.row.qcsrId, scope.row.topicActivityStatus, scope.row.topicType)">成果提交</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')"
                @click="examineStatus(scope.row.qcsrId)">审核状态</el-button>
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
        <el-table :data="filteredJoinList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%" stripe :default-sort="{ prop: 'qcsrId', order: 'descending' }" highlight-current-row>
          <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column> -->
          <el-table-column prop="qcsrId" header-align="center" align="center" label="课题ID" fixed sortable width="100">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" width="160" fixed>
          </el-table-column>
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
          </el-table-column>
          <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column>
          <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
          </el-table-column>
          <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
          </el-table-column>
          <el-table-column prop="startDate" header-align="center" align="center" label="开始日期" width="120">
          </el-table-column>
          <el-table-column prop="endDate" header-align="center" align="center" label="结束日期" width="120">
          </el-table-column>
          <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述">
          </el-table-column>
          <el-table-column prop="topicType" header-align="center" align="center" label="课题类型" width="160">
          </el-table-column>
          <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
          </el-table-column>
          <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字" width="120">
          </el-table-column>
          <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态">
            <template slot-scope="scope">
              <span>{{ toStatus(scope.row.topicActivityStatus,
                scope.row.topicType) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
            <template slot-scope="scope">
              <span v-if="scope.row.topicActivityResult && 85 <= scope.row.topicActivityResult">一等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 75 <= scope.row.topicActivityResult < 85">二等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 65 <= scope.row.topicActivityResult < 75">三等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 55 <= scope.row.topicActivityResult < 65">四等奖</span>
              <span v-else-if="scope.row.topicActivityResult && 45 <= scope.row.topicActivityResult < 55">鼓励奖</span>
              <span v-else>--</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>

          <el-table-column prop="resultType" header-align="center" align="center" label="提交类型">
          </el-table-column>
          <el-table-column prop="note" header-align="center" align="center" label="备注">
          </el-table-column>

          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" v-if="isAuth('qcPlan:step:list')"
                @click="newPlanHandle(scope.row.qcsrId)">关联计划</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcSubject:plan:submit')"
                @click="addOrUpdateHandle(scope.row.qcsrId, scope.row.topicActivityStatus, scope.row.topicType)">成果提交</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')"
                @click="examineStatus(scope.row.qcsrId)">审核状态</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandleJoin" @current-change="currentChangeHandleJoin"
          :current-page="pageIndexJoin" :page-sizes="[10, 20, 50, 100]" :page-size="pageSizeJoin" :total="totalPageJoin"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

      </div>
    </el-tab-pane>
  </el-tabs>


</template>

<style scoped>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>

<script>
import AddOrUpdate from './qcPlan-add-or-update'
export default {
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

      subjectLeadList: [],
      subjectJoinList: [],
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
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
      }
    }
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList()
    this.getJoinList();
    this.getLeadList();
  },
  computed: {
    filteredDataList() {
      return this.dataList.filter(item => item.topicReviewStatus === 3 || item.topicReviewStatus === '3');
    },
    filteredJoinList() {
      return this.subjectJoinList.filter(item => item.topicReviewStatus === 3 || item.topicReviewStatus === '3');
    },
    filteredLeadList() {
      return this.subjectLeadList.filter(item => item.topicReviewStatus === 3 || item.topicReviewStatus === '3');
    }
  },
  methods: {
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex === 1) {
        return 'warning-row';
      } else if (rowIndex === 3) {
        return 'success-row';
      }
      return '';
    },
    //创建计划跳转
    newPlanHandle(id) {
      let qcsrId = id;
      // console.log("qcsrId======xht=======>"+qcsrId);
      let filteredArray = [];
      // 遍历原始数组
      for (let i = 0; i < this.dataList.length; i++) {
        if (this.dataList[i].qcsrId === id) {
          // 如果满足条件，将对象添加到新数组中
          filteredArray.push(this.dataList[i]);
        }
      }
      this.$router.push(
        {
          name: 'qcPlanNew',
          query: {
            data: JSON.stringify(filteredArray),
            qcsrId: qcsrId,
          }
        });
    },
    //计划审批跳转
    examineStatus(id, resultType) {
      console.log(resultType)
      console.log(id)
      if (resultType === null || resultType === '') {
        this.$message({
          message: '课题计划尚未提交',
          type: 'warning',
          duration: 1500
        })
      } else {
        let filteredArray = [];
        // 遍历原始数组
        for (let i = 0; i < this.dataList.length; i++) {
          if (this.dataList[i].qcsrId === id) {
            // 如果满足条件，将对象添加到新数组中
            filteredArray.push(this.dataList[i]);
            console.log(filteredArray)
          }
        }
        // console.log('+-+-+-+-++-+-+--+')
        // console.log(this.dataList)
        // console.log('+-+-+-+-++-+-+--+')
        this.$router.push(
          {
            name: 'qcExamineStatus',
            query: {
              data: JSON.stringify(filteredArray)
            }
          });
      }
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
          // 'topicName': this.myQueryParam.topicName,
          // 'keywords': this.myQueryParam.keywords
          'key': this.myQueryParamJoin,
          // 'reuseStepId': 5
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
          'page': 1,
          'limit': 1000,
          // 'topicName': this.myQueryParam.topicName,
          // 'keywords': this.myQueryParam.keywords
          'key': this.myQueryParamLead,
          // 'reuseStepId': 5
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


    toIssue() {
      this.$router.push(
        {
          name: 'otherToIssue',
          // query: {
          //   data: JSON.stringify(filteredArray)
          // }
        });
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
    parseTime(time) {
      return new Date(time).toLocaleString();
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
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
    currentChangeHandleLead(val) {
      this.pageIndexLead = val
      this.getLeadList()
    },
    currentChangeHandleJoin(val) {
      this.pageIndexJoin = val
      this.getJoinList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(id, status, type) {
      if (type == '创新型') {
        if (status != '8') {
          this.$message({
            message: '请先完成课题计划',
            type: 'warning',
            duration: 1500
          })
        } else {
          this.addOrUpdateVisible = true
          this.$nextTick(() => {
            this.$refs.addOrUpdate.init(id)
          })
        }
      } else {
        if (status != '10') {
          this.$message({
            message: '请先完成课题计划',
            type: 'warning',
            duration: 1500
          })
        } else {
          this.addOrUpdateVisible = true
          this.$nextTick(() => {
            this.$refs.addOrUpdate.init(id)
          })
        }
      }
      console.log(this.dataListSelections)
      // console.log(id)
      // this.addOrUpdateVisible = true
      // this.$nextTick(() => {
      //   this.$refs.addOrUpdate.init(id)
      // })
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
    }
  }
}
</script>
