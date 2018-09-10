package com.netx.utils.uri;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * 此类是线程不安全的
 * <p>
 * 一个URL包括如下几个部分
 * </p>
 * <pre>
 * URI = SERVER_URL + PATH + &quot;?&quot; + QUERY_DATA + &quot;#&quot; SERVER_URL = protocol://serverDomain:serverPort
 * PATH = /path/path QUERY_DATA = queryKey1=value1&amp;queryKey2=value2
 * </pre>
 * <p>
 * </p>
 * <pre>
 * http://www.zhiwen.com:8080/template/resource/100/?t=2011.04.22.css
 * </pre>
 *
 * @author Tiger
 * @version 1.0.0
 * @since 2011-4-22 下午02:56:40
 * @update runshine 2014-10-22
 */
public abstract class URLBroker {

    protected List<String> serverUrl = new ArrayList<>(4);

    protected String serverDomain;

    protected String serverPort;

    protected String protocol;

    protected String path;

    protected Map<String, Object> queryDatas = new HashMap<>();

    protected Set<String> tokens = new HashSet<>();

    private final List<String> serverPathUrl = new ArrayList<>(4);

    protected URLBroker(URLBroker urlBroker) {
        this.serverUrl = urlBroker.getServerUrl();
        this.path = urlBroker.getPath();
        this.serverDomain = urlBroker.getServerDomain();
        this.serverPort = urlBroker.getServerPort();
        this.protocol = urlBroker.getProtocol();
        this.tokens = urlBroker.getTokens();

        initServerPathUrl();
    }

    public URLBroker() {
    }

    private void initServerPathUrl() {
        if (serverUrl != null && !serverUrl.isEmpty()) {
            StringBuilder serverPathBuilder = new StringBuilder(50);
            for (String theUrl : serverUrl) {
                serverPathBuilder.append(theUrl);
                if (StringUtils.isNotBlank(path)) {
                    serverPathBuilder.append(path);
                }
                serverPathUrl.add(serverPathBuilder.toString());
                serverPathBuilder.delete(0, serverPathBuilder.length());
            }
        }
    }

    @Override
    public String toString() {
        String renderServerPathUrl = render();
        String renderQueryData = renderQueryData();
        boolean spuNotBlank = StringUtils.isNotBlank(renderServerPathUrl);
        boolean qdnotBlank = StringUtils.isNotBlank(renderQueryData);
        if (spuNotBlank && qdnotBlank) {
            return renderServerPathUrl + renderQueryData;
        } else if (spuNotBlank) {
            return renderServerPathUrl;
        } else if (qdnotBlank) {
            return renderQueryData;
        }
        return null;  //方便vm中排查问题
    }

    /*
     * 执行url渲染,并使返回域名尽量均匀
     */
    protected String render() {
        if (serverPathUrl != null && !serverPathUrl.isEmpty()) {
            int cs = (int)System.nanoTime();
            if (cs == Integer.MIN_VALUE) {
                cs = 0;
            } else if (cs < 0) {
                cs = (-cs);
            }
            int index = cs % serverPathUrl.size();
            String theUrl = serverPathUrl.get(index);

            return performTokens(theUrl);
        }
        return "";
    }

    private String performTokens(String serverUrl) {
        for (String token : tokens) {
            serverUrl = serverUrl.replaceAll("\\{" + token + "\\}", queryDatas.get(token) == null ? "" : String.valueOf(queryDatas.get(token)));
            queryDatas.remove(token);
        }
        return serverUrl;
    }

    private String renderQueryData() {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (String key : queryDatas.keySet()) {
            if (i == 0) {
                result.append("?");
            }
            if (i > 0) {
                result.append("&");
            }
            result.append(key);
            result.append("=");
            Object value = queryDatas.get(key);
            result.append(value == null ? "" : value);
            i++;
        }
        return result.toString();
    }

    public URLBroker addQueryData(String key, Object value) {
        this.queryDatas.put(key, value);
        return this;
    }

    public abstract URLBroker getInstance();

    public void addToken(String token) {
        tokens.add(token);
    }

    public Set<String> getTokens() {
        return new HashSet<>(tokens);
    }

    /**
     * @return the serverUrl
     */
    List<String> getServerUrl() {
        return new ArrayList<>(serverUrl);
    }

    /**
     * @param serverUrl the serverUrl to set
     */
    void addServerUrl(String serverUrl) {
        if (StringUtils.isNotBlank(serverUrl)) {
            if (serverUrl.endsWith("/")) {
                this.serverUrl.add(serverUrl.substring(0, serverUrl.length() - 1));
            } else {
                this.serverUrl.add(serverUrl);
            }
        }
    }

    /**
     * @return the serverDomain
     */
    String getServerDomain() {
        return serverDomain;
    }

    /**
     * @param serverDomain the serverDomain to set
     */
    void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }

    /**
     * @return the serverPort
     */
    String getServerPort() {
        return serverPort;
    }

    /**
     * @param serverPort the serverPort to set
     */
    void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @return the protocol
     */
    String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the path
     */
    String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    void setPath(String path) {
        if (StringUtils.isNotBlank(path)) {
            if (path.charAt(0) != '/') {
                this.path = "/" + path;
            } else {
                this.path = path;
            }
        }
    }
}
