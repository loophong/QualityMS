<template>
  <div>
<!--    div 放到底层-->


    <div style=" height: auto; width: auto; ">
<!--      <el-col>-->
<!--        <el-row style="color: #95a5a6; font-size: 20px; margin-bottom: 5px; width: 200px">● 未开始</el-row>-->
<!--        <el-row style="color: #3498db; font-size: 20px; margin-bottom: 5px; width: 200px">● 进行中</el-row>-->
<!--        <el-row style="color: #e67e22; font-size: 20px; margin-bottom: 5px; width: 200px">● 审批中</el-row>-->
<!--        <el-row style="color: #2ecc71; font-size: 20px; margin-bottom: 5px; width: 200px">● 已完成</el-row>-->
<!--        &lt;!&ndash;      <el-row style="color: #0BB2D4; font-size: 20px">● 红色代表已过期</el-row>&ndash;&gt;-->
<!--      </el-col>-->
      <el-row>
        <el-col style="color: #95a5a6; font-size: 20px; margin-bottom: 5px; width: 200px">● 未开始</el-col>
        <el-col style="color: #3498db; font-size: 20px; margin-bottom: 5px; width: 200px">● 进行中</el-col>
        <el-col style="color: #e67e22; font-size: 20px; margin-bottom: 5px; width: 200px">● 审批中</el-col>
        <el-col style="color: #2ecc71; font-size: 20px; margin-bottom: 5px; width: 200px">● 已完成</el-col>
      </el-row>


    </div>
    <div ref="treeChart" style="width: 100%; height: 500px;">


    </div>
  </div>

</template>

<script>
import * as d3 from 'd3';

export default {
  data() {
    return {
      rootData: null,
      dataList: [], // 这里的数据会从服务器获取
    };
  },
  async created() {
    // this.init(this.$route.query.planId);
    console.log('this.$route.query.planId:', this.$route.query.planId)
  },

  mounted() {
    this.$nextTick(() => {
      this.updateSvgSize();
      window.addEventListener('resize', this.updateSvgSize); // 添加窗口大小变化监听器，实时调整SVG大小
      // this.getDataList();
    });

    this.getDataList();
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
        url: this.$http.adornUrl('/taskmanagement/plan/getPlanAllInfo'),
        method: 'get',
        params: this.$http.adornParams({
          // 'planId': this.pageIndex
          'planId': this.$route.query.planId

        })
      }).then(({data}) => {
        if (data) {
          console.log('data:', data);
          this.rootData = data.plan;
          this.dataList = data.tasks;

          console.log('rootData:', this.rootData);
          console.log('dataList:', this.dataList);

          this.renderTree();

          // this.dataList = data.page.list;
          // console.log('DataList222:', this.dataList);
          // this.renderTree();  // 在数据加载完成后渲染树状图
        } else {
          this.dataList = [];
        }
      });
    },

    renderTree() {

      const mainContainer = document.querySelector('main');
      const mainWidth = mainContainer.clientWidth;
      const mainHeight = mainContainer.clientHeight;

      // 创建根节点
      const rootNode = {
        id: this.rootData.planId,
        name: this.rootData.planName,
        parentId: '',
        statusColor: this.rootData.planCurrentState
        // parentName: '',
        // url: '/some-url-based-on-indicator-id/1',
        // classification: 'A'
      };

      // 格式化数据并插入根节点
      const formattedData = [rootNode, ...this.dataList.map(item => ({
        // id: item.indicatorId,
        // name: item.indicatorName,
        // parentName: item.indicatorParentNode,
        // url: '/some-url-based-on-indicator-id/' + item.indicatorId,
        // url: 'indicatorchart',
        // classification: item.indicatorClassification,
        id: item.taskId,
        name: item.taskName,
        parentId: item.taskParentNode,
        statusColor: item.taskCurrentState
      }))];

      console.log('Formatted Data:', formattedData); // 调试信息

      const treeData = this.buildTree(formattedData);

      console.log('Tree Data:', treeData); // 调试信息

      const margin = {top: 20, right: 120, bottom: 20, left: 120};
      const width = mainWidth - margin.right - margin.left;
      const height = mainHeight - margin.top - margin.bottom;

      const yOffset = height / 2;
      const initialTransform = `translate(${margin.left},${yOffset + margin.top})`;

      const svg = d3.select(this.$refs.treeChart)
        .append('svg')
        .attr('width', width + margin.right + margin.left)
        .attr('height', height + margin.top + margin.bottom)
        .call(d3.zoom().on("zoom", (event) => {  // 添加缩放和平移功能
          svg.attr("transform", event.transform);
        }))
        .append('g')
        .attr('transform', initialTransform)
        .append("g");

      const root = d3.hierarchy(treeData[0]);
      // const treeLayout = d3.tree().size([height, width]); // 增加高度比例
      // const treeLayout = d3.tree().size([200, 400]); // 增加高度比例
      const treeLayout = d3.tree().nodeSize([200, 400]); // 设置节点的水平和垂直间距
      treeLayout(root);

      // root.x = width / 2;

      // 计算垂直居中的偏移量
      // const yOffset = (height - root.height * 200) / 2;
      // const initialTransform = `translate(${margin.left},${yOffset + margin.top})`;
      // svg.attr('transform', initialTransform).append("g");


      svg.selectAll('line.link')
        .data(root.links())
        .enter()
        .append('line')
        .attr('class', 'link')
        .attr('x1', d => d.source.y + 100) // 100 是节点宽度的一半
        .attr('y1', d => d.source.x + 20)  // 40 是节点高度的一半
        .attr('x2', d => d.target.y - 100) // 100 是节点宽度的一半
        .attr('y2', d => d.target.x + 20)  // 40 是节点高度的一半
        .style('stroke', '#ccc');

      const nodes = svg.selectAll('g.node')
        .data(root.descendants())
        .enter()
        .append('g')
        .attr('class', 'node')
        .attr('transform', d => `translate(${d.y},${d.x})`);


      // 根据指标等级设置矩形颜色
      const colorScale = {
        'NOT_STARTED': '#95a5a6', // 灰色
        'IN_PROGRESS': '#3498db', // 蓝色
        'APPROVAL_IN_PROGRESS': '#e67e22', // 黄色
        'COMPLETED': '#2ecc71' // 绿色
      };


      // 'NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'OVERDUE', 'APPROVAL_IN_PROGRESS'
      // 'A': '#e74c3c', // 红色
      // 'B': '#e67e22', // 橙色
      // 'C': '#3498db'  // 蓝色


      // 添加圆角矩形
      nodes.append('rect')
        .attr('width', 200)
        .attr('height', 80)
        .attr('x', -100)
        .attr('y', -25)
        .attr('rx', 10)
        .attr('ry', 10)
        .style('fill', d => {
          // const color = colorScale[d.data.classification] || '#000'; // 如果未匹配到颜色，使用默认黑色
          const color = colorScale[d.data.statusColor] || '#000';
          return color;
        })
        .on('click', (event, d) => {
          console.log('Clicked on1:', d); // 打印节点数据
          console.log('d.data 内容:', d.data);
          console.log("即将传递的url:", d.data.url);
          console.log("即将传递的参数:", d.data.name);
          this.$router.push({
            name: d.data.url,
            params: {indicatorName: d.data.name}
          });
        });

      // 在矩形左上角添加小圆圈
      // nodes.append('circle')
      //     .attr('cx', -90)
      //     .attr('cy', -15)
      //     .attr('r', 10)
      //     .style('fill', '#fff');

      // 在小圆圈内添加指标等级文本
      // nodes.append('text')
      //     .attr('x', -90)
      //     .attr('y', -15)
      //     .attr('text-anchor', 'middle')
      //     .style('dominant-baseline', 'middle')
      //     .style('font-size', '10px')
      //     .style('fill', '#000')
      //     .text(d => {
      //         // return d.data.classification;
      //     });

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
            params: {indicatorName: d.data.name}
          });
        });


    },


    // buildTree(data, parentName = '') {
    //     return data
    //         .filter(item => item.parentName === parentName)  // 使用parentName来过滤
    //         .map(item => ({
    //             id: item.id,
    //             name: item.name,
    //             // classification: item.classification,
    //             url: item.url,
    //             children: this.buildTree(data, item.name)  // 使用name来匹配
    //         }));
    // },

    buildTree(data, parentId = '') {
      return data
        .filter(item => item.parentId === parentId)  // 使用parentName来过滤
        .map(item => ({
          id: item.id,
          name: item.name,
          // classification: item.classification,
          // url: item.url,
          statusColor: item.statusColor,
          children: this.buildTree(data, item.id)  // 使用name来匹配
        }));
    },
  }
};
</script>

<style>
/* 添加一些样式来美化图表 */
</style>
