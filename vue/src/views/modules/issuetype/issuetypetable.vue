<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
<!--        <el-upload-->
<!--          class="upload-button"-->
<!--          :show-file-list="false"-->
<!--          :before-upload="beforeUpload"-->
<!--          :on-change="handleFileChange"-->
<!--        >-->
<!--          <el-button type="primary">上传图片</el-button>-->
<!--        </el-upload>-->
        <el-button v-if="isAuth('generator:issuetypetable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('generator:issuetypetable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
<!--        prop="issueCategoryId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="问题类别ID">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="concreteIssueCategory"
        header-align="center"
        align="center"
        label="具体问题类别">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.issueCategoryId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.issueCategoryId)">删除</el-button>
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
  import AddOrUpdate from './issuetypetable-add-or-update'
  export default {
    data () {
      return {
        uploadingFile: null, // 用于存储待上传文件
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
      handleFileChange(file) {
        // 存储待上传的文件
        this.uploadingFile = file.raw; // 获取 File 对象
        this.uploadFile(file.raw); // 调用上传方法
      },
      uploadFile(file) {
        const formData = new FormData();
        formData.append('file', file); // 将文件添加到 FormData

        this.$http({
          url: this.$http.adornUrl('/test/upload'), // 替换为实际上传接口
          method: 'post',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data' // 指定为文件上传
          }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('文件上传成功');
            // 处理成功后的逻辑，例如更新状态
          } else {
            this.$message.error(data.msg);
          }
        }).catch(error => {
          this.$message.error('上传失败');
          console.error(error);
        });
      },
      beforeUpload(file) {
        const isImage = file.type.startsWith('image/');
        if (!isImage) {
          this.$message.error('上传图片只能是图片格式!');
        }
        return isImage; // 返回 true 继续上传，返回 false 不上传
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/generator/issuetypetable/list'),
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
          return item.issueCategoryId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/generator/issuetypetable/delete'),
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
