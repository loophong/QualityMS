<template>
  <div class="mod-config">

    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getPTDChartDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getPTDChartDataList()">查询</el-button>
        <el-button v-if="isAuth('spc:spcptd:save')" type="primary" @click="PTDChartaddOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('spc:spcptd:delete')" type="danger" @click="PTDChartdeleteHandle()" :disabled="PTDChartdataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="PTDChartselectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="主键">
      </el-table-column>
      <el-table-column
        prop="acceptanceRegion"
        header-align="center"
        align="center"
        label="接收区域">
      </el-table-column>
      <el-table-column
        prop="workTime"
        header-align="center"
        align="center"
        label="工时">
      </el-table-column>
      <el-table-column
        prop="ptdValue"
        header-align="center"
        align="center"
        label="正态分布值,按照公式计算">
      </el-table-column>
      <el-table-column
        prop="frequency"
        header-align="center"
        align="center"
        label="频率">
      </el-table-column>
      <el-table-column
        prop="craetTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="createPeople"
        header-align="center"
        align="center"
        label="创建人">
      </el-table-column>
      <el-table-column
        prop="deletFlag"
        header-align="center"
        align="center"
        label="逻辑删除字段">
      </el-table-column>
      <el-table-column
        prop="spare"
        header-align="center"
        align="center"
        label="备用字段">
      </el-table-column>
      <el-table-column
        prop="dataImportTime"
        header-align="center"
        align="center"
        label="数据导入日期">
      </el-table-column>
      <el-table-column
        prop="updateTime"
        header-align="center"
        align="center"
        label="数据更新时间">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="PTDChartaddOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="PTDChartdeleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="PTDChartsizeChangeHandle"
      @current-change="PTDChartcurrentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="PTDChartaddOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getPTDChartDataList"></add-or-update>

  </div>
</template>

<script>
  import AddOrUpdate from './spcptd-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          key: ''
        },
        PTDChartdataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        PTDChartdataListSelections: [],
        PTDChartaddOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getPTDChartDataList()
    },
    methods: {
      // 获取数据列表
      getPTDChartDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/spc/spcptd/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.PTDChartdataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.PTDChartdataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      PTDChartsizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getPTDChartDataList()
      },
      // 当前页
      PTDChartcurrentChangeHandle (val) {
        this.pageIndex = val
        this.getPTDChartDataList()
      },
      // 多选
      PTDChartselectionChangeHandle (val) {
        this.PTDChartdataListSelections = val
      },
      // 新增 / 修改
      PTDChartaddOrUpdateHandle (id) {
        this.PTDChartaddOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      PTDChartdeleteHandle (id) {
        var ids = id ? [id] : this.PTDChartdataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/spc/spcptd/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getPTDChartDataList()
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
