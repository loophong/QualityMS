<template>
  <div class="mod-config">
    <el-form :inline="true" :model="myQueryParam" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="myQueryParam.topicName" placeholder="课题名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="myQueryParam.keywords" placeholder="课题关键字" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('qcSubject:registration:save')" type="primary"
          @click="addOrUpdateHandle()">新增</el-button>
        <el-button type="danger" @click="toIssue()">问题添加</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="filteredDataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column prop="topicName" header-align="center" align="center" label="课题名称">
      </el-table-column>
      <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
      </el-table-column>
      <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
      </el-table-column>
      <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
      </el-table-column>
      <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
      </el-table-column>
      <el-table-column prop="startDate" header-align="center" align="center" label="开始日期">
      </el-table-column>
      <el-table-column prop="endDate" header-align="center" align="center" label="结束日期">
      </el-table-column>
      <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述">
      </el-table-column>
      <el-table-column prop="topicType" header-align="center" align="center" label="课题类型">
      </el-table-column>
      <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
      </el-table-column>
      <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字">
      </el-table-column>
      <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态">
      </el-table-column>
      <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
      </el-table-column>
      <el-table-column prop="resultType" header-align="center" align="center" label="提交类型">
      </el-table-column>
      <el-table-column prop="note" header-align="center" align="center" label="备注">
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="newPlanHandle(scope.row.qcsrId)">创建计划</el-button>
          <el-button type="text" size="small" v-if="isAuth('qcSubject:registration:save')"
            @click="addOrUpdateHandle(scope.row.qcsrId)">提交计划</el-button>
          <el-button type="text" size="small"
            @click="examineStatus(scope.row.qcsrId, scope.row.resultType)">审核状态</el-button>
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
import AddOrUpdate from './qcPlan-add-or-update'
export default {
  data() {
    return {
      dataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      myQueryParam: {
        topicName: '',
        keywords: '',
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
      // console.log(resultType)
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
          }
        }
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


    parseTime(time) {
      return new Date(time).toLocaleString();
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'topicName': this.myQueryParam.topicName,
          'keywords': this.myQueryParam.keywords
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
    }
  }
}
</script>
