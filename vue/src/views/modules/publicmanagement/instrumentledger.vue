<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button v-if="isAuth('publicmanagement:instrumentledger:list')" type="primary" @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('publicmanagement:instrumentledger:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('publicmanagement:instrumentledger:delete')"  type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
<!-- 
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="主键">
      </el-table-column>
 -->  
      <el-table-column
        prop="companyId"
        header-align="center"
        align="center"
        label="公司编号">
      </el-table-column>
      <el-table-column
        prop="level"
        header-align="center"
        align="center"
        label="级别">
      </el-table-column>
      <el-table-column
        prop="calibrationCycle"
        header-align="center"
        align="center"
        label="检定周期">
      </el-table-column>
      <el-table-column
        prop="calibrationStatus"
        header-align="center"
        align="center"
        label="检定状态">
      </el-table-column>
      <el-table-column
        prop="usageStatus"
        header-align="center"
        align="center"
        label="使用状态">
      </el-table-column>
      <el-table-column
        prop="location"
        header-align="center"
        align="center"
        label="配置地点">
      </el-table-column>
      <el-table-column
        prop="responsibleDepartment"
        header-align="center"
        align="center"
        label="责任部门">
      </el-table-column>

      <el-table-column
        prop="user"
        header-align="center"
        align="center"
        label="使用人">
      </el-table-column>

      <el-table-column
        prop="calibrationDate"
        header-align="center"
        align="center"
        label="检校日期">
      </el-table-column>
      <el-table-column
        prop="calibrationValidity"
        header-align="center"
        align="center"
        label="检校有效期">
      </el-table-column>
      <el-table-column
        prop="calibrationUnit"
        header-align="center"
        align="center"
        label="检校单位">
      </el-table-column>
      <el-table-column
        prop="certificateNumber"
        header-align="center"
        align="center"
        label="证书编号">
      </el-table-column>
      <el-table-column
        prop="remark"
        header-align="center"
        align="center"
        label="备注">
      </el-table-column>

      <el-table-column
        prop="quantity"
        header-align="center"
        align="center"
        label="数量">
      </el-table-column>
<!-- 
      <el-table-column
        prop="flag"
        header-align="center"
        align="center"
        label="逻辑删除字段">
      </el-table-column>

      <el-table-column
        prop="creatTime"
        header-align="center"
        align="center"
        label="创建日期">
      </el-table-column>
      -->
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" v-if="isAuth('publicmanagement:instrumentledger:update')"  @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" v-if="isAuth('publicmanagement:instrumentledger:delete')"  @click="deleteHandle(scope.row.id)">删除</el-button>
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
  import AddOrUpdate from './instrumentledger-add-or-update'
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
          url: this.$http.adornUrl('/publicmanagement/instrumentledger/list'),
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
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/publicmanagement/instrumentledger/delete'),
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
