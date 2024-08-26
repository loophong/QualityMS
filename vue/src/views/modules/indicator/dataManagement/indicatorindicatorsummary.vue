<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="queryParams.indicatorName" placeholder="请输入指标名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('indicator:indicatorindicatorsummary:save')" type="primary" @click="addOrUpdateHandle()">填报</el-button>
        <el-button v-if="isAuth('indicator:indicatorindicatorsummary:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
        <el-button v-if="isAuth('indicator:indicatorindicatorsummary:save')" type="primary" @click="showUploadDialog = true">Excel导入</el-button>
        <el-col :span="1.5">
          <el-dialog title="导入Excel文件" :visible.sync="showUploadDialog" width="30%" @close="resetFileInput">
            <el-form :model="form" ref="form" label-width="90px">
              <el-form-item label="上传表类:">
                <span style="color: rgb(68, 140, 39);">指标汇总表</span>
                <br>
                <el-date-picker
                  clearable
                  v-model="form.yearMonth"
                  type="month"
                  value-format="yyyy-MM"
                  placeholder="请选择月份"
                >
                </el-date-picker>
              </el-form-item>
            </el-form>
            <i class="el-icon-upload"></i>
            <input type="file" id="inputFile" ref="fileInput" @change="checkFile" />
            <span slot="footer" class="dialog-footer">
        <el-button @click="showUploadDialog = false">取消</el-button>
        <el-button type="primary" @click="fileSend()">确定</el-button>
      </span>
          </el-dialog>
        </el-col>

      </el-form-item>
      <el-form-item label="选择月份">
        <el-date-picker
          v-model="dataForm.key"
          type="month"
          placeholder="选择月份"
          format="yyyy-MM"
          value-format="yyyy-MM"
          clearable
          @change="getDataListByMonth">
        </el-date-picker>
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
      <!-- 序号列 -->
      <el-table-column
        header-align="center"
        align="center"
        label="序号"
        width="60"
      >
        <template slot-scope="scope">
          {{ (pageIndex - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="indicatorId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="指标ID">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="indicatorName"
        header-align="center"
        align="center"
        label="指标名称">
      </el-table-column>
      <el-table-column
        prop="yearMonth"
        header-align="center"
        align="center"
        label="年月">
      </el-table-column>
      <el-table-column
        prop="indicatorValue"
        header-align="center"
        align="center"
        label="指标值">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="indicatorValueUpperBound"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="指标值上界">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="indicatorValueLowerBound"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="指标值下界">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="managementDepartment"
        header-align="center"
        align="center"
        label="管理部门">
      </el-table-column>
      <el-table-column
        prop="assessmentDepartment"
        header-align="center"
        align="center"
        label="考核部门">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="managementContentCurrentAnalysis"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="管理内容（现状分析）">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="indicatorCreatTime"
        header-align="center"
        align="center"
        label="指标填报时间">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.indicatorId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.indicatorId)">删除</el-button>
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
  import AddOrUpdate from './indicatorindicatorsummary-add-or-update'
  import { listIndicatorSummary, uploadFile} from '@/api/indicator/indicator.js'
  import http from "../../../../utils/httpRequest";
  export default {
    data () {
      return {
        indicatorDictionaryList: {},

        //excel上传
        showUploadDialog:false,
        dataForm: {
          key: ''
        },
        //查询参数列表
        queryParams: {
          indicatorName: '',
          indicatorValue: '',
          indicatorValueUpperBound: '',
          indicatorValueLowerBound: '',
          assessmentDepartment: '',
          managementDepartment: '',
          indicatorDefinition: '',
          indicatorClassification: '',
          managementContentCurrentAnalysis: '',
          dataId: '',
          sourceDepartment: '',
          collectionMethod: '',
          collectionFrequency: '',
          planId: '',
          taskId: '',
          indicatorParentNode: '',
          indicatorCreatTime: '',
          yearMonth: '',
          indicatorState: '',
          indicatorChildNode: ''
        },
        form: {
          indicatorId: 0,
          indicatorName: '',
          indicatorValue: '',
          indicatorValueUpperBound: '',
          indicatorValueLowerBound: '',
          assessmentDepartment: '',
          managementDepartment: '',
          indicatorDefinition: '',
          indicatorClassification: '',
          managementContentCurrentAnalysis: '',
          dataId: '',
          sourceDepartment: '',
          collectionMethod: '',
          collectionFrequency: '',
          planId: '',
          taskId: '',
          indicatorParentNode: '',
          indicatorCreatTime: '',
          yearMonth: '',
          indicatorState: '',
          indicatorChildNode: ''
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
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        // const params = {
        //   page: this.pageIndex,
        //   limit: this.pageSize,
        //   key: this.dataForm.key
        // }
        // listIndicatorSummary(params).then(({data}) => {
        //   if (data && data.code === 0) {
        //     console.log("1235454")
        //     this.dataList = data.page.list
        //     this.totalPage = data.page.totalCount
        //   } else {
        //     this.dataList = []
        //     this.totalPage = 0
        //   }
        //   this.dataListLoading = false
        // })
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.queryParams
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            console.log("data====>",data)
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 根据月份获取数据列表
      getDataListByMonth () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.form.yearMonth,
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
      // 导入excel，检查文件类型
      checkFile() {
        const file = this.$refs.fileInput.files[0];
        const fileName = file.name;
        const fileExt = fileName.split(".").pop(); // 获取文件的扩展名

        if (fileExt.toLowerCase() !== "xlsx" && fileExt.toLowerCase() !== "xlsm") {
          this.$message.error("只能上传 Excel 文件！");
          // this.$refs.fileInput.value = ""; // 清空文件选择框
        }
      },
      //导入excel，取消按钮绑定取消所选的xlsx
      resetFileInput() {
        this.$refs.fileInput.value = "";
      },
      /** 导入按钮 */
      fileSend() {
        const formData = new FormData();
        const file = document.getElementById("inputFile").files[0]; // 获取文件对象
        const yearMonth = this.form.yearMonth;
        formData.append("multipartFile", file);
        formData.append("yearMonth", yearMonth);
        console.log("file=====>",file);
        console.log("this.form=====>",this.form);
        console.log("formData=====>",formData);
        if (file === undefined || yearMonth == null) {
          if (file === undefined) {
            this.$message.error("请选择文件!");
            return;
          } else {
            this.$message.error("请选择日期!");
            return;
          }
        } else {
          console.log("formData=====>",formData);
          const aimUrl = http.adornUrl('/indicator/indicatorindicatorsummary/upload');
          uploadFile(formData, aimUrl)
            .then(response => {
              console.log("response=====>",response);
              if (response && response.data.code === 0) {
                if (response.data.msg.includes('存在未定义的指标')) {
                  this.$message.warning(response.data.msg); // 提示存在未定义的指标
                } else {
                  this.$message.success(response.data.msg); // 正常成功提示
                }
                this.getDataList();
              } else {
                this.$message.error("上传失败，请重试");
              }
            })
            .catch(error => {
              console.error('上传失败：', error);
              this.$message.error("上传失败，请重试");
            })
            .finally(() => {
              this.showUploadDialog = false;
            });

        }
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
          return item.indicatorId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/delete'),
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
      },
    }
  }
</script>
