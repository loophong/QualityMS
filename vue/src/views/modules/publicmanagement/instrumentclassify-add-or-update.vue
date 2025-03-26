<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="一级分类" prop="primaryCategory">
      <el-input v-model="dataForm.primaryCategory" placeholder="一级分类"></el-input>
    </el-form-item>
    <el-form-item label="二级分类" prop="secondaryCategory">
      <el-input v-model="dataForm.secondaryCategory" placeholder="二级分类"></el-input>
    </el-form-item>
    <el-form-item label="创建人" prop="createdBy">
      <el-input v-model="dataForm.createdBy" placeholder="创建人"></el-input>
    </el-form-item>
    <!-- <el-form-item label="逻辑删除标识 (0: 不删除, 1: 删除)" prop="isDeleted">
      <el-input v-model="dataForm.isDeleted" placeholder="逻辑删除标识 (0: 不删除, 1: 删除)"></el-input>
    </el-form-item>
    <el-form-item label="备用字段1" prop="backupField1">
      <el-input v-model="dataForm.backupField1" placeholder="备用字段1"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createdAt">
      <el-input v-model="dataForm.createdAt" placeholder="创建时间"></el-input>
    </el-form-item> -->
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
        dataForm: {
          id: 0,
          primaryCategory: '',
          secondaryCategory: '',
          createdBy: '',
          isDeleted: '',
          backupField1: '',
          createdAt: ''
        },
        dataRule: {
          primaryCategory: [
            { required: true, message: '一级分类不能为空', trigger: 'blur' }
          ],
          secondaryCategory: [
            { required: true, message: '二级分类不能为空', trigger: 'blur' }
          ],
          createdBy: [
            { required: true, message: '创建人不能为空', trigger: 'blur' }
          ],
          isDeleted: [
            { required: true, message: '逻辑删除标识 (0: 不删除, 1: 删除)不能为空', trigger: 'blur' }
          ],
          backupField1: [
            { required: true, message: '备用字段1不能为空', trigger: 'blur' }
          ],
          createdAt: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/generator/instrumentclassify/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.primaryCategory = data.instrumentClassify.primaryCategory
                this.dataForm.secondaryCategory = data.instrumentClassify.secondaryCategory
                this.dataForm.createdBy = data.instrumentClassify.createdBy
                this.dataForm.isDeleted = data.instrumentClassify.isDeleted
                this.dataForm.backupField1 = data.instrumentClassify.backupField1
                this.dataForm.createdAt = data.instrumentClassify.createdAt
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
              url: this.$http.adornUrl(`/generator/instrumentclassify/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'primaryCategory': this.dataForm.primaryCategory,
                'secondaryCategory': this.dataForm.secondaryCategory,
                'createdBy': this.dataForm.createdBy,
                'isDeleted': this.dataForm.isDeleted,
                'backupField1': this.dataForm.backupField1,
                'createdAt': this.dataForm.createdAt
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
