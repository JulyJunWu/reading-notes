package com.ws.book.深入理解java虚拟机;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jun
 * data  2019-10-04 20:36
 */
public class Test {

    int size = 2;

    private Object idleKey;

    private Map<Object, Object> linkedHashMap = new LinkedHashMap(size, .75F) {

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {

            boolean out = size() > size;

            if (out) {
                idleKey = eldest.getKey();
            }

            return out;
        }
    };

    private Map map = new HashMap();

    public static void main(String[] args) {


        Test test = new Test();
        test.put("ws");
        test.put("ws2");
        test.put("ws3");
        test.put("ws4");
        test.put("ws5");
        test.put("ws6");



    }

    public void put(Object key) {

        linkedHashMap.put(key, key);

        if (idleKey != null) {
            map.remove(idleKey);
            System.out.println("容量超出,移除" + idleKey);
            idleKey = null;
        }
        map.put(key, key);
    }
}


