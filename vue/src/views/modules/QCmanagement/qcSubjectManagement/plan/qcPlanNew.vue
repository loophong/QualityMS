<template>
  <div class="container">
    <div>
      <ul>
        <li v-for="item in this.routerParam" :key="item.qcsrId">
          {{ item.topicName }}
        </li>
      </ul>
    </div>

    <div class="plan1" v-if="value === '1'">
      <br>
      <el-steps :active="active1" align-center>
        <el-step v-for="item in filteredDataList1" :key="item.dictId" :title="`${item.phase}阶段`"
          :description="item.step"></el-step>
      </el-steps>
      <br>
      <br>
      <el-card class="formZone" shadow="hover">
        <div v-if="active1 === 1">
          <br>
          <el-form ref="form" :model="form" label-width="200px">
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
            <!-- 弹窗按钮 -->
            <el-form-item>
              <el-button type="primary" @click="toggleLineAndBar">折柱混合图</el-button>
            </el-form-item>
            <!-- 弹窗 -->
            <el-form-item label="备注">
              <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">提交</el-button>
              <el-button>取消</el-button>
              <el-button :disabled="lastDisabled1">上一步</el-button>
              <el-button :disabled="nextDisabled1">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="active1 === 2">
          <br>
          这是现状调查阶段
        </div>
        <div v-if="active1 === 3">
          <br>
          这是设定目标阶段
        </div>
        <div v-if="active1 === 4">
          <br>
          这是原因分析阶段
        </div>
        <div v-if="active1 === 5">
          <br>
          这是要因确定阶段
        </div>
        <div v-if="active1 === 6">
          <br>
          这是制定对策阶段
        </div>
        <div v-if="active1 === 7">
          <br>
          这是对策实施阶段
        </div>
        <div v-if="active1 === 8">
          <br>
          这是检查效果阶段
        </div>
        <div v-if="active1 === 9">
          <br>
          这是巩固措施阶段
        </div>
        <div v-if="active1 === 10">
          <br>
          这是总结和下一步打算阶段
        </div>
      </el-card>
    </div>
    <div class="plan2" v-if="value === '2'">
      <br>
      <el-steps :active="active2" align-center>
        <el-step title="P阶段" description="选择课题"></el-step>
        <el-step title="P阶段" description="设定目标"></el-step>
        <el-step title="P阶段" description="可靠性分析"></el-step>
        <el-step title="P阶段" description="原因分析"></el-step>
        <el-step title="P阶段" description="要因确定"></el-step>
        <el-step title="P阶段" description="制定对策"></el-step>
        <el-step title="D阶段" description="对策实施"></el-step>
        <el-step title="C阶段" description="检查效果"></el-step>
        <el-step title="A阶段" description="巩固措施"></el-step>
        <el-step title="A阶段" description="总结和下一步打算"></el-step>
      </el-steps>
    </div>
    <div class="plan3" v-if="value === '3'">
      <br>
      <el-steps :active="active3" align-center>
        <el-step title="P阶段" description="选择课题"></el-step>
        <el-step title="P阶段" description="设定目标"></el-step>
        <el-step title="P阶段" description="提出方案并确定最佳方案"></el-step>
        <el-step title="P阶段" description="选制定对策"></el-step>
        <el-step title="D阶段" description="对策实施"></el-step>
        <el-step title="C阶段" description="检查效果"></el-step>
        <el-step title="A阶段" description="标准化"></el-step>
        <el-step title="A阶段" description="总结和下一步打算"></el-step>
      </el-steps>
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
  </div>
</template>

<script>
import * as echarts from 'echarts';
export default {
  data() {
    return {
      dialogVisible: false,
      active1: 1,
      active2: 1,
      active3: 1,
      dataForm: {
        key: ''
      },
      pageIndex: 1,
      pageSize: 50,
      totalPage: 0,
      dataListLoading: false,
      planStyle1: false,
      planStyle2: false,
      planStyle3: false,
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: '',
        participants: []
      },
      participantOptions: [
        { value: 'participant1', label: '参与人员1' },
        { value: 'participant2', label: '参与人员2' },
      ],
      planA1: [{
        value: '1',
        label: '课题1'
      }, {
        value: '2',
        label: '课题2'
      }],
      options: [{
        value: '1',
        label: '问题解决型'
      }, {
        value: '2',
        label: '问题解决型(指令型目标)'
      }, {
        value: '3',
        label: '创新型'
      }],
      value: '1',
      fileList: [],
      dataList: [],
      routerParam: {
        qcsrId: '',
        topicName: '',
        teamNumberIds: '',
        topicLeader: '',
        topicType: '',
      },
    }
  },
  mounted() {
    this.routerParam = this.$route.query.data ? JSON.parse(this.$route.query.data) : { qcsrId: '', topicName: '', teamNumberIds: '', topicLeader: '', topicType: '' };
  },
  activated() {
    this.getDataList()
  },
  computed: {
    lastDisabled1() {
      return this.active1 === 1;
    },
    nextDisabled1() {
      return this.active1 === 10;
    },
    filteredDataList1() {
      return this.dataList.filter(item => item.type === '问题解决型1');
    },
    filteredDataList2() {
      return this.dataList.filter(item => item.type === '问题解决型2');
    },
    filteredDataList3() {
      return this.dataList.filter(item => item.type === '创新型');
    },
  },
  methods: {
    ensureName() {
      if (this.dataList === '问题解决型1')
        this.planStyle1 = true;
      else if (this.dataList === '问题解决型2')
        this.planStyle2 = true;
      else if (this.dataList === '创新型')
        this.planStyle3 = true;
    },
    chart(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
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
      this.fileList = fileList;
    },
    handlePreview(file) {
      console.log('预览文件:', file);
    },
    handleRemove(file, fileList) {
      console.log('移除文件:', file);
      this.fileList = fileList;
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
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => { });
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
  }
}
</script>

<style lang="scss" scoped>
.formZone {
  padding: 20px;
}
</style>
