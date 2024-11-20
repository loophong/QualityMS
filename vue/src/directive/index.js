import removeAria from './radio/removeAria'

const install = function (Vue) {
  Vue.directive('remove-aria', removeAria)
}

if (window.Vue) {
  window['remove-aria'] = removeAria
  Vue.use(install); // eslint-disable-line
}

export default install
