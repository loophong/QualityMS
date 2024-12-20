<template>
  <div>
    <!-- div 状态提示条  -->
    <div style=" height: auto; width: auto; ">
      <el-row>
        <el-col style="color: #95a5a6; font-size: 20px; margin-bottom: 5px; width: 200px">● 未开始</el-col>
        <el-col style="color: #3498db; font-size: 20px; margin-bottom: 5px; width: 200px">● 进行中</el-col>
        <el-col style="color: #e67e22; font-size: 20px; margin-bottom: 5px; width: 200px">● 审批中</el-col>
        <el-col style="color: #2ecc71; font-size: 20px; margin-bottom: 5px; width: 200px">● 已完成</el-col>
      </el-row>
    </div>

    <!--  任务详情窗口  -->
    <el-dialog title="任务详情" :visible.sync="isTaskVisible" width="50%">
      <el-form label-position="left" label-width="100px">
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="任务编号" prop="taskId">
              <el-input v-model="taskInfo.taskId" disabled placeholder="任务编号"></el-input>
            </el-form-item>
            <el-form-item label="开始日期" prop="taskStartDate">
              <el-date-picker v-model="taskInfo.taskStartDate" disabled type="date" placeholder="选择开始日期"
                              style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item label="任务天数" prop="taskScheduleDays">
              <el-input :value="taskInfo.taskScheduleDays" disabled placeholder="任务天数"></el-input>
            </el-form-item>

            <el-form-item label="负责人" prop="taskPrincipal">
              <el-select v-model="taskInfo.taskPrincipal" disabled filterable placeholder="请选择负责人">
                <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                  <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>

          </el-col>

          <el-col :span="12">
            <el-form-item label="任务名" prop="taskName">
              <el-input v-model="taskInfo.taskName" disabled placeholder="任务名"></el-input>
            </el-form-item>

            <el-form-item label="预计完成日期" prop="taskScheduleCompletionDate">
              <el-date-picker v-model="taskInfo.taskScheduleCompletionDate" disabled type="date"
                              placeholder="选择任务完成日期"
                              style="width: 100%;"></el-date-picker>
            </el-form-item>

            <el-form-item label="审核人" prop="taskAuditor">
              <el-select v-model="taskInfo.taskAuditor" disabled filterable placeholder="请选择审核人">
                <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                  <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>

            <el-form-item label="执行人" prop="taskExecutor">
              <el-select v-model="taskInfo.taskExecutor" disabled multiple filterable placeholder="执行人">
                <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                  <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="任务内容" prop="taskContent">
          <el-input type="textarea" :autosize="{ minRows: 5, maxRows: 5 }" placeholder="请输入任务内容"
                    v-model="taskInfo.taskContent" disabled maxlength="1000">
          </el-input>
        </el-form-item>

        <div v-if="approvalInfo !== null && approvalInfo !== ''">
          <el-form-item v-if="approvalInfo !== '' && approvalInfo !== null" label="审批内容" prop="approvalContent">
            <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 5 }"
                      v-model="approvalInfo.approvalContent" disabled maxlength="1000">
            </el-input>
          </el-form-item>
          <el-form-item label="审批附件">
            <el-row v-for="file in taskFiles" :key="file.name" style="margin-bottom: 4px">
              <el-col :span="12">
                {{ file.name }}
              </el-col>
              <el-col :span="12">
                <el-button type="primary" @click="downloadFile(file.url)">下载</el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item v-if="approvalInfo !== '' && approvalInfo !== null" label="审批意见" prop="approvalComments">
            <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 5 }"
                      v-model="approvalInfo.approvalComments" disabled maxlength="1000">
            </el-input>
          </el-form-item>
          <el-form-item v-if="approvalInfo !== '' && approvalInfo !== null" label="审批备注" prop="remarks">
            <el-input type="textarea"
                      v-model="approvalInfo.remarks" disabled maxlength="1000">
            </el-input>
          </el-form-item>
        </div>


      </el-form>
    </el-dialog>

    <!--  计划详情窗口  -->
    <el-dialog title="计划详情" :visible.sync="isPlanVisible" width="50%">
      <el-form label-position="left" label-width="100px">
        <el-row :gutter="18">
          <el-col :span="12">
            <el-form-item label="计划编号" prop="planId">
              <el-input v-model="planInfo.planId" disabled placeholder="计划编号"></el-input>
            </el-form-item>
            <el-form-item label="开始日期" prop="planStartDate">
              <el-date-picker v-model="planInfo.planStartDate" disabled type="date" placeholder="选择开始日期"
                              style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item label="计划天数" prop="planScheduleDays">
              <el-input :value="planInfo.planScheduleDays" disabled placeholder="计划天数"></el-input>
            </el-form-item>
            <el-form-item label="发起人" prop="planInitiator">
              <el-select v-model="planInfo.planInitiator" disabled filterable placeholder="请选择审核人">
                <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                  <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
            <el-form-item label="审核人" prop="planAuditor">
              <el-select v-model="planInfo.planAuditor" disabled filterable placeholder="请选择审核人">
                <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                  <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="计划名" prop="planName">
              <el-input v-model="planInfo.planName" disabled placeholder="计划名"></el-input>
            </el-form-item>
            <el-form-item label="计划完成日期" prop="planScheduleCompletionDate">
              <el-date-picker v-model="planInfo.planScheduleCompletionDate" disabled type="date"
                              placeholder="选择计划完成日期"
                              style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item label="关联指标" prop="planAssociatedIndicatorsId">
              <el-select v-model="planInfo.planAssociatedIndicatorsId" disabled clearable placeholder="无"
                         ref="transferName">
                <el-option v-for="item in indicatorOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="负责人" prop="planPrincipal">
              <el-select v-model="planInfo.planPrincipal" disabled filterable placeholder="请选择负责人">
                <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                  <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
            <el-form-item label="执行人" prop="planExecutor">
              <el-select v-model="planInfo.planExecutor" disabled multiple filterable placeholder="执行人">
                <el-option-group v-for="group in options" :key="group.label" :label="group.label">
                  <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="计划内容" prop="planContent">
          <el-input type="textarea" disabled :autosize="{ minRows: 5, maxRows: 10 }" placeholder="请输入计划内容"
                    v-model="planInfo.planContent" maxlength="1000">
          </el-input>
        </el-form-item>

        <el-form-item v-if="planFiles && planFiles.length > 0" label="附件">
          <el-row v-for="file in planFiles" :key="file.name" style="margin-bottom: 4px">
            <el-col :span="12">
              {{ file.name }}
            </el-col>
            <el-col :span="12">
              <el-button type="primary" @click="downloadFile(file.url)">下载</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </el-dialog>

    <div ref="treeChart" style="width: 100%; height: 500px;"></div>

  </div>

</template>

<script>
import * as d3 from 'd3';

export default {
  data() {
    return {
      rootData: null,
      dataList: [], // 这里的数据会从服务器获取

      taskInfo: '',
      taskFiles: [],
      approvalInfo: '',

      planInfo: '',
      planFiles: [],

      // 任务弹窗可见性
      isTaskVisible: false,

      // 计划弹窗可见性
      isPlanVisible: false,

      // 员工信息
      options: [],

      indicatorOptions: [],
    };
  },
  async created() {
    // this.init(this.$route.query.planId);
    console.log('this.$route.query.planId:', this.$route.query.planId)

    // 获取员工信息
    this.$http({
      url: this.$http.adornUrl(`/taskmanagement/user/getName`),
      method: 'get',
    }).then(({data}) => {
      console.log(data);
      console.log(1111);

      this.options = data;
      console.log(this.options);
    });

    // 获取指标列表
    await this.$http({
      url: this.$http.adornUrl(`/indicator/indicatordatadictionary/getIndicatorsList`),
      method: 'get',
    }).then(response => {
      const opt = response.data.map(item => ({
        value: item.value,
        label: item.label
      }));
      console.log(opt);
      this.indicatorOptions = opt;
    })
  },

  mounted() {
    this.$nextTick(() => {
      this.updateSvgSize();
      window.addEventListener('resize', this.updateSvgSize); // 添加窗口大小变化监听器，实时调整SVG大小
      // this.getDataList();
    });

    this.getDataList();
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateSvgSize); // 组件销毁前移除监听器
  },

  methods: {

    updateSvgSize() {
      this.$nextTick(() => {
        const mainContainer = document.querySelector('main');
        if (mainContainer) {
          const mainWidth = mainContainer.clientWidth;
          const mainHeight = mainContainer.clientHeight;
          const svg = d3.select(this.$refs.treeChart).select('svg');
          svg.attr('width', mainWidth).attr('height', mainHeight);
          console.log(`Updated SVG size to width: ${mainWidth}, height: ${mainHeight}`);
        }
      });
    },

    getDataList() {
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/plan/getPlanAllInfo'),
        method: 'get',
        params: this.$http.adornParams({
          // 'planId': this.pageIndex
          'planId': this.$route.query.planId

        })
      }).then(({data}) => {
        if (data) {
          console.log('data:', data);
          this.rootData = data.plan;
          this.dataList = data.tasks;

          console.log('rootData:', this.rootData);
          console.log('dataList:', this.dataList);

          this.renderTree();

        } else {
          this.dataList = [];
        }
      });
    },

    renderTree() {

      const mainContainer = document.querySelector('main');
      const mainWidth = mainContainer.clientWidth;
      const mainHeight = mainContainer.clientHeight;

      // 创建根节点
      const rootNode = {
        id: this.rootData.planId,
        name: this.rootData.planName,
        parentId: '',
        statusColor: this.rootData.planCurrentState
        // parentName: '',
        // url: '/some-url-based-on-indicator-id/1',
        // classification: 'A'
      };

      // 格式化数据并插入根节点
      const formattedData = [rootNode, ...this.dataList.map(item => ({
        // id: item.indicatorId,
        // name: item.indicatorName,
        // parentName: item.indicatorParentNode,
        // url: '/some-url-based-on-indicator-id/' + item.indicatorId,
        // url: 'indicatorchart',
        // classification: item.indicatorClassification,
        id: item.taskId,
        name: item.taskName,
        parentId: item.taskParentNode,
        statusColor: item.taskCurrentState
      }))];

      console.log('Formatted Data:', formattedData); // 调试信息

      const treeData = this.buildTree(formattedData);

      console.log('Tree Data:', treeData); // 调试信息

      const margin = {top: 20, right: 120, bottom: 20, left: 120};
      const width = mainWidth - margin.right - margin.left;
      const height = mainHeight - margin.top - margin.bottom;

      const yOffset = height / 2;
      const initialTransform = `translate(${margin.left},${yOffset + margin.top})`;

      const svg = d3.select(this.$refs.treeChart)
        .append('svg')
        .attr('width', width + margin.right + margin.left)
        .attr('height', height + margin.top + margin.bottom)
        .call(d3.zoom().on("zoom", (event) => {  // 添加缩放和平移功能
          svg.attr("transform", event.transform);
        }))
        .append('g')
        .attr('transform', initialTransform)
        .append("g");

      const root = d3.hierarchy(treeData[0]);
      // const treeLayout = d3.tree().size([height, width]); // 增加高度比例
      // const treeLayout = d3.tree().size([200, 400]); // 增加高度比例
      const treeLayout = d3.tree().nodeSize([200, 400]); // 设置节点的水平和垂直间距
      treeLayout(root);

      // root.x = width / 2;

      // 计算垂直居中的偏移量
      // const yOffset = (height - root.height * 200) / 2;
      // const initialTransform = `translate(${margin.left},${yOffset + margin.top})`;
      // svg.attr('transform', initialTransform).append("g");


      svg.selectAll('line.link')
        .data(root.links())
        .enter()
        .append('line')
        .attr('class', 'link')
        .attr('x1', d => d.source.y + 100) // 100 是节点宽度的一半
        .attr('y1', d => d.source.x + 20)  // 40 是节点高度的一半
        .attr('x2', d => d.target.y - 100) // 100 是节点宽度的一半
        .attr('y2', d => d.target.x + 20)  // 40 是节点高度的一半
        .style('stroke', '#ccc');

      const nodes = svg.selectAll('g.node')
        .data(root.descendants())
        .enter()
        .append('g')
        .attr('class', 'node')
        .attr('transform', d => `translate(${d.y},${d.x})`);


      // 根据指标等级设置矩形颜色
      const colorScale = {
        'NOT_STARTED': '#95a5a6', // 灰色
        'IN_PROGRESS': '#3498db', // 蓝色
        'APPROVAL_IN_PROGRESS': '#e67e22', // 黄色
        'COMPLETED': '#2ecc71' // 绿色
      };


      // 'NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'OVERDUE', 'APPROVAL_IN_PROGRESS'
      // 'A': '#e74c3c', // 红色
      // 'B': '#e67e22', // 橙色
      // 'C': '#3498db'  // 蓝色


      // 添加圆角矩形
      nodes.append('rect')
        .attr('width', 200)
        .attr('height', 80)
        .attr('x', -100)
        .attr('y', -25)
        .attr('rx', 10)
        .attr('ry', 10)
        .style('fill', d => {
          // const color = colorScale[d.data.classification] || '#000'; // 如果未匹配到颜色，使用默认黑色
          const color = colorScale[d.data.statusColor] || '#000';
          return color;
        })
        .on('click', (event, d) => {
          if (d.parent === null) {
            console.log('根节点', d);
            this.getPlanInfo(d.data.id)
          } else {
            console.log('非根节点', d);
            // 获取task信息和审批信息
            this.getTaskInfo(d.data.id);
          }
        });

      // 在矩形中添加文本
      nodes.append('text')
        .attr('dy', '.9em')
        .attr('x', 0)
        .attr('y', 0)
        .attr('text-anchor', 'middle')
        .style('dominant-baseline', 'central')
        .text(d => d.data.name)
        .style('fill', 'white');
      // // 在矩形中添加文本
      // nodes.append('text')
      //   .attr('dy', '.9em')
      //   .attr('x', 0)
      //   .attr('y', 0)
      //   .attr('text-anchor', 'middle')
      //   .style('dominant-baseline', 'central')
      //   .text(d => d.data.name)
      //   .style('fill', 'white')
      //   .on('click', (event, d) => {
      //     console.log('Clicked on2:', d); // 打印节点数据
      //     console.log('d.data 内容:', d.data);
      //     console.log("即将传递的url:", d.data.url);
      //     console.log("即将传递的参数:", d.data.name);
      //     this.$router.push({
      //       name: d.data.url,
      //       params: {indicatorName: d.data.name}
      //     });
      //   });
      // .on('mouseover', (event, d) => {
      //   console.log('d.data 内容:', d.data)
      //   this.getTaskInfo(d.data.id);
      //   tooltip.transition()
      //     .duration(200)
      //     .style('visibility', 'visible');
      //   tooltip.html(`<strong>${d.data.name}</strong><br/>状态: ${d.data.statusColor}`)
      //     .style('left', `${event.pageX}px`)
      //     .style('top', `${event.pageY - 28}px`);
      //
      // })
      // .on('mouseout', () => {
      //   tooltip.transition()
      //     .duration(500)
      //     .style('visibility', 'hidden');
      // });


    },


    // buildTree(data, parentName = '') {
    //     return data
    //         .filter(item => item.parentName === parentName)  // 使用parentName来过滤
    //         .map(item => ({
    //             id: item.id,
    //             name: item.name,
    //             // classification: item.classification,
    //             url: item.url,
    //             children: this.buildTree(data, item.name)  // 使用name来匹配
    //         }));
    // },

    buildTree(data, parentId = '') {
      return data
        .filter(item => item.parentId === parentId)  // 使用parentName来过滤
        .map(item => ({
          id: item.id,
          name: item.name,
          // classification: item.classification,
          // url: item.url,
          statusColor: item.statusColor,
          children: this.buildTree(data, item.id)  // 使用name来匹配
        }));
    },

    // 获取任务的全部信息 和 审核文件
    getTaskInfo(taskId) {
      console.log("任务id:", taskId)
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/task/taskAllInfo'),
        method: 'post',
        data: this.$http.adornData({
          taskId: taskId
        })
      }).then(({data}) => {
        console.log("获取任务的全部信息 和 审核文件:", data);
        this.taskInfo = data.task
        this.taskFiles = data.fileList
        this.approvalInfo = data.approval
        console.log("获取任务信息:", this.taskInfo);
        console.log("获取任务审批信息:", this.taskFiles);
        console.log("获取任务审核文件:", this.approvalInfo);
        this.isTaskVisible = true;
      });
    },
    // 下载附件
    downloadFile(url) {
      // 获取当前的 token，假设它存储在 localStorage 中
      const token = this.$cookie.get('token');
      if (token) {
        console.log('Token found:', token);
      } else {
        console.error('Token not found!');
      }

      // 将 token 作为参数添加到 URL
      // const imageUrlWithToken = `${url}?token=${token}`;

      // 使用window.open方法打开下载链接
      // window.open(imageUrlWithToken);

      // 拼接带有 token 的请求地址
      const fileUrl = `${this.$http.adornUrl(`/generator/issuetable/${url}`)}?token=${token}`;
      window.open(fileUrl);
    },

    // 获取计划的全部信息 和 文件
    getPlanInfo(planId) {
      console.log("计划id:", planId)
      this.$http({
        url: this.$http.adornUrl('/taskmanagement/plan/planAllInfo'),
        method: 'post',
        data: this.$http.adornData({
          paramPlanId: planId
        })
      }).then(({data}) => {
        console.log("获取任务的全部信息 和 审核文件:", data);
        this.planInfo = data.plan
        this.planFiles = data.files
        console.log("获取计划信息:", this.planInfo);
        console.log("获取计划文件:", this.planFiles);
        this.isPlanVisible = true;
      });
    },
  }
};
</script>

<style>
/* 添加一些样式来美化图表 */
</style>
