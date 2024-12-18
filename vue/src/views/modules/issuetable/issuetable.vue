<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item label="问题类别" prop="issueCategoryId">
        <el-select v-model="queryParams.issueCategoryId" filterable placeholder="请选择问题类别">
          <el-option
            v-for="item in issueCategoryOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="车型" prop="vehicleTypeId">
        <el-select v-model="queryParams.vehicleTypeId" filterable placeholder="请选择车型">
          <el-option
            v-for="item in vehicleTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="责任科室" prop="responsibleDepartment">
        <el-select v-model="queryParams.responsibleDepartment" filterable placeholder="请选择责任科室">
          <el-option
            v-for="department in departmentOptions"
            :key="department.value"
            :label="department.label"
            :value="department.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getQueryList()">查询</el-button>
        <el-button @click="resetQuery()">重置</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="downloadTemplate()">下载模板</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="exportAll()">导出</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="taskDetailVisible" title="问题详情">
      <div>
        <span style="font-weight: bold; font-size: 16px; margin-right: 10px;">总任务数量：{{ taskDetails.totalCount }}</span>
        <span style="font-weight: bold; font-size: 16px;">已完成数量：{{ taskDetails.completedCount }}</span>
      </div>
      <div id="task-chart" style="width: 100%; height: 400px;"></div> <!-- 用于echarts图表 -->
      <div slot="footer">
        <el-button v-if="isAuth('generator:issuemasktable:admin')" @click="openTaskList">任务列表</el-button>
        <el-button @click="taskDetailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="整改情况"
      :visible.sync="dialogVisible"
      width="400px">
      <p>{{ fullDescription }}</p>
    </el-dialog>
    <el-dialog
      title="原因分析"
      :visible.sync="dialogVisible1"
      width="400px">
      <p>{{ fullCause }}</p>
    </el-dialog>
    <el-dialog
      title="验证整改情况"
      :visible.sync="dialogVisible2"
      width="400px">
      <p>{{ fullRetStates }}</p>
    </el-dialog>
    <el-table
      :data="dataList"
      height="750"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      :row-class-name="rowClassName"
      style="width: 100%;">
    <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="serialNumber"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="序号">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="issueNumber"
        header-align="center"
        align="center"
        label="问题编号">
        <template slot-scope="scope">
           <span :style="{ color: scope.row.overDue === 'true' ? 'red' : 'black' }">
              {{ scope.row.issueNumber }}
            </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="inspectionDepartment"
        header-align="center"
        align="center"
        label="检查科室">
      </el-table-column>
      <el-table-column
        prop="inspectionDate"
        header-align="center"
        align="center"
        label="检查日期">
      </el-table-column>
      <el-table-column
        prop="issueCategoryId"
        header-align="center"
        align="center"
        label="问题类别">
      </el-table-column>
      <el-table-column
        prop="vehicleTypeId"
        header-align="center"
        align="center"
        label="车型">
      </el-table-column>
      <el-table-column
        prop="vehicleNumberId"
        header-align="center"
        align="center"
        label="车号">
      </el-table-column>
            <el-table-column
        prop="issueDescription"
        header-align="center"
        align="center"
        label="问题描述">
      </el-table-column>
      <el-table-column
        prop="peliminaryAnalysis"
        header-align="center"
        align="center"
        label="初步分析">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="issuePhoto"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="问题照片">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="text" size="small" @click="handleFileAction(scope.row.issuePhoto)">预览</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column
        prop="issuePhoto"
        header-align="center"
        align="center"
        label="问题照片">
        <template slot-scope="scope">
          <img
            :src="getImageUrl(scope.row.issuePhoto)"
            alt="问题照片"
            style="width: 100px; height: auto; cursor: pointer;"
            @click="handleFileAction(scope.row.issuePhoto)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="rectificationRequirement"
        header-align="center"
        align="center"
        label="整改要求">
      </el-table-column>
      <el-table-column
        prop="requiredCompletionTime"
        header-align="center"
        align="center"
        label="要求完成时间">
      </el-table-column>
      <el-table-column
        prop="responsibleDepartment"
        header-align="center"
        align="center"
        label="责任科室">
      </el-table-column>
      <el-table-column
        prop="rectificationStatus"
        header-align="center"
        align="center"
        label="整改情况">
        <template slot-scope="scope">
          <div style="max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; cursor: pointer;" @click="showFullDescription(scope.row.rectificationStatus)">
      <span v-if="scope.row.rectificationStatus && scope.row.rectificationStatus.length > 8">
        {{ scope.row.rectificationStatus.slice(0, 8) }}<strong>...</strong> <!-- 显示前八个字符，加粗省略号 -->
      </span>
            <span v-else>
        {{ scope.row.rectificationStatus || '' }} <!-- 显示完整描述，或为空字符串 -->
      </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="actualCompletionTime"
        header-align="center"
        align="center"
        label="实际完成时间">
      </el-table-column>
      <el-table-column
        prop="rectificationPhotoDeliverable"
        header-align="center"
        align="center"
        label="整改照片交付物">
        <template slot-scope="scope">
          <el-button type="text" @click="showFileList(scope.row.rectificationPhotoDeliverable)">
            预览
          </el-button>
        </template>
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="rectificationPhotoDeliverable"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="整改照片交付物">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="text" size="small" @click="handleFileAction(scope.row.rectificationPhotoDeliverable)">预览</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="rectificationPhotoDeliverable"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="整改照片交付物">-->
<!--        <template slot-scope="scope">-->
<!--          <img-->
<!--            :src="getImageUrl(scope.row.rectificationPhotoDeliverable)"-->
<!--            alt="整改照片交付物"-->
<!--            style="width: 100px; height: auto; cursor: pointer;"-->
<!--            @click="handleFileAction(scope.row.rectificationPhotoDeliverable)"-->
<!--          />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column
        prop="rectificationResponsiblePerson"
        header-align="center"
        align="center"
        label="整改责任人">
        <template slot-scope="scope">
          {{ getUsernameByUserId(scope.row.rectificationResponsiblePerson) }}
        </template>
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="requiredSecondRectificationTime"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="要求二次整改时间">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="remark"
        header-align="center"
        align="center"
        label="备注">
      </el-table-column>
      <el-table-column
        prop="creator"
        header-align="center"
        align="center"
        label="创建人">
        <template slot-scope="scope">
          {{ getUsernameByUserId(scope.row.creator) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="creationTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="lastModifier"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="最后修改时间">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="lastModificationTime"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="最后修改时间">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="associatedRectificationRecords"
        header-align="center"
        align="center"
        label="关联问题">
      </el-table-column>

<!--      <el-table-column-->
<!--        prop="associatedIssueAddition"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="关联问题添加">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="creationDuration"
        header-align="center"
        align="center"
        label="创建时长">
      </el-table-column>
      <el-table-column
        prop="causeAnalysis"
        header-align="center"
        align="center"
        label="原因分析">
        <template slot-scope="scope">
          <div style="max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; cursor: pointer;" @click="showFullCauseAnalysis(scope.row.causeAnalysis)">
      <span v-if="scope.row.causeAnalysis && scope.row.causeAnalysis.length > 8">
        {{ scope.row.causeAnalysis.slice(0, 8) }}<strong>...</strong> <!-- 显示前八个字符，加粗省略号 -->
      </span>
            <span v-else>
        {{ scope.row.causeAnalysis || '' }} <!-- 显示完整描述，或为空字符串 -->
      </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="rectificationVerificationStatus"
        header-align="center"
        align="center"
        label="整改验证情况">
        <template slot-scope="scope">
          <div style="max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; cursor: pointer;" @click="showFullRectificationStatus(scope.row.rectificationVerificationStatus)">
      <span v-if="scope.row.rectificationVerificationStatus && scope.row.rectificationVerificationStatus.length > 50">
        {{ scope.row.rectificationVerificationStatus.slice(0, 50) }}<strong>...</strong> <!-- 显示前50个字符，加粗省略号 -->
      </span>
            <span v-else>
        {{ scope.row.rectificationVerificationStatus || '' }} <!-- 显示完整描述，或为空字符串 -->
      </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="verificationConclusion"
        header-align="center"
        align="center"
        label="验证结论">
        <template slot-scope="scope">
          <div>
            <span v-for="(state, index) in getStates(scope.row.verificationConclusion)" :key="index">
                <el-tag v-if="state === '持续'" type="danger" disable-transitions>{{ state }}</el-tag>
<!--                <el-tag v-else-if="state === '已完成'" type="success" disable-transitions>{{ state }}</el-tag>-->
                <el-tag v-else-if="state === '暂缓'" type="info" disable-transitions>{{ state }}</el-tag>
                <el-tag v-else-if="state === '结项'" type="warning" disable-transitions>{{ state }}</el-tag>
<!--                <el-tag v-else-if="state === '持续，暂停'" type="danger" disable-transitions>{{ state }}</el-tag>-->
<!--                <el-tag v-else-if="state === '暂停，未完成'" type="info" disable-transitions>{{ state }}</el-tag>-->
                <el-tag v-else>{{ state }}</el-tag> <!-- 处理未定义的状态 -->
            </span>
          </div>
        </template>
      </el-table-column>

      <el-table-column
        prop="verifier"
        header-align="center"
        align="center"
        label="验证人">
        <template slot-scope="scope">
          {{ getUsernameByUserId(scope.row.verifier) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="formula"
        header-align="center"
        align="center"
        label="公式">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
<!--          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.issueId)">修改</el-button>-->
          <el-button type="text" size="small" @click="showTaskDetails(scope.row.issueNumber, scope.row.issueId)">完成情况</el-button>
          <el-button type="text" size="small" @click="reuseTask(scope.row.issueId)">问题重写</el-button>
          <el-button type="text" size="small" @click="openCloseDialog(scope.row.issueId)">
            问题关闭
          </el-button>
          <el-button type="text" size="small" @click="showAssociatedIssues(scope.row.associatedRectificationRecords)">关联问题</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹出框，用于显示关联问题列表 -->
    <el-dialog
      :visible.sync="dialogVisible3"
      title="关联问题"
      width="30%"
    >
      <el-row>
        <el-col :span="24" v-for="(issue, index) in currentIssues" :key="index">
        <span
          @click="viewIssueDetails(issue)"
          style="display: block; color: #409EFF; cursor: pointer; margin-bottom: 8px;"
        >
          {{ issue }}
        </span>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible3 = false">关闭</el-button>
    </span>
    </el-dialog>
    <el-dialog
      title="附件预览"
      :visible.sync="fileDialogVisible"
      width="50%">
      <el-table :data="fileList" style="width: 100%">
        <el-table-column prop="name" label="文件名称" align="left"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="previewFile(scope.row.url)">
              点击预览
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fileDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
    <!-- 弹窗 -->
    <el-dialog
      title="关闭问题"
      :visible.sync="showCloseDialog"
      width="400px"
      @close="cancelCloseDialog"
    >
      <p>是否关闭此问题以及关联问题？</p>
      <el-radio-group v-model="selectedOption" style="margin-top: 10px;">
        <el-radio :label="1">关闭关联问题</el-radio>
        <el-radio :label="0">不关闭关联问题</el-radio>
      </el-radio-group>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelCloseDialog">取消</el-button>
        <el-button type="primary" @click="closeRelatedTasks">确定</el-button>
      </span>
    </el-dialog>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <add-or-updateD v-if="addOrUpdateVisibleD" ref="addOrUpdateD" @refreshDataList="recigetDataList"></add-or-updateD>
  </div>
</template>

<script>
  import AddOrUpdate from '../issuefind/issuetable-add-or-update.vue'
  import AddOrUpdateD from "./issuetable-add-or-update.vue";
  import {Loading} from "element-ui";
  import * as XLSX from "xlsx";


  // import {isAuth} from "../../../utils";
  export default {
    data () {
      return {
        //关闭问题
        showCloseDialog: false, // 控制弹窗的显示
        selectedOption: 0, // 默认选项，0：不关闭关联问题，1：关闭关联问题
        currentIssueId: null, // 当前操作的问题ID
        //文件预览
        fileDialogVisible: false, // 控制文件预览弹窗显示
        fileList: [], // 存储当前记录的附件列表
        //查询参数
        options:[],
        queryParams:{
          issueCategoryId: '',
          vehicleTypeId: '',
          responsibleDepartment: '',
        },
        tempParams:{
          issueId: '',
          issueNumber: '',
        },
        issueCategoryOptions: [],
        vehicleTypeOptions: [],
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
        heightTable: 0,
        currentIssues: [],
        tableData: [],
        dataForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        reuseTaskVisible: false,
        addOrUpdateVisible: false,
        addOrUpdateVisibleD: false,
        taskDetailVisible: false, // 控制弹窗可见性
        taskDetails: {
          totalCount: 0,
          completedCount: 0,
          inProgressCount: 0,
          reviewingCount: 0,
        },
        dialogVisible: false, // 控制对话框显示
        fullDescription: '' ,   // 用于存储完整描述
        dialogVisible1: false, // 控制对话框显示
        fullCause: '',    // 用于存储完整描述
        fullstatus: '',
        dataList01: [], //列表数据(不分页)
        dialogVisible2: false,
        dialogVisible3: false,
        fullRetStates:'',
      }

    },
    components: {
      AddOrUpdateD,
      AddOrUpdate
    },
    async created(){
      // 获取分组后的员工数据
      this.$http({
        url: this.$http.adornUrl(`/taskmanagement/user/getEmployeesGroupedByDepartment`),
        method: 'get',
      }).then(({data}) => {
        this.options = data;
        console.log(this.options);
      });
    },
    activated () {
      this.fetchIssueCategories()
      this.fetchVehicleTypes()
      this.getDataList()
      // this.fetchData()
    },
    computed: {


    },
    methods: {
      // 显示文件列表弹窗
      showFileList(annex) {
        try {
          if (!annex) {
            this.$message.warning("没有附件可预览！");
            return;
          }
          // 解析数据库中存储的附件信息
          const parsedAnnex = JSON.parse(annex);
          if (Array.isArray(parsedAnnex) && parsedAnnex.length > 0) {
            this.fileList = parsedAnnex;
            this.fileDialogVisible = true; // 打开弹窗
          } else {
            this.$message.warning("附件数据格式不正确！");
          }
        } catch (error) {
          console.error("附件解析失败:", error);
          this.$message.error("附件数据解析失败！");
        }
      },
      // 点击预览具体文件
      previewFile(fileUrl) {
        const token = this.$cookie.get("token"); // 获取当前的 token
        if (!fileUrl) {
          this.$message.warning("文件路径为空，无法预览！");
          return;
        }
        if (!token) {
          this.$message.error("登录已过期，请重新登录！");
          return;
        }
        // 拼接带有 token 的预览地址
        const url = `${this.$http.adornUrl(`/generator/issuetable/${fileUrl}`)}?token=${token}`;
        window.open(url, "_blank");
      },
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
      // rowStyle({ row }) {
      //   console.log("染色")
      //   return row.overDue === 'true' ? { color: 'red' } : {};
      // },
      // formatIssueNumber(row) {
      //   if (row.overDue === 'true') {
      //     // 当 overDue 为 true 时，返回带有红色样式的 HTML
      //     return `<span style="color: red;">${row.issueNumber}</span>`;
      //   }
      //   return `<span>${row.issueNumber}</span>`;
      // },
      rowClassName({ row }) {
        return row.overDue === 'true' ? 'overdue-text' : '';
      },

      openTaskList() {
        // console.log('打开任务列表', this.tempParams.issueId)
        // console.log('打开任务列表', this.tempParams.issueNumber)
        this.taskDetailVisible = false;
        this.$router.push({
          name: 'issue-issueAllmask',
          params: {
            issueId: this.tempParams.issueId,
            issueNumber: this.tempParams.issueNumber
          }
        })
      },
      getImageUrl(fileflag) {
        const token = this.$cookie.get('token'); // 获取当前的 token
        if (!token) {
          console.error('Token not found!');
          return '';
        }
        return `${this.$http.adornUrl(`/generator/issuetable/${fileflag}`)}?token=${token}`;
      },
      handleFileAction(fileflag) {
        const token = this.$cookie.get('token'); // 获取当前的 token
        if (!token) {
          console.error('Token not found!');
          return;
        }
        console.log('获取的地址 ' ,fileflag)
        // 拼接带有 token 的请求地址
        const url = `${this.$http.adornUrl(`/generator/issuetable/${fileflag}`)}?token=${token}`;
          window.open(url);
      },

      getStates(verificationConclusion) {
        if (!verificationConclusion) return [];
        // 按照逗号分隔，并去除多余的空格
        return verificationConclusion.split(',').map(state => state.trim());
      },
      showTaskDetails(issueNumber,issueId) {
        this.fetchTaskDetails(issueNumber);
        this.tempParams.issueId = issueId;
        this.tempParams.issueNumber = issueNumber;
        this.taskDetailVisible = true; // 显示弹窗
      },
      fetchTaskDetails(issueNumber) {
        this.$http({
          url: this.$http.adornUrl(`/generator/issuetable/taskDetails/${issueNumber}`), // 请求后端API
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.taskDetails.totalCount = data.result.totalCount; // 总任务数量
            this.taskDetails.completedCount = data.result.completedCount; // 已完成数量
            this.taskDetails.inProgressCount = data.result.inProgressCount; // 执行中任务数量
            this.taskDetails.reviewingCount = data.result.reviewingCount; // 审核中任务数量

            // 调用方法绘制ECharts图表
            this.initECharts();
          } else {
            this.$message.error(data.msg);
          }
        });
      },
      initECharts() {
        // 使用ECharts初始化环形图
        const chartDom = document.getElementById('task-chart');
        const myChart = echarts.init(chartDom);

        // 判断任务状态数量，如果没有数据，则默认显示一个绿色环
        const hasData = this.taskDetails.completedCount > 0 || this.taskDetails.inProgressCount > 0 || this.taskDetails.reviewingCount > 0;

        const option = {
          series: [
            {
              name: '任务状态',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false, // 不显示文字
              },
              emphasis: {
                label: {
                  show: false, // 不显示文字
                },
              },
              labelLine: {
                show: false, // 不显示文字连接线
              },
              data: hasData ? [ // 根据是否有数据选择数据
                { value: this.taskDetails.completedCount, name: '已完成' },
                { value: this.taskDetails.inProgressCount, name: '执行中' },
                { value: this.taskDetails.reviewingCount, name: '审核中' },
              ] : [
                { value: 1, name: '无任务', itemStyle: { color: 'green' } } // 默认显示一个绿色的环
              ]
            }
          ]
        };

        myChart.setOption(option);
      }
      ,
      showAssociatedIssues(associatedRectificationRecords) {
        // 将数据库中的字符串分割成数组
        this.currentIssues = associatedRectificationRecords.split(',');
        this.dialogVisible3 = true; // 打开对话框
      },
      viewIssueDetails(associatedRectificationRecords) {
        // 将数据库中的字符串分割成数组
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.viewIssueDetails(associatedRectificationRecords)
        })
        this.dialogVisible3 = false; // 打开对话框
      },
      // getRowClassName({ row, rowIndex }) {
      //   return rowIndex % 2 === 0 ? 'row-even' : 'row-odd';
      // },
      // 关闭相关任务
      // 打开弹窗
      openCloseDialog(issueId) {
        this.currentIssueId = issueId; // 记录当前问题ID
        this.showCloseDialog = true; // 显示弹窗
      },
      // 提交关闭问题请求
      closeRelatedTasks() {
        this.$http({
          url: this.$http.adornUrl(
            `/generator/issuetable/closeRelatedTasks/${this.currentIssueId}/${this.selectedOption}`
          ),
          method: 'post',
        })
          .then(({ data }) => {
            if (data && data.code === 0) {
              // 关闭成功提示
              this.$message({
                message: '任务已成功关闭',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  // 刷新列表
                  this.getDataList();
                },
              });
            } else {
              // 失败提示
              this.$message.error(data.msg);
            }
          })
          .catch(() => {
            this.$message.error('操作失败，请稍后重试');
          })
          .finally(() => {
            this.showCloseDialog = false; // 关闭弹窗
          });
      },
      // 取消操作
      cancelCloseDialog() {
        this.showCloseDialog = false; // 隐藏弹窗
        // this.$message.info('操作已取消');
      },

      // fetchData () {
      //   // Assuming you have an API endpoint to fetch the data
      //   fetch('/api/data')
      //     .then(response => response.json())
      //     .then(data => {
      //       const promises = data.map(item => {
      //         if (item.issuePhoto) {
      //           return fetch(`/api/images/${item.issuePhoto}`)
      //             .then(response => response.blob())
      //             .then(blob => this.blobToBase64(blob))
      //             .then(base64Data => {
      //               item.issuePhoto = base64Data
      //             })
      //         }
      //         return Promise.resolve()
      //       })
      //
      //       Promise.all(promises).then(() => {
      //         this.tableData = data
      //       })
      //     })
      // },
      blobToBase64 (blob) {
        return new Promise((resolve, reject) => {
          const reader = new FileReader()
          reader.onloadend = () => resolve(reader.result.split(',')[1])
          reader.onerror = reject
          reader.readAsDataURL(blob)
        })
      },
      // getDataList () {
      //   this.dataListLoading = true
      //   this.$http({
      //     url: this.$http.adornUrl('/generator/issuetable/list'),
      //     method: 'get',
      //     params: this.$http.adornParams({
      //       'page': this.pageIndex,
      //       'limit': this.pageSize,
      //       'key': this.dataForm.key
      //     })
      //   }).then(({data}) => {
      //     if (data && data.code === 0) {
      //       this.dataList = data.page.list.map(item => {
      //         // 确保图片路径有效
      //         if (!item.issuePhoto || !this.isValidImageUrl(item.issuePhoto)) {
      //           // item.issuePhoto = '默认图片路径' // 设置默认图片路径
      //         }
      //         return item
      //       })
      //       this.totalPage = data.page.totalCount
      //     } else {
      //       this.dataList = []
      //       this.totalPage = 0
      //     }
      //     this.dataListLoading = false
      //   })
      // },
      getDataList () {
        this.dataListLoading = true;
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list.map(item => {
              if (!item.issuePhoto || !this.isValidImageUrl(item.issuePhoto)) {
                // item.issuePhoto = '默认图片路径';
              }
              return item;
            });
            this.heightTable=data.page.totalCount*300;
            console.log(this.heightTable)

            // 排序操作前，输出每个元素的 overDue 和 creationTime
            // console.log("排序前的 dataList: ", this.dataList);
            this.dataList.forEach(item => {
              // console.log(`item.overDue: ${item.overDue}, type: ${typeof item.overDue}`);
              // console.log(`item.creationTime: ${item.creationTime}, type: ${typeof item.creationTime}`);
            });

            // 排序操作
            // this.dataList.sort((a, b) => {
            //   // 转换 overDue 为布尔值，'true' 转为 true，其他转为 false
            //   const isOverDueA = a.overDue === 'true';
            //   const isOverDueB = b.overDue === 'true';
            //
            //   // 优先按照 overDue 排序，overDue 为 true 的排在前面
            //   if (isOverDueA !== isOverDueB) {
            //     return isOverDueA ? -1 : 1;
            //   }
            //
            //   // 如果 overDue 相同，则按创建时间排序
            //   return new Date(b.creationTime) - new Date(a.creationTime);
            // });

            // 排序后再输出一次以确认排序结果
            // console.log("排序后的 dataList: ", this.dataList);

            this.totalPage = data.page.totalCount;
          } else {
            this.dataList = [];
            this.totalPage = 0;
          }
          this.dataListLoading = false;
        });
      },


      getQueryList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/Querylist'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'issueCategoryId': this.queryParams.issueCategoryId,
            'vehicleTypeId': this.queryParams.vehicleTypeId,
            'responsibleDepartment': this.queryParams.responsibleDepartment,
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list.map(item => {
              // 确保图片路径有效
              if (!item.issuePhoto || !this.isValidImageUrl(item.issuePhoto)) {
                // item.issuePhoto = '默认图片路径' // 设置默认图片路径
              }
              return item
            })
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      //重置
      resetQuery() {
        this.queryParams = {
          indicatorName: null,
          indicatorValue: null,
          yearMonth: null,
          indicatorValueUpperBound: null,
          indicatorValueLowerBound: null,
          assessmentDepartment: null,
          managementDepartment: null,
          indicatorDefinition: null,
          indicatorClassification: null,
          managementContentCurrentAnalysis: null,
        }
        this.getDataList()
      },
      // 问题重写
      reuseTask (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.reuseIssue(id)
        })
      },
      isValidImageUrl (url) {
        // 简单的URL验证，可以根据需要扩展
        return url && (url.startsWith('http://') || url.startsWith('https://'))
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.issueId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/generator/issuetable/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },
      truncateDescription(description) {
        if (!description || typeof description !== 'string') return '';
        return description.length > 20 ? description.slice(0, 20) : description;
      },


      showFullDescription(description) {
        this.fullDescription = description; // 存储完整描述
        this.dialogVisible = true; // 显示对话框
      },
      // 新增的完整原因分析显示方法
      showFullCauseAnalysis(causeAnalysis) {
        this.fullCause = causeAnalysis; // 存储原因分析完整描述
        this.dialogVisible1 = true; // 显示对话框
      },
      // 新增显示整改验证情况的完整方法
      showFullRectificationStatus(rectificationVerificationStatus) {
        this.fullRetStates = rectificationVerificationStatus; // 存储整改验证情况完整描述
        this.dialogVisible2 = true; // 显示对话框
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
      downloadTemplate() {
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/generateTemplate'), // 替换为实际的下载模板API
          method: 'get',
          responseType: 'blob', // 设置响应类型为blob以便下载文件
        }).then(response => {
          const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }); // 设置blob类型
          const link = document.createElement('a');
          link.href = window.URL.createObjectURL(blob); // 创建Object URL
          link.setAttribute('download', 'template.xlsx'); // 设置下载文件名
          document.body.appendChild(link);
          link.click(); // 触发下载
          document.body.removeChild(link); // 下载后移除链接
        }).catch(error => {
          this.$message.error('下载模板失败，请重试！');
          console.error(error);
        });
      },
      // 导出
      exportAll(){
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/issuesAll'),
          method: 'get',
          params: this.$http.adornParams({
            'key': this.queryParams
          })
        }).then(({data}) => {
          if (data) {
            this.dataList01 = data
          } else {
            this.dataList01 = []
          }
        })
        const loadingInstance = Loading.service({
          lock: true,
          text: "正在导出，请稍后...",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });

        const promises = this.dataList01.map((tableRow, index) => {
          return {
            序号: index + 1,
            问题编号: tableRow.issueNumber,
            检查科室: tableRow.inspectionDepartment,
            检查日期: tableRow.inspectionDate,
            问题类别: tableRow.issueCategoryId,
            系统分类: tableRow.systematicClassification,
            故障类别: tableRow.faultType,
            故障件一级: tableRow.firstFaultyParts,
            故障件二级: tableRow.secondFaultyParts,
            故障模式: tableRow.faultModel,
            车型: tableRow.vehicleTypeId,
            车号: tableRow.vehicleNumberId,
            初步分析: tableRow.peliminaryAnalysis,
            问题描述: tableRow.issueDescription,
            整改要求: tableRow.rectificationRequirement,
            要求完成时间: tableRow.requiredCompletionTime,
            责任科室: tableRow.responsibleDepartment,
            整改情况: tableRow.rectificationStatus,
            实际完成时间: tableRow.actualCompletionTime,
            整改责任人: tableRow.rectificationResponsiblePerson,
            关联问题: tableRow.associatedIssueAddition,
            整改验证情况: tableRow.rectificationVerificationStatus,
            验证截止时间: tableRow.verificationDeadline,
            验证结论: tableRow.verificationConclusion,
            验证人: tableRow.verifier,

          };
        });
        Promise.all(promises)
          .then((data) => {
            const ws = XLSX.utils.json_to_sheet(data);
            const wb = XLSX.utils.book_new();
            XLSX.utils.book_append_sheet(wb, ws, "问题列表");

            const wbout = XLSX.write(wb, { bookType: "xlsx", type: "array" });
            saveAs(
              new Blob([wbout], { type: "application/octet-stream" }),
              "问题月度数据总台账.xlsx"
            );

            // // 提交数据到Vuex Store
            // this.updateExportedData(data);


          })
          .finally(() => {
            loadingInstance.close();
          })
          .catch((error) => {
            console.error("导出失败:", error);
            loadingInstance.close();
          });
      },
    }
  }
</script>

<style scoped>
/* 表格的样式 */
.el-table {
  font-size: 16px; /* 放大文字字体 */
  border: 2px solid #ccc; /* 加粗边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 增加阴影效果 */
  border-radius: 10px; /* 可选：使表格边角变圆滑 */
}

/* 表格头部文字的样式 */
.el-table th {
  font-size: 18px; /* 放大表头的字体 */
  font-weight: bold; /* 加粗表头文字 */
  color: #333; /* 表头文字颜色 */
  border-bottom: 2px solid #ccc; /* 表头的下边框 */
}

/* 表格单元格文字样式 */
.el-table td {
  font-size: 16px; /* 调整单元格字体大小 */
  color: #333; /* 单元格文字颜色 */
}

/* 设置选中行的样式 */
.el-table .el-table__row--striped:hover {
  background-color: #f5f7fa; /* 表格行的悬停背景颜色 */
}

/* 调整表格分页器的样式 */
.el-pagination {
  margin-top: 10px;
  font-size: 14px; /* 分页器字体大小 */
}
.overdue-text {
  color: red;  /* 红色字体 */
}


</style>



