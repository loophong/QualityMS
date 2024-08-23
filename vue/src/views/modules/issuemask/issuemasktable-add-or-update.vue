<template>
  <div>
  <el-dialog
    :title="!dataForm.issuemaskId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="序号" prop="serialNumber">
      <el-input v-model="dataForm.serialNumber" placeholder="序号"></el-input>
    </el-form-item>
    <el-form-item label="问题编号(所属问题)" prop="issueNumber">
      <el-input v-model="dataForm.issueNumber" placeholder="问题编号(所属问题)"></el-input>
    </el-form-item>
    <el-form-item label="审核人" prop="reviewers">
      <el-input v-model="dataForm.reviewers" placeholder="审核人"></el-input>
    </el-form-item>
    <el-form-item label="接收人" prop="recipients">
      <el-input v-model="dataForm.recipients" placeholder="接收人"></el-input>
    </el-form-item>
    <el-form-item label="任务内容" prop="maskcontent">
      <el-input v-model="dataForm.maskcontent" placeholder="任务内容"></el-input>
    </el-form-item>
    <el-form-item label="任务发起人" prop="creator">
      <el-input v-model="dataForm.creator" placeholder="任务发起人"></el-input>
    </el-form-item>
    <el-form-item label="发起时间" prop="creationTime">
      <el-input v-model="dataForm.creationTime" placeholder="发起时间"></el-input>
    </el-form-item>
      <el-form-item label="要求完成时间" prop="creationTime">
        <el-input v-model="dataForm.requiredcompletiontime" placeholder="要求完成时间"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>

    <!-- 派发弹窗 -->
    <el-dialog
      title="派发"
      :close-on-click-modal="false"
      :visible.sync="visible1">
      <el-form :model="dataForm" :rules="dataRule" ref="dispatchForm" @keyup.enter.native="dispatchFormSubmit()" label-width="80px">
        <el-form-item label="接收人" prop="recipients">
          <el-input v-model="dataForm.recipients" placeholder="接收人"></el-input>
        </el-form-item>
        <el-form-item label="派发时间" prop="dispatchTime">
          <el-date-picker v-model="dataForm.creationTime" type="datetime" placeholder="选择派发时间"></el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visible1 = false">取消</el-button>
        <el-button type="primary" @click="dispatchFormSubmit()">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        visible1: false,
        dataForm: {
          issuemaskId: 0,
          serialNumber: '',
          issueNumber: '',
          reviewers: '',
          recipients: '',
          maskcontent: '',
          creator: '',
          creationTime: '',
          requiredcompletiontime: ''
        },
        dataRule: {
          // serialNumber: [
          //   { required: true, message: '序号不能为空', trigger: 'blur' }
          // ],
          // issueNumber: [
          //   { required: true, message: '问题编号(所属问题)不能为空', trigger: 'blur' }
          // ],
          // reviewers: [
          //   { required: true, message: '审核人不能为空', trigger: 'blur' }
          // ],
          // recipients: [
          //   { required: true, message: '接收人不能为空', trigger: 'blur' }
          // ],
          // maskcontent: [
          //   { required: true, message: '任务内容不能为空', trigger: 'blur' }
          // ],
          // creator: [
          //   { required: true, message: '任务发起人不能为空', trigger: 'blur' }
          // ],
          // creationTime: [
          //   { required: true, message: '发起时间不能为空', trigger: 'blur' }
          // ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.issuemaskId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issuemaskId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/info/${this.dataForm.issuemaskId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.serialNumber = data.issueMaskTable.serialNumber
                this.dataForm.issueNumber = data.issueMaskTable.issueNumber
                this.dataForm.reviewers = data.issueMaskTable.reviewers
                this.dataForm.recipients = data.issueMaskTable.recipients
                this.dataForm.maskcontent = data.issueMaskTable.maskcontent
                this.dataForm.creator = data.issueMaskTable.creator
                this.dataForm.creationTime = data.issueMaskTable.creationTime
                this.dataForm.requiredcompletiontime = data.issueMaskTable.requiredcompletiontime
                this.dataForm.state = '审核中'
              }
            })
          }
        })
      },
      init1 (id) {
        console.log('初始化弹窗，ID:', id)
        this.dataForm.issuemaskId = id || 0
        this.visible1 = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issuemaskId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/info/${this.dataForm.issuemaskId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.serialNumber = data.issueMaskTable.serialNumber
                this.dataForm.issueNumber = data.issueMaskTable.issueNumber
                this.dataForm.reviewers = data.issueMaskTable.reviewers
                this.dataForm.recipients = data.issueMaskTable.recipients
                this.dataForm.maskcontent = data.issueMaskTable.maskcontent
                this.dataForm.creator = data.issueMaskTable.creator
                this.dataForm.creationTime = data.issueMaskTable.creationTime
              }
            })
          }
        })
      },
      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('提交成功!')
          } else {
            console.log('提交失败!!')
            return false
          }
        })
      },
      resetForm (formName) {
        this.$refs[formName].resetFields()
      },
      initDispatch (id) {
        console.log('初始化派发弹窗，ID:', id)
        this.dataForm.issuemaskId = id || 0
        this.visible1 = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetForm()
        })
      },
      dispatchFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            console.log('派发表单提交', this.dispatchForm)
            this.visible1 = false // 关闭派发弹窗
          } else {
            console.log('派发表单验证失败')
            return false
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/${!this.dataForm.issuemaskId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'issuemaskId': this.dataForm.issuemaskId || undefined,
                'serialNumber': this.dataForm.serialNumber,
                'issueNumber': this.dataForm.issueNumber,
                'reviewers': this.dataForm.reviewers,
                'recipients': this.dataForm.recipients,
                'maskcontent': this.dataForm.maskcontent,
                'creator': this.dataForm.creator,
                'creationTime': this.dataForm.creationTime
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
