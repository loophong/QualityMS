<template>
  <div class="mod-config">
    <el-form :inline="true" :model="queryParams" @keyup.enter.native="getDataList()">
      <el-form-item label="指标名称" prop="indicatorName">
        <el-select v-model="queryParams.indicatorName" filterable placeholder="请选择指标名称">
          <el-option v-for="field in indicatorDictionaryList" :key="field.indicatorId" :value="field.indicatorName">
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
        <el-select v-model="queryParams.indicatorClassification" placeholder="请选择指标分级">
          <el-option label="A" value="A"></el-option>
          <el-option label="B" value="B"></el-option>
          <el-option label="C" value="C"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="上级指标" prop="indicatorParentNode">
        <el-select v-model="queryParams.indicatorParentNode" filterable clearable placeholder="请选择上级指标">
          <el-option v-for="field in indicatorDictionaryList1" :key="field.indicatorId" :value="field.indicatorName">
            {{ field.indicatorName }}
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="isAuth('indicator:indicatordictionary:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="isAuth('indicator:indicatordictionary:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button  type="primary" @click="exportAll()">导出</el-button>
      </el-col>
    </el-row>

    <div class="table-container">
      <el-table
        :data="dataList"
        border
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%; margin-top: 10px;"
        height="1000"
      >
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
        <el-table-column
          prop="indicatorName"
          header-align="center"
          align="center"
          label="指标名称">
        </el-table-column>
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
        <el-table-column
          prop="indicatorDefinition"
          header-align="center"
          align="center"
          label="指标公式定义">
        </el-table-column>
        <el-table-column
          prop="indicatorClassification"
          header-align="center"
          align="center"
          label="指标分级">
        </el-table-column>
        <el-table-column
          prop="indicatorPlannedValue"
          header-align="center"
          align="center"
          label="指标目标值">
        </el-table-column>
        <el-table-column
          prop="weight"
          header-align="center"
          align="center"
          label="指标权重">
          <template slot-scope="scope">
          <span v-if="scope.row.weight !== null && scope.row.weight !== ''">
          {{ scope.row.weight }}%
        </span>
            <span v-else>
          {{ scope.row.weight }}
        </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="indicatorParentNode"
          header-align="center"
          align="center"
          label="上级指标">
        </el-table-column>
        <el-table-column
          prop="indicatorCreatTime"
          header-align="center"
          align="center"
          label="指标创建时间">
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
            <el-button type="text" size="small" @click="keyControlHandle(scope.row.indicatorId)">重点管控</el-button>
            <el-button type="text" size="small" @click="queryKeyControlHandle(scope.row.indicatorName)">查看重点管控措施</el-button>
            <el-button type="text" size="small" @click="addPlanHandle(scope.row.indicatorId)">新建计划</el-button>
            <el-button type="text" size="small" @click="queryPlanHandle(scope.row)">关联计划</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>


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
    <add-plan v-if="addPlanVisible" ref="addPlan" @refreshDataList="getDataList"></add-plan>
    <key-control v-if="keyControlVisible" ref="keyControl" @refreshDataList="getDataList"></key-control>
  </div>
</template>

<script>
  import AddOrUpdate from './indicatordictionary-add-or-update'
  import KeyControl from './keyindicator-add'
  import AddPlan from "../../taskmanagement/plan/plan-add-page";
  import * as XLSX from "xlsx";
  import {Loading} from "element-ui";
  export default {
    data () {
      return {
        dataForm: {
          key: ''
        },
        //查询参数列表
        queryParams: {
          indicatorName: '',
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
          indicatorState: '',
          indicatorChildNode: '',
          indicatorValueUpperBound: '',
          indicatorValueLowerBound: '',
          storageFlag: '',
          weight: '',
          indicatorPlannedValue: '',
          indicatorBoundFlag: ''
        },
        dataList: [],
        indicatorSummaryList: [],    //指标summary列表
        indicatorDictionaryList: [], //指标列表（不分页）
        indicatorDictionaryList1: [],//包括根节点的指标列表
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        addPlanVisible: false, //新建计划
        // 关键指标
        keyControlVisible: false
      }
    },
    components: {
      AddOrUpdate,
      AddPlan,
      KeyControl,
    },
    mounted () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      async getDataList () {
        // 查询指标summary列表
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': 10000,
            'key': this.queryParams
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.indicatorSummaryList = data.page.list
            console.log("this.indicatorSummaryList====>",this.indicatorSummaryList)
          } else {
            this.indicatorSummaryList = []
          }
        })

        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.queryParams
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

        this.$http({
          url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': 10000,
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            console.log("data2====>",data)
            this.indicatorDictionaryList = data.page.list
          } else {
            this.indicatorDictionaryList = []
          }
        })

        this.$http({
          url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
          method: 'get',
          params: this.$http.adornParams({
            'limit': 10000,
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            console.log("data=====>",data)
            this.indicatorDictionaryList1 = data.page.list
            // 在数组最前面添加新的对象
            this.indicatorDictionaryList1.unshift({
              indicatorId: 1,
              indicatorName: '公司质量指标管控体系',
              indicatorParentNode: ''
            });
          }
        })


      },
      //查询重置
      resetQuery() {
        this.queryParams = {
          indicatorName: null,
          assessmentDepartment: null,
          managementDepartment: null,
          indicatorDefinition: null,
          indicatorClassification: null,
          managementContentCurrentAnalysis: null,
          dataId: null,
          sourceDepartment: null,
          collectionMethod: null,
          collectionFrequency: null,
          planId: null,
          taskId: null,
          indicatorParentNode: null,
          indicatorCreatTime: null,
          indicatorState: null,
          indicatorChildNode: null,
          indicatorValueLowerBound: null,
          indicatorValueUpperBound: null,
          storageFlag: null,
          weight: null,
          indicatorPlannedValue: null,
          indicatorBoundFlag: null
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
      // 新建计划
      addPlanHandle (id) {
        this.$router.push({
          name: 'plan-add-page',
          query: {
            indicatorId: id
          }
        });
      },
      // 查看计划
      queryPlanHandle (data) {
        // console.log("查看计划id:"+id)
        this.$router.push({
          name: 'plan-indicator-index',
          query: {
            data: JSON.stringify(data)
          }
        });
      },
      // 查看任务
      queryTaskHandle (id) {
        this.$router.push({
          name: 'task-list',
          query: {
            indicatorId: id
          }
        });
      },
      //重点指标管控
      keyControlHandle (id) {
        this.keyControlVisible = true
        this.$nextTick(() => {
          this.$refs.keyControl.init(id)
        })
      },
      //查询重点管控措施
      queryKeyControlHandle (id) {
        this.$router.push({
          name: 'indicator-key-indicators',
          query: {
            indicatorName: id
          }
        });
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
            url: this.$http.adornUrl('/indicator/indicatordictionary/delete'),
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
      // 导出
      exportAll(){
        const loadingInstance = Loading.service({
          lock: true,
          text: "正在导出，请稍后...",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });

        const promises = this.indicatorDictionaryList.map((tableRow, index) => {
          return {
            序号: index + 1,
            指标名称: tableRow.indicatorName,
            管理部门: tableRow.managementDepartment,
            考核部门: tableRow.assessmentDepartment,
            指标公式定义: tableRow.indicatorDefinition,
            指标分级: tableRow.indicatorClassification,
            指标目标值: tableRow.indicatorPlannedValue,
            指标权重: tableRow.weight ? `${tableRow.weight}%` : '',
            上级指标: tableRow.indicatorParentNode,
            指标创建时间: tableRow.indicatorCreatTime,
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
              "指标总表.xlsx"
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

<style scoped>

.table-container .el-table {
  max-height: calc(100vh - 300px );
}



</style>
