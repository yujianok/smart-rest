package com.tsfintech.rest.example.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tsfintech.rest.example.entity.SalesOrder;

/**
 * Created by jack on 14-12-16.
 */
@Repository
public interface SalesOrderDao extends JpaRepository<SalesOrder, Long>, SalesOrderCustom {

    public List<SalesOrder> findByShopIdAndCode(@Param("shopId") long userId, @Param("code") String code, Pageable pageable);

    public List<SalesOrder> findByShopIdAndCode(@Param("shopId") long userId, @Param("code") String code, Sort sort);

    public List<SalesOrder> findByShopIdAndCode(@Param("shopId") long userId, @Param("code") String code);

    public SalesOrder findOneByShopIdAndId(@Param("shopId") Long shopId, @Param("id") long id);
}
