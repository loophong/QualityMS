<template>
  <div class="mod-config">
    <el-form :inline="true" :model="queryParams" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-select v-model="queryParams.dataContent" filterable placeholder="请选择数据">
          <el-option v-for="field in dataList1" :key="field.dataId" :value="field.dataContent">
            {{ field.dataContent }}
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button @click="resetQuery()">重置</el-button>
        <el-button v-if="isAuth('indicator:indicatordatadictionary:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('indicator:indicatordatadictionary:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
<!--      <el-table-column-->
<!--        prop="dataId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="数据ID">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="dataContent"
        header-align="center"
        align="center"
        label="数据">
      </el-table-column>
      <el-table-column
        prop="dataType"
        header-align="center"
        align="center"
        label="数据类型">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.dataId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.dataId)">删除</el-button>
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
  import AddOrUpdate from './indicatordatadictionary-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          key: ''
        },
        //查询残水列表
        queryParams: {
          dataContent: '',
          dataType: ''
        },
        dataList: [],
        dataList1: [], //查询列表（不分页）
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
          url: this.$http.adornUrl('/indicator/indicatordatadictionary/list'),
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
        //不分页查询
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatordatadictionary/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': 10000,
            'key': this.queryParams
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList1 = data.page.list
          } else {
            this.dataList1 = []
          }
        })
      },
      //查询重置
      resetQuery() {
        this.queryParams = {
          dataContent: null,
          dataType: null
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
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.dataId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/indicator/indicatordatadictionary/delete'),
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
      }
    }
  }
</script>
