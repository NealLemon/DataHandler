package DataHandlerTool.demo.mapper;

import org.apache.ibatis.annotations.Insert;

/**
 * demo示例
 * 学生类对应的mapper
 */
public interface StudentMapper{

    /**
     * 入库接口
     * @param o
     * @return
     */
    @Insert("insert into student (name,number) values(#{name},#{number})")
    void insert(Object o);
}
