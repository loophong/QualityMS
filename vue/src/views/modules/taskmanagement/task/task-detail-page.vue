<template>
  <div class="main-content">
    <el-container>
      <el-main>
        <h2>{{ title }}</h2>
        <el-form :model="rootTask" ref="rootTask" @keyup.enter.native="taskDecompositionSubmit()" label-width="120px">

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="任务编号" prop="taskId">
                <el-input v-model="rootTask.taskId" disabled placeholder="任务编号"></el-input>
              </el-form-item>
              <el-form-item label="开始日期" prop="taskStartDate">
                <el-date-picker v-model="rootTask.taskStartDate" disabled type="date" placeholder="选择开始日期"
                  style="width: 100%;"></el-date-picker>
              </el-form-item>
              <el-form-item label="任务天数" prop="taskScheduleDays">
                <el-input :value="rootTask.taskScheduleDays" disabled placeholder="任务天数"></el-input>
              </el-form-item>

              <el-form-item label="负责人" prop="taskPrincipal">
                <el-select v-model="rootTask.taskPrincipal" disabled filterable placeholder="请选择负责人">
                  <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>

            </el-col>

            <el-col :span="12">
              <el-form-item label="任务名" prop="taskName">
                <el-input v-model="rootTask.taskName" disabled placeholder="任务名"></el-input>
              </el-form-item>

              <el-form-item label="预计完成日期" prop="taskScheduleCompletionDate">
                <el-date-picker v-model="rootTask.taskScheduleCompletionDate" disabled type="date" placeholder="选择任务完成日期"
                  style="width: 100%;"></el-date-picker>
              </el-form-item>

              <el-form-item label="审核人" prop="taskAuditor">
                <el-select v-model="rootTask.taskAuditor" disabled filterable placeholder="请选择审核人">
                  <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>

              <el-form-item label="执行人" prop="taskExecutor">
                <el-select v-model="rootTask.taskExecutor" disabled multiple filterable placeholder="执行人">
                  <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
            </el-col>

          </el-row>

          <el-form-item label="任务内容" prop="taskContent">
            <el-input type="textarea" :autosize="{ minRows: 5, maxRows: 5 }" placeholder="请输入任务内容"
              v-model="rootTask.taskContent" disabled maxlength="1000">
            </el-input>
          </el-form-item>



          <!-- 任务列表 -->
          <div v-for="(task, index) in tasks" :key="index" style="background-color:#EEEEEE;" class="task-list-item">
            <el-form :model="task" :rules="taskRule" ref="taskForm" label-width="120px">

              <el-row :gutter="20">
                <el-col :span="12">
                  <h3>{{ '任务 ' + (index + 1) }}</h3>
                </el-col>
                <el-col :span="12" class="button-col">
                  <!-- <i class="el-icon-close" @click="removeTask(index)" aria-hidden="true"></i> -->
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
                <el-input type="textarea" :autosize="{ minRows: 5, maxRows: 5 }" placeholder="请输入任务内容"
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
          <el-button type="primary" @click="taskDecompositionSubmit()">保存</el-button>
        </div>

      </el-main>
    </el-container>
  </div>
</template>




<script>

export default {
  data() {
    return {
      taskIdUsed: false,
      title: '任务' + this.$route.query.taskId + '详情',

      routerPlanId: '',

      // 当前任务-根节点
      rootTask: {
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


      dataForm: { // 计划的基本结构
        tmPid: 0,
        planId: '',
        planName: '',
        planContent: '',
        planStartDate: '',
        planScheduleCompletionDate: '',
        planScheduleDays: '',
        planActualCompletionDate: '',
        planActualDays: '',
        planTasksAssignment: '',
        planAssociatedTasksId: '',
        planSchedule: '',
        planPrincipal: '',
        planExecutor: '',
        planAuditor: '',
        planCurrentState: '',
        planIsCompleted: '',
        planIsOverdue: '',
        planIsOnTime: '',
        planEarlyCompletionDays: '',
        planLagDays: '',
        planLagReasons: '',
        planAssociatedIndicatorsId: ''

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

      // tasks: [{
      //   taskId: '',
      //   taskName: '',
      //   taskContent: '',
      //   taskStartDate: '',
      //   taskScheduleCompletionDate: '',
      //   taskScheduleDays: '',
      //   taskActualCompletionDate: '',
      //   taskActualDays: '',
      //   taskAssociatedPlanId: '',
      //   taskSchedule: '',
      //   taskPrincipal: '',
      //   taskExecutor: '',
      //   taskAuditor: '',
      //   taskCurrentState: '',
      //   taskIsCompleted: '',
      //   taskIsOverdue: '',
      //   taskIsOnTime: '',
      //   taskEarlyCompletionDays: '',
      //   taskLagDays: '',
      //   taskLagReasons: '',
      //   taskAssociatedIndicatorsId: ''
      // }], // 初始化成员列表

      tasks: [], // 初始化成员列表


      taskRule: {
        taskId: [
          { required: true, message: '任务编号不能为空', trigger: 'blur' },
          { validator: this.validateTaskId, trigger: 'blur' }
        ],


      },


      // plan校验规则
      // dataRule: {
      //   planId: [
      //     { required: true, message: '计划编号不能为空', trigger: 'blur' },
      //     { validator: this.validatePlanId, trigger: 'blur' }
      //   ],
      //   planName: [
      //     { required: true, message: '计划名不能为空', trigger: 'blur' }
      //   ],
      //   planContent: [
      //     { required: true, message: '任务内容不能为空', trigger: 'blur' }
      //   ],
      //   planStartDate: [
      //     { required: true, message: '开始日期不能为空', trigger: 'blur' }
      //   ],
      //   planScheduleCompletionDate: [
      //     {
      //       required: true,
      //       validator: (rule, value, callback) => this.dateValidate(rule, value, callback),
      //       trigger: 'blur'
      //     }
      //   ],
      //   planPrincipal: [
      //     { required: true, message: '负责人不能为空', trigger: ['change', 'blur'] }
      //   ],
      //   planExecutor: [
      //     { required: true, message: '执行人不能为空', trigger: 'blur' }
      //   ],
      //   planAuditor: [
      //     { required: true, message: '审核人不能为空', trigger: 'blur' }
      //   ],
      //   // planAssociatedTasksId: [
      //   //     { required: true, message: '关联指标编号不能为空', trigger: 'blur' }
      //   // ],
      // },

      // statusOptions: [
      //   { value: 0, label: '未开始' },
      //   { value: 1, label: '进行中' },
      //   { value: 2, label: '已完成' },
      // ],

      isStatus: [
        { value: 0, label: '否' },
        { value: 1, label: '是' },
      ],

      //员工列表
      options: [],
      value: '',
    }
  },


  async created() {
    // 获取分组后的员工数据
    this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
      method: 'get',
    }).then(({ data }) => {
      this.options = data;
      console.log("员工数据" + data);
    });
    console.log("id" + this.$route.query.taskId);
    this.init(this.$route.query.taskId);
  },

  // updatePlanPage(planId) {
  //   // 使用Vue Router进行页面跳转
  //   this.$router.push({
  //     name: 'plan-update-page',
  //     query: { planId } // 如果需要，可以通过query传递参数

  //   });
  // },
  mounted() {
    // this.routerPlanId = this.$route.query.planId;
    // console.log(this.routerPlanId);
    // this.init(this.routerPlanId);

  },


  methods: {

    init(routerPlanId) {
      // this.dataForm.planId = routerPlanId || 0
      this.$nextTick(() => {
        this.$refs['rootTask'].resetFields()
        if (routerPlanId) {
          this.$http({
            url: this.$http.adornUrl(`/taskmanagement/task/getTaskDetailInfo?taskId=${routerPlanId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data) {
              console.log("任务详情" + data);
              this.rootTask = data.taskDetail.rootTask
              this.tasks = data.taskDetail.tasks
            }
          })
        }
      })
    },

    // 时间校验
    // dateValidate(rule, value, callback) {
    //   let startDate = this.dataForm.planStartDate;
    //   if (!startDate) {
    //     callback(new Error('请先输入开始日期'));
    //   } else if (value === undefined || value === null || value === '') {
    //     callback(new Error('计划日期不能为空'));
    //   } else if (startDate && value) {
    //     if (value < startDate) {
    //       startDate = new Date(startDate); // 转换开始日期为日期对象
    //       value = new Date(value); // 转换计划日期为日期对象
    //       callback(new Error('结束日期不能早于开始日期'));
    //     } else {
    //       callback();
    //     }
    //   }
    // },

    // 取消，返回上一级
    cancel() {
      this.$router.back(); // 假设你有一个名为TasksList的路由
    },

    // 表单提交
    taskDecompositionSubmit() {
      this.$refs['rootTask'].validate((valid) => {
        if (valid) {

          // const membersData = this.dataForm.members.map(member => member.name);
          const tasksData = this.tasks.map(task => ({
            tmTid: task.tmTid || undefined,
            taskId: task.taskId,
            taskName: task.taskName,
            taskContent: task.taskContent,
            taskStartDate: task.taskStartDate,
            taskScheduleCompletionDate: task.taskScheduleCompletionDate,
            taskScheduleDays: task.taskScheduleDays,
            taskAssociatedPlanId: this.rootTask.taskAssociatedPlanId,
            taskPrincipal: task.taskPrincipal,
            taskExecutor: task.taskExecutor,
            taskAuditor: task.taskAuditor,
            taskAssociatedIndicatorsId: this.rootTask.taskAssociatedIndicatorsId,
            taskIsCompleted: 0,
            taskIsOverdue: 0,
            taskIsOnTime: 0,
            taskParentNode: this.rootTask.taskId
          }));

          console.log("任务数据" + tasksData);

          this.$http({
            url: this.$http.adornUrl(`/taskmanagement/task/decomposition`),
            method: 'post',
            data: this.$http.adornData({
              // 'rootTask': '',
              // 'plan': {
              //     'tmTid': this.rootTask.tmTid || undefined,
              //     'taskId': this.rootTask.taskId,
              //     'taskName': this.rootTask.taskName,
              //     'taskContent': this.rootTask.taskContent,
              //     'taskStartDate': this.rootTask.taskStartDate,
              //     'taskScheduleCompletionDate': this.rootTask.taskScheduleCompletionDate,
              //     // 'planScheduleDays': this.planScheduleDays,
              //     'taskScheduleDays': this.rootTask.taskScheduleDays,
              //     // 'planActualCompletionDate': this.rootTask.planActualCompletionDate,
              //     // 'planActualDays': this.rootTask.planActualDays,
              //     'planTasksAssignment': this.rootTask.planTasksAssignment,
              //     'planAssociatedTasksId': this.rootTask.planAssociatedTasksId,
              //     'planSchedule': this.rootTask.planSchedule,
              //     'taskPrincipal': this.rootTask.taskPrincipal,
              //     'taskExecutor': this.rootTask.taskExecutor,
              //     'taskAuditor': this.rootTask.taskAuditor,
              //     'planCurrentState': 0,
              //     'planIsCompleted': 0,
              //     'planIsOverdue': 0,
              //     'planIsOnTime': 0,
              //     'planAssociatedIndicatorsId': this.dataForm.planAssociatedIndicatorsId,
              // },
              'tasks': tasksData
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
    },




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
      // 弹窗询问用户是否确认删除
      this.$confirm('此操作将删除该任务, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteTaskByTaskId(this.tasks[index].tmTid);

        this.tasks.splice(index, 1);
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },

    deleteTaskByTaskId(tmTid) {
      console.log("删除任务" + tmTid);
      this.$http({
        url: this.$http.adornUrl(`/taskmanagement/task/deleteByTaskId`),
        method: 'get',
        params: {
          tmTid: tmTid
        }
      })

    },

    updatePlanScheduleDays() {
      this.$forceUpdate();
    },

    //任务时间计算
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

    // 验证任务编号是否使用
    validateTaskId(rule, value, callback) {
      // 这里可以添加自定义的验证逻辑
      this.checkTaskIdUsed(value);
      if (value === '' || value === null || value === undefined) {
        callback(new Error('请输入任务编号'));
      } else {
        // 检查任务编号是否已存在
        console.log(this.taskIdUsed);
        callback(new Error('此任务编号已被使用，请更换!'));
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
            this.$message.error('任务编号已被使用111');
          }
        })
      }
    },




  },

  computed: {
    // planScheduleDays() {
    //   if (this.dataForm.planStartDate && this.dataForm.planScheduleCompletionDate) {
    //     const startDate = new Date(this.dataForm.planStartDate);
    //     const endDate = new Date(this.dataForm.planScheduleCompletionDate);
    //     const diffTime = endDate - startDate;
    //     const diffDays = diffTime / (1000 * 60 * 60 * 24) + 1;
    //     return diffDays > 0 ? diffDays : 0;
    //   }
    //   return '';
    // },

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