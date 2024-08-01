<template>
  <el-dialog :title="!dataForm.tmTid ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="80px">
      <el-form-item label="任务ID" prop="taskId">
        <el-input v-model="dataForm.taskId" placeholder="任务ID"></el-input>
      </el-form-item>
      <el-form-item label="任务名" prop="taskName">
        <el-input v-model="dataForm.taskName" placeholder="任务名"></el-input>
      </el-form-item>
      <el-form-item label="任务内容" prop="taskContent">
        <el-input v-model="dataForm.taskContent" placeholder="任务内容"></el-input>
      </el-form-item>
      <el-form-item label="开始日期" prop="taskStartDate">
        <el-input v-model="dataForm.taskStartDate" placeholder="开始日期"></el-input>
      </el-form-item>
      <el-form-item label="计划完成日期" prop="taskScheduleCompletionDate">
        <el-input v-model="dataForm.taskScheduleCompletionDate" placeholder="计划完成日期"></el-input>
      </el-form-item>
      <el-form-item label="计划天数" prop="taskScheduleDays">
        <el-input v-model="dataForm.taskScheduleDays" placeholder="计划天数"></el-input>
      </el-form-item>
      <el-form-item label="实际完成日期" prop="taskActualCompletionDate">
        <el-input v-model="dataForm.taskActualCompletionDate" placeholder="实际完成日期"></el-input>
      </el-form-item>
      <el-form-item label="实际天数" prop="taskActualDays">
        <el-input v-model="dataForm.taskActualDays" placeholder="实际天数"></el-input>
      </el-form-item>
      <el-form-item label="相关计划编号" prop="taskAssociatedPlanId">
        <el-input v-model="dataForm.taskAssociatedPlanId" placeholder="相关计划编号"></el-input>
      </el-form-item>
      <el-form-item label="进度" prop="taskSchedule">
        <el-input v-model="dataForm.taskSchedule" placeholder="进度"></el-input>
      </el-form-item>
      <el-form-item label="负责人" prop="taskPrincipal">
        <el-input v-model="dataForm.taskPrincipal" placeholder="负责人"></el-input>
      </el-form-item>
      <el-form-item label="执行人" prop="taskExecutor">
        <el-input v-model="dataForm.taskExecutor" placeholder="执行人"></el-input>
      </el-form-item>
      <el-form-item label="审核人" prop="taskAuditor">
        <el-input v-model="dataForm.taskAuditor" placeholder="审核人"></el-input>
      </el-form-item>
      <el-form-item label="当前状态" prop="taskCurrentState">
        <el-input v-model="dataForm.taskCurrentState" placeholder="当前状态"></el-input>
      </el-form-item>
      <el-form-item label="是否完成" prop="taskIsCompleted">
        <el-input v-model="dataForm.taskIsCompleted" placeholder="是否完成"></el-input>
      </el-form-item>
      <el-form-item label="是否超期" prop="taskIsOverdue">
        <el-input v-model="dataForm.taskIsOverdue" placeholder="是否超期"></el-input>
      </el-form-item>
      <el-form-item label="按时完工" prop="taskIsOnTime">
        <el-input v-model="dataForm.taskIsOnTime" placeholder="按时完工"></el-input>
      </el-form-item>
      <el-form-item label="提前完工" prop="taskEarlyCompletionDays">
        <el-input v-model="dataForm.taskEarlyCompletionDays" placeholder="提前完工"></el-input>
      </el-form-item>
      <el-form-item label="滞后天数" prop="taskLagDays">
        <el-input v-model="dataForm.taskLagDays" placeholder="滞后天数"></el-input>
      </el-form-item>
      <el-form-item label="滞后原因" prop="taskLagReasons">
        <el-input v-model="dataForm.taskLagReasons" placeholder="滞后原因"></el-input>
      </el-form-item>
      <el-form-item label="关联指标编号" prop="taskAssociatedIndicatorsId">
        <el-input v-model="dataForm.taskAssociatedIndicatorsId" placeholder="关联指标编号"></el-input>
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
        tmTid: 0,
        taskId: '',
        taskContent: '',
        taskStartDate: '',
        taskScheduleCompletionDate: '',
        taskScheduleDays: '',
        taskActualCompletionDate: '',
        taskActualDays: '',
        taskAssociatedPlanId: '',
        taskSchedule: '',
        taskPrincipal: '',
        taskExecutor: '',
        taskAuditor: '',
        taskCurrentState: '',
        taskIsCompleted: '',
        taskIsOverdue: '',
        taskIsOnTime: '',
        taskEarlyCompletionDays: '',
        taskLagDays: '',
        taskLagReasons: '',
        taskAssociatedIndicatorsId: ''
      },
      dataRule: {
        taskId: [
          { required: true, message: '任务ID不能为空', trigger: 'blur' }
        ],
        taskContent: [
          { required: true, message: '任务内容不能为空', trigger: 'blur' }
        ],
        taskStartDate: [
          { required: true, message: '开始日期不能为空', trigger: 'blur' }
        ],
        taskScheduleCompletionDate: [
          { required: true, message: '计划完成日期不能为空', trigger: 'blur' }
        ],
        taskScheduleDays: [
          { required: true, message: '计划天数不能为空', trigger: 'blur' }
        ],
        taskActualCompletionDate: [
          { required: true, message: '实际完成日期不能为空', trigger: 'blur' }
        ],
        taskActualDays: [
          { required: true, message: '实际天数不能为空', trigger: 'blur' }
        ],
        taskAssociatedPlanId: [
          { required: true, message: '相关计划编号不能为空', trigger: 'blur' }
        ],
        taskSchedule: [
          { required: true, message: '进度不能为空', trigger: 'blur' }
        ],
        taskPrincipal: [
          { required: true, message: '负责人不能为空', trigger: 'blur' }
        ],
        taskExecutor: [
          { required: true, message: '执行人不能为空', trigger: 'blur' }
        ],
        taskAuditor: [
          { required: true, message: '审核人不能为空', trigger: 'blur' }
        ],
        taskCurrentState: [
          { required: true, message: '当前状态不能为空', trigger: 'blur' }
        ],
        taskIsCompleted: [
          { required: true, message: '是否完成不能为空', trigger: 'blur' }
        ],
        taskIsOverdue: [
          { required: true, message: '是否超期不能为空', trigger: 'blur' }
        ],
        taskIsOnTime: [
          { required: true, message: '按时完工不能为空', trigger: 'blur' }
        ],
        taskEarlyCompletionDays: [
          { required: true, message: '提前完工不能为空', trigger: 'blur' }
        ],
        taskLagDays: [
          { required: true, message: '滞后天数不能为空', trigger: 'blur' }
        ],
        taskLagReasons: [
          { required: true, message: '滞后原因不能为空', trigger: 'blur' }
        ],
        taskAssociatedIndicatorsId: [
          { required: true, message: '关联指标编号不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.tmTid = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.tmTid) {
          this.$http({
            url: this.$http.adornUrl(`/taskmanagement/task/info/${this.dataForm.tmTid}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.taskId = data.taskManagementTask.taskId
              this.dataForm.taskName = data.taskManagementTask.taskName
              this.dataForm.taskContent = data.taskManagementTask.taskContent
              this.dataForm.taskStartDate = data.taskManagementTask.taskStartDate
              this.dataForm.taskScheduleCompletionDate = data.taskManagementTask.taskScheduleCompletionDate
              this.dataForm.taskScheduleDays = data.taskManagementTask.taskScheduleDays
              this.dataForm.taskActualCompletionDate = data.taskManagementTask.taskActualCompletionDate
              this.dataForm.taskActualDays = data.taskManagementTask.taskActualDays
              this.dataForm.taskAssociatedPlanId = data.taskManagementTask.taskAssociatedPlanId
              this.dataForm.taskSchedule = data.taskManagementTask.taskSchedule
              this.dataForm.taskPrincipal = data.taskManagementTask.taskPrincipal
              this.dataForm.taskExecutor = data.taskManagementTask.taskExecutor
              this.dataForm.taskAuditor = data.taskManagementTask.taskAuditor
              this.dataForm.taskCurrentState = data.taskManagementTask.taskCurrentState
              this.dataForm.taskIsCompleted = data.taskManagementTask.taskIsCompleted
              this.dataForm.taskIsOverdue = data.taskManagementTask.taskIsOverdue
              this.dataForm.taskIsOnTime = data.taskManagementTask.taskIsOnTime
              this.dataForm.taskEarlyCompletionDays = data.taskManagementTask.taskEarlyCompletionDays
              this.dataForm.taskLagDays = data.taskManagementTask.taskLagDays
              this.dataForm.taskLagReasons = data.taskManagementTask.taskLagReasons
              this.dataForm.taskAssociatedIndicatorsId = data.taskManagementTask.taskAssociatedIndicatorsId
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
            url: this.$http.adornUrl(`/taskmanagement/task/${!this.dataForm.tmTid ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'tmTid': this.dataForm.tmTid || undefined,
              'taskId': this.dataForm.taskId,
              'taskName': this.dataForm.taskName,
              'taskContent': this.dataForm.taskContent,
              'taskStartDate': this.dataForm.taskStartDate,
              'taskScheduleCompletionDate': this.dataForm.taskScheduleCompletionDate,
              'taskScheduleDays': this.dataForm.taskScheduleDays,
              'taskActualCompletionDate': this.dataForm.taskActualCompletionDate,
              'taskActualDays': this.dataForm.taskActualDays,
              'taskAssociatedPlanId': this.dataForm.taskAssociatedPlanId,
              'taskSchedule': this.dataForm.taskSchedule,
              'taskPrincipal': this.dataForm.taskPrincipal,
              'taskExecutor': this.dataForm.taskExecutor,
              'taskAuditor': this.dataForm.taskAuditor,
              'taskCurrentState': this.dataForm.taskCurrentState,
              'taskIsCompleted': this.dataForm.taskIsCompleted,
              'taskIsOverdue': this.dataForm.taskIsOverdue,
              'taskIsOnTime': this.dataForm.taskIsOnTime,
              'taskEarlyCompletionDays': this.dataForm.taskEarlyCompletionDays,
              'taskLagDays': this.dataForm.taskLagDays,
              'taskLagReasons': this.dataForm.taskLagReasons,
              'taskAssociatedIndicatorsId': this.dataForm.taskAssociatedIndicatorsId
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
