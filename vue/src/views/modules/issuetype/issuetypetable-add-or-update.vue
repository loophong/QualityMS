<template>
  <el-dialog
    :title="!dataForm.issueCategoryId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="问题类别" prop="concreteIssueCategory">
      <el-input v-model="dataForm.concreteIssueCategory" placeholder="具体问题类别"></el-input>
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
          issueCategoryId: 0,
          concreteIssueCategory: ''
        },
        dataRule: {
          concreteIssueCategory: [
            { required: true, message: '具体问题类别不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.issueCategoryId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issueCategoryId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetypetable/info/${this.dataForm.issueCategoryId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.concreteIssueCategory = data.issueTypeTable.concreteIssueCategory
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
              url: this.$http.adornUrl(`/generator/issuetypetable/${!this.dataForm.issueCategoryId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'issueCategoryId': this.dataForm.issueCategoryId || undefined,
                'concreteIssueCategory': this.dataForm.concreteIssueCategory
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
