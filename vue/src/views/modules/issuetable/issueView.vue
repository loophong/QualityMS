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
<!--<template>-->
<!--  <div>-->

<!--    <div style="display: flex; flex-wrap: wrap; justify-content: space-between; gap: 20px;height: 200px">-->
<!--      &lt;!&ndash; Bar Chart 1 with adjacent stats &ndash;&gt;-->
<!--      <div style="flex: 0 0 48%; display: flex; gap: 20px;">-->

<!--        &lt;!&ndash; Stats Section &ndash;&gt;-->
<!--        <div-->
<!--          style="flex: 0 0 30%; display: flex; flex-direction: column; justify-content: space-around; padding: 10px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;"-->
<!--        >-->
<!--          <div style="display: flex; align-items: center; gap: 5px;">-->
<!--            <span style="font-weight: bold; font-size: 16px;">当月问题总数：</span>-->
<!--            <span style="font-weight: bold; font-size: 16px;">{{ totalCount || 0 }}</span>-->
<!--          </div>-->
<!--          <div style="display: flex; align-items: center; gap: 5px;">-->
<!--            <span style="font-weight: bold; font-size: 16px;">同比：</span>-->
<!--            <span style="font-weight: bold; font-size: 16px;">{{ formattedYearlyGrowth }}</span>-->
<!--          </div>-->
<!--          <div style="display: flex; align-items: center; gap: 5px;">-->
<!--            <span style="font-weight: bold; font-size: 16px;">环比：</span>-->
<!--            <span style="font-weight: bold; font-size: 16px;">{{ formattedMonthlyGrowth }}</span>-->
<!--          </div>-->
<!--        </div>-->




<!--        &lt;!&ndash; Bar Chart &ndash;&gt;-->
<!--        <div style="flex: 1; height: 300px;" id="barChart1" ref="barChart1"></div>-->

<!--      </div>-->

<!--      &lt;!&ndash; Bar Chart 2 (with Year Selector and additional stats) &ndash;&gt;-->
<!--      &lt;!&ndash; Bar Chart 2 (with Year Selector and additional stats) &ndash;&gt;-->
<!--      <div style="flex: 0 0 48%; height: 300px;">-->
<!--        <div id="barChart2" ref="barChart2" style="height: 250px;"></div>-->
<!--        &lt;!&ndash; Additional Stats &ndash;&gt;-->
<!--        <div style="display: flex; align-items: center; justify-content: flex-start; gap: 150px; margin-top: 10px; margin-bottom: 10px; padding: 10px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;">-->
<!--          <div style="display: flex; align-items: center;">-->
<!--            <span style="font-weight: bold; font-size: 16px;">当前问题验证未通过数：</span>-->
<!--            <span style="font-weight: bold; font-size: 16px;">{{ nopassissue }}</span>-->
<!--          </div>-->
<!--          <div style="display: flex; align-items: center;">-->
<!--            <span style="font-weight: bold; font-size: 16px;">通过率：</span>-->
<!--            <span style="font-weight: bold; font-size: 16px;">{{ passRate + '%' }}</span>-->
<!--          </div>-->
<!--        </div>-->

<!--      </div>-->

<!--      &lt;!&ndash; Bar Chart 3 &ndash;&gt;-->
<!--      <div style="flex: 0 0 48%; height: 300px;">-->
<!--        <div style="margin-bottom: 10px; text-align: center;">-->
<!--          &lt;!&ndash; 年份选择器 &ndash;&gt;-->
<!--          <el-select-->
<!--            v-model="selectedYearChart3"-->
<!--            placeholder="选择年份"-->
<!--            @change="onYearChangeChart3"-->
<!--            style="width: 150px; margin: 0 auto;"-->
<!--          >-->
<!--            <el-option-->
<!--              v-for="year in yearOptions"-->
<!--              :key="year"-->
<!--              :label="year"-->
<!--              :value="year"-->
<!--            ></el-option>-->
<!--          </el-select>-->
<!--        </div>-->
<!--        <div id="barChart3" ref="barChart3" style="height: 250px;"></div>-->
<!--      </div>-->

<!--      &lt;!&ndash; Bar Chart 4 &ndash;&gt;-->
<!--      <div style="flex: 0 0 48%; height: 300px;" id="barChart4" ref="barChart4">-->
<!--&lt;!&ndash;        <div style="text-align: center; margin-bottom: 10px; font-weight: bold;">&ndash;&gt;-->
<!--&lt;!&ndash;          图表 4 标题&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--      </div>-->
      <!-- Bar Chart 4 -->
      <!-- Bar Chart 4 -->
<!--      <div style="flex: 0 0 48%; height: 300px;">-->
<!--        <div style="margin-bottom: 10px; text-align: center;">-->
<!--          &lt;!&ndash; 月份选择器 &ndash;&gt;-->
<!--          <el-date-picker-->
<!--            v-model="selectedMonthChart4"-->
<!--            type="month"-->
<!--            placeholder="选择月份"-->
<!--            @change="onMonthChangeChart4"-->
<!--            style="width: 150px; margin: 0 auto;"-->
<!--          ></el-date-picker>-->
<!--        </div>-->
<!--        <div id="barChart4" ref="barChart4" style="height: 250px;"></div>-->
<!--      </div>-->

<!--      <div style="flex: 0 0 48%; height: 300px;">-->
<!--        <div id="barChar5" ref="barChar5" style="height: 250px;"></div>-->
<!--      </div>-->

<!--      <div style="flex: 0 0 48%; height: 300px;">-->
<!--        <div id="barChar6" ref="barChar6" style="height: 250px;"></div>-->
<!--      </div>-->

<!--      <div style="flex: 0 0 48%; height: 300px;">-->
<!--        <div id="barChar7" ref="barChar7" style="height: 250px;"></div>-->
<!--      </div>-->

<!--      <div style="flex: 0 0 48%; height: 300px;">-->
<!--        <div id="barChar8" ref="barChar8" style="height: 250px;"></div>-->
<!--      </div>-->




<!--    </div>-->



<!--  </div>-->
<!--</template>-->


<template>
  <div style="display: flex; flex-direction: column; gap: 20px;">

    <!-- 第一行：三块 -->
    <div style="display: flex; justify-content: space-between; gap: 20px; height: 200px;">
      <div style="flex: 1; height: 100%; border-radius: 8px; " id="chart1" ref="chart1">
        <!-- 图表 1 内容 -->
        <!-- Bar Chart 1 with adjacent stats -->
        <div style="flex: 0 0 48%; display: flex; gap: 20px;">

          <!-- Stats Section -->
          <div
            style="flex: 0 0 30%; display: flex; flex-direction: column; justify-content: space-around; padding: 10px; border-radius: 8px;"
          >
            <div style="display: flex; align-items: center; gap: 5px;">
              <span style="font-weight: bold; font-size: 16px;">当月总数：</span>
              <span style="font-weight: bold; font-size: 16px;">{{ totalCount || 0 }}</span>
            </div>
            <div style="display: flex; align-items: center; gap: 5px;">
              <span style="font-weight: bold; font-size: 16px;">同比：</span>
              <span style="font-weight: bold; font-size: 16px;">{{ formattedYearlyGrowth }}</span>
            </div>
            <div style="display: flex; align-items: center; gap: 5px;">
              <span style="font-weight: bold; font-size: 16px;">环比：</span>
              <span style="font-weight: bold; font-size: 16px;">{{ formattedMonthlyGrowth }}</span>
            </div>
          </div>
          <div style="flex: 1; height: 200px;" id="barChart1" ref="barChart1"></div>
        </div>
      </div>
      <div style="flex: 1; height: 100%; border-radius: 8px;" id="chart2" ref="chart2">
        <!-- 图表 2 内容 -->
        <div style="flex: 0 0 48%; height: 200px;">
          <div id="barChart2" ref="barChart2" style="height: 150px;"></div>
          <!-- Additional Stats -->
          <div style="display: flex; align-items: center; justify-content: flex-start; gap: 100px; margin-top: 5px; padding: 5px; border-radius: 8px; font-size: 14px;">
            <div style="display: flex; align-items: center;">
              <span style="font-weight: bold;">当前问题验证未通过数：</span>
              <span style="font-weight: bold;">{{ nopassissue }}</span>
            </div>
            <div style="display: flex; align-items: center;">
              <span style="font-weight: bold;">通过率：</span>
              <span style="font-weight: bold;">{{ passRate + '%' }}</span>
            </div>
          </div>
        </div>


      </div>
      <div style="flex: 1; height: 100%; border-radius: 8px;" id="chart3" ref="chart3">
        <!-- 图表 3 内容 -->
        <div style="display: flex; flex-direction: column; gap: 20px; height: 200px; padding: 10px;">
          <!-- 第一行 -->
          <div style="display: flex; justify-content: space-between; font-size: 18px; gap: 20px;">
            <div>
              <span style="font-weight: bold;">超期问题数：</span>
              <span>{{ ThirdstateData['超期总数'] }}</span>
            </div>
            <div>
              <span style="font-weight: bold;">累计超期时间：</span>
              <span>{{ ThirdstateData['累计超期时间'] + "天"  }}</span>
            </div>
          </div>

          <!-- 第二行 -->
          <div style="display: flex; justify-content: space-between; font-size: 18px; gap: 20px;">
            <div>
              <span style="font-weight: bold;">措施完成率：</span>
              <span>{{ rectificationRate + '%' }}</span>
            </div>
            <div>
              <span style="font-weight: bold;">超期率：</span>
              <span>{{ overdueRate + '%' }}</span>
            </div>
          </div>

          <!-- 第三行 -->
          <div style="display: flex; justify-content: space-between; font-size: 18px; gap: 20px;">
            <div>
              <span style="font-weight: bold;">验证完成率：</span>
              <span>{{ verifyRate + '%' }}</span>
            </div>
            <div>
              <span style="font-weight: bold;">超期率：</span>
              <span>{{ overdueRate + '%' }}</span>
            </div>
          </div>
        </div>



      </div>
    </div>

    <!-- 第二行：三块 -->
    <div style="display: flex; justify-content: space-between; gap: 20px; height: 300px;">
      <div style="flex: 1; height: 100%; border-radius: 8px; " id="chart4" ref="chart4">
        <!-- 图表 4 内容 -->
        <div style="flex: 0 0 48%; height: 300px;">
          <div style="margin-bottom: 10px; text-align: center;">
            <!-- 年份选择器 -->
            <el-select
              v-model="selectedYearChart3"
              placeholder="选择年份"
              @change="onYearChangeChart3"
              style="width: 150px; margin: 0 auto;"
            >
              <el-option
                v-for="year in yearOptions"
                :key="year"
                :label="year"
                :value="year"
              ></el-option>
            </el-select>
          </div>
          <div id="barChart3" ref="barChart3" style="height: 280px;"></div>
        </div>

      </div>
      <div style="flex: 1; height: 100%; border-radius: 8px;" id="chart5" ref="chart5">
        <!-- 图表 5 内容 -->
        <div style="flex: 0 0 48%; height: 300px;">
          <div style="margin-bottom: 10px; text-align: center;">
            <!-- 月份选择器 -->
            <el-date-picker
              v-model="selectedMonthChart4"
              type="month"
              placeholder="选择月份"
              @change="onMonthChangeChart4"
              style="width: 150px; margin: 0 auto;"
            ></el-date-picker>
          </div>
          <div id="barChart4" ref="barChart4" style="height: 250px;"></div>
        </div>
      </div>
      <div style="flex: 1; height: 100%;  border-radius: 8px; " id="chart6" ref="chart6">
        <!-- 图表 6 内容 -->
        <div id="barChart6" ref="barChart6" style="height: 300px; margin-top: 20px;"></div>
      </div>
    </div>

    <!-- 第三行：四块 -->
    <div style="display: flex; justify-content: space-between; gap: 20px; height: 200px;">
      <div style="flex: 1; height: 100%;" id="chart7" ref="chart7">
        <!-- 图表 7 内容 -->
      </div>
      <div style="flex: 1; height: 100%; border-radius: 8px;" id="chart8" ref="chart8">
        <!-- 图表 8 内容 -->
      </div>
      <div style="flex: 1; height: 100%;  border-radius: 8px;" id="chart9" ref="chart9">
        <!-- 图表 9 内容 -->
      </div>
      <div style="flex: 1; height: 100%; border-radius: 8px;" id="chart10" ref="chart10">
        <!-- 图表 10 内容 -->
      </div>
    </div>

  </div>
</template>


<script>
import * as echarts from 'echarts';

export default {
  data() {
    return {
      totalCount: 0, // 当月总数
      allnumber: 0, // 数据库 issue 总数
      formattedMonthlyGrowth: '0.00%', // 初始化格式化后的变量
      formattedYearlyGrowth: '0.00%', // 初始化格式化后的变量
      //图一
      issueStats: {  持续: 0, 结项: 0, 关闭: 0, 创建: 0 }, // 初始状态
      yearOverYearChange: null, // 初始化为null
      //图二
      selectedYear: new Date().getFullYear(), // 默认当前年份
      yearOptions: [], // 存储动态生成的年份列表 // 可选择的年份列表
      monthlyData: [], // 每个月的问题数
      months: [], // 月份数组（1 到 12）
      monthlyGrowth: null, // 月同比增长问题数

      nopassissue: 0, // 未通过问题数
      passissue: 0, // 通过问题数
      stateData: { 问题添加: 0, 问题整改: 0, 问题验证: 0,验证已通过: 0, 验证未通过:0 }, // 问题状态数据
      passRate: 0, // 通过率
      //图三
      selectedYearChart3: new Date().getFullYear(),  // 当前选择的年份（Bar Chart 3）
      monthlyDuplicateData: [], // 存储月度重复问题数的数据（Bar Chart 3）
      duplicateIssueTotal: 0, // 重复问题总数
      duplicateIssuePass: 0, // 重复问题占比

      //第三部分
      ThirdstateData: { 超期总数: 0, 累计超期时间: 0, 等待整改记录填写数量: 0,总数据条数: 0, 等待验证数量:0, 验证未通过数量: 0, 结项数量:0 }, // 问题状态数据
      rectificationRate:0, // 整改率
      verifyRate:0, // 验证率
      overdueRate:0, // 超期率
      anotherOverdueRate:0, // 另一种超期率


      // 图四
      issueCategoryStats: {}, // 当前月进行中问题的类别数据
      selectedMonthChart4: new Date().toISOString().slice(0, 7), // 默认是当前月份

      //图六
      departmentStats: {},

      //图七
      indemnificationTypeStats: {},
      indemnificationOptions: [], // 赔偿件选项

      //图八
      vendorTypeStats: {},

      //图九
      carModelStats: {},

      //图十
      regiontype: {},

    };
  },
  async created(){
    // 获取赔偿件数据
    this.$http({
      url: this.$http.adornUrl(`/generator/indemnificationtable/list1`),
      method: 'get',
    }).then(({data}) => {
      this.indemnificationOptions = data.result.map(item => ({
        id: item.indeId,
        name: item.indemnification,
      }));
      console.log("peichangjian", this.indemnificationOptions);
    });
  },

  mounted() {
    // 初始化图表
    this.initCharts();
    // 获取数据
    this.getIssueStats();
    this.getallIssueStats()
    this.getMonthlyIssueStats(this.selectedYear); // 默认加载当前年份的数据
    this.generateYearOptions();  // 动态生成年份列表

    this.getMonthlyDuplicateStats(this.selectedYearChart3);  // 获取初始数据（Bar Chart 3）

    this.getThirdIssue(); // 获取第三部分数据

    this.getInProgressIssueCategoryStats();  // 获取当前月进行中问题的类别数据（Bar Chart 4）

    this.getIssuebyDepartment();

    this.getbyindemnification();

    this.getbyvendor();

    this.getbyvehicletype();

    this.getbyregiontype();
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
          this.issueStats = {  持续: 0, 结项: 0,创建: 0 ,关闭: 0};
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
      this.totalCount = totalCount;
      // console.log('总数0', this.totalCount);
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
            type: 'bar',
            label: {
              show: true,
              position: 'top'
            }
          }
        ]
      };

      chart1.setOption(options);
    },


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
    // getMonthlyIssueStats(year) {
    //   this.$http({
    //     url: this.$http.adornUrl(`/generator/issuetable/monthlyStats`),
    //     method: 'get',
    //     params: { year },
    //   }).then(({ data }) => {
    //     if (data && data.code === 0) {
    //       this.monthlyData = data.stats; // 假设返回的数据格式 { 1: 10, 2: 12, ..., 12: 5 }
    //       // 计算月同比增长
    //
    //       this.calculateMonthGrowth();
    //       this.renderMonthlyChart(); // 渲染图表
    //     } else {
    //       // 默认值（如果没有数据）
    //       this.monthlyData = Array(12).fill(0);
    //       this.renderMonthlyChart();
    //     }
    //   });
    // },
    // 向后端请求某个年份的月度问题数
    getMonthlyIssueStats(year) {
      // 请求当前年份数据
      this.$http({
        url: this.$http.adornUrl(`/generator/issuetable/monthlyStats`),
        method: 'get',
        params: { year },
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.monthlyData = data.stats; // 当前年份数据 { 1: 10, 2: 12, ..., 12: 5 }

          // 请求前一年的数据
          const previousYear = year - 1;
          this.$http({
            url: this.$http.adornUrl(`/generator/issuetable/monthlyStats`),
            method: 'get',
            params: { year: previousYear },
          }).then(({ data: prevData }) => {
            if (prevData && prevData.code === 0) {
              this.previousMonthlyData = prevData.stats; // 前一年数据 { 1: 8, 2: 10, ..., 12: 4 }
            } else {
              // 如果前一年没有数据，默认填充 0
              this.previousMonthlyData = Array(12).fill(0);
            }

            // 计算月环比增长
            this.calculateMonthGrowth();
            // 计算年同比变化
            this.calculateYearOverYearChange();

            // 渲染图表
            this.renderMonthlyChart();
          });
        } else {
          // 如果没有当前年的数据，默认填充 0
          this.monthlyData = Array(12).fill(0);
          this.previousMonthlyData = Array(12).fill(0);
          this.calculateYearOverYearChange();
          this.calculateMonthGrowth();
          this.renderMonthlyChart();
        }
      });
    },

    // 计算年同比变化
    calculateYearOverYearChange() {
      this.yearOverYearChange = Array(12).fill(null); // 初始化同比变化数组

      for (let month = 1; month <= 12; month++) {
        const currentMonthCount = this.monthlyData[month] || 0; // 当前年的数据
        const previousMonthCount = this.previousMonthlyData[month] || 0; // 前一年的数据

        if (previousMonthCount === 0) {
          this.yearOverYearChange[month - 1] = null; // 若前一年数据为0，无法计算同比
        } else {
          const growth = (((currentMonthCount - previousMonthCount) / previousMonthCount) * 100).toFixed(2); // 计算同比增长百分比
          this.formattedYearlyGrowth = growth + '%';
          this.yearOverYearChange[month - 1] = growth;
        }
      }

      // 输出计算后的同比变化
      // console.log("年同比变化:", this.yearOverYearChange);
    },


    // 计算月环比增长
    calculateMonthGrowth() {
      const currentMonth = new Date().getMonth() + 1; // 当前月
      const previousMonth = currentMonth === 1 ? 12 : currentMonth - 1; // 上个月

      const currentMonthCount = this.monthlyData[currentMonth] || 0;
      const previousMonthCount = this.monthlyData[previousMonth] || 0;
      // console.log("当月问题数:", currentMonthCount);
      // console.log("上月问题数:", previousMonthCount);
      if (previousMonthCount !== 0) {
        this.monthlyGrowth = ((currentMonthCount - previousMonthCount) / previousMonthCount) * 100;
        this.formattedMonthlyGrowth = this.monthlyGrowth.toFixed(2) + '%'
        // console.log("当月环比增长:", this.formattedMonthlyGrowth);
        // // 手动更新视图
        // this.$forceUpdate();
      } else {
        this.monthlyGrowth = 0; // 如果上个月的问题数为0，则同比增长为0
      }
    },
    //图二
    // 获取所有问题状态数据
    getallIssueStats() {
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/truecurrentall'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.stateData = data.stats; // 假设返回的数据格式为 { 提出: 10, 暂停: 12, ... }
          // console.log('数据转换中......', this.stateData);
          this.nopassissue = this.stateData['验证未通过']; // 未通过问题数
          this.passissue = this.stateData['验证已通过']; // 通过问题数
          this.passRate = (this.passissue / (this.passissue + this.nopassissue) * 100).toFixed(2); // 通过率
          this.renderIssueChart(); // 渲染图表
        } else {
          // 默认值
          this.stateData = { 问题添加: 0, 问题整改: 0, 问题验证: 0,问题处理完成:0 };
        }
      });
    },
    // 渲染月度问题数的柱状图
    renderMonthlyChart() {
      const chart2 = echarts.init(this.$refs.barChart2);

      const options = {
        title: {
          text: `持续问题进度`,
          textStyle: {
            fontSize: 14 // 缩小标题字体
          }
        },
        tooltip: {},
        grid: {
          top: '12%',   // 上边距
          left: '12%',  // 左边距
          right: '15%', // 右边距
          bottom: '15%' // 下边距
        },
        xAxis: {
          type: 'value',
          splitLine: { show: false }, // 隐藏X轴的网格线
          axisLabel: {
            fontSize: 10 // 缩小X轴字体大小
          }
        },
        yAxis: {
          type: 'category',
          data: ['问题验证', '问题整改', '问题添加'], // Y轴类别
          axisLabel: {
            fontSize: 10 // 缩小Y轴字体大小
          }
        },
        series: [
          {
            data: [
              this.stateData['问题验证'],
              this.stateData['问题整改'],
              this.stateData['问题添加'],
            ],
            type: 'bar',
            barWidth: '50%' // 缩小柱状图宽度
          }
        ]
      };

      chart2.setOption(options);
    }
,
    //第三部分数据
    // 获取所有问题状态数据
    getThirdIssue() {
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/getThirdIssue'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.ThirdstateData = data.stats; // 假设返回的数据格式为 { 提出: 10, 暂停: 12, ... }
          console.log('数据转换中......', this.ThirdstateData);
          const issueTotal = this.ThirdstateData['总数据条数'];
          this.rectificationRate = ((issueTotal - this.ThirdstateData['等待整改记录填写数量'] ) / issueTotal * 100).toFixed(2); // 通过率
          this.verifyRate = ((this.ThirdstateData['验证未通过数量'] + this.ThirdstateData['结项数量'] ) / (this.ThirdstateData['验证未通过数量'] + this.ThirdstateData['结项数量'] + this.ThirdstateData['等待验证数量']) * 100).toFixed(2);
          this.overdueRate = (this.ThirdstateData['超期总数'] / this.ThirdstateData['总数据条数'] * 100).toFixed(2);
        } else {
          // 默认值
          // this.stateData = { 问题添加: 0, 问题整改: 0, 问题验证: 0,问题处理完成:0 };
        }
      });
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

          this.duplicateIssueTotal = Number(this.duplicateIssueTotal);
          // console.log('总数1:', this.duplicateIssueTotal);
          // const totalCount = this.issueStats['暂缓'] + this.issueStats['持续'] + this.issueStats['结项'];
          this.totalCount = data.monthallissue;
          console.log('总数2:', this.totalCount);
          // console.log('总数2类型', typeof this.totalCount);

          if (!isNaN(this.duplicateIssueTotal) && typeof this.totalCount === 'number' && this.totalCount > 0) {
            this.duplicateIssuePass = (this.duplicateIssueTotal / this.totalCount * 100).toFixed(2);
            // console.log('重复问题占比:', this.duplicateIssuePass);
          } else {
            this.duplicateIssuePass = 0; // 或者设置为0等其他合适的默认值
            // console.warn('Invalid data for calculating duplicate issue pass rate');
          }

          // console.log('重复问题总数:', this.duplicateIssueTotal);
          // console.log('重复问题数据:', this.totalCount);
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
          text: `重复问题总数: ${this.duplicateIssueTotal}    重复问题占比: ${this.duplicateIssuePass}%`
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
    // getInProgressIssueCategoryStats() {
    //   this.$http({
    //     url: this.$http.adornUrl('/generator/issuetable/currentMonthInProgressCategoryStats'),
    //     method: 'get',
    //     params: this.$http.adornParams({})
    //   }).then(({ data }) => {
    //     console.log('返回的数据：', data); // 调试输出
    //     if (data && data.code === 0) {
    //       this.issueCategoryStats = data.stats;  // 确保 data.stats 格式正确
    //       console.log('处理后的统计数据：', this.issueCategoryStats); // 调试输出
    //       this.renderIssueCategoryChart();  // 渲染图表
    //     } else {
    //       // 默认值
    //       this.issueCategoryStats = {};
    //       this.renderIssueCategoryChart();
    //     }
    //   });
    // },
    // getInProgressIssueCategoryStats() {
    //   const startDate = `${this.selectedMonthChart4}-01`;
    //   const endDate = `${this.selectedMonthChart4}-${new Date(this.selectedMonthChart4.split('-')[0], this.selectedMonthChart4.split('-')[1], 0).getDate()}`;
    //   console.log('日期范围：', startDate, endDate);
    //   this.$http({
    //     url: this.$http.adornUrl('/generator/issuetable/currentMonthInProgressCategoryStats'),
    //     method: 'get',
    //     params: this.$http.adornParams({startDate, endDate})
    //   }).then(({ data }) => {
    //     // console.log('返回的数据：', data); // 调试输出
    //     if (data && data.code === 0) {
    //       // 排序统计数据，只显示数量最多的十种问题类别
    //       const sortedStats = Object.entries(data.stats)
    //         .sort(([, countA], [, countB]) => countB - countA)
    //         .slice(0, 10);
    //       this.issueCategoryStats = Object.fromEntries(sortedStats);  // 处理后的统计数据
    //       // console.log('处理后的统计数据：', this.issueCategoryStats); // 调试输出
    //       this.renderIssueCategoryChart();  // 渲染图表
    //     } else {
    //       // 默认值
    //       this.issueCategoryStats = {};
    //       this.renderIssueCategoryChart();
    //     }
    //   });
    // },
    // 图4获取数据
    getInProgressIssueCategoryStats() {
      const startDate = `${this.selectedMonthChart4}-01`;
      const endDate = `${this.selectedMonthChart4}-${new Date(this.selectedMonthChart4.split('-')[0], this.selectedMonthChart4.split('-')[1], 0).getDate()}`;

      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/currentMonthInProgressCategoryStats'),
        method: 'get',
        params: this.$http.adornParams({ startDate, endDate })  // 传递开始和结束日期
      }).then(({ data }) => {
        if (data && data.code === 0) {
          const sortedStats = Object.entries(data.stats)
            .sort(([, countA], [, countB]) => countB - countA)
            .slice(0, 10);
          this.issueCategoryStats = Object.fromEntries(sortedStats);  // 处理后的统计数据
          // console.log('处理后的统计数据：', this.issueCategoryStats); // 调试输出
          this.renderIssueCategoryChart();  // 渲染图表
        } else {
          this.issueCategoryStats = {};  // 默认值
          this.renderIssueCategoryChart();  // 渲染空图表
        }
      });
    },


    // 渲染图四的柱状图
    // renderIssueCategoryChart() {
    //   const chart4 = echarts.init(this.$refs.barChart4);
    //
    //   // 获取问题类别和问题数
    //   const categories = Object.keys(this.issueCategoryStats);  // 问题类别
    //   const issueCounts = categories.map(category => this.issueCategoryStats[category]);  // 对应问题数
    //
    //   // 图表配置项
    //   const options = {
    //     title: {
    //       text: '当月问题类别统计'
    //     },
    //     tooltip: {
    //       trigger: 'axis',
    //     },
    //     xAxis: {
    //       type: 'category',
    //       data: categories,  // 问题类别
    //     },
    //     yAxis: {
    //       type: 'value',
    //     },
    //     series: [
    //       {
    //         data: issueCounts,  // 问题数
    //         type: 'bar',  // 设置为柱状图
    //       }
    //     ]
    //   };
    //
    //   chart4.setOption(options);  // 渲染图表
    // },
//     renderIssueCategoryChart() {
//       const chart4 = echarts.init(this.$refs.barChart4);
//
//       // 获取问题类别和问题数
//       const categories = Object.keys(this.issueCategoryStats).filter(category => this.issueCategoryStats[category] > 0);  // 过滤无效问题类别
//       const issueCounts = categories.map(category => this.issueCategoryStats[category]);  // 对应问题数
//
//       // 图表配置项
//       const options = {
//         title: {
//           text: '前十问题展示'
//         },
//         tooltip: {
//           trigger: 'axis',
//         },
//         xAxis: {
//           type: 'value',
//           name: '数量',  // Y轴显示数量
//           splitLine: { show: false },  // 隐藏Y轴的网格线
//         },
//         yAxis: {
//           type: 'category',
//           data: categories,  // 问题类别
//         },
//         series: [
//           {
//             data: issueCounts,  // 问题数
//             type: 'bar',  // 设置为柱状图
//           }
//         ]
//       };
//
//       chart4.setOption(options);  // 渲染图表
//     }
// ,
    // 处理月份选择变化
    onMonthChangeChart4(value) {
      // 如果 value 是 Date 对象，转换为 'YYYY-MM' 格式的字符串
      if (value instanceof Date) {
        const year = value.getFullYear();
        const month = (value.getMonth() + 1).toString().padStart(2, '0');  // 获取月份，确保是两位数
        this.selectedMonthChart4 = `${year}-${month}`;  // 格式化为 'YYYY-MM'
      } else {
        // 如果是字符串，则直接使用它
        this.selectedMonthChart4 = value;
      }
      // console.log("Selected month:", this.selectedMonthChart4);  // 确认输出正确的日期格式
      this.getInProgressIssueCategoryStats();  // 根据选择的月份重新获取数据
    },
    renderIssueCategoryChart() {
      const chart4 = echarts.init(this.$refs.barChart4);

      // 确保数据存在且有效
      // if (!this.issueCategoryStats || Object.keys(this.issueCategoryStats).length === 0) {
      //   // console.warn('No issue category data available');
      //   return;
      // }

      // 获取问题类别和问题数
      const categories = Object.keys(this.issueCategoryStats).filter(category => this.issueCategoryStats[category] > 0);  // 过滤无效问题类别
      let issueCounts = categories.map(category => this.issueCategoryStats[category]);  // 对应问题数

      // 对数据进行从小到大的排序
      const sortedData = categories
        .map((category, index) => ({
          category,
          count: issueCounts[index]
        }))
        .sort((a, b) => a.count - b.count);  // 按照问题数从小到大排序

      // 排序后的类别和问题数
      const sortedCategories = sortedData.map(item => item.category);
      const sortedIssueCounts = sortedData.map(item => item.count);

      // 图表配置项
      const options = {
        title: {
          text: '前十问题展示',
          left: 'center',
          top: '10',  // 设置标题的位置
          textStyle: {
            fontSize: 18,  // 设置标题的字体大小
            fontWeight: 'bold',
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',  // 阴影指示器
          },
          backgroundColor: 'rgba(50, 50, 50, 0.7)',  // 提示框背景色
          textStyle: {
            color: '#fff',  // 提示框文字颜色
          }
        },
        xAxis: {
          type: 'value',
          name: '数量',  // X轴显示“数量”
          splitLine: { show: false },  // 隐藏X轴的网格线
          axisLabel: {
            formatter: '{value}',  // X轴数字格式化
          }
        },
        yAxis: {
          type: 'category',
          data: sortedCategories,  // 排序后的问题类别
          axisLabel: {
            formatter: (value) => {
              // 根据需要调整Y轴标签的显示格式
              return value.length > 10 ? value.slice(0, 10) + '...' : value;
            },
          }
        },
        series: [
          {
            data: sortedIssueCounts,  // 排序后的问题数
            type: 'bar',  // 设置为柱状图
            itemStyle: {
              color: '#4caf50',  // 设置柱状图颜色
            },
            barWidth: '40%',  // 设置柱状图的宽度
            label: {
              show: true,  // 显示柱子上的数据
              position: 'insideRight',  // 数据显示在柱子的右侧
              valueAnimation: true,  // 启动数据动画
            }
          }
        ],
        grid: {
          left: '10%',
          right: '10%',
          bottom: '10%',
          top: '20%',
        },
        animationDuration: 1000,  // 动画时长
      };

      // 渲染图表
      chart4.setOption(options);

      // 适配窗口尺寸变化
      window.addEventListener('resize', () => {
        chart4.resize();
      });
    },
    //图六
    // 获取各个科室问题数量及完成率
    // 获取数据
    getIssuebyDepartment() {
      const startDate = `${this.selectedMonthChart4}-01`;
      const endDate = `${this.selectedMonthChart4}-${new Date(this.selectedMonthChart4.split('-')[0], this.selectedMonthChart4.split('-')[1], 0).getDate()}`;

      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/getIssuebyDepartment'),
        method: 'get',
        params: this.$http.adornParams({ startDate, endDate })  // 传递开始和结束日期
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // console.log("fanhuishuju" ,data.stats)
          // 处理数据，准备用于渲染图表
          const departments = Object.keys(data.stats); // 科室名称
          const totalCounts = departments.map((dept) => data.stats[dept].total); // 问题总数
          const completedCounts = departments.map((dept) => data.stats[dept].completed); // 完成总数
          const completionRates = departments.map(
            (dept) =>
              Math.round((data.stats[dept].completed / data.stats[dept].total || 0) * 100) // 计算完成率，保留整数
          );

          // 保存到组件的状态中
          this.departmentStats = {
            departments,
            totalCounts,
            completedCounts,
            completionRates,
          };

          this.renderDepartmentChart(); // 渲染图表
        } else {
          // 数据为空的情况
          this.departmentStats = {
            departments: [],
            totalCounts: [],
            completedCounts: [],
            completionRates: [],
          };
          this.renderDepartmentChart(); // 渲染空图表
        }
      });
    },
    // 渲染科室完成情况图表
    renderDepartmentChart() {
      const { departments, totalCounts, completedCounts, completionRates } = this.departmentStats;

      const chart = this.$echarts.init(document.getElementById('barChart6')); // 图表容器的 DOM 元素 ID

      const option = {
        title: {
          text: '各科室问题完成情况',
          left: 'center',
          // top: '10',  // 设置标题的位置
          textStyle: {
            fontSize: 18,  // 设置标题的字体大小
            fontWeight: 'bold',
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow', // 显示阴影指示器
          },
        },
        legend: {
          data: ['问题总数', '完成总数', '完成率'],
          top: '20',
        },
        xAxis: {
          type: 'category',
          data: departments, // x轴为科室
          axisLabel: {
            interval: 0,
            rotate: 10, // 避免文字过长重叠
          },
        },
        yAxis: [
          {
            type: 'value',
            name: '数量', // 对应柱状图
          },
          {
            type: 'value',
            name: '完成率 (%)', // 对应折线图
            axisLabel: {
              formatter: '{value}%',
            },
          },
        ],
        series: [
          {
            name: '问题总数',
            type: 'bar',
            data: totalCounts, // 各科室的问题总数
            barGap: '0%', // 紧贴完成数柱
            barWidth: '40%', // 控制柱的宽度
            itemStyle: {
              color: '#73C9E6',
            },
          },
          {
            name: '完成总数',
            type: 'bar',
            barWidth: '40%', // 控制柱的宽度
            data: completedCounts, // 各科室的完成总数
            itemStyle: {
              color: '#5470C6',
            },
          },
          {
            name: '完成率',
            type: 'line',
            yAxisIndex: 1, // 使用第二个 Y 轴
            data: completionRates, // 各科室的完成率
            itemStyle: {
              color: '#91CC75',
            },
            lineStyle: {
              width: 2,
            },
          },
        ],
      };

      // 设置图表配置项
      chart.setOption(option);
    },

    getindemnificationById(auditorId) {
      // console.log("peichangjian", this.indemnificationOptions);
      for (const category of this.indemnificationOptions) {
        if (category.id == auditorId) {
          return category.name;
        }
      }
      return "-";
    },
    //图7数据
    getbyindemnification() {
      const startDate = `${this.selectedMonthChart4}-01`;
      const endDate = `${this.selectedMonthChart4}-${new Date(this.selectedMonthChart4.split('-')[0], this.selectedMonthChart4.split('-')[1], 0).getDate()}`;

      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/getbyindemnification'),
        method: 'get',
        params: this.$http.adornParams({ startDate, endDate })  // 传递开始和结束日期
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // 对数据按数量排序，并取前 10 项
          // 对数据按数量（count）降序排序，并筛选前 10
          const sortedStats = Object.entries(data.stats)
            .map(([name, stats]) => ({
              name: this.getindemnificationById(name),
              count: stats.count,
              totalCost: stats.totalCost
            }))
            .sort((a, b) => b.count - a.count)
            .slice(0, 10);

          // 准备用于渲染图表的数据
          const indemnificationNames = sortedStats.map(item => item.name); // 赔偿件名称
          const totalCounts = sortedStats.map(item => item.count); // 赔偿件数量
          const totalCosts = sortedStats.map(item => item.totalCost); // 赔偿件成本总和

          // 保存到组件的状态中
          this.indemnificationTypeStats = {
            indemnificationNames,
            totalCounts,
            totalCosts,
          };

          // console.log("fanhuishuju1", sortedStats);
          // console.log("fanhuishuju2", this.indemnificationTypeStats);

          this.renderIndemnificationChart(); // 渲染图表
        } else {
          this.indemnificationTypeStats = {};  // 默认值
          this.renderIndemnificationChart(); // 渲染空图表
        }
      });
    },

    renderIndemnificationChart() {
      const { indemnificationNames, totalCosts, totalCounts } = this.indemnificationTypeStats;

      const chart = this.$echarts.init(document.getElementById('chart7')); // 图表容器的 DOM 元素 ID

      const option = {
        title: {
          text: '前十赔偿件统计',
          left: 'center',
          textStyle: {
            fontSize: 18,  // 设置标题的字体大小
            fontWeight: 'bold',
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow', // 显示阴影指示器
          },
        },
        legend: {
          data: ['成本总和', '问题数量'],
          top: '20',
        },
        xAxis: {
          type: 'category',
          data: indemnificationNames, // x轴为赔偿件名称
          axisLabel: {
            interval: 0,
            rotate: 10, // 避免文字过长重叠
          },
        },
        yAxis: [
          {
            type: 'value',
            name: '成本总和', // 对应柱状图
          },
          {
            type: 'value',
            name: '问题数量', // 对应折线图
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: '成本总和（元）',
            type: 'bar',
            data: totalCosts, // 各赔偿件的成本总和
            itemStyle: {
              color: '#73C9E6',
            },
          },
          {
            name: '赔偿件数量',
            type: 'line',
            yAxisIndex: 1, // 使用第二个 Y 轴
            data: totalCounts, // 各赔偿件的发生次数
            itemStyle: {
              color: '#91CC75',
            },
            lineStyle: {
              width: 2,
            },
          },
        ],
        grid: {
          left: '20%', // 设置左边距
          right: '15%', // 设置右边距
        }
      };

      // 设置图表配置项
      chart.setOption(option);
    },

    //图8数据
    getbyvendor() {
      const startDate = `${this.selectedMonthChart4}-01`;
      const endDate = `${this.selectedMonthChart4}-${new Date(this.selectedMonthChart4.split('-')[0], this.selectedMonthChart4.split('-')[1], 0).getDate()}`;

      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/getbyvendor'),
        method: 'get',
        params: this.$http.adornParams({ startDate, endDate })  // 传递开始和结束日期
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // 对数据按数量排序，并取前 10 项
          // 对数据按数量（count）降序排序，并筛选前 10
          const sortedStats = Object.entries(data.stats)
            .map(([name, stats]) => ({
              name,
              count: stats.count,
              totalCost: stats.totalCost
            }))
            .sort((a, b) => b.count - a.count)
            .slice(0, 10);

          // 准备用于渲染图表的数据
          const indemnificationNames = sortedStats.map(item => item.name); // 供应商名称
          const totalCounts = sortedStats.map(item => item.count); // 供应商数量
          const totalCosts = sortedStats.map(item => item.totalCost); // 供应商成本总和

          // 保存到组件的状态中
          this.vendorTypeStats = {
            indemnificationNames,
            totalCounts,
            totalCosts,
          };

          // console.log("fanhuishuju1", sortedStats);
          // console.log("fanhuishuju2", this.indemnificationTypeStats);

          this.rendervendorChart(); // 渲染图表
        } else {
          this.vendorTypeStats = {};  // 默认值
          this.rendervendorChart(); // 渲染空图表
        }
      });
    },

    rendervendorChart() {
      const { indemnificationNames, totalCosts, totalCounts } = this.vendorTypeStats;

      const chart = this.$echarts.init(document.getElementById('chart8')); // 图表容器的 DOM 元素 ID

      const option = {
        title: {
          text: '前十供应商统计',
          left: 'center',
          textStyle: {
            fontSize: 18,  // 设置标题的字体大小
            fontWeight: 'bold',
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow', // 显示阴影指示器
          },
        },
        legend: {
          data: ['成本总和', '问题数量'],
          top: '20',
        },
        xAxis: {
          type: 'category',
          data: indemnificationNames, // x轴为赔偿件名称
          axisLabel: {
            interval: 0,
            rotate: 10, // 避免文字过长重叠
          },
        },
        yAxis: [
          {
            type: 'value',
            name: '成本总和', // 对应柱状图
          },
          {
            type: 'value',
            name: '问题数量', // 对应折线图
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: '成本总和（元）',
            type: 'bar',
            data: totalCosts, // 各赔偿件的成本总和
            itemStyle: {
              color: '#73C9E6',
            },
          },
          {
            name: '赔偿件数量',
            type: 'line',
            yAxisIndex: 1, // 使用第二个 Y 轴
            data: totalCounts, // 各赔偿件的发生次数
            itemStyle: {
              color: '#91CC75',
            },
            lineStyle: {
              width: 2,
            },
          },
        ],
        grid: {
          left: '20%', // 设置左边距
          right: '15%', // 设置右边距
        }
      };

      // 设置图表配置项
      chart.setOption(option);
    },

    //图九数据
    getbyvehicletype() {
      const startDate = `${this.selectedMonthChart4}-01`;
      const endDate = `${this.selectedMonthChart4}-${new Date(this.selectedMonthChart4.split('-')[0], this.selectedMonthChart4.split('-')[1], 0).getDate()}`;

      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/getbyvehicletype'),
        method: 'get',
        params: this.$http.adornParams({ startDate, endDate })  // 传递开始和结束日期
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // 对数据按数量排序，并取前 10 项
          // 对数据按数量（count）降序排序，并筛选前 10
          const sortedStats = Object.entries(data.stats)
            .map(([name, stats]) => ({
              name,
              count: stats.count,
              totalCost: stats.totalCost
            }))
            .sort((a, b) => b.count - a.count)
            .slice(0, 10);

          // 准备用于渲染图表的数据
          const indemnificationNames = sortedStats.map(item => item.name); // 供应商名称
          const totalCounts = sortedStats.map(item => item.count); // 供应商数量
          const totalCosts = sortedStats.map(item => item.totalCost); // 供应商成本总和

          // 保存到组件的状态中
          this.carModelStats = {
            indemnificationNames,
            totalCounts,
            totalCosts,
          };

          // 渲染图表
          this.renderCarModelChart();
        } else {
          this.carModelStats = {};
          this.renderCarModelChart();  // 渲染空图表
        }
      });
    },

    renderCarModelChart() {
      const { indemnificationNames, totalCosts, totalCounts } = this.carModelStats;

      const chart = this.$echarts.init(document.getElementById('chart9')); // 图表容器的 DOM 元素 ID

      const option = {
        title: {
          text: '前十车型统计',
          left: 'center',
          textStyle: {
            fontSize: 18,  // 设置标题的字体大小
            fontWeight: 'bold',
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow', // 显示阴影指示器
          },
        },
        legend: {
          data: ['成本总和', '问题数量'],
          top: '20',
        },
        xAxis: {
          type: 'category',
          data: indemnificationNames, // x轴为赔偿件名称
          axisLabel: {
            interval: 0,
            rotate: 10, // 避免文字过长重叠
          },
        },
        yAxis: [
          {
            type: 'value',
            name: '成本总和', // 对应柱状图
          },
          {
            type: 'value',
            name: '问题数量', // 对应折线图
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: '成本总和（元）',
            type: 'bar',
            data: totalCosts, // 各赔偿件的成本总和
            itemStyle: {
              color: '#73C9E6',
            },
          },
          {
            name: '赔偿件数量',
            type: 'line',
            yAxisIndex: 1, // 使用第二个 Y 轴
            data: totalCounts, // 各赔偿件的发生次数
            itemStyle: {
              color: '#91CC75',
            },
            lineStyle: {
              width: 2,
            },
          },
        ],
        grid: {
          left: '20%', // 设置左边距
          right: '15%', // 设置右边距
        }
      };

      // 设置图表配置项
      chart.setOption(option);
    },

    //图十数据
    getbyregiontype() {
      const startDate = `${this.selectedMonthChart4}-01`;
      const endDate = `${this.selectedMonthChart4}-${new Date(this.selectedMonthChart4.split('-')[0], this.selectedMonthChart4.split('-')[1], 0).getDate()}`;

      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/getbyregiontype'),
        method: 'get',
        params: this.$http.adornParams({ startDate, endDate })  // 传递开始和结束日期
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // 对数据按数量排序，并取前 10 项
          // 对数据按数量（count）降序排序，并筛选前 10
          const sortedStats = Object.entries(data.stats)
            .map(([name, stats]) => ({
              name,
              count: stats.count,
              totalCost: stats.totalCost
            }))
            .sort((a, b) => b.count - a.count)
            .slice(0, 10);

          // 准备用于渲染图表的数据
          const indemnificationNames = sortedStats.map(item => item.name); // 供应商名称
          const totalCounts = sortedStats.map(item => item.count); // 供应商数量
          const totalCosts = sortedStats.map(item => item.totalCost); // 供应商成本总和

          // 保存到组件的状态中
          this.regiontype = {
            indemnificationNames,
            totalCounts,
            totalCosts,
          };

          // 渲染图表
          this.renderregionChart();
        } else {
          this.regiontype = {};
          this.renderregionChart();  // 渲染空图表
        }
      });
    },

    renderregionChart() {
      const { indemnificationNames, totalCosts, totalCounts } = this.regiontype;

      const chart = this.$echarts.init(document.getElementById('chart10')); // 图表容器的 DOM 元素 ID

      const option = {
        title: {
          text: '前十区域统计',
          left: 'center',
          textStyle: {
            fontSize: 18,  // 设置标题的字体大小
            fontWeight: 'bold',
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow', // 显示阴影指示器
          },
        },
        legend: {
          data: ['成本总和', '问题数量'],
          top: '20',
        },
        xAxis: {
          type: 'category',
          data: indemnificationNames, // x轴为赔偿件名称
          axisLabel: {
            interval: 0,
            rotate: 10, // 避免文字过长重叠
          },
        },
        yAxis: [
          {
            type: 'value',
            name: '成本总和', // 对应柱状图
          },
          {
            type: 'value',
            name: '问题数量', // 对应折线图
            axisLabel: {
              formatter: '{value}',
            },
          },
        ],
        series: [
          {
            name: '成本总和（元）',
            type: 'bar',
            data: totalCosts, // 各赔偿件的成本总和
            itemStyle: {
              color: '#73C9E6',
            },
          },
          {
            name: '赔偿件数量',
            type: 'line',
            yAxisIndex: 1, // 使用第二个 Y 轴
            data: totalCounts, // 各赔偿件的发生次数
            itemStyle: {
              color: '#91CC75',
            },
            lineStyle: {
              width: 2,
            },
          },
        ],
        grid: {
          left: '20%', // 设置左边距
          right: '15%', // 设置右边距
        }
      };

      // 设置图表配置项
      chart.setOption(option);
    },







  }
};
</script>

<style scoped>
</style>
