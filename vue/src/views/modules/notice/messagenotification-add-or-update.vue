<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="接收者ID" prop="receiverId">
        <el-input v-model="dataForm.receiverId" placeholder="接收者ID"></el-input>
      </el-form-item>
      <el-form-item label="发送者ID" prop="senderId">
        <el-input v-model="dataForm.senderId" placeholder="发送者ID"></el-input>
      </el-form-item>
      <el-form-item label="消息内容" prop="content">
        <el-input v-model="dataForm.content" placeholder="消息内容"></el-input>
      </el-form-item>
      <el-form-item label="消息类型" prop="type">
        <el-input v-model="dataForm.type" placeholder="消息类型"></el-input>
      </el-form-item>
<!--      <el-form-item label="创建时间" prop="createdAt">-->
<!--        &lt;!&ndash;        <el-input v-model="dataForm.createdAt" placeholder="创建时间"></el-input>&ndash;&gt;-->
<!--        <el-date-picker v-model="dataForm.createdAt" type="date" placeholder="选择日期"></el-date-picker>-->

<!--      </el-form-item>-->

      <el-form-item label="消息状态" prop="status">
        <el-input v-model="dataForm.status" placeholder="消息状态"></el-input>
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
        id: 0,
        receiverId: '',
        senderId: '',
        content: '',
        type: '',
        createdAt: '',
        status: ''
      },
      dataRule: {
        receiverId: [
          {required: true, message: '接收者ID不能为空', trigger: 'blur'}
        ],
        senderId: [
          {required: true, message: '发送者ID不能为空', trigger: 'blur'}
        ],
        content: [
          {required: true, message: '消息内容不能为空', trigger: 'blur'}
        ],
        type: [
          {required: true, message: '消息类型不能为空', trigger: 'blur'}
        ],
        createdAt: [
          {required: true, message: '创建时间不能为空', trigger: 'blur'}
        ],
        status: [
          {required: true, message: '消息状态不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/notice/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.receiverId = data.messageNotification.receiverId
              this.dataForm.senderId = data.messageNotification.senderId
              this.dataForm.content = data.messageNotification.content
              this.dataForm.type = data.messageNotification.type
              this.dataForm.createdAt = data.messageNotification.createdAt
              this.dataForm.status = data.messageNotification.status
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
            url: this.$http.adornUrl(`/notice/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'receiverId': this.dataForm.receiverId,
              'senderId': this.dataForm.senderId,
              'content': this.dataForm.content,
              'type': this.dataForm.type,
              'createdAt': this.dataForm.createdAt,
              'status': this.dataForm.status
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
