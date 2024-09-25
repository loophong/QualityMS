<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('taskmanagement:plan:save')" type="primary" @click="addPlanPage()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column> -->
      <el-table-column prop="planId" header-align="center" align="center" label="计划编号" width="100">
      </el-table-column>
      <el-table-column prop="planName" header-align="center" align="center" label="计划名" width="150">
      </el-table-column>
      <el-table-column prop="planContent" header-align="center" align="center" label="计划内容">
      </el-table-column>
      <el-table-column prop="planStartDate" header-align="center" align="center" label="开始日期" width="110">
      </el-table-column>
      <el-table-column prop="planScheduleCompletionDate" header-align="center" align="center" label="计划完成日期" width="110">
      </el-table-column>
      <el-table-column prop="planScheduleDays" header-align="center" align="center" label="计划天数">
      </el-table-column>
      <el-table-column prop="planActualCompletionDate" header-align="center" align="center" label="实际完成日期" width="110">
        <template slot-scope="scope">
          <span v-if="scope.row.planActualCompletionDate === null" style="color: gray;">-</span>
        </template>
      </el-table-column>
      <el-table-column prop="planActualDays" header-align="center" align="center" label="实际天数">
        <template slot-scope="scope">
          <span v-if="scope.row.planActualDays === null" style="color: gray;">-</span>
        </template>
      </el-table-column>
      <el-table-column prop="planTasksAssignment" header-align="center" align="center" label="任务派发">
      </el-table-column>
      <el-table-column prop="planAssociatedTasksId" header-align="center" align="center" label="关联任务编号" width="110">
      </el-table-column>
      <el-table-column prop="planSchedule" header-align="center" align="center" label="进度">
      </el-table-column>
      <el-table-column prop="planPrincipal" header-align="center" align="center" label="负责人">
        <template slot-scope="scope">
          {{ getUserameByUserId(scope.row.planPrincipal) }}
        </template>
      </el-table-column>

      <el-table-column prop="planExecutor" header-align="center" align="center" label="执行人" width="110">
        <template slot-scope="scope">
          <span v-for="(executorId, index) in scope.row.planExecutor" :key="executorId">
            {{ getUserameByUserId(executorId) }}
            <span v-if="index !== scope.row.planExecutor.length - 1">, </span>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="planAuditor" header-align="center" align="center" label="审核人">
        <template slot-scope="scope">
          {{ getUserameByUserId(scope.row.planAuditor) }}
        </template>
      </el-table-column>
      <!-- <el-table-column prop="planCurrentState" header-align="center" align="center" label="当前状态">
      </el-table-column>
      <el-table-column prop="planIsCompleted" header-align="center" align="center" label="是否完成">
      </el-table-column>
      <el-table-column prop="planIsOverdue" header-align="center" align="center" label="是否超期">
      </el-table-column>
      <el-table-column prop="planIsOnTime" header-align="center" align="center" label="按时完工">
      </el-table-column> -->

      <!-- <el-table-column prop="planCurrentState" label="当前状态" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.planCurrentState === 0" style="color: gray;">未开始</span>
          <span v-else-if="scope.row.planCurrentState === 1" style="color: gray;">进行中</span>
          <span v-else-if="scope.row.planCurrentState === 2" style="color: green;">已完成</span>
          <span v-else>-</span> 
        </template>
      </el-table-column> -->
      <el-table-column prop="planCurrentState" label="当前状态" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.planCurrentState === 'NOT_STARTED'">
            <el-tag type="info" disable-transitions>未开始</el-tag></span>
          <span v-else-if="scope.row.planCurrentState === 'IN_PROGRESS'">
            <el-tag disable-transitions>进行中</el-tag></span>
          <span v-else-if="scope.row.planCurrentState === 'APPROVAL_IN_PROGRESS'">
            <el-tag disable-transitions>审批中</el-tag></span>
          <span v-else-if="scope.row.planCurrentState === 'OVERDUE'">
            <el-tag type="danger" disable-transitions>已逾期</el-tag></span>
          <span v-else-if="scope.row.planCurrentState === 'COMPLETED'">
            <el-tag type="success" disable-transitions>已完成</el-tag></span>
          <span v-else>-</span> <!-- 处理未知状态 -->
        </template>
      </el-table-column>
      <el-table-column prop="planIsCompleted" label="是否完成" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.planIsCompleted === 0" style="color: gray;">否</span>
          <span v-else-if="scope.row.planIsCompleted === 1" style="color: gray;">是</span>
          <span v-else>-</span> <!-- 处理未知状态 -->
        </template>
      </el-table-column>
      <el-table-column prop="planIsOverdue" label="是否超期" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.planIsOverdue === 0" style="color: gray;">否</span>
          <span v-else-if="scope.row.planIsOverdue === 1" style="color: gray;">是</span>
          <span v-else>-</span> <!-- 处理未知状态 -->
        </template>
      </el-table-column>
      <el-table-column prop="planIsOnTime" label="是否按时完工" header-align="center" align="center" width="110">
        <template slot-scope="scope">
          <span v-if="scope.row.planIsOnTime === 0" style="color: DeepSkyBlue;">否</span>
          <span v-else-if="scope.row.planIsOnTime === 1" style="color: blue;">是</span>
          <span v-else>-</span> <!-- 处理未知状态 -->
        </template>
      </el-table-column>
      <el-table-column prop="planEarlyCompletionDays" header-align="center" align="center" label="提前完工">
        <template slot-scope="scope">
          {{ scope.row.planEarlyCompletionDays !== null && scope.row.planEarlyCompletionDays !== undefined &&
            scope.row.planEarlyCompletionDays !== '' ? scope.row.planEarlyCompletionDays : '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="planLagDays" header-align="center" align="center" label="滞后天数">
        <template slot-scope="scope">
          {{ scope.row.planLagDays !== null && scope.row.planLagDays !== undefined && scope.row.planLagDays !== '' ?
            scope.row.planLagDays : '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="planLagReasons" header-align="center" align="center" label="滞后原因">
        <template slot-scope="scope">
          {{ scope.row.planLagReasons !== null && scope.row.planLagReasons !== undefined && scope.row.planLagReasons !==
            '' ? scope.row.planLagReasons : '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="planAssociatedIndicatorsId" header-align="center" align="center" label="关联指标编号" width="110">
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="showPlanTree(scope.row.planId)">查看结构</el-button>
          <el-button type="text" size="small" @click="updatePlanPage(scope.row.planId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row)">删除</el-button>
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
import AddOrUpdate from './taskmanagementplan-add-or-update'
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

      //员工列表
      options: [],
    }
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList()
  },
  async created() {
    // 获取分组后的员工数据
    this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
      method: 'get',
    }).then(({ data }) => {
      this.options = data;
      console.log(this.options);
    })
  },


  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/plan/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
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
    // addOrUpdateHandle(id) {
    //   this.addOrUpdateVisible = true
    //   this.$nextTick(() => {
    //     this.$refs.addOrUpdate.init(id)
    //   })
    // },
    addPlanPage() {
      this.$router.push({
        name: 'plan-add-page',
      });
    },
    // addPlanPage() {
    //   // 获取当前域名和端口
    //   const baseUrl = window.location.origin;
    //   // 指定要打开的新标签页的完整 URL
    //   const url = `${baseUrl}/#/plan-add-page`;
    //   // 使用 window.open 打开新标签页
    //   window.open(url, '_blank');
    // },
    updatePlanPage(planId) {
      // 使用Vue Router进行页面跳转
      this.$router.push({
        name: 'plan-update-page',
        query: { planId } // 如果需要，可以通过query传递参数
      });
    },

    showPlanTree(planId) {
      // 使用Vue Router进行页面跳转
      this.$router.push({
        name: 'plan-tree',
        query: { planId } // 如果需要，可以通过query传递参数
      });
    },

    // 删除
    deleteHandle(row) {
      this.$confirm(`确定删除计划[${row.planName}]?删除计划将同时删除其子任务`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/taskmanagement/plan/delete/'+row.planId),
          method: 'get',
          params: this.$http.adornParams()
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
  }
}
</script>
