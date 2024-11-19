export default {
  bind (el, binding) {
    // 获取节点
    let ariaEls = el.querySelectorAll('.el-radio__original')
    ariaEls.forEach((item) => {
      item.removeAttribute('aria-hidden')
    })
  }
}
