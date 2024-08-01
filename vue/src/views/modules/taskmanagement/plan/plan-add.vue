<template>
  <el-dialog :title="!dataForm.planId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="80px">
      <el-form-item label="任务内容" prop="taskContent">
        <el-input v-model="dataForm.taskContent" placeholder="任务内容"></el-input>
      </el-form-item>
      <el-form-item label="开始日期" prop="startDate">
        <el-date-picker v-model="dataForm.startDate" type="date" placeholder="选择开始日期"
          style="width: 100%;"></el-date-picker>
      </el-form-item>
      <el-form-item label="计划完成日期" prop="plannedFinishDate">
        <el-date-picker v-model="dataForm.plannedFinishDate" type="date" placeholder="选择计划完成日期"
          style="width: 100%;"></el-date-picker>
      </el-form-item>
      <el-form-item label="计划天数" prop="scheduleDays">
        <el-input v-model="dataForm.scheduleDays" placeholder="计划天数"></el-input>
      </el-form-item>
      <el-form-item label="实际完成日期" prop="actualFinishingDate">
        <el-date-picker v-model="dataForm.actualFinishingDate" type="date" placeholder="选择实际完成日期"
          style="width: 100%;"></el-date-picker>
      </el-form-item>
      <el-form-item label="实际天数" prop="actualDays">
        <el-input v-model="dataForm.actualDays" placeholder="实际天数"></el-input>
      </el-form-item>
      <el-form-item label="任务派发" prop="tasksAssignment">
        <el-input v-model="dataForm.tasksAssignment" placeholder="任务派发"></el-input>
      </el-form-item>
      <el-form-item label="关联任务编号" prop="associatedTaskNumber">
        <el-input v-model="dataForm.associatedTaskNumber" placeholder="关联任务编号"></el-input>
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
        <el-select v-model="dataForm.currentState" placeholder="请选择当前状态">
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="是否完成" prop="whetherComplete">
        <el-input v-model="dataForm.whetherComplete" placeholder="是否完成"></el-input>
      </el-form-item> -->
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
        planId: 0,
        taskContent: '',
        startDate: '',
        plannedFinishDate: '',
        scheduleDays: '',
        actualFinishingDate: '',
        actualDays: '',
        tasksAssignment: '',
        associatedTaskNumber: '',
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
        tasksAssignment: [
          { required: true, message: '任务派发不能为空', trigger: 'blur' }
        ],
        associatedTaskNumber: [
          { required: true, message: '关联任务编号不能为空', trigger: 'blur' }
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
      },
      statusOptions: [
        { value: '未开始', label: '未开始' },
        { value: '进行中', label: '进行中' },
        { value: '已完成', label: '已完成' },
      ]
    }
  },
  methods: {
    init(id) {
      this.dataForm.planId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.planId) {
          this.$http({
            url: this.$http.adornUrl(`/taskmanagement/plan/info/${this.dataForm.planId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.taskContent = data.taskDataPlan.taskContent
              this.dataForm.startDate = data.taskDataPlan.startDate
              this.dataForm.plannedFinishDate = data.taskDataPlan.plannedFinishDate
              this.dataForm.scheduleDays = data.taskDataPlan.scheduleDays
              this.dataForm.actualFinishingDate = data.taskDataPlan.actualFinishingDate
              this.dataForm.actualDays = data.taskDataPlan.actualDays
              this.dataForm.tasksAssignment = data.taskDataPlan.tasksAssignment
              this.dataForm.associatedTaskNumber = data.taskDataPlan.associatedTaskNumber
              this.dataForm.schedule = data.taskDataPlan.schedule
              this.dataForm.principal = data.taskDataPlan.principal
              this.dataForm.executor = data.taskDataPlan.executor
              this.dataForm.auditor = data.taskDataPlan.auditor
              this.dataForm.currentState = data.taskDataPlan.currentState
              this.dataForm.whetherComplete = data.taskDataPlan.whetherComplete
              this.dataForm.whetherOverdue = data.taskDataPlan.whetherOverdue
              this.dataForm.finishOnTime = data.taskDataPlan.finishOnTime
              this.dataForm.daysAheadOfSchedule = data.taskDataPlan.daysAheadOfSchedule
              this.dataForm.lagDays = data.taskDataPlan.lagDays
              this.dataForm.delayReasons = data.taskDataPlan.delayReasons
              this.dataForm.associatedIndexNumber = data.taskDataPlan.associatedIndexNumber
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
            url: this.$http.adornUrl(`/taskmanagement/plan/${!this.dataForm.planId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'planId': this.dataForm.planId || undefined,
              'taskContent': this.dataForm.taskContent,
              'startDate': this.dataForm.startDate,
              'plannedFinishDate': this.dataForm.plannedFinishDate,
              'scheduleDays': this.dataForm.scheduleDays,
              'actualFinishingDate': this.dataForm.actualFinishingDate,
              'actualDays': this.dataForm.actualDays,
              'tasksAssignment': this.dataForm.tasksAssignment,
              'associatedTaskNumber': this.dataForm.associatedTaskNumber,
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
