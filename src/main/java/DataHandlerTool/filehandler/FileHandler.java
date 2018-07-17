package DataHandlerTool.filehandler;

import DataHandlerTool.exception.DataHandlerException;

import java.util.List;

public interface FileHandler {
    void process() throws DataHandlerException;

    void setCharsetName(String charsetName);

    void setFilePath(String filePath) throws DataHandlerException;
    List<List<String>> getDataList();
}
