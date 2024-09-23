<template>
  <div class="mod-config">
    <el-form v-if="showForm" :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('generator:issuetable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('generator:issuetable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
      <el-form-item>
        <el-upload
          class="upload-demo"
          action="上传接口URL"
        :before-upload="beforeUpload"
        :on-success="handleUploadSuccess"
        show-file-list="false">
        <el-button size="small" type="primary">点击上传 Excel</el-button>
        </el-upload>
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
          <el-button type="text" size="small" @click="previewImage(scope.row.issuePhoto)">预览</el-button>
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
<!--      <el-table-column-->
<!--        prop="rectificationStatus"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="整改情况">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="actualCompletionTime"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="实际完成时间">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="rectificationPhotoDeliverable"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="整改照片交付物">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="rectificationResponsiblePerson"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="整改责任人">-->
<!--      </el-table-column>-->
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
<!--        prop="causeAnalysis"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="原因分析">-->
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
<!--          <el-button type="text" size="small" @click="assetOrUpdateHandle(scope.row.issueId)">问题分析</el-button>-->
          <el-button type="text" size="small" @click="deleteHandle(scope.row.issueId)">删除</el-button>
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


//如果新打开的页面是另外一个项目(前提是另一个项目也是自己的)的话可以在请求拦截request.interceptors.js中获取

//如果新打开的页面是另外一个项目(不是自己的项目)的话我们只负责Cookie.set存，取得话需要根据实际情况考虑


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
    // 在上传前的检查
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
      const isLt2M = file.size / 1024 / 1024 < 2; // 限制文件大小为2MB

      if (!isExcel) {
        this.$message.error('上传文件只能是 Excel 文件!');
      }
      if (!isLt2M) {
        this.$message.error('上传文件大小不能超过 2MB!');
      }
      return isExcel && isLt2M; // 返回true表示允许上传
    },

    // 上传成功的处理
    handleUploadSuccess(response, file) {
      if (response && response.code === 0) {
        this.$message.success('文件上传成功！');
        this.getDataList(); // 上传成功后可以重新获取数据
      } else {
        this.$message.error('文件上传失败：' + response.msg);
      }
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
    // 问题分析
    assetOrUpdateHandle (id) {
      this.assertOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.assetOrUpdate.init1(id)
      })
    },
    // 图片预览
    previewImage (imageUrl) {
      console.log("cur imageUrl====>" + imageUrl);
      window.open(imageUrl);
      // this.imagePreviewUrl = imageUrl
      // this.imagePreviewTitle = '图片预览'
      // this.imagePreviewVisible = true
      console.log("图片地址：" ,imageUrl)
    },
    //图片预览2

    // handlePreviewCur(file) {
    //   console.log(file)
    //   console.log(file);
    //   window.open(file);
    // },


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

