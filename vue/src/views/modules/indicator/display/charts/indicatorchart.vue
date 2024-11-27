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
          actualValue: item.indicatorActualValue,
          value: item.indicatorValue
        }));
        console.log('Chart Data:', this.chartData);
      }).then(() => {
        this.renderChart();
      });
    },
    renderChart() {
      console.log('renderChart===>',this.selectedDate);
      // 获取起始日期和结束日期
      const startDate = this.selectedDate[0];
      const endDate = this.selectedDate[1];
      // 生成起止时间内的所有年月
      const generateYearMonths = (start, end) => {
        const yearMonths = [];
        let currentDate = new Date(start);
        while (currentDate <= end) {
          const year = currentDate.getFullYear();
          const month = String(currentDate.getMonth() + 1).padStart(2, '0');
          yearMonths.push(`${year}-${month}`);

          // 增加一个月
          currentDate.setMonth(currentDate.getMonth() + 1);
        }
        return yearMonths;
      };

      const allYearMonths = generateYearMonths(startDate, endDate);
      console.log('allYearMonths:', allYearMonths);

      // 处理 chartData，使其与 allYearMonths 对应
      const processChartData = (data, yearMonths) => {
        const processedData = yearMonths.map(yearMonth => {
          const matchingItem = data.find(item => item.date === yearMonth);
          return matchingItem ? matchingItem.value : null; // 或者使用 0 作为默认值
        });

        const processedActualData = yearMonths.map(yearMonth => {
          const matchingItem = data.find(item => item.date === yearMonth);
          return matchingItem ? matchingItem.actualValue : null; // 或者使用 0 作为默认值
        });

        return { processedData, processedActualData };
      };

      const { processedData, processedActualData } = processChartData(this.chartData, allYearMonths);

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
        legend: {
          data: ['指标值', '目标值']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: allYearMonths
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '指标值',
            type: 'bar',
            data: processedActualData,
          },
          {
            name: '目标值',
            type: 'line',
            data: processedData,
          }
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
