package com.ws.agent.agentmain;

import java.io.File;
import java.io.FileInputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 尝试用agent attach 的方式进行对内存中的字节码进行热更新
 */
public class AgentMain {

    public static void agentmain(String args, Instrumentation instrumentation) {
        System.out.println("agentmain start");
        try {
            Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
            System.out.println("已加载类的数量:" + allLoadedClasses.length);
            Map<String, byte[]> loadClassFile = loadClassFile();
            if (loadClassFile.isEmpty()) {
                System.out.println("无 redefine class");
                return;
            }

            Map<String, byte[]> needRedefine = new HashMap<String, byte[]>();

            for (Map.Entry<String, byte[]> entry : loadClassFile.entrySet()) {
                String key = entry.getKey();
                byte[] value = entry.getValue();
                Integer integer = TestAgent.REDEFINE_CLASS_MAP.get(key);
                //  比较下是否发生改变
                int newI = ByteUtils.bytes2Int(value);
                if (integer != null) {
                    //  说明没有发生改变
                    if (newI == integer) {
                        System.out.println("字节码未发生改变,无需重新加载!");
                        continue;
                    }
                }
                needRedefine.put(key, value);
                TestAgent.REDEFINE_CLASS_MAP.put(key, newI);
            }

            if (needRedefine.isEmpty()) {
                System.out.println("无新class");
                return;
            }

            ClassDefinition[] classDefinitions = new ClassDefinition[needRedefine.size()];
            int i = 0;
            for (Class klass : allLoadedClasses) {
                String simpleName = klass.getSimpleName();
                byte[] bytes = loadClassFile.get(simpleName);
                if (bytes != null) {
                    System.out.println("加载" + simpleName);
                    ClassDefinition definition = new ClassDefinition(klass, bytes);
                    classDefinitions[i++] = definition;
                }
            }
            //  修改内存已加载的字节数据
            instrumentation.redefineClasses(classDefinitions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("agentmain end");
    }

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("premain start");
        System.out.println("premain end");
    }

    public static Map<String, byte[]> loadClassFile() {
        Map<String, byte[]> map = Collections.emptyMap();
        try {
            File file = new File("C:\\Users\\DELL\\Desktop\\redefineClass");
            //  需要重新加载的class文件
            String[] list = file.list();
            if (list == null) return map;
            map = new HashMap(list.length);
            for (String klass : list) {
                if (klass.contains(".class")) {
                    FileInputStream inputStream = null;
                    try {
                        String fileName = file.getAbsolutePath() + File.separator + klass;
                        System.out.println(fileName);
                        inputStream = new FileInputStream(fileName);
                        int available = inputStream.available();
                        byte[] bytes = new byte[available];
                        inputStream.read(bytes, 0, bytes.length);
                        String className = klass.substring(0, klass.indexOf(".class"));
                        map.put(className, bytes);
                    } catch (Exception E) {
                        E.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
