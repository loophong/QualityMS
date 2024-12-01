<template>
  <el-dialog :title="!dataForm.qcTableId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="80px">
      <!-- <el-form-item label="类型" prop="tableType">
        <el-input v-model="dataForm.tableType" placeholder="类型"></el-input>
      </el-form-item> -->
      <el-form-item label="标题编号" prop="tableNumber">
        <el-input v-model="dataForm.tableNumber" placeholder="标题编号"></el-input>
      </el-form-item>
      <el-form-item label="标题内容" prop="tableTag">
        <el-input v-model="dataForm.tableTag" placeholder="标题内容"></el-input>
      </el-form-item>
      <el-form-item label="评审内容" prop="tableComment">
        <el-input type="textarea" autosize v-model="dataForm.tableComment" placeholder="评审内容"></el-input>
      </el-form-item>
      <!-- <el-form-item label="备注" prop="tableExtra">
        <el-input v-model="dataForm.tableExtra" placeholder="备注"></el-input>
      </el-form-item> -->
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
        qcTableId: 0,
        tableType: '',
        tableNumber: '',
        tableTag: '',
        tableComment: '',
        tableExtra: ''
      },
      dataRule: {
        tableType: [
          { required: true, message: '初评或终评不能为空', trigger: 'blur' }
        ],
        tableNumber: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        tableTag: [
          { required: true, message: '小标题不能为空', trigger: 'blur' }
        ],
        tableComment: [
          { required: true, message: '评审内容不能为空', trigger: 'blur' }
        ],
        tableExtra: [
          { required: true, message: '备注不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.qcTableId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.qcTableId) {
          this.$http({
            url: this.$http.adornUrl(`/qc/table/info/${this.dataForm.qcTableId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.tableType = data.qcCommentTable.tableType
              this.dataForm.tableNumber = data.qcCommentTable.tableNumber
              this.dataForm.tableTag = data.qcCommentTable.tableTag
              this.dataForm.tableComment = JSON.parse(data.qcCommentTable.tableComment)
              // this.dataForm.tableExtra = data.qcCommentTable.tableExtra
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
            url: this.$http.adornUrl(`/qc/table/${!this.dataForm.qcTableId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'qcTableId': this.dataForm.qcTableId || undefined,
              'tableNumber': this.dataForm.tableNumber,
              'tableTag': this.dataForm.tableTag,
              'tableComment': JSON.stringify(this.dataForm.tableComment),
              // 'tableExtra': this.dataForm.tableExtra
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
