<template>
  <el-dialog :title="!dataForm.stepId ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="80px">
      <el-form-item label="关联课题id" prop="stepSubjectId">
        <el-input v-model="dataForm.stepSubjectId" placeholder="关联课题id"></el-input>
      </el-form-item>
      <el-form-item label="课题类型" prop="stepType">
        <el-input v-model="dataForm.stepType" placeholder="课题类型"></el-input>
      </el-form-item>
      <el-form-item label="步骤id" prop="stepProcess">
        <el-input v-model="dataForm.stepProcess" placeholder="步骤id"></el-input>
      </el-form-item>
      <el-form-item label="阶段名称" prop="stageName">
        <el-input v-model="dataForm.stageName" placeholder="阶段名称"></el-input>
      </el-form-item>
      <el-form-item label="计划开始时间" prop="stagePlanStart">
        <el-input v-model="dataForm.stagePlanStart" placeholder="计划开始时间"></el-input>
      </el-form-item>
      <el-form-item label="计划结束时间" prop="stagePlanEnd">
        <el-input v-model="dataForm.stagePlanEnd" placeholder="计划结束时间"></el-input>
      </el-form-item>
      <el-form-item label="实际开始时间" prop="stageActualStart">
        <el-input v-model="dataForm.stageActualStart" placeholder="实际开始时间"></el-input>
      </el-form-item>
      <el-form-item label="实际结束时间" prop="stageActualEnd">
        <el-input v-model="dataForm.stageActualEnd" placeholder="实际结束时间"></el-input>
      </el-form-item>
      <el-form-item label="参与人员" prop="stagePeople">
        <el-input v-model="dataForm.stagePeople" placeholder="参与人员"></el-input>
      </el-form-item>
      <el-form-item label="步骤描述" prop="stageDescribe">
        <el-input v-model="dataForm.stageDescribe" placeholder="步骤描述"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="stageExtra">
        <el-input v-model="dataForm.stageExtra" placeholder="备注"></el-input>
      </el-form-item>
      <el-form-item label="活动前现状" prop="stageBefore">
        <el-input v-model="dataForm.stageBefore" placeholder="活动前现状"></el-input>
      </el-form-item>
      <el-form-item label="活动后现状" prop="stageAfter">
        <el-input v-model="dataForm.stageAfter" placeholder="活动后现状"></el-input>
      </el-form-item>
      <el-form-item label="巩固措施" prop="stageConsolidate">
        <el-input v-model="dataForm.stageConsolidate" placeholder="巩固措施"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      dataForm: {
        stepId: 0,
        stepSubjectId: '1',
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
        stageConsolidate: ''
      },
      dataRule: {

      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.stepId = id || 0
      this.visible = true
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
    exportForm() {
      const form = this.dataForm;
      let tableData = [];
      let titles = [];

      // 遍历表单元素
      form.$children.forEach((item) => {
        if (item.$options._componentTag === 'el-input') {
          tableData.push({
            name: item.$attrs.title,
            value: item.$attrs.value || item.$attrs.modelValue
          });
          titles.push(item.$attrs.title);
        } else if (item.$options._componentTag === 'el-input-number') {
          tableData.push({
            name: item.$attrs.title,
            value: item.$attrs.value || item.$attrs.modelValue
          });
          titles.push(item.$attrs.title);
        } else if (item.$options._componentTag === 'el-radio-group') {
          tableData.push({
            name: item.$attrs.title,
            value: item.$attrs.value || item.$attrs.modelValue
          });
          titles.push(item.$attrs.title);
        } else if (item.$options._componentTag === 'el-checkbox-group') {
          const values = item.$attrs.value || item.$attrs.modelValue;
          if (values && values.length > 0) {
            tableData.push({
              name: item.$attrs.title,
              value: values.join(', ')
            });
            titles.push(item.$attrs.title);
          }
        } else if (item.$options._componentTag === 'el-select') {
          tableData.push({
            name: item.$attrs.title,
            value: item.$attrs.value || item.$attrs.modelValue
          });
          titles.push(item.$attrs.title);
        }
      });

      // 构建表格HTML
      let tr = '';
      tableData.forEach((item) => {
        tr += `<tr><td style="padding:5px 10px;background:#efefef;">${item.name}</td><td style="padding:5px 10px;">${item.value}</td></tr>`;
      });
      const htmls = `<table style="width:500px;margin:0 auto;border:1px solid #efefef" border="0" cellspacing="0" cellpadding="0">${tr}</table>`;

      // 导出HTML
      this.exportHtmls(htmls, 'doc', '人员信息.doc', '人员信息');
    },
    exportHtmls(htmls, type, fileName, title) {
      let a = document.createElement('a');
      let mimeType = '';
      let fileExtension = '';

      switch (type) {
        case 'doc':
          mimeType = 'application/msword';
          fileExtension = '.doc';
          break;
        case 'xls':
          mimeType = 'application/vnd.ms-excel';
          fileExtension = '.xls';
          break;
      }

      const fullHtml = `<html><head><meta charset='utf-8' /></head><body><h1 style='text-align:center'>${title}</h1>${htmls}</body></html>`;
      const blob = new Blob([fullHtml], { type: mimeType });

      a.href = URL.createObjectURL(blob);
      a.download = fileName + fileExtension;
      document.querySelector('body').appendChild(a);
      a.click();
      a.remove();
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/qcPlan/step/${!this.dataForm.stepId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'stepId': this.dataForm.stepId || undefined,
              'stepSubjectId': this.dataForm.stepSubjectId,
              'stepType': this.dataForm.stepType,
              'stepProcess': this.dataForm.stepProcess,
              'stageName': this.dataForm.stageName,
              'stagePlanStart': this.dataForm.stagePlanStart,
              'stagePlanEnd': this.dataForm.stagePlanEnd,
              'stageActualStart': this.dataForm.stageActualStart,
              'stageActualEnd': this.dataForm.stageActualEnd,
              'stagePeople': this.dataForm.stagePeople,
              'stageDescribe': this.dataForm.stageDescribe,
              'stageExtra': this.dataForm.stageExtra,
              'stageBefore': this.dataForm.stageBefore,
              'stageAfter': this.dataForm.stageAfter,
              'stageConsolidate': this.dataForm.stageConsolidate
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
    }
  }
}
</script>
