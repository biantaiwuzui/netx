import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>
 * 代码生成器
 * </p>
 */
public class UcenterMapperGenerator {
    static TabelBuild[] tabelBuilds = build();
    static class TabelBuild{
        public String[] tabelNames;
        public String type;
        public TabelBuild(String[] tabelNames, String type) {
            this.tabelNames = tabelNames;
            this.type = type;
        }
    }
    public static TabelBuild[] build(){
        TabelBuild[] tabelBuilds = new TabelBuild[3];
        tabelBuilds[0] = new TabelBuild(new String[]{"common_add_friend","common_arbitration_result",
                "common_area","common_article_limit","common_bill","common_cost_setting",
                "common_deposit_bill","common_evaluate","common_examine_finance","common_group_chat",
                "common_im_history","common_job","common_jpush_message","common_lucky_money",
                "common_manage_arbitration","common_other_set","common_receivables_order",
                "common_sensitive","common_sensitive_suggest","common_suggestion","common_tags",
                "common_wallet","common_wallet_frozen"},"common");
        tabelBuilds[1] = new TabelBuild(new String[]{"user","user_article","user_article_likes",
                "user_blacklist","user_blacklist_log","user_contribution","user_credit",
                "user_education","user_income","user_interest","user_login_history",
                "user_oauth","user_photo","user_profession","user_profile","user_score",
                "user_system_blacklist","user_value","user_verification_code","user_verify",
                "user_verify_credit","user_verify_resource","user_watch"},"user");
        tabelBuilds[2] = new TabelBuild(new String[]{"common_friends"},"friend");
        return tabelBuilds;
    }

    @Test
    public void main() {
        for (TabelBuild tabelBuild : tabelBuilds){
            build(tabelBuild.tabelNames,tabelBuild.type);
        }
    }

    @Test
    public void test(){
        build(new String[]{"common_im_history"},tabelBuilds[0].type);
    }

    /**
     * java类生成器
     * @param tables
     * @param type
     */
    public static void build(String[] tables,String type) {
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("deleted", FieldFill.INSERT));
        tableFillList.add(new TableFill("create_time", FieldFill.INSERT));
        tableFillList.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));

        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor("黎子安");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        gc.setServiceName("%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("wz");
        dsc.setPassword("WzJh6612520");
        dsc.setUrl("jdbc:mysql://112.74.200.88:3306/netx?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setTableFillList(tableFillList);
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(new String[]{""});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(tables); // 需要生成的表
        //strategy.setLogicDeleteFieldName("delTag");
        //strategy.setVersionFieldName("lockVersion");
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
//         strategy.setSuperEntityClass("com.worth.ucenter.redis.config.XX");
        // 自定义实体，公共字段
//         strategy.setSuperEntityColumns(new String[] { "createTime", "createUser", "updateTime", "updateUser", "delTag" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public user setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.netx.ucenter");
        //pc.setModuleName("testUser");
        pc.setEntity("model."+type);
        pc.setMapper("mapper."+type);
        pc.setXml("xml."+type);
        //pc.setService("receivablesOrder");
        //pc.setServiceImpl("receivablesOrder.impl");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        mpg.setCfg(cfg);


        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        // tc.setEntity("...");
        //tc.setMapper(null);
        // tc.setXml("...");
        tc.setService(null);
        tc.setServiceImpl(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

        // 打印注入设置【可无】
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }

}
