<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="recigetDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="recigetDataList()">查询</el-button>
<!--        <el-button v-if="isAuth('generator:issuemasktable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
<!--        <el-button v-if="isAuth('generator:issuemasktable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
        <el-button v-if="isAuth('generator:issuemasktable:delete')" type="danger" @click="auditHandle()" :disabled="dataListSelections.length <= 0">审核</el-button>
      </el-form-item>
    </el-form>
<!--    <el-dialog-->
<!--      title="审核"-->
<!--      :visible.sync="auditDialogVisible"-->
<!--      width="30%">-->
<!--      <span>请选择审核结果：</span>-->
<!--      <el-radio-group v-model="auditResult">-->
<!--        <el-radio label="approved">审核通过</el-radio>-->
<!--        <el-radio label="rejected">审核不通过</el-radio>-->
<!--      </el-radio-group>-->
<!--      <el-form-item label="审核意见" prop="reviewerOpinion">-->
<!--        <el-input v-model="reviewerOpinion" placeholder="请输入审核意见" clearable></el-input>-->
<!--      </el-form-item>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="auditDialogVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="confirmAudit">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
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
<!--      <el-table-column-->
<!--        prop="issuemaskId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="serialNumber"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="序号">-->
<!--      </el-table-column>-->
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
      </el-table-column>
      <el-table-column
        prop="recipients"
        header-align="center"
        align="center"
        label="接收人">
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
  activated () {
    this.recigetDataList()
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
    // 获取数据列表，审核人可以看见的数据
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
          this.$message({
            message: '审核操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.recigetDataList();
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
    }
    // auditHandle (id) {
    //   var ids = id ? [id] : this.dataListSelections.map(item => {
    //     return item.issuemaskId
    //   })
    //   this.$confirm(`确定审核?`, '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(() => {
    //     this.$http({
    //       url: this.$http.adornUrl('/generator/issuemasktable/audit'),
    //       method: 'post',
    //       data: this.$http.adornData(ids, false)
    //     }).then(({data}) => {
    //       if (data && data.code === 0) {
    //         this.$message({
    //           message: '操作成功',
    //           type: 'success',
    //           duration: 1500,
    //           onClose: () => {
    //             this.recigetDataList();
    //           }
    //         });
    //       } else {
    //         this.$message.error(data.msg);
    //       }
    //     }).catch(error => {
    //       this.$message.error('请求失败，请稍后重试');
    //       console.error(error);
    //     });
    //   }).catch(() => {
    //     this.$message({
    //       type: 'info',
    //       message: '已取消'
    //     });
    //   });
    // }
  }
}
</script>
