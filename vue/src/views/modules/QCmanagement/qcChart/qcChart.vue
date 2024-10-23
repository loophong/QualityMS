<template>
  <div style="display: flex;">
    <div id="barChart" ref="barChart" style="width: 100%; height: 225px;"></div>
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
      allCount: '',
      currentCount: '',
    }
  },
  computed: {},
  async mounted() {
    await this.initData();
    this.myChart = echarts.init(document.getElementById('barChart'))
    this.updateChart()
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
      //QC小组普及率
      await this.$http({
        url: this.$http.adornUrl('/qcMembers/qcGroupMember/penetrationRate'),
        method: 'get',
      })
        .then(({ data }) => {
          this.currentCount = data.result.registrationPeopleNumbers
          this.chartLoading = false
        })

      await this.$http({
        url: this.$http.adornUrl('/qcManagement/qcAllCount/list'),
        method: 'get',
      })
        .then(({ data }) => {
          this.allCount = data.page.list[0].qcMemberCount
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
      let dataR = [(this.currentCount / this.allCount) * 100, this.activityDataResult, (this.countData.countExamined / this.countData.countSubmitted) * 100]
      let formattedData = [
        { value: dataR[0], itemStyle: { normal: { color: '#409eff' } } },
        { value: dataR[1], itemStyle: { normal: { color: '#67c23a' } } },
        { value: dataR[2], itemStyle: { normal: { color: '#e6a23c' } } }
      ];
      option = {
        title: {
          text: '点检统计',
          left: 'center', // 居中对齐
          textStyle: {
            color: '#ffffff' // 设置字体颜色为白色
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        // legend: {
        //   data: ['普及率', '活动率', '成果率']
        // },
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
            data: ['普及率', '活动率', '成果率'],
            axisLabel: {
              color: '#ffffff' // 设置x轴标签字体颜色为白色
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            axisLabel: {
              color: '#ffffff' // 设置y轴标签字体颜色为白色
            },
            splitLine: {
              show: false
            }
          }
        ],
        series: [
          {
            name: '值',
            type: 'bar',
            barGap: 0,
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            // itemStyle: {
            //   normal: {
            //     color: '#17b3a3' // 设置柱状图颜色为橙色
            //   }
            // },
            data: formattedData
          },
          // {
          //   name: '活动率',
          //   type: 'bar',
          //   label: labelOption,
          //   emphasis: {
          //     focus: 'series'
          //   },
          //   data: [this.activityDataResult]
          // },
          // {
          //   name: '成果率',
          //   type: 'bar',
          //   label: labelOption,
          //   emphasis: {
          //     focus: 'series'
          //   },
          //   data: [(this.countData.countExamined / this.countData.countSubmitted) * 100]
          // },

        ]
      };

      option && myChart.setOption(option);
    },

  },



}





</script>

<style lang="scss" scoped>
// #barChart {
//   width: 1000px;
//   height: 300px;
//   margin: 40px auto;
// }


.block {
  margin-top: 50px;
  text-align: center;
}
</style>
