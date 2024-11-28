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
<!--        <el-form-item label="问题类别" prop="issueCategoryId">-->
<!--          <el-select v-model="dataForm.issueCategoryId" filterable placeholder="请选择问题类别">-->
<!--            <el-option-->
<!--              v-for="item in issueCategoryOptions"-->
<!--              :key="item.value"-->
<!--              :label="item.label"-->
<!--              :value="item.value">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <!-- 问题等级选择器 -->
        <el-form-item label="问题等级" prop="issueGrade">
          <el-select v-model="selectedGrade" @change="filterCategories" filterable placeholder="请选择问题等级">
            <el-option
              v-for="grade in issueGrades"
              :key="grade"
              :label="grade"
              :value="grade">
            </el-option>
          </el-select>
        </el-form-item>

        <!-- 问题类别选择器 -->
        <el-form-item label="问题类别" prop="issueCategoryId">
          <el-select v-model="dataForm.issueCategoryId" filterable placeholder="请选择问题类别">
            <el-option
              v-for="item in filteredIssueCategoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="系统分类" prop="systematicClassification">
          <el-select
            v-model="selectedSystematic"
            @change="filterFirstFaultyParts"
            filterable
            placeholder="请选择系统分类">
            <el-option
              v-for="option in filteredSystematicOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="故障件一级" prop="firstFaultyParts">
          <el-select
            v-model="selectedFirstFaultyParts"
            @change="filterSecondFaultyParts"
            filterable
            placeholder="请选择故障件一级">
            <el-option
              v-for="option in filteredFirstFaultyPartsOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="故障件二级" prop="secondFaultyParts">
          <el-select
            v-model="selectedSecondFaultyParts"
            @change="filterFaultType"
            filterable
            placeholder="请选择故障件二级">
            <el-option
              v-for="option in filteredSecondFaultyPartsOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="故障类别" prop="faultType">
          <el-select
            v-model="selectedFaultType"
            @change="filterFaultModel"
            filterable
            placeholder="请选择故障类别">
            <el-option
              v-for="option in filteredFaultTypeOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="故障模式" prop="faultModel">
          <el-select
            v-model="selectedFaultModel"
            filterable
            placeholder="请选择故障模式">
            <el-option
              v-for="option in filteredFaultModelOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="80px">
          <div v-for="(vehicle, index) in dataForm.vehicles" :key="vehicle.key">
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
        <el-form-item label="初步分析" prop="peliminaryAnalysis">
          <el-select v-model="dataForm.peliminaryAnalysis" filterable placeholder="请选择初步分析">
            <el-option
              v-for="department in issuepeliminaryAnalysis"
              :key="department.value"
              :label="department.label"
              :value="department.value">
            </el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="初步分析" prop="peliminaryAnalysis">-->
<!--          <el-input v-model="dataForm.peliminaryAnalysis" placeholder="初步分析"></el-input>-->
<!--        </el-form-item>-->
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
          <el-button type="primary">上传文件</el-button>
        </el-upload>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取消</el-button>
        <el-button type="primary" @click="dataFormSubmitR()">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :title="'验证指定'"
      :close-on-click-modal="false"
      :visible.sync="visibleVE">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitR()" label-width="150px">
        <el-form-item label="验证人" prop="verifier">
          <el-select v-model="dataForm.verifier" filterable placeholder="请选择验证人">
            <el-option-group v-for="group in options" :key="group.label" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label"
                         :value="item.label">
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="验证截止日期" prop="verificationDeadline">
          <el-date-picker
            v-model="dataForm.verificationDeadline"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
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
      title="添加图片"
      :visible.sync="addImageDialogVisible"
      width="30%">
      <el-form ref="dataForm" :model="dataForm">
        <el-form-item label="上传图片">
          <el-upload
            :before-upload="beforeUpload"
            :on-change="handleFileChange"
            :show-file-list="false"
            :auto-upload="false"
            accept="image/*"> <!-- 限制上传类型为图片 -->
            <el-button size="small" type="primary">选择文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addPhotoSubmit ()">确认</el-button>
        <el-button @click="addImageDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :title="'问题详情'"
      :close-on-click-modal="false"
      :visible.sync="visibleAs">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
<!--        <el-form-item label="序号" prop="serialNumber">-->
<!--          <el-input v-model="dataForm.serialNumber" placeholder="序号"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="问题编号" prop="issueNumber">
          <el-input v-model="dataForm.issueNumber" placeholder="问题编号"></el-input>
        </el-form-item>
        <el-form-item label="检查科室" prop="inspectionDepartment">
          <el-input v-model="dataForm.inspectionDepartment" placeholder="检查科室"></el-input>
        </el-form-item>
        <el-form-item label="检查日期" prop="inspectionDate">
          <el-input v-model="dataForm.inspectionDate" placeholder="检查日期"></el-input>
        </el-form-item>
        <el-form-item label="问题类别" prop="issueCategoryId">
          <el-input v-model="dataForm.issueCategoryId" placeholder="问题类别ID"></el-input>
        </el-form-item>
        <el-form-item label="车型" prop="vehicleTypeId">
          <el-input v-model="dataForm.vehicleTypeId" placeholder="车型ID"></el-input>
        </el-form-item>
        <el-form-item label="车号" prop="vehicleNumberId">
          <el-input v-model="dataForm.vehicleNumberId" placeholder="车号ID"></el-input>
        </el-form-item>
        <el-form-item label="问题描述" prop="issueDescription">
          <el-input v-model="dataForm.issueDescription" placeholder="问题描述"></el-input>
        </el-form-item>
<!--        <el-form-item label="问题照片" prop="issuePhoto">-->
<!--          <el-input v-model="dataForm.issuePhoto" placeholder="问题照片"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="整改要求" prop="rectificationRequirement">
          <el-input v-model="dataForm.rectificationRequirement" placeholder="整改要求"></el-input>
        </el-form-item>
        <el-form-item label="要求完成时间" prop="requiredCompletionTime">
          <el-input v-model="dataForm.requiredCompletionTime" placeholder="要求完成时间"></el-input>
        </el-form-item>
        <el-form-item label="责任科室" prop="responsibleDepartment">
          <el-input v-model="dataForm.responsibleDepartment" placeholder="责任科室"></el-input>
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
<!--        <el-form-item label="要求二次整改时间" prop="requiredSecondRectificationTime">-->
<!--          <el-input v-model="dataForm.requiredSecondRectificationTime" placeholder="要求二次整改时间"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="备注" prop="remark">-->
<!--          <el-input v-model="dataForm.remark" placeholder="备注"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="创建人" prop="creator">
          <el-input v-model="dataForm.creator" placeholder="创建人"></el-input>
        </el-form-item>
        <el-form-item label="创建时间" prop="creationTime">
          <el-input v-model="dataForm.creationTime" placeholder="创建时间"></el-input>
        </el-form-item>
<!--        <el-form-item label="最后修改人" prop="lastModifier">-->
<!--          <el-input v-model="dataForm.lastModifier" placeholder="最后修改人"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="最后修改时间" prop="lastModificationTime">-->
<!--          <el-input v-model="dataForm.lastModificationTime" placeholder="最后修改时间"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="关联问题" prop="associatedRectificationRecords">
          <el-input v-model="dataForm.associatedRectificationRecords" placeholder="关联问题整改记录"></el-input>
        </el-form-item>
<!--        <el-form-item label="关联问题添加" prop="associatedIssueAddition">-->
<!--          <el-input v-model="dataForm.associatedIssueAddition" placeholder="关联问题添加"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="创建时长" prop="creationDuration">
          <el-input v-model="dataForm.creationDuration" placeholder="创建时长"></el-input>
        </el-form-item>
        <el-form-item label="原因分析" prop="causeAnalysis">
          <el-input v-model="dataForm.causeAnalysis" placeholder="原因分析"></el-input>
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
        <el-form-item label="公式" prop="formula">
          <el-input v-model="dataForm.formula" placeholder="公式"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="visibleAs = false">关闭</el-button>
<!--      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>-->
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
      // 全部选项数据
      allOptions: [],
      // 各级筛选后的选项
      filteredSystematicOptions: [],
      filteredFirstFaultyPartsOptions: [],
      filteredSecondFaultyPartsOptions: [],
      filteredFaultTypeOptions: [],
      filteredFaultModelOptions: [],
      // 选中值
      selectedSystematic: null,
      selectedFirstFaultyParts: null,
      selectedSecondFaultyParts: null,
      selectedFaultType: null,
      selectedFaultModel: null,
      issueGrades: [],  // 存储所有唯一的问题等级
      issueCategoryOptions: [],  // 原始数据
      issuepeliminaryAnalysis: [],  // 存储所有唯一的初步分析
      filteredIssueCategoryOptions: [],  // 过滤后的问题类别选项
      selectedGrade: null,  // 用户选择的问题等级
      addImageDialogVisible: false,
      selectedResponsiblePersons: [], // 存放选中的责任人ID
      bloburl: '',
      imageurl: '',
      visible: false,
      visible1: false,
      visibleR: false,
      visibleT: false,
      visibleVE: false,
      visibleAs: false,
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
        peliminaryAnalysis: '',
        verificationDeadline: '',
        overDue: '',
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
      // issueCategoryOptions: [],
      departments: [],
      selectedIssue: '',
      issueOptions: [], // 所有问题编号选项
      taskList: [], // 任务列表
      departmentOptions: [],

      dataRule: {
      },
      options: ''
    }
  },
  created () {
    this.fetchIssueCategories()
    this.fetchVehicleTypes()
    this.fetchIssueOptions() // 获取所有问题编号选项
    this.fetchDepartments()
    this.fetchAnalysis()
    this.fetchFaultOptions()
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
    // 通用数组去重方法
    removeDuplicates(array, key) {
      const seen = new Set();
      return array.filter(item => {
        const val = key ? item[key] : item; // 如果有 key，按 key 去重，否则直接按值去重
        if (seen.has(val)) {
          return false;
        }
        seen.add(val);
        return true;
      });
    },

    // 批量去重所有数组
    deduplicateAll() {
      this.filteredIssueCategoryOptions = this.removeDuplicates(this.filteredIssueCategoryOptions, 'value');
      this.filteredSystematicOptions = this.removeDuplicates(this.filteredSystematicOptions, 'value');
      this.filteredFirstFaultyPartsOptions = this.removeDuplicates(this.filteredFirstFaultyPartsOptions, 'value');
      this.filteredSecondFaultyPartsOptions = this.removeDuplicates(this.filteredSecondFaultyPartsOptions, 'value');
      this.filteredFaultTypeOptions = this.removeDuplicates(this.filteredFaultTypeOptions, 'value');
      this.filteredFaultModelOptions = this.removeDuplicates(this.filteredFaultModelOptions, 'value');
    },
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
    // imageload() {
    //   this.url = this.$http.adornUrl(`/sys/oss/upload?token=${this.$cookie.get('token')}`);
    //   this.visibleUpload = true; // 打开上传对话框
    // },
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
        console.log('Successfully fetched new issue number:', response.data.useID);

        // 返回问题编号，确保 random 是从后端返回的字符串
        const random = response.data.useID;
        return `ZL-IS-${year}${month}${day}-${random}`;
      } catch (error) {
        console.error('Failed to fetch new issue number:', error);
        throw new Error('Failed to generate serial number');
      }
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
      this.visibleVE = false // 关闭对话框或重置其他状态
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
    // fetchIssueCategories () {
    //   this.$http({
    //     url: this.$http.adornUrl('/generator/issuetypetable/issuestype'),
    //     method: 'get',
    //     params: this.$http.adornParams()
    //   }).then(({data}) => {
    //     if (data && data.code === 0) {
    //       console.log('Successfully fetched issue categories:', data.category)
    //       this.issueCategoryOptions = data.category.map(category => ({
    //         value: category.concreteIssueCategory,
    //         label: category.concreteIssueCategory
    //       }))
    //     } else {
    //       console.error('Failed to fetch issue categories:', data.msg)
    //     }
    //   }).catch(error => {
    //     console.error('There was an error fetching the issue categories!', error)
    //   })
    // },
    fetchAnalysis () {
      this.$http({
        url: this.$http.adornUrl('/generator/peliminaryanalysistable/fetchAnalysis'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        if (data && data.code === 0) {
          // console.log('Successfully fetched departmentOptions:', data.departmentTableEntities)
          this.issuepeliminaryAnalysis = data.peliminaryAnalysisTableEntities.map(peliminaryAnalysisTableEntities => ({
            value: peliminaryAnalysisTableEntities.peliminaryAnalysis,
            label: peliminaryAnalysisTableEntities.peliminaryAnalysis
          }))
        } else {

          console.error('Failed to fetch issue categories:', data.msg)
        }
      }).catch(error => {
        console.error('There was an error fetching the issue categories!', error)
      })
      // console.log('Successfully fetched departmentOptions:', this.departmentOptions)

    },
    fetchFaultOptions() {
      this.$http({
        url: this.$http.adornUrl('/generator/issuefaulttable/options'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // 全量数据
          this.allOptions = data.options.map(option => ({
            systematicClassification: option.systematicClassification,
            firstFaultyParts: option.firstFaultyParts,
            secondFaultyParts: option.secondFaultyParts,
            faultType: option.faultType,
            faultModel: option.faultModel
          }));

          // 初始化系统分类选项
          this.filteredSystematicOptions = [...new Set(this.allOptions.map(item => ({
            value: item.systematicClassification,
            label: item.systematicClassification
          })))];
          // 去重
          this.deduplicateAll();
        } else {
          console.error('Failed to fetch fault options:', data.msg);
        }
      }).catch(error => {
        console.error('There was an error fetching the fault options!', error);
      });
    },
    // 筛选一级故障件
    filterFirstFaultyParts() {
      const filtered = this.allOptions.filter(
        item => item.systematicClassification === this.selectedSystematic
      );
      this.filteredFirstFaultyPartsOptions = [...new Set(filtered.map(item => ({
        value: item.firstFaultyParts,
        label: item.firstFaultyParts
      })))];

      this.selectedFirstFaultyParts = null;
      this.filterSecondFaultyParts();
      this.deduplicateAll();
    },

    // 筛选二级故障件
    filterSecondFaultyParts() {
      const filtered = this.allOptions.filter(
        item =>
          item.systematicClassification === this.selectedSystematic &&
          item.firstFaultyParts === this.selectedFirstFaultyParts
      );
      this.filteredSecondFaultyPartsOptions = [...new Set(filtered.map(item => ({
        value: item.secondFaultyParts,
        label: item.secondFaultyParts
      })))];

      this.selectedSecondFaultyParts = null;
      this.filterFaultType();
      this.deduplicateAll();
    },

    // 筛选故障类别
    filterFaultType() {
      const filtered = this.allOptions.filter(
        item =>
          item.systematicClassification === this.selectedSystematic &&
          item.firstFaultyParts === this.selectedFirstFaultyParts &&
          item.secondFaultyParts === this.selectedSecondFaultyParts
      );
      this.filteredFaultTypeOptions = [...new Set(filtered.map(item => ({
        value: item.faultType,
        label: item.faultType
      })))];

      this.selectedFaultType = null;
      this.filterFaultModel();
      this.deduplicateAll();
    },

    // 筛选故障模式
    filterFaultModel() {
      const filtered = this.allOptions.filter(
        item =>
          item.systematicClassification === this.selectedSystematic &&
          item.firstFaultyParts === this.selectedFirstFaultyParts &&
          item.secondFaultyParts === this.selectedSecondFaultyParts &&
          item.faultType === this.selectedFaultType
      );
      this.filteredFaultModelOptions = [...new Set(filtered.map(item => ({
        value: item.faultModel,
        label: item.faultModel
      })))];
      this.deduplicateAll();
      this.selectedFaultModel = null;
    },

    fetchIssueCategories() {
      this.$http({
        url: this.$http.adornUrl('/generator/issuetypetable/issuestype'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // console.log('Successfully fetched issue categories:', data.category);

          // 原始数据
          this.issueCategoryOptions = data.category.map(category => ({
            value: category.concreteIssueCategory,
            label: category.concreteIssueCategory,
            grade: category.issueGrade
          }));

          // 获取唯一的等级选项
          this.issueGrades = [...new Set(data.category.map(category => category.issueGrade))];
          // 去重
          this.deduplicateAll();
        } else {
          console.error('Failed to fetch issue categories:', data.msg);
        }
      }).catch(error => {
        console.error('There was an error fetching the issue categories!', error);
      });
    },

    // 根据选择的等级筛选问题类别
    filterCategories() {
      if (this.selectedGrade) {
        // 过滤出与所选等级匹配的问题类别
        this.filteredIssueCategoryOptions = this.issueCategoryOptions.filter(
          option => option.grade === this.selectedGrade
        );
      } else {
        // 如果未选择等级，则不显示任何类别
        this.filteredIssueCategoryOptions = [];
      }

      // 清空已选的问题类别
      this.dataForm.issueCategoryId = null;
    },

    fetchDepartments () {
      this.$http({
        url: this.$http.adornUrl('/generator/departmenttable/fetchDepartments'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log('Successfully fetched departmentOptions:', data.departmentTableEntities)
          this.departmentOptions = data.departmentTableEntities.map(departmentTableEntities => ({
            value: departmentTableEntities.departmentName,
            label: departmentTableEntities.departmentName
          }))
        } else {

          console.error('Failed to fetch issue categories:', data.msg)
        }
      }).catch(error => {
        console.error('There was an error fetching the issue categories!', error)
      })
      // console.log('Successfully fetched departmentOptions:', this.departmentOptions)

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
    checkDuplicateIssue() {
      return new Promise((resolve, reject) => {
        this.$http({
          url: this.$http.adornUrl('/generator/issuetable/checkDuplicateIssue'),
          method: 'post',
          data: this.$http.adornData({
            vehicleNumbers: this.dataForm.vehicleNumbers,
            issueCategoryId: this.dataForm.issueCategoryId,
          })
        }).then(({ data }) => {
          console.log('checkDuplicateIssue', data);
          if (data && data.code === 0) {
            if (data.isDuplicate) {
              this.$message.error('此车已发生过相同问题');
              reject(new Error('Duplicate issue found'));
            } else {
              resolve(); // No duplicates found
            }
          } else {
            this.$message.error(data.msg);
            reject(new Error('Duplicate check failed'));
          }
        }).catch(error => {
          this.$message.error('检查重复问题时出错，请稍后再试');
          console.error('请求失败：', error);
          reject(error);
        });
      });
    },
    // dataFormSubmit () {
    //   this.$refs['dataForm'].validate((valid) => {
    //     console.log(valid)
    //     if (valid) {
    //       this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId);
    //       this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber);
    //       console.log("this.dataForm.issueCategoryId", this.dataForm.issueCategoryId);
    //       console.log("this.dataForm.vehicleNumbers", this.dataForm.vehicleNumbers);
    //
    //
    //       // 调用检查重复问题的方法
    //       this.checkDuplicateIssue()
    //         .then(() => {
    //           // 重复检查通过，继续提交数据
    //           this.$http({
    //             url: this.$http.adornUrl(`/generator/issuetable/${!this.dataForm.issueId ? 'save' : 'update'}`),
    //             method: 'post',
    //             data: this.$http.adornData({
    //               'issueId': this.dataForm.issueId || undefined,
    //               'serialNumber': this.dataForm.serialNumber,
    //               'issueNumber': this.generateSerialNumber(),
    //               'inspectionDepartment': this.dataForm.inspectionDepartment,
    //               'inspectionDate': this.dataForm.inspectionDate,
    //               'issueCategoryId': this.dataForm.issueCategoryId,
    //               'vehicleTypeId': this.dataForm.vehicleTypeIds.join(','),
    //               'vehicleNumberId': this.dataForm.vehicleNumbers.join(','),
    //               'issueDescription': this.dataForm.issueDescription,
    //               'issuePhoto': this.dataForm.issuePhoto,
    //               'rectificationRequirement': this.dataForm.rectificationRequirement,
    //               'peliminaryAnalysis': this.dataForm.peliminaryAnalysis,
    //               'requiredCompletionTime': this.dataForm.requiredCompletionTime,
    //               'responsibleDepartment': this.dataForm.responsibleDepartment,
    //               'rectificationStatus': this.dataForm.rectificationStatus,
    //               'actualCompletionTime': this.dataForm.actualCompletionTime,
    //               'rectificationPhotoDeliverable': this.dataForm.rectificationPhotoDeliverable,
    //               'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
    //               'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
    //               'remark': this.dataForm.remark,
    //               'creator': this.dataForm.creator,
    //               'creationTime': this.dataForm.creationTime,
    //               'lastModifier': this.dataForm.lastModifier,
    //               'lastModificationTime': this.dataForm.lastModificationTime,
    //               'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
    //               'associatedIssueAddition': this.dataForm.associatedIssueIds.join(','),
    //               'creationDuration': this.dataForm.creationDuration,
    //               'causeAnalysis': this.dataForm.causeAnalysis,
    //               'rectificationVerificationStatus': this.dataForm.rectificationVerificationStatus,
    //               'verificationConclusion': this.dataForm.verificationConclusion,
    //               'verifier': this.dataForm.verifier,
    //               'level': this.dataForm.level,
    //               'state': this.dataForm.state,
    //               'formula': this.dataForm.formula
    //             })
    //           })
    //             .then(({ data }) => {
    //             if (data && data.code === 0) {
    //               // this.$message({
    //                 // message: '操作成功',
    //                 // type: 'success',
    //                 // duration: 1500,
    //                 // onClose: () => {
    //                   this.visible = false;
    //                   this.visibleR = false;
    //                   this.$emit('refreshDataList');
    //                   // 新增的请求，发送问题ID和问题类型到后端
    //                   this.$http({
    //                     url: this.$http.adornUrl('/generator/issuetable/checkReplicateIssue'),
    //                     method: 'post',
    //                     data: this.$http.adornData({
    //                       issueId: this.dataForm.issueId,
    //                       issueCategoryId: this.dataForm.issueCategoryId
    //                     })
    //                   }).then(({ data }) => {
    //                     console.log('checkreplicateIssue', data);
    //                     if (data && data.code === 0) {
    //                       this.$message.success("操作成功" + data.msg);
    //                     } else {
    //                       this.$message.error("操作失败: " + data.msg);
    //                     }
    //                   }).catch(error => {
    //                     console.error('请求失败：', error);
    //                   });
    //                 // }
    //               // })
    //               // ;
    //             } else {
    //               this.$message.error(data.msg);
    //             }
    //           }).catch(error => {
    //             console.error('请求失败：', error);
    //           });
    //         })
    //         .catch(error => {
    //           // 如果有重复数据，或者发生错误，终止后续操作
    //           console.error(error);
    //         });
    //     }
    //   });
    // },
    // dataFormSubmit () {
    //   this.$refs['dataForm'].validate((valid) => {
    //     console.log(valid);
    //     if (valid) {
    //       this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId);
    //       this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber);
    //       console.log("this.dataForm.issueCategoryId", this.dataForm.issueCategoryId);
    //       console.log("this.dataForm.vehicleNumbers", this.dataForm.vehicleNumbers);
    //
    //       // 调用检查重复问题的方法
    //       this.checkDuplicateIssue()
    //         .then(() => {
    //           // 调用 /generator/issuetable/checkReplicateIssue 请求
    //           return this.$http({
    //             url: this.$http.adornUrl('/generator/issuetable/checkReplicateIssue'),
    //             method: 'post',
    //             data: this.$http.adornData({
    //               issueId: this.dataForm.issueId,
    //               issueCategoryId: this.dataForm.issueCategoryId,
    //               systematicClassification: this.selectedSystematic,
    //               firstFaultyParts: this.selectedFirstFaultyParts,
    //               secondFaultyParts: this.selectedSecondFaultyParts,
    //               faultType: this.selectedFaultType,
    //               faultModel: this.selectedFaultModel
    //             })
    //           });
    //         })
    //         .then(({ data }) => {
    //           // console.log('checkReplicateIssue', data);
    //           // console.log('issueNumber', this.generateSerialNumber());
    //
    //           if (data && data.code === 0) {
    //             // 重复检查通过，继续提交数据
    //             return this.$http({
    //               url: this.$http.adornUrl(`/generator/issuetable/${!this.dataForm.issueId ? 'save' : 'update'}`),
    //               method: 'post',
    //               data: this.$http.adornData({
    //                 'issueId': this.dataForm.issueId || undefined,
    //                 'serialNumber': this.dataForm.serialNumber,
    //                 'issueNumber': this.generateSerialNumber(),
    //                 'inspectionDepartment': this.dataForm.inspectionDepartment,
    //                 'inspectionDate': this.dataForm.inspectionDate,
    //                 'issueCategoryId': this.dataForm.issueCategoryId,
    //                 'vehicleTypeId': this.dataForm.vehicleTypeIds.join(','),
    //                 'vehicleNumberId': this.dataForm.vehicleNumbers.join(','),
    //                 'issueDescription': this.dataForm.issueDescription,
    //                 'systematicClassification': this.selectedSystematic,
    //                 'firstFaultyParts': this.selectedFirstFaultyParts,
    //                'secondFaultyParts': this.selectedSecondFaultyParts,
    //                 'faultType': this.selectedFaultType,
    //                 'faultModel': this.selectedFaultModel,
    //                 'issuePhoto': this.dataForm.issuePhoto,
    //                 'rectificationRequirement': this.dataForm.rectificationRequirement,
    //                 'peliminaryAnalysis': this.dataForm.peliminaryAnalysis,
    //                 'requiredCompletionTime': this.dataForm.requiredCompletionTime,
    //                 'responsibleDepartment': this.dataForm.responsibleDepartment,
    //                 'rectificationStatus': this.dataForm.rectificationStatus,
    //                 'actualCompletionTime': this.dataForm.actualCompletionTime,
    //                 'rectificationPhotoDeliverable': this.dataForm.rectificationPhotoDeliverable,
    //                 'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
    //                 'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
    //                 'remark': this.dataForm.remark,
    //                 'creator': this.dataForm.creator,
    //                 'creationTime': this.dataForm.creationTime,
    //                 'lastModifier': this.dataForm.lastModifier,
    //                 'lastModificationTime': this.dataForm.lastModificationTime,
    //                 'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
    //                 'associatedIssueAddition': this.dataForm.associatedIssueIds.join(','),
    //                 'creationDuration': this.dataForm.creationDuration,
    //                 'causeAnalysis': this.dataForm.causeAnalysis,
    //                 'rectificationVerificationStatus': this.dataForm.rectificationVerificationStatus,
    //                 'verificationConclusion': this.dataForm.verificationConclusion,
    //                 'verifier': this.dataForm.verifier,
    //                 'level': this.dataForm.level,
    //                 'state': this.dataForm.state,
    //                 'formula': this.dataForm.formula
    //               })
    //             });
    //           } else {
    //             throw new Error("操作失败: " + data.msg);
    //           }
    //         })
    //         .then(({ data }) => {
    //           if (data && data.code === 0) {
    //             this.visible = false;
    //             this.visibleR = false;
    //             this.$emit('refreshDataList');
    //             this.$message.success("操作成功");
    //           } else {
    //             this.$message.error(data.msg);
    //           }
    //         })
    //         .catch(error => {
    //           console.error(error);
    //         });
    //     }
    //   });
    // },
    dataFormSubmit() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId);
            this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber);

            console.log("this.dataForm.issueCategoryId", this.dataForm.issueCategoryId);
            console.log("this.dataForm.vehicleNumbers", this.dataForm.vehicleNumbers);

            // 调用检查重复问题的方法
            // await this.checkDuplicateIssue();

            // 调用后端重复检查接口
            const { data: checkData } = await this.$http({
              url: this.$http.adornUrl('/generator/issuetable/checkReplicateIssue'),
              method: 'post',
              data: this.$http.adornData({
                issueId: this.dataForm.issueId,
                issueCategoryId: this.dataForm.issueCategoryId,
                systematicClassification: this.selectedSystematic,
                firstFaultyParts: this.selectedFirstFaultyParts,
                secondFaultyParts: this.selectedSecondFaultyParts,
                faultType: this.selectedFaultType,
                faultModel: this.selectedFaultModel,
              }),
            });

            if (checkData && checkData.code === 0) {
              // 生成问题编号
              const issueNumber = await this.generateSerialNumber();
              this.dataForm.issueNumber = issueNumber; // 确保是字符串

              // 提交保存或更新请求
              const { data: saveData } = await this.$http({
                url: this.$http.adornUrl(`/generator/issuetable/${!this.dataForm.issueId ? 'save' : 'update'}`),
                method: 'post',
                data: this.$http.adornData({
                  ...this.dataForm, // 展开表单数据
                  systematicClassification: this.selectedSystematic,
                  firstFaultyParts: this.selectedFirstFaultyParts,
                  secondFaultyParts: this.selectedSecondFaultyParts,
                  faultType: this.selectedFaultType,
                  faultModel: this.selectedFaultModel,
                  vehicleTypeId: this.dataForm.vehicleTypeIds.join(','),
                  vehicleNumberId: this.dataForm.vehicleNumbers.join(','),
                  associatedIssueAddition: this.dataForm.associatedIssueIds.join(','),
                }),
              });

              if (saveData && saveData.code === 0) {
                this.visible = false;
                this.visibleR = false;
                this.$emit('refreshDataList');
                this.$message.success("操作成功");
              } else {
                throw new Error(saveData.msg);
              }
            } else {
              throw new Error("操作失败: " + checkData.msg);
            }
          } catch (error) {
            console.error(error);
            this.$message.error(error.message || "操作失败");
          }
        }
      });
    }
,

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
    addPhoto (id) {
      this.dataForm.issueId = id
      this.addImageDialogVisible = true
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
      console.log('数据+++' ,this.dataForm)
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
              this.dataForm.peliminaryAnalysis = data.issueTable.peliminaryAnalysis
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
    initVE (id) {
      this.fetchuserinform() //获取用户名
      this.dataForm.issueId = id || 0
      // console.log("成功获取ID：" ,this.dataForm.issueId)
      this.visibleVE = true
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
              this.dataForm.overDue = data.issueTable.overDue
              this.dataForm.verificationDeadline = data.issueTable.verificationDeadline
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
              // 计算 creationDuration
              // const currentTime = new Date()
              // const creationTime = new Date(this.dataForm.creationTime)
              // const duration = Math.floor((currentTime - creationTime) / (1000 * 60 * 60 * 24)) // 计算相差的天数
              // this.dataForm.creationDuration = duration

            }
          })
        }
      })
      // const currentTime = new Date()
      // const creationTimeString = this.dataForm.creationTime.replace(" ", "T");
      // const creationTime = new Date(creationTimeString);
      // console.log("成功获取时长：" ,creationTime)

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
    viewIssueDetails (associatedRectificationRecords) {
      this.visibleAs = true
      console.log("查看关联问题：" ,associatedRectificationRecords)
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (associatedRectificationRecords) {
          this.$http({
            url: this.$http.adornUrl(`/generator/issuetable/infoByassociate/${associatedRectificationRecords}`),
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
    // dataFormSubmit () {
    //   this.$refs['dataForm'].validate((valid) => {
    //     if (valid) {
    //       this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId)
    //       this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber)
    //       // 确保 issueCategoryId 是一个数组
    //       // if (!Array.isArray(this.dataForm.issueCategoryId)) {
    //       //   this.dataForm.issueCategoryId = [this.dataForm.issueCategoryId]
    //       // }
    //       // 将数组转换为逗号分隔的字符串
    //       // const issueCategoryIdString = this.dataForm.issueCategoryId.join(',')
    //         this.$http({
    //           url: this.$http.adornUrl(`/generator/issuetable/${!this.dataForm.issueId ? 'save' : 'update'}`),
    //           method: 'post',
    //           data: this.$http.adornData({
    //             'issueId': this.dataForm.issueId || undefined,
    //             'serialNumber': this.dataForm.serialNumber,
    //             'issueNumber': this.generateSerialNumber(),
    //             'inspectionDepartment': this.dataForm.inspectionDepartment,
    //             'inspectionDate': this.dataForm.inspectionDate,
    //             'issueCategoryId': this.dataForm.issueCategoryId, // 使用转换后的字符串
    //             'vehicleTypeId': this.dataForm.vehicleTypeIds.join(','),
    //             'vehicleNumberId': this.dataForm.vehicleNumbers.join(','),
    //             'issueDescription': this.dataForm.issueDescription,
    //             'issuePhoto': this.dataForm.issuePhoto,
    //             'rectificationRequirement': this.dataForm.rectificationRequirement,
    //             'requiredCompletionTime': this.dataForm.requiredCompletionTime,
    //             'responsibleDepartment': this.dataForm.responsibleDepartment,
    //             'rectificationStatus': this.dataForm.rectificationStatus,
    //             'actualCompletionTime': this.dataForm.actualCompletionTime,
    //             'rectificationPhotoDeliverable': this.dataForm.rectificationPhotoDeliverable,
    //             'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
    //             'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
    //             'remark': this.dataForm.remark,
    //             'creator': this.dataForm.creator,
    //             'creationTime': this.dataForm.creationTime,
    //             'lastModifier': this.dataForm.lastModifier,
    //             'lastModificationTime': this.dataForm.lastModificationTime,
    //             'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
    //             'associatedIssueAddition': this.dataForm.associatedIssueIds.join(','), // 将数组转换为逗号分隔的字符串
    //             'creationDuration': this.dataForm.creationDuration,
    //             'causeAnalysis': this.dataForm.causeAnalysis,
    //             'rectificationVerificationStatus':this.dataForm.rectificationVerificationStatus,
    //             'verificationConclusion': this.dataForm.verificationConclusion,
    //             'verifier': this.dataForm.verifier,
    //             // 'reviewers': this.dataForm.reviewers,
    //             'level': this.dataForm.level,
    //             'state': this.dataForm.state,
    //             'formula': this.dataForm.formula
    //           })
    //         }).then(({data}) => {
    //           if (data && data.code === 0) {
    //             this.$message({
    //               message: '操作成功',
    //               type: 'success',
    //               duration: 1500,
    //               onClose: () => {
    //                 this.visible = false
    //                 this.visibleR = false
    //                 this.$emit('refreshDataList')
    //               }
    //             })
    //           } else {
    //             this.$message.error(data.msg)
    //           }
    //         })
    //     }
    //   })
    //   // 重置 vehicles 数组，只保留一个初始组合
    //   this.dataForm.vehicles = [{ vehicleTypeId: '', vehicleNumber: '', key: Date.now() }]
    //   this.vehicleNumberOptions = []
    // },


    // dataFormSubmit () {
    //   this.$refs['dataForm'].validate((valid) => {
    //     console.log(valid)
    //     if (valid) {
    //       this.dataForm.vehicleTypeIds = this.dataForm.vehicles.map(vehicle => vehicle.vehicleTypeId)
    //       this.dataForm.vehicleNumbers = this.dataForm.vehicles.map(vehicle => vehicle.vehicleNumber)
    //       console.log("this.dataForm.issueCategoryId" ,this.dataForm.issueCategoryId)
    //       // 确保 issueCategoryId 是一个数组
    //       // if (!Array.isArray(this.dataForm.issueCategoryId)) {
    //       //   this.dataForm.issueCategoryId = [this.dataForm.issueCategoryId]
    //       // }
    //       // 将数组转换为逗号分隔的字符串
    //       // const issueCategoryIdString = this.dataForm.issueCategoryId.join(',')
    //       this.$http({
    //         url: this.$http.adornUrl('/generator/issuetable/checkDuplicate'),
    //         method: 'post',
    //         data: this.$http.adornData({
    //           // vehicleTypeIds: this.dataForm.vehicleTypeIds.join(','),
    //           vehicleNumbers: this.dataForm.vehicleNumbers.join(','),
    //           issueCategoryId: this.dataForm.issueCategoryId,
    //         })
    //       }).then(({ data }) => {
    //         if (data && data.code === 0) {
    //           // 如果有重复，提示用户并终止后续操作
    //           if (data.isDuplicate) {
    //             this.$message.error('此车已发生过相同问题');
    //             return;
    //           }
    //           this.$http({
    //             url: this.$http.adornUrl(`/generator/issuetable/${!this.dataForm.issueId ? 'save' : 'update'}`),
    //             method: 'post',
    //             data: this.$http.adornData({
    //               'issueId': this.dataForm.issueId || undefined,
    //               'serialNumber': this.dataForm.serialNumber,
    //               'issueNumber': this.generateSerialNumber(),
    //               'inspectionDepartment': this.dataForm.inspectionDepartment,
    //               'inspectionDate': this.dataForm.inspectionDate,
    //               'issueCategoryId': this.dataForm.issueCategoryId, // 使用转换后的字符串
    //               'vehicleTypeId': this.dataForm.vehicleTypeIds.join(','),
    //               'vehicleNumberId': this.dataForm.vehicleNumbers.join(','),
    //               'issueDescription': this.dataForm.issueDescription,
    //               'issuePhoto': this.dataForm.issuePhoto,
    //               'rectificationRequirement': this.dataForm.rectificationRequirement,
    //               'requiredCompletionTime': this.dataForm.requiredCompletionTime,
    //               'responsibleDepartment': this.dataForm.responsibleDepartment,
    //               'rectificationStatus': this.dataForm.rectificationStatus,
    //               'actualCompletionTime': this.dataForm.actualCompletionTime,
    //               'rectificationPhotoDeliverable': this.dataForm.rectificationPhotoDeliverable,
    //               'rectificationResponsiblePerson': this.dataForm.rectificationResponsiblePerson,
    //               'requiredSecondRectificationTime': this.dataForm.requiredSecondRectificationTime,
    //               'remark': this.dataForm.remark,
    //               'creator': this.dataForm.creator,
    //               'creationTime': this.dataForm.creationTime,
    //               'lastModifier': this.dataForm.lastModifier,
    //               'lastModificationTime': this.dataForm.lastModificationTime,
    //               'associatedRectificationRecords': this.dataForm.associatedRectificationRecords,
    //               'associatedIssueAddition': this.dataForm.associatedIssueIds.join(','), // 将数组转换为逗号分隔的字符串
    //               'creationDuration': this.dataForm.creationDuration,
    //               'causeAnalysis': this.dataForm.causeAnalysis,
    //               'rectificationVerificationStatus':this.dataForm.rectificationVerificationStatus,
    //               'verificationConclusion': this.dataForm.verificationConclusion,
    //               'verifier': this.dataForm.verifier,
    //               // 'reviewers': this.dataForm.reviewers,
    //               'level': this.dataForm.level,
    //               'state': this.dataForm.state,
    //               'formula': this.dataForm.formula
    //             })
    //           }).then(({data}) => {
    //             if (data && data.code === 0) {
    //               this.$message({
    //                 message: '操作成功',
    //                 type: 'success',
    //                 duration: 1500,
    //                 onClose: () => {
    //                   this.visible = false
    //                   this.visibleR = false
    //                   this.$emit('refreshDataList')
    //                 }
    //               })
    //             } else {
    //               this.$message.error(data.msg)
    //             }
    //           })
    //         } else {
    //           this.$message.error(data.msg)
    //         }
    //       }).catch(error => {
    //         this.$message.error('检查重复问题时出错，请稍后再试');
    //         console.error('请求失败：', error);
    //       });
    //     }
    //   });
    //   // 重置 vehicles 数组，只保留一个初始组合
    //   this.dataForm.vehicles = [{ vehicleTypeId: '', vehicleNumber: '', key: Date.now() }]
    //   this.vehicleNumberOptions = []
    // },
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
              'verificationDeadline': this.dataForm.verificationDeadline,
              'verifier': this.dataForm.verifier,
              'overDue':'false',
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
                  this.visibleVE = false
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
      this.fetchuserinform()
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
              'creator':this.dataForm.creator,
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
    // 新图提交
    addPhotoSubmit () {
      this.addImageDialogVisible = false
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/generator/issuetable/update`),
            method: 'post',
            data: this.$http.adornData({
              'issueId': this.dataForm.issueId || undefined,
              'serialNumber': this.dataForm.serialNumber,
              'issueNumber': this.dataForm.issueNumber,
              'inspectionDepartment': this.dataForm.inspectionDepartment,
              'inspectionDate': this.dataForm.inspectionDate,
              'issueCategoryId': this.dataForm.issueCategoryId,
              'vehicleTypeId': this.dataForm.vehicleTypeId,
              'vehicleNumberId': this.dataForm.vehicleNumberId,
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
              'associatedIssueAddition': this.dataForm.associatedIssueAddition,
              'creationDuration': this.dataForm.creationDuration,
              'causeAnalysis': this.dataForm.causeAnalysis,
              'rectificationVerificationStatus': this.dataForm.rectificationVerificationStatus,
              'verificationConclusion': this.dataForm.verificationConclusion,
              'verifier': this.dataForm.verifier,
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

  }
}
</script>
<style scoped>
.right-aligned {
  margin-left: 80px; /* 可根据需要调整移动的距离 */
}
</style>
