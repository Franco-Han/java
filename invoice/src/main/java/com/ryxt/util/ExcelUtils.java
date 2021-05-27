package com.ryxt.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ryxt.base.annotation.RyExcel;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.exception.BusinessException;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.service.FileService;
 
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Component
public class ExcelUtils {

//    private static String UPLOAD_PATH;
//    private static String GATE_PATH;
//
//    @Value("${upload.service.url}")
//    public void setUploadPath(String uploadPath) {
//        UPLOAD_PATH = uploadPath;
//    }
//
//    @Value("${upload.service.gate_path}")
//    public void setGatePath(String gatePath) {
//        GATE_PATH = gatePath;
//    }


    @SuppressWarnings("resource")
    public static List Upload(MultipartFile file, Class<?> entityClass) throws Exception {
        String fileName = file.getOriginalFilename();
//        CommonsMultipartFile cf = (CommonsMultipartFile) file;
//        DiskFileItem fi = (DiskFileItem) cf.getFileItem();

        //临时文件
//        File tempFile = fi.getStoreLocation();
//        String filePath = tempFile.getPath();
//        String fileName = fi.getName();
        //文件拓展名
        InputStream is = file.getInputStream();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        Workbook wb;
        if (ext.equalsIgnoreCase("xls")) {// 2003及以前的版本后缀为.xls,
            wb = new HSSFWorkbook(is);
        } else if (ext.equalsIgnoreCase("xlsx")) {//2007以后的版本后缀为.xlsx.
            wb = new XSSFWorkbook(is);
        } else {
            return null;
        }
        List<Object> l = new ArrayList<Object>();
        //获取excel第一个工作的数据
        Sheet sheet = wb.getSheetAt(0);
        //获取excel第二个工作的数据
        Sheet sheet2 = wb.getSheetAt(1);
        if (sheet != null && sheet2 != null) {
            Row hiddenRow = sheet.getRow(2);// 获取隐藏页的第一行
            Row hiddenRow2 = sheet.getRow(2);// 获取隐藏页的第二行
            List<String> list = new ArrayList<String>();
            if(hiddenRow != null){
                for (int j = 0; j < hiddenRow.getLastCellNum(); j++) {
                    //某一行的某一列的数据
                    Cell cell = hiddenRow.getCell(j);
                    Cell cell2 = hiddenRow2.getCell(j);
                    String cellName = cell.getStringCellValue();
                    String typeName = cell2.getStringCellValue();
                    list.add(cellName);
                }
            }
            int count = sheet.getLastRowNum();
            for (int i = 3; i < count + 2; i++) {//第二行开始计算TODO
                Row row = sheet.getRow(i);
                if(row == null){
                    continue;
                }
                Object o = entityClass.newInstance();
                for (int k = 0; k < row.getLastCellNum(); k++) {
                    //某一行的某一列的数据
                    Cell cell = row.getCell(k);
                    String name="";
                    switch (list.get(k)){
                        case  "发票代码":
                            name="invoiceCode";
                        break;
                        case "发票号码":
                            name="invoiceNumber";
                        break;
                        case "发票类型":
                            name="invoiceType";
                        break;
                        case "录入方式":
                            name="checkType";
                        break;
                        case "开票日期":
                            name="invoiceDate";
                        break;
                        case "开具金额（不含税）":
                            name="excludingTaxPrice";
                        break;
                        case "校验码后六位":
                            name="checkCode";
                        break;
                        default:
                            throw  new BusinessException("");

                    }
                    Field f = entityClass.getDeclaredField(name);
                    f.setAccessible(true);
                    
                    if (f.getType().equals("class java.util.Date")) {
                        String cellValue = "";
                        try {
                            cellValue = cell.getStringCellValue();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            f.set(o, formatter.parse(cellValue));
                        } catch (Exception e) {
                            cellValue = "";
                        }
                    } else if (f.getType().equals("class java.util.Integer")) {
                        int numericCellValue = (int) cell.getNumericCellValue();
                        f.set(o, numericCellValue);
                    } else {
                        String cellValue = "";
                        try {
                            cellValue = cell.getStringCellValue();
                        } catch (Exception e) {
                            cellValue = "";
                        }
                        f.set(o, cellValue);
                    }
                }
                l.add(o);
            }

        } else {
            return null;
        }
        return l;
    }

    @SuppressWarnings("resource")
    public static void Download(HttpServletResponse response, Object o, Class<?> entityClass, String fileName,BaseInput record) throws Exception {
        List<Object> l = (List<Object>) o;
        // 获取实体类的所有属性，返回Field数组
        Field[] field = entityClass.getDeclaredFields();
        // 新建excel
        Workbook wb = new HSSFWorkbook();
        // 新建 sheet
        Sheet sheet = wb.createSheet();
        wb.setSheetName(0, "数据");
        Sheet sheet2 = wb.createSheet();
        wb.setSheetName(1, "导入用");
        wb.setSheetHidden(1, 2);//设置sheet2隐藏
//      wb.setSheetHidden(i, true);// 把所有的sheet工作表都给隐藏掉 
//      wb.setSheetHidden(i,1);//把所有的sheet工作表都给隐藏掉,0=显示 1=隐藏 2=非常隐秘(我在Excel中找不到。但是程序找得到~_~) 
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        //设置字体大小
        font.setFontHeightInPoints((short) 12);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        // 设置表头
        setHeader(field, sheet, sheet2, style,record,"");

        int rowNum = 1; //行号
        for (Object obj : l) {// 循环 List
            Row row = sheet.createRow(rowNum); // 创建行
            int colNum = 0; //列号
            for (int i = 0; i < field.length; i++) {
                // 获取属性的名字
                String name = field[i].getName();
                // 获取属性类型
                String type = field[i].getGenericType().toString();
                //关键。。。可访问私有变量
                field[i].setAccessible(true);
                // 将属性的首字母大写
                name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                Method m = null;
                try {
                    m = obj.getClass().getMethod("get" + name);
                } catch (Exception e) {
                    continue;
                }
                RyExcel re = field[i].getAnnotation(RyExcel.class);
                    if (re == null) {
                    continue;
                }
                setValue(type, m, obj, row, colNum,style,name,"");
                colNum++;
            }
            rowNum++;
        }


        try {
            // excel 表文件名  
            String fileName11 = URLEncoder.encode(fileName + ".xls", "UTF-8");
            String headStr = "attachment; filename=\"" + fileName11 + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("resource")
    public static JSONObject DownloadUrl(Object o, Class<?> entityClass, String title, BaseInput record) throws Exception {
        FileService fileService  = (FileService) SpringContextUtil.getBean("fileServiceImpl");

        List<Object> l = (List<Object>) o;
        // 获取实体类的所有属性，返回Field数组
        Field[] field = entityClass.getDeclaredFields();
        // 新建excel
        Workbook wb = new HSSFWorkbook();
        // 新建 sheet
        Sheet sheet = wb.createSheet();
        wb.setSheetName(0, "数据");
        Sheet sheet2 = wb.createSheet();
        wb.setSheetName(1, "导入用");
        //设置sheet2隐藏
        wb.setSheetHidden(1, 2);
//      wb.setSheetHidden(i, true);// 把所有的sheet工作表都给隐藏掉
//      wb.setSheetHidden(i,1);//把所有的sheet工作表都给隐藏掉,0=显示 1=隐藏 2=非常隐秘(我在Excel中找不到。但是程序找得到~_~)


        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        //设置字体大小
        font.setFontHeightInPoints((short) 12);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 把字体应用到当前的样式
        style.setFont(font);



        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        // 设置表头
       int headerLength =  setHeader(field, sheet, sheet2,style,record,title);


        // 设置表头
        setTitle(headerLength, sheet, title);
        //设置查询区间
        String val1 ="";
        String val2 ="";
        if(record.getOptions().size()>0){
            List<Map<String,Object>> conditionList = record.getOptions();
            for (Map<String,Object> map: conditionList) {
                String filter = map.get("filter").toString();
                String value = map.get("value").toString();
                if(filter.equals("BETWEEN")){
                     val1 = value.replace("-","-").replace(" ","").replace(":","");
                     val2 = map.get("value2").toString().replace("-","-").replace(" ","").replace(":","");
                }
            }
        }
        if(!(title.equals("查验模板"))){
            if(!((val1 == null || val1=="")&& (val2 == null || val2 == ""))){
                setTimeInterval(headerLength, sheet, "查询区间:"+val1+"-"+val2);
            }
        }
        //行号
        int rowNum = 3;
        // 循环 List
        for (Object obj : l) {
            // 创建行
            Row row = sheet.createRow(rowNum);

            //列号
            int colNum = 0;
            for (int i = 0; i < field.length; i++) {
                // 获取属性的名字
                String name = field[i].getName();
                // 获取属性类型
                String type = field[i].getGenericType().toString();
                //关键。。。可访问私有变量
                field[i].setAccessible(true);
                // 将属性的首字母大写
                
                name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                if(record.getOptions().size()>0){
                    List<Map<String,Object>> custonList = record.getOptions();
                    if(title.equals("查验清单")){
                        for (Map<String,Object> map: custonList) {
                            String custonfilter = map.get("filter").toString();
                            String custonvalue = map.get("value").toString();
                            if(custonfilter.equals("CUSTON")){
                                custonvalue = custonvalue.replaceFirst(custonvalue.substring(0, 1), custonvalue.substring(0, 1).toUpperCase());
                                Method m = null;
                                try {
                                    m = obj.getClass().getMethod("get" + name);
                                } catch (Exception e) {
                                    continue;
                                }
                                RyExcel re = field[i].getAnnotation(RyExcel.class);
                                if (re == null) {
                                    continue;
                                }
                                if(name.equals(custonvalue)){
                                    setValue(type, m, obj, row, colNum,style,name,title);
                                    colNum++;
                                }
                            }
                        }
                    }else{
                            Method m = null;
                            try {
                                m = obj.getClass().getMethod("get" + name);
                            } catch (Exception e) {
                                continue;
                            }
                            RyExcel re = field[i].getAnnotation(RyExcel.class);
                            if (re == null) {
                                continue;
                            }
                            setValue(type, m, obj, row, colNum,style,name,title);
                            colNum++;
                        }
                }else{
                    Method m = null;
                    try {
                        m = obj.getClass().getMethod("get" + name);
                    } catch (Exception e) {
                        continue;
                    }
                    RyExcel re = field[i].getAnnotation(RyExcel.class);
                    if (re == null) {
                        continue;
                    }
                    setValue(type, m, obj, row, colNum,style,name,title);
                    colNum++;
                }
            }
            rowNum++;
        }
        if(title.equals("查验模板")){
            setSelect(sheet);
            setTextStyle(wb,sheet);
        }
        if(title.equals("票面信息对比统计表")||
            title.equals("购买方/销售方信息不符统计表")||
            title.equals("开票日期不符统计表")||
            title.equals("重复查询统计表")||
            title.equals("复查结果变更统计表")||
            title.equals("同一销售方统计表")){
            setRules(rowNum,sheet,title);
        }
        try {
            String fileName ;
            if((val1 == null || val1=="")&& (val2 == null || val2 == "")){
                fileName = title+"-";
            }else{
                fileName= title+"("+val1+"至"+val2+")"+"-";
            }
            File file = File.createTempFile(fileName, ".xls");
            OutputStream f = new FileOutputStream(file);
            wb.write(f);
//            JSONObject params = new JSONObject();
//            params.put("file", file);
//            params.put("path", GATE_PATH);
//            params.put("output", "json");
//            String resp = HttpUtil.post(UPLOAD_PATH, params);
//            JSON.parseObject(resp)
            JSONObject jb = fileService.upload(file);
            jb.put("title",title);
            return jb;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static void setTextStyle(Workbook wb,Sheet sheet){
        DataFormat fmt = wb.createDataFormat();
        CellStyle textStyle = wb.createCellStyle();
        textStyle.setDataFormat(fmt.getFormat("@"));
        for(int i=0;i<10;i++){
            sheet.setDefaultColumnStyle(i, textStyle);
        }

    }
    private static void setSelect(Sheet sheet){
        String[] list={"增值税电子普通发票","增值税普通发票","增值税专用发票"};
        //获得总列数
        //int coloumNum=sheet.getRow(2).getPhysicalNumberOfCells();
        //获得总行数
        //int rowNum=sheet.getPhysicalNumberOfRows();
        //起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(2,10,2,2);
        //生成下拉框内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(list);
        //绑定下拉框和作用区域
        HSSFDataValidation data_validation = new HSSFDataValidation(regions,constraint);
        //对sheet页生效
        sheet.addValidationData(data_validation);
    }
    private static void setTimeInterval(int length, Sheet sheet, String title){
        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setColor(HSSFColor.RED.index);
        //设置字体大小
        font.setFontHeightInPoints((short) 12);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.LEFT);
        //设置垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 把字体应用到当前的样式
        style.setFont(font);
        // 创建行
        Row row = sheet.createRow(1);
        Cell cell =  row.createCell(0);

        cell.setCellStyle(style);
        cell.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, length-1));
    }
    public static  void setRules(int rowNum,Sheet sheet,String title){
        // 创建行
        Row row = sheet.createRow(rowNum);
        Cell cell =  row.createCell(0);
        String value="";
        switch (title){
            case "票面信息对比统计表":
                value="报表规则说明：\n"+
                "1.参加统计的数据为“查验结果”为“真票”，录入方式为“扫描仪”、“手机拍照”、“PC端图像导入”、“手机APP端图像导入”的。\n"+
                "2.数据项名称包括：购买方名称、购买方纳税人识别号、购买方地址电话、购买方开户行及账号、货物或应税劳务服务名称等、价款合计金额、税额合计金额、价税合计金额（大写）、价税合计金额（小写）、销售方名称、销售方纳税人识别号、销售方地址电话、销售方开户行及账号、备注。用户可在“设置”中的“票面信息对项设置”中进行勾选。";
                break;
            case "购买方/销售方信息不符统计表":
                value="报表规则说明：\n" +
                        "1.用户可在“设置”中的“购买方/销售方信息维护”中维护相关企业信息，参加统计的数据为购买方/销售方信息不在用户维护的范围之内的。若用户未维护，则此表不进行统计。\n" +
                        "2.数据类型包括：购买方、销售方。\n" +
                        "3.数据内容均为官网查验获得的信息，而非票面OCR信息。";
                break;
            case "开票日期不符统计表":
                value="报表规则说明：\n" +
                        "1.用户可在“设置”中的“开票区间设置”中设置开票日期区间，参加统计的数据为开票日期不在用户维护的范围之内的。若用户未维护，则此表不进行统计。\n" +
                        "2.开票日期为官网查验获得的信息，而非票面OCR信息。";
                break;
            case "重复查询统计表":
                value="报表规则说明：\n" +
                        "1.参加统计的数据为同一张票在不同用户不同日期内重复查验的，一天内多次查验、复查、只查验过一次的，不在统计范围内。\n" +
                        "2.查验用户只显示“本用户”和“非本用户”。\n" +
                        "3.此表为根据本用户查验过的发票进行统计，即重复查验的票中，至少有一张是本用户查验的。非本用户查验的重复票，不在本用户的统计表中体现。";
                break;
            case "复查结果变更统计表":
                value="报表规则说明：\n" +
                        "1.参加统计的数据为原“查验结果”为“真票”，复查结果为非“真票”的。\n" +
                        "2.如果为多次复查，则查验日期为上次结果为“真票”的查验。一次复查结果为非“真票”的，二次复查结果也为非“真票”的，则为两条数据。";
                break;
            case "同一销售方统计表":
                value="报表规则说明：\n" +
                        "1.参加统计的数据为同一购买方在同一销售方下发生的发票为两张或两张以上，且“查验结果”为“真票”的。\n" +
                        "2.数据信息均为官网查验获得的信息，而非票面OCR信息。";
                break;
                default:
                    value="";
        }
        cell.setCellValue(value);
    }
    private static void setTitle(int length, Sheet sheet, String title){
        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("微软雅黑");
        //设置字体大小
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 创建行
        Row row = sheet.createRow(0);
        Cell cell =  row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, length-1));
    }

    /**
     * 
     * 
     * 设置表头
     * @param field
     * @param sheet
     * @param sheet2
     * @param style
     */
    
    private static int  setHeader(Field[] field, Sheet sheet, Sheet sheet2, CellStyle style,BaseInput record,String title){
        //列号
    	int colNum = 0;
        // 创建行
        Row row = sheet.createRow(2);
        // 创建行
        Row row2 = sheet2.createRow(2);
        // 创建行
        Row row3 = sheet2.createRow(3);


        for (int i = 0; i < field.length; i++) {
            RyExcel re = field[i].getAnnotation(RyExcel.class);
            if(re == null){
            	continue;
            }
            // 获取属性的名字
            String name = field[i].getName();
            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());

            if(record.getOptions().size()>0){
                List<Map<String,Object>> custonList = record.getOptions();
                if(title.equals("查验清单")){
                    for (Map<String,Object> map: custonList) {
                        String custonfilter = map.get("filter").toString();
                        String custonvalue = map.get("value").toString();
                        if(custonfilter.equals("CUSTON")){
                            custonvalue = custonvalue.replaceFirst(custonvalue.substring(0, 1), custonvalue.substring(0, 1).toUpperCase());
                            if(name.equals(custonvalue)){
                                String colName = re.ColName();
                                int colSize= re.ColSize();
                                sheet.setColumnWidth(colNum, colSize*25);
                                Cell cell =  row.createCell(colNum);
                                cell.setCellStyle(style);
                                cell.setCellValue(colName);
                                // 实体属性
                                row2.createCell(colNum).setCellValue(field[i].getName());
                                // 类型
                                row3.createCell(colNum).setCellValue(field[i].getGenericType().toString());
                                colNum++;
                            }
                        }
                    }
                }else{
                        String colName = re.ColName();
                        int colSize= re.ColSize();
                        sheet.setColumnWidth(colNum, colSize*25);
                        Cell cell =  row.createCell(colNum);
                        cell.setCellStyle(style);
                        cell.setCellValue(colName);
                        // 实体属性
                        row2.createCell(colNum).setCellValue(field[i].getName());
                        // 类型
                        row3.createCell(colNum).setCellValue(field[i].getGenericType().toString());
                        colNum++;
                    }

            }else{
                String colName = re.ColName();
                int colSize= re.ColSize();
                sheet.setColumnWidth(colNum, colSize*25);
                Cell cell =  row.createCell(colNum);
                cell.setCellStyle(style);
                cell.setCellValue(colName);
                // 实体属性
                row2.createCell(colNum).setCellValue(field[i].getName());
                // 类型
                row3.createCell(colNum).setCellValue(field[i].getGenericType().toString());
                colNum++;
            }
        }

        return colNum;

    }
    
    
    private static void setValue(String type, Method m, Object obj, Row row, int colNum,CellStyle style,String name,String title) throws Exception{

        /*      {
                    value:'10',
                    label:'增值税电子普通发票',
                },
                {
                    value:'04',
                    label:'增值税普通发票',
                },
                {
                    value:'01',
                    label:'增值税专用发票',
                }*/
        Cell cell =  row.createCell(colNum);
        cell.setCellStyle(style);
    	 if (type.equals("class java.lang.String")) {
             // 如果type是类类型，则前面包含"class "，后面跟类名  
             // 调用getter方法获取属性值
    		 String value = (String) m.invoke(obj);
             if(name.equals("InvoiceType") && value != "" && value != null){
                 switch (value){
                     case "01":
                         value ="增值税专用发票";
                         break;
                     case "04":
                         value="增值税普通发票";
                         break;
                     case "10":
                         value="增值税电子普通发票";
                         break;
                     case "":
                         value="";
                         break;
                     default:
                         throw  new BusinessException("");
                 }

             }
             if(title.equals("查验模板")){
                 value="";
             }
             if (value != null) {
            	 System.out.println("attribute value:" + value);
                 cell.setCellValue(value);
             }
         }
         if (type.equals("class java.lang.Integer")) {
        	 Integer value = (Integer) m.invoke(obj);
             if (value != null) {
                 System.out.println("attribute value:" + value);
                 cell.setCellValue(value);
             }
         }
         if (type.equals("class java.lang.Short")) {
        	 Short value = (Short) m.invoke(obj);
             if (value != null) {
                 System.out.println("attribute value:" + value);
                 cell.setCellValue(value);
             }
         }
         if (type.equals("class java.lang.Double")) {
        	 Double  value = (Double) m.invoke(obj);
             if (value != null) {
                 System.out.println("attribute value:" + value);
                 cell.setCellValue(value);
             }
         }
         if (type.equals("class java.lang.Boolean")) {
             Boolean value = (Boolean) m.invoke(obj);

             if (value != null) {
                 System.out.println("attribute value:" + value);
                 if(name.equals("ReCheck")){
                     if(value){
                         cell.setCellValue("是");
                     }else{
                         cell.setCellValue("否");
                     }
                 }else{
                     cell.setCellValue(value);
                 }

             }
         }
         if (type.equals("class java.util.Date")) {
        	 Date value = (Date) m.invoke(obj);
             if (value != null) { 
            	 DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 System.out.println("attribute value:" + value.toString());
                 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                 cell.setCellValue( formater.format(value));
             }
         }

    }
    
    
    public static String validate(Object o,Class<?> entityClass){
    	 Field[] field = entityClass.getDeclaredFields();

     	String msg = "";
    	 for(int i=0;i<field.length;i++){
    		 // 获取属性的名字  
             String name = field[i].getName();
             // 获取属性类型  
             String type = field[i].getGenericType().toString();
             //关键。。。可访问私有变量  
             field[i].setAccessible(true);
             // 将属性的首字母大写  
             name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
             Method m= null;
             Object value ;
             try {
            	 m = o.getClass().getMethod("get" + name);
            	 value = m.invoke(o);
			} catch (Exception e) {
				continue;
			}
             RyExcel re = field[i].getAnnotation(RyExcel.class);
             if(re == null){
             	continue;
             }
             String colName = re.ColName();
             boolean required = re.required();
             String pattern = re.pattern();
             if(required && (value=="" || value==null)){
            	 msg = "未填写:"+ colName;
            	 break;
             }
    		 if(!"".equals(pattern)){
   				 if(!value.toString().matches(pattern)){
	    			 msg = colName+ "格式不正确";
//	    			 msg = colName+ "格式不正确,正确格式为:"+ pattern + ",实际为:" + value.toString();
	            	 break;
	    		 }
    		 }
    	 }
    	
		return msg;
    }
}
