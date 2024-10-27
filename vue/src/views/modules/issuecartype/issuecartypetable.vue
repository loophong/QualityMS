<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
<!--      <el-form-item>-->
<!--        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
<!--      </el-form-item>-->
      <el-form-item>
<!--        <el-button @click="getDataList()">查询</el-button>-->
        <el-button v-if="isAuth('generator:issuecartypetable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('generator:issuecartypetable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
      <el-form-item>
        <el-upload
          class="upload-demo"
          :before-upload="beforeUpload"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError">
          <el-button size="small" type="primary">上传车型</el-button>
        </el-upload>
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
<!--        prop="vehicleTypeId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="车型ID">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="concreteVehicleType"
        header-align="center"
        align="center"
        label="具体车型">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.vehicleTypeId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.vehicleTypeId)">删除</el-button>
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
  </div>
</template>

<script>
  import AddOrUpdate from './issuecartypetable-add-or-update'
  export default {
    data () {
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
          return false; // 返回 false，阻止上传
        }

        if (!isLt2M) {
          this.$message.error('上传文件大小不能超过 2MB!');
          return false; // 返回 false，阻止上传
        }

        // 使用 FormData 创建上传文件数据
        const formData = new FormData();
        formData.append('file', file);

        // 开始上传请求
        this.$http({
          url: this.$http.adornUrl('/generator/issuecartypetable/uploadExcel'),
          method: 'post',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data' // 重要：设置请求头
          }
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message.success('文件上传成功！');
            this.getDataList(); // 上传成功后可以重新获取数据
          } else {
            this.$message.error('文件上传失败：' + data.msg);
          }
        }).catch(error => {
          this.$message.error('文件上传错误：' + error.message);
        });

        return false; // 返回 false，阻止 el-upload 的自动上传
      }
      ,


      // 上传成功的处理
      handleUploadSuccess(response, file) {
        if (response && response.code === 0) {
          this.$message.success('文件上传成功！');
          this.getDataList(); // 上传成功后可以重新获取数据
        } else {
          this.$message.error('文件上传失败：' + response.msg);
        }
      },
      handleUploadError(error) {
        console.error('上传错误:', error);  // 打印错误对象
        this.$message.error('文件上传失败：' + (error.response ? error.response.data.message : error.message)); // 提供更详细的错误信息
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/generator/issuecartypetable/list'),
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
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.vehicleTypeId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/generator/issuecartypetable/delete'),
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

