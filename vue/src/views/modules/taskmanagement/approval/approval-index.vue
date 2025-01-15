<template>
  <div class="mod-config">

    <el-tabs type="border-card">
      <el-tab-pane label="待处理审批">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
<!--          <el-form-item>-->
<!--            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button @click="getDataList()">查询</el-button>-->
<!--          </el-form-item>-->
        </el-form>
        <el-table :data="dataList" border v-loading="dataListLoading" style="width: 100%;">
          <el-table-column prop="approvalId" header-align="center" align="center" label="审批编号">
          </el-table-column>
          <el-table-column prop="taskId" header-align="center" align="center" label="任务编号">
          </el-table-column>
          <el-table-column prop="taskName" header-align="center" align="center" label="任务名">
          </el-table-column>
          <el-table-column prop="taskContent" header-align="center" align="center" label="任务内容">
          </el-table-column>
          <el-table-column prop="approvalType" label="审批类型" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.approvalType === 'FINISH'">
                <el-tag type="success" disable-transitions>完成</el-tag></span>
              <span v-else-if="scope.row.approvalType === 'NEW'">
                <el-tag type="info" disable-transitions>新建</el-tag></span>
              <span v-else-if="scope.row.approvalType === 'UPDATE'">
                <el-tag type="warning" disable-transitions>修改</el-tag></span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
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
              <el-button type="text" size="small" @click="approvalDialogHandle(scope.row.approvalId)">审批</el-button>
              <!-- <el-button type="text" size="small" @click="deleteHandle(scope.row.approvalId)">删除</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>


      </el-tab-pane>

      <el-tab-pane label="已处理审批">

        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getHistoryApprovalList()">
<!--          <el-form-item>-->
<!--            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button @click="getHistoryApprovalList()">查询</el-button>-->
<!--          </el-form-item>-->
        </el-form>
        <el-table :data="historyDataList" border v-loading="historyDataListLoading" style="width: 100%;">
          <el-table-column prop="approvalId" header-align="center" align="center" label="审批编号">
          </el-table-column>
          <el-table-column prop="taskId" header-align="center" align="center" label="任务ID">
          </el-table-column>
          <el-table-column prop="taskName" header-align="center" align="center" label="任务名">
          </el-table-column>
          <el-table-column prop="taskContent" header-align="center" align="center" label="任务内容">
          </el-table-column>
          <el-table-column prop="approvalType" label="审批类型" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.approvalType === 'FINISH'">
                <el-tag type="success" disable-transitions>完成</el-tag></span>
              <span v-else-if="scope.row.approvalType === 'NEW'">
                <el-tag type="info" disable-transitions>新建</el-tag></span>
              <span v-else-if="scope.row.approvalType === 'UPDATE'">
                <el-tag type="warning" disable-transitions>修改</el-tag></span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
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
        <el-pagination @size-change="historySizeChangeHandle" @current-change="historyCurrentChangeHandle" :current-page="historyPageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="historyPageSize" :total="historyTotalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

      </el-tab-pane>
    </el-tabs>


    <!-- <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('taskmanagement:approval:save')" type="primary"
          @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('taskmanagement:approval:delete')" type="danger" @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form> -->
    <!-- <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column>
      <el-table-column prop="approvalId" header-align="center" align="center" label="审批编号">
      </el-table-column>
      <el-table-column prop="taskId" header-align="center" align="center" label="任务ID">
      </el-table-column>
      <el-table-column prop="taskName" header-align="center" align="center" label="任务名">
      </el-table-column>
      <el-table-column prop="taskContent" header-align="center" align="center" label="任务内容">
      </el-table-column> -->
    <!-- <el-table-column prop="taskStartDate" header-align="center" align="center" label="开始日期">
      </el-table-column> -->
    <!-- <el-table-column prop="taskAssociatedPlanId" header-align="center" align="center" label="相关计划编号">
      </el-table-column> -->
    <!-- <el-table-column prop="taskPrincipal" header-align="center" align="center" label="负责人">
      </el-table-column> -->
    <!-- <el-table-column prop="taskPrincipal" header-align="center" align="center" label="负责人">
        <template slot-scope="scope">
          {{ getUserameByUserId(scope.row.taskPrincipal) }}
        </template>
      </el-table-column> -->
    <!-- <el-table-column prop="taskExecutor" header-align="center" align="center" label="执行人">
      </el-table-column> -->
    <!-- <el-table-column prop="taskAssociatedIndicatorsId" header-align="center" align="center" label="关联指标编号">
      </el-table-column>
      <el-table-column prop="taskSubmissionTime" header-align="center" align="center" label="任务送审时间">
      </el-table-column> -->
    <!-- <el-table-column prop="approvalEndTime" header-align="center" align="center" label="审批结束时间">
      </el-table-column> -->
    <!-- <el-table-column prop="approver" header-align="center" align="center" label="审批人">
      </el-table-column> -->
    <!-- <el-table-column prop="submitter" header-align="center" align="center" label="送审人">
      </el-table-column> -->
    <!-- <el-table-column prop="submitter" header-align="center" align="center" label="送审人">
        <template slot-scope="scope">
          {{ getUserameByUserId(scope.row.submitter) }}
        </template>
      </el-table-column> -->
    <!-- <el-table-column prop="approvalStatus" header-align="center" align="center" label="审批状态">
      </el-table-column> -->
    <!-- <el-table-column prop="approvalStatus" label="审批状态" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.approvalStatus === 'PENDING'">
            <el-tag type="info" disable-transitions>待审批</el-tag></span>
          <span v-else-if="scope.row.approvalStatus === 'APPROVED'">
            <el-tag type="success" disable-transitions>已通过</el-tag></span>
          <span v-else-if="scope.row.approvalStatus === 'REJECTED'">
            <el-tag type="danger" disable-transitions>已拒绝</el-tag></span>
          <span v-else>-</span>
          处理未知状态
        </template>
      </el-table-column> -->

    <!-- <el-table-column prop="approvalComments" header-align="center" align="center" label="审批意见">
      </el-table-column>
      <el-table-column prop="remarks" header-align="center" align="center" label="备注">
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.approvalId)">审批</el-button> -->
    <!-- <el-button type="text" size="small" @click="deleteHandle(scope.row.approvalId)">删除</el-button> -->
    <!-- </template>
      </el-table-column>
    </el-table>  -->
    <!-- <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination> -->
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>

    <approval-dialog v-if="approvalDialogVisible" ref="approvalDialog" @refreshDataList="reloadDataList"></approval-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './approval-add-or-update'
import ApprovalDialog from './approval-dialog'
export default {
  data() {
    return {
      dataForm: {
        key: ''
      },


      dataListSelections: [],
      addOrUpdateVisible: false,

      // 审批
      approvalDialogVisible: false,

      // 待审批列表、分页
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,

      // 已审批列表、分页
      historyDataList: [],
      historyTotalPage: 0,
      historyPageIndex: 1,
      historyPageSize: 10,
      historyDataListLoading: false,




    }
  },
  components: {
    AddOrUpdate,
    ApprovalDialog
  },
  activated() {
    this.getDataList(),
      this.getHistoryApprovalList()
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
    reloadDataList(){
      this.getDataList()
      this.getHistoryApprovalList()
    },

    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        // url: this.$http.adornUrl('/taskmanagement/approval/list'),
        url: this.$http.adornUrl('/taskmanagement/approval/pendingApprovalsList'),


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

    // 获取历史审批列表
    async getHistoryApprovalList() {
      this.historyDataListLoading = true
      await this.$http({
        // url: this.$http.adornUrl('/taskmanagement/approval/list'),
        url: this.$http.adornUrl('/taskmanagement/approval/historyApprovalList'),


        method: 'get',
        params: this.$http.adornParams({
          'page': this.historyPageIndex,
          'limit': this.historyPageSize,
          'key': this.dataForm.key
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.historyDataList = data.page.list
          this.historyTotalPage = data.page.totalCount
        } else {
          this.historyDataList = []
          this.historyTotalPage = 0
        }
        this.historyDataListLoading = false
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

    // 历史审批记录-每页数
    historySizeChangeHandle(val) {
      this.historyPageSize = val
      this.historyPageIndex = 1
      this.getHistoryApprovalList()
    },
    // 历史审批记录-当前页
    historyCurrentChangeHandle(val) {
      this.historyPageIndex = val
      this.getHistoryApprovalList()
    },

    // 多选
    // selectionChangeHandle(val) {
    //   this.dataListSelections = val
    // },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },

    // 审批弹窗
     approvalDialogHandle(id) {
      this.approvalDialogVisible = true
      this.$nextTick(() => {
        this.$refs.approvalDialog.init(id)
      })
    },

    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.approvalId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/taskmanagement/approval/delete'),
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
  }
}
</script>
