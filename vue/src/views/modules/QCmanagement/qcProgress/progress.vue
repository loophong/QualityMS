<template>
  <div class="container">
    <div class="progress">
      <br>
      <el-steps :active="activeStep" align-center>
        <el-step title="标准化阶段" description=""></el-step>
        <el-step title="总结及下一步打算阶段" description=""></el-step>
        <el-step title="科室审核" description=""></el-step>
        <el-step title="初评" description=""></el-step>
        <el-step title="财务审核" description=""></el-step>
        <el-step title="终评" description=""></el-step>
      </el-steps>
      <br>
      <br>
      <el-card class="formZone" shadow="hover">
        <div v-if="activeStep === 1">
          <br>
          <el-form ref="form" :model="form" label-width="100px">
            <!-- <el-form-item label="预计完成时间">
              <el-date-picker clearable v-model="form.participationDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item> -->
            <!-- <el-form-item label="选择审批人">
              <el-checkbox-group v-model="form.type">
                <el-checkbox label="1" name="type"></el-checkbox>
                <el-checkbox label="2" name="type"></el-checkbox>
              </el-checkbox-group>
            </el-form-item> -->
            <el-form-item label="上传附件">
              <el-upload class="upload-demo" action="url" :file-list="fileList" :auto-upload="false"
                :on-change="handleChange" :on-preview="handlePreview" :on-remove="handleRemove">
                <el-button size="small" type="primary">点击上传</el-button>
                <!-- <div slot="tip" class="el-upload__tip">只能上传doc、pdf文件，且不超过10MB</div> -->
              </el-upload>
            </el-form-item>
            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">提交</el-button>
              <el-button>取消</el-button>
              <el-button>上一步</el-button>
              <el-button>下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="activeStep === 2">
          <br>
          这是现状调查阶段
        </div>
        <div v-if="activeStep === 3">
          <br>
          这是设定目标阶段
        </div>
        <div v-if="activeStep === 4">
          <br>
          这是原因分析阶段
        </div>
        <div v-if="activeStep === 5">
          <br>
          这是要因确定阶段
        </div>
        <div v-if="activeStep === 6">
          <br>
          这是制定对策阶段
        </div>
        <div v-if="activeStep === 7">
          <br>
          这是对策实施阶段
        </div>
        <div v-if="activeStep === 8">
          <br>
          这是检查效果阶段
        </div>
        <div v-if="activeStep === 9">
          <br>
          这是巩固措施阶段
        </div>
        <div v-if="activeStep === 10">
          <br>
          这是总结和下一步打算阶段
        </div>
      </el-card>
    </div>
  </div>

</template>


<script>

export default {
  data() {
    return {
      activeStep: 1,

      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      },
      dataList: [],
      fileList: []
    }
  },
  activated() {
    this.getDataList()
  },
  methods: {

    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/qcManagement/dictWorkFlow/list'),
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

    handleChange(file, fileList) {
      // 处理文件列表变化
      this.fileList = fileList;
    },
    handlePreview(file) {
      // 预览文件
      console.log('预览文件:', file);
    },
    handleRemove(file, fileList) {
      // 移除文件
      console.log('移除文件:', file);
      this.fileList = fileList;
    },
    onSubmit() {
      // 提交表单时触发上传
      // 在这里处理文件上传逻辑，可以使用axios或其他方法将文件上传到服务器
      console.log('提交表单，上传文件:', this.fileList);
    }

  }




}
</script>


<style lang="scss" scoped>
.formZone {
  padding: 20px;
}
</style>