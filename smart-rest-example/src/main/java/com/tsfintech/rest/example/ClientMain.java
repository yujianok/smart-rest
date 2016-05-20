package com.tsfintech.rest.example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.tsfintech.rest.client.CommonRestServiceClient;
import com.tsfintech.rest.client.RestServiceClient;
import com.tsfintech.rest.core.model.EntityQuery;
import com.tsfintech.rest.example.entity.Customer;
import com.tsfintech.rest.example.entity.SalesOrder;

/**
 * Created by jack on 14-8-1.
 */
public class ClientMain {

    public static void main(String[] args) {

        RestServiceClient restServiceClient = new CommonRestServiceClient("http://localhost:8080/service");

        for (int i = 0; i < 10; i++) {
            SalesOrder order = new SalesOrder();
            order.setShopId(123L);
            order.setCode("123456");
            order.setOrderDate(new Date());
            order.setPayment(new BigDecimal(Math.random()).setScale(2, BigDecimal.ROUND_HALF_UP));

            restServiceClient.save("123", order, SalesOrder.class);

        }

        for (int i = 0; i < restServiceClient.count("123", SalesOrder.class, EntityQuery.buildQuery()); i++) {
            SalesOrder salesOrder = restServiceClient.retrieve("123", SalesOrder.class, String.valueOf(i + 1));
            System.out.println(salesOrder.getId() + ":" + salesOrder.getCode());
        }

        EntityQuery query = EntityQuery.buildQuery().queryBy("findByShopIdAndCode")
                .addCondition("shopId", "123")
                .addCondition("code", "123456").disablePage();

        List<SalesOrder> orders = restServiceClient.find("123", SalesOrder.class, query);
        for (SalesOrder order : orders) {
            System.out.println("No page: " + order.getId() + ":" + order.getCode());
        }

        query = EntityQuery.buildQuery().queryBy("findByShopIdAndCode")
                .addCondition("shopId", "123")
                .addCondition("code", "123456")
                .pageBy(1, 5);

        orders = restServiceClient.find("123", SalesOrder.class, query);
        for (SalesOrder order : orders) {
            System.out.println("Page without order: " + order.getId() + ":" + order.getCode());
        }

        query = EntityQuery.buildQuery().queryBy("findByShopIdAndCode")
                .addCondition("shopId", "123")
                .addCondition("code", "123456")
                .orderBy("id", false);

        orders = restServiceClient.find("123", SalesOrder.class, query);
        for (SalesOrder order : orders) {
            System.out.println("Order by id: " + order.getId() + ":" + order.getCode());
        }

        query = EntityQuery.buildQuery()
                .queryBy("findByShopIdAndCode")
                .addCondition("shopId", "123")
                .addCondition("code", "123456")
                .orderBy("code,id", false)
                .pageBy(1, 5);

        orders = restServiceClient.find("123", SalesOrder.class, query);
        for (SalesOrder order : orders) {
            System.out.println("Page with order: " + order.getId() + ":" + order.getCode());
        }

        query = EntityQuery.buildQuery().queryBy("findByIds")
                .disablePage()
                .addCondition("shopId", "123")
                .addCondition("ids", "1,2,3");

        orders = restServiceClient.find("123", SalesOrder.class, query);
        for (SalesOrder order : orders) {
            System.out.println("Query by ids: " + order.getId() + ":" + order.getCode());
        }

        query = EntityQuery.buildQuery().queryBy("findOneByShopIdAndId")
                .disablePage()
                .addCondition("shopId", "123")
                .addCondition("id", "2");

        orders = restServiceClient.find("123", SalesOrder.class, query);
        for (SalesOrder order : orders) {
            System.out.println("Query one by id: " + order.getId() + ":" + order.getCode());
        }
        SalesOrder salesOrder = restServiceClient.serviceRequest("123", SalesOrder.class).entity("2").retrieve();
        salesOrder.setCode("abc");
        SalesOrder newOne = restServiceClient.serviceRequest("123", SalesOrder.class).entity("2").update(salesOrder);
        System.out.println("Update sales order: " + newOne.getId() + ":" + newOne.getCode());

        Customer customer = new Customer();
        customer.setName("小明");
        customer.setId(1);
        customer.setShopId(2);
        customer.setAge(20);

        restServiceClient.serviceRequest("2", Customer.class).save(customer);
        Customer customer1 = restServiceClient.serviceRequest("2", Customer.class)
                .entity("1")
                .operation("changeAge")
                .operate(new Object[]{30});

        System.out.println("Customer new age : " + customer1.getAge());

        query = EntityQuery.buildQuery().queryBy("findByAge")
                .disablePage()
                .addCondition("shopId", "123")
                .addCondition("age", "20");

        List<Customer> customers = restServiceClient.find("123", Customer.class, query);
        for (Customer customer2 : customers) {
            System.out.println("Customer id : " + customer2.getId());
        }

    }

}
