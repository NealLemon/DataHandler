package DataHandlerTool.management;

import DataHandlerTool.exception.initConfigException;
import DataHandlerTool.management.constant.ConfigEnum;
import DataHandlerTool.management.preconfig.DataFileAttribute;
import DataHandlerTool.management.preconfig.DefinedConfigBean;
import DataHandlerTool.management.preconfig.DefinedXmlBean;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class InitConfiguration {

    Logger logger = LogManager.getLogger(getClass());

    //处理文件属性集合
    public static List<DataFileAttribute> attributeList = new ArrayList<DataFileAttribute>();

    public InitConfiguration() {
        initDefinedXml();
    }

    /**
     * 读取自定义XML配置文件并解析
     */
    private void initDefinedXml() {
        logger.info("-------------读取初始化自定配置-----------");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DefinedConfigBean.class);
            FileReader fileReader = new FileReader(getClass().getResource("/").getPath() + ConfigEnum.DEFINED_XML.getStr());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            DefinedConfigBean definedConfigBean = (DefinedConfigBean) unmarshaller.unmarshal(fileReader);
            //分配XML 组装配置
            assembleConfig(definedConfigBean.getDataConfig());
        } catch (JAXBException e) {
           logger.error("自定义XML解析文件异常",e);
        } catch (FileNotFoundException e) {
            logger.error("自定义XML没有对应的文件",e);
        }
    }

    /**
     * 组装配置
     * @param definedConfigBeans  自定义配置bean 集合
     */
    private void assembleConfig(List<DefinedXmlBean> definedConfigBeans) {
        logger.info("-------------开始初始化自定配置-----------");
        try {
            //初始化各个自定义配置
            for (DefinedXmlBean definedConfigBean : definedConfigBeans) {
                DataFileAttribute dataFileAttribute = new DataFileAttribute();
                dataFileAttribute.setSqlSessionFactory(initSqlSessionFacotry(definedConfigBean.getDatabaseId()));
                dataFileAttribute.setModelName(definedConfigBean.getModelName());
                dataFileAttribute.setFileDir(definedConfigBean.getFileDir());
                dataFileAttribute.setFileBakDir(definedConfigBean.getFileBakDir());
                dataFileAttribute.setFileErrorDir(definedConfigBean.getFileErrorDir());
                dataFileAttribute.setFileHandleClazz(initFileHanleProcessor(definedConfigBean.getClazz()));
                dataFileAttribute.setCharsetName(definedConfigBean.getCharsetName());
                dataFileAttribute.setMapper(definedConfigBean.getMapper());
                attributeList.add(dataFileAttribute);
            }
        } catch (IOException e) {
            logger.error(e);
        } catch (initConfigException e) {
            logger.info(e);
        }
        logger.info("-------------初始化自定配置结束-----------");
    }


    /**
     * 定义自定义文件处理bean
     * @param
     * @return
     */
    private Class initFileHanleProcessor(String clazzStr) throws initConfigException {
        try {
            Class clazz = Class.forName(clazzStr);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new initConfigException(clazzStr + " 自定义数据处理类查找不到 ");
        }
    }

    /**
     * 初始化需要的mybatis session工厂
     * @param databaseId  数据库对应的ID
     *
     */
    private SqlSessionFactory initSqlSessionFacotry(String databaseId) throws IOException, initConfigException {
        InputStream inputStream = Resources.getResourceAsStream(ConfigEnum.MYBATIS_CONFIG_XML.getStr());
        SqlSessionFactory sqlSessionFactory =  new SqlSessionFactoryBuilder().build(inputStream,databaseId);
        if(sqlSessionFactory.getConfiguration().getEnvironment() == null) {
            throw new initConfigException("初始化Mybatis sqlSessionFactory时，没有找到对应的数据库,请检查自定义配置 databaseId 是否正确或者是否在Mybatis中配置是数据库环境");
        }
        return sqlSessionFactory;
    }


}
