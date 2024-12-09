<template>
  <div class="mod-config">
    <el-form :inline="true" :model="myQueryParam" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="myQueryParam.topicName" placeholder="课题名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="myQueryParam.keywords" placeholder="课题关键字" clearable></el-input>
      </el-form-item>
       <!-- <el-form-item>
        <el-input v-model="myQueryParam.startDate" placeholder="开始日期" clearable></el-input>
      </el-form-item>
            <el-form-item>
        <el-input v-model="myQueryParam.endDate" placeholder="结束日期" clearable></el-input>
      </el-form-item> -->
      <el-form-item label="开始日期" prop="startDate">
        <el-date-picker
          v-model="myQueryParam.startDate"
          type="date"
          placeholder="选择日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
        <el-date-picker
          v-model="myQueryParam.endDate"
          type="date"
          placeholder="选择日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <!-- <el-button v-if="isAuth('qcSubject:plan:submit')" type="primary"
          @click="newPlanHandle(dataListSelections[0].qcsrId)"
          :disabled="dataListSelections.length != 1">关联计划</el-button>
        <el-button v-if="isAuth('qcSubject:plan:submit')" type="primary"
          @click="addOrUpdateHandle(dataListSelections[0].qcsrId)"
          :disabled="dataListSelections.length != 1">提交计划</el-button>
        <el-button v-if="isAuth('qcSubject:plan:submit')" type="primary"
          @click="examineStatus(dataListSelections[0].qcsrId)"
          :disabled="dataListSelections.length != 1">审核状态</el-button> -->
        <!-- <el-button type="danger" @click="toIssue()">问题添加</el-button> -->
      </el-form-item>
     <el-row :gutter="10" class="mb8">
       <el-col :span="1.5">
        <el-button v-if="isAuth('qcSubject:registration:list')"  type="primary" @click="exportAll()">导出</el-button>
      </el-col>
      </el-row>
    </el-form>
    <el-table :data="filteredDataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column>
      <el-table-column prop="qcsrId" header-align="center" align="center" label="课题ID" fixed>
      </el-table-column>
      <el-table-column prop="topicName" header-align="center" align="center" label="课题名称" width="160" fixed>
      </el-table-column>
      <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
      </el-table-column>
      <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
      </el-table-column>
      <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
      </el-table-column>
      <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
      </el-table-column>
      <el-table-column prop="startDate" header-align="center" align="center" label="开始日期" width="120">
      </el-table-column>
      <el-table-column prop="endDate" header-align="center" align="center" label="结束日期" width="120">
      </el-table-column>
      <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述">
      </el-table-column>
      <el-table-column prop="topicType" header-align="center" align="center" label="课题类型" width="160">
      </el-table-column>
      <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
      </el-table-column>
      <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字" width="120">
      </el-table-column>
      <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态">
        <template slot-scope="scope">
          <span>{{ toStatus(scope.row.topicActivityStatus,
      scope.row.topicType) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
        <template slot-scope="scope">
          <span v-if="scope.row.topicActivityResult && 85 <= scope.row.topicActivityResult">一等奖</span>
          <span v-else-if="scope.row.topicActivityResult && 75 <= scope.row.topicActivityResult < 85">二等奖</span>
          <span v-else-if="scope.row.topicActivityResult && 65 <= scope.row.topicActivityResult < 75">三等奖</span>
          <span v-else-if="scope.row.topicActivityResult && 55 <= scope.row.topicActivityResult < 65">四等奖</span>
          <span v-else-if="scope.row.topicActivityResult && 45 <= scope.row.topicActivityResult < 55">鼓励奖</span>
          <span v-else>--</span> <!-- 处理未知状态 -->
        </template>
        </el-table-column>
      <el-table-column prop="resultType" header-align="center" align="center" label="提交类型">
      </el-table-column>
      <el-table-column prop="note" header-align="center" align="center" label="备注">
      </el-table-column>

      <el-table-column prop="qcTwoContent" header-align="center" align="center" label="成果认定审核意见" width="180">
      </el-table-column>
      <el-table-column prop="qcThreeContent" header-align="center" align="center" label="相关方审核意见" width="180">
      </el-table-column>
      <el-table-column prop="qcFourContent" header-align="center" align="center" label="成果初评审核意见" width="180">
      </el-table-column>
       <el-table-column prop="qcFirstScore" header-align="center" align="center" label="成果初评分数" width="120">
      </el-table-column>
       <el-table-column prop="qcSecondScore" header-align="center" align="center" label="成果复评分数" width="120">
      </el-table-column>
        <el-table-column prop="qcFiveContent" header-align="center" align="center" label="成果复评审核意见" width="180">
      </el-table-column>
         <el-table-column prop="qcSixContent" header-align="center" align="center" label="财务部审核意见" width="180">
      </el-table-column>
       <el-table-column prop="qcSevenContent" header-align="center" align="center" label="终评审核意见" width="180">
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" v-if="isAuth('qcPlan:step:list')"
            @click="newPlanHandle(scope.row.qcsrId)">关联计划</el-button>
          <!-- <el-button type="text" size="small" v-if="isAuth('qcSubject:plan:submit')"
            @click="addOrUpdateHandle(scope.row.qcsrId)">提交计划</el-button>
          <el-button type="text" size="small" v-if="isAuth('qcManagement:examineStatus:list')"
            @click="examineStatus(scope.row.qcsrId, scope.row.resultType)">审核状态</el-button> -->
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
import AddOrUpdate from './K_qcPlan-add-or-update'
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";
import {Loading} from "element-ui";
export default {
  data() {
    return {
      dataForm: {
        key: ''
      },
      dataList: [],
      dataList01: [], //列表数据(不分页)
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      myQueryParam: {
        topicName: '',
        keywords: '',
        startDate: '',
        endDate: '',
      }
    }
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList()
  },
  computed: {
    filteredDataList() {
      return this.dataList.filter(item => item.topicReviewStatus === 3 || item.topicReviewStatus === '3');
    }
  },
  methods: {
    //创建计划跳转
    newPlanHandle(id) {
      let filteredArray = [];
      // 遍历原始数组
      for (let i = 0; i < this.dataList.length; i++) {
        if (this.dataList[i].qcsrId === id) {
          // 如果满足条件，将对象添加到新数组中
          filteredArray.push(this.dataList[i]);
        }
      }
      this.$router.push(
        {
          name: 'qcPlanNew',
          query: {
            data: JSON.stringify(filteredArray)
          }
        });
    },
    //计划审批跳转
    examineStatus(id, resultType) {
      console.log(resultType)
      console.log(id)
      if (resultType === null || resultType === '') {
        this.$message({
          message: '课题计划尚未提交',
          type: 'warning',
          duration: 1500
        })
      } else {
        let filteredArray = [];
        // 遍历原始数组
        for (let i = 0; i < this.dataList.length; i++) {
          if (this.dataList[i].qcsrId === id) {
            // 如果满足条件，将对象添加到新数组中
            filteredArray.push(this.dataList[i]);
            console.log(filteredArray)
          }
        }
        // console.log('+-+-+-+-++-+-+--+')
        // console.log(this.dataList)
        // console.log('+-+-+-+-++-+-+--+')
        this.$router.push(
          {
            name: 'qcExamineStatus',
            query: {
              data: JSON.stringify(filteredArray)
            }
          });
      }
    },

    toIssue() {
      this.$router.push(
        {
          name: 'otherToIssue',
          // query: {
          //   data: JSON.stringify(filteredArray)
          // }
        });
    },
    toStatus(num, type) {
      if (type == '创新型') {
        if (num == '1') {
          return '设定目标'
        } else if (num == '2') {
          return '提出方案确定最佳方案'
        } else if (num == '3') {
          return '制定对策'
        } else if (num == '4') {
          return '对策实施'
        } else if (num == '5') {
          return '检查效果'
        } else if (num == '6') {
          return '标准化'
        } else if (num == '7') {
          return '总结和下一步打算'
        } else if (num == '8') {
          return '完成'
        } else {
          return '选择课题'
        }
      } else if (type === '问题解决型') {
        if (num === '1') {
          return '现状调查'
        } else if (num === '2') {
          return '设定目标'
        } else if (num === '3') {
          return '原因分析'
        } else if (num === '4') {
          return '要因确定'
        } else if (num === '5') {
          return '制定对策'
        } else if (num === '6') {
          return '对策实施'
        } else if (num === '7') {
          return '检查效果'
        } else if (num === '8') {
          return '巩固措施'
        } else if (num === '9') {
          return '总结和下一步打算'
        } else if (num === '10') {
          return '完成'
        } else {
          return '选择课题'
        }
      } else if (type === '问题解决型(指令型)') {
        if (num === '1') {
          return '设定目标'
        } else if (num === '2') {
          return '可靠性分析'
        } else if (num === '3') {
          return '原因分析'
        } else if (num === '4') {
          return '要因确定'
        } else if (num === '5') {
          return '制定对策'
        } else if (num === '6') {
          return '对策实施'
        } else if (num === '7') {
          return '检查效果'
        } else if (num === '8') {
          return '巩固措施'
        } else if (num === '9') {
          return '总结和下一步打算'
        } else if (num === '10') {
          return '完成'
        } else {
          return '选择课题'
        }
      } else {
        return '-'
      }
    },
    parseTime(time) {
      return new Date(time).toLocaleString();
    },
    // 获取数据列表
    async getDataList() {
      this.dataListLoading = true
      await this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/finishedList'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'topicName': this.myQueryParam.topicName,
          'keywords': this.myQueryParam.keywords,
          'startDate': this.myQueryParam.startDate,
          'endDate': this.myQueryParam.endDate,
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
      console.log(this.dataListSelections)
      // console.log(id)
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.qcsrId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/qcSubject/registration/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
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
      })
    },
    //导出excel
    async exportAll(){
        await this.$http({
          url: this.$http.adornUrl('/qcSubject/registration/finishedList01'),
          method: 'get',
          params: this.$http.adornParams({
          'topicName': this.myQueryParam.topicName,
          'keywords': this.myQueryParam.keywords,
          'startDate': this.myQueryParam.startDate,
          'endDate': this.myQueryParam.endDate,
          })
        }).then(({data}) => {
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
              课题ID:tableRow.qcsrId,
              课题名称: tableRow.topicName,
              课题编号:tableRow.topicNumber,
              课题组长:tableRow.topicLeader,
              课题顾问:tableRow.topicConsultant,
              小组成员:tableRow.teamNumberIds,
              开始日期:tableRow.startDate,
              结束日期:tableRow.endDate,
              课题描述:tableRow.topicDescription,
              课题类型:tableRow.topicType,
              活动特性:tableRow.activityCharacteristics,
              课题关键字:tableRow.keywords,
              课题活动评分结果:tableRow.topicActivityResult,
              提交类型:tableRow.resultType,
              成果认定审核意见:tableRow.qcTwoContent,
              相关方审核意见:tableRow.qcThreeContent,
              成果初评分数:tableRow.qcFirstScore,
              成果复评分数:tableRow.qcSecondScore,
              成果复评审核意见:tableRow.qcFiveContent,
              财务部审核意见:tableRow.qcSixContent,
              终评审核意见:tableRow.qcSixContent
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
              "QC知识库数据.xlsx"
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
