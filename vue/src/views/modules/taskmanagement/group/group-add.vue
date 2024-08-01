<template>
  <el-dialog :title="!dataForm.tdGid ? '新增小组' : '修改小组'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
      label-width="80px">
      <el-form-item label="组名" prop="groupName">
        <el-input v-model="dataForm.groupName" placeholder="组名"></el-input>
      </el-form-item>
      <el-form-item label="人数" prop="groupNumber">
        <el-input v-model="dataForm.groupNumber" placeholder="人数"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="groupDescription">
        <el-input v-model="dataForm.groupDescription" placeholder="描述"></el-input>
      </el-form-item>
      <!-- 成员列表 -->
      <div v-for="(member, index) in dataForm.members" :key="index">
        <el-form-item :label="'成员 ' + (index + 1)">
          <el-row :gutter="0">
            <el-col :span="16">
              <el-input v-model="member.name" placeholder="成员姓名"></el-input>
              <!-- <div> -->
                <!-- <el-input v-model="member.name" placeholder="成员姓名"> -->
                  <!-- <template #append>
                    <el-button icon="el-icon-search" @click="showSearchWindow"></el-button>
                  </template> -->
                <!-- </el-input> -->
                <!-- <DepartmentSearch :visible="searchWindowVisible" /> -->
              <!-- </div> -->
            </el-col>
            <el-col :span="8">
              <el-button type="danger" @click="removeMember(index)">移除</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </div>
      <!-- 添加成员按钮 -->
      <!-- <el-form-item>
        <el-button type="primary" @click="addMember">添加成员</el-button>
      </el-form-item> -->
    </el-form>
    <span slot="footer" class="dialog-footer">
      <!-- 添加成员按钮 -->
      <!-- <el-form-item> -->
      <el-button type="primary" @click="addMember">添加成员</el-button>
      <!-- </el-form-item> -->
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import DepartmentSearch from './DepartmentSearch.vue';
export default {
  components: {
    DepartmentSearch
  },
  data() {
    return {
      visible: false,
      dataForm: {
        tdGid: 0,
        groupName: '',
        groupNumber: '',
        groupDescription: '',
        members: [{ name: '' }] // 初始化成员列表
      },
      dataRule: {
        groupName: [
          { required: true, message: '组名不能为空', trigger: 'blur' }
        ],
        groupNumber: [
          { required: true, message: '人数不能为空', trigger: 'blur' }
        ],
        groupDescription: [
          { required: true, message: '描述不能为空', trigger: 'blur' }
        ]
      },
      searchWindowVisible: false
    }
  },
  methods: {
    init(id) {
      this.dataForm.tdGid = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.tdGid) {
          this.$http({
            url: this.$http.adornUrl(`/taskmanagement/group/info/${this.dataForm.tdGid}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.groupName = data.taskDataGroup.groupName
              this.dataForm.groupNumber = data.taskDataGroup.groupNumber
              this.dataForm.groupDescription = data.taskDataGroup.groupDescription
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {

          const membersData = this.dataForm.members.map(member => member.name);

          this.$http({
            url: this.$http.adornUrl(`/taskmanagement/group/${!this.dataForm.tdGid ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'tdGid': this.dataForm.tdGid || undefined,
              'groupName': this.dataForm.groupName,
              'groupNumber': this.dataForm.groupNumber,
              'groupDescription': this.dataForm.groupDescription,
              'members': membersData
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
    },



    addMember() {
      this.dataForm.members.push({ name: '' });
    },
    removeMember(index) {
      this.dataForm.members.splice(index, 1);
    },
    showSearchWindow() {
      this.searchWindowVisible = true;
    }
  }
}
</script>


<style scoped>
.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 10px;
  /* 按钮之间的间距 */
}

.department-search {
  position: absolute;
  z-index: 1000;
  background-color: white;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.2);
}
</style>