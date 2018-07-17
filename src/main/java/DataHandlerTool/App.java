package DataHandlerTool;

import DataHandlerTool.management.DataProcessorImp;
import DataHandlerTool.management.InitConfiguration;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, ClassNotFoundException {
      InitConfiguration initConfiguration = new InitConfiguration();
        DataProcessorImp dataProcessorImp = new DataProcessorImp();
        dataProcessorImp.generate(initConfiguration.attributeList);

        //System.out.println(TestClass.class.getCanonicalName());


    }
}
