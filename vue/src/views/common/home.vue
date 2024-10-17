
<!--    <div class="header-box">-->
<!--      <h1 class="header-title">盘锦企管系统</h1>-->
<!--    </div>-->
<!--<template>-->
<!--        <div class="mod-home">-->
<!--          <div id="indicator" class="box">-->
<!--            <div style="height: 10%">指标总数：{{indicatorCounts}}</div>-->
<!--            <div id="indicatorChart1" ref="indicatorChart1"></div>-->
<!--            <div id="indicatorChart2" ref="indicatorChart2"></div>-->
<!--          </div>-->


<!--          <div id="issue" class="box">-->
<!--            <div style="height: 10%"></div>-->
<!--            <div id="issueChart"></div>-->
<!--          </div>-->

<!--          <div id="task" class="box">-->
<!--            <div id="task" class="box">-->
<!--              <div class="flex-container">-->
<!--                <div class="flex-item" style="width: 50%; height: 96%;">-->
<!--                  <div class="picture" ref="onTimePieChart" style="width: 96%; height: 96%;"></div>-->
<!--                </div>-->
<!--                <div class="flex-item" style="width: 50%; height: 96%;">-->
<!--                  <div class="picture" ref="earlyCompletionPieChart" style="width: 96%; height: 96%;"></div>-->
<!--                </div>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->

<!--          <div id="QC" class="box">-->
<!--            &lt;!&ndash; QC图表 &ndash;&gt;-->
<!--            <div>-->
<!--              <qc-chart ref="qcChart"></qc-chart>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--</template>-->
<template>
  <div id="index">
    <header>
      <h1>盘锦企管系统</h1>
      <div class="showTime">{{ currentTime }}</div>
    </header>
    <section class="mainbox">
      <div class="column">
        <div class="panel indicator1">
          <h2>部门指标统计</h2>
          <div id="indicatorChart1" ref="indicatorChart1"></div>
          <div class="panel-footer"></div>
        </div>
        <div class="panel indicator2">
          <h2>分级指标统计</h2>
          <div id="indicatorChart2" ref="indicatorChart2"></div>
          <div class="panel-footer"></div>
        </div>
        <div class="panel issue">
          <h2>当月问题统计</h2>
          <div id="issueChart" ref="issueChart"></div>
          <div class="panel-footer"></div>
        </div>
      </div>
      <div class="column">
        <div class="no">
          <div class="no-hd">
            <ul>
              <li>{{ indicatorCounts }}</li>
              <li>{{ completionRate }}</li>
          </ul>
          </div>
          <div class="no-bd">
            <ul>
              <li>指标总数</li>
              <li>当月问题完成率</li>
            </ul>
          </div>
        </div>
        <div class="map">
          <div class="map1"></div>
          <div class="map2"></div>
          <div class="map3"></div>
          <div class="chart"></div>
        </div>
      </div>
      <div class="column">
        <div class="panel task">
          <h2>计划完成情况</h2>
          <div class="picture" ref="onTimePieChart" style="width: 98%; height: 90%;"></div>
          <div class="panel-footer"></div>
        </div>
        <div class="panel QC">
          <h2>点检统计</h2>
          <div id="qc-chart" ref="qcChart"></div>
          <div class="panel-footer"></div>
        </div>
        <div class="panel QC">
          <h2>QC</h2>
          <div id="qc-chart" ref="qcChart"></div>
          <div class="panel-footer"></div>
        </div>
      </div>
    </section>

  </div>
</template>

<script>
import * as echarts from "echarts";
import qcChart from "../modules/QCmanagement/qcChart/qcChart.vue";
import { color } from "d3";
import china from "./china.js";  // 导入 .js 文件


export default {
  name: 'home',
  data() {
    return {
      //----------------指标模块-----------------
      indicatorCounts: 0,
      departmentCountsList: [], //按部门查询返回list
      classificationCountList: [],  //按指标分级查询返回list

      //----------------任务模块-----------------
      taskData: {},

      //----------------问题模块-----------------
      issueStats: {}, // 存储当月问题统计数据
      issueCategories: ["暂停", "未完成", "已完成", "结项"], // 问题分类
      completionRateData: { completed: 0, notCompleted: 0 },// 新增的完成率数据
      completionRate: 0, // 完成率
      // issueCategories: ["创建", "暂停", "未完成", "已完成", "结项"], // 问题分类
      currentTime: '', // 存储当前时间
      t: null, // 定时器
    }
  },
  components: {
    qcChart
  },

  async mounted() {
    console.log('组件已经挂载');
    this.updateTime(); // 初始化时立即调用一次以显示当前时间
    this.t = setInterval(this.updateTime, 1000); // 每秒更新一次
    // this.getIndicatorCounts()
    // this.renderChart()
    this.getIssueStats(); // 获取问题统计数据
    this.getIndicatorCounts();

    this.getCompletionRate(); // 新增调用完成率数据的方法

    await this.getTaskCounts();
    this.initOnTimePieChart();
    this.initEarlyCompletionPieChart();


  },
  methods: {
    updateTime() {
      const dt = new Date();
      const y = dt.getFullYear();
      const m = ('0' + (dt.getMonth() + 1)).slice(-2); // 补零
      const d = ('0' + dt.getDate()).slice(-2); // 补零
      const h = ('0' + dt.getHours()).slice(-2); // 补零
      const mi = ('0' + dt.getMinutes()).slice(-2); // 补零
      const s = ('0' + dt.getSeconds()).slice(-2); // 补零
      this.currentTime = `${y}-${m}-${d} ${h}:${mi}:${s}`; // 格式化时间
      // console.log('当前时间1：' ,this.currentTime);
    },
    beforeDestroy() {
      if (this.t) {
        clearInterval(this.t); // 清除定时器
      }
      console.log('组件即将销毁');
    },
    //----------------指标模块-----------------
    getIndicatorCounts() {
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/countsByDepartmant'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        console.log('data111:', data);
        this.departmentCountsList = data;
        console.log('departmentCountsList:', this.departmentCountsList);
        this.indicatorCounts = data.reduce((total, item) => total + item.counts, 0);
        console.log('indicatorCounts:', this.indicatorCounts);
        this.renderChart1();
      });

      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/countsByClassification'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        console.log('data222:', data);
        this.classificationCountList = data;
        console.log('classificationCountList:', this.classificationCountList);
        this.renderChart2();
      });
    },
    renderChart1() {
        // 销毁旧的实例，防止缓存导致问题
  if (this.chartInstance1) {
    this.chartInstance1.dispose();
  }
      const chart = echarts.init(this.$refs.indicatorChart1);

      const option = {
        title: {
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '0%',
          top: '10px',
          right: '0%',
          bottom: '20px',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.departmentCountsList.map(item => item.managementDepartment),
          axisLabel: {
            color:"rgba(255,255,255,.6)",
            fontSize:12,
          },
          axisLine: {
            show:false,
            lineStyle: {
              color: "rgba(255,255,255,.1)",
              width: 2,
            },
          },
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color:"rgba(255,255,255,.6)",
            fontSize:12,
          },
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
    renderChart2() {
      const chart = echarts.init(this.$refs.indicatorChart2);

      const option = {
        title: {
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '0%',
          top: '10px',
          right: '0%',
          bottom: '20px',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.classificationCountList.map(item => item.indicatorClassification),
          axisLabel: {
            color:"rgba(255,255,255,.6)",
            fontSize:12,
          },
          axisLine: {
            show:false,
            lineStyle: {
              color: "rgba(255,255,255,.1)",
              width: 2,
            },
          },
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color:"rgba(255,255,255,.6)",
            fontSize:12,
          },
        },
        series: [
          {
            name: '指标总数',
            type: 'bar',
            data: this.classificationCountList.map(item => item.counts),
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

    //----------------问题模块-----------------
    // 查询当月问题数量
    getIssueStats() {
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/currentMonth'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.issueStats = data.stats; // 假设返回的数据格式为 { 提出: 10, 暂停: 12, ... }
          console.log('数据转换中......', this.issueStats)
          this.renderIssueChart(); // 渲染图表
        } else {
          this.issueStats = { 暂停: 0, 未完成: 0, 已完成: 0, 结项: 0}; // 默认值
          // this.issueStats = {创建: 0, 持续: 0, 未完成: 0, 已完成: 0, 结项: 0};
        }
      });
    },
    // 查询当月问题完成情况
    getCompletionRate() {
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/completionRate'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({data}) => {
        if (data) {
          console.log('返回的data', data);
          // 获取完成率
          const completed = data.completionRate.completed;
          const notCompleted = data.completionRate.notCompleted;
          const total = completed + notCompleted;
          const completionRate = total > 0 ? ((completed / total) * 100).toFixed(2) + '%' : '0%';

          // 更新显示的完成率
          this.completionRate = completionRate;  // 使用 Vue 响应式数据绑定
        } else {
          console.error('无效的数据格式');
        }
      });
    },


    // 渲染问题统计图表
    renderIssueChart() {
      console.log('开始渲染.....')
      const chart = echarts.init(document.getElementById("issueChart"));

      const option = {
        title: {
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '0%',
          top: '10px',
          right: '0%',
          bottom: '26px',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.issueCategories, // 问题分类作为X轴
          axisLabel: {
            color:"rgba(255,255,255,.6)",
            fontSize:12,
          },
          axisLine: {
            show:false,
            lineStyle: {
              color: "rgba(255,255,255,.1)",
              width: 2,
            },
          },
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color:"rgba(255,255,255,.6)",
            fontSize:12,
          },
        },
        series: [
          {
            name: '数量',
            type: 'bar',
            data: [
              // this.issueStats['创建'] || 0,
              this.issueStats['暂停'] || 0,
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
<style lang="scss">
@import "../../assets/echart/index.scss";
/* 保持原有样式 */
.box {
  background-color: transparent; /* 设置为透明背景 */
}
.el-card__body {
  background-color: transparent; /* 设置为透明背景 */
}
</style>

<style>
</style>
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

#indicatorChart1 {
  width: 98%;
  height: 90%;
}
#indicatorChart2 {
  width: 98%;
  height: 90%;
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
  width: 98%; /* 使其宽度充满父容器 */
  height: 90%; /* 高度可以设置为100%，需要确保父容器有明确的高度 */
}
</style>

