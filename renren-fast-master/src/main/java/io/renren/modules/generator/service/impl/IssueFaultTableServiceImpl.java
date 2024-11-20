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
        IPage<IssueFaultTableEntity> page = this.page(
                new Query<IssueFaultTableEntity>().getPage(params),
                new QueryWrapper<IssueFaultTableEntity>()
        );

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

            // 用于存储上一次的字段值
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

                // 创建实体对象
                IssueFaultTableEntity issue = new IssueFaultTableEntity();

                // 获取每一列的值
                String currentSystematicClassification = getCellValueAsString(row.getCell(0));
                String currentFirstFaultyParts = getCellValueAsString(row.getCell(1));
                String currentSecondFaultyParts = getCellValueAsString(row.getCell(2));
                String currentFaultType = getCellValueAsString(row.getCell(3));
                String currentFaultModel = getCellValueAsString(row.getCell(4));

                // 判断是否有新值，若前四个字段有新值，则强制后面的字段为空值
                boolean hasNewValue = false;

                if (currentSystematicClassification != null && !currentSystematicClassification.isEmpty()) {
                    systematicClassification = currentSystematicClassification;
                    hasNewValue = true;
                }
                if (currentFirstFaultyParts != null && !currentFirstFaultyParts.isEmpty()) {
                    firstFaultyParts = currentFirstFaultyParts;
                    hasNewValue = true;
                }
                if (currentSecondFaultyParts != null && !currentSecondFaultyParts.isEmpty()) {
                    secondFaultyParts = currentSecondFaultyParts;
                    hasNewValue = true;
                }
                if (currentFaultType != null && !currentFaultType.isEmpty()) {
                    faultType = currentFaultType;
                    hasNewValue = true;
                }

                // 如果有新值，则后续字段为空时强制置为空
                if (hasNewValue) {
                    secondFaultyParts = (currentSecondFaultyParts != null && !currentSecondFaultyParts.isEmpty()) ? currentSecondFaultyParts : null;
                    faultType = (currentFaultType != null && !currentFaultType.isEmpty()) ? currentFaultType : null;
                    faultModel = (currentFaultModel != null && !currentFaultModel.isEmpty()) ? currentFaultModel : null;
                } else {
                    // 逐字段检查是否更新
                    if (currentSecondFaultyParts != null && !currentSecondFaultyParts.isEmpty()) {
                        secondFaultyParts = currentSecondFaultyParts;
                    }
                    if (currentFaultType != null && !currentFaultType.isEmpty()) {
                        faultType = currentFaultType;
                    }
                    if (currentFaultModel != null && !currentFaultModel.isEmpty()) {
                        faultModel = currentFaultModel;
                    }
                }

                // 设置实体属性
                issue.setSystematicClassification(systematicClassification);
                issue.setFirstFaultyParts(firstFaultyParts);
                issue.setSecondFaultyParts(secondFaultyParts);
                issue.setFaultType(faultType);
                issue.setFaultModel(faultModel);

                // 添加到列表中
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