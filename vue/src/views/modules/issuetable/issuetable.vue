<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
<!--        <el-button v-if="isAuth('generator:issuetable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
<!--        <el-button v-if="isAuth('generator:issuetable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="taskDetailVisible" title="问题详情">
      <div>
        <span style="font-weight: bold; font-size: 16px; margin-right: 10px;">总任务数量：{{ taskDetails.totalCount }}</span>
        <span style="font-weight: bold; font-size: 16px;">已完成数量：{{ taskDetails.completedCount }}</span>
      </div>
      <div id="task-chart" style="width: 100%; height: 400px;"></div> <!-- 用于echarts图表 -->
      <div slot="footer">
        <el-button @click="taskDetailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      :row-class-name="getRowClassName"
      style="width: 100%;">
    <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="issueId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="serialNumber"
        header-align="center"
        align="center"
        label="序号">
      </el-table-column>
      <el-table-column
        prop="issueNumber"
        header-align="center"
        align="center"
        label="问题编号">
      </el-table-column>
      <el-table-column
        prop="inspectionDepartment"
        header-align="center"
        align="center"
        label="检查科室">
      </el-table-column>
      <el-table-column
        prop="inspectionDate"
        header-align="center"
        align="center"
        label="检查日期">
      </el-table-column>
      <el-table-column
        prop="issueCategoryId"
        header-align="center"
        align="center"
        label="问题类别ID">
      </el-table-column>
      <el-table-column
        prop="vehicleTypeId"
        header-align="center"
        align="center"
        label="车型ID">
      </el-table-column>
      <el-table-column
        prop="vehicleNumberId"
        header-align="center"
        align="center"
        label="车号ID">
      </el-table-column>
      <el-table-column
        prop="issueDescription"
        header-align="center"
        align="center"
        label="问题描述">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="issuePhoto"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="问题照片">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="issuePhoto"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="问题照片">-->
<!--        <template slot-scope="scope">-->
<!--          <el-image :src="scope.row.issuePhoto" style="width: 100px;height: 100px;"></el-image>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column
        prop="issuePhoto"
        header-align="center"
        align="center"
        label="问题照片">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="previewImage(scope.row.issuePhoto)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="rectificationRequirement"
        header-align="center"
        align="center"
        label="整改要求">
      </el-table-column>
      <el-table-column
        prop="requiredCompletionTime"
        header-align="center"
        align="center"
        label="要求完成时间">
      </el-table-column>
      <el-table-column
        prop="responsibleDepartment"
        header-align="center"
        align="center"
        label="责任科室">
      </el-table-column>
      <el-table-column
        prop="rectificationStatus"
        header-align="center"
        align="center"
        label="整改情况">
      </el-table-column>
      <el-table-column
        prop="actualCompletionTime"
        header-align="center"
        align="center"
        label="实际完成时间">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="rectificationPhotoDeliverable"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="整改照片交付物">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="rectificationPhotoDeliverable"
        header-align="center"
        align="center"
        label="整改照片交付物">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="previewImage(scope.row.rectificationPhotoDeliverable)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="rectificationResponsiblePerson"
        header-align="center"
        align="center"
        label="整改责任人">
      </el-table-column>
      <el-table-column
        prop="requiredSecondRectificationTime"
        header-align="center"
        align="center"
        label="要求二次整改时间">
      </el-table-column>
      <el-table-column
        prop="remark"
        header-align="center"
        align="center"
        label="备注">
      </el-table-column>
      <el-table-column
        prop="creator"
        header-align="center"
        align="center"
        label="创建人">
      </el-table-column>
      <el-table-column
        prop="creationTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="lastModifier"
        header-align="center"
        align="center"
        label="最后修改人">
      </el-table-column>
      <el-table-column
        prop="lastModificationTime"
        header-align="center"
        align="center"
        label="最后修改时间">
      </el-table-column>
      <el-table-column
        prop="associatedRectificationRecords"
        header-align="center"
        align="center"
        label="关联问题整改记录">
      </el-table-column>
      <el-table-column
        prop="associatedIssueAddition"
        header-align="center"
        align="center"
        label="关联问题添加">
      </el-table-column>
      <el-table-column
        prop="creationDuration"
        header-align="center"
        align="center"
        label="创建时长">
      </el-table-column>
      <el-table-column
        prop="causeAnalysis"
        header-align="center"
        align="center"
        label="原因分析">
      </el-table-column>
      <el-table-column
        prop="rectificationVerificationStatus"
        header-align="center"
        align="center"
        label="整改验证情况">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="verificationConclusion"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="验证结论">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="verificationConclusion"
        header-align="center"
        align="center"
        label="验证结论">
        <template slot-scope="scope">
          <div>
            <span v-for="(state, index) in getStates(scope.row.verificationConclusion)" :key="index">
                <el-tag v-if="state === '未完成'" type="danger" disable-transitions>{{ state }}</el-tag>
                <el-tag v-else-if="state === '已完成'" type="success" disable-transitions>{{ state }}</el-tag>
                <el-tag v-else-if="state === '持续'" type="info" disable-transitions>{{ state }}</el-tag>
                <el-tag v-else-if="state === '结项'" type="warning" disable-transitions>{{ state }}</el-tag>
                <el-tag v-else-if="state === '未完成，持续'" type="danger" disable-transitions>{{ state }}</el-tag>
                <el-tag v-else-if="state === '持续，未完成'" type="info" disable-transitions>{{ state }}</el-tag>
                <el-tag v-else>{{ state }}</el-tag> <!-- 处理未定义的状态 -->
            </span>
          </div>
        </template>
      </el-table-column>

      <el-table-column
        prop="verifier"
        header-align="center"
        align="center"
        label="验证人">
      </el-table-column>
      <el-table-column
        prop="reviewers"
        header-align="center"
        align="center"
        label="审核人">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="level"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="问题等级">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="state"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="问题状态">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="formula"
        header-align="center"
        align="center"
        label="公式">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
<!--          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.issueId)">修改</el-button>-->
          <el-button type="text" size="small" @click="showTaskDetails(scope.row.issueNumber)">问题详情</el-button>
          <el-button type="text" size="small" @click="closeRelatedTasks(scope.row.issueId)">问题关闭</el-button>
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
  import AddOrUpdate from '../issuefind/issuetable-add-or-update.vue'

  // import {isAuth} from "../../../utils";
  export default {
    data () {
      return {
        tableData: [],
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
        taskDetailVisible: false, // 控制弹窗可见性
        taskDetails: {
          totalCount: 0,
          completedCount: 0,
          inProgressCount: 0,
          reviewingCount: 0,
        },
      }

    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
      this.fetchData()
    },
    methods: {
      getStates(verificationConclusion) {
        if (!verificationConclusion) return [];
        // 按照逗号分隔，并去除多余的空格
        return verificationConclusion.split(',').map(state => state.trim());
      },
      showTaskDetails(issueNumber) {
        this.fetchTaskDetails(issueNumber);
        this.taskDetailVisible = true; // 显示弹窗
      },
      fetchTaskDetails(issueNumber) {
        this.$http({
          url: this.$http.adornUrl(`/generator/issuetable/taskDetails/${issueNumber}`), // 请求后端API
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.taskDetails.totalCount = data.result.totalCount; // 总任务数量
            this.taskDetails.completedCount = data.result.completedCount; // 已完成数量
            this.taskDetails.inProgressCount = data.result.inProgressCount; // 执行中任务数量
            this.taskDetails.reviewingCount = data.result.reviewingCount; // 审核中任务数量

            // 调用方法绘制ECharts图表
            this.initECharts();
          } else {
            this.$message.error(data.msg);
          }
        });
      },
      initECharts() {
        // 使用ECharts初始化环形图
        const chartDom = document.getElementById('task-chart');
        const myChart = echarts.init(chartDom);

        // 判断任务状态数量，如果没有数据，则默认显示一个绿色环
        const hasData = this.taskDetails.completedCount > 0 || this.taskDetails.inProgressCount > 0 || this.taskDetails.reviewingCount > 0;

        const option = {
          series: [
            {
              name: '任务状态',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false, // 不显示文字
              },
              emphasis: {
                label: {
                  show: false, // 不显示文字
                },
              },
              labelLine: {
                show: false, // 不显示文字连接线
              },
              data: hasData ? [ // 根据是否有数据选择数据
                { value: this.taskDetails.completedCount, name: '已完成' },
                { value: this.taskDetails.inProgressCount, name: '执行中' },
                { value: this.taskDetails.reviewingCount, name: '审核中' },
              ] : [
                { value: 1, name: '无任务', itemStyle: { color: 'green' } } // 默认显示一个绿色的环
              ]
            }
          ]
        };

        myChart.setOption(option);
      }
      ,
      previewImage (imageUrl) {
        console.log("cur imageUrl====>" + imageUrl);
        window.open(imageUrl);
        console.log("图片地址：" ,imageUrl)
      },
      getRowClassName({ row, rowIndex }) {
        console.log(`Row index: ${rowIndex}, Class: ${rowIndex % 2 === 0 ? 'row-even' : 'row-odd'}`);
        return rowIndex % 2 === 0 ? 'row-even' : 'row-odd';
      },
      // 关闭相关任务
      closeRelatedTasks(id) {
        // 提示用户确认
        this.$confirm(`是否删除此问题并删除关联问题？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 用户确认后执行删除请求
          this.$http({
            url: this.$http.adornUrl(`/generator/issuetable/closeRelatedTasks/${id}`), // 修改为正确的URL
            method: 'post'
            // 此处不需要 data，因为我们在方法定义中直接使用了@PathVariable来接收ID
          }).then(({data}) => {
            if (data && data.code === 0) {
              // 删除成功的提示
              this.$message({
                message: '任务已成功关闭',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  // 删除成功后刷新数据列表
                  this.getDataList();
                }
              });
            } else {
              // 删除失败的提示
              this.$message.error(data.msg);
            }
          });
        }).catch(() => {
          // 用户点击了取消，不执行任何操作
          this.$message.info('操作已取消');
        });
      },

      fetchData () {
        // Assuming you have an API endpoint to fetch the data
        fetch('/api/data')
          .then(response => response.json())
          .then(data => {
            const promises = data.map(item => {
              if (item.issuePhoto) {
                return fetch(`/api/images/${item.issuePhoto}`)
                  .then(response => response.blob())
                  .then(blob => this.blobToBase64(blob))
                  .then(base64Data => {
                    item.issuePhoto = base64Data
                  })
              }
              return Promise.resolve()
            })

            Promise.all(promises).then(() => {
              this.tableData = data
            })
          })
      },
      blobToBase64 (blob) {
        return new Promise((resolve, reject) => {
          const reader = new FileReader()
          reader.onloadend = () => resolve(reader.result.split(',')[1])
          reader.onerror = reject
          reader.readAsDataURL(blob)
        })
      },

      // 获取数据列表
      // getDataList () {
      //   this.dataListLoading = true
      //   this.$http({
      //     url: this.$http.adornUrl('/generator/issuetable/list'),
      //     method: 'get',
      //     params: this.$http.adornParams({
      //       'page': this.pageIndex,
      //       'limit': this.pageSize,
      //       'key': this.dataForm.key
      //     })
      //   }).then(({data}) => {
      //     if (data && data.code === 0) {
      //       this.dataList = data.page.list
      //       this.totalPage = data.page.totalCount
      //     } else {
      //       this.dataList = []
      //       this.totalPage = 0
      //     }
      //     this.dataListLoading = false
      //   })
      // },
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list.map(item => {
              // 确保图片路径有效
              if (!item.issuePhoto || !this.isValidImageUrl(item.issuePhoto)) {
                item.issuePhoto = '默认图片路径' // 设置默认图片路径
              }
              return item
            })
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      isValidImageUrl (url) {
        // 简单的URL验证，可以根据需要扩展
        return url && (url.startsWith('http://') || url.startsWith('https://'))
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
          return item.issueId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/generator/issuetable/delete'),
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

<style scoped>
.row-even {
  background-color: #ffffff !important; /* 白色 */
}

.row-odd {
  background-color: #0BB2D4 !important; /* 灰色 */
}

</style>


