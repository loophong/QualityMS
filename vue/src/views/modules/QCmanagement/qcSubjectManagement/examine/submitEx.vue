<template>
  <div>
    <div ref="treeChart" style="width: 100%; height: 500px;"></div>
    <el-dialog title="课题提交" :visible.sync="showDialog1">
      <el-form :model="form1">
        <el-form-item label="审核结果" :label-width="formLabelWidth">
          <el-select v-model="form1.result" placeholder="" :disabled="!isAuth('qcSubject:plan:submit')">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form1.comment" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog1 = false">取 消</el-button>
        <el-button type="primary" @click="showDialog1 = false">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="成果认定" :visible.sync="showDialog2">
      <el-form :model="form2">
        <el-form-item label="审核结果" :label-width="formLabelWidth">
          <el-select v-model="form2.result" placeholder=""
            :disabled="!isAuth('qcExamine:Achievement:recognition') || dataList[0].status != 'B'">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form2.comment" autocomplete="off"
            :disabled="!isAuth('qcExamine:Achievement:recognition') || dataList[0].status != 'B'"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog2 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form2.id)">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="相关方审核" :visible.sync="showDialog3">
      <el-form :model="form3">
        <el-form-item label="审核结果" :label-width="formLabelWidth">
          <el-select v-model="form3.result" placeholder=""
            :disabled="!isAuth('qcExamine:interested:party') || dataList[1].status != 'B' || ifSelected">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form3.comment" autocomplete="off"
            :disabled="!isAuth('qcExamine:interested:party') || dataList[1].status != 'B' || ifSelected"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog3 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form3.id)">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="成果初评" :visible.sync="showDialog4" width="60%">
      <el-form :model="form4">
        <table width="100%">
          <thead>
            <tr>
              <th>序号</th>
              <th colspan="3">评价项目</th>
              <th>评审内容</th>
              <th>配分</th>
              <th>备注</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td colspan="7">初评标准:(满分100分)</td>
            </tr>
            <tr>
              <td rowspan="5">1</td>
              <td rowspan="5">活动过程<br />的规范性</td>
              <td>1</td>
              <td>否则项</td>
              <td>
                <ul>
                  <li>1、课题未完成，无具体措施或未实施，或实施无效果。</li>
                  <li>2、虚报数据，弄虚做假。</li>
                  <li>{{ '3、课题为单人独立完成课题或小组人数<3人(建议申报改善提案）' }}</li>
                </ul>
              </td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td>1.1</td>
              <td>选题内容</td>
              <td>
                <ul>
                  <li>(1)解决市场反馈问题的成果(20分);</li>
                  <li>(2)解决生产过程质量问题的成果(16分);</li>
                  <li>(3)其他类改善成果(10分)。</li>
                </ul>
              </td>
              <td>
                <el-input v-model="tableData[0]"
                  :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B'">
                  <template slot="append">分</template>
                </el-input>
              </td>
              <td rowspan="4">推进组</td>
            </tr>
            <tr>
              <td>1.2</td>
              <td>PDCA完整<br />性、步骤<br />连贯性、申报材料<br />规范</td>
              <td>
                <ul>
                  <li>(1)活动程序按要求进行，活动步骤完整、正确(10分);</li>
                  <li>(2)现状调查充分，有数据支撑，为目标设定提供依据(6分);</li>
                  <li>(3)目标设定恰当、且量化目标并有一定依据(6分);</li>
                  <li>(4)原因分析透彻，且用数据、事实客观证明主要原因(6分);</li>
                  <li>(5)针对主因逐条制定对策，制定对策应按5W1H原则(6分);</li>
                  <li>(6)资料以图表、数据为主(6分)。</li>
                </ul>
              </td>
              <td>
                <el-input v-model="tableData[1]"
                  :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B'">
                  <template slot="append">分</template>
                </el-input>
              </td>
            </tr>
            <tr>
              <td>1.3</td>
              <td>正确使用<br />改进工具</td>
              <td>正确且适宜应用超过4种QC新老七种工具(每种工具5分，错用不得分);</td>
              <td>
                <el-input v-model="tableData[2]"
                  :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B'">
                  <template slot="append">分</template>
                </el-input>
              </td>
            </tr>
            <tr>
              <td>1.4</td>
              <td>改进效果</td>
              <td>
                <ul>
                  <li>(1)取得效果后与原状比较,确认其改进的有效性，与所制订的目标比较，看其是否已达到(7分);</li>
                  <li>(2)有经济效益计算，有经济效益计算(10分)，无经济效益计算不得分;</li>
                  <li>(3)有无形效益的评价(3分)。</li>
                </ul>
              </td>
              <td>
                <el-input v-model="tableData[3]"
                  :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B'">
                  <template slot="append">分</template>
                </el-input>
              </td>
            </tr>
          </tbody>
        </table>
        <br>
        <el-form-item label="初评等级" :label-width="formLabelWidth">
          <el-select v-model="form4.level" placeholder=""
            :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B'">
            <el-option label="A" value="A"></el-option>
            <el-option label="B" value="B"></el-option>
            <el-option label="C" value="C"></el-option>
            <el-option label="D" value="D"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核结果" :label-width="formLabelWidth">
          <el-select v-model="form4.result" placeholder=""
            :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B'">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form4.comment" autocomplete="off"
            :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B'"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog4 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form4.id)">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="成果复评" :visible.sync="showDialog5" width="60%">
      <table>
        <thead>
          <tr>
            <th colspan="7">
              <h2>QC活动成果评分细则</h2>
            </th>
          </tr>
          <tr>
            <th>序号</th>
            <th colspan="3">评价项目</th>
            <th>评审内容</th>
            <th>配分</th>
            <th>备注</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>2</td>
            <td colspan="3">成果的经济效益</td>
            <td>
              <ul>
                <li>高收益:2万元以上(32-40分);</li>
                <li>较高收益:5000万元以上(16-28分);</li>
                <li>有一定收益:2000元以上(1-12分)。</li>
              </ul>
            </td>
            <td>
              <el-input v-model="finalData[0]"
                :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B'">
                <template slot="append">分</template>
              </el-input>
            </td>
            <td>评价组</td>
          </tr>
          <tr>
            <td>3</td>
            <td colspan="3">成果的创新性</td>
            <td>
              <ul>
                <li>原创:发明创造、新管理方法推广、技术改进行业领先(16-20分);</li>
                <li>集成:多种成熟技术综合运用、公司内首创、部门范围内管理方法推广(8-14分);</li>
                <li>引进:单项成熟技术应用(1-6分)。</li>
              </ul>
            </td>
            <td>
              <el-input v-model="finalData[1]"
                :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B'">
                <template slot="append">分</template>
              </el-input>
            </td>
            <td>评价组</td>
          </tr>
          <tr>
            <td>4</td>
            <td colspan="3">成果推广价值</td>
            <td>
              <ul>
                <li>很强：制度、标准、流程已在公司内得到应用的;模式可直接在企业管理中参照借鉴得到应用的(16-20分);</li>
                <li>较强：制度、标准、流程可直接在公司内参照借鉴得到应用的(8-14分);</li>
                <li>强：经验在集公司内举一反三容易得到应用(1-6分)。</li>
              </ul>
            </td>
            <td>
              <el-input v-model="finalData[2]"
                :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B'">
                <template slot="append">分</template>
              </el-input>
            </td>
            <td>评价组</td>
          </tr>
          <tr>
            <td>5</td>
            <td colspan="3">现场发布效果</td>
            <td>
              <ul>
                <li>(1)发布人自信大方、语言流利、口齿清楚(6分);</li>
                <li>(2)发表资料应以图、表、数据为主，避免通篇文字、照本宣读(6分);</li>
                <li>(3)时间控制在8分钟以内(4分),超时自动关闭,超时1分钟不得分;</li>
                <li>(4)小组成员发布，非小组成员发布不得分(4分)。</li>
              </ul>
            </td>
            <td>
              <el-input v-model="finalData[3]"
                :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B'">
                <template slot="append">分</template>
              </el-input>
            </td>
            <td>评价组</td>
          </tr>
        </tbody>
      </table>
      <br>
      <el-form :model="form5">
        <el-form-item label="复评等级" :label-width="formLabelWidth">
          <el-select v-model="form5.level" placeholder=""
            :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B'">
            <el-option label="A" value="A"></el-option>
            <el-option label="B" value="B"></el-option>
            <el-option label="C" value="C"></el-option>
            <el-option label="D" value="D"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核结果" :label-width="formLabelWidth">
          <el-select v-model="form5.result" placeholder=""
            :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B'">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form5.comment" autocomplete="off"
            :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B'"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog5 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form5.id)">确 定</el-button>
      </div>

    </el-dialog>
    <el-dialog title="财务部审核" :visible.sync="showDialog6">
      <el-form :model="form6">
        <el-form-item label="审核结果" :label-width="formLabelWidth">
          <el-select v-model="form6.result" placeholder=""
            :disabled="!isAuth('qcExamine:finance:department') || dataList[4].status != 'B'">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form6.comment" autocomplete="off"
            :disabled="!isAuth('qcExamine:finance:department') || dataList[4].status != 'B'"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog6 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form6.id)">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="终评提交" :visible.sync="showDialog7">
      <final-score-table />
      <br>
      <el-form :model="form7">
        <el-form-item label="审核结果" :label-width="formLabelWidth">
          <el-select v-model="form7.result" placeholder=""
            :disabled="!isAuth('qcExamine:final:submit') || dataList[5].status != 'B'">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form7.comment" autocomplete="off"
            :disabled="!isAuth('qcExamine:final:submit') || dataList[5].status != 'B'"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog7 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form7.id)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as d3 from 'd3';
export default {

  data() {
    return {
      ifSelected: false,
      tableData: [20, 40, 20, 20],
      finalData: [20, 40, 20, 20],
      formLabelWidth: '120px',
      showDialog1: false,
      showDialog2: false,
      showDialog3: false,
      showDialog4: false,
      showDialog5: false,
      showDialog6: false,
      showDialog7: false,
      form1: {
        id: 1,
        result: '',
        comment: '',
      },
      form2: {
        id: 2,
        result: '',
        comment: '',
        qcSecondScore: [],
      },
      form3: {
        id: 3,
        result: '',
        comment: '',
      },
      form4: {
        id: 4,
        result: '',
        comment: '',
        level: '',
      },
      form5: {
        level: '',
        id: 5,
        result: '',
        comment: '',
      },
      form6: {
        id: 6,
        result: '',
        comment: '',
      },
      form7: {
        id: 7,
        result: '',
        comment: '',
      },
      dataList: [
        {
          id: 2,
          name: '成果认定',
          parentName: '课题提交',
          status: '',
        },
        {
          id: 3,
          name: '相关方审核',
          parentName: '成果认定',
          status: 'B',
        },
        {
          id: 4,
          name: '成果初评',
          parentName: '相关方审核',
          status: 'B',
        },
        {
          id: 5,
          name: '成果复评',
          parentName: '成果初评',
          status: 'B',
        },
        {
          id: 6,
          name: '财务部审核',
          parentName: '成果初评',
          status: 'B',
        },
        {
          id: 7,
          name: '终评提交',
          parentName: '成果复评',
          status: 'B',
        }
      ],
      routerParam: {
        qcsrId: '',
        topicName: '',
        resultType: '',
        examineId: '',
      },
      value: '',
      dataForm: {
        key: ''
      },
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataFormEx: {
        qcExamineId: 0,
        qcExamineSubject: '',
        qcExamineCurrent: '',
        qcStatusOne: '',
        qcStatusTwo: '',
        qcStatusThree: '',
        qcStatusFour: '',
        qcStatusFive: '',
        qcStatusSix: '',
        qcStatusSeven: '',
        qcSevenContent: '',
        qcSixContent: '',
        qcFiveContent: '',
        qcSecondScore: '',
        qcFourContent: '',
        qcFirstScore: '',
        qcThreeContent: '',
        qcTwoContent: '',
        qcOneContent: '',
      },
    };
  },

  mounted() {
    this.initRouterParam()
    this.getStatusList()
    const tmp = this.ifSelectedPart()
    console.log(tmp)
  },
  computed: {

  },
  methods: {

    //处理跳转参数
    initRouterParam() {
      try {
        this.routerParam = this.$route.query.data ? JSON.parse(this.$route.query.data) : { qcsrId: '', topicName: '', topicType: '', resultType: '', examineId: '' };
        this.ifSelected = this.ifSelectedPart()
        // console.log(this.routerParam, '++++++++++++')
      } catch (e) {
        console.log('处理跳转参数失败')
        console.log(e)
      }
    },
    //是否是选择的相关方
    ifSelectedPart() {
      if ((this.routerParam[0].resultType.includes('安全') && this.isAuth('qcExamine:interested:safety'))) {
        return true
      } else if ((this.routerParam[0].resultType.includes('设备') && this.isAuth('qcExamine:interested:instrument'))) {
        return true
      } else if ((this.routerParam[0].resultType.includes('质量') && this.isAuth('qcExamine:interested:quality'))) {
        return true
      } else if ((this.routerParam[0].resultType.includes('生产') && this.isAuth('qcExamine:interested:production'))) {
        return true
      } else if ((this.routerParam[0].resultType.includes('技术') && this.isAuth('qcExamine:interested:technology'))) {
        return true
      } else {
        return false
      }
    },
    async getStatusList() {
      await this.$http({
        // url: this.$http.adornUrl('/qcManagement/examineStatus/list'),
        url: this.$http.adornUrl(`/qcManagement/examineStatus/info/${this.routerParam[0].examineId}`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataFormEx = data.qcExamineStatus
          this.renderTree()
          // this.tableData = this.dataFormEx.qcFirstScore.split(',')
          this.tableData = this.dataFormEx.qcFirstScore ? this.dataFormEx.qcFirstScore.split(',') : []
          // this.finalData = this.dataFormEx.qcSecondScore.split(',')
          this.tableData = this.dataFormEx.qcSecondScore ? this.dataFormEx.qcSecondScore.split(',') : []
          this.form2.result = this.dataFormEx.qcStatusTwo
          this.form3.result = this.dataFormEx.qcStatusThree
          this.form4.result = this.dataFormEx.qcStatusFour
          this.form4.level = this.dataFormEx.qcFirstLevel
          this.form5.level = this.dataFormEx.qcSecondLevel
          this.form5.result = this.dataFormEx.qcStatusFive
          this.form6.result = this.dataFormEx.qcStatusSix
          this.form7.result = this.dataFormEx.qcStatusSeven
          this.form2.comment = this.dataFormEx.qcTwoContent
          this.form3.comment = this.dataFormEx.qcThreeContent
          this.form4.comment = this.dataFormEx.qcFourContent
          this.form5.comment = this.dataFormEx.qcFiveContent
          this.form6.comment = this.dataFormEx.qcSixContent
          this.form7.comment = this.dataFormEx.qcSevenContent
        } else {
          this.dataFormEx = {}
          console.log('获取数据失败')
          this.totalPage = 0
        }
      })
    },
    //初始化节点状态
    initStatus(statusId) {

      if (statusId === '1') {
        this.dataList[0].status = 'B'
        this.dataList[1].status = 'A'
      } else if (statusId === '2') {
        if (this.routerParam[0].resultType.includes('其他')) {
          this.dataList[0].status = 'C'
          this.dataList[1].status = 'E'
          this.dataList[2].status = 'B'
          this.dataList[3].status = 'A'
        } else {
          this.dataList[0].status = 'C'
          this.dataList[1].status = 'B'
          this.dataList[2].status = 'A'
        }
      } else if (statusId === '3') {
        this.dataList[0].status = 'C'
        this.dataList[1].status = this.routerParam[0].resultType.includes('其他') ? 'E' : 'C'
        this.dataList[2].status = 'B'
        this.dataList[3].status = 'A'
      } else if (statusId === '4') {
        this.dataList[0].status = 'C'
        this.dataList[1].status = this.routerParam[0].resultType.includes('其他') ? 'E' : 'C'
        this.dataList[2].status = 'C'
        this.dataList[3].status = 'B'
        this.dataList[4].status = 'B'
        this.dataList[5].status = 'A'
      } else if (statusId === '4.1' || statusId === '4.2') {
        if (statusId === '4.1') {
          this.dataList[0].status = 'C'
          this.dataList[1].status = this.routerParam[0].resultType.includes('其他') ? 'E' : 'C'
          this.dataList[2].status = 'C'
          this.dataList[3].status = 'C'
          this.dataList[4].status = 'B'
          this.dataList[5].status = 'A'
        } else {
          this.dataList[0].status = 'C'
          this.dataList[1].status = this.routerParam[0].resultType.includes('其他') ? 'E' : 'C'
          this.dataList[2].status = 'C'
          this.dataList[3].status = 'B'
          this.dataList[4].status = 'C'
          this.dataList[5].status = 'A'
        }
      } else if (statusId === '5') {
        this.dataList[0].status = 'C'
        this.dataList[1].status = this.routerParam[0].resultType.includes('其他') ? 'E' : 'C'
        this.dataList[2].status = 'C'
        this.dataList[3].status = 'C'
        this.dataList[4].status = 'C'
        this.dataList[5].status = 'B'
      } else {
        this.dataList[0].status = 'C'
        this.dataList[1].status = this.routerParam[0].resultType.includes('其他') ? 'E' : 'C'
        this.dataList[2].status = 'C'
        this.dataList[3].status = 'C'
        this.dataList[4].status = 'C'
        this.dataList[5].status = 'C'
      }


    },
    //渲染审核流程树
    renderTree() {

      // 创建根节点
      const rootNode = {
        id: 1,
        name: '课题提交',
        parentName: '',
        url: '',
        status: this.routerParam[0].resultType ? 'C' : 'A',
      };
      // console.log(this.dataFormEx[0])
      // const statusId = this.dataFormEx[0].qcExamineCurrent
      this.initStatus(this.dataFormEx.qcExamineCurrent) // 初始化状态
      // 更新状态
      let foundA = false;
      this.dataList.forEach(item => {
        if (foundA) {
          item.status = 'A';
        } else if (item.status === 'A') {
          foundA = true;
        }
      });
      const formattedData = [rootNode, ...this.dataList.map(item => ({
        id: item.id,
        name: item.name,
        parentName: item.parentName,
        // url: '/some-url-based-on-indicator-id/' + item.indicatorId,
        url: '',
        status: item.status,
      }))];

      const treeData = this.buildTree(formattedData);
      const margin = { top: 20, right: 120, bottom: 20, left: 120 };
      const width = 1400 - margin.right - margin.left;
      const height = 700 - margin.top - margin.bottom;

      const svg = d3.select(this.$refs.treeChart)
        .append('svg')
        .attr('width', width + margin.right + margin.left)
        .attr('height', height + margin.top + margin.bottom)
        .append('g')
        .attr('transform', `translate(${margin.left},${margin.top})`);

      const root = d3.hierarchy(treeData[0]);

      const treeLayout = d3.tree().size([height, width]);
      treeLayout(root);

      svg.selectAll('line.link')
        .data(root.links())
        .enter()
        .append('line')
        .attr('class', 'link')
        .attr('x1', d => d.source.y + 100)
        .attr('y1', d => d.source.x)
        .attr('x2', d => d.target.y - 100)
        .attr('y2', d => d.target.x)
        .style('stroke', '#ccc');

      const nodes = svg.selectAll('g.node')
        .data(root.descendants())
        .enter()
        .append('g')
        .attr('class', 'node')
        .attr('transform', d => `translate(${d.y},${d.x})`);

      // 根据指标等级设置矩形颜色
      const colorScale = {
        'E': '#1e1e1e', // 黑色  //跳过
        'D': '#e74c3c', // 红色  //未通过
        'C': '#0dbc79', // 绿色  //已通过
        'B': '#3498db',  // 蓝色 //进行中
        'A': '#717182'  // 灰色 //未提交
      };

      // 添加圆角矩形
      nodes.append('rect')
        .attr('width', 200)
        .attr('height', 80)
        .attr('x', -100)
        .attr('y', -25)
        .attr('rx', 10)
        .attr('ry', 10)
        .style('fill', d => {
          const color = colorScale[d.data.status] || '#000'; // 如果未匹配到颜色，使用默认黑色
          return color;
        })
        .on('click', (event, d) => {
          console.log('Clicked on1:', d); // 打印节点数据
          if (d.data.name === '课题提交') {
            this.showDialog1 = true;
            // console.log('++++++++++');
          } else if (d.data.name === '成果认定') {
            if (d.data.status === 'A') {
              this.$message.warning('请先等待上一步');
              return;
            } else {
              this.showDialog2 = true;
            }
          } else if (d.data.name === '相关方审核') {
            if (d.data.status === 'A') {
              this.$message.warning('请先等待上一步');
              return;
            } else if (d.data.status === 'E') {
              this.$message.warning('无该过程审核');
              return;
            } else {
              this.showDialog3 = true;
            }
          } else if (d.data.name === '成果初评') {
            if (d.data.status === 'A') {
              this.$message.warning('请先等待上一步');
              return;
            } else {
              this.showDialog4 = true;
            }
          } else if (d.data.name === '成果复评') {
            if (d.data.status === 'A') {
              this.$message.warning('请先等待上一步');
              return;
            } else {
              this.showDialog5 = true;
            }
          } else if (d.data.name === '财务部审核') {
            if (d.data.status === 'A') {
              this.$message.warning('请先等待上一步');
              return;
            } else {
              this.showDialog6 = true;
            }
          } else if (d.data.name === '终评提交') {
            if (d.data.status === 'A') {
              this.$message.warning('请先等待上一步');
              return;
            } else {
              this.showDialog7 = true;
            }

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
        .style('fill', 'white')
        .on('click', (event, d) => {
          console.log('Clicked on2:', d); // 打印节点数据
        });
    },
    buildTree(data, parentName = '') {
      return data
        .filter(item => item.parentName === parentName)  // 使用parentName来过滤
        .map(item => ({
          id: item.id,
          name: item.name,
          status: item.status,
          url: item.url,
          children: this.buildTree(data, item.name)  // 使用name来匹配
        }));
    },
    // 表单提交
    dataFormSubmitEx(formId) {
      if (formId === 2) {
        this.$http({
          url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
          method: 'post',
          data: this.$http.adornData({
            'qcExamineId': this.routerParam[0].examineId || undefined,
            'qcExamineSubject': this.routerParam[0].qcsrId,
            'qcExamineCurrent': this.form2.result === '1' ? '2' : '1',
            'qcStatusTwo': this.form2.result,
            'qcTwoContent': this.form2.comment,
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.renderTree()
                this.showDialog2 = false
                // console.log('++++++++++');
                // console.log(this.routerParam[0].examineId);
                // console.log(this.routerParam[0].qcsrId);
                // console.log(this.form2.result);
                // console.log(this.form2.comment);
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        }).catch((error) => {
          console.log(error)
        })
      } else if (formId === 3) {
        this.$http({
          url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
          method: 'post',
          data: this.$http.adornData({
            'qcExamineId': this.routerParam[0].examineId || undefined,
            'qcExamineSubject': this.routerParam[0].qcsrId,
            'qcExamineCurrent': this.form3.result === '1' ? '3' : '1',
            'qcStatusThree': this.form3.result,
            'qcThreeContent': this.form3.comment,
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.showDialog3 = false
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })

      } else if (formId === 4) {
        this.$http({
          url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
          method: 'post',
          data: this.$http.adornData({
            'qcExamineId': this.routerParam[0].examineId || undefined,
            'qcExamineSubject': this.routerParam[0].qcsrId,
            'qcExamineCurrent': this.form4.result === '1' ? '4' : '1',
            'qcFirstScore': `${this.tableData}`,
            'qcFirstLevel': this.form4.level,
            'qcStatusFour': this.form4.result,
            'qcFourContent': this.form4.comment,
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.showDialog4 = false
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })

      } else if (formId === 5) {
        if (this.form5.result === '1') {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
            method: 'post',
            data: this.$http.adornData({
              'qcExamineId': this.routerParam.examineId || undefined,
              'qcExamineSubject': this.routerParam.qcsrId,
              'qcExamineCurrent': this.dataFormEx.qcStatusSix === '1' ? '5' : '4.1',
              'qcStatusFive': this.form5.result,
              'qcSecondLevel': this.form5.level,
              'qcFiveContent': this.form5.comment,
              'qcSecondScore': `${this.finalData}`,
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.showDialog5 = false
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        } else {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
            method: 'post',
            data: this.$http.adornData({
              'qcExamineId': this.routerParam[0].examineId || undefined,
              'qcExamineSubject': this.routerParam[0].qcsrId,
              'qcExamineCurrent': '4',
              'qcStatusFive': this.form5.result,
              'qcStatusSix': '0',
              'qcSecondLevel': this.form5.level,
              'qcFiveContent': this.form5.comment,
              'qcSecondScore': `${this.finalData}`,
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.showDialog5 = false
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }


      } else if (formId === 6) {
        if (this.form6.result === '1') {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
            method: 'post',
            data: this.$http.adornData({
              'qcExamineId': this.routerParam.examineId || undefined,
              'qcExamineSubject': this.routerParam.qcsrId,
              'qcExamineCurrent': this.dataFormEx.qcStatusFive === '1' ? '5' : '4.2',
              'qcStatusSix': this.form6.result,
              'qcSixContent': this.form6.comment,
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.showDialog6 = false
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        } else {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
            method: 'post',
            data: this.$http.adornData({
              'qcExamineId': this.routerParam[0].examineId || undefined,
              'qcExamineSubject': this.routerParam[0].qcsrId,
              'qcExamineCurrent': '1',
              'qcStatusSix': this.form6.result,
              'qcStatusFive': '0',
              'qcSixContent': this.form6.comment,
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.showDialog6 = false
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }

      } else if (formId === 7) {
        if (this.form7.result === '1') {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
            method: 'post',
            data: this.$http.adornData({
              'qcExamineId': this.routerParam[0].examineId || undefined,
              'qcExamineSubject': this.routerParam[0].qcsrId,
              'qcExamineCurrent': '完成',
              'qcStatusSeven': this.form7.result,
              'qcSevenContent': this.form7.comment,
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.showDialog7 = false
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        } else {
          this.$http({
            url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
            method: 'post',
            data: this.$http.adornData({
              'qcExamineId': this.routerParam[0].examineId || undefined,
              'qcExamineSubject': this.routerParam[0].qcsrId,
              'qcExamineCurrent': '1',
              'qcStatusSeven': this.form7.result,
              'qcStatusFive': this.form7.result,
              'qcStatusSix': this.form7.result,
              'qcSevenContent': this.form7.comment,
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.showDialog7 = false
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }

      }

    },
  }
};
</script>

<style lang="scss" scoped>
ul,
li {
  margin: 0;
  padding: 0;
  list-style: none;
}

table {
  border-collapse: collapse;
  border: 1px solid #000;
  /* 设置表格的边框为单线，颜色为黑色 */
}

th,
td {
  border: 1px solid #000;
  /* 设置单元格的边框为单线，颜色为黑色 */
  padding: 8px;
}
</style>
