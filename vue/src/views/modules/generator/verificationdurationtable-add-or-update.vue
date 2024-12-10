<template>
  <el-dialog
    :title="!dataForm.examineId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="验证时长" prop="verificationDuration">
      <el-input v-model="dataForm.verificationDuration" placeholder="验证时长"></el-input>
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
          examineId: 0,
          verificationDuration: ''
        },
        dataRule: {
          verificationDuration: [
            { required: true, message: '验证时长不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.examineId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.examineId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/verificationdurationtable/info/${this.dataForm.examineId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.verificationDuration = data.verificationDurationTable.verificationDuration
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
              url: this.$http.adornUrl(`/generator/verificationdurationtable/${!this.dataForm.examineId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'examineId': this.dataForm.examineId || undefined,
                'verificationDuration': this.dataForm.verificationDuration
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
