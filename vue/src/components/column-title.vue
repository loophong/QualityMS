<template>
  <div>
    <div class="title">{{ title }}</div>
  </div>
</template>

<script>
export default {
  name: 'ColumnTitle',
  props: {
    title: {
      type: String,
      default: '',
    },
  },
}
</script>

<style lang="scss" scoped>
.title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 20px;
  position: relative;
  background: #ededed;
  padding: 10px 20px;
}
</style>
