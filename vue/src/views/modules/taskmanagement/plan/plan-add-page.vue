<template>
  <div class="main-content">
    <el-container>
      <el-main>
        <h2>{{ title }}</h2>
        <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
                 label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="计划编号" prop="planId">
                <el-input v-model="dataForm.planId" placeholder="计划编号" @blur="checkPlanNumber"></el-input>
              </el-form-item>
              <el-form-item label="开始日期" prop="planStartDate">
                <el-date-picker v-model="dataForm.planStartDate" type="date" placeholder="选择开始日期"
                                style="width: 100%;"></el-date-picker>
              </el-form-item>
              <el-form-item label="计划天数" prop="planScheduleDays">
                <el-input :value="planScheduleDays" disabled placeholder="计划天数"></el-input>
              </el-form-item>
              <el-form-item label="发起人" prop="planInitiator">
                <el-select v-model="dataForm.planInitiator" filterable placeholder="请选择审核人">
                  <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
              <el-form-item label="审核人" prop="planAuditor">
                <el-select v-model="dataForm.planAuditor" filterable placeholder="请选择审核人">
                  <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="计划名" prop="planName">
                <el-input v-model="dataForm.planName" placeholder="计划名"></el-input>
              </el-form-item>
              <el-form-item label="计划完成日期" prop="planScheduleCompletionDate">
                <el-date-picker v-model="dataForm.planScheduleCompletionDate" type="date" placeholder="选择计划完成日期"
                                style="width: 100%;"></el-date-picker>
              </el-form-item>
              <el-form-item label="关联指标" prop="planAssociatedIndicatorsId">
                <el-select v-model="dataForm.planAssociatedIndicatorsId" clearable placeholder="请选择"
                           ref="transferName">
                  <el-option v-for="item in indicatorOptions" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="负责人" prop="planPrincipal">
                <el-select v-model="dataForm.planPrincipal" filterable placeholder="请选择负责人">
                  <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
              <el-form-item label="执行人" prop="planExecutor">
                <el-select v-model="dataForm.planExecutor" multiple filterable placeholder="执行人">
                  <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="计划内容" prop="planContent">
            <el-input type="textarea" :autosize="{ minRows: 5, maxRows: 10 }" placeholder="请输入计划内容"
                      v-model="dataForm.planContent" maxlength="1000">
            </el-input>
          </el-form-item>

          <el-form-item label="上传附件" prop="planFile">
            <el-upload ref="file" :file-list="planFileList" :action="uploadUrl"
                       :on-remove="handleRemove" :before-remove="beforeRemove" :on-change="uploadFile"
                       :auto-upload="false">
              <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
            </el-upload>
          </el-form-item>


          <!-- 任务列表 -->
          <div v-for="(task, index) in tasks" :key="index" style="background-color:#EEEEEE;" class="task-list-item">
            <el-form :model="task" :rules="dataRule" :ref="`task_${index}`" label-width="120px">

              <el-row :gutter="20">
                <el-col :span="12">
                  <h3>{{ '任务 ' + (index + 1) }}</h3>
                </el-col>
                <el-col :span="12" class="button-col">
                  <el-button type="text" icon="el-icon-close" size="large" @click="removeTask(index)"
                             class="remove-task-button"></el-button>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="任务编号" prop="taskId">
                    <el-input v-model="task.taskId" placeholder="任务编号"></el-input>
                  </el-form-item>
                  <el-form-item label="开始日期" prop="taskStartDate">
                    <el-date-picker v-model="task.taskStartDate" type="date" placeholder="选择开始日期"
                                    style="width: 100%;"></el-date-picker>
                  </el-form-item>
                  <el-form-item label="任务天数" prop="taskScheduleDays">
                    <el-input :value="task.taskScheduleDays" placeholder="任务天数" disabled></el-input>
                  </el-form-item>
                  <el-form-item label="负责人" prop="taskPrincipal">
                    <el-select v-model="task.taskPrincipal" filterable placeholder="请选择负责人">
                      <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                        <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                                   :value="item.value">
                        </el-option>
                      </el-option-group>
                    </el-select>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="任务名" prop="taskName">
                    <el-input v-model="task.taskName" placeholder="任务名"></el-input>
                  </el-form-item>
                  <el-form-item label="预计完成日期" prop="taskScheduleCompletionDate">
                    <el-date-picker v-model="task.taskScheduleCompletionDate" type="date" placeholder="选择任务完成日期"
                                    style="width: 100%;"></el-date-picker>
                  </el-form-item>
                  <el-form-item label="审核人" prop="taskAuditor">
                    <el-select v-model="task.taskAuditor" filterable placeholder="请选择审核人">
                      <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                        <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                                   :value="item.value">
                        </el-option>
                      </el-option-group>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="执行人" prop="taskExecutor">
                    <el-select v-model="task.taskExecutor" multiple filterable placeholder="执行人">
                      <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                        <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                                   :value="item.value">
                        </el-option>
                      </el-option-group>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="任务内容" prop="taskContent">
                <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 5 }" placeholder="请输入任务内容"
                          v-model="task.taskContent" maxlength="1000">
                </el-input>
              </el-form-item>

            </el-form>
          </div>
        </el-form>

        <!-- 表单操作按钮 -->
        <div class="form-actions center-buttons">
          <el-button type="primary" @click="addTask()">添加任务</el-button>
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="dataFormSubmit()">保存</el-button>
        </div>
      </el-main>
    </el-container>
  </div>
</template>


<script>
// import {uploadFile} from "../../../../api/indicator/indicator";
import moment from 'moment'

export default {
  data() {
    return {

      planIdIsUsed: true,

      title: '新建计划',
      visible: false,

      // 计划附件
      planFileList: [],
      uploadUrl: '',

      dataForm: { // 计划的基本结构
        tmPid: 0,
        planId: '',
        planName: '',
        planContent: '',
        planStartDate: '',
        planScheduleCompletionDate: '',
        planScheduleDays: '',
        // planActualCompletionDate: '',
        // planActualDays: '',
        planTasksAssignment: '',
        planAssociatedTasksId: '',
        planSchedule: '',
        planPrincipal: '',
        planExecutor: '',
        planAuditor: '',
        planCurrentState: '',
        planInitiator: '',
        // planIsCompleted: '',
        // planIsOverdue: '',
        // planIsOnTime: '',
        // planEarlyCompletionDays: '',
        // planLagDays: '',
        // planLagReasons: '',
        planAssociatedIndicatorsId: '',
        planFile: '',

      },

      task: { // 任务的基本结构
        tmTid: 0,
        taskId: '',
        taskName: '',
        taskContent: '',
        taskStartDate: '',
        taskScheduleCompletionDate: '',
        taskScheduleDays: '',
        taskActualCompletionDate: '',
        taskActualDays: '',
        taskAssociatedPlanId: '',
        taskSchedule: '',
        taskPrincipal: '',
        taskExecutor: '',
        taskAuditor: '',
        taskCurrentState: '',
        taskIsCompleted: '',
        taskIsOverdue: '',
        taskIsOnTime: '',
        taskEarlyCompletionDays: '',
        taskLagDays: '',
        taskLagReasons: '',
        taskAssociatedIndicatorsId: ''
      },

      tasks: [], // 初始化成员列表
      dataRule: {
        planId: [
          {required: true, message: '计划编号不能为空', trigger: 'blur'},
          {validator: this.validatePlanId, trigger: 'blur'}
        ],
        planName: [
          {required: true, message: '计划名不能为空', trigger: 'blur'}
        ],
        planContent: [
          {required: true, message: '任务内容不能为空', trigger: 'blur'}
        ],
        planStartDate: [
          {required: true, message: '开始日期不能为空', trigger: 'blur'}
        ],
        planScheduleCompletionDate: [
          {
            required: true,
            validator: (rule, value, callback) => this.dateValidate(rule, value, callback),
            trigger: 'blur'
          }
        ],
        planInitiator: [
          {required: true, message: '发起人不能为空', trigger: ['change', 'blur']}
        ],
        planPrincipal: [
          {required: true, message: '负责人不能为空', trigger: ['change', 'blur']}
        ],
        planExecutor: [
          {required: true, message: '执行人不能为空', trigger: ['change', 'blur']}
        ],
        planAuditor: [
          {required: true, message: '审核人不能为空', trigger: ['change', 'blur']}
        ],
        // planAssociatedIndicatorsId: [
        //   {required: true, message: '关联指标编号不能为空', trigger: ['change', 'blur']}
        // ],

        taskId: [
          {required: true, message: '任务编号不能为空', trigger: 'blur'},
          {validator: this.validateTaskId, trigger: 'blur'}
        ],
        taskName: [
          {required: true, message: '任务名不能为空', trigger: 'blur'}
        ],
        taskContent: [
          {required: true, message: '任务内容不能为空', trigger: 'blur'}
        ],
        taskStartDate: [
          {required: true, message: '任务开始日期不能为空', trigger: 'blur'}
        ],
        taskScheduleCompletionDate: [
          {required: true, message: '任务预计完成日期不能为空', trigger: 'blur'}
        ],
        taskPrincipal: [
          {required: true, message: '负责人不能为空', trigger: ['change', 'blur']}
        ],
        taskExecutor: [
          {required: true, message: '执行人不能为空', trigger: ['change', 'blur']}
        ],
        taskAuditor: [
          {required: true, message: '审核人不能为空', trigger: ['change', 'blur']}
        ],
      },

      // taskRules: {
      // taskId: [
      //   { required: true, message: '任务编号不能为空', trigger: 'blur' },
      //   { validator: this.validateTaskId, trigger: 'blur' }
      // ],
      // taskName: [
      //   { required: true, message: '任务名不能为空', trigger: 'blur' }
      // ],
      // taskContent: [
      //   { required: true, message: '任务内容不能为空', trigger: 'blur' }
      // ],
      // taskStartDate: [
      //   { required: true, message: '任务开始日期不能为空', trigger: 'blur' }
      // ],
      // taskScheduleCompletionDate: [
      //   { required: true, message: '任务预计完成日期不能为空', trigger: 'blur' }
      // ],
      // taskPrincipal: [
      //   { required: true, message: '负责人不能为空', trigger: ['change', 'blur'] }
      // ],
      // taskExecutor: [
      //   { required: true, message: '执行人不能为空', trigger: 'blur' }
      // ],
      // taskAuditor: [
      //   { required: true, message: '审核人不能为空', trigger: 'blur' }
      // ],
      // },

      statusOptions: [
        {value: '未开始', label: '未开始'},
        {value: '进行中', label: '进行中'},
        {value: '已完成', label: '已完成'},
      ],

      // 员工列表
      options: [],
      value: '',
      indicatorOptions: [],
    }
  },


  async created() {
    // 获取分组后的员工数据
    await this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
      method: 'get',
    }).then(({data}) => {
      this.options = data;
      console.log(data);
    })

    // 获取指标列表
    await this.$http({
      url: this.$http.adornUrl(`/indicator/indicatordatadictionary/getIndicatorsList`),
      method: 'get',
    }).then(response => {
      const opt = response.data.map(item => ({
        value: item.value,
        label: item.label
      }));
      console.log(opt);
      this.indicatorOptions = opt;
    })


    // 初始化计划编号
    await this.getPlanId()


  },


  methods: {

    // 上传文件
    uploadFile(file) {

      console.log("文件" + file.name);

      const formData = new FormData();
      formData.append('file', file.raw); // 将文件添加到 FormData

      this.$http({
        // url: this.$http.adornUrl('/test/upload'), // 替换为实际上传接口
        url: this.$http.adornUrl('/generator/issuetable/upload'), // 替换为实际上传接口
        method: 'post',
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data' // 指定为文件上传
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          // 保存后端返回的url到变量中
          this.dataForm.planFile = data.uploadurl; // 假设你有一个变量uploadedUrl来保存上传的url
          this.planFileList.push({
            'planId': this.dataForm.planId,
            'name': file.name,
            'url': data.uploadurl
          });
          console.log('获得的文件地址 ：', data.uploadurl)
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

    // 移除文件之前提示
    beforeRemove(file, planFileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },

    // 移除文件
    handleRemove(file, planFileList) {
      console.log("移除的文件为: " + JSON.stringify(file));
    },

    // 时间校验
    dateValidate(rule, value, callback) {
      let startDate = this.dataForm.planStartDate;
      if (!startDate) {
        callback(new Error('请先输入开始日期'));
      } else if (value === undefined || value === null || value === '') {
        callback(new Error('计划日期不能为空'));
      } else if (startDate && value) {
        if (value < startDate) {
          startDate = new Date(startDate); // 转换开始日期为日期对象
          value = new Date(value); // 转换计划日期为日期对象
          callback(new Error('结束日期不能早于开始日期'));
        } else {
          callback();
        }
      }
    },

    // 取消按钮,点击后返回，并且关闭当前标签页
    cancel() {
      //弹窗提示用户是否取消，取消将返回到任务列表页面，并且不保留数据
      this.$confirm('确定取消吗？取消后数据不保留', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$router.back();

        // 下面的操作时删除标签页
        console.log('取消按钮被点击' + this.$store.getters.getCommon);
        console.log("JSON对象:" + JSON.stringify(this.$store.getters.getCommon.mainTabs));
        const tabs = this.$store.getters.getCommon.mainTabs
        const newTabs = tabs.filter(tabs => {
          return tabs.name !== 'plan-add-page'
        })
        console.log("删除当前页JSON对象:" + JSON.stringify(newTabs));
        this.$store.commit('resetMainTabs', newTabs);
      })
    },

    dataFormSubmit() {
      // 先验证 dataForm
      this.$refs.dataForm.validate((validDataForm) => {
        if (!validDataForm) {
          console.log("dataForm 校验失败");
          return;
        }

        console.log("dataForm 校验通过");

        // 验证每个 task 表单
        // const tasksValidPromises = this.tasks.map((_, index) => {
        //   console.log("验证 task 表单" + JSON.stringify(this.tasks[index]));
        //   return new Promise((resolve, reject) => {
        //     const taskFormRef = this.$refs[`task_${index}`];

        //     console.log("taskFormRef" + taskFormRef);
        //     if (taskFormRef) {
        //       taskFormRef.validate((validTask) => {
        //         if (validTask) {
        //           resolve();
        //         } else {
        //           reject(`Task ${index + 1} 校验失败`);
        //         }
        //       });
        //     } else {
        //       reject(`Task ${index + 1} 表单未找到`);
        //     }
        //   });
        // });

        const tasksValidPromises = this.tasks.map((_, index) => {
          return new Promise((resolve, reject) => {
            this.$nextTick(() => { // 确保 DOM 渲染完成
              var taskForm = 'task_' + index;
              const taskFormRef = this.$refs[`task_${index}`][0];
              console.log("验证 task 表单%o" + taskFormRef);
              console.log(this.$refs);
              if (taskFormRef) {
                taskFormRef.validate((validTask) => {
                  if (validTask) {
                    resolve();
                  } else {
                    reject(`Task ${index + 1} 校验失败`);
                  }
                });
              } else {
                reject(`Task ${index + 1} 表单未找到`);
              }
            });
          });
        });

        Promise.all(tasksValidPromises)
          .then(() => {
            console.log("所有 task 校验通过");

            let number = this.tasks.length;
            let associatedTasks = '';
            // 遍历tasks列表，将其id拼接成字符串，放入associatedTasks中
            for (let i = 0; i < number; i++) {
              associatedTasks += this.tasks[i].taskId;
              if (i < number - 1) {
                associatedTasks += ',';
              }
            }

            console.log("任务是否分配:" + number)
            console.log("任务编号:" + associatedTasks)

            // 如果所有校验通过，准备提交数据
            const plan = {
              'tmPid': this.dataForm.tmPid || undefined,
              'planId': this.dataForm.planId,
              'planName': this.dataForm.planName,
              'planContent': this.dataForm.planContent,
              'planStartDate': this.dataForm.planStartDate,
              'planScheduleCompletionDate': this.dataForm.planScheduleCompletionDate,
              'planScheduleDays': this.planScheduleDays,
              'planActualCompletionDate': this.dataForm.planActualCompletionDate,
              'planActualDays': this.dataForm.planActualDays,
              'planTasksAssignment': number > 0 ? '已分配' : '未分配',
              'planAssociatedTasksId': number > 0 ? associatedTasks : '-',
              'planSchedule': this.dataForm.planSchedule,
              'planPrincipal': this.dataForm.planPrincipal,
              'planExecutor': this.dataForm.planExecutor,
              'planAuditor': this.dataForm.planAuditor,
              'planCurrentState': 0,
              'planIsCompleted': 0,
              'planIsOverdue': 0,
              'planIsOnTime': 0,
              'planAssociatedIndicatorsId': this.dataForm.planAssociatedIndicatorsId,
              'planFile': this.dataForm.planFile,
              'planInitiator': this.dataForm.planInitiator
            };

            const tasksData = this.tasks.map(task => ({
              taskId: task.taskId,
              taskName: task.taskName,
              taskContent: task.taskContent,
              taskStartDate: task.taskStartDate,
              taskScheduleCompletionDate: task.taskScheduleCompletionDate,
              taskScheduleDays: task.taskScheduleDays,
              taskAssociatedPlanId: this.dataForm.planId,
              taskPrincipal: task.taskPrincipal,
              taskExecutor: task.taskExecutor,
              taskAuditor: task.taskAuditor,
              taskAssociatedIndicatorsId: this.dataForm.planAssociatedIndicatorsId,
              taskCurrentState: 0,
              taskIsCompleted: 0,
              taskIsOverdue: 0,
              taskIsOnTime: 0,
              taskParentNode: this.dataForm.planId
            }));

            // 提交数据
            this.$http({
              url: this.$http.adornUrl(`/taskmanagement/plan/saveAndApproval`),
              method: 'post',
              data: this.$http.adornData({
                'plan': plan,
                'tasks': tasksData,
                'files': this.planFileList
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500
                });
                // this.$router.push({
                //   name: 'plan-list'
                // });
                this.$router.back();
              } else {
                this.$message.error(data.msg);
              }
            });
          }).catch(error => {
          console.error(error);
          this.$message.error('请修正任务表单中的错误');
        });
      });
    },

    // 添加任务
    addTask() {
      this.tasks.push({ // 假设这是一个任务的基本结构，根据实际情况调整
        tmTid: 0,
        taskId: '',
        taskContent: '',
        taskStartDate: '',
        taskScheduleCompletionDate: '',
        taskScheduleDays: '',
        taskActualCompletionDate: '',
        taskActualDays: '',
        taskAssociatedPlanId: '',
        taskSchedule: '',
        taskPrincipal: '',
        taskExecutor: '',
        taskAuditor: '',
        taskCurrentState: '',
        taskIsCompleted: '',
        taskIsOverdue: '',
        taskIsOnTime: '',
        taskEarlyCompletionDays: '',
        taskLagDays: '',
        taskLagReasons: '',
        taskAssociatedIndicatorsId: ''
      });
    },

    // 移除任务
    removeTask(index) {
      this.tasks.splice(index, 1);
    },

    updatePlanScheduleDays() {
      this.$forceUpdate();
    },

    // 任务时间计算
    calculateTaskScheduleDays(task) {
      if (task.taskStartDate && task.taskScheduleCompletionDate) {
        const startDate = new Date(task.taskStartDate);
        const endDate = new Date(task.taskScheduleCompletionDate);
        const diffTime = endDate - startDate;
        const diffDays = diffTime / (1000 * 60 * 60 * 24) + 1;
        return diffDays > 0 ? diffDays : 0;
      }
      return '';
    },

    // 校验计划编号是否重复
    validatePlanId(rule, value, callback) {
      if (this.dataForm.planId) {
        this.$http({
          url: this.$http.adornUrl(`/taskmanagement/plan/checkPlanNumber`),
          method: 'get',
          params: {
            planId: this.dataForm.planId
          }
        }).then(response => {
          if (response.data) {
            callback(new Error('计划编号已被使用'));
          } else {
            callback();
          }
        })
          .catch(error => {
            console.error(error);
            callback(new Error('验证计划编号时发生错误'));
          });
      } else {
        callback();
      }
    },

    checkPlanNumber() {
      if (this.dataForm.planId) {
        this.$http({
          url: this.$http.adornUrl(`/taskmanagement/plan/checkPlanNumber`),
          method: 'get',
          params: {
            planId: this.dataForm.planId
          }
        }).then(response => {
          if (response.data) {
            this.$message.error('计划编号已被使用');
          }
        })
      }
    },

    // 验证任务编号是否使用
    validateTaskId(rule, value, callback) {
      // 这里可以添加自定义的验证逻辑
      this.checkTaskIdUsed(value);
      if (value === '' || value === null || value === undefined) {
        callback(new Error('请输入任务编号'));
      } else if (this.taskIdUsed === true) {
        // 检查任务编号是否已存在
        console.log(this.taskIdUsed);
        callback(new Error('此任务编号已被使用，请更换!'));
      } else {
        callback();
      }
      this.taskIdUsed = false
    },

    checkTaskIdUsed(taskId) {
      if (taskId) {
        this.$http({
          url: this.$http.adornUrl(`/taskmanagement/task/checkTaskId`),
          method: 'get',
          params: {
            taskId: taskId
          }
        }).then(response => {
          if (response.data) {
            this.taskIdUsed = true;
            this.$message.error('任务编号已被使用');
          }
        })
      }
    },

    // 生成编号，并回显到planId
    async getPlanId() {

      // 判断是否由指标跳转，如果是获取指标id，并将其回显到关联指标列表
      if (this.$route.query.indicatorId !== undefined) {
        console.log("指标id" + this.$route.query.indicatorId)


        // 赋值给 dataForm.planAssociatedIndicatorsId
        this.dataForm.planAssociatedIndicatorsId = this.$route.query.indicatorId

        // 查找对应的 label
        let indicatorName;
        this.indicatorOptions.forEach(item => {
          // console.log("指标："+JSON.stringify(item) + "指标id：" + this.dataForm.planAssociatedIndicatorsId)

          if (item.value === this.dataForm.planAssociatedIndicatorsId.toString()) {
            indicatorName = item;
            console.log("找到了指标名"+JSON.stringify(item))
          }
        })

        // 如果找到了对应的 label，则手动设置 el-select 的 label
        if (indicatorName) {
          this.$nextTick(() => {
            const elSelect = this.$refs.transferName;
            if (elSelect) {
              elSelect.$emit('input', indicatorName.value);
            }
          });
        }
      }


      // 生成四位随机数字
      let id;
      let pre;
      let nowDate = moment(new Date()).format('YYYYMMDD');

      if (this.dataForm.planAssociatedIndicatorsId !== '') {
        console.log("关联指标id" + this.dataForm.planAssociatedIndicatorsId)
        pre = "ZL" + '-' + "ZB" + '-';
      } else {
        pre = "ZL" + '-' + "RW" + '-';
      }

      for (let i = 1; i < 10; i++) {
        id = pre + nowDate + '-' + i;

        console.log("生成的计划编号为" + id)

        // 向后端发送请求，校验Id
        await this.$http({
          url: this.$http.adornUrl(`/taskmanagement/plan/checkPlanNumber`),
          method: 'get',
          params: {
            planId: id
          }
        }).then(response => {
          this.planIdIsUsed = response.data
        });

        if (this.planIdIsUsed === false) {
          console.log("生成的计划编号未被使用" + this.planIdIsUsed)
          this.dataForm.planId = id;
          this.planIdIsUsed = true;
          break;
        }
      }

    },


  },




  computed: {
    planScheduleDays() {
      if (this.dataForm.planStartDate && this.dataForm.planScheduleCompletionDate) {
        const startDate = new Date(this.dataForm.planStartDate);
        const endDate = new Date(this.dataForm.planScheduleCompletionDate);
        const diffTime = endDate - startDate;
        const diffDays = diffTime / (1000 * 60 * 60 * 24) + 1;
        return diffDays > 0 ? diffDays : 0;
      }
      return '';
    },


  },

  watch: {
    'dataForm.planStartDate': 'updatePlanScheduleDays',
    'dataForm.planScheduleCompletionDate': 'updatePlanScheduleDays',
    //任务时间计算
    'tasks': {
      handler(newTasks) {
        newTasks.forEach(task => {
          this.$watch(() => task.taskStartDate, () => {
            task.taskScheduleDays = this.calculateTaskScheduleDays(task);
          });
          this.$watch(() => task.taskScheduleCompletionDate, () => {
            task.taskScheduleDays = this.calculateTaskScheduleDays(task);
          });
        });
      },
      deep: true,
      immediate: true
    },
  },


}
</script>

<style scoped>
.form-actions {
  margin-top: 20px;
  text-align: right;
}


/* 通用样式 */
.main-content {
  padding: 20px;
}

.el-form {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, .1); */
  box-shadow: 0 0 2px 2px rgba(0, 0, 0, 0.1);
}

.el-form-item {
  margin-bottom: 16px;
}

.el-col {
  margin-bottom: 0px;
}

/* 任务列表样式 */
.task-list-item {
  background-color: #f9fafb;
  padding: 2px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .1);
  transition: all 0.3s ease;
}

.task-list-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, .1);
}

.task-list-title {
  margin-bottom: 8px;
}

/* 按钮样式 */
.form-actions {
  text-align: right;
  margin-top: 20px;
}

.form-actions .el-button {
  margin-left: 10px;
}


.center-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  /* 垂直居中 */
  gap: 10px;
}


.button-col {
  display: flex;
  justify-content: flex-end;
}

.remove-task-button {
  color: #f00;
  font-size: 18px;
  /* 调整字体大小 */
  padding: 10px 20px;
  /* 调整内边距 */
  font-weight: 900;
}
</style>
