package DataHandlerTool.management;

import DataHandlerTool.exception.DataHandlerException;
import DataHandlerTool.filehandler.FileHandler;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 自定义文件处理接口
 */
public interface FileHandleProcessor {

    /**
     * 初始化操作
     * @param fileHandler 文件处理类
     */
    void generate(FileHandler fileHandler) throws DataHandlerException;

    /**
     * 读取操作以及处理操作
     */
    void dataInDatabaseProcess(SqlSessionFactory sqlSessionFactory,String mapper,String insertMethod) throws DataHandlerException;

    /**
     * 用户自定义处理
     */
    void selfDefinedHandle();
}
