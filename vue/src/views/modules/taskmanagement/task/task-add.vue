<template>
  <el-dialog :title="!dataForm.taskId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="80px">
      <el-form-item label="任务内容" prop="taskContent">
        <el-input v-model="dataForm.taskContent" placeholder="任务内容"></el-input>
      </el-form-item>
      <el-form-item label="开始日期" prop="startDate">
        <el-input v-model="dataForm.startDate" placeholder="开始日期"></el-input>
      </el-form-item>
      <el-form-item label="计划完成日期" prop="plannedFinishDate">
        <el-input v-model="dataForm.plannedFinishDate" placeholder="计划完成日期"></el-input>
      </el-form-item>
      <el-form-item label="计划天数" prop="scheduleDays">
        <el-input v-model="dataForm.scheduleDays" placeholder="计划天数"></el-input>
      </el-form-item>
      <el-form-item label="实际完成日期" prop="actualFinishingDate">
        <el-input v-model="dataForm.actualFinishingDate" placeholder="实际完成日期"></el-input>
      </el-form-item>
      <el-form-item label="实际天数" prop="actualDays">
        <el-input v-model="dataForm.actualDays" placeholder="实际天数"></el-input>
      </el-form-item>
      <el-form-item label="相关计划编号" prop="relevantProjectNumber">
        <el-input v-model="dataForm.relevantProjectNumber" placeholder="相关计划编号"></el-input>
      </el-form-item>
      <el-form-item label="进度" prop="schedule">
        <el-input v-model="dataForm.schedule" placeholder="进度"></el-input>
      </el-form-item>
      <el-form-item label="负责人" prop="principal">
        <el-input v-model="dataForm.principal" placeholder="负责人"></el-input>
      </el-form-item>
      <el-form-item label="执行人" prop="executor">
        <el-input v-model="dataForm.executor" placeholder="执行人"></el-input>
      </el-form-item>
      <el-form-item label="审核人" prop="auditor">
        <el-input v-model="dataForm.auditor" placeholder="审核人"></el-input>
      </el-form-item>
      <el-form-item label="当前状态" prop="currentState">
        <el-input v-model="dataForm.currentState" placeholder="当前状态"></el-input>
      </el-form-item>
      <el-form-item label="是否完成" prop="whetherComplete">
        <el-input v-model="dataForm.whetherComplete" placeholder="是否完成"></el-input>
      </el-form-item>
      <el-form-item label="是否超期" prop="whetherOverdue">
        <el-input v-model="dataForm.whetherOverdue" placeholder="是否超期"></el-input>
      </el-form-item>
      <el-form-item label="按时完工" prop="finishOnTime">
        <el-input v-model="dataForm.finishOnTime" placeholder="按时完工"></el-input>
      </el-form-item>
      <el-form-item label="提前完工" prop="daysAheadOfSchedule">
        <el-input v-model="dataForm.daysAheadOfSchedule" placeholder="提前完工"></el-input>
      </el-form-item>
      <el-form-item label="滞后天数" prop="lagDays">
        <el-input v-model="dataForm.lagDays" placeholder="滞后天数"></el-input>
      </el-form-item>
      <el-form-item label="滞后原因" prop="delayReasons">
        <el-input v-model="dataForm.delayReasons" placeholder="滞后原因"></el-input>
      </el-form-item>
      <el-form-item label="关联指标编号" prop="associatedIndexNumber">
        <el-input v-model="dataForm.associatedIndexNumber" placeholder="关联指标编号"></el-input>
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
        taskId: 0,
        taskContent: '',
        startDate: '',
        plannedFinishDate: '',
        scheduleDays: '',
        actualFinishingDate: '',
        actualDays: '',
        relevantProjectNumber: '',
        schedule: '',
        principal: '',
        executor: '',
        auditor: '',
        currentState: '',
        whetherComplete: '',
        whetherOverdue: '',
        finishOnTime: '',
        daysAheadOfSchedule: '',
        lagDays: '',
        delayReasons: '',
        associatedIndexNumber: ''
      },
      dataRule: {
        taskContent: [
          { required: true, message: '任务内容不能为空', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '开始日期不能为空', trigger: 'blur' }
        ],
        plannedFinishDate: [
          { required: true, message: '计划完成日期不能为空', trigger: 'blur' }
        ],
        scheduleDays: [
          { required: true, message: '计划天数不能为空', trigger: 'blur' }
        ],
        actualFinishingDate: [
          { required: true, message: '实际完成日期不能为空', trigger: 'blur' }
        ],
        actualDays: [
          { required: true, message: '实际天数不能为空', trigger: 'blur' }
        ],
        relevantProjectNumber: [
          { required: true, message: '相关计划编号不能为空', trigger: 'blur' }
        ],
        schedule: [
          { required: true, message: '进度不能为空', trigger: 'blur' }
        ],
        principal: [
          { required: true, message: '负责人不能为空', trigger: 'blur' }
        ],
        executor: [
          { required: true, message: '执行人不能为空', trigger: 'blur' }
        ],
        auditor: [
          { required: true, message: '审核人不能为空', trigger: 'blur' }
        ],
        currentState: [
          { required: true, message: '当前状态不能为空', trigger: 'blur' }
        ],
        whetherComplete: [
          { required: true, message: '是否完成不能为空', trigger: 'blur' }
        ],
        whetherOverdue: [
          { required: true, message: '是否超期不能为空', trigger: 'blur' }
        ],
        finishOnTime: [
          { required: true, message: '按时完工不能为空', trigger: 'blur' }
        ],
        daysAheadOfSchedule: [
          { required: true, message: '提前完工不能为空', trigger: 'blur' }
        ],
        lagDays: [
          { required: true, message: '滞后天数不能为空', trigger: 'blur' }
        ],
        delayReasons: [
          { required: true, message: '滞后原因不能为空', trigger: 'blur' }
        ],
        associatedIndexNumber: [
          { required: true, message: '关联指标编号不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.taskId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.taskId) {
          this.$http({
            url: this.$http.adornUrl(`/taskmanagement/task/info/${this.dataForm.taskId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.taskContent = data.taskDataTask.taskContent
              this.dataForm.startDate = data.taskDataTask.startDate
              this.dataForm.plannedFinishDate = data.taskDataTask.plannedFinishDate
              this.dataForm.scheduleDays = data.taskDataTask.scheduleDays
              this.dataForm.actualFinishingDate = data.taskDataTask.actualFinishingDate
              this.dataForm.actualDays = data.taskDataTask.actualDays
              this.dataForm.relevantProjectNumber = data.taskDataTask.relevantProjectNumber
              this.dataForm.schedule = data.taskDataTask.schedule
              this.dataForm.principal = data.taskDataTask.principal
              this.dataForm.executor = data.taskDataTask.executor
              this.dataForm.auditor = data.taskDataTask.auditor
              this.dataForm.currentState = data.taskDataTask.currentState
              this.dataForm.whetherComplete = data.taskDataTask.whetherComplete
              this.dataForm.whetherOverdue = data.taskDataTask.whetherOverdue
              this.dataForm.finishOnTime = data.taskDataTask.finishOnTime
              this.dataForm.daysAheadOfSchedule = data.taskDataTask.daysAheadOfSchedule
              this.dataForm.lagDays = data.taskDataTask.lagDays
              this.dataForm.delayReasons = data.taskDataTask.delayReasons
              this.dataForm.associatedIndexNumber = data.taskDataTask.associatedIndexNumber
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
            url: this.$http.adornUrl(`/taskmanagement/task/${!this.dataForm.taskId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'taskId': this.dataForm.taskId || undefined,
              'taskContent': this.dataForm.taskContent,
              'startDate': this.dataForm.startDate,
              'plannedFinishDate': this.dataForm.plannedFinishDate,
              'scheduleDays': this.dataForm.scheduleDays,
              'actualFinishingDate': this.dataForm.actualFinishingDate,
              'actualDays': this.dataForm.actualDays,
              'relevantProjectNumber': this.dataForm.relevantProjectNumber,
              'schedule': this.dataForm.schedule,
              'principal': this.dataForm.principal,
              'executor': this.dataForm.executor,
              'auditor': this.dataForm.auditor,
              'currentState': this.dataForm.currentState,
              'whetherComplete': this.dataForm.whetherComplete,
              'whetherOverdue': this.dataForm.whetherOverdue,
              'finishOnTime': this.dataForm.finishOnTime,
              'daysAheadOfSchedule': this.dataForm.daysAheadOfSchedule,
              'lagDays': this.dataForm.lagDays,
              'delayReasons': this.dataForm.delayReasons,
              'associatedIndexNumber': this.dataForm.associatedIndexNumber
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
