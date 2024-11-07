<template>
  <div>
  <el-dialog
    :title="!dataForm.issueId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="序号" prop="serialNumber">
      <el-input v-model="dataForm.serialNumber" placeholder="序号"></el-input>
    </el-form-item>
    <el-form-item label="问题编号" prop="issueNumber">
      <el-input v-model="dataForm.issueNumber" placeholder="问题编号"></el-input>
    </el-form-item>
    <el-form-item label="检查科室" prop="inspectionDepartment">
      <el-input v-model="dataForm.inspectionDepartment" placeholder="检查科室"></el-input>
    </el-form-item>
    <el-form-item label="检查日期" prop="inspectionDate">
      <el-input v-model="dataForm.inspectionDate" placeholder="检查日期"></el-input>
    </el-form-item>
    <el-form-item label="问题类别ID" prop="issueCategoryId">
      <el-input v-model="dataForm.issueCategoryId" placeholder="问题类别ID"></el-input>
    </el-form-item>
    <el-form-item label="车型ID" prop="vehicleTypeId">
      <el-input v-model="dataForm.vehicleTypeId" placeholder="车型ID"></el-input>
    </el-form-item>
    <el-form-item label="车号ID" prop="vehicleNumberId">
      <el-input v-model="dataForm.vehicleNumberId" placeholder="车号ID"></el-input>
    </el-form-item>
    <el-form-item label="问题描述" prop="issueDescription">
      <el-input v-model="dataForm.issueDescription" placeholder="问题描述"></el-input>
    </el-form-item>
    <el-form-item label="问题照片" prop="issuePhoto">
      <el-input v-model="dataForm.issuePhoto" placeholder="问题照片"></el-input>
    </el-form-item>
    <el-form-item label="整改要求" prop="rectificationRequirement">
      <el-input v-model="dataForm.rectificationRequirement" placeholder="整改要求"></el-input>
    </el-form-item>
    <el-form-item label="要求完成时间" prop="requiredCompletionTime">
      <el-input v-model="dataForm.requiredCompletionTime" placeholder="要求完成时间"></el-input>
    </el-form-item>
    <el-form-item label="责任科室" prop="responsibleDepartment">
      <el-input v-model="dataForm.responsibleDepartment" placeholder="责任科室"></el-input>
    </el-form-item>
    <el-form-item label="整改情况" prop="rectificationStatus">
      <el-input v-model="dataForm.rectificationStatus" placeholder="整改情况"></el-input>
    </el-form-item>
    <el-form-item label="实际完成时间" prop="actualCompletionTime">
      <el-input v-model="dataForm.actualCompletionTime" placeholder="实际完成时间"></el-input>
    </el-form-item>
    <el-form-item label="整改照片交付物" prop="rectificationPhotoDeliverable">
      <el-input v-model="dataForm.rectificationPhotoDeliverable" placeholder="整改照片交付物"></el-input>
    </el-form-item>
    <el-form-item label="整改责任人" prop="rectificationResponsiblePerson">
      <el-input v-model="dataForm.rectificationResponsiblePerson" placeholder="整改责任人"></el-input>
    </el-form-item>
    <el-form-item label="要求二次整改时间" prop="requiredSecondRectificationTime">
      <el-input v-model="dataForm.requiredSecondRectificationTime" placeholder="要求二次整改时间"></el-input>
    </el-form-item>
    <el-form-item label="备注" prop="remark">
      <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
    </el-form-item>
    <el-form-item label="创建人" prop="creator">
      <el-input v-model="dataForm.creator" placeholder="创建人"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="creationTime">
      <el-input v-model="dataForm.creationTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="最后修改人" prop="lastModifier">
      <el-input v-model="dataForm.lastModifier" placeholder="最后修改人"></el-input>
    </el-form-item>
    <el-form-item label="最后修改时间" prop="lastModificationTime">
      <el-input v-model="dataForm.lastModificationTime" placeholder="最后修改时间"></el-input>
    </el-form-item>
    <el-form-item label="关联问题整改记录" prop="associatedRectificationRecords">
      <el-input v-model="dataForm.associatedRectificationRecords" placeholder="关联问题整改记录"></el-input>
    </el-form-item>
    <el-form-item label="关联问题添加" prop="associatedIssueAddition">
      <el-input v-model="dataForm.associatedIssueAddition" placeholder="关联问题添加"></el-input>
    </el-form-item>
    <el-form-item label="创建时长" prop="creationDuration">
      <el-input v-model="dataForm.creationDuration" placeholder="创建时长"></el-input>
    </el-form-item>
    <el-form-item label="原因分析" prop="causeAnalysis">
      <el-input v-model="dataForm.causeAnalysis" placeholder="原因分析"></el-input>
    </el-form-item>
    <el-form-item label="整改验证情况" prop="rectificationVerificationStatus">
      <el-input v-model="dataForm.rectificationVerificationStatus" placeholder="整改验证情况"></el-input>
    </el-form-item>
    <el-form-item label="验证结论" prop="verificationConclusion">
      <el-input v-model="dataForm.verificationConclusion" placeholder="验证结论"></el-input>
    </el-form-item>
    <el-form-item label="验证人" prop="verifier">
      <el-input v-model="dataForm.verifier" placeholder="验证人"></el-input>
    </el-form-item>
    <el-form-item label="公式" prop="formula">
      <el-input v-model="dataForm.formula" placeholder="公式"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
  <el-dialog
    :title="'问题详情'"
    :close-on-click-modal="false"
    :visible.sync="visible1">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="80px">
<!--      <el-form-item label="序号" prop="serialNumber">-->
<!--        <el-input v-model="dataForm.serialNumber" readonly></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="问题编号" prop="issueNumber">
        <el-input v-model="dataForm.issueNumber" readonly></el-input>
      </el-form-item>
      <el-form-item label="检查科室" prop="inspectionDepartment">
        <el-input v-model="dataForm.inspectionDepartment" readonly></el-input>
      </el-form-item>
      <el-form-item label="检查日期" prop="inspectionDate">
        <el-input v-model="dataForm.inspectionDate" readonly></el-input>
      </el-form-item>
      <el-form-item label="问题类别" prop="issueCategoryId">
        <el-input v-model="dataForm.issueCategoryId" readonly></el-input>
      </el-form-item>
      <el-form-item label="车型" prop="vehicleTypeId">
        <el-input v-model="dataForm.vehicleTypeId" readonly></el-input>
      </el-form-item>
      <el-form-item label="车号" prop="vehicleNumberId">
        <el-input v-model="dataForm.vehicleNumberId" readonly></el-input>
      </el-form-item>
      <el-form-item label="问题描述" prop="issueDescription">
        <el-input v-model="dataForm.issueDescription" readonly></el-input>
      </el-form-item>
      <el-form-item label="问题照片" prop="issuePhoto">
        <el-input v-model="dataForm.issuePhoto" readonly></el-input>
      </el-form-item>
      <el-form-item label="整改要求" prop="rectificationRequirement">
        <el-input v-model="dataForm.rectificationRequirement" readonly></el-input>
      </el-form-item>
      <el-form-item label="要求完成时间" prop="requiredCompletionTime">
        <el-input v-model="dataForm.requiredCompletionTime" readonly></el-input>
      </el-form-item>
      <el-form-item label="责任科室" prop="responsibleDepartment">
        <el-input v-model="dataForm.responsibleDepartment" readonly></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible1 = false">关闭</el-button>
    </span>
  </el-dialog>
    <el-dialog
      :title="'问题重写'"
      :close-on-click-modal="false"
      :visible.sync="visible">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="80px">
        <el-form-item label="原因分析" prop="causeAnalysis">
          <el-input v-model="dataForm.causeAnalysis" placeholder="原因分析"></el-input>
        </el-form-item>
        <el-form-item label="整改情况" prop="rectificationStatus">
          <el-input v-model="dataForm.rectificationStatus" placeholder="整改情况"></el-input>
        </el-form-item>
        <el-form-item label="实际完成时间" prop="actualCompletionTime">
          <el-input v-model="dataForm.actualCompletionTime" placeholder="实际完成时间"></el-input>
        </el-form-item>
        <el-form-item label="整改照片交付物" prop="rectificationPhotoDeliverable">
          <el-input v-model="dataForm.rectificationPhotoDeliverable" placeholder="整改照片交付物"></el-input>
        </el-form-item>
        <el-form-item label="整改责任人" prop="rectificationResponsiblePerson">
          <el-input v-model="dataForm.rectificationResponsiblePerson" placeholder="整改责任人"></el-input>
        </el-form-item>
        <el-form-item label="关联问题整改记录" prop="rectificationVerificationStatus">
          <el-input v-model="dataForm.rectificationVerificationStatus" placeholder="关联问题整改记录"></el-input>
        </el-form-item>
        <el-form-item label="整改验证情况" prop="rectificationVerificationStatus">
          <el-input v-model="dataForm.rectificationVerificationStatus" placeholder="整改验证情况"></el-input>
        </el-form-item>
        <el-form-item label="验证结论" prop="verificationConclusion">
          <el-input v-model="dataForm.verificationConclusion" placeholder="验证结论"></el-input>
        </el-form-item>
        <el-form-item label="验证人" prop="verifier">
          <el-input v-model="dataForm.verifier" placeholder="验证人"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="revisible = false">关闭</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        visible1: false,
        revisible: false,
        dataForm: {
          issueId: 0,
          serialNumber: '',
          issueNumber: '',
          inspectionDepartment: '',
          inspectionDate: '',
          issueCategoryId: '',
          vehicleTypeId: '',
          vehicleNumberId: '',
          issueDescription: '',
          issuePhoto: '',
          rectificationRequirement: '',
          requiredCompletionTime: '',
          responsibleDepartment: '',
          rectificationStatus: '',
          actualCompletionTime: '',
          rectificationPhotoDeliverable: '',
          rectificationResponsiblePerson: '',
          requiredSecondRectificationTime: '',
          remark: '',
          creator: '',
          creationTime: '',
          lastModifier: '',
          lastModificationTime: '',
          associatedRectificationRecords: '',
          associatedIssueAddition: '',
          creationDuration: '',
          causeAnalysis: '',
          rectificationVerificationStatus: '',
          verificationConclusion: '',
          verifier: '',
          formula: ''
        },
        dataRule: {
        }
      }
    },
    methods: {
      // reuse (id) {
      //   this.dataForm.issueId = id || 0
      //   this.revisible = true
      //   this.$nextTick(() => {
      //     this.$refs['dataForm'].resetFields()
      //     if (this.dataForm.issueId) {
      //       this.$http({
      //         url: this.$http.adornUrl(`/generator/issuetable/info/${this.dataForm.issueId}`),
      //         method: 'get',
      //         params: this.$http.adornParams()
      //       }).then(({data}) => {
      //         if (data && data.code === 0) {
      //           this.dataForm.serialNumber = data.issueTable.serialNumber
      //           this.dataForm.issueNumber = data.issueTable.issueNumber
      //           this.dataForm.inspectionDepartment = data.issueTable.inspectionDepartment
      //           this.dataForm.inspectionDate = data.issueTable.inspectionDate
      //           this.dataForm.issueCategoryId = data.issueTable.issueCategoryId
      //           this.dataForm.vehicleTypeId = data.issueTable.vehicleTypeId
      //           this.dataForm.vehicleNumberId = data.issueTable.vehicleNumberId
      //           this.dataForm.issueDescription = data.issueTable.issueDescription
      //           this.dataForm.issuePhoto = data.issueTable.issuePhoto
      //           this.dataForm.rectificationRequirement = data.issueTable.rectificationRequirement
      //           this.dataForm.requiredCompletionTime = data.issueTable.requiredCompletionTime
      //           this.dataForm.responsibleDepartment = data.issueTable.responsibleDepartment
      //           this.dataForm.rectificationStatus = data.issueTable.rectificationStatus
      //           this.dataForm.actualCompletionTime = data.issueTable.actualCompletionTime
      //           this.dataForm.rectificationPhotoDeliverable = data.issueTable.rectificationPhotoDeliverable
      //           this.dataForm.rectificationResponsiblePerson = data.issueTable.rectificationResponsiblePerson
      //           this.dataForm.requiredSecondRectificationTime = data.issueTable.requiredSecondRectificationTime
      //           this.dataForm.remark = data.issueTable.remark
      //           this.dataForm.creator = data.issueTable.creator
      //           this.dataForm.creationTime = data.issueTable.creationTime
      //           this.dataForm.lastModifier = data.issueTable.lastModifier
      //           this.dataForm.lastModificationTime = data.issueTable.lastModificationTime
      //           this.dataForm.associatedRectificationRecords = data.issueTable.associatedRectificationRecords
      //           this.dataForm.associatedIssueAddition = data.issueTable.associatedIssueAddition
      //           this.dataForm.creationDuration = data.issueTable.creationDuration
      //           this.dataForm.causeAnalysis = data.issueTable.causeAnalysis
      //           this.dataForm.rectificationVerificationStatus = data.issueTable.rectificationVerificationStatus
      //           this.dataForm.verificationConclusion = data.issueTable.verificationConclusion
      //           this.dataForm.verifier = data.issueTable.verifier
      //           this.dataForm.formula = data.issueTable.formula
      //         }
      //       })
      //     }
      //   })
      // },
      init (id) {
        this.dataForm.issueId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issueId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetable/info/${this.dataForm.issueId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.serialNumber = data.issueTable.serialNumber
                this.dataForm.issueNumber = data.issueTable.issueNumber
                this.dataForm.inspectionDepartment = data.issueTable.inspectionDepartment
                this.dataForm.inspectionDate = data.issueTable.inspectionDate
                this.dataForm.issueCategoryId = data.issueTable.issueCategoryId
                this.dataForm.vehicleTypeId = data.issueTable.vehicleTypeId
                this.dataForm.vehicleNumberId = data.issueTable.vehicleNumberId
                this.dataForm.issueDescription = data.issueTable.issueDescription
                this.dataForm.issuePhoto = data.issueTable.issuePhoto
                this.dataForm.rectificationRequirement = data.issueTable.rectificationRequirement
                this.dataForm.requiredCompletionTime = data.issueTable.requiredCompletionTime
                this.dataForm.responsibleDepartment = data.issueTable.responsibleDepartment
                this.dataForm.rectificationStatus = data.issueTable.rectificationStatus
                this.dataForm.actualCompletionTime = data.issueTable.actualCompletionTime
                this.dataForm.rectificationPhotoDeliverable = data.issueTable.rectificationPhotoDeliverable
                this.dataForm.rectificationResponsiblePerson = data.issueTable.rectificationResponsiblePerson
                this.dataForm.requiredSecondRectificationTime = data.issueTable.requiredSecondRectificationTime
                this.dataForm.remark = data.issueTable.remark
                this.dataForm.creator = data.issueTable.creator
                this.dataForm.creationTime = data.issueTable.creationTime
                this.dataForm.lastModifier = data.issueTable.lastModifier
                this.dataForm.lastModificationTime = data.issueTable.lastModificationTime
                this.dataForm.associatedRectificationRecords = data.issueTable.associatedRectificationRecords
                this.dataForm.associatedIssueAddition = data.issueTable.associatedIssueAddition
                this.dataForm.creationDuration = data.issueTable.creationDuration
                this.dataForm.causeAnalysis = data.issueTable.causeAnalysis
                this.dataForm.rectificationVerificationStatus = data.issueTable.rectificationVerificationStatus
                this.dataForm.verificationConclusion = data.issueTable.verificationConclusion
                this.dataForm.verifier = data.issueTable.verifier
                this.dataForm.formula = data.issueTable.formula
              }
            })
          }
        })
      },

      // 新增根据问题编号查询并显示数据的函数
      showDataByIssueNumber(issueNumber) {
        this.visible1 = true;
        this.$nextTick(() => {
          this.$http({
            url: this.$http.adornUrl(`/generator/issuetable/infoByIssueNumber/${issueNumber}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              // 将获取的数据填入 dataForm
              this.dataForm = data.issueTable; // 直接覆盖
            } else {
              this.$message.error(data.msg);
            }
          });
        });
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetable/${!this.dataForm.issueId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'issueId': this.dataForm.issueId || undefined,
                'serialNumber': this.dataForm.serialNumber,
                'issueNumber': this.dataForm.issueNumber,
                'inspectionDepartment': this.dataForm.inspectionDepartment,
                'inspectionDate': this.dataForm.inspectionDate,
                'issueCategoryId': this.dataForm.issueCategoryId,
                'vehicleTypeId': this.dataForm.vehicleTypeId,
                'vehicleNumberId': this.dataForm.vehicleNumberId,
                'issueDescription': this.dataForm.issueDescription,
                'issuePhoto': this.dataForm.issuePhoto,
                'rectificationRequirement': this.dataForm.rectificationRequirement,
                'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                'responsibleDepartment': this.dataForm.responsibleDepartment,
                'rectificationStatus': this.dataForm.rectificationStatus,
                'actualCompletionTime': this.dataForm.actualCompletionTime,
                'rectificationPhotoDeliverable': this.dataForm.rectificationPhotoDeliverable,
                'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
                'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
                'remark': this.dataForm.remark,
                'creator': this.dataForm.creator,
                'creationTime': this.dataForm.creationTime,
                'lastModifier': this.dataForm.lastModifier,
                'lastModificationTime': this.dataForm.lastModificationTime,
                'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
                'associatedIssueAddition': this.dataForm.associatedIssueAddition,
                'creationDuration': this.dataForm.creationDuration,
                'causeAnalysis': this.dataForm.causeAnalysis,
                'rectificationVerificationStatus': this.dataForm.rectificationVerificationStatus,
                'verificationConclusion': this.dataForm.verificationConclusion,
                'verifier': this.dataForm.verifier,
                'formula': this.dataForm.formula
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
