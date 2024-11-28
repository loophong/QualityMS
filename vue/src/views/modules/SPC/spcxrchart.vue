<template>
  <div class="mod-config">

    <!-- 标签页 -->
    <el-tabs v-model="activeName" type="card" >

      <!-- X图数据及图表 -->
      <el-tab-pane label="X_Chart" name="first">
        <div ref="lineChart1" style="width: 1500px;height:300px;"></div>
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getXRChartDataList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getXRChartDataList()">查询</el-button>
            <el-button v-if="isAuth('spc:spcxrchart:save')" type="primary" @click="XRChartaddOrUpdateHandle()">新增</el-button>
            <el-button v-if="isAuth('spc:spcxrchart:delete')" type="danger" @click="XRChartdeleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
            <el-button type="primary" @click="showDialog = true">导入Excel文件</el-button>
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

      <!-- R图数据及图表 -->
      <el-tab-pane label="R_Chart" name="second">
        <div ref="lineChart2" style="width: 1500px;height:300px;"></div>
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
      </el-tab-pane>

      <!-- P图数据及图表 -->
      <el-tab-pane label="P_Chart" name="third">

        <div ref="lineChart3" style="width: 1500px;height:300px;"></div>
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

      </el-tab-pane>

      <el-tab-pane label="正态分布" name="fourth">正态分布</el-tab-pane>

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

    </el-tabs>



  </div>
</template>

<script>
  import AddOrUpdate from './spcxrchart-add-or-update';
  import PChartAddOrUpdate from './spcpchart-add-or-update';

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

        // 标签页设置参数
        activeName: 'first',   //此参数将决定标签页停留在哪一页

        //导入相关参数
        showDialog: false,
        progress: 0,
        chartInstance1: null,
        chartInstance2: null,
        chartInstance3: null,
        responseData: null,

        defaultData: [[1, 2, 3, 4, 5], [5, 4, 3, 2, 1]],
      }
    },
    components: {
      AddOrUpdate,
      PChartAddOrUpdate,

    },
    activated () {
      this.getXRChartDataList();
      this.getPChartDataList();
    },
    mounted() {
      this.initChart1();
      this.initChart2();
      this.initChart3();
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
              }).then(() => {  
                  this.getXRChartDataList(); 
                  this.refreshDisplay(); 
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
      //X_Chart图表
      //初始化图表
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

      //更新图表
      updateChart() {
        const firstSeriesData = this.responseData[1];
        const uclSeriesData = this.responseData[2];
        const clSeriesData = this.responseData[3];
        const lclSeriesData = this.responseData[4];
        const xAxisData = Array.from({ length: firstSeriesData.length }, (_, index) => index + 1);

        const option = {
            legend: {
                data: ['X', 'UCL', 'CL', 'LCL'] // 添加图例
            },
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
            series: [
                {
                    name: 'X',
                    data: firstSeriesData,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: uclSeriesData,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: clSeriesData,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: lclSeriesData,
                    type: 'line'
                }
            ]
            };

        if (this.chartInstance1) {
            this.chartInstance1.setOption(option);
        }
      },

      //R_Chart图表
      //初始化图表
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
      //更新图表
      updateChart2() {

        const secondSeriesData = this.responseData[5];
        const uclSeriesData = this.responseData[6];
        const clSeriesData = this.responseData[7];
        const lclSeriesData = this.responseData[8];
        const xAxisData = Array.from({ length: secondSeriesData.length }, (_, index) => index);
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
                    data: secondSeriesData,
                    type: 'line'
                },
                {
                    name: 'UCL',
                    data: uclSeriesData,
                    type: 'line'
                },
                {
                    name: 'CL',
                    data: clSeriesData,
                    type: 'line'
                },
                {
                    name: 'LCL',
                    data: lclSeriesData,
                    type: 'line'
                }
            ]
        };
        if (this.chartInstance2) {
            this.chartInstance2.setOption(option);
        }
      },

    //P_Chart图表
    //初始化图表
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

    //更新图表
    updateChart3() {
      const secondSeriesData3 = this.responseData[9].map(value => Number((value * 100).toFixed(2)));
      const secondSeriesData4 = this.responseData[10].map(value => Number((value * 100).toFixed(2)));
      const secondSeriesData5 = this.responseData[11].map(value => Number((value * 100).toFixed(2)));
      const secondSeriesData6 = this.responseData[12].map(value => Number((value * 100).toFixed(2)));

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

    }
  }
</script>
