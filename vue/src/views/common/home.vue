<template>
  <div class="mod-home">
    <div id="indicator" class="box">
      <div>
        指标总数：{{ indicatorCounts }}
      </div>
      <div id="indicatorChart" ref="indicatorChart"></div>
    </div>

    <div id="task" class="box">
      <div id="task" class="box">
        <div class="flex-container">
          <div class="flex-item" style="width: 48%; height: 96%;">
            <div class="picture" ref="onTimePieChart" style="width: 96%; height: 96%;"></div>
          </div>
          <div class="flex-item" style="width: 48%; height: 96%;">
            <div class="picture" ref="earlyCompletionPieChart" style="width: 96%; height: 96%;"></div>
          </div>
        </div>
      </div>
    </div>

    <div id="issue" class="box">
      <div>当月问题数量统计</div>
      <div id="issueChart"></div>
    </div>

    <div id="QC" class="box">
      区域4
      <!-- QC图表 -->
      <div>
        <qc-chart ref="qcChart"></qc-chart>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import qcChart from "../modules/QCmanagement/qcChart/qcChart.vue";

export default {
  name: 'home',
  data() {
    return {
      //----------------指标模块-----------------
      indicatorCounts: 0,
      myIndicatorChart1: {}, //指标柱状图
      departmentCountsList: [], //查询返回list
      departmentList: ["质量科", "市场科", "企业管理科"], //部门列表

      //----------------任务模块-----------------
      taskData: {},
      issueStats: {}, // 存储当月问题统计数据
      issueCategories: ["持续", "未完成", "已完成", "结项"], // 问题分类
      // issueCategories: ["创建", "持续", "未完成", "已完成", "结项"], // 问题分类
    }
  },
  components: {
    qcChart
  },

  async mounted() {
    console.log('组件已经挂载');
    // this.getIndicatorCounts()
    // this.renderChart()
    this.getIssueStats(); // 获取问题统计数据
    this.getIndicatorCounts();


    await this.getTaskCounts();
    this.initOnTimePieChart();
    this.initEarlyCompletionPieChart();

  },
  methods: {
    //----------------指标模块-----------------
    getIndicatorCounts() {
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/countsByDepartmant'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        console.log('data123:', data);
        this.departmentCountsList = data;
        console.log('departmentCountsList:', this.departmentCountsList);
        this.indicatorCounts = data.reduce((total, item) => total + item.counts, 0);
        console.log('indicatorCounts:', this.indicatorCounts);
        this.renderChart();
      });
    },

    renderChart() {
      const chart = echarts.init(this.$refs.indicatorChart);

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.departmentCountsList.map(item => item.managementDepartment),
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '指标总数',
            type: 'bar',
            data: this.departmentCountsList.map(item => item.counts),
            itemStyle: {
              color: '#409EFF',
            },
          },
        ],
      };

      chart.setOption(option);
    },

    //----------------任务模块-----------------
    async getTaskCounts() {
      await this.$http({
        url: this.$http.adornUrl('/taskmanagement/plan/home'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        // console.log('taskData:', data);
        // console.log('data.taskNum:', data[0].taskNum);
        this.taskData.taskNum = data[0].taskNum
        this.taskData.completedTaskNum = data[1].completedTaskNum
        this.taskData.planNum = data[2].planNum
        this.taskData.completedPlanNum = data[3].completedPlanNum
        console.log('taskData:', this.taskData);
        // this.renderChart();
      });
    },

    initOnTimePieChart() {
      this.chartInstance = echarts.init(this.$refs.onTimePieChart);
      const option = {
        title: {
          text: '计划完成情况',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            // name: 'Access From',
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
              {value: this.taskData.completedPlanNum, name: '已完成'},
              {value: this.taskData.planNum - this.taskData.completedPlanNum, name: '未完成'}
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
          text: '任务完成情况',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            // name: 'Access From',
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
              {value: this.taskData.completedTaskNum, name: '已完成'},
              {value: this.taskData.taskNum - this.taskData.completedTaskNum, name: '未完成'}
            ]
          }
        ]
      };
      this.chartInstance.setOption(option);
    },

    //----------------任务模块-----------------
    // 查询当月问题数量
    getIssueStats() {
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/currentMonth'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.issueStats = data.stats; // 假设返回的数据格式为 { 提出: 10, 持续: 12, ... }
          console.log('数据转换中......', this.issueStats)
          this.renderIssueChart(); // 渲染图表
        } else {
          this.issueStats = { 持续: 0, 未完成: 0, 已完成: 0, 结项: 0}; // 默认值
          // this.issueStats = {创建: 0, 持续: 0, 未完成: 0, 已完成: 0, 结项: 0};
        }
      });
    },

    // 渲染问题统计图表
    renderIssueChart() {
      console.log('开始渲染.....')
      const chart = echarts.init(document.getElementById("issueChart"));

      const option = {
        title: {
          text: "当月问题数量统计",
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.issueCategories, // 问题分类作为X轴
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '数量',
            type: 'bar',
            data: [
              // this.issueStats['创建'] || 0,
              this.issueStats['持续'] || 0,
              this.issueStats['未完成'] || 0,
              this.issueStats['已完成'] || 0,
              this.issueStats['结项'] || 0
            ], // 根据获取的数据填充
            itemStyle: {
              color: '#409EFF',
            },
          },
        ],
      };

      chart.setOption(option);
    },


  }
};
</script>

<style>
.mod-home {
  display: flex;
  flex-wrap: wrap; /* 自动换行 */
  gap: 20px; /* 每个区域之间的间距 */
}

.box {
  font-size: 24px; /* 设置字体大小 */
  flex: 1 1 40%; /* 每个区域占 30% 宽度 */
  text-align: center;
  padding: 20px;
  border-radius: 4px;
  min-width: 200px; /* 设置最小宽度，防止过小 */
  height: 400px; /* 设置每个容器的高度为父容器的百分比 */
}

#indicatorChart {
  width: 100%;
  height: 100%;
  /* 确保图表容器高度为 100% */
}

#QC {
  width: 100%;
  height: 100%;
  /* 确保图表容器高度为 100% */
}

.flex-container {
  display: flex;
  justify-content: space-between; /* 在主轴上均匀分布 */
  width: 100%;
  height: 100%;
}

.flex-item {
  border: 0px solid #ccc;
  text-align: center;
  line-height: 96%;
  height: 96%;
}

.picture {
  display: flex;
}

#issueChart {
  width: 80%; /* 使其宽度充满父容器 */
  height: 100%; /* 高度可以设置为100%，需要确保父容器有明确的高度 */
}
</style>
