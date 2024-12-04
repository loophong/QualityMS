<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="接收区域" prop="acceptanceRegion">
      <el-input v-model="dataForm.acceptanceRegion" placeholder="接收区域"></el-input>
    </el-form-item>
    <el-form-item label="工时" prop="workTime">
      <el-input v-model="dataForm.workTime" placeholder="工时"></el-input>
    </el-form-item>
    <el-form-item label="正态分布值,按照公式计算" prop="ptdValue">
      <el-input v-model="dataForm.ptdValue" placeholder="正态分布值,按照公式计算"></el-input>
    </el-form-item>
    <el-form-item label="频率" prop="frequency">
      <el-input v-model="dataForm.frequency" placeholder="频率"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="craetTime">
      <el-input v-model="dataForm.craetTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="创建人" prop="createPeople">
      <el-input v-model="dataForm.createPeople" placeholder="创建人"></el-input>
    </el-form-item>
    <el-form-item label="逻辑删除字段" prop="deletFlag">
      <el-input v-model="dataForm.deletFlag" placeholder="逻辑删除字段"></el-input>
    </el-form-item>
    <el-form-item label="备用字段" prop="spare">
      <el-input v-model="dataForm.spare" placeholder="备用字段"></el-input>
    </el-form-item>
    <el-form-item label="数据导入日期" prop="dataImportTime">
      <el-input v-model="dataForm.dataImportTime" placeholder="数据导入日期"></el-input>
    </el-form-item>
    <el-form-item label="数据更新时间" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="数据更新时间"></el-input>
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
          id: 0,
          acceptanceRegion: '',
          workTime: '',
          ptdValue: '',
          frequency: '',
          craetTime: '',
          createPeople: '',
          deletFlag: '',
          spare: '',
          dataImportTime: '',
          updateTime: ''
        },
        dataRule: {
          acceptanceRegion: [
            { required: true, message: '接收区域不能为空', trigger: 'blur' }
          ],
          workTime: [
            { required: true, message: '工时不能为空', trigger: 'blur' }
          ],
          ptdValue: [
            { required: true, message: '正态分布值,按照公式计算不能为空', trigger: 'blur' }
          ],
          frequency: [
            { required: true, message: '频率不能为空', trigger: 'blur' }
          ],
          craetTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          createPeople: [
            { required: true, message: '创建人不能为空', trigger: 'blur' }
          ],
          deletFlag: [
            { required: true, message: '逻辑删除字段不能为空', trigger: 'blur' }
          ],
          spare: [
            { required: true, message: '备用字段不能为空', trigger: 'blur' }
          ],
          dataImportTime: [
            { required: true, message: '数据导入日期不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '数据更新时间不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/spc/spcptd/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.acceptanceRegion = data.spcPtd.acceptanceRegion
                this.dataForm.workTime = data.spcPtd.workTime
                this.dataForm.ptdValue = data.spcPtd.ptdValue
                this.dataForm.frequency = data.spcPtd.frequency
                this.dataForm.craetTime = data.spcPtd.craetTime
                this.dataForm.createPeople = data.spcPtd.createPeople
                this.dataForm.deletFlag = data.spcPtd.deletFlag
                this.dataForm.spare = data.spcPtd.spare
                this.dataForm.dataImportTime = data.spcPtd.dataImportTime
                this.dataForm.updateTime = data.spcPtd.updateTime
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
              url: this.$http.adornUrl(`/spc/spcptd/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'acceptanceRegion': this.dataForm.acceptanceRegion,
                'workTime': this.dataForm.workTime,
                'ptdValue': this.dataForm.ptdValue,
                'frequency': this.dataForm.frequency,
                'craetTime': this.dataForm.craetTime,
                'createPeople': this.dataForm.createPeople,
                'deletFlag': this.dataForm.deletFlag,
                'spare': this.dataForm.spare,
                'dataImportTime': this.dataForm.dataImportTime,
                'updateTime': this.dataForm.updateTime
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
