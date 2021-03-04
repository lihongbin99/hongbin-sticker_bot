package io.lihongbin;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    public static void main(String[] args) {

        // 数据库原配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbQuery(new MySqlQuery());// 配置表数据查询
        dataSourceConfig.setDbType(DbType.MYSQL);// 配置Mysql数据库
        dataSourceConfig.setSchemaName("public");// 配置数据库schema名
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert());// 数据库字段类型转换
        dataSourceConfig.setUrl("jdbc:mysql://127.0.0.1:3306/sticker_bot");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");
        dataSourceConfig.setKeyWordsHandler(new MySqlKeyWordsHandler());// 设置关键字处理

        // 数据库表配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(false);// 是否大写命名
        strategyConfig.setSkipView(false);// 是否跳过视图
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);// 驼峰命名
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);// 驼峰命名
//        strategyConfig.setFieldPrefix("");// 字段前缀
//        strategyConfig.setSuperEntityClass(Object.class);// 实体类的父类
//        strategyConfig.setSuperEntityColumns("");// 实体类的公共字段
        strategyConfig.setSuperMapperClass(ConstVal.SUPER_MAPPER_CLASS);// Mapper的父类
        strategyConfig.setSuperServiceClass(ConstVal.SUPER_SERVICE_CLASS);// Service的父类
        strategyConfig.setSuperServiceImplClass(ConstVal.SUPER_SERVICE_IMPL_CLASS);// ServiceImpl的父类
//        strategyConfig.setSuperControllerClass(Object.class);// Controller的父类
        strategyConfig.setEnableSqlFilter(true);// 默认激活进行sql模糊表名匹配
        strategyConfig.setInclude("sticker_set", "sticker");// 需要包含的表名
//        strategyConfig.setLikeTable(new LikeTable(""));// 模糊匹配表名
//        strategyConfig.setNotLikeTable(new LikeTable(""));// 模糊排除表名
//        strategyConfig.setExclude("admin_role_permission");// 需要排除表明
        strategyConfig.setEntityColumnConstant(false);// 是否生成字段常量(默认 false)
        strategyConfig.setChainModel(false);// 是否为链式模型(默认 false)
        strategyConfig.setEntityLombokModel(true);// 是否为lombok模型(默认 false)
        strategyConfig.setEntityBooleanColumnRemoveIsPrefix(false);// Boolean类型字段是否移除is前缀(默认 false)
        strategyConfig.setRestControllerStyle(true);// 生成 @RestController 控制器
        strategyConfig.setControllerMappingHyphenStyle(false);// 驼峰转连字符
        strategyConfig.setEntityTableFieldAnnotationEnable(true);// 是否生成实体时，生成字段注解
//        strategyConfig.setVersionFieldName("");// 乐观锁属性名称
//        strategyConfig.setLogicDeleteFieldName("deleted");// 逻辑删除属性名称
//        strategyConfig.setTableFillList(null);// 表填充字段

        // 包名配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("io.lihongbin");// 父包名
//        packageConfig.setModuleName("ruoyi-admin");// 模块名
        packageConfig.setEntity("entity");// Entity包名
        packageConfig.setService("service");// Service包名
        packageConfig.setServiceImpl("service.impl");// Service Impl包名
        packageConfig.setMapper("mapper");// Mapper包名
        packageConfig.setXml("mapper");// Mapper XML包名
        packageConfig.setController("controller");// Controller包名
        packageConfig.setPathInfo(null);// 路径配置信息

        // 模板配置
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity(ConstVal.TEMPLATE_ENTITY_JAVA);// Java 实体类模板
        templateConfig.setEntityKt(ConstVal.TEMPLATE_ENTITY_KT);// Kotin 实体类模板
        templateConfig.setService(ConstVal.TEMPLATE_SERVICE);// Service 类模板
        templateConfig.setServiceImpl(ConstVal.TEMPLATE_SERVICE_IMPL);// Service impl 实现类模板
        templateConfig.setMapper(ConstVal.TEMPLATE_MAPPER);// mapper 模板
        templateConfig.setXml(ConstVal.TEMPLATE_XML);// mapper xml 模板
        templateConfig.setController(ConstVal.TEMPLATE_CONTROLLER);// controller 控制器模板

        // 全局策略配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");// 生成文件的输出目录
        globalConfig.setFileOverride(false);// 是否覆盖已有文件
        globalConfig.setOpen(false);// 是否打开输出目录
        globalConfig.setEnableCache(false);// 是否在xml中添加二级缓存配置
        globalConfig.setAuthor("Li Hong Bin");// 开发人员
        globalConfig.setKotlin(false);// 开启 Kotlin 模式
        globalConfig.setSwagger2(false);// 开启 swagger2 模式
        globalConfig.setActiveRecord(false);// 开启 ActiveRecord 模式
        globalConfig.setBaseResultMap(false);// 开启 BaseResultMap
        globalConfig.setBaseColumnList(false);// 开启 baseColumnList
        globalConfig.setDateType(DateType.TIME_PACK);// 时间类型对应策略
//        globalConfig.setEntityName("%sEntity");// 实体命名方式
        globalConfig.setServiceName("%sService");// 实体命名方式
        globalConfig.setServiceImplName("%sServiceImpl");// 实体命名方式
        globalConfig.setMapperName("%sMapper");// 实体命名方式
        globalConfig.setXmlName("%sMapper");
        globalConfig.setControllerName("%sController");// 实体命名方式
        globalConfig.setIdType(IdType.AUTO);// 指定生成的主键的ID类型

        // 个性化配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // 个性化配置
            }
        };

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        mpg.setDataSource(dataSourceConfig);
        mpg.setStrategy(strategyConfig);
        mpg.setPackageInfo(packageConfig);
        mpg.setTemplate(templateConfig);
        mpg.setGlobalConfig(globalConfig);
        mpg.setCfg(injectionConfig);

        mpg.execute();

    }

}
