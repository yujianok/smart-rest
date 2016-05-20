package com.tsfintech.rest.example.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tsfintech.rest.example.entity.SalesOrder;

/**
 * Created by jack on 15-5-20.
 */
@Repository
public class SalesOrderDaoImpl implements SalesOrderCustom {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<SalesOrder> findByIds(Long shopId, String ids) {
        String sql = "select * from sales_order where id in (" + ids + ")";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<SalesOrder>(SalesOrder.class));
    }
}
