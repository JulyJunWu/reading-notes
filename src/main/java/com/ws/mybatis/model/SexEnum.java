package com.ws.mybatis.model;

/**
 * @author JunWu
 * 枚举
 */
public enum SexEnum {

    MALE("男"),

    FEMALE("女");

    String sex;

    SexEnum(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    /**
     * 根据字符串获取枚举
     * @param sex
     * @return
     */
    public static SexEnum getEnum(String sex) {
        for (SexEnum s : values()) {
            if (s.getSex().equals(sex)) {
                return s;
            }
        }
        return null;
    }
}
