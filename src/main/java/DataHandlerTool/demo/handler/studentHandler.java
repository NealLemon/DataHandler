package DataHandlerTool.demo.handler;

import DataHandlerTool.demo.domain.Student;
import DataHandlerTool.management.AbstractFileHandleProcessor;

import java.util.List;

public class studentHandler extends AbstractFileHandleProcessor<Student> {

    /**
     * your data process logic write here
     */
    @Override
    public void selfDefinedHandle () {
        List<Student> studentList = getBeanList();
        studentList.stream().map(Student::getName).forEach(System.out::println);
    }
}
