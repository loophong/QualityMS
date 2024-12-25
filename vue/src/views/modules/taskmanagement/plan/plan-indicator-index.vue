<template>
  <div class="mod-config">
    <el-tabs type="border-card">
      <el-tab-pane label="当前计划">

        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataQueryList()">
          <el-form-item label="计划编号">
            <el-input v-model="queryParams.planId" placeholder="参数名"></el-input>
          </el-form-item>
          <el-form-item label="计划名">
            <el-input v-model="queryParams.planName" placeholder="计划名"></el-input>
          </el-form-item>
          <el-form-item label="指标名">
            <!--     下拉选择框       -->
            <el-select v-model="queryParams.planAssociatedIndicatorsId" placeholder="请选择指标">
              <el-option v-for="item in indicatorOptions" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="getDataQueryList()">查询</el-button>
            <el-button @click="clearUnfinishedPage()">重置</el-button>
            <el-button v-if="isAuth('taskmanagement:plan:save')" type="primary" @click="addPlanPage()">新增</el-button>
            <el-button @click="exportFile()">导出</el-button>
          </el-form-item>
        </el-form>

        <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
                  style="width: 100%;">
          <!-- <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column> -->
          <el-table-column prop="planId" header-align="center" align="center" label="计划编号" width="100">
          </el-table-column>
          <el-table-column prop="planName" header-align="center" align="center" label="计划名" width="150">
          </el-table-column>
          <el-table-column prop="planContent" header-align="center" align="center" label="计划内容">
          </el-table-column>
          <el-table-column prop="planStartDate" header-align="center" align="center" label="开始日期" width="110">
          </el-table-column>
          <el-table-column prop="planScheduleCompletionDate" header-align="center" align="center" label="计划完成日期"
                           width="110">
          </el-table-column>
          <el-table-column prop="planScheduleDays" header-align="center" align="center" label="计划天数">
          </el-table-column>
          <el-table-column prop="planActualCompletionDate" header-align="center" align="center" label="实际完成日期"
                           width="110">
            <template slot-scope="scope">
              <span v-if="scope.row.planActualCompletionDate === null" style="color: gray;">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="planActualDays" header-align="center" align="center" label="实际天数">
            <template slot-scope="scope">
              <span v-if="scope.row.planActualDays === null" style="color: gray;">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="planTasksAssignment" header-align="center" align="center" label="任务派发">
          </el-table-column>
          <el-table-column prop="planAssociatedTasksId" header-align="center" align="center" label="关联任务编号"
                           width="110">
          </el-table-column>
          <el-table-column prop="planSchedule" header-align="center" align="center" label="进度">
          </el-table-column>
          <el-table-column prop="planPrincipal" header-align="center" align="center" label="负责人">
            <template slot-scope="scope">
              {{ getUsernameByUserId(scope.row.planPrincipal) }}
            </template>
          </el-table-column>

          <el-table-column prop="planExecutor" header-align="center" align="center" label="执行人" width="110">
            <template slot-scope="scope">
          <span v-for="(executorId, index) in scope.row.planExecutor" :key="executorId">
            {{ getUsernameByUserId(executorId) }}
            <span v-if="index !== scope.row.planExecutor.length - 1">, </span>
          </span>
            </template>
          </el-table-column>
          <el-table-column prop="planAuditor" header-align="center" align="center" label="审核人">
            <template slot-scope="scope">
              {{ getUsernameByUserId(scope.row.planAuditor) }}
            </template>
          </el-table-column>
          <el-table-column prop="planCurrentState" label="当前状态" header-align="center" align="center">
            <template slot-scope="scope">
          <span v-if="scope.row.planCurrentState === 'NOT_STARTED'">
            <el-tag type="info" disable-transitions>未开始</el-tag></span>
              <span v-else-if="scope.row.planCurrentState === 'IN_PROGRESS'">
            <el-tag disable-transitions>进行中</el-tag></span>
              <span v-else-if="scope.row.planCurrentState === 'APPROVAL_IN_PROGRESS'">
            <el-tag disable-transitions>审批中</el-tag></span>
              <span v-else-if="scope.row.planCurrentState === 'OVERDUE'">
            <el-tag type="danger" disable-transitions>已逾期</el-tag></span>
              <span v-else-if="scope.row.planCurrentState === 'COMPLETED'">
            <el-tag type="success" disable-transitions>已完成</el-tag></span>
              <span v-else-if="scope.row.planCurrentState === 'CREATED_BUT_NOT_APPROVED'">
            <el-tag type="success" disable-transitions>未初审</el-tag></span>
              <span v-else-if="scope.row.planCurrentState === 'PREAPPROVAL_IN_PROGRESS'">
            <el-tag type="success" disable-transitions>初审中</el-tag></span>
              <span v-else-if="scope.row.planCurrentState === 'PREAPPROVAL_NOT_PASSED'">
            <el-tag type="danger" disable-transitions>未通过初审</el-tag></span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="planIsCompleted" label="是否完成" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.planIsCompleted === 0" style="color: gray;">否</span>
              <span v-else-if="scope.row.planIsCompleted === 1" style="color: gray;">是</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="planIsOverdue" label="是否超期" header-align="center" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.planIsOverdue === 0" style="color: gray;">否</span>
              <span v-else-if="scope.row.planIsOverdue === 1" style="color: gray;">是</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="planIsOnTime" label="是否按时完工" header-align="center" align="center" width="110">
            <template slot-scope="scope">
              <span v-if="scope.row.planIsOnTime === 0">否</span>
              <span v-else-if="scope.row.planIsOnTime === 1">是</span>
              <span v-else>-</span> <!-- 处理未知状态 -->
            </template>
          </el-table-column>
          <el-table-column prop="planEarlyCompletionDays" header-align="center" align="center" label="提前完工">
            <template slot-scope="scope">
              {{
                scope.row.planEarlyCompletionDays !== null && scope.row.planEarlyCompletionDays !== undefined &&
                scope.row.planEarlyCompletionDays !== '' ? scope.row.planEarlyCompletionDays : '-'
              }}
            </template>
          </el-table-column>
          <el-table-column prop="planLagDays" header-align="center" align="center" label="滞后天数">
            <template slot-scope="scope">
              {{
                scope.row.planLagDays !== null && scope.row.planLagDays !== undefined && scope.row.planLagDays !== '' ?
                  scope.row.planLagDays : '-'
              }}
            </template>
          </el-table-column>
          <el-table-column prop="planLagReasons" header-align="center" align="center" label="滞后原因">
            <template slot-scope="scope">
              {{
                scope.row.planLagReasons !== null && scope.row.planLagReasons !== undefined && scope.row.planLagReasons !==
                '' ? scope.row.planLagReasons : '-'
              }}
            </template>
          </el-table-column>
          <el-table-column prop="taskAssociatedIndicatorsId" header-align="center" align="center" label="关联指标名">
            <template slot-scope="scope">
              {{ getIndicatorNameById(scope.row.planAssociatedIndicatorsId) }}
            </template>
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <!--     如果planFile不为null,显示下载附件按钮     -->
              <!--              <el-button v-if="scope.row.planFile !== null" type="text" size="small"-->
              <!--                         @click="downloadFile(scope.row.planFile)">下载附件-->
              <!--              </el-button>-->
              <el-button type="text" size="small" @click="viewAttachments(scope.row.planId)">计划附件</el-button>
              <el-button type="text" size="small" @click="showPlanTree(scope.row.planId)">查看结构</el-button>
              <el-button v-if="scope.row.planCurrentState !== 'PREAPPROVAL_IN_PROGRESS'"
                         type="text" size="small" @click="updatePlanPage(scope.row)">修改
              </el-button>
              <el-button v-if="scope.row.planCurrentState === 'PREAPPROVAL_IN_PROGRESS'"
                         type="text" size="small" @click="cancelPreApproval(scope.row.planId)">取消初审
              </el-button>
              <el-button type="text" size="small" @click="deleteHandle(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
                       :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
                       layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

      </el-tab-pane>
    </el-tabs>


    <div>
      <!--       弹窗组件-->
      <el-dialog :visible.sync="filesDialogVisible" title="附件列表">
        <ul>
          <el-form>
            <el-row v-for="file in files" :key="file.name" style="margin-bottom: 4px">
              <el-col :span="12">
                {{ file.name }}
              </el-col>
              <el-col :span="12">
                <el-button type="primary" @click="downloadFile(file.url)">下载</el-button>
              </el-col>
            </el-row>
          </el-form>
        </ul>
      </el-dialog>
    </div>
  </div>


</template>

<script>


export default {
  data() {
    return {
      queryParams: {
        planId: '',
        planName: '',
        planAssociatedIndicatorsId: ''
      },

      dataForm: {
        key: ''
      },
      finishedDataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],

      // 已完成计划，数据和分页
      finishedPlanList: [],
      finishedPageIndex: 1,
      finishedPageSize: 10,
      finishedTotalPage: 0,
      finishedListLoading: false,

      addOrUpdateVisible: false,

      //员工列表
      options: [],
      indicatorOptions: [],

      // 附件弹窗组件
      filesDialogVisible: false,
      // 附件列表
      files: [],

      // 审批相关
      approvalDataList: [],
      approvalPageIndex: 1,
      approvalPageSize: 10,
      approvalTotalPage: 0,
      approvalListLoading: false,

    }
  },


  activated() {
    // this.getDataQueryList()
  },

  async created() {
    // 获取分组后的员工数据
    await this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getName`),
      method: 'get',
    }).then(({data}) => {
      this.options = data;
      console.log(this.options);
    });

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
    });

    // 获取计划详情,初始化页面
    this.init(JSON.parse(this.$route.query.data));
  },


  methods: {
    // 获取数据列表
    init(data) {
      this.dataListLoading = true
      console.log("data："+JSON.stringify(data) )
      // console.log("indicatorId："+indicatorId)

      // 将planAssociatedIndicatorsId封装入plan中
      const plan = {
        'planAssociatedIndicatorsId' : data.indicatorId
      }
      this.queryParams.planAssociatedIndicatorsId = data.indicatorName
      console.log("参数pLan："+plan.planAssociatedIndicatorsId)
      this.$http({
        // url: this.$http.adornUrl('/taskmanagement/plan/list'),
        url: this.$http.adornUrl('/taskmanagement/plan/queryPageByParams'),
        method: 'post',
        data: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'plan': plan
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },

    // 获取数据列表
    getDataQueryList() {
      this.dataListLoading = true
      console.log("queryParams"+this.queryParams)
      this.$http({
        // url: this.$http.adornUrl('/taskmanagement/plan/list'),
        url: this.$http.adornUrl('/taskmanagement/plan/queryPageByParams'),
        method: 'post',
        data: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'plan': this.queryParams
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },

    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataQueryList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataQueryList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },


    addPlanPage() {
      this.$router.push({
        name: 'plan-add-page',
      });
    },

    // 修改
    updatePlanPage(row) {
      // console.log("传入的row为 :"+JSON.stringify(row) )
      const planId = row.planId
      if (row.planCurrentState === "PREAPPROVAL_NOT_PASSED" || row.planCurrentState === "CREATED_BUT_NOT_APPROVED") {
        console.log("初审未通过")
        // 使用Vue Router进行页面跳转
        this.$router.push({
          name: 'plan-reject-reapproval-page',
          query: {planId} // 如果需要，可以通过query传递参数
        });
      } else {
        console.log("其他状态")
        // 使用Vue Router进行页面跳转
        this.$router.push({
          name: 'plan-update-page',
          query: {planId} // 如果需要，可以通过query传递参数
        });
      }
    },

    // 查看计划树
    showPlanTree(planId) {
      // 使用Vue Router进行页面跳转
      this.$router.push({
        name: 'plan-tree',
        query: {planId} // 如果需要，可以通过query传递参数
      });
    },


    // 获取用户编号
    getUsernameByUserId(auditorId) {
      for (const category of this.options) {
        for (const auditor of category.options) {
          if (auditor.value === auditorId) {
            return auditor.label;
          }
        }
      }
      return "-";
    },

    // 下载附件
    downloadFile(url) {
      // 获取当前的 token，假设它存储在 localStorage 中
      const token = this.$cookie.get('token');
      if (token) {
        console.log('Token found:', token);
      } else {
        console.error('Token not found!');
      }

      // 将 token 作为参数添加到 URL
      // const imageUrlWithToken = `${url}?token=${token}`;

      // 使用window.open方法打开下载链接
      // window.open(imageUrlWithToken);

      // 拼接带有 token 的请求地址
      const fileUrl = `${this.$http.adornUrl(`/generator/issuetable/${url}`)}?token=${token}`;
      window.open(fileUrl);
    },

    viewAttachments(planId) {
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/plan/files/' + planId),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log("当前文件附件为" + JSON.stringify(data))
          this.files = data.files
          this.filesDialogVisible = true
        }
      })
    },

    // 获取指标名称
    getIndicatorNameById(indicatorId) {
      for (const indicator of this.indicatorOptions) {

        console.log("indicator:" + indicator);
        if (indicator.value === indicatorId) {
          return indicator.label;
        }
      }
      return "-";
    },


    exportFile() {
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/plan/export'),
        method: 'get',
        params: this.$http.adornParams(),
        responseType: 'blob'
      }).then(res => {
        // 创建 Blob 对象
        const blob = new Blob([res.data], {type: 'application/octet-stream'});

        // 创建一个临时的 URL
        const url = window.URL.createObjectURL(blob);

        // 创建一个隐藏的 <a> 标签
        const link = document.createElement('a');
        link.href = url;
        // 获取当前时间
        const currentTime = new Date();
        // 格式化为中国时间
        var time = currentTime.toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
        }).replace(/\/|:/g, '-').replace(' ', ' ');

        // 设置下载的文件名
        link.download = time + '计划导出.xlsx'; // 你可以根据实际情况设置文件名

        // 将 <a> 标签添加到文档中
        document.body.appendChild(link);

        // 触发点击事件
        link.click();

        // 移除 <a> 标签
        document.body.removeChild(link);

        // 释放 URL 对象
        window.URL.revokeObjectURL(url);
      })
    },
    // 清空页码数据
    clearUnfinishedPage() {
      this.pageIndex = 1
      this.queryParamsUnfinished.planId = ''
      this.queryParamsUnfinished.planName = ''
      this.getDataQueryList()
    },


  }
}
</script>
