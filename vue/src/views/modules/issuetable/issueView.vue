<!--<template>-->
<!--  <div>-->
<!--    <div style="display: flex;">-->
<!--      <div id="barChart1" ref="barChart1" style="width: 50%; height: 300px;"></div>-->
<!--      <div id="barChart2" ref="barChart2" style="width: 50%; height: 300px;"></div>-->
<!--      <div id="barChart3" ref="barChart3" style="width: 50%; height: 300px;"></div>-->
<!--      <div id="barChart4" ref="barChart4" style="width: 50%; height: 300px;"></div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->
<template>
  <div>
    <div style="display: flex; flex-wrap: wrap; justify-content: space-between; gap: 20px;">
      <!-- Bar Chart 1 -->
      <div style="flex: 0 0 48%; height: 300px;" id="barChart1" ref="barChart1"></div>

      <!-- Bar Chart 2 (with Year Selector) -->
      <div style="flex: 0 0 48%; height: 300px;">
        <div style="margin-bottom: 10px; text-align: center;">
          <!-- 年份选择器 -->
          <el-select v-model="selectedYear" placeholder="选择年份" @change="onYearChange" style="width: 150px; margin: 0 auto;">
            <el-option
              v-for="year in yearOptions"
              :key="year"
              :label="year"
              :value="year"
            ></el-option>
          </el-select>
        </div>
        <div id="barChart2" ref="barChart2" style="height: 250px;"></div>
      </div>

      <!-- Bar Chart 3 -->
      <div style="flex: 0 0 48%; height: 300px;">
        <div style="margin-bottom: 10px; text-align: center;">
          <!-- 年份选择器 -->
          <el-select v-model="selectedYearChart3" placeholder="选择年份" @change="onYearChangeChart3" style="width: 150px; margin: 0 auto;">
            <el-option
              v-for="year in yearOptions"
              :key="year"
              :label="year"
              :value="year"
            ></el-option>
          </el-select>
        </div>
        <div id="barChart3" ref="barChart3" style="height: 250px;"></div>
      </div>

      <!-- Bar Chart 4 -->
      <div style="flex: 0 0 48%; height: 300px;" id="barChart4" ref="barChart4"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  data() {
    return {
      //图一
      issueStats: { 暂缓: 0, 持续: 0, 结项: 0, 关闭: 0, 创建: 0 }, // 初始状态
      //图二
      selectedYear: new Date().getFullYear(), // 默认当前年份
      yearOptions: [], // 存储动态生成的年份列表 // 可选择的年份列表
      monthlyData: [], // 每个月的问题数
      months: [], // 月份数组（1 到 12）
      monthlyGrowth: null, // 月同比增长问题数
      //图三
      selectedYearChart3: new Date().getFullYear(),  // 当前选择的年份（Bar Chart 3）
      monthlyDuplicateData: [], // 存储月度重复问题数的数据（Bar Chart 3）
      duplicateIssueTotal: 0, // 重复问题总数
      // 图四
      issueCategoryStats: {}, // 当前月进行中问题的类别数据

    };
  },
  mounted() {
    // 初始化图表
    this.initCharts();
    // 获取数据
    this.getIssueStats();

    this.getMonthlyIssueStats(this.selectedYear); // 默认加载当前年份的数据
    this.generateYearOptions();  // 动态生成年份列表

    this.getMonthlyDuplicateStats(this.selectedYearChart3);  // 获取初始数据（Bar Chart 3）

    this.getInProgressIssueCategoryStats();  // 获取当前月进行中问题的类别数据（Bar Chart 4）
  },
  methods: {
    //图一
    // 获取当前月份问题状态数据
    getIssueStats() {
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/truecurrentMonth'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.issueStats = data.stats; // 假设返回的数据格式为 { 提出: 10, 暂停: 12, ... }
          console.log('数据转换中......', this.issueStats);
          this.renderIssueChart(); // 渲染图表
        } else {
          // 默认值
          this.issueStats = { 暂缓: 0, 持续: 0, 结项: 0,创建: 0 ,关闭: 0};
        }
      });
    },
    // 初始化图表
    initCharts() {
      const chart1 = echarts.init(this.$refs.barChart1);

      const options = {
        title: {
          text: '问题当月完成情况'
        },
        tooltip: {},
        xAxis: {
          type: 'category',
          data: ['总数', '持续', '结项', '关闭']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [0, 0, 0, 0],  // 初始数据为 0
            type: 'bar'
          }
        ]
      };

      chart1.setOption(options);
    },
    // 渲染图表
    renderIssueChart() {
      const chart1 = echarts.init(this.$refs.barChart1);

      // 计算 "总数"
      const totalCount = this.issueStats['暂缓'] + this.issueStats['持续'] + this.issueStats['结项'];
      console.log('总数', totalCount);
      const options = {
        title: {
          text: '当月问题统计'
        },
        tooltip: {},
        xAxis: {
          type: 'category',
          data: ['总数', '持续', '结项', '关闭']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [
              totalCount,
              this.issueStats['持续'],
              this.issueStats['结项'],
              this.issueStats['关闭']
            ],
            type: 'bar'
          }
        ]
      };

      chart1.setOption(options);
    },

    //图二
    // 生成年份选择器的可选年份范围
    generateYearOptions() {
      const currentYear = new Date().getFullYear();
      const startYear = currentYear - 5; // 当前年份之前5年
      const endYear = currentYear; // 当前年份之后5年
      this.yearOptions = [];

      for (let year = startYear; year <= endYear; year++) {
        this.yearOptions.push(year);
      }
    },
    // 年份选择变更时
    onYearChange() {
      this.getMonthlyIssueStats(this.selectedYear);
    },

    // 向后端请求某个年份的月度问题数
    getMonthlyIssueStats(year) {
      this.$http({
        url: this.$http.adornUrl(`/generator/issuetable/monthlyStats`),
        method: 'get',
        params: { year },
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.monthlyData = data.stats; // 假设返回的数据格式 { 1: 10, 2: 12, ..., 12: 5 }
          // 计算月同比增长
          this.calculateMonthGrowth();
          this.renderMonthlyChart(); // 渲染图表
        } else {
          // 默认值（如果没有数据）
          this.monthlyData = Array(12).fill(0);
          this.renderMonthlyChart();
        }
      });
    },
    // 计算月同比增长
    calculateMonthGrowth() {
      const currentMonth = new Date().getMonth() + 1; // 当前月
      const previousMonth = currentMonth === 1 ? 12 : currentMonth - 1; // 上个月

      const currentMonthCount = this.monthlyData[currentMonth] || 0;
      const previousMonthCount = this.monthlyData[previousMonth] || 0;

      if (previousMonthCount !== 0) {
        this.monthlyGrowth = ((currentMonthCount - previousMonthCount) / previousMonthCount) * 100;
      } else {
        this.monthlyGrowth = 0; // 如果上个月的问题数为0，则同比增长为0
      }
    },
    // 渲染月度问题数的柱状图
    renderMonthlyChart() {
      const chart2 = echarts.init(this.$refs.barChart2);

      // 设置月份标签 ["1月", "2月", ..., "12月"]
      this.months = Array.from({ length: 12 }, (_, i) => `${i + 1}月`);

      // 将 this.monthlyData 的键转为数组，并确保它是从1到12的映射
      const monthData = this.months.map((month, index) => {
        const monthNumber = index + 1; // 获取当前月的数字，例如 1, 2, ..., 12
        return this.monthlyData[monthNumber] || 0; // 获取每个月的问题数，如果没有数据则为 0
      });

      const options = {
        title: {
          text: `月度问题数展示 -  本月同比增长: ${this.monthlyGrowth.toFixed(2)}%`
        },
        tooltip: {},
        xAxis: {
          type: 'category',
          data: this.months, // 月份数据
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: monthData, // 使用月度问题数数据
            type: 'bar',
          }
        ]
      };

      chart2.setOption(options);
    },

    // 年份选择变化处理（Bar Chart 3）
    onYearChangeChart3() {
      this.getMonthlyDuplicateStats(this.selectedYearChart3);
    },
    // 向后端请求某个年份的月度重复问题数
    getMonthlyDuplicateStats(year) {
      this.$http({
        url: this.$http.adornUrl(`/generator/issuetable/monthlyDuplicateStats`), // 假设后端接口为 `/monthlyDuplicateStats`
        method: 'get',
        params: { year },
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.monthlyDuplicateData = data.stats;  // 假设返回的数据格式 { 1: 3, 2: 5, ..., 12: 0 }
          this.duplicateIssueTotal = data.total; // 获取总数
          this.renderDuplicateChart();  // 渲染图表
        } else {
          // 默认值（如果没有数据）
          this.monthlyDuplicateData = Array(12).fill(0);
          this.renderDuplicateChart();
        }
      });
    },
    // 渲染月度重复问题数的柱状图（Bar Chart 3）
    renderDuplicateChart() {
      const chart3 = echarts.init(this.$refs.barChart3);

      // 设置月份标签 ["1月", "2月", ..., "12月"]
      this.months = Array.from({ length: 12 }, (_, i) => `${i + 1}月`);  // ["1月", "2月", ..., "12月"]

      // 将 this.monthlyDuplicateData 的键转为数组，并确保它是从1到12的映射
      const monthData = this.months.map((month, index) => {
        const monthNumber = index + 1; // 获取当前月的数字，例如 1, 2, ..., 12
        return this.monthlyDuplicateData[monthNumber] || 0; // 获取每个月的重复问题数，如果没有数据则为 0
      });

      // 图表配置
      const options = {
        title: {
          text: `月度重复问题数 - 重复问题总数: ${this.duplicateIssueTotal}`
        },
        tooltip: {},
        xAxis: {
          type: 'category',
          data: this.months,  // 月份数据
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: monthData,  // 使用月度重复问题数数据
            type: 'bar',  // 图表类型：柱状图
          }
        ]
      };

      // 设置图表的配置项
      chart3.setOption(options);
    },


    //图四
    // 获取当前月进行中问题的类别数据
    getInProgressIssueCategoryStats() {
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/currentMonthInProgressCategoryStats'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        console.log('返回的数据：', data); // 调试输出
        if (data && data.code === 0) {
          this.issueCategoryStats = data.stats;  // 确保 data.stats 格式正确
          console.log('处理后的统计数据：', this.issueCategoryStats); // 调试输出
          this.renderIssueCategoryChart();  // 渲染图表
        } else {
          // 默认值
          this.issueCategoryStats = {};
          this.renderIssueCategoryChart();
        }
      });
    },

    // 渲染图四的柱状图
    renderIssueCategoryChart() {
      const chart4 = echarts.init(this.$refs.barChart4);

      // 获取问题类别和问题数
      const categories = Object.keys(this.issueCategoryStats);  // 问题类别
      const issueCounts = categories.map(category => this.issueCategoryStats[category]);  // 对应问题数

      // 图表配置项
      const options = {
        title: {
          text: '当月问题类别统计'
        },
        tooltip: {
          trigger: 'axis',
        },
        xAxis: {
          type: 'category',
          data: categories,  // 问题类别
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: issueCounts,  // 问题数
            type: 'bar',  // 设置为柱状图
          }
        ]
      };

      chart4.setOption(options);  // 渲染图表
    },




  }
};
</script>

<style scoped>
</style>
