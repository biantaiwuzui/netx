package com.netx.utils.money;

/**
 * {说明}
 *
 * @author Retina.Ye
 * @since 3/12/15 11:44 AM
 */
public class CurrencyRate {

    private String now;     // 当前from->to汇率
    private String date;    // 汇率更新日期
    private String time;    // 汇率更新时间
    private String buy;     // 买入
    private String sale;    // 卖出
    private String from_cn;
    private String to_cn;

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getFrom_cn() {
        return from_cn;
    }

    public void setFrom_cn(String from_cn) {
        this.from_cn = from_cn;
    }

    public String getTo_cn() {
        return to_cn;
    }

    public void setTo_cn(String to_cn) {
        this.to_cn = to_cn;
    }
}