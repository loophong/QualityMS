package io.renren.modules.generator.service.impl;

import io.renren.common.utils.R;
import io.renren.modules.generator.entity.PeliminaryAnalysisTableEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.IndemnificationTableDao;
import io.renren.modules.generator.entity.IndemnificationTableEntity;
import io.renren.modules.generator.service.IndemnificationTableService;
import org.springframework.web.multipart.MultipartFile;


@Service("indemnificationTableService")
public class IndemnificationTableServiceImpl extends ServiceImpl<IndemnificationTableDao, IndemnificationTableEntity> implements IndemnificationTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IndemnificationTableEntity> page = this.page(
                new Query<IndemnificationTableEntity>().getPage(params),
                new QueryWrapper<IndemnificationTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<IndemnificationTableEntity> getAllIndemnifications() {
        return this.list();
    }

    @Override
    public IndemnificationTableEntity getIndemnificationById(int id) {
        return this.getOne(new QueryWrapper<IndemnificationTableEntity>().eq("Inde_id", id));
    }

    @Override
    public R uploadExcelFile(MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
            return R.error("请上传有效的Excel文件");
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 读取第一个sheet
            List<IndemnificationTableEntity> indemnificationTableEntities = new ArrayList<>();

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                // 跳过第一行（标题行）
                if (rowIndex == 0) {
                    continue; // 继续到下一循环，不处理这一行
                }

                IndemnificationTableEntity indemnificationTable = new IndemnificationTableEntity();

                // 读取名称
                indemnificationTable.setIndemnification(getCellValueAsString(row.getCell(1)));

                // 读取名称
                indemnificationTable.setFigureNumber(getCellValueAsString(row.getCell(2)));

                // 读取名称
                indemnificationTable.setQualitycost(Double.valueOf(getCellValueAsString(row.getCell(3))));
                System.out.println("实际上的数值" + getCellValueAsString(row.getCell(3)));
                System.out.println("转换后的数值" + Double.valueOf(getCellValueAsString(row.getCell(3))));
                System.out.println("赋予的数值" + indemnificationTable.getQualitycost());


                // 将 issue 对象添加到列表中
                indemnificationTableEntities.add(indemnificationTable);
            }


            // 保存到数据库
            this.saveBatch(indemnificationTableEntities); // 批量保存
            System.out.println("上传--------------结束");
            return R.ok().put("message", "上传成功，已保存 " + indemnificationTableEntities.size() + " 条数据");
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