<template>
  <div ref="graph_container" class="graph_container"></div>
</template>

<script>
import AddOrUpdate from '../dataManagement/indicatorindicatorsummary-add-or-update'
import { listIndicatorSummary } from '@/api/indicator/indicator.js'
import { mxGraph, mxEvent } from 'mxgraph-js'
export default {
  data () {
    return {
      dataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
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
      let v1 = graph.insertVertex(parent, null, '三包期内产品月度返修率', 1500, 300, 120, 60);
      let v2 = graph.insertVertex(parent, null, '222', 1500, 700, 120, 60);
      // 插入连线
      graph.insertEdge(parent, null, '', parentVertex, v1);
      graph.insertEdge(parent, null, '', parentVertex, v2);
      // 为节点添加点击事件
      graph.addListener(mxEvent.CLICK, (sender, evt) => {
        const cell = evt.getProperty('cell');
        if (cell) {
          if (cell === v1) {
            this.$router.push({ name: 'demo01' });
          } else if (cell === v2) {
            window.location.href = '/path/to/statistics/page2';
          }
        }
      });
    } finally {
      // 画布更新结束
      graph.getModel().endUpdate();
    }
  },
  methods: {
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      const params = {
        page: this.pageIndex,
        limit: this.pageSize,
        key: this.dataForm.key
      }
      listIndicatorSummary(params).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })

      // this.$http({
      //   url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/list'),
      //   method: 'get',
      //   params: this.$http.adornParams({
      //     'page': this.pageIndex,
      //     'limit': this.pageSize,
      //     'key': this.dataForm.key
      //   })
      // }).then(({data}) => {
      //   if (data && data.code === 0) {
      //     this.dataList = data.page.list
      //     this.totalPage = data.page.totalCount
      //   } else {
      //     this.dataList = []
      //     this.totalPage = 0
      //   }
      //   this.dataListLoading = false
      // })
    },
  }
}
</script>
<style>
.graph_container {
  width: 100%;
  height: 1000px;

}
</style>
