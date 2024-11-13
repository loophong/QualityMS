<template>
  <el-dialog
    :title="!dataForm.departmentId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="部门编号" prop="departmentNumber">
      <el-input v-model="dataForm.departmentNumber" placeholder="部门编号"></el-input>
    </el-form-item>
    <el-form-item label="部门名称" prop="departmentName">
      <el-input v-model="dataForm.departmentName" placeholder="部门名称"></el-input>
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
          departmentId: 0,
          departmentNumber: '',
          departmentName: ''
        },
        dataRule: {
          departmentNumber: [
            { required: true, message: '部门编号不能为空', trigger: 'blur' }
          ],
          departmentName: [
            { required: true, message: '部门名称不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.departmentId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.departmentId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/departmenttable/info/${this.dataForm.departmentId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.departmentNumber = data.departmentTable.departmentNumber
                this.dataForm.departmentName = data.departmentTable.departmentName
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
              url: this.$http.adornUrl(`/generator/departmenttable/${!this.dataForm.departmentId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'departmentId': this.dataForm.departmentId || undefined,
                'departmentNumber': this.dataForm.departmentNumber,
                'departmentName': this.dataForm.departmentName
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
