<template>
  <div class="mod-config">

    <!-- 标签页 -->
    <el-tabs v-model="firstactivename" type="card">

      <el-tab-pane label="正态分布图表" name="first">
        <div ref="lineChartPTD" style="width: 1500px;height:300px;"></div>
        
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getPTDChartDataList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getPTDChartDataList()">查询</el-button>
            <el-button v-if="isAuth('spc:spcptd:save')" type="primary" @click="PTDChartaddOrUpdateHandle()">新增</el-button>
            <el-button v-if="isAuth('spc:spcptd:delete')" type="danger" @click="PTDChartdeleteHandle()" :disabled="PTDChartdataListSelections.length <= 0">批量删除</el-button>
            <el-button type="primary" @click="showDialog1 = true">导入Excel文件</el-button>
          </el-form-item>
        </el-form>
        <el-table
          :data="PTDChartdataList"
          border
          v-loading="dataListLoading"
          @selection-change="PTDChartselectionChangeHandle"
          style="width: 100%;">
          <el-table-column
            type="selection"
            header-align="center"
            align="center"
            width="50">
          </el-table-column>
          <el-table-column
            prop="id"
            header-align="center"
            align="center"
            label="主键">
          </el-table-column>
          <el-table-column
            prop="acceptanceRegion"
            header-align="center"
            align="center"
            label="接收区域">
          </el-table-column>
          <el-table-column
            prop="workTime"
            header-align="center"
            align="center"
            label="工时">
          </el-table-column>
          <el-table-column
            prop="ptdValue"
            header-align="center"
            align="center"
            label="正态分布值,按照公式计算">
          </el-table-column>
          <el-table-column
            prop="frequency"
            header-align="center"
            align="center"
            label="频率">
          </el-table-column>
          <el-table-column
            prop="craetTime"
            header-align="center"
            align="center"
            label="创建时间">
          </el-table-column>
          <el-table-column
            prop="createPeople"
            header-align="center"
            align="center"
            label="创建人">
          </el-table-column>
          <el-table-column
            prop="deletFlag"
            header-align="center"
            align="center"
            label="逻辑删除字段">
          </el-table-column>
          <el-table-column
            prop="spare"
            header-align="center"
            align="center"
            label="备用字段">
          </el-table-column>
          <el-table-column
            prop="dataImportTime"
            header-align="center"
            align="center"
            label="数据导入日期">
          </el-table-column>
          <el-table-column
            prop="updateTime"
            header-align="center"
            align="center"
            label="数据更新时间">
          </el-table-column>
          <el-table-column
            fixed="right"
            header-align="center"
            align="center"
            width="150"
            label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="PTDChartaddOrUpdateHandle(scope.row.id)">修改</el-button>
              <el-button type="text" size="small" @click="PTDChartdeleteHandle(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="PTDChartsizeChangeHandle"
          @current-change="PTDChartcurrentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <p-t-d-add-or-update v-if="PTDChartaddOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getPTDChartDataList"></p-t-d-add-or-update>
      </el-tab-pane>

      <!-- 计量型图表 -->
      <el-tab-pane label="计量型图表" name="second">
        <el-tabs v-model="secondactivename" type="card">
          <!-- Xbar-R图数据及图表 -->
          <el-tab-pane label="Xbar-R" name="first" @@tab-click="getXbarRchart">
            <div ref="XbarR_XbarChart" style="width: 1500px;height:300px;"></div>
            <div ref="RChart" style="width: 1500px;height:300px;"></div>
          </el-tab-pane>

          <!-- Xbar-S图数据及图表 -->
          <el-tab-pane label="Xbar-S" name="second">
            <div ref="XbarS_XbarChart" style="width: 1500px;height:300px;"></div>
            <div ref="XbarS_SChart" style="width: 1500px;height:300px;"></div>
          </el-tab-pane>

          <!-- Me-R图数据及图表 -->
          <el-tab-pane label="Me-R" name="third">
            <div ref="MeR_MeChart" style="width: 1500px;height:300px;"></div>
            <div ref="MeR_RChart" style="width: 1500px;height:300px;"></div>
          </el-tab-pane>

          <!-- I-MR图数据及图表 -->
          <el-tab-pane label="I-MR" name="fourst">
            <div ref="IMR_IChart" style="width: 1500px;height:300px;"></div>
            <div ref="IMR_MRChart" style="width: 1500px;height:300px;"></div>
          </el-tab-pane>

        </el-tabs>

        <!-- 计量型图表数据展示 -->
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getXRChartDataList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getXRChartDataList()">查询</el-button>
            <el-button v-if="isAuth('spc:spcxrchart:save')" type="primary" @click="XRChartaddOrUpdateHandle()">新增</el-button>
            <el-button v-if="isAuth('spc:spcxrchart:delete')" type="danger" @click="XRChartdeleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
            <el-button type="primary" @click="showDialog = true">导入Excel文件</el-button>
            <el-button type="primary" @click="showDialog2 = true">统计用数据导入</el-button>
          </el-form-item>
        </el-form>
        <el-table
          :data="dataList"
          border
          v-loading="dataListLoading"
          @selection-change="XRChartselectionChangeHandle"
          style="width: 100%;">
          <el-table-column
            type="selection"
            header-align="center"
            align="center"
            width="50">
          </el-table-column>
          <el-table-column
            prop="spcxrId"
            header-align="center"
            align="center"
            label="主键">
          </el-table-column>
          <el-table-column
            prop="date1"
            header-align="center"
            align="center"
            label="当月第1天数据">
          </el-table-column>
          <el-table-column
            prop="date2"
            header-align="center"
            align="center"
            label="当月第2天数据">
          </el-table-column>
          <el-table-column
            prop="date3"
            header-align="center"
            align="center"
            label="当月第3天数据">
          </el-table-column>
          <el-table-column
            prop="date4"
            header-align="center"
            align="center"
            label="当月第4天数据">
          </el-table-column>
          <el-table-column
            prop="date5"
            header-align="center"
            align="center"
            label="当月第5天数据">
          </el-table-column>
          <el-table-column
            prop="date6"
            header-align="center"
            align="center"
            label="当月第6天数据">
          </el-table-column>
          <el-table-column
            prop="date7"
            header-align="center"
            align="center"
            label="当月第7天数据">
          </el-table-column>
          <el-table-column
            prop="date8"
            header-align="center"
            align="center"
            label="当月第8天数据">
          </el-table-column>
          <el-table-column
            prop="date9"
            header-align="center"
            align="center"
            label="当月第9天数据">
          </el-table-column>
          <el-table-column
            prop="date10"
            header-align="center"
            align="center"
            label="当月第10天数据">
          </el-table-column>
          <el-table-column
            prop="date11"
            header-align="center"
            align="center"
            label="当月第11天数据">
          </el-table-column>
          <el-table-column
            prop="date12"
            header-align="center"
            align="center"
            label="当月第12天数据">
          </el-table-column>
          <el-table-column
            prop="date13"
            header-align="center"
            align="center"
            label="当月第13天数据">
          </el-table-column>
          <el-table-column
            prop="date14"
            header-align="center"
            align="center"
            label="当月第14天数据">
          </el-table-column>
          <el-table-column
            prop="date15"
            header-align="center"
            align="center"
            label="当月第15天数据">
          </el-table-column>
          <el-table-column
            prop="date16"
            header-align="center"
            align="center"
            label="当月第16天数据">
          </el-table-column>
          <el-table-column
            prop="date17"
            header-align="center"
            align="center"
            label="当月第17天数据">
          </el-table-column>
          <el-table-column
            prop="date18"
            header-align="center"
            align="center"
            label="当月第18天数据">
          </el-table-column>
          <el-table-column
            prop="date19"
            header-align="center"
            align="center"
            label="当月第19天数据">
          </el-table-column>
          <el-table-column
            prop="date20"
            header-align="center"
            align="center"
            label="当月第20天数据">
          </el-table-column>
          <el-table-column
            prop="date21"
            header-align="center"
            align="center"
            label="当月第21天数据">
          </el-table-column>
          <el-table-column
            prop="date22"
            header-align="center"
            align="center"
            label="当月第22天数据">
          </el-table-column>
          <el-table-column
            prop="date23"
            header-align="center"
            align="center"
            label="当月第23天数据">
          </el-table-column>
          <el-table-column
            prop="date24"
            header-align="center"
            align="center"
            label="当月第24天数据">
          </el-table-column>
          <el-table-column
            prop="date25"
            header-align="center"
            align="center"
            label="当月第25天数据">
          </el-table-column>
          <el-table-column
            prop="date26"
            header-align="center"
            align="center"
            label="当月第26天数据">
          </el-table-column>
          <el-table-column
            prop="date27"
            header-align="center"
            align="center"
            label="当月第27天数据">
          </el-table-column>
          <el-table-column
            prop="date28"
            header-align="center"
            align="center"
            label="当月第28天数据">
          </el-table-column>
          <el-table-column
            prop="date29"
            header-align="center"
            align="center"
            label="当月第29天数据">
          </el-table-column>
          <el-table-column
            prop="date30"
            header-align="center"
            align="center"
            label="当月第30天数据">
          </el-table-column>
          <el-table-column
            prop="date31"
            header-align="center"
            align="center"
            label="当月第31天数据">
          </el-table-column>
          <el-table-column
            prop="datatime"
            header-align="center"
            align="center"
            label="数据日期">
          </el-table-column>
          <el-table-column
            prop="upperLimitStandards"
            header-align="center"
            align="center"
            label="标准上限USL">
          </el-table-column>
          <el-table-column
            prop="centerLimitStandards"
            header-align="center"
            align="center"
            label="标准中心限CL">
          </el-table-column>
          <el-table-column
            prop="lowerLimitStandards"
            header-align="center"
            align="center"
            label="标准下限LSL">
          </el-table-column>
          <el-table-column
            prop="upperLimitX"
            header-align="center"
            align="center"
            label="X上限UCL">
          </el-table-column>
          <el-table-column
            prop="centerLimitX"
            header-align="center"
            align="center"
            label="X中心限CL">
          </el-table-column>
          <el-table-column
            prop="lowerLimitX"
            header-align="center"
            align="center"
            label="X下限LCL">
          </el-table-column>
          <el-table-column
            prop="upperLimitR"
            header-align="center"
            align="center"
            label="R上限UCL">
          </el-table-column>
          <el-table-column
            prop="centerLimitR"
            header-align="center"
            align="center"
            label="R中心限CL">
          </el-table-column>
          <el-table-column
            prop="lowerLimitR"
            header-align="center"
            align="center"
            label="R下限LCL">
          </el-table-column>
          <el-table-column
            prop="createBy"
            header-align="center"
            align="center"
            label="创建人">
          </el-table-column>
          <el-table-column
            prop="createTime"
            header-align="center"
            align="center"
            label="创建时间">
          </el-table-column>
          <el-table-column
            prop="alternateFields1"
            header-align="center"
            align="center"
            label="备用字段1">
          </el-table-column>
          <el-table-column
            prop="alternateFields2"
            header-align="center"
            align="center"
            label="备用字段2">
          </el-table-column>
          <el-table-column
            prop="alternateFields3"
            header-align="center"
            align="center"
            label="备用字段3">
          </el-table-column>
          <el-table-column
            prop="alternateFields4"
            header-align="center"
            align="center"
            label="备用字段4">
          </el-table-column>
          <el-table-column
            prop="deleteFlag"
            header-align="center"
            align="center"
            label="逻辑删除标志">
          </el-table-column>
          <el-table-column
            fixed="right"
            header-align="center"
            align="center"
            width="150"
            label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="XRChartaddOrUpdateHandle(scope.row.spcxrId)">修改</el-button>
              <el-button type="text" size="small" @click="XRChartdeleteHandle(scope.row.spcxrId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="XRChartsizeChangeHandle"
          @current-change="XRChartcurrentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getXRChartDataList"></add-or-update>


      </el-tab-pane>

      <!-- 计数型图表 -->
      <el-tab-pane label="计数型图表" name="third">

        <el-tabs v-model="thirdactiveName" type="card" >
          <!-- P图数据及图表 -->

          <el-tab-pane label="P-Chart" name="first">
            <div ref="lineChartP" style="width: 1500px;height:300px;"></div>
          </el-tab-pane>
          <el-tab-pane label="NP-Chart" name="second">
            <div ref="lineChartNP" style="width: 1500px;height:300px;"></div>
          </el-tab-pane>
          <el-tab-pane label="U-Chart" name="third">
            <div ref="lineChartU" style="width: 1500px;height:300px;"></div>
          </el-tab-pane>
          <el-tab-pane label="C-Chart" name="fourst">
            <div ref="lineChartC" style="width: 1500px;height:300px;"></div>
          </el-tab-pane>

          <el-form :inline="true" :model="dataForm" @keyup.enter.native="getPChartDataList()">
            <el-form-item>
              <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="getPChartDataList()">查询</el-button>
              <el-button v-if="isAuth('spc:spcpchart:save')" type="primary" @click="PChartaddOrUpdateHandle()">新增</el-button>
              <el-button v-if="isAuth('spc:spcpchart:delete')" type="danger" @click="PChartdeleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
            </el-form-item>
          </el-form>
          <el-table
            :data="PChartdataList"
            border
            v-loading="dataListLoading"
            @selection-change="PChartselectionChangeHandle"
            style="width: 100%;">
            <el-table-column
              type="selection"
              header-align="center"
              align="center"
              width="50">
            </el-table-column>
            <el-table-column
              prop="spcpId"
              header-align="center"
              align="center"
              label="主键">
            </el-table-column>
            <el-table-column
              prop="date1"
              header-align="center"
              align="center"
              label="当月第1天数据">
            </el-table-column>
            <el-table-column
              prop="date2"
              header-align="center"
              align="center"
              label="当月第2天数据">
            </el-table-column>
            <el-table-column
              prop="date3"
              header-align="center"
              align="center"
              label="当月第3天数据">
            </el-table-column>
            <el-table-column
              prop="date4"
              header-align="center"
              align="center"
              label="当月第4天数据">
            </el-table-column>
            <el-table-column
              prop="date5"
              header-align="center"
              align="center"
              label="当月第5天数据">
            </el-table-column>
            <el-table-column
              prop="date6"
              header-align="center"
              align="center"
              label="当月第6天数据">
            </el-table-column>
            <el-table-column
              prop="date7"
              header-align="center"
              align="center"
              label="当月第7天数据">
            </el-table-column>
            <el-table-column
              prop="date8"
              header-align="center"
              align="center"
              label="当月第8天数据">
            </el-table-column>
            <el-table-column
              prop="date9"
              header-align="center"
              align="center"
              label="当月第9天数据">
            </el-table-column>
            <el-table-column
              prop="date10"
              header-align="center"
              align="center"
              label="当月第10天数据">
            </el-table-column>
            <el-table-column
              prop="date11"
              header-align="center"
              align="center"
              label="当月第11天数据">
            </el-table-column>
            <el-table-column
              prop="date12"
              header-align="center"
              align="center"
              label="当月第12天数据">
            </el-table-column>
            <el-table-column
              prop="date13"
              header-align="center"
              align="center"
              label="当月第13天数据">
            </el-table-column>
            <el-table-column
              prop="date14"
              header-align="center"
              align="center"
              label="当月第14天数据">
            </el-table-column>
            <el-table-column
              prop="date15"
              header-align="center"
              align="center"
              label="当月第15天数据">
            </el-table-column>
            <el-table-column
              prop="date16"
              header-align="center"
              align="center"
              label="当月第16天数据">
            </el-table-column>
            <el-table-column
              prop="date17"
              header-align="center"
              align="center"
              label="当月第17天数据">
            </el-table-column>
            <el-table-column
              prop="date18"
              header-align="center"
              align="center"
              label="当月第18天数据">
            </el-table-column>
            <el-table-column
              prop="date19"
              header-align="center"
              align="center"
              label="当月第19天数据">
            </el-table-column>
            <el-table-column
              prop="date20"
              header-align="center"
              align="center"
              label="当月第20天数据">
            </el-table-column>
            <el-table-column
              prop="date21"
              header-align="center"
              align="center"
              label="当月第21天数据">
            </el-table-column>
            <el-table-column
              prop="date22"
              header-align="center"
              align="center"
              label="当月第22天数据">
            </el-table-column>
            <el-table-column
              prop="date23"
              header-align="center"
              align="center"
              label="当月第23天数据">
            </el-table-column>
            <el-table-column
              prop="date24"
              header-align="center"
              align="center"
              label="当月第24天数据">
            </el-table-column>
            <el-table-column
              prop="date25"
              header-align="center"
              align="center"
              label="当月第25天数据">
            </el-table-column>
            <el-table-column
              prop="date26"
              header-align="center"
              align="center"
              label="当月第26天数据">
            </el-table-column>
            <el-table-column
              prop="date27"
              header-align="center"
              align="center"
              label="当月第27天数据">
            </el-table-column>
            <el-table-column
              prop="date28"
              header-align="center"
              align="center"
              label="当月第28天数据">
            </el-table-column>
            <el-table-column
              prop="date29"
              header-align="center"
              align="center"
              label="当月第29天数据">
            </el-table-column>
            <el-table-column
              prop="date30"
              header-align="center"
              align="center"
              label="当月第30天数据">
            </el-table-column>
            <el-table-column
              prop="date31"
              header-align="center"
              align="center"
              label="当月第31天数据">
            </el-table-column>
            <el-table-column
              prop="sum"
              header-align="center"
              align="center"
              label="合计">
            </el-table-column>
            <el-table-column
              prop="datatime"
              header-align="center"
              align="center"
              label="数据日期">
            </el-table-column>
            <el-table-column
              prop="dataContent"
              header-align="center"
              align="center"
              label="数据内容（抽检数/不良数）">
            </el-table-column>
            <el-table-column
              prop="createBy"
              header-align="center"
              align="center"
              label="创建人">
            </el-table-column>
            <el-table-column
              prop="createTime"
              header-align="center"
              align="center"
              label="创建时间">
            </el-table-column>
            <el-table-column
              prop="alternateFields1"
              header-align="center"
              align="center"
              label="备用字段1">
            </el-table-column>
            <el-table-column
              prop="alternateFields2"
              header-align="center"
              align="center"
              label="备用字段2">
            </el-table-column>
            <el-table-column
              prop="alternateFields3"
              header-align="center"
              align="center"
              label="备用字段3">
            </el-table-column>
            <el-table-column
              prop="alternateFields4"
              header-align="center"
              align="center"
              label="备用字段4">
            </el-table-column>
            <el-table-column
              prop="deleteFlag"
              header-align="center"
              align="center"
              label="逻辑删除标志">
            </el-table-column>
            <el-table-column
              fixed="right"
              header-align="center"
              align="center"
              width="150"
              label="操作">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="PChartaddOrUpdateHandle(scope.row.spcpId)">修改</el-button>
                <el-button type="text" size="small" @click="PChartdeleteHandle(scope.row.spcpId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            @size-change="PChartsizeChangeHandle"
            @current-change="PChartcurrentChangeHandle"
            :current-page="pageIndex"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            :total="totalPage"
            layout="total, sizes, prev, pager, next, jumper">
          </el-pagination>
          <!-- 弹窗, 新增 / 修改 -->
          <p-chart-add-or-update v-if="PChartaddOrUpdateVisible" ref="PChartAddOrUpdate" @refreshDataList="getPChartDataList"></p-chart-add-or-update>

        </el-tabs>
      </el-tab-pane>


    </el-tabs>



    <!-- 导入excel文件弹窗 -->
    <el-dialog title="导入Excel文件" :visible.sync="showDialog" width="30%" :before-close="handleClose" @close="resetFileInput">
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

    <!-- 导入PTDexcel文件弹窗 -->
    <el-dialog title="导入PTDExcel文件" :visible.sync="showDialog1" width="30%" :before-close="handleClose" @close="resetFileInput">
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
        <el-button @click="showDialog1 = false">取 消</el-button>
        <el-button type="primary" @click="fileSendPTD()">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 导入标准数据excel文件弹窗 -->
    <el-dialog title="导入标准数据Excel文件" :visible.sync="showDialog2" width="30%" :before-close="handleClose" @close="resetFileInput">
      <i class="el-icon-upload"></i>
      <input type="file" id="inputFile" ref="fileInput"  />

      <!-- 进度动画条 -->
      <div v-if="progress > 0">
        <el-progress
          :percentage="progress"
          color="rgb(19, 194, 194)"
        ></el-progress>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="showDialog2 = false">取 消</el-button>
        <el-button type="primary" @click="fileSendStandards()">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
  import AddOrUpdate from './spcxrchart-add-or-update';
  import PChartAddOrUpdate from './spcpchart-add-or-update';
  import PTDAddOrUpdate from './spcptd-add-or-update';

  export default {
    data () {
      return {
        //X_R图相关参数
        dataForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,

        //P图相关参数
        PChartdataList: [],
        PChartdataListSelections: [],
        PChartaddOrUpdateVisible: false,

        //PTD图相关参数
        PTDChartdataList: [],
        PTDChartdataListSelections: [],
        PTDChartaddOrUpdateVisible: false,

        // 标签页设置参数
        firstactivename: 'first', //外层页签
        secondactivename: 'first',
        thirdactiveName: 'first',   //此参数将决定标签页停留在哪一页

        //导入相关参数
        showDialog: false,
        showDialog1: false,
        showDialog2: false,
        progress: 0,

        //Xbar-R
        chartInstanceXbarR_Xbar: null,
        chartInstanceXbarR_R: null,

        //Xbar-S
        chartInstanceXbarS_Xbar: null,
        chartInstanceXbarS_S: null,

        //Me-R
        chartInstanceMeR_Me: null,
        chartInstanceMeR_R: null,

        //I-MR
        chartInstanceIMR_I: null,
        chartInstanceIMR_MR: null,

        //P
        chartInstanceP: null,

        //NP
        chartInstanceNP: null,

        //U
        chartInstanceU: null,

        //C
        chartInstanceC: null,

        //PTD
        chartInstancePTD: null,


        responseData: null,


        //Xbar-R图相关参数
        XbarRChartData: [],

        //Xbar-S图相关参数
        XbarSChartData: [],

        //Me-R图相关参数
        MeRChartData: [],

        //I-MR图相关参数
        IMRChartData: [],

        //P图相关参数
        PChartData: [],

        //NP图相关参数
        NPChartData: [],

        //U图相关参数
        UChartData: [],

        //C图相关参数
        CChartData: [],

        //正态分布图相关参数
        PTDChartData: [],

      }
    },
    components: {
      AddOrUpdate,
      PChartAddOrUpdate,
      PTDAddOrUpdate,

    },
    activated () {
      this.getXRChartDataList();
      this.getPChartDataList();
      this.getPTDChartDataList();
    },
    mounted() {

      this.getXbarRchart().then(() => {  
        console.log(this.XbarRChartData);
          // 确保数据已加载  
          if (this.XbarRChartData.length = 8) {  
              this.initChartXbarR_Xbar();  
              this.initChartXbarR_R();
          } else {  
              console.error("XbarRChartData length is less than 5");  
          }  
      }).catch(error => {  
          console.error("Error loading XbarRChartData:", error);  
      }); 

      this.getXbarSchart().then(() => {
          // 确保数据已加载  
          if (this.XbarSChartData.length = 8) {  
            this.initChartXbarS_Xbar();
            this.initChartXbarS_S();
          } else {  
              console.error("XbarRChartData length is less than 5");  
          }  
      }).catch(error => {  
          console.error("Error loading XbarSChartData:", error);  
      }); 

      this.getMeRchart().then(() => {
          // 确保数据已加载  
          if (this.MeRChartData.length = 8) {  
            this.initChartMeR_Me();
            this.initChartMeR_R();
          } else {  
              console.error("MeRChartData length is less than 5");  
          }  
      }).catch(error => {  
          console.error("Error loading MeRChartData:", error);  
      }); 

      this.getIMRchart().then(() => {
          // 确保数据已加载  
          if (this.IMRChartData.length = 8) {  
            this.initChartIMR_I();
            this.initChartIMR_MR();
          } else {  
              console.error("IMRChartData length is less than 5");  
          }  
      }).catch(error => {  
          console.error("Error loading IMRChartData:", error);  
      }); 

      this.getPchart().then(() => {
          // 确保数据已加载  
          if (this.PChartData.length = 4) {  
            this.initChartP();
          } else {  
              console.error("PChartData length is less than 5");  
          }  
      }).catch(error => {  
          console.error("Error loading PChartData:", error);  
      }); 

      this.getNPchart().then(() => {
          // 确保数据已加载  
          if (this.NPChartData.length = 4) {  
            this.initChartNP();
          } else {  
              console.error("NPChartData length is less than 5");  
          }  
      }).catch(error => {  
          console.error("Error loading NPChartData:", error);  
      }); 

      this.getUchart().then(() => {
          // 确保数据已加载  
          if (this.UChartData.length = 4) {  
            this.initChartU();
          } else {  
              console.error("UChartData length is less than 5");  
          }  
      }).catch(error => {  
          console.error("Error loading UChartData:", error);  
      }); 

      this.getCchart().then(() => {
          // 确保数据已加载  
          if (this.CChartData.length = 4) {  
            this.initChartC();
          } else {  
              console.error("CChartData length is less than 5");  
          }  
      }).catch(error => {  
          console.error("Error loading CChartData:", error);  
      }); 

      this.getPTDchart().then(() => {
          // 确保数据已加载  
          if (this.PTDChartData.length = 3) {  
            this.initChartPTD();
          } else {  
              console.error("PTDChartData length is less than 5");  
          }  
      }).catch(error => {  
          console.error("Error loading PTDChartData:", error);  
      }); 




      // this.setDefaultChartOptions();
    },

    methods: {

      //XR图相关方法
      // 获取数据列表
      getXRChartDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/spc/spcxrchart/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
        console.log(this.dataList);
      },
      // 每页数
      XRChartsizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getXRChartDataList()
      },
      // 当前页
      XRChartcurrentChangeHandle (val) {
        this.pageIndex = val
        this.getXRChartDataList()
      },
      // 多选
      XRChartselectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      XRChartaddOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      XRChartdeleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.spcxrId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/spc/spcxrchart/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getXRChartDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },


      //P_chart图相关方法
      // 获取数据列表
      getPChartDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/spc/spcpchart/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.PChartdataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.PChartdataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      PChartsizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getPChartDataList()
      },
      // 当前页
      PChartcurrentChangeHandle (val) {
        this.pageIndex = val
        this.getPChartDataList()
      },
      // 多选
      PChartselectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      PChartaddOrUpdateHandle (id) {
        this.PChartaddOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.PChartAddOrUpdate.init(id)
        })
      },
      // 删除
      PChartdeleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.spcpId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/spc/spcpchart/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getPChartDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },

      //正态分布相关方法
      // 获取数据列表
      getPTDChartDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/spc/spcptd/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.PTDChartdataList = data.page.list
            console.log("========"+this.PTDChartdataList);
            this.totalPage = data.page.totalCount
          } else {
            this.PTDChartdataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      PTDChartsizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getPTDChartDataList()
      },
      // 当前页
      PTDChartcurrentChangeHandle (val) {
        this.pageIndex = val
        this.getPTDChartDataList()
      },
      // 多选
      PTDChartselectionChangeHandle (val) {
        this.PTDChartdataListSelections = val
      },
      // 新增 / 修改
      PTDChartaddOrUpdateHandle (id) {
        this.PTDChartaddOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      PTDChartdeleteHandle (id) {
        var ids = id ? [id] : this.PTDChartdataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/spc/spcptd/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getPTDChartDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },


      /**----------------------------------------------------------*/

      //调用spc/spcxrchart/chart/Xbar_R接口获取Xbar-R图数据
      async getXbarRchart() {  
        try {  
            const { data } = await this.$http({  
                url: this.$http.adornUrl('/spc/spcxrchart/chart/Xbar_R'),  
                method: 'get',  
            });  

            // 检查 data 对象是否包含 Xbar-R_Info 属性  
            if (data && Array.isArray(data['Xbar-R_Info'])) { // 注意这里用方括号访问对象属性  
                this.XbarRChartData = data['Xbar-R_Info'];  
            } else {  
                console.error("Received data is missing Xbar-R_Info or is not an array:", data);  
                this.XbarRChartData = []; // 设置为空数组  
            }  
        } catch (error) {  
            // 处理错误  
            console.error('Error:', error);  
            this.XbarRChartData = []; // 出错时设置为空数组  
        }  
      },

      //调用spc/spcxrchart/chart/Xbar_S接口获取Xbar-S图数据
      async getXbarSchart() {  
        try {  
            const { data } = await this.$http({  
                url: this.$http.adornUrl('/spc/spcxrchart/chart/Xbar_S'),  
                method: 'get',  
            });  

            // 检查 data 对象是否包含 Xbar-R_Info 属性  
            if (data && Array.isArray(data['Xbar-S_Info'])) { // 注意这里用方括号访问对象属性  
                this.XbarSChartData = data['Xbar-S_Info'];  
            } else {  
                console.error("Received data is missing Xbar-S_Info or is not an array:", data);  
                this.XbarSChartData = []; // 设置为空数组  
            }  
        } catch (error) {  
            // 处理错误  
            console.error('Error:', error);  
            this.XbarSChartData = []; // 出错时设置为空数组  
        }  
      },

      //调用spc/spcxrchart/chart/Me_R接口获取Me_R图数据
      async getMeRchart() {  
        try {  
            const { data } = await this.$http({  
                url: this.$http.adornUrl('/spc/spcxrchart/chart/Me_R'),  
                method: 'get',  
            });  

            // 检查 data 对象是否包含 Xbar-R_Info 属性  
            if (data && Array.isArray(data['Me-R_Info'])) { // 注意这里用方括号访问对象属性  
                this.MeRChartData = data['Me-R_Info'];  
            } else {  
                console.error("Received data is missing Me-R_Info or is not an array:", data);  
                this.MeRChartData = []; // 设置为空数组  
            }  
        } catch (error) {  
            // 处理错误  
            console.error('Error:', error);  
            this.MeRChartData = []; // 出错时设置为空数组  
        }  
      },

      //调用spc/spcxrchart/chart/I_MR接口获取I_MR图数据
      async getIMRchart() {  
        try {  
            const { data } = await this.$http({  
                url: this.$http.adornUrl('/spc/spcxrchart/chart/I_MR'),  
                method: 'get',  
            });  

            // 检查 data 对象是否包含 Xbar-R_Info 属性  
            if (data && Array.isArray(data['I-MR_Info'])) { // 注意这里用方括号访问对象属性  
                this.IMRChartData = data['I-MR_Info'];  
            } else {  
                console.error("Received data is missing I-MR_Info or is not an array:", data);  
                this.IMRChartData = []; // 设置为空数组  
            }  
        } catch (error) {  
            // 处理错误  
            console.error('Error:', error);  
            this.IMRChartData = []; // 出错时设置为空数组  
        }  
      },

      //调用spc/spcpchart/chart/p接口获取p图数据
      async getPchart() {  
        try {  
            const { data } = await this.$http({  
                url: this.$http.adornUrl('/spc/spcpchart/chart/p'),  
                method: 'get',  
            });  

            // 检查 data 对象是否包含 p-chart_Info 属性  
            if (data && Array.isArray(data['p-chart_Info'])) { // 注意这里用方括号访问对象属性  
                this.PChartData = data['p-chart_Info'];  
            } else {  
                console.error("Received data is missing p-chart_Info or is not an array:", data);  
                this.PChartData = []; // 设置为空数组  
            }  
        } catch (error) {  
            // 处理错误  
            console.error('Error:', error);  
            this.PChartData = []; // 出错时设置为空数组  
        }  
      },

      //调用spc/spcpchart/chart/np接口获取p图数据
      async getNPchart() {  
        try {  
            const { data } = await this.$http({  
                url: this.$http.adornUrl('/spc/spcpchart/chart/np'),  
                method: 'get',  
            });  

            // 检查 data 对象是否包含 np-chart_Info 属性  
            if (data && Array.isArray(data['np-chart_Info'])) { // 注意这里用方括号访问对象属性  
                this.NPChartData = data['np-chart_Info'];  
            } else {  
                console.error("Received data is missing np-chart_Info or is not an array:", data);  
                this.NPChartData = []; // 设置为空数组  
            }  
        } catch (error) {  
            // 处理错误  
            console.error('Error:', error);  
            this.NPChartData = []; // 出错时设置为空数组  
        }  
      },

      //调用spc/spcpchart/chart/u接口获取p图数据
      async getUchart() {  
        try {  
            const { data } = await this.$http({  
                url: this.$http.adornUrl('/spc/spcpchart/chart/u'),  
                method: 'get',  
            });  

            // 检查 data 对象是否包含 np-chart_Info 属性  
            if (data && Array.isArray(data['u-chart_Info'])) { // 注意这里用方括号访问对象属性  
                this.UChartData = data['u-chart_Info'];  
            } else {  
                console.error("Received data is missing u-chart_Info or is not an array:", data);  
                this.UChartData = []; // 设置为空数组  
            }  
        } catch (error) {  
            // 处理错误  
            console.error('Error:', error);  
            this.UChartData = []; // 出错时设置为空数组  
        }  
      },

      //调用spc/spcpchart/chart/c接口获取p图数据
      async getCchart() {  
        try {  
            const { data } = await this.$http({  
                url: this.$http.adornUrl('/spc/spcpchart/chart/c'),  
                method: 'get',  
            });  

            // 检查 data 对象是否包含 np-chart_Info 属性  
            if (data && Array.isArray(data['c-chart_Info'])) { // 注意这里用方括号访问对象属性  
                this.CChartData = data['c-chart_Info'];  
            } else {  
                console.error("Received data is missing c-chart_Info or is not an array:", data);  
                this.CChartData = []; // 设置为空数组  
            }  
        } catch (error) {  
            // 处理错误  
            console.error('Error:', error);  
            this.CChartData = []; // 出错时设置为空数组  
        }  
      },

      //调用spc/spcptd/chart/ptd接口获取p图数据
      async getPTDchart() {  
        try {  
            const { data } = await this.$http({  
                url: this.$http.adornUrl('/spc/spcptd/chart/ptd'),  
                method: 'get',  
            });  

            // 检查 data 对象是否包含 np-chart_Info 属性  
            if (data && Array.isArray(data['ptd-chart_Info'])) { // 注意这里用方括号访问对象属性  
                this.PTDChartData = data['ptd-chart_Info'];  
            } else {  
                console.error("Received data is missing ptd-chart_Info or is not an array:", data);  
                this.PTDChartData = []; // 设置为空数组  
            }  
        } catch (error) {  
            // 处理错误  
            console.error('Error:', error);  
            this.PTDChartData = []; // 出错时设置为空数组  
        }  
      },

      //导入相关方法
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
                  
                  //获取新数据
                  this.getXbarRchart();
                  this.getXbarSchart();
                  this.getMeRchart();
                  this.getIMRchart();
                  this.getPchart();
                  this.getNPchart();
                  this.getUchart();
                  this.getCchart();


                  // 更新图表Xbar-R
                  this.updateChartXbarR_Xbar();
                  this.updateChartXbarR_R();


                  this.updateChartXbarS_Xbar();
                  this.updateChartXbarS_S();

                  this.updateChartMeR_Me();
                  this.updateChartMeR_R();

                  this.updateChartIMR_I();
                  this.updateChartIMR_MR();

                  this.updateChartP();
                  this.updateChartNP();
                  this.updateChartU();
                  this.updateChartC();


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

      fileSendPTD() {
          const formData = new FormData();
          const file = document.getElementById("inputFile").files[0]; // 获取文件对象
          if (file) {
              formData.append("file", file);

              this.$http({
                  url: this.$http.adornUrl('/SPC/spc/PTD'),
                  method: 'post',
                  data: formData,
                  headers: {
                      'Content-Type': 'multipart/form-data' // 设置正确的请求头
                  }
              }).then(response => {
                  // response.data 包含了从服务器返回的数据
                  this.responseData = response.data;
                  
                  //获取新数据
                  this.getPTDchart();

                  // 更新图表Xbar-R
                  this.updateChartPTD();


                  // 2秒后关闭上传面板，这里应该根据实际情况来决定是否需要刷新页面
                  setTimeout(() => {
                      this.showDialog1 = false; // 关闭上传面板
                      this.getPTDChartDataList();
                      // 如果需要刷新页面，取消注释下面的代码
                      // location.reload();
                  }, 1000); // 2000毫秒后执行
              }).catch(error => {
                  // 处理错误
                  console.error('Error uploading file:', error);
              });
          } else {
              console.error('No file selected.');
          }
      },

      fileSendStandards(){
        const formData = new FormData();
        const file = this.$refs.fileInput.files[0]; // 使用 $refs 获取文件对象 
          if (file) {
              formData.append("file", file);

              this.$http({
                  url: this.$http.adornUrl('/SPC/spc/standards'),
                  method: 'post',
                  data: formData,
                  headers: {
                      'Content-Type': 'multipart/form-data' // 设置正确的请求头
                  }
              }).then(response => {
                  // response.data 包含了从服务器返回的数据
                  this.responseData = response.data;          

                  // 2秒后关闭上传面板，这里应该根据实际情况来决定是否需要刷新页面
                  setTimeout(() => {
                      this.showDialog2 = false; // 关闭上传面板
                      // 如果需要刷新页面，取消注释下面的代码
                      // location.reload();
                  }, 1000); // 2000毫秒后执行
              }).catch(error => {
                  // 处理错误
                  console.error('Error uploading file:', error);
              });
          } else {
              console.error('No file selected.');
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

      //图表相关方法

      /** -------------------------------------  **/ 

      //Xbar_R图表 Xbar-Chart
      //初始化图表
      initChartXbarR_Xbar() {

        const XbarRCharXbar = this.XbarRChartData[0];
        const XbarRCharXbarUCL = this.XbarRChartData[1];
        const XbarRCharXbarCL = this.XbarRChartData[2];
        const XbarRCharXbarLCL = this.XbarRChartData[3];
        const xAxisData = Array.from({ length: XbarRCharXbar.length }, (_, index) => index + 1);


        if (this.$refs.XbarR_XbarChart) {
          this.chartInstanceXbarR_Xbar = echarts.init(this.$refs.XbarR_XbarChart);
          
          const option = {
            legend: {
                data: ['X-bar', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'X-bar'
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
                    name: 'X-bar',
                    data: XbarRCharXbar,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: XbarRCharXbarUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: XbarRCharXbarCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: XbarRCharXbarLCL,
                    type: 'line'
                }
            ]
            };

          this.chartInstanceXbarR_Xbar.setOption(option);
        }
      },
      //更新图表
      updateChartXbarR_Xbar() {
        const XbarRCharXbar = this.XbarRChartData[0];
        const XbarRCharXbarUCL = this.XbarRChartData[1];
        const XbarRCharXbarCL = this.XbarRChartData[2];
        const XbarRCharXbarLCL = this.XbarRChartData[3];
        const xAxisData = Array.from({ length: XbarRCharXbar.length }, (_, index) => index + 1);

        const option = {
            legend: {
                data: ['X-bar', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'X-bar'
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
                    name: 'X-bar',
                    data: XbarRCharXbar,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: XbarRCharXbarUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: XbarRCharXbarCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: XbarRCharXbarLCL,
                    type: 'line'
                }
            ]
            };

        if (this.chartInstanceXbarR_Xbar) {
            this.chartInstanceXbarR_Xbar.setOption(option);
        }
      },

      //Xbar_R图表 R-Chart
      //初始化图表
      initChartXbarR_R() {

        const XbarRCharR = this.XbarRChartData[4];
        const XbarRCharRUCL = this.XbarRChartData[5];
        const XbarRCharRCL = this.XbarRChartData[6];
        const XbarRCharRLCL = this.XbarRChartData[7];
        const xAxisData = Array.from({ length: XbarRCharR.length }, (_, index) => index + 1);

        if (this.$refs.RChart) {
          this.chartInstanceXbarR_R = echarts.init(this.$refs.RChart);
          const option = {
            legend: {
                data: ['R', 'UCL', 'CL', 'LCL'] // 添加图例
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
                    name: 'R',
                    data: XbarRCharR,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: XbarRCharRUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: XbarRCharRCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: XbarRCharRLCL,
                    type: 'line'
                }
            ]
        };
          this.chartInstanceXbarR_R.setOption(option);
        }
      },
      //更新图表
      updateChartXbarR_R() {

        const XbarRCharR = this.XbarRChartData[4];
        const XbarRCharRUCL = this.XbarRChartData[5];
        const XbarRCharRCL = this.XbarRChartData[6];
        const XbarRCharRLCL = this.XbarRChartData[7];
        const xAxisData = Array.from({ length: XbarRCharR.length }, (_, index) => index + 1);

        const option = {
            legend: {
                data: ['R', 'UCL', 'CL', 'LCL'] // 添加图例
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
                    name: 'R',
                    data: XbarRCharR,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: XbarRCharRUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: XbarRCharRCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: XbarRCharRLCL,
                    type: 'line'
                }
            ]
        };
        if (this.chartInstanceXbarR_R) {
            this.chartInstanceXbarR_R.setOption(option);
        }
      },

      /** -------------------------------------  **/ 

      //Xbar-S图表 Xbar-Chart
      //初始化图表
      initChartXbarS_Xbar(){
        const XbarSCharXbar = this.XbarSChartData[0];
        const XbarSCharXbarUCL = this.XbarSChartData[1];
        const XbarSCharXbarCL = this.XbarSChartData[2];
        const XbarSCharXbarLCL = this.XbarSChartData[3];
        const xAxisData = Array.from({ length: XbarSCharXbar.length }, (_, index) => index + 1);

        if (this.$refs.XbarS_XbarChart) {
          this.chartInstanceXbarS_Xbar = echarts.init(this.$refs.XbarS_XbarChart);
          
          const option = {
            legend: {
                data: ['X-bar', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'X-bar'
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
                    name: 'X-bar',
                    data: XbarSCharXbar,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: XbarSCharXbarUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: XbarSCharXbarCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: XbarSCharXbarLCL,
                    type: 'line'
                }
            ]
            };

          this.chartInstanceXbarS_Xbar.setOption(option);
        }

      },
      //更新图表
      updateChartXbarS_Xbar(){
        const XbarSCharXbar = this.XbarSChartData[0];
        const XbarSCharXbarUCL = this.XbarSChartData[1];
        const XbarSCharXbarCL = this.XbarSChartData[2];
        const XbarSCharXbarLCL = this.XbarSChartData[3];
        const xAxisData = Array.from({ length: XbarSCharXbar.length }, (_, index) => index + 1);
        const option = {
            legend: {
                data: ['X-bar', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'X-bar'
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
                    name: 'X-bar',
                    data: XbarSCharXbar,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: XbarSCharXbarUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: XbarSCharXbarCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: XbarSCharXbarLCL,
                    type: 'line'
                }
            ]
        };
        if (this.chartInstanceXbarS_Xbar) {
            this.chartInstanceXbarS_Xbar.setOption(option);
        }
      },

      //Xbar-S图表 S-Chart
      //初始化图表
      initChartXbarS_S(){
        const XbarSCharS = this.XbarSChartData[4];
        const XbarSCharSUCL = this.XbarSChartData[5];
        const XbarSCharSCL = this.XbarSChartData[6];
        const XbarSCharSLCL = this.XbarSChartData[7];
        const xAxisData = Array.from({ length: XbarSCharS.length }, (_, index) => index + 1);

        if (this.$refs.XbarS_SChart) {
          this.chartInstanceXbarS_S = echarts.init(this.$refs.XbarS_SChart);
          
          const option = {
            legend: {
                data: ['S', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'S'
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
                    name: 'S',
                    data: XbarSCharS,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: XbarSCharSUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: XbarSCharSCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: XbarSCharSLCL,
                    type: 'line'
                }
            ]
            };

          this.chartInstanceXbarS_S.setOption(option);
        }
      },
      //更新图表
      updateChartXbarS_S(){
        const XbarSCharS = this.XbarSChartData[4];
        const XbarSCharSUCL = this.XbarSChartData[5];
        const XbarSCharSCL = this.XbarSChartData[6];
        const XbarSCharSLCL = this.XbarSChartData[7];
        const xAxisData = Array.from({ length: XbarSCharS.length }, (_, index) => index + 1);

        const option = {
            legend: {
                data: ['S', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'S'
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
                    name: 'S',
                    data: XbarSCharS,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: XbarSCharSUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: XbarSCharSCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: XbarSCharSLCL,
                    type: 'line'
                }
            ]
        };
        if (this.chartInstanceXbarS_Xbar) {
            this.chartInstanceXbarS_Xbar.setOption(option);
        }
      },

      /** -------------------------------------  **/ 

      //Me-R图表 Me-Chart
      //初始化图表
      initChartMeR_Me(){
        const MeRCharMe = this.MeRChartData[0];
        const MeRCharMeUCL = this.MeRChartData[1];
        const MeRCharMeCL = this.MeRChartData[2];
        const MeRCharMeLCL = this.MeRChartData[3];
        const xAxisData = Array.from({ length: MeRCharMe.length }, (_, index) => index + 1);

        if (this.$refs.MeR_MeChart) {
          this.chartInstanceMeR_Me = echarts.init(this.$refs.MeR_MeChart);
          
          const option = {
            legend: {
                data: ['Me', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'Me'
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
                    name: 'Me',
                    data: MeRCharMe,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: MeRCharMeUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: MeRCharMeCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: MeRCharMeLCL,
                    type: 'line'
                }
            ]
            };

          this.chartInstanceMeR_Me.setOption(option);
        }
      },
      //更新图表
      updateChartMeR_Me(){
        const MeRCharMe = this.MeRChartData[0];
        const MeRCharMeUCL = this.MeRChartData[1];
        const MeRCharMeCL = this.MeRChartData[2];
        const MeRCharMeLCL = this.MeRChartData[3];
        const xAxisData = Array.from({ length: MeRCharMe.length }, (_, index) => index + 1);
        const option = {
            legend: {
                data: ['Me', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'Me'
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
                    name: 'Me',
                    data: MeRCharMe,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: MeRCharMeUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: MeRCharMeCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: MeRCharMeLCL,
                    type: 'line'
                }
            ]
            };
        if (this.chartInstanceMeR_Me) {
            this.chartInstanceMeR_Me.setOption(option);
        }
      },

      //Me-R图表 R-Chart
      //初始化图表
      initChartMeR_R(){
        const MeRCharR = this.MeRChartData[4];
        const MeRCharRUCL = this.MeRChartData[5];
        const MeRCharRCL = this.MeRChartData[6];
        const MeRCharRLCL = this.MeRChartData[7];
        const xAxisData = Array.from({ length: MeRCharR.length }, (_, index) => index + 1);

        if (this.$refs.MeR_RChart) {
          this.chartInstanceMeR_R = echarts.init(this.$refs.MeR_RChart);
          
          const option = {
            legend: {
                data: ['R', 'UCL', 'CL', 'LCL'] // 添加图例
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
                    name: 'R',
                    data: MeRCharR,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: MeRCharRUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: MeRCharRCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: MeRCharRLCL,
                    type: 'line'
                }
            ]
          };

          this.chartInstanceMeR_R.setOption(option);
        }
      },
      //更新图表
      updateChartMeR_R(){
        const MeRCharR = this.MeRChartData[4];
        const MeRCharRUCL = this.MeRChartData[5];
        const MeRCharRCL = this.MeRChartData[6];
        const MeRCharRLCL = this.MeRChartData[7];
        const xAxisData = Array.from({ length: MeRCharR.length }, (_, index) => index + 1);
        const option = {
            legend: {
                data: ['R', 'UCL', 'CL', 'LCL'] // 添加图例
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
                    name: 'R',
                    data: MeRCharR,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: MeRCharRUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: MeRCharRCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: MeRCharRLCL,
                    type: 'line'
                }
            ]
        };
        if (this.chartInstanceMeR_R) {
            this.chartInstanceMeR_R.setOption(option);
        }
      },

      /** -------------------------------------  **/ 

      //I-MR图表 I-Chart
      //初始化图表
      initChartIMR_I(){
        const IMRCharR = this.IMRChartData[0];
        const IMRCharRUCL = this.IMRChartData[1];
        const IMRCharRCL = this.IMRChartData[2];
        const IMRCharRLCL = this.IMRChartData[3];
        const xAxisData = Array.from({ length: IMRCharR.length }, (_, index) => index + 1);

        if (this.$refs.IMR_IChart) {
          this.chartInstanceIMR_I = echarts.init(this.$refs.IMR_IChart);
          
          const option = {
            legend: {
                data: ['I', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'I'
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
                    name: 'I',
                    data: IMRCharR,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: IMRCharRUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: IMRCharRCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: IMRCharRLCL,
                    type: 'line'
                }
            ]
          };

          this.chartInstanceIMR_I.setOption(option);
        }
      },
      //更新图表
      updateChartIMR_I(){
        const IMRCharR = this.IMRChartData[0];
        const IMRCharRUCL = this.IMRChartData[1];
        const IMRCharRCL = this.IMRChartData[2];
        const IMRCharRLCL = this.IMRChartData[3];
        const xAxisData = Array.from({ length: IMRCharR.length }, (_, index) => index + 1);
        const option = {
            legend: {
                data: ['I', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'I'
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
                    name: 'I',
                    data: IMRCharR,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: IMRCharRUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: IMRCharRCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: IMRCharRLCL,
                    type: 'line'
                }
            ]
          };
        if (this.chartInstanceIMR_I) {
            this.chartInstanceIMR_I.setOption(option);
        }
      },

      //I-MR图表 MR-Chart
      //初始化图表
      initChartIMR_MR(){
        const IMRCharR = this.IMRChartData[4];
        const IMRCharRUCL = this.IMRChartData[5];
        const IMRCharRCL = this.IMRChartData[6];
        const IMRCharRLCL = this.IMRChartData[7];
        const xAxisData = Array.from({ length: IMRCharR.length }, (_, index) => index + 1);

        if (this.$refs.IMR_MRChart) {
          this.chartInstanceIMR_MR = echarts.init(this.$refs.IMR_MRChart);
          
          const option = {
            legend: {
                data: ['MR', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'MR'
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
                    name: 'MR',
                    data: IMRCharR,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: IMRCharRUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: IMRCharRCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: IMRCharRLCL,
                    type: 'line'
                }
            ]
          };

          this.chartInstanceIMR_MR.setOption(option);
        }
      },
      //更新图表
      updateChartIMR_MR(){
        const IMRCharR = this.IMRChartData[4];
        const IMRCharRUCL = this.IMRChartData[5];
        const IMRCharRCL = this.IMRChartData[6];
        const IMRCharRLCL = this.IMRChartData[7];
        const xAxisData = Array.from({ length: IMRCharR.length }, (_, index) => index + 1);
        const option = {
            legend: {
                data: ['MR', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'MR'
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
                    name: 'MR',
                    data: IMRCharR,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: IMRCharRUCL,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: IMRCharRCL,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: IMRCharRLCL,
                    type: 'line'
                }
            ]
          };
        if (this.chartInstanceIMR_MR) {
            this.chartInstanceIMR_MR.setOption(option);
        }
      },

      /** -------------------------------------  **/ 
      //计数型图表
      /** -------------------------------------  **/ 

      //P-Chart图表
      //初始化图表
      initChartP() {
        const PChar = this.PChartData[0].map(value => value * 100); // 将不良率转换为百分数  
        const PCharUCL = this.PChartData[1].map(value => value * 100); // 将 UCL 转换为百分数  
        const PCharCL = this.PChartData[2].map(value => value * 100); // 将 CL 转换为百分数  
        const PCharLCL = this.PChartData[3].map(value => value * 100); // 将 LCL 转换为百分数  
        const xAxisData = Array.from({ length: PChar.length }, (_, index) => index + 1); 

        if (this.$refs.lineChartP) {
          this.chartInstanceP = echarts.init(this.$refs.lineChartP);

          const option = {
            legend: {
                data: ['不良率', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'P'
            },
            tooltip: {
                trigger: 'axis',  
                formatter: function (params) {  
                    // 格式化显示为百分数  
                    return params.map(param => `${param.seriesName}: ${param.data.toFixed(2)}%`).join('<br/>');  
                } 
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
                    data: PChar,
                    type: 'line'
                },
                {  
                    name: 'UCL',  
                    data: PCharUCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'CL',  
                    data: PCharCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'LCL',  
                    data: PCharLCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                } 
            ]
          };

          this.chartInstanceP.setOption(option);
        }
      },
      //更新图表
      updateChartP() {
        const PChart = this.PChartData[0].map(value => value * 100); // 将不良率转换为百分数  
        const PChartUCL = this.PChartData[1].map(value => value * 100); // 将 UCL 转换为百分数  
        const PChartCL = this.PChartData[2].map(value => value * 100); // 将 CL 转换为百分数  
        const PChartLCL = this.PChartData[3].map(value => value * 100); // 将 LCL 转换为百分数  
        const xAxisData = Array.from({ length: PChart.length }, (_, index) => index + 1); 

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
                data: PChart,
                type: 'line'
            },
            {
                name: 'UCL',
                data: PChartUCL,
                type: 'line'
            },
            {
                name: 'CL',
                data: PChartCL,
                type: 'line'
            },
            {
                name: 'LCL',
                data: PChartLCL,
                type: 'line'
            }
            ]
        };
        if (this.chartInstanceP) {
            this.chartInstanceP.setOption(option);
        }
      },

      /** -------------------------------------  **/ 

      //NP-Chart图表
      //初始化图表
      initChartNP() {
        const NPChart = this.NPChartData[0]; 
        const NPChartUCL = this.NPChartData[1]; 
        const NPCharCL = this.NPChartData[2]; 
        const NPCharLCL = this.NPChartData[3];
        const xAxisData = Array.from({ length: NPChart.length }, (_, index) => index + 1); 

        if (this.$refs.lineChartNP) {
          this.chartInstanceNP = echarts.init(this.$refs.lineChartNP);

          const option = {
            legend: {
                data: ['NP', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'NP'
            },
            tooltip: {
                trigger: 'axis',  
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
                    name: '不合格品数量',
                    data: NPChart,
                    type: 'line'
                },
                {  
                    name: 'UCL',  
                    data: NPChartUCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'CL',  
                    data: NPCharCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'LCL',  
                    data: NPCharLCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                } 
            ]
          };

          this.chartInstanceNP.setOption(option);
        }
      },
      //更新图表
      updateChartNP() {
        const NPChart = this.NPChartData[0];  
        const NPChartUCL = this.NPChartData[1]; 
        const NPCharCL = this.NPChartData[2]; 
        const NPCharLCL = this.NPChartData[3];  
        const xAxisData = Array.from({ length: NPChart.length }, (_, index) => index + 1); 

        const option = {
            legend: {
                data: ['NP', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'NP'
            },
            tooltip: {
                trigger: 'axis',  
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
                    name: '不合格品数量',
                    data: NPChart,
                    type: 'line'
                },
                {  
                    name: 'UCL',  
                    data: NPChartUCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'CL',  
                    data: NPCharCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'LCL',  
                    data: NPCharLCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                } 
            ]
          };
        if (this.chartInstanceNP) {
            this.chartInstanceNP.setOption(option);
        }
      },

      /** -------------------------------------  **/ 

      //U-Chart图表
      //初始化图表
      initChartU() {
        const UChart = this.UChartData[0].map(value => value * 100); // 将不良率转换为百分数  
        const UChartUCL = this.UChartData[1].map(value => value * 100); // 将 UCL 转换为百分数  
        const UChartCL = this.UChartData[2].map(value => value * 100); // 将 CL 转换为百分数  
        const UChartLCL = this.UChartData[3].map(value => value * 100); // 将 LCL 转换为百分数  
        const xAxisData = Array.from({ length: UChart.length }, (_, index) => index + 1); 

        if (this.$refs.lineChartU) {
          this.chartInstanceU = echarts.init(this.$refs.lineChartU);

          const option = {
            legend: {
                data: ['不良率', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'u'
            },
            tooltip: {
                trigger: 'axis',  
                formatter: function (params) {  
                    // 格式化显示为百分数  
                    return params.map(param => `${param.seriesName}: ${param.data.toFixed(2)}%`).join('<br/>');  
                } 
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
                    data: UChart,
                    type: 'line'
                },
                {  
                    name: 'UCL',  
                    data: UChartUCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'CL',  
                    data: UChartCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'LCL',  
                    data: UChartLCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                } 
            ]
          };

          this.chartInstanceU.setOption(option);
        }
      },
      //更新图表
      updateChartU() {
        const UChart = this.UChartData[0].map(value => value * 100); // 将不良率转换为百分数  
        const UChartUCL = this.UChartData[1].map(value => value * 100); // 将 UCL 转换为百分数  
        const UChartCL = this.UChartData[2].map(value => value * 100); // 将 CL 转换为百分数  
        const UChartLCL = this.UChartData[3].map(value => value * 100); // 将 LCL 转换为百分数  
        const xAxisData = Array.from({ length: UChart.length }, (_, index) => index + 1); 

        const option = {
            legend: {
                data: ['不良率', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'u'
            },
            tooltip: {
                trigger: 'axis',  
                formatter: function (params) {  
                    // 格式化显示为百分数  
                    return params.map(param => `${param.seriesName}: ${param.data.toFixed(2)}%`).join('<br/>');  
                } 
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
                    data: UChart,
                    type: 'line'
                },
                {  
                    name: 'UCL',  
                    data: UChartUCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'CL',  
                    data: UChartCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'LCL',  
                    data: UChartLCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                } 
            ]
          };
        
        if (this.chartInstanceU) {
            this.chartInstanceU.setOption(option);
        }
      },

      /** -------------------------------------  **/ 

      //NP-Chart图表
      //初始化图表
      initChartC() {
        const CChart = this.CChartData[0]; 
        const CChartUCL = this.CChartData[1]; 
        const CCharCL = this.CChartData[2]; 
        const CCharLCL = this.CChartData[3];
        const xAxisData = Array.from({ length: CChart.length }, (_, index) => index + 1); 

        if (this.$refs.lineChartC) {
          this.chartInstanceC = echarts.init(this.$refs.lineChartC);

          const option = {
            legend: {
                data: ['不合格品数量', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'C'
            },
            tooltip: {
                trigger: 'axis',  
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
                    name: '不合格品数量',
                    data: CChart,
                    type: 'line'
                },
                {  
                    name: 'UCL',  
                    data: CChartUCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'CL',  
                    data: CCharCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'LCL',  
                    data: CCharLCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                } 
            ]
          };

          this.chartInstanceC.setOption(option);
        }
      },
      //更新图表
      updateChartC() {
        const CChart = this.CChartData[0]; 
        const CChartUCL = this.CChartData[1]; 
        const CCharCL = this.CChartData[2]; 
        const CCharLCL = this.CChartData[3];
        const xAxisData = Array.from({ length: CChart.length }, (_, index) => index + 1); 

        const option = {
            legend: {
                data: ['不合格品数量', 'UCL', 'CL', 'LCL'] // 添加图例
            },
            title: {
                text: 'C'
            },
            tooltip: {
                trigger: 'axis',  
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
                    name: '不合格品数量',
                    data: CChart,
                    type: 'line'
                },
                {  
                    name: 'UCL',  
                    data: CChartUCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'CL',  
                    data: CCharCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                },  
                {  
                    name: 'LCL',  
                    data: CCharLCL,  
                    type: 'line',  
                    areaStyle: {}, // 可选: 添加面积填充  
                    step: true // 设置为阶梯图  
                } 
            ]
          };
        if (this.chartInstanceC) {
            this.chartInstanceC.setOption(option);
        }
      },

      /** -------------------------------------  **/ 

      //正态分布图表
      //初始化图表
      initChartPTD() {
        const xAxisData = this.PTDChartData[0]; 
        const Frequency = this.PTDChartData[1]; 
        const PTD = this.PTDChartData[2]; 

        if (this.$refs.lineChartPTD) {
          this.chartInstancePTD = echarts.init(this.$refs.lineChartPTD);

        // 计算 Frequency 的最大值和最小值  
        const frequencyMin = Math.min(...Frequency);  
        const frequencyMax = Math.max(...Frequency);  

        const option = {  
            legend: {  
                data: ['频率', '正态分布值'] // 添加图例  
            },  
            title: {  
                text: '正态分布'  
            },  
            tooltip: {  
                trigger: 'axis',  
            },  
            xAxis: {  
                type: 'category',  
                boundaryGap: false,  
                data: xAxisData  
            },  
            yAxis: [  
                {  
                    type: 'value',  
                    name: '频率', // 为左侧的 Y 轴设置名称  
                    min: Math.floor(frequencyMin), // 设置 Y 轴的最小值  
                    max: Math.ceil(frequencyMax), // 设置 Y 轴的最大值  
                    splitLine: { show: false } // 隐藏 Y 轴的分隔线  
                },  
                {  
                    type: 'value',  
                    name: '正态分布值', // 为右侧的 Y 轴设置名称  
                    position: 'right', // 设置 Y 轴的位置为右侧  
                    splitLine: { show: false } // 隐藏右侧 Y 轴的分隔线  
                }  
            ],  
            series: [  
                {  
                    name: '频率',  
                    data: Frequency,  
                    type: 'bar', // 将类型改为柱状图  
                    yAxisIndex: 0 // 使用左侧的 Y 轴  
                },  
                {  
                    name: '正态分布值',  
                    data: PTD,  
                    type: 'line',  
                    smooth: true, // 将曲线设置为平滑  
                    areaStyle: {}, // 可选: 添加面积填充  
                    yAxisIndex: 1 // 使用右侧的 Y 轴  
                }  
            ]  
        };  

          this.chartInstancePTD.setOption(option);
        }
      },
      //更新图表
      updateChartPTD() {
        const xAxisData = this.PTDChartData[0]; 
        const Frequency = this.PTDChartData[1]; 
        const PTD = this.PTDChartData[2]; 


        // 计算 Frequency 的最大值和最小值  
        const frequencyMin = Math.min(...Frequency);  
        const frequencyMax = Math.max(...Frequency);


        const option = {  
            legend: {  
                data: ['频率', '正态分布值'] // 添加图例  
            },  
            title: {  
                text: '正态分布'  
            },  
            tooltip: {  
                trigger: 'axis',  
            },  
            xAxis: {  
                type: 'category',  
                boundaryGap: false,  
                data: xAxisData  
            },  
            yAxis: [  
                {  
                    type: 'value',  
                    name: '频率', // 为左侧的 Y 轴设置名称  
                    min: Math.floor(frequencyMin), // 设置 Y 轴的最小值  
                    max: Math.ceil(frequencyMax), // 设置 Y 轴的最大值  
                    splitLine: { show: false } // 隐藏 Y 轴的分隔线  
                },  
                {  
                    type: 'value',  
                    name: '正态分布值', // 为右侧的 Y 轴设置名称  
                    position: 'right', // 设置 Y 轴的位置为右侧  
                    splitLine: { show: false } // 隐藏右侧 Y 轴的分隔线  
                }  
            ],  
            series: [  
                {  
                    name: '频率',  
                    data: Frequency,  
                    type: 'bar', // 将类型改为柱状图  
                    yAxisIndex: 0 // 使用左侧的 Y 轴  
                },  
                {  
                    name: '正态分布值',  
                    data: PTD,  
                    type: 'line',  
                    smooth: true, // 将曲线设置为平滑  
                    areaStyle: {}, // 可选: 添加面积填充  
                    yAxisIndex: 1 // 使用右侧的 Y 轴  
                }  
            ]  
        };  
        
        if (this.chartInstancePTD) {
            this.chartInstancePTD.setOption(option);
        }
      },

      /** -------------------------------------  **/ 

    }
  }
</script>
