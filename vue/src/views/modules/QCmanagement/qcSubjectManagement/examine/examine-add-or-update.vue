<template>
  <el-dialog :title="!dataForm.qcsrId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="150px">
      <el-form-item label="课题名称" prop="topicName">
        <el-input v-model="dataForm.topicName" placeholder="课题名称" disabled></el-input>
      </el-form-item>
      <el-form-item label="课题编号" prop="topicNumber">
        <el-input v-model="dataForm.topicNumber" placeholder="课题编号" disabled></el-input>
      </el-form-item>
      <el-form-item label="课题组长" prop="topicLeader">
        <el-input v-model="dataForm.topicLeader" placeholder="课题组长" disabled></el-input>
      </el-form-item>
      <el-form-item label="课题顾问" prop="topicConsultant">
        <el-input v-model="dataForm.topicConsultant" placeholder="课题顾问" disabled></el-input>
      </el-form-item>
      <!-- <el-form-item label="小组成员ids" prop="teamNumberIds">
        <el-input v-model="dataForm.teamNumberIds" placeholder="小组成员ids"></el-input>
      </el-form-item>     -->
      <el-form-item label="开始日期" prop="startDate">
        <el-date-picker clearable v-model="dataForm.startDate" type="date" value-format="yyyy-MM-dd" placeholder="开始日期"
          disabled>
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期" prop="endDate">
        <el-date-picker clearable v-model="dataForm.endDate" type="date" value-format="yyyy-MM-dd" placeholder="结束日期"
          disabled>
        </el-date-picker>
      </el-form-item>
      <el-form-item label="课题描述/摘要" prop="topicDescription">
        <el-input v-model="dataForm.topicDescription" placeholder="课题描述/摘要" disabled></el-input>
      </el-form-item>
      <el-form-item label="课题类型" prop="topicType">
        <el-select v-model="dataForm.topicType" placeholder="请选择QC课题类型" disabled>
          <el-option label="问题解决型" value="问题解决型"></el-option>
          <el-option label="问题解决型(指令型)" value="问题解决型(指令型)"></el-option>
          <el-option label="创新型" value="创新型"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="活动特性" prop="activityCharacteristics">
        <el-select v-model="dataForm.activityCharacteristics" placeholder="请选择活动特性" disabled>
          <el-option label="短期" value="短期"></el-option>
          <el-option label="长期" value="长期"></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="活动计划(细分日期)" prop="activityPlan">
        <el-input v-model="dataForm.activityPlan" placeholder="活动计划(细分日期)"></el-input>
      </el-form-item> -->
      <el-form-item label="活动计划" prop="activityPlan">
        <el-date-picker clearable v-model="dataForm.activityPlan" type="date" value-format="yyyy-MM-dd"
          placeholder="活动计划" disabled>
        </el-date-picker>
      </el-form-item>
      <el-form-item label="课题关键字tag" prop="keywords">
        <el-input v-model="dataForm.keywords" placeholder="课题关键字tag" disabled></el-input>
      </el-form-item>
      <el-form-item label="课题活动状态" prop="topicActivityStatus">
        <el-input v-model="dataForm.topicActivityStatus" placeholder="课题活动状态" disabled></el-input>
      </el-form-item>
      <!-- <el-form-item label="课题活动评分结果" prop="topicActivityResult">
        <el-input v-model="dataForm.topicActivityResult" placeholder="课题活动评分结果"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="删除标记位" prop="deleteFlag">
        <el-input v-model="dataForm.deleteFlag" placeholder="删除标记位"></el-input>
      </el-form-item> -->
      <el-form-item label="备注" prop="note">
        <el-input v-model="dataForm.note" placeholder="备注" disabled></el-input>
      </el-form-item>
      <el-form-item label="课题审核结果" prop="topicReviewStatus">
        <el-select v-model="dataForm.topicReviewStatus" placeholder="请选择课题审核结果">
          <el-option label="通过" value="3"></el-option>
          <el-option label="不通过" value="0"></el-option>
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
        note: ''
      },
      dataRule: {
        topicReviewStatus: [
          { required: true, message: '课题审核状态不能为空', trigger: 'blur' }
        ],

      }
    }
  },
  methods: {
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
              this.dataForm.topicLeader = data.qcSubjectRegistration.topicLeader
              this.dataForm.topicConsultant = data.qcSubjectRegistration.topicConsultant
              this.dataForm.teamNumberIds = data.qcSubjectRegistration.teamNumberIds
              this.dataForm.createDate = data.qcSubjectRegistration.createDate
              this.dataForm.creator = data.qcSubjectRegistration.creator
              this.dataForm.modificationDate = data.qcSubjectRegistration.modificationDate
              this.dataForm.modifier = data.qcSubjectRegistration.modifier
              this.dataForm.startDate = data.qcSubjectRegistration.startDate
              this.dataForm.endDate = data.qcSubjectRegistration.endDate
              this.dataForm.topicReviewStatus = data.qcSubjectRegistration.topicReviewStatus
              this.dataForm.topicDescription = data.qcSubjectRegistration.topicDescription
              this.dataForm.topicType = data.qcSubjectRegistration.topicType
              this.dataForm.activityCharacteristics = data.qcSubjectRegistration.activityCharacteristics
              this.dataForm.activityPlan = data.qcSubjectRegistration.activityPlan
              this.dataForm.keywords = data.qcSubjectRegistration.keywords
              this.dataForm.topicActivityStatus = data.qcSubjectRegistration.topicActivityStatus
              this.dataForm.topicActivityResult = data.qcSubjectRegistration.topicActivityResult
              this.dataForm.deleteFlag = data.qcSubjectRegistration.deleteFlag
              this.dataForm.note = data.qcSubjectRegistration.note
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/qcSubject/registration/${!this.dataForm.qcsrId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'qcsrId': this.dataForm.qcsrId || undefined,
              'topicReviewStatus': this.dataForm.topicReviewStatus,
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
