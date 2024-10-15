<template>
  <el-dialog :title="!dataForm.qcExamineId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="100px">
      <!-- <el-form-item label="审核关联课题" prop="qcExamineSubject">
        <el-input v-model="dataForm.qcExamineSubject" placeholder="审核关联课题"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="当前待审核步骤" prop="qcExamineCurrent">
        <el-input v-model="dataForm.qcExamineCurrent" placeholder="当前待审核步骤"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="课题提交状态" prop="qcStatusOne">
        <el-input v-model="dataForm.qcStatusOne" placeholder="课题提交状态"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="成果认定状态" prop="qcStatusTwo">
        <el-input v-model="dataForm.qcStatusTwo" placeholder="成果认定状态"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="相关方审核状态" prop="qcStatusThree">
        <el-input v-model="dataForm.qcStatusThree" placeholder="相关方审核状态"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="成果初评状态" prop="qcStatusFour">
        <el-input v-model="dataForm.qcStatusFour" placeholder="成果初评状态"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="成果复评状态" prop="qcStatusFive">
        <el-input v-model="dataForm.qcStatusFive" placeholder="成果复评状态"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="财务部审核状态" prop="qcStatusSix">
        <el-input v-model="dataForm.qcStatusSix" placeholder="财务部审核状态"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="终评提交状态" prop="qcStatusSeven">
        <el-input v-model="dataForm.qcStatusSeven" placeholder="终评提交状态"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="终评审核意见" prop="qcSevenContent">
        <el-input v-model="dataForm.qcSevenContent" placeholder="终评审核意见"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="财务部审核意见" prop="qcSixContent">
        <el-input v-model="dataForm.qcSixContent" placeholder="财务部审核意见"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="成果复评审核意见" prop="qcFiveContent">
        <el-input v-model="dataForm.qcFiveContent" placeholder="成果复评审核意见"></el-input>
      </el-form-item> -->
      <div style="color:#ce9178;">
        请直接修改数字，不要修改格式
      </div>
      <br>
      <el-form-item label="成果初评分数" prop="qcFirstScore">
        <el-input v-model="dataForm.qcFirstScore" placeholder="成果初评分数"></el-input>
      </el-form-item>
      <!-- <el-form-item label="成果初评审核意见" prop="qcFourContent">
        <el-input v-model="dataForm.qcFourContent" placeholder="成果初评审核意见"></el-input>
      </el-form-item> -->
      <el-form-item label="成果复评分数" prop="qcSecondScore">
        <el-input v-model="dataForm.qcSecondScore" placeholder="成果复评分数"></el-input>
      </el-form-item>

      <!-- <el-form-item label="相关方审核意见" prop="qcThreeContent">
        <el-input v-model="dataForm.qcThreeContent" placeholder="相关方审核意见"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="成果认定审核意见" prop="qcTwoContent">
        <el-input v-model="dataForm.qcTwoContent" placeholder="成果初评审核意见"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="成果认定审核意见" prop="qcOneContent">
        <el-input v-model="dataForm.qcOneContent" placeholder="成果认定审核意见"></el-input>
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
  data() {
    return {
      visible: false,
      dataForm: {
        qcExamineId: 0,
        qcExamineSubject: '',
        qcExamineCurrent: '',
        qcStatusOne: '',
        qcStatusTwo: '',
        qcStatusThree: '',
        qcStatusFour: '',
        qcStatusFive: '',
        qcStatusSix: '',
        qcStatusSeven: '',
        qcSevenContent: '',
        qcSixContent: '',
        qcFiveContent: '',
        qcSecondScore: '',
        qcFourContent: '',
        qcFirstScore: '',
        qcThreeContent: '',
        qcTwoContent: '',
        qcOneContent: ''
      },
      dataRule: {

      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.qcExamineId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.qcExamineId) {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/examineStatus/info/${this.dataForm.qcExamineId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.qcExamineSubject = data.qcExamineStatus.qcExamineSubject
              this.dataForm.qcExamineCurrent = data.qcExamineStatus.qcExamineCurrent
              this.dataForm.qcStatusOne = data.qcExamineStatus.qcStatusOne
              this.dataForm.qcStatusTwo = data.qcExamineStatus.qcStatusTwo
              this.dataForm.qcStatusThree = data.qcExamineStatus.qcStatusThree
              this.dataForm.qcStatusFour = data.qcExamineStatus.qcStatusFour
              this.dataForm.qcStatusFive = data.qcExamineStatus.qcStatusFive
              this.dataForm.qcStatusSix = data.qcExamineStatus.qcStatusSix
              this.dataForm.qcStatusSeven = data.qcExamineStatus.qcStatusSeven
              this.dataForm.qcSevenContent = data.qcExamineStatus.qcSevenContent
              this.dataForm.qcSixContent = data.qcExamineStatus.qcSixContent
              this.dataForm.qcFiveContent = data.qcExamineStatus.qcFiveContent
              this.dataForm.qcSecondScore = data.qcExamineStatus.qcSecondScore
              this.dataForm.qcFourContent = data.qcExamineStatus.qcFourContent
              this.dataForm.qcFirstScore = data.qcExamineStatus.qcFirstScore
              this.dataForm.qcThreeContent = data.qcExamineStatus.qcThreeContent
              this.dataForm.qcTwoContent = data.qcExamineStatus.qcTwoContent
              this.dataForm.qcOneContent = data.qcExamineStatus.qcOneContent
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/examineStatus/${!this.dataForm.qcExamineId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'qcExamineId': this.dataForm.qcExamineId || undefined,
              'qcExamineSubject': this.dataForm.qcExamineSubject,
              'qcExamineCurrent': this.dataForm.qcExamineCurrent,
              'qcStatusOne': this.dataForm.qcStatusOne,
              'qcStatusTwo': this.dataForm.qcStatusTwo,
              'qcStatusThree': this.dataForm.qcStatusThree,
              'qcStatusFour': this.dataForm.qcStatusFour,
              'qcStatusFive': this.dataForm.qcStatusFive,
              'qcStatusSix': this.dataForm.qcStatusSix,
              'qcStatusSeven': this.dataForm.qcStatusSeven,
              'qcSevenContent': this.dataForm.qcSevenContent,
              'qcSixContent': this.dataForm.qcSixContent,
              'qcFiveContent': this.dataForm.qcFiveContent,
              'qcSecondScore': this.dataForm.qcSecondScore,
              'qcFourContent': this.dataForm.qcFourContent,
              'qcFirstScore': this.dataForm.qcFirstScore,
              'qcThreeContent': this.dataForm.qcThreeContent,
              'qcTwoContent': this.dataForm.qcTwoContent,
              'qcOneContent': this.dataForm.qcOneContent
            })
          }).then(({ data }) => {
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
