<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('qcPlan:step:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('qcPlan:step:delete')" type="danger" @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column>
      <el-table-column prop="stepId" header-align="center" align="center" label="步骤主键id">
      </el-table-column>
      <el-table-column prop="stepSubjectId" header-align="center" align="center" label="关联课题id">
      </el-table-column>
      <el-table-column prop="stepType" header-align="center" align="center" label="课题类型">
      </el-table-column>
      <el-table-column prop="stepProcess" header-align="center" align="center" label="步骤id">
      </el-table-column>
      <el-table-column prop="stageName" header-align="center" align="center" label="阶段名称">
      </el-table-column>
      <el-table-column prop="stagePlanStart" header-align="center" align="center" label="计划开始时间">
      </el-table-column>
      <el-table-column prop="stagePlanEnd" header-align="center" align="center" label="计划结束时间">
      </el-table-column>
      <el-table-column prop="stageActualStart" header-align="center" align="center" label="实际开始时间">
      </el-table-column>
      <el-table-column prop="stageActualEnd" header-align="center" align="center" label="实际结束时间">
      </el-table-column>
      <el-table-column prop="stagePeople" header-align="center" align="center" label="参与人员">
      </el-table-column>
      <el-table-column prop="stageDescribe" header-align="center" align="center" label="步骤描述">
      </el-table-column>
      <el-table-column prop="stageExtra" header-align="center" align="center" label="备注">
      </el-table-column>
      <el-table-column prop="stageBefore" header-align="center" align="center" label="活动前现状">
      </el-table-column>
      <el-table-column prop="stageAfter" header-align="center" align="center" label="活动后现状">
      </el-table-column>
      <el-table-column prop="stageConsolidate" header-align="center" align="center" label="巩固措施">
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.stepId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.stepId)">删除</el-button>
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
import AddOrUpdate from './qcstep-add-or-update'
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
        url: this.$http.adornUrl('/qcPlan/step/list'),
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
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
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
        return item.stepId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/qcPlan/step/delete'),
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
