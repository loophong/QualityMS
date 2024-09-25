<template>
  <div class="container">

    <div v-if="this.routerParam">
      <ul>
        <li v-for="item in this.routerParam" :key="item.qcsrId">
          {{ item.topicName }}
          {{ item.topicType }}
        </li>
        <!-- <el-button type="danger" @click="toIssue()" style="justify-content: flex-end;">问题添加</el-button> -->
      </ul>
    </div>

    <div class="plan1" v-if="value === '问题解决型'">
      <br>
      <el-steps :active="active1" align-center>
        <el-step v-for="item in filteredDataList1" :key="item.dictId" :title="`${item.phase}阶段`"
          :description="item.step"></el-step>
      </el-steps>
      <br>
      <br>
      <el-card class="formZone" shadow="hover">
        <div v-if="active1 === 1">
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="选择课题描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>


            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" ref="upload" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :auto-upload="false" :on-remove="handleRemove"
                  :before-remove="beforeRemove" multiple :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button slot="trigger" size="small" type="primary">添加文件</el-button>
                  <el-button style="margin-left: 10px;" size="small" type="success"
                    @click="submitUpload">上传文件</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <!-- <el-button>取消</el-button> -->
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>

            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 2">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="现状调查描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :auto-upload="false" :on-remove="handleRemove"
                  :before-remove="beforeRemove" multiple :limit="3" :on-exceed="handleExceed" :file-list="fileList"
                  list-type="picture-card">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <!-- <el-button>取消</el-button> -->
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 3">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="设定目标描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary" @click="submitUpload">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <!-- <el-button>取消</el-button> -->
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 4">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="原因分析描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <!-- <el-button>取消</el-button> -->
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 5">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="要因确定描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 6">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="制定对策描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 7">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="对策实施描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 8">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="活动前现状" prop="stageBefore">
              <el-input v-model="form.stageBefore" placeholder="活动前现状"></el-input>
            </el-form-item>
            <el-form-item label="活动后现状" prop="stageAfter">
              <el-input v-model="form.stageAfter" placeholder="活动后现状"></el-input>
            </el-form-item>
            <el-form-item label="效果检查描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="效果检查描述"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 9">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="巩固措施描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 10">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="总结和下一步打算描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)">确定</el-button>
              <el-button :disabled="lastDisabled1" @click="lastStep1">上一步</el-button>
              <el-button :disabled="nextDisabled1" @click="nextStep1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </div>
    <div class="plan2" v-if="value === '问题解决型(指令型)'">
      <br>
      <el-steps :active="active2" align-center>
        <el-step v-for="item in filteredDataList2" :key="item.dictId" :title="`${item.phase}阶段`"
          :description="item.step"></el-step>
      </el-steps>
      <br>
      <br>
      <el-card class="formZone" shadow="hover">
        <div v-if="active2 === 1">
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="阶段名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="选择课题描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :on-change="handleUpChange" :file-list="fileList">
                  <el-button style="margin-left: 10px;" size="small" type="success"
                    @click="submitUpload">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 2">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.participants" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="设定目标描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 3">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.participants" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="可靠性分析描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 4">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.participants" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="原因分析描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 5">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.participants" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="要因确定描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 6">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.participants" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="制定对策描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 7">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.participants" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="对策实施描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 8">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.participants" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="活动前现状" prop="stageBefore">
              <el-input v-model="form.stageBefore" placeholder="活动前现状"></el-input>
            </el-form-item>
            <el-form-item label="活动后现状" prop="stageAfter">
              <el-input v-model="form.stageAfter" placeholder="活动后现状"></el-input>
            </el-form-item>
            <el-form-item label="效果检查描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 9">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.participants" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="巩固措施描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="课题名称"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 10">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="课题名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.participants" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="总结和下一步打算描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder="总结和下一步打算描述"></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)">确定</el-button>
              <el-button :disabled="lastDisabled2" @click="lastStep2">上一步</el-button>
              <el-button :disabled="nextDisabled2" @click="nextStep2">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </div>
    <div class="plan3" v-if="value === '创新型'">
      <br>
      <el-steps :active="active3" align-center>
        <el-step v-for="item in filteredDataList3" :key="item.dictId" :title="`${item.phase}阶段`"
          :description="item.step"></el-step>
      </el-steps>
      <br>
      <br>
      <el-card class="formZone" shadow="hover">
        <div v-if="active3 === 1">
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="阶段名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="选择课题描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder=""></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active3)">确定</el-button>
              <el-button :disabled="lastDisabled3" @click="lastStep3">上一步</el-button>
              <el-button :disabled="nextDisabled3" @click="nextStep3">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 2">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="阶段名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="设定目标描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder=""></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)">确定</el-button>
              <el-button :disabled="lastDisabled3" @click="lastStep3">上一步</el-button>
              <el-button :disabled="nextDisabled3" @click="nextStep3">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>

        <div v-if="active3 === 3">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="阶段名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="提出方案确定最佳方案描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder=""></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active3)">确定</el-button>
              <el-button :disabled="lastDisabled3" @click="lastStep3">上一步</el-button>
              <el-button :disabled="nextDisabled3" @click="nextStep3">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>

        <div v-if="active3 === 4">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="阶段名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="制定对策描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder=""></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)">确定</el-button>
              <el-button :disabled="lastDisabled3" @click="lastStep3">上一步</el-button>
              <el-button :disabled="nextDisabled3" @click="nextStep3">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 5">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="阶段名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="对策实施描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder=""></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)">确定</el-button>
              <el-button :disabled="lastDisabled3" @click="lastStep3">上一步</el-button>
              <el-button :disabled="nextDisabled3" @click="nextStep3">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 6">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="阶段名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="活动前现状" prop="stageBefore">
              <el-input v-model="form.stageBefore" placeholder=""></el-input>
            </el-form-item>
            <el-form-item label="活动后现状" prop="stageAfter">
              <el-input v-model="form.stageAfter" placeholder=""></el-input>
            </el-form-item>
            <el-form-item label="效果检查描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder=""></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)">确定</el-button>
              <el-button :disabled="lastDisabled3" @click="lastStep3">上一步</el-button>
              <el-button :disabled="nextDisabled3" @click="nextStep3">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 7">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="阶段名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="标准化描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder=""></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)">确定</el-button>
              <el-button :disabled="lastDisabled3" @click="lastStep3">上一步</el-button>
              <el-button :disabled="nextDisabled3" @click="nextStep3">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 8">
          <br>
          <el-form>
            <br>
            <el-form-item label="阶段名称" prop="stageName">
              <el-input v-model="form.stageName" placeholder="阶段名称"></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker clearable v-model="form.planDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker clearable v-model="form.actualDate" type="daterange" range-separator="-"
                start-placeholder="开始日期" end-placeholder="结束日期" placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select v-model="form.stagePeople" multiple placeholder="请选择主要参与人员">
                <el-option v-for="participant in participantOptions" :key="participant.value" :label="participant.label"
                  :value="participant.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="总结与下一步打算描述" prop="stageDescribe">
              <el-input v-model="form.stageDescribe" placeholder=""></el-input>
            </el-form-item>

            <el-form-item>
              <span>QC工具： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
              <el-button size="small" type="primary" @click="toggleControl">控制图</el-button>
              <el-button size="small" type="primary" @click="fishBonetoggleLineAndBar">鱼骨图</el-button>
            </el-form-item>

            <el-form-item>
              <div style="display: flex; align-items: center;">
                <span>上传附件：</span>
                <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple
                  :limit="3" :on-exceed="handleExceed" :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </div>
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active3)">确定</el-button>
              <el-button :disabled="lastDisabled3" @click="lastStep3">上一步</el-button>
              <el-button :disabled="nextDisabled3" @click="nextStep3">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </div>
    <el-dialog title="折柱混合图" :close-on-click-modal="false" :visible.sync="dialogVisible">
      <div id="main" ref="main" style="width: 100%; height: 400px;"></div>
      <div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
      </div>
    </el-dialog>
<!--    <el-dialog title="鱼骨图" :close-on-click-modal="false" :visible.sync="fishBonedialogVisible">-->
<!--      <div id="main" ref="main" style="width: 100%; height: 400px;"></div>-->
<!--      <div>-->
<!--        <span slot="footer" class="dialog-footer">-->
<!--          <el-button @click="fishBonedialogVisible = false">取 消</el-button>-->
<!--          <el-button type="primary" @click="fishBonedialogVisible = false">确 定</el-button>-->
<!--        </span>-->
<!--      </div>-->
<!--    </el-dialog>-->
    <el-dialog  title="鱼骨图" :close-on-click-modal="false" :visible.sync="fishBonedialogVisible" width="80%">
      <div>
        <fish-bone  ref="fishBone" ></fish-bone>
      </div>
      <div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="fishBonedialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="fishBonedialogVisible = false">确 定</el-button>
        </span>
      </div>
    </el-dialog>
    <el-dialog title="控制图" :visible.sync="dialogVisibleControl" :close-on-click-modal="false" width="80%">
      <div style="width: 100%; height: 100%;">
        <control ref="qcChart"></control>
      </div>
      <el-button @click="dialogVisibleControl = false">取 消</el-button>
      <el-button type="primary" @click="dialogVisibleControl = false">确 定</el-button>

    </el-dialog>

  </div>
</template>

<script>
import * as echarts from 'echarts';
import { FishBones } from '@/components/fishbone/FishBone'
import fishBone from "../../qcTools/fishBone.vue";
import control from '@/views/modules/QCmanagement/qcTools/control.vue';
export default {
  data() {
    return {
      fileList: [],
      dataForm: {
        stepId: 0,
        stepSubjectId: '',
        stepType: '',
        stepProcess: '',
        stageName: '',
        stagePlanStart: '',
        stagePlanEnd: '',
        stageActualStart: '',
        stageActualEnd: '',
        stagePeople: '',
        stageDescribe: '',
        stageExtra: '',
        stageBefore: '',
        stageAfter: '',
        stageConsolidate: '',
        key: ''
      },
      dialogVisible: false,
      dialogVisibleControl: false,
      fishBonedialogVisible: false,  //鱼骨图弹窗
      active1: 1,
      active2: 1,
      active3: 1,
      pageIndex: 1,
      pageSize: 50,
      totalPage: 0,
      dataListLoading: false,
      planStyle1: false,
      planStyle2: false,
      planStyle3: false,
      form: {
        stepId: 0,
        stageName: '',
        planDate: [],
        actualDate: [],
        stagePeople: '',
        stageDescribe: '',
        stageBefore: '',
        stageAfter: '',
        stageExtra: '',
        stageConsolidate: '',
      },

      participantOptions: [
        { value: 'participant1', label: '参与人员1' },
        { value: 'participant2', label: '参与人员2' },
      ],

      value: '问题解决型',
      // fileList: [],
      planStepList: [],
      dictWorkFlow: [],
      routerParam: {
        qcsrId: '',
        topicName: '',
        teamNumberIds: '',
        topicLeader: '',
        topicType: '',
      },
    }
  },
  components() {
    fishBone,
      control
  },
  mounted() {

  },
  activated() {
    this.initRouterParam()
    this.getDictWorkFlowList()
    this.getPlanStepDataList()

  },
  computed: {
    lastDisabled1() {
      return this.active1 === 1;
    },
    nextDisabled1() {
      return this.active1 === 10;
    },
    lastDisabled2() {
      return this.active2 === 1;
    },
    nextDisabled2() {
      return this.active2 === 10;
    },
    lastDisabled3() {
      return this.active3 === 1;
    },
    nextDisabled3() {
      return this.active3 === 8;
    },
    filteredDataList1() {
      return this.dictWorkFlow.filter(item => item.type === '问题解决型1');
    },
    filteredDataList2() {
      return this.dictWorkFlow.filter(item => item.type === '问题解决型2');
    },
    filteredDataList3() {
      return this.dictWorkFlow.filter(item => item.type === '创新型');
    },
  },
  methods: {
    // 获取数据列表
    async getPlanStepDataList() {
      await this.$http({
        url: this.$http.adornUrl('/qcPlan/step/idList'),
        method: 'get',
        params: this.$http.adornParams({
          'page': 1,
          'limit': 100,
          'key': this.routerParam[0].qcsrId
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.planStepList = data.idList
          console.log(this.planStepList)
          this.findMatchingItem(1)
          // this.totalPage = data.page.totalCount
        } else {
          this.planStepList = []
          // this.totalPage = 0
        }
      })
    },
    initRouterParam() {
      const res = this.$route.query.data ? JSON.parse(this.$route.query.data) : { qcsrId: '', topicName: '', teamNumberIds: '', topicLeader: '', topicType: '' };
      this.routerParam = res
      console.log(this.routerParam)
      const tmp = this.routerParam[0].teamNumberIds.split(',');
      this.participantOptions = tmp.map(id => ({
        value: id.trim(),
        label: id.trim()
      }));
      this.value = this.routerParam[0].topicType;
    },
    chart(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },

    findMatchingItem(id) {
      this.initForm()
      for (let item of this.planStepList) {
        if (item.stepProcess === id) {
          this.form.stepId = item.stepId
          this.form = item;
          this.form
          this.form.planDate = [item.stagePlanStart, item.stagePlanEnd]
          this.form.actualDate = [item.stageActualStart, item.stageActualEnd]
          if (item.stagePeople) {
            this.form.stagePeople = this.form.stagePeople.split(',').map(participant => participant.trim());
          }
          break;
        }
      }
    },
    getDictWorkFlowList() {
      this.$http({
        url: this.$http.adornUrl('/qcManagement/dictWorkFlow/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          // 'key': this.dataForm.key
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dictWorkFlow = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dictWorkFlow = []
          this.totalPage = 0
        }
      })
    },
    handleUpChange(file, fileList) {
      console.log('++++++++++++++++++++++++++++')
      console.log(file)
      console.log(fileList)
      console.log('++++++++++++++++++++++++++++')
      this.fileList = fileList;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    onSubmit() {
      console.log('提交表单，上传文件:', this.fileList);
    },
    toggleLineAndBar() {
      this.dialogVisible = !this.dialogVisible;
      this.$nextTick(() => {
        this.initChart();
      });
    },
    // 鱼骨图弹窗
    fishBonetoggleLineAndBar() {
      this.fishBonedialogVisible = !this.fishBonedialogVisible;

    },
    toggleControl() {
      this.dialogVisibleControl = !this.dialogVisibleControl;
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => { });
    },
    lastStep1() {
      if (this.active1 > 1) {
        this.active1--;
      }
      this.findMatchingItem(this.active1)
    },
    nextStep1() {
      if (this.active1 < 10) {
        this.active1++;
      }
    },
    lastStep2() {
      if (this.active2 > 1) {
        this.active2--;
      }
    },
    nextStep2() {
      if (this.active2 < 10) {
        this.active2++;
      }
    },
    lastStep3() {
      if (this.active3 > 1) {
        this.active3--;
      }
      console.log(this.active3)
      console.log(this.planStepList)
      this.findMatchingItem(this.active3)
      console.log(this.active3)
    },
    nextStep3() {
      if (this.active3 < 8) {
        this.active3++;
      }
      console.log(this.active3)
      console.log(this.planStepList)
      console.log(this.form)
      this.findMatchingItem(this.active3)
      console.log(this.active3)
    },
    initChart() {
      if (!this.$refs.main) {
        console.error('Main element not found');
        return;
      }
      var app = {};
      let myChart = echarts.init(this.$refs.main);
      const posList = [
        'left',
        'right',
        'top',
        'bottom',
        'inside',
        'insideTop',
        'insideLeft',
        'insideRight',
        'insideBottom',
        'insideTopLeft',
        'insideTopRight',
        'insideBottomLeft',
        'insideBottomRight'
      ];
      app.configParameters = {
        rotate: {
          min: -90,
          max: 90
        },
        align: {
          options: {
            left: 'left',
            center: 'center',
            right: 'right'
          }
        },
        verticalAlign: {
          options: {
            top: 'top',
            middle: 'middle',
            bottom: 'bottom'
          }
        },
        position: {
          options: posList.reduce(function (map, pos) {
            map[pos] = pos;
            return map;
          }, {})
        },
        distance: {
          min: 0,
          max: 100
        }
      };
      app.config = {
        rotate: 0,
        align: 'center',
        verticalAlign: 'middle',
        position: 'top',
        distance: 15,
        onChange: function () {
          const labelOption = {
            rotate: app.config.rotate,
            align: app.config.align,
            verticalAlign: app.config.verticalAlign,
            position: app.config.position,
            distance: app.config.distance
          };
          myChart.setOption({
            series: [
              {
                label: labelOption
              },
              {
                label: labelOption
              },
              {
                label: labelOption
              },
              {
                label: labelOption
              }
            ]
          });
        }
      };
      const labelOption = {
        show: true,
        position: app.config.position,
        distance: app.config.distance,
        align: app.config.align,
        verticalAlign: app.config.verticalAlign,
        rotate: app.config.rotate,
        formatter: '{c}',
        fontSize: 16,
        rich: {
          name: {}
        }
      };
      const option = {
        title: {
          text: '折柱混合图',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['柱', '线']
        },
        toolbox: {
          show: true,
          orient: 'vertical',
          left: 'right',
          top: 'center',
          feature: {
            mark: { show: true, },
            dataView: {
              show: true,
              readOnly: false,
              title: '编辑',
              lang: ['数据编辑', '关闭', '更新'],
            },
            restore: { show: true, title: '重置' },
            saveAsImage: { show: true, title: '保存' }
          }
        },
        xAxis: [
          {
            axisTick: { show: false },
            data: ['A', 'B', 'C', 'D', 'E', 'F'],
          }
        ],
        yAxis: [
          {
            type: 'value'
          },
          {
            type: 'value',
            name: '线',
            splitLine: { show: false },
            axisLabel: {
              formatter: '{value} '
            }
          }
        ],
        series: [{
          name: '柱',
          type: 'bar',
          color: '',
          label: labelOption,
          emphasis: {
            focus: 'series'
          },
          data: [5, 20, 36, 10, 10, 20],
        },
        {
          name: '线',
          type: 'line',
          label: labelOption,
          yAxisIndex: 1,
          emphasis: {
            focus: 'series'
          },
          data: [20, 3, 12, 46, 22, 14],
        }]
      };
      option && myChart.setOption(option);
    },
    //初始化鱼骨图
    initFishBoneChart() {
      console.log('initFishBone')
      new FishBones({
        id: 'fishBone',
        jsonData: this.testFishData,
        canvasSize: [document.body.scrollWidth, document.body.scrollHeight],
        clickNodeFunction: (node, event) => {
        },
      }).init()
    },
    initForm() {
      this.form = {
        stageName: '',
        planDate: [],
        actualDate: [],
        stagePeople: [],
        stageDescribe: '',
        stageBefore: '',
        stageAfter: '',
        stageExtra: '',
        stageConsolidate: '',
      }
    },
    init(id) {
      this.dataForm.stepId = id || 0
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.stepId) {
          this.$http({
            url: this.$http.adornUrl(`/qcPlan/step/info/${this.dataForm.stepId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.stepSubjectId = data.qcStep.stepSubjectId
              this.dataForm.stepType = data.qcStep.stepType
              this.dataForm.stepProcess = data.qcStep.stepProcess
              this.dataForm.stageName = data.qcStep.stageName
              this.dataForm.stagePlanStart = data.qcStep.stagePlanStart
              this.dataForm.stagePlanEnd = data.qcStep.stagePlanEnd
              this.dataForm.stageActualStart = data.qcStep.stageActualStart
              this.dataForm.stageActualEnd = data.qcStep.stageActualEnd
              this.dataForm.stagePeople = data.qcStep.stagePeople
              this.dataForm.stageDescribe = data.qcStep.stageDescribe
              this.dataForm.stageExtra = data.qcStep.stageExtra
              this.dataForm.stageBefore = data.qcStep.stageBefore
              this.dataForm.stageAfter = data.qcStep.stageAfter
              this.dataForm.stageConsolidate = data.qcStep.stageConsolidate
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit(id) {
      // this.$refs['dataForm'].validate((valid) => {
      //   if (valid) {

      this.$http({
        url: this.$http.adornUrl(`/qcPlan/step/${!this.form.stepId ? 'save' : 'update'}`),
        method: 'post',
        data: this.$http.adornData({
          'stepId': this.form.stepId || undefined,
          'stepSubjectId': this.routerParam[0].qcsrId,
          'stepType': this.routerParam[0].topicType,
          'stepProcess': id,
          'stageName': this.form.stageName,
          'stagePlanStart': this.form.planDate[0],
          'stagePlanEnd': this.form.planDate[1],
          'stageActualStart': this.form.actualDate[0],
          'stageActualEnd': this.form.actualDate[1],
          'stagePeople': `${this.form.stagePeople}`,
          'stageDescribe': this.form.stageDescribe,
          'stageExtra': this.form.stageExtra,
          'stageBefore': this.form.stageBefore,
          'stageAfter': this.form.stageAfter,
          'stageConsolidate': this.form.stageConsolidate || '',
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.visible = false
              // this.$emit('refreshDataList')
            }
          })
          if (this.routerParam[0].qcsrId) {
            this.$http({
              url: this.$http.adornUrl(`/qcSubject/registration/update`),
              method: 'post',
              data: this.$http.adornData({
                'qcsrId': this.routerParam[0].qcsrId,
                'topicReviewStatus': 3,
                'topicActivityStatus': id,
              })
            })
          }
        } else {
          this.$message.error(data.msg)
        }
      })
      //   }
      // })
    }
  }
}
</script>

<style lang="scss" scoped>
.formZone {
  padding: 20px;
}
</style>
