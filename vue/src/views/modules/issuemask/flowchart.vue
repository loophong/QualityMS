<template>
  <div class="flowchart-container">
    <div ref="chart" class="chart-area"></div>
    <div v-if="showDialog" class="dialog">
      <h3>{{ selectedNode.label }}</h3>
      <button @click="addChildNode">增加子节点</button>
      <button @click="deleteNode">删除节点</button>
      <button @click="closeDialog">关闭</button>
    </div>
  </div>
</template>



<script>
import * as d3 from 'd3';

export default {
  name: 'FlowChart',
  data() {
    return {
      nodes: [
        { id: 1, x: 100, y: 100, label: '开始' },
        { id: 2, x: 300, y: 100, label: '过程' },
        { id: 3, x: 500, y: 100, label: '结束' },
      ],
      links: [
        { source: 1, target: 2 },
        { source: 2, target: 3 },
      ],
      svg: null,
      width: 800,
      height: 600,
      drag: null,
      showDialog: false,
      selectedNode: null,
    };
  },
  mounted() {
    this.initSvg();
    this.drawLinks();
    this.drawNodes();
  },
  methods: {
    deleteNode() {
      this.deleteNodeAndChildren(this.selectedNode.id);
      this.updateChart();
      this.closeDialog();
    },
    deleteNodeAndChildren(nodeId) {
      // 找到所有子节点
      const childNodes = this.links
        .filter(link => link.source === nodeId)
        .map(link => link.target);

      // 递归删除所有子节点
      childNodes.forEach(childId => {
        this.deleteNodeAndChildren(childId);
      });

      // 删除当前节点和相关的链接
      this.nodes = this.nodes.filter(node => node.id !== nodeId);
      this.links = this.links.filter(
        link => link.source !== nodeId && link.target !== nodeId
      );
    },
    updateChart() {
      this.svg.selectAll('.node').remove();
      this.svg.selectAll('.link').remove();
      this.drawLinks();
      this.drawNodes();
    },
    initSvg() {
      this.svg = d3
        .select(this.$refs.chart)
        .append('svg')
        .attr('width', this.width)
        .attr('height', this.height)
        .style('border', '1px solid #ccc');

      this.drag = d3
        .drag()
        .on('start', this.dragStarted)
        .on('drag', this.dragged)
        .on('end', this.dragEnded);
    },
    drawNodes() {
      const nodes = this.svg
        .selectAll('.node')
        .data(this.nodes)
        .enter()
        .append('g')
        .attr('class', 'node')
        .attr('transform', d => `translate(${d.x}, ${d.y})`)
        .call(this.drag)
        .on('click', (event, d) => {
          this.selectedNode = d;
          this.showDialog = true;
        });

      nodes
        .append('rect')
        .attr('width', 100)
        .attr('height', 50)
        .attr('fill', '#69b3a2')
        .attr('rx', 5)
        .attr('ry', 5);

      nodes
        .append('text')
        .attr('x', 50)
        .attr('y', 25)
        .attr('dy', '.35em')
        .attr('text-anchor', 'middle')
        .text(d => d.label)
        .attr('fill', '#fff');
    },
    addChildNode() {
      // 获取当前选中节点的所有兄弟节点
      const siblingLinks = this.links.filter(link => link.source === this.selectedNode.id);
      const siblingCount = siblingLinks.length;

      // 计算兄弟节点的子节点总数
      let totalSiblingDescendants = 0;
      siblingLinks.forEach(link => {
        totalSiblingDescendants += this.getDescendantCount(link.target);
      });

      // 计算新节点的 y 位置，基于兄弟节点及其子节点总数
      const yOffset = (siblingCount + totalSiblingDescendants) * 50; // 每个兄弟节点及其子节点占用的垂直空间

      const newNode = {
        id: this.nodes.length + 1,
        x: this.selectedNode.x + 150, // 水平方向上偏移
        y: this.selectedNode.y + yOffset, // 垂直方向上根据兄弟节点及其子节点数量进行偏移
        label: `节点${this.nodes.length + 1}`,
      };

      this.nodes.push(newNode);
      this.links.push({ source: this.selectedNode.id, target: newNode.id });
      this.updateChart();
      this.closeDialog();
    },
    // 递归计算节点的所有子节点数
    getDescendantCount(nodeId) {
      const children = this.links.filter(link => link.source === nodeId);
      let count = children.length;
      children.forEach(childLink => {
        count += this.getDescendantCount(childLink.target);
      });
      return count;
    },
    closeDialog() {
      this.showDialog = false;
      this.selectedNode = null;
    },
    drawLinks() {
      this.svg
        .selectAll('.link')
        .data(this.links)
        .enter()
        .append('line')
        .attr('class', 'link')
        .attr('x1', d => this.getNodeById(d.source).x + 100)
        .attr('y1', d => this.getNodeById(d.source).y + 25)
        .attr('x2', d => this.getNodeById(d.target).x)
        .attr('y2', d => this.getNodeById(d.target).y + 25)
        .attr('stroke', '#999')
        .attr('stroke-width', 2)
        .attr('marker-end', 'url(#arrow)');

      // 定义箭头
      this.svg
        .append('defs')
        .append('marker')
        .attr('id', 'arrow')
        .attr('viewBox', '0 0 10 10')
        .attr('refX', 10)
        .attr('refY', 5)
        .attr('markerWidth', 6)
        .attr('markerHeight', 6)
        .attr('orient', 'auto-start-reverse')
        .append('path')
        .attr('d', 'M 0 0 L 10 5 L 0 10 z')
        .attr('fill', '#999');
    },
    getNodeById(id) {
      return this.nodes.find(node => node.id === id);
    },
    dragStarted(event, d) {
      d3.select(this).raise().attr('stroke', 'black');
    },
    dragged(event, d) {
      d.x = event.x;
      d.y = event.y;
      d3.select(this).attr('transform', `translate(${d.x}, ${d.y})`);
      this.updateLinks();
    },
    dragEnded(event, d) {
      d3.select(this).attr('stroke', null);
    },
    updateLinks() {
      this.svg
        .selectAll('.link')
        .attr('x1', d => this.getNodeById(d.source).x + 100)
        .attr('y1', d => this.getNodeById(d.source).y + 25)
        .attr('x2', d => this.getNodeById(d.target).x)
        .attr('y2', d => this.getNodeById(d.target).y + 25);
    },
  },
};
</script>
<style scoped>
.flowchart-container {
  display: flex;
  justify-content: space-between;
}

.chart-area {
  width: 80%;
  position: relative;
}

.dialog {
  width: 20%;
  padding: 10px;
  border: 1px solid #ccc;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
</style>

