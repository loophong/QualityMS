<template>
  <div class="container">
    <!-- 统计组件 -->
    <qc-all-count v-if="isAuth('qcManagement:qcAllCount:info')" @allCount="handleEvent"></qc-all-count>

    <!-- 图表区域 -->
    <div class="charts-container">
      <div class="chart-wrapper" id="barChart" ref="barChart" style="height: 300px;"></div>
      <div class="chart-wrapper" id="pieChart" ref="pieChart" style="height: 300px;"></div>
    </div>

    <!-- 课题获奖情况 -->
    <div class="award-section">
      <div class="chart-wrapper" id="groupChart3" ref="groupChart3" style="height: 300px;"></div>
      <div class="chart-wrapper" id="pieChart2" ref="pieChart2" style="height: 300px;"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import moment from 'moment';
import qcAllCount from './qcAllCount'
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
      stageNumData: {},
      activityDataResult: '',
      countData: {},
      stepResult: {
        chooseTopic: 0,
        currentSituation: 0,
        setGoals: 0,
        causeAnalysis: 0,
        reasonInsure: 0,
        developCountermeasures: 0,
        implementationCountermeasures: 0,
        checkEffectiveness: 0,
        consolidationMeasures: 0,
        summary: 0,
        reliabilityAnalysis: 0,
        insureBest: 0,
        standardization: 0,
      },
      //获奖情况统计
      // scoreResult: [{
      //   groupName:'',
      //   first: 0,
      //   second: 0,
      //   third: 0,
      //   fourth: 0,
      //   fifth: 0,
      // }],
      scoreResult: {},
      allCount: '',
      currentCount: '',
      pieTwoLegend: [],
    }
  },
  components: {
    qcAllCount
  },
  computed: {},
  async mounted() {
    await this.initData();
    this.myChart = echarts.init(document.getElementById('barChart'))
    this.updateChart()
    this.myChart2 = echarts.init(document.getElementById('pieChart'))
    this.pieChart() //课题阶段分布
    this.myChart3 = echarts.init(document.getElementById('pieChart2'))
    this.pieChart2() //课题获奖分布
    this.myChart4 = echarts.init(document.getElementById('groupChart3'))
    this.groupChart3() //点检小组情况
    //QC小组活动率
    await this.$http({
      url: this.$http.adornUrl('/qcSubject/registration/rewordRate'),
      method: 'get',
      groupName: 'ces',
    }).then(({ data }) => {
      if (data && data.code === 0) {

      } else {
        this.activityData = {}
      }
      this.chartLoading = false
    })
  },
  methods: {
    handleEvent(data) {
      this.allCount = data
    },
    async initData() {
      this.chartLoading = true
      //QC小组活动率
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
      //各阶段个数
      await this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/all'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': 10000000,
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.stageNumData = data.page.list.filter(item => item.topicReviewStatus === 3);
          this.stageNumData.forEach(item => {
            if (item.topicActivityStatus == '1') {
              if (item.topicType == '问题解决型') {
                this.stepResult.currentSituation++
              } else {
                this.stepResult.setGoals++
              }
            } else if (item.topicActivityStatus == '2') {
              if (item.topicType == '创新型') {
                this.stepResult.insureBest++
              } else if (item.topicType == '问题解决型') {
                this.stepResult.setGoals++
              } else if (item.topicType == '问题解决型(指令型)') {
                this.stepResult.reliabilityAnalysis++
              }
            } else if (item.topicActivityStatus == '3') {
              if (item.topicType == '创新型') {
                this.stepResult.developCountermeasures++
              } else {
                this.stepResult.causeAnalysis++
              }
            } else if (item.topicActivityStatus == '4') {
              if (item.topicType == '创新型') {
                this.stepResult.implementationCountermeasures++
              } else {
                this.stepResult.reasonInsure++
              }
            } else if (item.topicActivityStatus == '5') {
              if (item.topicType == '创新型') {
                this.stepResult.checkEffectiveness++
              } else {
                this.stepResult.developCountermeasures++
              }
            } else if (item.topicActivityStatus == '6') {
              if (item.topicType == '创新型') {
                this.stepResult.standardization++
              } else {
                this.stepResult.implementationCountermeasures++
              }
            } else if (item.topicActivityStatus == '7') {
              if (item.topicType == '创新型') {
                this.stepResult.summary++
              } else {
                this.stepResult.checkEffectiveness++
              }
            } else if (item.topicActivityStatus == '8') {
              this.stepResult.consolidationMeasures++
            } else if (item.topicActivityStatus == '9') {
              this.stepResult.summary++
            } else {
              if (item.topicType == '创新型' && item.topicActivityStatus != '8') {
                this.stepResult.chooseTopic++
              } else if (item.topicActivityStatus != '10') {
                this.stepResult.chooseTopic++
              }
            }


          });
          this.stageNumData.forEach(item => {
            if (item.topicActivityResult) {
              if (!(item.groupName in this.scoreResult)) {
                this.scoreResult[item.groupName] = {
                  first: 0,
                  second: 0,
                  third: 0,
                  fourth: 0,
                  fifth: 0,
                  allCount: 0,
                }
              }
              this.scoreResult[item.groupName].allCount++
              if ('一等奖' == item.topicActivityResult) {
                this.scoreResult[item.groupName].first++
              } else if ('二等奖' == item.topicActivityResult) {
                this.scoreResult[item.groupName].second++
              } else if ('三等奖' == item.topicActivityResult) {
                this.scoreResult[item.groupName].third++
              }
              else if ('四等奖' == item.topicActivityResult) {
                this.scoreResult[item.groupName].fourth++
              } else if ('鼓励奖' == item.topicActivityResult) {
                this.scoreResult[item.groupName].fifth++
              }
            } else {
              if (!(item.groupName in this.scoreResult)) {
                this.scoreResult[item.groupName] = {
                  first: 0,
                  second: 0,
                  third: 0,
                  fourth: 0,
                  fifth: 0,
                  allCount: 0,
                }
              }
              this.scoreResult[item.groupName].allCount++
            }

          });
          console.log('scoreResult:', this.scoreResult)
        } else {
          this.stageNumData = {}
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
        formatter: function (params) {
          // 确保 params.value 是数值类型，并添加百分号
          return `${params.value} %`;
        },
        fontSize: 16,
        rich: {
          name: {}
        }
      };
      let dataR = [((this.currentCount / this.allCount) * 100).toFixed(2), this.activityDataResult, ((this.countData.countSubmitted / this.countData.countRegistration) * 100).toFixed(2)]
      console.log({ dataR })
      let formattedData = [
        { value: dataR[0], itemStyle: { normal: { color: '#409eff' } } },
        { value: dataR[1], itemStyle: { normal: { color: '#67c23a' } } },
        { value: dataR[2], itemStyle: { normal: { color: '#e6a23c' } } }
      ];
      option = {
        title: {
          text: '点检统计',
          left: 'left', // 居中对齐
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
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
            data: ['普及率', '活动率', '成果率'],
          }
        ],
        yAxis: [
          {
            type: 'value',
            min: 0,
            max: 100,
            splitLine: {
              show: true,
            },
            axisLabel: {
              formatter: '{value} %'
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

          // },

        ]
      };

      option && myChart.setOption(option);
    },
    //课题阶段分布饼图
    pieChart() {
      console.log(this.countData)
      var chartDom = document.getElementById('pieChart');
      var myChart = echarts.init(chartDom);
      var option;
      let seriesData = [
        { value: this.stepResult.chooseTopic, name: '选择课题' },
        { value: this.stepResult.currentSituation, name: '现状调查' },
        { value: this.stepResult.setGoals, name: '设定目标' },
        { value: this.stepResult.causeAnalysis, name: '原因分析' },
        { value: this.stepResult.reasonInsure, name: '要因确定' },
        { value: this.stepResult.developCountermeasures, name: '制定对策' },
        { value: this.stepResult.implementationCountermeasures, name: '对策实施' },
        { value: this.stepResult.checkEffectiveness, name: '检查效果' },
        { value: this.stepResult.consolidationMeasures, name: '巩固措施' },
        { value: this.stepResult.reliabilityAnalysis, name: '可靠性分析' },
        { value: this.stepResult.insureBest, name: '提出方案确定最佳方案' },
        { value: this.stepResult.standardization, name: '标准化' },
        { value: this.stepResult.summary, name: '总结和下一步打算' },
      ]
      const filteredSeriesData = seriesData.filter(item => item.value !== 0);
      option = {
        title: {
          text: '课题阶段状态',
          left: 'left'
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
            name: '个数',
            type: 'pie',
            radius: '50%',
            center: ['50%', '50%'], // 饼图位置
            data: filteredSeriesData,
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
    },
    //课题获奖情况图
    pieChart2() {
      var app = {};

      var chartDom = document.getElementById('pieChart2');
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
      let legendData = [];
      let seriesData = [];
      // 遍历 this.scoreResult 来填充 legend 和 series
      Object.keys(this.scoreResult).forEach(groupName => {
        console.log('+', this.scoreResult[groupName])
        if (this.scoreResult[groupName].first != 0 || this.scoreResult[groupName].second != 0 || this.scoreResult[groupName].third != 0 || this.scoreResult[groupName].fourth != 0 || this.scoreResult[groupName].fifth != 0) {
          legendData.push(groupName); // 将每个 groupName 添加到 legend
          // 为每个 groupName 创建一个系列条目
          seriesData.push({
            name: groupName,
            type: 'bar',
            data: [
              this.scoreResult[groupName].first,
              this.scoreResult[groupName].second,
              this.scoreResult[groupName].third,
              this.scoreResult[groupName].fourth,
              this.scoreResult[groupName].fifth
            ],
            label: {
              show: true
            }
          });
        }
      });
      console.log('stageNumData', this.stageNumData)
      option = {
        title: {
          text: '课题获奖状态\n',
          left: 'left', // 居中对齐
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
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
        legend: {
          data: legendData
        },
        xAxis: [
          {
            type: 'category',
            axisTick: { show: false },
            data: ['一等奖', '二等奖', '三等奖', '四等奖', '鼓励奖'],
          }
        ],
        yAxis: [
          {
            type: 'value',
            axisLabel: {
              show: true,
              formatter: function (value) {
                // 只显示整数，小数部分不显示
                return Number.isInteger(value) ? value : '';
              }
            },
            splitLine: {
              show: true,
            }
          }
        ],
        series: seriesData,
      };
      option && myChart.setOption(option);
    },
    //点检小组情况图
    groupChart3() {
      var app = {};

      var chartDom = document.getElementById('groupChart3');
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
      let legendData = [];
      let seriesData = [];
      Object.keys(this.scoreResult).forEach(groupName => {
        legendData.push(groupName);
        seriesData.push({
          name: groupName,
          type: 'bar',
          label: labelOption,
          data: [
            0,
            0,
            (((this.scoreResult[groupName].first + this.scoreResult[groupName].second + this.scoreResult[groupName].third + this.scoreResult[groupName].fourth + this.scoreResult[groupName].fifth) / this.scoreResult[groupName].allCount) * 100).toFixed(2),
          ],
          label: {
            show: true
          }
        });
      });
      console.log('stageNumData', this.stageNumData)
      option = {
        title: {
          text: '小组点检统计',
          left: 'left',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
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
        legend: {
          type: 'scroll',
          animationDurationUpdate: 0,
          data: legendData
        },
        xAxis: [
          {
            type: 'category',
            axisTick: { show: false },
            data: ['普及率', '活动率', '成果率'],
          }
        ],
        yAxis: [
          {
            type: 'value',
            min: 0,
            max: 100,
            axisLabel: {
              show: true,
              formatter: function (value) {
                return value + '%';
              }
            },
            splitLine: {
              show: true,
            }
          }
        ],
        series: seriesData,
      };
      option && myChart.setOption(option);
    }

  },



}





</script>

<style lang="scss" scoped>
.container {
  padding: 20px;
}

.charts-container,
.award-section {
  display: flex;
  gap: 20px;
  /* 为图表之间添加间距 */
  margin-top: 20px;
  border-top: 3px solid #ccc;
  /* 添加顶部分隔线 */
  padding-top: 20px;
  flex-wrap: nowrap;
  /* 确保图表不会换行 */
}

.chart-wrapper {
  flex: 1;
  /* 让每个图表平分可用空间 */
  min-width: 0;
  /* 防止内容溢出 */
  border: 3px solid #ddd;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  padding: 20px;
  /* 增加内边距 */
  height: calc(300px - 40px);
  /* 减去内边距的高度，保持总高度不变 */
}
</style>
