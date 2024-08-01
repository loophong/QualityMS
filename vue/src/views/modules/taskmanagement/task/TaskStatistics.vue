<template>




  <div class="mod-config">


    <div>

      <el-row type="flex" class="row-bg">
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">总任务</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light"></div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">提前完成</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light"></div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">滞后完成</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light"></div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">按时完成</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light"></div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">按时完成率</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light"></div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">提前完成率</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light"></div>
        </el-col>
      </el-row>
    </div>


    <!-- 给出一个20px的间隔 -->
    <div style="margin-top: 20px;"></div>

    <!-- 将一个div横向分为三个，比例为3:1:1 ，然后把三个div放在一起-->
    <div class="flex-container" style="height: 240px;">
      <div class="flex-item item1">
        <div ref="chartContainer" style="width: 747px; height: 240px;"></div>
      </div>

      <div class="flex-item item2">
        <!-- <div  style="width: 200px; height: 200px; background-color: #fff;"></div> -->
        <div ref="onTimePieChart" style="width: 240px; height: 240px;"></div>
      </div>
      <div class="flex-item item3">
        <!-- <div style="width: 200px; height: 200px; background-color: #ddd;"></div> -->
        <div ref="earlyCompletionPieChart" style="width: 240px; height: 240px;"></div>
      </div>
    </div>



    <!-- <div ref="lineChart" style="width: 600px; height: 400px;"></div> -->

    <!-- <div ref="chartContainer" style="width: 100%; height: 400px;"></div> -->


        <!-- 给出一个20px的间隔 -->
        <div style="margin-top: 20px;"></div>

    <el-table :data="dataList" border v-loading="dataListLoading" style="width: 100%;">
      <el-table-column prop="taskId" header-align="center" align="center" label="任务ID">
      </el-table-column>
      <el-table-column prop="taskContent" header-align="center" align="center" label="任务内容">
      </el-table-column>
      <el-table-column prop="startDate" header-align="center" align="center" label="开始日期">
      </el-table-column>
      <el-table-column prop="plannedFinishDate" header-align="center" align="center" label="计划完成日期">
      </el-table-column>
      <el-table-column prop="scheduleDays" header-align="center" align="center" label="计划天数">
      </el-table-column>
      <el-table-column prop="actualFinishingDate" header-align="center" align="center" label="实际完成日期">
      </el-table-column>
      <el-table-column prop="actualDays" header-align="center" align="center" label="实际天数">
      </el-table-column>
      <el-table-column prop="finishOnTime" header-align="center" align="center" label="按时完工">
      </el-table-column>
      <el-table-column prop="daysAheadOfSchedule" header-align="center" align="center" label="提前完工">
      </el-table-column>
      <el-table-column prop="lagDays" header-align="center" align="center" label="滞后天数">
      </el-table-column>
      <el-table-column prop="delayReasons" header-align="center" align="center" label="滞后原因">
      </el-table-column>


    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>

  </div>
</template>

<script>
import AddOrUpdate from './task-add'
import * as echarts from 'echarts';
export default {
  // name: 'LineChart',
  // mounted() {
  //   this.initChart();
  // },

  mounted() {
    this.initLineChart();
    this.initOnTimePieChart();
    this.initEarlyCompletionPieChart();
  },
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
      addOrUpdateVisible: false,

      totalTasks: 100
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
        url: this.$http.adornUrl('/taskmanagement/task/list'),
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

    // initChart() {
    //   // 基于准备好的dom，初始化echarts实例
    //   const chart = echarts.init(this.$refs.lineChart);

    //   // 指定图表的配置项和数据
    //   const option = {
    //     title: {
    //       text: '折线图示例'
    //     },
    //     tooltip: {
    //       trigger: 'axis'
    //     },
    //     xAxis: {
    //       type: 'category',
    //       data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    //     },
    //     yAxis: {
    //       type: 'value'
    //     },
    //     series: [
    //       {
    //         name: '销量',
    //         type: 'line',
    //         data: [150, 230, 224, 218, 135, 147, 260]
    //       }
    //     ]
    //   };

    //   // 使用刚指定的配置项和数据显示图表。
    //   chart.setOption(option);
    // }
    initLineChart() {
      const chartContainer = this.$refs.chartContainer;
      const chartInstance = echarts.init(chartContainer);

      // 配置项
      const option = {
        title: {
          text: '项目计划进度图表',
        },
        tooltip: {},
        xAxis: {
          data: ["点1", "点2", "点3", "点4", "点5", "点6"]
        },
        yAxis: {},
        series: [{
          name: '数据',
          type: 'line',
          data: [80, 50, 30, 70, 20, 60]
        }]
      };

      // 使用刚指定的配置项和数据显示图表。
      chartInstance.setOption(option);
    },
    initOnTimePieChart() {
      this.chartInstance = echarts.init(this.$refs.onTimePieChart);
      const option = {
        title: {
          text: '按时完成率',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          // orient: 'vertical',
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              {
                value: 484, name: '已完成'
              },
              { value: 300, name: '未完成'}
            ]
          }
        ]
      };
      this.chartInstance.setOption(option);
    },

    initEarlyCompletionPieChart() {
      this.chartInstance = echarts.init(this.$refs.earlyCompletionPieChart);
      const option = {
        title: {
          text: '提前完成率',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          // orient: 'vertical',
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 1048, name: 'Search Engine' },
              { value: 735, name: 'Direct' }
            ]
          }
        ]
      };
      this.chartInstance.setOption(option);
    }


  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;

  &:last-child {
    margin-bottom: 0;
  }
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  display: flex;
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
  height: 100%;
  /* 确保父容器有高度 */
  width: 100%;
  /* 确保父容器有宽度 */
  background: #F5F7F9;
}

.bg-purple-light {
  width: 98%;
  background: #ffffff;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.row-bg {
  padding: 2px 0;
  background-color: #F5F7F9;
}


.table-text {
  margin: 0px;
  font-weight: bold;
}


.flex-container {
  display: flex;
  width: 100%;
}

.flex-item {
  border: 1px solid #ccc;
  text-align: center;
  line-height: 100%;
  height: 100%;
}
</style>