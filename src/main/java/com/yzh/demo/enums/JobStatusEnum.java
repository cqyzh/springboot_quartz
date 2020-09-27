package com.yzh.demo.enums;

/**
 * 任务状态枚举
 * @author : yzh
 * @date : 2020-09-27 10:08
 **/
public enum  JobStatusEnum {
    STOP("0","停止"),
    RUNNING("1","运行"),
    ;


    private final String code;

    private final String desc;

    JobStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getStatus() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}
