package DataHandlerTool.demo.mapper;

import DataHandlerTool.demo.domain.Student;
import DataHandlerTool.management.preconfig.BaseMapper;

import java.util.List;

/**
 * demo示例
 * 学生类对应的mapper
 */
public interface StudentMapper{

    //获取全部学生
    List<Student> selectStudents();

    int insert(Object o);
}
