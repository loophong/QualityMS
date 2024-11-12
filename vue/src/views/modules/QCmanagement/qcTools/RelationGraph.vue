<template>
  <div>
    <div>
      <span>
        <el-select v-model="value" filterable @change="handleSelectChange" placeholder="请选择模版">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <el-button type="danger" @click="handleDelete">删除当前模版</el-button>
      </span>
    </div>
    <br>
    <div id="chart" ref="chart">
      <el-button @click="addNode">增加节点</el-button>
      <el-button @click="removeNode">删除节点</el-button>
      <el-button @click="modifyNode">修改节点</el-button>
      <el-button @click="addLink">增加链接</el-button>
      <el-button @click="removeLink">删除链接</el-button>
      <el-button @click="modifyLink">修改链接</el-button>
      <el-button type="warning" @click="downloadAsImage">保存图片</el-button>
      <el-button type="success" @click="dialogFormVisible = true">保存为模版</el-button>

      <el-dialog title="模版名" :visible.sync="dialogFormVisible">
        <el-input v-model="inputName" placeholder="请输入模版名" style="width:50%"></el-input>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleUp">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import * as d3 from 'd3';
import html2canvas from 'html2canvas';

export default {
  name: 'RelationGraph',
  data() {
    return {
      nodes: [
        { id: 'A', x: 100, y: 100, size: 1, label: 'A' },
        { id: 'B', x: 300, y: 100, size: 1, label: 'B' },
        { id: 'C', x: 200, y: 200, size: 1, label: 'C' },
        { id: 'D', x: 400, y: 200, size: 1, label: 'D' }
      ],
      links: [
        { source: 'A', target: 'B' },
        { source: 'A', target: 'C' },
        { source: 'B', target: 'D' },
        { source: 'C', target: 'D' }
      ],
      nextNodeId: 5, // 用于生成新的节点ID
      selectedNode: null,
      dialogFormVisible: false,
      selectedLink: null,
      value: '',
      inputName: '',
      name: '',
      options: [], // 模版列表
    };
  },
  mounted() {
    this.getTemplateData()
    this.initSvg();
    this.renderGraph();
  },
  methods: {
    handleUp() {
      console.log(this.nodes)
      console.log('inputName:' + this.inputName)
      console.log(JSON.stringify(this.nodes))
      this.dialogFormVisible = false
      this.addTemplate()
    },
    //保存为模版
    async addTemplate() {
      await this.$http({
        url: this.$http.adornUrl(`/qcTools/template/save`),
        method: 'post',
        data: this.$http.adornData({
          'templateId': undefined,
          'templateName': this.inputName || '未命名',
          'templateType': '关联图',
          'templateText': undefined,
          'templateSeries': JSON.stringify(this.nodes),
          'templateAxis': JSON.stringify(this.links),
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.visible = false
            }
          })
        } else {
          this.$message.error(data.msg)
        }
      })
    },
    //获取模版数据
    async getTemplateData() {
      await this.$http({
        url: this.$http.adornUrl('/qcTools/template/templateList'),
        method: 'get',
        params: this.$http.adornParams({
          'templateType': '关联图',
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.resultList = data.resultList.map(row => ({
            templateId: row.templateId,
            templateName: row.templateName,
            templateType: row.templateType,
            templateText: row.templateText,
            templateSeries: JSON.parse(row.templateSeries),
            templateAxis: JSON.parse(row.templateAxis),
          }))

          this.options = data.resultList.map(item => ({
            value: item.templateId,
            label: item.templateName
          }))
          console.log(this.options)
          console.log('+++++')
          console.log(this.resultList)
        } else {
          this.options = []
        }
      })
    },
    //处理下拉框选项变化
    handleSelectChange() {
      this.resultList.forEach(item => {
        if (item.templateId == this.value) {
          console.log(item.templateSeries)
          this.nodes = item.templateSeries
          this.links = item.templateAxis
          this.name = item.templateName
        }
      })
      this.renderGraph()
    },
    //删除当前模版
    handleDelete() {
      let ids = [this.value]
      console.log(ids)
      if (ids) {
        this.$confirm(`确定对 [${this.name}] 进行删除?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/qcTools/template/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.name = ''
                  this.value = ''
                  this.getTemplateData()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      } else {
        this.$message({
          message: '未选择',
          type: 'info',
          duration: 1500,

        })
      }
    },
    //保存图片
    downloadAsImage() {
      html2canvas(document.querySelector('#chart')).then(canvas => {
        const imgData = canvas.toDataURL('image/png')
        const a = document.createElement('a')
        a.href = imgData
        a.download = 'relation_image.png'
        a.click()
      })
    },
    initSvg() {
      const width = this.$refs.chart.clientWidth;
      const height = 600;

      this.svg = d3.select(this.$refs.chart)
        .append('svg')
        .attr('width', width)
        .attr('height', height);
    },

    renderGraph() {
      if (!this.svg) return;

      // 绘制链接
      const link = this.svg.selectAll('.link')
        .data(this.links, d => `${d.source}-${d.target}`)
        .join(
          enter => enter.append('line')
            .attr('class', 'link')
            .attr('stroke', '#999')
            .attr('stroke-opacity', 0.6)
            .attr('stroke-width', 1),
          update => update,
          exit => exit.remove()
        )
        .attr('x1', d => this.nodes.find(node => node.id === d.source).x)
        .attr('y1', d => this.nodes.find(node => node.id === d.source).y)
        .attr('x2', d => this.nodes.find(node => node.id === d.target).x)
        .attr('y2', d => this.nodes.find(node => node.id === d.target).y);

      // 绘制节点
      const node = this.svg.selectAll('.node')
        .data(this.nodes, d => d.id)
        .join(
          enter => enter.append('g')
            .attr('class', 'node')
            .on('click', (event, d) => this.selectNode(d))
            .call(d3.drag()
              .on('start', (event, d) => this.dragstarted(event, d))
              .on('drag', (event, d) => this.dragged(event, d))
              .on('end', (event, d) => this.dragended(event, d))),
          update => update,
          exit => exit.remove()
        );

      node.append('circle')
        .attr('r', d => d.size)
        .attr('fill', 'steelblue');

      node.append('text')
        .attr('dy', '.35em')
        .attr('text-anchor', 'middle')
        .text(d => d.label);

      node.attr('transform', d => `translate(${d.x},${d.y})`);
    },

    addNode() {
      const newLabel = prompt('请输入新的节点标签');
      if (newLabel && !this.nodes.some(node => node.label === newLabel)) {
        const newNode = {
          id: `Node${this.nextNodeId}`,
          x: Math.random() * 500,
          y: Math.random() * 500,
          size: 1,
          label: newLabel
        };
        this.nodes.push(newNode);
        this.nextNodeId++;
        this.renderGraph(); // 确保重新渲染图表
      } else {
        alert('节点标签已存在或为空');
      }
    },

    removeNode() {
      if (this.selectedNode) {
        this.nodes = this.nodes.filter(node => node.id !== this.selectedNode.id);
        this.links = this.links.filter(link => link.source !== this.selectedNode.id && link.target !== this.selectedNode.id);
        this.selectedNode = null;
        this.renderGraph();
      }
    },

    modifyNode() {
      if (this.selectedNode) {
        const newLabel = prompt('请输入新的节点标签', this.selectedNode.label) || this.selectedNode.label;
        if (!this.nodes.some(node => node.id !== this.selectedNode.id && node.label === newLabel)) {
          const newSize = parseFloat(prompt('请输入新的节点大小', this.selectedNode.size)) || this.selectedNode.size;
          const newX = parseFloat(prompt('请输入新的X坐标', this.selectedNode.x)) || this.selectedNode.x;
          const newY = parseFloat(prompt('请输入新的Y坐标', this.selectedNode.y)) || this.selectedNode.y;

          this.selectedNode.label = newLabel;
          this.selectedNode.size = newSize;
          this.selectedNode.x = newX;
          this.selectedNode.y = newY;
          this.renderGraph();
        } else {
          alert('节点标签已存在');
        }
      }
    },

    addLink() {
      const sourceLabel = prompt('请输入起始节点标签');
      const targetLabel = prompt('请输入目标节点标签');
      const sourceNode = this.nodes.find(node => node.label === sourceLabel);
      const targetNode = this.nodes.find(node => node.label === targetLabel);

      if (sourceNode && targetNode && sourceNode !== targetNode) {
        this.links.push({ source: sourceNode.id, target: targetNode.id });
        this.renderGraph();
      } else {
        alert('起始节点或目标节点不存在，或起始节点和目标节点相同');
      }
    },

    removeLink() {
      const sourceLabel = prompt('请输入起始节点标签');
      const targetLabel = prompt('请输入目标节点标签');
      const sourceNode = this.nodes.find(node => node.label === sourceLabel);
      const targetNode = this.nodes.find(node => node.label === targetLabel);

      if (sourceNode && targetNode) {
        this.links = this.links.filter(link => link.source !== sourceNode.id || link.target !== targetNode.id);
        this.renderGraph();
      } else {
        alert('起始节点或目标节点不存在');
      }
    },

    modifyLink() {
      const sourceLabel = prompt('请输入当前起始节点标签', this.selectedLink ? this.selectedLink.source : '');
      const targetLabel = prompt('请输入当前目标节点标签', this.selectedLink ? this.selectedLink.target : '');
      const newSourceLabel = prompt('请输入新的起始节点标签', sourceLabel) || sourceLabel;
      const newTargetLabel = prompt('请输入新的目标节点标签', targetLabel) || targetLabel;
      const sourceNode = this.nodes.find(node => node.label === newSourceLabel);
      const targetNode = this.nodes.find(node => node.label === newTargetLabel);

      if (sourceNode && targetNode && sourceNode !== targetNode) {
        if (this.selectedLink) {
          this.selectedLink.source = sourceNode.id;
          this.selectedLink.target = targetNode.id;
        } else {
          this.links = this.links.map(link => {
            if (link.source === sourceLabel && link.target === targetLabel) {
              return { source: sourceNode.id, target: targetNode.id };
            }
            return link;
          });
        }
        this.renderGraph();
      } else {
        alert('起始节点或目标节点不存在，或起始节点和目标节点相同');
      }
    },

    selectNode(d) {
      this.selectedNode = d;
      this.selectedLink = null;
    },

    dragstarted(event, d) {
      d3.select(this).raise().attr('stroke', 'black');
    },

    dragged(event, d) {
      d.x = event.x;
      d.y = event.y;
      this.renderGraph();
    },

    dragended(event, d) {
      d3.select(this).attr('stroke', null);
    }
  }
};
</script>

<style scoped>
/* 组件内样式 */
button {
  margin: 5px;
}

#chart {
  width: 100%;
  height: 600px;
}
</style>