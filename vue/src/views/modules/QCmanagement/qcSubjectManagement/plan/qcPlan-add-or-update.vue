<template>
  <el-dialog :title="!dataForm.qcsrId ? '新增' : '提交计划'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="80px">

      <el-form-item label="课题类型" prop="resultType">
        <el-select v-model="dataForm.resultType" multiple placeholder="请选择" @change="handleSelectChange">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="初评人员" prop="firstExaminePeople">
        <el-select v-model="dataForm.firstExaminePeople" clearable multiple filterable placeholder="请选择角色">
          <el-option-group v-for="group in usersOption" :key="group.label" :label="group.label">
            <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.label">
            </el-option>
          </el-option-group>
        </el-select>
      </el-form-item>
      <el-form-item label="复评人员" prop="secondExaminePeople">
        <el-select v-model="dataForm.secondExaminePeople" clearable multiple filterable placeholder="请选择角色">
          <el-option-group v-for="group in usersOption" :key="group.label" :label="group.label">
            <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.label">
            </el-option>
          </el-option-group>
        </el-select>
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
      usersOption: [],
      options: [{
        value: '安全',
        label: '安全'
      }, {
        value: '设备',
        label: '设备'
      }, {
        value: '质量',
        label: '质量'
      }, {
        value: '生产',
        label: '生产'
      }, {
        value: '技术',
        label: '技术'
      }, {
        value: '其他',
        label: '其他'
      }],
      visible: false,
      dataForm: {
        qcsrId: 0,
        topicName: '',
        topicNumber: '',
        topicLeader: '',
        topicConsultant: '',
        teamNumberIds: '',
        createDate: '',
        creator: '',
        modificationDate: '',
        modifier: '',
        startDate: '',
        endDate: '',
        topicReviewStatus: '',
        topicDescription: '',
        topicType: '',
        activityCharacteristics: '',
        activityPlan: '',
        keywords: '',
        topicActivityStatus: '',
        topicActivityResult: '',
        deleteFlag: '',
        note: '',
        resultType: [],
        firstExaminePeople: [],
        secondExaminePeople: [],
      },
      dataFormEx: {
        qcExamineId: 0,
        qcExamineSubject: '',
        qcExamineCurrent: '',
        qcStatusOne: '',
        qcStatusTwo: '',
        qcStatusThree: '',
        qcStatusFour: '',
        qcStatusFive: '',
        qcStatusSix: '',
        qcStatusSeven: '',
        qcSevenContent: '',
        qcSixContent: '',
        qcFiveContent: '',
        qcSecondScore: '',
        qcFourContent: '',
        qcFirstScore: '',
        qcThreeContent: '',
        qcTwoContent: '',
        qcOneContent: ''
      },
      dataRule: {
        resultType: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleSelectChange(value) {
      // 当选择了"其他"，并且还有其他选项被选中时
      if (value.includes('其他') && value.length > 1) {
        // 清除其他选择，只保留"其他"
        this.dataForm.resultType = ['其他'];
      }
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
            let resultTypeString = data.qcSubjectRegistration.resultType;
            if (resultTypeString != '') {
              var resultArray = resultTypeString.split(",");
            } else {
              var resultArray = ''
            }
            if (data && data.code === 0) {
              this.dataForm.topicReviewStatus = data.qcSubjectRegistration.topicReviewStatus
              this.dataForm.resultType = resultArray
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          let tmpFirstList = this.dataForm.firstExaminePeople.map(item => ({
            name: item,
            score: [],
            result: '',
            comment: ''
          }));
          let tmpSecondList = this.dataForm.secondExaminePeople.map(item => ({
            name: item,
            score: [],
            result: '',
            comment: ''
          }));
          this.$http({
            url: this.$http.adornUrl(`/qcSubject/registration/update`),
            method: 'post',
            data: this.$http.adornData({
              'qcsrId': this.dataForm.qcsrId || undefined,
              'resultType': `${this.dataForm.resultType}`,
              'firstExaminePeople': JSON.stringify(this.dataForm.firstExaminePeople),
              'secondExaminePeople': JSON.stringify(this.dataForm.secondExaminePeople),
              'topicReviewStatus': 3,
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$http({
                url: this.$http.adornUrl(`/qcManagement/examineStatus/save`),
                method: 'post',
                data: this.$http.adornData({
                  'qcExamineId': this.dataFormEx.qcExamineId || undefined,
                  'qcExamineSubject': this.dataForm.qcsrId,
                  'qcExamineCurrent': '0',
                  'qcStatusOne': '0',
                  'qcFirstAll': JSON.stringify(tmpFirstList),
                  'qcSecondAll': JSON.stringify(tmpSecondList),
                })
              }).then(({ data }) => {
                if (data && data.code === 0) {
                  this.$http({
                    url: this.$http.adornUrl(`/qcSubject/registration/update`),
                    method: 'post',
                    data: this.$http.adornData({
                      'qcsrId': this.dataForm.qcsrId || undefined,
                      'topicReviewStatus': 3,
                      'examineId': data.id,
                    })
                  })
                } else {
                  this.$message.error(data.msg)
                }
              })
              this.$message({
                message: '提交成功',
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
      })
    }
  }
}
</script>
