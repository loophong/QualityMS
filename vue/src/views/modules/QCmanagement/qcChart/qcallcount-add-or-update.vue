<template>
  <el-dialog :title="!dataForm.qcAllId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="80px">
      <el-form-item label="职工总数" prop="qcMemberCount">
        <el-input v-model="dataForm.qcMemberCount" placeholder="职工总数"></el-input>
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
  data() {
    return {
      visible: false,
      dataForm: {
        qcAllId: 0,
        qcMemberCount: ''
      },
      dataRule: {
        qcMemberCount: [
          { required: true, validator: this.numValidatorNonZeroNature, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {

    //只允许正整数
    numValidatorNonZeroNature(rule, value, callback) {
      //验证是否为空
      if (value === undefined || value === null || value === '') {
        callback(new Error('数据不能为空'));
      } else {
        //验证是否符合数字和正负号的格式
        if (!/^\d+$/.test(value)) {
          callback(new Error('请输入正整数'));
        } else {
          const numValue = parseInt(value);
          // 验证是否为有效数字
          // if (isNaN(numValue)) {
          //   callback(new Error('请输入有效的数字'));
          if (isNaN(numValue) || numValue <= 0) {
            callback(new Error('请输入正整数'));
          } else if (numValue > 999999999999) {
            // 验证是否在数据范围内
            callback(new Error('数据长度过大!'));
          } else {
            // 如果所有验证都通过，则调用callback无参数
            callback();
          }
        }
      }
    },
    init(id) {
      this.dataForm.qcAllId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.qcAllId) {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/qcAllCount/info/${this.dataForm.qcAllId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.qcMemberCount = data.qcAllCount.qcMemberCount
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/qcAllCount/${!this.dataForm.qcAllId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'qcAllId': this.dataForm.qcAllId || undefined,
              'qcMemberCount': this.dataForm.qcMemberCount
            })
          }).then(({ data }) => {
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
