<template>
  <el-dialog :title="!dataForm.indicatorId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="100px">
      <el-form-item label="指标名称" prop="indicatorName">
        <el-input v-model="dataForm.indicatorName" placeholder="请输入指标名称"></el-input>
      </el-form-item>
      <el-form-item label="考核部门" prop="assessmentDepartment">
        <el-input v-model="dataForm.assessmentDepartment" placeholder="请输入考核部门"></el-input>
      </el-form-item>
      <el-form-item label="管理部门" prop="managementDepartment">
        <el-input v-model="dataForm.managementDepartment" placeholder="请输入管理部门"></el-input>
      </el-form-item>

      <el-form-item label="指标公式" prop="indicatorDefinition">
        <el-select v-model="selectedFormula" @change="onFormulaChange" placeholder="请选择指标公式">
          <el-option v-for="formula in formulas" :key="formula.id" :value="formula.template">
            {{ formula.name }}
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 显示占位符并为每个占位符生成下拉框 -->
      <el-form-item v-if="placeholders.length">
        <div v-for="(placeholder, index) in placeholders" :key="index">
          <label>{{ placeholder }}</label>
          <el-select v-model="placeholderMapping[placeholder]" filterable placeholder="请选择字段">
            <el-option v-for="field in dataList" :key="field.dataId" :value="field.dataContent">
              {{ field.dataContent }}
            </el-option>
          </el-select>
        </div>
      </el-form-item>
      <!-- 显示最终生成的公式 -->
      <el-form-item v-if="placeholders.length">
        <p>生成的公式: {{ generatedFormula }}</p>
      </el-form-item>

      <!-- 指标分级 -->
      <el-form-item label="指标分级" prop="indicatorClassification">
        <el-select v-model="dataForm.indicatorClassification" placeholder="请选择指标分级">
          <el-option
            v-for="(item, index) in indicatorClassificationList"
            :key="index"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="指标目标值" prop="indicatorPlannedValue">
        <el-input v-model="dataForm.indicatorPlannedValue" placeholder="请输入指标目标值" style="width: 202px;"></el-input>
        <el-radio-group v-model="dataForm.indicatorBoundFlag" style="margin-left: 20px;">
          <el-radio label="1">上界</el-radio>
          <el-radio label="0">下界</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="上级指标" prop="indicatorParentNode">
        <el-select v-model="dataForm.indicatorParentNode" clearable placeholder="请选择指标名称"  @change="calculateTotalWeight">
          <el-option v-for="field in indicatorDictionaryList" :key="field.indicatorId" :value="field.indicatorName">
            {{ field.indicatorName }}
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="指标权重" prop="weight">
        <div style="display: flex; align-items: center;">
          <el-input
            v-model="dataForm.weight"            style="width: 80px;"
            @input="calculateRemainingWeight"
          ></el-input>
          <span style="margin-left: 5px;">%</span>
          <span style="color: red; margin-left: 10px;" v-if="weightError">{{ weightError }}</span>
        </div>
        <div style="margin-top: 5px;">
          剩余权重: {{ remainingWeight }}%
        </div>
      </el-form-item>
      <el-form-item label="指标创建时间" prop="indicatorCreatTime">
        <el-date-picker
          v-model="dataForm.indicatorCreatTime"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <!-- <el-form-item label="0表示弃用，1表示使用中" prop="indicatorState">
      <el-input v-model="dataForm.indicatorState" placeholder="0表示弃用，1表示使用中"></el-input>
    </el-form-item> -->
      <!-- <el-form-item label="指标子节点" prop="indicatorChildNode">
      <el-input v-model="dataForm.indicatorChildNode" placeholder="指标子节点"></el-input>
    </el-form-item> -->
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleCancel()">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      selectedBound: '', // 中间变量，用于控制单选状态

      totalWeight: 0,  // 所有指标的权重之和
      originalWeight: 0, // 当前指标权重（修改时使用）
      remainingWeight: 100, // 剩余权重
      weightError: '',  // 错误提示

      //指标公式
      formulas: [
        { id: 1, name: 'a', template: 'a'},
        { id: 2, name: 'a / b', template: 'a / b' },
        { id: 3, name: 'a + b', template: 'a + b' },
        { id: 4, name: 'a + b + c', template: 'a + b + c' },
        { id: 5, name: '1 - a / b', template: '1 - a / b' },
        { id: 6, name: '(a + b + c - d) / e', template: '(a + b + c - d) / e' },
        { id: 7, name: '(a + b) / c', template: '(a + b) / c' },
        { id: 8, name: '(a + b + c) / d', template: '(a + b + c) / d' },
        { id: 9, name: 'a + b + c - d', template: 'a + b + c - d' },
        { id: 10, name: 'a / (b * c)', template: 'a / (b * c)' },
      ],
      dataList: [],  // 指标数据源列表
      indicatorDictionaryList: [], //指标列表
      indicatorChildrenList: [],  // 指标子节点列表
      selectedFormula: null,
      placeholders: [],
      placeholderMapping: {},

      indicatorClassificationList: ['A', 'B', 'C'],  //指标分级选项
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
        indicatorChildNode: '',
        indicatorValueUpperBound: '',
        indicatorValueLowerBound: '',
        storageFlag: '',
        weight: '',
        indicatorPlannedValue: '',
        indicatorBoundFlag: ''
      },
      dataDictionaryForm: {},  // 指标数据字典表
      dataRule: {
        indicatorName: [
          { required: true, message: '指标名称不能为空', trigger: 'blur' },
          { validator: this.validateIndicatorName, trigger: 'blur' }
        ],
        // assessmentDepartment: [
        //   { required: true, message: '考核部门不能为空', trigger: 'blur' }
        // ],
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
        // planId: [
        //   { required: true, message: '关联计划id不能为空', trigger: 'blur' }
        // ],
        // taskId: [
        //   { required: true, message: '关联任务id不能为空', trigger: 'blur' }
        // ],
        // indicatorParentNode: [
        //   { required: true, message: '指标父节点不能为空', trigger: 'blur' }
        // ],
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
  computed: {
    //填入数据源后最终生成的公式
    generatedFormula() {
      let formula = this.selectedFormula;
      for (const [placeholder, field] of Object.entries(this.placeholderMapping)) {
        formula = formula.replace(placeholder, field || placeholder);
      }
      this.dataForm.indicatorDefinition = formula;
      return formula;
    }
  },
  created () {
    this.getDataDictionaryList()
  },
  methods: {
    calculateTotalWeight() {
      // 计算所有子节点的权重总和
      const children = this.getChildren(this.dataForm.indicatorParentNode);
      console.log("children===>",children);
      this.totalWeight = children.reduce((sum, child) => sum + (child.weight || 0), 0);
      console.log("this.totalWeight===>",this.totalWeight)
      this.remainingWeight = 100 - this.totalWeight;
      this.weightError = '';
    },
    getChildren(parentNode) {
      this.indicatorChildrenList = this.indicatorDictionaryList;
      // 获取父节点的所有子节点
      return this.indicatorChildrenList.filter(item => item.indicatorParentNode === parentNode);
    },
    // 编辑指标时计算剩余权重
    calculateRemainingWeight() {
      const newWeight = parseFloat(this.dataForm.weight) || 0;
      console.log("newWeight===>",newWeight)
      let newTotalWeight;
      if (this.dataForm.indicatorId) { // 修改时
        newTotalWeight = this.totalWeight - (this.originalWeight || 0) + newWeight;
      } else { // 新增时
        newTotalWeight = this.totalWeight + newWeight;
      }
      console.log("newTotalWeight===>",newTotalWeight)
      if (newTotalWeight > 100) {
        this.weightError = '权重总和不能超过100%';
      } else {
        this.weightError = '';
      }
      this.remainingWeight = 100 - newTotalWeight;
      console.log("this.weightError===>",this.weightError)
    },
    //新增指标时验证是否有同名指标
    validateIndicatorName(rule, value, callback) {
      if (!value) {
        return callback();
      }
      if (this.dataForm.indicatorId) {
        return callback();
      }
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
        method: 'get',
        params: this.$http.adornParams({
          'limit': 10000,
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          console.log("validateIndicatorName===>data=====>",data)
          this.indicatorDictionaryList = data.page.list
          // 在数组最前面添加新的对象
          this.indicatorDictionaryList.unshift({
            indicatorId: 1,
            indicatorName: '公司质量指标管控体系',
            indicatorParentNode: ''
          });
          this.totalPage = data.page.totalCount
        }
      })
      // 假设 indicatorDictionaryList 已经从后端获取
      const existingIndicator = this.indicatorDictionaryList.find(indicator => indicator.indicatorName === value);

      if (existingIndicator) {
        callback(new Error('已有同名指标'));
      } else {
        callback();
      }
    },
    getDataDictionaryList() {
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordatadictionary/list'),
        method: 'get',
        params: this.$http.adornParams({
          'limit': 10000,
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          console.log("data=====>",data)
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        }
      })
      this.$http({
        url: this.$http.adornUrl('/indicator/indicatordictionary/list'),
        method: 'get',
        params: this.$http.adornParams({
          'limit': 10000,
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          console.log("data=====>",data)
          this.indicatorDictionaryList = data.page.list
          // 在数组最前面添加新的对象
          this.indicatorDictionaryList.unshift({
            indicatorId: 1,
            indicatorName: '公司质量指标管控体系',
            indicatorParentNode: ''
          });
          this.totalPage = data.page.totalCount
       }
      })
    },
    init(id) {
      this.dataForm.indicatorId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.indicatorId) {
          this.$http({
            url: this.$http.adornUrl(`/indicator/indicatordictionary/info/${this.dataForm.indicatorId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
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
              this.dataForm.indicatorValueLowerBound = data.indicatorDictionary.indicatorValueLowerBound
              this.dataForm.indicatorValueUpperBound = data.indicatorDictionary.indicatorValueUpperBound
              this.dataForm.weight = data.indicatorDictionary.weight
              this.dataForm.indicatorPlannedValue = data.indicatorDictionary.indicatorPlannedValue
              this.dataForm.indicatorBoundFlag = data.indicatorDictionary.indicatorBoundFlag
              this.dataForm.storageFlag = data.indicatorDictionary.storageFlag
              this.originalWeight = this.dataForm.weight;
              console.log("this.originalWeight====>",this.originalWeight)
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.weightError) {
            this.$message.error('权重总和不能超过100%');
            return;
          }

          console.log("this.dataForm2=====>",this.dataForm)
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
              'indicatorChildNode': this.dataForm.indicatorChildNode,
              'indicatorValueLowerBound': this.dataForm.indicatorValueLowerBound,
              'indicatorValueUpperBound': this.dataForm.indicatorValueUpperBound,
              'weight': this.dataForm.weight,
              'indicatorPlannedValue': this.dataForm.indicatorPlannedValue,
              'indicatorBoundFlag': this.dataForm.indicatorBoundFlag
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              console.log("data222=====>",data)
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.selectedFormula = null;
                  this.placeholders = [];
                  this.placeholderMapping = {};
                  this.indicatorChildrenList = []; // 清空指标子节点
                  this.$emit('refreshDataList')
                  this.getDataDictionaryList();
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    },
    //新增取消按钮
    handleCancel() {
      this.visible = false;
      this.selectedFormula = null;
      this.placeholders = [];
      this.placeholderMapping = {};
    },
    //公式字段选择数据
    onFormulaChange() {
      if (this.selectedFormula) {
        // 提取公式中的占位符，如 a, b, c
        this.placeholders = this.selectedFormula.match(/[a-z]/g) || [];
        // 初始化映射
        this.placeholderMapping = {};
        this.placeholders.forEach(placeholder => {
          // this.placeholderMapping[placeholder] = this.dataList[0]; // 默认选择第一个字段
          // this.placeholderMapping[placeholder] = this.dataList[index % this.dataList.length] || this.dataList[0];
        });
      } else {
        this.placeholders = [];
        this.placeholderMapping = {};
      }
    },

  }
}
</script>
