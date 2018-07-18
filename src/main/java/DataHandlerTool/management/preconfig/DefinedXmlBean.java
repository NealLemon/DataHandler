package DataHandlerTool.management.preconfig;


import DataHandlerTool.management.constant.ConfigEnum;

/**
 * 自定义配置xmlBean
 * @author Neal
 */
public class DefinedXmlBean {

    //模块名 唯一
    private String modelName;

    //文件所在目录
    private String fileDir;

    //文件备份目录
    private String fileBakDir;

    //出错文件存储目录
    private String fileErrorDir;

    //继承的数据处理处理类
    private String clazz;

    //数据库ID
    private String databaseId;

    //字符集编码
    private String charsetName;

    //对应的mapper接口
    private String mapper;

    //执行插入的方法名
    private String insertMethod = ConfigEnum.DEFAULT_INSERT_METHOD.getStr();

    public String getInsertMethod() {
        return insertMethod;
    }

    public void setInsertMethod(String insertMethod) {
        this.insertMethod = insertMethod;
    }

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

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
