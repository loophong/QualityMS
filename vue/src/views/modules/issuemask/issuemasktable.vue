<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="recigetDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="recigetDataList()">查询</el-button>
<!--        <el-button v-if="isAuth('generator:issuemasktable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
<!--        <el-button v-if="isAuth('generator:issuemasktable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
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
<!--      <el-table-column-->
<!--        prop="issuemaskId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="serialNumber"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="序号">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="issueNumber"
        header-align="center"
        align="center"
        label="问题编号(所属问题)">
      </el-table-column>
      <el-table-column
        prop="reviewers"
        header-align="center"
        align="center"
        label="审核人">
      </el-table-column>
      <el-table-column
        prop="recipients"
        header-align="center"
        align="center"
        label="接收人">
      </el-table-column>
      <el-table-column
        prop="maskcontent"
        header-align="center"
        align="center"
        label="任务内容">
      </el-table-column>
      <el-table-column
        prop="creator"
        header-align="center"
        align="center"
        label="任务发起人">
      </el-table-column>
      <el-table-column
        prop="creationTime"
        header-align="center"
        align="center"
        label="发起时间">
      </el-table-column>
      <el-table-column
        prop="requiredCompletionTime"
        header-align="center"
        align="center"
        label="要求完成时间">
      </el-table-column>
      <el-table-column
        prop="state"
        header-align="center"
        align="center"
        label="状态">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
<!--          <el-button v-if="showButtons" type="text" size="small" @click="addOrUpdateHandle(scope.row.issuemaskId)">修改</el-button>-->
<!--          <el-button v-if="showButtons" type="text" size="small" @click="deleteHandle(scope.row.issuemaskId)">删除</el-button>-->
          <el-button v-if="showButtons" type="text" size="small" @click="completeHandle(scope.row.issuemaskId,scope.row)">完成</el-button>
          <el-button v-if="showButtons" type="text" size="small" @click="dispatchHandle(scope.row.issuemaskId,scope.row)">派发</el-button>
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
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="recigetDataList"></add-or-update>
    <add-or-update v-if="assertOrUpdateVisible" ref="assetOrUpdate" @refreshDataList="recigetDataList"></add-or-update>
    <add-or-update v-if="completeVisible" ref="comleted" @refreshDataList="recigetDataList"></add-or-update>

  </div>
</template>

<script>
  import AddOrUpdate from './issuemasktable-add-or-update'
  export default {
    data () {
      return {
        showButtons: true,
        showCompleteButton: false,
        assertOrUpdateVisible: false,
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
        completeVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.recigetDataList()
    },
    methods: {
      // 任务派发
      dispatchHandle (id ,row) {
        // 派发操作的逻辑
        console.log('派发操作', id)
        this.assertOrUpdateVisible = true
        if(row.state === '审核中')
          this.$message.error('任务审核中！')
          else if (row.state === '已派发')
          this.$message.error('任务已派发！')
        else {
          this.$nextTick(() => {
            this.$refs.assetOrUpdate.init1(id)
          })
        }
      },
      // 获取数据列表，接收人可以看见的数据
      recigetDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/generator/issuemasktable/recipientslist'),
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
        this.recigetDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.recigetDataList()
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
      completeHandle (id ,row) {
        if(row.state === '审核中')
          this.$message.error('任务审核中！')
        else if(row.state === '已派发')
          this.$message.error('任务已派发！')
        else {
          this.assertOrUpdateVisible = true
          this.$nextTick(() => {
            this.$refs.assetOrUpdate.completeHandle(id)
          });
        }
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.issuemaskId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/generator/issuemasktable/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.recigetDataList()
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
