<template>
  <div class="mod-config">
    <el-tabs type="border-card" v-model="activeName">
      <el-tab-pane label="待处理审批" name="1">

      <el-form :inline="true" :model="dataForm" @keyup.enter.native="recigetDataList()">
<!--      <el-form-item>-->
<!--        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
<!--      </el-form-item>-->
      <el-form-item v-if="activeName == '1'">
<!--        <el-button @click="recigetDataList()">查询</el-button>-->
<!--        <el-button v-if="isAuth('generator:issuemasktable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
<!--        <el-button v-if="isAuth('generator:issuemasktable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
        <el-button v-if="isAuth('generator:issuemasktable:delete')" type="danger" @click="auditHandle()" :disabled="dataListSelections.length <= 0">审核</el-button>
      </el-form-item>
    </el-form>
      <el-dialog
      title="审核"
      :visible.sync="auditDialogVisible"
      width="30%">
      <el-form :model="dataForm">
        <span>请选择审核结果：</span>
        <el-radio-group v-model="auditResult">
          <el-radio label="approved">审核通过</el-radio>
          <el-radio label="rejected">审核不通过</el-radio>
        </el-radio-group>
        <el-form-item label="审核意见" prop="reviewerOpinion">
          <el-input v-model="reviewerOpinion" placeholder="请输入审核意见" clearable></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmAudit">确 定</el-button>
      </div>
      </el-dialog>



        <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="issueNumber"
        header-align="center"
        align="center"
        label="问题编号(所属问题)">
      </el-table-column>
      <el-table-column
        prop="reviewers"
        header-align="center"
        align="center"
        label="审核人">
        <template slot-scope="scope">
          {{ getUsernameByUserId(scope.row.reviewers) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="recipients"
        header-align="center"
        align="center"
        label="接收人">
        <template slot-scope="scope">
          {{ getUsernameByUserId(scope.row.recipients) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="maskcontent"
        header-align="center"
        align="center"
        label="任务内容">
      </el-table-column>
      <el-table-column
        prop="creator"
        header-align="center"
        align="center"
        label="任务发起人">
        <template slot-scope="scope">
          {{ getUsernameByUserId(scope.row.creator) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="creationTime"
        header-align="center"
        align="center"
        label="发起时间">
      </el-table-column>
      <el-table-column
        prop="requiredCompletionTime"
        header-align="center"
        align="center"
        label="要求完成时间">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="state"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="状态">-->
<!--      </el-table-column>-->
      <el-table-column prop="state" header-align="center" align="center" label="状态">
        <template slot-scope="scope">
    <span v-if="scope.row.state === '审核中'">
      <el-tag type="info" disable-transitions>审核中</el-tag>
    </span>
          <span v-else-if="scope.row.state === '执行中'">
      <el-tag type="primary" disable-transitions>执行中</el-tag>
    </span>
          <span v-else-if="scope.row.state === '未通过审核'">
      <el-tag type="danger" disable-transitions>未通过审核</el-tag>
    </span>
          <span v-else-if="scope.row.state === '已派发'">
      <el-tag type="warning" disable-transitions>已派发</el-tag>
    </span>
          <span v-else-if="scope.row.state === '已完成'">
      <el-tag type="success" disable-transitions>已完成</el-tag>
    </span>
          <span v-else>-</span> <!-- 处理未知状态 -->
        </template>
      </el-table-column>
<!--      <el-table-column-->
<!--        fixed="right"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        width="150"-->
<!--        label="操作">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button v-if="showButtons" type="text" size="small" @click="addOrUpdateHandle(scope.row.issuemaskId)">修改</el-button>-->
<!--          <el-button v-if="showButtons" type="text" size="small" @click="deleteHandle(scope.row.issuemaskId)">删除</el-button>-->
<!--          <el-button v-if="showButtons" type="text" size="small" @click="executeHandle(scope.row.issuemaskId)">执行</el-button>-->
<!--          <el-button v-if="showButtons" type="text" size="small" @click="dispatchHandle(scope.row.issuemaskId)">审核</el-button>-->
          <!-- 完成按钮 -->
<!--          <el-button v-if="showCompleteButton" type="text" size="small" @click="completeHandle(scope.row.issuemaskId)">完成</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>
        <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>

      </el-tab-pane>

      <el-tab-pane label="已处理审批" name="2">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="recigetedDataList()">
          <!--      <el-form-item>-->
          <!--        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
          <!--      </el-form-item>-->
          <el-form-item>
            <!--        <el-button @click="recigetDataList()">查询</el-button>-->
            <!--        <el-button v-if="isAuth('generator:issuemasktable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
            <!--        <el-button v-if="isAuth('generator:issuemasktable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
          </el-form-item>
        </el-form>
        <el-table
          :data="historyDataList"
          border
          v-loading="historyDataListLoading"
          @selection-change="selectionChangeHandle"
          style="width: 100%;">
          <el-table-column
            type="selection"
            header-align="center"
            align="center"
            width="50">
          </el-table-column>
          <el-table-column
            prop="issueNumber"
            header-align="center"
            align="center"
            label="问题编号(所属问题)">
          </el-table-column>
          <el-table-column
            prop="reviewers"
            header-align="center"
            align="center"
            label="审核人">
            <template slot-scope="scope">
              {{ getUsernameByUserId(scope.row.reviewers) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="recipients"
            header-align="center"
            align="center"
            label="接收人">
            <template slot-scope="scope">
              {{ getUsernameByUserId(scope.row.recipients) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="maskcontent"
            header-align="center"
            align="center"
            label="任务内容">
          </el-table-column>
          <el-table-column
            prop="creator"
            header-align="center"
            align="center"
            label="任务发起人">
            <template slot-scope="scope">
              {{ getUsernameByUserId(scope.row.creator) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="creationTime"
            header-align="center"
            align="center"
            label="发起时间">
          </el-table-column>
          <el-table-column
            prop="requiredCompletionTime"
            header-align="center"
            align="center"
            label="要求完成时间">
          </el-table-column>
          <!--      <el-table-column-->
          <!--        prop="state"-->
          <!--        header-align="center"-->
          <!--        align="center"-->
          <!--        label="状态">-->
          <!--      </el-table-column>-->
          <el-table-column prop="state" header-align="center" align="center" label="状态">
            <template slot-scope="scope">
    <span v-if="scope.row.state === '审核中'">
      <el-tag type="info" disable-transitions>审核中</el-tag>
    </span>
              <span v-else-if="scope.row.state === '执行中'">
      <el-tag type="primary" disable-transitions>执行中</el-tag>
    </span>
              <span v-else-if="scope.row.state === '未通过审核'">
      <el-tag type="danger" disable-transitions>未通过审核</el-tag>
    </span>
              <span v-else-if="scope.row.state === '已派发'">
      <el-tag type="warning" disable-transitions>已派发</el-tag>
    </span>
              <span v-else-if="scope.row.state === '已完成'">
      <el-tag type="success" disable-transitions>已完成</el-tag>
    </span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <!--      <el-table-column-->
          <!--        fixed="right"-->
          <!--        header-align="center"-->
          <!--        align="center"-->
          <!--        width="150"-->
          <!--        label="操作">-->
          <!--        <template slot-scope="scope">-->
          <!--          <el-button v-if="showButtons" type="text" size="small" @click="addOrUpdateHandle(scope.row.issuemaskId)">修改</el-button>-->
          <!--          <el-button v-if="showButtons" type="text" size="small" @click="deleteHandle(scope.row.issuemaskId)">删除</el-button>-->
          <!--          <el-button v-if="showButtons" type="text" size="small" @click="executeHandle(scope.row.issuemaskId)">执行</el-button>-->
          <!--          <el-button v-if="showButtons" type="text" size="small" @click="dispatchHandle(scope.row.issuemaskId)">审核</el-button>-->
          <!-- 完成按钮 -->
          <!--          <el-button v-if="showCompleteButton" type="text" size="small" @click="completeHandle(scope.row.issuemaskId)">完成</el-button>-->
          <!--        </template>-->
          <!--      </el-table-column>-->
        </el-table>
        <el-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </el-tab-pane>
    </el-tabs>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="recigetDataList"></add-or-update>
    <add-or-update v-if="assertOrUpdateVisible" ref="assetOrUpdate" @refreshDataList="recigetDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './issuemasktable-add-or-update'
export default {
  data () {
    return {
      activeName: '1', // 默认显示待处理审批
      auditIds: [], // 保存要审核的 ID
      auditResult: 'approved', // 默认审核结果为“通过”
      auditDialogVisible: false, // 控制审核弹窗可见性
      showButtons: true,
      showCompleteButton: false,
      assertOrUpdateVisible: false,
      reviewerOpinion: '',
      dataForm: {
        key: ''
      },
      dataList: [],
      historyDataList: [], // 历史数据列表
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      historyDataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      options: '',
    }
  },
  components: {
    AddOrUpdate
  },
  created() {
    this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
      method: 'get',
    }).then(({ data }) => {
      this.options = data;
      // console.log("所有的用户信息" ,data);
    })
  },
  activated () {
    this.recigetDataList()
    this.recigetedDataList()
    console.log((this.isAuth('generator:issuemasktable:delete') && this.activeName == '1'))
  },
  methods: {
    executeHandle (id) {
      this.$confirm('是否确认执行此操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确认执行操作的逻辑
        console.log('执行操作', id)
        this.$message({
          type: 'success',
          message: '执行成功!'
        })
        // 隐藏执行和派发按钮
        this.showButtons = false
        this.showCompleteButton = true
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消执行'
        })
      })
    },
    // 任务审核
    // 获取数据列表，审核人可以看见的未审核数据
    recigetDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/generator/issuemasktable/Auditlist'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        })
      }).then(({data}) => {
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
    // 获取数据列表，审核人可以看见的已审核数据
    recigetedDataList () {
      this.historyDataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/generator/issuemasktable/Auditedlist'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.historyDataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.historyDataList = []
          this.totalPage = 0
        }
        this.historyDataListLoading = false
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.recigetDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.recigetDataList()
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle (id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.issuemaskId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/generator/issuemasktable/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.recigetDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    }, // 审核处理逻辑 - 修改以显示弹窗
    // auditHandle (id) {
    //   var ids = id ? [id] : this.dataListSelections.map(item => item.issuemaskId);
    //   // 弹窗显示
    //   this.auditDialogVisible = true;
    //
    //   // 在此可以存储要审核的 ID 以备后续使用
    //   this.auditIds = ids; // 假设 auditIds 是你用于后续审核的状态
    // },
    auditHandle(id) {
      // 如果 id 存在，表示单个审核；否则取选中的列表
      var ids = id ? [id] : this.dataListSelections.map(item => item.issuemaskId);

      // 将 IDs 转换为逗号分隔的字符串
      const auditIds = ids.join(",");

      // 显示审核确认弹窗
      this.auditDialogVisible = true; // 打开弹窗
      this.auditIds = auditIds; // 保存审核的 ID 字符串
    },

    // confirmAudit() {
    //   // 发送审核请求
    //   this.$http({
    //     url: this.$http.adornUrl('/generator/issuemasktable/audit'),
    //     method: 'post',
    //     params: {
    //       issuemaskIds: this.auditIds, // 传递逗号分隔的任务 ID
    //       reviewerOpinion: this.reviewerOpinion, // 传递审核意见
    //       result: this.auditResult // 传递审核结果
    //     }
    //   }).then(({ data }) => {
    //     if (data && data.code === 0) {
    //       this.$message({
    //         message: '审核操作成功',
    //         type: 'success',
    //         duration: 1500,
    //         onClose: () => {
    //           this.recigetDataList();
    //         }
    //       });
    //     } else {
    //       this.$message.error(data.msg);
    //     }
    //   }).catch(error => {
    //     this.$message.error('请求失败，请稍后重试');
    //     console.error(error);
    //   }).finally(() => {
    //     this.auditDialogVisible = false; // 关闭弹窗
    //   });
    // }
    confirmAudit() {
      // 发送审核请求
      this.$http({
        url: this.$http.adornUrl('/generator/issuemasktable/audit'),
        method: 'post',
        params: {
          issuemaskIds: this.auditIds, // 传递逗号分隔的任务 ID
          reviewerOpinion: this.reviewerOpinion, // 传递审核意见
          result: this.auditResult // 传递审核结果
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // 如果审核通过，发送消息通知
          if (this.auditResult === 'approved') {
            this.sendMessageNotification();
          }
          this.$message({
            message: '审核操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.recigetDataList();
              this.recigetedDataList();
            }
          });
        } else {
          this.$message.error(data.msg);
        }
      }).catch(error => {
        this.$message.error('请求失败，请稍后重试');
        console.error(error);
      }).finally(() => {
        this.auditDialogVisible = false; // 关闭弹窗
      });
    },
    getUsernameByUserId(auditorId) {
      for (const category of this.options) {
        for (const auditor of category.options) {
          if (auditor.value === auditorId) {
            return auditor.label;
          }
        }
      }
      return "-";
    },
    sendMessageNotification() {
      const receivers = this.dataListSelections.map(item => Number(item.recipients)); // 获取选中任务的接收人
      const senderIds = this.dataListSelections.map(item => Number(item.creator)); // 获取发起人ID
      const contents = this.dataListSelections.map(item => item.maskcontent); // 获取任务内容
      // const senderId = this.getUserIdByUsername(this.dataList[0].creator); // 获取发起人ID
      console.log("发送人的id为 ：" ,this.dataListSelections);

      // 遍历接收人，发送消息通知
      receivers.forEach((receiverId, index) => {
        const messageNotification = {
          receiverId: receiverId, // 根据映射获取接收人ID
          senderId: senderIds[index], // 获取对应的发起人ID
          content: '任务内容：' + contents[index], // 获取对应的任务内容
          type: '任务执行通知', // 消息类型
          jumpdepart: '3' // 跳转部门
        };

        this.$http({
          url: this.$http.adornUrl('/notice/save'),
          method: 'post',
          data: messageNotification
        }).then(({ data }) => {
          if (data && data.code !== 0) {
            console.error('消息通知发送失败:', data.msg);
          }
        });
      });
    },
    getUserIdByUsername(username) {
      for (const category of this.options) {
        for (const auditor of category.options) {
          if (auditor.label === username) { // 根据标签匹配
            return auditor.value; // 返回对应的ID
          }
        }
      }
      return null; // 如果没有找到，返回null
    },



  }
}
</script>
<style scoped>
/* 表格的样式 */
.el-table {
  font-size: 16px; /* 放大文字字体 */
  border: 2px solid #ccc; /* 加粗边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 增加阴影效果 */
  border-radius: 10px; /* 可选：使表格边角变圆滑 */
}

/* 表格头部文字的样式 */
.el-table th {
  font-size: 18px; /* 放大表头的字体 */
  font-weight: bold; /* 加粗表头文字 */
  color: #333; /* 表头文字颜色 */
  border-bottom: 2px solid #ccc; /* 表头的下边框 */
}

/* 表格单元格文字样式 */
.el-table td {
  font-size: 16px; /* 调整单元格字体大小 */
  color: #333; /* 单元格文字颜色 */
}

/* 设置选中行的样式 */
.el-table .el-table__row--striped:hover {
  background-color: #f5f7fa; /* 表格行的悬停背景颜色 */
}

/* 调整表格分页器的样式 */
.el-pagination {
  margin-top: 10px;
  font-size: 14px; /* 分页器字体大小 */
}
</style>

