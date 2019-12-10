package com.ws.mybatis.service;

import com.ws.mybatis.dao.ShopMapper;
import com.ws.mybatis.model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JunWu
 */
@Service("shopService")
public class ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertList(List<Shop> shopList) {
        shopList.stream().forEach(shop -> this.insert(shop));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public int insert(Shop shop) {
        return shopMapper.insertShop(shop);
    }
}
