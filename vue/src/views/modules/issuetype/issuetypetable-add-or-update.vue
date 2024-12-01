<template>
  <el-dialog
    :title="!dataForm.issueCategoryId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
<!--      <el-form-item label="问题等级" prop="issueGrade">-->
<!--        <el-input v-model="dataForm.issueGrade" placeholder="问题等级"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="问题类别" prop="issueGrade">
        <el-select v-model="dataForm.issueGrade" filterable placeholder="请选择问题等级">
          <el-option
            v-for="grade in issueGrades"
            :key="grade"
            :label="grade"
            :value="grade">
          </el-option>
        </el-select>
      </el-form-item>
    <el-form-item label="问题类别" prop="concreteIssueCategory">
      <el-input v-model="dataForm.concreteIssueCategory" placeholder="具体问题类别"></el-input>
    </el-form-item>
<!--      <el-form-item label="等级说明" prop="gradeIllustrate">-->
<!--        <el-input v-model="dataForm.gradeIllustrate" placeholder="等级说明"></el-input>-->
<!--      </el-form-item>-->
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        issueGrades: [],  // 存储所有唯一的问题等级

        dataForm: {
          issueCategoryId: 0,
          issueGrade: '',
          concreteIssueCategory: '',
          gradeIllustrate: '',
        },
        dataRule: {
          concreteIssueCategory: [
            { required: true, message: '具体问题类别不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created () {
      this.fetchIssueCategories()
    },
    methods: {
      fetchIssueCategories() {
        this.$http({
          url: this.$http.adornUrl('/generator/issuetypegradetable/issuesgrade'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          if (data && data.code === 0) {
            console.log('Successfully fetched issue categories:', data.category);

            // 获取唯一的等级选项
            this.issueGrades = [...new Set(data.category.map(category => category.grade))];
            console.log('Successfully fetched issue issueGrades:', this.issueGrades);

            // 去重
            // this.deduplicateAll();
          } else {
            console.error('Failed to fetch issue categories:', data.msg);
          }
        }).catch(error => {
          console.error('There was an error fetching the issue categories!', error);
        });
      },
      init (id) {
        this.dataForm.issueCategoryId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.issueCategoryId) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetypetable/info/${this.dataForm.issueCategoryId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.issueGrade = data.issueTypeTable.issueGrade
                this.dataForm.gradeIllustrate = data.issueTypeTable.gradeIllustrate
                this.dataForm.concreteIssueCategory = data.issueTypeTable.concreteIssueCategory
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/generator/issuetypetable/${!this.dataForm.issueCategoryId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'issueCategoryId': this.dataForm.issueCategoryId || undefined,
                'issueGrade': this.dataForm.issueGrade,
                'gradeIllustrate': this.dataForm.gradeIllustrate,
                'concreteIssueCategory': this.dataForm.concreteIssueCategory
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
      }
    }
  }
</script>
