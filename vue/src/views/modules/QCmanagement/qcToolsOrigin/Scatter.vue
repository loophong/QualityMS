<template>
    <div>
        <span>
            <el-select v-model="value" filterable @change="handleSelectChange" placeholder="请选择模版">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
            </el-select>
            <el-button type="danger" @click="handleDelete">删除当前模版</el-button>
        </span>
        <span>
            <el-select v-model="valueConPlan" filterable @change="handleSelectChangeConPlan" placeholder="请选择实例">
                <el-option v-for="item in optionsConPlan" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
            </el-select>
        </span>
        <div class="content">
            <div class="chart-container">
                <div id="chartSdtV" style="width: 40vw; height: 40vw;"></div>
            </div>
            <div class="controls">
                <h3>新增数据</h3>
                <input v-model="newDataPoint.x" type="number" placeholder="横坐标值" />
                <input v-model="newDataPoint.y" type="number" placeholder="纵坐标值" />
                <br>
                <el-button type="primary" @click="addDataPoint">新增数据点</el-button>
                <h3>修改当前数据</h3>
                <select v-model="selectedIndex">
                    <option v-for="(point, index) in dataPoints" :key="index" :value="index">
                        点{{ index + 1 }}: ({{ point[0] }}, {{ point[1] }})
                    </option>
                </select>
                <br>
                <input v-model="modifiedDataPoint.x" type="number" placeholder="新横坐标值" />

                <input v-model="modifiedDataPoint.y" type="number" placeholder="新纵坐标值" />
                <el-button type="primary" @click="modifyDataPoint">修改数据点</el-button>
                <br>
                <h3>删除选中数据</h3>
                <select v-model="selectedIndex2">
                    <option v-for="(point, index) in dataPoints" :key="index" :value="index">
                        点{{ index + 1 }}: ({{ point[0] }}, {{ point[1] }})
                    </option>
                </select>
                <br>
                <el-button type="primary" @click="deletePoint">删除数据点</el-button>
                <br>
                <br>
                <el-button type="danger" @click="clearCurrent">清空当前数据</el-button>
                <br>
                <br>
                <el-button type="success" @click="dialogFormVisible = true" v-if="!valueConPlan">保存为模版</el-button>
            </div>
            <el-dialog title="模版名" :visible.sync="dialogFormVisible">
                <el-input v-model="inputName" placeholder="请输入模版名" style="width:50%"></el-input>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="handleUp">确 定</el-button>
                </div>
            </el-dialog>
        </div>
    </div>

</template>

<script>
import * as echarts from 'echarts';
export default {
    data() {
        return {
            optionsConPlan: [],//实例选择
            dataPoints: [
                [0.166, 0.948],
                [0.366, 0.248],
                [0.766, 0.248],
                [0.566, 0.248],
                [0.166, 0.248],
                [0.6686, 0.8948],
                [0.5686, 0.848],
                [0.686, 0.448],
                [0.386, 0.4448],
                [0.8686, 0.8448],
                [0.9686, 0.3448],
                [0.7686, 0.48],
                [0.786, 0.268],
                [0.36, 0.753],
                [0.756, 0.8434]
            ],
            dialogFormVisible: false,
            myChart: null,
            inputName: '',
            name: '',
            value: '',
            valueConPlan: '',
            option: null,
            options: [],
            resultList: [],
            resultConPlanList: [],
            newDataPoint: { x: '', y: '' },
            selectedIndex: null,
            selectedIndex2: null,
            modifiedDataPoint: { x: '', y: '' },
            deleteDataPoint: { x: '', y: '' }
        };
    },
    mounted() {
        this.getTemplateData();
        this.getConPlanData()
        this.init();
    },
    methods: {
        async getConPlanData() {
            await this.$http({
                url: this.$http.adornUrl("/qcTools/conplan/TList"),
                method: "get",
                params: this.$http.adornParams({
                    conplanType: "散点图",
                }),
            }).then(({ data }) => {
                if (data && data.code === 0) {
                    this.resultConPlanList = data.resultList.map((row) => ({
                        templateId: row.conplanId,
                        templateName: row.conplanName,
                        templateType: row.conplanType,
                        templateText: row.conplanText,
                        templateSeries: JSON.parse(row.conplanSeries),
                        templateAxis: JSON.parse(row.conplanAxis),
                    }));
                    this.optionsConPlan = data.resultList.map((item) => ({
                        value: item.conplanId,
                        label: item.conplanName,
                    }));
                    console.log(this.resultConPlanList);

                    console.log('---------------------');

                    console.log(this.optionsConPlan);
                } else {
                    this.options = [];
                }
            });

            // }
        },
        //处理下拉框选择变化
        handleSelectChange() {
            this.valueConPlan = ''
            console.log(this.resultList)
            this.resultList.forEach(item => {
                if (item.templateId == this.value) {
                    this.dataPoints = item.templateSeries
                    this.name = item.templateName
                }
            })
            console.log(this.dataPoints)
            this.updateChart()
        },
        handleSelectChangeConPlan() {
            this.value = ''
            this.resultConPlanList.forEach(item => {
                if (item.templateId == this.valueConPlan) {
                    this.dataPoints = item.templateSeries
                    this.name = item.templateName
                }
            })
            this.updateChart(tmpList)
        },
        async getTemplateData() {
            await this.$http({
                url: this.$http.adornUrl('/qcTools/template/templateList'),
                method: 'get',
                params: this.$http.adornParams({
                    'templateType': '散点图',
                })
            }).then(({ data }) => {
                if (data && data.code === 0) {
                    this.resultList = data.resultList.map(row => ({
                        templateId: row.templateId,
                        templateName: row.templateName,
                        templateType: row.templateType,
                        templateText: row.templateText,
                        templateSeries: JSON.parse(row.templateSeries),
                        // templateAxis: JSON.parse(row.templateAxis),
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
                                    this.name = ''
                                    this.value = ''
                                    this.getTemplateData()
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
                    'templateType': '散点图',
                    'templateText': this.textBy || '未命名',
                    'templateSeries': JSON.stringify(this.dataPoints),
                    //   'templateAxis': ,
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
        },
        init() {
            this.updateChart();
        },
        clearCurrent() {
            this.$confirm('确认清空当前数据吗', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.dataPoints = [];
                this.updateChart();
                this.$message({
                    type: 'success',
                    message: '清空成功!'
                });
            }).catch(() => {

            });

        },
        addDataPoint() {
            const x = parseFloat(this.newDataPoint.x);
            const y = parseFloat(this.newDataPoint.y);
            if (!isNaN(x) && !isNaN(y)) {
                this.dataPoints.push([x, y]);
                this.updateChart();
                this.newDataPoint.x = '';
                this.newDataPoint.y = '';
            } else {
                alert('请在输入框中输入有效的数字');
            }
        },
        modifyDataPoint() {
            const index = this.selectedIndex;
            const x = parseFloat(this.modifiedDataPoint.x);
            const y = parseFloat(this.modifiedDataPoint.y);
            if (index >= 0 && index < this.dataPoints.length && !isNaN(x) && !isNaN(y)) {
                this.dataPoints[index] = [x, y];
                this.updateChart();
                this.modifiedDataPoint.x = '';
                this.modifiedDataPoint.y = '';
                this.selectedIndex = null;
            } else {
                alert('无效的索引或坐标值');
            }
        },
        deletePoint() {
            const index = this.selectedIndex2;
            if (index >= 0 && index < this.dataPoints.length) {
                // 删除指定索引处的数据点
                this.dataPoints.splice(index, 1);
                // 更新图表
                this.updateChart();
                // 清空选择索引
                this.selectedIndex2 = null;
            } else {
                alert('无效的索引');
            }
        },
        updateChart() {
            let curSeries = [
                {
                    type: 'scatter',
                    data: this.dataPoints
                }
            ];
            this.option = {
                grid: {
                    left: '0%',
                    right: '12%',
                    bottom: '7%',
                    containLabel: true
                },
                tooltip: {
                    trigger: 'item',
                    formatter: (params) => {
                        return `横坐标: ${params.data[0]}, 纵坐标: ${params.data[1]}`;
                    }
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    left: 'right',
                    top: 'center',
                    feature: {
                        mark: { show: true },
                        saveAsImage: { show: true }
                    }
                },
                xAxis: {
                    name: '横坐标',
                    // min: 0,
                    // max: 1,
                },
                yAxis: {
                    name: '纵坐标',
                    // min: 0,
                    // max: 1
                },
                series: curSeries,
                animation: false,
            };
            const dom = document.getElementById("chartSdtV");
            this.myChart = echarts.init(dom);
            this.myChart.setOption(this.option);
        }
    }
}
</script>

<style>
.content {
    display: flex;
    flex-direction: row;
    /* Align children in a row */
    align-items: flex-start;
    /* Align items to the top */
    justify-content: center;
    /* Center the content horizontally */
    max-width: 1200px;
    /* Optional: Set a max width for the content */
    margin: 0 auto;
    /* Center the container */
}

.chart-container {
    margin-right: 20px;
    /* Add some space between chart and controls */
}

.controls {
    display: flex;
    flex-direction: column;
    /* Stack controls vertically */
}
</style>
