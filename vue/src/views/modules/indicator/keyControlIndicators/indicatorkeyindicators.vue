<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('indicator:indicatorkeyindicators:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('indicator:indicatorkeyindicators:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
        <el-button v-if="isAuth('indicator:indicatorkeyindicators:save')" type="danger" @click="issueHandle()">问题添加</el-button>
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
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatorkeyindicators/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
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
      //问题模块跳转
      issueHandle() {
        this.$router.push({ name: 'otherToIssue' })
      }
    }
  }
</script>
