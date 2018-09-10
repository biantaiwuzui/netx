package com.netx.boss.web.controller.shoppingmall.bussiness;

import com.netx.boss.utils.POIUtils;
import com.netx.boss.web.controller.BaseController;
import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created By lcx
 */
@Api(description = "网商商家相关接口")
@RestController
@RequestMapping("/business/seller")
public class SellerController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(SellerController.class);

    @Autowired
    MerchantFuseAction merchantFuseAction;

    @Autowired
    MerchantAction merchantAction;


    @ApiOperation(value = "模糊分页查询商家黑/白名单")
    @PostMapping("/getSellerList")
    public JsonResult getSellerList(@Valid @RequestBody GetSellerWhiteRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map<String, Object> whiteList = merchantFuseAction.getMerchantList(request);
            return JsonResult.successJsonResult(whiteList);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家白名单列表失败！");
        }
    }

    @ApiOperation(value = "打印商家白名单列表")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("出货表");
            Row row = null;
            Cell cell = null;

            // 设置excel的宽度
            sheet.setColumnWidth(0, 6*256);
            sheet.setColumnWidth(1, 26*256);
            sheet.setColumnWidth(2, 11*256);
            sheet.setColumnWidth(3, 11*256);
            sheet.setColumnWidth(4, 11*256);
            sheet.setColumnWidth(5, 15*256);
            sheet.setColumnWidth(6, 10*256);
            sheet.setColumnWidth(7, 10*256);
            sheet.setColumnWidth(8, 20*256);

            //.第一行
            //a. 合并单元格
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));
            //b. 创建行: 第一行
            row = sheet.createRow(0);
            //c. 创建列: 第二列
            cell = row.createCell(1);
            //-----> 设置大标题样式
            cell.setCellStyle(POIUtils.bigTitle(workbook));
            //d. 设置内容
            cell.setCellValue("商家白名单");
            // 设置行高
            row.setHeight((short) (36*20));

            //第二行
            String[] title = {"商家名称","访问量","省","市","区","乡镇","联系人","电话"};
            row = sheet.createRow(1);
            //b. 设置行高
            row.setHeight((short) (26*20));
            //c. 创建单元格
            for (int i=0; i<title.length; i++) {
                cell = row.createCell(i+1);
                // 设置样式
                cell.setCellStyle(POIUtils.title(workbook));
                // 设置内容
                cell.setCellValue(title[i]);
            }

            List<Merchant> list = merchantAction.export(1);
            if (list != null && list.size() > 0) {
                int rowNum = 2;
                for (Merchant merchant : list) {
                    // 创建行，从第3行开始创建
                    row = sheet.createRow(rowNum++);
                    row.setHeight((short) (24*20));

                    // 创建单元格
                    cell = row.createCell(1);
                    cell.setCellStyle(POIUtils.text(workbook));
                    cell.setCellValue(merchant.getName());

                    cell = row.createCell(2);
                    cell.setCellStyle(POIUtils.text(workbook));
                    cell.setCellValue("11");

                    cell = row.createCell(3);
                    cell.setCellStyle(POIUtils.text(workbook));
                    cell.setCellValue(merchant.getProvinceCode());

                    cell = row.createCell(4);
                    cell.setCellStyle(POIUtils.text(workbook));
                    cell.setCellValue(merchant.getCityCode());

                    cell = row.createCell(5);
                    cell.setCellStyle(POIUtils.text(workbook));
                    cell.setCellValue(merchant.getAreaCode());

                    cell = row.createCell(6);
                    cell.setCellStyle(POIUtils.text(workbook));
                    cell.setCellValue(merchant.getAddrCountry());

                    cell = row.createCell(7);
                    cell.setCellStyle(POIUtils.text(workbook));
                    cell.setCellValue(merchant.getAddrContact());

                    cell = row.createCell(8);
                    cell.setCellStyle(POIUtils.text(workbook));
                    cell.setCellValue(merchant.getAddrTel());
                }
            }

            //导出excel，下载
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            response.setHeader("content-type","text/html;charset=UTF-8");
            response.addHeader("Content-Disposition",   "attachment;filename="
                    + URLEncoder.encode("商品白名单" + ".xls", "UTF-8"));
            response.setContentLength(bos.size());

            OutputStream outputstream = response.getOutputStream();
            bos.writeTo(outputstream);
            bos.close();
            outputstream.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "拉黑商家")
    @PostMapping("/defriend")
    public JsonResult defriend(@Valid @RequestBody SellerStatusRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean defriend = merchantAction.updateStatus(request);
            return getResult(defriend,"拉黑成功","拉黑失败");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("拉黑商家异常");
        }
    }

    @ApiOperation(value = "解除商家黑名单")
    @PostMapping("/overBack")
    public JsonResult overBack(@Valid @RequestBody SellerStatusRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean overBack = merchantAction.updateStatus(request);
            return getResult(overBack,"解除成功","解除失败");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("解除黑名单异常");
        }
    }
}