package io.renren.modules.generator.service.impl;

import io.renren.common.utils.R;
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

import io.renren.modules.generator.dao.PeliminaryAnalysisTableDao;
import io.renren.modules.generator.entity.PeliminaryAnalysisTableEntity;
import io.renren.modules.generator.service.PeliminaryAnalysisTableService;
import org.springframework.web.multipart.MultipartFile;


@Service("peliminaryAnalysisTableService")
public class PeliminaryAnalysisTableServiceImpl extends ServiceImpl<PeliminaryAnalysisTableDao, PeliminaryAnalysisTableEntity> implements PeliminaryAnalysisTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<PeliminaryAnalysisTableEntity> queryWrapper = new QueryWrapper<>();

        // 按 ID 降序排序
        queryWrapper.orderByDesc("analysis_ID");

        // 分页查询
        IPage<PeliminaryAnalysisTableEntity> page = this.page(
                new Query<PeliminaryAnalysisTableEntity>().getPage(params),
                queryWrapper
        );

        // 返回分页数据
        return new PageUtils(page);
    }


    @Override
    public List<PeliminaryAnalysisTableEntity> listAll() {
        return this.list();
    }

    @Override
    public R uploadExcelFile(MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
            return R.error("请上传有效的Excel文件");
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 读取第一个sheet
            List<PeliminaryAnalysisTableEntity> peliminaryAnalysisTableEntities = new ArrayList<>();

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                // 跳过第一行（标题行）
                if (rowIndex == 0) {
                    continue; // 继续到下一循环，不处理这一行
                }

                PeliminaryAnalysisTableEntity peliminaryAnalysisTable = new PeliminaryAnalysisTableEntity();

                // 读取序列号
                peliminaryAnalysisTable.setPeliminaryAnalysis(getCellValueAsString(row.getCell(0)));



                // 将 issue 对象添加到列表中
                peliminaryAnalysisTableEntities.add(peliminaryAnalysisTable);
            }


            // 保存到数据库
            this.saveBatch(peliminaryAnalysisTableEntities); // 批量保存
            System.out.println("上传--------------结束");
            return R.ok().put("message", "上传成功，已保存 " + peliminaryAnalysisTableEntities.size() + " 条数据");
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("上传失败");
        }
    }
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