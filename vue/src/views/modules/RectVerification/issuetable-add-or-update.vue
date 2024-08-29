<template>
  <div>
  <el-dialog
    :title="!dataForm.issueId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px">
<!--    <el-form-item label="序号" prop="serialNumber">-->
<!--      <el-input v-model="dataForm.serialNumber" placeholder="序号"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="问题编号" prop="issueNumber">-->
<!--      <el-input v-model="dataForm.issueNumber" placeholder="问题编号"></el-input>-->
<!--    </el-form-item>-->
<!--      <el-form-item label="检查科室" prop="inspectionDepartment">-->
<!--        <el-select v-model="dataForm.inspectionDepartment" placeholder="请选择检查科室">-->
<!--          <el-option-->
<!--            v-for="department in departmentOptions"-->
<!--            :key="department.value"-->
<!--            :label="department.label"-->
<!--            :value="department.value">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--    <el-form-item label="检查日期" prop="inspectionDate">-->
<!--      <el-input v-model="dataForm.inspectionDate" placeholder="检查日期"></el-input>-->
<!--    </el-form-item>-->
<!--      :before-upload="handleFileChange"-->
<!--      <el-form-item label="检查日期" prop="inspectionDate">-->
<!--        <el-date-picker-->
<!--          v-model="dataForm.inspectionDate"-->
<!--          type="date"-->
<!--          placeholder="选择日期">-->
<!--          value-format="yyyy-MM-dd HH:mm:ss">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--            <el-form-item label="检查日期" prop="inspectionDate">-->
<!--              <el-date-picker-->
<!--                v-model="dataForm.inspectionDate"-->
<!--                type="date"-->
<!--                value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                placeholder="请选择上传日期">-->
<!--              </el-date-picker>-->
<!--            </el-form-item>-->
<!--      <el-form-item label="问题类别" prop="issueCategoryId">-->
<!--        <el-select v-model="dataForm.issueCategoryId" placeholder="请选择问题类别">-->
<!--          <el-option-->
<!--            v-for="item in issueCategoryOptions"-->
<!--            :key="item.value"-->
<!--            :label="item.label"-->
<!--            :value="item.value">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="80px">-->
<!--        <div v-for="(vehicle, index) in dataForm.vehicles" :key="vehicle.key">-->
<!--          <el-form-item :label="'车型 ' + (index + 1)" :prop="'vehicles.' + index + '.vehicleTypeId'" :rules="{ required: true, message: '请选择车型', trigger: 'change' }">-->
<!--            <el-select v-model="vehicle.vehicleTypeId" placeholder="请选择车型">-->
<!--              <el-option-->
<!--                v-for="item in vehicleTypeOptions"-->
<!--                :key="item.value"-->
<!--                :label="item.label"-->
<!--                :value="item.value">-->
<!--              </el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--          <el-form-item :label="'车号 ' + (index + 1)" :prop="'vehicles.' + index + '.vehicleNumber'" :rules="{ required: true, message: '请输入车号', trigger: 'blur' }">-->
<!--            <el-input v-model="vehicle.vehicleNumber" placeholder="请输入车号"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-button @click.prevent="removeVehicle(vehicle)">删除</el-button>-->
<!--        </div>-->
<!--        <el-form-item>-->
<!--          <el-button type="primary" @click="addVehicle">新增一组车型和车号</el-button>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--    <el-form-item label="问题描述" prop="issueDescription">-->
<!--      <el-input v-model="dataForm.issueDescription" placeholder="问题描述"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="问题照片" prop="issuePhoto">-->
<!--      <img v-if="dataForm.issuePhoto" :src="dataForm.issuePhoto" alt="问题照片" style="max-width: 100%;">-->
<!--    </el-form-item>-->
<!--    <el-form-item label="整改要求" prop="rectificationRequirement">-->
<!--      <el-input v-model="dataForm.rectificationRequirement" placeholder="整改要求"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="要求完成时间" prop="requiredCompletionTime">-->
<!--      <el-input v-model="dataForm.requiredCompletionTime" placeholder="要求完成时间"></el-input>-->
<!--    </el-form-item>-->
<!--      <el-form-item label="要求完成时间" prop="requiredCompletionTime">-->
<!--        <el-date-picker-->
<!--          v-model="dataForm.requiredCompletionTime"-->
<!--          type="datetime"-->
<!--          placeholder="选择日期">-->
<!--          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="要求完成时间" prop="requiredCompletionTime">-->
<!--        <el-date-picker-->
<!--          v-model="dataForm.requiredCompletionTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--          placeholder="请选择上传日期">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--    <el-form-item label="责任科室" prop="responsibleDepartment">-->
<!--      <el-input v-model="dataForm.responsibleDepartment" placeholder="责任科室"></el-input>-->
<!--    </el-form-item>-->
<!--      <el-form-item label="责任科室" prop="responsibleDepartment">-->
<!--        <el-select v-model="dataForm.responsibleDepartment" placeholder="请选择检查科室">-->
<!--          <el-option-->
<!--            v-for="department in departmentOptions"-->
<!--            :key="department.value"-->
<!--            :label="department.label"-->
<!--            :value="department.value">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="原因分析" prop="causeAnalysis">-->
<!--        <el-input v-model="dataForm.causeAnalysis" placeholder="原因分析"></el-input>-->
<!--      </el-form-item>-->
<!--    <el-form-item label="整改情况" prop="rectificationStatus">-->
<!--      <el-input v-model="dataForm.rectificationStatus" placeholder="整改情况"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="实际完成时间" prop="actualCompletionTime">-->
<!--      <el-input v-model="dataForm.actualCompletionTime" placeholder="实际完成时间"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="整改照片交付物" prop="rectificationPhotoDeliverable">-->
<!--      <el-input v-model="dataForm.rectificationPhotoDeliverable" placeholder="整改照片交付物"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="整改责任人" prop="rectificationResponsiblePerson">-->
<!--      <el-input v-model="dataForm.rectificationResponsiblePerson" placeholder="整改责任人"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="要求二次整改时间" prop="requiredSecondRectificationTime">-->
<!--      <el-input v-model="dataForm.requiredSecondRectificationTime" placeholder="要求二次整改时间"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="备注" prop="remark">-->
<!--      <el-input v-model="dataForm.remark" placeholder="备注"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="创建人" prop="creator">-->
<!--      <el-input v-model="dataForm.creator" placeholder="创建人"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="创建时间" prop="creationTime">-->
<!--      <el-input v-model="dataForm.creationTime" placeholder="创建时间"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="最后修改人" prop="lastModifier">-->
<!--      <el-input v-model="dataForm.lastModifier" placeholder="最后修改人"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="最后修改时间" prop="lastModificationTime">-->
<!--      <el-input v-model="dataForm.lastModificationTime" placeholder="最后修改时间"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="关联问题整改记录" prop="associatedRectificationRecords">-->
<!--      <el-input v-model="dataForm.associatedRectificationRecords" placeholder="关联问题整改记录"></el-input>-->
<!--    </el-form-item>-->
      <!-- 新增关联问题多选框 -->
      <el-form-item label="关联问题整改记录" prop="associatedRectificationRecords">
        <el-select v-model="dataForm.associatedIssueIds" multiple placeholder="请选择关联问题整改记录">
          <el-option
            v-for="item in issueOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
<!--    <el-form-item label="创建时长" prop="creationDuration">-->
<!--      <el-input v-model="dataForm.creationDuration" placeholder="创建时长"></el-input>-->
<!--    </el-form-item>-->

    <el-form-item label="整改验证情况" prop="rectificationVerificationStatus">
      <el-input v-model="dataForm.rectificationVerificationStatus" placeholder="整改验证情况"></el-input>
    </el-form-item>
    <el-form-item label="验证结论" prop="verificationConclusion">
      <el-input v-model="dataForm.verificationConclusion" placeholder="验证结论"></el-input>
    </el-form-item>
<!--    <el-form-item label="验证人" prop="verifier">-->
<!--      <el-input v-model="dataForm.verifier" placeholder="验证人"></el-input>-->
<!--    </el-form-item>-->
      <el-form-item label="验证人" prop="verifier">
        <el-select v-model="dataForm.verifier" filterable placeholder="请选择验证人">
          <el-option-group v-for="group in options" :key="group.label" :label="group.label">
            <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                       :value="item.value">
            </el-option>
          </el-option-group>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="审核人" prop="reviewers">-->
<!--        <el-input v-model="dataForm.reviewers" placeholder="审核人"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="问题等级" prop="level">-->
<!--        <el-input v-model="dataForm.level" placeholder="问题等级"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="问题状态" prop="state">-->
<!--        <el-input v-model="dataForm.state" placeholder="问题状态"></el-input>-->
<!--      </el-form-item>-->
<!--    <el-form-item label="公式" prop="formula">-->
<!--      <el-input v-model="dataForm.formula" placeholder="公式"></el-input>-->
<!--    </el-form-item>-->
      <el-form-item label="整改图片/交付物" prop="image">
        <el-upload
          action="#"
          list-type="picture-card"
          :on-preview="handlePictureCardPreview"
          :on-remove="handleRemove"
          :on-change="handleFileChange"
          :file-list="imageList"
          :auto-upload="false">
          <i class="el-icon-plus"></i>
        </el-upload>
        <el-dialog :visible.sync="dialogVisible">
          <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      </span>
  </el-dialog>
  <el-dialog
    :title="'问题分析'"
    :close-on-click-modal="false"
    :visible.sync="visible1">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
          <el-form-item label="原因分析" prop="causeAnalysis">
            <el-input v-model="dataForm.causeAnalysis" placeholder="原因分析"></el-input>
          </el-form-item>
      <el-form-item label="分析人" prop="lastModifier">
        <el-input v-model="dataForm.lastModifier" placeholder="分析人"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible1 = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
  </div>
</template>

<script>
  // import axios from 'axios'

  export default {
    data () {
      return {
        bloburl: '',
        imageurl: '',
        visible: false,
        visible1: false,
        file: null,
        dialogImageUrl: '',
        imageList: [],
        dialogVisible: false,
        dataForm: {
          userinfo: '',
          vehicles: [{ vehicleTypeId: '', vehicleNumber: '', key: Date.now() }],
          vehicleTypeIds: [],
          vehicleNumbers: [],
          associatedIssueIds: [],
          imageList: [],
          dialogImageUrl: '',
          issueCategoryOptions: [],
          issueId: 0,
          serialNumber: '',
          issueNumber: '',
          inspectionDepartment: '',
          inspectionDate: '',
          issueCategoryId: '',
          vehicleTypeId: '',
          vehicleNumberId: '',
          issueDescription: '',
          vehicleTypeOptions: [],
          vehicleNumberOptions: [],
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
          reviewers: '',
          level: '',
          state: '',
          formula: ''
        },
        vehicleTypeOptions: [],
        issueCategoryOptions: [],
        selectedIssue: '',
        issueOptions: [], // 所有问题编号选项
        departmentOptions: [
          { value: '财务科', label: '财务科' },
          { value: '市场科', label: '市场科' },
          { value: '安环科', label: '安环科' },
          { value: '生产科', label: '生产科' },
          { value: '供应科', label: '供应科' },
          { value: '技术科', label: '技术科' },
          { value: '企管科', label: '企管科' }
          // 其他科室选项
        ],
        dataRule: {
        },
        options: ''
      }
    },
    created () {
      this.fetchIssueCategories()
      this.fetchVehicleTypes()
      this.fetchIssueOptions() // 获取所有问题编号选项
      // 获取分组后的员工数据
      // const response = await fetch('/taskmanagement/user/getEmployeesGroupedByDepartment'); // 假设这是你的 API 路由
      // const data = await response.json();

      this.$http({
        url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
        method: 'get',
      }).then(({ data }) => {
        this.options = data;


        console.log(data);
        // if (data && data.code === 0) {
        //   console.log(data);
        // }
      })

      // console.log(data);
      // this.options = data;
    },
    activated () {

    },
    methods: {
      handlePictureCardPreview (file) {
        this.dialogImageUrl = file.url
        console.log('图片路径：', file.url)
        this.dialogVisible = true
      },
      handleRemove (file, fileList) {
        console.log('移除图片:', file, fileList)
      },
      uploadImage () {
        if (!this.file) {
          this.$message.error('请先选择文件')
          return
        }
        console.log('准备上传文件:', this.file)

        const formData = new FormData()
        formData.append('file', this.file)
        // formData.append('issueId', this.dataForm.issueId)

        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/upload'),
          method: 'post',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '文件上传成功',
              type: 'success'
            })
            // 处理上传成功后的逻辑
            // 例如，更新本地状态或执行其他操作
            const iamgenewURL = data.data
            this.dataForm.issuePhoto = iamgenewURL
          } else {
            this.$message.error('文件上传失败: ' + (data.message || '未知错误'))
          }
        }).catch(error => {
          console.error('文件上传失败:', error)
          if (error.response) {
            // 请求已发出，但服务器响应了状态码
            // 服务器响应的完整数据
            console.error('服务器响应数据:', error.response.data)
            console.error('服务器响应状态码:', error.response.status)
            console.error('服务器响应头:', error.response.headers)
          } else if (error.request) {
            // 请求已发出，但没有收到响应
            // `error.request` 在浏览器中是 XMLHttpRequest 的实例，
            // 在 Node.js 中是 http.ClientRequest 的实例
            console.error('请求未收到响应:', error.request)
          } else {
            // 其他错误
            console.error('设置请求时出错:', error.message)
          }
          this.$message.error('文件上传失败: ' + (error.message || '未知错误'))
        })
      },
      handleFileChange (file) {
        // this.file = file
        // console.log('Successfully fetched pohto:', this.file)
        if (file && file.raw) {
          this.file = file.raw
          const objectURL = URL.createObjectURL(this.file)
          console.log('File URL:', objectURL)
          this.imageurl = objectURL
          this.uploadImage() // 调用 uploadImage 方法
          console.log('File URL:', this.imageurl)
        } else {
          console.error('Invalid file object:', file)
        }
        return false // 阻止自动上传
      },
      cancel () {
        // 重置 vehicles 数组，只保留一个初始组合
        this.dataForm.vehicles = [{ vehicleTypeId: '', vehicleNumber: '', key: Date.now() }]
        this.vehicleNumberOptions = []
        this.visible = false // 关闭对话框或重置其他状态
      },
      addVehicle () {
        this.dataForm.vehicles.push({
          vehicleTypeId: '',
          vehicleNumber: '',
          key: Date.now()
        })
      },
      removeVehicle (vehicle) {
        let index = this.dataForm.vehicles.indexOf(vehicle)
        if (index !== -1) {
          this.dataForm.vehicles.splice(index, 1)
        }
      },
      fetchIssueCategories () {
        this.$http({
          url: this.$http.adornUrl('/generator/issuetypetable/issuestype'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            // console.log('Successfully fetched issue categories:', data.category)
            this.issueCategoryOptions = data.category.map(category => ({
              value: category.concreteIssueCategory,
              label: category.concreteIssueCategory
            }))
          } else {
            console.error('Failed to fetch issue categories:', data.msg)
          }
        }).catch(error => {
          console.error('There was an error fetching the issue categories!', error)
        })
      },
      fetchVehicleTypes () {
        this.$http({
          url: this.$http.adornUrl('/generator/issuecartypetable/carname'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            // console.log('Successfully fetched vehicle types:', data.carType)
            this.vehicleTypeOptions = data.carType.map(carType => ({
              value: carType.concreteVehicleType,
              label: carType.concreteVehicleType
            }))
          } else {
            console.error('Failed to fetch vehicle types:', data.msg)
          }
        }).catch(error => {
          console.error('There was an error fetching the vehicle types!', error)
        })
      },
      fetchIssueOptions () {
        // 获取所有问题编号选项
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/issuesid'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            // console.log('Successfully fetched issue options:', data.issues)
            this.issueOptions = data.issues.map(issue => ({
              value: issue.issueNumber,
              label: issue.issueNumber
            }))
          } else {
            console.error('Failed to fetch issue options:', data.msg)
          }
        }).catch(error => {
          console.error('There was an error fetching the issue options!', error)
        })
      },
      //获取用户人信息
      fetchuserinform () {
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/useinfo'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            // console.log('Successfully fetched user info:', data.userinfo)
            this.dataForm.userinfo = data.userinfo // 将后端返回的 userinfo 赋值给 this.userinfo
            this.dataForm.creator = this.dataForm.userinfo
            // console.log('Successfully fetched user info:', this.userinfo)
          } else {
            console.error('Failed to fetch vehicle types:', data.msg)
          }
        }).catch(error => {
          console.error('There was an error fetching the vehicle types!', error)
        })
      },
      init (id) {
        this.fetchuserinform() //获取用户名
        this.dataForm.issueId = id || 0
        this.visible = true
        // console.log("成功获取用户名：" ,this.dataForm.userinfo)
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
                this.dataForm.vehicleTypeIds = data.issueTable.vehicleTypeId ? data.issueTable.vehicleTypeId.split(',') : [] // 将逗号分隔的字符串转换为数组
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
                // 设置关联问题
                this.dataForm.associatedIssueIds = data.issueTable.associatedIssueAddition ? data.issueTable.associatedIssueAddition.split(',') : [] // 将逗号分隔的字符串转换为数组
              }
            })
          }
        })
      },
      init1 (id) {
        this.dataForm.issueId = id || 0
        this.visible1 = true
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
      // 表单提交
      dataFormSubmit () {
        console.log('Successfully 获得 vehicle:', this.dataForm.vehicles)
        console.log('Successfully fetched photo2:', this.imageurl)
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            // 检查是否上传了图片
            if (!this.imageurl) {
              this.$message.error('请上传图片')
              return
            }
            // 创建 vehicleTypeIds 数组
            this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId)
            this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber)
            console.log('Successfully 获得 vehicle:', this.dataForm.vehicleTypeIds)
            // 确保 issueCategoryId 是一个数组
            if (!Array.isArray(this.dataForm.issueCategoryId)) {
              this.dataForm.issueCategoryId = [this.dataForm.issueCategoryId]
            }
            // 将数组转换为逗号分隔的字符串
            const issueCategoryIdString = this.dataForm.issueCategoryId.join(',')
              this.$http({
                url: this.$http.adornUrl(`/generator/issuetable/${!this.dataForm.issueId ? 'save' : 'update'}`),
                method: 'post',
                data: this.$http.adornData({
                  'issueId': this.dataForm.issueId || undefined,
                  'serialNumber': this.dataForm.serialNumber,
                  'issueNumber': this.dataForm.issueNumber,
                  'inspectionDepartment': this.dataForm.inspectionDepartment,
                  'inspectionDate': this.dataForm.inspectionDate,
                  'issueCategoryId': issueCategoryIdString, // 使用转换后的字符串
                  'vehicleTypeId': this.dataForm.vehicleTypeIds.join(','),
                  'vehicleNumberId': this.dataForm.vehicleNumbers.join(','),
                  'issueDescription': this.dataForm.issueDescription,
                  // 'issuePhoto': this.dataForm.issuePhoto,
                  'rectificationRequirement': this.dataForm.rectificationRequirement,
                  'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                  'responsibleDepartment': this.dataForm.responsibleDepartment,
                  'rectificationStatus': this.dataForm.rectificationStatus,
                  'actualCompletionTime': this.dataForm.actualCompletionTime,
                  'rectificationPhotoDeliverable': this.dataForm.issuePhoto,
                  'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
                  'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
                  'remark': this.dataForm.remark,
                  'creator': this.dataForm.creator,
                  'creationTime': this.dataForm.creationTime,
                  'lastModifier': this.dataForm.lastModifier,
                  'lastModificationTime': this.dataForm.lastModificationTime,
                  'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
                  'associatedIssueAddition': this.dataForm.associatedIssueIds.join(','), // 将数组转换为逗号分隔的字符串
                  'creationDuration': this.dataForm.creationDuration,
                  'causeAnalysis': this.dataForm.causeAnalysis,
                  'rectificationVerificationStatus': this.dataForm.rectificationVerificationStatus,
                  'verificationConclusion': this.dataForm.verificationConclusion,
                  'verifier': this.dataForm.verifier,
                  'reviewers': this.dataForm.reviewers,
                  'level': this.dataForm.level,
                  'state': '审核中',
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
        // 重置 vehicles 数组，只保留一个初始组合
        this.dataForm.vehicles = [{ vehicleTypeId: '', vehicleNumber: '', key: Date.now() }]
        this.vehicleNumberOptions = []
      }

    }
  }
</script>
