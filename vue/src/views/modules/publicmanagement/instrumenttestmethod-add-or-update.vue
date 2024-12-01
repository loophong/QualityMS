<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="科室" prop="department">
      <el-input v-model="dataForm.department" placeholder="科室"></el-input>
    </el-form-item>
    <el-form-item label="标准编号" prop="standardnumber">
      <el-input v-model="dataForm.standardnumber" placeholder="标准编号"></el-input>
    </el-form-item>
    <el-form-item label="编制" prop="compilation">
      <el-input v-model="dataForm.compilation" placeholder="编制"></el-input>
    </el-form-item>
    <el-form-item label="编制日期" prop="compilationdate">
      <el-input v-model="dataForm.compilationdate" placeholder="编制日期"></el-input>
    </el-form-item>
    <el-form-item label="审核" prop="reviewer">
      <el-input v-model="dataForm.reviewer" placeholder="审核"></el-input>
    </el-form-item>
    <el-form-item label="审核日期" prop="reviewdate">
      <el-input v-model="dataForm.reviewdate" placeholder="审核日期"></el-input>
    </el-form-item>
    <el-form-item label="会签" prop="countersignature">
      <el-input v-model="dataForm.countersignature" placeholder="会签"></el-input>
    </el-form-item>
    <el-form-item label="批准" prop="approval">
      <el-input v-model="dataForm.approval" placeholder="批准"></el-input>
    </el-form-item>
    <el-form-item label="检验零部件" prop="inspectionparts">
      <el-input v-model="dataForm.inspectionparts" placeholder="检验零部件"></el-input>
    </el-form-item>
    <el-form-item label="图号" prop="drawingnumber">
      <el-input v-model="dataForm.drawingnumber" placeholder="图号"></el-input>
    </el-form-item>
    <el-form-item label="检验件类别" prop="inspectionpartcategory">
      <el-input v-model="dataForm.inspectionpartcategory" placeholder="检验件类别"></el-input>
    </el-form-item>
    <el-form-item label="工序名称" prop="processname">
      <el-input v-model="dataForm.processname" placeholder="工序名称"></el-input>
    </el-form-item>
    <el-form-item label="检验类别" prop="inspectioncategory">
      <el-input v-model="dataForm.inspectioncategory" placeholder="检验类别"></el-input>
    </el-form-item>
    <el-form-item label="检验项目" prop="inspectionitem">
      <el-input v-model="dataForm.inspectionitem" placeholder="检验项目"></el-input>
    </el-form-item>
    <el-form-item label="检验参数名称" prop="inspectionparametername">
      <el-input v-model="dataForm.inspectionparametername" placeholder="检验参数名称"></el-input>
    </el-form-item>
    <el-form-item label="检验部位" prop="inspectionlocation">
      <el-input v-model="dataForm.inspectionlocation" placeholder="检验部位"></el-input>
    </el-form-item>
    <el-form-item label="上限" prop="upperlimit">
      <el-input v-model="dataForm.upperlimit" placeholder="上限"></el-input>
    </el-form-item>
    <el-form-item label="下限" prop="lowerlimit">
      <el-input v-model="dataForm.lowerlimit" placeholder="下限"></el-input>
    </el-form-item>
    <el-form-item label="测量仪器名称" prop="measurementinstrument">
      <el-input v-model="dataForm.measurementinstrument" placeholder="测量仪器名称"></el-input>
    </el-form-item>
    <el-form-item label="检查操作要领" prop="checkoperationmanual">
      <el-input v-model="dataForm.checkoperationmanual" placeholder="检查操作要领"></el-input>
    </el-form-item>
    <el-form-item label="检查员" prop="inspector">
      <el-input v-model="dataForm.inspector" placeholder="检查员"></el-input>
    </el-form-item>
    <el-form-item label="检查频次" prop="inspectionfrequency">
      <el-input v-model="dataForm.inspectionfrequency" placeholder="检查频次"></el-input>
    </el-form-item>
    <el-form-item label="记录要求" prop="recordrequirement">
      <el-input v-model="dataForm.recordrequirement" placeholder="记录要求"></el-input>
    </el-form-item>
    <el-form-item label="巡检人" prop="patrolinspector">
      <el-input v-model="dataForm.patrolinspector" placeholder="巡检人"></el-input>
    </el-form-item>
    <el-form-item label="检具校正" prop="calibrationtool">
      <el-input v-model="dataForm.calibrationtool" placeholder="检具校正"></el-input>
    </el-form-item>
    <el-form-item label="校正频次" prop="calibrationfrequency">
      <el-input v-model="dataForm.calibrationfrequency" placeholder="校正频次"></el-input>
    </el-form-item>
    <el-form-item label="创建人" prop="creatPeople">
      <el-input v-model="dataForm.creatPeople" placeholder="创建人"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="creatTime">
      <el-input v-model="dataForm.creatTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="审批flag" prop="flag">
      <el-input v-model="dataForm.flag" placeholder="审批flag"></el-input>
    </el-form-item>


   
      

    <el-form-item label="删除flag" prop="delteflag">
      <el-input v-model="dataForm.delteflag" placeholder="删除flag"></el-input>
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
          department: '',
          standardnumber: '',
          compilation: '',
          compilationdate: '',
          reviewer: '',
          reviewdate: '',
          countersignature: '',
          approval: '',
          inspectionparts: '',
          drawingnumber: '',
          inspectionpartcategory: '',
          processname: '',
          inspectioncategory: '',
          inspectionitem: '',
          inspectionparametername: '',
          inspectionlocation: '',
          upperlimit: '',
          lowerlimit: '',
          measurementinstrument: '',
          checkoperationmanual: '',
          inspector: '',
          inspectionfrequency: '',
          recordrequirement: '',
          patrolinspector: '',
          calibrationtool: '',
          calibrationfrequency: '',
          creatPeople: '',
          creatTime: '',
          flag: '',
          delteflag: ''
        },
        dataRule: {
          department: [
            { required: true, message: '科室不能为空', trigger: 'blur' }
          ],
          standardnumber: [
            { required: true, message: '标准编号不能为空', trigger: 'blur' }
          ],
          compilation: [
            { required: true, message: '编制不能为空', trigger: 'blur' }
          ],
          compilationdate: [
            { required: true, message: '编制日期不能为空', trigger: 'blur' }
          ],
          reviewer: [
            { required: true, message: '审核不能为空', trigger: 'blur' }
          ],
          reviewdate: [
            { required: true, message: '审核日期不能为空', trigger: 'blur' }
          ],
          countersignature: [
            { required: true, message: '会签不能为空', trigger: 'blur' }
          ],
          approval: [
            { required: true, message: '批准不能为空', trigger: 'blur' }
          ],
          inspectionparts: [
            { required: true, message: '检验零部件不能为空', trigger: 'blur' }
          ],
          drawingnumber: [
            { required: true, message: '图号不能为空', trigger: 'blur' }
          ],
          inspectionpartcategory: [
            { required: true, message: '检验件类别不能为空', trigger: 'blur' }
          ],
          processname: [
            { required: true, message: '工序名称不能为空', trigger: 'blur' }
          ],
          inspectioncategory: [
            { required: true, message: '检验类别不能为空', trigger: 'blur' }
          ],
          inspectionitem: [
            { required: true, message: '检验项目不能为空', trigger: 'blur' }
          ],
          inspectionparametername: [
            { required: true, message: '检验参数名称不能为空', trigger: 'blur' }
          ],
          inspectionlocation: [
            { required: true, message: '检验部位不能为空', trigger: 'blur' }
          ],
          upperlimit: [
            { required: true, message: '上限不能为空', trigger: 'blur' }
          ],
          lowerlimit: [
            { required: true, message: '下限不能为空', trigger: 'blur' }
          ],
          measurementinstrument: [
            { required: true, message: '测量仪器名称不能为空', trigger: 'blur' }
          ],
          checkoperationmanual: [
            { required: true, message: '检查操作要领不能为空', trigger: 'blur' }
          ],
          inspector: [
            { required: true, message: '检查员不能为空', trigger: 'blur' }
          ],
          inspectionfrequency: [
            { required: true, message: '检查频次不能为空', trigger: 'blur' }
          ],
          recordrequirement: [
            { required: true, message: '记录要求不能为空', trigger: 'blur' }
          ],
          patrolinspector: [
            { required: true, message: '巡检人不能为空', trigger: 'blur' }
          ],
          calibrationtool: [
            { required: true, message: '检具校正不能为空', trigger: 'blur' }
          ],
          calibrationfrequency: [
            { required: true, message: '校正频次不能为空', trigger: 'blur' }
          ],
          creatPeople: [
            { required: true, message: '创建人不能为空', trigger: 'blur' }
          ],
          creatTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          flag: [
            { required: true, message: '审批flag不能为空', trigger: 'blur' }
          ],
          delteflag: [
            { required: true, message: '删除flag不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    
        computed: {  
      flagText() {  
        switch (this.dataForm.flag) {  
          case '0':  
            return '未审批';  
          case '1':  
            return '审核通过';  
          case '2':  
            return '驳回审核';  
          default:  
            return '';  
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
              url: this.$http.adornUrl(`/generator/instrumenttestmethod/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.department = data.instrumentTestMethod.department
                this.dataForm.standardnumber = data.instrumentTestMethod.standardnumber
                this.dataForm.compilation = data.instrumentTestMethod.compilation
                this.dataForm.compilationdate = data.instrumentTestMethod.compilationdate
                this.dataForm.reviewer = data.instrumentTestMethod.reviewer
                this.dataForm.reviewdate = data.instrumentTestMethod.reviewdate
                this.dataForm.countersignature = data.instrumentTestMethod.countersignature
                this.dataForm.approval = data.instrumentTestMethod.approval
                this.dataForm.inspectionparts = data.instrumentTestMethod.inspectionparts
                this.dataForm.drawingnumber = data.instrumentTestMethod.drawingnumber
                this.dataForm.inspectionpartcategory = data.instrumentTestMethod.inspectionpartcategory
                this.dataForm.processname = data.instrumentTestMethod.processname
                this.dataForm.inspectioncategory = data.instrumentTestMethod.inspectioncategory
                this.dataForm.inspectionitem = data.instrumentTestMethod.inspectionitem
                this.dataForm.inspectionparametername = data.instrumentTestMethod.inspectionparametername
                this.dataForm.inspectionlocation = data.instrumentTestMethod.inspectionlocation
                this.dataForm.upperlimit = data.instrumentTestMethod.upperlimit
                this.dataForm.lowerlimit = data.instrumentTestMethod.lowerlimit
                this.dataForm.measurementinstrument = data.instrumentTestMethod.measurementinstrument
                this.dataForm.checkoperationmanual = data.instrumentTestMethod.checkoperationmanual
                this.dataForm.inspector = data.instrumentTestMethod.inspector
                this.dataForm.inspectionfrequency = data.instrumentTestMethod.inspectionfrequency
                this.dataForm.recordrequirement = data.instrumentTestMethod.recordrequirement
                this.dataForm.patrolinspector = data.instrumentTestMethod.patrolinspector
                this.dataForm.calibrationtool = data.instrumentTestMethod.calibrationtool
                this.dataForm.calibrationfrequency = data.instrumentTestMethod.calibrationfrequency
                this.dataForm.creatPeople = data.instrumentTestMethod.creatPeople
                this.dataForm.creatTime = data.instrumentTestMethod.creatTime
                this.dataForm.flag = data.instrumentTestMethod.flag
                this.dataForm.delteflag = data.instrumentTestMethod.delteflag
              }
            })
          }
        })
      },
      //
      getFlagDescription(flag) {
        switch (flag) {
          case 0:
            return '未审批';
          case 1:
            return '审核通过';
          case 2:
            return '驳回审核';
          default:
            return '未知状态';
        }
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/instrumenttestmethod/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'department': this.dataForm.department,
                'standardnumber': this.dataForm.standardnumber,
                'compilation': this.dataForm.compilation,
                'compilationdate': this.dataForm.compilationdate,
                'reviewer': this.dataForm.reviewer,
                'reviewdate': this.dataForm.reviewdate,
                'countersignature': this.dataForm.countersignature,
                'approval': this.dataForm.approval,
                'inspectionparts': this.dataForm.inspectionparts,
                'drawingnumber': this.dataForm.drawingnumber,
                'inspectionpartcategory': this.dataForm.inspectionpartcategory,
                'processname': this.dataForm.processname,
                'inspectioncategory': this.dataForm.inspectioncategory,
                'inspectionitem': this.dataForm.inspectionitem,
                'inspectionparametername': this.dataForm.inspectionparametername,
                'inspectionlocation': this.dataForm.inspectionlocation,
                'upperlimit': this.dataForm.upperlimit,
                'lowerlimit': this.dataForm.lowerlimit,
                'measurementinstrument': this.dataForm.measurementinstrument,
                'checkoperationmanual': this.dataForm.checkoperationmanual,
                'inspector': this.dataForm.inspector,
                'inspectionfrequency': this.dataForm.inspectionfrequency,
                'recordrequirement': this.dataForm.recordrequirement,
                'patrolinspector': this.dataForm.patrolinspector,
                'calibrationtool': this.dataForm.calibrationtool,
                'calibrationfrequency': this.dataForm.calibrationfrequency,
                'creatPeople': this.dataForm.creatPeople,
                'creatTime': this.dataForm.creatTime,
                'flag': this.dataForm.flag,
                'delteflag': this.dataForm.delteflag
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
