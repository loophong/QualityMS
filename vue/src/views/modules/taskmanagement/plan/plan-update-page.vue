<template>
  <div class="main-content">
    <el-container>
      <el-main>
        <h2>{{ this.planId }}</h2>
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
              <!-- <el-form-item label="任务派发" prop="tasksAssignment">
                <el-input v-model="dataForm.tasksAssignment" placeholder="任务派发"></el-input>
              </el-form-item> -->

              <!-- <el-form-item label="审核人" prop="auditor">
                <el-input v-model="dataForm.auditor" placeholder="审核人"></el-input>
              </el-form-item> -->
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

              <el-form-item label="关联指标编号" prop="planAssociatedIndicatorsId">
                <el-input v-model="dataForm.planAssociatedIndicatorsId" placeholder="关联指标编号"></el-input>
              </el-form-item>

              <el-form-item label="负责人" prop="planPrincipal">
                <el-select v-model="dataForm.planPrincipal" filterable placeholder="请选择负责人">
                  <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
              <!-- <el-form-item label="执行人" prop="executor">
                <el-input v-model="dataForm.executor" placeholder="执行人"></el-input>
              </el-form-item> -->
              <el-form-item label="执行人" prop="planExecutor">
                <el-select v-model="dataForm.planExecutor" multiple filterable placeholder="执行人">
                  <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
              <!-- <el-form-item label="当前状态" prop="currentState">
                <el-select v-model="dataForm.currentState" placeholder="请选择当前状态">
                  <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select> -->
              <!-- </el-form-item> -->
            </el-col>
          </el-row>
          <!-- <el-form-item label="计划内容" prop="taskContent">
                <el-input v-model="dataForm.taskContent" placeholder="计划内容"></el-input>
              </el-form-item> -->
          <el-form-item label="计划内容" prop="planContent">
            <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 5 }" placeholder="请输入计划内容"
              v-model="dataForm.planContent" maxlength="1000">
            </el-input>
          </el-form-item>



          <!-- 任务列表 -->
          <div v-for="(task, index) in tasks" :key="index" style="background-color:#EEEEEE;" class="task-list-item">
            <el-form :model="task" :rules="dataRule" ref="taskForm" label-width="120px">

              <!-- <div class="remove-task-icon">
                <h3>{{ '任务 ' + (index + 1) }}</h3>
                <i class="el-icon-close" @click="removeTask(index)" aria-hidden="true"></i>
              </div> -->

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


                  <!-- <el-form-item label="负责人" prop="principal">
                    <el-input v-model="task.principal" placeholder="负责人"></el-input>
                  </el-form-item> -->
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
                  <!-- <el-form-item label="相关计划编号" prop="relevantProjectNumber">
                    <el-input v-model="task.relevantProjectNumber" placeholder="相关计划编号"></el-input>
                  </el-form-item> -->


                  <!-- <el-form-item label="审核人" prop="auditor">
                    <el-input v-model="task.auditor" placeholder="审核人"></el-input>
                  </el-form-item> -->
                  <el-form-item label="审核人" prop="taskAuditor">
                    <el-select v-model="task.taskAuditor" filterable placeholder="请选择审核人">
                      <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                        <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                          :value="item.value">
                        </el-option>
                      </el-option-group>
                    </el-select>
                  </el-form-item>

                  <!-- <el-form-item label="执行人" prop="executor">
                    <el-input v-model="task.executor" placeholder="执行人"></el-input>
                  </el-form-item> -->
                  <el-form-item label="执行人" prop="taskExecutor">
                    <el-select v-model="task.taskExecutor" multiple filterable placeholder="执行人">
                      <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                        <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                          :value="item.value">
                        </el-option>
                      </el-option-group>
                    </el-select>
                  </el-form-item>



                  <!-- <el-form-item label="当前状态" prop="currentState">
                    <el-select v-model="task.currentState" placeholder="请选择当前状态">
                      <el-option v-for="item in statusOptions" :key="item.value" :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item> -->

                  <!-- <el-button type="danger" @click="removeTask(index)">移除</el-button> -->

                </el-col>


              </el-row>


              <!-- <el-form-item label="任务内容" prop="taskContent">
                <el-input v-model="task.taskContent" placeholder="任务内容"></el-input>
              </el-form-item> -->

              <el-form-item label="任务内容" prop="taskContent">
                <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 5 }" placeholder="请输入任务内容"
                  v-model="task.taskContent" maxlength="1000">
                </el-input>
              </el-form-item>



            </el-form>
          </div>

          <!-- 添加任务按钮
          <el-form-item>
            <el-button type="primary" @click="addTask()">添加任务</el-button>
          </el-form-item> -->
        </el-form>


        <!-- 添加任务按钮 -->
        <!-- <div class="form-actions">
            <el-button type="primary" @click="addTask()">添加任务</el-button>
          </div> -->
        <!-- 表单操作按钮 -->
        <div class="form-actions center-buttons">
          <el-button type="primary" @click="addTask()">添加任务</el-button>
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="dataFormSubmit()">保存</el-button>
        </div>

        <!-- <div class="form-actions">
            <el-button @click="cancel">取消</el-button>
            <el-button type="primary" @click="dataFormSubmit()">保存</el-button>
          </div> -->
      </el-main>
    </el-container>
  </div>
</template>




<script>
import axios from 'axios';

export default {
  data() {
    return {
      title: '新建计划',
      visible: false,

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
        // planIsCompleted: '',
        // planIsOverdue: '',
        // planIsOnTime: '',
        // planEarlyCompletionDays: '',
        // planLagDays: '',
        // planLagReasons: '',
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

      tasks: [], // 初始化成员列表
      dataRule: {
        planId: [
          { required: true, message: '计划编号不能为空', trigger: 'blur' },
          { validator: this.validatePlanId, trigger: 'blur' }
        ],
        planName: [
          { required: true, message: '计划名不能为空', trigger: 'blur' }
        ],
        planContent: [
          { required: true, message: '任务内容不能为空', trigger: 'blur' }
        ],
        planStartDate: [
          { required: true, message: '开始日期不能为空', trigger: 'blur' }
        ],
        planScheduleCompletionDate: [
          {
            required: true,
            validator: (rule, value, callback) => this.dateValidate(rule, value, callback),
            trigger: 'blur'
          }
        ],
        planPrincipal: [
          { required: true, message: '负责人不能为空', trigger: ['change', 'blur'] }
        ],
        planExecutor: [
          { required: true, message: '执行人不能为空', trigger: 'blur' }
        ],
        planAuditor: [
          { required: true, message: '审核人不能为空', trigger: 'blur' }
        ],
        planAssociatedTasksId: [
          { required: true, message: '关联指标编号不能为空', trigger: 'blur' }
        ],
      },

      statusOptions: [
        { value: '未开始', label: '未开始' },
        { value: '进行中', label: '进行中' },
        { value: '已完成', label: '已完成' },
      ],

      //员工列表
      options: [{
        label: '企管科',
        options: [{
          value: 'Shanghai',
          label: '张科长'
        }, {
          value: 'Beijing',
          label: '李部长'
        }]
      }, {
        label: '生产科',
        options: [{
          value: 'Chengdu',
          label: '刘工'
        }, {
          value: 'Shenzhen',
          label: '李工'
        }, {
          value: 'Guangzhou',
          label: '张工'
        }, {
          value: 'Dalian',
          label: '何工'
        }]
      }],
      value: '',
    }
  },


  async created() {
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


  methods: {
    //
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

    cancel() {
      this.$router.push({ name: 'TasksList' }); // 假设你有一个名为TasksList的路由
    },

    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {

          // const membersData = this.dataForm.members.map(member => member.name);
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
            taskCurrentState: task.taskCurrentState,
            taskAssociatedIndicatorsId: this.dataForm.planAssociatedIndicatorsId
          }));

          this.$http({
            url: this.$http.adornUrl(`/taskmanagement/plan/save`),
            method: 'post',
            data: this.$http.adornData({
              'plan': {
                'tmPid': this.dataForm.tmPid || undefined,
                'planId': this.dataForm.planId,
                'planName': this.dataForm.planName,
                'planContent': this.dataForm.planContent,
                'planStartDate': this.dataForm.planStartDate,
                'planScheduleCompletionDate': this.dataForm.planScheduleCompletionDate,
                'planScheduleDays': this.planScheduleDays,
                'planActualCompletionDate': this.dataForm.planActualCompletionDate,
                'planActualDays': this.dataForm.planActualDays,
                'planTasksAssignment': this.dataForm.planTasksAssignment,
                'planAssociatedTasksId': this.dataForm.planAssociatedTasksId,
                'planSchedule': this.dataForm.planSchedule,
                'planPrincipal': this.dataForm.planPrincipal,
                'planExecutor': this.dataForm.planExecutor,
                'planAuditor': this.dataForm.planAuditor,
                'planCurrentState': this.dataForm.planCurrentState,
                'planIsCompleted': this.dataForm.planIsCompleted,
                'planIsOverdue': this.dataForm.planIsOverdue,
                'planIsOnTime': this.dataForm.planIsOnTime,
                'planEarlyCompletionDays': this.dataForm.planEarlyCompletionDays,
                'planLagDays': this.dataForm.planLagDays,
                'planLagReasons': this.dataForm.planLagReasons,
                'planAssociatedIndicatorsId': this.dataForm.planAssociatedIndicatorsId,
              },

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
      this.tasks.splice(index, 1);
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


    // checkPlanNumber() {
    //   if (this.dataForm.planId) {
    //     this.$http({
    //       url: this.$http.adornUrl(`/taskmanagement/plan/checkPlanNumber`),
    //       method: 'get',
    //       params: {
    //         planId: this.dataForm.planId
    //       }
    //     }).then(response => {
    //       if (response.data) {
    //         this.$message.error('计划编号已被使用');
    //       }
    //     })
    //       .catch(error => {
    //         console.error(error);
    //       });
    //   }
    // },



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