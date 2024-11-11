<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="我的课题" name="1">
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
            <!-- <el-button v-if="isAuth('qcSubject:registration:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button> -->
            <!-- <el-button v-if="isAuth('qcSubject:registration:save')" type="warning" @click="reuseHandle()"
              :disabled="dataListSelections.length != 1">课题重用</el-button> -->
            <el-button type="danger" @click="toIssue()">问题添加</el-button>
            <el-badge :value="superScriptNumber" class="item">
              <el-button type="warning" @click="dialogMessageVisible = true">消息详情</el-button>
            </el-badge>
          </el-form-item>
        </el-form>
        <el-table :data="subjectDataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%" stripe>
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" fixed>
          </el-table-column>
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
          </el-table-column>
          <!-- <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column> -->
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
          <!-- <el-table-column prop="topicReviewStatus" label="课题审核状态" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.topicReviewStatus === 0" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.topicReviewStatus === 1" style="color: gray;">未开始</span>
              <span v-else-if="scope.row.topicReviewStatus === 2" style="color: #3f9ccb;">审核中</span>
              <span v-else-if="scope.row.topicReviewStatus === 3" style="color: #8dc146;">已通过</span>
              <span v-else>-</span>
            </template>
          </el-table-column> -->
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" v-if="isAuth('qcPlan:step:list')"
                @click="newPlanHandle(scope.row.qcsrId)">关联计划</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcSubject:plan:submit')"
                @click="addOrUpdateHandle(scope.row.qcsrId)">提交计划</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')"
                @click="examineStatus(scope.row.qcsrId, scope.row.resultType)">审核状态</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPageSubject"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getSubjectList"></add-or-update>
      </div>
    </el-tab-pane>
    <el-tab-pane label="我的小组" name="2">
      <div class="mod-config">
        <!-- <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()"> -->
        <!-- <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item> -->
        <!-- <el-form-item>
            <el-button @click="getDataList()">查询</el-button>
            <el-button v-if="isAuth('qcMembers:qcGroupMember:save')" type="primary"
              @click="addOrUpdateHandle()">新增小组</el-button>
            <el-button type="danger" @click="toIssue()">问题添加</el-button>
          </el-form-item> -->
        <!-- </el-form> -->
        <el-table :data="dataList" stripe border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%" row-key="id">
          <el-table-column header-align="center" align="center" label="" width="40">
          </el-table-column>
          <el-table-column prop="groupName" header-align="center" align="center" label="小组名">
          </el-table-column>
          <el-table-column prop="name" header-align="center" align="center" label="姓名">
          </el-table-column>
          <el-table-column prop="number" header-align="center" align="center" label="员工编号">
          </el-table-column>
          <el-table-column prop="roleInTopic" header-align="center" align="center" label="组内角色">
          </el-table-column>
          <el-table-column prop="department" header-align="center" align="center" label="单位">
          </el-table-column>
          <el-table-column prop="team" header-align="center" align="center" label="小组类型">
          </el-table-column>
          <el-table-column prop="registrationNum" header-align="center" align="center" label="注册号">
          </el-table-column>
          <el-table-column prop="date" header-align="center" align="center" label="加入小组时间">
          </el-table-column>
          <!-- <el-table-column fixed="right" v-if="isAuth('qcMembers:qcGroupMember:save')" header-align="center"
            align="center" label="操作" width="160">
            <template slot-scope="scope">
              <el-button v-if="(!scope.row.parentId && isAuth('qcMembers:qcGroupMember:save'))" type="text" size="small"
                @click="addMemberHandle(scope.row.id)">新增成员</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcMembers:qcGroupMember:save')"
                @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcMembers:qcGroupMember:save')"
                @click="deleteHandle(scope.row.id)">删除</el-button>
            </template>
          </el-table-column> -->
        </el-table>
        <!-- <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100, 999]" :page-size="999" :total="totalPageGroup"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination> -->
        <!-- 弹窗, 新增 / 修改
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update> -->
      </div>
    </el-tab-pane>
    <el-tab-pane label="我的审核" name="3">
      <el-table :data="messageList" stripe border v-loading="messageListLoading" style="width: 100%" row-key="id">
        <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" fixed>
        </el-table-column>
        <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号" fixed>
        </el-table-column>
        <!-- <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column> -->
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
        <el-table-column prop="resultType" header-align="center" align="center" label="提交类型">
        </el-table-column>
        <el-table-column prop="note" header-align="center" align="center" label="备注">
        </el-table-column>
        <!-- <el-table-column prop="topicReviewStatus" label="课题审核状态" header-align="center" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.topicReviewStatus === 0" style="color: #f43628">未通过</span>
            <span v-else-if="scope.row.topicReviewStatus === 1" style="color: gray">未开始</span>
            <span v-else-if="scope.row.topicReviewStatus === 2" style="color: #3f9ccb">审核中</span>
            <span v-else-if="scope.row.topicReviewStatus === 3" style="color: #8dc146">已通过</span>
            <span v-else>-</span>
          </template>
        </el-table-column> -->
        <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" v-if="isAuth('qcPlan:step:list')"
              @click="messagePlanHandle(scope.row.qcsrId)">关联计划</el-button>
            <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')" @click="
              messageExamineStatus(scope.row.qcsrId, scope.row.resultType)
              ">审核状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-dialog title="消息详情" :visible.sync="dialogMessageVisible">
      <el-table :data="messageList" stripe border v-loading="messageListLoading" style="width: 100%" row-key="id">
        <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" fixed>
        </el-table-column>
        <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号" fixed>
        </el-table-column>
        <!-- <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column> -->
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
        <el-table-column prop="resultType" header-align="center" align="center" label="提交类型">
        </el-table-column>
        <el-table-column prop="note" header-align="center" align="center" label="备注">
        </el-table-column>
        <!-- <el-table-column prop="topicReviewStatus" label="课题审核状态" header-align="center" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.topicReviewStatus === 0" style="color: #f43628">未通过</span>
            <span v-else-if="scope.row.topicReviewStatus === 1" style="color: gray">未开始</span>
            <span v-else-if="scope.row.topicReviewStatus === 2" style="color: #3f9ccb">审核中</span>
            <span v-else-if="scope.row.topicReviewStatus === 3" style="color: #8dc146">已通过</span>
            <span v-else>-</span>
          </template>
        </el-table-column> -->
        <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" v-if="isAuth('qcPlan:step:list')"
              @click="messagePlanHandle(scope.row.qcsrId)">关联计划</el-button>
            <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')" @click="
              messageExamineStatus(scope.row.qcsrId, scope.row.resultType)
              ">审核状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </el-tabs>
</template>

<script>
// import AddOrUpdate from './qcSubjectRegistration-add-or-update'
import AddOrUpdate from "../qcSubjectManagement/plan/qcPlan-add-or-update";
export default {
  data() {
    return {
      activeName: "1",
      dialogMessageVisible: false,
      messageListLoading: false,
      dataForm: {
        key: "",
      },
      subjectDataList: [], //标签页1，我的课题
      dataList: [], //标签页2，我的小组
      examineList: [], //标签页3，我的审核
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      totalPageGroup: 0,
      totalPageSubject: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      groupMemberList: [],
      myQueryParam: {
        topicName: "",
        keywords: "",
      },
      reuseStepId: "",
      superScriptNumber: "", //消息详情角标
      messageList: [], //消息详情角标
      notifyOffset: 0,
    };
  },
  components: {
    AddOrUpdate,
  },
  activated() {
    this.getGroupList();
    this.getSubjectList();
    this.handleTip();
    // this.getGroupList().then(groupList => {
    //   this.groupMemberList = groupList;
    // });
    // console.log('+++++++++++++++++++++++++++++++')
    // console.log(this.isAuth('qcExamine:interested:technology'))
    // console.log('+++++++++++++++++++++++++++++++')
  },
  methods: {
    showNotification(keyword, offsetAdjustment = 65) {
      // 根据传递进来的参数计算新的偏移量
      const newOffset = this.notifyOffset + offsetAdjustment;
      // 显示通知
      this.$notify({
        dangerouslyUseHTMLString: true,
        message: `<span>您有课题</span><span style="color: #2d7ad6;">[${keyword}]</span><span>审核中，请及时处理</span>`,
        type: "warning",
        offset: newOffset,
        duration: 7000,
      });
      // 更新偏移量，避免堆叠
      this.notifyOffset = newOffset;
    },

    // handleClick() {
    //   console.log(this.activeName)
    //   if (this.activeName == '1') {
    //     this.getSubjectDataList()
    //   } else if (this.activeName == '2') {
    //     this.getGroupDataList()

    //   }
    // },

    parseTime(time) {
      return new Date(time).toLocaleString();
    },
    // 获取小组数据列表
    async getGroupList() {
      this.dataListLoading = true;
      await this.$http({
        url: this.$http.adornUrl("/qcMembers/qcGroupMember/myList"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          // 'key': 1,
          // 'reuseStepId': 5
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          const sameList = [];
          console.log(data.userName);
          const tmpList = data.page.list;
          try {
            data.page.list.forEach((item) => {
              if (item.name === data.userName) {
                if (item.parentId) {
                  tmpList.some((row) => {
                    if (
                      row.examineStatus === "通过" &&
                      item.parentId === row.qcgmId
                    ) {
                      sameList.push(item);
                      return true; // 找到匹配项
                    }
                  });
                } else {
                  if (item.examineStatus === "通过") {
                    sameList.push(item);
                  }
                }
              }
            });
          } catch (error) {
            console.log(error);
          }

          // console.log()
          console.log(sameList);
          const resultList = [];
          sameList.forEach((item) => {
            if (item.parentId) {
              data.page.list.forEach((row) => {
                if (row.parentId == item.parentId) {
                  resultList.push(row);
                } else if (row.qcgmId == item.parentId) {
                  resultList.push(row);
                }
              });
            } else {
              resultList.push(item);
              data.page.list.forEach((row) => {
                if (row.parentId == item.qcgmId) {
                  resultList.push(row);
                }
              });
            }
          });
          console.log(resultList);
          // 分组
          this.dataList = []; // 清空 dataList
          const map = {};
          resultList.forEach((item) => {
            if (item.parentId === null) {
              map[item.qcgmId] = {
                id: item.qcgmId,
                date: item.participationDate,
                name: item.name,
                number: item.number,
                groupName: item.groupName,
                roleInTopic: "组长",
                team: item.team,
                department: item.department,
                registrationNum: item.registrationNum,
                children: [],
              };
            }
          });
          resultList.forEach((item) => {
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
          console.log(map);
          this.dataList = Object.values(map);
          // this.dataList = resultList
          console.log(this.dataList);
          this.totalPageGroup = resultList.length;
        } else {
          this.dataList = [];
          this.totalPageGroup = 0;
        }

        this.dataListLoading = false;
      });
    },
    //消息提示
    async handleTip() {
      let registerList = [];
      let examineList = [];
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: 900000,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          registerList = data.page.list;
        } else {
          console.log("handleTip() error");
        }
      });
      await this.$http({
        url: this.$http.adornUrl("/qcManagement/examineStatus/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: 900000,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          examineList = data.page.list;
        } else {
          console.log("handleTip() error");
        }
      });
      let tipList = [];
      let tipSubjectList = [];
      examineList.forEach((item) => {
        if (item.qcExamineCurrent !== "完成") {
          if (item.qcExamineCurrent == "0" && this.isAuth("qcExamine:department:submit")) {
            tipList.push(item);
            this.showNotification("科室审核");
          }
          else if (item.qcExamineCurrent == "1" && this.isAuth("qcExamine:Achievement:recognition")) {
            tipList.push(item);
            this.showNotification("成果认定");
          } else if (item.qcExamineCurrent == "2") {
            registerList.forEach((row) => {
              if (row.qcsrId == item.qcExamineSubject) {
                if (row.resultType.includes("安全") && this.isAuth("qcExamine:interested:safety")) {
                  tipList.push(item);
                  this.showNotification("相关方审核");
                } else if (row.resultType.includes("设备") && this.isAuth("qcExamine:interested:instrument")) {
                  tipList.push(item);
                  this.showNotification("相关方审核");
                } else if (row.resultType.includes("质量") && this.isAuth("qcExamine:interested:quality")) {
                  tipList.push(item);
                  this.showNotification("相关方审核");
                } else if (row.resultType.includes("生产") && this.isAuth("qcExamine:interested:production")) {
                  tipList.push(item);
                  this.showNotification("相关方审核");
                } else if (row.resultType.includes("技术") && this.isAuth("qcExamine:interested:technology")) {
                  tipList.push(item);
                  this.showNotification("相关方审核");
                } else if (row.resultType.includes("其他") && this.isAuth("qcExamine:first:comment")) {
                  tipList.push(item);
                  this.showNotification("成果初评");
                }
              }
            });
          } else if (item.qcExamineCurrent == "3" && this.isAuth("qcExamine:first:comment")) {
            tipList.push(item);
            this.showNotification("成果初评");
          } else if ((item.qcExamineCurrent == "4" || item.qcExamineCurrent == "4.2") && this.isAuth("qcExamine:second:comment")) {
            tipList.push(item);
            console.log(item);
            this.showNotification("成果复评");
          } else if ((item.qcExamineCurrent == "4" || item.qcExamineCurrent == "4.1") && this.isAuth("qcExamine:second:comment")) {
            tipList.push(item);
            this.showNotification("财务部审核");
          } else if (item.qcExamineCurrent == "5" && this.isAuth("qcExamine:final:submit")) {
            tipList.push(item);
            this.showNotification("终评提交");
          }
        }
      });
      tipList.forEach((t) => {
        registerList.forEach((r) => {
          if (t.qcExamineSubject == r.qcsrId) {
            tipSubjectList.push(r);
          }
        });
      });
      this.superScriptNumber = tipList.length;
      this.messageList = tipSubjectList;
      console.log("++++++++++");
      console.log(tipSubjectList);
      console.log("++++++++++");
      console.log("----------");
      console.log(tipList);
      console.log("----------");
    },
    // 获取我的课题数据列表
    async getSubjectList() {
      this.dataListLoading = true;
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/myList"),
        // url: this.$http.adornUrl('/qcSubject/registration/myList'),
        // url: this.$http.adornUrl('/qcPlan/step/reuse'),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          // 'key': 1,
          // 'reuseStepId': 5
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.subjectDataList = data.page.list;
          // this.dataList = resultList
          this.totalPageSubject = data.page.totalCount;
        } else {
          this.subjectDataList = [];
          this.totalPageSubject = 0;
        }

        this.dataListLoading = false;
      });
    },

    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.groupMemberList = this.groupMemberList;
        this.$refs.addOrUpdate.init(id);
      });
    },
    toStatus(num, type) {
      // console.log(num)
      // console.log(type)
      if (type == "创新型") {
        if (num == "1") {
          return "设定目标";
        } else if (num == "2") {
          return "提出方案确定最佳方案";
        } else if (num == "3") {
          return "制定对策";
        } else if (num == "4") {
          return "对策实施";
        } else if (num == "5") {
          return "检查效果";
        } else if (num == "6") {
          return "标准化";
        } else if (num == "7") {
          return "总结和下一步打算";
        } else if (num == "8") {
          return "完成";
        } else {
          return "选择课题";
        }
      } else if (type === "问题解决型") {
        if (num === "1") {
          return "现状调查";
        } else if (num === "2") {
          return "设定目标";
        } else if (num === "3") {
          return "原因分析";
        } else if (num === "4") {
          return "要因确定";
        } else if (num === "5") {
          return "制定对策";
        } else if (num === "6") {
          return "对策实施";
        } else if (num === "7") {
          return "检查效果";
        } else if (num === "8") {
          return "巩固措施";
        } else if (num === "9") {
          return "总结和下一步打算";
        } else if (num === "10") {
          return "完成";
        } else {
          return "选择课题";
        }
      } else if (type === "问题解决型(指令型)") {
        if (num === "1") {
          return "设定目标";
        } else if (num === "2") {
          return "可靠性分析";
        } else if (num === "3") {
          return "原因分析";
        } else if (num === "4") {
          return "要因确定";
        } else if (num === "5") {
          return "制定对策";
        } else if (num === "6") {
          return "对策实施";
        } else if (num === "7") {
          return "检查效果";
        } else if (num === "8") {
          return "巩固措施";
        } else if (num === "9") {
          return "总结和下一步打算";
        } else if (num === "10") {
          return "完成";
        } else {
          return "选择课题";
        }
      } else {
        return "-";
      }
    },
    //审核
    checkHandle(id) {
      this.dataForm.qcsrId = id;
      this.dataForm.topicReviewStatus = 2;
      this.$http({
        url: this.$http.adornUrl(`/qcSubject/registration/update`),
        method: "post",
        data: this.$http.adornData({
          qcsrId: this.dataForm.qcsrId,
          topicReviewStatus: this.dataForm.topicReviewStatus,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: "提交审核成功",
            type: "success",
            duration: 1500,
            onClose: () => {
              this.visible = false;
              this.$emit("refreshDataList");
            },
          });
        } else {
          this.$message.error(data.msg);
        }
      });
      this.getDataList();
    },
    //获取成员小组数据
    async getGroupMemberData() {
      return new Promise((resolve, reject) => {
        let groupList = [];
        this.$http({
          url: this.$http.adornUrl("/qcMembers/qcGroupMember/list"),
          method: "get",
          params: this.$http.adornParams({
            page: 1,
            limit: 10000,
            key: "",
          }),
        })
          .then(({ data }) => {
            if (data && data.code === 0) {
              this.groupMemberList = data.page.list;
              // this.totalPage = data.page.totalCount;
              groupList = this.groupMemberList;
              // 分组
              const map = {};
              groupList.forEach((item) => {
                if (item.parentId === null) {
                  map[item.qcgmId] = {
                    id: item.qcgmId,
                    date: item.participationDate,
                    name: item.name,
                    number: item.number,
                    groupName: item.groupName,
                    roleInTopic: "管理员",
                    children: [],
                  };
                }
              });
              groupList.forEach((item) => {
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
              groupList = map;
              this.groupMemberList = groupList;
            } else {
              this.groupMemberList = [];
              this.totalPage = 0;
            }
            resolve(groupList);
          })
          .catch((error) => {
            console.error("Error fetching data:", error);
            // this.dataList = [];
            this.totalPage = 0;
            this.dataListLoading = false;
            reject(error);
          });
      });
    },
    toIssue() {
      this.$router.push({
        name: "otherToIssue",
        // query: {
        //   data: JSON.stringify(filteredArray)
        // }
      });
    },
    // 删除
    deleteHandle(id) {
      var ids = id
        ? [id]
        : this.dataListSelections.map((item) => {
          return item.qcsrId;
        });
      this.$confirm(
        `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        this.$http({
          url: this.$http.adornUrl("/qcSubject/registration/delete"),
          method: "post",
          data: this.$http.adornData(ids, false),
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.getDataList();
              },
            });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    },
    //创建计划跳转
    newPlanHandle(id) {
      let filteredArray = [];
      // 遍历原始数组
      for (let i = 0; i < this.subjectDataList.length; i++) {
        if (this.subjectDataList[i].qcsrId === id) {
          // 如果满足条件，将对象添加到新数组中
          filteredArray.push(this.subjectDataList[i]);
        }
      }
      this.$router.push({
        name: "qcPlanNew",
        query: {
          data: JSON.stringify(filteredArray),
        },
      });
    },
    //消息创建计划跳转
    messagePlanHandle(id) {
      this.dialogMessageVisible = false;
      let filteredArray = [];
      // 遍历原始数组
      for (let i = 0; i < this.messageList.length; i++) {
        if (this.messageList[i].qcsrId === id) {
          // 如果满足条件，将对象添加到新数组中
          filteredArray.push(this.messageList[i]);
        }
      }
      this.$router.push({
        name: "qcPlanNew",
        query: {
          data: JSON.stringify(filteredArray),
        },
      });
    },
    //计划审批跳转
    examineStatus(id, resultType) {
      console.log(resultType);
      console.log(id);
      if (resultType === null || resultType === "") {
        this.$message({
          message: "课题计划尚未提交",
          type: "warning",
          duration: 1500,
        });
      } else {
        this.dialogMessageVisible = false;
        let filteredArray = [];
        // 遍历原始数组
        for (let i = 0; i < this.subjectDataList.length; i++) {
          if (this.subjectDataList[i].qcsrId === id) {
            // 如果满足条件，将对象添加到新数组中
            filteredArray.push(this.subjectDataList[i]);
            console.log(filteredArray);
          }
        }
        this.$router.push({
          name: "qcExamineStatus",
          query: {
            data: JSON.stringify(filteredArray),
          },
        });
      }
    },
    //消息计划审批跳转
    messageExamineStatus(id, resultType) {
      console.log(resultType);
      console.log(id);
      if (resultType === null || resultType === "") {
        this.$message({
          message: "课题计划尚未提交",
          type: "warning",
          duration: 1500,
        });
      } else {
        this.dialogMessageVisible = false;
        let filteredArray = [];
        // 遍历原始数组
        for (let i = 0; i < this.messageList.length; i++) {
          if (this.messageList[i].qcsrId === id) {
            // 如果满足条件，将对象添加到新数组中
            filteredArray.push(this.messageList[i]);
            console.log(filteredArray);
          }
        }
        this.$router.push({
          name: "qcExamineStatus",
          query: {
            data: JSON.stringify(filteredArray),
          },
        });
      }
    },
  },
};
</script>
