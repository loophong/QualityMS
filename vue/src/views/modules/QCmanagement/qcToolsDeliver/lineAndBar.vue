<template>
  <div>
    <span>
      <!-- <el-select
        v-model="value"
        @change="handleSelectChange"
        placeholder="请选择模版"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select> -->
      <el-button type="danger" @click="handleDelete">删除当前QC图</el-button>
    </span>

    <div id="main" ref="main"></div>
    <span>
      <label for="xAxisDataBy">横坐标:</label>
      <el-input v-model="xAxisDataBy" placeholder="请输入内容" style="width: 30%"></el-input>
      <label for="seriesDataBy">折线数:</label>
      <el-input v-model="seriesDataBy" placeholder="请输入内容" style="width: 30%"></el-input>
    </span>
    <br />
    <br />
    <span>
      <label for="textBy">图标题:</label>
      <el-input v-model="textBy" placeholder="请输入内容" style="width: 15%"></el-input>
      <el-button type="primary" @click="initChart(tmpResultList)">更新图表</el-button>
      <!-- 保存为模板 change to 暂存当前数据  -->
      <!-- <el-button type="success" @click="dialogFormVisible = true"
        >更新当前数据</el-button
      > -->
      <el-button type="success" @click="handleUp" v-if="admitEdit">更新当前数据</el-button>
    </span>

    <el-dialog title="自定义图名" :visible.sync="dialogFormVisible" append-to-body>
      <el-input v-model="inputName" placeholder="请输入图名" style="width: 50%"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleUp">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from "echarts";
import moment from "moment";
import qcPlanNew from "../qcSubjectManagement/plan/qcPlanNew.vue";

export default {
  components: {
    qcPlanNew,
  },
  name: "lineAndBar",
  //在qcPlanNew.vue 中应用this, 传递过来的参数
  props: {
    item: {
      type: Object,
      required: true, // 确保 item 是必须传递的
    },
  },

  data() {
    return {
      loading: false,
      dialogFormVisible: false,
      inputName: "",
      updatedSeries: "",
      test: "",
      updatedAxis: "",
      data: [],
      pickerOptions: [],
      option: {},
      myChart: {},
      seriesDataBy: "折线,柱状(柱)",
      xAxisDataBy: "A,B,C,D,E,F",
      textBy: "排列图",
      options: [],
      value: "",
      resultList: [],
      tmpResultList: {
        templateId: 0,
      },
      tmpSeriesList: [],
      tmpAxisList: [],
      currentUserName: '',
      admitEdit: false,
    };
  },
  computed: {},
  mounted() {
    this.getUserName();
    this.getTemplateData();
    this.myChart = echarts.init(this.$refs.main);
  },
  methods: {
    async getUserName() {
      await this.$http({
        url: this.$http.adornUrl("/qcSubject/registration/user"),
        method: "get",
        params: this.$http.adornParams({
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.currentUserName = data.userName;
        } else {
        }

      });
    },

    async getTemplateData() {
      await this.$http({
        url: this.$http.adornUrl("/qcTools/conplan/GetById"),
        method: "get",
        params: this.$http.adornParams({
          conplanId: this.item.conplanId,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          if (data.resultList.length != 0) {
            if (data.resultList[0].conplanUser == this.currentUserName) {
              this.admitEdit = true;
            }
          }
          this.resultList = data.resultList.map((row) => ({
            templateId: row.conplanId,
            templateName: row.conplanName,
            templateType: row.conplanType,
            templateText: row.conplanText,
            templateSeries: JSON.parse(row.conplanSeries),
            templateAxis: JSON.parse(row.conplanAxis),
            conplanSubject: row.conplanSubject,
            conplanProcess: row.conplanProcess,
          }));
          this.options = data.resultList.map((item) => ({
            value: item.conplanId,
            label: item.conplanName,
          }));
        } else {
          this.options = [];
        }
      });

      // console.log("1119"+this.resultList[0]);
      //查询到有用户暂存的数据,就使用该数据渲染echarts
      if (this.resultList.length != 0) {
        let tmpList = {};
        this.resultList.forEach((item) => {
          tmpList = item;
          this.name = item.templateName;
        });
        console.log(tmpList);
        this.initChart(tmpList);
      } else {//否则使用默认数据渲染echarts
        this.initChart(this.tmpResultList);
      }


    },

    handleUp() {
      console.log(this.updatedSeries);
      console.log(this.test);
      this.dialogFormVisible = false;
      this.addTemplate();
    },

    //更新数据 是由save --> update
    addTemplate() {
      console.log(this.updatedSeries);
      console.log(this.xAxisDataBy);

      const tmp = this.xAxisDataBy.split(",");
      let tmpListString = JSON.stringify(this.updatedSeries, null, 2);
      let tmpListString2 = JSON.stringify(this.updatedAxis);
      console.log(tmpListString);
      console.log(tmpListString2);
      //过滤掉多余的数据
      const filteredData = this.updatedSeries.map((series) => ({
        name: series.name,
        data: series.data,
      }));
      console.log(filteredData);

      let img = new Image()
      img.src = this.myChart.getDataURL(
        {
          type: 'png',
          // pixelRatio: 1,
          // backgroundColor: '#fff',
          excludeComponents: ['toolbox']
        }
      )

      this.$http({
        url: this.$http.adornUrl(`/qcTools/conplan/update`),
        method: "post",
        data: this.$http.adornData({
          conplanId: this.resultList[0].templateId,
          conplanName: this.resultList[0].templateName,
          conplanType: this.resultList[0].templateType,
          conplanText: this.resultList[0].templateText,
          conplanSeries: JSON.stringify(filteredData),
          conplanAxis: JSON.stringify(tmp),
          conplanSubject: this.resultList[0].conplanSubject,
          conplanProcess: this.resultList[0].conplanProcess,
          conplanUrl: JSON.stringify(img.src),
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            // message: '操作成功',
            message: "更新成功",
            type: "success",
            duration: 1500,
            onClose: () => {
              this.visible = false;
            },
          });
        } else {
          this.$message.error(data.msg);
        }
      });
    },



    //删除当前图
    handleDelete() {
      let ids = this.item.conplanId;
      console.log(ids);
      if (ids) {
        this.$confirm(`确定对 [${this.item.conplanName}] 进行删除?`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl("/qcTools/conplan/delete"),
            method: "post",
            data: this.$http.adornData(ids, false),
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.value = "";
                  this.name = "";
                  this.initChart(this.tmpResultList);
                  this.getTemplateData();
                },
              });
            } else {
              this.$message.error(data.msg);
            }
          });
        });
      } else {
        this.$message({
          message: "未选择",
          type: "info",
          duration: 1500,
        });
      }
    },


    initChart(resultList) {
      // console.log(resultList.width)
      var app = {};
      const posList = [
        "left",
        "right",
        "top",
        "bottom",
        "inside",
        "insideTop",
        "insideLeft",
        "insideRight",
        "insideBottom",
        "insideTopLeft",
        "insideTopRight",
        "insideBottomLeft",
        "insideBottomRight",
      ];
      app.configParameters = {
        rotate: {
          min: -90,
          max: 90,
        },
        align: {
          options: {
            left: "left",
            center: "center",
            right: "right",
          },
        },
        verticalAlign: {
          options: {
            top: "top",
            middle: "middle",
            bottom: "bottom",
          },
        },
        position: {
          options: posList.reduce(function (map, pos) {
            map[pos] = pos;
            return map;
          }, {}),
        },
        distance: {
          min: 0,
          max: 100,
        },
      };
      app.config = {
        rotate: 0,
        align: "center",
        verticalAlign: "middle",
        position: "top",
        distance: 15,
        onChange: function () {
          const labelOption = {
            rotate: app.config.rotate,
            align: app.config.align,
            verticalAlign: app.config.verticalAlign,
            position: app.config.position,
            distance: app.config.distance,
          };
          this.myChart.setOption({
            series: [
              {
                label: labelOption,
              },
              {
                label: labelOption,
              },
              {
                label: labelOption,
              },
              {
                label: labelOption,
              },
            ],
          });
        },
      };
      const labelOption = {
        show: true,
        position: app.config.position,
        distance: app.config.distance,
        align: app.config.align,
        verticalAlign: app.config.verticalAlign,
        rotate: app.config.rotate,
        formatter: "{c}",
        fontSize: 16,
        rich: {
          name: {},
        },
      };
      let seriesData = [];
      let xAxisData = [];
      let legendData = [];
      let title = {
        text: "折柱图",
      };
      if (resultList.templateId == 0) {
        try {
          console.log(this.tmpSeriesList);
          if (this.tmpSeriesList.length > 0) {
            console.log(this.tmpSeriesList);
            console.log("-------");
            seriesData = this.tmpSeriesList;

            console.log(seriesData);
          } else {
            seriesData = [
              {
                name: "折线",
                type: "line",
                yAxisIndex: 1,
                label: labelOption,
                emphasis: {
                  focus: "series",
                },
                data: [5, 5, 5, 5, 5, 5],
              },
              {
                name: "柱状(柱)",
                type: "bar",
                yAxisIndex: 0,
                label: labelOption,
                emphasis: {
                  focus: "series",
                },
                data: [10, 10, 10, 10, 10, 10],
              },
            ];
          }
        } catch (e) {
          console.log(e);
        }

        title.text = this.textBy;
        if (this.seriesDataBy) {
          const tmp2 = this.seriesDataBy.split(",");
          console.log(tmp2);
          console.log(tmp2.length);
          try {
            for (let i = 0; i < tmp2.length; i++) {
              if (seriesData[i]) {
                seriesData[i].name = tmp2[i];
                console.log("存在");
              } else {
                console.log("不存在");
                console.log(tmp2[i].includes("(柱)"));
                seriesData.push({
                  name: tmp2[i],
                  type: tmp2[i].includes("(柱)") || tmp2[i].includes("（柱）") ? "bar" : "line",
                  yAxisIndex: (tmp2[i].includes('(柱)') || tmp2[i].includes('（柱）')) ? 0 : 1,
                  label: labelOption,
                  emphasis: {
                    focus: "series",
                  },
                  data: [0],
                });
              }
            }
          } catch (e) {
            console.log(e);
          }
          console.log(seriesData);
        }

        seriesData.forEach((item) => {
          legendData.push(item.name);
        });
        xAxisData = ["A", "B", "C", "D", "E", "F"];
        if (this.xAxisDataBy) {
          const tmp = this.xAxisDataBy.split(",");
          console.log(tmp);
          xAxisData = tmp;
          this.updatedAxis = xAxisData;
        }
        this.updatedSeries = seriesData;
      } else {
        try {
          seriesData = resultList.templateSeries.map((item) => ({
            name: item.name,
            type: item.name.includes("(柱)") || item.name.includes("（柱）") ? "bar" : "line",
            yAxisIndex: item.name.includes("(柱)") || item.name.includes("（柱）") ? 0 : 1,
            label: labelOption,
            emphasis: {
              focus: "series",
            },
            data: item.data,
          }));
          seriesData.forEach((item) => {
            legendData.push(item.name);
          });
          console.log(resultList.templateText);
          title.text = resultList.templateText;
          this.tmpSeriesList = seriesData;
          xAxisData = resultList.templateAxis;
          this.xAxisDataBy = `${resultList.templateAxis}`;
          this.seriesDataBy = `${resultList.templateSeries.map(
            (item) => item.name
          )}`;
          this.textBy = resultList.templateText;
          console.log(`${resultList.templateAxis}`);
          console.log(this.seriesDataBy);
          console.log(`${resultList.templateSeries.map((item) => item.name)}`);
          console.log(this.xAxisDataBy);
        } catch (e) {
          console.log(e);
        }
      }

      this.option = {
        title: title,
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        legend: {
          data: legendData,
        },
        toolbox: {
          show: true,
          orient: "vertical",
          left: "right",
          top: "center",
          feature: {
            mark: { show: true },
            dataView: {
              show: true,
              readOnly: false,
              title: "编辑",
              lang: ["数据编辑", "关闭", "更新"],
              onChange: (opt) => {
                console.log("更新后触发");
                console.log(opt);
              },
              optionToContent(opt) {
                let axisData = opt.xAxis[0].data; // 坐标数据
                let series = opt.series; // 折线图数据
                console.log(opt);
                let tdHeads =
                  '<td style="padding: 2px 10px;background-color: #eeeeee;font-weight: 700;color: #333333"><input type="text" value="" style="border: none;text-align: center;color: #444444;width: 100px;"></td>'; // 表头
                let tdBodys = ""; // 数据

                series.forEach(function (item) {
                  // 组装表头
                  tdHeads += `<td style="padding: 2px 10px;background-color: #eeeeee;font-weight: 700;color: #333333"><input type="text" value="${item.name}" style="border: none;text-align: center;color: #444444;width: 100px;"></td>`;
                });

                let table = `<table border="1" style="width: 96%;margin-left:20px;border-collapse:collapse;font-size:14px;text-align:center" class="dataViewTable"><tbody><tr>${tdHeads} </tr>`;

                for (let i = 0, l = axisData.length; i < l; i++) {
                  tdBodys += `<td><input type="text" value="${axisData[i]}" style="border: none;text-align: center;color: #444444;width: 100px;"></td>`;
                  for (let j = 0; j < series.length; j++) {
                    // 组装表数据
                    tdBodys += `<td><input class="${j}x" type="text" value="${series[j].data[i]}" style="border: none;text-align: center;color: #444444;width: 100px;"></td>`;
                  }
                  table += `<tr>${tdBodys}</tr>`;
                  tdBodys = "";
                }
                table += "</tbody></table>";
                return table;
              },
              contentToOption(HTMLDomElement, opt) {
                if (
                  document.getElementsByClassName("dataViewTable").length > 1
                ) {
                  this.$message.error("有其他未关闭的数据视图，请关闭后重试");
                } else {
                  let table = HTMLDomElement.querySelector(".dataViewTable");
                  let rows = table.querySelectorAll("tr");
                  let axisData = [];
                  let seriesNames = [];
                  let seriesData = [];

                  // 获取表头中的系列名称
                  let headerRow = rows[0];
                  let axisName =
                    headerRow.children[0].querySelector("input").value;
                  for (let i = 1; i < headerRow.children.length; i++) {
                    seriesNames.push(
                      headerRow.children[i].querySelector("input").value
                    );
                    seriesData.push([]);
                  }

                  // 获取数据行中的时间和系列数据
                  for (let i = 1; i < rows.length; i++) {
                    let row = rows[i];
                    axisData.push(row.children[0].querySelector("input").value);
                    for (let j = 0; j < seriesNames.length; j++) {
                      seriesData[j].push(
                        row.children[j + 1].querySelector("input").value
                      );
                    }
                  }

                  // 更新opt对象
                  opt.xAxis[0].name = axisName;
                  opt.xAxis[0].data = axisData;
                  for (let i = 0; i < seriesNames.length; i++) {
                    opt.series[i].name = seriesNames[i];
                    opt.series[i].data = seriesData[i].map(Number);
                  }

                  return opt;
                }
              },
            },
            // restore: { show: true, title: '重置' },
            saveAsImage: { show: true, title: "保存" },
          },
        },
        xAxis: [
          {
            axisTick: { show: false },
            data: xAxisData,
          },
        ],
        yAxis: [
          {
            // 左边的 Y 轴，用于柱状图
            type: 'value',
            name: '柱轴',
            position: 'left',
            alignTicks: true, // 对齐刻度
          },
          {
            // 右边的 Y 轴，用于折线图
            type: 'value',
            name: '线轴',
            position: 'right',
            alignTicks: true, // 对齐刻度
          }
        ],
        series: seriesData,
        animation: false,
      };
      this.option && this.myChart.setOption(this.option);
      // console.log(this.option)
      this.myChart.on("dataViewChanged", (params) => {
        console.log("dataViewChanged:", params);
        this.updatedSeries = params.newOption.series;
        this.tmpSeriesList = params.newOption.series;
        console.log(params.newOption.series);
        this.test = params.newOption.series;
        console.log(this.updatedSeries);
        console.log(this.test);
      });
    },
  },
};
</script>

<style lang="scss" scoped>
#main {
  width: 1000px;
  height: 600px;
  margin: 40px auto;
}

.block {
  margin-top: 50px;
  text-align: center;
}
</style>
