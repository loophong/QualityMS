<template>
  <el-dialog
    :title="!dataForm.spcxrId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="当月第1天数据" prop="date1">
      <el-input v-model="dataForm.date1" placeholder="当月第1天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第2天数据" prop="date2">
      <el-input v-model="dataForm.date2" placeholder="当月第2天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第3天数据" prop="date3">
      <el-input v-model="dataForm.date3" placeholder="当月第3天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第4天数据" prop="date4">
      <el-input v-model="dataForm.date4" placeholder="当月第4天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第5天数据" prop="date5">
      <el-input v-model="dataForm.date5" placeholder="当月第5天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第6天数据" prop="date6">
      <el-input v-model="dataForm.date6" placeholder="当月第6天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第7天数据" prop="date7">
      <el-input v-model="dataForm.date7" placeholder="当月第7天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第8天数据" prop="date8">
      <el-input v-model="dataForm.date8" placeholder="当月第8天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第9天数据" prop="date9">
      <el-input v-model="dataForm.date9" placeholder="当月第9天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第10天数据" prop="date10">
      <el-input v-model="dataForm.date10" placeholder="当月第10天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第11天数据" prop="date11">
      <el-input v-model="dataForm.date11" placeholder="当月第11天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第12天数据" prop="date12">
      <el-input v-model="dataForm.date12" placeholder="当月第12天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第13天数据" prop="date13">
      <el-input v-model="dataForm.date13" placeholder="当月第13天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第14天数据" prop="date14">
      <el-input v-model="dataForm.date14" placeholder="当月第14天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第15天数据" prop="date15">
      <el-input v-model="dataForm.date15" placeholder="当月第15天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第16天数据" prop="date16">
      <el-input v-model="dataForm.date16" placeholder="当月第16天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第17天数据" prop="date17">
      <el-input v-model="dataForm.date17" placeholder="当月第17天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第18天数据" prop="date18">
      <el-input v-model="dataForm.date18" placeholder="当月第18天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第19天数据" prop="date19">
      <el-input v-model="dataForm.date19" placeholder="当月第19天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第20天数据" prop="date20">
      <el-input v-model="dataForm.date20" placeholder="当月第20天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第21天数据" prop="date21">
      <el-input v-model="dataForm.date21" placeholder="当月第21天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第22天数据" prop="date22">
      <el-input v-model="dataForm.date22" placeholder="当月第22天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第23天数据" prop="date23">
      <el-input v-model="dataForm.date23" placeholder="当月第23天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第24天数据" prop="date24">
      <el-input v-model="dataForm.date24" placeholder="当月第24天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第25天数据" prop="date25">
      <el-input v-model="dataForm.date25" placeholder="当月第25天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第26天数据" prop="date26">
      <el-input v-model="dataForm.date26" placeholder="当月第26天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第27天数据" prop="date27">
      <el-input v-model="dataForm.date27" placeholder="当月第27天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第28天数据" prop="date28">
      <el-input v-model="dataForm.date28" placeholder="当月第28天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第29天数据" prop="date29">
      <el-input v-model="dataForm.date29" placeholder="当月第29天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第30天数据" prop="date30">
      <el-input v-model="dataForm.date30" placeholder="当月第30天数据"></el-input>
    </el-form-item>
    <el-form-item label="当月第31天数据" prop="date31">
      <el-input v-model="dataForm.date31" placeholder="当月第31天数据"></el-input>
    </el-form-item>
    <el-form-item label="数据日期" prop="datatime">
      <el-input v-model="dataForm.datatime" placeholder="数据日期"></el-input>
    </el-form-item>
    <el-form-item label="标准上限USL" prop="upperLimitStandards">
      <el-input v-model="dataForm.upperLimitStandards" placeholder="标准上限USL"></el-input>
    </el-form-item>
    <el-form-item label="标准中心限CL" prop="centerLimitStandards">
      <el-input v-model="dataForm.centerLimitStandards" placeholder="标准中心限CL"></el-input>
    </el-form-item>
    <el-form-item label="标准下限LSL" prop="lowerLimitStandards">
      <el-input v-model="dataForm.lowerLimitStandards" placeholder="标准下限LSL"></el-input>
    </el-form-item>
    <el-form-item label="X上限UCL" prop="upperLimitX">
      <el-input v-model="dataForm.upperLimitX" placeholder="X上限UCL"></el-input>
    </el-form-item>
    <el-form-item label="X中心限CL" prop="centerLimitX">
      <el-input v-model="dataForm.centerLimitX" placeholder="X中心限CL"></el-input>
    </el-form-item>
    <el-form-item label="X下限LCL" prop="lowerLimitX">
      <el-input v-model="dataForm.lowerLimitX" placeholder="X下限LCL"></el-input>
    </el-form-item>
    <el-form-item label="R上限UCL" prop="upperLimitR">
      <el-input v-model="dataForm.upperLimitR" placeholder="R上限UCL"></el-input>
    </el-form-item>
    <el-form-item label="R中心限CL" prop="centerLimitR">
      <el-input v-model="dataForm.centerLimitR" placeholder="R中心限CL"></el-input>
    </el-form-item>
    <el-form-item label="R下限LCL" prop="lowerLimitR">
      <el-input v-model="dataForm.lowerLimitR" placeholder="R下限LCL"></el-input>
    </el-form-item>
    <el-form-item label="创建人" prop="createBy">
      <el-input v-model="dataForm.createBy" placeholder="创建人"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="备用字段1" prop="alternateFields1">
      <el-input v-model="dataForm.alternateFields1" placeholder="备用字段1"></el-input>
    </el-form-item>
    <el-form-item label="备用字段2" prop="alternateFields2">
      <el-input v-model="dataForm.alternateFields2" placeholder="备用字段2"></el-input>
    </el-form-item>
    <el-form-item label="备用字段3" prop="alternateFields3">
      <el-input v-model="dataForm.alternateFields3" placeholder="备用字段3"></el-input>
    </el-form-item>
    <el-form-item label="备用字段4" prop="alternateFields4">
      <el-input v-model="dataForm.alternateFields4" placeholder="备用字段4"></el-input>
    </el-form-item>
    <el-form-item label="逻辑删除标志" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder="逻辑删除标志"></el-input>
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
          spcxrId: 0,
          date1: '',
          date2: '',
          date3: '',
          date4: '',
          date5: '',
          date6: '',
          date7: '',
          date8: '',
          date9: '',
          date10: '',
          date11: '',
          date12: '',
          date13: '',
          date14: '',
          date15: '',
          date16: '',
          date17: '',
          date18: '',
          date19: '',
          date20: '',
          date21: '',
          date22: '',
          date23: '',
          date24: '',
          date25: '',
          date26: '',
          date27: '',
          date28: '',
          date29: '',
          date30: '',
          date31: '',
          datatime: '',
          upperLimitStandards: '',
          centerLimitStandards: '',
          lowerLimitStandards: '',
          upperLimitX: '',
          centerLimitX: '',
          lowerLimitX: '',
          upperLimitR: '',
          centerLimitR: '',
          lowerLimitR: '',
          createBy: '',
          createTime: '',
          alternateFields1: '',
          alternateFields2: '',
          alternateFields3: '',
          alternateFields4: '',
          deleteFlag: ''
        },
        dataRule: {
        //   date1: [
        //     { required: true, message: '当月第1天数据不能为空', trigger: 'blur' }
        //   ],
        //   date2: [
        //     { required: true, message: '当月第2天数据不能为空', trigger: 'blur' }
        //   ],
        //   date3: [
        //     { required: true, message: '当月第3天数据不能为空', trigger: 'blur' }
        //   ],
        //   date4: [
        //     { required: true, message: '当月第4天数据不能为空', trigger: 'blur' }
        //   ],
        //   date5: [
        //     { required: true, message: '当月第5天数据不能为空', trigger: 'blur' }
        //   ],
        //   date6: [
        //     { required: true, message: '当月第6天数据不能为空', trigger: 'blur' }
        //   ],
        //   date7: [
        //     { required: true, message: '当月第7天数据不能为空', trigger: 'blur' }
        //   ],
        //   date8: [
        //     { required: true, message: '当月第8天数据不能为空', trigger: 'blur' }
        //   ],
        //   date9: [
        //     { required: true, message: '当月第9天数据不能为空', trigger: 'blur' }
        //   ],
        //   date10: [
        //     { required: true, message: '当月第10天数据不能为空', trigger: 'blur' }
        //   ],
        //   date11: [
        //     { required: true, message: '当月第11天数据不能为空', trigger: 'blur' }
        //   ],
        //   date12: [
        //     { required: true, message: '当月第12天数据不能为空', trigger: 'blur' }
        //   ],
        //   date13: [
        //     { required: true, message: '当月第13天数据不能为空', trigger: 'blur' }
        //   ],
        //   date14: [
        //     { required: true, message: '当月第14天数据不能为空', trigger: 'blur' }
        //   ],
        //   date15: [
        //     { required: true, message: '当月第15天数据不能为空', trigger: 'blur' }
        //   ],
        //   date16: [
        //     { required: true, message: '当月第16天数据不能为空', trigger: 'blur' }
        //   ],
        //   date17: [
        //     { required: true, message: '当月第17天数据不能为空', trigger: 'blur' }
        //   ],
        //   date18: [
        //     { required: true, message: '当月第18天数据不能为空', trigger: 'blur' }
        //   ],
        //   date19: [
        //     { required: true, message: '当月第19天数据不能为空', trigger: 'blur' }
        //   ],
        //   date20: [
        //     { required: true, message: '当月第20天数据不能为空', trigger: 'blur' }
        //   ],
        //   date21: [
        //     { required: true, message: '当月第21天数据不能为空', trigger: 'blur' }
        //   ],
        //   date22: [
        //     { required: true, message: '当月第22天数据不能为空', trigger: 'blur' }
        //   ],
        //   date23: [
        //     { required: true, message: '当月第23天数据不能为空', trigger: 'blur' }
        //   ],
        //   date24: [
        //     { required: true, message: '当月第24天数据不能为空', trigger: 'blur' }
        //   ],
        //   date25: [
        //     { required: true, message: '当月第25天数据不能为空', trigger: 'blur' }
        //   ],
        //   date26: [
        //     { required: true, message: '当月第26天数据不能为空', trigger: 'blur' }
        //   ],
        //   date27: [
        //     { required: true, message: '当月第27天数据不能为空', trigger: 'blur' }
        //   ],
        //   date28: [
        //     { required: true, message: '当月第28天数据不能为空', trigger: 'blur' }
        //   ],
        //   date29: [
        //     { required: true, message: '当月第29天数据不能为空', trigger: 'blur' }
        //   ],
        //   date30: [
        //     { required: true, message: '当月第30天数据不能为空', trigger: 'blur' }
        //   ],
        //   date31: [
        //     { required: true, message: '当月第31天数据不能为空', trigger: 'blur' }
        //   ],
        //   datatime: [
        //     { required: true, message: '数据日期不能为空', trigger: 'blur' }
        //   ],
        //   upperLimitStandards: [
        //     { required: true, message: '标准上限USL不能为空', trigger: 'blur' }
        //   ],
        //   centerLimitStandards: [
        //     { required: true, message: '标准中心限CL不能为空', trigger: 'blur' }
        //   ],
        //   lowerLimitStandards: [
        //     { required: true, message: '标准下限LSL不能为空', trigger: 'blur' }
        //   ],
        //   upperLimitX: [
        //     { required: true, message: 'X上限UCL不能为空', trigger: 'blur' }
        //   ],
        //   centerLimitX: [
        //     { required: true, message: 'X中心限CL不能为空', trigger: 'blur' }
        //   ],
        //   lowerLimitX: [
        //     { required: true, message: 'X下限LCL不能为空', trigger: 'blur' }
        //   ],
        //   upperLimitR: [
        //     { required: true, message: 'R上限UCL不能为空', trigger: 'blur' }
        //   ],
        //   centerLimitR: [
        //     { required: true, message: 'R中心限CL不能为空', trigger: 'blur' }
        //   ],
        //   lowerLimitR: [
        //     { required: true, message: 'R下限LCL不能为空', trigger: 'blur' }
        //   ],
        //   createBy: [
        //     { required: true, message: '创建人不能为空', trigger: 'blur' }
        //   ],
        //   createTime: [
        //     { required: true, message: '创建时间不能为空', trigger: 'blur' }
        //   ],
        //   alternateFields1: [
        //     { required: true, message: '备用字段1不能为空', trigger: 'blur' }
        //   ],
        //   alternateFields2: [
        //     { required: true, message: '备用字段2不能为空', trigger: 'blur' }
        //   ],
        //   alternateFields3: [
        //     { required: true, message: '备用字段3不能为空', trigger: 'blur' }
        //   ],
        //   alternateFields4: [
        //     { required: true, message: '备用字段4不能为空', trigger: 'blur' }
        //   ],
        //   deleteFlag: [
        //     { required: true, message: '逻辑删除标志不能为空', trigger: 'blur' }
        //   ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.spcxrId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.spcxrId) {
            this.$http({
              url: this.$http.adornUrl(`/spc/spcxrchart/info/${this.dataForm.spcxrId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.date1 = data.spcXrchart.date1
                this.dataForm.date2 = data.spcXrchart.date2
                this.dataForm.date3 = data.spcXrchart.date3
                this.dataForm.date4 = data.spcXrchart.date4
                this.dataForm.date5 = data.spcXrchart.date5
                this.dataForm.date6 = data.spcXrchart.date6
                this.dataForm.date7 = data.spcXrchart.date7
                this.dataForm.date8 = data.spcXrchart.date8
                this.dataForm.date9 = data.spcXrchart.date9
                this.dataForm.date10 = data.spcXrchart.date10
                this.dataForm.date11 = data.spcXrchart.date11
                this.dataForm.date12 = data.spcXrchart.date12
                this.dataForm.date13 = data.spcXrchart.date13
                this.dataForm.date14 = data.spcXrchart.date14
                this.dataForm.date15 = data.spcXrchart.date15
                this.dataForm.date16 = data.spcXrchart.date16
                this.dataForm.date17 = data.spcXrchart.date17
                this.dataForm.date18 = data.spcXrchart.date18
                this.dataForm.date19 = data.spcXrchart.date19
                this.dataForm.date20 = data.spcXrchart.date20
                this.dataForm.date21 = data.spcXrchart.date21
                this.dataForm.date22 = data.spcXrchart.date22
                this.dataForm.date23 = data.spcXrchart.date23
                this.dataForm.date24 = data.spcXrchart.date24
                this.dataForm.date25 = data.spcXrchart.date25
                this.dataForm.date26 = data.spcXrchart.date26
                this.dataForm.date27 = data.spcXrchart.date27
                this.dataForm.date28 = data.spcXrchart.date28
                this.dataForm.date29 = data.spcXrchart.date29
                this.dataForm.date30 = data.spcXrchart.date30
                this.dataForm.date31 = data.spcXrchart.date31
                this.dataForm.datatime = data.spcXrchart.datatime
                this.dataForm.upperLimitStandards = data.spcXrchart.upperLimitStandards
                this.dataForm.centerLimitStandards = data.spcXrchart.centerLimitStandards
                this.dataForm.lowerLimitStandards = data.spcXrchart.lowerLimitStandards
                this.dataForm.upperLimitX = data.spcXrchart.upperLimitX
                this.dataForm.centerLimitX = data.spcXrchart.centerLimitX
                this.dataForm.lowerLimitX = data.spcXrchart.lowerLimitX
                this.dataForm.upperLimitR = data.spcXrchart.upperLimitR
                this.dataForm.centerLimitR = data.spcXrchart.centerLimitR
                this.dataForm.lowerLimitR = data.spcXrchart.lowerLimitR
                this.dataForm.createBy = data.spcXrchart.createBy
                this.dataForm.createTime = data.spcXrchart.createTime
                this.dataForm.alternateFields1 = data.spcXrchart.alternateFields1
                this.dataForm.alternateFields2 = data.spcXrchart.alternateFields2
                this.dataForm.alternateFields3 = data.spcXrchart.alternateFields3
                this.dataForm.alternateFields4 = data.spcXrchart.alternateFields4
                this.dataForm.deleteFlag = data.spcXrchart.deleteFlag
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
              url: this.$http.adornUrl(`/spc/spcxrchart/${!this.dataForm.spcxrId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'spcxrId': this.dataForm.spcxrId || undefined,
                'date1': this.dataForm.date1,
                'date2': this.dataForm.date2,
                'date3': this.dataForm.date3,
                'date4': this.dataForm.date4,
                'date5': this.dataForm.date5,
                'date6': this.dataForm.date6,
                'date7': this.dataForm.date7,
                'date8': this.dataForm.date8,
                'date9': this.dataForm.date9,
                'date10': this.dataForm.date10,
                'date11': this.dataForm.date11,
                'date12': this.dataForm.date12,
                'date13': this.dataForm.date13,
                'date14': this.dataForm.date14,
                'date15': this.dataForm.date15,
                'date16': this.dataForm.date16,
                'date17': this.dataForm.date17,
                'date18': this.dataForm.date18,
                'date19': this.dataForm.date19,
                'date20': this.dataForm.date20,
                'date21': this.dataForm.date21,
                'date22': this.dataForm.date22,
                'date23': this.dataForm.date23,
                'date24': this.dataForm.date24,
                'date25': this.dataForm.date25,
                'date26': this.dataForm.date26,
                'date27': this.dataForm.date27,
                'date28': this.dataForm.date28,
                'date29': this.dataForm.date29,
                'date30': this.dataForm.date30,
                'date31': this.dataForm.date31,
                'datatime': this.dataForm.datatime,
                'upperLimitStandards': this.dataForm.upperLimitStandards,
                'centerLimitStandards': this.dataForm.centerLimitStandards,
                'lowerLimitStandards': this.dataForm.lowerLimitStandards,
                'upperLimitX': this.dataForm.upperLimitX,
                'centerLimitX': this.dataForm.centerLimitX,
                'lowerLimitX': this.dataForm.lowerLimitX,
                'upperLimitR': this.dataForm.upperLimitR,
                'centerLimitR': this.dataForm.centerLimitR,
                'lowerLimitR': this.dataForm.lowerLimitR,
                'createBy': this.dataForm.createBy,
                'createTime': this.dataForm.createTime,
                'alternateFields1': this.dataForm.alternateFields1,
                'alternateFields2': this.dataForm.alternateFields2,
                'alternateFields3': this.dataForm.alternateFields3,
                'alternateFields4': this.dataForm.alternateFields4,
                'deleteFlag': this.dataForm.deleteFlag
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
