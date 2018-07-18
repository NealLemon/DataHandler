package DataHandlerTool.management.constant;

public enum ConfigEnum {

    DEFAULT_INSERT_METHOD("insert"),
    DEFINED_XML("defined-config.xml"),
    MYBATIS_CONFIG_XML("mybatis-config.xml"),
    XLS(".xls"),
    XLSX(".xlsx"),
    CSV(".csv");

    private String str;


    ConfigEnum(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
