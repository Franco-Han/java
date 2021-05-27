package com.ryxt.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ryxt.entity.InvoiceDate;
import com.ryxt.entity.User;
import com.ryxt.mapper.InvoiceDateMapper;
import com.ryxt.service.InvoiceDateService;
import com.ryxt.util.BaseAuthUtil;
import com.ryxt.util.Const;
import com.ryxt.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceDateServiceImpl implements InvoiceDateService {

	@Autowired
	public InvoiceDateMapper invoiceDateMapper;

	@Override
	public List<InvoiceDate> getInvoiceDate() {
        EntityWrapper<InvoiceDate> wrapper = new EntityWrapper<InvoiceDate>();
		String userId = BaseAuthUtil.getCurrentUserId();
        wrapper.eq("user_id",  userId );
		return invoiceDateMapper.selectList(wrapper);
	}

    @Override
    public InvoiceDate saveOrUpdate(InvoiceDate record) {
        String userId = BaseAuthUtil.getCurrentUserId();

        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        InvoiceDate user1 = invoiceDateMapper.selectById(record.getId());

        if(user1!=null){
            invoiceDateMapper.updateById(record);
        }else {
            record.setUserId(userId);
            record.setCreateDate(new Date());
            record.setDeleteFlag(Const.NORMAL_STATUS);
            invoiceDateMapper.insert(record);
        }
        return record;
    }

}
