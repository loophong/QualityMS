<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="仓库号" prop="warehouseNumber">
      <el-input v-model="dataForm.warehouseNumber" placeholder="仓库号"></el-input>
    </el-form-item>
    <el-form-item label="量具编号" prop="toolNumber">
      <el-input v-model="dataForm.toolNumber" placeholder="量具编号"></el-input>
    </el-form-item>
    <el-form-item label="量具名称" prop="toolName">
      <el-input v-model="dataForm.toolName" placeholder="量具名称"></el-input>
    </el-form-item>
    <el-form-item label="规格型号" prop="specificationModel">
      <el-input v-model="dataForm.specificationModel" placeholder="规格型号"></el-input>
    </el-form-item>
    <el-form-item label="量程" prop="rangeValue">
      <el-input v-model="dataForm.rangeValue" placeholder="量程"></el-input>
    </el-form-item>
    <el-form-item label="分度值" prop="divisionValue">
      <el-input v-model="dataForm.divisionValue" placeholder="分度值"></el-input>
    </el-form-item>
    <el-form-item label="精度" prop="accuracy">
      <el-input v-model="dataForm.accuracy" placeholder="精度"></el-input>
    </el-form-item>
    <el-form-item label="用途" prop="purpose">
      <el-input v-model="dataForm.purpose" placeholder="用途"></el-input>
    </el-form-item>
    <el-form-item label="供应商" prop="supplier">
      <el-input v-model="dataForm.supplier" placeholder="供应商"></el-input>
    </el-form-item>
    <el-form-item label="出厂编号" prop="factoryNumber">
      <el-input v-model="dataForm.factoryNumber" placeholder="出厂编号"></el-input>
    </el-form-item>
    <el-form-item label="公司编号" prop="companyNumber">
      <el-input v-model="dataForm.companyNumber" placeholder="公司编号"></el-input>
    </el-form-item>
    <el-form-item label="级别" prop="level">
      <el-input v-model="dataForm.level" placeholder="级别"></el-input>
    </el-form-item>
    <el-form-item label="检定周期" prop="verificationCycle">
      <el-input v-model="dataForm.verificationCycle" placeholder="检定周期"></el-input>
    </el-form-item>
    <el-form-item label="检定状态" prop="verificationStatus">
      <el-input v-model="dataForm.verificationStatus" placeholder="检定状态"></el-input>
    </el-form-item>
    <el-form-item label="使用状态" prop="usageStatus">
      <el-input v-model="dataForm.usageStatus" placeholder="使用状态"></el-input>
    </el-form-item>
    <el-form-item label="配置地点" prop="allocationLocation">
      <el-input v-model="dataForm.allocationLocation" placeholder="配置地点"></el-input>
    </el-form-item>
    <el-form-item label="责任部门" prop="responsibleDepartment">
      <el-input v-model="dataForm.responsibleDepartment" placeholder="责任部门"></el-input>
    </el-form-item>
    <el-form-item label="使用人" prop="userName">
      <el-input v-model="dataForm.userName" placeholder="使用人"></el-input>
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
    <el-form-item label="备注" prop="remarks">
      <el-input v-model="dataForm.remarks" placeholder="备注"></el-input>
    </el-form-item>
    <el-form-item label="数量" prop="quantity">
      <el-input v-model="dataForm.quantity" placeholder="数量"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="creatTime">
      <el-input v-model="dataForm.creatTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="创建人" prop="createPeople">
      <el-input v-model="dataForm.createPeople" placeholder="创建人"></el-input>
    </el-form-item>
    <el-form-item label="负责人" prop="leader">
      <el-input v-model="dataForm.leader" placeholder="负责人"></el-input>
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
          warehouseNumber: '',
          toolNumber: '',
          toolName: '',
          specificationModel: '',
          rangeValue: '',
          divisionValue: '',
          accuracy: '',
          purpose: '',
          supplier: '',
          factoryNumber: '',
          companyNumber: '',
          level: '',
          verificationCycle: '',
          verificationStatus: '',
          usageStatus: '',
          allocationLocation: '',
          responsibleDepartment: '',
          userName: '',
          calibrationDate: '',
          calibrationValidity: '',
          calibrationUnit: '',
          certificateNumber: '',
          remarks: '',
          quantity: '',
          creatTime: '',
          createPeople: '',
          leader: ''
        },
        dataRule: {
          warehouseNumber: [
            { required: true, message: '仓库号不能为空', trigger: 'blur' }
          ],
          toolNumber: [
            { required: true, message: '量具编号不能为空', trigger: 'blur' }
          ],
          toolName: [
            { required: true, message: '量具名称不能为空', trigger: 'blur' }
          ],
          specificationModel: [
            { required: true, message: '规格型号不能为空', trigger: 'blur' }
          ],
          rangeValue: [
            { required: true, message: '量程不能为空', trigger: 'blur' }
          ],
          divisionValue: [
            { required: true, message: '分度值不能为空', trigger: 'blur' }
          ],
          accuracy: [
            { required: true, message: '精度不能为空', trigger: 'blur' }
          ],
          purpose: [
            { required: true, message: '用途不能为空', trigger: 'blur' }
          ],
          supplier: [
            { required: true, message: '供应商不能为空', trigger: 'blur' }
          ],
          factoryNumber: [
            { required: true, message: '出厂编号不能为空', trigger: 'blur' }
          ],
          companyNumber: [
            { required: true, message: '公司编号不能为空', trigger: 'blur' }
          ],
          level: [
            { required: true, message: '级别不能为空', trigger: 'blur' }
          ],
          verificationCycle: [
            { required: true, message: '检定周期不能为空', trigger: 'blur' }
          ],
          verificationStatus: [
            { required: true, message: '检定状态不能为空', trigger: 'blur' }
          ],
          usageStatus: [
            { required: true, message: '使用状态不能为空', trigger: 'blur' }
          ],
          allocationLocation: [
            { required: true, message: '配置地点不能为空', trigger: 'blur' }
          ],
          responsibleDepartment: [
            { required: true, message: '责任部门不能为空', trigger: 'blur' }
          ],
          userName: [
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
          remarks: [
            { required: true, message: '备注不能为空', trigger: 'blur' }
          ],
          quantity: [
            { required: true, message: '数量不能为空', trigger: 'blur' }
          ],
          creatTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          createPeople: [
            { required: true, message: '创建人不能为空', trigger: 'blur' }
          ],
          leader: [
            { required: true, message: '负责人不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/generator/instrumentation/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.warehouseNumber = data.instrumentation.warehouseNumber
                this.dataForm.toolNumber = data.instrumentation.toolNumber
                this.dataForm.toolName = data.instrumentation.toolName
                this.dataForm.specificationModel = data.instrumentation.specificationModel
                this.dataForm.rangeValue = data.instrumentation.rangeValue
                this.dataForm.divisionValue = data.instrumentation.divisionValue
                this.dataForm.accuracy = data.instrumentation.accuracy
                this.dataForm.purpose = data.instrumentation.purpose
                this.dataForm.supplier = data.instrumentation.supplier
                this.dataForm.factoryNumber = data.instrumentation.factoryNumber
                this.dataForm.companyNumber = data.instrumentation.companyNumber
                this.dataForm.level = data.instrumentation.level
                this.dataForm.verificationCycle = data.instrumentation.verificationCycle
                this.dataForm.verificationStatus = data.instrumentation.verificationStatus
                this.dataForm.usageStatus = data.instrumentation.usageStatus
                this.dataForm.allocationLocation = data.instrumentation.allocationLocation
                this.dataForm.responsibleDepartment = data.instrumentation.responsibleDepartment
                this.dataForm.userName = data.instrumentation.userName
                this.dataForm.calibrationDate = data.instrumentation.calibrationDate
                this.dataForm.calibrationValidity = data.instrumentation.calibrationValidity
                this.dataForm.calibrationUnit = data.instrumentation.calibrationUnit
                this.dataForm.certificateNumber = data.instrumentation.certificateNumber
                this.dataForm.remarks = data.instrumentation.remarks
                this.dataForm.quantity = data.instrumentation.quantity
                this.dataForm.creatTime = data.instrumentation.creatTime
                this.dataForm.createPeople = data.instrumentation.createPeople
                this.dataForm.leader = data.instrumentation.leader
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
              url: this.$http.adornUrl(`/generator/instrumentation/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'warehouseNumber': this.dataForm.warehouseNumber,
                'toolNumber': this.dataForm.toolNumber,
                'toolName': this.dataForm.toolName,
                'specificationModel': this.dataForm.specificationModel,
                'rangeValue': this.dataForm.rangeValue,
                'divisionValue': this.dataForm.divisionValue,
                'accuracy': this.dataForm.accuracy,
                'purpose': this.dataForm.purpose,
                'supplier': this.dataForm.supplier,
                'factoryNumber': this.dataForm.factoryNumber,
                'companyNumber': this.dataForm.companyNumber,
                'level': this.dataForm.level,
                'verificationCycle': this.dataForm.verificationCycle,
                'verificationStatus': this.dataForm.verificationStatus,
                'usageStatus': this.dataForm.usageStatus,
                'allocationLocation': this.dataForm.allocationLocation,
                'responsibleDepartment': this.dataForm.responsibleDepartment,
                'userName': this.dataForm.userName,
                'calibrationDate': this.dataForm.calibrationDate,
                'calibrationValidity': this.dataForm.calibrationValidity,
                'calibrationUnit': this.dataForm.calibrationUnit,
                'certificateNumber': this.dataForm.certificateNumber,
                'remarks': this.dataForm.remarks,
                'quantity': this.dataForm.quantity,
                'creatTime': this.dataForm.creatTime,
                'createPeople': this.dataForm.createPeople,
                'leader': this.dataForm.leader
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
