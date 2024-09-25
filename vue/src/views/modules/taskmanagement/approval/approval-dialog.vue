<template>
    <el-dialog title="任务审批" :close-on-click-modal="false" :visible.sync="visible">
        <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
            label-width="120px">
            <el-form-item label="任务编号" prop="taskId">
                <el-input v-model="dataForm.taskId" readonly placeholder="任务编号"></el-input>
            </el-form-item>
            <el-form-item label="任务名" prop="taskName">
                <el-input v-model="dataForm.taskName" readonly placeholder="任务名"></el-input>
            </el-form-item>
            <el-form-item label="任务内容" prop="taskContent">
                <el-input v-model="dataForm.taskContent" readonly placeholder="任务内容"></el-input>
            </el-form-item>
            <!-- <el-form-item label="开始日期" prop="taskStartDate">
                <el-input v-model="dataForm.taskStartDate" readonly placeholder="开始日期"></el-input>
            </el-form-item> -->
            <el-form-item label="开始日期" prop="taskStartDate">
                <el-date-picker v-model="dataForm.taskStartDate" disabled type="date" placeholder="选择日期"
                    value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="相关计划编号" prop="taskAssociatedPlanId">
                <el-input v-model="dataForm.taskAssociatedPlanId" readonly placeholder="相关计划编号"></el-input>
            </el-form-item>
            <!-- <el-form-item label="负责人" prop="taskPrincipal">
          <el-input v-model="dataForm.taskPrincipal" placeholder="负责人"></el-input>
        </el-form-item> -->
            <el-table-column prop="taskPrincipal" header-align="center" align="center" label="负责人">
                <template slot-scope="scope">
                    {{ getUserameByUserId(scope.row.taskPrincipal) }}
                </template>
            </el-table-column>
            <!-- <el-form-item label="执行人" prop="taskExecutor">
          <el-input v-model="dataForm.taskExecutor" placeholder="执行人"></el-input>
        </el-form-item> -->
            <!-- <el-table-column prop="taskExecutor" header-align="center" align="center" label="执行人" width="110">
          <template slot-scope="scope">
            <span v-for="(executorId, index) in scope.row.taskExecutor" :key="executorId">
              {{ getUserameByUserId(executorId) }}
              <span v-if="index !== scope.row.taskExecutor.length - 1">, </span>
            </span>
          </template>
        </el-table-column> -->

            <el-form-item label="关联指标编号" prop="taskAssociatedIndicatorsId">
                <el-input v-model="dataForm.taskAssociatedIndicatorsId" readonly placeholder="关联指标编号"></el-input>
            </el-form-item>
            <el-form-item label="任务送审时间" prop="taskSubmissionTime">
                <el-input v-model="dataForm.taskSubmissionTime" readonly placeholder="任务送审时间"></el-input>
            </el-form-item>
            <!-- <el-form-item label="审批结束时间" prop="approvalEndTime">
          <el-input v-model="dataForm.approvalEndTime" placeholder="审批结束时间"></el-input>
        </el-form-item> -->
            <!-- <el-form-item label="审批人" prop="approver">
          <el-input v-model="dataForm.approver" placeholder="审批人"></el-input>
        </el-form-item> -->
            <!-- <el-form-item label="送审人" prop="submitter">
          <el-input v-model="dataForm.submitter" placeholder="送审人"></el-input>
        </el-form-item> -->
            <el-table-column prop="submitter" header-align="center" align="center" label="送审人">
                <template slot-scope="scope">
                    {{ getUserameByUserId(scope.row.submitter) }}
                </template>
            </el-table-column>
            <!-- <el-form-item label="审批状态" prop="approvalStatus">
          <el-input v-model="dataForm.approvalStatus" placeholder="审批状态"></el-input>
        </el-form-item> -->
            <el-form-item label="审批状态" prop="approvalStatus">
                <!-- <el-input v-model="dataForm.approvalStatus" placeholder="审批状态"></el-input> -->
                <el-radio v-model="dataForm.approvalStatus" label="APPROVED">通过</el-radio>
                <el-radio v-model="dataForm.approvalStatus" label="REJECTED">拒绝</el-radio>
            </el-form-item>


            <el-form-item label="审批意见" prop="approvalComments">
                <el-input v-model="dataForm.approvalComments" placeholder="审批意见"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="remarks">
                <el-input v-model="dataForm.remarks" placeholder="备注"></el-input>
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
                approvalId: 0,
                taskId: '',
                taskName: '',
                taskContent: '',
                taskStartDate: '',
                taskAssociatedPlanId: '',
                taskPrincipal: '',
                taskExecutor: '',
                taskAssociatedIndicatorsId: '',
                taskSubmissionTime: '',
                approvalEndTime: '',
                approver: '',
                submitter: '',
                approvalStatus: '',
                approvalComments: '',
                remarks: ''
            },
            dataRule: {
                // taskId: [
                //     { required: true, message: '任务ID不能为空', trigger: 'blur' }
                // ],
                // taskName: [
                //     { required: true, message: '任务名不能为空', trigger: 'blur' }
                // ],
                // taskContent: [
                //     { required: true, message: '任务内容不能为空', trigger: 'blur' }
                // ],
                // taskStartDate: [
                //     { required: true, message: '开始日期不能为空', trigger: 'blur' }
                // ],
                // taskAssociatedPlanId: [
                //     { required: true, message: '相关计划编号不能为空', trigger: 'blur' }
                // ],
                // taskPrincipal: [
                //     { required: true, message: '负责人不能为空', trigger: 'blur' }
                // ],
                // taskExecutor: [
                //     { required: true, message: '执行人不能为空', trigger: 'blur' }
                // ],
                // taskAssociatedIndicatorsId: [
                //     { required: true, message: '关联指标编号不能为空', trigger: 'blur' }
                // ],
                // taskSubmissionTime: [
                //     { required: true, message: '任务送审时间不能为空', trigger: 'blur' }
                // ],
                // approvalEndTime: [
                //     { required: true, message: '审批结束时间不能为空', trigger: 'blur' }
                // ],
                // approver: [
                //     { required: true, message: '审批人不能为空', trigger: 'blur' }
                // ],
                // submitter: [
                //     { required: true, message: '送审人不能为空', trigger: 'blur' }
                // ],
                // approvalStatus: [
                //     { required: true, message: '审批状态不能为空', trigger: 'blur' }
                // ],
                //   approvalComments: [
                //     { required: true, message: '审批意见不能为空', trigger: 'blur' }
                //   ],
                // remarks: [
                //   { required: true, message: '备注不能为空', trigger: 'blur' }
                // ]

                approvalStatus: [
                    { required: true, message: '审批状态不能为空', trigger: 'blur' },
                    { validator: this.validateApprovalStatus, trigger: 'change' },
                ],
            }
        }
    },
    methods: {
        init(id) {
            this.dataForm.approvalId = id || 0
            this.visible = true
            this.$nextTick(() => {
                this.$refs['dataForm'].resetFields()
                if (this.dataForm.approvalId) {
                    this.$http({
                        url: this.$http.adornUrl(`/taskmanagement/approval/info/${this.dataForm.approvalId}`),
                        method: 'get',
                        params: this.$http.adornParams()
                    }).then(({ data }) => {
                        if (data && data.code === 0) {
                            this.dataForm.taskId = data.taskManagementApprovalTable.taskId
                            this.dataForm.taskName = data.taskManagementApprovalTable.taskName
                            this.dataForm.taskContent = data.taskManagementApprovalTable.taskContent
                            this.dataForm.taskStartDate = data.taskManagementApprovalTable.taskStartDate
                            this.dataForm.taskAssociatedPlanId = data.taskManagementApprovalTable.taskAssociatedPlanId
                            this.dataForm.taskPrincipal = data.taskManagementApprovalTable.taskPrincipal
                            this.dataForm.taskExecutor = data.taskManagementApprovalTable.taskExecutor
                            this.dataForm.taskAssociatedIndicatorsId = data.taskManagementApprovalTable.taskAssociatedIndicatorsId
                            this.dataForm.taskSubmissionTime = data.taskManagementApprovalTable.taskSubmissionTime
                            this.dataForm.approvalEndTime = data.taskManagementApprovalTable.approvalEndTime
                            this.dataForm.approver = data.taskManagementApprovalTable.approver
                            this.dataForm.submitter = data.taskManagementApprovalTable.submitter
                            this.dataForm.approvalStatus = data.taskManagementApprovalTable.approvalStatus
                            this.dataForm.approvalComments = data.taskManagementApprovalTable.approvalComments
                            this.dataForm.remarks = data.taskManagementApprovalTable.remarks
                        }
                    })
                }
            })
        },
        validateApprovalStatus(rule,value, callback) {
            if (value === "APPROVED" || value === "REJECTED") {
                callback();
            } else {
                callback(new Error('请选择审批是否通过'));
            }
        },
        // 表单提交
        dataFormSubmit() {
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    this.$http({
                        url: this.$http.adornUrl(`/taskmanagement/approval/${!this.dataForm.approvalId ? 'save' : 'update'}`),
                        method: 'post',
                        data: this.$http.adornData({
                            'approvalId': this.dataForm.approvalId || undefined,
                            'taskId': this.dataForm.taskId,
                            'taskName': this.dataForm.taskName,
                            'taskContent': this.dataForm.taskContent,
                            'taskStartDate': this.dataForm.taskStartDate,
                            'taskAssociatedPlanId': this.dataForm.taskAssociatedPlanId,
                            'taskPrincipal': this.dataForm.taskPrincipal,
                            'taskExecutor': this.dataForm.taskExecutor,
                            'taskAssociatedIndicatorsId': this.dataForm.taskAssociatedIndicatorsId,
                            'taskSubmissionTime': this.dataForm.taskSubmissionTime,
                            'approvalEndTime': this.dataForm.approvalEndTime,
                            'approver': this.dataForm.approver,
                            'submitter': this.dataForm.submitter,
                            'approvalStatus': this.dataForm.approvalStatus,
                            'approvalComments': this.dataForm.approvalComments,
                            'remarks': this.dataForm.remarks
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
  