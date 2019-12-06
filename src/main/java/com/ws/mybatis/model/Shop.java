package com.ws.mybatis.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Shop {

    private String id;

    private String userId;

    private String shopName;

    private BigDecimal price;

}
