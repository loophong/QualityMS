<template>
  <div class="mod-config">
    <el-form :inline="true" :model="queryParams" @keyup.enter.native="getDataList()">
      <el-form-item label="指标名称" prop="indicatorName">
        <el-select v-model="queryParams.indicatorName" filterable clearable placeholder="请选择指标名称">
          <el-option v-for="field in keyIndicatorList" :key="field.indicatorId" :value="field.indicatorName">
            {{ field.indicatorName }}
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="管理部门" prop="managementDepartment">
        <el-input v-model="queryParams.managementDepartment" placeholder="请输入管理部门"></el-input>
      </el-form-item>
      <el-form-item label="考核部门" prop="assessmentDepartment">
        <el-input v-model="queryParams.assessmentDepartment" placeholder="请输入考核部门"></el-input>
      </el-form-item>
      <el-form-item label="指标分级" prop="indicatorClassification">
        <el-select v-model="queryParams.indicatorClassification" clearable placeholder="请选择指标分级">
          <el-option label="A" value="A"></el-option>
          <el-option label="B" value="B"></el-option>
          <el-option label="C" value="C"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="isAuth('indicator:indicatorkeyindicators:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="isAuth('indicator:indicatorkeyindicators:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button  type="danger" @click="issueHandle()">问题添加</el-button>
      </el-col>
    </el-row>

    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      :span-method="arraySpanMethod"
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
          <!-- 使用v-if来判断是否显示序号 -->
          <span v-if="scope.row.showIndex===1">
       {{ scope.row.index }}
    </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="indicatorName"
        header-align="center"
        align="center"
        label="指标名称">
      </el-table-column>
      <el-table-column
        prop="indicatorClassification"
        header-align="center"
        align="center"
        label="指标分级">
      </el-table-column>
      <el-table-column
        prop="sourceDepartment"
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
      <el-table-column
        prop="managementContent"
        header-align="center"
        align="center"
        label="管理内容">
      </el-table-column>
      <el-table-column
        prop="isManagementOutOfControl"
        header-align="center"
        align="center"
        label="是否管理失控">
      </el-table-column>
      <el-table-column
        prop="isNeedsControl"
        header-align="center"
        align="center"
        label="是否需要理攻关">
      </el-table-column>
      <el-table-column
        prop="keyElements"
        header-align="center"
        align="center"
        label="关键要素">
      </el-table-column>
      <el-table-column
        prop="potentialFailureMode"
        header-align="center"
        align="center"
        label="潜在失效模式">
      </el-table-column>
      <el-table-column
        prop="potentialFailureConsequences"
        header-align="center"
        align="center"
        label="潜在失效后果">
      </el-table-column>
      <el-table-column
        prop="involvedProduct"
        header-align="center"
        align="center"
        label="涉及产品">
      </el-table-column>
      <el-table-column
        prop="processName"
        header-align="center"
        align="center"
        label="过程名称">
      </el-table-column>
      <el-table-column
        prop="controlPosition"
        header-align="center"
        align="center"
        label="控制位置">
      </el-table-column>
      <el-table-column
        prop="controlPersonnel"
        header-align="center"
        align="center"
        label="控制人员">
      </el-table-column>
      <el-table-column
        prop="controlMethod"
        header-align="center"
        align="center"
        label="控制方法">
      </el-table-column>
      <el-table-column
        prop="evaluationMeasurementTechnique"
        header-align="center"
        align="center"
        label="评价测量技术">
      </el-table-column>
      <el-table-column
        prop="sampleSize"
        header-align="center"
        align="center"
        label="样本大小">
      </el-table-column>
      <el-table-column
        prop="samplingFrequency"
        header-align="center"
        align="center"
        label="抽样频率">
      </el-table-column>
      <el-table-column
        prop="controlList"
        header-align="center"
        align="center"
        label="控制清单">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="100"
        label="入库操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="storageHandle(scope.row.indicatorId)">入库</el-button>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.keyIndicatorId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.keyIndicatorId)">删除</el-button>
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
  import AddOrUpdate from './indicatorkeyindicators-add-or-update'
  export default {
    data () {
      return {
        mergedRowInfo: [], // 用于存储合并行的信息
        keyIndicatorList: [], //重点管控指标列表（不分页）
        // 查询参数
        queryParams: {
          indicatorId: '',
          indicatorName: '',
          indicatorClassification: '',
          assessmentDepartment: '',
          sourceDepartment: '',
          managementContent: '',
          isManagementOutOfControl: '',
          isNeedsControl: '',
          keyElements: '',
          potentialFailureMode: '',
          potentialFailureConsequences: '',
          involvedProduct: '',
          processName: '',
          controlPosition: '',
          controlPersonnel: '',
          controlMethod: '',
          evaluationMeasurementTechnique: '',
          sampleSize: '',
          samplingFrequency: '',
          controlList: '',
          storageFlag: ''
        },
        dataForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 100,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        currentIndex: 0 // 新增 currentIndex 变量
      }
    },
    components: {
      AddOrUpdate
    },
    async activated () {
      await this.getIndicatorIdFromQuery();
      this.getDataList();
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.currentIndex = (this.pageIndex - 1) * this.pageSize + 1; // 初始化当前索引
        console.log("this.currentIndex=====>", this.currentIndex)
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatorkeyindicators/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.queryParams
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            let previousIndicatorId = null;
            this.dataList = data.page.list.map((item, index) => {
              let currentIndex = this.currentIndex;
              if (item.indicatorId !== previousIndicatorId) {
                this.currentIndex++;
              }
              previousIndicatorId = item.indicatorId;
              return {
                ...item,
                index: currentIndex,
                showIndex: item.indicatorId !== previousIndicatorId ? 1 : 0, // 设置 showIndex 属性
              };
            });
            console.log("data1====>", data);
            console.log("this.dataList====>", this.dataList);
            this.totalPage = data.page.totalCount;
          } else {
            this.dataList = [];
            this.totalPage = 0;
          }
          this.dataListLoading = false;
        });


        //获取重点管控指标列表（不分页）
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatorkeyindicators/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': 10000,
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            console.log("data2====>",data)
            this.keyIndicatorList = data.page.list
          } else {
            this.keyIndicatorList = []
          }
        })
      },
      //查询重置
      resetQuery() {
        this.queryParams = {
          indicatorId: null,
          indicatorName: null,
          indicatorClassification: null,
          assessmentDepartment: null,
          sourceDepartment: null,
          managementContent: null,
          isManagementOutOfControl: null,
          isNeedsControl: null,
          keyElements: null,
          potentialFailureMode: null,
          potentialFailureConsequences: null,
          involvedProduct: null,
          processName: null,
          controlPosition: null,
          controlPersonnel: null,
          controlMethod: null,
          evaluationMeasurementTechnique: null,
          sampleSize: null,
          samplingFrequency: null,
          controlList: null,
          storageFlag: null
        }
        this.getDataList()
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
          return item.keyIndicatorId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/indicator/indicatorkeyindicators/delete'),
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
      //入知识库
      storageHandle (indicatorId) {
        console.log("indicatorId=====>",indicatorId);
        const ids = this.keyIndicatorList.filter(item => item.indicatorId === indicatorId).map(item => item.keyIndicatorId);
        console.log("ids=====>",ids);
        this.$confirm(`是否入库该指标？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          ids.forEach(id => {
            // this.$http({
            //   url: this.$http.adornUrl(`/indicator/indicatorkeyindicators/info/${this.dataForm.keyIndicatorId}`),
            //   method: 'get',
            //   params: this.$http.adornParams()
            // }).then(({ data }) => {
            //   if (data && data.code === 0) {
            //     this.dataForm.indicatorId = data.indicatorKeyIndicators.indicatorId
            //     this.dataForm.indicatorName = data.indicatorKeyIndicators.indicatorName
            //     this.dataForm.indicatorClassification = data.indicatorKeyIndicators.indicatorClassification
            //     this.dataForm.assessmentDepartment = data.indicatorKeyIndicators.assessmentDepartment
            //     this.dataForm.sourceDepartment = data.indicatorKeyIndicators.sourceDepartment
            //     this.dataForm.managementContent = data.indicatorKeyIndicators.managementContent
            //     this.dataForm.isManagementOutOfControl = data.indicatorKeyIndicators.isManagementOutOfControl
            //     this.dataForm.isNeedsControl = data.indicatorKeyIndicators.isNeedsControl
            //     this.dataForm.keyElements = data.indicatorKeyIndicators.keyElements
            //     this.dataForm.potentialFailureMode = data.indicatorKeyIndicators.potentialFailureMode
            //     this.dataForm.potentialFailureConsequences = data.indicatorKeyIndicators.potentialFailureConsequences
            //     this.dataForm.involvedProduct = data.indicatorKeyIndicators.involvedProduct
            //     this.dataForm.processName = data.indicatorKeyIndicators.processName
            //     this.dataForm.controlPosition = data.indicatorKeyIndicators.controlPosition
            //     this.dataForm.controlPersonnel = data.indicatorKeyIndicators.controlPersonnel
            //     this.dataForm.controlMethod = data.indicatorKeyIndicators.controlMethod
            //     this.dataForm.evaluationMeasurementTechnique = data.indicatorKeyIndicators.evaluationMeasurementTechnique
            //     this.dataForm.sampleSize = data.indicatorKeyIndicators.sampleSize
            //     this.dataForm.samplingFrequency = data.indicatorKeyIndicators.samplingFrequency
            //     this.dataForm.controlList = data.indicatorKeyIndicators.controlList
            //     this.dataForm.storageFlag = data.indicatorKeyIndicators.storageFlag
            //   }
            // })

            this.$http({
              url: this.$http.adornUrl('/indicator/indicatorkeyindicators/update'),
              method: 'post',
              data: this.$http.adornData({
                'keyIndicatorId': id,
                'storageFlag': 1
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '入库成功',
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
        })
      },
      //获取指标页面查看重点管控措施时传递的id
      getIndicatorIdFromQuery() {
        const indicatorName = this.$route.query.indicatorName;
        if (indicatorName) {
          this.queryParams.indicatorName = indicatorName;
        }
      },
      //问题模块跳转
      issueHandle() {
        this.$router.push({ name: 'otherToIssue' })
      },
      arraySpanMethod({ row, column, rowIndex, columnIndex }) {
        if (columnIndex >= 1 && columnIndex <= 8 || columnIndex === 21) { // 合并从序号到是否需要理攻关的列
          if (rowIndex > 0 && row.indicatorId === this.dataList[rowIndex - 1].indicatorId && row.indicatorName === this.dataList[rowIndex - 1].indicatorName) {
            row.showIndex = 0; // 当前行不需要显示序号
            return {
              rowspan: 0,
              colspan: 0
            };
          } else {
            let rowspan = 1;
            for (let i = rowIndex + 1; i < this.dataList.length; i++) {
              if (row.indicatorId === this.dataList[i].indicatorId && row.indicatorName === this.dataList[i].indicatorName) {
                rowspan++;
              } else {
                break;
              }
            }
            row.showIndex = 1; // 当前行需要显示序号
            return {
              rowspan: rowspan,
              colspan: 1
            };
          }
        }
        // row.showIndex = 1; // 默认情况下显示序号
        return {
          rowspan: 1,
          colspan: 1
        };
      },
    }
  }
</script>
