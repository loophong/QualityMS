<template>
  <el-dialog
    :title="!dataForm.gradeId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="等级" prop="grade">
      <el-input v-model="dataForm.grade" placeholder="等级"></el-input>
    </el-form-item>
    <el-form-item label="等级说明" prop="gradeIllustrate">
      <el-input v-model="dataForm.gradeIllustrate" placeholder="等级说明"></el-input>
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
          gradeId: 0,
          grade: '',
          gradeIllustrate: ''
        },
        dataRule: {
          grade: [
            { required: true, message: '等级不能为空', trigger: 'blur' }
          ],
          gradeIllustrate: [
            { required: true, message: '等级说明不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.gradeId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.gradeId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetypegradetable/info/${this.dataForm.gradeId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.grade = data.issuetypeGradeTable.grade
                this.dataForm.gradeIllustrate = data.issuetypeGradeTable.gradeIllustrate
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
              url: this.$http.adornUrl(`/generator/issuetypegradetable/${!this.dataForm.gradeId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'gradeId': this.dataForm.gradeId || undefined,
                'grade': this.dataForm.grade,
                'gradeIllustrate': this.dataForm.gradeIllustrate
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
