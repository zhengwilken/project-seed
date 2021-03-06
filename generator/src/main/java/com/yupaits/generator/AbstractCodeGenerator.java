package com.yupaits.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yupaits.generator.GeneratorConsts.*;

/**
 * 代码生成器
 *
 * @author yupaits
 * @date 2018/10/15
 */
@Data
public abstract class AbstractCodeGenerator {

    private AutoGenerator mpg = new AutoGenerator();
    private GlobalConfig globalConfig = new GlobalConfig();
    private PackageConfig packageConfig = new PackageConfig();
    private StrategyConfig strategy = new StrategyConfig();

    private GeneratorConfig generatorConfig;

    /**
     * 控制台交互构造代码生成器配置
     * @param moduleName      模块名称，为空时需要在控制台手动输入
     * @param outputDir       文件输出路径，为空时使用默认路径
     */
    public AbstractCodeGenerator(String moduleName, String outputDir) {
        GeneratorConfig generatorConfig = new GeneratorConfig().setDbHost(DB_HOST)
                .setDbUsername(JDBC_USERNAME)
                .setDbPassword(JDBC_PASSWORD)
                .setDatabase(DATABASE_NAME)
                .setProjectPath(PROJECT_PATH)
                .setDeveloper(AUTHOR)
                .setModuleName(moduleName)
                .setTableName(scanner("表名"))
                .setRelatedTable(scannerBool(RELATED_TABLE_TIP))
                .setRestful(scannerBool(REST_CONTROLLER_STYLE_TIP))
                .setHasLogicDeleteField(scannerBool(DELETED_COLUMN_TIP))
                .setHasVersionField(scannerBool(VERSION_COLUMN_TIP));
        codeGenerator(generatorConfig, outputDir);
    }

    /**
     * web网页交互构造代码生成器配置
     */
    public AbstractCodeGenerator(GeneratorConfig generatorConfig) {
        codeGenerator(generatorConfig, null);
    }

    private void codeGenerator(GeneratorConfig generatorConfig, String outputDir) {
        packageConfig.setModuleName(generatorConfig.getModuleName());
        if (StringUtils.isNotBlank(outputDir)) {
            globalConfig.setOutputDir(outputDir);
        } else {
            globalConfig.setOutputDir(generatorConfig.getProjectPath() + "/server" + JAVA_PATH);
        }
        this.generatorConfig = generatorConfig;
    }

    private void init() {
        //全局配置
        globalConfig.setAuthor(generatorConfig.getDeveloper());
        //设置不覆盖已存在的文件，如需更改已存在的文件请手动进行
        globalConfig.setFileOverride(false);
        globalConfig.setOpen(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setSwagger2(true);
        globalConfig.setActiveRecord(true);
        globalConfig.setEnableCache(true);
        globalConfig.setIdType(IdType.ID_WORKER);
        globalConfig.setDateType(DateType.TIME_PACK);
        mpg.setGlobalConfig(globalConfig);

        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName(JDBC_DRIVER_CLASS_NAME);
        dsc.setUrl(String.format(JDBC_URL_PATTERN, generatorConfig.getDbHost(), generatorConfig.getDatabase()));
        dsc.setUsername(generatorConfig.getDbUsername());
        dsc.setPassword(generatorConfig.getDbPassword());
        mpg.setDataSource(dsc);

        //包配置
        packageConfig.setParent(BASE_PACKAGE);
        mpg.setPackageInfo(packageConfig);

        //策略配置
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setInclude(generatorConfig.getTableName());
        strategy.setTableFillList(Lists.newArrayList(
                new TableFill("created_at", FieldFill.INSERT),
                new TableFill("created_by", FieldFill.INSERT),
                new TableFill(DELETED_FIELD_NAME, FieldFill.INSERT),
                new TableFill("updated_at", FieldFill.INSERT_UPDATE),
                new TableFill("updated_by", FieldFill.INSERT_UPDATE)
        ));
        if (generatorConfig.getHasLogicDeleteField()) {
            strategy.setLogicDeleteFieldName(DELETED_FIELD_NAME);
        }
        if (generatorConfig.getHasVersionField()) {
            strategy.setVersionFieldName(VERSION_FIELD_NAME);
        }
        strategy.setRestControllerStyle(generatorConfig.isRestful());
        strategy.setTablePrefix(packageConfig.getModuleName() + "_");
        mpg.setStrategy(strategy);

        //注入自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                TableInfo tableInfo = this.getConfig().getTableInfoList().get(0);
                Map<String, Object> map = Maps.newHashMap();
                Map<String, String> classMap = Maps.newHashMap();
                Map<String, String> classNameMap = Maps.newHashMap();
                Map<String, String> packageMap = Maps.newHashMap();
                Map<String, String> objMap = Maps.newHashMap();
                classMap.put("result", RESULT_CLASS);
                classMap.put("resultWrapper", RESULT_WRAPPER_CLASS);
                classMap.put("modelWrapper", MODEL_WRAPPER_CLASS);
                classMap.put("resultCode", RESULT_CODE_CLASS);
                classMap.put("superDto", BASE_DTO_CLASS);
                classMap.put("serializer", SERIALIZER_CLASS);
                classMap.put("deserializer", DESERIALIZER_CLASS);
                classMap.put("validateUtils", VALIDATE_UTILS_CLASS);
                classMap.put("foreignId", FOREIGN_ID_CLASS);
                classMap.put("relatedId", RELATED_ID_CLASS);
                classMap.put("serviceException", SERVICE_EXCEPTION_CLASS);
                classNameMap.put("superDto", className(BASE_DTO_CLASS));
                classNameMap.put("serializer", className(SERIALIZER_CLASS));
                classNameMap.put("deserializer", className(DESERIALIZER_CLASS));
                classNameMap.put("create", tableInfo.getEntityName() + "Create");
                classNameMap.put("update", tableInfo.getEntityName() + "Update");
                classNameMap.put("vo", tableInfo.getEntityName() + "VO");
                packageMap.put("dto", BASE_PACKAGE + "." + packageConfig.getModuleName() + ".dto");
                packageMap.put("vo", BASE_PACKAGE + "." + packageConfig.getModuleName() + ".vo");
                objMap.put("service", beanName(tableInfo.getEntityName(), "Service"));
                objMap.put("create", beanName(tableInfo.getEntityName(), "Create"));
                objMap.put("update", beanName(tableInfo.getEntityName(), "Update"));
                objMap.put("vo", beanName(tableInfo.getEntityName(), "VO"));
                objMap.put("entity", beanName(tableInfo.getEntityName(), ""));
                map.put("class", classMap);
                map.put("className", classNameMap);
                map.put("package", packageMap);
                map.put("obj", objMap);
                this.setMap(map);
            }
        };
        //自定义文件生成配置
        List<FileOutConfig> focList = Lists.newArrayList();
        //生成Mapper.xml文件
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return generatorConfig.getProjectPath() + "/server" + MAPPER_XML_PATH + packageConfig.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        if (!generatorConfig.isRelatedTable()) {
            //生成DTO Create类
            focList.add(new FileOutConfig("/templates/dto.create.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    cfg.getMap().put("notRelateTable", true);
                    cfg.getMap().put("fieldPackages", dtoFieldPackages(tableInfo));
                    cfg.getMap().put("hasDeserializer", hasDeserializer(tableInfo.getFields()));
                    return generatorConfig.getProjectPath() + "/server" + JAVA_PATH + BASE_PACKAGE_PATH + packageConfig.getModuleName()
                            + "/dto/" + tableInfo.getEntityName() + "Create" + StringPool.DOT_JAVA;
                }
            });
            //生成DTO Update类
            focList.add(new FileOutConfig("/templates/dto.update.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    cfg.getMap().put("fieldPackages", dtoFieldPackages(tableInfo));
                    return generatorConfig.getProjectPath() + "/server" + JAVA_PATH + BASE_PACKAGE_PATH + packageConfig.getModuleName()
                            + "/dto/" + tableInfo.getEntityName() + "Update" + StringPool.DOT_JAVA;
                }
            });
        }
        //生成VO类
        focList.add(new FileOutConfig("/templates/vo.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                cfg.getMap().put("fieldPackages", voFieldPackages(tableInfo));
                return generatorConfig.getProjectPath() + "/server" + JAVA_PATH + BASE_PACKAGE_PATH + packageConfig.getModuleName()
                        + "/vo/" + tableInfo.getEntityName() + "VO" + StringPool.DOT_JAVA;
            }
        });
        //非RestController时生成Thymeleaf的html模板文件
        if (!strategy.isRestControllerStyle()) {
            focList.add(new FileOutConfig("/templates/html.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    //noinspection unchecked
                    String fileName = MapUtils.getString((Map<String, String>) MapUtils.getMap(cfg.getMap(),"obj"),
                            "entity") + ".html";
                    return generatorConfig.getProjectPath() + "/server" + HTML_TEMPLATES_PATH + packageConfig.getModuleName()
                            + "/" + fileName;
                }
            });
        }
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        //设置文件模板
        TemplateConfig templateConfig = new TemplateConfig().setXml(null);
        mpg.setTemplate(templateConfig);
        //设置文件模板引擎
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
    }

    /**
     * 根据类的路径获取类名
     */
    private String className(String clazz) {
        return StringUtils.substringAfterLast(clazz, StringPool.DOT);
    }

    /**
     * 使用EntityName和Bean的后缀生成Bean的名称
     */
    private String beanName(String entityName, String suffix) {
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.firstToLowerCase(entityName) + suffix;
    }

    /**
     * 获取DTO类需要导入的Field类型Package路径
     */
    private Set<String> dtoFieldPackages(TableInfo tableInfo) {
        boolean hasDateTimeType = false;
        for (TableField tableField : tableInfo.getFields()) {
            if (StringUtils.equals(tableField.getType(), FIELD_DATETIME_TYPE)
                    && !StringUtils.equalsAny(tableField.getName(), DATETIME_TYPE_IGNORED_FIELDS)) {
                hasDateTimeType = true;
                break;
            }
        }
        boolean finalHasDateTimeType = hasDateTimeType;
        return tableInfo.getImportPackages().stream().filter(pkg ->
                !StringUtils.startsWith(pkg, MYBATIS_PLUS_PACKAGE_PREFIX)
                && !StringUtils.equals(pkg, Serializable.class.getCanonicalName())
                && (finalHasDateTimeType || !StringUtils.equals(pkg, LocalDateTime.class.getCanonicalName()))
        ).collect(Collectors.toSet());
    }

    /**
     * 获取VO类需要导入的Field类型Package路径
     */
    private Set<String> voFieldPackages(TableInfo tableInfo) {
        return tableInfo.getImportPackages().stream().filter(pkg ->
                !StringUtils.startsWith(pkg, MYBATIS_PLUS_PACKAGE_PREFIX)
                && !StringUtils.equals(pkg, Serializable.class.getCanonicalName())
        ).collect(Collectors.toSet());
    }

    /**
     * 判断CreateDTO类是否需要导入Deserializer相关的类
     */
    private boolean hasDeserializer(List<TableField> tableFields) {
        for (TableField tableField : tableFields) {
            if (!tableField.isKeyFlag() && tableField.getFill() == null
                    && StringUtils.equals(tableField.getPropertyType(), "Long")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 读取控制台内容
     * @param tip 控制台输入提示
     * @return 读取到的内容
     */
    protected static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入").append(tip).append("：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 读取控制台输入的bool值
     * @param tip 控制台提示输入
     * @return 读取到的值
     */
    private boolean scannerBool(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请选择").append(tip).append("(是: y, 否: 其他输入)?");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            return StringUtils.isNotEmpty(ipt) && TRUE_VAL.equals(ipt);
        }
        return false;
    }

    /**
     * 生成
     */
    public void generate() {
        init();
        mpg.execute();
    }
}
