<template>
  <el-dialog
    :title="!dataForm.vendorId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="供应商编号" prop="vendornumber">
        <el-input v-model="dataForm.vendornumber" placeholder="供应商"></el-input>
      </el-form-item>
    <el-form-item label="供应商" prop="vendor">
      <el-input v-model="dataForm.vendor" placeholder="供应商"></el-input>
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
          vendorId: 0,
          vendornumber:'',
          vendor: ''
        },
        dataRule: {
          vendor: [
            { required: true, message: '供应商不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.vendorId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.vendorId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/vendortable/info/${this.dataForm.vendorId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.vendornumber = data.vendorTable.vendornumber
                this.dataForm.vendor = data.vendorTable.vendor
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
              url: this.$http.adornUrl(`/generator/vendortable/${!this.dataForm.vendorId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'vendorId': this.dataForm.vendorId || undefined,
                'vendornumber': this.dataForm.vendornumber,
                'vendor': this.dataForm.vendor
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
