package com.ryxt.util;

import com.ryxt.entity.BaseEntity;
import com.ryxt.entity.BaseInput;

import java.util.List;
/**
* @Description: CommonListResponse.java
* @Author: uenpeng
* @Date: 2020/10/10
*/
public class CommonListResponse<T>  {


    private List<T> list ;

    private int totalCount;

    private int page;

    private int pageSize;

    private int pageCount;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        this.pageCount = (int) Math.ceil(totalCount / this.pageSize);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }


   public <T> CommonListResponse(BaseInput e){
            this.page = e.getPage();
            this.pageSize = e.getPageSize();
    }
}
