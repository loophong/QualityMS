<template>
  <div>
  <el-dialog
    :title="!dataForm.issuemaskId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
<!--    <el-form-item label="序号" prop="serialNumber">-->
<!--      <el-input v-model="dataForm.serialNumber" placeholder="序号"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="问题编号(所属问题)" prop="issueNumber">-->
<!--      <el-input v-model="dataForm.issueNumber" placeholder="问题编号(所属问题)"></el-input>-->
<!--    </el-form-item>-->
    <el-form-item label="审核人" prop="reviewers">
<!--      <el-input v-model="dataForm.reviewers" placeholder="审核人"></el-input>-->
      <el-select v-model="dataForm.reviewers" filterable placeholder="请选择审核人">
        <el-option-group v-for="group in options" :key="group.label" :label="group.label">
          <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                     :value="item.label">
          </el-option>
        </el-option-group>
      </el-select>
    </el-form-item>
    <el-form-item label="接收人" prop="recipients">
<!--      <el-input v-model="dataForm.recipients" placeholder="接收人"></el-input>-->
      <el-select v-model="dataForm.recipients" filterable placeholder="请选择接收人">
        <el-option-group v-for="group in options" :key="group.label" :label="group.label">
          <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                     :value="item.label">
          </el-option>
        </el-option-group>
      </el-select>
    </el-form-item>
    <el-form-item label="任务内容" prop="maskcontent">
      <el-input v-model="dataForm.maskcontent" placeholder="任务内容"></el-input>
    </el-form-item>
<!--    <el-form-item label="任务发起人" prop="creator">-->
<!--      <el-input v-model="dataForm.creator" placeholder="任务发起人"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="发起时间" prop="creationTime">-->
<!--      <el-input v-model="dataForm.creationTime" placeholder="发起时间"></el-input>-->
<!--    </el-form-item>-->
<!--      <el-form-item label="要求完成时间" prop="requiredCompletionTime">-->
<!--        <el-input v-model="dataForm.requiredCompletionTime" placeholder="要求完成时间"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="要求完成时间" prop="requiredCompletionTime">
        <el-date-picker
          v-model="dataForm.requiredCompletionTime"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择日期">
        </el-date-picker>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFSubmit()">确定</el-button>
    </span>
  </el-dialog>
    <el-dialog
      :title="'是否确认完成'"
      :close-on-click-modal="false"
      :visible.sync="visible2"
      :width="'400px'"> <!-- 设置对话框宽度 -->
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="40px">
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="visible2 = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
    </el-dialog>

    <!-- 派发弹窗 -->
<!--    <el-dialog-->
<!--      title="派发"-->
<!--      :close-on-click-modal="false"-->
<!--      :visible.sync="visible1">-->
<!--      <el-form :model="dataForm" :rules="dataRule" ref="dispatchForm" @keyup.enter.native="datanewSubmit()" label-width="80px">-->
<!--        <el-form-item label="接收人" prop="recipients">-->
<!--          <el-select v-model="dataForm.recipients" filterable placeholder="请选择接收人">-->
<!--            <el-option-group v-for="group in options" :key="group.label" :label="group.label">-->
<!--              <el-option v-for="item in group.options" :key="item.value" :label="item.label"-->
<!--                         :value="item.label">-->
<!--              </el-option>-->
<!--            </el-option-group>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="派发时间" prop="dispatchTime">-->
<!--          <el-date-picker v-model="dataForm.creationTime" type="datetime" placeholder="选择派发时间"></el-date-picker>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="visible1 = false">取消</el-button>-->
<!--        <el-button type="primary" @click="datanewSubmit()">确定</el-button>-->
<!--      </span>-->
<!--    </el-dialog>-->
    <el-dialog
      :title="!dataForm.issuemaskId ? '新增' : '派发'"
      :close-on-click-modal="false"
      :visible.sync="visible1"
      :width="'400px'">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
        <el-form-item label="接收人" prop="recipients">
                    <el-select v-model="dataForm.recipients" filterable placeholder="请选择接收人">
                      <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                        <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                                   :value="item.label">
                        </el-option>
                      </el-option-group>
                    </el-select>
                  </el-form-item>
<!--                  <el-form-item label="派发时间" prop="creationTime">-->
<!--                    <el-date-picker v-model="dataForm.creationTime" type="datetime" placeholder="选择派发时间"></el-date-picker>-->
<!--                  </el-form-item>-->

      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="visible1 = false">取消</el-button>
      <el-button type="primary" @click="datanewSubmit()">确定</el-button>
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
        visible2: false,
        dataForm: {
          issuemaskId: 0,
          serialNumber: '',
          issueNumber: '',
          reviewers: '',
          recipients: '',
          maskcontent: '',
          creator: '',
          creationTime: '', // 初始化为当前时间
          state: '',
          requiredCompletionTime: '',
          superiorMask: '',
          options: ''
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
        },
        options: ''
      }
    },
    computed: {
      formattedCreationTime() {
        const date = new Date();
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      }
    },
    created () {
      this.$http({
        url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
        method: 'get',
      }).then(({ data }) => {
        this.options = data;
        console.log(data);
        // if (data && data.code === 0) {
        //   console.log(data);
        // }
      })
    },
    methods: {
      generateSerialNumber() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0');
        return `${year}${month}${day}${random}`;
      },
      init (id) {
        this.dataForm.issuemaskId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issuemaskId) {
            console.log('huoqu ' ,this.dataForm.issuemaskId)
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/info/${this.dataForm.issuemaskId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                // this.dataForm.serialNumber = data.issueMaskTable.serialNumber
                // this.dataForm.issueNumber = data.issueMaskTable.issueNumber
                this.dataForm.reviewers = data.issueMaskTable.reviewers
                this.dataForm.recipients = data.issueMaskTable.recipients
                this.dataForm.maskcontent = data.issueMaskTable.maskcontent
                // this.dataForm.creator = data.issueMaskTable.creator
                // this.dataForm.creationTime = data.issueMaskTable.creationTime
                this.dataForm.requiredCompletionTime = data.issueMaskTable.requiredCompletionTime
                // this.dataForm.state = '已派发'
              }
            })
          }
        })
      },
      init1 (id) {
        console.log('初始化弹窗，ID:', id)
        console.log('当前时间为: ' ,this.formattedCreationTime )
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
                this.dataForm.creationTime = this.formattedCreationTime // 初始化为当前时间
                this.dataForm.requiredCompletionTime = data.issueMaskTable.requiredCompletionTime
                this.dataForm.superiorMask = data.issueMaskTable.superiorMask
              }
            })
          }
        })
      },
      completeHandle (id) {
        this.dataForm.issuemaskId = id || 0
        this.visible2 = true
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
                this.dataForm.requiredCompletionTime = data.issueMaskTable.requiredCompletionTime
                this.dataForm.state = '已完成'
              }
            })
          }
        })
      },

      dataFSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/${!this.dataForm.issuemaskId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'issuemaskId': this.dataForm.issuemaskId || undefined,
                'reviewers': this.dataForm.reviewers,
                'recipients': this.dataForm.recipients,
                'maskcontent': this.dataForm.maskcontent,
                'requiredCompletionTime': this.dataForm.requiredCompletionTime,
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.visible2 = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
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
                'creationTime': this.dataForm.creationTime,
                'state': this.dataForm.state
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.visible2 = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
      // 表单提交
      datanewSubmit () {
        const serialNumber = this.generateSerialNumber();
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/save`),
              method: 'post',
              data: this.$http.adornData({
                'issuemaskId': undefined,
                'serialNumber': serialNumber,
                'issueNumber': this.dataForm.issueNumber,
                'reviewers': this.dataForm.reviewers,
                'recipients': this.dataForm.recipients,
                'maskcontent': this.dataForm.maskcontent,
                'creator': this.dataForm.creator,
                'creationTime': this.dataForm.creationTime,
                'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                'superiorMask': this.dataForm.superiorMask,
                'state': '审核中'
              })
            }).then(({data}) => {
              this.$http({
                url: this.$http.adornUrl('/generator/issuemasktable/dispach'),
                method: 'post',
                params: this.$http.adornParams({
                  issueNumber: this.dataForm.serialNumber,
                  serialNumber: serialNumber
                })
              }).catch(() => {
                this.$message.error('修改失败')
              })
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible1 = false
                    // this.visible2 = false
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
