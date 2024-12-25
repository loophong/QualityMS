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
            <el-input style="width: 100px;" v-model="myQueryParam.topicLeader" placeholder="组长" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input style="width: 100px;" v-model="myQueryParam.topicConsultant" placeholder="顾问"
              clearable></el-input>
          </el-form-item>
          <!-- <el-form-item>
            <el-select style="width: 120px;" v-model="myQueryParam.topicReviewStatus" placeholder="审核状态" clearable>
              <el-option label="未开始" value="1"></el-option>
              <el-option label="审核中" value="2"></el-option>
              <el-option label="未通过" value="0"></el-option>
              <el-option label="已通过" value="3"></el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item>
            <el-select style="width: 120px;" v-model="myQueryParam.topicDepartment" placeholder="科室" clearable>
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
            <el-date-picker style="width: 260px;" v-model="activityPlanAll" type="daterange" range-separator="-"
              start-placeholder="计划开始日期" end-placeholder="" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-date-picker style="width: 260px;" v-model="activityPlanEndAll" type="daterange" range-separator="-"
              start-placeholder="计划结束日期" end-placeholder="" value-format="yyyy-MM-dd">
            </el-date-picker>
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
        <el-table :data="dataList" :row-class-name="tableRowClassName" border v-loading="dataListLoading"
          @selection-change="selectionChangeHandle" style="width: 100%;"
          :default-sort="{ prop: 'qcsrId', order: 'descending' }" highlight-current-row>
          <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column> -->
          <el-table-column prop="qcsrId" header-align="center" align="center" label="课题ID" fixed sortable width="100">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" width="160" fixed>
          </el-table-column>
          <el-table-column prop="topicDepartment" header-align="center" align="center" label="科室" sortable>
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
          <el-table-column prop="activityPlan" header-align="center" align="center" label="计划开始日期" width="120">
          </el-table-column>
          <el-table-column prop="activityPlanEnd" header-align="center" align="center" label="计划结束日期" width="120">
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
                @click="newPlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcSubject:plan:submit')"
                :disabled="scope.row.resultType"
                @click="addOrUpdateHandle(scope.row.qcsrId, scope.row.topicActivityStatus, scope.row.topicType)">成果提交</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')"
                @click="examineStatus(scope.row, scope.row.resultType)">审核状态</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

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
            <el-input style="width: 100px;" v-model="myQueryParamLead.topicLeader" placeholder="组长"
              clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input style="width: 100px;" v-model="myQueryParamLead.topicConsultant" placeholder="顾问"
              clearable></el-input>
          </el-form-item>
          <!-- <el-form-item>
            <el-select style="width: 120px;" v-model="myQueryParamLead.topicReviewStatus" placeholder="审核状态" clearable>
              <el-option label="未开始" value="1"></el-option>
              <el-option label="审核中" value="2"></el-option>
              <el-option label="未通过" value="0"></el-option>
              <el-option label="已通过" value="3"></el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item>
            <el-select style="width: 120px;" v-model="myQueryParamLead.topicDepartment" placeholder="科室" clearable>
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
            <el-date-picker style="width: 260px;" v-model="activityPlanLead" type="daterange" range-separator="-"
              start-placeholder="计划开始日期" end-placeholder="" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-date-picker style="width: 260px;" v-model="activityPlanEndLead" type="daterange" range-separator="-"
              start-placeholder="计划结束日期" end-placeholder="" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button @click="getLeadList()">查询</el-button>
            <!-- <el-button v-if="isAuth('qcSubject:registration:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button> -->
            <!-- <el-button v-if="isAuth('qcSubject:registration:save')" type="warning" @click="reuseHandle()"
              :disabled="dataListSelections.length != 1">课题重用</el-button> -->
            <el-button type="danger" @click="toIssue()">问题添加</el-button>
            <!-- <el-badge :value="superScriptNumber" class="item">
              <el-button type="warning" @click="dialogMessageVisible = true">消息详情</el-button>
            </el-badge> -->
          </el-form-item>
        </el-form>
        <el-table :data="subjectLeadList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%" stripe :default-sort="{ prop: 'qcsrId', order: 'descending' }" highlight-current-row>
          <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column> -->
          <el-table-column prop="qcsrId" header-align="center" align="center" label="课题ID" fixed sortable width="100">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" width="160" fixed>
          </el-table-column>
          <el-table-column prop="topicDepartment" header-align="center" align="center" label="科室" sortable>
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
          <el-table-column prop="activityPlan" header-align="center" align="center" label="计划开始日期" width="120">
          </el-table-column>
          <el-table-column prop="activityPlanEnd" header-align="center" align="center" label="计划结束日期" width="120">
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
                @click="newPlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcSubject:plan:submit')"
                :disabled="scope.row.resultType"
                @click="addOrUpdateHandle(scope.row.qcsrId, scope.row.topicActivityStatus, scope.row.topicType)">成果提交</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')"
                @click="examineStatus(scope.row, scope.row.resultType)">审核状态</el-button>
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
            <el-input style="width: 100px;" v-model="myQueryParamJoin.topicLeader" placeholder="组长"
              clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input style="width: 100px;" v-model="myQueryParamJoin.topicConsultant" placeholder="顾问"
              clearable></el-input>
          </el-form-item>
          <!-- <el-form-item>
            <el-select style="width: 120px;" v-model="myQueryParamJoin.topicReviewStatus" placeholder="审核状态" clearable>
              <el-option label="未开始" value="1"></el-option>
              <el-option label="审核中" value="2"></el-option>
              <el-option label="未通过" value="0"></el-option>
              <el-option label="已通过" value="3"></el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item>
            <el-select style="width: 120px;" v-model="myQueryParamJoin.topicDepartment" placeholder="科室" clearable>
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
            <el-date-picker style="width: 260px;" v-model="activityPlanJoin" type="daterange" range-separator="-"
              start-placeholder="计划开始日期" end-placeholder="" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-date-picker style="width: 260px;" v-model="activityPlanEndJoin" type="daterange" range-separator="-"
              start-placeholder="计划结束日期" end-placeholder="" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button @click="getJoinList()">查询</el-button>
            <!-- <el-button v-if="isAuth('qcSubject:registration:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button> -->
            <!-- <el-button v-if="isAuth('qcSubject:registration:save')" type="warning" @click="reuseHandle()"
              :disabled="dataListSelections.length != 1">课题重用</el-button> -->
            <el-button type="danger" @click="toIssue()">问题添加</el-button>

          </el-form-item>
        </el-form>
        <el-table :data="subjectJoinList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%" stripe :default-sort="{ prop: 'qcsrId', order: 'descending' }" highlight-current-row>
          <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column> -->
          <el-table-column prop="qcsrId" header-align="center" align="center" label="课题ID" fixed sortable width="100">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" width="160" fixed>
          </el-table-column>
          <el-table-column prop="topicDepartment" header-align="center" align="center" label="科室" sortable>
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
          <el-table-column prop="activityPlan" header-align="center" align="center" label="计划开始日期" width="120">
          </el-table-column>
          <el-table-column prop="activityPlanEnd" header-align="center" align="center" label="计划结束日期" width="120">
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
                @click="newPlanHandle(scope.row.qcsrId, scope.row)">关联计划</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcSubject:plan:submit')"
                :disabled="scope.row.resultType"
                @click="addOrUpdateHandle(scope.row.qcsrId, scope.row.topicActivityStatus, scope.row.topicType)">成果提交</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')"
                @click="examineStatus(scope.row, scope.row.resultType)">审核状态</el-button>
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
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"
      @refreshLeadList="getLeadList()" @refreshJoinList="getJoinList()"></add-or-update>
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
  name: 'qcPlanIndex',
  data() {
    return {
      membersOptions: [],
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
      activityPlanLead: [],
      activityPlanEndLead: [],
      activityPlanJoin: [],
      activityPlanEndJoin: [],
      activityPlanAll: [],
      activityPlanEndAll: [],
      myQueryParam: {
        topicName: '',
        keywords: '',
        topicLeader: '',
        topicConsultant: '',
        topicReviewStatus: '',
        topicDepartment: '',
        activityPlan: '',
        activityPlanEnd: '',
      },
      myQueryParamLead: {
        topicName: '',
        keywords: '',
        topicLeader: '',
        topicConsultant: '',
        topicReviewStatus: '',
        topicDepartment: '',
        activityPlan: '',
        activityPlanEnd: '',
      },
      myQueryParamJoin: {
        topicName: '',
        keywords: '',
        topicLeader: '',
        topicConsultant: '',
        topicReviewStatus: '',
        topicDepartment: '',
        activityPlan: '',
        activityPlanEnd: '',
      }
    }
  },
  components: {
    AddOrUpdate
  },
  async activated() {
    this.getDataList()
    this.getJoinList();
    this.getLeadList();
    // 获取分组后的员工数据
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
              name: match ? match[1] : e.name || '',// 如果匹配到，使用匹配的结果；否则保持原名或为空字符串
              number: number ? number : e.name || '' // 如果匹配到，使用匹配的结果；否则保持原名或为空字符串
            };
          })
        };
      });
      // console.log(this.membersOptions)
    });
  },
  computed: {
    // filteredDataList() {
    //   return this.dataList.filter(item => item.topicReviewStatus === 3 || item.topicReviewStatus === '3');
    // },
    // filteredJoinList() {
    //   return this.subjectJoinList.filter(item => item.topicReviewStatus === 3 || item.topicReviewStatus === '3');
    // },
    // filteredLeadList() {
    //   return this.subjectLeadList.filter(item => item.topicReviewStatus === 3 || item.topicReviewStatus === '3');
    // }
  },
  methods: {
    test(type) {
      if (type) {
        console.log(true)
      } else {
        console.log(false)
      }
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

    tableRowClassName({ row, rowIndex }) {
      if (rowIndex === 1) {
        return 'warning-row';
      } else if (rowIndex === 3) {
        return 'success-row';
      }
      return '';
    },
    //创建计划跳转
    newPlanHandle(id, row) {
      let qcsrId = id;
      // console.log("qcsrId======xht=======>"+qcsrId);
      let filteredArray = [];
      // 遍历原始数组
      console.log(row)
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
    examineStatus(row, resultType) {
      // console.log(resultType)
      // console.log(id)
      console.log(row)
      if (resultType === null || resultType === '') {
        this.$message({
          message: '课题计划尚未提交',
          type: 'warning',
          duration: 1500
        })
      } else {
        let filteredArray = [];

        // for (let i = 0; i < this.dataList.length; i++) {
        //   if (this.dataList[i].qcsrId === id) {
        //     // 如果满足条件，将对象添加到新数组中
        filteredArray.push(row);
        //     console.log(filteredArray)
        //   }
        // }

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
      this.myQueryParamJoin.activityPlan = ''
      this.myQueryParamJoin.activityPlanEnd = ''
      let trueLeader = this.myQueryParamJoin.topicLeader
      let trueConsultant = this.myQueryParamJoin.topicConsultant
      if (this.nameToNumber(this.myQueryParamJoin.topicLeader)) {
        this.myQueryParamJoin.topicLeader = this.nameToNumber(this.myQueryParamJoin.topicLeader);
      }
      if (this.nameToNumber(this.myQueryParamJoin.topicConsultant)) {
        this.myQueryParamJoin.topicConsultant = this.nameToNumber(this.myQueryParamJoin.topicConsultant);
      }
      if (Array.isArray(this.activityPlanJoin) && this.activityPlanJoin.length == 2) {
        console.log(this.activityPlanJoin[0])
        this.myQueryParamJoin.activityPlan = `${this.activityPlanJoin[0]},${this.activityPlanJoin[1]}`
      }
      if (Array.isArray(this.activityPlanEndJoin) && this.activityPlanEndJoin.length == 2) {
        this.myQueryParamJoin.activityPlanEnd = `${this.activityPlanEndJoin[0]},${this.activityPlanEndJoin[1]}`
      }
      this.dataListLoading = true;
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/myListFilter"),
        method: "get",
        params: this.$http.adornParams({
          'page': this.pageIndexJoin,
          'limit': this.pageSizeJoin,
          'key': this.myQueryParamJoin,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          let tmp = data.page.list
          tmp.forEach(item => {
            item.teamNumberIds = JSON.parse(item.teamNumberIds)
          });
          this.subjectJoinList = tmp;
          this.myQueryParamJoin.topicLeader = trueLeader
          this.myQueryParamJoin.topicConsultant = trueConsultant
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
      this.myQueryParamLead.activityPlan = ''
      this.myQueryParamLead.activityPlanEnd = ''
      let trueLeader = this.myQueryParamLead.topicLeader
      let trueConsultant = this.myQueryParamLead.topicConsultant
      if (this.nameToNumber(this.myQueryParamLead.topicLeader)) {
        this.myQueryParamLead.topicLeader = this.nameToNumber(this.myQueryParamLead.topicLeader);
      }
      if (this.nameToNumber(this.myQueryParamLead.topicConsultant)) {
        this.myQueryParamLead.topicConsultant = this.nameToNumber(this.myQueryParamLead.topicConsultant);
      }
      if (Array.isArray(this.activityPlanLead) && this.activityPlanLead.length == 2) {
        console.log(this.activityPlanLead[0])
        this.myQueryParamLead.activityPlan = `${this.activityPlanLead[0]},${this.activityPlanLead[1]}`
      }
      if (Array.isArray(this.activityPlanEndLead) && this.activityPlanEndLead.length == 2) {
        this.myQueryParamLead.activityPlanEnd = `${this.activityPlanEndLead[0]},${this.activityPlanEndLead[1]}`
      }
      this.dataListLoading = true;
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/leadListFilter"),
        method: "get",
        params: this.$http.adornParams({
          'page': this.pageIndexLead,
          'limit': this.pageSizeLead,
          'key': this.myQueryParamLead,
          // 'reuseStepId': 5
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          let tmp = data.page.list
          tmp.forEach(item => {
            item.teamNumberIds = JSON.parse(item.teamNumberIds)
          });
          this.subjectLeadList = tmp;
          this.myQueryParamLead.topicLeader = trueLeader
          this.myQueryParamLead.topicConsultant = trueConsultant
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
      this.myQueryParam.activityPlan = ''
      this.myQueryParam.activityPlanEnd = ''
      let trueLeader = this.myQueryParam.topicLeader
      let trueConsultant = this.myQueryParam.topicConsultant
      if (this.nameToNumber(this.myQueryParam.topicLeader)) {
        this.myQueryParam.topicLeader = this.nameToNumber(this.myQueryParamJoin.topicLeader);
      }
      if (this.nameToNumber(this.myQueryParam.topicConsultant)) {
        this.myQueryParam.topicConsultant = this.nameToNumber(this.myQueryParam.topicConsultant);
      }
      if (Array.isArray(this.activityPlanAll) && this.activityPlanAll.length == 2) {
        console.log(this.activityPlanAll[0])
        this.myQueryParam.activityPlan = `${this.activityPlanAll[0]},${this.activityPlanAll[1]}`
      }
      if (Array.isArray(this.activityPlanEndAll && this.activityPlanEndAll.length == 2)) {
        this.myQueryParam.activityPlanEnd = `${this.activityPlanEndAll[0]},${this.activityPlanEndAll[1]}`
      }
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/listFilter'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.myQueryParam,
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          let tmp = data.page.list
          tmp.forEach(item => {
            item.teamNumberIds = JSON.parse(item.teamNumberIds)
          });
          this.dataList = tmp
          this.myQueryParam.topicLeader = trueLeader
          this.myQueryParam.topicConsultant = trueConsultant
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
      // console.log(id)
      // console.log(status)
      if (type) {
        console.log(type)
      }
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
          console.log('创')
          this.addOrUpdateVisible = true
          this.$nextTick(() => {
            this.$refs.addOrUpdate.init(id)
          })
        }
      }
      console.log(this.dataListSelections)
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
