<template>
  <nav class="site-navbar" :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand" @click="$router.push({ name: 'home' })">
        <a class="site-navbar__brand-lg" href="javascript:;">质量目标分解及工具应用管理系统</a>
        <a class="site-navbar__brand-mini" href="javascript:;">企管</a>
      </h1>
    </div>
    <div class="site-navbar__body clearfix">
      <el-menu
        class="site-navbar__menu"
        mode="horizontal">
        <el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
          <icon-svg name="zhedie"></icon-svg>
        </el-menu-item>
      </el-menu>
      <el-menu
        class="site-navbar__menu site-navbar__menu--right"
        mode="horizontal">
<!--        <el-menu-item index="1" @click="$router.push({ name: 'theme' })">-->
<!--          <template slot="title">-->
<!--            <el-badge value="new">-->
<!--              <icon-svg name="shezhi" class="el-icon-setting"></icon-svg>-->
<!--            </el-badge>-->
<!--          </template>-->
<!--        </el-menu-item>-->
<!--        <el-menu-item index="2">-->
<!--          <el-badge value="hot">-->
<!--            <a href="https://www.renren.io/" target="_blank">官方社区</a>-->
<!--          </el-badge>-->
<!--        </el-menu-item>-->
<!--        <el-submenu index="3">-->
<!--          <template slot="title">Git源码</template>-->
<!--          <el-menu-item index="2-1"><a href="https://github.com/renrenio/renren-fast-vue" target="_blank">前端</a></el-menu-item>-->
<!--          <el-menu-item index="2-2"><a href="https://gitee.com/renrenio/renren-fast" target="_blank">后台</a></el-menu-item>-->
<!--          <el-menu-item index="2-3"><a href="https://gitee.com/renrenio/renren-generator" target="_blank">代码生成器</a></el-menu-item>-->
<!--        </el-submenu>-->
<!--        <el-menu-item class="site-navbar__avatar" index="3">-->
<!--          <el-dropdown :show-timeout="0" placement="bottom">-->
<!--            <span class="el-dropdown-link">-->
<!--              <img src="~@/assets/img/avatar.png" :alt="userName">{{ userName }}-->
<!--            </span>-->
<!--            <el-dropdown-menu slot="dropdown">-->
<!--              <el-dropdown-item @click.native="updatePasswordHandle()">修改密码</el-dropdown-item>-->
<!--              <el-dropdown-item @click.native="updatePasswordHandle()">消息通知</el-dropdown-item>-->
<!--              <el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>-->
<!--            </el-dropdown-menu>-->
<!--          </el-dropdown>-->
<!--        </el-menu-item>-->
        <el-menu-item class="site-navbar__avatar" index="3">
          <el-dropdown :show-timeout="0" placement="bottom">
              <span class="el-dropdown-link">
                <!-- 新增 将原来的 'username' 显示 改为 'username+nickname' -->
                <img src="~@/assets/img/avatar.png" :alt="userName">{{ userName + "(" + nickName + ")"}}
                  <template v-if="hasUnreadMessages">
                    <el-badge value="new">

                    </el-badge>
                  </template>
              </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="updatePasswordHandle()">修改密码</el-dropdown-item>
<!--              <el-dropdown-item-->
<!--                @click.native="openNotice()"-->
<!--                :style="{ color: hasUnreadMessages ? 'red' : 'inherit' }">-->
<!--                消息通知-->
<!--              </el-dropdown-item>-->
              <el-dropdown-item
                @click.native="openNotice"
                class="menu-notification"
              >
                <span>
                  消息通知
                  <el-badge
                    v-if="hasUnreadMessages"
                    class="notification-dot"
                    type="error"
                    value=""
                  ></el-badge>
                </span>
              </el-dropdown-item>
              <el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>

      </el-menu>
    </div>
    <!-- 弹窗, 修改密码 -->
    <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
  </nav>
</template>


<script>
  import UpdatePassword from './main-navbar-update-password'
  import { clearLoginInfo } from '@/utils'
  export default {
    data () {
      return {
        updatePassowrdVisible: false,
        hasUnreadMessages: '', // 存储未读消息列表
        nickName: ''
      }
    },
    components: {
      UpdatePassword
    },
    computed: {
      navbarLayoutType: {
        get () { return this.$store.state.common.navbarLayoutType }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold },
        set (val) { this.$store.commit('common/updateSidebarFold', val) }
      },
      mainTabs: {
        get () { return this.$store.state.common.mainTabs },
        set (val) { this.$store.commit('common/updateMainTabs', val) }
      },
      userName: {
        get () { return this.$store.state.user.name }
      },
      // 获取 @TableName("sys_user") 的属性 'nickname'
      // nickName: {
      //   get () { return this.$store.state.user.nickname }
      // }

    },

    created (){
      this.messageReport()
      this.fetchUnreadNotices()
      //新增 获取用户nickname
      this.getNicknameById(this.$store.state.user.id)
    },
    methods: {

      getNicknameById(id){
        this.$http({
          url: this.$http.adornUrl(`/sys/user/getUserInfoById/${id}`),
          method: 'get',
        }).then(({ data }) => {
          if (data && data.code === 0) {
              this.nickName = data.user.nickname;
          }
          // console.log("this.nickName == >" + this.nickName)
        });

      },
      openNotice() {
        this.$router.push({
          name: 'toNotice',
          params: {
          }
        })
      },
      fetchUnreadNotices() {
        this.$http({
          url: this.$http.adornUrl('/notice/Unread'),
          method: 'get'
        }).then(({ data }) => {
          if (data && data.code === 0) {
            console.log("获取的消息有", data.hasUnread);
            // 根据返回值判断是否有未读消息
            this.hasUnreadMessages = data.hasUnread ; // 1有未读，0没有
            console.log("获取的未读消息有", this.hasUnreadMessages);
          }
        });
      },
      // 修改密码
      updatePasswordHandle () {
        this.updatePassowrdVisible = true
        this.$nextTick(() => {
          this.$refs.updatePassowrd.init()
        })
      },
      // 退出
      logoutHandle () {
        this.$confirm(`确定进行[退出]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/logout'),
            method: 'post',
            data: this.$http.adornData()
          }).then(({data}) => {
            if (data && data.code === 0) {
              clearLoginInfo()
              this.$router.push({ name: 'login' })
            }
          })
        }).catch(() => {})
      },
      messageReport(){
        console.log("消息通知能加载....")
      }
    }
  }
</script>

<style scoped>
.notification-dot {
  margin-left: 8px; /* 让小红点与“消息通知”文字有间距 */
  height: 10px; /* 小红点大小 */
  width: 10px;
  border-radius: 50%;
  display: inline-block;
  position: relative;
  top: -2px; /* 调整垂直对齐 */
  background-color: red; /* 小红点颜色 */
}
.site-navbar__brand-lg {
  font-size: 14px;
}
</style>

