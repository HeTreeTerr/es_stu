package com.hss.Enum;

/**
 * 性别枚举类
 */
public enum GenderEnum {

    MAN("0"),
    WOMEN("1");

    private String code;

    private GenderEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
