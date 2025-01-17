<template>

  <div>
    <div>
      <span style="color:#717182; font-size: 24px;">--未开始</span>
      <span style="color:#3498db; font-size: 24px;"> --进行中</span>
      <span style="color:#0dbc79; font-size: 24px;"> --已通过</span>
      <span style="color:#1e1e1e; font-size: 24px;"> --跳过</span>
    </div>
    <div ref="treeChart" style="width: 100%; height: 500px;"></div>
    <el-dialog title="科室审核" :visible.sync="showDialog1">
      <el-form :model="form1" ref="form1" :rules="rule">
        <el-form-item label="审核结果" :label-width="formLabelWidth" prop="result">
          <el-select v-model="form1.result" placeholder=""
            :disabled="!isAuth('qcExamine:department:submit') || rootNode.status != 'B' || !ifDepartment">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form1.comment" autocomplete="off"
            :disabled="!isAuth('qcExamine:department:submit') || rootNode.status != 'B' || !ifDepartment"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog1 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form1.id)"
          :disabled="!isAuth('qcExamine:department:submit') || rootNode.status != 'B' || !ifDepartment">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="成果认定" :visible.sync="showDialog2">
      <el-form :rules="rule" ref="form2" :model="form2">
        <el-form-item label="审核结果" :label-width="formLabelWidth" prop="result">
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
        <el-button type="primary" @click="dataFormSubmitEx(form2.id)"
          :disabled="!isAuth('qcExamine:Achievement:recognition') || dataList[0].status != 'B'">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="相关方审核" :visible.sync="showDialog3">
      <el-form :rules="rule" ref="form3" :model="form3">
        <el-form-item label="审核结果" :label-width="formLabelWidth" prop="result">
          <el-select v-model="form3.result" placeholder="" :disabled="dataList[1].status != 'B' || !ifSelected">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form3.comment" autocomplete="off"
            :disabled="dataList[1].status != 'B' || !ifSelected"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog3 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form3.id)"
          :disabled="dataList[1].status != 'B' || !ifSelected">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="成果初评" :visible.sync="showDialog4" width="60%">
      <el-tabs v-model="activeNameFirst" type="card" @tab-click="handleFirstChange(activeNameFirst)">
        <el-tab-pane v-for="(person, index) in routerParam[0].firstExaminePeople" :label="person" :name="person"
          :key="index"></el-tab-pane>
        <el-form :rules="rule" ref="form4" :model="form4">
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
                <td v-if="commentTable && commentTable[0]">{{ commentTable[0].tableNumber }}</td>
                <td v-if="commentTable && commentTable[0]">{{ commentTable[0].tableTag }}</td>
                <td>
                  <ul>
                    <li v-for="(item, index) in commentTableFirst[0]" :key="index">{{ item }}</li>
                  </ul>
                </td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td v-if="commentTable && commentTable[0]">{{ commentTable[1].tableNumber }}</td>
                <td v-if="commentTable && commentTable[0]">{{ commentTable[1].tableTag }}</td>
                <td>
                  <ul>
                    <li v-for="(item, index) in commentTableFirst[1]" :key="index">{{ item }}</li>
                  </ul>
                </td>
                <td>
                  <el-input v-model="tableData[0]"
                    :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B' || !activeNameFirst.includes(currentUserName)">
                    <template slot="append">分</template>
                  </el-input>
                </td>
                <td rowspan="4">推进组</td>
              </tr>
              <tr>
                <td v-if="commentTable && commentTable[0]">{{ commentTable[2].tableNumber }}</td>
                <td v-if="commentTable && commentTable[0]">{{ commentTable[2].tableTag }}</td>
                <td>
                  <ul>
                    <li v-for="(item, index) in commentTableFirst[2]" :key="index">{{ item }}</li>
                  </ul>
                </td>
                <td>
                  <el-input v-model="tableData[1]"
                    :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B' || !activeNameFirst.includes(currentUserName)">
                    <template slot="append">分</template>
                  </el-input>
                </td>
              </tr>
              <tr>
                <td v-if="commentTable && commentTable[0]"> {{ commentTable[3].tableNumber }}</td>
                <td v-if="commentTable && commentTable[0]">{{ commentTable[3].tableTag }}</td>
                <td>
                  <ul>
                    <li v-for="(item, index) in commentTableFirst[3]" :key="index">{{ item }}</li>
                  </ul>
                </td>
                <td>
                  <el-input v-model="tableData[2]"
                    :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B' || !activeNameFirst.includes(currentUserName)">
                    <template slot="append">分</template>
                  </el-input>
                </td>
              </tr>
              <tr>
                <td v-if="commentTable && commentTable[0]">{{ commentTable[4].tableNumber }}</td>
                <td v-if="commentTable && commentTable[0]">{{ commentTable[4].tableTag }}</td>
                <td>
                  <ul>
                    <li v-for="(item, index) in commentTableFirst[4]" :key="index">{{ item }}</li>
                  </ul>
                </td>
                <td>
                  <el-input v-model="tableData[3]"
                    :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B' || !activeNameFirst.includes(currentUserName)">
                    <template slot="append">分</template>
                  </el-input>
                </td>
              </tr>
            </tbody>
          </table>
          <br>
          <!-- <el-form-item label="初评等级" :label-width="formLabelWidth">
          <el-select v-model="form4.level" placeholder=""
            :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B'">
            <el-option label="A" value="A"></el-option>
            <el-option label="B" value="B"></el-option>
            <el-option label="C" value="C"></el-option>
            <el-option label="D" value="D"></el-option>
          </el-select>
        </el-form-item> -->
          <el-form-item label="审核结果" :label-width="formLabelWidth" prop="result">
            <el-select v-model="form4.result" placeholder=""
              :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B' || !activeNameFirst.includes(currentUserName)">
              <el-option label="通过" value="1"></el-option>
              <el-option label="不通过" value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="审核意见" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="form4.comment" autocomplete="off"
              :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B' || !activeNameFirst.includes(currentUserName)"></el-input>
          </el-form-item>
        </el-form>

      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog4 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form4.id)"
          :disabled="!isAuth('qcExamine:first:comment') || dataList[2].status != 'B' || !activeNameFirst.includes(currentUserName)">确
          定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="成果复评" :visible.sync="showDialog5" width="50%">
      <el-tabs v-model="activeNameSecond" type="card" @tab-click="handleSecondChange(activeNameSecond)">
        <el-tab-pane v-for="(person, index) in routerParam[0].secondExaminePeople" :label="person" :name="person"
          :key="index"></el-tab-pane>
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
              <td v-if="commentTable && commentTable[0]">{{ commentTable[5].tableNumber }}</td>
              <td colspan="3" v-if="commentTable && commentTable[0]">{{ commentTable[5].tableTag }}</td>
              <td>
                <ul>
                  <li v-for="(item, index) in commentTableSecond[0]" :key="index">{{ item }}</li>
                </ul>
              </td>
              <td>
                <el-input v-model="finalData[0]"
                  :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B' || !activeNameSecond.includes(currentUserName)">
                  <template slot="append">分</template>
                </el-input>
              </td>
              <td>评价组</td>
            </tr>
            <tr>
              <td v-if="commentTable && commentTable[0]">{{ commentTable[6].tableNumber }}</td>
              <td colspan="3" v-if="commentTable && commentTable[0]">{{ commentTable[6].tableTag }}</td>
              <td>
                <ul>
                  <li v-for="(item, index) in commentTableSecond[1]" :key="index">{{ item }}</li>
                </ul>
              </td>
              <td>
                <el-input v-model="finalData[1]"
                  :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B' || !activeNameSecond.includes(currentUserName)">
                  <template slot="append">分</template>
                </el-input>
              </td>
              <td>评价组</td>
            </tr>
            <tr>
              <td v-if="commentTable && commentTable[0]">{{ commentTable[7].tableNumber }}</td>
              <td colspan="3" v-if="commentTable && commentTable[0]">{{ commentTable[7].tableTag }}</td>
              <td>
                <ul>
                  <li v-for="(item, index) in commentTableSecond[2]" :key="index">{{ item }}</li>
                </ul>
              </td>
              <td>
                <el-input v-model="finalData[2]"
                  :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B' || !activeNameSecond.includes(currentUserName)">
                  <template slot="append">分</template>
                </el-input>
              </td>
              <td>评价组</td>
            </tr>
            <tr>
              <td v-if="commentTable && commentTable[0]">{{ commentTable[8].tableNumber }}</td>
              <td colspan="3" v-if="commentTable && commentTable[0]">{{ commentTable[8].tableTag }}</td>
              <td>
                <ul>
                  <li v-for="(item, index) in commentTableSecond[3]" :key="index">{{ item }}</li>
                </ul>
              </td>
              <td>
                <el-input v-model="finalData[3]"
                  :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B' || !activeNameSecond.includes(currentUserName)">
                  <template slot="append">分</template>
                </el-input>
              </td>
              <td>评价组</td>
            </tr>
          </tbody>
        </table>
        <br>
        <el-form :rules="rule" ref="form5" :model="form5">
          <!-- <el-form-item label="复评等级" :label-width="formLabelWidth">
          <el-select v-model="form5.level" placeholder=""
            :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B'">
            <el-option label="A" value="A"></el-option>
            <el-option label="B" value="B"></el-option>
            <el-option label="C" value="C"></el-option>
            <el-option label="D" value="D"></el-option>
          </el-select>
        </el-form-item> -->
          <el-form-item label="审核结果" :label-width="formLabelWidth" prop="result">
            <el-select v-model="form5.result" placeholder=""
              :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B' || !activeNameSecond.includes(currentUserName)">
              <el-option label="通过" value="1"></el-option>
              <el-option label="不通过" value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="审核意见" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="form5.comment" autocomplete="off"
              :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B' || !activeNameSecond.includes(currentUserName)"></el-input>
          </el-form-item>
        </el-form>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog5 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form5.id)"
          :disabled="!isAuth('qcExamine:second:comment') || dataList[3].status != 'B' || !activeNameSecond.includes(currentUserName)">确
          定</el-button>
      </div>

    </el-dialog>
    <el-dialog title="财务部审核" :visible.sync="showDialog6">
      <el-form :rules="rule" ref="form6" :model="form6">
        <el-form-item label="经济效益分析" :label-width="formLabelWidth">
          <el-input type="textarea" autosize v-model="currentEconomy" placeholder="" :disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="经济效益金额" :label-width="formLabelWidth">
          <el-input v-model="currentEconomyCount" placeholder="" :disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="审核结果" :label-width="formLabelWidth" prop="result">
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
        <el-button type="primary" @click="dataFormSubmitEx(form6.id)"
          :disabled="!isAuth('qcExamine:finance:department') || dataList[4].status != 'B'">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="终评提交" :visible.sync="showDialog7">

      <br>
      <el-form :rules="rule" ref="form7" :model="form7">
        <el-form-item label="复评平均分" :label-width="formLabelWidth">
          <el-input v-model="form7.avg" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="获奖等级" :label-width="formLabelWidth" prop="level">
          <el-select v-model="form7.level" placeholder=""
            :disabled="!isAuth('qcManagement:group:admin') || dataList[5].status != 'B'">
            <el-option label="一等奖" value="一等奖"></el-option>
            <el-option label="二等奖" value="二等奖"></el-option>
            <el-option label="三等奖" value="三等奖"></el-option>
            <el-option label="四等奖" value="四等奖"></el-option>
            <el-option label="鼓励奖" value="鼓励奖"></el-option>
            <el-option label="未获奖" value="未获奖"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核结果" :label-width="formLabelWidth" prop="result">
          <el-select v-model="form7.result" placeholder=""
            :disabled="!isAuth('qcManagement:group:admin') || dataList[5].status != 'B'">
            <el-option label="通过" value="1"></el-option>
            <el-option label="不通过" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form7.comment" autocomplete="off"
            :disabled="!isAuth('qcManagement:group:admin') || dataList[5].status != 'B'"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showDialog7 = false">取 消</el-button>
        <el-button type="primary" @click="dataFormSubmitEx(form7.id)"
          :disabled="!isAuth('qcManagement:group:admin') || dataList[5].status != 'B'">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as d3 from 'd3';
export default {
  name: 'submitEx',
  data() {
    return {
      activeNameFirst: '',//初评页签
      activeNameSecond: '',//复评页签
      ifSelected: false,
      ifDepartment: false,
      tableData: [20, 40, 20, 20],
      tableDataCount: '',
      finalData: [20, 40, 20, 20],
      finalDataCount: '',
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
        avg: '',
        level: '',
      },
      dataList: [
        {
          id: 2,
          name: '成果认定',
          parentName: '科室审核',
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
      rootNode: {
        id: 1,
        name: '科室审核',
        parentName: '',
        url: '',
        status: 'A',
      },
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
      rule: {
        result: [
          { required: true, message: '审核结果不能为空', trigger: 'blur' }
        ],
        level: [
          { required: true, message: '获奖等级不能为空', trigger: 'blur' }
        ],
      },
      ifFinancial: false,
      currentEconomy: '',
      currentEconomyCount: '',
      commentTable: [],
      commentTableFirst: [],
      commentTableSecond: [],
      currentUserName: '',
      currentSelectedName: '',
    };
  },

  async mounted() {

    this.initRouterParam()
    await this.getCommentTable()
    await this.getStatusList()
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
    // console.log(this.commentTable)
    this.initActiveName()
    this.ifMessage()
  },
  computed: {

  },
  methods: {
    ifMessage() {

    },
    initActiveName() {
      let first = this.routerParam[0].firstExaminePeople
      let firstAll = this.dataFormEx.qcFirstAll
      let second = this.routerParam[0].secondExaminePeople
      let secondAll = this.dataFormEx.qcSecondAll
      let flag1 = true
      let flag2 = true
      if (Array.isArray(first)) {
        if (flag1) {
          this.activeNameFirst = first[0]
        }
        firstAll.forEach((item, index) => {
          if (item.name.includes(this.currentUserName)) {
            this.activeNameFirst = item.name
            this.tableData = item.score
            this.form4.result = item.result
            this.form4.comment = item.comment
            flag1 = false
          }
        })
      } else {
        console.log('初评人员非数组')
      }
      if (Array.isArray(second)) {
        if (flag2) {
          this.activeNameSecond = second[0]
        }
        secondAll.forEach((item, index) => {
          if (item.name.includes(this.currentUserName)) {
            this.activeNameSecond = item.name
            this.finalData = item.score
            this.form5.result = item.result
            this.form5.comment = item.comment
            flag2 = false
          }
        })
      } else {
        console.log('复评人员非数组')
      }

    },

    //处理跳转参数
    async initRouterParam() {
      try {
        this.routerParam = this.$route.query.data ? JSON.parse(this.$route.query.data) : { qcsrId: '', topicName: '', topicType: '', resultType: '', examineId: '' };
        console.log(this.routerParam)
        this.routerParam[0].firstExaminePeople = JSON.parse(this.routerParam[0].firstExaminePeople)
        this.routerParam[0].secondExaminePeople = JSON.parse(this.routerParam[0].secondExaminePeople)
        // this.routerParam[0].firstExaminePeople = this.routerParam[0].firstExaminePeople.map(item => ({
        //   name: item,
        //   score: [],
        //   result: '',
        //   comment: ''
        // }));
        // this.routerParam[0].secondExaminePeople = this.routerParam[0].secondExaminePeople.map(item => ({
        //   name: item,
        //   score: [],
        //   result: '',
        //   comment: ''
        // }));
        console.log(this.routerParam)
        this.ifSelected = this.ifSelectedPart()
        this.ifDepartment = this.ifDepartmentEnsure()
        // 获取数据列表
        await this.$http({
          url: this.$http.adornUrl('/qcPlan/step/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 100000,
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            let dataList = []
            dataList = data.page.list.filter(item => item.stepSubjectId === this.routerParam[0].qcsrId);
            dataList.forEach(item => {
              item.stagePeople = JSON.parse(item.stagePeople)
            })
            dataList.sort((a, b) => a.stepProcess - b.stepProcess);
            console.log({ dataList })
            this.ifFinancial = false
            this.currentEconomy = ''
            this.currentEconomyCount = ''
            dataList.forEach(item => {
              if (item.stepType == '创新型' && item.stepProcess == 6) {
                if (item.stageEconomy && item.stageEconomyCount) {
                  this.ifFinancial = true
                  console.log('stageEconomy' + item.stageEconomy)
                  console.log('stageEconomyCount' + item.stageEconomyCount)
                  this.currentEconomy = item.stageEconomy
                  this.currentEconomyCount = item.stageEconomyCount
                }
              } else if (item.stepType != '创新型' && item.stepProcess == 8) {
                if (item.stageEconomy && item.stageEconomyCount) {
                  this.ifFinancial = true
                  console.log('stageEconomy' + item.stageEconomy)
                  console.log('stageEconomyCount' + item.stageEconomyCount)
                  this.currentEconomy = item.stageEconomy
                  this.currentEconomyCount = item.stageEconomyCount
                }
              }
            })
          } else {
            // this.dataList = []
            // this.totalPage = 0
          }
        })
      } catch (e) {
        console.log('处理跳转参数失败')
        console.log(e)
      }
    },
    handleFirstChange(person) {
      console.log(person)
      console.log(this.dataFormEx)
      console.log(this.dataFormEx.qcFirstAll)
      if (Array.isArray(this.dataFormEx.qcFirstAll))
        this.dataFormEx.qcFirstAll.forEach(item => {
          if (person == item.name) {
            this.tableData = item.score
            this.currentSelectedName = item.name
            this.form4.result = item.result
            this.form4.comment = item.comment
          }
        })
    },
    handleSecondChange(person) {
      console.log(person)
      console.log(this.dataFormEx)
      console.log(this.dataFormEx.qcSecondAll)
      if (Array.isArray(this.dataFormEx.qcSecondAll))
        this.dataFormEx.qcSecondAll.forEach(item => {
          if (person == item.name) {
            this.finalData = item.score
            this.currentSelectedName = item.name
            this.form5.result = item.result
            this.form5.comment = item.comment
          }
        })
    },
    //获取评论表数据
    async getCommentTable() {
      await this.$http({
        url: this.$http.adornUrl(`/qc/table/list`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.commentTable = data.page.list
          this.commentTable.forEach(item => {
            item.tableComment = JSON.parse(item.tableComment).split('\n')
          })
          this.commentTableFirst = this.commentTable.filter(item => item.tableType === '初评').map(item => item.tableComment)
          this.commentTableSecond = this.commentTable.filter(item => item.tableType === '复评').map(item => item.tableComment)
          // console.log(this.commentTableFirst)
          // console.log(this.commentTableSecond)
          console.log(this.commentTable)
          console.log(this.commentTable[8].tableNumber)
        } else {
          this.commentTable = {}
          console.log('获取评论表失败')
        }
      })
    },
    //是否是对应科室
    ifDepartmentEnsure() {
      if ((this.routerParam[0].topicDepartment == '质量科' && this.isAuth('department:quality:leader'))) {
        return true
      } else if ((this.routerParam[0].topicDepartment == '生产科' && this.isAuth('department:product:leader'))) {
        return true
      } else if ((this.routerParam[0].topicDepartment == '财务科' && this.isAuth('department:financial:leader'))) {
        return true
      } else if ((this.routerParam[0].topicDepartment == '市场科' && this.isAuth('department:market:leader'))) {
        return true
      } else if ((this.routerParam[0].topicDepartment == '供应科' && this.isAuth('department:supply:leader'))) {
        return true
      } else if ((this.routerParam[0].topicDepartment == '技术科' && this.isAuth('department:tech:leader'))) {
        return true
      } else if ((this.routerParam[0].topicDepartment == '安环设备科' && this.isAuth('department:safety:leader'))) {
        return true
      } else if ((this.routerParam[0].topicDepartment == '企业管理科' && this.isAuth('department:enterprise:leader'))) {
        return true
      } else if ((this.routerParam[0].topicDepartment == '党群办公室' && this.isAuth('department:party:leader'))) {
        return true
      } else {
        return false
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
        url: this.$http.adornUrl(`/qcManagement/examineStatus/info/${this.routerParam[0].examineId}`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataFormEx = data.qcExamineStatus
          this.dataFormEx.qcFirstAll = JSON.parse(this.dataFormEx.qcFirstAll)
          this.dataFormEx.qcSecondAll = JSON.parse(this.dataFormEx.qcSecondAll)
          // console.log(this.dataFormEx)
          this.renderTree()
          // this.tableData = this.dataFormEx.qcFirstScore.split(',')
          this.tableData = []
          // this.finalData = this.dataFormEx.qcSecondScore.split(',')
          this.finalData = []
          // this.finalDataCount = this.dataFormEx.qcSecondScore ? this.finalData.reduce((accumulator, currentValue) => accumulator + currentValue, 0) : undefined;
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
          this.form7.avg = this.dataFormEx.qcSevenAvg
          this.form7.level = this.dataFormEx.qcSevenAvg >= 85 ? '一等奖' : (this.dataFormEx.qcSevenAvg >= 75 ? '二等奖' : (this.dataFormEx.qcSevenAvg >= 65 ? '三等奖' : (this.dataFormEx.qcSevenAvg >= 55 ? '四等奖' : (this.dataFormEx.qcSevenAvg >= 45 ? '鼓励奖' : '未获奖'))))
        } else {
          this.dataFormEx = {}
          console.log('获取数据失败')
          this.totalPage = 0
        }
      })
    },
    //初始化节点状态
    initStatus(statusId) {
      if (statusId === '0') {
        this.dataList[0].status = 'A'

      } else {
        console.log({ statusId })
        this.rootNode.status = 'C'
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
          this.dataList[4].status = this.ifFinancial ? 'B' : 'E'
          this.dataList[5].status = 'A'
        } else if (statusId === '4.1' || statusId === '4.2') {
          if (statusId === '4.1') {
            this.dataList[0].status = 'C'
            this.dataList[1].status = this.routerParam[0].resultType.includes('其他') ? 'E' : 'C'
            this.dataList[2].status = 'C'
            this.dataList[3].status = 'C'
            this.dataList[4].status = this.ifFinancial ? 'B' : 'E'
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
          this.dataList[4].status = this.ifFinancial ? 'C' : 'E'
          this.dataList[5].status = 'B'
        } else {
          this.dataList[0].status = 'C'
          this.dataList[1].status = this.routerParam[0].resultType.includes('其他') ? 'E' : 'C'
          this.dataList[2].status = 'C'
          this.dataList[3].status = 'C'
          this.dataList[4].status = this.ifFinancial ? 'C' : 'E'
          this.dataList[5].status = 'C'
        }
      }

    },
    //渲染审核流程树
    async renderTree() {
      let ifEmpty = '';
      await this.$http({
        url: this.$http.adornUrl(`/qcSubject/registration//info/${this.routerParam[0].qcsrId}`),
        method: "get",
        params: this.$http.adornParams({
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          ifEmpty = data.qcSubjectRegistration.resultType;
          console.log({ ifEmpty })
        } else {
        }
      });
      // 创建根节点
      this.rootNode = {
        id: 1,
        name: '科室审核',
        parentName: '',
        url: '',
        status: ifEmpty == '' ? 'A' : 'B',
      };
      if (ifEmpty == '') {
        this.$message({
          message: '未进行成果提交，当前只可读！',
          type: 'warning',
          duration: 3000,
        })
      }
      // console.log(this.routerParam[0].resultType ? '1111' : '2222')
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
      const formattedData = [this.rootNode, ...this.dataList.map(item => ({
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
          if (d.data.name === '科室审核') {
            this.showDialog1 = true;
            // console.log('++++++++++');
          } else if (d.data.name === '成果认定') {
            // if (d.data.status === 'A') {
            //   this.$message.warning('请先等待上一步');
            //   return;
            // } else {
            this.showDialog2 = true;
            // }
          } else if (d.data.name === '相关方审核') {
            // if (d.data.status === 'A') {
            //   this.$message.warning('请先等待上一步');
            //   return;
            // } else
            if (d.data.status === 'E') {
              this.$message.warning('无该过程审核');
              return;
            } else {
              this.showDialog3 = true;
            }
          } else if (d.data.name === '成果初评') {
            // if (d.data.status === 'A') {
            //   this.$message.warning('请先等待上一步');
            //   return;
            // } else {
            this.showDialog4 = true;
            // }
          } else if (d.data.name === '成果复评') {
            // if (d.data.status === 'A') {
            //   this.$message.warning('请先等待上一步');
            //   return;
            // } else {
            this.showDialog5 = true;
            // }
          } else if (d.data.name === '财务部审核') {
            // if (d.data.status === 'A') {
            //   this.$message.warning('请先等待上一步');
            //   return;
            // } else {
            this.showDialog6 = true;
            // }
          } else if (d.data.name === '终评提交') {
            // if (d.data.status === 'A') {
            //   this.$message.warning('请先等待上一步');
            //   return;
            // } else {
            this.showDialog7 = true;
            // }

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

      //科室审核
      if (formId === 1) {
        this.$refs['form1'].validate((valid) => {
          if (valid) {
            if (this.form1.result != '1') {
              console.log('不通过')
              console.log(this.routerParam[0].qcsrId)
              this.$http({
                url: this.$http.adornUrl(`/qcSubject/registration/update`),
                method: 'post',
                data: this.$http.adornData({
                  'qcsrId': this.routerParam[0].qcsrId,
                  'topicReviewStatus': 3,
                  'resultType': '',
                })
              })
            }
            this.$http({
              url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
              method: 'post',
              data: this.$http.adornData({
                'qcExamineId': this.routerParam[0].examineId || undefined,
                'qcExamineSubject': this.routerParam[0].qcsrId,
                'qcExamineCurrent': this.form1.result === '1' ? '1' : '0',
                'qcStatusOne': this.form1.result,
                'qcOneContent': this.form1.comment,
              })
            }).then(({ data }) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    location.reload();
                    this.showDialog1 = false;
                  }
                })
              } else {
                this.$message.error(data.msg)
              }

            }).catch((error) => {
              console.log(error)
            })
          }
        })
      }//成果认定
      else if (formId === 2) {
        this.$refs['form2'].validate((valid) => {
          if (valid) {
            let tmpFirstList = this.dataFormEx.qcFirstAll
            tmpFirstList.forEach(item => {
              item.result = ''
            })
            this.$http({
              url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
              method: 'post',
              data: this.$http.adornData({
                'qcExamineId': this.routerParam[0].examineId || undefined,
                'qcExamineSubject': this.routerParam[0].qcsrId,
                'qcExamineCurrent': this.form2.result === '1' ? '2' : '0',
                'qcStatusTwo': this.form2.result,
                'qcTwoContent': this.form2.comment,
                'qcFirstAll': JSON.stringify(tmpFirstList),
              })
            }).then(({ data }) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    // this.renderTree()
                    location.reload()
                    this.showDialog2 = false

                  }
                })
              } else {
                this.$message.error(data.msg)
              }
              if (this.form2.result != '1') {
                this.$http({
                  url: this.$http.adornUrl(`/qcSubject/registration/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcsrId': this.routerParam[0].qcsrId,
                    'topicReviewStatus': 3,
                    'resultType': '',
                  })
                })
              }
            }).catch((error) => {
              console.log(error)
            })
          }
        })
        //相关方审核
      } else if (formId === 3) {
        this.$refs['form3'].validate((valid) => {
          if (valid) {
            let tmpFirstList = this.dataFormEx.qcFirstAll
            tmpFirstList.forEach(item => {
              item.result = ''
            })
            this.$http({
              url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
              method: 'post',
              data: this.$http.adornData({
                'qcExamineId': this.routerParam[0].examineId || undefined,
                'qcExamineSubject': this.routerParam[0].qcsrId,
                'qcExamineCurrent': this.form3.result === '1' ? '3' : '0',
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
                    location.reload()
                    this.showDialog3 = false
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
              if (this.form3.result != '1') {
                this.$http({
                  url: this.$http.adornUrl(`/qcSubject/registration/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcsrId': this.routerParam[0].qcsrId,
                    'topicReviewStatus': 3,
                    'resultType': '',
                  })
                })
              }
            })
          }
        })
        //成果初评⬇
      } else if (formId === 4) {
        this.$refs['form4'].validate((valid) => {
          if (valid) {
            var firstCount = 0
            let hasError = false
            // 验证函数，用于检查是否为有效数字
            function isValidNumber(item) {
              const cleanedItem = String(item).trim(); // 去除首尾空白字符
              if (cleanedItem === '') return false; // 空字符串视为无效
              const itemAsNumber = Number(cleanedItem.replace(/,/g, '')); // 移除逗号并转换为数字
              return !isNaN(itemAsNumber) && isFinite(itemAsNumber); // 检查是否为有效的有限数值
            }

            // 使用 some 方法来检查是否有任何元素不符合条件
            const hasInvalidItem = this.tableData.some((item, index) => {
              if (!isValidNumber(item)) {
                this.$message({
                  message: '存在非数字或为空的项!',
                  type: 'warning',
                  duration: 1500,
                });
                hasError = true;
                return true; // 发现无效项后立即停止迭代
              } else {
                firstCount += Number(item);
                return false;
              }
            });
            console.log({ firstCount })
            if (firstCount > 100 || firstCount < 0) {
              this.$message({
                message: firstCount > 100 ? '总分超过100!' : '总分小于0!',
                type: 'warning',
                duration: 1500,
                // onClose: () => {
                //   this.showDialog4 = false
                //   hasError = true;
                // }
              })
              hasError = true;
            }
            if (!hasInvalidItem && !hasError) {
              let tmpAllInfoList = this.dataFormEx.qcFirstAll
              tmpAllInfoList.forEach((item, index) => {
                if (item.name.includes(this.currentUserName)) {
                  tmpAllInfoList[index] = {
                    name: item.name,
                    score: this.tableData,
                    result: this.form4.result,
                    comment: this.form4.comment
                  };
                }
              });
              let allResult = '1'
              tmpAllInfoList.forEach(item => {
                if (item.result != '0' && item.result != '1') {
                  allResult = '未完成'
                  console.log({ allResult })
                } else if (item.result == '0' && allResult != '未完成') {
                  allResult = '0'
                  console.log({ allResult })
                } else {
                  console.log('初评其他情况')
                }
              })
              let tmpSecondList = this.dataFormEx.qcSecondAll
              tmpSecondList.forEach(item => {
                item.result = ''
              })
              if (allResult == '1') {
                this.$http({
                  url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcExamineId': this.routerParam[0].examineId || undefined,
                    'qcExamineSubject': this.routerParam[0].qcsrId,
                    'qcExamineCurrent': allResult == '1' ? '4' : '0',
                    'qcStatusFour': '1',
                    'qcFirstAll': JSON.stringify(tmpAllInfoList),
                    'qcSecondAll': JSON.stringify(tmpSecondList),
                  })
                }).then(({ data }) => {
                  if (data && data.code === 0) {
                    this.$message({
                      message: '操作成功',
                      type: 'success',
                      duration: 1500,
                      onClose: () => {
                        location.reload()
                        this.showDialog4 = false
                      }
                    })
                  } else {
                    this.$message.error(data.msg)
                  }
                })
              } else if (allResult == '0') {
                this.$http({
                  url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcExamineId': this.routerParam[0].examineId || undefined,
                    'qcExamineSubject': this.routerParam[0].qcsrId,
                    'qcExamineCurrent': '0',
                    'qcStatusFour': '0',
                    'qcFirstAll': JSON.stringify(tmpAllInfoList),
                  })
                }).then(({ data }) => {
                  if (data && data.code === 0) {
                    this.$message({
                      message: '操作成功',
                      type: 'success',
                      duration: 1500,
                      onClose: () => {
                        location.reload()
                        this.showDialog4 = false
                      }
                    })
                  } else {
                    this.$message.error(data.msg)
                  }
                })
                this.$http({
                  url: this.$http.adornUrl(`/qcSubject/registration/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcsrId': this.routerParam[0].qcsrId,
                    'topicReviewStatus': 3,
                    'resultType': '',
                    'qcFirstAll': '',
                    'qcSecondAll': '',
                  })
                })
              } else if (allResult == '未完成') {
                this.$http({
                  url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcExamineId': this.routerParam[0].examineId || undefined,
                    'qcExamineSubject': this.routerParam[0].qcsrId,
                    'qcFirstAll': JSON.stringify(tmpAllInfoList),
                  })
                }).then(({ data }) => {
                  if (data && data.code === 0) {
                    this.$message({
                      message: '操作成功',
                      type: 'success',
                      duration: 1500,
                      onClose: () => {
                        location.reload()
                        this.showDialog4 = false
                      }
                    })
                  } else {
                    this.$message.error(data.msg)
                  }
                })
              } else {
                console.log('初评未全部完成')
              }
            }
          }
        })
        //成果复评
      } else if (formId === 5) {
        this.$refs['form5'].validate((valid) => {
          if (valid) {
            var secondCount = 0
            let hasError = false;
            // 验证函数，用于检查是否为有效数字
            function isValidNumber(item) {
              const cleanedItem = String(item).trim(); // 去除首尾空白字符
              if (cleanedItem === '' || cleanedItem === undefined) return false; // 空字符串视为无效
              const itemAsNumber = Number(cleanedItem.replace(/,/g, '')); // 移除逗号并转换为数字
              return !isNaN(itemAsNumber) && isFinite(itemAsNumber); // 检查是否为有效的有限数值
            }
            console.log('finalData', this.finalData)
            console.log('finalDataLength', this.finalData.length)
            console.log('finalData', Array.isArray(this.finalData))
            // 使用 some 方法来检查是否有任何元素不符合条件
            if (this.finalData.length !== 4) {
              hasError = true
              this.$message({
                message: '存在空的项!',
                type: 'warning',
                duration: 1500,
              });
            }
            const hasInvalidItem = this.finalData.some((item, index) => {
              if (!isValidNumber(item)) {
                this.$message({
                  message: '存在非数字或为空的项!',
                  type: 'warning',
                  duration: 1500,
                });
                hasError = true;
                return true; // 发现无效项后立即停止迭代
              } else {
                console.log('item', item)
                secondCount += Number(item);
                return false;
              }
            });
            console.log({ secondCount })
            if (secondCount > 100 || secondCount < 0) {
              this.$message({
                message: secondCount > 100 ? '总分超过100!' : '总分小于0!',
                type: 'warning',
                duration: 1500,
              })
              hasError = true;
            }

            if (!hasError && !hasInvalidItem) {
              let tmpAllInfoList = this.dataFormEx.qcSecondAll
              tmpAllInfoList.forEach((item, index) => {
                if (item.name.includes(this.currentUserName)) {
                  tmpAllInfoList[index] = {
                    name: item.name,
                    score: this.finalData,
                    result: this.form5.result,
                    comment: this.form5.comment
                  };
                }
              });
              let allResult = '1'
              tmpAllInfoList.forEach(item => {
                if (item.result != '0' && item.result != '1') {
                  allResult = '未完成'
                  console.log({ allResult })
                } else if (item.result == '0' && allResult != '未完成') {
                  allResult = '0'
                  console.log({ allResult })
                } else {
                  console.log('复评其他情况')
                }
              })

              if (allResult === '1') {
                console.log(this.form5.result)
                let scoreAvg = 0
                let scoreAllCount = 0
                console.log({ tmpAllInfoList })
                tmpAllInfoList.forEach((item, index) => {
                  const sumOfScores = item.score.reduce((accumulator, currentValue) => {
                    return accumulator + parseInt(currentValue);
                  }, 0);
                  console.log({ sumOfScores })
                  scoreAllCount = scoreAllCount + sumOfScores
                })
                scoreAvg = scoreAllCount / (tmpAllInfoList.length)

                console.log({ scoreAllCount })
                console.log('tmpAllInfoList.length:', tmpAllInfoList.length)
                console.log({ scoreAvg })
                this.$http({
                  url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcExamineId': this.routerParam[0].examineId || undefined,
                    'qcExamineSubject': this.routerParam[0].qcsrId,
                    'qcExamineCurrent': this.ifFinancial ? (this.dataFormEx.qcStatusSix === '1' ? '5' : '4.1') : '5',
                    'qcStatusFive': '1',
                    'qcSecondAll': JSON.stringify(tmpAllInfoList),
                    'qcSevenAvg': `${scoreAvg}`,
                  })
                }).then(({ data }) => {
                  if (data && data.code === 0) {
                    this.$message({
                      message: '操作成功',
                      type: 'success',
                      duration: 1500,
                      onClose: () => {
                        location.reload()
                        this.showDialog5 = false
                      }
                    })
                  } else {
                    this.$message.error(data.msg)
                  }
                })
              } else if (allResult === '0') {
                this.$http({
                  url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcExamineId': this.routerParam[0].examineId || undefined,
                    'qcExamineSubject': this.routerParam[0].qcsrId,
                    'qcExamineCurrent': this.routerParam[0].resultType.includes('其他') ? '2' : '3',
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
                        location.reload()
                        this.showDialog5 = false
                      }
                    })
                  } else {
                    this.$message.error(data.msg)
                  }
                })
              } else if (allResult == '未完成') {
                this.$http({
                  url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcExamineId': this.routerParam[0].examineId || undefined,
                    'qcExamineSubject': this.routerParam[0].qcsrId,
                    'qcSecondAll': JSON.stringify(tmpAllInfoList),
                  })
                }).then(({ data }) => {
                  if (data && data.code === 0) {
                    this.$message({
                      message: '操作成功',
                      type: 'success',
                      duration: 1500,
                      onClose: () => {
                        location.reload()
                        this.showDialog5 = false
                      }
                    })
                  } else {
                    this.$message.error(data.msg)
                  }
                })
              }
            }
          }
        })

        //财务部审核
      } else if (formId === 6) {
        //财务部审核
        this.$refs['form6'].validate((valid) => {
          if (valid) {
            if (this.form6.result === '1') {
              this.$http({
                url: this.$http.adornUrl(`/qcManagement/examineStatus/update`),
                method: 'post',
                data: this.$http.adornData({
                  'qcExamineId': this.routerParam[0].examineId || undefined,
                  'qcExamineSubject': this.routerParam[0].qcsrId,
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
                      location.reload()
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
                  'qcExamineCurrent': '0',
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
                      location.reload()
                      this.showDialog6 = false
                    }
                  })
                } else {
                  this.$message.error(data.msg)
                }
                this.$http({
                  url: this.$http.adornUrl(`/qcSubject/registration/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcsrId': this.routerParam[0].qcsrId,
                    'topicReviewStatus': 3,
                    'resultType': '',
                  })
                })
              })
            }
          }
        })

      } else if (formId === 7) {
        //终评提交
        this.$refs['form7'].validate((valid) => {
          if (valid) {
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
                  'qcSevenLevel': this.form7.level,
                  'qcStorageFlag': '1',
                })
              }).then(({ data }) => {
                if (data && data.code === 0) {
                  this.$message({
                    message: '操作成功',
                    type: 'success',
                    duration: 1500,
                    onClose: () => {
                      location.reload()
                      this.showDialog7 = false
                    }
                  })
                  //更新终评分数至课题活动结果
                  this.$http({
                    url: this.$http.adornUrl(`/qcSubject/registration/update`),
                    method: 'post',
                    data: this.$http.adornData({
                      'qcsrId': this.routerParam[0].qcsrId,
                      'topicActivityResult': this.form7.level,
                      'topicReviewStatus': 3,
                    })
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
                  'qcExamineCurrent': '0',
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
                      location.reload()
                      this.showDialog7 = false
                    }
                  })
                } else {
                  this.$message.error(data.msg)
                }
                this.$http({
                  url: this.$http.adornUrl(`/qcSubject/registration/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'qcsrId': this.routerParam[0].qcsrId,
                    'topicReviewStatus': 3,
                    'resultType': '',
                  })
                })
              })
            }
          }
        })
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
