<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.userName" placeholder="用户名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.nickName" placeholder="昵称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('sys:user:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('sys:user:delete')" type="danger" @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0">批量删除</el-button>

                         <!--Excel 参数导入 -->
      <el-button
        type="primary"
        @click="showDialog = true"
        v-if="isAuth('generator:sys:upload')"
        ><i class="fa fa-download"></i> 导入Excel文件
      </el-button>
      <el-dialog
        title="导入Excel文件"
        :visible.sync="showDialog"
        width="30%"
        :before-close="handleClose"
        @close="resetFileInput"
      >
        <!-- <el-upload
          class="upload-demo"
          :on-success="handleUploadSuccess"
          drag
          action="#"
          :before-upload="beforeUpload"
          :show-file-list="false"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击导入</em></div>
        </el-upload> -->

        <!-- el-upload 的方法暂时无法测试通过，使用input-file进行传到后端 -->
        <i class="el-icon-upload"></i>
        <input type="file" id="inputFile" ref="fileInput" @change="checkFile" />

        <!-- 进度动画条 -->
        <div v-if="progress > 0">
          <el-progress
            :percentage="progress"
            color="rgb(19, 194, 194)"
          ></el-progress>
        </div>

        <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="fileSend()">确 定</el-button>
        </span>
      </el-dialog>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column>
      <el-table-column prop="userId" header-align="center" align="center" width="80" label="ID">
      </el-table-column>
      <el-table-column prop="username" header-align="center" align="center" label="用户名">
      </el-table-column>
      <el-table-column prop="nickname" header-align="center" align="center" label="昵称">
      </el-table-column>
      <el-table-column prop="email" header-align="center" align="center" label="邮箱">
      </el-table-column>
      <el-table-column prop="mobile" header-align="center" align="center" label="手机号">
      </el-table-column>
      <el-table-column prop="status" header-align="center" align="center" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" size="small" type="danger">禁用</el-tag>
          <el-tag v-else size="small">正常</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="dept" header-align="center" align="center" width="180" label="所属部门">
      </el-table-column>

      <el-table-column prop="createTime" header-align="center" align="center" width="180" label="创建时间">
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('sys:user:update')" type="text" size="small"
            @click="addOrUpdateHandle(scope.row.userId)">修改</el-button>
          <el-button v-if="isAuth('sys:user:delete')" type="text" size="small"
            @click="deleteHandle(scope.row.userId)">删除</el-button>
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
import AddOrUpdate from './user-add-or-update'
import { listIndicatorSummary, uploadImports} from '@/api/indicator/indicator.js'
export default {
  data() {
    return {
      dataForm: {
        userName: '',
        nickName: ''
      },
      dataList: [],
        showDialog: false,
        progress: 0,
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
        url: this.$http.adornUrl('/sys/user/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'username': this.dataForm.userName,
          'nickname': this.dataForm.nickName
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
      //导入excel，取消按钮绑定取消所选的xlsx
    resetFileInput() {
      this.$refs.fileInput.value = "";
    },
   checkFile() {
      const file = this.$refs.fileInput.files[0];
      const fileName = file.name;
      const fileExt = fileName.split(".").pop(); // 获取文件的扩展名

      if (fileExt !== "xlsx" && fileExt !== "xlsm") {
        this.$message.error("只能上传 Excel 文件！");
        this.$refs.fileInput.value = ""; // 清空文件选择框
      }
    },
      fileSend() {  
    const formData = new FormData();  
    const file = document.getElementById("inputFile").files[0]; // 获取文件对象  

    formData.append("file", file);  

    console.log("file====>", formData);  
    const url = this.$http.adornUrl(`/sys/user/import`);  
    
    uploadImports(formData, url)  
        .then(response => {  
            // 文件上传成功  
            this.$message.success("上传成功"); // 显示成功消息  
            
            // 关闭上传面板  
            this.showDialog = false;   
            location.reload(); // 刷新页面数据  
        })  
        .catch(error => {  
            // 处理错误  
            console.error('Error uploading file:', error);  
            this.$message.error("上传失败，请重试。"); // 显示错误消息  
        });  
} ,
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
      var userIds = id ? [id] : this.dataListSelections.map(item => {
        return item.userId
      })
      this.$confirm(`确定对[id=${userIds.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/sys/user/delete'),
          method: 'post',
          data: this.$http.adornData(userIds, false)
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
      }).catch(() => { })
    }
  }
}
</script>
