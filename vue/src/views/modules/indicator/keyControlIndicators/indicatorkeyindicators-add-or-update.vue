<template>
  <el-dialog
    :title="!dataForm.keyIndicatorId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="指标名称" prop="indicatorName">
        <el-select
          v-model="dataForm.indicatorName"
          placeholder="请选择指标名称"
          :disabled="!!dataForm.indicatorId"
          @change="onIndicatorNameChange"> <!-- 当 indicatorId 存在时禁用选择 -->
          <el-option v-for="field in indicatorDictionaryList" :key="field.indicatorId" :value="field.indicatorName">
            {{ field.indicatorName }}
          </el-option>
        </el-select>
      </el-form-item>
    <el-form-item label="指标分级" prop="indicatorClassification">
      <el-input v-model="dataForm.indicatorClassification" placeholder="指标分级" :disabled="true"></el-input>
    </el-form-item>
    <el-form-item label="考核部门" prop="assessmentDepartment">
      <el-input v-model="dataForm.assessmentDepartment" placeholder="考核部门" :disabled="true"></el-input>
    </el-form-item>
    <el-form-item label="管理部门" prop="sourceDepartment">
      <el-input v-model="dataForm.sourceDepartment" placeholder="管理部门" :disabled="true"></el-input>
    </el-form-item>
    <el-form-item label="管理内容" prop="managementContent">
      <el-input v-model="dataForm.managementContent" placeholder="管理内容"></el-input>
    </el-form-item>
      <el-form-item label="是否管理失控" prop="isManagementOutOfControl">
        <el-select v-model="dataForm.isManagementOutOfControl" placeholder="请选择">
          <el-option label="是" value="是"></el-option>
          <el-option label="否" value="否"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否需要理攻关" prop="isNeedsControl">
        <el-select v-model="dataForm.isNeedsControl" placeholder="请选择">
          <el-option label="是" value="是"></el-option>
          <el-option label="否" value="否"></el-option>
        </el-select>
      </el-form-item>
    <el-form-item label="关键要素" prop="keyElements">
      <el-select v-model="dataForm.keyElements" placeholder="请选择">
        <el-option label="测" value="测"></el-option>
        <el-option label="法" value="法"></el-option>
        <el-option label="环" value="环"></el-option>
        <el-option label="机" value="机"></el-option>
        <el-option label="料" value="料"></el-option>
        <el-option label="人" value="人"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="潜在失效模式" prop="potentialFailureMode">
      <el-input v-model="dataForm.potentialFailureMode" placeholder="潜在失效模式"></el-input>
    </el-form-item>
    <el-form-item label="潜在失效后果" prop="potentialFailureConsequences">
      <el-input v-model="dataForm.potentialFailureConsequences" placeholder="潜在失效后果"></el-input>
    </el-form-item>
    <el-form-item label="涉及产品" prop="involvedProduct">
      <el-input v-model="dataForm.involvedProduct" placeholder="涉及产品"></el-input>
    </el-form-item>
    <el-form-item label="过程名称" prop="processName">
      <el-input v-model="dataForm.processName" placeholder="过程名称"></el-input>
    </el-form-item>
    <el-form-item label="控制位置" prop="controlPosition">
      <el-input v-model="dataForm.controlPosition" placeholder="控制位置"></el-input>
    </el-form-item>
    <el-form-item label="控制人员" prop="controlPersonnel">
      <el-input v-model="dataForm.controlPersonnel" placeholder="控制人员"></el-input>
    </el-form-item>
    <el-form-item label="控制方法" prop="controlMethod">
      <el-input v-model="dataForm.controlMethod" placeholder="控制方法"></el-input>
    </el-form-item>
    <el-form-item label="评价测量技术" prop="evaluationMeasurementTechnique">
      <el-input v-model="dataForm.evaluationMeasurementTechnique" placeholder="评价测量技术"></el-input>
    </el-form-item>
    <el-form-item label="样本大小" prop="sampleSize">
      <el-input v-model="dataForm.sampleSize" placeholder="样本大小"></el-input>
    </el-form-item>
    <el-form-item label="抽样频率" prop="samplingFrequency">
      <el-input v-model="dataForm.samplingFrequency" placeholder="抽样频率"></el-input>
    </el-form-item>
    <el-form-item label="控制清单" prop="controlList">
      <el-input v-model="dataForm.controlList" placeholder="控制清单"></el-input>
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
        indicatorDictionaryList: {},
        visible: false,
        dataForm: {
          keyIndicatorId: 0,
          indicatorId: '',
          indicatorName: '',
          indicatorClassification: '',
          assessmentDepartment: '',
          sourceDepartment: '',
          managementContent: '',
          isManagementOutOfControl: '',
          isNeedsControl: '',
          keyElements: '',
          potentialFailureMode: '',
          potentialFailureConsequences: '',
          involvedProduct: '',
          processName: '',
          controlPosition: '',
          controlPersonnel: '',
          controlMethod: '',
          evaluationMeasurementTechnique: '',
          sampleSize: '',
          samplingFrequency: '',
          controlList: ''
        },
        dataRule: {
          indicatorName: [
            { required: true, message: '指标名称不能为空', trigger: 'blur' }
          ],
          indicatorClassification: [
            { required: true, message: '指标分级不能为空', trigger: 'blur' }
          ],
          assessmentDepartment: [
            { required: true, message: '考核部门不能为空', trigger: 'blur' }
          ],
          sourceDepartment: [
            { required: true, message: '管理部门不能为空', trigger: 'blur' }
          ],
          managementContent: [
            { required: true, message: '管理内容不能为空', trigger: 'blur' }
          ],
          isManagementOutOfControl: [
            { required: true, message: '是否管理失控不能为空', trigger: 'blur' }
          ],
          isNeedsControl: [
            { required: true, message: '是否需要理攻关不能为空', trigger: 'blur' }
          ],
          // keyElements: [
          //   { required: true, message: '关键要素不能为空', trigger: 'blur' }
          // ],
          // potentialFailureMode: [
          //   { required: true, message: '潜在失效模式不能为空', trigger: 'blur' }
          // ],
          // potentialFailureConsequences: [
          //   { required: true, message: '潜在失效后果不能为空', trigger: 'blur' }
          // ],
          // involvedProduct: [
          //   { required: true, message: '涉及产品不能为空', trigger: 'blur' }
          // ],
          // processName: [
          //   { required: true, message: '过程名称不能为空', trigger: 'blur' }
          // ],
          // controlPosition: [
          //   { required: true, message: '控制位置不能为空', trigger: 'blur' }
          // ],
          // controlPersonnel: [
          //   { required: true, message: '控制人员不能为空', trigger: 'blur' }
          // ],
          // controlMethod: [
          //   { required: true, message: '控制方法不能为空', trigger: 'blur' }
          // ],
          // evaluationMeasurementTechnique: [
          //   { required: true, message: '评价测量技术不能为空', trigger: 'blur' }
          // ],
          // sampleSize: [
          //   { required: true, message: '样本大小不能为空', trigger: 'blur' }
          // ],
          // samplingFrequency: [
          //   { required: true, message: '抽样频率不能为空', trigger: 'blur' }
          // ],
          // controlList: [
          //   { required: true, message: '控制清单不能为空', trigger: 'blur' }
          // ]
        }
      }
    },
    created() {
      this.getIndicatorDictionaryList()
    },
    methods: {
      onIndicatorNameChange(selectedName) {
        // 根据选中的指标名称查找对应的指标信息
        const selectedIndicator = this.indicatorDictionaryList.find(field => field.indicatorName === selectedName);

        if (selectedIndicator) {
          // 设置其他字段的值
          this.dataForm.indicatorClassification = selectedIndicator.indicatorClassification;
          this.dataForm.assessmentDepartment = selectedIndicator.assessmentDepartment;
          this.dataForm.sourceDepartment = selectedIndicator.managementDepartment;
        }
      },
      //获取全部指标列表
      getIndicatorDictionaryList() {
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': 10000,
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            console.log("data2====>",data)
            this.indicatorDictionaryList = data.page.list
          } else {
            this.dataList = []
          }
        })
      },
      init (id) {
        this.dataForm.keyIndicatorId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.keyIndicatorId) {
            this.$http({
              url: this.$http.adornUrl(`/indicator/indicatorkeyindicators/info/${this.dataForm.keyIndicatorId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.indicatorId = data.indicatorKeyIndicators.indicatorId
                this.dataForm.indicatorName = data.indicatorKeyIndicators.indicatorName
                this.dataForm.indicatorClassification = data.indicatorKeyIndicators.indicatorClassification
                this.dataForm.assessmentDepartment = data.indicatorKeyIndicators.assessmentDepartment
                this.dataForm.sourceDepartment = data.indicatorKeyIndicators.sourceDepartment
                this.dataForm.managementContent = data.indicatorKeyIndicators.managementContent
                this.dataForm.isManagementOutOfControl = data.indicatorKeyIndicators.isManagementOutOfControl
                this.dataForm.isNeedsControl = data.indicatorKeyIndicators.isNeedsControl
                this.dataForm.keyElements = data.indicatorKeyIndicators.keyElements
                this.dataForm.potentialFailureMode = data.indicatorKeyIndicators.potentialFailureMode
                this.dataForm.potentialFailureConsequences = data.indicatorKeyIndicators.potentialFailureConsequences
                this.dataForm.involvedProduct = data.indicatorKeyIndicators.involvedProduct
                this.dataForm.processName = data.indicatorKeyIndicators.processName
                this.dataForm.controlPosition = data.indicatorKeyIndicators.controlPosition
                this.dataForm.controlPersonnel = data.indicatorKeyIndicators.controlPersonnel
                this.dataForm.controlMethod = data.indicatorKeyIndicators.controlMethod
                this.dataForm.evaluationMeasurementTechnique = data.indicatorKeyIndicators.evaluationMeasurementTechnique
                this.dataForm.sampleSize = data.indicatorKeyIndicators.sampleSize
                this.dataForm.samplingFrequency = data.indicatorKeyIndicators.samplingFrequency
                this.dataForm.controlList = data.indicatorKeyIndicators.controlList
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
              url: this.$http.adornUrl(`/indicator/indicatorkeyindicators/${!this.dataForm.keyIndicatorId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'keyIndicatorId': this.dataForm.keyIndicatorId || undefined,
                'indicatorId': this.dataForm.indicatorId,
                'indicatorName': this.dataForm.indicatorName,
                'indicatorClassification': this.dataForm.indicatorClassification,
                'assessmentDepartment': this.dataForm.assessmentDepartment,
                'sourceDepartment': this.dataForm.sourceDepartment,
                'managementContent': this.dataForm.managementContent,
                'isManagementOutOfControl': this.dataForm.isManagementOutOfControl,
                'isNeedsControl': this.dataForm.isNeedsControl,
                'keyElements': this.dataForm.keyElements,
                'potentialFailureMode': this.dataForm.potentialFailureMode,
                'potentialFailureConsequences': this.dataForm.potentialFailureConsequences,
                'involvedProduct': this.dataForm.involvedProduct,
                'processName': this.dataForm.processName,
                'controlPosition': this.dataForm.controlPosition,
                'controlPersonnel': this.dataForm.controlPersonnel,
                'controlMethod': this.dataForm.controlMethod,
                'evaluationMeasurementTechnique': this.dataForm.evaluationMeasurementTechnique,
                'sampleSize': this.dataForm.sampleSize,
                'samplingFrequency': this.dataForm.samplingFrequency,
                'controlList': this.dataForm.controlList
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
