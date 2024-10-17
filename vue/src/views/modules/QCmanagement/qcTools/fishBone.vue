<template>
  <div>
    <div class="button-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" @click="addEdge">新增</el-button>
          <el-button type="success" @click="downloadAsImage">下载</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div id="fishBone">
      <el-dialog
        title="编辑节点"
        :visible.sync="editDialogVisible"
        width="30%"
        :modal="false">
        <el-form :model="editableNode">
          <el-form-item label="节点名称">
            <el-input v-model="editableNode.name"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveChanges">保存</el-button>
        </div>
      </el-dialog>
      <el-dialog
        title="新增节点"
        :visible.sync="addDialogVisible"
        width="30%"
        :modal="false">
        <el-form :model="newNode">
          <el-form-item label="节点名称">
            <el-input v-model="newNode.name"></el-input>
          </el-form-item>
          <el-form-item label="父节点">
            <el-select v-model="newNode.parentId" placeholder="请选择父节点">
              <el-option
                v-for="item in nodeNames"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addNode">保存</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>


import { FishBones } from '@/components/fishbone/FishBone'
import html2canvas from 'html2canvas';

export default {
  data () {
    return {
      testFishData: [
        {"id": "1", "fid": "0", "name": "质量问题", fontColor: "", lineColor: "", link: "", "children": [], }
      ],
      editDialogVisible: false, // 控制弹出框的可见性
      editableNode: { // 用于存储可编辑的节点信息
        name: '',
        link: ''
      },
      currentNode: null, // 用于存储当前被点击的节点
      addDialogVisible: false, // 控制新增弹出框的可见性
      newNode: { // 用于存储新增节点的信息
        name: '',
        link: '',
        parentId: ''
      },
      nodeNames: [] // 用于存储节点名称数组
    }
  },
  mounted () {
    this.initFishBone()
    this.getNodeNames(this.testFishData)
  },
  methods: {
    initFishBone () {
      console.log('initFishBone')
      new FishBones({
        id: 'fishBone',
        jsonData: this.testFishData,
        // canvasSize: [document.body.scrollWidth, document.body.scrollHeight],
        clickNodeFunction: (node, event) => {
          this.editNode(node) // 点击节点时调用 editNode 方法
        },
      }).init()
    },
    editNode(node) {
      this.currentNode = node // 保存当前点击的节点
      this.editableNode = { ...node } // 将节点信息复制到可编辑的对象中
      console.log('当前点击的节点信息:', node)
      console.log('当前可编辑的节点信息:', this.editableNode)
      this.editDialogVisible = true // 打开编辑框
    },
    saveChanges() {
      this.updateNode(this.testFishData, this.editableNode)
      // 打印修改后的节点信息
      console.log('保存的节点信息:', this.editableNode)
      console.log('更新后的数据:', this.testFishData)
      // 重新初始化鱼骨图，以反映更新后的数据
      this.initFishBone()
      // 关闭编辑框
      this.editDialogVisible = false
    },
    updateNode(nodeList, updatedNode) {
      for (let i = 0; i < nodeList.length; i++) {
        console.log('当前节点:', nodeList[i])
        if (nodeList[i].id === updatedNode.id_) {
          // 使用 Vue.set 确保变化被检测到
          console.log('Found and updating node:', nodeList[i]);
          this.$set(nodeList[i], 'name', updatedNode.name)
          return true
        } else if (nodeList[i].children && nodeList[i].children.length > 0) {
          const found = this.updateNode(nodeList[i].children, updatedNode)
          if (found) {
            return true
          }
        }
      }
      return false
    },
    addEdge() {
      this.nodeNames = [] // 清空现有的 nodeNames 数组
      this.getNodeNames(this.testFishData) // 重新获取最新的 nodeNames
      this.addDialogVisible = true // 打开新增节点对话框
    },
    addNode() {
      const parentNode = this.findNodeById(this.testFishData, this.newNode.parentId)
      if (parentNode) {
        const newNode = {
          id: Date.now().toString(), // 生成唯一ID
          fid: this.newNode.parentId,
          name: this.newNode.name,
          children: []
        }
        if (!parentNode.children) {
          this.$set(parentNode, 'children', [])
        }
        parentNode.children.push(newNode)
        // 重新初始化鱼骨图，以反映更新后的数据
        this.initFishBone()
      }
      this.addDialogVisible = false // 关闭新增节点对话框
    },
    findNodeById(nodeList, id) {
      for (let i = 0; i < nodeList.length; i++) {
        if (nodeList[i].id === id) {
          return nodeList[i]
        } else if (nodeList[i].children && nodeList[i].children.length > 0) {
          const found = this.findNodeById(nodeList[i].children, id)
          if (found) {
            return found
          }
        }
      }
      return null
    },
    //获取节点名称
    getNodeNames(nodeList) {
      for (let i = 0; i < nodeList.length; i++) {
        this.nodeNames.push({ value: nodeList[i].id, label: nodeList[i].name })
        if (nodeList[i].children && nodeList[i].children.length > 0) {
          this.getNodeNames(nodeList[i].children)
        }
      }
    },
    downloadAsImage() {
      html2canvas(document.querySelector('#fishBone')).then(canvas => {
        const imgData = canvas.toDataURL('image/png')
        const a = document.createElement('a')
        a.href = imgData
        a.download = 'fishbone_image.png'
        a.click()
      })
    }
  },
}
</script>

<style scoped>#fishBone {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 1000px;
  height: 600px;
  margin: 40px auto;
}

.button-container {
  position: absolute;
  top: 50px;
  left: 20px;
  padding: 10px;
}
</style>
