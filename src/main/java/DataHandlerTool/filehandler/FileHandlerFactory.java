package DataHandlerTool.filehandler;

import DataHandlerTool.management.constant.ConfigEnum;

public class FileHandlerFactory {

/*    private static CSV uniqueCsv;

    private static XLS2CSV uniqueXLS2CSV;

    private static XLSX2CSV uniqueXLSX2CSV;*/

    private FileHandlerFactory(){}

    public static FileHandler getFileHandler(String fileStr) {
        if(fileStr.endsWith(ConfigEnum.CSV.getStr())) {
/*            if(uniqueCsv == null) {
                uniqueCsv = new CSV();
            }*/
                return new CSV();
        }else if(fileStr.endsWith(ConfigEnum.XLS.getStr())) {
/*            if(uniqueXLS2CSV == null) {
                uniqueXLS2CSV = new XLS2CSV();
            }
            return uniqueXLS2CSV;*/
            return new XLS2CSV();
        }else if(fileStr.endsWith(ConfigEnum.XLSX.getStr())) {
/*            if(uniqueXLSX2CSV == null) {
                uniqueXLSX2CSV = new XLSX2CSV();
            }
            return uniqueXLSX2CSV;*/
            return new XLSX2CSV();
        }
        return null;
    }
}
