<template>
  <div id="index">
    <header>
      <h1>盘锦企管系统</h1>
      <div class="showTime">{{ currentTime }}</div>
    </header>
    <section class="mainbox">
      <div class="column">
<!--        <div class="panel indicator1">-->
<!--          <h2>部门指标统计</h2>-->
<!--          <div id="indicatorChart1" ref="indicatorChart1"></div>-->
<!--          <div class="panel-footer"></div>-->
<!--        </div>-->
<!--        <div class="panel indicator2">-->
<!--          <h2>分级指标统计</h2>-->
<!--          <div id="indicatorChart2" ref="indicatorChart2"></div>-->
<!--          <div class="panel-footer"></div>-->
<!--        </div>-->
        <div class="panel issue center-panel">
          <h2>问题统计 完成率：{{ completionRate }}</h2>
          <div id="issueChart" ref="issueChart"></div>
          <div class="panel-footer"></div>
        </div>

      </div>
<!--      <div class="column">-->
<!--        <div class="no">-->
<!--          <div class="no-bd">-->
<!--            <ul>-->
<!--              <li>指标总数</li>-->
<!--              <li>计划总数</li>-->
<!--              <li>问题总数</li>-->
<!--            </ul>-->
<!--          </div>-->
<!--          <div class="no-hd">-->
<!--            <ul>-->
<!--              <li>{{ indicatorCounts }}</li>-->
<!--              <li>{{ planCounts }}</li>-->
<!--              <li>{{ totalIssue }}</li>-->
<!--            </ul>-->
<!--          </div>-->
<!--        </div>-->
<!--        <div class="map">-->
<!--          <div class="map1"></div>-->
<!--          <div class="map2"></div>-->
<!--          <div class="map3"></div>-->
<!--          <div class="chart"></div>-->
<!--        </div>-->
<!--      </div>-->
<!--      <div class="column">-->
<!--        <div class="panel task">-->
<!--          <h2>计划完成情况</h2>-->
<!--          <div class="picture" ref="onTimePieChart" style="width: 98%; height: 90%;"></div>-->
<!--          <div class="panel-footer"></div>-->
<!--        </div>-->
<!--        <div class="panel QC">-->
<!--          <br>-->
<!--          <qc-chart></qc-chart>-->
<!--        </div>-->
<!--        <div class="panel QC">-->
<!--          <br>-->
<!--          <qc-pie-chart id="qc-pie-chart" ref="qcPieChart"></qc-pie-chart>-->
<!--        </div>-->
<!--      </div>-->
    </section>

  </div>
</template>

<script>
import VChart from 'vue-echarts';
import 'echarts';
import qcChart from "../QCmanagement/qcChart/qcChart.vue";
import qcPieChart from "../QCmanagement/qcChart/qcPieChart.vue";
import * as echarts from "echarts";

export default {
  components: {
    qcPieChart, qcChart,
    VChart
  },
  activated () {
    this.getIssueStats(); // 获取问题统计数据
    this.gettotalIssue();
    // this.fetchData()
  },
  data() {
    return {
//----------------问题模块-----------------
      issueStats: {}, // 存储当月问题统计数据
      issueCategories: ["暂缓", "持续", "结项"], // 问题分类
      totalIssue: '',
      completionRateData: { completed: 0, notCompleted: 0 },// 新增的完成率数据
      completionRate: 0, // 完成率
      // issueCategories: ["创建", "暂停", "未完成", "已完成", "结项"], // 问题分类
    };
  },
  async mounted() {
    // console.log('组件已经挂载');
    this.updateTime(); // 初始化时立即调用一次以显示当前时间
    this.t = setInterval(this.updateTime, 1000); // 每秒更新一次
    // this.getIndicatorCounts()
    // this.renderChart()
    this.getIssueStats(); // 获取问题统计数据
    this.gettotalIssue();
    this.getIndicatorCounts();

    this.getCompletionRate(); // 新增调用完成率数据的方法

    await this.getTaskCounts();
    await this.getPlanCounts();
    this.initOnTimePieChart();
    this.initEarlyCompletionPieChart();


  },
  methods: {
  // 查询问题数量
  getIssueStats() {
    this.$http({
      url: this.$http.adornUrl('/generator/issuetable/currentMonth'),
      method: 'get',
      params: this.$http.adornParams({})
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.issueStats = data.stats; // 假设返回的数据格式为 { 提出: 10, 暂停: 12, ... }
        // this.totalIssue = data.stats['暂停'] + data.stats['未完成'] + data.stats['已完成'] + data.stats['结项']
        console.log('数据转换中......', this.issueStats)
        this.renderIssueChart(); // 渲染图表
      } else {
        this.issueStats = { 暂缓: 0, 持续: 0, 结项: 0 }; // 默认值
        // this.issueStats = {创建: 0, 持续: 0, 未完成: 0, 已完成: 0, 结项: 0};
      }
    });
  },
  // 查询问题完成情况
  getCompletionRate() {
    this.$http({
      url: this.$http.adornUrl('/generator/issuetable/completionRate'),
      method: 'get',
      params: this.$http.adornParams({})
    }).then(({ data }) => {
      if (data) {
        console.log('返回的data', data);
        // 获取完成率
        const completed = data.completionRate.completed;
        // const notCompleted = data.completionRate.notCompleted;
        const total = data.completionRate.tolCompleted;
        const completionRate = total > 0 ? ((completed / total) * 100).toFixed(2) + '%' : '0%';

        // 更新显示的完成率
        this.completionRate = completionRate;  // 使用 Vue 响应式数据绑定
      } else {
        console.error('无效的数据格式');
      }
    });
  },
  gettotalIssue() {
    this.$http({
      url: this.$http.adornUrl('/generator/issuetable/totalIssue'),
      method: 'get',
      params: this.$http.adornParams({})
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.totalIssue = data.totalIssue; // 假设返回的数据格式为 { 提出: 10, 暂停: 12, ... }
        // console.log('数据转换中......', this.issueStats)
      } else {
        console.error('无效的数据格式');
      }
    });
  },

  // 渲染问题统计图表
  renderIssueChart() {
    // console.log('开始渲染.....')
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
          color: "rgba(255,255,255,.6)",
          fontSize: 12,
        },
        axisLine: {
          show: false,
          lineStyle: {
            color: "rgba(255,255,255,.1)",
            width: 2,
          },
        },
      },
      yAxis: {
        type: 'value',
        axisLabel: {
          color: "rgba(255,255,255,.6)",
          fontSize: 12,
        },
        splitLine: {
          show: false
        }
      },
      series: [
        {
          name: '数量',
          type: 'bar',
          data: [
            // this.issueStats['创建'] || 0,
            { value: this.issueStats['暂缓'] || 0, itemStyle: { normal: { color: '#2d84ff' } } },
            { value: this.issueStats['持续'] || 0, itemStyle: { normal: { color: '#ff8a22' } } },
            { value: this.issueStats['结项'] || 0, itemStyle: { normal: { color: '#f7ff10' } } }
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
  flex-wrap: wrap;
  /* 自动换行 */
  gap: 20px;
  /* 每个区域之间的间距 */
}


.box {
  font-size: 24px;
  /* 设置字体大小 */
  flex: 1 1 40%;
  /* 每个区域占 30% 宽度 */
  text-align: center;
  padding: 20px;
  border-radius: 4px;
  min-width: 200px;
  /* 设置最小宽度，防止过小 */
  height: 400px;
  /* 设置每个容器的高度为父容器的百分比 */
}

#panel QC {
  width: 98%;
  height: 90%;
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
  justify-content: space-between;
  /* 在主轴上均匀分布 */
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
  width: 98%;
  /* 使其宽度充满父容器 */
  height: 90%;
  /* 高度可以设置为100%，需要确保父容器有明确的高度 */
}
.center-panel {
  width: 60%; /* 调整宽度 */
  margin: 0 auto; /* 居中显示 */
  padding: 20px; /* 内边距，增加空间感 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 轻微阴影效果 */
  border-radius: 8px; /* 圆角 */
}

.center-panel h2 {
  text-align: center; /* 标题居中 */
}

#issueChart {
  width: 100%;
  height: 300px; /* 根据需求调整高度 */
}

</style>
