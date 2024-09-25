<template>
  <div>
    <div class="block">
      <span class="DataSelect" style="margin-right:10px">日期选择</span>
      <el-date-picker
        v-model="selectedDate"
        type="monthrange"
        start-placeholder="开始月份"
        end-placeholder="结束月份"
        style="width: 250px;"
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
      selectedDate: [],  // 选择日期范围
      pickerOptions: [],
      timeData: {        //起止时间
        startTime: new Date(),
        endTime: new Date(),
      },
      chartData: [],  // 图表数据
      dataList: [],
      queryParams: {
        indicatorName: this.$route.params.indicatorName,
        startTime: new Date(),
        endTime: new Date(),
      },
    };
  },
  mounted() {
    console.log("接收到的参数:", this.$route.params);
    console.log("Indicator Name:", this.$route.params.indicatorName);
    this.defaultMonth();
    this.getDataList();
  },
  methods: {
    //根据路由传递的指标名获取列表
    getDataList() {
      console.log('getDataList===>',this.selectedDate);
      this.queryParams.startTime = this.selectedDate[0];
      this.queryParams.endTime = this.selectedDate[1];
      console.log('queryParams:', this.queryParams);
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/indicatorlist'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': 10000,
          'key': this.queryParams,
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log('data:', data);
          this.dataList = data.page.list;
          console.log('DataList222:', this.dataList);
        } else {
          this.dataList = [];
        }
      }).then(() => {
        this.chartData = this.dataList.map(item => ({     //填充图表数据
          date: item.yearMonth,
          value: item.indicatorValue
        }));
        console.log('Chart Data:', this.chartData);
      }).then(() => {
        this.renderChart();
      });
    },
    renderChart() {
      console.log('renderChart===>',this.selectedDate);
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

    handleDateChange(value) {
      if (value && value[1]) {
        let endDate = new Date(value[1]);
        endDate.setMonth(endDate.getMonth() + 1);
        endDate.setDate(0);
        this.selectedDate[1] = endDate;
      }
      this.getDataList();
      console.log('value======>',value)
      console.log('this.selectedDate======>',this.selectedDate)
    },
    defaultMonth() {
      const currentDate = new Date();
      // 获取当前月份（JavaScript 中的月份是从0开始的，所以这里需要 +1）
      const currentYear = currentDate.getFullYear();
      const currentMonth = currentDate.getMonth() + 1;
      const startDate = new Date(currentYear, 0, 1);
      const endDate = new Date(currentYear, currentMonth, 0);
      this.selectedDate = [startDate, endDate];
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
