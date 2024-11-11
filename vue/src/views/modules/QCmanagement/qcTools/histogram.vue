<template>
  <div>
    <span>
      <el-select v-model="value" filterable @change="handleSelectChange" placeholder="请选择模版">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <el-button type="danger" @click="handleDelete">删除当前模版</el-button>
    </span>
    <div class="container">
      <div ref="chart" style="width: 900px; height: 500px;"></div>
      <div class="input-group">
        <span>
          <input v-model="newCategory" placeholder="请输入横坐标数据" />
          <input v-model="newValue" type="text" placeholder="请输入纵坐标数据" />
        </span>
      </div>
      <br>
      <div>
        <el-button @click="addData">添加数据</el-button>
        <el-button @click="removeData">删除数据</el-button>
        <el-button @click="modifyData">修改数据</el-button>
        <el-button @click="restoreData">恢复数据</el-button>
        <el-button type="success" @click="dialogFormVisible = true">保存为模版</el-button>
      </div>
    </div>
    <el-dialog title="模版名" :visible.sync="dialogFormVisible">
      <el-input v-model="inputName" placeholder="请输入模版名" style="width:50%"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleUp">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'SimpleHistogram',
  data() {
    return {
      myChart: null,
      categories: ['0', '5', '10', '15', '20', '25'],
      data: [5, 10, 25, 30, 20, 10],
      newCategory: '',
      newValue: '',
      inputName: '',
      value: '',
      name: '',//当前模版名
      dialogFormVisible: false,
      totalFrequency: 0,
      weightedMean: 0,
      resultList: [],
      standardDeviation: 0,
      options: [],//下拉框选择
      history: [], // 记录历史状态
      editingCategory: null // 记录当前编辑的横坐标
    };
  },
  mounted() {
    this.getTemplateData()
    this.drawChart();
    this.updateStatistics(); // 计算初始统计数据
    this.updateChart(); // 更新图表以显示初始数据
  },
  methods: {
    //处理下拉框选择变化
    handleSelectChange() {
      console.log(this.resultList)
      this.resultList.forEach(item => {
        if (item.templateId == this.value) {
          this.data = item.templateSeries
          this.categories = item.templateAxis
          this.name = item.templateName
        }
      })
      this.updateChart()

    },
    async getTemplateData() {
      await this.$http({
        url: this.$http.adornUrl('/qcTools/template/templateList'),
        method: 'get',
        params: this.$http.adornParams({
          'templateType': '直方图',
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.resultList = data.resultList.map(row => ({
            templateId: row.templateId,
            templateName: row.templateName,
            templateType: row.templateType,
            templateText: row.templateText,
            templateSeries: JSON.parse(row.templateSeries),
            templateAxis: JSON.parse(row.templateAxis),
          }))
          this.options = data.resultList.map(item => ({
            value: item.templateId,
            label: item.templateName
          }))
          console.log(this.resultList)
        } else {
          this.options = []
        }
      })
    },
    handleUp() {
      console.log(this.updatedSeries)
      console.log(this.test)
      this.dialogFormVisible = false
      this.addTemplate()
    },
    //删除当前模版
    handleDelete() {
      let ids = [this.value]
      console.log(ids)
      if (ids) {
        this.$confirm(`确定对 [${this.name}] 进行删除?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/qcTools/template/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.value = ''
                  this.name = ''
                  this.getTemplateData()
                  this.updateChart()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      } else {
        this.$message({
          message: '未选择',
          type: 'info',
          duration: 1500,

        })
      }

    },
    addTemplate() {

      this.$http({
        url: this.$http.adornUrl(`/qcTools/template/save`),
        method: 'post',
        data: this.$http.adornData({
          'templateId': undefined,
          'templateName': this.inputName || '未命名',
          'templateType': '直方图',
          'templateText': this.textBy || '未命名',
          'templateSeries': JSON.stringify(this.data),
          'templateAxis': JSON.stringify(this.categories),
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.getTemplateData()
            }
          })
        } else {
          this.$message.error(data.msg)
        }
      })
    },
    drawChart() {
      const chartDom = this.$refs.chart;
      this.myChart = echarts.init(chartDom);
      this.updateChart();
    },
    updateChart() {
      this.updateStatistics();
      const option = {
        tooltip: {},
        toolbox: {
          show: true,
          orient: 'vertical',
          left: 'right',
          top: 'center',
          feature: {
            mark: { show: true, },
            // restore: { show: true, title: '重置' },
            saveAsImage: { show: true, title: '保存' }
          }
        },
        xAxis: {
          type: 'category',
          data: this.categories,
          axisLine: {
            onZero: false
          }
        },
        yAxis: {
          name: '频数',
          type: 'value'
        },
        series: [
          {
            name: '频数',
            type: 'bar',
            data: this.data,
            barWidth: '60%',
            itemStyle: {
              color: '#ffffff',
              borderColor: '#000',
              borderWidth: 1
            }
          }
        ],
        graphic: {
          elements: [
            {
              type: 'text',
              left: '',
              top: '2%', // 调整位置
              style: {
                text: `n=${this.totalFrequency}\nx̄=${this.weightedMean}\ns=${this.standardDeviation}`,
                font: '14px sans-serif',
                fill: '#000'
              },
              z: 10
            }
          ]
        }
      };
      this.myChart.setOption(option);
    },
    addData() {
      const value = Number(this.newValue);
      const index = this.categories.indexOf(this.newCategory);

      if (this.newCategory && !isNaN(value)) {
        this.history.push({ categories: [...this.categories], data: [...this.data] }); // 记录历史
        if (index !== -1) {
          this.data[index] += value; // 累加频数
        } else {
          this.categories.push(this.newCategory);
          this.data.push(value);
        }

        const combined = this.categories.map((category, i) => ({ category: Number(category), value: this.data[i] }));
        combined.sort((a, b) => a.category - b.category);
        this.categories = combined.map(item => String(item.category));
        this.data = combined.map(item => item.value);

        this.newCategory = '';
        this.newValue = '';
        this.updateStatistics();
        this.updateChart();
        this.$message({
          message: '新增成功',
          type: 'success',
          duration: 1500,
        }); // 添加成功提示
      } else {
        alert('请确保横坐标类目非空，纵坐标值为有效数字！');
      }
    },
    removeData() {
      if (this.newCategory) {
        const index = this.categories.indexOf(this.newCategory);

        if (index !== -1) {
          this.history.push({ categories: [...this.categories], data: [...this.data] }); // 记录历史
          this.categories.splice(index, 1); // 删除对应的横坐标
          this.data.splice(index, 1); // 删除对应的频数
          this.updateStatistics();
          this.updateChart();
          this.newCategory = ''; // 清空输入框
          this.$message({
            message: '删除成功',
            type: 'success',
            duration: 1500,
          }) // 删除成功提示
        } else {
          alert('没有找到该横坐标数据！');
        }
      } else {
        alert('请输入要删除的横坐标类目！');
      }
    },
    modifyData() {
      const value = Number(this.newValue);
      const index = this.categories.indexOf(this.newCategory);

      if (this.newCategory && !isNaN(value)) {
        if (index !== -1) {
          // 直接修改纵坐标
          this.history.push({ categories: [...this.categories], data: [...this.data] }); // 记录历史
          this.data[index] = value; // 修改频数
          this.updateStatistics();
          this.updateChart();
          this.newValue = ''; // 清空纵坐标输入框
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1500,
          });
        } else {
          alert('没有找到该横坐标数据！');
        }
      } else {
        alert('请确保横坐标类目非空，纵坐标值为有效数字！');
      }
    },
    restoreData() {
      if (this.history.length > 0) {
        const lastState = this.history.pop();
        this.categories = lastState.categories;
        this.data = lastState.data;
        this.updateStatistics();
        this.updateChart();
        alert('恢复成功！'); // 恢复成功提示
      } else {
        alert('没有可恢复的历史记录！');
      }
    },

    updateStatistics() {
      // 计算总频数
      this.totalFrequency = this.data.reduce((sum, freq) => sum + freq, 0);

      // 计算加权平均
      const totalWeightedValue = this.categories.reduce((sum, category, index) => {
        return sum + (Number(category) * this.data[index]);
      }, 0);
      this.weightedMean = this.totalFrequency ? (totalWeightedValue / this.totalFrequency).toFixed(2) : 0;

      // 计算标准差
      const variance = this.totalFrequency > 1
        ? (this.data.reduce((sum, freq, index) => {
          const value = Number(this.categories[index]);
          return sum + (freq * Math.pow(value - this.weightedMean, 2));
        }, 0) / this.totalFrequency)
        : 0;

      this.standardDeviation = Math.sqrt(variance).toFixed(2); // 计算标准差
    }
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;

}

.input-group {
  display: flex;
  align-items: center;
}
</style>