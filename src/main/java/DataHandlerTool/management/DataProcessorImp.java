package DataHandlerTool.management;

import DataHandlerTool.exception.DataHandlerException;
import DataHandlerTool.filehandler.FileHandler;
import DataHandlerTool.filehandler.FileHandlerFactory;
import DataHandlerTool.management.preconfig.DataFileAttribute;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProcessorImp implements DataProcessor {

    Logger logger = LogManager.getLogger(getClass());

    @Override
    public void generate(List<DataFileAttribute> dataFileAttributes) {
        logger.info("数据开始进行处理.................");
        for (DataFileAttribute dataFileAttribute : dataFileAttributes) {
            logger.info("模型:" + dataFileAttribute.getModelName() + "开始.....");
            List<File> fileList = getAllFiles(dataFileAttribute.getFileDir());
            readAndHandle(fileList,dataFileAttribute);
            logger.info("模型:" + dataFileAttribute.getModelName() + "结束.....");
        }
    }

    /**
     * 处理同一文件夹下的所有数据
     * @param fileList
     * @param dataFileAttribute
     * @throws DataHandlerException
     */
    private void readAndHandle(List<File> fileList, DataFileAttribute dataFileAttribute)  {
            for (File file : fileList) {
                try{
                    AbstractFileHandleProcessor c = getHandleInstance(dataFileAttribute.getFileHandleClazz());
                    FileHandler fileHandler = getFileHandler(file);
                    fileHandler.setFilePath(file.getAbsolutePath());
                    fileHandler.setCharsetName(dataFileAttribute.getCharsetName());
                    c.generate(fileHandler);
                    c.selfDefinedHandle();
                    c.dataInDatabaseProcess(dataFileAttribute.getSqlSessionFactory(),dataFileAttribute.getMapper(),dataFileAttribute.getInsertMethod());
                }catch (DataHandlerException e) {
                    try {
                        FileUtils.copyToDirectory(file,new File(dataFileAttribute.getFileErrorDir()));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }finally {
                    try {
                        FileUtils.copyToDirectory(file,new File(dataFileAttribute.getFileBakDir()));
                        FileUtils.deleteQuietly(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    private FileHandler getFileHandler(File file) {
            return FileHandlerFactory.getFileHandler(file.getName());
    }

    /**
     * 获取自定义处理类实体
     * @param fileHandleClazz
     * @return
     */
    private AbstractFileHandleProcessor getHandleInstance(Class fileHandleClazz) throws DataHandlerException {
        try {
            return (AbstractFileHandleProcessor) fileHandleClazz.newInstance();
        } catch (InstantiationException e) {
            throw new DataHandlerException(e);
        } catch (IllegalAccessException e) {
            throw new DataHandlerException(e);
        }
    }


    /**
     * 获取文件夹下 所有文件
     * @param filePath 文件夹路径
     */
    private List<File> getAllFiles(String filePath) {
        List<File> fileList = new ArrayList<File>();
        File file = new File(filePath);
        if(!file.isDirectory()) {
            logger.info("该文件目录不是文件夹目录");
            return fileList;
        }else{
            File[] innerFiles = file.listFiles();
            for (int i = 0; i < innerFiles.length; i++) {
                if(innerFiles[i].isDirectory()) {
                    getAllFiles(innerFiles[i].getPath());
                }else{
                    fileList.add(innerFiles[i]);
                }
            }
        }
        return fileList;
    }
}
