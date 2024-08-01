<template>
  <el-dialog :title="!dataForm.qcsrId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="80px">
      <el-form-item label="课题名称" prop="topicName">
        <el-input v-model="dataForm.topicName" placeholder="课题名称"></el-input>
      </el-form-item>
      <el-form-item label="课题编号" prop="topicNumber">
        <el-input v-model="dataForm.topicNumber" placeholder="课题编号"></el-input>
      </el-form-item>
      <el-form-item label="课题组长" prop="topicLeader">
        <el-input v-model="dataForm.topicLeader" placeholder="课题组长"></el-input>
      </el-form-item>
      <el-form-item label="课题顾问" prop="topicConsultant">
        <el-input v-model="dataForm.topicConsultant" placeholder="课题顾问"></el-input>
      </el-form-item>
      <el-form-item label="小组成员ids" prop="teamNumberIds">
        <el-input v-model="dataForm.teamNumberIds" placeholder="小组成员ids"></el-input>
      </el-form-item>
      <!-- <el-form-item label="创建日期" prop="createDate">
      <el-input v-model="dataForm.createDate" placeholder="创建日期"></el-input>
    </el-form-item>
    <el-form-item label="创建人" prop="creator">
      <el-input v-model="dataForm.creator" placeholder="创建人"></el-input>
    </el-form-item>
    <el-form-item label="修改日期" prop="modificationDate">
      <el-input v-model="dataForm.modificationDate" placeholder="修改日期"></el-input>
    </el-form-item>
    <el-form-item label="修改人" prop="modifier">
      <el-input v-model="dataForm.modifier" placeholder="修改人"></el-input>
    </el-form-item> -->
      <el-form-item label="开始日期" prop="startDate">
        <el-input v-model="dataForm.startDate" placeholder="开始日期"></el-input>
      </el-form-item>
      <el-form-item label="结束日期" prop="endDate">
        <el-input v-model="dataForm.endDate" placeholder="结束日期"></el-input>
      </el-form-item>
      <!-- <el-form-item label="课题审核状态" prop="topicReviewStatus">
        <el-input v-model="dataForm.topicReviewStatus" placeholder="课题审核状态"></el-input>
      </el-form-item> -->
      <el-form-item label="课题描述/摘要" prop="topicDescription">
        <el-input v-model="dataForm.topicDescription" placeholder="课题描述/摘要"></el-input>
      </el-form-item>
      <!-- <el-form-item label="课题类型" prop="topicType">
      <el-input v-model="dataForm.topicType" placeholder="课题类型"></el-input>
    </el-form-item> -->
      <el-form-item label="课题类型" prop="topicType">
        <el-select v-model="dataForm.topicType" placeholder="请选择QC课题类型">
          <el-option label="问题解决型" value="问题解决型"></el-option>
          <el-option label="问题解决型(指令型)" value="问题解决型(指令型)"></el-option>
          <el-option label="创新型" value="创新型"></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="活动特性" prop="activityCharacteristics">
        <el-input v-model="dataForm.activityCharacteristics" placeholder="活动特性"></el-input>
      </el-form-item> -->
      <el-form-item label="活动特性" prop="activityCharacteristics">
        <el-select v-model="dataForm.activityCharacteristics" placeholder="请选择活动特性">
          <el-option label="短期" value="短期"></el-option>
          <el-option label="长期" value="长期"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="活动计划(细分日期)" prop="activityPlan">
        <el-input v-model="dataForm.activityPlan" placeholder="活动计划(细分日期)"></el-input>
      </el-form-item>
      <el-form-item label="课题关键字tag" prop="keywords">
        <el-input v-model="dataForm.keywords" placeholder="课题关键字tag"></el-input>
      </el-form-item>
      <el-form-item label="课题活动状态" prop="topicActivityStatus">
        <el-input v-model="dataForm.topicActivityStatus" placeholder="课题活动状态"></el-input>
      </el-form-item>
      <!-- <el-form-item label="课题活动评分结果" prop="topicActivityResult">
      <el-input v-model="dataForm.topicActivityResult" placeholder="课题活动评分结果"></el-input>
    </el-form-item> -->
      <!-- <el-form-item label="删除标记位" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder="删除标记位"></el-input>
    </el-form-item> -->
      <el-form-item label="备注" prop="note">
        <el-input v-model="dataForm.note" placeholder="备注"></el-input>
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
        topicName: [
          { required: true, message: '课题名称不能为空', trigger: 'blur' }
        ],
        topicNumber: [
          { required: true, message: '课题编号不能为空', trigger: 'blur' }
        ],
        topicLeader: [
          { required: true, message: '课题组长不能为空', trigger: 'blur' }
        ],
        topicConsultant: [
          { required: true, message: '课题顾问不能为空', trigger: 'blur' }
        ],
        teamNumberIds: [
          { required: true, message: '小组成员ids不能为空', trigger: 'blur' }
        ],
        createDate: [
          { required: true, message: '创建日期不能为空', trigger: 'blur' }
        ],
        creator: [
          { required: true, message: '创建人不能为空', trigger: 'blur' }
        ],
        modificationDate: [
          { required: true, message: '修改日期不能为空', trigger: 'blur' }
        ],
        modifier: [
          { required: true, message: '修改人不能为空', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '开始日期不能为空', trigger: 'blur' }
        ],
        endDate: [
          { required: true, message: '结束日期不能为空', trigger: 'blur' }
        ],
        topicReviewStatus: [
          { required: true, message: '课题审核状态不能为空', trigger: 'blur' }
        ],
        topicDescription: [
          { required: true, message: '课题描述/摘要不能为空', trigger: 'blur' }
        ],
        topicType: [
          { required: true, message: '课题类型不能为空', trigger: 'blur' }
        ],
        activityCharacteristics: [
          { required: true, message: '活动特性不能为空', trigger: 'blur' }
        ],
        activityPlan: [
          { required: true, message: '活动计划(细分日期)不能为空', trigger: 'blur' }
        ],
        keywords: [
          { required: true, message: '课题关键字tag不能为空', trigger: 'blur' }
        ],
        topicActivityStatus: [
          { required: true, message: '课题活动状态不能为空', trigger: 'blur' }
        ],
        topicActivityResult: [
          { required: true, message: '课题活动评分结果不能为空', trigger: 'blur' }
        ],
        deleteFlag: [
          { required: true, message: '删除标记位不能为空', trigger: 'blur' }
        ],
        note: [
          { required: true, message: '备注不能为空', trigger: 'blur' }
        ]
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
            url: this.$http.adornUrl(`/generator/qcsubjectregistration/info/${this.dataForm.qcsrId}`),
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
            url: this.$http.adornUrl(`/generator/qcsubjectregistration/${!this.dataForm.qcsrId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'qcsrId': this.dataForm.qcsrId || undefined,
              'topicName': this.dataForm.topicName,
              'topicNumber': this.dataForm.topicNumber,
              'topicLeader': this.dataForm.topicLeader,
              'topicConsultant': this.dataForm.topicConsultant,
              'teamNumberIds': this.dataForm.teamNumberIds,
              'createDate': this.dataForm.createDate,
              'creator': this.dataForm.creator,
              'modificationDate': this.dataForm.modificationDate,
              'modifier': this.dataForm.modifier,
              'startDate': this.dataForm.startDate,
              'endDate': this.dataForm.endDate,
              'topicReviewStatus': this.dataForm.topicReviewStatus,
              'topicDescription': this.dataForm.topicDescription,
              'topicType': this.dataForm.topicType,
              'activityCharacteristics': this.dataForm.activityCharacteristics,
              'activityPlan': this.dataForm.activityPlan,
              'keywords': this.dataForm.keywords,
              'topicActivityStatus': this.dataForm.topicActivityStatus,
              'topicActivityResult': this.dataForm.topicActivityResult,
              'deleteFlag': this.dataForm.deleteFlag,
              'note': this.dataForm.note
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
