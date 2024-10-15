<template>
    <div class="mod-config">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="USL" placeholder="上限 USL" clearable></el-input>
          <el-input v-model="CL" placeholder="中心限CL" clearable></el-input>
          <el-input v-model="LSL" placeholder="下限 LSL" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            plain
            @click="showDialog = true"
            >导入Excel文件</el-button>

        <el-dialog
          title="导入Excel文件"
          :visible.sync="showDialog"
          width="30%"
          :before-close="handleClose"
          @close="resetFileInput"
        >
          <i class="el-icon-upload"></i>
          <input type="file" id="inputFile" ref="fileInput" @change="checkFile" />

          <!-- 进度动画条 -->
          <div v-if="progress > 0">
            <el-progress
              :percentage="progress"
              color="rgb(19, 194, 194)"
            ></el-progress>
          </div>

          <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="fileSend()">确 定</el-button>
        </span>
        </el-dialog>
        </el-form-item>
      </el-form>


    <div ref="lineChart1" style="width: 1500px;height:300px;"></div>
    <div ref="lineChart2" style="width: 1500px;height:300px;"></div>
    <div ref="lineChart3" style="width: 1500px;height:300px;"></div>
    </div>
  </template>
  
  <script>
  
  import * as echarts from "echarts";
  import * as utils from './utils';

  export default {

    name: 'MultipleCharts',
    data() {
      return {
        dataForm: {
          key: ''
        },
        dataList: [],
        groupList: [],
        originList: [],
        tableData: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        map: {},

        //新增参数
        showDialog: false,
        progress: 0,
        chartInstance1: null,
        chartInstance2: null,
        chartInstance3: null,
        responseData: null,

        USL: null,
        CL: null,
        LSL: null,
        defaultData: [[1, 2, 3, 4, 5], [5, 4, 3, 2, 1]],

      }
    },


    mounted() {
    this.initChart1();
    this.initChart2();
    this.initChart3();
    this.setDefaultChartOptions();
    },


    methods: {

        setDefaultChartOptions() {
            this.updateChart(this.defaultData[0]);
            this.updateChart2(this.defaultData[1]);
            },

        initChart1() {
            if (this.$refs.lineChart1) {
                this.chartInstance1 = echarts.init(this.$refs.lineChart1);
                this.chartInstance1.setOption({ // 设置默认的图表配置
                title: {
                    text: 'X'
                },
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                },
                yAxis: {
                    type: 'value'
                },
                series: []
                });
            }
        },
        initChart2() {
            if (this.$refs.lineChart2) {
                this.chartInstance2 = echarts.init(this.$refs.lineChart2);
                this.chartInstance2.setOption({ // 设置默认的图表配置
                title: {
                    text: 'R'
                },
                tooltip: {},
                xAxis: {
                    type: 'category'
                },
                yAxis: {
                    type: 'value'
                },
                series: []
                });
            }
        },
        initChart3() {
            if (this.$refs.lineChart3) {
                this.chartInstance3 = echarts.init(this.$refs.lineChart3);
                this.chartInstance3.setOption({ // 设置默认的图表配置
                title: {
                    text: 'P Chart'
                },
                tooltip: {},
                xAxis: {
                    type: 'category'
                },
                yAxis: {
                    type: 'value'
                },
                series: []
                });
            }
        },

    /*  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  */
        fileSend() {
            const formData = new FormData();
            const file = document.getElementById("inputFile").files[0]; // 获取文件对象
            if (file) {
                formData.append("file", file);

                this.$http({
                    url: this.$http.adornUrl('/SPC/spc/list'),
                    method: 'post',
                    data: formData,
                    headers: {
                        'Content-Type': 'multipart/form-data' // 设置正确的请求头
                    }
                }).then(response => {
                    // response.data 包含了从服务器返回的数据
                    this.responseData = response.data;
                    console.log("Response data:", this.responseData);
                    
                    // 初始化图表
                    this.updateChart();
                    this.updateChart2();
                    this.updateChart3();

                    // 2秒后关闭上传面板，这里应该根据实际情况来决定是否需要刷新页面
                    setTimeout(() => {
                        this.showDialog = false; // 关闭上传面板
                        // 如果需要刷新页面，取消注释下面的代码
                        // location.reload();
                    }, 2000); // 2000毫秒后执行
                }).catch(error => {
                    // 处理错误
                    console.error('Error uploading file:', error);
                });
            } else {
                console.error('No file selected.');
            }
        },

        updateChart() {


            const firstSeriesData = this.responseData[1];
            const xAxisData = Array.from({ length: firstSeriesData.length }, (_, index) => index);

            const option = {
                title: {
                    text: 'X'
                },
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: xAxisData
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    name: 'X',
                    data: firstSeriesData,
                    type: 'line'
                }]
                };

                if (this.chartInstance1) {
                    this.chartInstance1.setOption(option);
                }
        },

        updateChart2() {
        if (this.responseData && this.responseData.length > 1 && this.chartInstance2) {
            const secondSeriesData = this.responseData[2];
            const xAxisData = Array.from({ length: secondSeriesData.length }, (_, index) => index);
            const option = {
            title: {
                text: 'R'
            },
            tooltip: {
                trigger: 'axis'
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: xAxisData
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                name: 'R',
                data: secondSeriesData,
                type: 'line'
            }]
            };
            if (this.chartInstance2) {
                this.chartInstance2.setOption(option);
            }
        }
        },

        updateChart3() {

            const secondSeriesData3 = this.responseData[3];
            const secondSeriesData4 = this.responseData[4];
            const secondSeriesData5 = this.responseData[5];
            const secondSeriesData6 = this.responseData[6];
            const xAxisData = Array.from({ length: secondSeriesData3.length }, (_, index) => index);
            const option = {
            legend: {
                data: ['不良率', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'R'
            },
            tooltip: {
                trigger: 'axis'
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: xAxisData
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                name: '不良率',
                data: secondSeriesData3,
                type: 'line'
            },
            {
                name: 'UCL',
                data: secondSeriesData4,
                type: 'line'
            },
            {
                name: 'CL',
                data: secondSeriesData5,
                type: 'line'
            },
            {
                name: 'LCL',
                data: secondSeriesData6,
                type: 'line'
            }
            ]
            };
            if (this.chartInstance3) {
                this.chartInstance3.setOption(option);
            }
        
        },


        handleClose(done) {
        this.$confirm('确认关闭？')
            .then(_ => {
            done();
            })
            .catch(_ => {});
        },
        //导入excel，取消按钮绑定取消所选的xlsx
        resetFileInput() {
        this.$refs.fileInput.value = "";
        },
        //检查文件是否为excel
        checkFile() {
        const file = this.$refs.fileInput.files[0];
        const fileName = file.name;
        const fileExt = fileName.split(".").pop(); // 获取文件的扩展名

        if (fileExt !== "xlsx" && fileExt !== "xlsm") {
            this.$message.error("只能上传 Excel 文件！");
            this.$refs.fileInput.value = ""; // 清空文件选择框
        }
        },




        }
    }
  </script>
  