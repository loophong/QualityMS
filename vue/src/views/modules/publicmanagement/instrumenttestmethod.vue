<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button  v-if="isAuth('generator:instrumenttestmethod:list')" type="primary" @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('generator:instrumenttestmethod:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('generator:instrumenttestmethod:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
      <!-- <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="">
      </el-table-column> -->
      <el-table-column
        prop="department"
        header-align="center"
        align="center"
        label="科室">
      </el-table-column>
      <el-table-column
        prop="standardnumber"
        header-align="center"
        align="center"
        label="标准编号">
      </el-table-column>
      <el-table-column
        prop="compilation"
        header-align="center"
        align="center"
        label="编制">
      </el-table-column>
      <el-table-column
        prop="compilationdate"
        header-align="center"
        align="center"
        label="编制日期">
      </el-table-column>
      <el-table-column
        prop="reviewer"
        header-align="center"
        align="center"
        label="审核">
      </el-table-column>
      <el-table-column
        prop="reviewdate"
        header-align="center"
        align="center"
        label="审核日期">
      </el-table-column>
      <el-table-column
        prop="countersignature"
        header-align="center"
        align="center"
        label="会签">
      </el-table-column>
      <el-table-column
        prop="approval"
        header-align="center"
        align="center"
        label="批准">
      </el-table-column>
      <el-table-column
        prop="inspectionparts"
        header-align="center"
        align="center"
        label="检验零部件">
      </el-table-column>
      <el-table-column
        prop="drawingnumber"
        header-align="center"
        align="center"
        label="图号">
      </el-table-column>
      <el-table-column
        prop="inspectionpartcategory"
        header-align="center"
        align="center"
        label="检验件类别">
      </el-table-column>
      <el-table-column
        prop="processname"
        header-align="center"
        align="center"
        label="工序名称">
      </el-table-column>
      <el-table-column
        prop="inspectioncategory"
        header-align="center"
        align="center"
        label="检验类别">
      </el-table-column>
      <el-table-column
        prop="inspectionitem"
        header-align="center"
        align="center"
        label="检验项目">
      </el-table-column>
      <el-table-column
        prop="inspectionparametername"
        header-align="center"
        align="center"
        label="检验参数名称">
      </el-table-column>
      <el-table-column
        prop="inspectionlocation"
        header-align="center"
        align="center"
        label="检验部位">
      </el-table-column>
      <el-table-column
        prop="upperlimit"
        header-align="center"
        align="center"
        label="上限">
      </el-table-column>
      <el-table-column
        prop="lowerlimit"
        header-align="center"
        align="center"
        label="下限">
      </el-table-column>
      <el-table-column
        prop="measurementinstrument"
        header-align="center"
        align="center"
        label="测量仪器名称">
      </el-table-column>
       <el-table-column
        prop="creatPeople"
        header-align="center"
        align="center"
        label="量具编号">
      </el-table-column>
      <el-table-column
        prop="checkoperationmanual"
        header-align="center"
        align="center"
        label="检查操作要领">
      </el-table-column>
      <el-table-column
        prop="inspector"
        header-align="center"
        align="center"
        label="检查员">
      </el-table-column>
      <el-table-column
        prop="inspectionfrequency"
        header-align="center"
        align="center"
        label="检查频次">
      </el-table-column>
      <el-table-column
        prop="recordrequirement"
        header-align="center"
        align="center"
        label="记录要求">
      </el-table-column>
      <el-table-column
        prop="patrolinspector"
        header-align="center"
        align="center"
        label="巡检人">
      </el-table-column>
      <el-table-column
        prop="calibrationtool"
        header-align="center"
        align="center"
        label="检具校正">
      </el-table-column>
      <el-table-column
        prop="calibrationfrequency"
        header-align="center"
        align="center"
        label="校正频次">
      </el-table-column>
     
      <!-- <el-table-column
        prop="creatTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column> -->
      <!-- <el-table-column
        prop="flag"
        header-align="center"
        align="center"
        label="审批flag">
      </el-table-column> -->

   <el-table-column prop="flag" label="审批状态" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.flag === '0'">
                <el-tag type="info" disable-transitions>审批中</el-tag></span>
              <span v-else-if="scope.row.flag === '1'">
                <el-tag disable-transitions>审批通过</el-tag></span>
              <span v-else-if="scope.row.flag === '2'">
                <el-tag disable-transitions>驳回审批</el-tag></span>
        
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>


      <!-- <el-table-column
        prop="delteflag"
        header-align="center"
        align="center"
        label="是否失效"> -->
        <!-- <el-table-column prop="flag" label="是否失效" header-align="center" align="center">
         <template slot-scope="scope">
              <span v-if="scope.row.delteflag === '0'">
                <el-tag type="info" disable-transitions>未失效</el-tag></span>
              <span v-else-if="scope.row.delteflag === '1'">
                <el-tag disable-transitions>已失效</el-tag></span>
              <span v-else-if="scope.row.delteflag === '2'">
                <el-tag disable-transitions>驳回审批</el-tag></span>
              <span v-else>-</span> 
          </template>
      </el-table-column> --> 
      
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
           <el-button type="text" size="small" @click="verify(scope.row.id)">审核</el-button>
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
  import AddOrUpdate from './instrumenttestmethod-add-or-update'
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
          url: this.$http.adornUrl('/generator/instrumenttestmethod/list'),
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
       // 审核
       verify(id) {
      this.$confirm('请确认是否审核通过?', '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '不通过',
        type: 'warning'
      }).then(() => {
        // 用户选择审核通过
        this.sendAuditRequest(id, '/generator/instrumenttestmethod/success');
      }).catch(() => {
        // 用户选择审核不通过
        this.sendAuditRequest(id, '/generator/instrumenttestmethod/fail');
      });
    },
    sendAuditRequest(id, path) {
      this.$http({
        url: this.$http.adornUrl(`${path}/${id}`),
        method: 'post',
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.getDataList(); // 假设这是刷新列表的方法
            }
          });
        } else {
          this.$message.error(data.msg);
        }
      }).catch(error => {
        this.$message.error('操作失败：' + error.toString());
      });
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
            url: this.$http.adornUrl('/generator/instrumenttestmethod/delete'),
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
