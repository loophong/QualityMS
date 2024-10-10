<template>
  <div id="fishBone">
    <el-dialog
      title="编辑节点"
      :visible.sync="editDialogVisible"
      width="30%">
      <el-form :model="editableNode">
        <el-form-item label="节点名称">
          <el-input v-model="editableNode.name"></el-input>
        </el-form-item>
        <el-form-item label="节点链接">
          <el-input v-model="editableNode.link"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveChanges">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>


import { FishBones } from '@/components/fishbone/FishBone'

export default {
  data () {
    return {
      testFishData: [
        {"children": [
            {"children": [
                {"children": [], "id": "1001001123123", "fid": "1001", "name": "主机A[100]", fontColor: "", lineColor: "", link: "http://www.baidu.com"},
                {"children": [], "id": "1001002123123", "fid": "1001", "name": "主机B[100]", fontColor: "", lineColor: "", link: ""}
              ], "id": "1001", "fid": "1", "name": "主机[100]", fontColor: "", lineColor: "", link: ""},
            {"children": [
                {"children": [], "id": "100200435431", "fid": "1002", "name": "探测点1[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1002034502", "fid": "1002", "name": "探测点2[100]", fontColor: "", lineColor: "", link: ""}
              ], "id": "1002", "fid": "1", "name": "网络[100]", fontColor: "", lineColor: "", link: ""},
            {"children": [
                {"children": [], "id": "100303451", "fid": "1003", "name": "数据库A[93]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1003034502", "fid": "1003", "name": "数据库B[93]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1003045603", "fid": "1003", "name": "数据库C[93]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "100345645004", "fid": "1003", "name": "数据库D[93]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "10064563005", "fid": "1003", "name": "数据库E[93]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1004563006", "fid": "1003", "name": "数据库F[93]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1004563007", "fid": "1003", "name": "数据库G[93]", fontColor: "", lineColor: "", link: ""}
              ], "id": "1003", "fid": "1", "name": "数据库[93]", fontColor: "", lineColor: "", link: ""},
            {"children": [
                {"children": [], "id": "100423423001", "fid": "1004", "name": "端口1[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1002344002", "fid": "1004", "name": "端口2[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1002344003", "fid": "1004", "name": "端口3[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1002344004", "fid": "1004", "name": "端口4[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1004354005", "fid": "1004", "name": "端口5[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1004354006", "fid": "1004", "name": "端口6[100]", fontColor: "", lineColor: "", link: ""}
              ], "id": "1004", "fid": "1", "name": "拨测[100]", fontColor: "", lineColor: "", link: ""},
            {"children": [
                {"children": [], "id": "1004355001", "fid": "10051111111", "name": "进程[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1003455002", "fid": "10051111111", "name": "进程[100]", fontColor: "", lineColor: "", link: ""}
              ], "id": "10051111111", "fid": "1", "name": "进程[100]", fontColor: "", lineColor: "", link: ""},
            {"children": [
                {"children": [], "id": "1006345001", "fid": "1006", "name": "中间件1[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1006345002", "fid": "1006", "name": "中间件2[100]", fontColor: "", lineColor: "", link: ""}
              ], "id": "1006", "fid": "1", "name": "中间件[100]", fontColor: "", lineColor: "", link: ""},
            {"children": [
                {"children": [], "id": "1007034501", "fid": "1007", "name": "安全[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1007034502", "fid": "1007", "name": "安全[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1007435003", "fid": "1007", "name": "安全[100]", fontColor: "", lineColor: "", link: ""}
              ], "id": "1007", "fid": "1", "name": "安全[100]", fontColor: "", lineColor: "", link: ""},
            {"children": [
                {"children": [], "id": "1008034501", "fid": "1008", "name": "巡检1[100]", fontColor: "", lineColor: "", link: ""},
                {"children": [], "id": "1008034502", "fid": "1008", "name": "巡检2[100]", fontColor: "", lineColor: "", link: ""}
              ], "id": "1008", "fid": "1", "name": "巡检[100]", fontColor: "", lineColor: "", link: ""}
          ], "id": "1", "fid": "0", "name": "质量问题", fontColor: "", lineColor: "", link: ""}
      ],
      editDialogVisible: false, // 控制弹出框的可见性
      editableNode: { // 用于存储可编辑的节点信息
        name: '',
        link: ''
      },
      currentNode: null // 用于存储当前被点击的节点
    }
  },
  mounted () {
    this.initFishBone()
  },
  methods: {
    initFishBone () {
      console.log('initFishBone')
      new FishBones({
        id: 'fishBone',
        jsonData: this.testFishData,
        canvasSize: [document.body.scrollWidth, document.body.scrollHeight],
        clickNodeFunction: (node, event) => {
          this.editNode(node) // 点击节点时调用 editNode 方法
        },
      }).init()
    },
    // editNode(node) {
    //   this.currentNode = node // 保存当前点击的节点
    //   this.editableNode = { ...node } // 将节点信息复制到可编辑的对象中
    //   this.editDialogVisible = true // 打开编辑框
    // },
    // saveChanges() {
    //   // 查找当前被编辑的节点在 testFishData 中的位置
    //   const updateNode = (nodeList, updatedNode) => {
    //     for (let i = 0; i < nodeList.length; i++) {
    //       if (nodeList[i].id === updatedNode.id) {
    //         nodeList[i] = { ...updatedNode }
    //         return true
    //       } else if (nodeList[i].children && nodeList[i].children.length > 0) {
    //         const found = updateNode(nodeList[i].children, updatedNode)
    //         if (found) {
    //           return true
    //         }
    //       }
    //     }
    //     return false
    //   }
    //
    //   updateNode(this.testFishData, this.editableNode)
    //   // 打印修改后的节点信息
    //   console.log('保存的节点信息:', this.editableNode)
    //   // 重新初始化鱼骨图，以反映更新后的数据
    //   this.initFishBone()
    //   // 关闭编辑框
    //   this.editDialogVisible = false
    // }
  },
}
</script>
