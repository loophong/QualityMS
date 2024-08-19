<template>
  <div>
  <el-dialog
    :title="'任务发起'"
    :close-on-click-modal="false"
    :visible.sync="visible1">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="审核人" prop="reviewers">
        <el-select v-model="dataForm.reviewers" filterable placeholder="请选择验证人">
          <el-option-group v-for="group in options" :key="group.label" :label="group.label">
            <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                       :value="item.value">
            </el-option>
          </el-option-group>
        </el-select>
      </el-form-item>

        <el-form-item
          v-for="(subtask, index) in dataForm.subtasks"
          :key="subtask.key"
          :label="'子任务 ' + (index + 1)"
          :prop="'subtasks.' + index + '.name'"
          :rules="{ required: true, message: '子任务不能为空', trigger: 'blur' }">
          <el-input v-model="subtask.name" placeholder="请输入子任务"></el-input>
<!--          <el-select v-model="subtask.assignee" placeholder="请选择接收人">-->
<!--            <el-option label="甲" value="甲"></el-option>-->
<!--            <el-option label="乙" value="乙"></el-option>-->
<!--            <el-option label="丙" value="丙"></el-option>-->
<!--          </el-select>-->
          <el-select v-model="subtask.assignee" filterable placeholder="请选择接收人">
            <el-option-group v-for="group in options" :key="group.label" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                         :value="item.value">
              </el-option>
            </el-option-group>
          </el-select>
          <el-button @click.prevent="removeSubtask(subtask)">删除</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="addSubtask">增加子任务</el-button>
          <el-button @click="resetForm('dataForm')">重置</el-button>
        </el-form-item>
<!--        <el-form-item label="要求完成日期" prop="requiredCompletionTime">-->
<!--          <el-date-picker-->
<!--            v-model="requiredCompletionTime"-->
<!--            type="date"-->
<!--            value-format="yyyy-MM-dd HH:mm:ss"-->
<!--            placeholder="选择日期">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
      <el-form-item label="要求完成日期" prop="requiredCompletionTime">
        <el-date-picker
          v-model="dataForm.requiredCompletionTime"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择上传日期">
        </el-date-picker>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel">取消</el-button>
      <el-button type="primary" @click="submitForm('dataForm')">提交</el-button>
    </span>
  </el-dialog>
<!--  </div>-->
<!--  <div>-->

  </div>
</template>

<script>
  export default {
    data () {
      return {
        requiredCompletionTime: '',
        visible: false,
        visible1: false,
        dataForm: {
          userinfo: '',
          initiator: '',
          initiationDate: '',
          reviewers: '',
          subtasks: [{ name: '', assignee: '', key: Date.now() }],
          imageList: [],
          dialogVisible: false,
          dialogImageUrl: '',
          issueCategoryOptions: [],
          issueId: 0,
          serialNumber: '',
          issueNumber: '',
          inspectionDepartment: '',
          inspectionDate: '',
          issueCategoryId: '',
          vehicleTypeId: '',
          vehicleNumberId: '',
          issueDescription: '',
          issuePhoto: '',
          rectificationRequirement: '',
          requiredCompletionTime: '',
          responsibleDepartment: '',
          rectificationStatus: '',
          actualCompletionTime: '',
          rectificationPhotoDeliverable: '',
          rectificationResponsiblePerson: '',
          requiredSecondRectificationTime: '',
          remark: '',
          creator: '',
          creationTime: '',
          lastModifier: '',
          lastModificationTime: '',
          associatedRectificationRecords: '',
          associatedIssueAddition: '',
          creationDuration: '',
          causeAnalysis: '',
          rectificationVerificationStatus: '',
          verificationConclusion: '',
          verifier: '',
          formula: ''
        },
        dataRule: {
          initiator: [
            { required: true, message: '请选择发起人', trigger: 'change' }
          ],
          initiationDate: [
            { required: true, message: '请选择发起日期', trigger: 'change' }
          ],
          reviewers: [
            { required: true, message: '请选择审核人', trigger: 'change' }
          ]
        },
        options: ''
      }
    },
    created () {
      this.$http({
        url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
        method: 'get',
      }).then(({ data }) => {
        this.options = data;


        console.log(data);
        // if (data && data.code === 0) {
        //   console.log(data);
        // }
      })
    },
    activated () {
      this.fetchuserinform()
    },
    methods: {
      cancel () {
        // 重置 subtasks 数组，只保留一个初始组合
        this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
        this.visible1 = false // 关闭对话框或重置其他状态
      },
      addSubtask () {
        this.dataForm.subtasks.push({
          name: '',
          assignee: '',
          key: Date.now()
        })
      },
      removeSubtask (subtask) {
        let index = this.dataForm.subtasks.indexOf(subtask)
        if (index !== -1) {
          this.dataForm.subtasks.splice(index, 1)
        }
      },
      fetchuserinform () {
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/useinfo'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            console.log('Successfully fetched user info:', data.userinfo)
            this.dataForm.userinfo = data.userinfo // 将后端返回的 userinfo 赋值给 this.userinfo
            console.log('Successfully fetched user info:', this.dataForm.userinfo)
          } else {
            console.error('Failed to fetch vehicle types:', data.msg)
          }
        }).catch(error => {
          console.error('There was an error fetching the vehicle types!', error)
        })
      },
      submitForm (formName) {
        this.fetchuserinform()
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dataForm.subtasks.forEach(subtask => {
              this.$http({
                url: this.$http.adornUrl(`/generator/issuemasktable/save`),
                method: 'post',
                data: this.$http.adornData({
                  'issuemaskId': this.dataForm.issuemaskId || undefined,
                  'issueNumber': this.dataForm.issueNumber,
                  'reviewers': this.dataForm.reviewers,
                  'recipients': subtask.assignee,
                  'maskcontent': subtask.name,
                  'creator': this.dataForm.userinfo,
                  'creationTime': this.dataForm.creationTime,
                  'requiredCompletionTime': this.dataForm.requiredCompletionTime
                })
              }).then(({data}) => {
                if (data && data.code === 0) {
                  this.$message({
                    message: '操作成功',
                    type: 'success',
                    duration: 1500,
                    onClose: () => {
                      this.visible = false
                      this.$emit('refreshDataList')
                    }
                  })
                } else {
                  this.$message.error(data.msg)
                }
              })
              console.log('时间:', this.dataForm.requiredCompletionTime)
              console.log('发起人:', this.dataForm.userinfo)
            })
          }
          // 重置 subtasks 数组，只保留一个初始组合
          this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
          this.visible1 = false // 关闭对话框或重置其他状态
        })
      },
      resetForm (formName) {
        // 重置 subtasks 数组，只保留一个初始组合
        this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
      },
      init (id) {
        this.dataForm.issueId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issueId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetable/info/${this.dataForm.issueId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.serialNumber = data.issueTable.serialNumber
                this.dataForm.issueNumber = data.issueTable.issueNumber
                this.dataForm.inspectionDepartment = data.issueTable.inspectionDepartment
                this.dataForm.inspectionDate = data.issueTable.inspectionDate
                this.dataForm.issueCategoryId = data.issueTable.issueCategoryId
                this.dataForm.vehicleTypeId = data.issueTable.vehicleTypeId
                this.dataForm.vehicleNumberId = data.issueTable.vehicleNumberId
                this.dataForm.issueDescription = data.issueTable.issueDescription
                this.dataForm.issuePhoto = data.issueTable.issuePhoto
                this.dataForm.rectificationRequirement = data.issueTable.rectificationRequirement
                this.dataForm.requiredCompletionTime = data.issueTable.requiredCompletionTime
                this.dataForm.responsibleDepartment = data.issueTable.responsibleDepartment
                this.dataForm.rectificationStatus = data.issueTable.rectificationStatus
                this.dataForm.actualCompletionTime = data.issueTable.actualCompletionTime
                this.dataForm.rectificationPhotoDeliverable = data.issueTable.rectificationPhotoDeliverable
                this.dataForm.rectificationResponsiblePerson = data.issueTable.rectificationResponsiblePerson
                this.dataForm.requiredSecondRectificationTime = data.issueTable.requiredSecondRectificationTime
                this.dataForm.remark = data.issueTable.remark
                this.dataForm.creator = data.issueTable.creator
                this.dataForm.creationTime = data.issueTable.creationTime
                this.dataForm.lastModifier = data.issueTable.lastModifier
                this.dataForm.lastModificationTime = data.issueTable.lastModificationTime
                this.dataForm.associatedRectificationRecords = data.issueTable.associatedRectificationRecords
                this.dataForm.associatedIssueAddition = data.issueTable.associatedIssueAddition
                this.dataForm.creationDuration = data.issueTable.creationDuration
                this.dataForm.causeAnalysis = data.issueTable.causeAnalysis
                this.dataForm.rectificationVerificationStatus = data.issueTable.rectificationVerificationStatus
                this.dataForm.verificationConclusion = data.issueTable.verificationConclusion
                this.dataForm.verifier = data.issueTable.verifier
                this.dataForm.formula = data.issueTable.formula
              }
            })
          }
        })
      },
      init1 (id) {
        this.dataForm.issueId = id || 0
        this.visible1 = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issueId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetable/info/${this.dataForm.issueId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.serialNumber = data.issueTable.serialNumber
                this.dataForm.issueNumber = data.issueTable.issueNumber
                this.dataForm.inspectionDepartment = data.issueTable.inspectionDepartment
                this.dataForm.inspectionDate = data.issueTable.inspectionDate
                this.dataForm.issueCategoryId = data.issueTable.issueCategoryId
                this.dataForm.vehicleTypeId = data.issueTable.vehicleTypeId
                this.dataForm.vehicleNumberId = data.issueTable.vehicleNumberId
                this.dataForm.issueDescription = data.issueTable.issueDescription
                this.dataForm.issuePhoto = data.issueTable.issuePhoto
                this.dataForm.rectificationRequirement = data.issueTable.rectificationRequirement
                this.dataForm.requiredCompletionTime = data.issueTable.requiredCompletionTime
                this.dataForm.responsibleDepartment = data.issueTable.responsibleDepartment
                this.dataForm.rectificationStatus = data.issueTable.rectificationStatus
                this.dataForm.actualCompletionTime = data.issueTable.actualCompletionTime
                this.dataForm.rectificationPhotoDeliverable = data.issueTable.rectificationPhotoDeliverable
                this.dataForm.rectificationResponsiblePerson = data.issueTable.rectificationResponsiblePerson
                this.dataForm.requiredSecondRectificationTime = data.issueTable.requiredSecondRectificationTime
                this.dataForm.remark = data.issueTable.remark
                this.dataForm.creator = data.issueTable.creator
                this.dataForm.creationTime = data.issueTable.creationTime
                this.dataForm.lastModifier = data.issueTable.lastModifier
                this.dataForm.lastModificationTime = data.issueTable.lastModificationTime
                this.dataForm.associatedRectificationRecords = data.issueTable.associatedRectificationRecords
                this.dataForm.associatedIssueAddition = data.issueTable.associatedIssueAddition
                this.dataForm.creationDuration = data.issueTable.creationDuration
                this.dataForm.causeAnalysis = data.issueTable.causeAnalysis
                this.dataForm.rectificationVerificationStatus = data.issueTable.rectificationVerificationStatus
                this.dataForm.verificationConclusion = data.issueTable.verificationConclusion
                this.dataForm.verifier = data.issueTable.verifier
                this.dataForm.formula = data.issueTable.formula
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetable/mask`),
              method: 'post',
              data: this.$http.adornData({
                'issueId': this.dataForm.issueId || undefined,
                'serialNumber': this.dataForm.serialNumber,
                'issueNumber': this.dataForm.issueNumber,
                'inspectionDepartment': this.dataForm.inspectionDepartment,
                'inspectionDate': this.dataForm.inspectionDate,
                'issueCategoryId': this.dataForm.issueCategoryId,
                'vehicleTypeId': this.dataForm.vehicleTypeId,
                'vehicleNumberId': this.dataForm.vehicleNumberId,
                'issueDescription': this.dataForm.issueDescription,
                'issuePhoto': this.dataForm.issuePhoto,
                'rectificationRequirement': this.dataForm.rectificationRequirement,
                'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                'responsibleDepartment': this.dataForm.responsibleDepartment,
                'rectificationStatus': this.dataForm.rectificationStatus,
                'actualCompletionTime': this.dataForm.actualCompletionTime,
                'rectificationPhotoDeliverable': this.dataForm.rectificationPhotoDeliverable,
                'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
                'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
                'remark': this.dataForm.remark,
                'creator': this.dataForm.creator,
                'creationTime': this.dataForm.creationTime,
                'lastModifier': this.dataForm.lastModifier,
                'lastModificationTime': this.dataForm.lastModificationTime,
                'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
                'associatedIssueAddition': this.dataForm.associatedIssueAddition,
                'creationDuration': this.dataForm.creationDuration,
                'causeAnalysis': this.dataForm.causeAnalysis,
                'rectificationVerificationStatus': this.dataForm.rectificationVerificationStatus,
                'verificationConclusion': this.dataForm.verificationConclusion,
                'verifier': this.dataForm.verifier,
                'formula': this.dataForm.formula
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
