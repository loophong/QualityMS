<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="creatorDataList()">
      <!--      <el-form-item>-->
      <!--        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
      <!--      </el-form-item>-->
      <el-form-item>
        <!--        <el-button @click="creatorDataList()">查询</el-button>-->
        <!--        <el-button v-if="isAuth('generator:issuemasktable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
        <el-button v-if="isAuth('generator:issuemasktable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
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
      <el-table-column
        prop="serialNumber"
        header-align="center"
        align="center"
        label="任务编号">
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
        prop="reviewerOpinion"
        header-align="center"
        align="center"
        label="审核意见">
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
        prop="handlingScenarios"
        header-align="center"
        align="center"
        label="处理方案">
      </el-table-column>
      <el-table-column
        prop="annex"
        header-align="center"
        align="center"
        label="附件">
        <template slot-scope="scope">
          <el-button type="text" @click="showFileList(scope.row.annex)">
            预览
          </el-button>
        </template>
      </el-table-column>
      <!--      <el-table-column-->
      <!--        prop="annex"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="附件">-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-button type="text" @click="handleFileAction(scope.row.annex, scope.row.issuemaskId)">-->
      <!--            预览-->
      <!--          </el-button>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
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
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="showButtons" type="text" size="small" @click="addOrUpdateHandle(scope.row.issuemaskId, scope.row.state)">修改</el-button>
          <el-button v-if="showButtons" type="text" size="small" @click="deleteHandle(scope.row.issuemaskId)">删除</el-button>
          <!--          <el-button v-if="showButtons" type="text" size="small" @click="executeHandle(scope.row.issuemaskId)">执行</el-button>-->
          <!--          <el-button v-if="showButtons" type="text" size="small" @click="dispatchHandle(scope.row.issuemaskId)">派发</el-button>-->
          <!-- 完成按钮 -->
          <el-button v-if="showCompleteButton" type="text" size="small" @click="completeHandle(scope.row.issuemaskId)">完成</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 文件列表的弹出框 -->
    <el-dialog
      title="附件预览"
      :visible.sync="fileDialogVisible"
      width="50%">
      <el-table :data="fileList" style="width: 100%">
        <el-table-column prop="name" label="文件名称" align="left"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="previewFile(scope.row.url)">
              点击预览
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fileDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
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
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="creatorDataList"></add-or-update>
    <add-or-update v-if="assertOrUpdateVisible" ref="assetOrUpdate" @refreshDataList="creatorDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './issuemasktable-add-or-update'
export default {
  data () {
    return {
      fileDialogVisible: false, // 控制文件预览弹窗显示
      fileList: [], // 存储当前记录的附件列表
      showButtons: true,
      showCompleteButton: false,
      assertOrUpdateVisible: false,
      dataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      options: '',
      addOrUpdateVisible: false
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
    this.creatorDataList()
  },
  methods: {
    // 显示文件列表弹窗
    showFileList(annex) {
      try {
        if (!annex) {
          this.$message.warning("没有附件可预览！");
          return;
        }
        // 解析数据库中存储的附件信息
        const parsedAnnex = JSON.parse(annex);
        if (Array.isArray(parsedAnnex) && parsedAnnex.length > 0) {
          this.fileList = parsedAnnex;
          this.fileDialogVisible = true; // 打开弹窗
        } else {
          this.$message.warning("附件数据格式不正确！");
        }
      } catch (error) {
        console.error("附件解析失败:", error);
        this.$message.error("附件数据解析失败！");
      }
    },
    // 点击预览具体文件
    previewFile(fileUrl) {
      const token = this.$cookie.get("token"); // 获取当前的 token
      if (!fileUrl) {
        this.$message.warning("文件路径为空，无法预览！");
        return;
      }
      if (!token) {
        this.$message.error("登录已过期，请重新登录！");
        return;
      }
      // 拼接带有 token 的预览地址
      const url = `${this.$http.adornUrl(`/generator/issuetable/${fileUrl}`)}?token=${token}`;
      window.open(url, "_blank");
    },
    handleFileAction(fileflag, id) {
      const token = this.$cookie.get('token'); // 获取当前的 token
      if (!token) {
        console.error('Token not found!');
        return;
      }

      if (!fileflag) {
        // 如果没有 fileflag，弹出警告框
        this.$message({
          message: '没有文件可预览！',
          type: 'warning'
        });
      } else {
        // 拼接带有 token 的请求地址
        const url = `${this.$http.adornUrl(`/generator/issuetable/${fileflag}`)}?token=${token}`;
        window.open(url);
      }
    },
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
    // 任务派发
    dispatchHandle (id) {
      // 派发操作的逻辑
      console.log('派发操作', id)
      this.assertOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.assetOrUpdate.init1(id)
      })
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
    // 获取数据列表，任务创建人可以看见的数据
    creatorDataList () {
      this.dataListLoading = true
      const serialNumber = this.$route.params.serialNumber
      console.log('Successfully fetched issueNumber :', serialNumber)
      this.$http({
        url: this.$http.adornUrl('/generator/issuemasktable/creatormasklist'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key,
          'serialNumber': serialNumber
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
      this.creatorDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.creatorDataList()
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle (id,state) {
      this.addOrUpdateVisible = true
      // console.log('id获取' ,id)
      if(state === '已派发')
        this.$message.error('任务已派发！')
      if(state === '已完成')
        this.$message.error('任务已完成！')
      else {
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      }
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
                this.creatorDataList()
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

