<template>
    <el-container>
      <el-container>
        <el-aside width="15%" style="font-size: 20px; height: 60vh; display: flex; flex-direction: column;">
          <el-card class="box-card" style="margin-top: 1px; flex-grow: 1;">
            <div slot="header" class="clearfix">
              <span>当月指标总数</span>
            </div>
            <div class="text item">{{ controlledDataCounts }}</div>
          </el-card>
          <el-card class="box-card" style="flex-grow: 1;">
            <div slot="header" class="clearfix">
              <span>未达成数</span>
            </div>
            <div class="text item">{{ dataList3.unfinishedCounts }}</div>
          </el-card>
          <el-card class="box-card" style="flex-grow: 1;">
            <div slot="header" class="clearfix">
              <span>已发任务指标数</span>
            </div>
            <div class="text item">{{ indicatorRelatedTaskNum }}</div>
          </el-card>
          <el-card class="box-card" style="flex-grow: 1;">
            <div slot="header" class="clearfix">
              <span>月度任务完成率</span>
            </div>
            <div class="text item">{{ (finishedNum / indicatorRelatedTaskNum * 100).toFixed(2) }}%</div>
          </el-card>
        </el-aside>

        <el-main>
          <div class="chart-column left-column">
            <div id="indicatorCharts1" ref="indicatorCharts1" class="box1"></div>
          </div>
          <div class="chart-column right-column">
            <div id="indicatorCharts2" ref="indicatorCharts2" class="box"></div>
            <div id="indicatorCharts4" ref="indicatorCharts4" class="box"></div>
          </div>
          <div class="foot">
          </div>
        </el-main>

      </el-container>
      <el-footer style="display: flex; flex-direction: row; font-size: 20px;  height: 15vh; padding: 0;">
        <el-card class="box-card" style="flex: 1;">
          <img :src="require('@/assets//indicator.png')" style="width: 100px; height: 100px;">
        </el-card>
        <el-card class="box-card" style="flex: 1;">
          <div slot="header" class="clearfix">
            <span>总管控指标数</span>
          </div>
          <div class="text item">
            <span :class="{ 'red-bold': unfinishedKeyIndicatorsCounts !== 0 }">{{ unfinishedKeyIndicatorsCounts }}</span>/<span>{{ controlledDataCounts }}</span>
          </div>
        </el-card>
        <el-card class="box-card" style="flex: 1;">
          <div slot="header" class="clearfix">
            <span>A类指标数</span>
          </div>
          <div class="text item">
            <span :class="{ 'red-bold': unfinishedIndicatorANum !== 0 }">{{ unfinishedIndicatorANum }}</span>/<span>{{ indicatorANum }}</span>
          </div>
        </el-card>
        <el-card class="box-card" style="flex: 1;">
          <div slot="header" class="clearfix">
            <span>末端指标数</span>
          </div>
          <div class="text item">
            <span :class="{ 'red-bold': recentMonthUnfinishedIndicatorNum !== 0 }">{{ recentMonthUnfinishedIndicatorNum }}</span>/<span>{{ recentMonthIndicatorNum }}</span>
          </div>
        </el-card>
      </el-footer>
    </el-container>

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
      keyIndicatorList: [], //重点管控指标列表
      indicatorSummaryList: [], //指标数据列表
      indicatorDictionaryList: [], //指标字典列表
      unfinishedKeyIndicatorsCounts: 0, //管控指标未达成数
      indicatorANum: 0, //A类指标数
      unfinishedIndicatorANum: 0, //A类未达成数
      unfinishedIndicatorAList: [], //A类未达成指标列表
      finishedIndicatorAList: [], //A类已完成指标列表
      recentMonthIndicatorNum: 0, //最近一个月指标数
      recentMonthUnfinishedIndicatorNum: 0, //最近一个月未达成指标数
    };
  },
  mounted() {
    this.getDataList();
  },
  methods: {
    async getDataList() {
      //图1、3
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/chart1'),
        method: 'get',
      }).then(({data}) => {
        this.dataList1 = data;
        console.log('dataList1:', this.dataList1);
        this.dataList3 = data.at(-1);
        console.log('dataList3:', this.dataList3)
        this.renderChart1();
        // this.renderChart3();
      })

      //图2
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/chart2'),
        method: 'get',
      }).then(({data}) => {
        this.dataList2 = data;
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

      this.getUnfinishedKeyIndicators();

    },
    // 获取未达成的重点管控指标数
    getUnfinishedKeyIndicators() {
      //获取重点管控指标列表（不分页）
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatorkeyindicators/list'),
        method: 'get',
        params: this.$http.adornParams({
          'limit': 10000,
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log("indicatorkeyindicators====>",data)
          this.keyIndicatorList = data.page.list
        } else {
          this.keyIndicatorList = []
        }
      }).then(() => {
        //获取指标数据列表（不分页）
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/list'),
          method: 'get',
          params: this.$http.adornParams({
            'limit': 10000,
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            console.log("indicatorSummaryList====>",data)
            this.indicatorSummaryList = data.page.list;
            // 提取 keyIndicatorList 中的指标名称列表
            const keyIndicatorNames = this.keyIndicatorList.map(item => item.indicatorName);
            console.log("keyIndicatorNames====>",keyIndicatorNames);
            // 筛选出 indicatorSummaryList 中存在于 keyIndicatorList 中的记录
            const filteredList = this.indicatorSummaryList.filter(item => keyIndicatorNames.includes(item.indicatorName));
            console.log("filteredList====>",filteredList);
            const recentYearMonth  = filteredList[0].yearMonth;
            // 筛选出 recentMonthFilteredList 中最新一个月未达成的指标数据
            const recentMonthFilteredList = filteredList.filter(item => item.yearMonth === recentYearMonth && item.finishedFlag === 0);
            console.log("recentMonthFilteredList====>",recentMonthFilteredList);
            this.unfinishedKeyIndicatorsCounts = recentMonthFilteredList.length;
            console.log("unfinishedKeyIndicatorsCounts====>",this.unfinishedKeyIndicatorsCounts);
            this.getIndicatorA();
            this.getRecentMonthIndicator();

          } else {
            console.log("indicatorSummaryList====>",data);
          }
        })
      })
    },
    // 获取A类指标数/未达成数
    getIndicatorA() {
      //A类指标数
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
        method: 'get',
        params: this.$http.adornParams({
          'limit': 10000,
        })
      }).then(({data}) => {
        this.indicatorDictionaryList = data.page.list;
        const indicatorAList = this.indicatorDictionaryList.filter(item => item.indicatorClassification === 'A');
        console.log("indicatorAList====>",indicatorAList);
        this.indicatorANum = indicatorAList.length;
        console.log("this.indicatorANum====>",this.indicatorANum);
        console.log("indicatorSummaryList====>",this.indicatorSummaryList);
        const recentYearMonth  = this.indicatorSummaryList[0].yearMonth;
        // 填充 finishedIndicatorAList 和 unfinishedIndicatorAList
        this.finishedIndicatorAList = this.indicatorSummaryList
          .filter(item => item.indicatorClassification === 'A' && item.finishedFlag === 1 && item.yearMonth === recentYearMonth)
          .map(item => item.indicatorName);
        this.unfinishedIndicatorAList = this.indicatorSummaryList
          .filter(item => item.indicatorClassification === 'A' && item.finishedFlag === 0 && item.yearMonth === recentYearMonth)
          .map(item => item.indicatorName);
        this.unfinishedIndicatorANum = this.unfinishedIndicatorAList.length;
        console.log("this.unfinishedIndicatorAList====>",this.unfinishedIndicatorAList);
        console.log("this.unfinishedIndicatorANum====>",this.unfinishedIndicatorANum);
      }).then(() => {
        this.renderChart2();
      })
    },
    // 最近一个月指标数/未达成指标数
    getRecentMonthIndicator() {
      const recentYearMonth  = this.indicatorSummaryList[0].yearMonth;
      this.recentMonthIndicatorNum = this.indicatorSummaryList.filter(item => item.yearMonth === recentYearMonth).length;
      this.recentMonthUnfinishedIndicatorNum = this.indicatorSummaryList.filter(item => item.finishedFlag === 0 && item.yearMonth === recentYearMonth).length;
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
          // data: [this.dataList3.yearMonth],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '达标',
            type: 'bar',
            data: this.dataList1.map(item => item.finishedCounts),
            // data: [this.dataList3.finishedCounts],
          },
          {
            name: '未达标',
            type: 'bar',
            data: this.dataList1.map(item => item.unfinishedCounts),
            // data: [this.dataList3.unfinishedCounts],
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
          trigger: 'item',
          formatter: (params) => {
            let tooltipContent = `总数: ${params.value}<br>`;
            // tooltipContent += `<strong>${params.seriesName}</strong><br>`;

            if (params.name === '达成') {
              this.finishedIndicatorAList.forEach((name, index) => {
                tooltipContent += `${index + 1}. ${name}<br>`;
              });
            } else if (params.name === '未达成') {
              this.unfinishedIndicatorAList.forEach((name, index) => {
                tooltipContent += `${index + 1}. ${name}<br>`;
              });
            }

            return tooltipContent;
          }
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
  gap: 10px; /* 每个区域之间的间距 */
}
.el-main {
  display: flex;
  justify-content: space-between; /* 使列之间有间距 */
  height: 70vh; /* 设置 el-main 的高度为视口高度的 70% */
}
.chart-column {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  width: 50%; /* 每列占据大约一半宽度 */
}
.left-column {
  justify-content: center; /* 垂直居中 */
}

.right-column {
  justify-content: flex-start; /* 上对齐 */
}
.box {
  flex: 0 1 50%; /* 每个区域占 30% 宽度 */
  //background-color: #f2f2f2;
  text-align: center;
  padding: 20px;
  border-radius: 4px;
  min-width: 200px; /* 设置最小宽度，防止过小 */
  height: 300px; /* 设置每个容器的高度为父容器的百分比 */
}
.box1 {
  flex: 1 1 50%; /* 每个区域占 50% 宽度，第一个0表示 .box1 不会增长以填充剩余空间，第二个1表示 .box1 可以收缩以适应较小的空间 */
  //background-color: #f2f2f2;
  text-align: center;
  padding: 20px; /* 调整内边距 */
  border-radius: 4px;
  min-width: 200px; /* 设置最小宽度，防止过小 */
  height: 45%; /* 设置高度占父容器的一半 */
}
.box:last-child {
  margin-bottom: 0; /* 最后一个图表不需要底部间距 */
}

.text.item span {
  font-size: 1.5em; /* 使数字比文字大 */
}

.red-bold {
  color: red; /* 变红 */
  font-weight: bold; /* 加粗 */
}
.box-card {
  display: flex;
  flex-direction: column;
  align-items: center; /* 水平居中 */
  justify-content: center; /* 垂直居中 */
  text-align: center; /* 文本居中 */
}
</style>
