package DataHandlerTool.demo.domain;

import DataHandlerTool.management.annotation.Column;
import DataHandlerTool.management.preconfig.BaseDataBean;

/**
 * 示例 学生姓名
 *
 */
public class Student{
    //学生姓名
    @Column(name="学生姓名")
    private String name;
    //学生学号
    @Column(name="学生学号")
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
