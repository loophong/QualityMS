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
      <el-form-item label="开始日期" prop="taskStartDate">
        <el-date-picker v-model="dataForm.taskStartDate" disabled type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="相关计划编号" prop="taskAssociatedPlanId">
        <el-input v-model="dataForm.taskAssociatedPlanId" readonly placeholder="相关计划编号"></el-input>
      </el-form-item>

      <el-table-column prop="taskPrincipal" header-align="center" align="center" label="负责人">
        <template slot-scope="scope">
          {{ getUserameByUserId(scope.row.taskPrincipal) }}
        </template>
      </el-table-column>

      <el-form-item label="关联指标" prop="planAssociatedIndicatorsId">
        <el-select v-model="dataForm.taskAssociatedIndicatorsId" disabled placeholder="请选择">
          <el-option v-for="item in indicatorOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="任务送审时间" prop="taskSubmissionTime">
        <el-input v-model="dataForm.taskSubmissionTime" readonly placeholder="任务送审时间"></el-input>
      </el-form-item>

      <el-table-column prop="submitter" header-align="center" align="center" label="送审人">
        <template slot-scope="scope">
          {{ getUserameByUserId(scope.row.submitter) }}
        </template>
      </el-table-column>

      <!--  如果审批类型为FINISH，则显示一下几个form-item    -->
      <p v-if="dataForm.approvalType === 'FINISH'">
        <el-form-item label="现状">
          <el-input v-model="dataForm.currentStatus" readonly type="textarea" :rows="3"
                    placeholder="请输入现状"></el-input>
        </el-form-item>
        <el-form-item label="目标">
          <el-input v-model="dataForm.objectiveGoal" readonly type="textarea" :rows="3"
                    placeholder="请输入任务目标"></el-input>
        </el-form-item>
        <el-form-item label="主要措施">
          <el-input v-model="dataForm.keyMeasuresActions" readonly type="textarea" :rows="3"
                    placeholder="请输入主要措施"></el-input>
        </el-form-item>
        <el-form-item label="实施人">
          <el-select v-model="dataForm.implementerResponsiblePerson" readonly multiple filterable placeholder="实施人">
            <el-option-group v-for="group in options" :key="group.label" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                         :value="item.value">
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="开展时间">
          <el-date-picker v-model="dataForm.implementationStartTime" readonly value-format="yyyy-MM-dd"
                          type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item v-if="dataForm.files && dataForm.files.length > 0" label="附件">
          <el-row v-for="file in dataForm.files" :key="file.name" style="margin-bottom: 4px">
            <el-col :span="12">
              {{ file.name }}
            </el-col>
            <el-col :span="12">
              <el-button type="primary" @click="downloadFile(file.url)">下载</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </p>


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
    <span slot="footer" class="dialog-footer" style="display: flex; justify-content: center;">
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
        files: [],
        remarks: '',
        implementationStartTime: [new Date(), new Date()],
        //员工列表
        options: [],
        // 指标列表
        indicatorOptions: [],
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
          {required: true, message: '审批状态不能为空', trigger: 'blur'},
          {validator: this.validateApprovalStatus, trigger: 'change'},
        ],
      }
    }
  },


  async created() {
    // 获取分组后的员工数据
    // const response = await fetch('/taskmanagement/user/getEmployeesGroupedByDepartment'); // 假设这是你的 API 路由
    // const data = await response.json();

    // 获取分组后的员工数据
    await this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getName`),
      method: 'get',
    }).then(({data}) => {
      this.options = data;
      console.log(data);
    })


    this.$http({
      url: this.$http.adornUrl(`/indicator/indicatordatadictionary/getIndicatorsList`),
      method: 'get',
    }).then(response => {
      const opt = response.data.map(item => ({
        value: item.value,
        label: item.label
      }));
      console.log(opt);
      this.indicatorOptions = opt;
    })
  },


  methods: {
    init(id) {
      console.log(12344444444)
      this.dataForm.approvalId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.approvalId) {
          this.$http({
            url: this.$http.adornUrl(`/taskmanagement/approval/info/${this.dataForm.approvalId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
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
              this.dataForm.approvalContent = data.taskManagementApprovalTable.approvalContent
              this.dataForm.approvalType = data.taskManagementApprovalTable.approvalType
              this.dataForm.currentStatus = data.taskManagementApprovalTable.currentStatus
              this.dataForm.objectiveGoal = data.taskManagementApprovalTable.objectiveGoal
              this.dataForm.keyMeasuresActions = data.taskManagementApprovalTable.keyMeasuresActions


              this.dataForm.implementerResponsiblePerson = data.taskManagementApprovalTable.implementerResponsiblePerson
                .split(',');

              console.log(this.dataForm.implementerResponsiblePerson);
              this.dataForm.implementationStartTime[0] = data.taskManagementApprovalTable.implementationStartTime
              this.dataForm.implementationStartTime[1] = data.taskManagementApprovalTable.implementationEndTime
              this.dataForm.files = data.files
            }
          })
        }
      })
      console.log(12344444444)
    },
    validateApprovalStatus(rule, value, callback) {
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

          let data = {
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
            'remarks': this.dataForm.remarks,
            'approvalType': this.dataForm.approvalType
          };

          if (this.dataForm.approvalType === "FINISH") {
            this.$http({
              url: this.$http.adornUrl(`/taskmanagement/approval/${!this.dataForm.approvalId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData(data)
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
          } else {

            this.$http({
              url: this.$http.adornUrl(`/taskmanagement/approval/approval`),
              method: 'post',
              data: this.$http.adornData(data)
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
        }
      })
    },
    // 下载附件
    downloadFile(url) {
      // 获取当前的 token，假设它存储在 localStorage 中
      const token = this.$cookie.get('token');
      if (token) {
        console.log('Token found:', token);
      } else {
        console.error('Token not found!');
      }

      // 将 token 作为参数添加到 URL
      // const imageUrlWithToken = `${url}?token=${token}`;

      // 使用window.open方法打开下载链接
      // window.open(imageUrlWithToken);

      // 拼接带有 token 的请求地址
      const fileUrl = `${this.$http.adornUrl(`/generator/issuetable/${url}`)}?token=${token}`;
      window.open(fileUrl);
    },
  }
}
</script>
