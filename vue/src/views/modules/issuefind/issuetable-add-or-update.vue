<template>
  <div>
  <el-dialog
    :title="!dataForm.issueId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px">

      <el-form-item label="检查科室" prop="inspectionDepartment">
        <el-select v-model="dataForm.inspectionDepartment" filterable placeholder="请选择检查科室">
          <el-option
            v-for="department in departmentOptions"
            :key="department.value"
            :label="department.label"
            :value="department.value">
          </el-option>
        </el-select>
      </el-form-item>
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
            <el-form-item label="检查日期" prop="inspectionDate">
              <el-date-picker
                v-model="dataForm.inspectionDate"
                type="date"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
      <el-form-item label="问题类别" prop="issueCategoryId">
        <el-select v-model="dataForm.issueCategoryId" filterable placeholder="请选择问题类别">
          <el-option
            v-for="item in issueCategoryOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="80px">
        <div v-for="(vehicle, index) in dataForm.vehicles" :key="vehicle.key">
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
          <el-form-item :label="'车型 ' + (index + 1)" :prop="'vehicles.' + index + '.vehicleTypeId'" :rules="{ required: true, message: '请选择车型', trigger: 'change' }" label-width="140px">
            <div style="display: flex; align-items: center; margin-left: 10px;">
              <el-select v-model="vehicle.vehicleTypeId" filterable placeholder="请选择车型" style="flex: 1;">
                <el-option
                  v-for="item in vehicleTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
              <el-button type="danger" @click.prevent="removeVehicle(vehicle)" style="margin-left: 10px;">删除</el-button>
            </div>
          </el-form-item>

          <el-form-item :label="'车号 ' + (index + 1)" :prop="'vehicles.' + index + '.vehicleNumber'" :rules="{ required: true, message: '请输入车号', trigger: 'blur' }" label-width="140px">
            <el-input v-model="vehicle.vehicleNumber" placeholder="请输入车号" style="margin-left: 10px;"></el-input>
          </el-form-item>

          <!--          <el-button @click.prevent="removeVehicle(vehicle)">删除</el-button>-->
        </div>
        <el-form-item>
          <el-button type="primary" @click="addVehicle">新增一组车型和车号</el-button>
        </el-form-item>
      </el-form>
    <el-form-item label="问题描述" prop="issueDescription">
      <el-input v-model="dataForm.issueDescription" placeholder="问题描述"></el-input>
    </el-form-item>
<!--    <el-form-item label="问题照片" prop="issuePhoto">-->
<!--      <img v-if="dataForm.issuePhoto" :src="dataForm.issuePhoto" alt="问题照片" style="max-width: 100%;">-->
<!--    </el-form-item>-->
    <el-form-item label="整改要求" prop="rectificationRequirement">
      <el-input v-model="dataForm.rectificationRequirement" placeholder="整改要求"></el-input>
    </el-form-item>
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
      <el-form-item label="要求完成时间" prop="requiredCompletionTime">
        <el-date-picker
          v-model="dataForm.requiredCompletionTime"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择日期">
        </el-date-picker>
      </el-form-item>
<!--    <el-form-item label="责任科室" prop="responsibleDepartment">-->
<!--      <el-input v-model="dataForm.responsibleDepartment" placeholder="责任科室"></el-input>-->
<!--    </el-form-item>-->
      <el-form-item label="责任科室" prop="responsibleDepartment">
        <el-select v-model="dataForm.responsibleDepartment" filterable placeholder="请选择检查科室">
          <el-option
            v-for="department in departmentOptions"
            :key="department.value"
            :label="department.label"
            :value="department.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-upload
        class="upload-button right-aligned"
        :show-file-list="false"
        :before-upload="beforeUpload"
        :on-change="handleFileChange"
        :auto-upload="false"
      >
        <el-button type="primary">上传图片</el-button>
      </el-upload>


    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取消</el-button>
        <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      </span>
  </el-dialog>
    <el-dialog
      :title="'整改记录'"
      :close-on-click-modal="false"
      :visible.sync="visibleR">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitR()" label-width="150px">
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
        <el-form-item label="整改责任人" prop="rectificationResponsiblePerson">
          <el-select v-model="selectedResponsiblePersons" filterable multiple placeholder="请选择整改责任人">
            <el-option-group v-for="group in options" :key="group.label" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.label">
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="关联相关问题" prop="isRelatedIssue">
          <el-radio-group v-model="dataForm.isRelatedIssue">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-upload
          class="upload-button right-aligned"
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-change="handleFileChange1"
          :auto-upload="false"
        >
          <el-button type="primary">上传图片</el-button>
        </el-upload>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取消</el-button>
        <el-button type="primary" @click="dataFormSubmitR()">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :title="'任务发起'"
      :close-on-click-modal="false"
      :visible.sync="visibleT">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
        <el-form-item label="审核人" prop="reviewers">
          <el-select v-model="dataForm.reviewert" filterable placeholder="请选择审核人">
            <el-option-group v-for="group in options" :key="group.label" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                         :value="item.label">
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
            <el-option-group v-for="group in options" :key="group.label" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                         :value="item.label">
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
      <el-button type="primary" @click="submitFormT ('dataForm')">提交</el-button>
    </span>
    </el-dialog>
    <el-dialog
      :title="'问题重写'"
      :close-on-click-modal="false"
      :visible.sync="revisible">
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
<!--        <el-form-item label="整改照片交付物" prop="rectificationPhotoDeliverable">-->
<!--          <el-input v-model="dataForm.rectificationPhotoDeliverable" placeholder="整改照片交付物"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="整改责任人" prop="rectificationResponsiblePerson">
          <el-input v-model="dataForm.rectificationResponsiblePerson" placeholder="整改责任人"></el-input>
        </el-form-item>
        <el-form-item label="关联问题" prop="rectificationVerificationStatus">
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
        <el-button type="primary" @click="timeReuse ()">确认</el-button>
        <el-button @click="revisible = false">关闭</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :title="'请选择日期'"
      :close-on-click-modal="false"
      :visible.sync="retimevisible">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="80px">
        <el-form-item label="检查日期" prop="inspectionDate">
          <el-date-picker
            v-model="dataForm.inspectionDate"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="要求完成时间" prop="requiredCompletionTime">
          <el-date-picker
            v-model="dataForm.requiredCompletionTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormreuse ()">确认</el-button>
        <el-button @click="retimevisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  // import axios from 'axios'

  export default {
    data () {
      return {
        selectedResponsiblePersons: [], // 存放选中的责任人ID
        bloburl: '',
        imageurl: '',
        visible: false,
        visible1: false,
        visibleR: false,
        visibleT: false,
        revisible: false,
        retimevisible: false,
        file: null,
        dialogImageUrl: '',
        imageList: [],
        dialogVisible: false,
        visibleUpload: false, // 上传对话框的可见性
        url: '',
        num: 0,
        successNum: 0,
        fileList: [],
        dataForm: {
          userinfo: '',
          vehicles: [{ vehicleTypeId: '', vehicleNumber: '', key: Date.now() }],
          subtasks: [{ name: '', assignee: '', parentTask: '', serialNumber: '', key: Date.now() }],
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
          reviewert: '',
          level: '',
          state: '',
          formula: '',
          isRelatedIssue: '否',  // 添加此行以初始化
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


        // console.log(data);
        // if (data && data.code === 0) {
        //   console.log(data);
        // }
      })
    },
    activated () {

    },
    methods: {
      uploadFile(file) {
        const formData = new FormData();
        formData.append('file', file); // 将文件添加到 FormData

        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/upload'), // 替换为实际上传接口
          method: 'post',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data' // 指定为文件上传
          }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            // 保存后端返回的url到变量中
            this.dataForm.issuePhoto = data.uploadurl; // 假设你有一个变量uploadedUrl来保存上传的url
            // console.log('获得的文件地址 ：' ,data.uploadurl)
            this.$message.success('文件上传成功');
            // 处理成功后的逻辑，例如更新状态
          } else {
            this.$message.error(data.msg);
          }
        }).catch(error => {
          this.$message.error('上传失败');
          console.error(error);
        });
      },
      beforeUpload(file) {
        const isImage = file.type.startsWith('image/');
        if (!isImage) {
          this.$message.error('上传图片只能是图片格式!');
        }
        return isImage; // 返回 true 继续上传，返回 false 不上传
      },
      handleFileChange(file) {
        // 存储待上传的文件
        this.uploadingFile = file.raw; // 获取 File 对象
        this.uploadFile(file.raw); // 调用上传方法
      },
      uploadFile1(file) {
        const formData = new FormData();
        formData.append('file', file); // 将文件添加到 FormData

        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/upload'), // 替换为实际上传接口
          method: 'post',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data' // 指定为文件上传
          }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            // 保存后端返回的url到变量中
            this.dataForm.rectificationPhotoDeliverable = data.uploadurl; // 假设你有一个变量uploadedUrl来保存上传的url
            // console.log('获得的文件地址 ：' ,data.uploadurl)
            this.$message.success('文件上传成功');
            // 处理成功后的逻辑，例如更新状态
          } else {
            this.$message.error(data.msg);
          }
        }).catch(error => {
          this.$message.error('上传失败');
          console.error(error);
        });
      },
      handleFileChange1(file) {
        // 存储待上传的文件
        this.uploadingFile = file.raw; // 获取 File 对象
        this.uploadFile1(file.raw); // 调用上传方法
      },
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
          // console.log('获取图片的返回', response);
          // console.log('获取图片的返回', response.code);
          this.dataForm.issuePhoto = response.url; // 假设返回的数据中包含图片地址

          // 显示操作成功的消息
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500 // 提示的持续时间（毫秒）
          });
          // 直接关闭上传对话框
          this.visibleUpload = false; // 关闭上传对话框
        } else {
          this.$message.error(response.msg);
        }
      },
      // 弹窗关闭时
      closeHandle() {
        this.fileList = []; // 清空文件列表
        this.$emit('refreshDataList'); // 可选：触发数据刷新
      },
      //问题编号
      generateSerialNumber() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0');
        return `ZL-IS-${year}${month}${day}${random}`;
      },
      // 处理图片预览
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url || file.preview;
        this.dialogVisible = true;
      },
      cancel () {
        // 重置 vehicles 数组，只保留一个初始组合
        this.dataForm.vehicles = [{ vehicleTypeId: '', vehicleNumber: '', key: Date.now() }]
        this.vehicleNumberOptions = []
        this.visible = false // 关闭对话框或重置其他状态
        this.visibleR = false // 关闭对话框或重置其他状态
      },
      cancel1 () {
        // 重置 subtasks 数组，只保留一个初始组合
        this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
        this.visibleT = false // 关闭对话框或重置其他状态
      },
      addSubtask () {
        const serialNumber = this.generateSerialNumber();
        // console.log('Successfully setnumber:', serialNumber)
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
        if (index !== -1) {
          this.dataForm.subtasks.splice(index, 1)
        }
      },
      resetForm (formName) {
        // 重置 subtasks 数组，只保留一个初始组合
        this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
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
      timeReuse(){
        this.retimevisible = true
      },
      reuseIssue (id) {
        this.dataForm.issueId = id
        this.revisible = true
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
      init (id) {
        this.fetchuserinform() //获取用户名
        this.fetchIssueCategories()
        this.fetchVehicleTypes()
        this.fetchIssueOptions() // 获取所有问题编号选项
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
                this.dataForm.issueNumber = this.generateSerialNumber();
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
      initR (id) {
        this.fetchuserinform() //获取用户名
        this.dataForm.issueId = id || 0
        this.visibleR = true
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
                this.dataForm.issueNumber = this.generateSerialNumber();
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
      initT (id,issueNumber) {
        this.fetchTasksByIssueNumber(issueNumber)
        this.dataForm.issueId = id || 0
        this.visibleT = true
        // 为第一个子任务生成序列号
        this.dataForm.subtasks = [{ name: '', assignee: '', parentTask: '', serialNumber: this.generateSerialNumber(), key: Date.now() }];
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
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId)
            this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber)
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
                  'issueNumber': this.generateSerialNumber(),
                  'inspectionDepartment': this.dataForm.inspectionDepartment,
                  'inspectionDate': this.dataForm.inspectionDate,
                  'issueCategoryId': issueCategoryIdString, // 使用转换后的字符串
                  'vehicleTypeId': this.dataForm.vehicleTypeIds.join(','),
                  'vehicleNumberId': this.dataForm.vehicleNumbers.join(','),
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
                  'associatedIssueAddition': this.dataForm.associatedIssueIds.join(','), // 将数组转换为逗号分隔的字符串
                  'creationDuration': this.dataForm.creationDuration,
                  'causeAnalysis': this.dataForm.causeAnalysis,
                  'rectificationVerificationStatus':this.dataForm.rectificationVerificationStatus,
                  'verificationConclusion': this.dataForm.verificationConclusion,
                  'verifier': this.dataForm.verifier,
                  // 'reviewers': this.dataForm.reviewers,
                  'level': this.dataForm.level,
                  'state': this.dataForm.state,
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
                      this.visibleR = false
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
      },
      // 表单提交
      dataFormSubmitR () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId)
            this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber)
            // 将选中的责任人ID数组转换为字符串
            this.dataForm.rectificationResponsiblePerson = this.selectedResponsiblePersons.join(',');
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
                'rectificationStatus': this.dataForm.rectificationStatus,
                'actualCompletionTime': this.dataForm.actualCompletionTime,
                'rectificationPhotoDeliverable': this.dataForm.rectificationPhotoDeliverable,
                'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
                'causeAnalysis': this.dataForm.causeAnalysis,
                'state': '持续',
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visibleR = false
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
                      'issueId' : this.dataForm.issueId, // 根据需要传递的参数
                      // ...其他所需数据
                    }
                  }).then(({ data }) => {
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
          }
        })
        // 重置 vehicles 数组，只保留一个初始组合
        this.dataForm.vehicles = [{ vehicleTypeId: '', vehicleNumber: '', key: Date.now() }]
        this.vehicleNumberOptions = []
      },
      submitFormT (formName) {
        this.fetchuserinform()
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dataForm.subtasks.forEach(subtask => {
              this.$http({
                url: this.$http.adornUrl(`/generator/issuemasktable/save`),
                method: 'post',
                data: this.$http.adornData({
                  'issuemaskId': this.dataForm.issuemaskId || undefined,
                  'serialNumber': subtask.serialNumber,
                  'issueNumber': this.dataForm.issueNumber,
                  'reviewers': this.dataForm.reviewert,
                  'recipients': subtask.assignee,
                  'maskcontent': subtask.name,
                  'creator': this.dataForm.creator,
                  'creationTime': this.dataForm.creationTime,
                  'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                  'superiorMask': subtask.parentTask, // 确保上级任务数据被包含                  'nextMask': this.dataForm.nextMask,
                  'state': '审核中'
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
            })
          }
          // 重置 subtasks 数组，只保留一个初始组合
          this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
          this.visible1 = false // 关闭对话框或重置其他状态
        })
      },
      // 表单提交
      submitFormreuse() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId)
            this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber)
            // 确保 issueCategoryId 是一个数组
            if (!Array.isArray(this.dataForm.issueCategoryId)) {
              this.dataForm.issueCategoryId = [this.dataForm.issueCategoryId]
            }
            // 将数组转换为逗号分隔的字符串
            const issueCategoryIdString = this.dataForm.issueCategoryId.join(',')
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetable/save`),
              method: 'post',
              data: this.$http.adornData({
                'issueId': this.dataForm.issueId || undefined,
                'serialNumber': this.dataForm.serialNumber,
                'issueNumber': this.generateSerialNumber(),
                'inspectionDepartment': this.dataForm.inspectionDepartment,
                'inspectionDate': this.dataForm.inspectionDate,
                'issueCategoryId': issueCategoryIdString,
                'vehicleTypeId': this.dataForm.vehicleTypeId,
                'vehicleNumberId': this.dataForm.vehicleNumberId,
                'issueDescription': this.dataForm.issueDescription,
                'issuePhoto': this.dataForm.issuePhoto,
                'rectificationRequirement': this.dataForm.rectificationRequirement,
                'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                'responsibleDepartment': this.dataForm.responsibleDepartment,
                // 'rectificationStatus': this.dataForm.rectificationStatus,
                // 'actualCompletionTime': this.dataForm.actualCompletionTime,
                // 'rectificationPhotoDeliverable': this.dataForm.rectificationPhotoDeliverable,
                // 'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
                // 'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
                // 'remark': this.dataForm.remark,
                // 'creator': this.dataForm.creator,
                // 'creationTime': this.dataForm.creationTime,
                // 'lastModifier': this.dataForm.lastModifier,
                // 'lastModificationTime': this.dataForm.lastModificationTime,
                // 'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
                // 'associatedIssueAddition': this.dataForm.associatedIssueIds.join(','), // 将数组转换为逗号分隔的字符串
                // 'creationDuration': this.dataForm.creationDuration,
                // 'causeAnalysis': this.dataForm.causeAnalysis,
                // 'rectificationVerificationStatus':this.dataForm.rectificationVerificationStatus,
                // 'verificationConclusion': this.dataForm.verificationConclusion,
                // 'verifier': this.dataForm.verifier,
                // 'level': this.dataForm.level,
                // 'state': this.dataForm.state,
                // 'formula': this.dataForm.formula
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.revisible = false
                    this.retimevisible = false
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
      },

    }
  }
</script>
<style scoped>
.right-aligned {
  margin-left: 80px; /* 可根据需要调整移动的距离 */
}
</style>
