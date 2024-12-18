<template>
  <div>
  <el-dialog
    :title="'整改记录'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px">
      <el-form-item label="原因分析" prop="causeAnalysis">
        <el-input v-model="dataForm.causeAnalysis" placeholder="原因分析"></el-input>
      </el-form-item>
    <el-form-item label="整改情况" prop="rectificationStatus">
      <el-input v-model="dataForm.rectificationStatus" placeholder="整改情况"></el-input>
    </el-form-item>
      <el-form-item label="实际完成时间" prop="actualCompletionTime">
        <el-date-picker
          v-model="dataForm.actualCompletionTime"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择实际完成时间">
        </el-date-picker>
      </el-form-item>
<!--      <el-form-item label="整改责任人" prop="rectificationResponsiblePerson">-->
<!--        <el-select v-model="dataForm.rectificationResponsiblePerson" filterable placeholder="请选择验证人">-->
<!--          <el-option-group v-for="group in options" :key="group.label" :label="group.label">-->
<!--            <el-option v-for="item in group.options" :key="item.value" :label="item.label"-->
<!--                       :value="item.label">-->
<!--            </el-option>-->
<!--          </el-option-group>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="整改责任人" prop="rectificationResponsiblePerson">
        <el-select v-model="selectedResponsiblePersons" filterable multiple placeholder="请选择整改责任人">
          <el-option-group v-for="group in options" :key="group.label" :label="group.label">
            <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.label">
            </el-option>
          </el-option-group>
        </el-select>
      </el-form-item>
      <!--      <el-form-item label="整改图片/交付物" prop="image">-->
<!--        <el-upload-->
<!--          action="#"-->
<!--          list-type="picture-card"-->
<!--          :on-preview="handlePictureCardPreview"-->
<!--          :on-remove="handleRemove"-->
<!--          :on-change="handleFileChange"-->
<!--          :file-list="imageList"-->
<!--          :auto-upload="false">-->
<!--          <i class="el-icon-plus"></i>-->
<!--        </el-upload>-->
<!--        <el-dialog :visible.sync="dialogVisible">-->
<!--          <img width="100%" :src="dialogImageUrl" alt="">-->
<!--        </el-dialog>-->
<!--      </el-form-item>-->
      <el-form-item label="" prop="rectificationPhotoDeliverable">
        <el-button @click="imageload()">上传图片</el-button>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      </span>
  </el-dialog>
    <el-dialog
      :title="'任务发起'"
      :close-on-click-modal="false"
      :visible.sync="visible1">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
        <el-form-item label="审核人" prop="reviewers">
          <el-select v-model="dataForm.reviewers" filterable placeholder="请选择审核人">
            <el-option-group v-for="group in options" :key="group.value" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                         :value="item.value">
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>

        <el-form-item
          v-for="(subtask, index) in dataForm.subtasks"
          :key="subtask.key"
          :label="'子任务 ' + (index + 1)"
          :prop="'subtasks.' + index + '.name'"
          :rules="{ required: true, message: '子任务不能为空', trigger: 'blur' }">
          <el-input v-model="subtask.name" placeholder="请输入子任务"></el-input>
          <el-select v-model="subtask.assignee" filterable placeholder="请选择接收人">
            <el-option-group v-for="group in options" :key="group.value" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                         :value="item.value">
              </el-option>
            </el-option-group>
          </el-select>
          <el-form-item label="" prop="superiorMask">
            <el-select v-model="subtask.parentTask" placeholder="请选择上级任务">
              <el-option
                key="question"
                label="问题"
                value="0">
              </el-option>
              <el-option
                v-for="item in taskList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-button @click.prevent="removeSubtask(subtask)">删除</el-button>
          <el-button type="primary" @click="addSubtask">增加子任务</el-button>

        </el-form-item>

<!--        <el-form-item>-->
<!--          <el-button @click="resetForm('dataForm')">重置</el-button>-->
<!--        </el-form-item>-->
        <el-form-item label="要求完成日期" prop="requiredCompletionTime">
          <el-date-picker
            v-model="dataForm.requiredCompletionTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="cancel1">取消</el-button>
      <el-button type="primary" @click="submitForm('dataForm')">提交</el-button>
    </span>
    </el-dialog>

    <el-dialog
      title="上传文件"
      :close-on-click-modal="false"
      @close="closeHandle"
      :visible.sync="visibleUpload">
      <el-upload
        drag
        :action="url"
        :before-upload="beforeUploadHandle"
        :on-success="successHandle"
        multiple
        :file-list="fileList"
        style="text-align: center;">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只支持jpg、png、gif格式的图片！</div>
      </el-upload>
    </el-dialog>
    <!--  </div>-->
    <!--  <div>-->
  </div>

</template>

<script>
  // import axios from 'axios'
  import init1 from '../issueset/issuetable-add-or-update.vue'

  export default {
    data () {
      return {
        // 新增数组来存放选择的整改责任人ID
        selectedResponsiblePersons: [], // 存放选中的责任人ID
        bloburl: '',
        imageurl: '',
        visible: false,
        visible1: false,
        file: null,
        dialogImageUrl: '',
        imageList: [],
        dialogVisible: false,
        visibleUpload: false, // 上传对话框的可见性
        url: '',
        num: 0,
        successNum: 0,
        fileList: [],
        masknumber: 0,
        dataForm: {
          userinfo: '',
          vehicles: [{ vehicleTypeId: '', vehicleNumber: '', key: Date.now() }],
          subtasks: [{ name: '', assignee: '', parentTask: '', serialNumber: '', key: Date.now() }],          vehicleTypeIds: [],
          vehicleNumbers: [],
          associatedIssueIds: [],
          imageList: [],
          dialogImageUrl: '',
          issueCategoryOptions: [],
          issueId: 0,
          serialNumber: '',
          issueNumber: '',
          issuecreator: '',
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
          superiorMask: '',
          nextMask: '',
          formula: ''
        },
        vehicleTypeOptions: [],
        issueCategoryOptions: [],
        selectedIssue: '',
        issueOptions: [], // 所有问题编号选项
        taskList: [], // 任务列表
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
      this.$http({
        url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
        method: 'get',
      }).then(({ data }) => {
        this.options = data;
        // console.log("所有的用户信息" ,data);
      })
    },
    activated () {
        this.fetchuserinform()
    },
    methods: {
      imageload() {
        this.url = this.$http.adornUrl(`/sys/oss/upload?token=${this.$cookie.get('token')}`);
        this.visibleUpload = true; // 打开上传对话框
      },
      // 上传之前
      beforeUploadHandle(file) {
        if (file.type !== 'image/jpg' && file.type !== 'image/jpeg' && file.type !== 'image/png' && file.type !== 'image/gif') {
          this.$message.error('只支持jpg、png、gif格式的图片！');
          return false;
        }
        this.num++;
      },
      // 上传成功
      successHandle(response, file, fileList) {
        this.fileList = fileList;
        this.successNum++;
        if (response && response.code === 0) {
          console.log('获取图片的返回' ,response)
          console.log('获取图片的返回' ,response.code)
          this.dataForm.rectificationPhotoDeliverable = response.url; // 假设返回的数据中包含图片地址
          if (this.num === this.successNum) {
            this.$confirm('操作成功, 是否继续操作?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).catch(() => {
              this.visibleUpload = false; // 关闭上传对话框
            });
          }
        } else {
          this.$message.error(response.msg);
        }
      },
      // 弹窗关闭时
      closeHandle() {
        this.fileList = []; // 清空文件列表
        this.$emit('refreshDataList'); // 可选：触发数据刷新
      },
      // generateSerialNumber() {
      //   const now = new Date();
      //   const year = now.getFullYear();
      //   const month = String(now.getMonth() + 1).padStart(2, '0');
      //   const day = String(now.getDate()).padStart(2, '0');
      //   const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0');
      //   console.log('执行随机+++++')
      //   return `ZL-TA-${year}${month}${day}-${random}`;
      // },
      //问题任务编号
      async generateSerialNumber(issuenumber) {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        // console.log('Successfully fetched new issue number:', issuenumber);
        try {
          // 向后端发送请求，获取新的随机数（四字符字符串）
          const response = await this.$http({
            url: this.$http.adornUrl(`/generator/issuemasktable/newIssueNumber`),
            method: 'get',
            params: { issuenumber }, // 使用 params 传递参数
          });
          // console.log('Successfully fetched new issue number:', response.data.useID);
          this.masknumber = response.data.useID;
          // 返回问题编号，确保 random 是从后端返回的字符串
          const random = response.data.useID;
          return `ZL-IS-${year}${month}${day}-${random}`;
        } catch (error) {
          console.error('Failed to fetch new issue number:', error);
          throw new Error('Failed to generate serial number');
        }
      },
      generateSerialNumber1() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');

        // 将 masknumber 转换为整数加 1
        const newNumber = parseInt(this.masknumber, 10) + 1;

        // 将加 1 后的结果格式化为四位字符
        this.masknumber = newNumber.toString().padStart(4, '0');

        console.log('Successfully fetched issue number:', this.masknumber);

        return `ZL-IS-${year}${month}${day}-${this.masknumber}`;
      },

      cancel1 () {
        // 重置 subtasks 数组，只保留一个初始组合
        this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
        this.visible1 = false // 关闭对话框或重置其他状态
      },
      addSubtask () {
        const serialNumber = this.generateSerialNumber1();
        console.log('Successfully setnumber:', serialNumber)
        this.dataForm.subtasks.push({
          name: '',
          assignee: '',
          parentTask: '',
          serialNumber: serialNumber,
          key: Date.now()
        })

      },
      removeSubtask (subtask) {
        let index = this.dataForm.subtasks.indexOf(subtask)
        this.masknumber = this.masknumber - 1;
        if (index !== -1) {
          this.dataForm.subtasks.splice(index, 1)
        }
      },
      //获取用户人信息
      fetchuserinform () {
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/useinfo'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            // console.log('Successfully fetched user info1:', data.userinfo)
            this.dataForm.userinfo = data.userinfo // 将后端返回的 userinfo 赋值给 this.userinfo
            // console.log('Successfully fetched user info2:', this.dataForm.userinfo)
            this.dataForm.creator = this.dataForm.userinfo
            // console.log('Successfully fetched user info3:', this.dataForm.creator)
          } else {
            console.error('Failed to fetch vehicle types:', data.msg)
          }
        }).catch(error => {
          console.error('There was an error fetching the vehicle types!', error)
        })
      },
      // submitForm (formName) {
      //   this.fetchuserinform()
      //   console.log('Successfully fetched user info4:', this.dataForm.creator)
      //   this.$refs['dataForm'].validate((valid) => {
      //     if (valid) {
      //       this.dataForm.subtasks.forEach(subtask => {
      //         this.$http({
      //           url: this.$http.adornUrl(`/generator/issuemasktable/save`),
      //           method: 'post',
      //           data: this.$http.adornData({
      //             'issuemaskId': this.dataForm.issuemaskId || undefined,
      //             'serialNumber': subtask.serialNumber,
      //             'issueNumber': this.dataForm.issueNumber,
      //             'reviewers': this.dataForm.reviewers,
      //             'recipients': subtask.assignee,
      //             'maskcontent': subtask.name,
      //             'creator': this.dataForm.creator,
      //             'creationTime': this.dataForm.creationTime,
      //             'requiredCompletionTime': this.dataForm.requiredCompletionTime,
      //             'superiorMask': subtask.parentTask, // 确保上级任务数据被包含                  'nextMask': this.dataForm.nextMask,
      //             'state': '审核中'
      //           })
      //         }).then(({data}) => {
      //           if (data && data.code === 0) {
      //             this.$message({
      //               message: '操作成功',
      //               type: 'success',
      //               duration: 1500,
      //               onClose: () => {
      //                 this.visible = false
      //                 this.$emit('refreshDataList')
      //               }
      //             })
      //           } else {
      //             this.$message.error(data.msg)
      //           }
      //         })
      //         console.log('时间:', this.dataForm.requiredCompletionTime)
      //         console.log('发起人:', this.dataForm.userinfo)
      //       })
      //     }
      //     // 重置 subtasks 数组，只保留一个初始组合
      //     this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
      //     this.visible1 = false // 关闭对话框或重置其他状态
      //   })
      // },
      submitForm(formName) {
        this.fetchuserinform();
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dataForm.subtasks.forEach(subtask => {
              // 获取审核人和发起人的 ID
              const receiverId = this.getUserIdByUsername(this.dataForm.reviewers);
              const creatorId = this.getUserIdByUsername(this.dataForm.creator);
              console.log("用户的id为：" ,receiverId)
              this.$http({
                url: this.$http.adornUrl(`/generator/issuemasktable/save`),
                method: 'post',
                data: this.$http.adornData({
                  'issuemaskId': this.dataForm.issuemaskId || undefined,
                  'serialNumber': subtask.serialNumber,
                  'issueNumber': this.dataForm.issueNumber,
                  'reviewers': this.dataForm.reviewers,
                  'recipients': subtask.assignee,
                  'maskcontent': subtask.name,
                  'creator': this.dataForm.creator,
                  'issuecreator': this.dataForm.issuecreator,
                  'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
                  'creationTime': this.dataForm.creationTime,
                  'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                  'superiorMask': subtask.parentTask,
                  'nextMask': this.dataForm.nextMask,
                  'state': '审核中'
                })
              }).then(({ data }) => {
                if (data && data.code === 0) {
                  this.$message({
                    message: '操作成功',
                    type: 'success',
                    duration: 1500,
                    onClose: () => {
                      this.visible = false;
                      this.$emit('refreshDataList');
                    }
                  });

                  // 发送消息通知给审核人
                  this.$http({
                    url: this.$http.adornUrl(`/notice/save`),
                    method: 'post',
                    data: this.$http.adornData({
                      'receiverId': receiverId, // 审核人ID
                      'senderId': creatorId, // 发起人ID
                      'content': `有新的任务需要审核`, // 消息内容
                      'type': '任务审核' // 消息类型
                    })
                  });
                } else {
                  this.$message.error(data.msg);
                }
              });

              console.log('时间:', this.dataForm.requiredCompletionTime);
              console.log('发起人:', this.dataForm.userinfo);
            });
          }
          // 重置 subtasks 数组，只保留一个初始组合
          this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }];
          this.visible1 = false; // 关闭对话框或重置其他状态
        });
      },
      getUserIdByUsername(username) {
        for (const category of this.options) {
          for (const auditor of category.options) {
            if (auditor.label === username) { // 根据标签匹配
              return auditor.value; // 返回对应的ID
            }
          }
        }
        return null; // 如果没有找到，返回null
      },
      resetForm (formName) {
        // 重置 subtasks 数组，只保留一个初始组合
        this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
      },
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
      fetchTasksByIssueNumber (issueNumber) {
        this.$http({
          url: this.$http.adornUrl('/generator/issuemasktable/issuemasklist'),
          method: 'get',
          params: this.$http.adornParams({ issueNumber: issueNumber })
        }).then(({data}) => {
          if (data && data.code === 0) {
            // 过滤任务列表，只保留所述问题编号与当前问题编号相同的问题
            this.taskList = data.issues.map(issue => ({
              value: issue.serialNumber,
              label: issue.serialNumber
            }))
          } else {
            console.error('Failed to fetch tasks:', data.msg)
          }
        }).catch(error => {
          console.error('There was an error fetching the tasks!', error)
        })
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
                this.dataForm.superiorMask = data.issueMaskTable.superiorMask
                this.dataForm.nextMask = data.issueMaskTable.nextMask
                this.dataForm.formula = data.issueTable.formula
                // 设置关联问题
                // this.dataForm.associatedIssueIds = data.issueTable.associatedIssueAddition ? data.issueTable.associatedIssueAddition.split(',') : [] // 将逗号分隔的字符串转换为数组
              }
            })
          }
        })
      },
      async init1 (id,issueNumber) {
        this.fetchTasksByIssueNumber(issueNumber)
        this.dataForm.issueId = id || 0
        this.visible1 = true
        console.log("开始获取任务序号++++++++")
        // 为第一个子任务生成序列号
        // this.dataForm.subtasks = [{ name: '', assignee: '', parentTask: '', serialNumber: this.generateSerialNumber(issueNumber), key: Date.now() }];
        this.dataForm.subtasks = [
          {
            name: '',
            assignee: '',
            parentTask: '',
            serialNumber: await this.generateSerialNumber(issueNumber), // 使用 await 等待结果
            key: Date.now(),
          },
        ];
        console.log('Successfully fetched 序号:', this.dataForm.subtasks);
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
                this.dataForm.issuecreator = data.issueTable.creator
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
                this.dataForm.superiorMask = data.issueMaskTable.superiorMask
                this.dataForm.nextMask = data.issueMaskTable.nextMask
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
            // if (!this.imageurl) {
            //   this.$message.error('请上传图片')
            //   return
            // }
            // 创建 vehicleTypeIds 数组
            this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId)
            this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber)
            // 将选中的责任人ID数组转换为字符串
            this.dataForm.rectificationResponsiblePerson = this.selectedResponsiblePersons.join(',');
            console.log('整改责任人:', this.dataForm.rectificationResponsiblePerson);
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
                  // 'serialNumber': this.dataForm.serialNumber,
                  // 'issueNumber': this.dataForm.issueNumber,
                  // 'inspectionDepartment': this.dataForm.inspectionDepartment,
                  // 'inspectionDate': this.dataForm.inspectionDate,
                  // 'issueCategoryId': this.dataForm.issueCategoryId, // 使用转换后的字符串
                  // 'vehicleTypeId': this.dataForm.vehicleTypeIds,
                  // 'vehicleNumberId': this.dataForm.vehicleNumbers,
                  // 'issueDescription': this.dataForm.issueDescription,
                  // 'issuePhoto': this.dataForm.issuePhoto,
                  // 'rectificationRequirement': this.dataForm.rectificationRequirement,
                  // 'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                  // 'responsibleDepartment': this.dataForm.responsibleDepartment,
                  'rectificationStatus': this.dataForm.rectificationStatus,
                  'actualCompletionTime': this.dataForm.actualCompletionTime,
                  'rectificationPhotoDeliverable': this.dataForm.rectificationPhotoDeliverable,
                  'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
                  // 'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
                  // 'remark': this.dataForm.remark,
                  // 'creator': this.dataForm.creator,
                  // 'creationTime': this.dataForm.creationTime,
                  // 'lastModifier': this.dataForm.lastModifier,
                  // 'lastModificationTime': this.dataForm.lastModificationTime,
                  // 'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
                  // 'associatedIssueAddition': this.dataForm.associatedIssueIds, // 将数组转换为逗号分隔的字符串
                  // 'creationDuration': this.dataForm.creationDuration,
                  'causeAnalysis': this.dataForm.causeAnalysis,
                  // 'rectificationVerificationStatus': this.dataForm.rectificationVerificationStatus,
                  // 'verificationConclusion': this.dataForm.verificationConclusion,
                  // 'verifier': this.dataForm.verifier,
                  // 'reviewers': this.dataForm.reviewers,
                  // 'level': this.dataForm.level,
                  'state': '暂停',
                  // 'superiorMask': this.dataForm.superiorMask,
                  // 'nextMask': this.dataForm.nextMask,
                  // 'formula': this.dataForm.formula
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
