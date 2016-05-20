package com.tsfintech.rest.example.manager;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.tsfintech.rest.core.model.EntityQuery;
import com.tsfintech.rest.example.dao.CommonDao;
import com.tsfintech.rest.example.entity.Customer;
import com.tsfintech.rest.core.operator.*;

/**
 * Created by jack on 14-10-15.
 */
@Service
public class CustomerManager implements
        EntityCreator<Customer>,
        EntityFinder<Customer>,
        EntityUpdater<Customer>,
        EntityRetriever<Customer>,
        EntityDeleter<Customer>,
        EntityInvoker<Customer> {

    @Autowired
    private CommonDao commonDao;

    @Override
    public Object save(String scope, Customer entity) {
        commonDao.save(entity);

        return entity;
    }

    @Override
    public long count(String scope, EntityQuery entityQuery) {
        String name = entityQuery.getConditionValue("name");
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        if (name != null) {
            criteria.add(Restrictions.eq("name", name));
        }

        return commonDao.count(criteria);
    }

    @Override
    public List<Customer> find(String scope, EntityQuery entityQuery) {
        String name = entityQuery.getConditionValue("name");
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        if (name != null) {
            criteria.add(Restrictions.eq("name", name));
        }

        return commonDao.find(criteria);
    }

    @Override
    public Object update(String scope, String id, Customer entity) {
        commonDao.update(entity);

        return entity;
    }

    @Override
    public Customer retrieve(String scope, String id) {

        return commonDao.retrieve(Customer.class, Long.valueOf(id));
    }

    @Override
    public Customer delete(String scope, String id) {
        Customer customer = commonDao.retrieve(Customer.class, Long.valueOf(id));
        commonDao.delete(customer);

        return customer;
    }

    public Customer changeName(Customer customer, String name) {
        customer.setName(name);

        return customer;
    }

    public Customer changeAge(Customer customer, int age) {
        customer.setAge(age);

        return customer;
    }

    public List<Customer> findByAge(@Param("shopId") long shopId, @Param("age") int age) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
        criteria.add(Restrictions.eq("shopId", shopId));
        criteria.add(Restrictions.eq("age", age));

        return commonDao.find(criteria);
    }
}
