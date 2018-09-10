package com.netx.utils.money;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Closure.Yang
 * @since 2014/10/20
 */
public enum CurrencyType {

    CNY {
                @Override
                public String toDesc() {
                    return "人民币";
                }

                @Override
                public String areaName() {
                    return "大陆";
                }

                @Override
                public String sign() {
                    return "¥";
                }

                @Override
                public String areaCircleFlag() {
                    return "http://assets.sephome.com/system/b2c/images/area_flag/circular50px/flag_china.png";
                }

            },
    HKD {
                @Override
                public String toDesc() {
                    return "港元";
                }

                @Override
                public String areaName() {
                    return "香港";
                }

                @Override
                public String sign() {
                    return "HK$";
                }

                @Override
                public String areaCircleFlag() {
                    return "http://assets.sephome.com/system/b2c/images/area_flag/circular50px/flag_hongkong.png";
                }
            },
    TWD {
                @Override
                public String toDesc() {
                    return "台币";
                }

                @Override
                public String areaName() {
                    return "台湾";
                }

                @Override
                public String sign() {
                    return "NT$";
                }

                @Override
                public String areaCircleFlag() {
                    return "http://assets.sephome.com/system/b2c/images/area_flag/circular50px/flag_china.png";
                }
            },
    EUR {
                @Override
                public String toDesc() {
                    return "欧元";
                }

                @Override
                public String areaName() {
                    return "欧洲";
                }

                @Override
                public String sign() {
                    return "€";
                }

                @Override
                public String areaCircleFlag() {
                    return "http://assets.sephome.com/system/b2c/images/area_flag/circular50px/flag_EuropeanUnion.png";
                }
            },
    USD {
                @Override
                public String toDesc() {
                    return "美元";
                }

                @Override
                public String areaName() {
                    return "美国";
                }

                @Override
                public String sign() {
                    return "$";
                }

                @Override
                public String areaCircleFlag() {
                    return "http://assets.sephome.com/system/b2c/images/area_flag/circular50px/flag_unitedstates.png";
                }
            },
    GBP {
                @Override
                public String toDesc() {
                    return "英镑";
                }

                @Override
                public String areaName() {
                    return "英国";
                }

                @Override
                public String sign() {
                    return "￡";
                }

                @Override
                public String areaCircleFlag() {
                    return "http://assets.sephome.com/system/b2c/images/area_flag/circular50px/flag_unitedkingdom.png";
                }
            },
    AUD {
                @Override
                public String toDesc() {
                    return "澳元";
                }

                @Override
                public String areaName() {
                    return "澳洲";
                }

                @Override
                public String sign() {
                    return "AU$";
                }

                @Override
                public String areaCircleFlag() {
                    return "http://assets.sephome.com/system/b2c/images/area_flag/circular50px/flag_australia.png";
                }
            },
    KRW {
                @Override
                public String toDesc() {
                    return "韩元";
                }

                @Override
                public String areaName() {
                    return "韩国";
                }

                @Override
                public String sign() {
                    return "₩";
                }

                @Override
                public String areaCircleFlag() {
                    return "http://assets.sephome.com/system/b2c/images/area_flag/circular50px/flag_southkorea.png";
                }
            },
    JPY {
                @Override
                public String toDesc() {
                    return "日元";
                }

                @Override
                public String areaName() {
                    return "日本";
                }

                @Override
                public String sign() {
                    return "¥";
                }

                @Override
                public String areaCircleFlag() {
                    return "http://assets.sephome.com/system/b2c/images/area_flag/circular50px/flag_japan.png";
                }
            };

    public abstract String toDesc();

    public abstract String areaName();

    public abstract String sign();

    public String areaCircleFlag() {
        return "";
    }
}
