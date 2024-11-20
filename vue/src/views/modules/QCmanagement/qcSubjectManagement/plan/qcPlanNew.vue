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
      <br />
      <el-steps :active="active1" align-center>
        <el-step
          v-for="item in filteredDataList1"
          :key="item.dictId"
          :title="`${item.phase}阶段`"
          :description="item.step"
        ></el-step>
      </el-steps>
      <!-- <br> -->
      <span>
        <el-button @click="handleClickToStep(1)">选择课题</el-button>
        <el-button @click="handleClickToStep(2)">现状调查</el-button>
        <el-button @click="handleClickToStep(3)">设定目标</el-button>
        <el-button @click="handleClickToStep(4)">原因分析</el-button>
        <el-button @click="handleClickToStep(5)">要因确定</el-button>
        <el-button @click="handleClickToStep(6)">制定对策</el-button>
        <el-button @click="handleClickToStep(7)">对策实施</el-button>
        <el-button @click="handleClickToStep(8)">检查效果</el-button>
        <el-button @click="handleClickToStep(9)">巩固措施</el-button>
        <el-button @click="handleClickToStep(10)">总结</el-button>
      </span>
      <br />
      <br />
      <el-card class="formZone" shadow="hover">
        <div v-if="active1 === 1">
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="选择课题描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <!-- <el-button>取消</el-button> -->
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>

        <div v-if="active1 === 2">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="现状调查描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>
            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <!-- <el-button>取消</el-button> -->
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 3">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              type="textarea"
              label="设定目标描述"
              prop="stageDescribe"
            >
              <el-input
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <!-- <el-button>取消</el-button> -->
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 4">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="原因分析描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加添加QC工具实例实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <!-- <el-button>取消</el-button> -->
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 5">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="要因确定描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 6">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="制定对策描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 7">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="对策实施描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 8">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="活动前现状" prop="stageBefore">
              <el-input
                type="textarea"
                v-model="form.stageBefore"
                placeholder="活动前现状"
              ></el-input>
            </el-form-item>
            <el-form-item label="活动后现状" prop="stageAfter">
              <el-input
                type="textarea"
                v-model="form.stageAfter"
                placeholder="活动后现状"
              ></el-input>
            </el-form-item>
            <el-form-item label="效果检查描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="效果检查描述"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 9">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="巩固措施描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 10">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              type="textarea"
              label="总结和下一步打算描述"
              prop="stageDescribe"
            >
              <el-input
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active1
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active1)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled1" @click="lastStep1"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled1" @click="nextStep1"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </div>

    <div class="plan2" v-if="value === '问题解决型(指令型)'">
      <br />
      <el-steps :active="active2" align-center>
        <el-step
          v-for="item in filteredDataList2"
          :key="item.dictId"
          :title="`${item.phase}阶段`"
          :description="item.step"
        ></el-step>
      </el-steps>
      <span>
        <el-button @click="handleClickToStep(1)">选择课题</el-button>
        <el-button @click="handleClickToStep(2)">设定目标</el-button>
        <el-button @click="handleClickToStep(3)">可靠性分析</el-button>
        <el-button @click="handleClickToStep(4)">原因分析</el-button>
        <el-button @click="handleClickToStep(5)">要因确定</el-button>
        <el-button @click="handleClickToStep(6)">制定对策</el-button>
        <el-button @click="handleClickToStep(7)">对策实施</el-button>
        <el-button @click="handleClickToStep(8)">检查效果</el-button>
        <el-button @click="handleClickToStep(9)">巩固措施</el-button>
        <el-button @click="handleClickToStep(10)">总结</el-button>
      </span>
      <br />
      <br />
      <el-card class="formZone" shadow="hover">
        <div v-if="active2 === 1">
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="阶段名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="选择课题描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 2">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.participants"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="设定目标描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 3">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.participants"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="可靠性分析描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 4">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.participants"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="原因分析描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 5">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.participants"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="要因确定描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 6">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.participants"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="制定对策描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 7">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.participants"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="对策实施描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>
            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 8">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.participants"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="活动前现状" prop="stageBefore">
              <el-input
                type="textarea"
                v-model="form.stageBefore"
                placeholder="活动前现状"
              ></el-input>
            </el-form-item>
            <el-form-item label="活动后现状" prop="stageAfter">
              <el-input
                v-model="form.stageAfter"
                placeholder="活动后现状"
              ></el-input>
            </el-form-item>
            <el-form-item label="效果检查描述" prop="stageDescribe">
              <el-input
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 9">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.participants"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="巩固措施描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active2 === 10">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="课题名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.participationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.participants"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="总结和下一步打算描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder="总结和下一步打算描述"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active2
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active2)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled2" @click="lastStep2"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled2" @click="nextStep2"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </div>

    <div class="plan3" v-if="value === '创新型'">
      <br />
      <el-steps :active="active3" align-center>
        <el-step
          v-for="item in filteredDataList3"
          :key="item.dictId"
          :title="`${item.phase}阶段`"
          :description="item.step"
        ></el-step>
      </el-steps>
      <span>
        <el-button @click="handleClickToStep(1)">选择课题</el-button>
        <el-button @click="handleClickToStep(2)">设定目标</el-button>
        <el-button @click="handleClickToStep(3)">提出方案</el-button>
        <el-button @click="handleClickToStep(4)">制定对策</el-button>
        <el-button @click="handleClickToStep(5)">对策实施</el-button>
        <el-button @click="handleClickToStep(6)">效果检查</el-button>
        <el-button @click="handleClickToStep(7)">标准化</el-button>
        <el-button @click="handleClickToStep(8)">总结</el-button>
      </span>
      <br />
      <br />
      <el-card class="formZone" shadow="hover">
        <div v-if="active3 === 1">
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="阶段名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="选择课题描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder=""
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active3
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active3)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled3" @click="lastStep3"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled3" @click="nextStep3"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 2">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="阶段名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="设定目标描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder=""
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active3
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled3" @click="lastStep3"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled3" @click="nextStep3"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>

        <div v-if="active3 === 3">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="阶段名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="提出方案确定最佳方案描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder=""
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active3
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active3)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled3" @click="lastStep3"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled3" @click="nextStep3"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>

        <div v-if="active3 === 4">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="阶段名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="制定对策描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder=""
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active3
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled3" @click="lastStep3"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled3" @click="nextStep3"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 5">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="阶段名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="对策实施描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder=""
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active3
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled3" @click="lastStep3"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled3" @click="nextStep3"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 6">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="阶段名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="活动前现状" prop="stageBefore">
              <el-input
                type="textarea"
                v-model="form.stageBefore"
                placeholder=""
              ></el-input>
            </el-form-item>
            <el-form-item label="活动后现状" prop="stageAfter">
              <el-input
                type="textarea"
                v-model="form.stageAfter"
                placeholder=""
              ></el-input>
            </el-form-item>
            <el-form-item label="效果检查描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder=""
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active3
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled3" @click="lastStep3"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled3" @click="nextStep3"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 7">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="阶段名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="标准化描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder=""
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active3
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <!-- <el-button type="primary" @click="onSubmit">提交</el-button> -->
              <el-button type="primary" @click="dataFormSubmit(active3)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled3" @click="lastStep3"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled3" @click="nextStep3"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active3 === 8">
          <br />
          <el-form>
            <br />
            <el-form-item label="阶段名称" prop="stageName">
              <el-input
                v-model="form.stageName"
                placeholder="阶段名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="阶段计划活动时间">
              <el-date-picker
                clearable
                v-model="form.planDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段实际活动时间">
              <el-date-picker
                clearable
                v-model="form.actualDate"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                placeholder="请选择日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="阶段主要参与人员">
              <el-select
                v-model="form.stagePeople"
                multiple
                placeholder="请选择主要参与人员"
              >
                <el-option
                  v-for="participant in participantOptions"
                  :key="participant.value"
                  :label="participant.label"
                  :value="participant.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="总结与下一步打算描述" prop="stageDescribe">
              <el-input
                type="textarea"
                v-model="form.stageDescribe"
                placeholder=""
              ></el-input>
            </el-form-item>

            <el-form-item>
              <span>添加QC工具实例： </span>
              <el-button size="small" type="primary" @click="toggleLineAndBar"
                >折柱混合图</el-button
              >
              <el-button size="small" type="primary" @click="toggleControl"
                >控制图</el-button
              >
              <el-button
                size="small"
                type="primary"
                @click="fishBonetoggleLineAndBar"
                >鱼骨图</el-button
              >
              <el-button size="small" type="primary" @click="toggleSystem"
                >系统图</el-button
              >
              <el-button size="small" type="primary" @click="toggleScatter"
                >散点图</el-button
              >
              <el-button size="small" type="primary" @click="toggleRelation"
                >关联图</el-button
              >
              <el-button size="small" type="primary" @click="toggleHistogram"
                >直方图</el-button
              >
            </el-form-item>

            <!-- 动态生成的按钮组 -->
            <el-form-item>
              <span>已添加QC工具实例：</span>
              <template v-for="(group, type) in groupedData">
                <template v-for="(item, index) in group">
                  <!-- 判断条件修改为显示符合条件的数据 -->
                  <el-button
                    v-if="
                      item.conplanSubject === conplanSubject &&
                      item.conplanProcess === active3
                    "
                    :key="type + '-' + index"
                    size="small"
                    type="primary"
                    @click="toggleLineAndBarShow(item)"
                  >
                    {{ item.conplanName }}
                  </el-button>
                </template>
              </template>
            </el-form-item>

            <el-form-item label="上传附件">
              <el-upload
                ref="file"
                class="upload-btn-group"
                :file-list="fileList"
                :action="uploadUrl"
                :on-change="handleFileChange"
                :auto-upload="false"
              >
                <el-button size="middle" type="primary" icon="el-icon-upload"
                  >点击上传</el-button
                >
              </el-upload>
              <el-button
                class="upload-preview-btn"
                type="primary"
                size="middle"
                @click="uploadAllListVisible = true"
                >附件预览</el-button
              >
            </el-form-item>

            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.stageExtra"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit(active3)"
                >确定</el-button
              >
              <el-button :disabled="lastDisabled3" @click="lastStep3"
                >上一步</el-button
              >
              <el-button :disabled="nextDisabled3" @click="nextStep3"
                >下一步</el-button
              >
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </div>

    <!-- 引用的组件s -->
    <el-dialog
      title="折柱混合图"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      width="80%"
      v-if="dialogVisible"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <!-- <line-and-bar ref="qcChart"></line-and-bar> -->
        <line-and-bar
          ref="qcChart"
          :conplanSubject="conplanSubject"
          :conplanProcess="conplanProcess"
        ></line-and-bar>
      </div>
    </el-dialog>

    <el-dialog
      title="鱼骨图"
      :close-on-click-modal="false"
      :visible.sync="fishBonedialogVisible"
      width="80%"
      v-if="fishBonedialogVisible"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <fish-bone
          ref="fishBone"
          :conplanSubject="conplanSubject"
          :conplanProcess="conplanProcess"
        ></fish-bone>
      </div>
    </el-dialog>

    <el-dialog
      title="控制图"
      :visible.sync="dialogVisibleControl"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleControl"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <control
          ref="qcChart"
          :conplanSubject="conplanSubject"
          :conplanProcess="conplanProcess"
        ></control>
      </div>
    </el-dialog>

    <el-dialog
      title="系统图"
      :visible.sync="dialogVisibleSystem"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleSystem"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <system
          ref="qcChart"
          :conplanSubject="conplanSubject"
          :conplanProcess="conplanProcess"
        ></system>
      </div>
    </el-dialog>

    <el-dialog
      title="散点图"
      :visible.sync="dialogVisibleScatter"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleScatter"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <Scatter
          ref="qcChart"
          :conplanSubject="conplanSubject"
          :conplanProcess="conplanProcess"
        ></Scatter>
      </div>
    </el-dialog>

    <el-dialog
      title="关联图"
      :visible.sync="dialogVisibleRelation"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleRelation"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <RelationGraph
          ref="qcChart"
          :conplanSubject="conplanSubject"
          :conplanProcess="conplanProcess"
        ></RelationGraph>
      </div>
    </el-dialog>

    <el-dialog
      title="直方图"
      :visible.sync="dialogVisibleHistogram"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleHistogram"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <histogram
          ref="qcChart"
          :conplanSubject="conplanSubject"
          :conplanProcess="conplanProcess"
        ></histogram>
      </div>
    </el-dialog>

    <!-- 引用的组件s deliver-->
    <!-- 1、折柱图 -->
    <el-dialog
      :title="this.selectedItem.conplanName"
      :close-on-click-modal="false"
      :visible.sync="dialogVisibleDeliver"
      width="80%"
      v-if="dialogVisibleDeliver"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <line-and-bar-deliver
          ref="qcChart"
          :item="selectedItem"
        ></line-and-bar-deliver>
      </div>
    </el-dialog>

    <!-- 2、控制图 -->
    <el-dialog
      :title="this.selectedItem.conplanName"
      :visible.sync="dialogVisibleControlDeliver"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleControlDeliver"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <control-deliver ref="qcChart" :item="selectedItem"></control-deliver>
      </div>
    </el-dialog>

    <!-- 3、鱼骨图 -->
    <el-dialog
      :title="this.selectedItem.conplanName"
      :close-on-click-modal="false"
      :visible.sync="fishBonedialogVisibleDeliver"
      width="80%"
      v-if="fishBonedialogVisibleDeliver"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <fish-bone-deliver
          ref="fishBone"
          :item="selectedItem"
        ></fish-bone-deliver>
      </div>
    </el-dialog>

    <!-- 4、系统图 -->
    <el-dialog
      :title="this.selectedItem.conplanName"
      :visible.sync="dialogVisibleSystemDeliver"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleSystemDeliver"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <system-deliver ref="qcChart" :item="selectedItem"></system-deliver>
      </div>
    </el-dialog>

    <!-- 5、散点图 -->
    <el-dialog
      :title="this.selectedItem.conplanName"
      :visible.sync="dialogVisibleScatterDeliver"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleScatterDeliver"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <Scatter-deliver ref="qcChart" :item="selectedItem"></Scatter-deliver>
      </div>
    </el-dialog>

    <!-- 6、关联图 -->
    <el-dialog
      :title="this.selectedItem.conplanName"
      :visible.sync="dialogVisibleRelationDeliver"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleRelationDeliver"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <RelationGraph-deliver
          ref="qcChart"
          :item="selectedItem"
        ></RelationGraph-deliver>
      </div>
    </el-dialog>

    <!-- 7、直方图 -->
    <el-dialog
      :title="this.selectedItem.conplanName"
      :visible.sync="dialogVisibleHistogramDeliver"
      :close-on-click-modal="false"
      width="80%"
      v-if="dialogVisibleHistogramDeliver"
      @close="handleDialogClose"
    >
      <div style="width: 100%; height: 100%">
        <histogram-deliver
          ref="qcChart"
          :item="selectedItem"
        ></histogram-deliver>
      </div>
    </el-dialog>

    <!-- Deliver over -->

    <el-dialog title="附件预览" :visible.sync="uploadAllListVisible">
      <div style="color: orange">
        注：若原先存在文件，则点击备注下方的确定后，将会用新上传文件替换原文件
      </div>
      <br />
      <el-table :data="uploadAllList" border style="width: 100%">
        <el-table-column prop="name" label="文件名"> </el-table-column>
        <el-table-column prop="url" label="预览">
          <template slot-scope="scope">
            <el-button v-if="scope.row.url" @click="previewDoc(scope.row.url)"
              >点击预览</el-button
            >
            <span v-else>--</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { FishBones } from "@/components/fishbone/FishBone";
import fishBone from "../../qcTools/fishBone.vue";
import control from "@/views/modules/QCmanagement/qcTools/control.vue";
import histogram from "@/views/modules/QCmanagement/qcTools/histogram.vue";
import RelationGraph from "@/views/modules/QCmanagement/qcTools/RelationGraph.vue";
import Scatter from "@/views/modules/QCmanagement/qcTools/Scatter.vue";
import system from "@/views/modules/QCmanagement/qcTools/system.vue";
import lineAndBar from "@/views/modules/QCmanagement/qcTools/lineAndBar.vue"; //折柱混合图 引用组件

import lineAndBarDeliver from "@/views/modules/QCmanagement/qcToolsDeliver/lineAndBar.vue"; //折柱混合图 引用组件
import fishBoneDeliver from "../../qcToolsDeliver/fishBone.vue";
import controlDeliver from "@/views/modules/QCmanagement/qcToolsDeliver/control.vue";
import histogramDeliver from "@/views/modules/QCmanagement/qcToolsDeliver/histogram.vue";
import RelationGraphDeliver from "@/views/modules/QCmanagement/qcToolsDeliver/RelationGraph.vue";
import ScatterDeliver from "@/views/modules/QCmanagement/qcToolsDeliver/Scatter.vue";
import systemDeliver from "@/views/modules/QCmanagement/qcToolsDeliver/system.vue";

export default {
  components: {
    fishBone,
    control,
    histogram,
    RelationGraph,
    Scatter,
    system,
    lineAndBar,

    lineAndBarDeliver,
    fishBoneDeliver,
    controlDeliver,
    histogramDeliver,
    RelationGraphDeliver,
    ScatterDeliver,
    systemDeliver,
  },
  data() {
    return {
      fileList: [],
      uploadUrl: "",
      uploadAllListVisible: false,
      uploadUrlList: [],
      uploadNameList: [],
      uploadAllList: [],
      tmpAllList: [],
      uploadingFile: "",
      dataForm: {
        stepId: 0,
        stepSubjectId: "",
        stepType: "",
        stepProcess: "",
        stageName: "",
        stagePlanStart: "",
        stagePlanEnd: "",
        stageActualStart: "",
        stageActualEnd: "",
        stagePeople: "",
        stageDescribe: "",
        stageExtra: "",
        stageBefore: "",
        stageAfter: "",
        stageConsolidate: "",
        key: "",
      },
      dialogVisible: false, //折线柱状混合图
      dialogVisibleControl: false, //控制图弹窗
      dialogVisibleSystem: false, //系统图弹窗
      dialogVisibleRelation: false, //关联图弹窗
      dialogVisibleScatter: false, //散点图弹窗
      dialogVisibleHistogram: false, //直方图弹窗
      fishBonedialogVisible: false, //鱼骨图弹窗

      //Deliver
      dialogVisibleDeliver: false, //折线柱状混合图
      dialogVisibleControlDeliver: false, //控制图弹窗
      dialogVisibleSystemDeliver: false, //系统图弹窗
      dialogVisibleRelationDeliver: false, //关联图弹窗
      dialogVisibleScatterDeliver: false, //散点图弹窗
      dialogVisibleHistogramDeliver: false, //直方图弹窗
      fishBonedialogVisibleDeliver: false, //鱼骨图弹窗

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
        stageName: "",
        planDate: "",
        actualDate: "",
        stagePeople: "",
        stageDescribe: "",
        stageBefore: "",
        stageAfter: "",
        stageExtra: "",
        stageConsolidate: "",
        stepFile: "",
        stepAttachment: "",
      },

      participantOptions: [
        { value: "participant1", label: "参与人员1" },
        { value: "participant2", label: "参与人员2" },
      ],

      value: "问题解决型",
      // fileList: [],
      planStepList: [],
      dictWorkFlow: [],
      routerParam: {
        qcsrId: "",
        topicName: "",
        teamNumberIds: "",
        topicLeader: "",
        topicType: "",
      },

      //ref 传递参数
      conplanSubject: 0, // 示例默认数据
      conplanProcess: 0, // 示例默认数据

      //根据 this.conplanSubject和 this.conplanProcess 查询模板数据
      // resultList: [],

      // 数据列表
      conplanDataList: [],
      // 基于conplanType分组的数据
      groupedData: {
        mixChart: [], // 折柱混合图
        controlChart: [], // 控制图
        fishboneChart: [], // 鱼骨图
        systemChart: [], // 系统图
        scatterChart: [], // 散点图
        associationChart: [], // 关联图
        histogram: [], // 直方图
      },

      selectedItem: null, // 用于存储当前选中的 item 数据
    };
  },

  mounted() {},
  activated() {
    this.initRouterParam();
    this.getDictWorkFlowList();
    this.getPlanStepDataList();
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
      return this.dictWorkFlow.filter((item) => item.type === "问题解决型1");
    },
    filteredDataList2() {
      return this.dictWorkFlow.filter((item) => item.type === "问题解决型2");
    },
    filteredDataList3() {
      return this.dictWorkFlow.filter((item) => item.type === "创新型");
    },
  },
  methods: {
    // 监听关闭事件
    handleDialogClose() {
      this.getTemplateData();
    },

    //1119 lbbx 添加控制图
    //根据查询到的控制图数据, 提取其中的conplanType，根据包含的类型，来显示当前页面保存的对应的图标数据
    //conplanType:折柱混合图、控制图、鱼骨图、系统图、散点图、关联图、直方图
    async getTemplateData() {
      await this.$http({
        url: this.$http.adornUrl("/qcTools/conplan/SList"),
        method: "get",
        params: this.$http.adornParams({
          conplanSubject: this.conplanSubject,
          // conplanProcess: this.conplanProcess,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.conplanDataList = data.resultList;
          this.groupDataByType();
        }
      });
    },

    // 按照conplanType对数据进行分类
    groupDataByType() {
      this.groupedData = {
        mixChart: [],
        controlChart: [],
        fishboneChart: [],
        systemChart: [],
        scatterChart: [],
        associationChart: [],
        histogram: [],
      };

      this.conplanDataList.forEach((item) => {
        switch (item.conplanType) {
          case "折柱图":
            this.groupedData.mixChart.push(item);
            break;
          case "控制图":
            this.groupedData.controlChart.push(item);
            break;
          case "鱼骨图":
            this.groupedData.fishboneChart.push(item);
            break;
          case "系统图":
            this.groupedData.systemChart.push(item);
            break;
          case "散点图":
            this.groupedData.scatterChart.push(item);
            break;
          case "关联图":
            this.groupedData.associationChart.push(item);
            break;
          case "直方图":
            this.groupedData.histogram.push(item);
            break;
          default:
            break;
        }
      });
    },

    // click() {
    //   console.log(this.fileList)
    // },
    // 获取数据列表
    async getPlanStepDataList() {
      await this.$http({
        url: this.$http.adornUrl("/qcPlan/step/idList"),
        method: "get",
        params: this.$http.adornParams({
          page: 1,
          limit: 100,
          key: this.routerParam[0].qcsrId,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.planStepList = data.idList;

          this.findMatchingItem(1);
          // this.totalPage = data.page.totalCount
        } else {
          this.planStepList = [];
          // this.totalPage = 0
        }
      });

      //1119 新增 this.conplanSubject和 this.conplanProcess 查询模板数据
      this.getTemplateData();
    },

    initRouterParam() {
      const res = this.$route.query.data
        ? JSON.parse(this.$route.query.data)
        : {
            qcsrId: "",
            topicName: "",
            teamNumberIds: "",
            topicLeader: "",
            topicType: "",
          };
      this.routerParam = res;

      //==================2024.11.11新增调整================================
      // console.log("this.routerParam===xht==>"+ this.routerParam[0].qcsrId);
      //在init的时候 将qcsrId赋值传递
      this.conplanSubject = this.routerParam[0].qcsrId;
      // console.log("this.routerParam===xht==>"+this.routerParam);
      // console.log("this.this.active1 ===xht==>"+this.active1);
      this.conplanProcess = this.active1;
      //==================2024.11.11新增调整================================

      const tmp = this.routerParam[0].teamNumberIds.split(",");
      this.participantOptions = tmp.map((id) => ({
        value: id.trim(),
        label: id.trim(),
      }));
      this.value = this.routerParam[0].topicType;
    },
    chart(id) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    handleClickToStep(id) {
      console.log(this.routerParam);
      if (this.routerParam[0].topicType == "问题解决型") {
        this.active1 = id;
        this.findMatchingItem(this.active1);
      } else if (this.routerParam[0].topicType == "问题解决型(指令型)") {
        this.active2 = id;
        this.findMatchingItem(this.active2);
      } else if (this.routerParam[0].topicType == "创新型") {
        this.active3 = id;
        this.findMatchingItem(this.active3);
      }
    },
    //每步数据查询
    findMatchingItem(id) {
      this.initForm();
      this.uploadAllList = [];
      // console.log('++++++++++++++++')
      for (let item of this.planStepList) {
        if (item.stepProcess === id) {
          this.form.stepId = item.stepId;
          // this.form = item;
          if (item.stageName) {
            this.form.stageName = item.stageName;
          }
          if (item.stagePlanStart) {
            this.form.planDate = [item.stagePlanStart, item.stagePlanEnd];
          }
          if (item.stageActualStart) {
            this.form.actualDate = [item.stageActualStart, item.stageActualEnd];
          }
          if (item.stagePeople) {
            this.form.stagePeople = JSON.parse(item.stagePeople);
          } else {
            if (item.stagePeople !== "null" || item.stagePeople !== "") {
              this.form.stagePeople = [item.stagePeople];
            } else {
              this.form.stagePeople = [];
            }
          }
          if (item.stageDescribe) {
            this.form.stageDescribe = item.stageDescribe;
          }
          if (item.stageBefore) {
            this.form.stageBefore = item.stageBefore;
          }
          if (item.stageAfter) {
            this.form.stageAfter = item.stageAfter;
          }
          if (item.stageConsolidate) {
            this.form.stageConsolidate = item.stageConsolidate;
          }
          if (item.stageAttachment) {
            const tmp = JSON.parse(item.stageAttachment);
            this.uploadAllList = tmp;
            this.form.stepAttachment = item.stageAttachment;
          }
          break;
        }
      }
    },
    getDictWorkFlowList() {
      this.$http({
        url: this.$http.adornUrl("/qcManagement/dictWorkFlow/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          // 'key': this.dataForm.key
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dictWorkFlow = data.page.list;
          this.totalPage = data.page.totalCount;
        } else {
          this.dictWorkFlow = [];
          this.totalPage = 0;
        }
      });
    },

    toggleLineAndBar() {
      this.dialogVisible = !this.dialogVisible;
    },
    // 鱼骨图弹窗
    fishBonetoggleLineAndBar() {
      this.fishBonedialogVisible = !this.fishBonedialogVisible;
    },
    //控制图弹窗
    toggleControl() {
      this.dialogVisibleControl = !this.dialogVisibleControl;
    },
    //系统图弹窗
    toggleSystem() {
      this.dialogVisibleSystem = !this.dialogVisibleSystem;
    },
    //散点图弹窗
    toggleScatter() {
      this.dialogVisibleScatter = !this.dialogVisibleScatter;
    },
    //关联图弹窗
    toggleRelation() {
      this.dialogVisibleRelation = !this.dialogVisibleRelation;
    },
    //直方图弹窗
    toggleHistogram() {
      this.dialogVisibleHistogram = !this.dialogVisibleHistogram;
    },

    //1119 传递参数展示
    toggleLineAndBarShow(item) {
      this.selectedItem = item; // 设置当前选中的 item 数据
      if (item.conplanType === "折柱图") {
        this.dialogVisibleDeliver = !this.dialogVisibleDeliver;
      } else if (item.conplanType === "控制图") {
        this.dialogVisibleControlDeliver = !this.dialogVisibleControlDeliver;
      } else if (item.conplanType === "鱼骨图") {
        this.fishBonedialogVisibleDeliver = !this.fishBonedialogVisibleDeliver;
      } else if (item.conplanType === "系统图") {
        this.dialogVisibleSystemDeliver = !this.dialogVisibleSystemDeliver;
      } else if (item.conplanType === "散点图") {
        this.dialogVisibleScatterDeliver = !this.dialogVisibleScatterDeliver;
      } else if (item.conplanType === "关联图") {
        this.dialogVisibleRelationDeliver = !this.dialogVisibleRelationDeliver;
      } else if (item.conplanType === "直方图") {
        this.dialogVisibleHistogramDeliver =
          !this.dialogVisibleHistogramDeliver;
      }
    },

    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
    //上一步
    lastStep1() {
      if (this.active1 > 1) {
        this.active1--;
        this.conplanProcess = this.active1;
        console.log("this.this.active1 ===xht==>" + this.active1);
      }
      this.findMatchingItem(this.active1);
    },
    //下一步
    nextStep1() {
      if (this.active1 < 10) {
        this.active1++;
        this.conplanProcess = this.active1;
        console.log("this.this.active1 ===xht==>" + this.active1);
      }
      this.findMatchingItem(this.active1);
    },
    lastStep2() {
      if (this.active2 > 1) {
        this.active2--;
        this.conplanProcess = this.active2;
      }
      this.findMatchingItem(this.active2);
    },
    nextStep2() {
      if (this.active2 < 10) {
        this.active2++;
        this.conplanProcess = this.active2;
      }
      this.findMatchingItem(this.active2);
    },
    lastStep3() {
      if (this.active3 > 1) {
        this.active3--;
        this.conplanProcess = this.active3;
      }
      this.findMatchingItem(this.active3);
    },
    nextStep3() {
      if (this.active3 < 8) {
        this.active3++;
        this.conplanProcess = this.active3;
      }
      this.findMatchingItem(this.active3);
    },

    handleRemove(file, fileList) {
      // 移除文件时触发
      console.log(file);
      this.uploadAllList.filter((item) => item.name !== file.name);
      this.tmpAllList.filter((item) => item.name !== file.name);
      console.log(fileList);
    },
    //初始化鱼骨图
    initFishBoneChart() {
      console.log("initFishBone");
      new FishBones({
        id: "fishBone",
        jsonData: this.testFishData,
        canvasSize: [document.body.scrollWidth, document.body.scrollHeight],
        clickNodeFunction: (node, event) => {},
      }).init();
    },
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
      console.log("获取的地址 ", fileflag);
      // 拼接带有 token 的请求地址
      const url = `${this.$http.adornUrl(
        `/generator/issuetable/${fileflag}`
      )}?token=${token}`;
      window.open(url);
    },

    initForm() {
      this.form = {
        stepId: 0,
        stageName: "",
        planDate: "",
        actualDate: "",
        stagePeople: "",
        stageDescribe: "",
        stageBefore: "",
        stageAfter: "",
        stageExtra: "",
        stageConsolidate: "",
      };
    },
    init(id) {
      this.dataForm.stepId = id || 0;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.stepId) {
          this.$http({
            url: this.$http.adornUrl(
              `/qcPlan/step/info/${this.dataForm.stepId}`
            ),
            method: "get",
            params: this.$http.adornParams(),
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.stepSubjectId = data.qcStep.stepSubjectId;
              this.dataForm.stepType = data.qcStep.stepType;
              this.dataForm.stepProcess = data.qcStep.stepProcess;
              this.dataForm.stageName = data.qcStep.stageName;
              this.dataForm.stagePlanStart = data.qcStep.stagePlanStart;
              this.dataForm.stagePlanEnd = data.qcStep.stagePlanEnd;
              this.dataForm.stageActualStart = data.qcStep.stageActualStart;
              this.dataForm.stageActualEnd = data.qcStep.stageActualEnd;
              this.dataForm.stagePeople = data.qcStep.stagePeople;
              this.dataForm.stageDescribe = data.qcStep.stageDescribe;
              this.dataForm.stageExtra = data.qcStep.stageExtra;
              this.dataForm.stageBefore = data.qcStep.stageBefore;
              this.dataForm.stageAfter = data.qcStep.stageAfter;
              this.dataForm.stageConsolidate = data.qcStep.stageConsolidate;
            }
          });
        }
      });
    },
    // 表单提交
    dataFormSubmit(id) {
      let tmpListString = [];
      if (this.tmpAllList.length) {
        tmpListString = JSON.stringify(this.tmpAllList);
      }
      const tmpStagePeople = JSON.stringify(this.form.stagePeople);
      this.$http({
        url: this.$http.adornUrl(
          `/qcPlan/step/${!this.form.stepId ? "save" : "update"}`
        ),
        method: "post",
        data: this.$http.adornData({
          stepId: this.form.stepId || undefined,
          stepSubjectId: this.routerParam[0].qcsrId,
          stepType: this.routerParam[0].topicType,
          stepProcess: id,
          stageName: this.form.stageName ? this.form.stageName : "",
          stagePlanStart: this.form.planDate[0],
          stagePlanEnd: this.form.planDate[1],
          stageActualStart: this.form.actualDate[0]
            ? this.form.actualDate[0]
            : "",
          stageActualEnd: this.form.actualDate[1]
            ? this.form.actualDate[1]
            : "",
          stagePeople: this.form.stagePeople ? tmpStagePeople : "",
          stageDescribe: this.form.stageDescribe || "",
          stageExtra: this.form.stageExtra || "",
          stageBefore: this.form.stageBefore || "",
          stageAfter: this.form.stageAfter || "",
          stageConsolidate: this.form.stageConsolidate || "",
          stageAttachment: this.tmpAllList.length
            ? tmpListString
            : this.form.stepAttachment,
        }),
      })
        .then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.visible = false;
              },
            });
            if (this.routerParam[0].qcsrId) {
              this.$http({
                url: this.$http.adornUrl(`/qcSubject/registration/update`),
                method: "post",
                data: this.$http.adornData({
                  qcsrId: this.routerParam[0].qcsrId,
                  topicReviewStatus: 3,
                  topicActivityStatus: id,
                }),
              });
            }
          } else {
            this.$message.error(data.msg);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.formZone {
  padding: 20px;
}

.upload-btn-group {
  display: inline-block;
  vertical-align: top;
}

.upload-preview-btn {
  margin-left: 10px;
  /* 调整间距以适应你的布局 */
  display: inline-block;
  vertical-align: top;
}
</style>
