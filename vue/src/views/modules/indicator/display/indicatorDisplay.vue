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
    // renderTree() {
    //   const mainContainer = document.querySelector('main'); const mainWidth = mainContainer.clientWidth; const mainHeight = mainContainer.clientHeight;
    //   const rootNode = {
    //     id: 1,
    //     name: '公司质量指标管控体系',
    //     parentName: '',
    //     url: '',
    //     classification: 'A'
    //   };
    //
    //   const formattedData = [rootNode, ...this.dataList.map(item => ({
    //     id: item.indicatorId,
    //     name: item.indicatorName,
    //     parentName: item.indicatorParentNode,
    //     url: 'indicatorchart',
    //     classification: item.indicatorClassification,
    //     indicatorDefinition: item.indicatorDefinition,
    //     weight: item.weight,
    //     indicatorPlannedValue: item.indicatorPlannedValue,
    //     assessmentDepartment: item.assessmentDepartment,
    //     managementDepartment: item.managementDepartment
    //   }))];
    //
    //   const treeData = this.buildTree(formattedData);
    //
    //   const margin = { top: 20, right: 120, bottom: 20, left: 120 };
    //   const width = mainWidth - margin.right - margin.left;
    //   const height = mainHeight - margin.top - margin.bottom;
    //
    //   const svg = d3.select(this.$refs.treeChart)
    //     .append('svg')
    //     .attr('width', mainWidth)
    //     .attr('height', mainHeight)
    //     .call(d3.zoom().on("zoom", (event) => {  // 添加缩放和平移功能
    //       svg.attr("transform", event.transform);
    //     }))
    //     .append('g');
    //
    //   const root = d3.hierarchy(treeData[0]);
    //   const treeLayout = d3.tree().size([height * 3, width * 2]); // 增加高度比例
    //   treeLayout(root);
    //
    //   const links = svg.selectAll('line.link')  // 连线
    //     .data(root.links())
    //     .enter()
    //     .append('line')
    //     .attr('class', 'link')
    //     .attr('id', (d, i) => `link-${i}`)  // 为每个连线添加唯一 id
    //     .attr('x1', d => d.source.y + 100)
    //     .attr('y1', d => d.source.x + 10)
    //     .attr('x2', d => d.target.y - 100)
    //     .attr('y2', d => d.target.x + 10)
    //     .style('stroke', '#333')
    //     .style('stroke-width', 2);  // 加粗线条
    //
    //   console.log("links====>",links)
    //
    //   const nodes = svg.selectAll('g.node')
    //     .data(root.descendants())
    //     .enter()
    //     .append('g')
    //     .attr('class', 'node')
    //     .attr('id', d => `node-${d.data.id}`)  // 为每个节点添加唯一 id
    //     .attr('transform', d => `translate(${d.y},${d.x})`)
    //     .on('click', (event, d) => {
    //       this.$router.push({
    //         name: d.data.url,
    //         params: { indicatorName: d.data.name }
    //       });
    //     })
    //     .on('mouseover', function (event, d) { // 鼠标移入事件
    //       // 将其他节点和连线变浅
    //       nodes.style('opacity', 0.3);
    //       links.style('opacity', 0.3);
    //
    //       // 恢复当前节点及其相关节点和连线的正常显示
    //       d3.select(this).style('opacity', 1);
    //
    //       // 找到当前节点的所有祖先节点
    //       const ancestors = [];
    //       let current = d;
    //       console.log("current===>", current);
    //       ancestors.push(current);
    //       while (current.parent) {
    //         ancestors.push(current.parent);
    //         current = current.parent;
    //       }
    //       console.log("ancestors===>", ancestors);
    //
    //       // 恢复祖先节点及其连线的正常显示
    //       ancestors.forEach(a => {
    //         d3.select(`#node-${a.data.id}`).style('opacity', 1);
    //         // 找到祖先节点的连线
    //         const linkIndex = root.links().findIndex(link => link.target === a);
    //         if (linkIndex !== -1) {
    //           d3.select(`#link-${linkIndex}`).style('opacity', 1);
    //         }
    //       });
    //
    //       // 找到当前节点的所有子节点
    //       const descendants = [];
    //       function findDescendants(node) {
    //         if (node.children) {
    //           node.children.forEach(child => {
    //             descendants.push(child);
    //             findDescendants(child);
    //           });
    //         }
    //       }
    //       findDescendants(d);
    //       console.log("descendants===>", descendants);
    //
    //       // 恢复子节点及其连线的正常显示
    //       descendants.forEach(desc => {
    //         d3.select(`#node-${desc.data.id}`).style('opacity', 1);
    //         const linkIndex = root.links().findIndex(link => link.target === desc);
    //         if (linkIndex !== -1) {
    //           d3.select(`#link-${linkIndex}`).style('opacity', 1);
    //         }
    //       });
    //
    //       // 创建信息框
    //       const tooltip = svg.append('g')
    //         .attr('class', 'tooltip')
    //         .style('display', 'block')
    //         .style('pointer-events', 'none')
    //         .attr('transform', `translate(${d.y + 100}, ${d.x - 100})`);
    //
    //       // 添加表格内容
    //       const table = tooltip.append('g')
    //         .attr('transform', 'translate(10, 10)');
    //
    //       const rowHeight = 20;
    //       const rows = [
    //         { label: '管理部门', value: d.data.managementDepartment },
    //         { label: '考核部门', value: d.data.assessmentDepartment },
    //         { label: '指标定义', value: d.data.indicatorDefinition },
    //         { label: '目标值', value: d.data.indicatorPlannedValue },
    //         { label: '权重', value: d.data.weight ? `${d.data.weight}%` : '' }
    //       ];
    //
    //       let maxTextWidth = 0;
    //       const texts = rows.map((row, index) => {
    //         const text = table.append('text')
    //           .attr('x', 0)
    //           .attr('y', index * rowHeight + 15)
    //           .text(row.label + ': ' + row.value)
    //           .style('font-size', '12px')
    //           .style('fill', '#333');
    //
    //         // 计算文本的宽度
    //         const bbox = text.node().getBBox();
    //         if (bbox.width > maxTextWidth) {
    //           maxTextWidth = bbox.width;
    //         }
    //
    //         return text;
    //       });
    //
    //       // 计算总的文本高度
    //       const totalHeight = rows.length * rowHeight + 20;
    //
    //       // 添加背景矩形
    //       const rect = tooltip.insert('rect', ':first-child')
    //         .attr('width', maxTextWidth + 20)  // 20 是左右边距
    //         .attr('height', totalHeight)
    //         .attr('rx', 10)
    //         .attr('ry', 10)
    //         .style('fill', '#f9f9f9')
    //         .style('stroke', '#333')
    //         .style('stroke-width', 1);
    //
    //     })
    //     .on('mouseout', function (event, d) { // 鼠标移出事件
    //       // 恢复所有节点和连线的正常显示
    //       nodes.style('opacity', 1);
    //       links.style('opacity', 1);
    //
    //       // 移除信息框
    //       svg.selectAll('.tooltip').remove();
    //     });
    //
    //   const colorScale = {
    //     'A': '#e74c3c',
    //     'B': '#e67e22',
    //     'C': '#3498db'
    //   };
    //
    //   nodes.append('rect')
    //     .attr('width', 200)
    //     .attr('height', 80)
    //     .attr('x', -100)
    //     .attr('y', -25)
    //     .attr('rx', 10)
    //     .attr('ry', 10)
    //     .style('fill', d => colorScale[d.data.classification] || '#000');
    //
    //   nodes.append('circle')  // 添加圆圈
    //     .attr('cx', -90)
    //     .attr('cy', -15)
    //     .attr('r', 10)
    //     .style('fill', '#fff');
    //
    //   nodes.append('text')      // 圆圈添加文本
    //     .attr('x', -90)
    //     .attr('y', -15)
    //     .attr('text-anchor', 'middle')
    //     .style('dominant-baseline', 'middle')
    //     .style('font-size', '10px')
    //     .style('fill', '#000')
    //     .text(d => d.data.classification);
    //
    //   nodes.append('text')     // 节点添加文本
    //     .attr('dy', '.9em')
    //     .attr('x', 0)
    //     .attr('y', 0)
    //     .attr('text-anchor', 'middle')
    //     .style('dominant-baseline', 'central')
    //     .text(d => d.data.name)
    //     .style('fill', 'white');
    // },


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
        indicatorDefinition: item.indicatorDefinition,
        weight: item.weight,
        indicatorPlannedValue: item.indicatorPlannedValue,
        assessmentDepartment: item.assessmentDepartment,
        managementDepartment: item.managementDepartment
      }))];
      console.log('formattedData===>', formattedData);

      // 获取所有没有父节点的数据项
      const rootNodes = formattedData.filter(item => item.parentName === '');
      console.log('rootNodes===>', rootNodes);

      const margin = { top: 20, right: 120, bottom: 20, left: 120 };
      const width = mainWidth - margin.right - margin.left;
      const height = mainHeight - margin.top - margin.bottom;

      console.log('treeChart ref:', this.$refs.treeChart);
      const svg = d3.select(this.$refs.treeChart)
        .append('svg')
        .attr('width', mainWidth)
        .attr('height', mainHeight)
        .call(d3.zoom().on("zoom", (event) => {  // 添加缩放和平移功能
          svg.attr("transform", event.transform);
        }))
        .append('g');
      console.log('SVG element:', svg.node());



      // 遍历每个根节点并渲染树
      rootNodes.forEach((rootNode, index) => {
        const treeData = this.buildTree(formattedData,rootNode.parentName);
        console.log('treeData===>', treeData);

        const root = d3.hierarchy(treeData[index]);
        console.log('root===>', root);
        const treeLayout = d3.tree().size([height * 3, width * 2]); // 增加高度比例
        console.log('treeLayout===>', treeLayout);
        treeLayout(root);
        console.log('Root after layout:', root);

        const links = svg.selectAll(`line.link-${index}`)  // 连线
          .data(root.links())
          .enter()
          .append('line')
          .attr('class', `link link-${index}`)
          .attr('id', (d, i) => `link-${index}-${i}`)  // 为每个连线添加唯一 id
          .attr('x1', d => d.source.y + 100)
          .attr('y1', d => d.source.x + 10 + index * (height + 2000))
          .attr('x2', d => d.target.y - 100)
          .attr('y2', d => d.target.x + 10 + index * (height + 2000))
          .style('stroke', '#333')
          .style('stroke-width', 2);  // 加粗线条

        const nodes = svg.selectAll(`g.node-${index}`)
          .data(root.descendants())
          .enter()
          .append('g')
          .attr('class', `node node-${index}`)
          .attr('id', d => `node-${index}-${d.data.id}`)  // 为每个节点添加唯一 id
          .attr('transform', d => `translate(${d.y},${d.x + index * (height + 2000)})`) // 调整位置以避免重叠
          .on('click', (event, d) => {
            this.$router.push({
              name: d.data.url,
              params: { indicatorName: d.data.name }
            });
          })
          .on('mouseover', function (event, d) { // 鼠标移入事件
            // 将其他节点和连线变浅
            nodes.style('opacity', 0.3);
            links.style('opacity', 0.3);

            console.log('Nodes:', nodes.nodes());

            // 恢复当前节点及其相关节点和连线的正常显示
            d3.select(this).style('opacity', 1);

            // 找到当前节点的所有祖先节点
            const ancestors = [];
            let current = d;
            console.log("current===>", current);
            ancestors.push(current);
            while (current.parent) {
              ancestors.push(current.parent);
              current = current.parent;
            }
            console.log("ancestors===>", ancestors);

            // 恢复祖先节点及其连线的正常显示
            ancestors.forEach(a => {
              d3.select(`#node-${index}-${a.data.id}`).style('opacity', 1);
              // 找到祖先节点的连线
              const linkIndex = root.links().findIndex(link => link.target === a);
              if (linkIndex !== -1) {
                d3.select(`#link-${index}-${linkIndex}`).style('opacity', 1);
              }
            });

            // 找到当前节点的所有子节点
            const descendants = [];
            function findDescendants(node) {
              if (node.children) {
                node.children.forEach(child => {
                  descendants.push(child);
                  findDescendants(child);
                });
              }
            }
            findDescendants(d);
            console.log("descendants===>", descendants);

            // 恢复子节点及其连线的正常显示
            descendants.forEach(desc => {
              d3.select(`#node-${index}-${desc.data.id}`).style('opacity', 1);
              const linkIndex = root.links().findIndex(link => link.target === desc);
              if (linkIndex !== -1) {
                d3.select(`#link-${index}-${linkIndex}`).style('opacity', 1);
              }
            });

            // 创建信息框
            const tooltip = svg.append('g')
              .attr('class', 'tooltip')
              .style('display', 'block')
              .style('pointer-events', 'none')
              .attr('transform', `translate(${d.y + 100}, ${d.x - 100 + index * (height + 2000)})`);

            // 添加表格内容
            const table = tooltip.append('g')
              .attr('transform', 'translate(10, 10)');

            const rowHeight = 20;
            const rows = [
              { label: '管理部门', value: d.data.managementDepartment },
              { label: '考核部门', value: d.data.assessmentDepartment },
              { label: '指标定义', value: d.data.indicatorDefinition },
              { label: '目标值', value: d.data.indicatorPlannedValue },
              { label: '权重', value: d.data.weight ? `${d.data.weight}%` : '' }
            ];

            let maxTextWidth = 0;
            const texts = rows.map((row, index) => {
              const text = table.append('text')
                .attr('x', 0)
                .attr('y', index * rowHeight + 15)
                .text(row.label + ': ' + row.value)
                .style('font-size', '12px')
                .style('fill', '#333');

              // 计算文本的宽度
              const bbox = text.node().getBBox();
              if (bbox.width > maxTextWidth) {
                maxTextWidth = bbox.width;
              }

              return text;
            });

            // 计算总的文本高度
            const totalHeight = rows.length * rowHeight + 20;

            // 添加背景矩形
            const rect = tooltip.insert('rect', ':first-child')
              .attr('width', maxTextWidth + 20)  // 20 是左右边距
              .attr('height', totalHeight)
              .attr('rx', 10)
              .attr('ry', 10)
              .style('fill', '#f9f9f9')
              .style('stroke', '#333')
              .style('stroke-width', 1);

          })
          .on('mouseout', function (event, d) { // 鼠标移出事件
            // 恢复所有节点和连线的正常显示
            nodes.style('opacity', 1);
            links.style('opacity', 1);

            // 移除信息框
            svg.selectAll('.tooltip').remove();
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
      });
    },




    buildTree(data, parentName = '') {
      return data
        .filter(item => item.parentName === parentName)
        .map(item => ({
          id: item.id,
          name: item.name,
          classification: item.classification,
          url: item.url,
          indicatorDefinition: item.indicatorDefinition,
          weight: item.weight,
          indicatorPlannedValue: item.indicatorPlannedValue,
          assessmentDepartment: item.assessmentDepartment,
          managementDepartment: item.managementDepartment,
          children: this.buildTree(data, item.name)
        }));
    },
  }
};
</script>

<style>
/* 添加一些样式来美化图表 */
</style>
