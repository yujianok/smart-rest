package com.tsfintech.rest.example.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.tsfintech.rest.example.entity.SalesOrder;

/**
 * Created by jack on 15-5-20.
 */
public interface SalesOrderCustom {

    public List<SalesOrder> findByIds(@Param("shopId") Long shopId, @Param("ids") String ids);

}
