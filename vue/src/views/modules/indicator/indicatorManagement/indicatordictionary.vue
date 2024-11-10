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
        <el-select v-model="queryParams.indicatorParentNode" filterable placeholder="请选择上级指标">
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
        <el-button v-if="isAuth('indicator:indicatordictionary:save')" type="primary" @click="addOrUpdateHandle()">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-if="isAuth('indicator:indicatordictionary:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
      </el-col>
    </el-row>

    <el-table
      :data="dataList"
      :row-style="getRowStyle"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%; margin-top: 10px;">
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
      <!--      <el-table-column-->
      <!--        prop="managementContentCurrentAnalysis"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="管理内容（现状分析）">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="dataId"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="数据ID">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="sourceDepartment"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="来源部门">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="collectionMethod"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="收集方法">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="collectionFrequency"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="收集频次">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="planId"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="关联计划">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="taskId"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="关联任务id">-->
      <!--      </el-table-column>-->
      <el-table-column
        prop="indicatorParentNode"
        header-align="center"
        align="center"
        label="上级指标">
      </el-table-column>
      <!--      <el-table-column-->
      <!--        prop="indicatorValueLowerBound"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="指标值下界">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="indicatorValueUpperBound"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="指标值上界">-->
      <!--      </el-table-column>-->
      <el-table-column
        prop="indicatorCreatTime"
        header-align="center"
        align="center"
        label="指标创建时间">
      </el-table-column>
      <!--      <el-table-column-->
      <!--        prop="indicatorState"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="0表示弃用，1表示使用中">-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="indicatorChildNode"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="指标子节点">-->
      <!--      </el-table-column>-->
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
          <el-button type="text" size="small" @click="createNewPlan(scope.row.indicatorId)">新建计划</el-button>
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
    <key-control v-if="keyControlVisible" ref="keyControl" @refreshDataList="getDataList"></key-control>
  </div>
</template>

<script>
import AddOrUpdate from './indicatordictionary-add-or-update'
import KeyControl from './keyindicator-add'

export default {
  data() {
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
        indicatorValueLowerBound: ''
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
      // 关键指标
      keyControlVisible: false
    }
  },
  components: {
    AddOrUpdate,
    KeyControl
  },
  mounted() {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    // async getDataList() {
    //   this.dataListLoading = true; // Set loading state before starting requests
    //
    //   try {
    //     // Create an array of promises
    //     const promises = [];
    //
    //     // Add the first HTTP request to the promises array
    //     promises.push(
    //       this.$http({
    //         url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/list'),
    //         method: 'get',
    //         params: this.$http.adornParams({ 'page': this.pageIndex, 'limit': 10000, 'key': this.queryParams })
    //       }).then(({data}) => {
    //         if (data && data.code === 0) {
    //           this.indicatorSummaryList = data.page.list;
    //           console.log("this.indicatorSummaryList====>", this.indicatorSummaryList);
    //         } else {
    //           this.indicatorSummaryList = [];
    //         }
    //       })
    //     );
    //
    //     // Add the second HTTP request to the promises array
    //     promises.push(
    //       this.$http({
    //         url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
    //         method: 'get',
    //         params: this.$http.adornParams({
    //           'page': this.pageIndex,
    //           'limit': this.pageSize,
    //           'key': this.queryParams
    //         })
    //       }).then(({data}) => {
    //         if (data && data.code === 0) {
    //           this.dataList = data.page.list;
    //           this.totalPage = data.page.totalCount;
    //         } else {
    //           this.dataList = [];
    //           this.totalPage = 0;
    //         }
    //       })
    //     );
    //
    //     // Add the third HTTP request to the promises array
    //     promises.push(
    //       this.$http({
    //         url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
    //         method: 'get',
    //         params: this.$http.adornParams({
    //           'page': this.pageIndex,
    //           'limit': 10000,
    //         })
    //       }).then(({data}) => {
    //         if (data && data.code === 0) {
    //           console.log("data2====>", data);
    //           this.indicatorDictionaryList = data.page.list;
    //         } else {
    //           this.indicatorDictionaryList = [];
    //         }
    //       })
    //     );
    //
    //     // Add the fourth HTTP request to the promises array
    //     promises.push(
    //       this.$http({
    //         url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
    //         method: 'get',
    //         params: this.$http.adornParams({
    //           'limit': 10000,
    //         })
    //       }).then(({ data }) => {
    //         if (data && data.code === 0) {
    //           console.log("data=====>", data);
    //           this.indicatorDictionaryList1 = data.page.list;
    //           // 在数组最前面添加新的对象
    //           this.indicatorDictionaryList1.unshift({
    //             indicatorId: 1,
    //             indicatorName: '公司质量指标管控体系',
    //             indicatorParentNode: ''
    //           });
    //         }
    //       })
    //     );
    //
    //     // Wait for all promises to complete
    //     await Promise.all(promises);
    //   } catch (error) {
    //     console.error("An error occurred while fetching data:", error);
    //   } finally {
    //     this.dataListLoading = false; // Reset loading state
    //   }
    // },
    async getDataList() {
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
          console.log("this.indicatorSummaryList====>", this.indicatorSummaryList)
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
          console.log("data2====>", data)
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
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log("data=====>", data)
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
        indicatorValueUpperBound: null
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
    //重点指标管控
    keyControlHandle(id) {
      this.keyControlVisible = true
      this.$nextTick(() => {
        this.$refs.keyControl.init(id)
      })
    },

    createNewPlan(id) {
      this.$router.push({
        name: 'plan-add-page',
        query: {
          indicatorId: id
        }
      });
    },

    // 删除
    deleteHandle(id) {
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
    // async handle123() {
    //   this.$http({
    //     url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/list'),
    //     method: 'get',
    //     params: this.$http.adornParams({
    //       'page': this.pageIndex,
    //       'limit': 10000,
    //       'key': this.queryParams
    //     })
    //   }).then(({data}) => {
    //     if (data && data.code === 0) {
    //       this.indicatorSummaryList = data.page.list
    //       console.log("this.indicatorSummaryList====>",this.indicatorSummaryList)
    //     } else {
    //       this.indicatorSummaryList = []
    //     }
    //   })
    // },
    getRowStyle(row, rowIndex) {
      console.log("1111111111====>", this.indicatorSummaryList)
      console.log("2222222====>", this.indicatorDictionaryList)
      const summaryItems = this.indicatorSummaryList.filter(
        item => item.indicatorName === row.indicatorName
      );
      console.log("summaryItems====>", summaryItems)

      for (const summaryItem of summaryItems) {
        if (summaryItem.indicatorValue < row.lowerBound || summaryItem.indicatorValue > row.upperBound) {
          console.log(`Row ${row.indicatorName} is out of bounds`);
          return {backgroundColor: 'red'};
        }
      }

      console.log(`Row ${row.indicatorName} is within bounds`);
      return {};
    }

  }
}
</script>
