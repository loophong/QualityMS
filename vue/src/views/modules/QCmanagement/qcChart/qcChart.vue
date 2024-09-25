<template>
  <div style="display: flex;">
    <div id="barChart" ref="barChart" style="width: 50%; height: 300px;"></div>
    <div id="pieChart" ref="pieChart" style="width: 50%; height: 300px;"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import moment from 'moment';

export default {
  data() {
    return {
      chartLoading: false,
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
      parsedData: {},
      activityData: {},
      activityDataResult: '',
      countData: {},
    }
  },
  computed: {},
  async mounted() {
    await this.initData();
    this.myChart = echarts.init(document.getElementById('barChart'))
    this.updateChart()
    this.myChart2 = echarts.init(document.getElementById('pieChart'))
    this.pieChart()
  },
  methods: {
    async initData() {
      this.chartLoading = true
      await this.$http({
        url: this.$http.adornUrl('/qcMembers/qcGroupMember/activityRate'),
        method: 'get',
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.activityData = data.result
          if (data.result.registrationNumbers === 0) {
            this.activityDataResult = 0;
          } else {
            this.activityDataResult = (data.result.haveRegistrationNumbers / data.result.registrationNumbers * 100).toFixed(2)
          }
        } else {
          this.activityData = {}
        }
        this.chartLoading = false
      })
      await this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/count'),
        method: 'get',
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.countData = data.count
        } else {
          this.countData = {}
        }
        this.chartLoading = false
      })
    },


    updateChart() {
      var app = {};

      var chartDom = document.getElementById('barChart');
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
          text: ''
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['普及率', '活动率', '成果率']
        },
        toolbox: {
          show: false,
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
            data: ['点检统计']
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '普及率',
            type: 'bar',
            barGap: 0,
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [82]
          },
          {
            name: '活动率',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [this.activityDataResult]
          },
          {
            name: '成果率',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [71]
          },

        ]
      };

      option && myChart.setOption(option);
    },

    pieChart() {
      console.log(this.countData)
      var chartDom = document.getElementById('pieChart');
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        title: {
          text: '课题状态分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '课题状态',
            type: 'pie',
            radius: '50%',
            center: ['50%', '50%'], // 饼图位置
            data: [
              { value: this.countData.countRegistration - this.countData.countSubmitted, name: '进行中' },
              { value: this.countData.countWithoutExamined, name: '审核中' },
              { value: this.countData.countExamined, name: '已完成' },
              { value: this.countData.countRegistration, name: '已注册' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            label: {
              show: true,
              position: 'outside', // 标签位置,inside、outside、top、bottom、left、right
              formatter: '{b}: {c} ({d}%)'
            },
            labelLine: {
              show: true,
              smooth: 0.2,
            }
          }
        ]
      };

      option && myChart.setOption(option);
    }
  },



}





</script>

<style lang="scss" scoped>
#barChart {
  width: 1000px;
  height: 600px;
  margin: 40px auto;
}


.block {
  margin-top: 50px;
  text-align: center;
}

#pieChart {
  width: 1000px;
  height: 600px;
  margin: 40px auto;
}
</style>
