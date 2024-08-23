<template>
  <div>
    <div class="block">
      <span class="DataSelect" style="margin-right:10px">日期选择</span>
      <el-date-picker v-model="selectedDate" type="monthrange" unlink-panels range-separator="至"
                      start-placeholder="开始月份" end-placeholder="结束月份" :picker-options="pickerOptions"
                      @change="handleDateChange">
      </el-date-picker>
    </div>
    <div v-if="loading"
         style="display: flex; justify-content: center; align-items: center; height: 50vh; font-size: 24px;">加载中……
    </div>
    <div id="indicatorChart" ref="indicatorChart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  data() {
    return {
      loading: false,
      selectedDate: [],
      pickerOptions: [],
      chartData: [
        { date: '2023-01', value: 120 },
        { date: '2023-02', value: 200 },
        { date: '2023-03', value: 150 },
        { date: '2023-04', value: 80 },
        { date: '2023-05', value: 70 },
        { date: '2023-06', value: 110 },
        { date: '2023-07', value: 130 }
      ],
      dataList: [],
    };
  },
  mounted() {
    console.log("接收到的参数:", this.$route.params);
    console.log("Indicator Name:", this.$route.params.indicatorName);
    this.getDataList();
    this.renderChart();
  },
  methods: {
    getDataList() {
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': 10000,
          'key': this.$route.params.indicatorName,
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log('data:', data);
          this.dataList = data.page.list;
          console.log('DataList222:', this.dataList);
        } else {
          this.dataList = [];
        }
      });
    },
    handleDateChange() {

    },
    renderChart() {
      const chart = echarts.init(this.$refs.indicatorChart);

      const option = {
        title: {
          text: this.$route.params.indicatorName,
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
          data: this.chartData.map(item => item.date),
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
  },
};
</script>

<style>
#indicatorChart {
  width: 1000px;
  height: 600px;
  margin: 40px auto;
}

.block {
  margin-top: 100px;
  text-align: center;
}
</style>
