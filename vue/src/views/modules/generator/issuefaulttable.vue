<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <!-- 选择器 -->
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
      <el-form-item>
        <el-button @click="getQueryList()">查询</el-button>
        <el-button v-if="isAuth('generator:issuefaulttable:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('generator:issuefaulttable:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
      <el-form-item>
        <el-upload
          class="upload-demo"
          :before-upload="beforeUpload"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError">
          <el-button size="small" type="primary">上传故障树</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      @sort-change="sortChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="faultId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="ID"-->
<!--        sortable="custom">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="systematicClassification"
        header-align="center"
        align="center"
        label="系统分类">
      </el-table-column>
      <el-table-column
        prop="firstFaultyParts"
        header-align="center"
        align="center"
        label="故障件一级">
      </el-table-column>
      <el-table-column
        prop="secondFaultyParts"
        header-align="center"
        align="center"
        label="故障件二级">
      </el-table-column>
      <el-table-column
        prop="faultType"
        header-align="center"
        align="center"
        label="故障类别">
      </el-table-column>
      <el-table-column
        prop="faultModel"
        header-align="center"
        align="center"
        label="故障模式">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.faultId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.faultId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>

</template>

<script>
  import AddOrUpdate from './issuefaulttable-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
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
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
      this.fetchFaultOptions()
    },
    methods: {
      sortChangeHandle({ prop, order }) {
        // 根据排序字段 `prop` 和顺序 `order` 排序
        if (prop === 'faultId') {
          this.dataList.sort((a, b) => {
            if (order === 'ascending') {
              return a.faultId - b.faultId;
            } else if (order === 'descending') {
              return b.faultId - a.faultId;
            }
            return 0;
          });
        }
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
          url: this.$http.adornUrl('/generator/issuefaulttable/uploadExcel'),
          method: 'post',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data' // 重要：设置请求头
          }
        }).then(({data}) => {
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
      // 批量去重所有数组
      deduplicateAll() {
        // this.filteredIssueCategoryOptions = this.removeDuplicates(this.filteredIssueCategoryOptions, 'value');
        this.filteredSystematicOptions = this.removeDuplicates(this.filteredSystematicOptions, 'value');
        this.filteredFirstFaultyPartsOptions = this.removeDuplicates(this.filteredFirstFaultyPartsOptions, 'value');
        this.filteredSecondFaultyPartsOptions = this.removeDuplicates(this.filteredSecondFaultyPartsOptions, 'value');
        this.filteredFaultTypeOptions = this.removeDuplicates(this.filteredFaultTypeOptions, 'value');
        this.filteredFaultModelOptions = this.removeDuplicates(this.filteredFaultModelOptions, 'value');
      },
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
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/generator/issuefaulttable/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
          })
        }).then(({data}) => {
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
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      getQueryList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/generator/issuefaulttable/Querylist'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'selectedSystematic': this.selectedSystematic,
            'selectedFirstFaultyParts': this.selectedFirstFaultyParts,
            'selectedSecondFaultyParts': this.selectedSecondFaultyParts,
            'selectedFaultType': this.selectedFaultType,
            'selectedFaultModel': this.selectedFaultModel,
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list.map(item => {
              // 确保图片路径有效
              // if (!item.issuePhoto || !this.isValidImageUrl(item.issuePhoto)) {
              //   // item.issuePhoto = '默认图片路径' // 设置默认图片路径
              // }
              return item
            })
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.faultId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/generator/issuefaulttable/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
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
