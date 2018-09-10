package com.netx.utils.money;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author plus
 * @since 10/23/14.
 */
public class ExchangeRateUtil {

    public static final String url = "http://api.uihoo.com/currency/currency.http.php?";

    public static final String current = "CNY";

    public static final String format = "json";

    public static String CNYToHKDRate() {
        return getRateJson(createUrl("HKD"));
    }

    public static String CNYToUSDRate() {
        return getRateJson(createUrl("USD"));
    }

    public static String getRate(String from, String to) {
        return getRateJson(createUrl(from, to));
    }

    public static CurrencyRate getRate(CurrencyType from, CurrencyType to) {
        return JSONObject.parseObject(getRateJson(createUrl(from.name(), to.name())), CurrencyRate.class);
    }

    public static CurrencyRate getRateToCurrencyRate(CurrencyType from, CurrencyType to) {
        return JSONObject.parseObject(getRateJson(createUrl(from.name(), to.name())), CurrencyRate.class);
    }

    private static String getRateJson(String urlvalue) {
        String inputLine = "";
        URL url = null;
        try {
            url = new URL(urlvalue);
        } catch (MalformedURLException ex) {
            return inputLine;
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            return inputLine;
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            inputLine = in.readLine();
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        } catch (IOException ex) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex1) {
                }
            }
        }

        return inputLine;
    }

    private static String createUrl(String to) {
        return url + "from=" + current.toLowerCase() + "&to=" + to.toLowerCase() + "&format=" + format;
    }

    private static String createUrl(CurrencyType from, CurrencyType to) {
        return url + "from=" + from.toString().toUpperCase() + "&to=" + to.toString().toUpperCase() + "&format=" + format;
    }

    private static String createUrl(String from, String to) {
        return url + "from=" + from.toUpperCase() + "&to=" + to.toUpperCase() + "&format=" + format;
    }

}
