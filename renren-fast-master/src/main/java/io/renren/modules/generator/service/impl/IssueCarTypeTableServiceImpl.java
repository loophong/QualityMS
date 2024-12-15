package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.generator.dao.IssueCarTypeTableDao;
import io.renren.modules.generator.entity.IssueCarTypeTableEntity;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.service.IssueCarTypeTableService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("issueCarTypeTableService")
public class IssueCarTypeTableServiceImpl extends ServiceImpl<IssueCarTypeTableDao, IssueCarTypeTableEntity> implements IssueCarTypeTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<IssueCarTypeTableEntity> queryWrapper = new QueryWrapper<>();

        // 按 ID 降序排序
        queryWrapper.orderByDesc("vehicle_type_id");

        // 分页查询
        IPage<IssueCarTypeTableEntity> page = this.page(
                new Query<IssueCarTypeTableEntity>().getPage(params),
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
            Sheet sheet = workbook.getSheetAt(0); // 读取第一个sheet
            List<IssueCarTypeTableEntity> issueList = new ArrayList<>();

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                // 跳过第一行（标题行）
                if (rowIndex == 0) {
                    continue; // 继续到下一循环，不处理这一行
                }

                IssueCarTypeTableEntity issuetype = new IssueCarTypeTableEntity();

                // 读取车型
                issuetype.setConcreteVehicleType(getCellValueAsString(row.getCell(0)));

                // 将 issue 对象添加到列表中
                issueList.add(issuetype);
            }


            // 保存到数据库
            this.saveBatch(issueList); // 批量保存
            System.out.println("上传--------------结束");
            return R.ok().put("message", "上传成功，已保存 " + issueList.size() + " 条数据");
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("上传失败");
        }
    }

    @Override
    public List<IssueCarTypeTableEntity> listAll() {
        return this.list();
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