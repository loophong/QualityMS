<template>
  <div ref="graph_container" class="graph_container"></div>
</template>

<script>
import { mxGraph } from 'mxgraph-js'
export default {
  name: 'indicatorDisplay',
  mounted () {
    // 创建画布
    var graph = new mxGraph(this.$refs.graph_container);
    // 获取容器的宽度和高度
    var containerWidth = this.$refs.graph_container.clientWidth;
    var containerHeight = this.$refs.graph_container.clientHeight;
    // 计算根节点的坐标，使其位于容器的中央
    var rootNodeWidth = 240;
    var rootNodeHeight = 120;
    var rootNodeX = (containerWidth - rootNodeWidth) / 2;
    var rootNodeY = (containerHeight - rootNodeHeight) / 2;
    var parent = graph.getDefaultParent();
    // 开始更新画布
    graph.getModel().beginUpdate();
    try {
      // 插入根节点
      let parentVertex = graph.insertVertex(parent, null, '公司质量指标体系', rootNodeX, rootNodeY, rootNodeWidth, rootNodeHeight);
      // 插入节点
      let v0 = graph.insertVertex();
      let v1 = graph.insertVertex(parent, null, 'Hello,', 20, 20, 80, 30);
      let v2 = graph.insertVertex(parent, null, 'World!', 200, 150, 80, 30);
      // 插入连线
      graph.insertEdge(parent, null, '', parentVertex, v1);
      graph.insertEdge(parent, null, '', parentVertex, v2);
    } finally {
      // 画布更新结束
      graph.getModel().endUpdate();
    }
  }
};
</script>

<style>
.graph_container {
  width: 100%;
  height: 1000px;
  border: 1px solid #ccc;
}
</style>
