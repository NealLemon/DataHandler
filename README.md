# Simple DataHandler
## Description
This tool is particularly proficient at handling large files with `.csv`,`.xls`,`.xlsx` suffixes . It avoids the problem of memory overflow when using `POI` to read files.What's more,  it supports storing database which using `Mybatis` ORM as the default ORM Util.  Providing simple external interfaces for handling data,developers only need to configure the simple format XML file and extend the related Class to make the data read and storage easily and quickly.

这个工具 非常擅长处理以.csv,.xls,.xlsx  后缀的大文件，避免了 使用POI 读取文件时内存溢出的问题，
同时支持数据库入库操作，默认使用mybatis ORM。提供了简单的对外数据处理接口，
开发者只需要配置简单的XML文件相关属性以及继承相关的类，就可以简单快捷的读取数据并且入库。


## Example 
I have alread give an example in the project named Demo,You can develop in the same way.
* 1.create your own package.
  * 1.1 create Mapped SQL Statements xml eg.. DataHandlerTool/demo/config/StudentMapper.xml
  * 1.2 create entity eg.. DataHandlerTool.demo.domain.Student  ps.. Annotation - @Column(name="`mapped Excel column title`",regex = "`filter data with regex`")
  * 1.3 create interface mapper instance eg.. DataHandlerTool.demo.mapper.StudentMapper  ps ..the defalut using method is `insert` and the parame must be `Object`.
  * 1.4 create your handler class eg..DataHandlerTool.demo.handler.studentHandler   ps..you can do your own logic on method `selfDefinedHandle` and pay attention to Generics.
* 2.configure defined-config.xml . 
* 3.configure mybatis-config.xml.  ps.. configure in the mybatis offical way.

Any database supported by mybatis can be imported.


在项目中已经有一个Demo 示例,大家可以按照这个示例进行开发。
* 1.创建自己的包
  * 1.1创建映射sql语句的XML文件.  如.DataHandlerTool/demo/config/StudentMapper.xml
  * 1.2 创建实体类.   如.DataHandlerTool.demo.domain.Student   ps..注释 - @Column(name="`映射的表头`",regex = "`字段匹配的正则`")
  * 1.3创建 mapper接口 如.DataHandlerTool.demo.mapper.StudentMapper  注意 默认方法名是 `insert` 参数必须为 `Object`.
  * 1.4创建自己的处理类  如..DataHandlerTool.demo.handler.studentHandler  注意 你可以在`selfDefinedHandle`方法中执行 自己的数据处理逻辑。
* 2.配置 defined-config.xml 
* 3 配置 mybatis-config.xml 注意 .按照官方配置方法配置.


只要mybatis支持的数据库都可以进行导入。


##Multi-thread is not currently supported。  目前版本不支持多线程处理。

##后期会加入数据库之间的数据逻辑处理以及导出功能。
