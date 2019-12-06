package com.ws.mybatis.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Shop implements Serializable {

    private String id;

    private String userId;

    private String shopName;

    private BigDecimal price;

}
