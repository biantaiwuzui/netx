package com.netx.utils.uri;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * URLBroker工厂类
 *
 * @author Tiger
 * @version 1.0
 * @since 2011-4-22 tiger
 * @update runshine 2014-10-22
 */
public class URLBrokerFactory {

    private Log logger = LogFactory.getLog(URLBrokerFactory.class);
    /**
     * 如果uri配置中的serverUrl没有分组，或者group没有命名，则放置在此命名下
     */
    private static final String DEFAULT_MODE = "__DEFAULT_MODE__";
    /**
     * modeConfig中作为全局默认设置的key，如果没有设置一个group对应使用的id，则默认使用default这个key设置的id
     * 如果default这个key设置的id在group下没有对应的id，则使用DEFAULT_MODE组的设置
     */
    private static final String DEFAULT_CONF = "default";
    /**
     * url.xml对应的文件路径
     */
    private String urlConfigName;
    /**
     * url-mode.properties对应的文件路径
     */
    private String modeConfigProperties;
    /**
     * 默认使用的URLBroker实现，可以使用配置类完全限定名的方式注入使用其它实现
     */
    private Class<? extends URLBroker> URLBrokerClass = DefaultURLBroker.class;
    /**
     * 版本号
     */
    private Long version = System.currentTimeMillis();
    /**
     * url-mode.properties对应的配置
     */
    private Properties configProperties = new Properties();
    /**
     * 可以根据分组设置切换的Broker，规则是Map[url name,group name,URLBroker]
     */
    private Map<String, ? extends Map<String, URLBroker>> nameModeUrlBrokers = new HashMap<>(0);

    private Timer configPropertiesTimer;
    /**
     * 手工切换分组时最大等待时间 单位：秒
     */
    private long cycle = 0;
    private long lastModified = -111;

    public void init() {
        if (StringUtils.isBlank(urlConfigName)) {
            throw new RuntimeException("没有配置url.xml");
        }

        ClassPathResource resource = new ClassPathResource(urlConfigName);

        //必须至少执行一次,定时更新modeConfig,如果周期时间小于等于0，则表示永不更新.
        //如果modeConfigProperties未定义，同样会永不更新。但是如果modeConfigProperties定义了，即使读取不到依然会定时更新(保证能读取到时能够更新).
        if (processModeConfig() && cycle > 0) {
            configPropertiesTimer = new Timer("URLBrokerFactory-ModeConfig-Timer", true);
            configPropertiesTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    processModeConfig();
                }
            }, cycle * 1000, cycle * 1000);
        }

        try {
            nameModeUrlBrokers = initUrlBroker(resource.getInputStream());
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void destroy() {
        if (configPropertiesTimer != null) {
            configPropertiesTimer.cancel();
        }
    }

    //modeConfig读取，没有变化则不更新.倘若未定义modeConfigProperties,则返回false.倘若定义了modeConfigProperties，无论是否存在都会返回true.
    private boolean processModeConfig() {
        if (StringUtils.isNotBlank(modeConfigProperties)) {
            ClassPathResource configResource = new ClassPathResource(modeConfigProperties);
            if (configResource.exists()) {
                try {
                    long modified = configResource.lastModified();
                    if (lastModified != modified) {
                        configProperties = PropertiesLoaderUtils.loadProperties(configResource);
                        lastModified = modified;
                    }
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            return true;
        }
        return false;
    }

    private Map<String, ? extends Map<String, URLBroker>> initUrlBroker(InputStream inputStream) throws InstantiationException, IllegalAccessException {
        HashMap<String, HashMap<String, URLBroker>> nameModeUrlBrokers = new HashMap<>();  //按名称与模式分组的URLBroker

        SAXReader reader = new SAXReader();
        try {
            Document root = reader.read(inputStream);
            @SuppressWarnings("unchecked")
            List<Node> urls = (List<Node>)root.selectNodes("/urlConfig/url");
            for (Node url : urls) {

                String name = url.valueOf("@name");

                // path默认是空字符串
                String path = "";
                Node pathNode = url.selectSingleNode("path");
                if (pathNode != null) {
                    path = pathNode.getText().trim();
                }
                @SuppressWarnings("unchecked")
                List<Node> tokens = (List<Node>)url.selectNodes("tokens/token");
                List<Node> serverGroupNode = url.selectNodes("serverGroup");

                if (serverGroupNode == null || serverGroupNode.isEmpty()) {                  //兼容老的配置，读取一个url下单个存在的serverUrl
                    Node singleServerUrlNode = url.selectSingleNode("serverUrl");            //如果一个url下单个serverUrl与group同时存在，则忽略单个的serverUrl
                    if (singleServerUrlNode == null) {
                        throw new RuntimeException("服务器URL必须配置");
                    } else {
                        String text = singleServerUrlNode.getText();
                        if (StringUtils.isNotBlank(text)) {
                            URLBroker urlBroker = URLBrokerClass.newInstance();
                            urlBroker.addServerUrl(text.trim());
                            urlBroker.setPath(path);

                            HashMap<String, URLBroker> urlBrokers = new HashMap<>();          //按模式分组的URLBroker
                            urlBrokers.put(DEFAULT_MODE, urlBroker);
                            nameModeUrlBrokers.put(name, urlBrokers);
                        }
                    }
                } else {                                                                      //读取划分group存在的serverUrl
                    HashMap<String, URLBroker> urlBrokers = new HashMap<>();                  //按模式分组的URLBroker
                    for (Node aServerGroup : serverGroupNode) {
                        List<Node> groupServerUrlNode = aServerGroup.selectNodes("serverUrl");

                        if (groupServerUrlNode == null || groupServerUrlNode.isEmpty()) {
                            throw new RuntimeException("若使用服务器组,其中的服务器URL必须配置");
                        }

                        String groupName = DEFAULT_MODE;
                        Node id = aServerGroup.selectSingleNode("id");
                        if (id != null) {
                            String idString = id.getText();
                            if (StringUtils.isNotBlank(idString)) {
                                groupName = idString.trim();
                            }
                        }

                        URLBroker urlBroker = URLBrokerClass.newInstance();
                        for (Node serverUrl : groupServerUrlNode) {
                            String text = serverUrl.getText();
                            if (StringUtils.isNotBlank(text)) {
                                urlBroker.addServerUrl(text.trim());
                            }
                        }
                        urlBroker.setPath(path);

                        if (tokens != null && !tokens.isEmpty()) {
                            for (Node token : tokens) {
                                urlBroker.addToken(token.valueOf("@name"));
                            }
                        }

                        urlBrokers.put(groupName, urlBroker);
                    }
                    nameModeUrlBrokers.put(name, urlBrokers);
                }

            }
        } catch (DocumentException e) {
            logger.error("解析url.xml时发现异常:" + e);
            logger.error(e);
        }
        return nameModeUrlBrokers;
    }

    public URLBroker getUrl(String name) {
        String config = (String)configProperties.get(name);
        Map<String, URLBroker> urlBrokers = nameModeUrlBrokers.get(name);

        if (StringUtils.isBlank(config)) {
            config = (String)configProperties.get(DEFAULT_CONF);
        }

        URLBroker urlBroker = urlBrokers.get(config);
        if (urlBroker == null) {
            URLBroker urlBrokerDef = urlBrokers.get(DEFAULT_MODE);
            if (urlBrokerDef == null) {
                return null;
            } else {
                return urlBrokerDef.getInstance();
            }
        } else {
            return urlBroker.getInstance();
        }
    }

    /**
     * @param urlConfigName the urlConfigName to set
     */
    public void setUrlConfigName(String urlConfigName) {
        this.urlConfigName = urlConfigName;
    }

    public void setModeConfigProperties(String modeConfigProperties) {
        this.modeConfigProperties = modeConfigProperties;
    }

    public void setURLBrokerClass(String URLBrokerClassName) throws ClassNotFoundException {
        this.URLBrokerClass = (Class<? extends URLBroker>)Class.forName(URLBrokerClassName);
    }

    /**
     * 手工切换分组时最大等待时间 单位：秒
     */
    public void setCycle(long cycle) {
        this.cycle = cycle;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
