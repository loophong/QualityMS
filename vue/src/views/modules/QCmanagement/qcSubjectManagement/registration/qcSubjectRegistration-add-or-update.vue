<template>
  <el-dialog :title="!dataForm.qcsrId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="150px">
      <el-form-item label="课题名称" prop="topicName">
        <el-input v-model="dataForm.topicName" placeholder="课题名称"></el-input>
      </el-form-item>
      <el-form-item label="课题编号" prop="topicNumber">
        <el-input v-model="dataForm.topicNumber" placeholder="课题编号"></el-input>
      </el-form-item>
      <!-- <el-form-item label="课题组长" prop="topicLeader">
        <el-input v-model="dataForm.topicLeader" placeholder="课题组长"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="课题顾问" prop="topicConsultant">
        <el-input v-model="dataForm.topicConsultant" placeholder="课题顾问"></el-input>
      </el-form-item> -->
      <el-form-item label="课题顾问" prop="topicConsultant">
        <el-select v-model="dataForm.topicConsultant" multiple placeholder="请选择课题顾问">
          <el-option v-for="item in consultantOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="小组成员" prop="teamNumberIds">
        <el-input v-model="dataForm.teamNumberIds" placeholder="小组成员"></el-input>
      </el-form-item> -->
      <el-form-item label="小组成员" prop="teamNumberIds">
        <el-select v-model="dataForm.teamNumberIds" multiple placeholder="请选择小组成员">
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
        <el-input type="textarea" v-model="dataForm.note" placeholder="备注"></el-input>
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
      flag: 1,
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
        activityPlanEnd: '',
        keywords: '',
        topicActivityStatus: '',
        topicActivityResult: '',
        deleteFlag: '',
        note: ''
      },
      dataList: [],
      consultantOptions: [
        { value: 'consultant1', label: '顾问1' },
        { value: 'consultant2', label: '顾问2' },
        // 其他顾问选项
      ],
      membersSelect: [
        { value: 'member1', label: '成员1' },
        { value: 'member2', label: '成员2' },
        { value: 'member3', label: '成员3' },
        { value: 'member4', label: '成员4' },
        { value: 'member5', label: '成员5' },
        // 其他顾问选项
      ],

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
        startDate: [
          { required: true, message: '开始日期不能为空', trigger: 'blur' }
        ],
        endDate: [
          { required: true, message: '结束日期不能为空', trigger: 'blur' }
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
          { required: true, message: '活动计划不能为空', trigger: 'blur' }
        ],
        keywords: [
          { required: true, message: '课题关键字tag不能为空', trigger: 'blur' }
        ],
        topicActivityStatus: [
          { required: true, message: '课题活动状态不能为空', trigger: 'blur' }
        ],

      }
    }
  },
  mounted: {

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
              this.dataForm.activityPlanEnd = data.qcSubjectRegistration.activityPlanEnd
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

    ifUpdate() {
      if (this.dataForm.qcsrId) {
        this.flag = 1;
      }
    },
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
              url: this.$http.adornUrl(`/qcSubject/registration/${!this.dataForm.qcsrId ? 'save' : 'update'}`),
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
                'activityPlan': startDatePlan,
                'activityPlanEnd': endDatePlan,
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
    },
    getDataList() {
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
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
      })

    },
  }
}
</script>
