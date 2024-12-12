package io.renren.modules.generator.service.impl;

import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueCarTypeTableEntity;
import io.renren.modules.generator.entity.IssueTableEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.IssueFaultTableDao;
import io.renren.modules.generator.entity.IssueFaultTableEntity;
import io.renren.modules.generator.service.IssueFaultTableService;
import org.springframework.web.multipart.MultipartFile;


@Service("issueFaultTableService")
public class IssueFaultTableServiceImpl extends ServiceImpl<IssueFaultTableDao, IssueFaultTableEntity> implements IssueFaultTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<IssueFaultTableEntity> queryWrapper = new QueryWrapper<>();

        // 按 ID 降序排序
        queryWrapper.orderByAsc("fault_ID");

        // 分页查询
        IPage<IssueFaultTableEntity> page = this.page(
                new Query<IssueFaultTableEntity>().getPage(params),
                queryWrapper
        );

        // 返回分页数据
        return new PageUtils(page);
    }


    @Override
    public R uploadExcelFile(MultipartFile file) throws IOException {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
            return R.error("请上传有效的Excel文件");
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(2); // 读取第三个sheet
            List<IssueFaultTableEntity> issueList = new ArrayList<>();

            // 上一次的字段值，用于继承
            String systematicClassification = null;
            String firstFaultyParts = null;
            String secondFaultyParts = null;
            String faultType = null;
            String faultModel = null;

            // 遍历每一行
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                // 跳过第一行（标题行）
                if (rowIndex == 0) {
                    continue;
                }

                // 跳过空行
                if (row == null) {
                    continue;
                }

                // 获取当前行每一列的值
                String currentSystematicClassification = getCellValueAsString(row.getCell(0));
                String currentFirstFaultyParts = getCellValueAsString(row.getCell(1));
                String currentSecondFaultyParts = getCellValueAsString(row.getCell(2));
                String currentFaultType = getCellValueAsString(row.getCell(3));
                String currentFaultModel = getCellValueAsString(row.getCell(4));

                // 判断是否有新值（第一个字段）
                boolean hasNewValue = false;

                if (currentSystematicClassification != null && !currentSystematicClassification.isEmpty()) {
                    systematicClassification = currentSystematicClassification;
                    hasNewValue = true;
                }

                // 如果发现新值，继承前面字段，清空后面字段
                if (hasNewValue) {
                    firstFaultyParts = (currentFirstFaultyParts != null && !currentFirstFaultyParts.isEmpty()) ? currentFirstFaultyParts : null;
                    secondFaultyParts = (currentSecondFaultyParts != null && !currentSecondFaultyParts.isEmpty()) ? currentSecondFaultyParts : null;
                    faultType = (currentFaultType != null && !currentFaultType.isEmpty()) ? currentFaultType : null;
                    faultModel = (currentFaultModel != null && !currentFaultModel.isEmpty()) ? currentFaultModel : null;
                } else {
                    // 逐个字段检查是否有值，无值则继承上一行
                    firstFaultyParts = (currentFirstFaultyParts != null && !currentFirstFaultyParts.isEmpty()) ? currentFirstFaultyParts : firstFaultyParts;
                    secondFaultyParts = (currentSecondFaultyParts != null && !currentSecondFaultyParts.isEmpty()) ? currentSecondFaultyParts : secondFaultyParts;
                    faultType = (currentFaultType != null && !currentFaultType.isEmpty()) ? currentFaultType : faultType;
                    faultModel = (currentFaultModel != null && !currentFaultModel.isEmpty()) ? currentFaultModel : faultModel;
                }

                // 创建实体对象并设置字段
                IssueFaultTableEntity issue = new IssueFaultTableEntity();
                issue.setSystematicClassification(systematicClassification);
                issue.setFirstFaultyParts(firstFaultyParts);
                issue.setSecondFaultyParts(secondFaultyParts);
                issue.setFaultType(faultType);
                issue.setFaultModel(faultModel);

                // 添加到列表
                issueList.add(issue);
            }

            // 批量保存到数据库
            if (!issueList.isEmpty()) {
                this.saveBatch(issueList); // 批量保存
            }

            return R.ok().put("message", "上传成功，已保存 " + issueList.size() + " 条数据");
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("上传失败，发生异常：" + e.getMessage());
        }
    }

    @Override
    public PageUtils QueryPage(Map<String, Object> params) {
        // 提取查询参数
        String selectedSystematic = (String) params.get("selectedSystematic");
        String selectedFirstFaultyParts = (String) params.get("selectedFirstFaultyParts");
        String selectedSecondFaultyParts = (String) params.get("selectedSecondFaultyParts");
        String selectedFaultType = (String) params.get("selectedFaultType");
        String selectedFaultModel = (String) params.get("selectedFaultModel");

        // 创建查询条件
        QueryWrapper<IssueFaultTableEntity> queryWrapper = new QueryWrapper<>();

        // 如果 selectedSystematic 不为空，则添加模糊查询条件
        if (selectedSystematic != null && !selectedSystematic.isEmpty()) {
            queryWrapper.like("Systematic_classification", selectedSystematic);
        }

        // 如果 selectedFirstFaultyParts 不为空，则添加模糊查询条件
        if (selectedFirstFaultyParts != null && !selectedFirstFaultyParts.isEmpty()) {
            queryWrapper.like("First_Faulty_parts", selectedFirstFaultyParts);
        }

        // 如果 selectedSecondFaultyParts 不为空，则添加模糊查询条件
        if (selectedSecondFaultyParts != null && !selectedSecondFaultyParts.isEmpty()) {
            queryWrapper.like("Second_Faulty_parts", selectedSecondFaultyParts);
        }

        // 如果 selectedFaultType 不为空，则添加模糊查询条件
        if (selectedFaultType != null && !selectedFaultType.isEmpty()) {
            queryWrapper.like("fault_type", selectedFaultType);
        }

        // 如果 selectedFaultModel 不为空，则添加模糊查询条件
        if (selectedFaultModel != null && !selectedFaultModel.isEmpty()) {
            queryWrapper.like("fault_model", selectedFaultModel);
        }

        // 执行分页查询并返回结果
        IPage<IssueFaultTableEntity> page = this.page(
                new Query<IssueFaultTableEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }



    // 工具方法：获取单元格值为字符串
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return null;
        }
    }



}