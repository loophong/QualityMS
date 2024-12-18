<template>

  
  <div class="mod-config">
    <!-- 在模板中添加告警信息的展示 -->
<div v-for="alert in alerts" :key="alert.numberId" @click="acknowledgeAlert(alert)" class="alert">
  仪器{{ alert.numberId }}临期了！
</div>

    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
         <el-button v-if="isAuth('publicmanagement:instrumentledger:Fuzzy_queries')" type="primary" @click="Fuzzy_queries()">查询</el-button>

        <!-- <el-button v-if="isAuth('publicmanagement:instrumentledger:list')" type="primary" @click="getDataList()"></el-button> -->
        <!-- <el-button v-if="isAuth('publicmanagement:instrumentledger:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button> -->
        <el-button v-if="isAuth('publicmanagement:instrumentledger:delete')"  type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
         <!--Excel 参数导入 -->
      <!-- <el-button
        type="primary"
        @click="showDialog = true"
        v-if="isAuth('publicmanagement:instrumentledger:upload')"
        ><i class="fa fa-download"></i>导入Excel文件
      </el-button> -->
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
<!-- 
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="主键">
      </el-table-column>
 -->  
      <el-table-column
        prop="companyId"
        header-align="center"
        align="center"
        label="公司编号">
      </el-table-column>
      <el-table-column
        prop="level"
        header-align="center"
        align="center"
        label="级别">
      </el-table-column>
      <el-table-column
        prop="calibrationCycle"
        header-align="center"
        align="center"
        label="检定周期">
      </el-table-column>
      <el-table-column
        prop="calibrationStatus"
        header-align="center"
        align="center"
        label="检定状态">
      </el-table-column>
      <el-table-column
        prop="usageStatus"
        header-align="center"
        align="center"
        label="使用状态">
      </el-table-column>
      <el-table-column
        prop="location"
        header-align="center"
        align="center"
        label="配置地点">
      </el-table-column>
      <el-table-column
        prop="responsibleDepartment"
        header-align="center"
        align="center"
        label="责任部门">
      </el-table-column>

      <el-table-column
        prop="user"
        header-align="center"
        align="center"
        label="使用人">
      </el-table-column>

      <el-table-column
        prop="calibrationDate"
        header-align="center"
        align="center"
        label="检校日期">
      </el-table-column>
      <el-table-column
        prop="calibrationValidity"
        header-align="center"
        align="center"
        label="检校有效期">
      </el-table-column>
      <el-table-column
        prop="calibrationUnit"
        header-align="center"
        align="center"
        label="检校单位">
      </el-table-column>
      <el-table-column
        prop="certificateNumber"
        header-align="center"
        align="center"
        label="证书编号">
      </el-table-column>
      <el-table-column
        prop="remark"
        header-align="center"
        align="center"
        label="备注">
      </el-table-column>

      <el-table-column
        prop="quantity"
        header-align="center"
        align="center"
        label="数量">
      </el-table-column>
<!-- 
      <el-table-column
        prop="flag"
        header-align="center"
        align="center"
        label="逻辑删除字段">
      </el-table-column>

      <el-table-column
        prop="creatTime"
        header-align="center"
        align="center"
        label="创建日期">
      </el-table-column>
      -->
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" v-if="isAuth('publicmanagement:instrumentledger:update')"  @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" v-if="isAuth('publicmanagement:instrumentledger:delete')"  @click="deleteHandle(scope.row.id)">删除</el-button>
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
  import AddOrUpdate from './instrumentledger-add-or-update'
   import { listIndicatorSummary, uploadImports} from '@/api/indicator/indicator.js'
  import { Notification } from 'element-ui'; // 引入 Notification 组件  
  

  export default {
    data () {
      return {
        alerts: [],
        dataForm: {
          key: ''
        },
        showDialog: false,
        progress: 0,
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
     mounted() {
    this.getMessageList();
  },
    methods: {
// 在methods中添加以下方法
acknowledgeAlert(alert) {
  alert.state = 0;
  this.updateMessageState(alert.numberId, 0);
},

updateMessageState(numberId, newState) {
  this.$http({
    url: this.$http.adornUrl(`/publicmanagement/instrumentledger/updatemessage`),
    method: 'post',
    data: this.$http.adornData({ numberId, state: newState }),
  }).then(({ data }) => {
    if (data && data.code === 0) {
      // 更新成功，可以在这里做进一步处理
    }
  });
},


      //告警信息
      
getMessageList() {
    

  this.$http({
    url: this.$http.adornUrl('/publicmanagement/instrumentledger/message'),
    method: 'post',
  }).then(({ data }) => {
      if (data) {
        data.forEach(item => {
          if (item.state === 1) {
            this.showAlert(item.numberId);
          }
        });
      }
    });
  },

  Fuzzy_queries(){
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/publicmanagement/instrumentledger/Fuzzy_queries'),
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

  showAlert(numberId) {
    // 在这里实现展示警告信息的功能
    // 例如，使用Element UI的$message组件来显示警告信息
    this.$message({
      message: `警告：编号为 ${numberId} 的项需要关注！`,
      type: 'warning',
      duration: 3000
    });
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
    // excel 导入
  fileSend() {
        const formData = new FormData();
        const file = document.getElementById("inputFile").files[0]; // 获取文件对象
        // console.log(file);
        formData.append("file", file);
//         // console.log("file====>",formData)
        // axios({
        //   method: "post",
        //   // this $axios.post,
        //   url: "http://localhost:8080/market/ledger/import",
        //   // params:{
        //   //   userName: this.$store.state.user.name,
        //   // },
        //   headers: {
        //     "Content-Type": "multipart/form-data",
        //   },
        //   withCredentials: true,
        //   data: formData,
        //   onUploadProgress: (progressEvent) => {
        //     this.progress = Math.round(
        //       (progressEvent.loaded * 100) / progressEvent.total
        //     );
        //   },
        // });
console.log("file====>",formData)
   const  url=this.$http.adornUrl(`/publicmanagement/instrumentledger/import`)
        uploadImports(formData,url)
console.log("file====>",formData)
        .then(response => {
          // 文件上传成功

          setTimeout(() => {
            this.showDialog = false; // 关闭上传面板
            location.reload(); // 调用此方法刷新页面数据
          }, 5000); // 2000毫秒后关闭
          this.$message.success("上传成功");
        })
        .catch(error => {
          // 处理错误
          console.error('Error uploading file:', error);
        });

        

    },

      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/publicmanagement/instrumentledger/list'),
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
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/publicmanagement/instrumentledger/delete'),
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
