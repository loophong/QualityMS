<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="序号" prop="serialNumber">
      <el-input v-model="dataForm.serialNumber" placeholder="序号"></el-input>
    </el-form-item>
    <el-form-item label="检验报告类别" prop="testReportType">
      <el-input v-model="dataForm.testReportType" placeholder="检验报告类别"></el-input>
    </el-form-item>
    <el-form-item label="检验报告名称" prop="testReportName">
      <el-input v-model="dataForm.testReportName" placeholder="检验报告名称"></el-input>
    </el-form-item>
    <el-form-item label="检验件名称" prop="testItemName">
      <el-input v-model="dataForm.testItemName" placeholder="检验件名称"></el-input>
    </el-form-item>
    <el-form-item label="检验件规格型号" prop="testItemStandardNumber">
      <el-input v-model="dataForm.testItemStandardNumber" placeholder="检验件规格型号"></el-input>
    </el-form-item>
    <el-form-item label="检验件批次/编号" prop="testItemBatchNumber">
      <el-input v-model="dataForm.testItemBatchNumber" placeholder="检验件批次/编号"></el-input>
    </el-form-item>
    <el-form-item label="测量设备" prop="measuringEquipment">
      <el-input v-model="dataForm.measuringEquipment" placeholder="测量设备"></el-input>
    </el-form-item>
    <el-form-item label="测量方法" prop="measuringMethod">
      <el-input v-model="dataForm.measuringMethod" placeholder="测量方法"></el-input>
    </el-form-item>
    <el-form-item label="检验人" prop="tester">
      <el-input v-model="dataForm.tester" placeholder="检验人"></el-input>
    </el-form-item>
    <el-form-item label="检验日期" prop="testDate">
      <el-input v-model="dataForm.testDate" placeholder="检验日期"></el-input>
    </el-form-item>
    <el-form-item label="检验报告" prop="testReport">
      <el-input v-model="dataForm.testReport" placeholder="检验报告"></el-input>
    </el-form-item>
    <el-form-item label="备注" prop="notes">
      <el-input v-model="dataForm.notes" placeholder="备注"></el-input>
    </el-form-item>
    <!-- <el-form-item label="逻辑删除标识 (0: 不删除, 1: 删除)" prop="isDeleted">
      <el-input v-model="dataForm.isDeleted" placeholder="逻辑删除标识 (0: 不删除, 1: 删除)"></el-input>
    </el-form-item>
    <el-form-item label="创建人" prop="createdBy">
      <el-input v-model="dataForm.createdBy" placeholder="创建人"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="备用字段" prop="backupField">
      <el-input v-model="dataForm.backupField" placeholder="备用字段"></el-input>
    </el-form-item> -->
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
          serialNumber: '',
          testReportType: '',
          testReportName: '',
          testItemName: '',
          testItemStandardNumber: '',
          testItemBatchNumber: '',
          measuringEquipment: '',
          measuringMethod: '',
          tester: '',
          testDate: '',
          testReport: '',
          notes: '',
          isDeleted: '',
          createdBy: '',
          createdAt: '',
          backupField: ''
        },
        dataRule: {
          serialNumber: [
            { required: true, message: '序号不能为空', trigger: 'blur' }
          ],
          testReportType: [
            { required: true, message: '检验报告类别不能为空', trigger: 'blur' }
          ],
          testReportName: [
            { required: true, message: '检验报告名称不能为空', trigger: 'blur' }
          ],
          testItemName: [
            { required: true, message: '检验件名称不能为空', trigger: 'blur' }
          ],
          testItemStandardNumber: [
            { required: true, message: '检验件规格型号不能为空', trigger: 'blur' }
          ],
          testItemBatchNumber: [
            { required: true, message: '检验件批次/编号不能为空', trigger: 'blur' }
          ],
          measuringEquipment: [
            { required: true, message: '测量设备不能为空', trigger: 'blur' }
          ],
          measuringMethod: [
            { required: true, message: '测量方法不能为空', trigger: 'blur' }
          ],
          tester: [
            { required: true, message: '检验人不能为空', trigger: 'blur' }
          ],
          testDate: [
            { required: true, message: '检验日期不能为空', trigger: 'blur' }
          ],
          testReport: [
            { required: true, message: '检验报告不能为空', trigger: 'blur' }
          ],
          notes: [
            { required: true, message: '备注不能为空', trigger: 'blur' }
          ],
          isDeleted: [
            { required: true, message: '逻辑删除标识 (0: 不删除, 1: 删除)不能为空', trigger: 'blur' }
          ],
          createdBy: [
            { required: true, message: '创建人不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          backupField: [
            { required: true, message: '备用字段不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/generator/instrumenttestreport/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.serialNumber = data.instrumentTestreport.serialNumber
                this.dataForm.testReportType = data.instrumentTestreport.testReportType
                this.dataForm.testReportName = data.instrumentTestreport.testReportName
                this.dataForm.testItemName = data.instrumentTestreport.testItemName
                this.dataForm.testItemStandardNumber = data.instrumentTestreport.testItemStandardNumber
                this.dataForm.testItemBatchNumber = data.instrumentTestreport.testItemBatchNumber
                this.dataForm.measuringEquipment = data.instrumentTestreport.measuringEquipment
                this.dataForm.measuringMethod = data.instrumentTestreport.measuringMethod
                this.dataForm.tester = data.instrumentTestreport.tester
                this.dataForm.testDate = data.instrumentTestreport.testDate
                this.dataForm.testReport = data.instrumentTestreport.testReport
                this.dataForm.notes = data.instrumentTestreport.notes
                this.dataForm.isDeleted = data.instrumentTestreport.isDeleted
                this.dataForm.createdBy = data.instrumentTestreport.createdBy
                this.dataForm.createdAt = data.instrumentTestreport.createdAt
                this.dataForm.backupField = data.instrumentTestreport.backupField
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
              url: this.$http.adornUrl(`/generator/instrumenttestreport/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'serialNumber': this.dataForm.serialNumber,
                'testReportType': this.dataForm.testReportType,
                'testReportName': this.dataForm.testReportName,
                'testItemName': this.dataForm.testItemName,
                'testItemStandardNumber': this.dataForm.testItemStandardNumber,
                'testItemBatchNumber': this.dataForm.testItemBatchNumber,
                'measuringEquipment': this.dataForm.measuringEquipment,
                'measuringMethod': this.dataForm.measuringMethod,
                'tester': this.dataForm.tester,
                'testDate': this.dataForm.testDate,
                'testReport': this.dataForm.testReport,
                'notes': this.dataForm.notes,
                'isDeleted': this.dataForm.isDeleted,
                'createdBy': this.dataForm.createdBy,
                'createdAt': this.dataForm.createdAt,
                'backupField': this.dataForm.backupField
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
