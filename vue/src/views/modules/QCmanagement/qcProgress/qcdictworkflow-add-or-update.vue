<template>
  <el-dialog
    :title="!dataForm.dictId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="类型" prop="type">
      <el-input v-model="dataForm.type" placeholder="类型"></el-input>
    </el-form-item>
    <el-form-item label="阶段" prop="phase">
      <el-input v-model="dataForm.phase" placeholder="阶段"></el-input>
    </el-form-item>
    <el-form-item label="步骤" prop="step">
      <el-input v-model="dataForm.step" placeholder="步骤"></el-input>
    </el-form-item>
    <el-form-item label="补充描述" prop="description">
      <el-input v-model="dataForm.description" placeholder="补充描述"></el-input>
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
          dictId: 0,
          type: '',
          phase: '',
          step: '',
          description: ''
        },
        dataRule: {
          type: [
            { required: true, message: '类型不能为空', trigger: 'blur' }
          ],
          phase: [
            { required: true, message: '阶段不能为空', trigger: 'blur' }
          ],
          step: [
            { required: true, message: '步骤不能为空', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '补充描述不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.dictId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.dictId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/qcdictworkflow/info/${this.dataForm.dictId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.type = data.qcDictWorkflow.type
                this.dataForm.phase = data.qcDictWorkflow.phase
                this.dataForm.step = data.qcDictWorkflow.step
                this.dataForm.description = data.qcDictWorkflow.description
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
              url: this.$http.adornUrl(`/generator/qcdictworkflow/${!this.dataForm.dictId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'dictId': this.dataForm.dictId || undefined,
                'type': this.dataForm.type,
                'phase': this.dataForm.phase,
                'step': this.dataForm.step,
                'description': this.dataForm.description
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
