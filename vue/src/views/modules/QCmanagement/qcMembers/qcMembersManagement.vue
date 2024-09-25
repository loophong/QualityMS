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
        <el-button type="danger" @click="toIssue()">问题添加</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="tableData" stripe border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%;" row-key="id">
      <el-table-column header-align="center" align="center" label="" width="40">
      </el-table-column>
      <el-table-column prop="groupName" header-align="center" align="center" label="小组名" width="300">
      </el-table-column>
      <el-table-column prop="name" header-align="center" align="center" label="姓名" width="120">

      </el-table-column>
      <el-table-column prop="number" header-align="center" align="center" label="员工编号" width="200">
      </el-table-column>
      <el-table-column prop="roleInTopic" header-align="center" align="center" label="组内角色" width="140">
      </el-table-column>
      <el-table-column prop="date" header-align="center" align="center" label="加入小组时间" width="280">
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" label="操作">
        <template slot-scope="scope">
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
import AddOrUpdate from './qcMembersManagement-add-or-update'

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
      pageSize: 10000,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      map: {},
      GroupMemberList: [],
      membersOptions: []
    }
  },
  components: {
    AddOrUpdate
  },
  async activated() {
    await this.getDataList().then(groupList => {
      this.groupList = groupList;
    });
    // 获取分组后的员工数据
    await this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
      method: 'get',
    }).then(({ data }) => {
      this.membersOptions = data;
      // console.log(this.membersOptions);
    });
  },
  computed: {
    // formattedName() {
    //     return this.tableData.map(row => {
    //         const value = row.name.toString();
    //         for (const optionGroup of this.optionsData) {
    //             const option = optionGroup.options.find(opt => opt.value === value);
    //             if (option) {
    //                 return option.label;
    //             }
    //         }
    //         return '--';
    //     });
    // },
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
            groupList = this.dataList.filter(function (item) {
              return item.deleteFlag !== 1;
            });
            console.log(groupList);
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
    toIssue() {
      this.$router.push(
        {
          name: 'otherToIssue',
          // query: {
          //   data: JSON.stringify(filteredArray)
          // }
        });
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.isAddMember = false;
        this.$refs.addOrUpdate.membersOptions = this.membersOptions;
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 新增成员
    addMemberHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.isAddMember = true;
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    //   deleteHandle(id) {
    //     var ids = id ? [id] : this.dataListSelections.map(item => {
    //       return item.qcgmId
    //     })
    //     this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
    //       confirmButtonText: '确定',
    //       cancelButtonText: '取消',
    //       type: 'warning'
    //     }).then(() => {
    //       this.$http({
    //         url: this.$http.adornUrl('/qcMembers/qcGroupMember/delete'),
    //         method: 'post',
    //         data: this.$http.adornData(ids, false)
    //       }).then(({ data }) => {
    //         if (data && data.code === 0) {
    //           this.$message({
    //             message: '操作成功',
    //             type: 'success',
    //             duration: 1500,
    //             onClose: () => {
    //               this.getDataList()
    //             }
    //           })
    //         } else {
    //           this.$message.error(data.msg)
    //         }
    //       })
    //     })
    //   }
    // }
    //删除
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
          url: this.$http.adornUrl(`/qcMembers/qcGroupMember/update`),
          method: 'post',
          data: this.$http.adornData({
            'qcgmId': id || undefined,
            'deleteFlag': 1,
          })
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

<!-- this.$http({
  url: this.$http.adornUrl(`/qcMembers/qcGroupMember/${!this.dataForm.qcgmId ? 'save' : 'update'}`),
  method: 'post',
  data: this.$http.adornData({
    'qcgmId': this.dataForm.qcgmId || undefined,
    'name': this.dataForm.mainName,
    'gender': this.dataForm.gender,
    'telNumber': this.dataForm.telNumber,
    'number': this.dataForm.number,
    'education': this.dataForm.education,
    'department': this.dataForm.department,
    'position': this.dataForm.position,
    'team': this.dataForm.team,
    'participationDate': this.dataForm.participationDate,
    'topic': this.dataForm.topic,
    'roleInTopic': '管理员',
    'deleteFlag': 0,
    'groupName': this.dataForm.groupName
  })
}).then(({ data }) => {
  if (data && data.code === 0) {
    this.$message({
      message: '操作成功',
      type: 'success',
      duration: 1500,
      onClose: () => {
        this.visible = false;
        this.$emit('refreshDataList');
      }
    }); -->
