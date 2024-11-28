<template>
  <el-dialog
    :title="!dataForm.spcpId ? '新增' : '修改'"
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
    <el-form-item label="合计" prop="sum">
      <el-input v-model="dataForm.sum" placeholder="合计"></el-input>
    </el-form-item>
    <el-form-item label="数据日期" prop="datatime">
      <el-input v-model="dataForm.datatime" placeholder="数据日期"></el-input>
    </el-form-item>
    <el-form-item label="数据内容（抽检数/不良数）" prop="dataContent">
      <el-input v-model="dataForm.dataContent" placeholder="数据内容（抽检数/不良数）"></el-input>
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
          spcpId: 0,
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
          sum: '',
          datatime: '',
          dataContent: '',
          createBy: '',
          createTime: '',
          alternateFields1: '',
          alternateFields2: '',
          alternateFields3: '',
          alternateFields4: '',
          deleteFlag: ''
        },
        dataRule: {
          date1: [
            { required: true, message: '当月第1天数据不能为空', trigger: 'blur' }
          ],
          date2: [
            { required: true, message: '当月第2天数据不能为空', trigger: 'blur' }
          ],
          date3: [
            { required: true, message: '当月第3天数据不能为空', trigger: 'blur' }
          ],
          date4: [
            { required: true, message: '当月第4天数据不能为空', trigger: 'blur' }
          ],
          date5: [
            { required: true, message: '当月第5天数据不能为空', trigger: 'blur' }
          ],
          date6: [
            { required: true, message: '当月第6天数据不能为空', trigger: 'blur' }
          ],
          date7: [
            { required: true, message: '当月第7天数据不能为空', trigger: 'blur' }
          ],
          date8: [
            { required: true, message: '当月第8天数据不能为空', trigger: 'blur' }
          ],
          date9: [
            { required: true, message: '当月第9天数据不能为空', trigger: 'blur' }
          ],
          date10: [
            { required: true, message: '当月第10天数据不能为空', trigger: 'blur' }
          ],
          date11: [
            { required: true, message: '当月第11天数据不能为空', trigger: 'blur' }
          ],
          date12: [
            { required: true, message: '当月第12天数据不能为空', trigger: 'blur' }
          ],
          date13: [
            { required: true, message: '当月第13天数据不能为空', trigger: 'blur' }
          ],
          date14: [
            { required: true, message: '当月第14天数据不能为空', trigger: 'blur' }
          ],
          date15: [
            { required: true, message: '当月第15天数据不能为空', trigger: 'blur' }
          ],
          date16: [
            { required: true, message: '当月第16天数据不能为空', trigger: 'blur' }
          ],
          date17: [
            { required: true, message: '当月第17天数据不能为空', trigger: 'blur' }
          ],
          date18: [
            { required: true, message: '当月第18天数据不能为空', trigger: 'blur' }
          ],
          date19: [
            { required: true, message: '当月第19天数据不能为空', trigger: 'blur' }
          ],
          date20: [
            { required: true, message: '当月第20天数据不能为空', trigger: 'blur' }
          ],
          date21: [
            { required: true, message: '当月第21天数据不能为空', trigger: 'blur' }
          ],
          date22: [
            { required: true, message: '当月第22天数据不能为空', trigger: 'blur' }
          ],
          date23: [
            { required: true, message: '当月第23天数据不能为空', trigger: 'blur' }
          ],
          date24: [
            { required: true, message: '当月第24天数据不能为空', trigger: 'blur' }
          ],
          date25: [
            { required: true, message: '当月第25天数据不能为空', trigger: 'blur' }
          ],
          date26: [
            { required: true, message: '当月第26天数据不能为空', trigger: 'blur' }
          ],
          date27: [
            { required: true, message: '当月第27天数据不能为空', trigger: 'blur' }
          ],
          date28: [
            { required: true, message: '当月第28天数据不能为空', trigger: 'blur' }
          ],
          date29: [
            { required: true, message: '当月第29天数据不能为空', trigger: 'blur' }
          ],
          date30: [
            { required: true, message: '当月第30天数据不能为空', trigger: 'blur' }
          ],
          date31: [
            { required: true, message: '当月第31天数据不能为空', trigger: 'blur' }
          ],
          sum: [
            { required: true, message: '合计不能为空', trigger: 'blur' }
          ],
          datatime: [
            { required: true, message: '数据日期不能为空', trigger: 'blur' }
          ],
          dataContent: [
            { required: true, message: '数据内容（抽检数/不良数）不能为空', trigger: 'blur' }
          ],
          createBy: [
            { required: true, message: '创建人不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          alternateFields1: [
            { required: true, message: '备用字段1不能为空', trigger: 'blur' }
          ],
          alternateFields2: [
            { required: true, message: '备用字段2不能为空', trigger: 'blur' }
          ],
          alternateFields3: [
            { required: true, message: '备用字段3不能为空', trigger: 'blur' }
          ],
          alternateFields4: [
            { required: true, message: '备用字段4不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '逻辑删除标志不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.spcpId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.spcpId) {
            this.$http({
              url: this.$http.adornUrl(`/spc/spcpchart/info/${this.dataForm.spcpId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.date1 = data.spcPchart.date1
                this.dataForm.date2 = data.spcPchart.date2
                this.dataForm.date3 = data.spcPchart.date3
                this.dataForm.date4 = data.spcPchart.date4
                this.dataForm.date5 = data.spcPchart.date5
                this.dataForm.date6 = data.spcPchart.date6
                this.dataForm.date7 = data.spcPchart.date7
                this.dataForm.date8 = data.spcPchart.date8
                this.dataForm.date9 = data.spcPchart.date9
                this.dataForm.date10 = data.spcPchart.date10
                this.dataForm.date11 = data.spcPchart.date11
                this.dataForm.date12 = data.spcPchart.date12
                this.dataForm.date13 = data.spcPchart.date13
                this.dataForm.date14 = data.spcPchart.date14
                this.dataForm.date15 = data.spcPchart.date15
                this.dataForm.date16 = data.spcPchart.date16
                this.dataForm.date17 = data.spcPchart.date17
                this.dataForm.date18 = data.spcPchart.date18
                this.dataForm.date19 = data.spcPchart.date19
                this.dataForm.date20 = data.spcPchart.date20
                this.dataForm.date21 = data.spcPchart.date21
                this.dataForm.date22 = data.spcPchart.date22
                this.dataForm.date23 = data.spcPchart.date23
                this.dataForm.date24 = data.spcPchart.date24
                this.dataForm.date25 = data.spcPchart.date25
                this.dataForm.date26 = data.spcPchart.date26
                this.dataForm.date27 = data.spcPchart.date27
                this.dataForm.date28 = data.spcPchart.date28
                this.dataForm.date29 = data.spcPchart.date29
                this.dataForm.date30 = data.spcPchart.date30
                this.dataForm.date31 = data.spcPchart.date31
                this.dataForm.sum = data.spcPchart.sum
                this.dataForm.datatime = data.spcPchart.datatime
                this.dataForm.dataContent = data.spcPchart.dataContent
                this.dataForm.createBy = data.spcPchart.createBy
                this.dataForm.createTime = data.spcPchart.createTime
                this.dataForm.alternateFields1 = data.spcPchart.alternateFields1
                this.dataForm.alternateFields2 = data.spcPchart.alternateFields2
                this.dataForm.alternateFields3 = data.spcPchart.alternateFields3
                this.dataForm.alternateFields4 = data.spcPchart.alternateFields4
                this.dataForm.deleteFlag = data.spcPchart.deleteFlag
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
              url: this.$http.adornUrl(`/generator/spcpchart/${!this.dataForm.spcpId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'spcpId': this.dataForm.spcpId || undefined,
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
                'sum': this.dataForm.sum,
                'datatime': this.dataForm.datatime,
                'dataContent': this.dataForm.dataContent,
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
