<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
                 <el-button v-if="isAuth('generator:instrumentation:Fuzzy_queries')" type="primary" @click="Fuzzy_queries()">查询</el-button>

        <!-- <el-button @click="getDataList()">查询</el-button> -->
        <el-button v-if="isAuth('generator:instrumentation:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('generator:instrumentation:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
                 <!--Excel 参数导入 -->
      <el-button
        type="primary"
        @click="showDialog = true"
        v-if="isAuth('generator:instrumentation:upload')"
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
      <!-- <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="">
      </el-table-column> -->
      <el-table-column
        prop="warehouseNumber"
        header-align="center"
        align="center"
        label="仓库号">
      </el-table-column>
      <el-table-column
        prop="toolNumber"
        header-align="center"
        align="center"
        label="量具编号">
      </el-table-column>
      <el-table-column
        prop="toolName"
        header-align="center"
        align="center"
        label="量具名称">
      </el-table-column>
      <el-table-column
        prop="specificationModel"
        header-align="center"
        align="center"
        label="规格型号">
      </el-table-column>
      <el-table-column
        prop="rangeValue"
        header-align="center"
        align="center"
        label="量程">
      </el-table-column>
      <el-table-column
        prop="divisionValue"
        header-align="center"
        align="center"
        label="分度值">
      </el-table-column>
      <el-table-column
        prop="accuracy"
        header-align="center"
        align="center"
        label="精度">
      </el-table-column>
      <el-table-column
        prop="purpose"
        header-align="center"
        align="center"
        label="用途">
      </el-table-column>
      <el-table-column
        prop="supplier"
        header-align="center"
        align="center"
        label="供应商">
      </el-table-column>
      <el-table-column
        prop="factoryNumber"
        header-align="center"
        align="center"
        label="出厂编号">
      </el-table-column>
      <el-table-column
        prop="companyNumber"
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
        prop="verificationCycle"
        header-align="center"
        align="center"
        label="检定周期">
      </el-table-column>
      <el-table-column
        prop="verificationStatus"
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
        prop="allocationLocation"
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
        prop="userName"
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
        prop="remarks"
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
      <!-- <el-table-column
        prop="creatTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="createPeople"
        header-align="center"
        align="center"
        label="创建人">
      </el-table-column> -->
      <!-- <el-table-column
        prop="leader"
        header-align="center"
        align="center"
        label="负责人">
      </el-table-column> -->
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
          <el-button type="text" size="small" @click="linked_method(scope.row.id,scope.row.toolNumber)">关联检验方法</el-button>

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
  import AddOrUpdate from './instrumentation-add-or-update'
   import { listIndicatorSummary, uploadImports} from '@/api/indicator/indicator.js'
  export default {
    data () {
      return {
        dataForm: {
          key: ''
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
    activated () {
      this.getDataList()
    },
    methods: {
      //点击关联检验方法按钮，携带对应的toolNumber，跳到instrumenttestmethod.vue页面，
      // 并且调整后发送后端请求到检验方法的control模块，只显示包含toolNumber的所有检验方法。
        
    linked_method(id, toolNumber) {
    this.$router.push({
      name: 'instrument-testmethod', // 确保这是在路由配置中定义的路由名称
      params: {
        id: id,
        toolNumber: toolNumber
      }
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

    formData.append("file", file);  

    console.log("file====>", formData);  
    const url = this.$http.adornUrl(`/generator/instrumentation/import`);  
    
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

      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/generator/instrumentation/list'),
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
            url: this.$http.adornUrl('/generator/instrumentation/delete'),
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
