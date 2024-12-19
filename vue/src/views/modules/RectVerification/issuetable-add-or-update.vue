<template>
  <div>
  <el-dialog
    :title="!dataForm.issueId ? '新增' : '验证'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px">

            <el-form-item label="整改验证情况" prop="rectificationVerificationStatus">
              <el-input v-model="dataForm.rectificationVerificationStatus" placeholder="整改验证情况"></el-input>
            </el-form-item>
      <el-form-item label="验证结论" prop="isaccessory">
        <el-radio-group v-model="dataForm.isaccessory" @change="handleAccessoryChange">
          <el-radio label="通过">通过</el-radio>
          <el-radio label="未通过">未通过</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="关联相关问题" prop="isRelatedIssue">
        <el-radio-group v-model="dataForm.isRelatedIssue">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
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
    data() {
      return {
        bloburl: '',
        imageurl: '',
        visible: false,
        visible1: false,
        file: null,
        dialogImageUrl: '',
        imageList: [],
        dialogVisible: false,
        newissuenumber: '',
        connectissue: '',
        dataForm: {
          userinfo: '',
          vehicles: [{vehicleTypeId: '', vehicleNumber: '', key: Date.now()}],
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
          systematicClassification: '',
          firstFaultyParts: '',
          secondFaultyParts: '',
          faultType: '',
          faultModel: '',
          vehicleNumberId: '',
          issueDescription: '',
          vehicleTypeOptions: [],
          vehicleNumberOptions: [],
          issuePhoto: '',
          peliminaryAnalysis: '',
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
          verificationDeadline: '',
          causeAnalysis: '',
          rectificationVerificationStatus: '',
          verificationConclusion: '',
          verificationConclusions: [], // 初始化为数组
          verifier: '',
          reviewers: '',
          level: '',
          state: '',
          parentQuestion: '',
          formula: '',
          isRelatedIssue: '否',  // 添加此行以初始化
          isaccessory: '',  // 添加此行以初始化
        },
        verificationOptions: [
          {label: '未完成', value: '未完成'},
          {label: '已完成', value: '已完成'},
          {label: '暂停', value: '暂停'},
          {label: '结项', value: '结项'},
        ],
        vehicleTypeOptions: [],
        issueCategoryOptions: [],
        selectedIssue: '',
        issueOptions: [], // 所有问题编号选项
        departmentOptions: [
          {value: '财务科', label: '财务科'},
          {value: '市场科', label: '市场科'},
          {value: '安环科', label: '安环科'},
          {value: '生产科', label: '生产科'},
          {value: '供应科', label: '供应科'},
          {value: '技术科', label: '技术科'},
          {value: '企管科', label: '企管科'}
          // 其他科室选项
        ],
        dataRule: {  isaccessory: [
            { required: true, message: '请选择验证结论', trigger: 'blur' }
          ]},
        options: ''
      }
    },
    created() {
      this.fetchIssueCategories()
      this.fetchVehicleTypes()
      this.fetchIssueOptions() // 获取所有问题编号选项
      // 获取分组后的员工数据
      // const response = await fetch('/taskmanagement/user/getEmployeesGroupedByDepartment'); // 假设这是你的 API 路由
      // const data = await response.json();

      this.$http({
        url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
        method: 'get',
      }).then(({data}) => {
        this.options = data;


        console.log(data);
        // if (data && data.code === 0) {
        //   console.log(data);
        // }
      })

      // console.log(data);
      // this.options = data;
    },
    activated() {

    },
    methods: {
      //问题编号
      async generateconnectNumber(id, issuenumber) {

        const regex = /^(ZL-IS-\d{8}-\d{4})/;  // 匹配格式：ZL-IS-YYYYMMDD-XXXX

        try {
          const R = await this.$http({
            url: this.$http.adornUrl(`/generator/issuetable/connectNumber`),
            method: 'post',
            headers: {
              'Content-Type': 'application/json', // 设置 Content-Type 为 application/json
            },
            data: { id },  // 通过请求体传递 id
          });
          // 返回问题编号，确保 random 是从后端返回的字符串
          const random2 = R.data.usedID;
          if(random2 === 1){
            return `${issuenumber}-${random2}`;
          }else {
            const match = issuenumber.match(regex);
            if (match) {
              const extracted = match[1]; // 提取到的部分
              console.log(extracted);  // 输出: ZL-IS-20241216-0002
              return `${extracted}-${random2}`;
            } else {
              console.log('No match found');
            }
          }
        } catch (error) {
          console.error('Failed to fetch new issue number:', error);
          throw new Error('Failed to generate serial number');
        }
      },
      //问题编号
      async generateSerialNumber() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');

        try {
          // 向后端发送请求，获取新的随机数（四字符字符串）
          const response = await this.$http({
            url: this.$http.adornUrl(`/generator/issuetable/newIssueNumber`),
            method: 'get',
          });
          // console.log('Successfully fetched new issue number:', response.data.useID);

          // 返回问题编号，确保 random 是从后端返回的字符串
          const random = response.data.useID;
          return `ZL-IS-${year}${month}${day}-${random}`;
        } catch (error) {
          console.error('Failed to fetch new issue number:', error);
          throw new Error('Failed to generate serial number');
        }
      },
      //修改原问题状态
      changeparentquestion(issuenumber) {
        // 检查 issuenumber 是否为空
        // if (!issuenumber) {
        //   this.$message.error("问题编号不能为空！");
        //   return;
        // }

        // 发送请求到后端修改原问题状态
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/updateParentQuestion'),
          method: 'post',
          data: this.$http.adornData({
            issuenumber: issuenumber
          })
        }).then(({ data }) => {
          // 检查后端返回的结果
          // if (data && data.code === 0) {
          //   this.$message.success("原问题状态已成功更新！");
          //   // 可以调用数据刷新方法
          //   this.getDataList && this.getDataList();
          // } else {
          //   this.$message.error(data.msg || "更新失败，请稍后重试！");
          // }
        }).catch(error => {
          console.error("更新原问题状态失败：", error);
          this.$message.error("请求出错，请检查网络或稍后再试！");
        });
      },
      handleAccessoryChange(value) {
        // 检查验证状态是否为 "通过"
        if (value === '通过') {
          const currentDate = new Date();
          const verificationDeadline = new Date(this.dataForm.verificationDeadline); // 确保 verificationDeadline 是 Date 类型

          if (verificationDeadline > currentDate) {
            this.$message.error('验证时间未到，不能选择“通过”');
            // 重置选项
            this.dataForm.isaccessory = '未通过';
          }
        }
      },
      handleSelectChange(selectedValues) {
        // 将选中的值转化为一个 Set 以便检查
        const selectedSet = new Set(selectedValues);

        // 逻辑判断
        if (selectedSet.has('已完成') && selectedSet.has('未完成')) {
          // 如果同时选择了"已完成"和"未完成"，则清除其中一个
          this.dataForm.verificationConclusion = [...selectedSet].filter(item => item !== '未完成');
        }

        if (selectedSet.has('结项')) {
          // 如果选择了"结项"，则清除其他所有选项
          this.dataForm.verificationConclusion = ['结项'];
        }
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url
        console.log('图片路径：', file.url)
        this.dialogVisible = true
      },
      handleRemove(file, fileList) {
        console.log('移除图片:', file, fileList)
      },
      uploadImage() {
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
        }).then(({data}) => {
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
      handleFileChange(file) {
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
      cancel() {
        // 重置 vehicles 数组，只保留一个初始组合
        this.dataForm.vehicles = [{vehicleTypeId: '', vehicleNumber: '', key: Date.now()}]
        this.vehicleNumberOptions = []
        this.visible = false // 关闭对话框或重置其他状态
      },
      addVehicle() {
        this.dataForm.vehicles.push({
          vehicleTypeId: '',
          vehicleNumber: '',
          key: Date.now()
        })
      },
      removeVehicle(vehicle) {
        let index = this.dataForm.vehicles.indexOf(vehicle)
        if (index !== -1) {
          this.dataForm.vehicles.splice(index, 1)
        }
      },
      fetchIssueCategories() {
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
      fetchVehicleTypes() {
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
      fetchIssueOptions() {
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
      fetchuserinform() {
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
      newissue() {
        this.$http({
          url: this.$http.adornUrl(`/generator/issuetable/save`),
          method: 'post',
          data: this.$http.adornData({
            'issueId': undefined,
            // 'serialNumber': this.dataForm.serialNumber,
            'issueNumber': this.connectissue,
            'inspectionDepartment': this.dataForm.inspectionDepartment,
            'inspectionDate': this.dataForm.inspectionDate,
            'issueCategoryId': this.dataForm.issueCategoryId, // 使用转换后的字符串
            'vehicleTypeId': this.dataForm.vehicleTypeId,
            'vehicleNumberId': this.dataForm.vehicleNumberId,
            'issueDescription': this.dataForm.issueDescription,
            'issuePhoto': this.dataForm.issuePhoto,
            'rectificationRequirement': this.dataForm.rectificationRequirement,
            'requiredCompletionTime': this.dataForm.requiredCompletionTime,
            'responsibleDepartment': this.dataForm.responsibleDepartment,
            'systematicClassification': this.dataForm.systematicClassification,
            'faultType': this.dataForm.faultType,
            'faultModel': this.dataForm.faultModel,
            'firstFaultyParts': this.dataForm.firstFaultyParts,
            'secondFaultyParts': this.dataForm.secondFaultyParts,
            'peliminaryAnalysis': this.dataForm.peliminaryAnalysis,
            // 'rectificationStatus': this.dataForm.rectificationStatus,
            // 'actualCompletionTime': this.dataForm.actualCompletionTime,
            // 'rectificationPhotoDeliverable': this.dataForm.issuePhoto,
            'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
            // 'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
            // 'remark': this.dataForm.remark,
            'creator': this.dataForm.creator,
            // 'creationTime': this.dataForm.creationTime,
            // 'lastModifier': this.dataForm.lastModifier,
            // 'lastModificationTime': this.dataForm.lastModificationTime,
            // 'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
            // 'associatedIssueAddition': this.dataForm.associatedIssueIds.join(','), // 将数组转换为逗号分隔的字符串
            // 'creationDuration': this.dataForm.creationDuration,
            // 'causeAnalysis': this.dataForm.causeAnalysis,
            // 'rectificationVerificationStatus': this.dataForm.rectificationVerificationStatus,
            // 'verificationConclusion': this.dataForm.verificationConclusions.join(','),
            // 'verifier': this.dataForm.verifier,
            // 'reviewers': this.dataForm.reviewers,
            'level': '等待整改记录填写',
            'state': '持续',
            'parentQuestion': this.dataForm.parentQuestion
            // 'formula': this.dataForm.formula
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            // 发送消息通知给整改负责人
            this.$http({
              url: this.$http.adornUrl(`/notice/save`),
              method: 'post',
              data: this.$http.adornData({
                'receiverId': this.dataForm.rectificationResponsiblePerson, // 审核人ID
                'senderId': this.dataForm.creator, // 发起人ID
                'content': '问题描述：'+ this.dataForm.issueDescription, // 消息内容
                'type': '问题整改通知', // 消息类型
                'jumpdepart': '1', // 跳转部门
              })
            });
          } else {
          }
        })
      },

      async init(id, issuenumber) {
        console.log("开始初始化")
        // this.fetchuserinform() //获取用户名
        // 生成问题编号
        this.newissuenumber = await this.generateSerialNumber();
        this.connectissue = await this.generateconnectNumber(id, issuenumber);
        console.log("成功生成问题编号：", this.connectissue)
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
                this.dataForm.peliminaryAnalysis = data.issueTable.peliminaryAnalysis
                this.dataForm.systematicClassification = data.issueTable.systematicClassification
                this.dataForm.faultType = data.issueTable.faultType
                this.dataForm.faultModel = data.issueTable.faultModel
                this.dataForm.firstFaultyParts = data.issueTable.firstFaultyParts
                this.dataForm.secondFaultyParts = data.issueTable.secondFaultyParts
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
                this.dataForm.creator = data.issueTable.creator
                this.dataForm.lastModificationTime = data.issueTable.lastModificationTime
                this.dataForm.associatedRectificationRecords = data.issueTable.associatedRectificationRecords
                this.dataForm.associatedIssueAddition = data.issueTable.associatedIssueAddition
                this.dataForm.creationDuration = data.issueTable.creationDuration
                this.dataForm.causeAnalysis = data.issueTable.causeAnalysis
                this.dataForm.rectificationVerificationStatus = data.issueTable.rectificationVerificationStatus
                this.dataForm.verificationConclusion = data.issueTable.verificationConclusion
                this.dataForm.verifier = data.issueTable.verifier
                this.dataForm.formula = data.issueTable.formula
                this.dataForm.verificationDeadline = data.issueTable.verificationDeadline
                this.dataForm.parentQuestion = data.issueTable.parentQuestion
                // 设置关联问题
                this.dataForm.associatedIssueIds = data.issueTable.associatedIssueAddition ? data.issueTable.associatedIssueAddition.split(',') : [] // 将逗号分隔的字符串转换为数组
              console.log("成功获取数据：" ,this.dataForm)
              }
            })
          }
        })

      },
      init1(id) {
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
      dataFormSubmit() {
        // console.log('Successfully 获得 vehicle:', this.dataForm.vehicles)
        // console.log('Successfully fetched photo2:', this.imageurl)
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            // 检查是否上传了图片
            // if (!this.imageurl) {
            //   this.$message.error('请上传图片')
            //   return
            // }
            // 创建 vehicleTypeIds 数组
            // this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId)
            // this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber)
            // console.log('Successfully 获得 vehicle:', this.dataForm.vehicleTypeIds)
            // 确保 issueCategoryId 是一个数组
            // if (!Array.isArray(this.dataForm.issueCategoryId)) {
            //   this.dataForm.issueCategoryId = [this.dataForm.issueCategoryId]
            // }
            // 检查是否通过
            if (this.dataForm.isaccessory === '通过') {
              this.dataForm.verificationConclusion = '结项'
              this.dataForm.level = '结项'
              this.dataForm.state = '已完成'
              if(this.dataForm.parentQuestion !== null){
                this.changeparentquestion(this.dataForm.parentQuestion)
              }
            } else {
              this.dataForm.verificationConclusion = '持续'
              this.dataForm.level = '未通过验证'
              this.dataForm.state = '未完成'
              if(this.dataForm.parentQuestion === '' || this.dataForm.parentQuestion === null){
                this.dataForm.parentQuestion = this.dataForm.issueNumber
                // console.log("父问题为空")
              }
              this.newissue()
            }
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetable/${!this.dataForm.issueId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'issueId': this.dataForm.issueId || undefined,
                // 'serialNumber': this.dataForm.serialNumber,
                // 'issueNumber': this.dataForm.issueNumber,
                // 'inspectionDepartment': this.dataForm.inspectionDepartment,
                // 'inspectionDate': this.dataForm.inspectionDate,
                // 'issueCategoryId': issueCategoryIdString, // 使用转换后的字符串
                // 'vehicleTypeId': this.dataForm.vehicleTypeIds.join(','),
                // 'vehicleNumberId': this.dataForm.vehicleNumbers.join(','),
                // 'issueDescription': this.dataForm.issueDescription,
                // // 'issuePhoto': this.dataForm.issuePhoto,
                // 'rectificationRequirement': this.dataForm.rectificationRequirement,
                // 'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                // 'responsibleDepartment': this.dataForm.responsibleDepartment,
                // 'rectificationStatus': this.dataForm.rectificationStatus,
                // 'actualCompletionTime': this.dataForm.actualCompletionTime,
                // 'rectificationPhotoDeliverable': this.dataForm.issuePhoto,
                // 'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
                // 'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
                // 'remark': this.dataForm.remark,
                // 'creator': this.dataForm.creator,
                // 'creationTime': this.dataForm.creationTime,
                // 'lastModifier': this.dataForm.lastModifier,
                // 'lastModificationTime': this.dataForm.lastModificationTime,
                // 'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
                'associatedIssueAddition': this.dataForm.associatedIssueIds.join(','), // 将数组转换为逗号分隔的字符串
                // 'creationDuration': this.dataForm.creationDuration,
                // 'causeAnalysis': this.dataForm.causeAnalysis,
                'rectificationVerificationStatus': this.dataForm.rectificationVerificationStatus,
                'verificationConclusion': this.dataForm.verificationConclusion,
                'verifier': this.dataForm.verifier,
                // 'reviewers': this.dataForm.reviewers,
                'level': this.dataForm.level,
                'state': this.dataForm.state,
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
                // 检查是否关联相关问题
                if (this.dataForm.isRelatedIssue === '是') {
                  // 发起新的请求
                  this.$http({
                    url: this.$http.adornUrl(`/generator/issuetable/connection`),  // 请替换为实际请求的 URL
                    method: 'post',
                    data: {
                      'issueId': this.dataForm.issueId, // 根据需要传递的参数
                      // ...其他所需数据
                    }
                  }).then(({data}) => {
                    if (data && data.code === 0) {
                    } else {
                      this.$message.error(data.msg);
                    }
                  });
                }
              } else {
                this.$message.error(data.msg)
              }
            })
            // 将数组转换为逗号分隔的字符串
            // const issueCategoryIdString = this.dataForm.issueCategoryId.join(',')
            // const verificationConclusionString = this.dataForm.verificationConclusion.join(',');

          }
        })
        // 重置 vehicles 数组，只保留一个初始组合
        this.dataForm.vehicles = [{vehicleTypeId: '', vehicleNumber: '', key: Date.now()}]
        this.vehicleNumberOptions = []
      }

    }
  }
</script>
