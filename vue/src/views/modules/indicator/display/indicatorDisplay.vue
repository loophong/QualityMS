<template>
  <div ref="treeChart" style="width: 100%; height: 500px;"></div>
</template>

<script>
import * as d3 from 'd3';

export default {
  data() {
    return {
      dataList: [], // 这里的数据会从服务器获取
    };
  },
  mounted() {
    this.getDataList();
  },
  methods: {
    getDataList() {
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': 10000,
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
      // 创建根节点
      const rootNode = {
        id: 1,
        name: '公司质量指标管控体系',
        parentName: '',
        url: '/some-url-based-on-indicator-id/1',
        classification: 'A'
      };
      // 格式化数据并插入根节点
      const formattedData = [rootNode, ...this.dataList.map(item => ({
        id: item.indicatorId,
        name: item.indicatorName,
        parentName: item.indicatorParentNode,
        // url: '/some-url-based-on-indicator-id/' + item.indicatorId,
        url: 'indicatorchart',
        classification: item.indicatorClassification,
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

      // 根据指标等级设置矩形颜色
      const colorScale = {
        'A': '#e74c3c', // 红色
        'B': '#e67e22', // 橙色
        'C': '#3498db'  // 蓝色
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
          console.log('Clicked on1:', d); // 打印节点数据
          console.log('d.data 内容:', d.data);
          console.log("即将传递的url:", d.data.url);
          console.log("即将传递的参数:", d.data.name);
          this.$router.push({
            name: d.data.url,
            params: { indicatorName: d.data.name }
          });
        });

      // 在矩形左上角添加小圆圈
      nodes.append('circle')
        .attr('cx', -90)
        .attr('cy', -15)
        .attr('r', 10)
        .style('fill', '#fff');

      // 在小圆圈内添加指标等级文本
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
          this.$router.push({
            name: d.data.url,
            params: { indicatorName: d.data.name }
          });
        });


    },



    buildTree(data, parentName = '') {
      return data
        .filter(item => item.parentName === parentName)  // 使用parentName来过滤
        .map(item => ({
          id: item.id,
          name: item.name,
          classification: item.classification,
          url: item.url,
          children: this.buildTree(data, item.name)  // 使用name来匹配
        }));
    },
  }
};
</script>

<style>
/* 添加一些样式来美化图表 */
</style>
