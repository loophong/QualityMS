<template>
  <el-dialog
    :title="!dataForm.dataId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="数据" prop="dataContent">
      <el-input v-model="dataForm.dataContent" placeholder="数据"></el-input>
    </el-form-item>
    <el-form-item label="数据类型" prop="dataType">
      <el-input v-model="dataForm.dataType" placeholder="数据类型"></el-input>
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
          dataId: 0,
          dataContent: '',
          dataType: ''
        },
        dataRule: {
          dataContent: [
            { required: true, message: '数据不能为空', trigger: 'blur' }
          ],
          dataType: [
            { required: true, message: '数据类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.dataId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.dataId) {
            this.$http({
              url: this.$http.adornUrl(`/indicator/indicatordatadictionary/info/${this.dataForm.dataId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.dataContent = data.indicatorDataDictionary.dataContent
                this.dataForm.dataType = data.indicatorDataDictionary.dataType
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
              url: this.$http.adornUrl(`/indicator/indicatordatadictionary/${!this.dataForm.dataId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'dataId': this.dataForm.dataId || undefined,
                'dataContent': this.dataForm.dataContent,
                'dataType': this.dataForm.dataType
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
