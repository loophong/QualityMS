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
      <!-- <el-button @click="resetQuery()">重置</el-button> -->
    </el-form-item>
    <el-col :span="1.5">
      <el-button type="primary" @click="exportAll()">导出</el-button>
            <el-col :span="1.5">
      <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-col>
    </el-col>
  </el-form>


  <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
    style="width: 100%;">
    <el-table-column type="selection" header-align="center" align="center" width="50">
    </el-table-column>
    <!-- 序号列 -->
    <el-table-column header-align="center" align="center" label="序号" width="60">
      <template slot-scope="scope">
        {{ (pageIndex - 1) * pageSize + scope.$index + 1 }}
      </template>
    </el-table-column>
    <!-- <el-table-column
      prop="keyIndicatorId"
      header-align="center"
      align="center"
      label="重点指标ID（主键）">
    </el-table-column> -->
    <!-- <el-table-column
      prop="indicatorId"
      header-align="center"
      align="center"
      label="指标ID">
    </el-table-column> -->
    <el-table-column prop="indicatorName" header-align="center" align="center" label="指标名称">
    </el-table-column>
    <el-table-column prop="indicatorClassification" header-align="center" align="center" label="指标分级">
    </el-table-column>
    <el-table-column prop="sourceDepartment" header-align="center" align="center" label="管理部门">
    </el-table-column>
    <el-table-column prop="assessmentDepartment" header-align="center" align="center" label="考核部门">
    </el-table-column>
    <el-table-column prop="managementContent" header-align="center" align="center" label="管理内容">
    </el-table-column>
    <el-table-column prop="isManagementOutOfControl" header-align="center" align="center" label="是否管理失控">
    </el-table-column>
    <el-table-column prop="isNeedsControl" header-align="center" align="center" label="是否需要理攻关">
    </el-table-column>
    <el-table-column prop="keyElements" header-align="center" align="center" label="关键要素">
    </el-table-column>
    <el-table-column prop="potentialFailureMode" header-align="center" align="center" label="潜在失效模式">
    </el-table-column>
    <el-table-column prop="potentialFailureConsequences" header-align="center" align="center" label="潜在失效后果">
    </el-table-column>
    <el-table-column prop="involvedProduct" header-align="center" align="center" label="涉及产品">
    </el-table-column>
    <el-table-column prop="processName" header-align="center" align="center" label="过程名称">
    </el-table-column>
    <el-table-column prop="controlPosition" header-align="center" align="center" label="控制位置">
    </el-table-column>
    <el-table-column prop="controlPersonnel" header-align="center" align="center" label="控制人员">
    </el-table-column>
    <el-table-column prop="controlMethod" header-align="center" align="center" label="控制方法">
    </el-table-column>
    <el-table-column prop="evaluationMeasurementTechnique" header-align="center" align="center" label="评价测量技术">
    </el-table-column>
    <el-table-column prop="sampleSize" header-align="center" align="center" label="样本大小">
    </el-table-column>
    <el-table-column prop="samplingFrequency" header-align="center" align="center" label="抽样频率">
    </el-table-column>
    <el-table-column prop="controlList" header-align="center" align="center" label="控制清单">
    </el-table-column>
    <!-- <el-table-column
      fixed="right"
      header-align="center"
      align="center"
      width="150"
      label="操作">
      <template slot-scope="scope">
        <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.keyIndicatorId)">修改</el-button>
        <el-button type="text" size="small" @click="deleteHandle(scope.row.keyIndicatorId)">删除</el-button>
        <el-button type="text" size="small" @click="storageHandle(scope.row.keyIndicatorId)">入库</el-button>
      </template>
    </el-table-column> -->
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
import AddOrUpdate from './indicate-knowledgebase-add or-update'
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";
import { Loading } from "element-ui";
export default {
data() {
  return {
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
    dataList01: [],
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
async activated() {
  await this.getIndicatorIdFromQuery();
  this.getDataList();
},
methods: {
  // 获取数据列表
  getDataList() {
    this.dataListLoading = true
    this.$http({
      url: this.$http.adornUrl('/indicator/indicatorkeyindicators/list1'),
      method: 'get',
      params: this.$http.adornParams({
        'page': this.pageIndex,
        'limit': this.pageSize,
        'key': this.queryParams
      })
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.dataList = data.page.list
        console.log("data1====>", data)
        this.totalPage = data.page.totalCount
      } else {
        this.dataList = []
        this.totalPage = 0
      }
      this.dataListLoading = false
    })

    //获取重点管控指标列表（不分页）
    this.$http({
      url: this.$http.adornUrl('/indicator/indicatorkeyindicators/list'),
      method: 'get',
      params: this.$http.adornParams({
        'page': this.pageIndex,
        'limit': 10000,
      })
    }).then(({ data }) => {
      if (data && data.code === 0) {
        console.log("data2====>", data)
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
  sizeChangeHandle(val) {
    this.pageSize = val
    this.pageIndex = 1
    this.getDataList()
  },
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
  // 批量删除
 deleteHandle() {
  var ids = this.dataListSelections.map(item => {
    return item.keyIndicatorId;
  });

  this.$confirm(`确定对[id=${ids.join(',')}]进行[批量删除]操作?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    this.$http({
      url: this.$http.adornUrl('/indicator/indicatorkeyindicators/clearStorageFlag'),
      method: 'post',
      data: this.$http.adornData(ids,false)
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.getDataList()
      }
      console.log(data);
    });


  });
},


// 清除storageFlag
async clearStorageFlag(ids) {
  try {
    const response = await this.$http({
      url: this.$http.adornUrl('/indicator/indicators/clearStorageFlag'),
      method: 'post',
      data: this.$http.adornData(ids)
    });

    if (response.data && response.data.code === 0) {
      this.$message({
        message: '操作成功',
        type: 'success',
        duration: 1500,
        onClose: () => {
          // 刷新数据列表的操作已移至 deleteHandle 方法中
        }
      });
      return true;
    } else {
      this.$message.error(response.data.msg);
      return false;
    }
  } catch (error) {
    console.error('清除storageFlag失败:', error);
    this.$message.error('操作失败，请重试');
    return false;
  }
},
  //入知识库
  storageHandle(id) {
    console.log("id=====>", id)
    this.$confirm(`是否入库该指标？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      if (id !== undefined) {
        this.$http({
          url: this.$http.adornUrl(`/indicator/indicatorkeyindicators/info/${this.dataForm.keyIndicatorId}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.dataForm.indicatorId = data.indicatorKeyIndicators.indicatorId
            this.dataForm.indicatorName = data.indicatorKeyIndicators.indicatorName
            this.dataForm.indicatorClassification = data.indicatorKeyIndicators.indicatorClassification
            this.dataForm.assessmentDepartment = data.indicatorKeyIndicators.assessmentDepartment
            this.dataForm.sourceDepartment = data.indicatorKeyIndicators.sourceDepartment
            this.dataForm.managementContent = data.indicatorKeyIndicators.managementContent
            this.dataForm.isManagementOutOfControl = data.indicatorKeyIndicators.isManagementOutOfControl
            this.dataForm.isNeedsControl = data.indicatorKeyIndicators.isNeedsControl
            this.dataForm.keyElements = data.indicatorKeyIndicators.keyElements
            this.dataForm.potentialFailureMode = data.indicatorKeyIndicators.potentialFailureMode
            this.dataForm.potentialFailureConsequences = data.indicatorKeyIndicators.potentialFailureConsequences
            this.dataForm.involvedProduct = data.indicatorKeyIndicators.involvedProduct
            this.dataForm.processName = data.indicatorKeyIndicators.processName
            this.dataForm.controlPosition = data.indicatorKeyIndicators.controlPosition
            this.dataForm.controlPersonnel = data.indicatorKeyIndicators.controlPersonnel
            this.dataForm.controlMethod = data.indicatorKeyIndicators.controlMethod
            this.dataForm.evaluationMeasurementTechnique = data.indicatorKeyIndicators.evaluationMeasurementTechnique
            this.dataForm.sampleSize = data.indicatorKeyIndicators.sampleSize
            this.dataForm.samplingFrequency = data.indicatorKeyIndicators.samplingFrequency
            this.dataForm.controlList = data.indicatorKeyIndicators.controlList
            this.dataForm.storageFlag = data.indicatorKeyIndicators.storageFlag
          }
        })
      }
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatorkeyindicators/update'),
        method: 'post',
        data: this.$http.adornData({
          'keyIndicatorId': id,
          'storageFlag': 1
        })
      }).then(({ data }) => {
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
  async exportAll() {
    await this.$http({
      url: this.$http.adornUrl('/indicator/indicatorkeyindicators/list01'),
      method: 'get',
      params: this.$http.adornParams({
        'key': this.queryParams
      })
    }).then(({ data }) => {
      if (data) {
        this.dataList01 = data
      } else {
        this.dataList01 = []
      }
    })
    const loadingInstance = Loading.service({
      lock: true,
      text: "正在导出，请稍后...",
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)",
    });

    const promises = this.dataList01.map((tableRow, index) => {
      return {
        序号: index + 1,
        指标名称: tableRow.indicatorName,
        指标分级: tableRow.indicatorClassification,
        管理部门: tableRow.sourceDepartment,
        考核部门: tableRow.assessmentDepartment,
        管理内容: tableRow.managementContent,
        是否管理失控: tableRow.isManagementOutOfControl,
        是否需要攻关: tableRow.isNeedsControl,
        关键要素: tableRow.keyElements,
        潜在失效模式: tableRow.potentialFailureMode,
        潜在失效后果: tableRow.potentialFailureConsequences,
        涉及产品: tableRow.involvedProduct,
        过程名称: tableRow.processName,
        控制位置: tableRow.controlPosition,
        控制人员: tableRow.controlPersonnel,
        控制方法: tableRow.controlMethod,
        评价测量技术: tableRow.evaluationMeasurementTechnique,
        样本大小: tableRow.sampleSize,
        抽样频率: tableRow.samplingFrequency,
        控制清单: tableRow.controlList,
      };
    });
    Promise.all(promises)
      .then((data) => {
        const ws = XLSX.utils.json_to_sheet(data);
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, "项目列表");

        const wbout = XLSX.write(wb, { bookType: "xlsx", type: "array" });
        saveAs(
          new Blob([wbout], { type: "application/octet-stream" }),
          "指标知识库数据.xlsx"
        );

        // // 提交数据到Vuex Store
        // this.updateExportedData(data);


      })
      .finally(() => {
        loadingInstance.close();
      })
      .catch((error) => {
        console.error("导出失败:", error);
        loadingInstance.close();
      });
  },
}
}
</script>
