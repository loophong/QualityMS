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
          filterable
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
      <el-form-item v-for="(keyElement, index) in dataFormLists.keyElements" :key="`keyElement-${index}`" :label="'关键要素 ' + (index + 1)" :prop="'keyElements.' + index">
        <el-select v-model="dataFormLists.keyElements[index]" placeholder="请选择">
          <el-option label="测" value="测"></el-option>
          <el-option label="法" value="法"></el-option>
          <el-option label="环" value="环"></el-option>
          <el-option label="机" value="机"></el-option>
          <el-option label="料" value="料"></el-option>
          <el-option label="人" value="人"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-for="(mode, index) in dataFormLists.potentialFailureMode" :key="`mode-${index}`" :label="'潜在失效模式 ' + (index + 1)" :prop="'potentialFailureMode.' + index">
        <el-input v-model="dataFormLists.potentialFailureMode[index]" placeholder="请输入潜在失效模式"></el-input>
      </el-form-item>
      <el-form-item v-for="(consequences, index) in dataFormLists.potentialFailureConsequences" :key="`consequences-${index}`" :label="'潜在失效后果 ' + (index + 1)" :prop="'potentialFailureConsequences.' + index">
        <el-input v-model="dataFormLists.potentialFailureConsequences[index]" placeholder="请输入潜在失效后果"></el-input>
      </el-form-item>
      <el-form-item v-for="(product, index) in dataFormLists.involvedProduct" :key="`product-${index}`" :label="'涉及产品 ' + (index + 1)" :prop="'involvedProduct.' + index">
        <el-input v-model="dataFormLists.involvedProduct[index]" placeholder="请输入涉及产品"></el-input>
      </el-form-item>
      <el-form-item v-for="(name, index) in dataFormLists.processName" :key="`name-${index}`" :label="'过程名称 ' + (index + 1)" :prop="'processName.' + index">
        <el-input v-model="dataFormLists.processName[index]" placeholder="请输入过程名称"></el-input>
      </el-form-item>
      <el-form-item v-for="(position, index) in dataFormLists.controlPosition" :key="`position-${index}`" :label="'控制位置 ' + (index + 1)" :prop="'controlPosition.' + index">
        <el-input v-model="dataFormLists.controlPosition[index]" placeholder="请输入控制位置"></el-input>
      </el-form-item>
      <el-form-item v-for="(personnel, index) in dataFormLists.controlPersonnel" :key="`personnel-${index}`" :label="'控制人员 ' + (index + 1)" :prop="'controlPersonnel.' + index">
        <el-input v-model="dataFormLists.controlPersonnel[index]" placeholder="请输入控制人员"></el-input>
      </el-form-item>
      <el-form-item v-for="(method, index) in dataFormLists.controlMethod" :key="`method-${index}`" :label="'控制方法 ' + (index + 1)" :prop="'controlMethod.' + index">
        <el-input v-model="dataFormLists.controlMethod[index]" placeholder="请输入控制方法"></el-input>
      </el-form-item>
      <el-form-item v-for="(technique, index) in dataFormLists.evaluationMeasurementTechnique" :key="`technique-${index}`" :label="'评价测量技术 ' + (index + 1)" :prop="'evaluationMeasurementTechnique.' + index">
        <el-input v-model="dataFormLists.evaluationMeasurementTechnique[index]" placeholder="请输入评价测量技术"></el-input>
      </el-form-item>
      <el-form-item v-for="(size, index) in dataFormLists.sampleSize" :key="`size-${index}`" :label="'样本大小 ' + (index + 1)" :prop="'sampleSize.' + index">
        <el-input v-model="dataFormLists.sampleSize[index]" placeholder="请输入样本大小"></el-input>
      </el-form-item>
      <el-form-item v-for="(frequency, index) in dataFormLists.samplingFrequency" :key="`frequency-${index}`" :label="'抽样频率 ' + (index + 1)" :prop="'samplingFrequency.' + index">
        <el-input v-model="dataFormLists.samplingFrequency[index]" placeholder="请输入抽样频率"></el-input>
      </el-form-item>
      <el-form-item v-for="(list, index) in dataFormLists.controlList" :key="`list-${index}`" :label="'控制清单 ' + (index + 1)" :prop="'controlList.' + index">
        <el-input v-model="dataFormLists.controlList[index]" placeholder="请输入控制清单"></el-input>
      </el-form-item>
      <el-form-item v-if="!dataForm.keyIndicatorId">
        <el-button type="primary" @click="addKeyControl">新增一组管控措施</el-button>
      </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose()">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>

</template>

<script>
  export default {
    data () {
      return {
        keyElementsList: [], // 关键要素列表
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
          controlList: '',
          storageFlag: ''
        },
        // 新增一组管控措施
        dataFormLists: {
          keyElements: [],
          potentialFailureMode: [],
          potentialFailureConsequences: [],
          involvedProduct: [],
          processName: [],
          controlPosition: [],
          controlPersonnel: [],
          controlMethod: [],
          evaluationMeasurementTechnique: [],
          sampleSize: [],
          samplingFrequency: [],
          controlList: [],
        },
        dataRule: {
          indicatorName: [
            { required: true, message: '指标名称不能为空', trigger: 'blur' }
          ],
          indicatorClassification: [
            { required: true, message: '指标分级不能为空', trigger: 'blur' }
          ],
          // assessmentDepartment: [
          //   { required: true, message: '考核部门不能为空', trigger: 'blur' }
          // ],
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
          this.dataForm.indicatorId = selectedIndicator.indicatorId;
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
                this.dataFormLists.keyElements[0] = data.indicatorKeyIndicators.keyElements
                this.dataFormLists.potentialFailureMode[0] = data.indicatorKeyIndicators.potentialFailureMode
                this.dataFormLists.potentialFailureConsequences[0] = data.indicatorKeyIndicators.potentialFailureConsequences
                this.dataFormLists.involvedProduct[0] = data.indicatorKeyIndicators.involvedProduct
                this.dataFormLists.processName[0] = data.indicatorKeyIndicators.processName
                this.dataFormLists.controlPosition[0] = data.indicatorKeyIndicators.controlPosition
                this.dataFormLists.controlPersonnel[0] = data.indicatorKeyIndicators.controlPersonnel
                this.dataFormLists.controlMethod[0] = data.indicatorKeyIndicators.controlMethod
                this.dataFormLists.evaluationMeasurementTechnique[0] = data.indicatorKeyIndicators.evaluationMeasurementTechnique
                this.dataFormLists.sampleSize[0] = data.indicatorKeyIndicators.sampleSize
                this.dataFormLists.samplingFrequency[0] = data.indicatorKeyIndicators.samplingFrequency
                this.dataFormLists.controlList[0] = data.indicatorKeyIndicators.controlList
                this.dataFormLists.storageFlag[0] = data.indicatorKeyIndicators.storageFlag
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          console.log("dataFormSubmit=====>",this.dataForm)
          if (valid) {
            for (let i = 0; i < this.dataFormLists.keyElements.length; i++) {
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
                  'keyElements': this.dataFormLists.keyElements[i],
                  'potentialFailureMode': this.dataFormLists.potentialFailureMode[i] || '',
                  'potentialFailureConsequences': this.dataFormLists.potentialFailureConsequences[i] || '',
                  'involvedProduct': this.dataFormLists.involvedProduct[i] || '',
                  'processName': this.dataFormLists.processName[i] || '',
                  'controlPosition': this.dataFormLists.controlPosition[i] || '',
                  'controlPersonnel': this.dataFormLists.controlPersonnel[i] || '',
                  'controlMethod': this.dataFormLists.controlMethod[i] || '',
                  'evaluationMeasurementTechnique': this.dataFormLists.evaluationMeasurementTechnique[i] || '',
                  'sampleSize': this.dataFormLists.sampleSize[i] || '',
                  'samplingFrequency': this.dataFormLists.samplingFrequency[i] || '',
                  'controlList': this.dataFormLists.controlList[i] || ''
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
          }
        })
      },
      handleClose() {
        this.visible = false;
        this.dataFormLists = {
          keyElements: [],
          potentialFailureMode: [],
          potentialFailureConsequences: [],
          involvedProduct: [],
          processName: [],
          controlPosition: [],
          controlPersonnel: [],
          controlMethod: [],
          evaluationMeasurementTechnique: [],
          sampleSize: [],
          samplingFrequency: [],
          controlList: [],
        };
      },
      addKeyControl() {
        this.dataFormLists.keyElements.push('');
        this.dataFormLists.potentialFailureMode.push('');
        this.dataFormLists.potentialFailureConsequences.push('');
        this.dataFormLists.involvedProduct.push('');
        this.dataFormLists.processName.push('');
        this.dataFormLists.controlPosition.push('');
        this.dataFormLists.controlPersonnel.push('');
        this.dataFormLists.controlMethod.push('');
        this.dataFormLists.evaluationMeasurementTechnique.push('');
        this.dataFormLists.sampleSize.push('');
        this.dataFormLists.samplingFrequency.push('');
        this.dataFormLists.controlList.push('');
        console.log("this.dataFormLists===>",this.dataFormLists);
      }
    }
  }
</script>
