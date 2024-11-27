<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="管理员" name="1" v-if="isAuth('qcManagement:group:admin')">
      <div class="mod-config">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
          <!-- <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item> -->
          <el-form-item>
            <!-- <el-button @click="getDataList()">查询</el-button> -->
            <el-button v-if="isAuth('qcMembers:qcGroupMember:save')" type="primary"
              @click="addOrUpdateHandle()">新增小组</el-button>
            <el-button type="danger" @click="toIssue()">问题添加</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="tableData" stripe border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%;" row-key="id">
          <el-table-column header-align="center" align="center" label="" width="40">
          </el-table-column>
          <el-table-column prop="examineStatus" header-align="center" align="center" label="小组审核状态" width="120">
            <template slot-scope="scope">
              <span v-if="scope.row.examineStatus === '待审核'" style="color: blue;">{{ scope.row.examineStatus }}</span>
              <span v-else-if="scope.row.examineStatus === '未通过'" style="color: red;">{{ scope.row.examineStatus
                }}</span>
              <span v-else-if="scope.row.examineStatus == '通过'" style="color: #8dc146;">{{ scope.row.examineStatus
                }}</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="groupName" header-align="center" align="center" label="小组名" width="160">
          </el-table-column>
          <el-table-column prop="memberName" header-align="center" align="center" label="姓名" width="120">
          </el-table-column>
          <el-table-column prop="number" header-align="center" align="center" label="员工编号" width="160">
          </el-table-column>
          <el-table-column prop="roleInTopic" header-align="center" align="center" label="组内角色" width="140">
          </el-table-column>
          <el-table-column prop="department" header-align="center" align="center" label="单位" width="140">
          </el-table-column>
          <el-table-column prop="team" header-align="center" align="center" label="小组类型" width="140">
          </el-table-column>
          <el-table-column prop="registrationNum" header-align="center" align="center" label="注册号" width="160">
          </el-table-column>
          <el-table-column prop="date" header-align="center" align="center" label="加入小组时间" width="180">
          </el-table-column>
          <el-table-column fixed="right" v-if="isAuth('qcMembers:qcGroupMember:save')" header-align="center"
            align="center" label="操作">
            <template slot-scope="scope">
              <el-button v-if="(!scope.row.parentId && isAuth('qcMembers:qcGroupMember:save'))" type="text" size="small"
                @click="addMemberHandle(scope.row.id, scope.row.memberName)">新增成员</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcMembers:qcGroupMember:update')"
                @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcMembers:qcGroupMember:delete')"
                @click="deleteHandle(scope.row.id)">删除</el-button>
              <!-- <el-button type="text" size="small" v-if="scope.row.parentId&&isAuth('qcMembers:leader:change')"
                @click="leaderChangeHandle(scope.row.id, scope.row.parentId)">移交组长</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100, 1000]" :page-size="1000" :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination> -->

      </div>
    </el-tab-pane>
    <el-tab-pane label="小组管理" name="2" v-else>
      <div class="mod-config">
        <el-form :inline="true" :model="dataForm">
          <el-form-item>
            <!-- <el-button @click="getDataList()">查询</el-button> -->
            <el-button v-if="isAuth('qcMembers:qcGroupMember:save')" type="primary"
              @click="addOrUpdateHandle()">新增小组</el-button>
            <el-button type="danger" @click="toIssue()">问题添加</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="dataList" stripe border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
          style="width: 100%;" row-key="id">
          <el-table-column header-align="center" align="center" label="" width="40">
          </el-table-column>
          <el-table-column prop="groupName" header-align="center" align="center" label="小组名" width="160">
          </el-table-column>
          <el-table-column prop="examineStatus" header-align="center" align="center" label="小组审核状态" width="140">
            <template slot-scope="scope">
              <span v-if="scope.row.examineStatus === '待审核'" style="color: blue;">{{ scope.row.examineStatus }}</span>
              <span v-else-if="scope.row.examineStatus === '未通过'" style="color: red;">{{ scope.row.examineStatus
                }}</span>
              <span v-else-if="scope.row.examineStatus == '通过'" style="color: #8dc146;">{{ scope.row.examineStatus
                }}</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="memberName" header-align="center" align="center" label="姓名" width="120">
          </el-table-column>
          <el-table-column prop="number" header-align="center" align="center" label="员工编号" width="160">
          </el-table-column>
          <el-table-column prop="roleInTopic" header-align="center" align="center" label="组内角色" width="140">
          </el-table-column>
          <el-table-column prop="department" header-align="center" align="center" label="单位" width="140">
          </el-table-column>
          <el-table-column prop="team" header-align="center" align="center" label="小组类型" width="140">
          </el-table-column>
          <el-table-column prop="registrationNum" header-align="center" align="center" label="注册号" width="160">
          </el-table-column>
          <el-table-column prop="date" header-align="center" align="center" label="加入小组时间" width="180">
          </el-table-column>
          <el-table-column fixed="right" v-if="isAuth('qcMembers:qcGroupMember:save')" header-align="center"
            align="center" label="操作">
            <template slot-scope="scope">
              <el-button v-if="(!scope.row.parentId && isAuth('qcMembers:qcGroupMember:save'))" type="text" size="small"
                @click="addMemberHandle(scope.row.id, scope.row.memberName)">新增成员</el-button>
              <el-button
                v-if="(!scope.row.parentId && isAuth('qcMembers:qcGroupMember:save') && scope.row.examineStatus === '未通过')"
                type="text" size="small" @click="handleReexamine(scope.row.id)">提交审核</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcMembers:qcGroupMember:update')"
                @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
              <el-button type="text" size="small" v-if="isAuth('qcMembers:qcGroupMember:delete')"
                @click="deleteHandle(scope.row.id)">删除</el-button>
              <el-button type="text" size="small" v-if="scope.row.parentId && scope.row.management"
                @click="changeHandle(scope.row.id, scope.row.parentId)">移交组长</el-button>
              <!-- <el-button type="text" size="small" @click="ad(scope.row)">移交组长</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100, 999]" :page-size="999" :total="totalPageGroup"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination> -->
        <!-- 弹窗, 新增 / 修改 -->
        <!-- <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update> -->
      </div>
    </el-tab-pane>
    <el-tab-pane label="小组审核" name="3" v-if="isAuth('qcManagement:group:examine')">
      <div class="mod-config">
        <!-- <el-form :inline="true" :model="dataForm" >
        </el-form> -->
        <el-table :data="examineList" stripe border v-loading="dataListLoading"
          @selection-change="selectionChangeHandle" style="width: 100%;" row-key="id">
          <el-table-column header-align="center" align="center" label="" width="40">
          </el-table-column>
          <el-table-column prop="examineStatus" header-align="center" align="center" label="小组审核状态" width="140">
            <template slot-scope="scope">
              <span v-if="scope.row.examineStatus === '待审核'" style="color: blue;">{{ scope.row.examineStatus }}</span>
              <span v-else-if="scope.row.examineStatus === '未通过'" style="color: red;">{{ scope.row.examineStatus
                }}</span>
              <span v-else-if="scope.row.examineStatus == '通过'" style="color: #8dc146;">{{ scope.row.examineStatus
                }}</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="groupName" header-align="center" align="center" label="小组名" width="160">
          </el-table-column>
          <el-table-column prop="memberName" header-align="center" align="center" label="姓名" width="160">
          </el-table-column>
          <el-table-column prop="number" header-align="center" align="center" label="员工编号" width="160">
          </el-table-column>
          <el-table-column prop="roleInTopic" header-align="center" align="center" label="组内角色" width="160">
          </el-table-column>
          <el-table-column prop="department" header-align="center" align="center" label="单位" width="160">
          </el-table-column>
          <el-table-column prop="team" header-align="center" align="center" label="小组类型" width="140">
          </el-table-column>
          <!-- <el-table-column prop="registrationNum" header-align="center" align="center" label="注册号" width="140">
          </el-table-column> -->
          <el-table-column prop="date" header-align="center" align="center" label="加入小组时间">
          </el-table-column>
          <el-table-column fixed="right" v-if="isAuth('qcMembers:qcGroupMember:save')" header-align="center"
            align="center" label="操作" width="160">
            <template slot-scope="scope">
              <!-- <el-button v-if="(!scope.row.parentId && isAuth('qcMembers:qcGroupMember:save'))" type="text" size="small"
                @click="addMemberHandle(scope.row.id)">新增成员</el-button> -->
              <el-button type="text" size="small" v-if="!scope.row.parentId"
                @click="handleExamine(scope.row.id, scope.row.registrationNum)">处理审核</el-button>
              <!-- <el-button type="text" size="small" v-if="isAuth('qcMembers:qcGroupMember:delete')"
                @click="deleteHandle(scope.row.id)">删除</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100, 1000]" :page-size="1000" :total="totalPageExamine"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination> -->
        <el-dialog :title="'处理审核'" :close-on-click-modal="false" :visible.sync="dialogFormVisible">
          <el-form :model="examineFrom">
            <el-form-item label="审核结果">
              <el-select v-model="examineFrom.result" placeholder="请选择">
                <el-option label="通过" value="通过"></el-option>
                <el-option label="未通过" value="未通过"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="examineSubmit()">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </el-tab-pane>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"
      @refreshCommonList="getGroupList"></add-or-update>
    <add-group-dialog v-if="AddGroupDialogVisible" ref="AddGroupDialog" @refreshDataList="getDataList"
      @refreshCommonList="getGroupList"></add-group-dialog>
  </el-tabs>

</template>

<script>
import AddOrUpdate from './qcMembersManagement-add-or-update'
import AddGroupDialog from './qcMembersManagement-add-group'

export default {
  data() {
    return {
      activeName: '1',
      examineId: '',
      num: '',
      dataForm: {
        key: ''
      },
      registrationNum: '',
      examineFrom: {
        result: ''
      },
      dialogFormVisible: false,
      dataList: [],
      tmpList: [],
      groupList: [],
      examineList: [],
      originList: [],
      tableData: [],
      pageIndex: 1,
      pageSize: 100,
      totalPageGroup: 0,
      totalPageExamine: 0,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      // 新增弹窗
      AddGroupDialogVisible: false,
      map: {},
      GroupMemberList: [],
      membersOptions: [],

      // 角色列表
      roleIdList: [],
    }
  },
  components: {
    AddOrUpdate,
    AddGroupDialog
  },
  async created() {
    await this.getRoleList();
  },
  async activated() {
    this.initActiveName();
    if ((this.isAuth('qcManagement:group:admin')) || (this.isAuth('qcManagement:group:examine'))) {
      await this.getDataList().then(groupList => {
        this.groupList = groupList;
      });
      console.log(this.tableData)
      this.tableData.sort(function (a, b) {
        const dateA = new Date(a.date);
        const dateB = new Date(b.date);
        // 比较两个日期对象，降序排列
        return dateB - dateA;
      })
      this.tableData.forEach(e => {
        e.children.sort(function (a, b) {
          const dateA = new Date(a.date);
          const dateB = new Date(b.date);
          // 比较两个日期对象，降序排列
          return dateB - dateA;
        });
      });
      console.log(this.tableData)
      this.examineList = this.tableData.filter(item => item.examineStatus == '待审核');
      this.totalPageExamine = this.examineList.length;
      console.log(this.examineList.length)
    }
    if (!this.isAuth('qcManagement:group:admin')) {
      await this.getGroupList();
    }


    // 获取分组后的员工数据
    await this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
      method: 'get',
    }).then(({ data }) => {
      this.membersOptions = data;
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
    ad(s) {
      console.log(s)
    },
    // 获取我的小组数据列表
    async getGroupList() {
      this.dataListLoading = true
      await this.$http({
        url: this.$http.adornUrl('/qcMembers/qcGroupMember/myList'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': 1000,
          // 'key': 1,
          // 'reuseStepId': 5
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          console.log(data)
          const sameList = [];
          // this.dataList = data.page.list
          data.page.list.forEach(item => {
            if (item.memberName == data.userName) {
              sameList.push(item)
            }
          });
          console.log(sameList)
          const resultList = [];
          const seen = new Set(); // 用于去重

          sameList.forEach(item => {
            if (!seen.has(item.qcgmId)) {
              resultList.push(item);
              seen.add(item.qcgmId);
            }

            if (item.parentId) {
              data.page.list.forEach(row => {
                if ((row.parentId == item.parentId || row.qcgmId == item.parentId) && !seen.has(row.qcgmId)) {
                  resultList.push(row);
                  seen.add(row.qcgmId);
                }
              });
            } else {
              data.page.list.forEach(row => {
                if (row.parentId == item.qcgmId && !seen.has(row.qcgmId)) {
                  row.management = 1 //标识当前角色为组长，可以移交组长
                  resultList.push(row);
                  seen.add(row.qcgmId);
                }
              });
            }
          });
          console.log(resultList)
          // 分组
          this.dataList = []; // 清空 dataList
          const map = {};
          resultList.forEach(item => {
            if (item.parentId === null) {
              map[item.qcgmId] = {
                id: item.qcgmId,
                date: item.participationDate,
                memberName: item.memberName,
                number: item.number,
                groupName: item.groupName,
                roleInTopic: '组长',
                team: item.team,
                department: item.department,
                examineStatus: item.examineStatus,
                registrationNum: item.registrationNum,
                children: []
              };
            }
          });
          resultList.forEach(item => {
            if (item.parentId !== null && map[item.parentId]) {
              map[item.parentId].children.push({
                id: item.qcgmId,
                date: item.participationDate,
                memberName: item.memberName,
                number: item.number,
                management: item.management,
                department: item.department,
                roleInTopic: item.roleInTopic,
                parentId: item.parentId,
              });
            }
          });
          console.log(resultList)
          console.log(map)
          this.dataList = Object.values(map);
          // this.dataList = resultList
          this.dataList.sort(function (a, b) {
            const dateA = new Date(a.date);
            const dateB = new Date(b.date);
            // 比较两个日期对象，降序排列
            return dateB - dateA;
          })
          this.dataList.forEach(e => {
            e.children.sort(function (a, b) {
              const dateA = new Date(a.date);
              const dateB = new Date(b.date);
              // 比较两个日期对象，降序排列
              return dateB - dateA;
            });
          });
          console.log(this.dataList)
          this.totalPageGroup = resultList.length
        } else {
          this.dataList = []
          this.totalPageGroup = 0
        }

        this.dataListLoading = false
      })
    },
    initActiveName() {
      if (this.isAuth('qcManagement:group:admin')) {
        this.activeName = '1'
      } else {
        if (this.isAuth('qcManagement:group:examine')) {
          this.activeName = '3'
        } else {
          this.activeName = '2'
        }
      }
    },
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
            this.tmpList = data.page.list;
            this.totalPage = data.page.totalCount;
            groupList = this.tmpList.filter(function (item) {
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
                  memberName: item.memberName,
                  number: item.number,
                  groupName: item.groupName,
                  roleInTopic: '组长',
                  team: item.team,
                  department: item.department,
                  examineStatus: item.examineStatus,
                  registrationNum: item.registrationNum,
                  children: []
                };
              }
            });
            groupList.forEach(item => {
              if (item.parentId !== null && map[item.parentId]) {
                map[item.parentId].children.push({
                  id: item.qcgmId,
                  date: item.participationDate,
                  memberName: item.memberName,
                  number: item.number,
                  roleInTopic: item.roleInTopic,
                  department: item.department,
                  parentId: item.parentId,
                });
              }
            });
            console.log(map)
            this.tableData = Object.values(map);
          } else {
            this.tmpList = [];
            this.totalPage = 0;
          }
          this.dataListLoading = false;
          resolve(groupList);
        }).catch(error => {
          console.error('Error fetching data:', error);
          this.tmpList = [];
          this.totalPage = 0;
          this.dataListLoading = false;
          reject(error);
        });
      });

    },
    //移交组长
    async changeHandle(id, parentId) {
      console.log("传入id为" + id + "父节点为" + parentId)
      await this.$http({
        url: this.$http.adornUrl('/qcMembers/qcGroupMember/change'),
        method: 'get',
        params: this.$http.adornParams({
          'id': id,
          'parentId': parentId,
          // 'key': 1,
          // 'reuseStepId': 5
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
        });
    },

    // 新增 / 修改
    addOrUpdateHandle(id) {
      console.log("传入id为" + id)

      if (id === undefined) {
        this.AddGroupDialogVisible = true
        this.$nextTick(() => {
          this.$refs.AddGroupDialog.membersOptions = this.membersOptions;
          this.$refs.AddGroupDialog.roleIdList = this.roleIdList;
          this.$refs.AddGroupDialog.init(id)
        })
      } else {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.isAddMember = false;
          this.$refs.addOrUpdate.membersOptions = this.membersOptions;
          this.$refs.addOrUpdate.init(id)
        })
      }

    },
    // 新增成员
    addMemberHandle(id, name) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        let tmp = this.membersOptions
        tmp.forEach(item => {
          item.options = item.options.filter(row => row.label !== name);
        });
        this.$refs.addOrUpdate.membersOptions = tmp
        this.$refs.addOrUpdate.isAddMember = true;
        this.$refs.addOrUpdate.init(id)
      })
    },
    handleExamine(id, num) {
      // this.registrationNum = '';
      if (num) {
        this.registrationNum = num;
      } else {
        this.registrationNum = '';
      }

      this.dialogFormVisible = true;
      this.examineId = id;
    },
    //提交审核结果
    async examineSubmit() {
      this.num = '';
      await this.$http({
        url: this.$http.adornUrl(`/qcMembers/qcGroupMember/registration`),
        method: 'get',
      }).then(({ data }) => {
        this.num = data.result.registrationNum + 1
      })
      if (this.num !== '') {
        const year = new Date().getFullYear();
        if (this.num < 10) {
          this.num = `PJHLQC-${year}-00${this.num}`;
        } else if (this.num < 100) {
          this.num = `PJHLQC-${year}-0${this.num}`;
        } else {
          this.num = `PJHLQC-${year}-${this.num}`;
        }
      }
      await this.$http({
        url: this.$http.adornUrl(`/qcMembers/qcGroupMember/update`),
        method: 'post',
        data: this.$http.adornData({
          'qcgmId': this.examineId,
          'examineStatus': this.examineFrom.result,
          'registrationNum': this.registrationNum == '' ? this.num : this.registrationNum,
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: '审核成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.dialogFormVisible = false;
              this.getDataList();
              this.examineList = this.tableData.filter(item => item.examineStatus === '待审核');
              this.totalPageExamine = this.examineList.length;
            }
          })
        } else {
          this.$message.error(data.msg)
        }
      })
    },
    // 提交审核按钮
    handleReexamine(id) {
      // this.dataForm.qcsrId = id;
      // this.dataForm.topicReviewStatus = 2;
      this.$http({
        url: this.$http.adornUrl(`/qcMembers/qcGroupMember/update`),
        method: 'post',
        data: this.$http.adornData({
          'qcgmId': id,
          'examineStatus': '待审核'
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: '提交审核成功',
            type: 'success',
            duration: 1500,
            // onClose: () => {
            //   this.visible = false
            //   this.$emit('refreshDataList')
            // }
          })
        } else {
          this.$message.error(data.msg)
        }
      })
      this.getGroupList();
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
    // 逻辑删除
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
          url: this.$http.adornUrl(`/qcMembers/qcGroupMember/updateLogic`),
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
    },

    // 获取全角色列表
    async getRoleList() {
      await this.$http({
        url: this.$http.adornUrl('/sys/role/select'),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        this.roleIdList = data && data.code === 0 ? data.list : []
      })
    },
  }
}
</script>
