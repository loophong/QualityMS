<template>
  <div class="mod-home">
    <div id="indicator" class="box">
      <div>
        指标总数：{{indicatorCounts}}
      </div>
      <div id="indicatorChart">

      </div>
    </div>
    <div id="task" class="box">区域2</div>
    <div id="issue" class="box">区域3</div>
    <div id="QC" class="box">区域4</div>
  </div>
</template>


<script>
import * as echarts from "echarts";
export default {
  name: 'home',
  data () {
    return {
      //---------------------指标模块-------------------------
      indicatorCounts: 0,
      myIndicatorChart1: {}, //指标柱状图
      departmentList: ["质量科", "市场科", "企业管理科"], //部门列表
    }
  },
  mounted() {
    this.getIndicatorCounts()
    this.renderChart()

  },
  methods: {
    //---------------------指标模块-------------------------
    //查询指标总数
    getIndicatorCounts() {
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/list02'),
        method: 'get',
        params: this.$http.adornParams({
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.indicatorCounts = data.page.totalCount
        } else {
          this.indicatorCounts = 0
        }
      })
    },
    renderChart() {
      console.log('renderChart===>',this.selectedDate);
      const chart = echarts.init(document.getElementById("indicatorChart"));

      const option = {
        title: {
          text: "指标汇总",
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
          data: this.departmentList,
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '值',
            type: 'bar',
            data: this.chartData.map(item => item.value),
            itemStyle: {
              color: '#409EFF',
            },
          },
        ],
      };

      chart.setOption(option);
    },

  }
}

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
    //background-color: #f2f2f2;
    text-align: center;
    padding: 20px;
    border-radius: 4px;
    min-width: 200px; /* 设置最小宽度，防止过小 */
    height: 400px; /* 设置每个容器的高度为父容器的百分比 */
  }
</style>

