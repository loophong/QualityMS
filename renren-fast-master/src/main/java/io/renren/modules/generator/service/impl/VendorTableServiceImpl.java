package io.renren.modules.generator.service.impl;

import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IndemnificationTableEntity;
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

import io.renren.modules.generator.dao.VendorTableDao;
import io.renren.modules.generator.entity.VendorTableEntity;
import io.renren.modules.generator.service.VendorTableService;
import org.springframework.web.multipart.MultipartFile;


@Service("vendorTableService")
public class VendorTableServiceImpl extends ServiceImpl<VendorTableDao, VendorTableEntity> implements VendorTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<VendorTableEntity> page = this.page(
                new Query<VendorTableEntity>().getPage(params),
                new QueryWrapper<VendorTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<VendorTableEntity> listAll() {
        return this.list();
    }

    @Override
    public R uploadExcelFile(MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
            return R.error("请上传有效的Excel文件");
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 读取第一个sheet
            List<VendorTableEntity> vendorTableEntities = new ArrayList<>();

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                // 跳过第一行（标题行）
                if (rowIndex == 0) {
                    continue; // 继续到下一循环，不处理这一行
                }

                VendorTableEntity vendorTable = new VendorTableEntity();

                // 读取名称
                vendorTable.setVendornumber(getCellValueAsString(row.getCell(1)));

                // 读取名称
                vendorTable.setVendor(getCellValueAsString(row.getCell(2)));


                // 将 issue 对象添加到列表中
                vendorTableEntities.add(vendorTable);
            }


            // 保存到数据库
            this.saveBatch(vendorTableEntities); // 批量保存
            System.out.println("上传--------------结束");
            return R.ok().put("message", "上传成功，已保存 " + vendorTableEntities.size() + " 条数据");
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
                // 如果是数字类型，强制转换为字符串
                return String.valueOf(cell.getNumericCellValue()).replaceAll("\\.0$", "");  // 去掉 .0
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