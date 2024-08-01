<template>
  <el-dialog :title="!dataForm.qcgmId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="120px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="dataForm.name" placeholder="姓名"></el-input>
      </el-form-item>
      <!-- <el-form-item label="性别" prop="gender">
        <el-input v-model="dataForm.gender" placeholder="性别" disabled></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="手机号" prop="telNumber">
        <el-input v-model="dataForm.telNumber" placeholder="手机号" disabled></el-input>
      </el-form-item> -->
      <el-form-item label="员工编号" prop="number">
        <el-input v-model="dataForm.number" placeholder="员工编号"></el-input>
      </el-form-item>
      <!-- <el-form-item label="学历" prop="education">
        <el-input v-model="dataForm.education" placeholder="学历" disabled></el-input>
      </el-form-item>
      <el-form-item label="部门" prop="department">
        <el-input v-model="dataForm.department" placeholder="部门" disabled></el-input>
      </el-form-item>
      <el-form-item label="岗位" prop="position">
        <el-input v-model="dataForm.position" placeholder="岗位" disabled></el-input>
      </el-form-item>
      <el-form-item label="班组" prop="team">
        <el-input v-model="dataForm.team" placeholder="班组" disabled></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="参加QC时间" prop="participationDate">
        <el-input v-model="dataForm.participationDate" placeholder="参加QC时间"></el-input>
      </el-form-item> -->
      <el-form-item label="参加QC时间" prop="participationDate">
        <el-date-picker clearable v-model="dataForm.participationDate" type="date" value-format="yyyy-MM-dd"
          placeholder="请选择日期">
        </el-date-picker>
      </el-form-item>
      <!-- <el-form-item label="QC课题" prop="topic">
        <el-input v-model="dataForm.topic" placeholder="QC课题"></el-input>
      </el-form-item>
      <el-form-item label="QC课题内角色" prop="roleInTopic">
        <el-input v-model="dataForm.roleInTopic" placeholder="QC课题内角色"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="逻辑删除flag" prop="deleteFlag">
        <el-input v-model="dataForm.deleteFlag" placeholder="逻辑删除flag"></el-input>
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
        qcgmId: 0,
        name: '',
        gender: '',
        telNumber: '',
        number: '',
        education: '',
        department: '',
        position: '',
        team: '',
        participationDate: '',
        topic: '',
        roleInTopic: '',
        deleteFlag: ''
      },
      dataRule: {

        name: [
          { required: true, message: '姓名不能为空', trigger: 'blur' }
        ],
        // gender: [
        //   { required: true, message: '性别不能为空', trigger: 'blur' }
        // ],
        // telNumber: [
        //   { required: true, message: '手机号不能为空', trigger: 'blur' }
        // ],
        number: [
          { required: true, message: '员工编号不能为空', trigger: 'blur' }
        ],
        // education: [
        //   { required: true, message: '学历不能为空', trigger: 'blur' }
        // ],
        // department: [
        //   { required: true, message: '部门不能为空', trigger: 'blur' }
        // ],
        // position: [
        //   { required: true, message: '岗位不能为空', trigger: 'blur' }
        // ],
        // team: [
        //   { required: true, message: '班组不能为空', trigger: 'blur' }
        // ],

        participationDate: [
          { required: true, message: '参加QC时间不能为空', trigger: 'blur' }
        ],
        // topic: [
        //   { required: true, message: 'QC课题不能为空', trigger: 'blur' }
        // ],
        // roleInTopic: [
        //   { required: true, message: 'QC课题内角色不能为空', trigger: 'blur' }
        // ],
        // deleteFlag: [
        //   { required: true, message: '逻辑删除flag不能为空', trigger: 'blur' }
        // ]
      }
    }
  },
  mounted() {
    // 设置默认时间为当前时间
    this.setDefaultDate();
  },
  methods: {
    setDefaultDate() {
      // 获取当前时间并格式化为 yyyy-MM-dd
      const currentDate = new Date().toISOString().slice(0, 10);
      // 设置 dataForm.participationDate 的值为当前时间
      this.dataForm.participationDate = currentDate;
    },
    init(id) {
      this.dataForm.qcgmId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.qcgmId) {
          this.$http({
            url: this.$http.adornUrl(`/qcMembers/qcGroupMember/info/${this.dataForm.qcgmId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.name = data.qcGroupMember.name
              this.dataForm.gender = data.qcGroupMember.gender
              this.dataForm.telNumber = data.qcGroupMember.telNumber
              this.dataForm.number = data.qcGroupMember.number
              this.dataForm.education = data.qcGroupMember.education
              this.dataForm.department = data.qcGroupMember.department
              this.dataForm.position = data.qcGroupMember.position
              this.dataForm.team = data.qcGroupMember.team
              this.dataForm.participationDate = data.qcGroupMember.participationDate
              this.dataForm.topic = data.qcGroupMember.topic
              this.dataForm.roleInTopic = data.qcGroupMember.roleInTopic
              this.dataForm.deleteFlag = data.qcGroupMember.deleteFlag
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
            url: this.$http.adornUrl(`/qcMembers/qcGroupMember/${!this.dataForm.qcgmId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'qcgmId': this.dataForm.qcgmId || undefined,
              'name': this.dataForm.name,
              'gender': this.dataForm.gender,
              'telNumber': this.dataForm.telNumber,
              'number': this.dataForm.number,
              'education': this.dataForm.education,
              'department': this.dataForm.department,
              'position': this.dataForm.position,
              'team': this.dataForm.team,
              'participationDate': this.dataForm.participationDate,
              'topic': this.dataForm.topic,
              'roleInTopic': this.dataForm.roleInTopic,
              'deleteFlag': this.dataForm.deleteFlag
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
