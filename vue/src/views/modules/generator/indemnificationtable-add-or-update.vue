<template>
  <el-dialog
    :title="!dataForm.indeId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="赔偿件" prop="indemnification">
      <el-input v-model="dataForm.indemnification" placeholder="赔偿件"></el-input>
    </el-form-item>
    <el-form-item label="图号" prop="figureNumber">
      <el-input v-model="dataForm.figureNumber" placeholder="图号"></el-input>
    </el-form-item>
    <el-form-item label="质量成本" prop="qualitycost">
      <el-input v-model="dataForm.qualitycost" placeholder="质量成本"></el-input>
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
          indeId: 0,
          indemnification: '',
          figureNumber: '',
          qualitycost: ''
        },
        dataRule: {
          indemnification: [
            { required: true, message: '赔偿件不能为空', trigger: 'blur' }
          ],
          figureNumber: [
            { required: true, message: '图号不能为空', trigger: 'blur' }
          ],
          qualitycost: [
            { required: true, message: '质量成本不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.indeId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.indeId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/indemnificationtable/info/${this.dataForm.indeId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.indemnification = data.indemnificationTable.indemnification
                this.dataForm.figureNumber = data.indemnificationTable.figureNumber
                this.dataForm.qualitycost = data.indemnificationTable.qualitycost
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
              url: this.$http.adornUrl(`/generator/indemnificationtable/${!this.dataForm.indeId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'indeId': this.dataForm.indeId || undefined,
                'indemnification': this.dataForm.indemnification,
                'figureNumber': this.dataForm.figureNumber,
                'qualitycost': this.dataForm.qualitycost
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
