<template>
  <el-dialog
    :title="!dataForm.tmPid ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="计划ID" prop="planId">
      <el-input v-model="dataForm.planId" placeholder="计划ID"></el-input>
    </el-form-item>
    
    <el-form-item label="任务内容" prop="planContent">
      <el-input v-model="dataForm.planContent" placeholder="任务内容"></el-input>
    </el-form-item>
    <el-form-item label="开始日期" prop="planStartDate">
      <el-input v-model="dataForm.planStartDate" placeholder="开始日期"></el-input>
    </el-form-item>
    <el-form-item label="计划完成日期" prop="planScheduleCompletionDate">
      <el-input v-model="dataForm.planScheduleCompletionDate" placeholder="计划完成日期"></el-input>
    </el-form-item>
    <el-form-item label="计划天数" prop="planScheduleDays">
      <el-input v-model="dataForm.planScheduleDays" placeholder="计划天数"></el-input>
    </el-form-item>
    <el-form-item label="实际完成日期" prop="planActualCompletionDate">
      <el-input v-model="dataForm.planActualCompletionDate" placeholder="实际完成日期"></el-input>
    </el-form-item>
    <el-form-item label="实际天数" prop="planActualDays">
      <el-input v-model="dataForm.planActualDays" placeholder="实际天数"></el-input>
    </el-form-item>
    <el-form-item label="任务派发" prop="planTasksAssignment">
      <el-input v-model="dataForm.planTasksAssignment" placeholder="任务派发"></el-input>
    </el-form-item>
    <el-form-item label="关联任务编号" prop="planAssociatedTasksId">
      <el-input v-model="dataForm.planAssociatedTasksId" placeholder="关联任务编号"></el-input>
    </el-form-item>
    <el-form-item label="进度" prop="planSchedule">
      <el-input v-model="dataForm.planSchedule" placeholder="进度"></el-input>
    </el-form-item>
    <el-form-item label="负责人" prop="planPrincipal">
      <el-input v-model="dataForm.planPrincipal" placeholder="负责人"></el-input>
    </el-form-item>
    <el-form-item label="执行人" prop="planExecutor">
      <el-input v-model="dataForm.planExecutor" placeholder="执行人"></el-input>
    </el-form-item>
    <el-form-item label="审核人" prop="planAuditor">
      <el-input v-model="dataForm.planAuditor" placeholder="审核人"></el-input>
    </el-form-item>
    <el-form-item label="当前状态" prop="planCurrentState">
      <el-input v-model="dataForm.planCurrentState" placeholder="当前状态"></el-input>
    </el-form-item>
    <el-form-item label="是否完成" prop="planIsCompleted">
      <el-input v-model="dataForm.planIsCompleted" placeholder="是否完成"></el-input>
    </el-form-item>
    <el-form-item label="是否超期" prop="planIsOverdue">
      <el-input v-model="dataForm.planIsOverdue" placeholder="是否超期"></el-input>
    </el-form-item>
    <el-form-item label="按时完工" prop="planIsOnTime">
      <el-input v-model="dataForm.planIsOnTime" placeholder="按时完工"></el-input>
    </el-form-item>
    <el-form-item label="提前完工" prop="planEarlyCompletionDays">
      <el-input v-model="dataForm.planEarlyCompletionDays" placeholder="提前完工"></el-input>
    </el-form-item>
    <el-form-item label="滞后天数" prop="planLagDays">
      <el-input v-model="dataForm.planLagDays" placeholder="滞后天数"></el-input>
    </el-form-item>
    <el-form-item label="滞后原因" prop="planLagReasons">
      <el-input v-model="dataForm.planLagReasons" placeholder="滞后原因"></el-input>
    </el-form-item>
    <el-form-item label="关联指标编号" prop="planAssociatedIndicatorsId">
      <el-input v-model="dataForm.planAssociatedIndicatorsId" placeholder="关联指标编号"></el-input>
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
    data () {
      return {
        visible: false,
        dataForm: {
          tmPid: 0,
          planId: '',
          planContent: '',
          planStartDate: '',
          planScheduleCompletionDate: '',
          planScheduleDays: '',
          planActualCompletionDate: '',
          planActualDays: '',
          planTasksAssignment: '',
          planAssociatedTasksId: '',
          planSchedule: '',
          planPrincipal: '',
          planExecutor: '',
          planAuditor: '',
          planCurrentState: '',
          planIsCompleted: '',
          planIsOverdue: '',
          planIsOnTime: '',
          planEarlyCompletionDays: '',
          planLagDays: '',
          planLagReasons: '',
          planAssociatedIndicatorsId: ''
        },
        dataRule: {
          planId: [
            { required: true, message: '计划ID不能为空', trigger: 'blur' }
          ],
          planContent: [
            { required: true, message: '任务内容不能为空', trigger: 'blur' }
          ],
          planStartDate: [
            { required: true, message: '开始日期不能为空', trigger: 'blur' }
          ],
          planScheduleCompletionDate: [
            { required: true, message: '计划完成日期不能为空', trigger: 'blur' }
          ],
          planScheduleDays: [
            { required: true, message: '计划天数不能为空', trigger: 'blur' }
          ],
          planActualCompletionDate: [
            { required: true, message: '实际完成日期不能为空', trigger: 'blur' }
          ],
          planActualDays: [
            { required: true, message: '实际天数不能为空', trigger: 'blur' }
          ],
          planTasksAssignment: [
            { required: true, message: '任务派发不能为空', trigger: 'blur' }
          ],
          planAssociatedTasksId: [
            { required: true, message: '关联任务编号不能为空', trigger: 'blur' }
          ],
          planSchedule: [
            { required: true, message: '进度不能为空', trigger: 'blur' }
          ],
          planPrincipal: [
            { required: true, message: '负责人不能为空', trigger: 'blur' }
          ],
          planExecutor: [
            { required: true, message: '执行人不能为空', trigger: 'blur' }
          ],
          planAuditor: [
            { required: true, message: '审核人不能为空', trigger: 'blur' }
          ],
          planCurrentState: [
            { required: true, message: '当前状态不能为空', trigger: 'blur' }
          ],
          planIsCompleted: [
            { required: true, message: '是否完成不能为空', trigger: 'blur' }
          ],
          planIsOverdue: [
            { required: true, message: '是否超期不能为空', trigger: 'blur' }
          ],
          planIsOnTime: [
            { required: true, message: '按时完工不能为空', trigger: 'blur' }
          ],
          planEarlyCompletionDays: [
            { required: true, message: '提前完工不能为空', trigger: 'blur' }
          ],
          planLagDays: [
            { required: true, message: '滞后天数不能为空', trigger: 'blur' }
          ],
          planLagReasons: [
            { required: true, message: '滞后原因不能为空', trigger: 'blur' }
          ],
          planAssociatedIndicatorsId: [
            { required: true, message: '关联指标编号不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {

        this.dataForm.tmPid = id || 0
        
        
        
        
        
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.tmPid) {
            this.$http({
              url: this.$http.adornUrl(`/taskmanagement/plan/info/${this.dataForm.tmPid}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.planId = data.taskManagementPlan.planId
                this.dataForm.planContent = data.taskManagementPlan.planContent
                this.dataForm.planStartDate = data.taskManagementPlan.planStartDate
                this.dataForm.planScheduleCompletionDate = data.taskManagementPlan.planScheduleCompletionDate
                this.dataForm.planScheduleDays = data.taskManagementPlan.planScheduleDays
                this.dataForm.planActualCompletionDate = data.taskManagementPlan.planActualCompletionDate
                this.dataForm.planActualDays = data.taskManagementPlan.planActualDays
                this.dataForm.planTasksAssignment = data.taskManagementPlan.planTasksAssignment
                this.dataForm.planAssociatedTasksId = data.taskManagementPlan.planAssociatedTasksId
                this.dataForm.planSchedule = data.taskManagementPlan.planSchedule
                this.dataForm.planPrincipal = data.taskManagementPlan.planPrincipal
                this.dataForm.planExecutor = data.taskManagementPlan.planExecutor
                this.dataForm.planAuditor = data.taskManagementPlan.planAuditor
                this.dataForm.planCurrentState = data.taskManagementPlan.planCurrentState
                this.dataForm.planIsCompleted = data.taskManagementPlan.planIsCompleted
                this.dataForm.planIsOverdue = data.taskManagementPlan.planIsOverdue
                this.dataForm.planIsOnTime = data.taskManagementPlan.planIsOnTime
                this.dataForm.planEarlyCompletionDays = data.taskManagementPlan.planEarlyCompletionDays
                this.dataForm.planLagDays = data.taskManagementPlan.planLagDays
                this.dataForm.planLagReasons = data.taskManagementPlan.planLagReasons
                this.dataForm.planAssociatedIndicatorsId = data.taskManagementPlan.planAssociatedIndicatorsId
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
              url: this.$http.adornUrl(`/taskmanagement/plan/${!this.dataForm.tmPid ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'tmPid': this.dataForm.tmPid || undefined,
                'planId': this.dataForm.planId,
                'planContent': this.dataForm.planContent,
                'planStartDate': this.dataForm.planStartDate,
                'planScheduleCompletionDate': this.dataForm.planScheduleCompletionDate,
                'planScheduleDays': this.dataForm.planScheduleDays,
                'planActualCompletionDate': this.dataForm.planActualCompletionDate,
                'planActualDays': this.dataForm.planActualDays,
                'planTasksAssignment': this.dataForm.planTasksAssignment,
                'planAssociatedTasksId': this.dataForm.planAssociatedTasksId,
                'planSchedule': this.dataForm.planSchedule,
                'planPrincipal': this.dataForm.planPrincipal,
                'planExecutor': this.dataForm.planExecutor,
                'planAuditor': this.dataForm.planAuditor,
                'planCurrentState': this.dataForm.planCurrentState,
                'planIsCompleted': this.dataForm.planIsCompleted,
                'planIsOverdue': this.dataForm.planIsOverdue,
                'planIsOnTime': this.dataForm.planIsOnTime,
                'planEarlyCompletionDays': this.dataForm.planEarlyCompletionDays,
                'planLagDays': this.dataForm.planLagDays,
                'planLagReasons': this.dataForm.planLagReasons,
                'planAssociatedIndicatorsId': this.dataForm.planAssociatedIndicatorsId
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
