package com.ryxt.service;

import com.ryxt.entity.InvoiceDate;

import java.util.List;

public interface InvoiceDateService {
    List<InvoiceDate> getInvoiceDate();

    InvoiceDate saveOrUpdate(InvoiceDate record);
}
