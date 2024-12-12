<template>
  <div>
  <el-dialog
    :title="!dataForm.issuemaskId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
<!--    <el-form-item label="序号" prop="serialNumber">-->
<!--      <el-input v-model="dataForm.serialNumber" placeholder="序号"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="问题编号(所属问题)" prop="issueNumber">-->
<!--      <el-input v-model="dataForm.issueNumber" placeholder="问题编号(所属问题)"></el-input>-->
<!--    </el-form-item>-->
    <el-form-item label="审核人" prop="reviewers">
<!--      <el-input v-model="dataForm.reviewers" placeholder="审核人"></el-input>-->
      <el-select v-model="dataForm.reviewers" filterable placeholder="请选择审核人">
        <el-option-group v-for="group in options" :key="group.label" :label="group.label">
          <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                     :value="item.label">
          </el-option>
        </el-option-group>
      </el-select>
    </el-form-item>
    <el-form-item label="接收人" prop="recipients">
<!--      <el-input v-model="dataForm.recipients" placeholder="接收人"></el-input>-->
      <el-select v-model="dataForm.recipients" filterable placeholder="请选择接收人">
        <el-option-group v-for="group in options" :key="group.label" :label="group.label">
          <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                     :value="item.label">
          </el-option>
        </el-option-group>
      </el-select>
    </el-form-item>
    <el-form-item label="任务内容" prop="maskcontent">
      <el-input v-model="dataForm.maskcontent" placeholder="任务内容"></el-input>
    </el-form-item>

<!--    <el-form-item label="任务发起人" prop="creator">-->
<!--      <el-input v-model="dataForm.creator" placeholder="任务发起人"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="发起时间" prop="creationTime">-->
<!--      <el-input v-model="dataForm.creationTime" placeholder="发起时间"></el-input>-->
<!--    </el-form-item>-->
<!--      <el-form-item label="要求完成时间" prop="requiredCompletionTime">-->
<!--        <el-input v-model="dataForm.requiredCompletionTime" placeholder="要求完成时间"></el-input>-->
<!--      </el-form-item>-->
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
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFSubmit()">确定</el-button>
    </span>
  </el-dialog>
    <el-dialog
      :title="'是否确认完成'"
      :close-on-click-modal="false"
      :visible.sync="visible2"
      :width="'400px'"> <!-- 设置对话框宽度 -->
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
        <el-form-item label="处理方案" prop="handlingScenarios">
<!--          <el-input v-model="dataForm.handlingscenarios" placeholder="处理方案"></el-input>-->
          <el-input v-model="dataForm.handlingscenarios" placeholder="处理方案"></el-input>
        </el-form-item>
<!--        <el-upload-->
<!--          class="upload-button right-aligned"-->
<!--          :show-file-list="false"-->
<!--          :before-upload="beforeUpload"-->
<!--          :on-change="handleFileChange"-->
<!--          :auto-upload="false"-->
<!--        >-->
<!--          <el-button type="primary">上传附件</el-button>-->
<!--        </el-upload>-->
        <el-form-item label="上传附件">
<!--          <el-upload ref="file" class="upload-btn-group" :file-list="fileList" :action="uploadUrl"-->
<!--                     :on-change="handleFileChange"-->
<!--                     :on-remove="handleFileRemove"-->
<!--                     :auto-upload="false">-->
<!--            <el-button size="middle" type="primary" icon="el-icon-upload">点击上传</el-button>-->
<!--          </el-upload>-->
          <el-upload
            ref="file"
            class="upload-btn-group"
            :file-list="fileList"
            :action="uploadUrl"
            :on-change="handleFileChange"
            :auto-upload="false">
            <el-button size="middle" type="primary" icon="el-icon-upload">点击上传</el-button>
          </el-upload>
          <el-button class="upload-preview-btn" type="primary" size="middle"
                     @click="uploadAllListVisible = true">附件预览</el-button>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="visible2 = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
    </el-dialog>
    <el-dialog
      :title="!dataForm.issuemaskId ? '新增' : '派发'"
      :close-on-click-modal="false"
      :visible.sync="visible1"
      :width="'400px'">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
        <el-form-item label="接收人" prop="recipients">
                    <el-select v-model="dataForm.recipients" filterable placeholder="请选择接收人">
                      <el-option-group v-for="group in options" :key="group.value" :label="group.label">
                        <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                                   :value="item.value">
                        </el-option>
                      </el-option-group>
                    </el-select>
                  </el-form-item>
<!--                  <el-form-item label="派发时间" prop="creationTime">-->
<!--                    <el-date-picker v-model="dataForm.creationTime" type="datetime" placeholder="选择派发时间"></el-date-picker>-->
<!--                  </el-form-item>-->

      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="visible1 = false">取消</el-button>
      <el-button type="primary" @click="datanewSubmit()">确定</el-button>
    </span>
    </el-dialog>
    <el-dialog
      :title="'任务发起'"
      :close-on-click-modal="false"
      :visible.sync="visible3">
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
    <el-dialog title="附件预览" :visible.sync="uploadAllListVisible">
      <div style="color: orange">
<!--        注：若原先存在文件，则点击备注下方的确定后，将会用新上传文件替换原文件-->
      </div>
      <br />
      <el-table :data="uploadAllList" border style="width: 100%">
        <el-table-column prop="name" label="文件名"> </el-table-column>
        <el-table-column prop="url" label="预览">
          <template slot-scope="scope">
            <el-button v-if="scope.row.url" @click="previewDoc(scope.row.url)">预览</el-button>
            <el-button v-if="scope.row.name" @click="previewDocRemove(scope.row.name)" type="warning">移除</el-button>
            <span v-else>--</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        stepAttachment: "",
        fileList: [],
        uploadUrl: "",
        uploadAllListVisible: false,
        uploadUrlList: [],
        uploadNameList: [],
        uploadAllList: [],
        tmpAllList: [],
        uploadingFile: "",
        visible: false,
        visible1: false,
        visible2: false,
        visible3: false,
        dataForm: {
          issuemaskId: 0,
          serialNumber: '',
          issueNumber: '',
          reviewers: '',
          reviewerOpinion: '',
          recipients: '',
          maskcontent: '',
          handlingscenarios:'',
          annex: '',
          creator: '',
          creationTime: '', // 初始化为当前时间
          state: '',
          requiredCompletionTime: '',
          superiorMask: '',
          subtasks: [{ name: '', assignee: '', parentTask: '', serialNumber: '', key: Date.now() }],
          options: ''
        },
        dataRule: {
        },
        masknumber: '',
        newmasknumber: '',
        options: ''
      }
    },
    computed: {

      formattedCreationTime() {
        const date = new Date();
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      }
    },
    created () {
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
    },
    methods: {
      cancel1 () {
        // 重置 subtasks 数组，只保留一个初始组合
        this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }]
        this.visible3 = false // 关闭对话框或重置其他状态
      },
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
      beforeUpload(file) {
        // 定义允许的文件类型
        const allowedTypes = [
          'image/',       // 图片文件类型
          'application/pdf',    // PDF文件
          'application/msword', // Word文件（.doc）
          'application/vnd.openxmlformats-officedocument.wordprocessingml.document', // Word文件（.docx）
        ];

        // 检查文件类型是否在允许的类型列表中
        const isAllowedType = allowedTypes.some(type => file.type.startsWith(type));

        if (!isAllowedType) {
          this.$message.error('仅支持上传图片、Word文件和PDF文件!');
        }

        return isAllowedType; // 返回 true 继续上传，返回 false 阻止上传
      },

      // handleFileChange(file) {
      //   // 存储待上传的文件
      //   this.uploadingFile = file.raw; // 获取 File 对象
      //   this.uploadFile(file.raw); // 调用上传方法
      // },
      // 上传文件
      handleFileChange(file) {
        // 存储待上传的文件

        this.uploadingFile = file.raw; // 获取 File 对象

        this.uploadNameList.push(file.raw.name);
        this.uploadFile(file.raw); // 调用上传方法
      },
      uploadFile(file) {
        const formData = new FormData();
        let file2 = file;
        formData.append("file", file); // 将文件添加到 FormData
        this.$http({
          url: this.$http.adornUrl("/generator/issuetable/upload"), // 替换为实际上传接口
          method: "post",
          data: formData,
          headers: {
            "Content-Type": "multipart/form-data", // 指定为文件上传
          },
        })
          .then(({ data }) => {
            if (data && data.code === 0) {
              // 保存后端返回的url到变量中
              this.dataForm.rectificationPhotoDeliverable = data.uploadurl; // 假设你有一个变量uploadedUrl来保存上传的url
              // console.log('获得的文件地址 ：', data.uploadurl)
              // console.log('获得的文件 ：', data)
              this.uploadUrlList.push(data.uploadurl);
              let fileTmp = {
                name: file2.name,
                url: data.uploadurl,
              };
              // this.uploadAllList = []
              this.tmpAllList.push(fileTmp);
              // console.log('上传文件列表 ：', fileTmp)
              this.uploadAllList.push(fileTmp);
              console.log("上传文件列表 ：", this.uploadAllList);
              console.log("上传文件列表2 ：", this.tmpAllList);
              // console.log('上传文件名字列表 ：', this.uploadNameList)
              // this.form.stepFile = data.uploadurl
              // this.$message.success('文件上传成功');
              // 处理成功后的逻辑，例如更新状态
            } else {
              this.$message.error(data.msg);
            }
          })
          .catch((error) => {
            this.$message.error("上传失败");
            console.error(error);
          });
      },
      //文件预览
      previewDoc(fileflag) {
        const token = this.$cookie.get("token"); // 获取当前的 token
        if (!token) {
          console.error("Token not found!");
          return;
        }
        // console.log("获取的地址 ", fileflag);
        // 拼接带有 token 的请求地址
        const url = `${this.$http.adornUrl(
          `/generator/issuetable/${fileflag}`
        )}?token=${token}`;
        window.open(url);
      },
      previewDocRemove(name) {
        console.log("删除的名字 ", name);
        console.log("上传文件列表 ：", this.uploadAllList);
        this.uploadAllList = this.uploadAllList.filter((item) => item.name !== name);
        this.tmpAllList = this.tmpAllList.filter((item) => item.name !== name);
        console.log("上传文件列表after ：", this.uploadAllList);
      },
      handleFileChange1(file) {
        // 存储待上传的文件
        this.uploadingFile = file.raw; // 获取 File 对象
        this.uploadFile1(file.raw); // 调用上传方法
      },
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
      init (id) {
        this.dataForm.issuemaskId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issuemaskId) {
            console.log('huoqu ' ,this.dataForm.issuemaskId)
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/info/${this.dataForm.issuemaskId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                // this.dataForm.serialNumber = data.issueMaskTable.serialNumber
                // this.dataForm.issueNumber = data.issueMaskTable.issueNumber
                this.dataForm.reviewers = data.issueMaskTable.reviewers
                this.dataForm.reviewerOpinion = data.issueMaskTable.reviewerOpinion
                this.dataForm.recipients = data.issueMaskTable.recipients
                this.dataForm.maskcontent = data.issueMaskTable.maskcontent
                // this.dataForm.creator = data.issueMaskTable.creator
                // this.dataForm.creationTime = data.issueMaskTable.creationTime
                this.dataForm.requiredCompletionTime = data.issueMaskTable.requiredCompletionTime
                // this.dataForm.state = '已派发'
              }
            })
          }
        })
      },
      async init1 (id ,issueNumber) {
        // console.log('初始化弹窗，ID:', id)
        // console.log('当前时间为: ' ,this.formattedCreationTime )
        this.dataForm.issuemaskId = id || 0
        this.visible1 = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issuemaskId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/info/${this.dataForm.issuemaskId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.serialNumber = data.issueMaskTable.serialNumber
                this.dataForm.issueNumber = data.issueMaskTable.issueNumber
                this.dataForm.reviewers = data.issueMaskTable.reviewers
                this.dataForm.recipients = data.issueMaskTable.recipients
                this.dataForm.maskcontent = data.issueMaskTable.maskcontent
                this.dataForm.creator = data.issueMaskTable.creator
                this.dataForm.creationTime = this.formattedCreationTime // 初始化为当前时间
                this.dataForm.requiredCompletionTime = data.issueMaskTable.requiredCompletionTime
                this.dataForm.superiorMask = data.issueMaskTable.superiorMask
                console.log('初始化弹窗，ID:', data.issueMaskTable.issueNumber)
              }
            })
          }
        })
        console.log('初始化弹窗dataForm:', this.dataForm)
        this.newmasknumber = await this.generateSerialNumber(issueNumber);
        console.log('初始化弹窗newmasknumber', this.newmasknumber)
      },
      completeHandle (id) {
        this.dataForm.issuemaskId = id || 0
        this.visible2 = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issuemaskId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/info/${this.dataForm.issuemaskId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.serialNumber = data.issueMaskTable.serialNumber
                this.dataForm.issueNumber = data.issueMaskTable.issueNumber
                this.dataForm.reviewers = data.issueMaskTable.reviewers
                this.dataForm.recipients = data.issueMaskTable.recipients
                this.dataForm.maskcontent = data.issueMaskTable.maskcontent
                this.dataForm.handlingscenarios = data.issueMaskTable.handlingscenarios
                this.dataForm.creator = data.issueMaskTable.creator
                this.dataForm.creationTime = data.issueMaskTable.creationTime
                this.dataForm.requiredCompletionTime = data.issueMaskTable.requiredCompletionTime
                this.dataForm.state = '已完成'
              }
            })
          }
        })
      },
      async distribute (issueNumber, maskNumber, creator) {
        // this.fetchTasksByIssueNumber(issueNumber)
        this.dataForm.issuemaskId = 0
        this.visible3 = true
        this.dataForm.issueNumber = issueNumber
        this.dataForm.serialNumber = maskNumber
        this.dataForm.creator = creator
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
      },
      removeSubtask (subtask) {
        let index = this.dataForm.subtasks.indexOf(subtask)
        this.masknumber = this.masknumber - 1;
        if (index !== -1) {
          this.dataForm.subtasks.splice(index, 1)
        }
      },
      addSubtask () {
        const serialNumber = this.generateSerialNumber1();
        // console.log('Successfully setnumber:', serialNumber)
        this.dataForm.subtasks.push({
          name: '',
          assignee: '',
          parentTask: '',
          serialNumber: serialNumber,
          key: Date.now()
        })

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
                  'creationTime': this.dataForm.creationTime,
                  'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                  'superiorMask': this.dataForm.serialNumber,
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

              // console.log('时间:', this.dataForm.requiredCompletionTime);
              // console.log('发起人:', this.dataForm.userinfo);
            });
          }
          // 重置 subtasks 数组，只保留一个初始组合
          this.dataForm.subtasks = [{ name: '', assignee: '', key: Date.now() }];
          this.visible3 = false; // 关闭对话框或重置其他状态
        });
      },
      dataFSubmit () {

        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/${!this.dataForm.issuemaskId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'issuemaskId': this.dataForm.issuemaskId || undefined,
                'reviewers': this.dataForm.reviewers,
                'reviewerOpinion': this.dataForm.reviewerOpinion,
                'recipients': this.dataForm.recipients,
                'maskcontent': this.dataForm.maskcontent,
                'requiredCompletionTime': this.dataForm.requiredCompletionTime,
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.visible2 = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
      statechange () {
        // console.log('状态修改', this.dataForm.issueNumber);
        this.$http({
          url: this.$http.adornUrl(`/generator/issuemasktable/statechange?issueNumber=${this.dataForm.issueNumber}`),
          method: 'post'
        }).then(({data}) => {
          if (data && data.code === 0) {
            // 成功处理
          } else {
            this.$message.error(data.msg)
          }
        })
      },

      // 表单提交
      dataFormSubmit () {
        let tmpListString = [];
        if (this.tmpAllList.length) {
          tmpListString = JSON.stringify(this.tmpAllList);
        }
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            const requestData = this.$http.adornData({
              'issuemaskId': this.dataForm.issuemaskId || undefined,
              'serialNumber': this.dataForm.serialNumber,
              'issueNumber': this.dataForm.issueNumber,
              'reviewers': this.dataForm.reviewers,
              // 'reviewerOpinion': this.dataForm.reviewerOpinion,
              'recipients': this.dataForm.recipients,
              'maskcontent': this.dataForm.maskcontent,
              'handlingScenarios': this.dataForm.handlingscenarios,
              // 'annex': this.dataForm.annex,
              'annex': this.tmpAllList.length ? tmpListString : this.stepAttachment,
              'creator': this.dataForm.creator,
              'creationTime': this.dataForm.creationTime,
              'state': this.dataForm.state
            });

            // console.log('Request Data:', requestData);  // 打印请求数据
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/${!this.dataForm.issuemaskId ? 'save' : 'update'}`),
              method: 'post',
              data: requestData
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.visible2 = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
              this.statechange();
            })
          }
        })
      },
      // 表单提交
      datanewSubmit () {
        // const serialNumber = this.generateSerialNumber(this.dataForm.issueNumber);
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuemasktable/save`),
              method: 'post',
              data: this.$http.adornData({
                'issuemaskId': undefined,
                'serialNumber': this.newmasknumber,
                'issueNumber': this.dataForm.issueNumber,
                'reviewers': this.dataForm.reviewers,
                'reviewerOpinion': this.dataForm.reviewerOpinion,
                'recipients': this.dataForm.recipients,
                'maskcontent': this.dataForm.maskcontent,
                'handlingscenarios': this.dataForm.handlingscenarios,
                'annex': this.dataForm.annex,
                'creator': this.dataForm.creator,
                'creationTime': this.dataForm.creationTime,
                'requiredCompletionTime': this.dataForm.requiredCompletionTime,
                'superiorMask': this.dataForm.superiorMask,
                'state': '审核中'
              })
            }).then(({data}) => {
              this.$http({
                url: this.$http.adornUrl('/generator/issuemasktable/dispach'),
                method: 'post',
                params: this.$http.adornParams({
                  issueNumber: this.dataForm.serialNumber,
                  serialNumber: this.newmasknumber
                })
              }).catch(() => {
                this.$message.error('修改失败')
              })
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible1 = false
                    // this.visible2 = false
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
