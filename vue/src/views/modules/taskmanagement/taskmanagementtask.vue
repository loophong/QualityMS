<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('taskmanagement:task:save')" type="primary"
          @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('taskmanagement:task:delete')" type="danger" @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column>
      <el-table-column prop="tmTid" header-align="center" align="center" label="主键">
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
      </el-table-column>
      <el-table-column prop="taskExecutor" header-align="center" align="center" label="执行人">
      </el-table-column>
      <el-table-column prop="taskAuditor" header-align="center" align="center" label="审核人">
      </el-table-column>
      <el-table-column prop="taskCurrentState" header-align="center" align="center" label="当前状态">
      </el-table-column>
      <el-table-column prop="taskIsCompleted" header-align="center" align="center" label="是否完成">
      </el-table-column>
      <el-table-column prop="taskIsOverdue" header-align="center" align="center" label="是否超期">
      </el-table-column>
      <el-table-column prop="taskIsOnTime" header-align="center" align="center" label="按时完工">
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
// import AddOrUpdate from './taskmanagementtask-add-or-update'
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
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/list'),
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
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.tmTid
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
    }
  }
}
</script>
