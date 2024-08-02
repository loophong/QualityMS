<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="公司编号" prop="companyId">
      <el-input v-model="dataForm.companyId" placeholder="公司编号"></el-input>
    </el-form-item>
    <el-form-item label="级别" prop="level">
      <el-input v-model="dataForm.level" placeholder="级别"></el-input>
    </el-form-item>
    <el-form-item label="检定周期" prop="calibrationCycle">
      <el-input v-model="dataForm.calibrationCycle" placeholder="检定周期"></el-input>
    </el-form-item>
    <el-form-item label="检定状态" prop="calibrationStatus">
      <el-input v-model="dataForm.calibrationStatus" placeholder="检定状态"></el-input>
    </el-form-item>
    <el-form-item label="使用状态" prop="usageStatus">
      <el-input v-model="dataForm.usageStatus" placeholder="使用状态"></el-input>
    </el-form-item>
    <el-form-item label="配置地点" prop="location">
      <el-input v-model="dataForm.location" placeholder="配置地点"></el-input>
    </el-form-item>
    <el-form-item label="责任部门" prop="responsibleDepartment">
      <el-input v-model="dataForm.responsibleDepartment" placeholder="责任部门"></el-input>
    </el-form-item>
    <el-form-item label="使用人" prop="user">
      <el-input v-model="dataForm.user" placeholder="使用人"></el-input>
    </el-form-item>
    <el-form-item label="检校日期" prop="calibrationDate">
      <el-input v-model="dataForm.calibrationDate" placeholder="检校日期"></el-input>
    </el-form-item>
    <el-form-item label="检校有效期" prop="calibrationValidity">
      <el-input v-model="dataForm.calibrationValidity" placeholder="检校有效期"></el-input>
    </el-form-item>
    <el-form-item label="检校单位" prop="calibrationUnit">
      <el-input v-model="dataForm.calibrationUnit" placeholder="检校单位"></el-input>
    </el-form-item>
    <el-form-item label="证书编号" prop="certificateNumber">
      <el-input v-model="dataForm.certificateNumber" placeholder="证书编号"></el-input>
    </el-form-item>
    <el-form-item label="备注" prop="remark">
      <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
    </el-form-item>
    <el-form-item label="数量" prop="quantity">
      <el-input v-model="dataForm.quantity" placeholder="数量"></el-input>
    </el-form-item>

  
    <el-form-item label="创建日期" prop="creatTime">
      <el-input v-model="dataForm.creatTime" placeholder="创建日期"></el-input>
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
          id: 0,
          companyId: '',
          level: '',
          calibrationCycle: '',
          calibrationStatus: '',
          usageStatus: '',
          location: '',
          responsibleDepartment: '',
          user: '',
          calibrationDate: '',
          calibrationValidity: '',
          calibrationUnit: '',
          certificateNumber: '',
          remark: '',
          quantity: '',
          flag: '',
          creatTime: ''
        },
        dataRule: {
          companyId: [
            { required: true, message: '公司编号不能为空', trigger: 'blur' }
          ],
          level: [
            { required: true, message: '级别不能为空', trigger: 'blur' }
          ],
          calibrationCycle: [
            { required: true, message: '检定周期不能为空', trigger: 'blur' }
          ],
          calibrationStatus: [
            { required: true, message: '检定状态不能为空', trigger: 'blur' }
          ],
          usageStatus: [
            { required: true, message: '使用状态不能为空', trigger: 'blur' }
          ],
          location: [
            { required: true, message: '配置地点不能为空', trigger: 'blur' }
          ],
          responsibleDepartment: [
            { required: true, message: '责任部门不能为空', trigger: 'blur' }
          ],
          user: [
            { required: true, message: '使用人不能为空', trigger: 'blur' }
          ],
          calibrationDate: [
            { required: true, message: '检校日期不能为空', trigger: 'blur' }
          ],
          calibrationValidity: [
            { required: true, message: '检校有效期不能为空', trigger: 'blur' }
          ],
          calibrationUnit: [
            { required: true, message: '检校单位不能为空', trigger: 'blur' }
          ],
          certificateNumber: [
            { required: true, message: '证书编号不能为空', trigger: 'blur' }
          ],
          remark: [
            { required: true, message: '备注不能为空', trigger: 'blur' }
          ],
          quantity: [
            { required: true, message: '数量不能为空', trigger: 'blur' }
          ],
          flag: [
            { required: true, message: '逻辑删除字段不能为空', trigger: 'blur' }
          ],
          creatTime: [
            { required: true, message: '创建日期不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/taskmanagement/instrumentledger/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.companyId = data.instrumentLedger.companyId
                this.dataForm.level = data.instrumentLedger.level
                this.dataForm.calibrationCycle = data.instrumentLedger.calibrationCycle
                this.dataForm.calibrationStatus = data.instrumentLedger.calibrationStatus
                this.dataForm.usageStatus = data.instrumentLedger.usageStatus
                this.dataForm.location = data.instrumentLedger.location
                this.dataForm.responsibleDepartment = data.instrumentLedger.responsibleDepartment
                this.dataForm.user = data.instrumentLedger.user
                this.dataForm.calibrationDate = data.instrumentLedger.calibrationDate
                this.dataForm.calibrationValidity = data.instrumentLedger.calibrationValidity
                this.dataForm.calibrationUnit = data.instrumentLedger.calibrationUnit
                this.dataForm.certificateNumber = data.instrumentLedger.certificateNumber
                this.dataForm.remark = data.instrumentLedger.remark
                this.dataForm.quantity = data.instrumentLedger.quantity
                this.dataForm.flag = data.instrumentLedger.flag
                this.dataForm.creatTime = data.instrumentLedger.creatTime
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
              url: this.$http.adornUrl(`/taskmanagement/instrumentledger/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'companyId': this.dataForm.companyId,
                'level': this.dataForm.level,
                'calibrationCycle': this.dataForm.calibrationCycle,
                'calibrationStatus': this.dataForm.calibrationStatus,
                'usageStatus': this.dataForm.usageStatus,
                'location': this.dataForm.location,
                'responsibleDepartment': this.dataForm.responsibleDepartment,
                'user': this.dataForm.user,
                'calibrationDate': this.dataForm.calibrationDate,
                'calibrationValidity': this.dataForm.calibrationValidity,
                'calibrationUnit': this.dataForm.calibrationUnit,
                'certificateNumber': this.dataForm.certificateNumber,
                'remark': this.dataForm.remark,
                'quantity': this.dataForm.quantity,
                'flag': this.dataForm.flag,
                'creatTime': this.dataForm.creatTime
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
