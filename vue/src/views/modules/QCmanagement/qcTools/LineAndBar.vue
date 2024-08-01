<template>
  <div>
    <div id="main" ref="main"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import moment from 'moment'

export default {
  name: 'lineAndBar',
  data() {
    return {
      loading: false,
      data: [],
      pickerOptions: [],
      option: {},
      myChart: {}
    }
  },
  computed: {},
  mounted() {
    this.myChart = echarts.init(this.$refs.main);
    this.initChart()
  },
  methods: {
    initChart() {
      var app = {};
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
          this.myChart.setOption({
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
      this.option = {
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
              optionToContent(opt) {
                let axisData = opt.xAxis[0].data; // 坐标数据
                let series = opt.series; // 折线图数据
                let tdHeads = '<td style="padding: 2px 10px;background-color: #eeeeee;font-weight: 700;color: #333333"><input type="text" value="时间" style="border: none;text-align: center;color: #444444;width: 100px;"></td>'; // 表头
                let tdBodys = ''; // 数据

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
                  tdBodys = '';
                }
                table += '</tbody></table>';
                return table;
              },
              contentToOption(HTMLDomElement, opt) {
                if (document.getElementsByClassName('dataViewTable').length > 1) {
                  this.$message.error('有其他未关闭的数据视图，请关闭后重试');
                } else {
                  let table = HTMLDomElement.querySelector('.dataViewTable');
                  let rows = table.querySelectorAll('tr');
                  let axisData = [];
                  let seriesNames = [];
                  let seriesData = [];

                  // 获取表头中的系列名称
                  let headerRow = rows[0];
                  let axisName = headerRow.children[0].querySelector('input').value;
                  for (let i = 1; i < headerRow.children.length; i++) {
                    seriesNames.push(headerRow.children[i].querySelector('input').value);
                    seriesData.push([]);
                  }

                  // 获取数据行中的时间和系列数据
                  for (let i = 1; i < rows.length; i++) {
                    let row = rows[i];
                    axisData.push(row.children[0].querySelector('input').value);
                    for (let j = 0; j < seriesNames.length; j++) {
                      seriesData[j].push(row.children[j + 1].querySelector('input').value);
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
              }


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
            // interval: 5,
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
      this.option && this.myChart.setOption(this.option);
    },
  },
}
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
