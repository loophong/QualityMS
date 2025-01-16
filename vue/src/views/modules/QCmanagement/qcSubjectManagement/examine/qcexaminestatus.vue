<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="审核记录" name="1">
      <div class="mod-config">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
          <!-- <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item> -->
          <!-- <el-form-item>
        <el-button @click="getDataList()">查询</el-button> -->
          <!-- <el-button v-if="isAuth('indicator:qcexaminestatus:save')" type="primary"
          @click="addOrUpdateHandle()">新增</el-button> -->
          <!-- <el-button v-if="isAuth('indicator:qcexaminestatus:delete')" type="danger" @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0">批量删除</el-button> -->
          <!-- </el-form-item> -->
        </el-form>
        <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%;">
          <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
        </el-table-column> -->
          <!-- <el-table-column prop="qcExamineId" header-align="center" align="center" label="主键id">
        </el-table-column> -->
          <el-table-column prop="qcExamineSubject" header-align="center" align="center" label="审核关联课题ID" width="120">
          </el-table-column>
          <el-table-column prop="qcExamineCurrent" header-align="center" align="center" label="当前待审核步骤" width="180">
            <template slot-scope="scope">
              <span v-if="scope.row.qcExamineCurrent === '1'">成果初评</span>
              <span v-else-if="scope.row.qcExamineCurrent === '2'">相关方审核</span>
              <span v-else-if="scope.row.qcExamineCurrent === '3'">成果初评</span>
              <span v-else-if="scope.row.qcExamineCurrent === '4'">成果复评，财务部审核</span>
              <span v-else-if="scope.row.qcExamineCurrent === '4.1'">财务部审核</span>
              <span v-else-if="scope.row.qcExamineCurrent === '4.2'">成果复评</span>
              <span v-else-if="scope.row.qcExamineCurrent === '5'">成果终评</span>
              <span v-else-if="scope.row.qcExamineCurrent === '完成'" style="color:green;">已完成</span>
              <!-- <span v-if="scope.row.qcStatusOne === '1'">已提交</span> -->
              <span v-else>-</span>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="qcStatusOne" header-align="center" align="center" label="课题提交状态">
        <template slot-scope="scope">
          <span v-if="scope.row.qcStatusOne === '1'">已提交</span>
          <span v-else>-</span>
        </template>
      </el-table-column> -->
          <el-table-column prop="qcStatusTwo" header-align="center" align="center" label="成果认定状态">
            <template slot-scope="scope">
              <span v-if="scope.row.qcStatusTwo === '0'" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.qcStatusTwo === '1'" style="color: #8dc146;">已通过</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="qcTwoContent" header-align="center" align="center" label="成果认定审核意见" width="180">
          </el-table-column>
          <el-table-column prop="qcStatusThree" header-align="center" align="center" label="相关方审核状态">
            <template slot-scope="scope">
              <span v-if="scope.row.qcStatusThree === '0'" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.qcStatusThree === '1'" style="color: #8dc146;">已通过</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="qcThreeContent" header-align="center" align="center" label="相关方审核意见" width="180">
          </el-table-column>
          <el-table-column prop="qcStatusFour" header-align="center" align="center" label="成果初评状态">
            <template slot-scope="scope">
              <span v-if="scope.row.qcStatusFour === '0'" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.qcStatusFour === '1'" style="color: #8dc146;">已通过</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="qcFourContent" header-align="center" align="center" label="成果初评审核意见" width="180">
          </el-table-column>
          <!-- <el-table-column prop="qcFirstScore" header-align="center" align="center" label="成果初评分数" width="120">
          </el-table-column> -->
          <el-table-column prop="qcStatusFive" header-align="center" align="center" label="成果复评状态">
            <template slot-scope="scope">
              <span v-if="scope.row.qcStatusFive === '0'" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.qcStatusFive === '1'" style="color: #8dc146;">已通过</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <!-- <el-table-column prop="qcSecondScore" header-align="center" align="center" label="成果复评分数" width="120">
          </el-table-column> -->
          <el-table-column prop="qcFiveContent" header-align="center" align="center" label="成果复评审核意见" width="180">
          </el-table-column>
          <el-table-column prop="qcStatusSix" header-align="center" align="center" label="财务部审核状态">
            <template slot-scope="scope">
              <span v-if="scope.row.qcStatusSix === '0'" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.qcStatusSix === '1'" style="color: #8dc146;">已通过</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="qcSixContent" header-align="center" align="center" label="财务部审核意见" width="180">
          </el-table-column>
          <el-table-column prop="qcStatusSeven" header-align="center" align="center" label="终评提交状态">
            <template slot-scope="scope">
              <span v-if="scope.row.qcStatusSeven === '0'" style="color: #f43628;">未通过</span>
              <span v-else-if="scope.row.qcStatusSeven === '1'" style="color: #8dc146;">已通过</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="qcSevenContent" header-align="center" align="center" label="终评审核意见" width="180">
          </el-table-column>
          <!-- <el-table-column prop="qcOneContent" header-align="center" align="center" label="成果认定审核意见">
      </el-table-column> -->
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" v-if="isAuth('qcManagement:group:admin')"
                @click="addOrUpdateHandle(scope.row.qcExamineId)">修改</el-button>
              <!-- <el-button type="text" size="small" @click="deleteHandle(scope.row.qcExamineId)">删除</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination> -->
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import AddOrUpdate from './qcexaminestatus-add-or-update'
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
    AddOrUpdate
  },
  activated() {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/qcManagement/examineStatus/list'),
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
        return item.qcExamineId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/indicator/qcexaminestatus/delete'),
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
