<template>
  <div class="mod-home">
    <div id="indicator" class="box">
      <div>
        指标总数：{{ indicatorCounts }}
      </div>
      <div id="indicatorChart" ref="indicatorChart"></div>
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
  data() {
    return {
      //----------------指标模块-----------------
      indicatorCounts: 0,
      myIndicatorChart1: {}, //指标柱状图
      departmentCountsList: [], //查询返回list
      departmentList: ["质量科", "市场科", "企业管理科"], //部门列表
    };
  },
  mounted() {
    this.getIndicatorCounts();

  },
  methods: {
    //----------------指标模块-----------------
    getIndicatorCounts() {
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/countsByDepartmant'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        console.log('data123:', data);
        this.departmentCountsList = data;
        console.log('departmentCountsList:', this.departmentCountsList);
        this.indicatorCounts = data.reduce((total, item) => total + item.counts, 0);
        console.log('indicatorCounts:', this.indicatorCounts);
        this.renderChart();
      });
    },
    renderChart() {
      const chart = echarts.init(this.$refs.indicatorChart);

      const option = {
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
          data: this.departmentCountsList.map(item => item.managementDepartment),
        },
        yAxis: {
          type: 'value',
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
  }
};
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
  text-align: center;
  padding: 20px;
  border-radius: 4px;
  min-width: 200px; /* 设置最小宽度，防止过小 */
  height: 400px; /* 设置每个容器的高度为父容器的百分比 */
}
#indicatorChart {
  width: 100%;
  height: 100%; /* 确保图表容器高度为 100% */
}
</style>
