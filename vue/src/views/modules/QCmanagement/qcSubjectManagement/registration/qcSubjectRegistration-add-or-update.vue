<template>
  <el-dialog :title="!dataForm.qcsrId ? '新增' : '修改'" :close-on-click-modal="true" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="150px">
      <el-form-item label="课题名称" prop="topicName">
        <el-input v-model="dataForm.topicName" placeholder="课题名称"></el-input>
      </el-form-item>
      <el-form-item label="课题科室" prop="topicDepartment">
        <el-select v-model="dataForm.topicDepartment" placeholder="请选择科室">
          <el-option label="生产科" value="生产科"></el-option>
          <el-option label="供应科" value="供应科"></el-option>
          <el-option label="市场科" value="市场科"></el-option>
          <el-option label="技术科" value="技术科"></el-option>
          <el-option label="质量科" value="质量科"></el-option>
          <el-option label="财务科" value="财务科"></el-option>
          <el-option label="安环设备科" value="安环设备科"></el-option>
          <el-option label="企业管理科" value="企业管理科"></el-option>
          <el-option label="党群办公室" value="党群办公室"></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="课题编号" prop="topicNumber">
        <el-input v-model="dataForm.topicNumber" placeholder="课题编号"></el-input>
      </el-form-item> -->
      <el-form-item label="小组名称" prop="groupName">
        <el-select v-model="dataForm.groupName" filterable placeholder="请选择小组名称" @change="updateOptions">
          <el-option v-for="item in groupNameOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课题组长" prop="topicLeader">
        <el-input v-model="dataForm.topicLeader" placeholder="课题组长" :disabled="true"></el-input>
      </el-form-item>
      <!-- <el-form-item label="课题顾问" prop="topicConsultant">
        <el-input v-model="dataForm.topicConsultant" placeholder="课题顾问"></el-input>
      </el-form-item> -->
      <el-form-item label="课题顾问" prop="topicConsultant">
        <el-select v-model="dataForm.topicConsultant" :disabled="true" filterable placeholder="请选择课题顾问">
          <el-option-group v-for="group in membersOptions" :key="group.label" :label="group.label">
            <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.label">
            </el-option>
          </el-option-group>
        </el-select>
        <!-- <el-select v-model="dataForm.topicConsultant" filterable placeholder="请选择课题顾问">
          <el-option v-for="item in membersOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select> -->
      </el-form-item>
      <!-- <el-form-item label="小组成员" prop="teamNumberIds">
        <el-input v-model="dataForm.teamNumberIds" placeholder="小组成员"></el-input>
      </el-form-item> -->
      <el-form-item label="小组成员" prop="teamNumberIds">
        <el-select v-model="dataForm.teamNumberIds" filterable multiple placeholder="请选择小组成员">
          <el-option v-for="item in membersSelect" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <!-- <el-form-item label="开始日期" prop="startDate">
        <el-date-picker clearable v-model="dataForm.startDate" type="date" value-format="yyyy-MM-dd" placeholder="开始日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期" prop="endDate">
        <el-date-picker clearable v-model="dataForm.endDate" type="date" value-format="yyyy-MM-dd" placeholder="结束日期">
        </el-date-picker>
      </el-form-item> -->
      <el-form-item label="课题描述/摘要" prop="topicDescription">
        <el-input v-model="dataForm.topicDescription" placeholder="课题描述/摘要"></el-input>
      </el-form-item>
      <el-form-item label="课题类型" prop="topicType">
        <el-select v-model="dataForm.topicType" placeholder="请选择QC课题类型">
          <el-option label="问题解决型" value="问题解决型"></el-option>
          <el-option label="问题解决型(指令型)" value="问题解决型(指令型)"></el-option>
          <el-option label="创新型" value="创新型"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="活动特性" prop="activityCharacteristics">
        <el-select v-model="dataForm.activityCharacteristics" placeholder="请选择活动特性">
          <el-option label="短期" value="短期"></el-option>
          <el-option label="长期" value="长期"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="活动计划" prop="activityPlan">
        <el-date-picker clearable v-model="dataForm.activityPlan" type="daterange" range-separator="-"
          start-placeholder="开始日期" end-placeholder="结束日期" placeholder="活动计划">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="课题关键字" prop="keywords">
        <el-input v-model="dataForm.keywords" placeholder="课题关键字"></el-input>
      </el-form-item>
      <!-- <el-form-item label="课题活动状态" prop="topicActivityStatus">
        <el-input v-model="dataForm.topicActivityStatus" placeholder="课题活动状态"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="课题活动评分结果" prop="topicActivityResult">
        <el-input v-model="dataForm.topicActivityResult" placeholder="课题活动评分结果"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="删除标记位" prop="deleteFlag">
        <el-input v-model="dataForm.deleteFlag" placeholder="删除标记位"></el-input>
      </el-form-item> -->
      <el-form-item label="备注" prop="note">
        <el-input type="textarea" autosize v-model="dataForm.note" placeholder="备注"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>

export default {
  data() {
    return {
      groupMemberList: [],
      flag: 1,
      visible: false,
      membersOptions: [],
      dataForm: {
        qcsrId: 0,
        topicName: '',
        topicNumber: '',
        topicLeader: '',
        topicConsultant: '',
        teamNumberIds: [],
        createDate: '',
        creator: '',
        modificationDate: '',
        modifier: '',
        startDate: '',
        endDate: '',
        topicReviewStatus: '',
        topicDescription: '',
        topicType: '',
        topicDepartment: '',
        activityCharacteristics: '',
        activityPlan: [],
        activityPlanEnd: '',
        keywords: '',
        topicActivityStatus: '',
        topicActivityResult: '',
        groupName: '',
        deleteFlag: '',
        note: ''
      },
      resultSubmit: {
        leader: '',
        consultant: '',
      },
      dataList: [],
      //小组名称选择
      groupNameOptions: [],
      //顾问选择
      consultantOptions: [],
      //成员选择
      membersSelect: [],

      dataRule: {
        topicName: [
          { required: true, message: '课题名称不能为空', trigger: 'blur' }
        ],
        // topicConsultant: [
        //   { required: true, message: '课题顾问不能为空', trigger: 'blur' }
        // ],
        // teamNumberIds: [
        //   { required: true, message: '小组成员ids不能为空', trigger: 'blur' }
        // ],
        // createDate: [
        //   { required: true, message: '创建日期不能为空', trigger: 'blur' }
        // ],
        // startDate: [
        //   { required: true, message: '开始日期不能为空', trigger: 'blur' }
        // ],
        // endDate: [
        //   { required: true, message: '结束日期不能为空', trigger: 'blur' }
        // ],
        // topicDescription: [
        //   { required: true, message: '课题描述/摘要不能为空', trigger: 'blur' }
        // ],
        topicType: [
          { required: true, message: '课题类型不能为空', trigger: 'blur' }
        ],
        activityCharacteristics: [
          { required: true, message: '活动特性不能为空', trigger: 'blur' }
        ],
        activityPlan: [
          { required: true, message: '活动计划不能为空', trigger: 'blur' }
        ],
        topicDepartment: [
          { required: true, message: '课题单位不能为空', trigger: 'blur' }
        ],
        // keywords: [
        //   { required: true, message: '课题关键字tag不能为空', trigger: 'blur' }
        // ],
        // topicActivityStatus: [
        //   { required: true, message: '课题活动状态不能为空', trigger: 'blur' }
        // ],
      }
    }
  },
  methods: {
    updateOptions(number) {
      // 首先找到匹配的组名的项
      try {
        console.log(number)
        if (number != '清除') {
          this.dataForm.topicConsultant = '';
          this.dataForm.teamNumberIds = '';
        }
        // console.log(this.dataForm.groupName)
        // console.log(this.dataForm.topicConsultant)
        // console.log(this.dataForm.teamNumberIds)
        console.log(this.membersOptions)
        // console.log(this.dataForm)

        var matchedItem = Object.values(this.groupMemberList).find(item => {
          return item.groupName === this.dataForm.groupName;
        });

        if (matchedItem) {
          console.log(matchedItem)
          //更新组长
          this.resultSubmit.leader = matchedItem.name
          this.dataForm.topicLeader = this.numberToName(matchedItem.name)
          // 更新顾问
          this.resultSubmit.consultant = matchedItem.position
          this.dataForm.topicConsultant = this.numberToName(matchedItem.position)
          // this.consultantOptions = matchedItem.children.filter(member => member.roleInTopic === '顾问').map(member => ({
          //   value: member.name,
          //   label: member.name
          // }));

          // 更新成员选项
          this.membersSelect = matchedItem.children.filter(member => member.roleInTopic === '成员').map(member => ({
            value: member.name,
            label: member.name
          }));
          console.log(this.membersSelect)
          console.log('-----------')
          this.membersOptions.forEach(a => {
            a.options.forEach(b => {
              this.membersSelect.forEach(c => {
                if (b.name === c.label) {
                  c.label = b.label
                }
              })
            })
          })
          console.log(this.membersSelect)



        } else {
          // 如果没有找到匹配项，可以清空选项
          this.consultantOptions = [];
          this.membersSelect = [];
        }
      } catch (e) {
        console.log(e)
      }

    },
    numberToName(number) {
      var result = ''
      this.membersOptions.forEach(o => {
        o.options.map(e => {
          if (e.name == number) {
            result = e.label.replace(/\(.*?\)/, '')
          }
        })
      });
      return result
    },
    nameToNumber(name) {
      var result = ''
      this.membersOptions.forEach(o => {
        o.options.map(e => {
          if (e.number == name) {
            result = e.name
            console.log(e.name)
          }
        })
      });
      return result
    },
    init(id) {
      this.dataForm.qcsrId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.qcsrId) {
          this.$http({
            url: this.$http.adornUrl(`/qcSubject/registration/info/${this.dataForm.qcsrId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.topicName = data.qcSubjectRegistration.topicName
              this.dataForm.topicNumber = data.qcSubjectRegistration.topicNumber
              this.dataForm.topicLeader = this.numberToName(data.qcSubjectRegistration.topicLeader)
              this.resultSubmit.leader = data.qcSubjectRegistration.topicLeader
              this.dataForm.topicConsultant = this.numberToName(data.qcSubjectRegistration.topicConsultant)
              this.resultSubmit.consultant = data.qcSubjectRegistration.topicConsultant
              this.dataForm.teamNumberIds = JSON.parse(data.qcSubjectRegistration.teamNumberIds)
              this.dataForm.topicReviewStatus = data.qcSubjectRegistration.topicReviewStatus
              this.dataForm.topicDescription = data.qcSubjectRegistration.topicDescription
              this.dataForm.topicType = data.qcSubjectRegistration.topicType
              this.dataForm.activityCharacteristics = data.qcSubjectRegistration.activityCharacteristics
              this.dataForm.activityPlan = [new Date(data.qcSubjectRegistration.activityPlan), new Date(data.qcSubjectRegistration.activityPlanEnd)]
              // this.dataForm.activityPlan[1] = new Date(data.qcSubjectRegistration.activityPlanEnd)
              this.dataForm.keywords = data.qcSubjectRegistration.keywords
              this.dataForm.topicDepartment = data.qcSubjectRegistration.topicDepartment
              this.dataForm.topicActivityStatus = data.qcSubjectRegistration.topicActivityStatus
              this.dataForm.topicActivityResult = data.qcSubjectRegistration.topicActivityResult
              this.dataForm.deleteFlag = data.qcSubjectRegistration.deleteFlag
              this.dataForm.note = data.qcSubjectRegistration.note
              this.dataForm.groupName = data.qcSubjectRegistration.groupName
            }
            this.updateOptions('清除')
            // data.userName
            console.log(this.groupMemberList)
            if ((this.groupMemberList && typeof this.groupMemberList === 'object' && !Array.isArray(this.groupMemberList) && !this.isAuth('qcManagement:group:admin'))) {
              // 遍历对象
              console.log('11')
              this.groupNameOptions = Object.keys(this.groupMemberList).map(key => {
                if (this.groupMemberList[key].name == data.userName && this.groupMemberList[key].examineStatus === '通过') {
                  return {
                    value: this.groupMemberList[key].groupName,
                    label: this.groupMemberList[key].groupName
                  };
                }
              });
              const filteredGroupMemberList = Object.entries(this.groupMemberList).reduce((acc, [key, value]) => {
                if (value.name === data.userName) {
                  acc[key] = value;
                }
                return acc;
              }, {});

              // 更新 groupNameOptions
              this.groupNameOptions = Object.keys(filteredGroupMemberList).map(key => ({
                value: filteredGroupMemberList[key].groupName,
                label: filteredGroupMemberList[key].groupName
              }));
              console.log(this.groupNameOptions)
            } else if (Array.isArray(this.groupMemberList)) {
              this.groupNameOptions = this.groupMemberList.map(item => ({
                value: item.groupName,
                label: item.groupName
              }));
            } else {
              if (this.isAuth('qcManagement:group:admin')) {
                // 遍历对象
                this.groupNameOptions = Object.keys(this.groupMemberList).map(key => {
                  if (this.groupMemberList[key].examineStatus === '通过') {
                    return {
                      value: this.groupMemberList[key].groupName,
                      label: this.groupMemberList[key].groupName
                    };
                  }
                });
                const filteredGroupMemberList = Object.entries(this.groupMemberList).reduce((acc, [key, value]) => {
                  acc[key] = value;
                  return acc;
                }, {});

                // 更新 groupNameOptions
                this.groupNameOptions = Object.keys(filteredGroupMemberList).map(key => ({
                  value: filteredGroupMemberList[key].groupName,
                  label: filteredGroupMemberList[key].groupName
                }));
              } else {
                console.error('this.groupMemberList is neither an array nor an object');
              }

            }
          })
        } else {
          this.$http({
            url: this.$http.adornUrl(`/qcSubject/registration/user`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              if ((this.groupMemberList && typeof this.groupMemberList === 'object' && !Array.isArray(this.groupMemberList) && !this.isAuth('qcManagement:group:admin'))) {
                // 遍历对象
                this.groupNameOptions = Object.keys(this.groupMemberList).map(key => {
                  if (this.groupMemberList[key].name == data.userName) {
                    return {
                      value: this.groupMemberList[key].groupName,
                      label: this.groupMemberList[key].groupName
                    };
                  }
                });
                const filteredGroupMemberList = Object.entries(this.groupMemberList).reduce((acc, [key, value]) => {
                  if (value.name === data.userName) {
                    acc[key] = value;
                  }
                  return acc;
                }, {});

                // 更新 groupNameOptions
                this.groupNameOptions = Object.keys(filteredGroupMemberList).map(key => ({
                  value: filteredGroupMemberList[key].groupName,
                  label: filteredGroupMemberList[key].groupName
                }));
                console.log(this.groupNameOptions)
              } else if (Array.isArray(this.groupMemberList)) {
                this.groupNameOptions = this.groupMemberList.map(item => ({
                  value: item.groupName,
                  label: item.groupName
                }));
              } else {
                if (this.isAuth('qcManagement:group:admin')) {
                  // 遍历对象
                  this.groupNameOptions = Object.keys(this.groupMemberList).map(key => {
                    if (this.groupMemberList[key].examineStatus == '通过') {
                      return {
                        value: this.groupMemberList[key].groupName,
                        label: this.groupMemberList[key].groupName
                      };
                    }
                  });
                  const filteredGroupMemberList = Object.entries(this.groupMemberList).reduce((acc, [key, value]) => {
                    acc[key] = value;
                    return acc;
                  }, {});

                  // 更新 groupNameOptions
                  this.groupNameOptions = Object.keys(filteredGroupMemberList).map(key => ({
                    value: filteredGroupMemberList[key].groupName,
                    label: filteredGroupMemberList[key].groupName
                  }));
                } else {
                  console.error('this.groupMemberList is neither an array nor an object');
                }
              }
            }
          })

        }

      })
      // this.updateOptions()
    },

    ifUpdate() {
      if (this.dataForm.qcsrId) {
        this.flag = 1;
      }
    },
    // handleFormEdit() {
    //   this.groupMemberList.filter(item => {
    //     if (item.groupName === this.dataForm.groupName) {
    //       this.consultantOptions = item.children.map(member => {
    //         if (member.roleInTopic === '顾问') {
    //           return {
    //             value: member.name,
    //             label: member.name
    //           }
    //         }
    //       });
    //     }
    //   });
    // },
    // 表单提交
    dataFormSubmit() {
      if (this.flag && this.dataForm.qcsrId && this.dataForm.topicReviewStatus === 3) {
        this.$confirm('修改表单会重置审核状态，是否确认修改?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.flag = 0;
          this.dataForm.topicReviewStatus = 1;
          this.dataFormSubmit();
          this.flag = 1;
        }).catch(() => {
        });
      } else {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            const startDatePlan = new Date(this.dataForm.activityPlan[0]);
            const endDatePlan = new Date(this.dataForm.activityPlan[1]);
            startDatePlan.setHours(startDatePlan.getHours() + 13);
            endDatePlan.setHours(endDatePlan.getHours() + 13);
            this.$http({
              url: this.$http.adornUrl('/qcSubject/registration/exist'),
              method: 'get',
              params: this.$http.adornParams({
                'topicName': this.dataForm.topicName,
              })
            }).then(({ data }) => {
              if (data && data.code === 0) {
                if (data.exist && (!this.dataForm.qcsrId)) {
                  this.$message.warning('课题名称已存在!')
                  return
                } else {
                  this.$http({
                    url: this.$http.adornUrl(`/qcSubject/registration/${!this.dataForm.qcsrId ? 'save' : 'update'}`),
                    method: 'post',
                    data: this.$http.adornData({
                      'qcsrId': this.dataForm.qcsrId || undefined,
                      'topicName': this.dataForm.topicName,
                      'topicNumber': this.dataForm.topicNumber,
                      'topicLeader': this.resultSubmit.leader,
                      'topicConsultant': this.resultSubmit.consultant,
                      'teamNumberIds': JSON.stringify(this.dataForm.teamNumberIds),
                      'topicReviewStatus': this.dataForm.qcsrId ? this.dataForm.topicReviewStatus : 1,
                      'topicDescription': this.dataForm.topicDescription ? this.dataForm.topicDescription : '',
                      'topicType': this.dataForm.topicType,
                      'activityCharacteristics': this.dataForm.activityCharacteristics,
                      'activityPlan': startDatePlan,
                      'activityPlanEnd': endDatePlan,
                      'keywords': this.dataForm.keywords,
                      'topicActivityStatus': this.dataForm.topicActivityStatus,
                      'topicActivityResult': this.dataForm.topicActivityResult,
                      'deleteFlag': this.dataForm.deleteFlag,
                      'note': this.dataForm.note,
                      'groupName': this.dataForm.groupName,
                      'topicDepartment': this.dataForm.topicDepartment,
                    })
                  }).then(({ data }) => {
                    if (data && data.code === 0) {
                      this.$message({
                        message: '操作成功',
                        type: 'success',
                        duration: 1500,
                        onClose: () => {
                          this.visible = false
                          this.$emit('refreshDataList')
                          this.$emit('refreshLeadList')
                          this.$emit('refreshJoinList')
                        }
                      })
                    } else {
                      this.$message.error(data.msg)
                    }
                  })
                }
              }
            })

          }
        })
      }
    },
  }
}
</script>
