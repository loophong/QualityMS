<template>
  <el-dialog
    :title="!dataForm.indicatorId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="指标名称" prop="indicatorName">
      <el-select v-model="dataForm.indicatorName" placeholder="请选择指标名称">
        <el-option v-for="field in indicatorDictionaryList" :key="field.indicatorId" :value="field.indicatorName">
          {{ field.indicatorName }}
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="指标值" prop="indicatorValue">
      <el-input v-model="dataForm.indicatorValue" placeholder="指标值"></el-input>
    </el-form-item>
<!--      <el-form-item label="管理部门" prop="managementDepartment">-->
<!--        <el-input v-model="dataForm.managementDepartment" placeholder="管理部门"></el-input>-->
<!--      </el-form-item>-->
<!--    <el-form-item label="考核部门" prop="assessmentDepartment">-->
<!--      <el-input v-model="dataForm.assessmentDepartment" placeholder="考核部门"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="指标定义" prop="indicatorDefinition">-->
<!--      <el-input v-model="dataForm.indicatorDefinition" placeholder="指标定义"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="指标分级" prop="indicatorClassification">-->
<!--      <el-input v-model="dataForm.indicatorClassification" placeholder="指标分级"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="管理内容（现状分析）" prop="managementContentCurrentAnalysis">-->
<!--      <el-input v-model="dataForm.managementContentCurrentAnalysis" placeholder="管理内容（现状分析）"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="数据ID" prop="dataId">-->
<!--      <el-input v-model="dataForm.dataId" placeholder="数据ID"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="来源部门" prop="sourceDepartment">-->
<!--      <el-input v-model="dataForm.sourceDepartment" placeholder="来源部门"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="收集方法" prop="collectionMethod">-->
<!--      <el-input v-model="dataForm.collectionMethod" placeholder="收集方法"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="收集频次" prop="collectionFrequency">-->
<!--      <el-input v-model="dataForm.collectionFrequency" placeholder="收集频次"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="关联计划id" prop="planId">-->
<!--      <el-input v-model="dataForm.planId" placeholder="关联计划id"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="关联任务id" prop="taskId">-->
<!--      <el-input v-model="dataForm.taskId" placeholder="关联任务id"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="指标父节点" prop="indicatorParentNode">-->
<!--      <el-input v-model="dataForm.indicatorParentNode" placeholder="指标父节点"></el-input>-->
<!--    </el-form-item>-->
<!--      <el-form-item label="指标填报时间" prop="indicatorCreatTime">-->
<!--        <el-date-picker-->
<!--          v-model="dataForm.indicatorCreatTime"-->
<!--          type="datetime"-->
<!--          placeholder="选择指标创建时间"-->
<!--          format="yyyy-MM-dd HH:mm:ss"-->
<!--          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--          clearable>-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
      <el-form-item label="年月" prop="yearMonth">
        <el-date-picker
          v-model="dataForm.yearMonth"
          type="month"
          placeholder="选择月份"
          format="yyyy-MM"
          value-format="yyyy-MM"
        ></el-date-picker>
      </el-form-item>

      <!--    <el-form-item label="0表示弃用，1表示使用中" prop="indicatorState">-->
<!--      <el-input v-model="dataForm.indicatorState" placeholder="0表示弃用，1表示使用中"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="指标子节点" prop="indicatorChildNode">-->
<!--      <el-input v-model="dataForm.indicatorChildNode" placeholder="指标子节点"></el-input>-->
<!--    </el-form-item>-->
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
          indicatorId: 0,
          indicatorName: '',
          indicatorValue: '',
          indicatorValueUpperBound: '',
          indicatorValueLowerBound: '',
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
          yearMonth: '',
          indicatorState: '',
          indicatorChildNode: ''
        },
        dataRule: {
          indicatorName: [
            { required: true, message: '指标名称不能为空', trigger: 'blur' }
          ],
          indicatorValue: [
            { required: true, message: '指标值不能为空', trigger: 'blur' }
          ],
          assessmentDepartment: [
            { required: true, message: '考核部门不能为空', trigger: 'blur' }
          ],
          managementDepartment: [
            { required: true, message: '管理部门不能为空', trigger: 'blur' }
          ],
          // indicatorDefinition: [
          //   { required: true, message: '指标定义不能为空', trigger: 'blur' }
          // ],
          // indicatorClassification: [
          //   { required: true, message: '指标分级不能为空', trigger: 'blur' }
          // ],
          managementContentCurrentAnalysis: [
            { required: true, message: '管理内容（现状分析）不能为空', trigger: 'blur' }
          ],
          // dataId: [
          //   { required: true, message: '数据ID不能为空', trigger: 'blur' }
          // ],
          // sourceDepartment: [
          //   { required: true, message: '来源部门不能为空', trigger: 'blur' }
          // ],
          // collectionMethod: [
          //   { required: true, message: '收集方法不能为空', trigger: 'blur' }
          // ],
          // collectionFrequency: [
          //   { required: true, message: '收集频次不能为空', trigger: 'blur' }
          // ],
          // planId: [
          //   { required: true, message: '关联计划id不能为空', trigger: 'blur' }
          // ],
          // taskId: [
          //   { required: true, message: '关联任务id不能为空', trigger: 'blur' }
          // ],
          // indicatorParentNode: [
          //   { required: true, message: '指标父节点不能为空', trigger: 'blur' }
          // ],
          yearMonth: [
            { required: true, message: '年月不能为空', trigger: 'blur' }
          ],
          // indicatorState: [
          //   { required: true, message: '0表示弃用，1表示使用中不能为空', trigger: 'blur' }
          // ],
          // indicatorChildNode: [
          //   { required: true, message: '指标子节点不能为空', trigger: 'blur' }
          // ]
        }
      }
    },
    created() {
      this.getIndicatorDictionaryList()
    },
    methods: {
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
        this.dataForm.indicatorId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.indicatorId) {
            this.$http({
              url: this.$http.adornUrl(`/indicator/indicatorindicatorsummary/info/${this.dataForm.indicatorId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.indicatorName = data.indicatorIndicatorSummary.indicatorName
                this.dataForm.indicatorValue = data.indicatorIndicatorSummary.indicatorValue
                this.dataForm.assessmentDepartment = data.indicatorIndicatorSummary.assessmentDepartment
                this.dataForm.managementDepartment = data.indicatorIndicatorSummary.managementDepartment
                this.dataForm.indicatorDefinition = data.indicatorIndicatorSummary.indicatorDefinition
                this.dataForm.indicatorClassification = data.indicatorIndicatorSummary.indicatorClassification
                this.dataForm.managementContentCurrentAnalysis = data.indicatorIndicatorSummary.managementContentCurrentAnalysis
                this.dataForm.dataId = data.indicatorIndicatorSummary.dataId
                this.dataForm.sourceDepartment = data.indicatorIndicatorSummary.sourceDepartment
                this.dataForm.collectionMethod = data.indicatorIndicatorSummary.collectionMethod
                this.dataForm.collectionFrequency = data.indicatorIndicatorSummary.collectionFrequency
                this.dataForm.planId = data.indicatorIndicatorSummary.planId
                this.dataForm.taskId = data.indicatorIndicatorSummary.taskId
                this.dataForm.indicatorParentNode = data.indicatorIndicatorSummary.indicatorParentNode
                this.dataForm.indicatorCreatTime = data.indicatorIndicatorSummary.indicatorCreatTime
                this.dataForm.indicatorState = data.indicatorIndicatorSummary.indicatorState
                this.dataForm.indicatorChildNode = data.indicatorIndicatorSummary.indicatorChildNode
                this.dataForm.yearMonth = data.indicatorIndicatorSummary.yearMonth
              }
            })
          }
        })
      },
      //设置当前时间，格式为yyyy-MM-dd HH:mm:ss
      setCurrentTime() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');

        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      },
      // 表单提交
      dataFormSubmit () {
        // 设置当前时间
        this.dataForm.indicatorCreatTime = this.setCurrentTime();
        console.log("this.dataForm.indicatorCreatTime=====>",this.dataForm.indicatorCreatTime);
        console.log("new Date()=====>",new Date());

        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/indicator/indicatorindicatorsummary/${!this.dataForm.indicatorId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'indicatorId': this.dataForm.indicatorId || undefined,
                'indicatorName': this.dataForm.indicatorName,
                'indicatorValue': this.dataForm.indicatorValue,
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
                'indicatorChildNode': this.dataForm.indicatorChildNode,
                'yearMonth': this.dataForm.yearMonth
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
