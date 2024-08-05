<template>
  <el-dialog
    :title="!dataForm.indicatorId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="指标名称" prop="indicatorName">
      <el-input v-model="dataForm.indicatorName" placeholder="指标名称"></el-input>
    </el-form-item>
    <el-form-item label="考核部门" prop="assessmentDepartment">
      <el-input v-model="dataForm.assessmentDepartment" placeholder="考核部门"></el-input>
    </el-form-item>
    <el-form-item label="管理部门" prop="managementDepartment">
      <el-input v-model="dataForm.managementDepartment" placeholder="管理部门"></el-input>
    </el-form-item>
    <el-form-item label="指标公式定义" prop="indicatorDefinition">
      <el-input v-model="dataForm.indicatorDefinition" placeholder="指标定义"></el-input>
    </el-form-item>
    <el-form-item label="指标分级" prop="indicatorClassification">
      <el-input v-model="dataForm.indicatorClassification" placeholder="指标分级"></el-input>
    </el-form-item>
    <el-form-item label="管理内容（现状分析）" prop="managementContentCurrentAnalysis">
      <el-input v-model="dataForm.managementContentCurrentAnalysis" placeholder="管理内容（现状分析）"></el-input>
    </el-form-item>
    <!-- <el-form-item label="数据ID" prop="dataId">
      <el-input v-model="dataForm.dataId" placeholder="数据ID"></el-input>
    </el-form-item>
    <el-form-item label="来源部门" prop="sourceDepartment">
      <el-input v-model="dataForm.sourceDepartment" placeholder="来源部门"></el-input>
    </el-form-item>
    <el-form-item label="收集方法" prop="collectionMethod">
      <el-input v-model="dataForm.collectionMethod" placeholder="收集方法"></el-input>
    </el-form-item>
    <el-form-item label="收集频次" prop="collectionFrequency">
      <el-input v-model="dataForm.collectionFrequency" placeholder="收集频次"></el-input>
    </el-form-item> -->
    <el-form-item label="关联计划" prop="planId">
      <el-input v-model="dataForm.planId" placeholder="关联计划"></el-input>
    </el-form-item>
    <!-- <el-form-item label="关联任务id" prop="taskId">
      <el-input v-model="dataForm.taskId" placeholder="关联任务id"></el-input>
    </el-form-item> -->
    <el-form-item label="上级指标" prop="indicatorParentNode">
      <el-input v-model="dataForm.indicatorParentNode" placeholder="上级指标"></el-input>
    </el-form-item>
    <el-form-item label="指标创建时间" prop="indicatorCreatTime">
      <el-input v-model="dataForm.indicatorCreatTime" placeholder="指标创建时间"></el-input>
    </el-form-item>
    <!-- <el-form-item label="0表示弃用，1表示使用中" prop="indicatorState">
      <el-input v-model="dataForm.indicatorState" placeholder="0表示弃用，1表示使用中"></el-input>
    </el-form-item> -->
    <!-- <el-form-item label="指标子节点" prop="indicatorChildNode">
      <el-input v-model="dataForm.indicatorChildNode" placeholder="指标子节点"></el-input>
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
    data () {
      return {
        visible: false,
        dataForm: {
          indicatorId: 0,
          indicatorName: '',
          assessmentDepartment: '',
          managementDepartment: '',
          indicatorDefinition: '',
          indicatorClassification: '',
          managementContentCurrentAnalysis: '',
          dataId: '',
          sourceDepartment: '',
          collectionMethod: '',
          collectionFrequency: '',
          planId: '',
          taskId: '',
          indicatorParentNode: '',
          indicatorCreatTime: '',
          indicatorState: '',
          indicatorChildNode: ''
        },
        dataRule: {
          indicatorName: [
            { required: true, message: '指标名称不能为空', trigger: 'blur' }
          ],
          assessmentDepartment: [
            { required: true, message: '考核部门不能为空', trigger: 'blur' }
          ],
          managementDepartment: [
            { required: true, message: '管理部门不能为空', trigger: 'blur' }
          ],
          indicatorDefinition: [
            { required: true, message: '指标定义不能为空', trigger: 'blur' }
          ],
          indicatorClassification: [
            { required: true, message: '指标分级不能为空', trigger: 'blur' }
          ],
          managementContentCurrentAnalysis: [
            { required: true, message: '管理内容（现状分析）不能为空', trigger: 'blur' }
          ],
          dataId: [
            { required: true, message: '数据ID不能为空', trigger: 'blur' }
          ],
          sourceDepartment: [
            { required: true, message: '来源部门不能为空', trigger: 'blur' }
          ],
          collectionMethod: [
            { required: true, message: '收集方法不能为空', trigger: 'blur' }
          ],
          collectionFrequency: [
            { required: true, message: '收集频次不能为空', trigger: 'blur' }
          ],
          planId: [
            { required: true, message: '关联计划id不能为空', trigger: 'blur' }
          ],
          taskId: [
            { required: true, message: '关联任务id不能为空', trigger: 'blur' }
          ],
          indicatorParentNode: [
            { required: true, message: '指标父节点不能为空', trigger: 'blur' }
          ],
          indicatorCreatTime: [
            { required: true, message: '指标创建时间不能为空', trigger: 'blur' }
          ],
          indicatorState: [
            { required: true, message: '0表示弃用，1表示使用中不能为空', trigger: 'blur' }
          ],
          indicatorChildNode: [
            { required: true, message: '指标子节点不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.indicatorId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.indicatorId) {
            this.$http({
              url: this.$http.adornUrl(`/indicator/indicatordictionary/info/${this.dataForm.indicatorId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.indicatorName = data.indicatorDictionary.indicatorName
                this.dataForm.assessmentDepartment = data.indicatorDictionary.assessmentDepartment
                this.dataForm.managementDepartment = data.indicatorDictionary.managementDepartment
                this.dataForm.indicatorDefinition = data.indicatorDictionary.indicatorDefinition
                this.dataForm.indicatorClassification = data.indicatorDictionary.indicatorClassification
                this.dataForm.managementContentCurrentAnalysis = data.indicatorDictionary.managementContentCurrentAnalysis
                this.dataForm.dataId = data.indicatorDictionary.dataId
                this.dataForm.sourceDepartment = data.indicatorDictionary.sourceDepartment
                this.dataForm.collectionMethod = data.indicatorDictionary.collectionMethod
                this.dataForm.collectionFrequency = data.indicatorDictionary.collectionFrequency
                this.dataForm.planId = data.indicatorDictionary.planId
                this.dataForm.taskId = data.indicatorDictionary.taskId
                this.dataForm.indicatorParentNode = data.indicatorDictionary.indicatorParentNode
                this.dataForm.indicatorCreatTime = data.indicatorDictionary.indicatorCreatTime
                this.dataForm.indicatorState = data.indicatorDictionary.indicatorState
                this.dataForm.indicatorChildNode = data.indicatorDictionary.indicatorChildNode
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
              url: this.$http.adornUrl(`/indicator/indicatordictionary/${!this.dataForm.indicatorId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'indicatorId': this.dataForm.indicatorId || undefined,
                'indicatorName': this.dataForm.indicatorName,
                'assessmentDepartment': this.dataForm.assessmentDepartment,
                'managementDepartment': this.dataForm.managementDepartment,
                'indicatorDefinition': this.dataForm.indicatorDefinition,
                'indicatorClassification': this.dataForm.indicatorClassification,
                'managementContentCurrentAnalysis': this.dataForm.managementContentCurrentAnalysis,
                'dataId': this.dataForm.dataId,
                'sourceDepartment': this.dataForm.sourceDepartment,
                'collectionMethod': this.dataForm.collectionMethod,
                'collectionFrequency': this.dataForm.collectionFrequency,
                'planId': this.dataForm.planId,
                'taskId': this.dataForm.taskId,
                'indicatorParentNode': this.dataForm.indicatorParentNode,
                'indicatorCreatTime': this.dataForm.indicatorCreatTime,
                'indicatorState': this.dataForm.indicatorState,
                'indicatorChildNode': this.dataForm.indicatorChildNode
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
