<template>
  <div class="mod-config">
    <el-form v-if="showForm" :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
<!--      <el-form-item>-->
<!--        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
<!--      </el-form-item>-->
      <el-form-item>
<!--        <el-button @click="getDataList()">查询</el-button>-->
<!--        <el-button v-if="isAuth('generator:issuetable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
        <el-button v-if="isAuth('generator:issuetable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table
      v-if="showForm"
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
      <!--        prop="issueId"-->
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
        label="问题编号">
      </el-table-column>
      <el-table-column
        prop="inspectionDepartment"
        header-align="center"
        align="center"
        label="检查科室">
      </el-table-column>
      <el-table-column
        prop="inspectionDate"
        header-align="center"
        align="center"
        label="检查日期">
      </el-table-column>
      <el-table-column
        prop="issueCategoryId"
        header-align="center"
        align="center"
        label="问题类别">
      </el-table-column>
      <el-table-column
        prop="vehicleTypeId"
        header-align="center"
        align="center"
        label="车型">
      </el-table-column>
      <el-table-column
        prop="vehicleNumberId"
        header-align="center"
        align="center"
        label="车号">
      </el-table-column>
      <el-table-column
        prop="issueDescription"
        header-align="center"
        align="center"
        label="问题描述">
      </el-table-column>

      <el-table-column
        prop="issuePhoto"
        header-align="center"
        align="center"
        label="问题照片">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="previewImage(scope.row.issuePhoto)">下载</el-button>
        </template>
      </el-table-column>
      <!--      <el-table-column-->
      <!--        prop="issuePhoto"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="问题照片">-->
      <!--      </el-table-column>-->
      <el-table-column
        prop="rectificationRequirement"
        header-align="center"
        align="center"
        label="整改要求">
      </el-table-column>
      <el-table-column
        prop="requiredCompletionTime"
        header-align="center"
        align="center"
        label="要求完成时间">
      </el-table-column>
      <el-table-column
        prop="responsibleDepartment"
        header-align="center"
        align="center"
        label="责任科室">
      </el-table-column>
      <el-table-column
        prop="causeAnalysis"
        header-align="center"
        align="center"
        label="原因分析">
      </el-table-column>
            <el-table-column
              prop="rectificationStatus"
              header-align="center"
              align="center"
              label="整改情况">
            </el-table-column>
            <el-table-column
              prop="actualCompletionTime"
              header-align="center"
              align="center"
              label="实际完成时间">
            </el-table-column>
      <el-table-column
        prop="rectificationPhotoDeliverable"
        header-align="center"
        align="center"
        label="整改照片交付物">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="previewImage(scope.row.rectificationPhotoDeliverable)">下载</el-button>
        </template>
      </el-table-column>
            <el-table-column
              prop="rectificationResponsiblePerson"
              header-align="center"
              align="center"
              label="整改责任人">
            </el-table-column>
      <!--      <el-table-column-->
      <!--        prop="requiredSecondRectificationTime"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="要求二次整改时间">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="remark"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="备注">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="creator"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="创建人">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="creationTime"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="创建时间">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="lastModifier"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="最后修改人">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="lastModificationTime"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="最后修改时间">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="associatedRectificationRecords"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="关联问题整改记录">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="associatedIssueAddition"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="关联问题添加">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="creationDuration"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="创建时长">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="rectificationVerificationStatus"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="整改验证情况">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="verificationConclusion"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="验证结论">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="verifier"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="验证人">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="formula"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="公式">-->
      <!--      </el-table-column>-->
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleRectificationRecords(scope.row.issueNumber,scope.row.issueId)">整改记录</el-button>
<!--          <el-button type="text" size="small" @click="deleteHandle(scope.row.issueId)">删除</el-button>-->
          <el-button type="text" size="small" @click="assetOrUpdateHandle(scope.row.issueId,scope.row.issueNumber)">任务发起</el-button>
          <el-button type="text" size="small" @click="openflow(scope.row.issueId,scope.row.issueNumber)">任务流程</el-button>
          <el-button type="text" size="small" @click="openNewPage(scope.row.issueId,scope.row.issueNumber)">任务列表</el-button>
        </template>
      </el-table-column>
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
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <add-or-update v-if="assertOrUpdateVisible" ref="assetOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <!-- 图片预览对话框 -->
    <el-dialog :visible.sync="imagePreviewVisible" :title="imagePreviewTitle">
      <img :src="imagePreviewUrl" style="width: 100%;">
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './issuetable-add-or-update.vue'
// import {isAuth} from "../../../utils";
export default {
  data () {
    return {
      showForm: true, // 控制表单显示的变量
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
      assertOrUpdateVisible: false,
      imagePreviewVisible: false, // 图片预览对话框显示控制
      imagePreviewUrl: '', // 图片预览URL
      imagePreviewTitle: '' // 图片预览标题
    }
  },
  components: {
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  methods: {
    openNewPage (issueId, issueNumber) {
      this.$router.push({
        name: 'issue-issuemask',
        params: {
          issueId: issueId,
          issueNumber: issueNumber
        }
      })
    },
    // 处理整改记录
    handleRectificationRecords (issueNumber, issueId) {
      this.$http({
        url: this.$http.adornUrl('/generator/issuemasktable/records'),
        method: 'post',
        params: this.$http.adornParams({ issueNumber: issueNumber })
      }).then(({data}) => {
        console.log("返回数据：" ,data)
        if (data && data.msg === 'success') {
          console.log('整改得到的id为' , issueId)
          // 操作成功后触发addOrUpdateHandle
          this.addOrUpdateHandle(issueId)
        }else if (data && data.msg === 'error') {
          this.$message.error('任务未全部完成')
        } else {
          this.$message.error('操作失败')
        }
      }).catch(() => {
        this.$message.error('请求失败')
      })

    },
    openflow (issueId, issueNumber) {
      this.$router.push({
        name: 'issue-issueflow',
        params: {
          issueId: issueId,
          issueNumber: issueNumber
        }
      })
    },
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/list'),
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
      this.getDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
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
    // 任务发起
    assetOrUpdateHandle (id,issueNumber) {
      this.assertOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.assetOrUpdate.init1(id,issueNumber)
      })
    },
    // 图片预览
    previewImage (imageUrl) {
      console.log("cur imageUrl====>" + imageUrl);
      window.open(imageUrl);
      console.log("图片地址：" ,imageUrl)
    },
    // 删除
    deleteHandle (id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.issueId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
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


