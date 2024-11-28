<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getPChartDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getPChartDataList()">查询</el-button>
        <el-button v-if="isAuth('spc:spcpchart:save')" type="primary" @click="PChartaddOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('spc:spcpchart:delete')" type="danger" @click="PChartdeleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="PChartselectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="spcpId"
        header-align="center"
        align="center"
        label="主键">
      </el-table-column>
      <el-table-column
        prop="date1"
        header-align="center"
        align="center"
        label="当月第1天数据">
      </el-table-column>
      <el-table-column
        prop="date2"
        header-align="center"
        align="center"
        label="当月第2天数据">
      </el-table-column>
      <el-table-column
        prop="date3"
        header-align="center"
        align="center"
        label="当月第3天数据">
      </el-table-column>
      <el-table-column
        prop="date4"
        header-align="center"
        align="center"
        label="当月第4天数据">
      </el-table-column>
      <el-table-column
        prop="date5"
        header-align="center"
        align="center"
        label="当月第5天数据">
      </el-table-column>
      <el-table-column
        prop="date6"
        header-align="center"
        align="center"
        label="当月第6天数据">
      </el-table-column>
      <el-table-column
        prop="date7"
        header-align="center"
        align="center"
        label="当月第7天数据">
      </el-table-column>
      <el-table-column
        prop="date8"
        header-align="center"
        align="center"
        label="当月第8天数据">
      </el-table-column>
      <el-table-column
        prop="date9"
        header-align="center"
        align="center"
        label="当月第9天数据">
      </el-table-column>
      <el-table-column
        prop="date10"
        header-align="center"
        align="center"
        label="当月第10天数据">
      </el-table-column>
      <el-table-column
        prop="date11"
        header-align="center"
        align="center"
        label="当月第11天数据">
      </el-table-column>
      <el-table-column
        prop="date12"
        header-align="center"
        align="center"
        label="当月第12天数据">
      </el-table-column>
      <el-table-column
        prop="date13"
        header-align="center"
        align="center"
        label="当月第13天数据">
      </el-table-column>
      <el-table-column
        prop="date14"
        header-align="center"
        align="center"
        label="当月第14天数据">
      </el-table-column>
      <el-table-column
        prop="date15"
        header-align="center"
        align="center"
        label="当月第15天数据">
      </el-table-column>
      <el-table-column
        prop="date16"
        header-align="center"
        align="center"
        label="当月第16天数据">
      </el-table-column>
      <el-table-column
        prop="date17"
        header-align="center"
        align="center"
        label="当月第17天数据">
      </el-table-column>
      <el-table-column
        prop="date18"
        header-align="center"
        align="center"
        label="当月第18天数据">
      </el-table-column>
      <el-table-column
        prop="date19"
        header-align="center"
        align="center"
        label="当月第19天数据">
      </el-table-column>
      <el-table-column
        prop="date20"
        header-align="center"
        align="center"
        label="当月第20天数据">
      </el-table-column>
      <el-table-column
        prop="date21"
        header-align="center"
        align="center"
        label="当月第21天数据">
      </el-table-column>
      <el-table-column
        prop="date22"
        header-align="center"
        align="center"
        label="当月第22天数据">
      </el-table-column>
      <el-table-column
        prop="date23"
        header-align="center"
        align="center"
        label="当月第23天数据">
      </el-table-column>
      <el-table-column
        prop="date24"
        header-align="center"
        align="center"
        label="当月第24天数据">
      </el-table-column>
      <el-table-column
        prop="date25"
        header-align="center"
        align="center"
        label="当月第25天数据">
      </el-table-column>
      <el-table-column
        prop="date26"
        header-align="center"
        align="center"
        label="当月第26天数据">
      </el-table-column>
      <el-table-column
        prop="date27"
        header-align="center"
        align="center"
        label="当月第27天数据">
      </el-table-column>
      <el-table-column
        prop="date28"
        header-align="center"
        align="center"
        label="当月第28天数据">
      </el-table-column>
      <el-table-column
        prop="date29"
        header-align="center"
        align="center"
        label="当月第29天数据">
      </el-table-column>
      <el-table-column
        prop="date30"
        header-align="center"
        align="center"
        label="当月第30天数据">
      </el-table-column>
      <el-table-column
        prop="date31"
        header-align="center"
        align="center"
        label="当月第31天数据">
      </el-table-column>
      <el-table-column
        prop="sum"
        header-align="center"
        align="center"
        label="合计">
      </el-table-column>
      <el-table-column
        prop="datatime"
        header-align="center"
        align="center"
        label="数据日期">
      </el-table-column>
      <el-table-column
        prop="dataContent"
        header-align="center"
        align="center"
        label="数据内容（抽检数/不良数）">
      </el-table-column>
      <el-table-column
        prop="createBy"
        header-align="center"
        align="center"
        label="创建人">
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="alternateFields1"
        header-align="center"
        align="center"
        label="备用字段1">
      </el-table-column>
      <el-table-column
        prop="alternateFields2"
        header-align="center"
        align="center"
        label="备用字段2">
      </el-table-column>
      <el-table-column
        prop="alternateFields3"
        header-align="center"
        align="center"
        label="备用字段3">
      </el-table-column>
      <el-table-column
        prop="alternateFields4"
        header-align="center"
        align="center"
        label="备用字段4">
      </el-table-column>
      <el-table-column
        prop="deleteFlag"
        header-align="center"
        align="center"
        label="逻辑删除标志">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="PChartaddOrUpdateHandle(scope.row.spcpId)">修改</el-button>
          <el-button type="text" size="small" @click="PChartdeleteHandle(scope.row.spcpId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="PChartsizeChangeHandle"
      @current-change="PChartcurrentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getPChartDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './spcpchart-add-or-update'
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
      this.getPChartDataList()
    },
    methods: {
      // 获取数据列表
      getPChartDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/spc/spcpchart/list'),
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
      PChartsizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getPChartDataList()
      },
      // 当前页
      PChartcurrentChangeHandle (val) {
        this.pageIndex = val
        this.getPChartDataList()
      },
      // 多选
      PChartselectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      PChartaddOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      PChartdeleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.spcpId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/spc/spcpchart/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getPChartDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },
    }
  }
</script>
