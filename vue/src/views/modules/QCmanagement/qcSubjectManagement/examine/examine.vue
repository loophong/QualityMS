<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="开题审核" name="1" v-if="isAuth('qc:first:examine')">
      <div class="mod-config">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getFIR()">
          <!-- <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item> -->
          <el-form-item>
            <!-- <el-button @click="getFirstList()">查询</el-button> -->
            <el-button type="danger" @click="toIssue()">问题添加</el-button>
            <!-- <el-button v-if="isAuth('qcSubject:registration:save')" type="primary"
              @click="addOrUpdateHandle()">新增</el-button> -->
            <!-- <el-button v-if="isAuth('qcSubject:registration:delete')" type="danger" @click="deleteHandle()"
              :disabled="dataListSelections.length <= 0">批量删除</el-button> -->
          </el-form-item>
        </el-form>
        <el-table :data="filteredDataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%;">
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="qcsrId" header-align="center" align="center" label="id">
          </el-table-column>
          <el-table-column prop="topicName" header-align="center" align="center" label="课题名称">
          </el-table-column>
          <el-table-column prop="topicNumber" header-align="center" align="center" label="课题编号">
          </el-table-column>
          <!-- <el-table-column prop="topicLeader" header-align="center" align="center" label="课题组长">
          </el-table-column> -->
          <el-table-column prop="topicConsultant" header-align="center" align="center" label="课题顾问">
          </el-table-column>
          <el-table-column prop="teamNumberIds" header-align="center" align="center" label="小组成员">
          </el-table-column>
          <el-table-column prop="startDate" header-align="center" align="center" label="开始日期">
          </el-table-column>
          <el-table-column prop="endDate" header-align="center" align="center" label="结束日期">
          </el-table-column>
          <el-table-column prop="topicDescription" header-align="center" align="center" label="课题描述/摘要">
          </el-table-column>
          <el-table-column prop="topicType" header-align="center" align="center" label="课题类型">
          </el-table-column>
          <el-table-column prop="activityCharacteristics" header-align="center" align="center" label="活动特性">
          </el-table-column>
          <el-table-column prop="activityPlan" header-align="center" align="center" label="活动计划">
          </el-table-column>
          <el-table-column prop="keywords" header-align="center" align="center" label="课题关键字tag">
          </el-table-column>
          <!-- <el-table-column prop="topicActivityStatus" header-align="center" align="center" label="课题活动状态">
          </el-table-column> -->
          <!-- <el-table-column prop="topicActivityResult" header-align="center" align="center" label="课题活动评分结果">
          </el-table-column> -->
          <!-- <el-table-column prop="deleteFlag" header-align="center" align="center" label="删除标记位">
            </el-table-column> -->
          <el-table-column prop="note" header-align="center" align="center" label="备注">
          </el-table-column>
          <el-table-column prop="topicReviewStatus" label="课题审核状态" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.topicReviewStatus === 0" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.topicReviewStatus === 1" style="color: gray;">未开始</span>
              <span v-else-if="scope.row.topicReviewStatus === 2" style="color: #3f9ccb;">审核中</span>
              <span v-else-if="scope.row.topicReviewStatus === 3" style="color: #8dc146;">已通过</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.qcsrId)">处理审核</el-button>
              <!-- <el-button type="text" size="small" @click="deleteHandle(scope.row.qcsrId)">删除</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="100" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getFirstList"></add-or-update>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import firstUpdate from './examine-add-or-update'
export default {
  data() {
    return {
      activeName: '1',
      dataForm: {
        key: ''
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
    firstUpdate
  },
  activated() {
    this.getFirstList()
  },
  computed: {
    filteredDataList() {
      this.totalPage = this.dataList.filter(item => item.topicReviewStatus === 2).length
      return this.dataList.filter(item => item.topicReviewStatus === 2);
    }
  },
  methods: {
    parseTime(time) {
      return new Date(time).toLocaleString();
    },
    // 获取数据列表
    getFirstList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
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
      this.getFirstList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getFirstList()
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
    toIssue() {
      this.$router.push(
        {
          name: 'otherToIssue',
          // query: {
          //   data: JSON.stringify(filteredArray)
          // }
        });
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
                this.getFirstList()
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
