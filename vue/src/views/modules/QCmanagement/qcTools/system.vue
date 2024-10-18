<template>
  <div>
    <vue2-org-tree
      :data="treeData"
      horizontal="true"
      collapsable
      :render-content="renderContent"
      @on-expand="onExpand"
      @on-node-click="NodeClick"/>
    <div v-if="showMenu" class="context-menu" :style="menuStyle">
      <button @click="editNode(selectedNode)">编辑</button>
      <button @click="addNode(selectedNode)">添加</button>
      <button @click="deleteNode(selectedNode.id)">删除</button>
    </div>
    <button @click="downloadScreenshot">下载截图</button>
  </div>
</template>

<script>
import html2canvas from 'html2canvas';
import { saveAs } from 'file-saver';

export default {
  data() {
    return {
      treeData: {
        id: '0',
        label: "NBA季后赛",
        children: [
          {
            id: '1',
            label: "西部球队",
            children: [
              {
                id: '1-1',
                label: "勇士"
              },
              {
                id: '1-2',
                label: "火箭"
              },
              {
                id: '1-3',
                label: "太阳"
              },
              {
                id: '1-4',
                label: "小牛"
              }
            ]
          },
          {
            id: '2',
            label: "东部球队",
            children: [
              {
                id: '2-1',
                label: "热火"
              },
              {
                id: '2-2',
                label: "雄鹿"
              },
              {
                id: '2-3',
                label: "骑士"
              },
              {
                id: '2-4',
                label: "凯尔特人"
              },
              {
                id: '2-5',
                label: "76人"
              }
            ]
          }
        ]
      },
      showMenu: false,
      menuStyle: {
        top: '0px',
        left: '0px'
      },
      selectedNode: null
    };
  },

  created() {
    this.toggleExpand(this.treeData, true);
  },

  methods: {

    addNode(data) {
      const newNode = {
        id: `${data.id}-${Date.now()}`,
        label: `新节点 ${Date.now()}`
      };
      if (!data.children) {
        this.$set(data, 'children', []);
      }
      data.children.push(newNode);
      this.showMenu = false; // 隐藏菜单
    },

    editNode(data) {
      const newLabel = prompt("请输入新的节点名称", data.label);
      if (newLabel !== null) {
        this.$set(data, 'label', newLabel);
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
        data.children = data.children.map(child => {
          const result = this.removeNodeById(child, nodeId);
          return result ? result : null;
        }).filter(Boolean); // 过滤掉 null 值
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
      console.log(data, 'data');
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
      return h('div', { class: 'custom-tree-node' }, [
        h('span', { on: { click: (e) => this.NodeClick(e, data) } }, data.label)
      ]);
    },

    handleClickOutside(event) {
      if (this.showMenu && !event.target.closest('.context-menu')) {
        this.showMenu = false;
      }
    },

    downloadScreenshot() {
      const element = document.querySelector('.org-tree-container'); // 获取需要截图的元素
      html2canvas(element).then(canvas => {
        const imgData = canvas.toDataURL('image/png');
        const blob = this.dataURLtoBlob(imgData);
        saveAs(blob, 'tree-screenshot.png');
      });
    },

    dataURLtoBlob(dataurl) {
      const arr = dataurl.split(',');
      const mime = arr[0].match(/:(.*?);/)[1];
      const bstr = atob(arr[1]);
      let n = bstr.length;
      const u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new Blob([u8arr], { type: mime });
    }
  }
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
  background: rgba(0, 0, 0, .7);
  color: #fff;
  border-radius: 15px;
  padding: 15px;
  transition: all 0.3s;
  z-index: 999;
  text-align: left;
  font-size: 12px;
}
</style>
