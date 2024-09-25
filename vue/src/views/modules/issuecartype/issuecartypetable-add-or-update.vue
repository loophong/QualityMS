<template>
  <el-dialog
    :title="!dataForm.vehicleTypeId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="具体车型" prop="concreteVehicleType">
      <el-input v-model="dataForm.concreteVehicleType" placeholder="具体车型"></el-input>
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
          vehicleTypeId: 0,
          concreteVehicleType: ''
        },
        dataRule: {
          concreteVehicleType: [
            { required: true, message: '具体车型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.vehicleTypeId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.vehicleTypeId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuecartypetable/info/${this.dataForm.vehicleTypeId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.concreteVehicleType = data.issueCarTypeTable.concreteVehicleType
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
              url: this.$http.adornUrl(`/generator/issuecartypetable/${!this.dataForm.vehicleTypeId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'vehicleTypeId': this.dataForm.vehicleTypeId || undefined,
                'concreteVehicleType': this.dataForm.concreteVehicleType
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
