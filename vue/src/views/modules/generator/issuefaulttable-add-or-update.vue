<template>
  <el-dialog
    :title="!dataForm.faultId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="系统分类" prop="systematicClassification">
      <el-input v-model="dataForm.systematicClassification" placeholder="系统分类"></el-input>
    </el-form-item>
    <el-form-item label="故障件一级" prop="firstFaultyParts">
      <el-input v-model="dataForm.firstFaultyParts" placeholder="故障件一级"></el-input>
    </el-form-item>
    <el-form-item label="故障件二级" prop="secondFaultyParts">
      <el-input v-model="dataForm.secondFaultyParts" placeholder="故障件二级"></el-input>
    </el-form-item>
    <el-form-item label="故障类别" prop="faultType">
      <el-input v-model="dataForm.faultType" placeholder="故障类别"></el-input>
    </el-form-item>
    <el-form-item label="故障模式" prop="faultModel">
      <el-input v-model="dataForm.faultModel" placeholder="故障模式"></el-input>
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
          faultId: 0,
          systematicClassification: '',
          firstFaultyParts: '',
          secondFaultyParts: '',
          faultType: '',
          faultModel: ''
        },
        dataRule: {
          systematicClassification: [
            { required: true, message: '系统分类不能为空', trigger: 'blur' }
          ],
          firstFaultyParts: [
            { required: true, message: '故障件一级不能为空', trigger: 'blur' }
          ],
          secondFaultyParts: [
            { required: true, message: '故障件二级不能为空', trigger: 'blur' }
          ],
          faultType: [
            { required: true, message: '故障类别不能为空', trigger: 'blur' }
          ],
          faultModel: [
            { required: true, message: '故障模式不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.faultId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.faultId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuefaulttable/info/${this.dataForm.faultId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.systematicClassification = data.issueFaultTable.systematicClassification
                this.dataForm.firstFaultyParts = data.issueFaultTable.firstFaultyParts
                this.dataForm.secondFaultyParts = data.issueFaultTable.secondFaultyParts
                this.dataForm.faultType = data.issueFaultTable.faultType
                this.dataForm.faultModel = data.issueFaultTable.faultModel
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
              url: this.$http.adornUrl(`/generator/issuefaulttable/${!this.dataForm.faultId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'faultId': this.dataForm.faultId || undefined,
                'systematicClassification': this.dataForm.systematicClassification,
                'firstFaultyParts': this.dataForm.firstFaultyParts,
                'secondFaultyParts': this.dataForm.secondFaultyParts,
                'faultType': this.dataForm.faultType,
                'faultModel': this.dataForm.faultModel
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
