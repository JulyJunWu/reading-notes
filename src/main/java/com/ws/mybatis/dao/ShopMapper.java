package com.ws.mybatis.dao;

import com.ws.mybatis.model.Shop;
import com.ws.mybatis.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author JunWu
 * Shop Mapper
 */
public interface ShopMapper {

    List<Shop> selectShopByUserId(@Param("userId") String userId);

    Shop selectOneByUserId(@Param("userId") String userId);

}
