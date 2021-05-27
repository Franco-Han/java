package com.ryxt.controller;

import com.ryxt.entity.CheckList;
import com.ryxt.entity.Company;
import com.ryxt.entity.OcrInfo;
import com.ryxt.entity.OcrScanParam;
import com.ryxt.exception.BusinessException;
import com.ryxt.service.CheckService;
import com.ryxt.service.OcrService;
import com.ryxt.util.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* @Description: OCR识别
* @Author: uenpeng
* @Date: 2020/11/3
*/
@RestController
@RequestMapping(value = "/ocr")
public class OcrController {

    @Autowired
    private OcrService ocrService;
    @RequestMapping("/scan")
    public AjaxResponse<OcrInfo> scan(@RequestBody OcrInfo orc) {

        if(orc.getUrl()==null){
            throw new BusinessException("请传入正确的文件地址");
        }
        return AjaxResponse.success(ocrService.scan(orc.getUrl()));
    }
    @RequestMapping("/scan2")
    public AjaxResponse<OcrInfo> scan2(@RequestBody OcrScanParam orc) {

        if(orc.getUrl()==null){
            throw new BusinessException("请传入正确的文件地址");
        }
        return AjaxResponse.success(ocrService.scan2(orc.getUrl(),orc.getTempFile()));
    }
    /**
     * 保存或更新
     * @param record
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */

    @RequestMapping("/saveOrUpdate")
    public AjaxResponse<OcrInfo> saveOrUpdate(@RequestBody OcrInfo record) {
        return AjaxResponse.success(ocrService.saveOrUpdate(record));
    }
    /**
     * 查看详细
     * @param id
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.OcrInfo>
     * @throws Exception
     */

    @GetMapping("/selectById/{id}")
    public AjaxResponse<OcrInfo> selectById(@PathVariable(value = "id") String id) {
        OcrInfo record = ocrService.selectById(id);
        return AjaxResponse.success(record);
    }
    /**
     * 删除
     * @param id
     * @return com.ryxt.util.AjaxResponse<java.lang.String>
     * @throws Exception
     */

    @DeleteMapping("/deleteById/{id}")
    public AjaxResponse<String> deleteById(@PathVariable(value = "id") String id) {
        int count = ocrService.deleteById(id);
        return AjaxResponse.success(count>0?"删除成功":"删除失败");
    }
}
