package DataHandlerTool.management.preconfig;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义配置装载类
 */
@XmlRootElement(name = "configuration")
public class DefinedConfigBean {

    List<DefinedXmlBean> dataConfig = new ArrayList<DefinedXmlBean>();

    public List<DefinedXmlBean> getDataConfig() {
        return dataConfig;
    }

    public void setDataConfig(List<DefinedXmlBean> dataConfig) {
        this.dataConfig = dataConfig;
    }
}
