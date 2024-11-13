<template>
  <div class="my-process-designer">
    <div class="my-process-designer__container">
      <div class="my-process-designer__canvas" style="height: 400px" ref="bpmnCanvas"></div>
    </div>
  </div>
</template>

<script>
import BpmnViewer from 'bpmn-js/lib/Viewer'
import DefaultEmptyXML from './plugins/defaultEmpty'
import { formatTime } from '@/utils/filters'
import { isEmpty } from '@/utils/is'

export default {
  name: 'ProcessViewer',
  props: {
    value: { type: String, default: '', }, // BPMN XML 字符串
    prefix: { type: String, default: 'camunda' }, // 使用哪个引擎
    activityData: { type: Array, default: () => [] }, // 活动的数据。传递时，可高亮流程
    processInstanceData: { type: Object, default: () => { } }, // 流程实例的数据。传递时，可展示流程发起人等信息
    taskData: { type: Array, default: () => [] } // 任务实例的数据。传递时，可展示 UserTask 审核相关的信息
  },
  data() {
    return {
      optionData: [
        { label: '审批中', value: 1 },
        { label: '已通过', value: 2 },
        { label: '不通过', value: 3 },
        { label: '已取消', value: 4 },
        { label: '退回', value: 5 },
        { label: '委派', value: 6 },
        { label: '审批通过中', value: 7 },
        { label: '待审批', value: 0 }
      ],
      activityLists: [],
      processInstance: {},
      taskList: [],
      bpmnCanvas: null,
      element: null,
      elementOverlayIds: null,
      bpmnModeler: null,
      overlays: null,
      xml: this.value,
      activityList: this.activityData,
    }
  },
  watch: {
    value(val) {
      this.xml = val
      this.createNewDiagram(this.xml)
    },
    activityData(val) {
      this.activityLists = val
      // this.highlightDiagram()
      this.createNewDiagram(this.xml)
    },
    processInstanceData(val) {
      this.processInstance = val
      // this.highlightDiagram()
      this.createNewDiagram(this.xml)
    },
    taskData(val) {
      this.taskList = val
      // this.highlightDiagram()
      this.createNewDiagram(this.xml)
    }
  },
  mounted() {
    this.xml = this.value
    this.activityLists = this.activityData
    // 初始化
    this.initBpmnModeler()
    // this.createNewDiagram(this.xml)
    // 初始模型的监听器
    // this.initModelListeners()
  },
  beforeDestroy() {
    // this.$once('hook:beforeDestroy', () => {
    // })
    if (this.bpmnModeler) this.bpmnModeler.destroy()
    this.$emit('destroy', this.bpmnModeler)
    this.bpmnModeler = null
  },
  methods: {
    initBpmnModeler() {
      if (this.bpmnModeler) return
      this.bpmnModeler = new BpmnViewer({
        container: this.$refs.bpmnCanvas,
        bpmnRenderer: {},
      })
    },
    /* 创建新的流程图 */
    async createNewDiagram(xml) {
      // 将字符串转换成图显示出来
      const newId = `Process_${new Date().getTime()}`
      const newName = `业务流程_${new Date().getTime()}`
      const xmlString = xml || DefaultEmptyXML(newId, newName, this.prefix)
      try {
        const { warnings } = await this.bpmnModeler.importXML(xmlString)
        if (warnings && warnings.length) {
          warnings.forEach((warn) => console.warn(warn))
        }
        // 高亮流程图
        this.highlightDiagram()
        const canvas = this.bpmnModeler.get('canvas')
        canvas.zoom('fit-viewport', 'auto')
      } catch (e) {
        console.error(e)
        // console.error(`[Process Designer Warn]: ${e?.message || e}`);
      }
    },
    /* 高亮流程图 */
    // TODO：如果多个 endActivity 的话，目前的逻辑可能有一定的问题。https://www.jdon.com/workflow/multi-events.html
    highlightDiagram() {
      // 过滤掉或签中取消审批的数据
      const activityList = this.activityLists.filter((activity) => activity.deleteReason !== 'MI_END')
      if (activityList.length === 0) { return }
      // 参考自 https://gitee.com/tony2y/RuoYi-flowable/blob/master/ruoyi-ui/src/components/Process/index.vue#L222 实现
      // 在此基础上，增加不同审批结果的颜色等等
      const canvas = this.bpmnModeler.get('canvas')
      const todoActivity = activityList.find((m) => !m.endTime) // 找到待办的任务
      const endActivity = activityList[activityList.length - 1] // 获得最后一个任务
      let findProcessTask = false //是否已经高亮了进行中的任务
      //进行中高亮之后的任务 key 集合，用于过滤掉 taskList 进行中后面的任务，避免进行中后面的数据 Hover 还有数据
      let removeTaskDefinitionKeyList = []
      this.bpmnModeler
        .getDefinitions()
        .rootElements[0].flowElements.forEach((n) => {
          const activity = activityList.find((m) => m.key === n.id) // 找到对应的活动
          if (!activity) { return }
          if (n.$type === 'bpmn:UserTask') {
            // 用户任务
            // 处理用户任务的高亮
            const task = this.taskList.find((m) => m.id === activity.taskId) // 找到活动对应的 taskId
            if (!task) { return }
            // 进行中的任务已经高亮过了，则不高亮后面的任务了
            if (findProcessTask) {
              removeTaskDefinitionKeyList.push(n.id)
              return
            }
            // 高亮任务
            canvas.addMarker(n.id, this.getResultCss(task.status))
            //标记是否高亮了进行中任务
            if (task.status === 1) {
              findProcessTask = true
            }
            // 如果非通过，就不走后面的线条了
            if (task.status !== 2) {
              return
            }
            // 处理 outgoing 出线
            const outgoing = this.getActivityOutgoing(activity)
            if (outgoing) {
              outgoing.forEach((nn) => {
                // debugger
                let targetActivity = activityList.find((m) => m.key === nn.targetRef.id)
                // 如果目标活动存在，则根据该活动是否结束，进行【bpmn:SequenceFlow】连线的高亮设置
                if (targetActivity) {
                  canvas.addMarker(nn.id, targetActivity.endTime ? 'highlight' : 'highlight-todo')
                } else if (nn.targetRef.$type === 'bpmn:ExclusiveGateway') {
                  // TODO：这个流程，暂时没走到过
                  canvas.addMarker(nn.id, activity.endTime ? 'highlight' : 'highlight-todo')
                  canvas.addMarker(nn.targetRef.id, activity.endTime ? 'highlight' : 'highlight-todo')
                } else if (nn.targetRef.$type === 'bpmn:EndEvent') {
                  // TODO：这个流程，暂时没走到过
                  if (!todoActivity && endActivity.key === n.id) {
                    canvas.addMarker(nn.id, 'highlight')
                    canvas.addMarker(nn.targetRef.id, 'highlight')
                  }
                  if (!activity.endTime) {
                    canvas.addMarker(nn.id, 'highlight-todo')
                    canvas.addMarker(nn.targetRef.id, 'highlight-todo')
                  }
                }
              })
            }
          } else if (n.$type === 'bpmn:ExclusiveGateway') {
            // 排它网关
            // 设置【bpmn:ExclusiveGateway】排它网关的高亮
            canvas.addMarker(n.id, this.getActivityHighlightCss(activity))
            // 查找需要高亮的连线
            let matchNN = undefined
            let matchActivity = undefined
            if (n.outgoing) {
              n.outgoing.forEach((nn) => {
                let targetActivity = activityList.find((m) => m.key === nn.targetRef.id)
                if (!targetActivity) {
                  return
                }
                // 特殊判断 endEvent 类型的原因，ExclusiveGateway 可能后续连有 2 个路径：
                //  1. 一个是 UserTask => EndEvent
                //  2. 一个是 EndEvent
                // 在选择路径 1 时，其实 EndEvent 可能也存在，导致 1 和 2 都高亮，显然是不正确的。
                // 所以，在 matchActivity 为 EndEvent 时，需要进行覆盖~~
                if (!matchActivity || matchActivity.type === 'endEvent') {
                  matchNN = nn
                  matchActivity = targetActivity
                }
              })
            }
            if (matchNN && matchActivity) {
              canvas.addMarker(matchNN.id, this.getActivityHighlightCss(matchActivity))
            }
          } else if (n.$type === 'bpmn:ParallelGateway') {
            // 并行网关
            // 设置【bpmn:ParallelGateway】并行网关的高亮
            canvas.addMarker(n.id, this.getActivityHighlightCss(activity))
            if (n.outgoing) {
              n.outgoing.forEach((nn) => {
                // 获得连线是否有指向目标。如果有，则进行高亮
                const targetActivity = activityList.find((m) => m.key === nn.targetRef.id)
                if (targetActivity) {
                  canvas.addMarker(nn.id, this.getActivityHighlightCss(targetActivity)) // 高亮【bpmn:SequenceFlow】连线
                  // 高亮【...】目标。其中 ... 可以是 bpm:UserTask、也可以是其它的。当然，如果是 bpm:UserTask 的话，其实不做高亮也没问题，因为上面有逻辑做了这块。
                  canvas.addMarker(nn.targetRef.id, this.getActivityHighlightCss(targetActivity))
                }
              })
            }
          } else if (n.$type === 'bpmn:StartEvent') {
            // 开始节点
            canvas.addMarker(n.id, 'highlight')
            if (n.outgoing) {
              n.outgoing.forEach((nn) => {
                // outgoing 例如说【bpmn:SequenceFlow】连线
                // 获得连线是否有指向目标。如果有，则进行高亮
                const targetActivity = activityList.find((m) => m.key === nn.targetRef.id)
                if (targetActivity) {
                  canvas.addMarker(nn.id, 'highlight') // 高亮【bpmn:SequenceFlow】连线
                  canvas.addMarker(n.id, 'highlight') // 高亮【bpmn:StartEvent】开始节点（自己）
                }
              })
            }
          } else if (n.$type === 'bpmn:EndEvent') {
            // 结束节点
            if (!this.processInstance || this.processInstance.status === 1) {
              return
            }
            canvas.addMarker(n.id, this.getResultCss(this.processInstance.status))
          } else if (n.$type === 'bpmn:ServiceTask') {
            //服务任务
            if (activity.startTime > 0 && activity.endTime === 0) {
              //进入执行，标识进行色
              canvas.addMarker(n.id, this.getResultCss(1))
            }
            if (activity.endTime > 0) {
              // 执行完成，节点标识完成色, 所有outgoing标识完成色。
              canvas.addMarker(n.id, this.getResultCss(2))
              const outgoing = this.getActivityOutgoing(activity)
              if (outgoing) {
                outgoing.forEach((out) => {
                  canvas.addMarker(out.id, this.getResultCss(2))
                })
              }
            }
          } else if (n.$type === 'bpmn:SequenceFlow') {
            let targetActivity = activityList.find((m) => m.key === n.targetRef.id)
            if (targetActivity) {
              canvas.addMarker(n.id, this.getActivityHighlightCss(targetActivity))
            }
          }
        })
      if (!isEmpty(removeTaskDefinitionKeyList)) {
        this.taskList = this.taskList.filter(
          (item) => !removeTaskDefinitionKeyList.includes(item.taskDefinitionKey)
        )
      }
    },
    getActivityHighlightCss(activity) {
      return activity.endTime ? 'highlight' : 'highlight-todo'
    },
    getResultCss(status) {
      if (status === 1) {
        // 审批中
        return 'highlight-todo'
      } else if (status === 2) {
        // 已通过
        return 'highlight'
      } else if (status === 3) {
        // 不通过
        return 'highlight-reject'
      } else if (status === 4) {
        // 已取消
        return 'highlight-cancel'
      } else if (status === 5) {
        // 退回
        return 'highlight-return'
      } else if (status === 6) {
        // 委派
        return 'highlight-todo'
      } else if (status === 7) {
        // 审批通过中
        return 'highlight-todo'
      } else if (status === 0) {
        // 待审批
        return 'highlight-todo'
      }
      return ''
    },
    getActivityOutgoing(activity) {
      // 如果有 outgoing，则直接使用它
      if (activity.outgoing && activity.outgoing.length > 0) {
        return activity.outgoing
      }
      // 如果没有，则遍历获得起点为它的【bpmn:SequenceFlow】节点们。原因是：bpmn-js 的 UserTask 拿不到 outgoing
      const flowElements = this.bpmnModeler.getDefinitions().rootElements[0].flowElements
      const outgoing = []
      if (flowElements) {
        flowElements.forEach((item) => {
          if (item.$type !== 'bpmn:SequenceFlow') {
            return
          }
          if (item.sourceRef.id === activity.key) {
            outgoing.push(item)
          }
        })
      }
      return outgoing
    },
    initModelListeners() {
      const EventBus = this.bpmnModeler.get('eventBus')
      // 注册需要的监听事件
      EventBus.on('element.hover', function (eventObj) {
        let element = eventObj ? eventObj.element : null
        this.elementHover(element)
      })
      EventBus.on('element.out', function (eventObj) {
        let element = eventObj ? eventObj.element : null
        this.elementOut(element)
      })
    },
    // 流程图的元素被 hover
    elementHover(element) {
      this.element = element
      !this.elementOverlayIds && (this.elementOverlayIds = {})
      !this.overlays && (this.overlays = this.bpmnModeler.get('overlays'))
      // 展示信息
      const activity = this.activityLists.find((m) => m.key === this.element.id)
      if (!activity) {
        return
      }
      if (
        !this.elementOverlayIds[this.element.id] &&
        [
          'bpmn:StartEvent',
          'bpmn:UserTask',
          'bpmn:ServiceTask',
          'bpmn:ServiceTask',
          'bpmn:EndEvent',
        ].includes(this.element.type)
      ) {
        let html = `` // 默认值
        if (this.element.type === 'bpmn:StartEvent' && this.processInstance) {
          html = `<p>发起人：${this.processInstance.startUser.nickname}</p>
                  <p>部门：${this.processInstance.startUser.deptName}</p>
                  <p>创建时间：${formatTime(this.processInstance.createTime)}`
        } else if (this.element.type === 'bpmn:UserTask') {
          let task = this.taskList.find((m) => m.id === activity.taskId) // 找到活动对应的 taskId
          if (!task) {
            return
          }
          // let optionData = getIntDictOptions(DICT_TYPE.BPM_TASK_STATUS)
          let optionData = this.optionData
          let dataResult = ''
          optionData.forEach((element) => {
            if (this.element == task.status) {
              dataResult = element.label
            }
          })
          html = `<p>审批人：${task.assigneeUser.nickname}</p>
                  <p>部门：${task.assigneeUser.deptName}</p>
                  <p>结果：${dataResult}</p>
                  <p>创建时间：${formatTime(task.createTime)}</p>`
          // html = `<p>审批人：${task.assigneeUser.nickname}</p>
          //             <p>部门：${task.assigneeUser.deptName}</p>
          //             <p>结果：${getIntDictOptions(
          //               DICT_TYPE.BPM_PROCESS_INSTANCE_RESULT,
          //               task.status
          //             )}</p>
          //             <p>创建时间：${formatTime(task.createTime)}</p>`
          if (task.endTime) {
            html += `<p>结束时间：${formatTime(task.endTime)}</p>`
          }
          if (task.reason) {
            html += `<p>审批建议：${task.reason}</p>`
          }
        } else if (
          this.element.type === 'bpmn:ServiceTask' &&
          this.processInstance
        ) {
          if (activity.startTime > 0) {
            html = `<p>创建时间：${formatTime(activity.startTime)}</p>`
          }
          if (activity.endTime > 0) {
            html += `<p>结束时间：${formatTime(activity.endTime)}</p>`
          }
        } else if (
          this.element.type === 'bpmn:EndEvent' &&
          this.processInstance
        ) {
          let optionData = getIntDictOptions(DICT_TYPE.BPM_TASK_STATUS)
          let dataResult = ''
          optionData.forEach((element) => {
            if (this.element == this.processInstance.status) {
              dataResult = element.label
            }
          })
          html = `<p>结果：${dataResult}</p>`
          // html = `<p>结果：${getIntDictOptions(
          //   DICT_TYPE.BPM_PROCESS_INSTANCE_RESULT,
          //   this.processInstance.status
          // )}</p>`
          if (this.processInstance.endTime) {
            html += `<p>结束时间：${formatTime(this.processInstance.endTime)}</p>`
          }
        }
        if (toRaw(overlays.value)) {
          this.elementOverlayIds[this.element.id] = toRaw(overlays.value).add(
            this.element,
            {
              position: { right: -10, top: -50 },
              html: `<div class="element-overlays">${html}</div>`,
            }
          )
        }
      }
    },
    // 流程图的元素被 out
    elementOut(element) {
      toRaw(this.overlays).remove({ element })
      this.elementOverlayIds[element.id] = null
    }
  }
}
</script>

<style lang="scss" scoped>
/** 处理中 */
.highlight-todo.djs-connection > .djs-visual > path {
  stroke: #1890ff !important;
  stroke-dasharray: 4px !important;
  fill-opacity: 0.2 !important;
}

.highlight-todo.djs-shape .djs-visual > :nth-child(1) {
  fill: #1890ff !important;
  stroke: #1890ff !important;
  stroke-dasharray: 4px !important;
  fill-opacity: 0.2 !important;
}

:deep(.highlight-todo.djs-connection > .djs-visual > path) {
  stroke: #1890ff !important;
  stroke-dasharray: 4px !important;
  fill-opacity: 0.2 !important;
  marker-end: url('#sequenceflow-end-_E7DFDF-_E7DFDF-803g1kf6zwzmcig1y2ulm5egr');
}

:deep(.highlight-todo.djs-shape .djs-visual > :nth-child(1)) {
  fill: #1890ff !important;
  stroke: #1890ff !important;
  stroke-dasharray: 4px !important;
  fill-opacity: 0.2 !important;
}

/** 通过 */
.highlight.djs-shape .djs-visual > :nth-child(1) {
  fill: green !important;
  stroke: green !important;
  fill-opacity: 0.2 !important;
}

.highlight.djs-shape .djs-visual > :nth-child(2) {
  fill: green !important;
}

.highlight.djs-shape .djs-visual > path {
  fill: green !important;
  fill-opacity: 0.2 !important;
  stroke: green !important;
}

.highlight.djs-connection > .djs-visual > path {
  stroke: green !important;
}

.highlight:not(.djs-connection) .djs-visual > :nth-child(1) {
  fill: green !important; /* color elements as green */
}

:deep(.highlight.djs-shape .djs-visual > :nth-child(1)) {
  fill: green !important;
  stroke: green !important;
  fill-opacity: 0.2 !important;
}

:deep(.highlight.djs-shape .djs-visual > :nth-child(2)) {
  fill: green !important;
}

:deep(.highlight.djs-shape .djs-visual > path) {
  fill: green !important;
  fill-opacity: 0.2 !important;
  stroke: green !important;
}

:deep(.highlight.djs-connection > .djs-visual > path) {
  stroke: green !important;
}

.djs-element.highlight > .djs-visual > path {
  stroke: green !important;
}

/** 不通过 */
.highlight-reject.djs-shape .djs-visual > :nth-child(1) {
  fill: red !important;
  stroke: red !important;
  fill-opacity: 0.2 !important;
}

.highlight-reject.djs-shape .djs-visual > :nth-child(2) {
  fill: red !important;
}

.highlight-reject.djs-shape .djs-visual > path {
  fill: red !important;
  fill-opacity: 0.2 !important;
  stroke: red !important;
}

.highlight-reject.djs-connection > .djs-visual > path {
  stroke: red !important;
  marker-end: url(#sequenceflow-end-white-success) !important;
}

.highlight-reject:not(.djs-connection) .djs-visual > :nth-child(1) {
  fill: red !important; /* color elements as green */
}

:deep(.highlight-reject.djs-shape .djs-visual > :nth-child(1)) {
  fill: red !important;
  stroke: red !important;
  fill-opacity: 0.2 !important;
}

:deep(.highlight-reject.djs-shape .djs-visual > :nth-child(2)) {
  fill: red !important;
}

:deep(.highlight-reject.djs-shape .djs-visual > path) {
  fill: red !important;
  fill-opacity: 0.2 !important;
  stroke: red !important;
}

:deep(.highlight-reject.djs-connection > .djs-visual > path) {
  stroke: red !important;
}

/** 已取消 */
.highlight-cancel.djs-shape .djs-visual > :nth-child(1) {
  fill: grey !important;
  stroke: grey !important;
  fill-opacity: 0.2 !important;
}

.highlight-cancel.djs-shape .djs-visual > :nth-child(2) {
  fill: grey !important;
}

.highlight-cancel.djs-shape .djs-visual > path {
  fill: grey !important;
  fill-opacity: 0.2 !important;
  stroke: grey !important;
}

.highlight-cancel.djs-connection > .djs-visual > path {
  stroke: grey !important;
}

.highlight-cancel:not(.djs-connection) .djs-visual > :nth-child(1) {
  fill: grey !important; /* color elements as green */
}

:deep(.highlight-cancel.djs-shape .djs-visual > :nth-child(1)) {
  fill: grey !important;
  stroke: grey !important;
  fill-opacity: 0.2 !important;
}

:deep(.highlight-cancel.djs-shape .djs-visual > :nth-child(2)) {
  fill: grey !important;
}

:deep(.highlight-cancel.djs-shape .djs-visual > path) {
  fill: grey !important;
  fill-opacity: 0.2 !important;
  stroke: grey !important;
}

:deep(.highlight-cancel.djs-connection > .djs-visual > path) {
  stroke: grey !important;
}

/** 回退 */
.highlight-return.djs-shape .djs-visual > :nth-child(1) {
  fill: #e6a23c !important;
  stroke: #e6a23c !important;
  fill-opacity: 0.2 !important;
}

.highlight-return.djs-shape .djs-visual > :nth-child(2) {
  fill: #e6a23c !important;
}

.highlight-return.djs-shape .djs-visual > path {
  fill: #e6a23c !important;
  fill-opacity: 0.2 !important;
  stroke: #e6a23c !important;
}

.highlight-return.djs-connection > .djs-visual > path {
  stroke: #e6a23c !important;
}

.highlight-return:not(.djs-connection) .djs-visual > :nth-child(1) {
  fill: #e6a23c !important; /* color elements as green */
}

:deep(.highlight-return.djs-shape .djs-visual > :nth-child(1)) {
  fill: #e6a23c !important;
  stroke: #e6a23c !important;
  fill-opacity: 0.2 !important;
}

:deep(.highlight-return.djs-shape .djs-visual > :nth-child(2)) {
  fill: #e6a23c !important;
}

:deep(.highlight-return.djs-shape .djs-visual > path) {
  fill: #e6a23c !important;
  fill-opacity: 0.2 !important;
  stroke: #e6a23c !important;
}

:deep(.highlight-return.djs-connection > .djs-visual > path) {
  stroke: #e6a23c !important;
}

.element-overlays {
  width: 200px;
  padding: 8px;
  color: #fafafa;
  background: #00000080;
  border-radius: 4px;
  box-sizing: border-box;
  z-index: 1000;
}
</style>
