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
        <el-table :data="unfinishedTasksList" border v-loading="unfinishedTasksListLoading" @selection-change="selectionChangeHandle"
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
          <!-- <el-table-column prop="taskCurrentState" label="当前状态" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.taskCurrentState === 0" style="color: gray;">未开始</span>
          <span v-else-if="scope.row.taskCurrentState === 1" style="color: gray;">进行中</span>
          <span v-else-if="scope.row.taskCurrentState === 2" style="color: green;">已完成</span>
          <span v-else>{{ scope.row.taskCurrentState }}</span>
        </template>
      </el-table-column> -->
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
              <el-button type="text" size="small" @click="finishTask(scope.row.taskId)">完成</el-button>
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.tmTid)">修改</el-button>
              <el-button type="text" size="small" @click="deleteHandle(scope.row.tmTid)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

      </el-tab-pane>


      <el-tab-pane label="审批记录">审批记录</el-tab-pane>

      <el-tab-pane label="历史任务">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getCompletedTasksList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getCompletedTasksList()">查询</el-button>
            <el-button v-if="isAuth('taskmanagement:ask:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
            <el-button v-if="isAuth('taskmanagement:task:delete')" type="danger" @click="deleteHandle()"
              :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
          <!-- <el-table-column prop="taskCurrentState" label="当前状态" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.taskCurrentState === 0" style="color: gray;">未开始</span>
          <span v-else-if="scope.row.taskCurrentState === 1" style="color: gray;">进行中</span>
          <span v-else-if="scope.row.taskCurrentState === 2" style="color: green;">已完成</span>
          <span v-else>{{ scope.row.taskCurrentState }}</span>
        </template>
      </el-table-column> -->
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
              <el-button type="text" size="small" @click="finishTask(scope.row.taskId)">完成</el-button>
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.tmTid)">修改</el-button>
              <el-button type="text" size="small" @click="deleteHandle(scope.row.tmTid)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </el-tab-pane>
    </el-tabs>

    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('taskmanagement:ask:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('taskmanagement:task:delete')" type="danger" @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
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
      <!-- <el-table-column prop="taskCurrentState" label="当前状态" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.taskCurrentState === 0" style="color: gray;">未开始</span>
          <span v-else-if="scope.row.taskCurrentState === 1" style="color: gray;">进行中</span>
          <span v-else-if="scope.row.taskCurrentState === 2" style="color: green;">已完成</span>
          <span v-else>{{ scope.row.taskCurrentState }}</span>
        </template>
      </el-table-column> -->
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
          <el-button type="text" size="small" @click="finishTask(scope.row.taskId)">完成</el-button>
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.tmTid)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.tmTid)">删除</el-button>
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
// import AddOrUpdate from './task-add'
import AddOrUpdate from './task-update'
// import updateTask from './task-update'
export default {
  data() {
    return {
      dataForm: {
        key: ''
      },
      dataList: [],
      unfinishedTasksList: [],
      completedTasksList: [],

      pageIndex: 1,
      pageSize: 10,

      totalPage: 0,
      unfinishedTasksTotalPage: 0,
      completedTasksTotalPage: 0,

      dataListLoading: false,
      unfinishedTasksListLoading: false,
      completedTasksListLoading: false,

      dataListSelections: [],
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList(),
    this.getUnfinishedTasksList(),
    this.getCompletedTasksList()
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
      // if (data && data.code === 0) {
      //   console.log(data);
      // } 
    })

    // console.log(data);
    // this.options = data;
  },

  methods: {
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

          console.log("data" + data);

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
          console.log("data" + data);
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
          console.log("data" + data);
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
    }
  }
}
</script>
  