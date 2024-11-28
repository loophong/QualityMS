<template>
  <div class="mod-home">
    <div id="indicator" class="box">
      <div style="height: 10%"></div>
      <div id="indicatorChart1" ref="indicatorChart1"></div>
      <div id="indicatorChart2" ref="indicatorChart2"></div>
    </div>


    <div id="issue" class="box">
      <div style="height: 10%"></div>
      <div id="issueChart"></div>
    </div>


  </div>
</template>

<style>
.mod-home {
  display: flex;
  flex-wrap: wrap;
  /* 自动换行 */
  gap: 20px;
  /* 每个区域之间的间距 */
}


.box {
  font-size: 24px;
  /* 设置字体大小 */
  flex: 1 1 40%;
  /* 每个区域占 30% 宽度 */
  text-align: center;
  padding: 20px;
  border-radius: 4px;
  min-width: 200px;
  /* 设置最小宽度，防止过小 */
  height: 400px;
  /* 设置每个容器的高度为父容器的百分比 */
}
</style>
