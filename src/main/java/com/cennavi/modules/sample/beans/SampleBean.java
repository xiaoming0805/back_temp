package com.cennavi.modules.sample.beans;

import com.cennavi.core.common.MyTable;

/**
 * Created by sunpengyan on 2021/1/5.
 */
@MyTable("sample")//使用BeseDao时，需要bean名和表名一致，不一致则需要加此注解；
public class SampleBean {
    //id
    private String id;
    //名称
    private String name;
    //code
    private String code;
    //age
    private int age;

//    @IgnoreColumn("")  //使用BeseDao保存更新字段时，需要忽略的字段，使用此注解
//    private String otherName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;

    }
}
