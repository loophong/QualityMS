<template>
  <el-dialog
    :title="!dataForm.indicatorId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="指标名称" prop="indicatorName">
      <el-select v-model="dataForm.indicatorName" placeholder="请选择指标名称" filterable :disabled="!!dataForm.indicatorId">  <!-- 当 indicatorId 存在时禁用选择 -->
        <el-option v-for="field in indicatorDictionaryList" :key="field.indicatorId" :value="field.indicatorName">
          {{ field.indicatorName }}
        </el-option>
      </el-select>
    </el-form-item>
<!--    <el-form-item label="指标目标值" prop="indicatorValue">-->
<!--      <el-input v-model="dataForm.indicatorValue" placeholder="指标值"></el-input>-->
<!--    </el-form-item>-->
      <el-form-item label="指标完成值" prop="indicatorValue">
        <el-input v-model="dataForm.indicatorActualValue" placeholder="指标完成值"></el-input>
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
      <el-form-item label="年月" prop="yearMonth" >
        <el-date-picker
          v-model="dataForm.yearMonth"
          type="month"
          placeholder="选择月份"
          format="yyyy-MM"
          value-format="yyyy-MM"
          :disabled="!!dataForm.indicatorId"
          :picker-options="pickerOptions"
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
  import http from "../../../../utils/httpRequest";

  export default {
    data () {
      return {
        pickerOptions: {
          disabledDate: time => {
            const now = new Date();
            return time.getTime() > now.getTime();
          }
        },
        indicatorDictionaryList: {},
        visible: false,
        // 查询参数列表
        queryParams: {
          indicatorName: '',
          indicatorValue: '',
          indicatorActualValue: '',
          indicatorValueUpperBound: '',
          indicatorValueLowerBound: '',
          assessmentDepartment: '',
          managementDepartment: '',
          indicatorDefinition: '',
          indicatorClassification: '',
          indicatorParentNode: '',
          indicatorCreatTime: '',
          yearMonth: '',
          indicatorState: '',
          finishedFlag: ''
        },
        dataForm: {
          indicatorId: 0,
          indicatorName: '',
          indicatorValue: '',
          indicatorActualValue: '',
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
          indicatorChildNode: '',
          finishedFlag: ''
        },
        dataRule: {
          indicatorName: [
            { required: true, message: '指标名称不能为空', trigger: 'blur' }
          ],
          indicatorActualValue: [
            { required: true, message: '指标值不能为空', trigger: 'blur' }
          ],
          yearMonth: [
            { required: true, message: '年月不能为空', trigger: 'blur' }
          ],
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
      //修改时初始化数据（根据id判断：id=1为修改）
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
                this.dataForm.indicatorActualValue = data.indicatorIndicatorSummary.indicatorActualValue
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
                this.dataForm.finishedFlag = data.indicatorIndicatorSummary.finishedFlag
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
      validateIndicatorValue() {

      },
      // 表单提交前校验
      // 表单提交
      dataFormSubmit() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            // 判断是新增还是修改
            const isCreate = !this.dataForm.indicatorId;
            if (isCreate) {
              // 提交前校验年月是否已存在
              this.$http({
                url: this.$http.adornUrl('/indicator/indicatorindicatorsummary/list'),
                method: 'get',
                params: this.$http.adornParams({
                  'page': this.pageIndex,
                  'limit': this.pageSize,
                  'key': {
                    'indicatorName': this.dataForm.indicatorName,
                    'yearMonth': this.dataForm.yearMonth
                  }
                })
              }).then(({ data }) => {
                if (data && data.code === 0 && data.page.totalCount === 0) {
                  this.submitForm();
                } else {
                  this.$message.error(`该月份(${this.dataForm.yearMonth})已有数据，无法重复添加`);
                }
              });
            } else {
              // 直接提交修改
              this.submitForm();
            }
          }
        });
      },
      // 提交表单
      submitForm() {
        this.queryParams.indicatorName = this.dataForm.indicatorName
        console.log('this.queryParams===>',this.queryParams)
        //按指标名称查询
        this.$http({
          url: this.$http.adornUrl('/indicator/indicatordictionary/querylist'),
          method: 'get',
          params: this.$http.adornParams({
            'key': this.queryParams
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            console.log("data33333333====>",data)
            // 设置当前时间
            this.dataForm.indicatorCreatTime = this.setCurrentTime();
            this.dataForm.indicatorValue = data.page.list[0].indicatorPlannedValue
            this.dataForm.assessmentDepartment = data.page.list[0].assessmentDepartment
            this.dataForm.managementDepartment = data.page.list[0].managementDepartment
            this.dataForm.indicatorClassification = data.page.list[0].indicatorClassification
            console.log("this.dataForm=====>",this.dataForm);
            if(data.page.list[0].indicatorBoundFlag === 1) {
              if(this.dataForm.indicatorActualValue <= this.dataForm.indicatorValue)
                this.dataForm.finishedFlag = 1
              else this.dataForm.finishedFlag = 0
            }
            else if(data.page.list[0].indicatorBoundFlag === 0) {
              if(this.dataForm.indicatorActualValue >= this.dataForm.indicatorValue)
                this.dataForm.finishedFlag = 1
              else this.dataForm.finishedFlag = 0
            }
            this.queryParams = []
          }
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`/indicator/indicatorindicatorsummary/${!this.dataForm.indicatorId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'indicatorId': this.dataForm.indicatorId || undefined,
              'indicatorName': this.dataForm.indicatorName,
              'indicatorValue': this.dataForm.indicatorValue,
              'indicatorActualValue': this.dataForm.indicatorActualValue,
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
              'yearMonth': this.dataForm.yearMonth,
              'finishedFlag': this.dataForm.finishedFlag
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
        })
      },
    }
  }
</script>
