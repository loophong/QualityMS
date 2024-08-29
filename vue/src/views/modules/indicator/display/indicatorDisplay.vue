<template>
  <div ref="treeChartContainer" style="width: 100%; height: 100%; overflow: auto;">
    <div ref="treeChart" style="width: 100%; height: 100%;"></div>
  </div>
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
    this.$nextTick(() => {
      this.updateSvgSize();
      window.addEventListener('resize', this.updateSvgSize); // 添加窗口大小变化监听器，实时调整SVG大小
      this.getDataList();
    });
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateSvgSize); // 组件销毁前移除监听器
  },
  methods: {
    updateSvgSize() {
      this.$nextTick(() => {
        const mainContainer = document.querySelector('main');
        if (mainContainer) {
          const mainWidth = mainContainer.clientWidth;
          const mainHeight = mainContainer.clientHeight;
          const svg = d3.select(this.$refs.treeChart).select('svg');
          svg.attr('width', mainWidth).attr('height', mainHeight);
          console.log(`Updated SVG size to width: ${mainWidth}, height: ${mainHeight}`);
        }
      });
    },
    getDataList() {
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/list02'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': 10000,
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list;
          this.renderTree();
        } else {
          this.dataList = [];
        }
      });
    },

    renderTree() {
      const mainContainer = document.querySelector('main');
      const mainWidth = mainContainer.clientWidth;
      const mainHeight = mainContainer.clientHeight;

      const rootNode = {
        id: 1,
        name: '公司质量指标管控体系',
        parentName: '',
        url: '',
        classification: 'A'
      };

      const formattedData = [rootNode, ...this.dataList.map(item => ({
        id: item.indicatorId,
        name: item.indicatorName,
        parentName: item.indicatorParentNode,
        url: 'indicatorchart',
        classification: item.indicatorClassification,
      }))];

      const treeData = this.buildTree(formattedData);

      const margin = {top: 20, right: 120, bottom: 20, left: 120};
      const width = mainWidth - margin.right - margin.left;
      const height = mainHeight - margin.top - margin.bottom;

      const svg = d3.select(this.$refs.treeChart)
        .append('svg')
        .attr('width', mainWidth)
        .attr('height', mainHeight)
        .call(d3.zoom().on("zoom", (event) => {  // 添加缩放和平移功能
          svg.attr("transform", event.transform);
        }))
        .append('g');

      const root = d3.hierarchy(treeData[0]);
      const treeLayout = d3.tree().size([height * 2, width]); // 增加高度比例
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
        .attr('transform', d => `translate(${d.y},${d.x})`)
        .on('click', (event, d) => {
          this.$router.push({
            name: d.data.url,
            params: {indicatorName: d.data.name}
          });
        })
        .on('mouseover', function(event, d) { // 鼠标移入事件
          d3.select(this).select('rect')
            .style('stroke', '#000')
            .style('stroke-width', 2);
        })
        .on('mouseout', function(event, d) { // 鼠标移出事件
          d3.select(this).select('rect')
            .style('stroke', 'none');
        });

      const colorScale = {
        'A': '#e74c3c',
        'B': '#e67e22',
        'C': '#3498db'
      };

      nodes.append('rect')
        .attr('width', 200)
        .attr('height', 80)
        .attr('x', -100)
        .attr('y', -25)
        .attr('rx', 10)
        .attr('ry', 10)
        .style('fill', d => colorScale[d.data.classification] || '#000');

      nodes.append('circle')  // 添加圆圈
        .attr('cx', -90)
        .attr('cy', -15)
        .attr('r', 10)
        .style('fill', '#fff');

      nodes.append('text')      // 圆圈添加文本
        .attr('x', -90)
        .attr('y', -15)
        .attr('text-anchor', 'middle')
        .style('dominant-baseline', 'middle')
        .style('font-size', '10px')
        .style('fill', '#000')
        .text(d => d.data.classification);

      nodes.append('text')     // 节点添加文本
        .attr('dy', '.9em')
        .attr('x', 0)
        .attr('y', 0)
        .attr('text-anchor', 'middle')
        .style('dominant-baseline', 'central')
        .text(d => d.data.name)
        .style('fill', 'white');
    },

    buildTree(data, parentName = '') {
      return data
        .filter(item => item.parentName === parentName)
        .map(item => ({
          id: item.id,
          name: item.name,
          classification: item.classification,
          url: item.url,
          children: this.buildTree(data, item.name)
        }));
    },
  }
};
</script>

<style>
/* 添加一些样式来美化图表 */
</style>
