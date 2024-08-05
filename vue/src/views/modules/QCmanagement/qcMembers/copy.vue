<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('qcMembers:qcGroupMember:save')" type="primary"
          @click="addOrUpdateHandle()">新增小组</el-button>
        <!-- <el-button v-if="isAuth('qcMembers:qcGroupMember:delete')" type="danger" @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0">批量删除</el-button> -->
      </el-form-item>
    </el-form>
    <el-table :data="tableData" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;" row-key="id">
      <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column> -->
      <el-table-column header-align="center" align="center" label="" width="40">
      </el-table-column>
      <el-table-column prop="qcgmId" header-align="center" align="center" label="小组ID" width="70">
      </el-table-column>
      <el-table-column prop="groupName" header-align="center" align="center" label="小组名" width="200">
      </el-table-column>
      <el-table-column prop="name" header-align="center" align="center" label="姓名" width="100">
      </el-table-column>
      <!-- <el-table-column prop="gender" header-align="center" align="center" label="性别">
      </el-table-column> -->
      <!-- <el-table-column prop="telNumber" header-align="center" align="center" label="手机号">
      </el-table-column> -->
      <el-table-column prop="number" header-align="center" align="center" label="员工编号" width="140">
      </el-table-column>
      <el-table-column prop="roleInTopic" header-align="center" align="center" label="组内角色" width="80">
      </el-table-column>
      <!-- <el-table-column prop="education" header-align="center" align="center" label="学历">
      </el-table-column>
      <el-table-column prop="department" header-align="center" align="center" label="部门">
      </el-table-column>
      <el-table-column prop="position" header-align="center" align="center" label="岗位">
      </el-table-column>
      <el-table-column prop="team" header-align="center" align="center" label="班组">
      </el-table-column> -->
      <el-table-column prop="date" header-align="center" align="center" label="加入小组时间" width="240">
      </el-table-column>
      <!-- <el-table-column label="参加QC时间" align="center" prop="participationDate" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.participationDate, '{y}-{m}-{d}') }}</span>
        </template>
</el-table-column> -->
      <!-- <el-table-column prop="topic" header-align="center" align="center" label="QC课题">
      </el-table-column>
       -->
      <!-- <el-table-column prop="deleteFlag" header-align="center" align="center" label="逻辑删除flag">
      </el-table-column> -->
      <el-table-column fixed="right" header-align="center" align="center" label="操作">
        <template slot-scope="scope">
          <!-- TODO -->
          <el-button v-if="!scope.row.parentId" type="text" size="small"
            @click="addMemberHandle(scope.row.id)">新增成员</el-button>
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './copy-add-or-update copy'

export default {
  data() {
    return {
      dataForm: {
        key: ''
      },
      dataList: [],
      groupList: [],
      originList: [],
      tableData: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      map: {}
    }
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList().then(groupList => {
      this.groupList = groupList;
      this.groupBy();
    });
  },
  computed: {
    isAdmin() {
      if (!this.dataForm.parentId || this.dataForm.parentId == '') {
        return true;
      } else {
        return false;
      }
    }
  },
  methods: {
    // 获取数据列表
    getDataList() {
      return new Promise((resolve, reject) => {
        let groupList = [];
        this.dataListLoading = true;
        this.$http({
          url: this.$http.adornUrl('/qcMembers/qcGroupMember/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list;
            this.totalPage = data.page.totalCount;
            groupList = this.dataList;
            // 分组
            this.tableData = []; // 清空 tableData
            const map = {};
            groupList.forEach(item => {
              if (item.parentId === null) {
                map[item.qcgmId] = {
                  id: item.qcgmId,
                  date: item.participationDate,
                  name: item.name,
                  number: item.number,
                  groupName: item.groupName,
                  roleInTopic: '管理员',
                  children: []
                };
              }
            });
            groupList.forEach(item => {
              if (item.parentId !== null && map[item.parentId]) {
                map[item.parentId].children.push({
                  id: item.qcgmId,
                  date: item.participationDate,
                  name: item.name,
                  number: item.number,
                  roleInTopic: item.roleInTopic,
                  parentId: item.parentId,
                });
              }
            });
            this.tableData = Object.values(map);
          } else {
            this.dataList = [];
            this.totalPage = 0;
          }
          this.dataListLoading = false;
          resolve(groupList);
        }).catch(error => {
          console.error('Error fetching data:', error);
          this.dataList = [];
          this.totalPage = 0;
          this.dataListLoading = false;
          reject(error);
        });
      });
    },

    //分组
    // groupBy() {
    //   this.groupList.forEach(parent => {
    //     if (parent.parentId === null) {
    //       const formattedParent = {
    //         id: parent.qcgmId,
    //         date: parent.participationDate,
    //         name: parent.name,
    //         number: parent.number,
    //         groupName: parent.groupName,
    //         children: [] // 初始化子级数组
    //       };
    //       // 查找该父级的所有子级条目
    //       const children = this.groupList.filter(child => child.parentId === parent.qcgmId);
    //       // 将每个子级条目添加到 formattedParent 的 children 数组中
    //       children.forEach(child => {
    //         formattedParent.children.push({
    //           id: child.qcgmId,
    //           date: child.participationDate,
    //           name: child.name,
    //           number: child.number,
    //           parentId: child.parentId,
    //         });
    //       });
    //       // 将格式化后的父级对象添加到 tableData 数组中
    //       this.tableData.push(formattedParent);
    //     }
    //   });
    // },
    parseTime(time) {
      return new Date(time).toLocaleString();
    },

    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.qcgmId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/qcMembers/qcGroupMember/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    }
  }
}
</script>
