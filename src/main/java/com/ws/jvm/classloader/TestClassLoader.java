package com.ws.jvm.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author Jun
 * data  2019-08-11 11:37
 * 自定义类加载器
 */
public class TestClassLoader extends ClassLoader {

    private String classLoaderName;

    public static final String FILE_EXTENSION = ".class";

    public TestClassLoader(String classLoaderName) {
        //若未指定父类加载器,则自定义实现的类加载器默认的父加载器是系统类加载器
        super();
        this.classLoaderName = classLoaderName;
    }

    public TestClassLoader(String classLoaderName, ClassLoader parentClassLoader) {
        //显示设置父类加载器
        super(parentClassLoader);
        this.classLoaderName = classLoaderName;
    }


    private byte[] loadClassData(String name) {

        String fileName = name.replace(".", "/") + FILE_EXTENSION;

        byte[] bytes = null;

        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                FileInputStream inputStream = new FileInputStream(new File(fileName))
        ) {

            int temp;

            while ((temp = inputStream.read()) != -1) {
                outputStream.write(temp);
            }
            bytes = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = this.loadClassData(name);
        return super.defineClass(name, bytes, 0, bytes.length);
    }


    @Override
    public String toString() {
        return classLoaderName;
    }

    public static void main(String[] args) throws Exception {

        TestClassLoader loader = new TestClassLoader("ZWS");
        //不使用laodClass, 是因为没有覆盖loadClass,要不然最终调用的还是父类加载器去加载字节码
        Class<?> aClass = loader.findClass("com.ws.jvm.Test7");

    }
}
