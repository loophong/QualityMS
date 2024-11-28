<template>
  <div>
    <div>
      <span>
        <el-select v-model="value" filterable @change="handleSelectChange" placeholder="请选择模版">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <!-- <el-button type="danger" @click="handleDelete">删除当前模版</el-button> -->
      </span>
    </div>
    <div>
      <vue2-org-tree :data="treeData" horizontal collapsable :render-content="renderContent" @on-expand="onExpand"
        @on-node-click="NodeClick" />
      <div v-if="showMenu" class="context-menu" :style="menuStyle">
        <button @click="editNode(selectedNode)">编辑</button>
        <button @click="addNode(selectedNode)">添加</button>
        <button @click="deleteNode(selectedNode.id)">删除</button>
      </div>

      <el-button type="success" @click="dialogFormVisible = true">保存当前数据</el-button>
      <!-- <el-button type="success" @click="handleUp">更新当前数据</el-button> -->

      <el-button type="warning" @click="downloadScreenshot">下载截图</el-button>
    </div>
    <el-dialog title="自定义图名" :visible.sync="dialogFormVisible" append-to-body>
      <el-input v-model="inputName" placeholder="请输入图名" style="width: 50%"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleUp">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import html2canvas from "html2canvas";
import { saveAs } from "file-saver";

export default {
  //在qcPlanNew.vue 中应用this, 传递过来的参数
  props: {
    conplanSubject: {
      type: Number,
      required: true,
    },
    conplanProcess: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      treeData: {
        id: "0",
        label: "直拉钢丝亮丝不良",
        children: [
          {
            id: "1",
            label: "粗拉钢丝表面涂硼不良",
            children: [
              {
                id: "1-1",
                label: "涂硼液位偏低",
              },
              {
                id: "1-2",
                label: "涂硼温度低",
              },
              {
                id: "1-3",
                label: "涂硼浓度偏低",
              },
              {
                id: "1-4",
                label: "涂硼后钢丝表面潮湿",
              },
            ],
          },
          {
            id: "2",
            label: "拉拔工艺不合理",
            children: [
              {
                id: "2-1",
                label: "拉拔压缩率不合理",
              },
              {
                id: "2-2",
                label: "进线规格不合理",
              },
              {
                id: "2-3",
                label: "拉拔速度过大",
              },
              {
                id: "2-4",
                label: "涂硼浓度标准不合理",
              },
            ],
          },
        ],
      },
      options: [],
      value: "",
      name: "",
      inputName: "",
      showMenu: false,
      dialogFormVisible: false,
      menuStyle: {
        top: "0px",
        left: "0px",
      },
      selectedNode: null,
      currentUserName: '',
    };
  },

  created() {
    this.toggleExpand(this.treeData, true);
    this.getUserName();
    this.getTemplateData();
  },

  methods: {
    async getUserName() {
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/user"),
        method: "get",
        params: this.$http.adornParams({
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.currentUserName = data.userName;
        } else {
        }

      });
    },
    handleUp() {
      console.log(this.treeData);
      console.log(this.inputName);
      console.log(JSON.stringify(this.treeData));
      this.dialogFormVisible = false;
      this.addTemplate();
    },
    //保存为模版
    async addTemplate() {
      let imgData;
      const element = document.querySelector(".org-tree-container"); // 获取需要截图的元素
      await html2canvas(element).then((canvas) => {
        imgData = canvas.toDataURL("image/png");
      });
      await this.$http({
        url: this.$http.adornUrl(`/qcTools/conplan/save`),
        method: "post",
        data: this.$http.adornData({
          conplanId: undefined,
          conplanName: this.inputName || "未命名",
          conplanType: "系统图",
          conplanText: undefined,
          conplanSeries: JSON.stringify(this.treeData),
          conplanAxis: undefined,
          conplanSubject: this.conplanSubject,
          conplanProcess: this.conplanProcess,
          conplanUrl: JSON.stringify(imgData),
          conplanUser: this.currentUserName,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: "操作成功",
            type: "success",
            duration: 1500,
            onClose: () => {
              this.visible = false;
              this.$emit("refreshDataList");
            },
          });
        } else {
          this.$message.error(data.msg);
        }
      });
    },
    //获取模版数据
    async getTemplateData() {
      await this.$http({
        url: this.$http.adornUrl("/qcTools/template/templateList"),
        // url: this.$http.adornUrl("/qcTools/conplan/TspList"),
        method: "get",
        params: this.$http.adornParams({
          templateType: "系统图",
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.resultList = data.resultList.map((row) => ({
            templateId: row.templateId,
            templateName: row.templateName,
            templateType: row.templateType,
            templateText: row.templateText,
            templateSeries: JSON.parse(row.templateSeries),

          }));
          this.options = data.resultList.map((item) => ({
            value: item.templateId,
            label: item.templateName,
          }));
          console.log(this.resultList);
        } else {
          this.options = [];
        }

        // // 将获取的数据传递给 this.treeData
        // if (this.resultList.length != 0) {
        //   this.resultList.forEach((item) => {
        //     this.treeData = item.templateSeries;
        //   });
        // }
        // //渲染数据
        // this.toggleExpand(this.treeData, true);
      });
    },
    //删除当前模版
    handleDelete() {
      let ids = [this.value];
      console.log(ids);
      if (ids) {
        this.$confirm(`确定对 [${this.name}] 进行删除?`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl("/qcTools/template/delete"),
            method: "post",
            data: this.$http.adornData(ids, false),
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.name = "";
                  this.value = "";
                  // this.toggleExpand(this.treeData, true);
                  this.getTemplateData();
                },
              });
            } else {
              this.$message.error(data.msg);
            }
          });
        });
      } else {
        this.$message({
          message: "未选择",
          type: "info",
          duration: 1500,
        });
      }
    },
    handleSelectChange(value) {
      console.log(this.treeData);
      this.resultList.forEach((item) => {
        if (item.templateId == this.value) {
          console.log(item.templateSeries);
          this.treeData = item.templateSeries;
          this.name = item.templateName;
        }
      });
    },

    addNode(data) {
      const newNode = {
        id: `${data.id}-${Date.now()}`,
        label: `新节点 ${Date.now()}`,
      };
      if (!data.children) {
        this.$set(data, "children", []);
      }
      data.children.push(newNode);
      this.showMenu = false; // 隐藏菜单
    },

    editNode(data) {
      const newLabel = prompt("请输入新的节点名称", data.label);
      if (newLabel !== null) {
        this.$set(data, "label", newLabel);
      }
      this.showMenu = false; // 隐藏菜单
    },

    deleteNode(nodeId) {
      this.treeData = this.removeNodeById(this.treeData, nodeId);
      this.showMenu = false; // 隐藏菜单
    },

    removeNodeById(data, nodeId) {
      if (data.id === nodeId) {
        return null;
      }

      if (data.children) {
        data.children = data.children
          .map((child) => {
            const result = this.removeNodeById(child, nodeId);
            return result ? result : null;
          })
          .filter(Boolean); // 过滤掉 null 值
      }

      return data;
    },

    collapse(list) {
      list.forEach((child) => {
        if (child.expand) {
          child.expand = false;
        }
        child.children && this.collapse(child.children);
      });
    },

    onExpand(e, data) {
      console.log(data, "data");
      if ("expand" in data) {
        data.expand = !data.expand;
        if (!data.expand && data.children) {
          this.collapse(data.children);
        }
      } else {
        this.$set(data, "expand", true);
      }
    },

    toggleExpand(data, val) {
      if (Array.isArray(data)) {
        data.forEach((item) => {
          this.$set(item, "expand", val);
          if (item.children) {
            this.toggleExpand(item.children, val);
          }
        });
      } else {
        this.$set(data, "expand", val);
        if (data.children) {
          this.toggleExpand(data.children, val);
        }
      }
    },

    NodeClick(e, data) {
      e.stopPropagation();
      this.selectedNode = data;
      this.showMenu = true;
      this.menuStyle.top = `${e.clientY}px`;
      this.menuStyle.left = `${e.clientX}px`;
    },

    renderContent(h, data) {
      return h("div", { class: "custom-tree-node" }, [
        h(
          "span",
          { on: { click: (e) => this.NodeClick(e, data) } },
          data.label
        ),
      ]);
    },

    handleClickOutside(event) {
      if (this.showMenu && !event.target.closest(".context-menu")) {
        this.showMenu = false;
      }
    },

    downloadScreenshot() {
      const element = document.querySelector(".org-tree-container"); // 获取需要截图的元素
      html2canvas(element).then((canvas) => {
        const imgData = canvas.toDataURL("image/png");
        console.log(imgData);
        const blob = this.dataURLtoBlob(imgData);
        console.log(blob);
        saveAs(blob, "tree-screenshot.png");
      });
    },

    dataURLtoBlob(dataurl) {
      const arr = dataurl.split(",");
      const mime = arr[0].match(/:(.*?);/)[1];
      const bstr = atob(arr[1]);
      let n = bstr.length;
      const u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new Blob([u8arr], { type: mime });
    },
  },
};
</script>

<style lang="less">
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;

  span:first-child {
    cursor: pointer;
  }
}

.context-menu {
  position: fixed;
  background: white;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
  padding: 5px;
  z-index: 1000;

  button {
    display: block;
    width: 100%;
    margin: 2px 0;
    padding: 5px;
    text-align: left;
    cursor: pointer;
  }
}

.show-info {
  position: absolute;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  border-radius: 15px;
  padding: 15px;
  transition: all 0.3s;
  z-index: 999;
  text-align: left;
  font-size: 12px;
}
</style>
