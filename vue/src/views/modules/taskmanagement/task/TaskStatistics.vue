<template>


  <div class="mod-config">

    <div>
      <el-form :inline="true" @keyup.enter.native="getData()">
        <el-select v-model="planId" placeholder="请选择计划">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <el-form-item>
          <el-button @click="getData()">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div>
      <el-row type="flex" class="row-bg">
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">总任务</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light">{{ taskStatistics.taskTotal }}</div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">提前完成</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light">{{ taskStatistics.taskAheadOfTime }}</div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">滞后完成</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light">{{ taskStatistics.taskBehindSchedule }}</div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">按时完成</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light">{{ taskStatistics.taskOnTime }}</div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">按时完成率</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light">{{ taskStatistics.taskOnTimeRate }}%</div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <p class="table-text">提前完成率</p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple-light">{{ taskStatistics.taskAheadOfTimeRate }}%</div>
        </el-col>
      </el-row>
    </div>


    <!-- 给出一个20px的间隔 -->
    <div style="margin-top: 20px;"></div>

    <!-- 将一个div横向分为三个，比例为3:1:1 ，然后把三个div放在一起-->
    <div class="flex-container">

      <div class="flex-item" style="width: 60%; height: 100%;">
        <div class="picture" ref="chartContainer" style="width: 99%; height: 99%; top: 8px;"></div>
      </div>

      <div class="flex-item" style="width: 20%; height: 100%;">
        <!-- <div  style="width: 200px; height: 200px; background-color: #fff;"></div> -->
        <div class="picture" ref="onTimePieChart" style="width: 99%; height: 99%; top: 8px;"></div>
      </div>

      <div class="flex-item" style="width: 20%; height: 100%;">
        <!-- <div style="width: 200px; height: 200px; background-color: #ddd;"></div> -->
        <div class="picture" ref="earlyCompletionPieChart" style="width: 99%; height: 99%; top: 8px;"></div>
      </div>

    </div>


    <!-- <div ref="lineChart" style="width: 600px; height: 400px;"></div> -->

    <!-- <div ref="chartContainer" style="width: 100%; height: 400px;"></div> -->


    <!-- 给出一个20px的间隔 -->
    <div style="margin-top: 20px;"></div>

    <el-table :data="dataList" border v-loading="dataListLoading" style="width: 100%;">
      <el-table-column prop="taskId" header-align="center" align="center" label="任务编号">
      </el-table-column>
      <el-table-column prop="taskContent" header-align="center" align="center" label="任务内容">
      </el-table-column>
      <el-table-column prop="taskStartDate" header-align="center" align="center" label="开始日期">
      </el-table-column>
      <el-table-column prop="taskScheduleCompletionDate" header-align="center" align="center" label="计划完成日期">
      </el-table-column>
      <el-table-column prop="taskScheduleDays" header-align="center" align="center" label="计划天数">
      </el-table-column>
      <el-table-column prop="taskActualCompletionDate" header-align="center" align="center" label="实际完成日期">
      </el-table-column>
      <el-table-column prop="taskActualDays" header-align="center" align="center" label="实际天数">
      </el-table-column>
      <!--      <el-table-column prop="taskIsOnTime" header-align="center" align="center" label="是否按时完工">-->
      <!--      </el-table-column>-->
      <el-table-column prop="taskIsOnTime" label="是否按时完工" header-align="center" align="center" width="110">
        <template slot-scope="scope">
          <span v-if="scope.row.taskIsOnTime === 0" style="color: gray;">否</span>
          <span v-else-if="scope.row.taskIsOnTime === 1" style="color: gray;">是</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="taskEarlyCompletionDays" header-align="center" align="center" label="提前完工天数">
      </el-table-column>
      <el-table-column prop="taskLagDays" header-align="center" align="center" label="滞后天数">
      </el-table-column>
      <el-table-column prop="taskLagReasons" header-align="center" align="center" label="滞后原因">
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

      totalTasks: 100,
      taskStatistics: {},
      taskList: [],
      planId: '',
      taskLineData: {
        taskId: [],
        taskScheduleDays: [],
        taskActualDays: []
      },

      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
      value: ''
    }
  },

  components: {
    AddOrUpdate
  },
  // activated() {
  //   this.getDataList()
  // },
  async mounted() {
    await this.getPlanLabelList();
    await this.getTasksList();
    await this.getDataList();
    await this.getTaskLineDisplayData();
    this.initLineChart();
    this.initOnTimePieChart();
    this.initEarlyCompletionPieChart();
  },
  methods: {
    // 获取plan列表
    async getPlanLabelList() {
      this.dataListLoading = true
      await this.$http({
        url: this.$http.adornUrl('/taskmanagement/plan/getPlanLabel'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        console.log("plan列表" + data)
        this.options = data
      })
      this.dataListLoading = false
    },

    //获取数据，并渲染表格式
    async getData() {
      await this.getDataList();
      await this.getTasksList();
      await this.getTaskLineDisplayData();
      this.initLineChart();
      this.initOnTimePieChart();
      this.initEarlyCompletionPieChart();
    },


    // 获取数据列表
    async getDataList() {
      this.dataListLoading = true
      await this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/taskStatistics'),
        method: 'get',
        params: this.$http.adornParams({
          // 'planId': "2024-100"
          'planId': this.planId
        })
      }).then(({data}) => {
        console.log(data)
        this.taskStatistics = data
      })
    },
    async getTaskLineDisplayData() {
      this.dataListLoading = true
      await this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/taskLineDisplay'),
        method: 'get',
        params: this.$http.adornParams({
          // 'planId': "2024-100"
          'planId': this.planId
        })
      }).then(({data}) => {
        console.log("折线图数据" + data)
        //放入数据之前先初始化
        this.taskLineData.taskId = []
        this.taskLineData.taskScheduleDays = []
        this.taskLineData.taskActualDays = []

        for (let i = 0; i < data.length; i++) {
          this.taskLineData.taskId.push(data[i].taskId)
          this.taskLineData.taskScheduleDays.push(data[i].taskScheduleDays)
          this.taskLineData.taskActualDays.push(data[i].taskActualDays)
        }
        console.log("折线图数据" + this.taskLineData)
      })
    },
    async getTasksList() {
      this.dataListLoading = true
      await this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/getAllTasksByPlanId'),
        method: 'get',
        params: this.$http.adornParams({
          // 'planId': "2024-100",
          'planId': this.planId,
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        })
      }).then(({data}) => {
        console.log("列表数据" + data)
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
          left: 'center'
        },
        tooltip: {},
        xAxis: {
          // data: ["点1", "点2", "点3", "点4", "点5", "点6"]
          data: this.taskLineData.taskId
        },
        legend: {
          data: ['计划时间', '实际时间'],
          left: "76%"
        },
        yAxis: {},
        series: [{
          name: '计划时间',
          type: 'line',
          // data: [80, 50, 30, 70, 20, 60]
          data: this.taskLineData.taskScheduleDays,
          // label: {
          //   show: true,
          //   position: 'top'
          // },
          // itemStyle: {
          //   color: "rgb(69,255,0)",
          //   borderColor: "rgba(47, 144, 48, 1)",
          // },
          // lineStyle: {
          //   color: "rgba(22,185,245,0.52)"
          // }
        }, {
          name: '实际时间',
          type: 'line',
          // data: [40, 10, 50, 70, 40, 20]
          data: this.taskLineData.taskActualDays,
          // label: {
          //   show: true,
          //   position: 'top'
          // },
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
              {value: this.taskStatistics.taskOnTimeRate, name: '已完成'},
              {value: 100 - this.taskStatistics.taskOnTimeRate, name: '未完成'}
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
              {value: this.taskStatistics.taskAheadOfTimeRate, name: '已完成'},
              {value: 100 - this.taskStatistics.taskAheadOfTimeRate, name: '未完成'}
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
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  border-radius: 4px;
//min-height: 36px; min-height: 45px; font-size: 18px;
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
//height: 240px; height: 300px; width: 100%;
}


.flex-item {
  border: 1px solid #ccc;
  text-align: center;
  line-height: 100%;
  height: 100%;
}

.picture {
  display: flex;
}
</style>
