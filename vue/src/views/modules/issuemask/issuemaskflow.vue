<template>
  <div ref="treeChart" style="width: 100%; height: 500px;"></div>
</template>
<script>
import * as d3 from 'd3';

export default {
  data() {
    return {
      dataList: [], // 这里的数据会从服务器获取
      dataForm: {
        key: ''
      },
    };
  },
  mounted() {
    this.getDataList();
  },
  methods: {
    getDataList() {
      const issueNumber = this.$route.params.issueNumber
      console.log('Successfully fetched issueNumber :', issueNumber)
      this.$http({
        url: this.$http.adornUrl('/generator/issuemasktable/creatorlist'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': 10000,
          'key': this.dataForm.key,
          'issueNumber': issueNumber
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log('data:', data);
          this.dataList = data.page.list;
          console.log('DataList222:', this.dataList);
          this.renderTree();  // 在数据加载完成后渲染树状图
        } else {
          this.dataList = [];
        }
      });
    },

    renderTree() {
      // // 创建根节点
      // const rootNode = {
      //   id: 1,
      //   name: '问题',
      //   parentName: '',
      //   url: '/some-url-based-on-task-id/1',
      //   classification: 'A'
      // };
      // 创建根节点
      const rootNode = {
        id: '0',
        name: '问题',
        superiorMask: '',  // 根节点的superiorMask设为null
        url: '/some-url-based-on-task-id/1',
        classification: '开始'
      };
      // 格式化数据并插入根节点
      // const formattedData = [rootNode, ...this.dataList.map(item => ({
      //   id: item.issuemaskId,
      //   name: item.issueNumber,
      //   parentName: item.issueNumber,
      //   url: 'taskchart',
      //   classification: item.state,
      // }))];
      const formattedData = [rootNode, ...this.dataList.map(item => ({
        id: item.serialNumber,
        name: item.maskcontent,
        superiorMask: item.superiorMask,  // 使用superiorMask
        url: 'taskchart',
        classification: item.state,
      }))];

      console.log('Formatted Data:', formattedData); // 调试信息

      const treeData = this.buildTree(formattedData);
      console.log('Tree Data:', treeData); // 调试信息

      const margin = { top: 20, right: 120, bottom: 20, left: 120 };
      const width = 960 - margin.right - margin.left;
      const height = 500 - margin.top - margin.bottom;

      const svg = d3.select(this.$refs.treeChart)
        .append('svg')
        .attr('width', width + margin.right + margin.left)
        .attr('height', height + margin.top + margin.bottom)
        .append('g')
        .attr('transform', `translate(${margin.left},${margin.top})`);

      const root = d3.hierarchy(treeData[0]);

      const treeLayout = d3.tree().size([height, width]);
      treeLayout(root);

      svg.selectAll('line.link')
        .data(root.links())
        .enter()
        .append('line')
        .attr('class', 'link')
        .attr('x1', d => d.source.y + 100)
        .attr('y1', d => d.source.x)
        .attr('x2', d => d.target.y - 100)
        .attr('y2', d => d.target.x)
        .style('stroke', '#ccc');

      const nodes = svg.selectAll('g.node')
        .data(root.descendants())
        .enter()
        .append('g')
        .attr('class', 'node')
        .attr('transform', d => `translate(${d.y},${d.x})`);

      // 根据任务状态设置矩形颜色
      const colorScale = {
        '开始': '#2ecc71' ,// 白色
        '审核中': '#e74c3c', // 红色
        '执行中': '#e67e22', // 橙色
        '验证中': '#3498db'  // 蓝色
      };

      // 添加圆角矩形
      nodes.append('rect')
        .attr('width', 200)
        .attr('height', 80)
        .attr('x', -100)
        .attr('y', -25)
        .attr('rx', 10)
        .attr('ry', 10)
        .style('fill', d => {
          const color = colorScale[d.data.classification] || '#000'; // 如果未匹配到颜色，使用默认黑色
          return color;
        })
        .on('click', (event, d) => {
          console.log('Clicked on2:', d); // 打印节点数据
          console.log('d.data 内容:', d.data);
          console.log("即将传递的url:", d.data.url);
          console.log("即将传递的参数:", d.data.name);
          this.$router.push({ name: 'issue-issueRectification' }); // 直接跳转到指定路由
        });

      // 在矩形左上角添加小圆圈
      nodes.append('circle')
        .attr('cx', -90)
        .attr('cy', -15)
        .attr('r', 10)
        .style('fill', '#fff');

      // 在小圆圈内添加任务状态文本
      nodes.append('text')
        .attr('x', -90)
        .attr('y', -15)
        .attr('text-anchor', 'middle')
        .style('dominant-baseline', 'middle')
        .style('font-size', '10px')
        .style('fill', '#000')
        .text(d => {
          return d.data.classification;
        });

      // 在矩形中添加文本
      nodes.append('text')
        .attr('dy', '.9em')
        .attr('x', 0)
        .attr('y', 0)
        .attr('text-anchor', 'middle')
        .style('dominant-baseline', 'central')
        .text(d => d.data.name)
        .style('fill', 'white')
        .on('click', (event, d) => {
          console.log('Clicked on2:', d); // 打印节点数据
          console.log('d.data 内容:', d.data);
          console.log("即将传递的url:", d.data.url);
          console.log("即将传递的参数:", d.data.name);
          this.$router.push({ name: 'issue-issueRectification' }); // 直接跳转到指定路由
        });
    },

    // buildTree(data, parentName = '') {
    //   return data
    //     .filter(item => item.parentName === parentName)  // 使用parentName来过滤
    //     .map(item => ({
    //       id: item.id,
    //       name: item.name,
    //       classification: item.classification,
    //       url: item.url,
    //       children: this.buildTree(data, item.name)  // 使用name来匹配
    //     }));
    // },
    buildTree(data, parentId = '') {
      return data
        .filter(item => item.superiorMask === parentId)  // 使用superiorMask来过滤
        .map(item => ({
          id: item.id,
          name: item.name,
          classification: item.classification,
          url: item.url,
          children: this.buildTree(data, item.id)  // 使用id来匹配
        }));
    },
    openflow(issueId, issueNumber) {
      this.$router.push({
        name: 'issue-issuemask',
        params: {
          issueId: issueId,
          issueNumber: issueNumber
        }
      });
    }

  }
};
</script>

<style>
.node rect {
  cursor: pointer;
}

.node circle {
  cursor: pointer;
}

.node text {
  cursor: pointer;
}
</style>

<!--<template>-->
<!--  <div id="app">-->
<!--    <h1>流程图示例</h1>-->
<!--    <FlowChart />-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import FlowChart from './flowchart.vue';-->

<!--export default {-->
<!--  name: 'App',-->
<!--  components: {-->
<!--    FlowChart,-->
<!--  },-->
<!--};-->
<!--</script>-->

<!--<style>-->
<!--#app {-->
<!--  font-family: Avenir, Helvetica, Arial, sans-serif;-->
<!--  -webkit-font-smoothing: antialiased;-->
<!--  -moz-osx-font-smoothing: grayscale;-->
<!--  text-align: center;-->
<!--  color: #2c3e50;-->
<!--  margin-top: 60px;-->
<!--}-->
<!--</style>-->

