<template>
  <div class="mod-config">
    <el-tabs type="border-card">
      <el-tab-pane label="当前任务">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getUnfinishedTasksList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getUnfinishedTasksList()">查询</el-button>
            <el-button v-if="isAuth('taskmanagement:task:delete')" type="danger" @click="deleteHandle()"
              :disabled="dataListSelections.length <= 0">批量删除</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="unfinishedTasksList" border v-loading="unfinishedTasksListLoading"
          @selection-change="selectionChangeHandle" style="width: 100%;">
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="taskId" header-align="center" align="center" label="任务编号">
          </el-table-column>
          <el-table-column prop="taskName" header-align="center" align="center" label="任务名">
          </el-table-column>
          <el-table-column prop="taskContent" header-align="center" align="center" label="任务内容">
          </el-table-column>
          <el-table-column prop="taskStartDate" header-align="center" align="center" label="开始日期">
          </el-table-column>
          <el-table-column prop="taskScheduleCompletionDate" header-align="center" align="center" label="计划完成日期">
          </el-table-column>
          <el-table-column prop="taskScheduleDays" header-align="center" align="center" label="计划天数">
          </el-table-column>
          <!-- <el-table-column prop="taskActualCompletionDate" header-align="center" align="center" label="实际完成日期">
          </el-table-column>
          <el-table-column prop="taskActualDays" header-align="center" align="center" label="实际天数">
          </el-table-column> -->
          <el-table-column prop="taskAssociatedPlanId" header-align="center" align="center" label="相关计划编号">
          </el-table-column>
          <el-table-column prop="taskSchedule" header-align="center" align="center" label="进度">
          </el-table-column>
          <el-table-column prop="taskPrincipal" header-align="center" align="center" label="负责人">
            <template slot-scope="scope">
              {{ getUserameByUserId(scope.row.taskPrincipal) }}
            </template>
          </el-table-column>
          <el-table-column prop="taskExecutor" header-align="center" align="center" label="执行人" width="110">
            <template slot-scope="scope">
              <span v-for="(executorId, index) in scope.row.taskExecutor" :key="executorId">
                {{ getUserameByUserId(executorId) }}
                <span v-if="index !== scope.row.taskExecutor.length - 1">, </span>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="taskAuditor" header-align="center" align="center" label="审核人">
            <template slot-scope="scope">
              {{ getUserameByUserId(scope.row.taskAuditor) }}
            </template>
          </el-table-column>
          <el-table-column prop="taskCurrentState" label="当前状态" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.taskCurrentState === 'NOT_STARTED'">
                <el-tag type="info" disable-transitions>未开始</el-tag></span>
              <span v-else-if="scope.row.taskCurrentState === 'IN_PROGRESS'">
                <el-tag disable-transitions>进行中</el-tag></span>
              <span v-else-if="scope.row.taskCurrentState === 'APPROVAL_IN_PROGRESS'">
                <el-tag disable-transitions>审批中</el-tag></span>
              <span v-else-if="scope.row.taskCurrentState === 'OVERDUE'">
                <el-tag type="danger" disable-transitions>已逾期</el-tag></span>
              <span v-else-if="scope.row.taskCurrentState === 'COMPLETED'">
                <el-tag type="success" disable-transitions>已完成</el-tag></span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="taskIsCompleted" label="是否完成" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.taskIsCompleted === 0" style="color: gray;">否</span>
              <span v-else-if="scope.row.taskIsCompleted === 1" style="color: gray;">是</span>
              <span v-else>-</span>
            </template>
          </el-table-column> -->
          <el-table-column prop="taskIsOverdue" label="是否超期" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.taskIsOverdue === 0" style="color: gray;">否</span>
              <span v-else-if="scope.row.taskIsOverdue === 1" style="color: gray;">是</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="taskIsOnTime" label="是否按时完工" header-align="center" align="center" width="110">
            <template slot-scope="scope">
              <span v-if="scope.row.taskIsOnTime === 0" style="color: gray;">否</span>
              <span v-else-if="scope.row.taskIsOnTime === 1" style="color: gray;">是</span>
              <span v-else>-</span>
            </template>
          </el-table-column> -->
          <!-- <el-table-column prop="taskEarlyCompletionDays" header-align="center" align="center" label="提前完工">
          </el-table-column> -->
          <el-table-column prop="taskLagDays" header-align="center" align="center" label="滞后天数">
          </el-table-column>
          <el-table-column prop="taskLagReasons" header-align="center" align="center" label="滞后原因">
          </el-table-column>
          <el-table-column prop="taskAssociatedIndicatorsId" header-align="center" align="center" label="关联指标名">
            <template slot-scope="scope">
              {{ getIndicatorNameById(scope.row.taskAssociatedIndicatorsId) }}
            </template>
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <!-- <el-button type="text" size="small" @click="finishTask(scope.row.taskId)">完成</el-button> -->
              <el-button type="text" size="small"
                @click="taskUpdatePage(scope.row.taskId)">任务分解</el-button>
              <el-button type="text" size="small" v-if="scope.row.taskCurrentState === 'IN_PROGRESS'"
                @click="showApproverDialog(scope.row)">提交审批</el-button>
              <el-button type="text" size="small" v-if="scope.row.taskCurrentState === 'APPROVAL_IN_PROGRESS'"
                @click="showApproverDialog(scope.row)">取消审批</el-button>
              <!-- <el-button type="text" size="small" @click="showApproverDialog(scope.row)">完成</el-button> -->
              <!-- <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.tmTid)">修改</el-button> -->
              <!-- <el-button type="text" size="small" @click="deleteHandle(scope.row.tmTid)">删除</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="unfinishedTasksSizeChangeHandle" @current-change="unfinishedTasksCurrentChangeHandle"
          :current-page="unfinishedTasksPageIndex" :page-sizes="[10, 20, 50, 100]" :page-size="unfinishedTasksPageSize"
          :total="unfinishedTasksTotalPage" layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </el-tab-pane>


      <el-tab-pane label="审批记录">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getMySubmitApprovalList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getMySubmitApprovalList()">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="mySubmitApprovalList" border v-loading="mySubmitApprovalListLoading" style="width: 100%;">
          <el-table-column prop="approvalId" header-align="center" align="center" label="审批编号">
          </el-table-column>
          <el-table-column prop="taskId" header-align="center" align="center" label="任务ID">
          </el-table-column>
          <el-table-column prop="taskName" header-align="center" align="center" label="任务名">
          </el-table-column>
          <el-table-column prop="taskContent" header-align="center" align="center" label="任务内容">
          </el-table-column>
          <!-- <el-table-column prop="taskStartDate" header-align="center" align="center" label="开始日期">
      </el-table-column> -->
          <el-table-column prop="taskAssociatedPlanId" header-align="center" align="center" label="相关计划编号">
          </el-table-column>
          <!-- <el-table-column prop="taskPrincipal" header-align="center" align="center" label="负责人">
      </el-table-column> -->
          <el-table-column prop="taskPrincipal" header-align="center" align="center" label="负责人">
            <template slot-scope="scope">
              {{ getUserameByUserId(scope.row.taskPrincipal) }}
            </template>
          </el-table-column>
          <!-- <el-table-column prop="taskExecutor" header-align="center" align="center" label="执行人">
      </el-table-column> -->
          <el-table-column prop="taskAssociatedIndicatorsId" header-align="center" align="center" label="关联指标编号">
          </el-table-column>
          <el-table-column prop="taskSubmissionTime" header-align="center" align="center" label="任务送审时间">
          </el-table-column>
          <!-- <el-table-column prop="approvalEndTime" header-align="center" align="center" label="审批结束时间">
      </el-table-column> -->
          <!-- <el-table-column prop="approver" header-align="center" align="center" label="审批人">
      </el-table-column> -->
          <!-- <el-table-column prop="submitter" header-align="center" align="center" label="送审人">
      </el-table-column> -->
          <el-table-column prop="submitter" header-align="center" align="center" label="送审人">
            <template slot-scope="scope">
              {{ getUserameByUserId(scope.row.submitter) }}
            </template>
          </el-table-column>
          <!-- <el-table-column prop="approvalStatus" header-align="center" align="center" label="审批状态">
      </el-table-column> -->
          <el-table-column prop="approvalStatus" label="审批状态" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.approvalStatus === 'PENDING'">
                <el-tag type="info" disable-transitions>待审批</el-tag></span>
              <span v-else-if="scope.row.approvalStatus === 'APPROVED'">
                <el-tag type="success" disable-transitions>已通过</el-tag></span>
              <span v-else-if="scope.row.approvalStatus === 'REJECTED'">
                <el-tag type="danger" disable-transitions>已拒绝</el-tag></span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>

          <el-table-column prop="approvalComments" header-align="center" align="center" label="审批意见">
          </el-table-column>
          <el-table-column prop="remarks" header-align="center" align="center" label="备注">
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.approvalId)">查看</el-button>
              <!-- <el-button type="text" size="small" @click="deleteHandle(scope.row.approvalId)">删除</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="mySubmitApprovalSizeChangeHandle"
          @current-change="mySubmitApprovalCurrentChangeHandle" :current-page="mySubmitApprovalPageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="mySubmitApprovalPageSize" :total="mySubmitApprovalTotalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>


      </el-tab-pane>

      <el-tab-pane label="历史任务">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getCompletedTasksList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getCompletedTasksList()">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="completedTasksList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%;">
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="taskId" header-align="center" align="center" label="任务ID">
          </el-table-column>
          <el-table-column prop="taskName" header-align="center" align="center" label="任务名">
          </el-table-column>
          <el-table-column prop="taskContent" header-align="center" align="center" label="任务内容">
          </el-table-column>
          <el-table-column prop="taskStartDate" header-align="center" align="center" label="开始日期">
          </el-table-column>
          <el-table-column prop="taskScheduleCompletionDate" header-align="center" align="center" label="计划完成日期">
          </el-table-column>
          <el-table-column prop="taskScheduleDays" header-align="center" align="center" label="计划天数">
          </el-table-column>
          <el-table-column prop="taskActualCompletionDate" header-align="center" align="center" label="实际完成日期">
          </el-table-column>
          <el-table-column prop="taskActualDays" header-align="center" align="center" label="实际天数">
          </el-table-column>
          <el-table-column prop="taskAssociatedPlanId" header-align="center" align="center" label="相关计划编号">
          </el-table-column>
          <el-table-column prop="taskSchedule" header-align="center" align="center" label="进度">
          </el-table-column>
          <el-table-column prop="taskPrincipal" header-align="center" align="center" label="负责人">
            <template slot-scope="scope">
              {{ getUserameByUserId(scope.row.taskPrincipal) }}
            </template>
          </el-table-column>
          <el-table-column prop="taskExecutor" header-align="center" align="center" label="执行人" width="110">
            <template slot-scope="scope">
              <span v-for="(executorId, index) in scope.row.taskExecutor" :key="executorId">
                {{ getUserameByUserId(executorId) }}
                <span v-if="index !== scope.row.taskExecutor.length - 1">, </span>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="taskAuditor" header-align="center" align="center" label="审核人">
            <template slot-scope="scope">
              {{ getUserameByUserId(scope.row.taskAuditor) }}
            </template>
          </el-table-column>
          <el-table-column prop="taskCurrentState" label="当前状态" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.taskCurrentState === 'NOT_STARTED'">
                <el-tag type="info" disable-transitions>未开始</el-tag></span>
              <span v-else-if="scope.row.taskCurrentState === 'IN_PROGRESS'">
                <el-tag disable-transitions>进行中</el-tag></span>
              <span v-else-if="scope.row.taskCurrentState === 'APPROVAL_IN_PROGRESS'">
                <el-tag disable-transitions>审批中</el-tag></span>
              <span v-else-if="scope.row.taskCurrentState === 'OVERDUE'">
                <el-tag type="danger" disable-transitions>已逾期</el-tag></span>
              <span v-else-if="scope.row.taskCurrentState === 'COMPLETED'">
                <el-tag type="success" disable-transitions>已完成</el-tag></span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="taskIsCompleted" label="是否完成" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.taskIsCompleted === 0" style="color: gray;">否</span>
              <span v-else-if="scope.row.taskIsCompleted === 1" style="color: gray;">是</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="taskIsOverdue" label="是否超期" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.taskIsOverdue === 0" style="color: gray;">否</span>
              <span v-else-if="scope.row.taskIsOverdue === 1" style="color: gray;">是</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="taskIsOnTime" label="是否按时完工" header-align="center" align="center" width="110">
            <template slot-scope="scope">
              <span v-if="scope.row.taskIsOnTime === 0" style="color: gray;">否</span>
              <span v-else-if="scope.row.taskIsOnTime === 1" style="color: gray;">是</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="taskEarlyCompletionDays" header-align="center" align="center" label="提前完工">
          </el-table-column>
          <el-table-column prop="taskLagDays" header-align="center" align="center" label="滞后天数">
          </el-table-column>
          <el-table-column prop="taskLagReasons" header-align="center" align="center" label="滞后原因">
          </el-table-column>
          <el-table-column prop="taskAssociatedIndicatorsId" header-align="center" align="center" label="关联指标编号">
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.tmTid)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="completedTasksSizeChangeHandle" @current-change="completedTasksCurrentChangeHandle"
          :current-page="completedTasksPageIndex" :page-sizes="[10, 20, 50, 100]" :page-size="completedTasksPageSize"
          :total="completedTasksTotalPage" layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </el-tab-pane>
    </el-tabs>

    <!-- 样表表单 -->
    <!-- <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('taskmanagement:ask:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('taskmanagement:task:delete')" type="danger" @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form> -->
    <!-- <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column>
      <el-table-column prop="taskId" header-align="center" align="center" label="任务ID">
      </el-table-column>
      <el-table-column prop="taskName" header-align="center" align="center" label="任务名">
      </el-table-column>
      <el-table-column prop="taskContent" header-align="center" align="center" label="任务内容">
      </el-table-column>
      <el-table-column prop="taskStartDate" header-align="center" align="center" label="开始日期">
      </el-table-column>
      <el-table-column prop="taskScheduleCompletionDate" header-align="center" align="center" label="计划完成日期">
      </el-table-column>
      <el-table-column prop="taskScheduleDays" header-align="center" align="center" label="计划天数">
      </el-table-column>
      <el-table-column prop="taskActualCompletionDate" header-align="center" align="center" label="实际完成日期">
      </el-table-column>
      <el-table-column prop="taskActualDays" header-align="center" align="center" label="实际天数">
      </el-table-column>
      <el-table-column prop="taskAssociatedPlanId" header-align="center" align="center" label="相关计划编号">
      </el-table-column>
      <el-table-column prop="taskSchedule" header-align="center" align="center" label="进度">
      </el-table-column>
      <el-table-column prop="taskPrincipal" header-align="center" align="center" label="负责人"> -->
    <!-- <template slot-scope="scope">
          {{ getUserameByUserId(scope.row.taskPrincipal) }}
        </template>
      </el-table-column>
      <el-table-column prop="taskExecutor" header-align="center" align="center" label="执行人" width="110">
        <template slot-scope="scope">
          <span v-for="(executorId, index) in scope.row.taskExecutor" :key="executorId">
            {{ getUserameByUserId(executorId) }}
            <span v-if="index !== scope.row.taskExecutor.length - 1">, </span>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="taskAuditor" header-align="center" align="center" label="审核人">
        <template slot-scope="scope">
          {{ getUserameByUserId(scope.row.taskAuditor) }}
        </template>
      </el-table-column> -->
    <!-- <el-table-column prop="taskCurrentState" label="当前状态" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.taskCurrentState === 0" style="color: gray;">未开始</span>
          <span v-else-if="scope.row.taskCurrentState === 1" style="color: gray;">进行中</span>
          <span v-else-if="scope.row.taskCurrentState === 2" style="color: green;">已完成</span>
          <span v-else>{{ scope.row.taskCurrentState }}</span>
        </template>
      </el-table-column> -->
    <!-- <el-table-column prop="taskCurrentState" label="当前状态" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.taskCurrentState === 'NOT_STARTED'">
            <el-tag type="info" disable-transitions>未开始</el-tag></span>
          <span v-else-if="scope.row.taskCurrentState === 'IN_PROGRESS'">
            <el-tag disable-transitions>进行中</el-tag></span>
          <span v-else-if="scope.row.taskCurrentState === 'APPROVAL_IN_PROGRESS'">
            <el-tag disable-transitions>审批中</el-tag></span>
          <span v-else-if="scope.row.taskCurrentState === 'OVERDUE'">
            <el-tag type="danger" disable-transitions>已逾期</el-tag></span>
          <span v-else-if="scope.row.taskCurrentState === 'COMPLETED'">
            <el-tag type="success" disable-transitions>已完成</el-tag></span> -->
    <!-- <span v-else>-</span> 处理未知状态 -->
    <!-- </template>
      </el-table-column>
      <el-table-column prop="taskIsCompleted" label="是否完成" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.taskIsCompleted === 0" style="color: gray;">否</span>
          <span v-else-if="scope.row.taskIsCompleted === 1" style="color: gray;">是</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="taskIsOverdue" label="是否超期" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.taskIsOverdue === 0" style="color: gray;">否</span>
          <span v-else-if="scope.row.taskIsOverdue === 1" style="color: gray;">是</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="taskIsOnTime" label="是否按时完工" header-align="center" align="center" width="110">
        <template slot-scope="scope">
          <span v-if="scope.row.taskIsOnTime === 0" style="color: gray;">否</span>
          <span v-else-if="scope.row.taskIsOnTime === 1" style="color: gray;">是</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="taskEarlyCompletionDays" header-align="center" align="center" label="提前完工">
      </el-table-column>
      <el-table-column prop="taskLagDays" header-align="center" align="center" label="滞后天数">
      </el-table-column>
      <el-table-column prop="taskLagReasons" header-align="center" align="center" label="滞后原因">
      </el-table-column>
      <el-table-column prop="taskAssociatedIndicatorsId" header-align="center" align="center" label="关联指标编号">
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope"> -->
    <!-- <el-button type="text" size="small" @click="finishTask(scope.row.taskId)">完成</el-button>showApproverDialog -->
    <!-- <el-button type="text" size="small" @click="showApproverDialog(scope.row)">完成</el-button> -->

    <!-- <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.tmTid)">修改</el-button> -->
    <!-- <el-button type="text" size="small" @click="deleteHandle(scope.row.tmTid)">删除</el-button>
        </template>
      </el-table-column>
    </el-table> -->
    <!-- <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination> -->
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>



    <!-- 审批人弹窗 -->
    <el-dialog title="选择审批人" :visible.sync="submitApprovalDialogVisible" width="50%">
      <!-- <el-form :model="form"> -->
      <!-- <el-form-item label="审批人" :label-width="formLabelWidth">
          <el-select v-model="form.approver" placeholder="请选择审批人">
            <el-option v-for="item in approvers" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item> -->
      <el-form ref="formRef" :model="approvalForm" label-width="80px">
        <el-form-item label="审核人" prop="taskApprovalor">
          <el-select v-model="approvalForm.taskApprovalor" filterable placeholder="请选择审核人">
            <el-option-group v-for="group in options" :key="group.label" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- </el-form> -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="submitApprovalDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitApprover">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>



<script>
import AddOrUpdate from './task-update'


export default {
  data() {
    return {
      approvalForm: {
        taskApprovalor: '',
        taskId: ''
      },
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

      // 指标列表
      indicatorOptions: [],
      // 任务审批人
      taskApprovalor: '',

      // 我的审批列表、分页数据
      mySubmitApprovalList: [],
      mySubmitApprovalTotalPage: 0,
      mySubmitApprovalListLoading: false,
      mySubmitApprovalPageIndex: 1,
      mySubmitApprovalPageSize: 10,

      //未完成数据列表、分页数据
      unfinishedTasksList: [],
      unfinishedTasksTotalPage: 0,
      unfinishedTasksListLoading: false,
      unfinishedTasksPageIndex: 1,
      unfinishedTasksPageSize: 10,

      //已完成数据列表、分页数据
      completedTasksList: [],
      completedTasksTotalPage: 0,
      completedTasksListLoading: false,
      completedTasksPageIndex: 1,
      completedTasksPageSize: 10,

      //提交审批对话框
      submitApprovalDialogVisible: false,
      //员工列表
      options: [],


    }
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList();
    this.getUnfinishedTasksList();
    this.getCompletedTasksList();
    this.getMySubmitApprovalList();
  },

  async created() {
    // 获取分组后的员工数据
    // const response = await fetch('/taskmanagement/user/getEmployeesGroupedByDepartment'); // 假设这是你的 API 路由
    // const data = await response.json();

    this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
      method: 'get',
    }).then(({ data }) => {
      console.log(data);
      console.log(1111);

      this.options = data;
      console.log(this.options);
    });


    this.$http({
      url: this.$http.adornUrl(`/indicator/indicatordatadictionary/getIndicatorsList`),
      method: 'get',
    }).then(response => {
      const opt = response.data.map(item => ({
        value: item.value,
        label: item.label
      }));
      console.log(opt);
      this.indicatorOptions = opt;
    })



  },

  methods: {

    showApproverDialog(row) {
      this.approvalForm.taskApprovalor = row.taskAuditor;
      this.approvalForm.taskId = row.taskId;
      // this.fetchApprovers().then(() => {
      this.submitApprovalDialogVisible = true;
      // });
    },



    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/getTasksByUserId'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {

          // console.log("data" + data);

          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },

    // 获取未完成任务
    getUnfinishedTasksList() {
      this.unfinishedTasksListLoading = true
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/getUnfinishedTasksList'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // console.log("data" + data);
          this.unfinishedTasksList = data.page.list
          this.unfinishedTasksTotalPage = data.page.totalCount
        } else {
          this.unfinishedTasksList = []
          this.unfinishedTasksTotalPage = 0
        }
        this.unfinishedTasksListLoading = false
      })
    },
    // 获取已完成任务
    getCompletedTasksList() {
      this.completedTasksListLoading = true
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/getCompletedTasksList'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // console.log("data" + data);
          this.completedTasksList = data.page.list
          this.completedTasksTotalPage = data.page.totalCount
        } else {
          this.completedTasksList = []
          this.completedTasksTotalPage = 0
        }
        this.completedTasksListLoading = false
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

    // 未完成任务-每页数
    unfinishedTasksSizeChangeHandle(val) {
      this.unfinishedTasksPageSize = val
      this.unfinishedTasksPageIndex = 1
      this.getUnfinishedTasksList()
    },
    // 未完成任务-当前页
    unfinishedTasksCurrentChangeHandle(val) {
      this.unfinishedTasksPageIndex = val
      this.getUnfinishedTasksList()
    },

    // 我提交审批-每页数
    mySubmitApprovalSizeChangeHandle(val) {
      this.mySubmitApprovalPageSize = val
      this.mySubmitApprovalPageIndex = 1
      this.getMySubmitApprovalList()
    },
    // 我提交审批-当前页
    mySubmitApprovalCurrentChangeHandle(val) {
      this.mySubmitApprovalPageIndex = val
      this.getMySubmitApprovalList()
    },

    // 已完成任务-每页数
    completedTasksSizeChangeHandle(val) {
      this.completedTasksPageSize = val
      this.completedTasksPageIndex = 1
      this.getCompletedTasksList()
    },
    // 已完成任务-当前页
    completedTasksCurrentChangeHandle(val) {
      this.completedTasksPageIndex = val
      this.getCompletedTasksList()
    },


    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },

    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.taskId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/taskmanagement/task/delete'),
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


    getUserameByUserId(auditorId) {
      for (const category of this.options) {
        for (const auditor of category.options) {
          if (auditor.value === auditorId) {
            return auditor.label;
          }
        }
      }
      return "-";
    },

    getIndicatorNameById(indicatorId) {
      for (const indicator of this.indicatorOptions) {

        console.log("indicator:" + indicator);
        if (indicator.value === indicatorId) {
          return indicator.label;
        }
      }
      return "-";
    },



    finishTask(taskId) {
      console.log("当前任务id" + taskId)
      this.$confirm(`确定完成任务吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`/taskmanagement/task/executeTask/${taskId}`),
          method: 'get',
          data: this.$http.adornData({})
        })
        // window.location.reload();
      })
    },


    // 提交审批
    submitApprover() {
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/submitApprover'),
        method: 'get',
        params: this.$http.adornParams({
          'taskId': this.approvalForm.taskId,
          'taskApprovalor': this.approvalForm.taskApprovalor,
        })
      }).then(({ data }) => {

        if (data && data.code === 0) {
          this.submitApprovalDialogVisible = false;
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.getUnfinishedTasksList()
            }
          })
        } else {
          this.$message.error(data.msg)
        }
      })
    },

    // 获取历史审批列表
    getMySubmitApprovalList() {
      this.mySubmitApprovalListLoading = true
      this.$http({
        // url: this.$http.adornUrl('/taskmanagement/approval/list'),
        url: this.$http.adornUrl('/taskmanagement/approval/getMySubmitApprovalList'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.mySubmitApprovalPageIndex,
          'limit': this.mySubmitApprovalPageSize,
          'key': this.dataForm.key

        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.mySubmitApprovalList = data.page.list
          this.mySubmitApprovalTotalPage = data.page.totalCount
        } else {
          this.mySubmitApprovalList = []
          this.mySubmitApprovalTotalPage = 0
        }
        this.mySubmitApprovalListLoading = false


      })
    },

    taskUpdatePage(taskId){
      this.$router.push({
        // path: '/taskmanagement/task/task-detail-page',
        name: 'task-update-page',
        query: {
          taskId: taskId
        }
      })

    }
    // updatePlanPage(planId) {
    //   // 使用Vue Router进行页面跳转
    //   this.$router.push({
    //     name: 'plan-update-page',
    //     query: { planId } // 如果需要，可以通过query传递参数
    //   });
    // },

  }




}
</script>
