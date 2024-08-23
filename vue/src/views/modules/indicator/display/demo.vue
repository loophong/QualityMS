<template>
  <div id="tree-chart" style="width: 100%; height: 500px;"></div>
</template>

<script>
import * as echarts from 'echarts';
import 'echarts/lib/chart/tree'; // 引入树状图
console.log("echarts===>",echarts);
import 'echarts/lib/component/tooltip'; // 如果用到了提示工具，也要引入
import 'echarts/lib/component/title'; // 如果用到了标题组件，也要引入



export default {
  data() {
    return {
      treeData: [], // 树形数据
    }
  },
  mounted() {
    this.initChart();
  },
  created() {
    const data = [
      {id: 1, name: '指标 A', parentId: null},
      {id: 2, name: '指标 B', parentId: 1},
      {id: 3, name: '指标 C', parentId: 1},
      {id: 4, name: '指标 D', parentId: 2}
    ];

    // 构建树状图数据
    this.treeData = this.buildTree(data)[0];  // 根节点
  },
  methods: {
    initChart() {
      console.log("initChart");
      const chartDom = document.getElementById('tree-chart');
      console.log('chartDom===>', chartDom)
      if (!chartDom) {
        console.log("DOM element not found: #tree-chart");
      }

      const myChart = echarts.init(chartDom);
      console.log('myChart===>', myChart)
      if (!myChart) {
        console.error("ECharts initialization failed");
      }
      const option = {
        tooltip: {
          trigger: 'item',
          triggerOn: 'mousemove'
        },
        series: [
          {
            type: 'tree',  // 指定使用树图
            data: [this.treeData],  // 根节点的数组
            top: '1%',
            left: '7%',
            bottom: '1%',
            right: '20%',
            symbolSize: 7,
            label: {
              position: 'left',
              verticalAlign: 'middle',
              align: 'right',
              fontSize: 9
            },
            leaves: {
              label: {
                position: 'right',
                verticalAlign: 'middle',
                align: 'left'
              }
            },
            expandAndCollapse: true,
            animationDuration: 550,
            animationDurationUpdate: 750
          }
        ]
      };
    }
  }
}
</script>
