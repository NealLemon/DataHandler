package DataHandlerTool.management;

import DataHandlerTool.exception.DataHandlerException;
import DataHandlerTool.management.preconfig.DataFileAttribute;

import java.util.List;

/**
 * 文件处理模板接口
 */
public interface DataProcessor {

    //读取文件数据
    void generate(List<DataFileAttribute> dataFileAttributes) throws DataHandlerException;

    //导出操作
    //void dataExport();
}
