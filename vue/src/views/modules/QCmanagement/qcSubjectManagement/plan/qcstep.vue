<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-select v-model="which" filterable @change="handleAllSelectChange" placeholder="请选择课题"
        v-if="isAuth('qcManagement:group:admin')">
        <el-option v-for="item in allOptions" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <el-select v-model="which" filterable @change="handleSelectChange" placeholder="请选择课题" v-else>
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <el-form-item>
        <el-button type="" @click="wordExport(form2, dataList)">导出为word</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading" stripe style="width: 100%;">
      <!-- <el-table-column prop="stepId" header-align="center" align="center" label="步骤主键id">
      </el-table-column> -->
      <!-- <el-table-column prop="stepSubjectId" header-align="center" align="center" label="关联课题id">
      </el-table-column> -->
      <el-table-column prop="stepProcess" header-align="center" align="center" label="步骤序号">
      </el-table-column>
      <el-table-column prop="stepType" header-align="center" align="center" label="课题类型">
      </el-table-column>
      <el-table-column prop="stageName" header-align="center" align="center" label="阶段名称">
      </el-table-column>
      <el-table-column prop="stagePlanStart" header-align="center" align="center" label="计划开始时间">
      </el-table-column>
      <el-table-column prop="stagePlanEnd" header-align="center" align="center" label="计划结束时间">
      </el-table-column>
      <el-table-column prop="stageActualStart" header-align="center" align="center" label="实际开始时间">
      </el-table-column>
      <el-table-column prop="stageActualEnd" header-align="center" align="center" label="实际结束时间">
      </el-table-column>
      <el-table-column prop="stagePeople" header-align="center" align="center" label="参与人员">
      </el-table-column>
      <el-table-column prop="stageDescribe" header-align="center" align="center" label="步骤描述">
      </el-table-column>
      <el-table-column prop="stageExtra" header-align="center" align="center" label="备注">
      </el-table-column>
      <el-table-column prop="stageBefore" header-align="center" align="center" label="活动前现状">
      </el-table-column>
      <el-table-column prop="stageAfter" header-align="center" align="center" label="活动后现状">
      </el-table-column>
      <el-table-column prop="stageConsolidate" header-align="center" align="center" label="巩固措施">
      </el-table-column>

      <!-- <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.stepId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.stepId)">删除</el-button>
          <el-button type="text" size="small" @click="wordExport(form2, scope)">导出</el-button>
        </template>
</el-table-column> -->
    </el-table>
    <!-- <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination> -->
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import JSZipUtils from 'jszip-utils'
import JSZip from 'pizzip'
import Docxtemplater from 'docxtemplater'
import AddOrUpdate from './qcstep-add-or-update'
import 'jspdf-autotable';


export default {
  data() {
    return {
      dataForm: {
        key: ''
      },
      which: '',
      name: '',
      dataList: [],
      subjectLeadList: [],
      subjectJoinList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      form: {},
      options: [],
      options2: [],
      allOptions: [],
      form2: {
        topicType: '',
        tableTitle: "过程表",
        topicName: '',
      },
    }
  },
  components: {
    AddOrUpdate
  },
  async activated() {
    // this.getDataList()
    await this.getJoinList()
    await this.getLeadList()
    await this.getAllList()
    console.log(this.options)
    console.log(this.options2)
    //合并两个数组
    this.options = this.options.concat(this.options2)
    console.log(this.options)
  },
  mounted() {

  },
  methods: {
    handleSelectChange() {
      this.name = this.options.find(item => item.value === this.which).label
      this.getDataList()
    },
    handleAllSelectChange() {
      this.name = this.allOptions.find(item => item.value === this.which).label
      this.getDataList()
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/qcPlan/step/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': 1,
          'limit': 1000,
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list.filter(item => item.stepSubjectId === this.which);
          this.dataList.forEach(item => {
            item.stagePeople = JSON.parse(item.stagePeople)
          })

        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
    // 获取所有课题数据列表
    async getAllList() {
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/list"),
        method: "get",
        params: this.$http.adornParams({
          'page': 1,
          'limit': 1000,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.subjectJoinList = data.page.list;
          this.allOptions = data.page.list.map(item => ({
            value: item.qcsrId,
            label: item.topicName
          }))
          console.log(this.allOptions)
        } else {
          this.subjectJoinList = [];
          this.totalPageSubject = 0;
        }
      });
    },
    // 获取我参与课题数据列表
    async getJoinList() {
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/myList"),
        method: "get",
        params: this.$http.adornParams({
          'page': 1,
          'limit': 1000,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.subjectJoinList = data.page.list;
          this.options2 = data.page.list.map(item => ({
            value: item.qcsrId,
            label: item.topicName
          }))
          console.log(this.options2)
        } else {
          this.subjectJoinList = [];
          this.totalPageSubject = 0;
        }
      });
    },
    // 获取我创办课题数据列表
    async getLeadList() {
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/leadList"),
        method: "get",
        params: this.$http.adornParams({
          'page': 1,
          'limit': 1000,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.subjectLeadList = data.page.list;
          this.options = data.page.list.map(item => ({
            value: item.qcsrId,
            label: item.topicName
          }))
          console.log(this.options)
        } else {
          this.subjectLeadList = [];
          this.totalPageSubject = 0;
        }
      });
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },

    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    wordExport(form, table) {
      this.form2.topicType = this.dataList[0].stepType
      this.form2.topicName = this.name
      form = this.form2
      table = this.dataList
      table.forEach(item => {
        // item.stagePeople = JSON.parse(item.stagePeople)
        if (item.stepProcess == 1) {
          item.titleName = '选择课题'
        }
        else if (item.stepProcess == 2) {
          if (item.stepType == '问题解决型') {
            item.titleName = '现状调查'
          } else {
            item.titleName = '设定目标'
          }
        }
        else if (item.stepProcess == 3) {
          if (item.stepType == '创新型') {
            item.titleName = '提出方案确定最佳方案'
          } else if (item.stepType == '问题解决型') {
            item.titleName = '设定目标'
          } else {
            item.titleName = '可靠性分析'
          }
        }
        else if (item.stepProcess == 4) {
          if (item.stepType == '创新型') {
            item.titleName = '指定对策'
          } else {
            item.titleName = '原因分析'
          }
        }
        else if (item.stepProcess == 5) {
          if (item.stepType == '创新型') {
            item.titleName = '对策实施'
          } else {
            item.titleName = '要因确定'
          }
        }
        else if (item.stepProcess == 6) {
          if (item.stepType == '创新型') {
            item.titleName = '检查效果'
          } else {
            item.titleName = '制定对策'
          }
        }
        else if (item.stepProcess == 7) {
          if (item.stepType == '创新型') {
            item.titleName = '标准化'
          } else {
            item.titleName = '对策实施'
          }
        }
        else if (item.stepProcess == 8) {
          item.titleName = '总结和下一步打算'
        }
        else if (item.stepProcess == 9) {
          item.titleName = '巩固措施'
        }
        else {
          item.titleName = '总结和下一步打算'
        }
      })
      console.log(table)
      // const _this = this
      JSZipUtils.getBinaryContent('/static/test.docx', function (error, content) {
        // 抛出异常
        if (error) {
          throw error
        }
        // 创建一个JSZip实例，内容为模板的内容
        let zip = new JSZip(content)
        // 创建并加载docxtemplater实例对象
        let doc = new Docxtemplater().loadZip(zip)
        // 设置模板变量的值
        doc.setData({
          ...form,
          table
        })
        try {
          // 用模板变量的值替换所有模板变量
          doc.render()
        } catch (error) {
          this.$message.error('导出word失败')
          throw error
        }

        // 生成一个代表docxtemplater对象的zip文件（不是一个真实的文件，而是在内存中的表示）
        let out = doc.getZip().generate({
          type: 'blob',
          mimeType:
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        })
        // 将目标文件对象保存为目标类型的文件，并命名
        saveAs(out, form.tableTitle + '.docx')
      })
    },

    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.stepId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/qcPlan/step/delete'),
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
    }
  }
}
</script>
