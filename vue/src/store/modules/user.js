export default {
  namespaced: true,
  state: {
    id: 0,
    //name 为编号, 例如 '68004367'
    name: '',
    //nickname 为名称, 例如 '陈经理'
    nickname: '',
  },
  mutations: {
    updateId (state, id) {
      state.id = id
    },
    updateName (state, name) {
      state.name = name
    },
    //新增
    updateNickname (state, nickname) {
      state.nickname = nickname
    }

  }
}
