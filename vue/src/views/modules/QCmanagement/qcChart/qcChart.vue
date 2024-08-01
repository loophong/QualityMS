<template>
  <div>
    <!-- <div class="block">
      <span class="DataSelect" style="margin-right:10px">日期选择</span>
      <el-date-picker v-model="selectedDate" type="monthrange" unlink-panels range-separator="至"
        start-placeholder="开始月份" end-placeholder="结束月份" :picker-options="pickerOptions" @change="handleDateChange">
      </el-date-picker>
    </div> -->
    <div>
      <div id="today" ref="today"></div>
    </div>
    <div id="main" ref="main"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import moment from 'moment';
// import { getOutputPercapitavalueData } from '@/api/safety/chartAPI'

export default {
  data() {
    return {
      loading: false,
      data: [],
      timeData: {
        startTime: new Date(),
        endTime: new Date(),
      },
      selectedDate: [],
      pickerOptions: [],
      option: {},
      myChart: {},
      myChart2: {},
      chartData: [], // 存放格式化后的数据
      parsedData: {}
    }
  },
  computed: {},
  mounted() {
    // this.defaultMonth()
    this.myChart = echarts.init(document.getElementById('main'))
    // this.initData()
    this.todayChart()
    this.updateChart()

  },
  methods: {
    // async initData() {
    //   this.timeData.startTime = this.selectedDate[0],
    //     this.timeData.endTime = this.selectedDate[1]
    //   try {
    //     this.loading = true
    //     const res = await getOutputPercapitavalueData(this.timeData);
    //     this.data = res
    //     this.loading = false
    //     this.formatData()
    //     this.updateChart()
    //   } catch (error) {
    //     this.loading = false
    //   }
    // },
    // handleDateChange(value) {
    //   if (value && value[1]) {
    //     let endDate = new Date(value[1]);
    //     endDate.setMonth(endDate.getMonth() + 1);
    //     endDate.setDate(0);
    //     this.selectedDate[1] = endDate;
    //   }
    //   this.initData()
    // },
    todayChart() {
      var chartDom = document.getElementById('today');
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        title: {
          text: '今日点检统计',
          // subtext: 'Fake Data',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '点检统计表',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 72, name: '活跃度', itemStyle: { color: '#f56c6c' } },
              { value: 75, name: '参与度', itemStyle: { color: '#4bae4f' } },
              { value: 89, name: '完成度', itemStyle: { color: '#5db2f1' } },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      option && myChart.setOption(option);
    },

    updateChart() {
      var app = {};

      var chartDom = document.getElementById('main');
      var myChart = echarts.init(chartDom);
      var option;

      const posList = [
        'left',
        'right',
        'top',
        'bottom',
        'inside',
        'insideTop',
        'insideLeft',
        'insideRight',
        'insideBottom',
        'insideTopLeft',
        'insideTopRight',
        'insideBottomLeft',
        'insideBottomRight'
      ];
      app.configParameters = {
        rotate: {
          min: -90,
          max: 90
        },
        align: {
          options: {
            left: 'left',
            center: 'center',
            right: 'right'
          }
        },
        verticalAlign: {
          options: {
            top: 'top',
            middle: 'middle',
            bottom: 'bottom'
          }
        },
        position: {
          options: posList.reduce(function (map, pos) {
            map[pos] = pos;
            return map;
          }, {})
        },
        distance: {
          min: 0,
          max: 100
        }
      };
      app.config = {
        rotate: 90,
        align: 'left',
        verticalAlign: 'middle',
        position: 'top',
        distance: 15,
        onChange: function () {
          const labelOption = {
            rotate: app.config.rotate,
            align: app.config.align,
            verticalAlign: app.config.verticalAlign,
            position: app.config.position,
            distance: app.config.distance
          };
          myChart.setOption({
            series: [
              {
                label: labelOption
              },
              {
                label: labelOption
              },
              {
                label: labelOption
              },
              {
                label: labelOption
              }
            ]
          });
        }
      };
      const labelOption = {
        show: true,
        position: app.config.position,
        distance: app.config.distance,
        align: app.config.align,
        verticalAlign: app.config.verticalAlign,
        rotate: app.config.rotate,
        formatter: '{c} %',
        fontSize: 16,
        rich: {
          name: {}
        }
      };
      option = {
        title: {
          text: '点检统计'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['参与度', '活跃度', '完成度']
        },
        toolbox: {
          show: true,
          orient: 'vertical',
          left: 'right',
          top: 'center',
          feature: {
            mark: { show: true },
            dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ['line', 'bar'] },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        xAxis: [
          {
            type: 'category',
            axisTick: { show: false },
            data: ['7-16', '7-17', '7-18']
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '参与度',
            type: 'bar',
            barGap: 0,
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [82, 56, 91]
          },
          {
            name: '活跃度',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [76, 54, 93]
          },
          {
            name: '完成度',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [71, 52, 95]
          },

        ]
      };

      option && myChart.setOption(option);
    },
    // defaultMonth() {
    //   const currentDate = new Date();
    //   const currentYear = currentDate.getFullYear();
    //   const currentMonth = currentDate.getMonth() + 1;
    //   const startDate = new Date(currentYear, 0, 1);
    //   const endDate = new Date(currentYear, currentMonth, 0);
    //   this.selectedDate = [startDate, endDate];
    // },
    // formatData() {
    //   this.chartData = this.data.rows.map(rows => {
    //     const month = rows.Year_And_Month;
    //     const resultData = rows.resultData.split(',');
    //     return {
    //       month,
    //       mechanical: parseInt(resultData[0].split(':')[1]),
    //       pneumatic: parseInt(resultData[1].split(':')[1]),
    //       hydraulic: parseInt(resultData[2].split(':')[1]),
    //       electrical: parseInt(resultData[3].split(':')[1]),
    //     };

    //   });
    // }

  },



}





</script>

<style lang="scss" scoped>
#today {
  width: 1000px;
  height: 600px;
  margin: 40px auto;
}



#main {
  width: 1000px;
  height: 600px;
  margin: 40px auto;
}


.block {
  margin-top: 50px;
  text-align: center;
}
</style>
