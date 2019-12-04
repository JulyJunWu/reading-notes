package com.ws.design.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 * data  2019-09-22 10:51
 *
 * add,remove,getAll是否抽象出来放在Component是个问题,
 * 优点 : 便于统一管理;
 * 缺点 : 这些方法对于叶子节点是没有任何意义的;
 *
 */
public class Composite implements Component {

    private List<Component> componentList = new ArrayList<>();

    @Override
    public void println() {
        componentList.stream().forEach(p -> p.println());
    }

    public boolean add(Component component) {
        return componentList.add(component);
    }

    public boolean remove(Component component) {
        return componentList.remove(component);
    }


    public List<Component> getAll() {
        return componentList;
    }
}
