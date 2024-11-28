<template>
  <el-dialog
    :title="!dataForm.analysisId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="初步分析" prop="peliminaryAnalysis">
      <el-input v-model="dataForm.peliminaryAnalysis" placeholder="初步分析"></el-input>
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
          analysisId: 0,
          peliminaryAnalysis: ''
        },
        dataRule: {
          peliminaryAnalysis: [
            { required: true, message: '初步分析不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.analysisId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.analysisId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/peliminaryanalysistable/info/${this.dataForm.analysisId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.peliminaryAnalysis = data.peliminaryAnalysisTable.peliminaryAnalysis
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
              url: this.$http.adornUrl(`/generator/peliminaryanalysistable/${!this.dataForm.analysisId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'analysisId': this.dataForm.analysisId || undefined,
                'peliminaryAnalysis': this.dataForm.peliminaryAnalysis
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
