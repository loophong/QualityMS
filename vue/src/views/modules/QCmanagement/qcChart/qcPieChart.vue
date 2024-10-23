<template>
  <div>
    <div style="display: flex;">
      <div id="pieChart" ref="pieChart" style="width: 100%; height: 200px;"></div>
      <div id="pieChart2" ref="pieChart2" style="width: 100%; height: 200px;"></div>
    </div>
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
      scoreResult: {
        first: 0,
        second: 0,
        third: 0,
        fourth: 0,
        fifth: 0,
      },
      allCount: '',
      currentCount: '',
    }
  },
  computed: {},
  async mounted() {
    await this.initData();
    this.myChart2 = echarts.init(document.getElementById('pieChart'))
    this.pieChart() //课题阶段分布
    this.myChart3 = echarts.init(document.getElementById('pieChart2'))
    this.pieChart2() //课题获奖分布
  },
  methods: {
    handleEvent(data) {
      this.allCount = data
    },
    async initData() {
      this.chartLoading = true
      //各阶段个数
      await this.$http({
        url: this.$http.adornUrl('/qcSubject/registration/list'),
        method: 'get',
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
              this.stepResult.chooseTopic++
            }


          });
          this.stageNumData.forEach(item => {
            console.log(item.topicActivityResult)
            if (item.topicActivityResult) {
              if (85 <= item.topicActivityResult) {
                this.scoreResult.first++
              } else if (75 <= item.topicActivityResult < 85) {
                this.scoreResult.second++
              } else if (65 <= item.topicActivityResult < 75) {
                this.scoreResult.third++
              }
              else if (55 <= item.topicActivityResult < 65) {
                this.scoreResult.fourth++
              } else if (45 <= item.topicActivityResult < 55) {
                this.scoreResult.fifth++
              }
            }

          });
        } else {
          this.stageNumData = {}
        }
        this.chartLoading = false
      })
    },

    //课题阶段分布饼图
    pieChart() {
      console.log(this.countData)
      var chartDom = document.getElementById('pieChart');
      var myChart = echarts.init(chartDom);
      var option;
      let seriesData = [
        { value: this.stepResult.chooseTopic, name: '选择课题', itemStyle: { normal: { color: '#FFFFCC' } } },
        { value: this.stepResult.currentSituation, name: '现状调查', itemStyle: { normal: { color: '#FFCC99' } } },
        { value: this.stepResult.setGoals, name: '设定目标', itemStyle: { normal: { color: '#CCFFFF' } } },
        { value: this.stepResult.causeAnalysis, name: '原因分析', itemStyle: { normal: { color: '#CCFFCC' } } },
        { value: this.stepResult.reasonInsure, name: '要因确定', itemStyle: { normal: { color: '#FFCCCC' } } },
        { value: this.stepResult.developCountermeasures, name: '制定对策', itemStyle: { normal: { color: '#FFCCFF' } } },
        { value: this.stepResult.implementationCountermeasures, name: '对策实施', itemStyle: { normal: { color: '#CCCCFF' } } },
        { value: this.stepResult.checkEffectiveness, name: '检查效果', itemStyle: { normal: { color: '#FFFF99' } } },
        { value: this.stepResult.consolidationMeasures, name: '巩固措施', itemStyle: { normal: { color: '#FFFFBB' } } },
        { value: this.stepResult.reliabilityAnalysis, name: '可靠性分析', itemStyle: { normal: { color: '#CCFFEE' } } },
        { value: this.stepResult.insureBest, name: '提出方案确定最佳方案', itemStyle: { normal: { color: '#CCFFDD' } } },
        { value: this.stepResult.standardization, name: '标准化', itemStyle: { normal: { color: '#FFCCCC' } } },
        { value: this.stepResult.summary, name: '总结和下一步打算', itemStyle: { normal: { color: '#FFCCFF' } } }
      ]
      const filteredSeriesData = seriesData.filter(item => item.value !== 0);
      option = {
        title: {
          text: '课题阶段状态',
          left: 'center',
          textStyle: {
            color: '#ffffff' // 设置字体颜色为白色
          }
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
    //课题获奖情况饼图
    pieChart2() {
      var chartDom = document.getElementById('pieChart2');
      var myChart = echarts.init(chartDom);
      var option;
      let seriesData = [
        { value: this.scoreResult.first, name: '一等奖', itemStyle: { normal: { color: '#FFD700' } } },
        { value: this.scoreResult.second, name: '二等奖', itemStyle: { normal: { color: '#FFA500' } } },
        { value: this.scoreResult.third, name: '三等奖', itemStyle: { normal: { color: '#FFFF00' } } },
        { value: this.scoreResult.fourth, name: '四等奖', itemStyle: { normal: { color: '#00FF00' } } },
        { value: this.scoreResult.fifth, name: '鼓励奖', itemStyle: { normal: { color: '#ADD8E6' } } },
      ]
      const filteredSeriesData = seriesData.filter(item => item.value !== 0);
      option = {
        title: {
          text: '课题获奖情况',
          left: 'center',
          textStyle: {
            color: '#ffffff' // 设置字体颜色为白色
          }
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
    }
  },



}





</script>

<style lang="scss" scoped>
.block {
  margin-top: 50px;
  text-align: center;
}

// #pieChart {
//   width: 1000px;
//   height: 00px;
//   margin: 40px auto;
// }

// #pieChart2 {
//   width: 1000px;
//   height: 600px;
//   margin: 40px auto;
// }</style>
