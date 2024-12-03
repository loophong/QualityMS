<template>
  <div class="mod">
    <div class="box1">
      控制指标数：{{ controlledDataCounts }}
    </div>
    <div class="box2">
      指标任务数：{{ indicatorRelatedTaskNum }}
    </div>
    <div id="indicatorCharts1" ref="indicatorCharts1" class="box">区域1</div>
    <div id="indicatorCharts2" ref="indicatorCharts2" class="box">区域2</div>
    <div id="indicatorCharts3" ref="indicatorCharts3" class="box">区域3</div>
    <div id="indicatorCharts4" ref="indicatorCharts4" class="box">区域4</div>
    <div id="indicatorCharts5" ref="indicatorCharts5" class="box">区域5</div>
    <div id="indicatorCharts6" ref="indicatorCharts6" class="box"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
export default {
  data() {
    return {
      dataList1: [], //指标图1数据
      dataList2: [],
      dataList3: [],
      dataList4: [],
      dataList5: [],
      controlledDataCounts: 0,  //图4中已管控指标数
      uncontrolledDataCounts: 0, //图4中未管控指标数
      finishedNum: 0, //图5中已完成指标数
      unfinishedNum: 0, //图5中未完成指标数
      indicatorRelatedTaskNum: 0,  //指标任务数
    };
  },
  mounted() {
    this.getDataList();
  },
  methods: {
    getDataList() {
      //图1、3
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/chart1'),
        method: 'get',
      }).then(({data}) => {
        this.dataList1 = data;
        this.dataList3 = data.at(-1);
        this.renderChart1();
        this.renderChart3();
      })

      //图2
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/chart2'),
        method: 'get',
      }).then(({data}) => {
        this.dataList2 = data;
        this.renderChart2();
      })

      //图4
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/list02'),
        method: 'get',
        'limit': 10000,
      }).then(({data}) => {
        const allDataList4 = data.page.totalCount;
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatorkeyindicators/list'),
          method: 'get',
          'limit': 10000,
        }).then(({data}) => {
          this.controlledDataCounts = data.page.totalCount;
          this.uncontrolledDataCounts = allDataList4-this.controlledDataCounts;
          this.renderChart4();
        })
      })

      //图5
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/statisticsOnTaskRelatedIndicators'),
        method: 'get',
      }).then(({data}) => {
        this.finishedNum = data.finishedNum;
        this.unfinishedNum = data.unfinishedNum;
        this.renderChart5();
      })

      //指标任务数
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/countTaskRelatedIndicatorsNum'),
        method: 'get',
      }).then(({data}) => {
        this.indicatorRelatedTaskNum = data.indicatorRelatedTaskNum;
      })
    },
    renderChart1() {
      const chart = echarts.init(this.$refs.indicatorCharts1);
      const option = {
        title: {
          text: "月度指标完成情况",
          left: 'center',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['达标', '未达标'],
          top: '10%',
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.dataList1.map(item => item.yearMonth),
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '达标',
            type: 'bar',
            data: this.dataList1.map(item => item.finishedCounts),
          },
          {
            name: '未达标',
            type: 'bar',
            data: this.dataList1.map(item => item.unfinishedCounts),
          }
        ],
      };

      chart.setOption(option);

    },

    renderChart2() {
      const chart = echarts.init(this.$refs.indicatorCharts2);
      const option = {
        title: {
          text: "当月A类指标完成情况",
          left: 'center',
        },
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            type: 'pie',
            avoidLabelOverlap: false,
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
              { value: this.dataList2[0].finishedCounts, name: '达成' },
              { value: this.dataList2[0].unfinishedCounts, name: '未达成' }
            ]
          }
        ]
      };
      chart.setOption(option);
    },

    renderChart3() {
      const chart = echarts.init(this.$refs.indicatorCharts3);
      const option = {
        title: {
          text: "当月指标完成情况",
          left: 'center',
        },
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            type: 'pie',
            avoidLabelOverlap: false,
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
              { value: this.dataList3.finishedCounts, name: '达成' },
              { value: this.dataList3.unfinishedCounts, name: '未达成' }
            ]
          }
        ]
      };
      chart.setOption(option);
    },

    renderChart4() {
      const chart = echarts.init(this.$refs.indicatorCharts4);
      const option = {
        title: {
          text: "重点管控情况",
          left: 'center',
        },
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            type: 'pie',
            avoidLabelOverlap: false,
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
              { value: this.controlledDataCounts, name: '已管控指标数' },
              { value: this.uncontrolledDataCounts, name: '未管控指标数' }
            ]
          }
        ]
      };
      chart.setOption(option);
    },

    renderChart5() {
      const chart = echarts.init(this.$refs.indicatorCharts5);
      const option = {
        title: {
          text: "任务进度",
          left: 'center',
        },
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            type: 'pie',
            avoidLabelOverlap: false,
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
              { value: this.finishedNum, name: '已完成指标数' },
              { value: this.unfinishedNum, name: '未完成指标数' }
            ]
          }
        ]
      };
      chart.setOption(option);
    },

  }
}
</script>

<style>
.mod {
  display: flex;
  flex-wrap: wrap; /* 自动换行 */
  gap: 20px; /* 每个区域之间的间距 */
}
.box1 {
  font-size: 24px; /* 设置字体大小 */
  flex: 1 1 40%; /* 每个区域占 30% 宽度 */
  //background-color: #f2f2f2;
  text-align: center;
  padding: 20px;
  border-radius: 4px;
  min-width: 200px; /* 设置最小宽度，防止过小 */
  height: 100px; /* 设置每个容器的高度为父容器的百分比 */
}
.box2 {
  font-size: 24px; /* 设置字体大小 */
  flex: 1 1 40%; /* 每个区域占 30% 宽度 */
  //background-color: #f2f2f2;
  text-align: center;
  padding: 20px;
  border-radius: 4px;
  min-width: 200px; /* 设置最小宽度，防止过小 */
  height: 100px; /* 设置每个容器的高度为父容器的百分比 */
}
.box {
  flex: 1 1 40%; /* 每个区域占 30% 宽度 */
  //background-color: #f2f2f2;
  text-align: center;
  padding: 20px;
  border-radius: 4px;
  min-width: 200px; /* 设置最小宽度，防止过小 */
  height: 400px; /* 设置每个容器的高度为父容器的百分比 */
}
</style>
