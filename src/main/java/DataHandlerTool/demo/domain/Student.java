package DataHandlerTool.demo.domain;

import DataHandlerTool.management.annotation.Column;

/**
 * 学生实体类
 *
 *
 */
public class Student{
    //学生姓名
    @Column(name="学生姓名")
    private String name;
    //学生学号  匹配数字
    @Column(name="学生学号",regex = "[1-9]+")
    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
