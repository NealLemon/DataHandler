package DataHandlerTool.management.preconfig;

import DataHandlerTool.exception.initConfigException;
import DataHandlerTool.management.FileHandleProcessor;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 文件处理属性
 */
public class DataFileAttribute {

    //模型名称
    private String modelName;

    //mybatis session工厂
    private SqlSessionFactory sqlSessionFactory;

    //自定义数据处理类
    private Class fileHandleClazz;

    //文件所在目录
    private String fileDir;

    //文件备份目录
    private String fileBakDir;

    //出错文件存储目录
    private String fileErrorDir;

    private String charsetName;

    private String mapper;

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) throws initConfigException {
        if(null == modelName || "".equals(modelName)) {
            throw new initConfigException("modelName 不能为空！");
        }
        this.modelName = modelName;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public Class getFileHandleClazz() {
        return fileHandleClazz;
    }

    public void setFileHandleClazz(Class fileHandleClazz) {
        this.fileHandleClazz = fileHandleClazz;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileBakDir() {
        return fileBakDir;
    }

    public void setFileBakDir(String fileBakDir) {
        this.fileBakDir = fileBakDir;
    }

    public String getFileErrorDir() {
        return fileErrorDir;
    }

    public void setFileErrorDir(String fileErrorDir) {
        this.fileErrorDir = fileErrorDir;
    }
}
