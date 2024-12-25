<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="我的课题" name="1">
      <div class="mod-config">
        <el-form :inline="true" :model="myQueryParamSubject" @keyup.enter.native="getSubjectList()">
          <el-form-item>
            <el-input v-model="myQueryParamSubject.topicName" placeholder="课题名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="myQueryParamSubject.keywords" placeholder="课题关键字" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input style="width: 100px;" v-model="myQueryParamSubject.topicLeader" placeholder="组长"
              clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input style="width: 100px;" v-model="myQueryParamSubject.topicConsultant" placeholder="顾问"
              clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 120px;" v-model="myQueryParamSubject.topicReviewStatus" placeholder="审核状态"
              clearable>
              <el-option label="未开始" value="1"></el-option>
              <el-option label="审核中" value="2"></el-option>
              <el-option label="未通过" value="0"></el-option>
              <el-option label="已通过" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 120px;" v-model="myQueryParamSubject.topicDepartment" placeholder="请选择科室"
              clearable>
              <el-option label="生产科" value="生产科"></el-option>
              <el-option label="供应科" value="供应科"></el-option>
              <el-option label="市场科" value="市场科"></el-option>
              <el-option label="技术科" value="技术科"></el-option>
              <el-option label="质量科" value="质量科"></el-option>
              <el-option label="财务科" value="财务科"></el-option>
              <el-option label="安环设备科" value="安环设备科"></el-option>
              <el-option label="企业管理科" value="企业管理科"></el-option>
              <el-option label="党群办公室" value="党群办公室"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-date-picker style="width: 260px;" v-model="activityPlanSubject" type="daterange" range-separator="-"
              start-placeholder="计划开始日期" end-placeholder="" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-date-picker style="width: 260px;" v-model="activityPlanEndSubject" type="daterange" range-separator="-"
              start-placeholder="计划结束日期" end-placeholder="" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button @click="getSubjectList()">查询</el-button>
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
        <el-table :data="subjectDataList" border v-loading="dataListLoading" style="width: 100%" stripe>
          <el-table-column prop="qcsrId" header-align="center" align="center" label="课题ID" fixed>
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" fixed>
          </el-table-column>
          <el-table-column prop="topicDepartment" header-align="center" align="center" label="科室">
          </el-table-column>
          <el-table-column prop="topicReviewStatus" label="课题审核状态" header-align="center" align="center" width="120">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.topicReviewStatus === 0" type="danger">未通过</el-tag>
              <el-tag v-else-if="scope.row.topicReviewStatus === 1" type="info">未开始</el-tag>
              <el-tag v-else-if="scope.row.topicReviewStatus === 2 && !scope.row.topicReviewDepartment">审核中(科室)</el-tag>
              <el-tag
                v-else-if="scope.row.topicReviewStatus === 2 && scope.row.topicReviewDepartment == 1">审核中(管理员)</el-tag>
              <el-tag v-else-if="scope.row.topicReviewStatus === 3" type="success">已通过</el-tag>
              <el-tag v-else>-</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号" width="150">
          </el-table-column>
          <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
            <template slot-scope="scope">
              {{ numberToName(scope.row.topicLeader) }}
            </template>
          </el-table-column>
          <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
            <template slot-scope="scope">
              {{ numberToName(scope.row.topicConsultant) }}
            </template>
          </el-table-column>
          <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员" width="180">
            <template slot-scope="scope">
              {{ numberToNameArray(scope.row.teamNumberIds) }}
            </template>
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
          <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字" width="200">
          </el-table-column>
          <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态" width="120">
            <template slot-scope="scope">
              <span>{{
                toStatus(scope.row.topicActivityStatus, scope.row.topicType)
              }}</span>
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
          <el-table-column prop="note" header-align="center" align="center" label="备注" width="200">
          </el-table-column>
          <el-table-column prop="firstComment" header-align="center" align="center" label="课题初审意见" width="180">
          </el-table-column>
          <el-table-column prop="secondComment" header-align="center" align="center" label="管理员初审意见" width="180">
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <div v-if="scope.row.topicReviewStatus == '3'">
                <el-button type="text" size="small" v-if="(isAuth('qcPlan:step:list'))"
                  @click="messagePlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
                <el-button type="text" size="small" v-if="(isAuth('qcSubject:plan:submit'))"
                  @click="handleSubmitPlan(scope.row.qcsrId, scope.row.topicActivityStatus, scope.row.topicType)">成果提交</el-button>
                <el-button type="text" size="small" v-if="(isAuth('qcManagement:examineStatus:list'))"
                  @click="examineStatus(scope.row.qcsrId, scope.row.resultType)">审核状态</el-button>
              </div>
              <div v-else>
                <el-button type="text" size="small" @click="subjectEditHandle(scope.row.qcsrId)"
                  :disabled="!groupLead">修改</el-button>
                <!-- <el-button type="text" size="small" @click="deleteHandle(scope.row.qcsrId)"
                  :disabled="!groupLead">删除</el-button> -->
                <el-button type="text" size="small"
                  :disabled="((scope.row.topicReviewStatus == 3) || (scope.row.topicReviewStatus == 2)) || !groupLead"
                  @click="checkHandle(scope.row.qcsrId)">提交审核</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
          :current-page="pageIndexSubject" :page-sizes="[10, 20, 50, 100]" :page-size="pageSizeSubject"
          :total="totalPageSubject" layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <result-type v-if="resultTypeVisible" ref="resultType" @refreshDataList="getSubjectList"></result-type>
        <subject-edit v-if="subjectEditVisible" ref="subjectEdit" @refreshDataList="getSubjectList"></subject-edit>
      </div>
      <first-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getFirstList"></first-update>
    </el-tab-pane>
    <el-tab-pane label="我的小组" name="2">
      <div class="mod-config">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getGroupList()">
          <el-form-item>
            <el-input v-model="dataListQuery.groupName" placeholder="小组名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 120px;" v-model="dataListQuery.examineStatus" placeholder="审核状态" clearable>
              <el-option label="通过" value="通过"></el-option>
              <el-option label="未通过" value="未通过"></el-option>
              <el-option label="待审核" value="待审核"></el-option>
              <el-option label="待审核(科室)" value="待审核(科室)"></el-option>
              <el-option label="待审核(管理员)" value="待审核(管理员)"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 120px;" v-model="dataListQuery.department" placeholder="科室" clearable>
              <el-option label="生产科" value="生产科"></el-option>
              <el-option label="供应科" value="供应科"></el-option>
              <el-option label="市场科" value="市场科"></el-option>
              <el-option label="技术科" value="技术科"></el-option>
              <el-option label="质量科" value="质量科"></el-option>
              <el-option label="财务科" value="财务科"></el-option>
              <el-option label="安环设备科" value="安环设备科"></el-option>
              <el-option label="企业管理科" value="企业管理科"></el-option>
              <el-option label="党群办公室" value="党群办公室"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 100px;" v-model="dataListQuery.groupType" placeholder="类型" clearable>
              <el-option label="攻关" value="攻关"></el-option>
              <el-option label="现场" value="现场"></el-option>
              <el-option label="管理" value="管理"></el-option>
              <el-option label="服务" value="服务"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input style="width: 120px;" v-model="dataListQuery.consultant" placeholder="顾问" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-date-picker v-model="dataListQuery.buildTime" type="daterange" range-separator="-"
              start-placeholder="开始" end-placeholder="结束" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 120px;" v-model="dataListQuery.aboutMe" placeholder="关于我" clearable>
              <el-option label="我创办" value="我创办"></el-option>
              <el-option label="我参与" value="我参与"></el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item> -->
          <el-form-item>
            <el-button @click="upQuery()">查询</el-button>
            <!-- <el-button v-if="isAuth('qcMembers:qcGroupMember:save')" type="primary"
              @click="addOrUpdateHandle()">新增小组</el-button> -->
            <!-- <el-button type="danger" @click="toIssue()">问题添加</el-button> -->
          </el-form-item>
        </el-form>
        <el-table :data="dataListAfterQuery" stripe border v-loading="dataListLoading"
          @selection-change="selectionChangeHandle" style="width: 100%" row-key="id">
          <el-table-column header-align="center" align="center" label="" width="40">
          </el-table-column>
          <el-table-column prop="groupName" header-align="center" align="center" label="小组名">
          </el-table-column>
          <el-table-column prop="examineStatus" header-align="center" align="center" label="小组审核状态" width="140">
            <template slot-scope="scope">
              <el-tag v-if="(scope.row.examineStatus === '待审核') && scope.row.examineDepartment != '1'">待审核(科室)</el-tag>
              <el-tag
                v-else-if="(scope.row.examineStatus === '待审核') && scope.row.examineDepartment == '1'">待审核(管理员)</el-tag>
              <el-tag v-else-if="scope.row.examineStatus === '未通过' && scope.row.examineGroup === '0'"
                type="danger">未通过(管理员)</el-tag>
              <el-tag v-else-if="scope.row.examineStatus === '未通过'" type="danger">未通过(科室)</el-tag>
              <el-tag v-else-if="scope.row.examineStatus == '通过'" type="success">{{ scope.row.examineStatus
                }}</el-tag>
              <el-tag v-else type="info">-</el-tag> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="memberName" header-align="center" align="center" label="姓名">
            <template slot-scope="scope">
              {{ numberToName(scope.row.memberName) }}
            </template>
          </el-table-column>
          <!-- <el-table-column prop="number" header-align="center" align="center" label="员工编号">
          </el-table-column> -->
          <el-table-column prop="roleInTopic" header-align="center" align="center" label="组内角色">
          </el-table-column>
          <el-table-column prop="department" header-align="center" align="center" label="科室">
          </el-table-column>
          <el-table-column prop="team" header-align="center" align="center" label="小组类型">
          </el-table-column>
          <el-table-column prop="position" header-align="center" align="center" label="顾问">
            <template slot-scope="scope">
              {{ numberToName(scope.row.position) }}
            </template>
          </el-table-column>
          <el-table-column prop="registrationNum" header-align="center" align="center" label="注册号">
          </el-table-column>
          <el-table-column prop="date" header-align="center" align="center" label="创建小组时间">
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
    <el-tab-pane label="我指导的小组" name="3" v-if="isAuth('qc:group:consultant')">
      <div class="mod-config">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="upQueryConsultant()">
          <el-form-item>
            <el-input v-model="dataListQueryConsultant.groupName" placeholder="小组名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 120px;" v-model="dataListQueryConsultant.examineStatus" placeholder="审核状态"
              clearable>
              <el-option label="通过" value="通过"></el-option>
              <el-option label="未通过" value="未通过"></el-option>
              <el-option label="待审核" value="待审核"></el-option>
              <el-option label="待审核(科室)" value="待审核(科室)"></el-option>
              <el-option label="待审核(管理员)" value="待审核(管理员)"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 120px;" v-model="dataListQueryConsultant.department" placeholder="科室" clearable>
              <el-option label="生产科" value="生产科"></el-option>
              <el-option label="供应科" value="供应科"></el-option>
              <el-option label="市场科" value="市场科"></el-option>
              <el-option label="技术科" value="技术科"></el-option>
              <el-option label="质量科" value="质量科"></el-option>
              <el-option label="财务科" value="财务科"></el-option>
              <el-option label="安环设备科" value="安环设备科"></el-option>
              <el-option label="企业管理科" value="企业管理科"></el-option>
              <el-option label="党群办公室" value="党群办公室"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select style="width: 100px;" v-model="dataListQueryConsultant.groupType" placeholder="类型" clearable>
              <el-option label="攻关" value="攻关"></el-option>
              <el-option label="现场" value="现场"></el-option>
              <el-option label="管理" value="管理"></el-option>
              <el-option label="服务" value="服务"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input style="width: 120px;" v-model="dataListQueryConsultant.memberName" placeholder="组长"
              clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-date-picker v-model="dataListQueryConsultant.buildTime" type="daterange" range-separator="-"
              start-placeholder="开始" end-placeholder="结束" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <!-- <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item> -->
          <el-form-item>
            <el-button @click="upQueryConsultant()">查询</el-button>
            <!-- <el-button v-if="isAuth('qcMembers:qcGroupMember:save')" type="primary"
              @click="addOrUpdateHandle()">新增小组</el-button> -->
            <!-- <el-button type="danger" @click="toIssue()">问题添加</el-button> -->
          </el-form-item>
        </el-form>
        <el-table :data="dataListAfterQueryConsultant" stripe border v-loading="dataListLoading"
          @selection-change="selectionChangeHandle" style="width: 100%" row-key="id">
          <el-table-column header-align="center" align="center" label="" width="40">
          </el-table-column>
          <el-table-column prop="groupName" header-align="center" align="center" label="小组名">
          </el-table-column>
          <el-table-column prop="examineStatus" header-align="center" align="center" label="小组审核状态" width="140">
            <template slot-scope="scope">
              <el-tag v-if="(scope.row.examineStatus === '待审核') && scope.row.examineDepartment != '1'">待审核(科室)</el-tag>
              <el-tag
                v-else-if="(scope.row.examineStatus === '待审核') && scope.row.examineDepartment == '1'">待审核(管理员)</el-tag>
              <el-tag v-else-if="scope.row.examineStatus === '未通过' && scope.row.examineGroup === '0'"
                type="danger">未通过(管理员)</el-tag>
              <el-tag v-else-if="scope.row.examineStatus === '未通过'" type="danger">未通过(科室)</el-tag>
              <el-tag v-else-if="scope.row.examineStatus == '通过'" type="success">{{ scope.row.examineStatus
                }}</el-tag>
              <el-tag v-else type="info">-</el-tag> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="memberName" header-align="center" align="center" label="姓名">
            <template slot-scope="scope">
              {{ numberToName(scope.row.memberName) }}
            </template>
          </el-table-column>
          <!-- <el-table-column prop="number" header-align="center" align="center" label="员工编号">
          </el-table-column> -->
          <el-table-column prop="roleInTopic" header-align="center" align="center" label="组内角色">
          </el-table-column>
          <el-table-column prop="department" header-align="center" align="center" label="科室">
          </el-table-column>
          <el-table-column prop="team" header-align="center" align="center" label="小组类型">
          </el-table-column>
          <el-table-column prop="position" header-align="center" align="center" label="顾问">
            <template slot-scope="scope">
              {{ numberToName(scope.row.position) }}
            </template>
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
    <el-tab-pane label="我的审核" name="4">
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
              toStatus(scope.row.topicActivityStatus, scope.row.topicType) }}
            </span>
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
              @click="messagePlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
            <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')" @click="
              messageExamineStatus(scope.row.qcsrId, scope.row.resultType)
              ">审核状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="课题初审" name="5" v-if="isAuth('qc:first:examine')">
      <div class="mod-config">
        <el-form :inline="true" :model="dataForm">
          <!-- <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item> -->
          <el-form-item>
            <!-- <el-button @click="getFirstList()">查询</el-button> -->
            <el-button type="danger" @click="toIssue()">问题添加</el-button>
            <!-- <el-button v-if="isAuth('qcSubject:registration:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button> -->
            <!-- <el-button v-if="isAuth('qcSubject:registration:delete')" type="danger" @click="deleteHandle()"
              :disabled="dataListSelections.length <= 0">批量删除</el-button> -->
          </el-form-item>
        </el-form>
        <el-table :data="filteredDataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%;">
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="qcsrId" header-align="center" align="center" label="id" width="60">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称">
          </el-table-column>
          <el-table-column prop="topicDepartment" header-align="center" align="center" label="科室">
          </el-table-column>
          <el-table-column prop="topicReviewStatus" label="课题审核状态" header-align="center" align="center" width="120">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.topicReviewStatus === 0" type="danger">未通过</el-tag>
              <el-tag v-else-if="scope.row.topicReviewStatus === 1" type="info">未开始</el-tag>
              <el-tag
                v-else-if="scope.row.topicReviewStatus === 2 && scope.row.topicReviewDepartment != 1">审核中(科室)</el-tag>
              <el-tag
                v-else-if="scope.row.topicReviewStatus === 2 && scope.row.topicReviewDepartment == 1">审核中(管理员)</el-tag>
              <el-tag v-else-if="scope.row.topicReviewStatus === 3" type="success">已通过</el-tag>
              <el-tag v-else>-</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
          </el-table-column>
          <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
            <template slot-scope="scope">
              {{ numberToName(scope.row.topicLeader) }}
            </template>
          </el-table-column>
          <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
            <template slot-scope="scope">
              {{ numberToName(scope.row.topicConsultant) }}
            </template>
          </el-table-column>
          <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
            <template slot-scope="scope">
              {{ numberToNameArray(scope.row.teamNumberIds) }}
            </template>
          </el-table-column>
          <el-table-column prop="startDate" header-align="center" align="center" label="开始日期">
          </el-table-column>
          <el-table-column prop="endDate" header-align="center" align="center" label="结束日期">
          </el-table-column>
          <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述/摘要">
          </el-table-column>
          <el-table-column prop="topicType" header-align="center" align="center" label="课题类型">
          </el-table-column>
          <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
          </el-table-column>
          <el-table-column prop="activityPlan" header-align="center" align="center" label="活动计划">
          </el-table-column>
          <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字tag">
          </el-table-column>
          <!-- <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态">
          </el-table-column> -->
          <!-- <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
          </el-table-column> -->
          <!-- <el-table-column prop="deleteFlag" header-align="center" align="center" label="删除标记位">
            </el-table-column> -->
          <el-table-column prop="note" header-align="center" align="center" label="备注">
          </el-table-column>
          <el-table-column prop="firstComment" header-align="center" align="center" label="科室初审意见">
          </el-table-column>

          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small"
                :disabled="!checkIfAdmit(scope.row.topicDepartment, scope.row.topicReviewStatus, scope.row.topicReviewDepartment)"
                @click="addOrUpdateHandle(scope.row.qcsrId)">处理审核</el-button>
              <!-- <el-button type="text" size="small" @click="deleteHandle(scope.row.qcsrId)">删除</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="100" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <first-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getFirstList"></first-update>
      </div>
    </el-tab-pane>
    <el-tab-pane label="成果科室批准" name="6" v-if="isAuth('qcExamine:department:submit')">
      <el-table :data="departmentExamineList" stripe border v-loading="messageListLoading" style="width: 100%"
        row-key="id">
        <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" fixed>
        </el-table-column>
        <el-table-column prop="topicDepartment" header-align="center" align="center" label="科室" fixed>
        </el-table-column>
        <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号" fixed>
        </el-table-column>
        <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
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
              toStatus(scope.row.topicActivityStatus, scope.row.topicType) }}
            </span>
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
              @click="messagePlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
            <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')" @click="
              messageExamineStatus(scope.row.qcsrId, scope.row.resultType)
              ">审核状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="成果确认" name="7" v-if="isAuth('qcExamine:Achievement:recognition')">
      <el-table :data="recognitionExamineList" stripe border v-loading="messageListLoading" style="width: 100%"
        row-key="id">
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
              toStatus(scope.row.topicActivityStatus, scope.row.topicType) }}
            </span>
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
              @click="messagePlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
            <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')" @click="
              messageExamineStatus(scope.row.qcsrId, scope.row.resultType)
              ">审核状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="初评课题" name="8" v-if="isAuth('qcExamine:first:comment')">
      <el-table :data="firstExamineList" stripe border v-loading="messageListLoading" style="width: 100%" row-key="id">
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
              toStatus(scope.row.topicActivityStatus, scope.row.topicType) }}
            </span>
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
              @click="messagePlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
            <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')" @click="
              messageExamineStatus(scope.row.qcsrId, scope.row.resultType)
              ">审核状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="经济效果确认" name="9" v-if="isAuth('qcExamine:finance:department')">
      <el-table :data="financialExamineList" stripe border v-loading="messageListLoading" style="width: 100%"
        row-key="id">
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
              toStatus(scope.row.topicActivityStatus, scope.row.topicType) }}
            </span>
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
              @click="messagePlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
            <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')" @click="
              messageExamineStatus(scope.row.qcsrId, scope.row.resultType)
              ">审核状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="成果复评" name="10" v-if="isAuth('qcExamine:second:comment')">
      <el-table :data="secondExamineList" stripe border v-loading="messageListLoading" style="width: 100%" row-key="id">
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
              toStatus(scope.row.topicActivityStatus, scope.row.topicType) }}
            </span>
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
              @click="messagePlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
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
              @click="messagePlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
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
import resultType from "../qcSubjectManagement/plan/qcPlan-add-or-update";
import moment from "moment";
import firstUpdate from "../qcSubjectManagement/examine/examine-add-or-update";
import subjectEdit from "../qcSubjectManagement/registration/qcSubjectRegistration-add-or-update";
import { group } from "d3";
import { update } from "lodash";
export default {
  name: "mine",
  data() {
    return {
      membersOptions: [], //分组后的员工数据
      activeName: "1",
      dialogMessageVisible: false,
      messageListLoading: false,
      dataForm: {
        key: "",
      },
      dataListAfterQuery: [],
      dataListQuery: {
        groupName: '',
        department: '',
        examineStatus: '',
        buildTime: [],
        consultant: '',
        aboutMe: '',
        groupType: '',
      },
      dataListQueryConsultant: {
        groupName: '',
        department: '',
        examineStatus: '',
        buildTime: [],
        consultant: '',
        memberName: '',
        groupType: '',
      },
      dataListAfterQueryConsultant: [],
      subjectDataList: [], //标签页1，我的课题
      dataList: [], //标签页2，我的小组
      consultantList: [], //标签页，我指导的小组
      examineList: [], //标签页3，我的审核
      pageIndexSubject: 1,
      pageIndex: 1,
      pageSize: 10,
      pageSizeSubject: 10,
      totalPage: 0,
      totalPageGroup: 0,
      groupLead: false, //是否是组长
      totalPageSubject: 0,
      dataListLoading: false,
      dataListSelections: [],
      resultTypeVisible: false,
      subjectEditVisible: false,
      addOrUpdateVisible: false,
      groupMemberList: [],
      activityPlanSubject: [],
      activityPlanEndSubject: [],
      departmentExamineList: [], // 部门审核列表
      recognitionExamineList: [],
      firstExamineList: [], // 初评列表
      secondExamineList: [], // 复评列表
      financialExamineList: [], // 财务审核列表
      firstList: [],
      myQueryParamSubject: {
        topicName: '',
        keywords: '',
        topicLeader: '',
        topicConsultant: '',
        topicReviewStatus: '',
        topicDepartment: '',
        activityPlan: '',
        activityPlanEnd: '',
      },
      currentUserName: '',
      reuseStepId: "",
      superScriptNumber: "", //消息详情角标
      messageList: [], //消息详情角标
      notifyOffset: 0,
    };
  },
  components: {
    resultType,
    firstUpdate,
    subjectEdit,
  },
  computed: {
    filteredDataList() {
      this.totalPage = this.firstList.filter(item => item.topicReviewStatus === 2).length
      // console.log('***************')
      // console.log(this.firstList.filter(item => item.topicReviewStatus === 2))
      return this.firstList.filter(item => item.topicReviewStatus === 2);
    }
  },
  async activated() {
    this.getGroupList();
    this.getConsultantList();
    this.getSubjectList();
    this.getFirstList();
    this.handleTip();
    this.ifGroupLead();
    await this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
      method: 'get',
    }).then(({ data }) => {
      this.membersOptions = data.map(o => {
        return {
          ...o, // 复制原对象属性
          options: o.options.map(e => {
            const match = e.label.match(/\(([^)]+)\)/);
            const number = e.label.replace(`${match ? match[0] : ''}`, '');
            return {
              ...e, // 复制原选项属性
              name: match ? match[1] : e.name || '', // 如果匹配到，使用匹配的结果；否则保持原名或为空字符串
              number: number ? number : e.name || '' // 如果匹配到，使用匹配的结果；否则保持原名或为空字符串
            };
          })
        };
      });
      // console.log(this.membersOptions)
    });
    await this.$http({
      url: this.$http.adornUrl("/qcSubject/registration/user"),
      method: "get",
      params: this.$http.adornParams({
      }),
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.currentUserName = data.userName;
      } else {
      }
    });
  },
  methods: {
    async ifGroupLead() {
      this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/ifGroupLead'),
        method: 'get',
        params: this.$http.adornParams({
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.groupLead = data.ifLead
        }
      });
    },
    numberToNameArray(numbers) {
      if (Array.isArray(numbers)) {
        let result = numbers.map(number => this.numberToName(number));
        return `${result}`
      } else {
        return numbers
      }
    },
    numberToName(number) {
      var result = ''
      this.membersOptions.forEach(o => {
        o.options.map(e => {
          if (e.name == number) {
            result = e.label.replace(/\(.*?\)/, '')
          }
        })
      });
      return result
    },

    checkIfAdmit(department, examineStatus, examineDepartment) {
      // console.log(examineStatus + '/////' + examineDepartment);
      if (examineStatus == '2' && examineDepartment != '1') {
        if ((department == '生产科' && this.isAuth('department:product:leader'))) {
          return true;
        } else if ((department == '质量科' && this.isAuth('department:quality:leader'))) {
          return true;
        } else if ((department == '党群办公室' && this.isAuth('department:party:leader'))) {
          return true;
        } else if ((department == '供应科' && this.isAuth('department:supply:leader'))) {
          return true;
        } else if ((department == '市场科' && this.isAuth('department:market:leader'))) {
          return true;
        } else if ((department == '财务科' && this.isAuth('department:financial:leader'))) {
          return true;
        } else if ((department == '技术科' && this.isAuth('department:tech:leader'))) {
          return true;
        } else if ((department == '安环设备科' && this.isAuth('department:safety:leader'))) {
          return true;
        } else if ((department == '企业管理科' && this.isAuth('department:enterprise:leader'))) {
          return true;
        } else {
          return false;
        }
      } else if (examineStatus == '2' && examineDepartment == '1') {
        console.log('管理员审核');
        console.log('isAuth(qcManagement:group:admin): ', this.isAuth('qcManagement:group:admin'));

        if (this.isAuth('qcManagement:group:admin')) {
          console.log('返回 true - 是管理员');
          return true;
        } else {
          console.log('返回 false - 非管理员');
          return false;
        }
      } else {
        console.log('返回 false - 其他情况');
        return false;
      }
    },
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
    getFirstList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': 1,
          'limit': 10000000,
          // 'key': this.dataForm.key
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          let tmp = data.page.list
          tmp.forEach(item => {
            item.teamNumberIds = JSON.parse(item.teamNumberIds)
          });
          console.log(tmp)
          this.firstList = tmp
          this.totalPage = data.page.totalCount
        } else {
          this.firstList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
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
          // this.dataList = data.page.list
          data.page.list.forEach(item => {
            if (item.memberName == data.userName) {
              sameList.push(item)
            }
          });
          console.log(sameList)
          const resultList = [];
          const seen = new Set(); // 用于去重

          sameList.forEach(item => {
            if (!seen.has(item.qcgmId)) {
              resultList.push(item);
              seen.add(item.qcgmId);
            }

            if (item.parentId) {
              data.page.list.forEach(row => {
                if ((row.parentId == item.parentId || row.qcgmId == item.parentId) && !seen.has(row.qcgmId)) {
                  resultList.push(row);
                  seen.add(row.qcgmId);
                }
              });
            } else {
              data.page.list.forEach(row => {
                if (row.parentId == item.qcgmId && !seen.has(row.qcgmId)) {
                  row.management = 1 //标识当前角色为组长，可以移交组长
                  resultList.push(row);
                  seen.add(row.qcgmId);
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
                memberName: item.memberName,
                number: item.number,
                groupName: item.groupName,
                roleInTopic: "组长",
                team: item.team,
                department: item.department,
                examineStatus: item.examineStatus,
                position: item.position,
                examineDepartment: item.examineDepartment,
                examineGroup: item.examineGroup,
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
                memberName: item.memberName,
                number: item.number,
                department: item.department,
                roleInTopic: item.roleInTopic,
                parentId: item.parentId,
              });
            }
          });
          console.log(map);
          this.dataList = Object.values(map);
          this.dataList.sort(function (a, b) {
            const dateA = new Date(a.date);
            const dateB = new Date(b.date);
            // 比较两个日期对象，降序排列
            return dateB - dateA;
          })
          this.dataList.forEach(e => {
            e.children.sort(function (a, b) {
              const dateA = new Date(a.date);
              const dateB = new Date(b.date);
              // 比较两个日期对象，降序排列
              return dateB - dateA;
            });
          });
          // this.dataList = resultList
          console.log(this.dataList);
          this.dataListAfterQuery = this.dataList
          this.totalPageGroup = resultList.length;
        } else {
          this.dataList = [];
          this.totalPageGroup = 0;
        }

        this.dataListLoading = false;
      });
    },
    upQuery() {
      let tmpList = JSON.parse(JSON.stringify(this.dataList))
      if (this.dataListQuery.groupName != '' && this.dataListQuery.groupName != null) {
        tmpList = tmpList.filter(item => item.groupName.includes(this.dataListQuery.groupName))
      }
      if (this.dataListQuery.department != '' && this.dataListQuery.department != null) {
        console.log('进入科室')
        console.log(this.dataListQuery.department)
        tmpList = tmpList.filter(item => item.department == this.dataListQuery.department)
      }
      if (this.dataListQuery.groupType != '' && this.dataListQuery.groupType != null) {
        console.log('进入类型')
        console.log(this.dataListQuery.groupType)
        tmpList = tmpList.filter(item => item.team == this.dataListQuery.groupType)
      }
      if (this.dataListQuery.consultant != '' && this.dataListQuery.consultant != null) {
        console.log('进入顾问')
        console.log(this.dataListQuery.consultant)
        console.log(this.nameToNumber(this.dataListQuery.consultant))
        var trueConsultant = this.dataListQuery.consultant
        console.log({ trueConsultant })
        if (this.nameToNumber(this.dataListQuery.consultant)) {
          console.log('进入顾问内部转换')
          let tmp = this.nameToNumber(this.dataListQuery.consultant)
          this.dataListQuery.consultant = this.nameToNumber(this.dataListQuery.consultant);
          console.log({ tmp })
        }
        tmpList = tmpList.filter(item => item.position == this.dataListQuery.consultant)
      }
      if (this.dataListQuery.examineStatus != '' && this.dataListQuery.examineStatus != null) {
        console.log('进入审核')
        console.log(this.dataListQuery.examineStatus)
        if (this.dataListQuery.examineStatus == '待审核(科室)') {
          tmpList = tmpList.filter(item => item.examineStatus == '待审核' && item.examineDepartment != '1')
        } else if (this.dataListQuery.examineStatus == '待审核(管理员)') {
          tmpList = tmpList.filter(item => item.examineStatus == '待审核' && item.examineDepartment == '1')
        } else {
          tmpList = tmpList.filter(item => item.examineStatus == this.dataListQuery.examineStatus)
        }
      }
      if (Array.isArray(this.dataListQuery.buildTime) && this.dataListQuery.buildTime.length == 2) {
        console.log('进入日期')
        console.log(this.dataListQuery.buildTime)
        tmpList = tmpList.filter(item => moment(item.date).format('YYYY-MM-DD') >= moment(this.dataListQuery.buildTime[0]).startOf('day').format('YYYY-MM-DD') && moment(item.date).format('YYYY-MM-DD') <= moment(this.dataListQuery.buildTime[1]).endOf('day').format('YYYY-MM-DD'))
      }
      if (this.dataListQuery.aboutMe != '' && this.dataListQuery.aboutMe != null) {
        console.log('进入关于我')
        console.log(this.dataListQuery.aboutMe)
        console.log(this.currentUserName)
        if (this.dataListQuery.aboutMe == '我创办') {
          console.log('我创办筛选')
          tmpList = tmpList.filter(item => item.memberName == this.currentUserName)
        } else if (this.dataListQuery.aboutMe == '我参与') {
          console.log('我参与筛选')
          tmpList = tmpList.filter(item => item.memberName != this.currentUserName)
        }
      }
      this.dataListAfterQuery = tmpList
    },
    upQueryConsultant() {
      let tmpList = JSON.parse(JSON.stringify(this.consultantList))
      if (this.dataListQueryConsultant.groupName != '' && this.dataListQueryConsultant.groupName != null) {
        tmpList = tmpList.filter(item => item.groupName.includes(this.dataListQueryConsultant.groupName))
      }
      if (this.dataListQueryConsultant.department != '' && this.dataListQueryConsultant.department != null) {
        console.log('进入科室')
        console.log(this.dataListQueryConsultant.department)
        tmpList = tmpList.filter(item => item.department == this.dataListQueryConsultant.department)
      }
      if (this.dataListQueryConsultant.groupType != '' && this.dataListQueryConsultant.groupType != null) {
        console.log('进入类型')
        console.log(this.dataListQueryConsultant.groupType)
        tmpList = tmpList.filter(item => item.team == this.dataListQueryConsultant.groupType)
      }
      if (this.dataListQueryConsultant.memberName != '' && this.dataListQueryConsultant.memberName != null) {
        console.log('进入组长')
        console.log(this.dataListQueryConsultant.memberName)
        console.log(this.nameToNumber(this.dataListQueryConsultant.memberName))
        var trueConsultant = this.dataListQueryConsultant.memberName
        console.log({ trueConsultant })
        if (this.nameToNumber(this.dataListQueryConsultant.memberName)) {
          console.log('进入组长内部转换')
          let tmp = this.nameToNumber(this.dataListQueryConsultant.memberName)
          this.dataListQueryConsultant.memberName = this.nameToNumber(this.dataListQueryConsultant.memberName);
          console.log({ tmp })
        }
        tmpList = tmpList.filter(item => item.memberName == this.dataListQueryConsultant.memberName)
      }
      if (this.dataListQueryConsultant.examineStatus != '' && this.dataListQueryConsultant.examineStatus != null) {
        console.log('进入审核')
        console.log(this.dataListQueryConsultant.examineStatus)
        if (this.dataListQueryConsultant.examineStatus == '待审核(科室)') {
          tmpList = tmpList.filter(item => item.examineStatus == '待审核' && item.examineDepartment != '1')
        } else if (this.dataListQueryConsultant.examineStatus == '待审核(管理员)') {
          tmpList = tmpList.filter(item => item.examineStatus == '待审核' && item.examineDepartment == '1')
        } else {
          tmpList = tmpList.filter(item => item.examineStatus == this.dataListQueryConsultant.examineStatus)
        }
      }
      if (Array.isArray(this.dataListQueryConsultant.buildTime) && this.dataListQueryConsultant.buildTime.length == 2) {
        console.log('进入日期')
        console.log(this.dataListQueryConsultant.buildTime)
        tmpList = tmpList.filter(item => moment(item.date).format('YYYY-MM-DD') >= moment(this.dataListQueryConsultant.buildTime[0]).startOf('day').format('YYYY-MM-DD') && moment(item.date).format('YYYY-MM-DD') <= moment(this.dataListQueryConsultant.buildTime[1]).endOf('day').format('YYYY-MM-DD'))
      }
      this.dataListAfterQueryConsultant = tmpList
    },
    handleSubmitPlan(id, status, type) {
      console.log(id)
      console.log(status)
      console.log(type)
      if (type == '创新型') {
        if (status != '8') {
          this.$message({
            message: '请先完成课题计划',
            type: 'warning',
            duration: 1500
          })
        } else {
          this.resultTypeVisible = true
          this.$nextTick(() => {
            this.$refs.resultType.init(id)
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
          console.log('创')
          this.resultTypeVisible = true
          this.$nextTick(() => {
            this.$refs.resultType.init(id)
          })
        }
      }
    },
    // 获取顾问数据列表
    async getConsultantList() {
      this.dataListLoading = true;
      await this.$http({
        url: this.$http.adornUrl("/qcMembers/qcGroupMember/myList"),
        method: "get",
        params: this.$http.adornParams({
          'page': 1,
          'limit': 1000000,
          // 'reuseStepId': 5
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          const sameList = [];
          // this.dataList = data.page.list
          data.page.list.forEach(item => {
            // console.log('-----------------')
            // console.log(item.position)
            // console.log(data.userName)
            // console.log('-----------------')
            if (item.position == data.userName && item.roleInTopic == "组长") {
              // console.log('存在')
              sameList.push(item)
            }
          });
          console.log(sameList)
          const resultList = [];
          const seen = new Set(); // 用于去重

          sameList.forEach(item => {
            if (!seen.has(item.qcgmId)) {
              resultList.push(item);
              seen.add(item.qcgmId);
            }
            if (item.parentId) {
              data.page.list.forEach(row => {
                if ((row.parentId == item.parentId || row.qcgmId == item.parentId) && !seen.has(row.qcgmId)) {
                  resultList.push(row);
                  seen.add(row.qcgmId);
                }
              });
            } else {
              data.page.list.forEach(row => {
                if (row.parentId == item.qcgmId && !seen.has(row.qcgmId)) {
                  row.management = 1 //标识当前角色为组长，可以移交组长
                  resultList.push(row);
                  seen.add(row.qcgmId);
                }
              });
            }
          });
          console.log(resultList);
          // 分组
          this.consultantList = []; // 清空
          const map = {};
          resultList.forEach((item) => {
            if (item.parentId === null) {
              map[item.qcgmId] = {
                id: item.qcgmId,
                date: item.participationDate,
                memberName: item.memberName,
                number: item.number,
                groupName: item.groupName,
                roleInTopic: "组长",
                team: item.team,
                position: item.position,
                examineStatus: item.examineStatus,
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
                memberName: item.memberName,
                number: item.number,
                department: item.department,
                roleInTopic: item.roleInTopic,
                parentId: item.parentId,
              });
            }
          });
          console.log(map);

          this.consultantList = Object.values(map);

          this.consultantList.sort(function (a, b) {
            const dateA = new Date(a.date);
            const dateB = new Date(b.date);
            // 比较两个日期对象，降序排列
            return dateB - dateA;
          })
          this.consultantList.forEach(e => {
            e.children.sort(function (a, b) {
              const dateA = new Date(a.date);
              const dateB = new Date(b.date);
              // 比较两个日期对象，降序排列
              return dateB - dateA;
            });
          });
          // this.dataList = resultList
          console.log(this.consultantList);
          this.dataListAfterQueryConsultant = this.consultantList
          this.totalPageGroup = resultList.length;
        } else {
          this.consultantList = [];
          this.totalPageGroup = 0;
        }

        this.dataListLoading = false;
      });
    },
    // 课题新增 / 修改
    subjectEditHandle(id) {
      this.subjectEditVisible = true
      this.$nextTick(() => {
        this.$refs.subjectEdit.membersOptions = this.membersOptions;
        this.$refs.subjectEdit.groupMemberList = this.groupMemberList
        this.$refs.subjectEdit.updateOptions()
        this.$refs.subjectEdit.init(id)
      })
    },
    //消息提示
    async handleTip() {
      let registerList = [];
      let examineList = [];
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/list"),
        method: "get",
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': 900000,
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
          'page': this.pageIndex,
          'limit': 900000,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          examineList = data.page.list;
        } else {
          console.log("handleTip() error");
        }
      });
      let tipList = [];
      let tipDepartmentList = [];
      let tipRecognitionList = [];
      let tipFirstList = [];
      let tipSecondList = [];
      let tipFinancialList = [];
      let tipSubjectList = [];

      examineList.forEach((item) => {
        if (item.qcExamineCurrent !== "完成") {
          if (item.qcExamineCurrent == "0" && this.isAuth("qcExamine:department:submit")) {
            registerList.forEach((row) => {
              if (row.qcsrId == item.qcExamineSubject) {
                if (row.topicDepartment == '质量科' && this.isAuth('department:quality:leader')) {
                  tipList.push(item);
                  tipDepartmentList.push(item);
                  this.showNotification("科室审核");
                } else if (row.topicDepartment == '生产科' && this.isAuth('department:product:leader')) {
                  tipList.push(item);
                  tipDepartmentList.push(item);
                  this.showNotification("科室审核");
                } else if (row.topicDepartment == '财务科' && this.isAuth('department:financial:leader')) {
                  tipList.push(item);
                  tipDepartmentList.push(item);
                  this.showNotification("科室审核");
                } else if (row.topicDepartment == '市场科' && this.isAuth('department:market:leader')) {
                  tipList.push(item);
                  tipDepartmentList.push(item);
                  this.showNotification("科室审核");
                } else if (row.topicDepartment == '供应科' && this.isAuth('department:supply:leader')) {
                  tipList.push(item);
                  tipDepartmentList.push(item);
                  this.showNotification("科室审核");
                } else if (row.topicDepartment == '技术科' && this.isAuth('department:tech:leader')) {
                  tipList.push(item);
                  tipDepartmentList.push(item);
                  this.showNotification("科室审核");
                } else if (row.topicDepartment == '安环设备科' && this.isAuth('department:safety:leader')) {
                  tipList.push(item);
                  tipDepartmentList.push(item);
                  this.showNotification("科室审核");
                } else if (row.topicDepartment == '企业管理科' && this.isAuth('department:enterprise:leader')) {
                  tipList.push(item);
                  tipDepartmentList.push(item);
                  this.showNotification("科室审核");
                } else if (row.topicDepartment == '党群办公室' && this.isAuth('department:party:leader')) {
                  tipList.push(item);
                  tipDepartmentList.push(item);
                  this.showNotification("科室审核");
                }
              }
            });
            // tipList.push(item);
            // tipDepartmentList.push(item);
            // this.showNotification("科室审核");
          }
          else if (item.qcExamineCurrent == "1" && this.isAuth("qcExamine:Achievement:recognition")) {
            tipList.push(item);
            tipRecognitionList.push(item);
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
                  tipFirstList.push(item);
                  this.showNotification("成果初评");
                }
              }
            });
          } else if (item.qcExamineCurrent == "3" && this.isAuth("qcExamine:first:comment")) {
            tipList.push(item);
            tipFirstList.push(item);
            this.showNotification("成果初评");
          } else if ((item.qcExamineCurrent == "4" || item.qcExamineCurrent == "4.2") && this.isAuth("qcExamine:second:comment")) {
            tipList.push(item);
            // console.log(item);
            tipSecondList.push(item);
            this.showNotification("成果复评");
          } else if ((item.qcExamineCurrent == "4" || item.qcExamineCurrent == "4.1") && this.isAuth("qcExamine:second:comment")) {
            tipList.push(item);
            tipFinancialList.push(item);
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
      console.log(tipDepartmentList)
      if (tipDepartmentList.length) {
        tipDepartmentList.forEach((t) => {
          registerList.forEach((r) => {
            if (t.qcExamineSubject == r.qcsrId) {
              this.departmentExamineList.push(r);
            }
          });
        });
      }
      if (tipRecognitionList.length) {
        tipRecognitionList.forEach((t) => {
          registerList.forEach((r) => {
            if (t.qcExamineSubject == r.qcsrId) {
              this.recognitionExamineList.push(r);
            }
          });
        });
      }
      if (tipFirstList.length) {
        tipFirstList.forEach((t) => {
          registerList.forEach((r) => {
            if (t.qcExamineSubject == r.qcsrId) {
              this.firstExamineList.push(r);
            }
          });
        });
      }
      if (tipSecondList.length) {
        tipSecondList.forEach((t) => {
          registerList.forEach((r) => {
            if (t.qcExamineSubject == r.qcsrId) {
              this.secondExamineList.push(r);
            }
          });
        });
      }

      if (tipFinancialList.length) {
        tipFinancialList.forEach((t) => {
          registerList.forEach((r) => {
            if (t.qcExamineSubject == r.qcsrId) {
              this.financialExamineList.push(r);
            }
          });
        });
      }

      console.log(this.departmentExamineList)
      this.superScriptNumber = tipList.length;
      this.messageList = tipSubjectList;
      console.log("++++++++++");
      console.log(tipSubjectList);
      console.log("++++++++++");
      console.log("----------");
      console.log(tipList);
      console.log("----------");
    },
    // 获取关于我的课题数据列表
    async getSubjectList() {
      this.myQueryParamSubject.activityPlan = ''
      this.myQueryParamSubject.activityPlanEnd = ''
      let trueLeader = this.myQueryParamSubject.topicLeader
      let trueConsultant = this.myQueryParamSubject.topicConsultant
      if (this.nameToNumber(this.myQueryParamSubject.topicLeader)) {
        this.myQueryParamSubject.topicLeader = this.nameToNumber(this.myQueryParamSubject.topicLeader);
      }
      if (this.nameToNumber(this.myQueryParamSubject.topicConsultant)) {
        console.log('进入顾问')
        console.log(this.myQueryParamSubject.topicConsultant)
        this.myQueryParamSubject.topicConsultant = this.nameToNumber(this.myQueryParamSubject.topicConsultant);
      }
      if (Array.isArray(this.activityPlanSubject) && this.activityPlanSubject.length == 2) {
        console.log(this.activityPlanSubject[0])
        this.myQueryParamSubject.activityPlan = `${this.activityPlanSubject[0]},${this.activityPlanSubject[1]}`
      }
      if (Array.isArray(this.activityPlanEndSubject) && this.activityPlanEndSubject.length == 2) {
        this.myQueryParamSubject.activityPlanEnd = `${this.activityPlanEndSubject[0]},${this.activityPlanEndSubject[1]}`
      }
      this.dataListLoading = true;
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/aboutMeList"),
        method: "get",
        params: this.$http.adornParams({
          'page': this.pageIndexSubject,
          'limit': this.pageSizeSubject,
          'key': this.myQueryParamSubject,
          // 'reuseStepId': 5
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          let tmp = data.page.list
          tmp.forEach(item => {
            item.teamNumberIds = JSON.parse(item.teamNumberIds)
          });
          this.subjectDataList = tmp;
          this.subjectDataList = data.page.list;
          // this.dataList = resultList
          this.totalPageSubject = data.page.totalCount;
          this.myQueryParamSubject.topicLeader = trueLeader
          this.myQueryParamSubject.topicConsultant = trueConsultant
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
    numberToNameArray(numbers) {
      if (Array.isArray(numbers)) {
        let result = numbers.map(number => this.numberToName(number));
        return `${result}`
        // return numbers
      } else {
        return numbers
      }
    },
    //用户名转昵称
    numberToName(number) {
      var result = ''
      this.membersOptions.forEach(o => {
        o.options.map(e => {
          if (e.name == number) {
            result = e.label.replace(/\(.*?\)/, '')
          }
        })
      });
      return result
    },
    //昵称转用户名
    nameToNumber(name) {
      var result = ''
      this.membersOptions.forEach(o => {
        o.options.map(e => {
          if (e.number == name) {
            result = e.name
            console.log(e.name)
          }
        })
      });
      return result
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
            limit: 1000000,
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
                    roleInTopic: "组长",
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
    messagePlanHandle(id, row) {
      let qcsrId = id;
      // console.log("qcsrId======xht=======>"+qcsrId);
      let filteredArray = [];
      // 遍历原始数组
      // console.log(row)
      let tmpList = [];
      // for (let i = 0; i < tmpList.length; i++) {
      //   if (this.dataList[i].qcsrId === id) {
      //     // 如果满足条件，将对象添加到新数组中
      //     filteredArray.push(this.dataList[i]);
      //     // console.log('1')
      //   }
      // }
      // console.log(filteredArray)
      this.$router.push(
        {
          name: 'qcPlanNew',
          query: {
            data: JSON.stringify(row),
            qcsrId: qcsrId,
          }
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
