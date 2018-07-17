package DataHandlerTool.management;

import DataHandlerTool.exception.DataHandlerException;
import DataHandlerTool.filehandler.FileHandler;
import DataHandlerTool.management.annotation.Column;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义数据处理类的父类
 */
public abstract class AbstractFileHandleProcessor<C> implements FileHandleProcessor{

    //日志
    Logger logger = LogManager.getLogger(getClass());

    //表头索引列
    private List<Integer> headIndexs = new ArrayList<Integer>();

    //正则校验集合
    private Map<Integer,String> regxMap = new HashMap<Integer, String>();

    //数据bean set方法集合
    private Map<Integer,Method> headMethod = new HashMap<Integer,Method>();
    //读取数据结果集
    List<C> beanList = new ArrayList<C>();

    @Override
    public void generate(FileHandler fileHandler) throws DataHandlerException {
        fileHandler.process();
       List<List<String>> dataList = fileHandler.getDataList();
       if(dataList.size() <= 0) {
           logger.info("该文件无法获取数据,请检查数据正确性。");
           return;
       }
        try {
            String superClazzStr  = getClass().getGenericSuperclass().getTypeName();
            String clazzName = superClazzStr.substring(superClazzStr.indexOf("<")+1,superClazzStr.indexOf(">"));
            Class clazz = Class.forName(clazzName);
            initHeadDom(dataList.get(0),clazz);
            assembleData(dataList,clazz);
        } catch (ClassNotFoundException e) {
            logger.info("加载自定义bean时出错");
        }

    }

    /**
     * 根据初始化的表头索引，正则装载数据bean集合
     * @param dataList
     * @param clazz
     */
    private void assembleData(List<List<String>> dataList, Class clazz) {
        int rowNum = 0;
        try {
        for (int i = 1; i < dataList.size(); i++) {
            rowNum = i;
            List<String> tempList = dataList.get(i);
               //初始化是自定义数据bean
               C bean = (C) clazz.newInstance();
               //根据表头索引加载bean字段
               for(Integer index : headIndexs) {
                   String value = tempList.get(index);
                   String regxStr = regxMap.get(index);
                   if(null== value) {
                       value = "";
                   }
                   /**
                    * 进行字段校验
                    */
                   if(null != regxStr) {
                       Pattern pattern = Pattern.compile(regxStr);
                       Matcher m = pattern.matcher(value);
                       String filterStr = "";  //初始化校验后的存储的字符串变量
                       while(m.find()) {
                           filterStr= m.group();
                       }
                       value = filterStr;  //如果没有校验结果 则value为空
                   }
                   Method method = headMethod.get(index);
                   if(null != method) {
                       method.invoke(bean,value);
                   }
               }
               beanList.add(bean);
            }
        } catch (InstantiationException e) {
            logger.info("数据在第" + rowNum + "加载bean出问题",e);
        } catch (IllegalAccessException e) {
            logger.info("数据在第" + rowNum + "加载bean是出问题",e);
        } catch (InvocationTargetException e) {
            logger.info("数据在第" + rowNum + "加载bean是出问题",e);
        }
    }

    /**
     * 初始化读取数据的表头对应的配置
     * @param headList
     * @param clazz
     */
    private void initHeadDom(List<String> headList, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getMethods();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            int index = -1;
            String headStrs = column.name();
            //匹配表头 获取表头在数据中的位置
            if (null != headStrs && headStrs.length() > 0) {
                for (String headStr : headStrs.split(",")) {
                    index = headList.indexOf(headStr);
                    if (index != -1) {
                        headIndexs.add(index);
                        break;
                    }
                }
            }
            if (index != -1 && null == regxMap.get(index) && column.regex().length() > 0) {
                regxMap.put(index,column.regex());
            } else {
                logger.info("该表头没有正则校验配置");
            }

            //初始化 字段set方法
            String fieldName = field.getName();
            StringBuilder getMethodName = new StringBuilder();
            getMethodName.append("set").append(fieldName.substring(0, 1).toUpperCase());
            getMethodName.append(fieldName.substring(1));
            for (Method method : methods) {
                if (getMethodName.toString().equals(method.getName())) {
                    headMethod.put(index, method);
                    break;
                }
            }
        }
    }


    @Override
    public void dataInDatabaseProcess(SqlSessionFactory sqlSessionFactory,String mapperStr) throws DataHandlerException {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        Method insert = null;
        Object mapper =null;
        try {
            mapper = session.getMapper(Class.forName(mapperStr));
            Method[] methods = mapper.getClass().getMethods();
            for (Method method : methods) {
                if(method.getName().equals("insert")) {
                    insert = method;
                    break;
                }
            }
            }catch (ClassNotFoundException e) {
                logger.error("没有匹配到对应的Mapper接口..");
                e.printStackTrace();
            }
            if(insert == null || mapper == null) {
                logger.info("无法找到对应的mapper中的insert方法");
                throw new DataHandlerException("无法找到对应的mapper中的insert方法");
            }
            logger.info("启用 mybatis 入库 总数据:" + beanList.size());
            try {
                for (int i = 0; i < beanList.size(); i++) {
                    insert.invoke(mapper, beanList.get(i));
                    if (i % 500 == 0 && i != 0) {
                        session.commit();
                        session.clearCache();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("入库失败，开始回滚......");
                session.rollback();
                logger.error("回滚完成.........");
                throw new DataHandlerException("数据入库失败");
            } finally {
                session.commit();
                session.clearCache();
                session.close();
            }

        }


        @Override
        public void selfDefinedHandle () {

        }


    public List<C> getBeanList() {
        return beanList;
    }
}
