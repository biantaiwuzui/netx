package com.netx.utils.security;

/**
 *
 * @author Runshine
 * @since 2015-5-25
 * @version 1.0.0
 *
 */
public class XSS {
//    public static final HashMap<String, String> FILTER_MAP = new HashMap<>();
//
//    static {
//        FILTER_MAP.put("<", "＞");
//        FILTER_MAP.put(">", "&gt;");
////        FILTER_MAP.put("\"", "&quot;");
////        FILTER_MAP.put(".", "&sdot;");
////        FILTER_MAP.put("&", "&amp;");
////        FILTER_MAP.put(":", "：");
//        FILTER_MAP.put("\\(", "（");
//        FILTER_MAP.put("\\)", "）");
//    }

    public static String filter(String input) {
        if (input == null || "".equals(input)) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input.length() + 16);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '>':
                    sb.append('＞');//全角大于号  
                    break;
                case '<':
                    sb.append('＜');//全角小于号  
                    break;
                case '(':
                    sb.append('（');//全角井号  
                    break;
                case ')':
                    sb.append('）');//全角井号  
                    break;
//            case '\'':  
//                sb.append('‘');//全角单引号  
//                break;  
//            case '"':  
//                sb.append('“');//全角双引号  
//                break;  
//                case '\\':
//                    sb.append('＼');//全角斜线  
//                    break;
//                case '&':
//                    sb.append('＆');//全角
//                    break;
//                case '#':
//                    sb.append('＃');//全角井号
//                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
