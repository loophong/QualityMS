<template>
  <el-dialog :title="!dataForm.qcExamineId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="120px">

      <!-- <div style="color:#ce9178;">
        请直接修改数字，不要修改格式
      </div> -->
      <h3>初评</h3>
      <div v-for="(item) in dataForm.qcFirstAll" :key="item.name" style="margin-bottom: 20px;">


        <!-- name 字段只读显示 -->
        <el-form-item label="用户名">
          <el-input v-model="item.name" disabled></el-input>
        </el-form-item>

        <!-- score 字段 -->
        <el-form-item label="分数">
          <el-row :gutter="20">
            <el-col :span="5" v-for="(score, idx) in item.score" :key="idx">
              <el-input-number v-model="item.score[idx]" :min="0" :max="100"></el-input-number>
            </el-col>
          </el-row>
        </el-form-item>
      </div>
      <h3>复评</h3>
      <div v-for="(item, index2) in dataForm.qcSecondAll" :key="index2" style="margin-bottom: 20px;">
        <!-- <h3>{{ item.name }}</h3> -->

        <!-- name 字段只读显示 -->
        <el-form-item label="用户名">
          <el-input v-model="item.name" disabled></el-input>
        </el-form-item>

        <!-- score 字段 -->
        <el-form-item label="分数">
          <el-row :gutter="20">
            <el-col :span="5" v-for="(score, idx) in item.score" :key="idx">
              <el-input-number v-model="item.score[idx]" :min="0" :max="100"></el-input-number>
            </el-col>
          </el-row>
        </el-form-item>
      </div>
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
      groupLeadSure: false,
      visible: false,
      qcFirstAll: [],
      qcSecondAll: [],
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
        qcOneContent: '',
        qcFirstAll: [],
        qcSecondAll: [],
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
              this.dataForm.qcFirstAll = JSON.parse(data.qcExamineStatus.qcFirstAll)
              this.dataForm.qcSecondAll = JSON.parse(data.qcExamineStatus.qcSecondAll)
              this.qcFirstAll = JSON.parse(data.qcExamineStatus.qcFirstAll)
              this.qcSecondAll = JSON.parse(data.qcExamineStatus.qcSecondAll)
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        console.log(this.dataForm.qcFirstAll)
        console.log(this.dataForm.qcSecondAll)
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/examineStatus/${!this.dataForm.qcExamineId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'qcExamineId': this.dataForm.qcExamineId || undefined,
              'qcExamineSubject': this.dataForm.qcExamineSubject,
              'qcFirstAll': JSON.stringify(this.dataForm.qcFirstAll),
              'qcSecondAll': JSON.stringify(this.dataForm.qcSecondAll),
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
