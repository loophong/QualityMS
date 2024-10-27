
<template>
  <div class="mod-config">
    <el-dialog title="整改情况" :visible.sync="dialogVisible" width="400px">
      <p>{{ fullDescription }}</p>
    </el-dialog>
    <el-dialog title="原因分析" :visible.sync="dialogVisible1" width="400px">
      <p>{{ fullCause }}</p>
    </el-dialog>
    <el-dialog title="验证整改情况" :visible.sync="dialogVisible2" width="400px">
      <p>{{ fullRetStates }}</p>
    </el-dialog>
    <el-tabs type="border-card">

      <!-- 问题添加 Tab Pane -->
      <el-tab-pane label="问题添加">
        <el-form v-if="showForm" :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
<!--          <el-form-item>-->
<!--            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
<!--          </el-form-item>-->
          <el-form-item>
<!--            <el-button @click="getDataList()">查询</el-button>-->
            <el-button v-if="isAuth('generator:issuetable:save')" type="primary"
                       @click="addOrUpdateHandle()">新增</el-button>
            <el-button v-if="isAuth('generator:issuetable:delete')" type="danger" @click="deleteHandle()"
                       :disabled="dataListSelections.length <= 0">批量删除</el-button>
          </el-form-item>
          <el-form-item>
            <el-upload class="upload-demo" :before-upload="beforeUpload" :show-file-list="false"
                       :on-success="handleUploadSuccess" :on-error="handleUploadError">
              <el-button size="small" type="primary">点击上传 Excel</el-button>
            </el-upload>
          </el-form-item>
        </el-form>

        <el-table v-if="showForm" :data="dataList" border v-loading="dataListLoading"
                  @selection-change="selectionChangeHandle" style="width: 100%;">
          <el-table-column type="selection" header-align="center" align="center" width="50">
          </el-table-column>
          <el-table-column prop="issueNumber" header-align="center" align="center" label="问题编号">
          </el-table-column>
          <el-table-column prop="inspectionDepartment" header-align="center" align="center" label="检查科室">
          </el-table-column>
          <el-table-column prop="inspectionDate" header-align="center" align="center" label="检查日期">
          </el-table-column>
          <el-table-column prop="issueCategoryId" header-align="center" align="center" label="问题类别">
          </el-table-column>
          <el-table-column prop="vehicleTypeId" header-align="center" align="center" label="车型">
          </el-table-column>
          <el-table-column prop="vehicleNumberId" header-align="center" align="center" label="车号">
          </el-table-column>
          <el-table-column prop="issueDescription" header-align="center" align="center" label="问题描述">
          </el-table-column>
          <el-table-column prop="issuePhoto" header-align="center" align="center" label="问题照片">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleFileAction(scope.row.issuePhoto)">预览</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="rectificationRequirement" header-align="center" align="center" label="整改要求">
          </el-table-column>
          <el-table-column prop="requiredCompletionTime" header-align="center" align="center" label="要求完成时间">
          </el-table-column>
          <el-table-column prop="responsibleDepartment" header-align="center" align="center" label="责任科室">
          </el-table-column>
          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="deleteHandle(scope.row.issueId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
                       :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
                       layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </el-tab-pane>

      <!-- 整改记录 Tab Pane -->
      <el-tab-pane label="整改记录">
        <div class="mod-config">
          <el-form v-if="showForm" :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
<!--            <el-form-item>-->
<!--              <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
<!--            </el-form-item>-->
            <el-form-item>
<!--              <el-button @click="getDataList()">查询</el-button>-->
              <!--        <el-button v-if="isAuth('generator:issuetable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
              <el-button v-if="isAuth('generator:issuetable:delete')" type="danger" @click="deleteHandle()"
                         :disabled="dataListSelections.length <= 0">批量删除</el-button>
            </el-form-item>
          </el-form>
          <el-table v-if="showForm" :data="dataList" border v-loading="dataListLoading"
                    @selection-change="selectionChangeHandle" style="width: 100%;">
            <el-table-column type="selection" header-align="center" align="center" width="50">
            </el-table-column>
            <el-table-column prop="issueNumber" header-align="center" align="center" label="问题编号">
            </el-table-column>
            <el-table-column prop="inspectionDepartment" header-align="center" align="center" label="检查科室">
            </el-table-column>
            <el-table-column prop="inspectionDate" header-align="center" align="center" label="检查日期">
            </el-table-column>
            <el-table-column prop="issueCategoryId" header-align="center" align="center" label="问题类别">
            </el-table-column>
            <el-table-column prop="vehicleTypeId" header-align="center" align="center" label="车型">
            </el-table-column>
            <el-table-column prop="vehicleNumberId" header-align="center" align="center" label="车号">
            </el-table-column>
            <el-table-column prop="issueDescription" header-align="center" align="center" label="问题描述">
            </el-table-column>

            <el-table-column prop="issuePhoto" header-align="center" align="center" label="问题照片">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="handleFileAction(scope.row.issuePhoto)">预览</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="rectificationRequirement" header-align="center" align="center" label="整改要求">
            </el-table-column>
            <el-table-column prop="requiredCompletionTime" header-align="center" align="center" label="要求完成时间">
            </el-table-column>
            <el-table-column prop="responsibleDepartment" header-align="center" align="center" label="责任科室">
            </el-table-column>
            <!--            <el-table-column-->
            <!--              prop="causeAnalysis"-->
            <!--              header-align="center"-->
            <!--              align="center"-->
            <!--              label="原因分析">-->
            <!--            </el-table-column>-->
            <el-table-column prop="causeAnalysis" header-align="center" align="center" label="原因分析">
              <template slot-scope="scope">
                <div
                  style="max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; cursor: pointer;"
                  @click="showFullCauseAnalysis(scope.row.causeAnalysis)">
                  <span v-if="scope.row.causeAnalysis.length > 8">
                    {{ scope.row.causeAnalysis.slice(0, 8) }}<strong>...</strong> <!-- 显示前八个字符，加粗省略号 -->
                  </span>
                  <span v-else>
                    {{ scope.row.causeAnalysis }} <!-- 显示完整描述 -->
                  </span>
                </div>
              </template>
            </el-table-column>
            <!--            <el-table-column-->
            <!--              prop="rectificationStatus"-->
            <!--              header-align="center"-->
            <!--              align="center"-->
            <!--              label="整改情况">-->
            <!--            </el-table-column>-->
            <el-table-column prop="rectificationStatus" header-align="center" align="center" label="整改情况">
              <template slot-scope="scope">
                <div
                  style="max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; cursor: pointer;"
                  @click="showFullDescription(scope.row.rectificationStatus)">
                  {{ truncateDescription(scope.row.rectificationStatus) }}
                  <span v-if="scope.row.rectificationStatus.length > 20">
                    <strong>...</strong> <!-- 将省略号加粗 -->
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="actualCompletionTime" header-align="center" align="center" label="实际完成时间">
            </el-table-column>
            <el-table-column prop="rectificationPhotoDeliverable" header-align="center" align="center" label="整改照片交付物">
              <template slot-scope="scope">
                <el-button type="text" size="small"
                           @click="handleFileAction(scope.row.rectificationPhotoDeliverable)">预览</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="rectificationResponsiblePerson" header-align="center" align="center" label="整改责任人">
            </el-table-column>
            <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
              <template slot-scope="scope">
                <el-button type="text" size="small"
                           @click="handleRectificationRecords(scope.row.issueNumber, scope.row.issueId)">整改记录</el-button>
                <!--          <el-button type="text" size="small" @click="deleteHandle(scope.row.issueId)">删除</el-button>-->
                <el-button type="text" size="small"
                           @click="assetOrUpdateHandle(scope.row.issueId, scope.row.issueNumber)">任务发起</el-button>
                <el-button type="text" size="small"
                           @click="openflow(scope.row.issueId, scope.row.issueNumber)">任务流程</el-button>
                <el-button type="text" size="small"
                           @click="openNewPage(scope.row.issueId, scope.row.issueNumber)">任务列表</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
                         :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
                         layout="total, sizes, prev, pager, next, jumper">
          </el-pagination>
        </div>
      </el-tab-pane>

      <!-- 问题验证 Tab Pane -->
      <el-tab-pane label="问题验证">
        <div class="mod-config">
          <el-form v-if="showForm1" :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
<!--            <el-form-item>-->
<!--              <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
<!--            </el-form-item>-->
            <el-form-item>
<!--              <el-button @click="getDataList()">查询</el-button>-->
              <!--        <el-button v-if="isAuth('generator:issuetable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
              <el-button v-if="isAuth('generator:issuetable:delete')" type="danger" @click="deleteHandle()"
                         :disabled="dataListSelections.length <= 0">批量删除</el-button>
            </el-form-item>
          </el-form>
          <el-table v-if="showForm" :data="dataList" border v-loading="dataListLoading"
                    @selection-change="selectionChangeHandle" style="width: 100%;">
            <el-table-column type="selection" header-align="center" align="center" width="50">
            </el-table-column>
            <!--      <el-table-column-->
            <!--        prop="issueId"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="">-->
            <!--      </el-table-column>-->
            <!--      <el-table-column-->
            <!--        prop="serialNumber"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="序号">-->
            <!--      </el-table-column>-->
            <el-table-column prop="issueNumber" header-align="center" align="center" label="问题编号">
            </el-table-column>
            <el-table-column prop="inspectionDepartment" header-align="center" align="center" label="检查科室">
            </el-table-column>
            <el-table-column prop="inspectionDate" header-align="center" align="center" label="检查日期">
            </el-table-column>
            <el-table-column prop="issueCategoryId" header-align="center" align="center" label="问题类别">
            </el-table-column>
            <el-table-column prop="vehicleTypeId" header-align="center" align="center" label="车型">
            </el-table-column>
            <el-table-column prop="vehicleNumberId" header-align="center" align="center" label="车号">
            </el-table-column>
            <el-table-column prop="issueDescription" header-align="center" align="center" label="问题描述">
            </el-table-column>

            <el-table-column prop="issuePhoto" header-align="center" align="center" label="问题照片">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="handleFileAction(scope.row.issuePhoto)">预览</el-button>
              </template>
            </el-table-column>
            <!--      <el-table-column-->
            <!--        prop="issuePhoto"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="问题照片">-->
            <!--      </el-table-column>-->
            <el-table-column prop="rectificationRequirement" header-align="center" align="center" label="整改要求">
            </el-table-column>
            <el-table-column prop="requiredCompletionTime" header-align="center" align="center" label="要求完成时间">
            </el-table-column>
            <el-table-column prop="responsibleDepartment" header-align="center" align="center" label="责任科室">
            </el-table-column>
            <!--            <el-table-column-->
            <!--              prop="causeAnalysis"-->
            <!--              header-align="center"-->
            <!--              align="center"-->
            <!--              label="原因分析">-->
            <!--            </el-table-column>-->
            <el-table-column prop="causeAnalysis" header-align="center" align="center" label="原因分析">
              <template slot-scope="scope">
                <div
                  style="max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; cursor: pointer;"
                  @click="showFullCauseAnalysis(scope.row.causeAnalysis)">
                  <span v-if="scope.row.causeAnalysis.length > 8">
                    {{ scope.row.causeAnalysis.slice(0, 8) }}<strong>...</strong> <!-- 显示前八个字符，加粗省略号 -->
                  </span>
                  <span v-else>
                    {{ scope.row.causeAnalysis }} <!-- 显示完整描述 -->
                  </span>
                </div>
              </template>
            </el-table-column>
            <!--            <el-table-column-->
            <!--              prop="rectificationStatus"-->
            <!--              header-align="center"-->
            <!--              align="center"-->
            <!--              label="整改情况">-->
            <!--            </el-table-column>-->
            <el-table-column prop="rectificationStatus" header-align="center" align="center" label="整改情况">
              <template slot-scope="scope">
                <div
                  style="max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; cursor: pointer;"
                  @click="showFullDescription(scope.row.rectificationStatus)">
                  {{ truncateDescription(scope.row.rectificationStatus) }}
                  <span v-if="scope.row.rectificationStatus.length > 20">
                    <strong>...</strong> <!-- 将省略号加粗 -->
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="actualCompletionTime" header-align="center" align="center" label="实际完成时间">
            </el-table-column>
            <el-table-column prop="rectificationPhotoDeliverable" header-align="center" align="center" label="整改照片交付物">
              <template slot-scope="scope">
                <el-button type="text" size="small"
                           @click="handleFileAction(scope.row.rectificationPhotoDeliverable)">预览</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="rectificationResponsiblePerson" header-align="center" align="center" label="整改责任人">
            </el-table-column>
            <!--      <el-table-column-->
            <!--        prop="requiredSecondRectificationTime"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="要求二次整改时间">-->
            <!--      </el-table-column>-->
            <!--      <el-table-column-->
            <!--        prop="remark"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="备注">-->
            <!--      </el-table-column>-->
            <!--      <el-table-column-->
            <!--        prop="creator"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="创建人">-->
            <!--      </el-table-column>-->
            <!--      <el-table-column-->
            <!--        prop="creationTime"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="创建时间">-->
            <!--      </el-table-column>-->
            <!--      <el-table-column-->
            <!--        prop="lastModifier"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="最后修改人">-->
            <!--      </el-table-column>-->
            <!--      <el-table-column-->
            <!--        prop="lastModificationTime"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="最后修改时间">-->
            <!--      </el-table-column>-->
            <el-table-column prop="associatedIssueAddition" header-align="center" align="center" label="关联问题整改记录">
            </el-table-column>
            <!--      <el-table-column-->
            <!--        prop="associatedIssueAddition"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="关联问题添加">-->
            <!--      </el-table-column>-->
            <!--      <el-table-column-->
            <!--        prop="creationDuration"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="创建时长">-->
            <!--      </el-table-column>-->
            <!--            <el-table-column-->
            <!--              prop="rectificationVerificationStatus"-->
            <!--              header-align="center"-->
            <!--              align="center"-->
            <!--              label="整改验证情况">-->
            <!--            </el-table-column>-->
            <el-table-column prop="rectificationVerificationStatus" header-align="center" align="center" label="整改验证情况">
              <template slot-scope="scope">
                <div
                  style="max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; cursor: pointer;"
                  @click="showFullRectificationStatus(scope.row.rectificationVerificationStatus)">
                  <span v-if="scope.row.rectificationVerificationStatus.length > 50">
                    {{ scope.row.rectificationVerificationStatus.slice(0, 50) }}<strong>...</strong>
                    <!-- 显示前八个字符，加粗省略号 -->
                  </span>
                  <span v-else>
                    {{ scope.row.rectificationVerificationStatus }} <!-- 显示完整描述 -->
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="verificationConclusion" header-align="center" align="center" label="验证结论">
              <template slot-scope="scope">
                <div>
                  <span v-for="(state, index) in getStates(scope.row.verificationConclusion)" :key="index">
                    <el-tag v-if="state === '未完成'" type="danger" disable-transitions>{{ state }}</el-tag>
                    <el-tag v-else-if="state === '已完成'" type="success" disable-transitions>{{ state }}</el-tag>
                    <el-tag v-else-if="state === '持续'" type="info" disable-transitions>{{ state }}</el-tag>
                    <el-tag v-else-if="state === '结项'" type="warning" disable-transitions>{{ state }}</el-tag>
                    <el-tag v-else-if="state === '未完成，持续'" type="danger" disable-transitions>{{ state }}</el-tag>
                    <el-tag v-else-if="state === '持续，未完成'" type="info" disable-transitions>{{ state }}</el-tag>
                    <el-tag v-else>{{ state }}</el-tag> <!-- 处理未定义的状态 -->
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="verifier" header-align="center" align="center" label="验证人">
            </el-table-column>
            <!--      <el-table-column-->
            <!--        prop="formula"-->
            <!--        header-align="center"-->
            <!--        align="center"-->
            <!--        label="公式">-->
            <!--      </el-table-column>-->
            <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="checkStateAndHandle(scope.row)">验证记录</el-button>
                <el-button type="text" size="small" @click="deleteHandle(scope.row.issueId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
                         :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
                         layout="total, sizes, prev, pager, next, jumper">
          </el-pagination>
        </div>
      </el-tab-pane>

    </el-tabs>

    <!-- 弹窗, 新增/修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <add-or-updateT v-if="addOrUpdateVisibleT" ref="addOrUpdateT" @refreshDataList="getDataList"></add-or-updateT>
    <add-or-updateV v-if="addOrUpdateVisibleV" ref="addOrUpdateV" @refreshDataList="getDataList"></add-or-updateV>

    <!-- 图片预览对话框 -->
    <el-dialog :visible.sync="imagePreviewVisible" :title="imagePreviewTitle">
      <img :src="imagePreviewUrl" style="width: 100%;">
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './issuetable-add-or-update.vue'
import AddOrUpdateT from '../issueRectification/issuetable-add-or-update.vue'
import AddOrUpdateV from '../RectVerification/issuetable-add-or-update.vue'


//如果新打开的页面是另外一个项目(前提是另一个项目也是自己的)的话可以在请求拦截request.interceptors.js中获取

//如果新打开的页面是另外一个项目(不是自己的项目)的话我们只负责Cookie.set存，取得话需要根据实际情况考虑


// import {isAuth} from "../../../utils";
export default {
  data() {
    return {
      showForm: true, // 控制表单显示的变量
      showForm1: true, // 控制表单显示的变量
      dataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      addOrUpdateVisibleT: false,
      addOrUpdateVisibleV: false,
      assertOrUpdateVisible: false,
      imagePreviewVisible: false, // 图片预览对话框显示控制
      imagePreviewUrl: '', // 图片预览URL
      imagePreviewTitle: '', // 图片预览标题
      dialogVisible: false, // 控制对话框显示
      fullDescription: '',   // 用于存储完整描述
      dialogVisible1: false, // 控制对话框显示
      fullCause: '',    // 用于存储完整描述
      dialogVisible2: false,
      fullRetStates: '',
    }
  },
  components: {
    AddOrUpdate,
    AddOrUpdateT,
    AddOrUpdateV
  },
  activated() {
    this.getDataList()
  },
  methods: {
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
    checkStateAndHandle(row) {
      this.addOrUpdateHandlev(row.issueId)
    },
    addOrUpdateHandlev(id) {
      this.addOrUpdateVisibleV = true
      this.$nextTick(() => {
        this.$refs.addOrUpdateV.init(id)
      })
    },
    handleRectificationRecords(issueNumber, issueId) {
      this.$http({
        url: this.$http.adornUrl('/generator/issuemasktable/records'),
        method: 'post',
        params: this.$http.adornParams({ issueNumber: issueNumber })
      }).then(({ data }) => {
        console.log("返回数据：", data)
        if (data && data.msg === 'success') {
          console.log('整改得到的id为', issueId)
          // 操作成功后触发addOrUpdateHandle
          this.addOrUpdateHandleR(issueId)
        } else if (data && data.msg === 'error') {
          this.$message.error('任务未全部完成')
        } else {
          this.$message.error('操作失败')
        }
      }).catch(() => {
        this.$message.error('请求失败')
      })

    },
    // 在上传前的检查
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
      const isLt2M = file.size / 1024 / 1024 < 2; // 限制文件大小为2MB

      if (!isExcel) {
        this.$message.error('上传文件只能是 Excel 文件!');
        return false; // 返回 false，阻止上传
      }

      if (!isLt2M) {
        this.$message.error('上传文件大小不能超过 2MB!');
        return false; // 返回 false，阻止上传
      }

      // 使用 FormData 创建上传文件数据
      const formData = new FormData();
      formData.append('file', file);

      // 开始上传请求
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/uploadExcel'),
        method: 'post',
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data' // 重要：设置请求头
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success('文件上传成功！');
          this.getDataList(); // 上传成功后可以重新获取数据
        } else {
          this.$message.error('文件上传失败：' + data.msg);
        }
      }).catch(error => {
        this.$message.error('文件上传错误：' + error.message);
      });

      return false; // 返回 false，阻止 el-upload 的自动上传
    }
    ,


    // 上传成功的处理
    handleUploadSuccess(response, file) {
      if (response && response.code === 0) {
        this.$message.success('文件上传成功！');
        this.getDataList(); // 上传成功后可以重新获取数据
      } else {
        this.$message.error('文件上传失败：' + response.msg);
      }
    },
    handleUploadError(error) {
      console.error('上传错误:', error);  // 打印错误对象
      this.$message.error('文件上传失败：' + (error.response ? error.response.data.message : error.message)); // 提供更详细的错误信息
    },

    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/generator/issuetable/listcreator'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        })
      }).then(({ data }) => {
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
      this.getDataList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 新增 / 修改整改
    addOrUpdateHandleR(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.initR(id)
      })
    },
    // 任务发起
    assetOrUpdateHandle(id, issueNumber) {
      this.addOrUpdateVisibleT = true
      this.$nextTick(() => {
        this.$refs.addOrUpdateT.init1(id, issueNumber)
      })
    },
    //图片预览
    // previewImage(imageUrl) {
    //   // 获取当前的 token，假设它存储在 localStorage 中
    //   const token = this.$cookie.get('token');
    //   if (token) {
    //     console.log('Token found:', token);
    //   } else {
    //     console.error('Token not found!');
    //   }
    //
    //   // 将 token 作为参数添加到 URL
    //   const imageUrlWithToken = `${imageUrl}?token=${token}`;
    //
    //   console.log("cur imageUrl====>", imageUrlWithToken);
    //
    //   // 打开包含 token 的图片地址
    //   window.open(imageUrlWithToken);
    //
    //   console.log("图片地址：", imageUrlWithToken);
    // },
    // previewImage(imageUrl) {
    //   console.log("cur imageUrl====>", imageUrl);
    //
    //   // Fetch the image as a blob
    //   fetch(imageUrl)
    //     .then(response => {
    //       if (!response.ok) {
    //         throw new Error('网络错误，无法获取图片');
    //       }
    //       return response.blob();
    //     })
    //     .then(blob => {
    //       const blobUrl = URL.createObjectURL(blob);
    //       window.open(blobUrl); // 在新窗口打开 Blob URL
    //     })
    //     .catch(error => {
    //       console.error('获取图片失败:', error);
    //     });
    // },

    // handlePreviewCur(file) {
    //   console.log(file)
    //   console.log(file);
    //   window.open(file);
    // },
    openflow(issueId, issueNumber) {
      this.$router.push({
        name: 'issue-issueflow',
        params: {
          issueId: issueId,
          issueNumber: issueNumber
        }
      })
    },
    openNewPage(issueId, issueNumber) {
      this.$router.push({
        name: 'issue-issuemask',
        params: {
          issueId: issueId,
          issueNumber: issueNumber
        }
      })
    },

    // 删除
    deleteHandle(id) {
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
        }).then(({ data }) => {
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
      if (!description) return '';
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
    }
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
</style>
