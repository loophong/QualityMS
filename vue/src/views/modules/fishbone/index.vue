<template>
  <div id="fishBone"></div>
</template>

<script>
import { FishBones } from './FishBone';
import { testFishData } from './testFishData';

export default {
  name: 'FishBoneComponent',
  data() {
    return {
      fishData: testFishData, // 直接引用导入的数据
    };
  },
  mounted() {
    new FishBones({
      // 模板id
      id: 'fishBone',
      /* json数据！重要，必填 */
      jsonData: this.fishData, // 使用data中的fishData
      canvasSize: [document.body.scrollWidth, document.body.scrollHeight],
      // 右键事件
      rClickNodeFunction: (node, event) => {
        this.handleRightClick(node, event);
      },
      // 点击事件
      clickNodeFunction: (node, event) => {
        this.handleClick(node, event);
      },
      // 当鼠标在元素上移动时事件
      mouseOverFunction: (node, event) => {
        this.handleMouseOver(node, event);
      },
    }).init();
  },
  methods: {
    handleRightClick(node, event) {
      // 右键点击处理函数
    },
    handleClick(node, event) {
      // 左键点击处理函数
    },
    handleMouseOver(node, event) {
      // 鼠标悬停处理函数
    },
  },
};
</script>
