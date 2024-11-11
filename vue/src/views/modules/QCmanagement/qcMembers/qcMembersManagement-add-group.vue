<template>
  <el-dialog :title="!dataForm.qcgmId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form v-if="!dataForm.qcgmId || !isEditingMember" :model="dataForm" :rules="dataRule" ref="dataForm"
             @keyup.enter.native="dataFormSubmit()" label-width="120px">
      <el-row :gutter="20" v-show="!isAddMember">
        <el-col :span="12">
          <el-form-item v-if="isAdmin" prop="groupName" label="小组名称">
            <el-input v-model="dataForm.groupName" placeholder="请输入小组名称"></el-input>
          </el-form-item>
          <el-form-item prop="mainName" label="组长姓名">
<!--             <el-input v-model="dataForm.mainName" placeholder="请输入姓名"></el-input>-->
<!--            <el-select v-model="dataForm.userId" placeholder="请选择角色">-->
<!--              <el-option-group v-for="group in membersOptions" :key="group.label" :label="group.label">-->
<!--                <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">-->
<!--                </el-option>-->
<!--              </el-option-group>-->
<!--              -->
<!--            </el-select>-->
<!--            <el-select v-model="dataForm.userId" placeholder="请选择用户">-->
<!--              <el-option v-for="item in membersOptions.options" :key="item.value" :label="item.label" :value="item.value"></el-option>-->
<!--            </el-select>-->
            <el-select v-model="dataForm.userId" filterable placeholder="请选择用户">
              <el-option-group v-for="group in membersOptions" :key="group.label" :label="group.label">
                <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-option-group>
            </el-select>


          </el-form-item>
          <el-form-item label="角色" >
            <el-select v-model="dataForm.memberRole" placeholder="请选择">
              <el-option v-for="role in roleIdList" :key="role.roleId" :label="role.roleName" :value="role.roleId"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="number" label="员工编号">
            <el-input v-model="dataForm.number" placeholder="请输入员工编号"></el-input>
          </el-form-item>
          <el-form-item v-if="isAdmin" prop="department" label="单位">
            <el-input v-model="dataForm.department" placeholder="请输入单位"></el-input>
          </el-form-item>
          <el-form-item v-if="isAdmin" prop="team" label="小组类型">
            <el-select v-model="dataForm.team" placeholder="请选择小组类型">
              <el-option label="攻关" value="攻关"></el-option>
              <el-option label="现场" value="现场"></el-option>
              <el-option label="管理" value="管理"></el-option>
              <el-option label="服务" value="服务"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="!isAdmin" label="组内角色" prop="roleInTopic">
            <el-select v-model="dataForm.roleInTopic" placeholder="请选择组内角色">
              <el-option label="成员" value="成员"></el-option>
              <el-option label="顾问" value="顾问"></el-option>
              <el-option label="评审员" value="评审员"></el-option>
              <el-option label="相关方评审员" value="相关方评审员"></el-option>
              <el-option label="成果初评管理员" value="成果初评管理员"></el-option>
              <el-option label="财务科评审员" value="财务科评审员"></el-option>
              <el-option label="成果复评管理员" value="成果复评管理员"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="participationDate" label="参加QC时间">
            <el-date-picker v-model="dataForm.participationDate" type="date" value-format="yyyy-MM-dd"
                            placeholder="请选择日期" clearable></el-date-picker>
          </el-form-item>



        </el-col>
      </el-row>
      <div v-for="(member, index) in dataForm.members" :key="index" style="background-color:#EEEEEE;"
           class="members-list-item">
        <el-form :model="member" :rules="dataRule" :ref="'dataForm' + index" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <h3>{{ '成员 ' + (index + 1) }}</h3>
            </el-col>
            <el-col :span="12" class="button-col">
              <el-button type="text" icon="el-icon-close" size="large" @click="removeMember(member)"
                         class="remove-member-button"></el-button>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <!-- <el-input v-model="member.name" placeholder="请输入姓名"></el-input> -->
                <el-select v-model="member.name" placeholder="请选择角色">
                  <el-option-group v-for="group in membersOptions" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.label">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
              <el-form-item label="员工编号" prop="number">
                <el-input v-model="member.number" placeholder="请输入员工编号"></el-input>
              </el-form-item>
              <el-form-item label="组内角色" prop="roleInTopic">
                <el-select v-model="member.roleInTopic" placeholder="请选择组内角色">
                  <el-option label="成员" value="成员"></el-option>
                  <el-option label="顾问" value="顾问"></el-option>
                  <el-option label="评审员" value="评审员"></el-option>
                  <el-option label="相关方评审员" value="相关方评审员"></el-option>
                  <el-option label="成果初评管理员" value="成果初评管理员"></el-option>
                  <el-option label="财务科评审员" value="财务科评审员"></el-option>
                  <el-option label="成果复评管理员" value="成果复评管理员"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="参加时间" prop="participationDate">
                <el-date-picker v-model="member.participationDate" type="date"
                                placeholder="请输入参加时间"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <el-form-item>
        <br>

        <el-button v-if="!dataForm.qcgmId || isAddMember" type="primary" @click="addMember">新增成员</el-button>
      </el-form-item>
    </el-form>

    <span slot="footer">
      <div v-if="isAddMember" style="color:#e6a23c">点击添加将重置审核状态</div>
      <br>
      <el-button class="dialog-footer" @click="visible = false">取消</el-button>
      <el-button class="dialog-footer" type="primary" v-if="!isAddMember" @click="dataFormSubmit()">确定</el-button>
      <el-button class="dialog-footer" type="primary" v-else @click="dataMembersSubmit()">添加</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      // 角色列表
      roleIdList: [],
      // 用户选择的角色
      memberRole: '',

      membersOptions: [],
      isAddMember: false,
      parentFlag: '',
      visible: false,
      isEditingMember: false,
      dataForm: {
        qcgmId: 0,
        mainName: '',
        userId: '',
        gender: '',
        telNumber: '',
        number: '',
        education: '',
        department: '',
        position: '',
        team: '',
        participationDate: '',
        topic: '',
        roleInTopic: '',
        deleteFlag: '',
        groupName: '',
        members: [],
        parentId: '',
        examineStatus: '',
        // 角色id
        memberRole: ''
      },
      editingMember: {
        qcgmId: '',
        name: '',
        number: '',
        participationDate: '',
        parentId: '',
      },
      dataRule: {
        userId: [
          {required: true, message: '姓名不能为空', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '姓名不能为空', trigger: 'blur'}
        ],
        // number: [
        //   { required: true, message: '员工编号不能为空', trigger: 'blur' }
        // ],
        participationDate: [
          {required: true, message: '参加时间不能为空', trigger: 'blur'}
        ],
      }
    }
  },
  mounted() {
    this.setDefaultDate();
    this.parentFlag = this.dataForm.qcgmId;

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
    setDefaultDate() {
      const currentDate = new Date().toISOString().slice(0, 10);
      this.dataForm.participationDate = currentDate;
      this.dataForm.members.joinDate = currentDate;
    },
    async init(id) {
      console.log("传入的"+JSON.stringify(this.membersOptions) )
      this.dataForm.qcgmId = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields();
        if (this.dataForm.qcgmId) {
          this.$http({
            url: this.$http.adornUrl(`/qcMembers/qcGroupMember/info/${this.dataForm.qcgmId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.mainName = data.qcGroupMember.name;
              this.dataForm.userId = data.qcGroupMember.userId;
              this.dataForm.gender = data.qcGroupMember.gender;
              this.dataForm.telNumber = data.qcGroupMember.telNumber;
              this.dataForm.number = data.qcGroupMember.number;
              this.dataForm.education = data.qcGroupMember.education;
              this.dataForm.department = data.qcGroupMember.department;
              this.dataForm.position = data.qcGroupMember.position;
              this.dataForm.team = data.qcGroupMember.team;
              this.dataForm.participationDate = data.qcGroupMember.participationDate;
              this.dataForm.topic = data.qcGroupMember.topic;
              this.dataForm.roleInTopic = data.qcGroupMember.roleInTopic;
              this.dataForm.deleteFlag = data.qcGroupMember.deleteFlag;
              this.dataForm.groupName = data.qcGroupMember.groupName;
              this.dataForm.parentId = data.qcGroupMember.parentId || '';
              this.dataForm.members = data.qcGroupMember.members || [];
              this.dataForm.examineStatus = data.qcGroupMember.examineStatus;
            }
          });
        }
      });
    },
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/qcMembers/qcGroupMember/${!this.dataForm.qcgmId ? 'createGroup' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'qcgmId': this.dataForm.qcgmId || undefined,
              'name': this.dataForm.mainName,
              'userId': this.dataForm.userId,
              'gender': this.dataForm.gender,
              'telNumber': this.dataForm.telNumber,
              'number': this.dataForm.number,
              'education': this.dataForm.education,
              'department': this.dataForm.department,
              'position': this.dataForm.position,
              'team': this.dataForm.team,
              'participationDate': this.dataForm.participationDate,
              'topic': this.dataForm.topic,
              'roleInTopic': '组长',
              'deleteFlag': 0,
              'groupName': this.dataForm.groupName,
              'examineStatus': '待审核',
              'memberRole': this.dataForm.memberRole
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false;
                  if (this.isAuth('qcManagement:group:admin') || this.isAuth('qcManagement:group:examine')) {
                    this.$emit('refreshDataList');
                  } else {
                    this.$emit('refreshCommonList');
                  }
                }
              });

              const parentId = data.id;
              this.dataForm.members.forEach(member => {
                member.parentId = parentId;
              });

              Promise.all(this.dataForm.members.map(member => {
                return this.$http({
                  url: this.$http.adornUrl(`/qcMembers/qcGroupMember/${!member.qcgmId ? 'save' : 'update'}`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcgmId': member.qcgmId || undefined,
                    'name': member.name,
                    'team': member.team,
                    'number': member.number,
                    'participationDate': member.participationDate,
                    'roleInTopic': member.roleInTopic,
                    // 'deleteFlag': member.deleteFlag,
                    'parentId': member.parentId,
                    'deleteFlag': 0,
                    'groupName': this.dataForm.groupName,
                    // 'examineStatus': this.dataForm.qcgmId ? this.dataForm.examineStatus : '待审核',
                  })
                });
              })).then(responses => {
                console.log('所有成员数据提交完成:', responses);
              }).catch(error => {
                console.error('成员数据提交时发生错误:', error);
              });

            } else {
              this.$message.error(data.msg);
            }
          });
        }
      });
    },
    dataMembersSubmit() {
      this.$http({
        url: this.$http.adornUrl(`/qcMembers/qcGroupMember/update`),
        method: 'post',
        data: this.$http.adornData({
          'qcgmId': this.dataForm.qcgmId,
          'examineStatus': '待审核',
        })
      })
      Promise.all(this.dataForm.members.map(member => {
        return this.$http({
          url: this.$http.adornUrl(`/qcMembers/qcGroupMember/${!member.qcgmId ? 'save' : 'update'}`),
          method: 'post',
          data: this.$http.adornData({
            'qcgmId': member.qcgmId || undefined,
            'name': member.name,
            'team': member.team,
            'number': member.number,
            'participationDate': member.participationDate,
            'roleInTopic': member.roleInTopic,
            // 'deleteFlag': member.deleteFlag,
            'parentId': this.dataForm.qcgmId,
            'deleteFlag': 0,
            'groupName': this.dataForm.groupName
          })
        });
      })).then(responses => {
        console.log('所有成员数据提交完成:', responses);
        this.$message({
          message: '操作成功',
          type: 'success',
          duration: 1500,
          onClose: () => {
            this.visible = false;
            if (this.isAuth('qcManagement:group:admin') || this.isAuth('qcManagement:group:examine')) {
              this.$emit('refreshDataList');
            } else {
              this.$emit('refreshCommonList');
            }
          }
        });
      }).catch(error => {
        console.error('成员数据提交时发生错误:', error);
      });
    },
    addMember() {
      const currentDate = new Date().toISOString().slice(0, 10);
      this.dataForm.members.push({
        qcgmId: '',
        name: '',
        number: '',
        participationDate: currentDate,
        parentId: this.dataForm.qcgmId,
        roleInTopic: ''
      });
    },
    removeMember(item) {
      var index = this.dataForm.members.indexOf(item);
      if (index !== -1) {
        this.dataForm.members.splice(index, 1);
      }
    },
    editMember(member) {
      this.isEditingMember = true;
      this.editingMember = {...member};
    },
    updateMember() {
      this.$refs['editingMemberForm'].validate((valid) => {
        if (valid) {
          const index = this.dataForm.members.findIndex(m => m.qcgmId === this.editingMember.qcgmId);
          if (index !== -1) {
            this.dataForm.members.splice(index, 1, {...this.editingMember});
          }
          this.isEditingMember = false;
        }
      });
    },
    cancelEditMember() {
      this.isEditingMember = false;
    }
  }
}
</script>

<style scoped>
.form-actions {
  margin-top: 20px;
  text-align: right;
}

.main-content {
  padding: 20px;
}

.el-form {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 0 2px 2px rgba(0, 0, 0, 0.1);
}

.el-form-item {
  margin-bottom: 16px;
}

.el-col {
  margin-bottom: 0px;
}

.members-list-item {
  background-color: #f9fafb;
  padding: 2px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .1);
  transition: all 0.3s ease;
}

.members-list-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, .1);
}

.members-list-title {
  margin-bottom: 8px;
}

.form-actions {
  text-align: right;
  margin-top: 20px;
}

.form-actions .el-button {
  margin-left: 10px;
}

.center-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.button-col {
  display: flex;
  justify-content: flex-end;
}

.remove-member-button {
  color: #979a9f;
  font-size: 18px;
  padding: 10px 20px;
  font-weight: 900;
}
</style>
