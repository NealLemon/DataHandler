package DataHandlerTool;

import DataHandlerTool.management.DataProcessorImp;
import DataHandlerTool.management.InitConfiguration;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        InitConfiguration initConfiguration = new InitConfiguration();
        DataProcessorImp dataProcessorImp = new DataProcessorImp();
        dataProcessorImp.generate(initConfiguration.attributeList);
    }
}
