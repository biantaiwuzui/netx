package com.netx.utils.uri;

/**
 * User: Asion Date: 11-11-24 Time: 上午10:39
 */
public class UrlHelper {

    /**
     * 归一化请求URL
     *
     * @param requestURL
     * @return
     */
    public static String normalizedUrl(String requestURL) {
        StringBuilder sb = new StringBuilder();
        if (!requestURL.startsWith("/")) {
            sb.append("/");
        }
        String url = requestURL;
        if (requestURL.endsWith("/") && requestURL.length() > 1) {
            int index = requestURL.lastIndexOf("/");
            url = requestURL.substring(0, index);
        }
        sb.append(url);
        return sb.toString();
    }

}
