package com.ryxt.service.impl;

import cn.hutool.core.date.DateException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.*;
import com.ryxt.exception.BusinessException;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.mapper.CheckListOcrMapper;
import com.ryxt.mapper.CheckResultMapper;
import com.ryxt.mapper.CompanyMapper;
import com.ryxt.service.*;
import com.ryxt.task.StatisticsThread;
import com.ryxt.util.*;
import com.sun.tools.javac.comp.Check;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CheckServiceImpl implements CheckService {


    @Autowired
    private ReCheckTaskService reCheckTaskService;
	@Autowired
	private CheckListMapper checkListMapper;
	@Autowired
    private CheckListOcrMapper checkListOcrMapper;

	@Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CheckResultMapper checkResultMapper;

    @Autowired
    private InformationDiscrepancyService informationDiscrepancyService;
    @Autowired
    private InvoiceDateService invoiceDateService;

    @Autowired
    private StatisticsSettingService statisticsSettingService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DuplicateQueryService duplicateQueryService;

    /*开票日期不符*/
    @Autowired
    private InvoiceDateDiscrepancyService invoiceDateDiscrepancyService;
    /*同一销售方统计*/
    @Autowired
    private CommonSellerService commonSeller;

    @Autowired
    private ReviewResultChangeService reviewResultChangeService;


    @Autowired
    private InformationComparisonService informationComparisonService;

    @Autowired
    private OcrService ocrService;

    @Autowired
    private ParamsService paramsService;

    @Override
    @Transactional
    public CommonListResponse<CheckListOcrVo> getListVo(BaseInput record) {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<CheckListOcrVo> commonListResponse = new CommonListResponse<CheckListOcrVo>(record);
//        EntityWrapper<CheckList> wrapper =  (EntityWrapper<CheckList>) BeanTrans.beanToNormal(record,CheckList.class);
//        wrapper.orderBy("create_date",false);
//        wrapper.eq("user_id",  userId );
//        wrapper.eq("delete_flag","0");
        Integer page = record.getPage();
        Integer pageSize = record.getPageSize();
        Map map = new HashMap();
        map.put("page",(page-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("userId",userId);
        String field="";
        String value="";
        String value2="";
        List<Map<String,Object>> conditionList = record.getOptions();
        if(record.getOptions().size()>0){
            for(Map<String,Object> resultMap: conditionList){
                field = resultMap.get("field").toString();
                value = resultMap.get("value").toString();
                if(field.equals("CUSTON")){
                    continue;
                }
                if(field != null && field != "" && value != null && value !=""){
                    if(resultMap.get("filter").toString().equals("BETWEEN")){
                        if(field.equals("invoiceDate")){
                            map.put("istartTime",value.replace("-","").replace(" ","").replace(":","")+"000000");
                            map.put("iendTime",resultMap.get("value2").toString().replace("-","").replace(" ","").replace(":","")+"235959");
                        }else if(field.equals("createDate")){
                            map.put("cstartTime",value.replace("-","").replace(" ","").replace(":","")+"000000");
                            map.put("cendTime",resultMap.get("value2").toString().replace("-","").replace(" ","").replace(":","")+"235959");
                        }
                    }else{
                        map.put(field,value);
                    }
                }
            }
        }

        List<CheckListOcrVo> list =  checkListOcrMapper.getCheckListOcrInfo(map);
        commonListResponse.setList(list);
        commonListResponse.setTotalCount(checkListOcrMapper.getCheckListOcrCount(map));
        return commonListResponse;
    }
    @Override
    public AjaxResponse<JSONObject> exportVo(HttpServletResponse response, BaseInput record) throws Exception {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<CheckListOcrVo> commonListResponse = new CommonListResponse<CheckListOcrVo>(record);
        Map map = new HashMap();
        map.put("userId",userId);
        String field="";
        String value="";
        List<Map<String,Object>> conditionList = record.getOptions();
        if(record.getOptions().size()>0){
            for(Map<String,Object> resultMap: conditionList){
                field = resultMap.get("field").toString();
                value = resultMap.get("value").toString();
                if(field.equals("CUSTON")){
                    continue;
                }
                if(field != null && field != "" && value != null && value !=""){
                    if(resultMap.get("filter").toString().equals("BETWEEN")){
                        if(field.equals("invoiceDate")){
                            map.put("istartTime", value.replace("-","-").replace(" ","").replace(":",""));
                            map.put("iendTime",resultMap.get("value2").toString().replace("-","-").replace(" ","").replace(":",""));
                        }else if(field.equals("createDate")){
                            map.put("cstartTime",value.replace("-","-").replace(" ","").replace(":",""));
                            map.put("cendTime",resultMap.get("value2").toString().replace("-","-").replace(" ","").replace(":",""));
                        }
                    }else{
                        map.put(field,value);
                    }
                }
            }
        }
        List<CheckListOcrVo> list =  checkListOcrMapper.getCheckListOcrInfo(map);
        return  AjaxResponse.success(ExcelUtils.DownloadUrl(list,CheckListOcrVo.class,"查验清单",record));
    }

    @Override
    public CommonListResponse<CheckList> getListPage(BaseInput record) {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<CheckList> commonListResponse = new CommonListResponse<CheckList>(record);
        Page<CheckList> page = new Page<CheckList>(record.getPage(),record.getPageSize());
        EntityWrapper<CheckList> wrapper =  (EntityWrapper<CheckList>) BeanTrans.beanToNormal(record,CheckList.class);
        wrapper.where("to_days(create_date) = to_days(now())");
        wrapper.orderBy("create_date",false);
        wrapper.eq("delete_flag","0");
        wrapper.eq("user_id",  userId );
        commonListResponse.setTotalCount(checkListMapper.selectCount(wrapper));
        List<CheckList> list = checkListMapper.selectPage(page,wrapper);
//        for(CheckList checkList:list){
//            EntityWrapper<CheckResult> resultWrapper = new EntityWrapper<CheckResult>();
//            resultWrapper.eq("parent_id",checkList.getId());
//            resultWrapper.orderBy("create_date",true);
//            resultWrapper.eq("delete_flag","0");
//            checkList.setCheckResults(checkResultMapper.selectList(resultWrapper));
//        }
        commonListResponse.setList(list);
        return commonListResponse;
    }

    @Override
    public  Map<String,Integer> findHandleCount() {
        String userId = BaseAuthUtil.getCurrentUserId();
        EntityWrapper<CheckList> Unwrapper = new EntityWrapper<CheckList>();
        Unwrapper.in("status","0,1");
        Unwrapper.eq("delete_flag","0");
        Unwrapper.eq("user_id",  userId  );
        Unwrapper.where("to_days(create_date) = to_days(now())");
        Integer Unprocessed = checkListMapper.selectCount(Unwrapper);
        EntityWrapper<CheckList> wrapper = new EntityWrapper<CheckList>();
        wrapper.in("status","2");
        wrapper.eq("delete_flag","0");
        wrapper.eq("user_id",  userId );
        wrapper.where("to_days(create_date) = to_days(now())");
        Integer processed = checkListMapper.selectCount(wrapper);
        Map<String,Integer> map = new HashMap();
        map.put("Unprocessed",Unprocessed);
        map.put("processed",processed);
        return map;
    }

    @Override
    public CommonListResponse<CheckList> getListPageHistory(BaseInput record) {
        CommonListResponse<CheckList> commonListResponse = new CommonListResponse<CheckList>(record);
        Page<CheckList> page = new Page<CheckList>(record.getPage(),record.getPageSize());
        EntityWrapper<CheckList> wrapper =  (EntityWrapper<CheckList>) BeanTrans.beanToNormal(record,CheckList.class);
//        wrapper.where("to_days(now()) - to_days(create_date) >=1");
        wrapper.orderBy("create_date",false);
        String userId = BaseAuthUtil.getCurrentUserId();
        wrapper.eq("user_id",  userId );
        wrapper.eq("delete_flag","0");
        commonListResponse.setTotalCount(checkListMapper.selectCount(wrapper));
        List<CheckList> list = checkListMapper.selectPage(page,wrapper);

//        for(CheckList checkList:list){
//            EntityWrapper<CheckResult> resultWrapper = new EntityWrapper<CheckResult>();
//            resultWrapper.eq("parent_id",checkList.getId());
//            // 按时间倒序
//            resultWrapper.orderBy("create_date",true);
//            resultWrapper.eq("delete_flag","0");
//            List<CheckResult> results = checkResultMapper.selectList(resultWrapper);
//            checkList.setCheckResults(results);
//        }
        commonListResponse.setList(list);
        return commonListResponse;
    }
    @Override
    public AjaxResponse batchInsert(List<CheckList> checkLists) {
        for(CheckList checkList :checkLists){
            CheckList list = new CheckList();
            list.setInvoiceCode(checkList.getInvoiceCode());
            list.setInvoiceNumber(checkList.getInvoiceNumber());
            list.setInvoiceType(checkList.getInvoiceType());
            list.setInvoiceDate(checkList.getInvoiceDate());
            list.setExcludingTaxPrice(checkList.getExcludingTaxPrice());
            list.setCheckCode(checkList.getCheckCode());
            list.setCheckType("手工批量查验");
            saveOrUpdate(list);
        }
        return AjaxResponse.success(checkLists);
    }

    @Override
//    @Transactional
    public CheckList saveOrUpdate(CheckList record) {

        String userId = BaseAuthUtil.getCurrentUserId();

        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
//        CheckList user1 = checkListMapper.selectById(user.getId());
//
//        if(user1!=null){
//            user.setUpdateDate(new Date());
//            checkListMapper.updateById(user);
//        }else {
        // 1 查询今天是不是已经查验过
       CheckList todayCheck = check(record);

        if(todayCheck!=null){
            throw  new BusinessException("今天已经扫描过此发票["+record.getInvoiceNumber()+"]了");
//            // 是不是正在处理中
//            if(todayCheck.getStatus()!="2"){
//                throw  new BusinessException("这张发票正在处理中，请勿重复查验");
//            }
//            // 今天已经查验过了
//            // 2 判断今天查验了几次
//            Integer checkTimes =  getCheckTimes(todayCheck);
//            if(checkTimes<4){
//                // 增加一条查验
//                startCheck(todayCheck,record,checkTimes);
//            }else{
//                // 返回已经查验4次了
//                throw  new BusinessException("这张怕票您今天已经查验4次了，请手动查验，或者明天再试试吧！");
//            }
        }else{
            // 今天没有查验过，增加一条查验
            record.setUserId(userId);
            record.setCreateDate(new Date());
            record.setStatus("0");
            record.setApplyDate(new Date());
            record.setDeleteFlag(Const.NORMAL_STATUS);
           int count =  checkListMapper.insert(record);
           if(count<=0){
               throw  new BusinessException("今天已经扫描过此发票["+record.getInvoiceNumber()+"]了");
           }
//            // 增加一条查验
//            startCheck(record,record,0);
        }
//        }
        return record;
    }
    @Override
    public CheckList update(CheckList record) {
        if(StringUtil.isNullEmpty(record.getId())){
           throw new BusinessException("失败");
        }
        CheckList record2 =  checkListMapper.selectById(record.getId());
        if(record2!=null){
            record2.setInvoiceCode(record.getInvoiceCode());
            record2.setInvoiceNumber(record.getInvoiceNumber());
            record2.setInvoiceDate(record.getInvoiceDate());
            record2.setExcludingTaxPrice(record.getExcludingTaxPrice());
            record2.setCheckCode(record.getCheckCode());
            record2.setStatus("0");
            record2.setResult("");
            record2.setReason("");
            // 申请查验日期
            record2.setApplyDate(new Date());
            checkListMapper.updateById(record2);
        }
        return record2;
    }


    @Override
    public int deleteDeptById(String id) {
        CheckList sysCheckList =checkListMapper.selectById(id);
        if(sysCheckList!=null){
            sysCheckList.setDeleteFlag(Const.DELETE_STATUS);
            return checkListMapper.updateById(sysCheckList);
        }else{
            throw new BusinessException("删除失败,目标不存在");
        }
    }
    @Override
    @Transactional
    public int batchDelete(List<CheckList> checkLists) {
        int count = 0;
        for(CheckList checkList:checkLists){
            count += deleteDeptById(checkList.getId());
        }
        return count;
    }
    @Override
    public CheckList selectById(String id) {
        return checkListMapper.selectById(id);
    }

    @Override
    public CheckList getLastTask(List<CheckList> list) {
        return checkListMapper.getFirstRecord(list);
    }

    /**
    * 获取今天查验的信息
    * @param record
    * @return com.ryxt.entity.CheckList
    * @throws Exception
    */

    public CheckList check(CheckList record) {
        EntityWrapper<CheckList> wrapper = new EntityWrapper<CheckList>();
        String userId = BaseAuthUtil.getCurrentUserId();
        wrapper.where("to_days(create_date) = to_days(now())");
        // 发票代码
        wrapper.eq("invoice_code",record.getInvoiceCode());
        // 发票号码
        wrapper.eq("invoice_number",record.getInvoiceNumber());
        // 用户id
        wrapper.eq("user_id",userId);
        // TODO 其他 待考虑

        List<CheckList>  todayCheck =  checkListMapper.selectList(wrapper);

        if(todayCheck!=null && todayCheck.size()>0){
            // 今天已经查验过了
            // 2 判断今天查验了几次

            return  todayCheck.get(0);
        }

        return null;
    }

    /**
    * 获取查验的次数
    * @param record
    * @return
    * @throws Exception
    */
    public Integer getCheckTimes(CheckList record){
        EntityWrapper<CheckResult> wrapper = new EntityWrapper<CheckResult>();
        // 查验的id
        wrapper.eq("parent_id",record.getId());
        return  checkResultMapper.selectCount(wrapper);
    }

    /**
     * 再查验一次
     * @param todayCheck
     * @param record
     */
    public void startCheck(CheckList todayCheck, CheckList record, Integer checkTimes){
        String userId = BaseAuthUtil.getCurrentUserId();
        CheckResult checkResult = new  CheckResult();
        checkResult.setId(StringUtil.generateUUID());
        checkResult.setParentId(todayCheck.getId());
        // 查验状态
        checkResult.setStatus(Const.NORMAL_STATUS);
        // 当前日期
        checkResult.setCreateDate(new Date());
        // 第几次查验
        checkResult.setCount(checkTimes+1);
        // 查验人
        checkResult.setUserId(userId);

        checkResult.setInvoiceCode(record.getCheckCode());
        checkResult.setInvoiceNumber(record.getInvoiceNumber());
        // TODO 其他字段
        // 插入一条 查验结果 ==》查验中
        checkResultMapper.insert(checkResult);
        // 更新 查验状态为查验中
        todayCheck.setStatus("0");
        checkListMapper.updateById(todayCheck);
    }

    @Override
    public AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput record) throws Exception {
        String userId = BaseAuthUtil.getCurrentUserId();
        EntityWrapper<CheckList> wrapper =  (EntityWrapper<CheckList>) BeanTrans.beanToNormal(record,CheckList.class);
        wrapper.where("to_days(create_date) = to_days(now())");
        wrapper.orderBy("create_date",false);
        wrapper.eq("delete_flag","0");
        wrapper.eq("user_id",  userId );
        List<CheckList> list = checkListMapper.selectList(wrapper);
        return AjaxResponse.success( ExcelUtils.DownloadUrl(list,CheckList.class,"查验清单",record));
    }


    @Override
    public AjaxResponse<JSONObject> exportTemplate(HttpServletResponse response, BaseInput user) throws Exception {
        List<ExcelTemplate>  list = new ArrayList<>();
        return AjaxResponse.success( ExcelUtils.DownloadUrl(list,ExcelTemplate.class,"查验模板",user));
    }

    @Override
    public AjaxResponse<List<CheckList>> uploadTemplate(MultipartFile file) throws Exception {
       List list =ExcelUtils.Upload(file,ExcelTemplate.class);
       CheckList checkList = new CheckList();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
       ExcelTemplate excelTemplate = new ExcelTemplate();
       List<CheckList> list1 = new ArrayList<>();
       for(   Object o: list){
           checkList = new CheckList();
           if(o instanceof ExcelTemplate){
               excelTemplate = (ExcelTemplate) o;
           }
           checkList.setInvoiceCode(excelTemplate.getInvoiceCode());
           checkList.setInvoiceNumber(excelTemplate.getInvoiceNumber());
           checkList.setInvoiceType(excelTemplate.getInvoiceType());
           switch (excelTemplate.getInvoiceType()){
               case "增值税专用发票":
                   checkList.setInvoiceType("01");
                   break;
               case "增值税普通发票":
                   checkList.setInvoiceType("04");
                   break;
               case "增值税电子普通发票":
                   checkList.setInvoiceType("10");
                   break;
               default:
                   throw  new BusinessException("发票类型填写错误");
           }
           checkList.setCheckType("手工批量查验");
           try {
               checkList.setInvoiceDate(formatter.parse(excelTemplate.getInvoiceDate()));
           }catch( Exception e){

           }
           checkList.setExcludingTaxPrice(excelTemplate.getExcludingTaxPrice());
           checkList.setCheckCode(excelTemplate.getCheckCode());
//         String userId = BaseAuthUtil.getCurrentUserId();
//         if(StringUtil.isNullEmpty(checkList.getId())){
//             checkList.setId(StringUtil.generateUUID());
//         }
//         CheckList todayCheck = check(checkList);
//         if(todayCheck!=null){
//         }else{
//             checkList.setUserId(userId);
//             checkList.setCreateDate(new Date());
//             checkList.setDeleteFlag(Const.NORMAL_STATUS);
//             checkListMapper.insert(checkList);
//         }
           list1.add(checkList);
         }
        return AjaxResponse.success(list1);
    }

    @Override
    public AjaxResponse<JSONObject> scanningExport(List<CheckList> checkLists , String code) throws Exception {
        Set<String> urlArr = new HashSet<String>();
        String userId = BaseAuthUtil.getCurrentUserId();
        EntityWrapper<CheckResult> resultWrapper  ;
        EntityWrapper<CheckList> listWrapper  ;
        List<CheckResult> resultList=new ArrayList<CheckResult>();
        List<CheckList> list=new ArrayList<CheckList>();
        for(CheckList checkList:checkLists){
            if(code.equals("0")){
                listWrapper = new EntityWrapper<CheckList>();
                listWrapper.where("id="+"'"+checkList.getId()+"'");
                listWrapper.eq("user_id",userId);
                listWrapper.eq("delete_flag","0");
                list= checkListMapper.selectList(listWrapper);
            }else{
                resultWrapper = new EntityWrapper<CheckResult>();
                resultWrapper.where("parent_id="+"'"+checkList.getId()+"'");
                resultWrapper.eq("user_id",userId);
                resultWrapper.eq("delete_flag","0");
                resultList= checkResultMapper.selectList(resultWrapper);
            }
            if(code.equals("1")&& resultList.size()>0){
                for(CheckResult result : resultList){
                    if(result.getUrl() != null && result.getUrl() != ""){
                        urlArr.add(result.getUrl());
                    }
                }
            }else{
                for(CheckList list1 : list){
                    if(list1.getUrl() != null && list1.getUrl() != ""){
                        urlArr.add(list1.getUrl());
                    }
                }
            }
        }
        if(urlArr.size()>0){
            String fileName = UUID.randomUUID().toString().replaceAll("-", "");
            return AjaxResponse.success( ZipUtil.DownloadUrl(urlArr, fileName));
        }else{
            return null;
        }

    }
    @Override
    public AjaxResponse<Set> urlListExport(List<CheckList> checkLists, String code) throws Exception {
        Set<String> urlArr = new HashSet<String>();
        String userId = BaseAuthUtil.getCurrentUserId();
        EntityWrapper<CheckResult> resultWrapper  ;
        EntityWrapper<CheckList> listWrapper  ;
        List<CheckResult> resultList=new ArrayList<CheckResult>();
        List<CheckList> list=new ArrayList<CheckList>();
        for(CheckList checkList:checkLists){
            if(code.equals("0")){
                listWrapper = new EntityWrapper<CheckList>();
                listWrapper.where("id="+"'"+checkList.getId()+"'");
                listWrapper.eq("user_id",userId);
                listWrapper.eq("delete_flag","0");
                list= checkListMapper.selectList(listWrapper);
            }else{
                resultWrapper = new EntityWrapper<CheckResult>();
                resultWrapper.where("parent_id="+"'"+checkList.getId()+"'");
                resultWrapper.eq("user_id",userId);
                resultWrapper.eq("delete_flag","0");
                resultList= checkResultMapper.selectList(resultWrapper);
            }
            if(code.equals("1")&& resultList.size()>0){
                for(CheckResult result : resultList){
                    if(result.getUrl() != null && result.getUrl() != ""){
                        urlArr.add(result.getUrl());
                    }
                }
            }else{
                for(CheckList list1 : list){
                    if(list1.getUrl() != null && list1.getUrl() != ""){
                        urlArr.add(list1.getUrl());
                    }
                }
            }
        }
        if(urlArr.size()>0){
            return AjaxResponse.success(urlArr);
        }else{
            return null;
        }

    }

    @Override
    @Transactional
    public Object callback(CheckResult record) {


//        CheckList checkList = new CheckList();
//        checkList.setStatus(record.getStatus());
//        checkList.setResult(record.getResult());
//        checkList.setReason(record.getReason());
//        checkList.setId(record.getParentId());
//        checkListMapper.updateById(checkList);
        CheckList afterRecord =  checkListMapper.selectById(record.getParentId());
        //CheckResult 查验结果
        record.setId(StringUtil.generateUUID());
        record.setCreateDate(new Date());
        record.setUserId(afterRecord.getUserId());
        record.setInvoiceType(afterRecord.getInvoiceType());
        record.setInvoiceCode(afterRecord.getInvoiceCode());
        record.setInvoiceNumber(afterRecord.getInvoiceNumber());
        record.setInvoiceDate(afterRecord.getInvoiceDate());
        record.setCheckCode(afterRecord.getCheckCode());
        record.setExcludingTaxPrice(afterRecord.getExcludingTaxPrice());
        //插入查验结果
        checkResultMapper.insert(record);
//        ThreadPoolTaskExecutor tpte = (ThreadPoolTaskExecutor) SpringContextUtil.getBean("taskExecutor");
        StatisticsThread statisticsThread = new StatisticsThread(afterRecord,record);
        statisticsThread.run();
//        tpte.execute(statisticsThread);
//        // 统计分析条件
//        StatisticsSetting statisticsSetting = statisticsSettingService.selectByUserId(afterRecord.getUserId());
//        // 信息对比统计表
//        if(statisticsSetting.isCompare()){
//            isCompare(afterRecord,record);
//        }
//        // 重复查询统计表
//        if(statisticsSetting.isDuplicate()){
//            isDuplicate(record);
//        }
//        // 复查结果变更统计表
//        if(statisticsSetting.isChange()){
//            isChange(afterRecord,record);
//        }
//        // 购买方/销售方信息不符统计表
//        if(statisticsSetting.isCompany()){
//            isCompany(record);
//        }
//        // 开票日期不符统计表
//        if(statisticsSetting.isDatematch()){
//            isDatematch(record);
//        } // 同一销售方统计表
//        if(statisticsSetting.isOneseller()){
//            isOneseller(record);
//        }
        return null;
    }

    /**
     * 票面信息对比统计表
     * 说明:
     * 1.参加统计的数据为“查验结果”为成功，录入方式为“扫描仪”、“手机拍照”、“PC端图像导入”、“手机APP端图像导入”的。
     * 2.数据项名称包括：购买方名称、购买方纳税人识别号、购买方地址电话、购买方开户行及账号、价款合计金额、税额合计金额、价税合计金额（大写）、价税合计金额（小写）、销售方名称、销售方纳税人识别号、销售方地址电话、销售方开户行及账号、备注。用户可以在“设置”中进行勾选。
     * @param afterRecord
     * @param record
     */
    @Override
    public void isCompare(CheckList afterRecord, CheckResult record){
        if(record.getResult().equals("真票")){
            if(StringUtil.isNotNullEmpty(afterRecord.getOcrId())){
                ParamsMy paramsMy =  new ParamsMy();
                paramsMy.setUserId(afterRecord.getUserId());
                // 信息对比
                InformationComparison informationComparison =   new InformationComparison();
                informationComparison.setInvoiceName(InvoiceUtils.getName(afterRecord.getInvoiceType()));
                informationComparison.setInvoiceCode(afterRecord.getInvoiceCode());
                informationComparison.setInvoiceNumber(afterRecord.getInvoiceNumber());
                informationComparison.setInvoiceDate(afterRecord.getInvoiceDate());
                informationComparison.setCreateDate(record.getCreateDate());
                informationComparison.setUserId(afterRecord.getUserId());
                AjaxListResponse<ParamsMy> paramsMyAjaxListResponse =  paramsService.getMyListPage(paramsMy);
                List<ParamsMy> paramsMyList = paramsMyAjaxListResponse.getData();
                if(paramsMyList!=null &&  paramsMyList.size()>0){
                    OcrInfo ocrInfo = ocrService.selectById(afterRecord.getOcrId());
                    for (ParamsMy paramsMy1 :paramsMyList){
                        // 判断值是否一致
                        if(!resultAndOCREqual(ocrInfo,record,paramsMy1,informationComparison)){
                            informationComparison.setId(StringUtil.generateUUID());
                            informationComparisonService.saveOrUpdate(informationComparison);
                        }
                    }
                }
            }
        }
    }
    //重复查询统计表
    @Override
    public void isDuplicate(CheckResult record){
        String userId = BaseAuthUtil.getCurrentUserId();
        if(record.getResult().equals("真票")){
            DuplicateQuery dq = new DuplicateQuery();
            dq.setInvoiceName(InvoiceUtils.getName(record.getInvoiceType()));
            dq.setCreateDate(record.getCreateDate());
            dq.setInvoiceDate(record.getInvoiceDate());
            dq.setInvoiceCode(record.getInvoiceCode());
            dq.setInvoiceNumber(record.getInvoiceNumber());
            dq.setResult(record.getResult());
            dq.setUserId(record.getUserId());
            EntityWrapper<CheckResult> wrapper =  new EntityWrapper<CheckResult>();
            //wrapper.where("DATE(create_date) !="+ new SimpleDateFormat("yyyy-MM-dd").format(record.getCreateDate()));
            //wrapper.where("user_id = '" +record.getUserId()+"'");
            wrapper.eq("user_id =",  userId );
            wrapper.where("invoice_code =" +record.getInvoiceCode());
            wrapper.where("invoice_number =" +record.getInvoiceNumber());
            wrapper.eq("delete_flag","0");
            wrapper.groupBy("create_date");
            List<CheckResult> resultsList = checkResultMapper.selectList(wrapper);
            if(resultsList.size()>1){
                dq.setId(StringUtil.generateUUID());
                duplicateQueryService.saveOrUpdate(dq);

            }
        }
    }

    /**
     * 复查结果变更统计
     * 说明:
     * 1.参加统计的数据为原“查验结果”为成功，复查结果为非成功的。
     * 2.如果为多次复查，则查询日期为上次结果为成功的查询。一次复查结果为非成功的，二次复查结果也为非成功的，则为两条数据。
     * @param afterRecord
     * @param record
     */
    @Override
    public void isChange(CheckList afterRecord,CheckResult record){
            // 非复查项目 , 不用统计
            // 结果是真票不用复查
            if(!afterRecord.getReCheck()|| "真票".equals(record.getResult()) ){
                return;
            }
            if("真票".equals(afterRecord.getReCheckResult()) ){
                CheckList c = checkListMapper.selectById(afterRecord.getReCheckId());
                ReviewResultChange reviewResultChange = new ReviewResultChange();
                reviewResultChange.setInvoiceCode(record.getInvoiceCode());
                reviewResultChange.setInvoiceName(InvoiceUtils.getName(record.getInvoiceType()));
                reviewResultChange.setInvoiceNumber(record.getInvoiceNumber());
                reviewResultChange.setInvoiceDate(record.getInvoiceDate());
                reviewResultChange.setResult("真票");
                reviewResultChange.setReexamineDate(new Date());
                reviewResultChange.setReexamineResult(record.getResult());
                reviewResultChange.setId(StringUtil.generateUUID());
                reviewResultChange.setCreateDate(c.getCreateDate());
                reviewResultChange.setUserId(afterRecord.getUserId());
                reviewResultChangeService.saveOrUpdate(reviewResultChange);

            }
            // 如果不是真票,查找是否有真票的记录
            if(!"真票".equals(afterRecord.getReCheckResult())){
                EntityWrapper<CheckResult> wrapper =  new EntityWrapper<CheckResult>();
                wrapper.where("parent_id =" +"'"+afterRecord.getId()+"'");
                wrapper.eq("delete_flag","0");
                wrapper.eq("user_id",afterRecord.getUserId());
                wrapper.eq("result","真票");
                wrapper.ne("id",record.getId());
                List<CheckResult> resultsList = checkResultMapper.selectList(wrapper);
                if(resultsList!=null && resultsList.size()>0){
                    ReviewResultChange reviewResultChange = new ReviewResultChange();
                    reviewResultChange.setInvoiceCode(record.getInvoiceCode());
                    reviewResultChange.setInvoiceName(InvoiceUtils.getName(record.getInvoiceType()));
                    reviewResultChange.setInvoiceNumber(record.getInvoiceNumber());
                    reviewResultChange.setInvoiceDate(record.getInvoiceDate());
                    reviewResultChange.setReexamineDate(new Date());
                    reviewResultChange.setResult("真票");
                    reviewResultChange.setReexamineResult(record.getResult());
                    reviewResultChange.setId(StringUtil.generateUUID());
                    reviewResultChange.setCreateDate(resultsList.get(resultsList.size()-1).getCreateDate());
                    reviewResultChange.setUserId(afterRecord.getUserId());
                    reviewResultChangeService.saveOrUpdate(reviewResultChange);

                }
            }
//            Boolean isReally = false;
//            ReviewResultChange reviewResultChange = new ReviewResultChange();
//            CheckResult result = checkResultMapper.findImmediately(record);
//            //CheckResult lastTime = checkResultMapper.findLastTime(record);
//            EntityWrapper<CheckResult> wrapper =  new EntityWrapper<CheckResult>();
//            wrapper.where("parent_id =" +"'"+record.getId()+"'");
//            wrapper.eq("delete_flag","0");
//            wrapper.eq("user_id",userId);
//            //List<CheckResult> resultsList = checkResultMapper.selectList(wrapper);
//            if(record.getResult().equals("假票")){
//                if(afterRecord.getResult() != null && result.getResult() != null){
//                    if(!(afterRecord.getResult().equals(result.getResult()))){
//                        reviewResultChange.setResult(result.getResult());
//                        reviewResultChange.setCreateDate(record.getCreateDate());
//                        isReally = true;
//                    }else if(record.getResult().equals("假票") && result.getResult().equals("假票")){
//                        reviewResultChange.setResult(result.getResult());
//                        reviewResultChange.setCreateDate(record.getCreateDate());
//                        isReally = true;
//                    }
//                }
//            }
//        if(isReally){
//            reviewResultChange.setInvoiceCode(record.getInvoiceCode());
//            reviewResultChange.setInvoiceName(InvoiceUtils.getName(record.getInvoiceType()));
//            reviewResultChange.setInvoiceNumber(record.getInvoiceNumber());
//            reviewResultChange.setInvoiceDate(record.getInvoiceDate());
//            reviewResultChange.setReexamineDate(record.getCreateDate());
//            reviewResultChange.setReexamineResult(record.getResult());
//            reviewResultChange.setId(StringUtil.generateUUID());
//            reviewResultChange.setUserId(userId);
//            reviewResultChangeService.saveOrUpdate(reviewResultChange);
//        }
            }


    @Override
    public void isCompany( CheckResult record){
        String userId = BaseAuthUtil.getCurrentUserId();
        if(record.getResult().equals("真票")){
            InformationDiscrepancy informationDiscrepancy = new InformationDiscrepancy();
            informationDiscrepancy.setInvoiceCode(record.getInvoiceCode());
            informationDiscrepancy.setInvoiceName(InvoiceUtils.getName(record.getInvoiceType()));
            informationDiscrepancy.setCreateDate(record.getCreateDate());
            informationDiscrepancy.setInvoiceDate(record.getInvoiceDate());
            informationDiscrepancy.setInvoiceNumber(record.getInvoiceNumber());
            EntityWrapper<Company> wrapper =  new EntityWrapper<Company>();
            wrapper.where("user_id = '"+userId+"'");
            wrapper.where("status = '1'");
            wrapper.eq("delete_flag","0");
            List<Company> result=  companyMapper.selectList(wrapper);
            if(result.size()>0){
                for(Company c : result){
                    if(c.getCompanyName() != null && record.getSellerName() != null && record.getPurchaser()!=null ){
                        if( !(c.getCompanyName().equals(record.getSellerName())  )){
                            informationDiscrepancy.setType("销售方");
                            informationDiscrepancy.setContent(record.getSellerName());
                            informationDiscrepancy.setId(StringUtil.generateUUID());
                            informationDiscrepancyService.saveOrUpdate(informationDiscrepancy);
                        }else if(!(c.getCompanyName().equals(record.getPurchaser()))){
                            informationDiscrepancy.setType("购买方");
                            informationDiscrepancy.setContent(record.getPurchaser());
                            informationDiscrepancy.setId(StringUtil.generateUUID());
                            informationDiscrepancyService.saveOrUpdate(informationDiscrepancy);
                        }
                    }
                }
            }
        }
    }
    @Override
    public void isDatematch(  CheckResult record)  {
        if(record.getResult().equals("真票")){
            InvoiceDateDiscrepancy invoiceDateDiscrepancy = new InvoiceDateDiscrepancy();
            //invoiceDateDiscrepancy.setInvoiceName(InvoiceUtils.getName(record.getInvoiceType()));
            invoiceDateDiscrepancy.setInvoiceCode(record.getInvoiceCode());
            invoiceDateDiscrepancy.setInvoiceDate(record.getInvoiceDate());
            invoiceDateDiscrepancy.setCreateDate(record.getCreateDate());
            invoiceDateDiscrepancy.setInvoiceNumber(record.getInvoiceNumber());
            List<InvoiceDate> invoiceDates = invoiceDateService.getInvoiceDate();
            Date start = new Date();
            Date end  = new Date();
            Date time  = record.getInvoiceDate();
            for( InvoiceDate i: invoiceDates){
                start = i.getStartDate();
                end = i.getEndDate();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            if(time != null && time.toString() != ""){
                if(!(start.getTime()<time.getTime() && time.getTime()<end.getTime())){
                    invoiceDateDiscrepancy.setId(StringUtil.generateUUID());
                    invoiceDateDiscrepancyService.saveOrUpdate(invoiceDateDiscrepancy);
                }
            }
        }
    }
    @Override
    public void isOneseller(CheckResult record){
            CommonSeller cs = new CommonSeller();
            cs.setInvoiceCode(record.getInvoiceCode());
            cs.setInvoiceDate(record.getInvoiceDate());
            cs.setInvoiceNumber(record.getInvoiceNumber());
            cs.setBuyerName(record.getPurchaser());
            cs.setTaxAmount( record.getTaxAmount());
            cs.setSellerName(record.getSellerName());
            cs.setPrice(record.getPrice());
            cs.setPriceTax(record.getTotalAmountOfTax());
            cs.setInvoiceName(InvoiceUtils.getName(record.getInvoiceType()));
            if(record.getPurchaser() != null && record.getSellerName()!= null){
                EntityWrapper<CheckResult> wrapper =  new EntityWrapper<CheckResult>();
                wrapper.where("result = '真票'" );
                wrapper.where("purchaser = '" +record.getPurchaser()+"'");
                wrapper.where("seller_name ='" +record.getSellerName()+"'");
                wrapper.eq("delete_flag","0");
                List<CheckResult> resultsList = checkResultMapper.selectList(wrapper);
                if(resultsList.size()>=2){
                    cs.setId(StringUtil.generateUUID());
                    commonSeller.saveOrUpdate(cs);
                }
            }
    }


    @SneakyThrows
    private Boolean resultAndOCREqual(OcrInfo ocrInfo, CheckResult record, ParamsMy paramsMy1, InformationComparison informationComparison ){
        Boolean isEqual = false;
        String name = paramsMy1.getKey().replaceFirst(paramsMy1.getKey().substring(0, 1), paramsMy1.getKey().substring(0, 1).toUpperCase());
            Method methodOrc = ocrInfo.getClass().getMethod("get" + name);
            String ocrValue = (String) methodOrc.invoke(ocrInfo);
            Method methodResult = record.getClass().getMethod("get" + name);
            String resultValue = (String) methodResult.invoke(record);
            if(ocrValue  == resultValue){
                isEqual = true;
            }else{
                informationComparison.setItemName(paramsMy1.getValue());
                informationComparison.setWebsiteInfo(resultValue);
                informationComparison.setOcrInfo(ocrValue);
                isEqual = false;
            }


        return isEqual;
    }


}
