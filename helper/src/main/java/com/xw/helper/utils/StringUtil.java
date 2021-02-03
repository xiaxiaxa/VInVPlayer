package com.xw.helper.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 字符串通用方法
 *
 * @author HS
 */
public class StringUtil {

    /**
     * 判断指定的字符串是否为空(为null或者只包括空格)
     *
     * @param str 指定字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 将null字符串转成空串，永不返回null
     *
     * @param str 字符串
     * @return 转换后的字符串
     */
    public static String getNoNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param src   源字符串
     * @param desti 目的字符串
     * @return 字符串是否相等
     */
    public static boolean isStrEqual(String src, String desti) {
        return isStrEqual(src, desti, false, false);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param src          源字符串
     * @param desti        目的字符串
     * @param isEmptyEqual 空是否相等
     * @return 字符串是否相等
     */
    public static boolean isStrEqual(String src, String desti, boolean isEmptyEqual) {
        return isStrEqual(src, desti, isEmptyEqual, false);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param src          源字符串
     * @param desti        目的字符串
     * @param isEmptyEqual 空是否相等
     * @param isIgnoreCase 是否不区分大小写
     * @return 字符串是否相等
     */
    public static boolean isStrEqual(String src, String desti, boolean isEmptyEqual, boolean isIgnoreCase) {
        return src == desti || (isEmptyEqual && StringUtil.isEmpty(src) && StringUtil.isEmpty(desti))
                || (src != null && (isIgnoreCase ? src.equalsIgnoreCase(desti) : src.equals(desti)));
    }

    /**
     * 比较字符串
     *
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 比较结果 等于0表示相等 大于0表示s1大于s2 小于0表示s1小于s2
     */
    public static int compareStr(String s1, String s2) {
        return compareStr(s1, s2, false);
    }

    /**
     * 比较字符串
     *
     * @param s1           字符串1
     * @param s2           字符串2
     * @param isIgnoreCase 是否不区分大小写
     * @return 比较结果 等于0表示相等 大于0表示s1大于s2 小于0表示s1小于s2
     */
    public static int compareStr(String s1, String s2, boolean isIgnoreCase) {
        if (s1 == null && s2 == null) {
            return 0;
        }
        if (s1 == null && s2 != null) {
            return -1;
        }
        if (s1 != null && s2 == null) {
            return 1;
        }
        return isIgnoreCase ? s1.compareToIgnoreCase(s2) : s1.compareTo(s2);
    }

    /**
     * 转换字符串为字节值
     *
     * @param str 字符串
     * @return 字节值 空的时候返回0
     */
    public static byte getByteValue(String str) {
        return getByteValue(str, 10);
    }

    /**
     * 转换字符串为字节值
     *
     * @param str   字符串
     * @param radix 基数值
     * @return 字节值 空的时候返回0
     */
    public static byte getByteValue(String str, int radix) {
        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Byte.parseByte(str, radix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 转换字符串为短整型值
     *
     * @param str 字符串
     * @return 短整型值 空的时候返回0
     */
    public static short getShortValue(String str) {
        return getShortValue(str, 10);
    }

    /**
     * 转换字符串为短整型值
     *
     * @param str   字符串
     * @param radix 基数值
     * @return 短整型值 空的时候返回0
     */
    public static short getShortValue(String str, int radix) {
        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Short.parseShort(str, radix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 转换字符串为整型值
     *
     * @param str 字符串
     * @return 整型值 空的时候返回0
     */
    public static int getIntValue(String str) {
        return getIntValue(str, 10);
    }

    /**
     * 转换字符串为整型值
     *
     * @param str   字符串
     * @param radix 基数值
     * @return 整型值 空的时候返回0
     */
    public static int getIntValue(String str, int radix) {
        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str, radix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 转换字符串为长整型值
     *
     * @param str 字符串
     * @return 长整型值 空的时候返回0
     */
    public static long getLongValue(String str) {
        return getLongValue(str, 10);
    }

    /**
     * 转换字符串为长整型值
     *
     * @param str   字符串
     * @param radix 基数值
     * @return 长整型值 空的时候返回0
     */
    public static long getLongValue(String str, int radix) {
        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Long.parseLong(str, radix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 转换字符串为浮点值
     *
     * @param str 字符串
     * @return 浮点值
     */
    public static float getFloatValue(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 转换字符串为双精度浮点值
     *
     * @param str 字符串
     * @return 双精度浮点值
     */
    public static double getDoubleValue(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取指定长度的字符串
     *
     * @param value    待转换的值
     * @param fillChar 待填充的字符
     * @param isPre    是否是前部（在指定长度大于实际长度时表示前填充，否则表示前截取）
     * @param len      填充后总长度
     * @return 字符串
     */
    public static String getStr(int value, char fillChar, boolean isPre, int len) {
        return getStr(value, fillChar, isPre, len, null);
    }

    /**
     * 获取指定长度的字符串
     *
     * @param value        待转换的值
     * @param fillChar     待填充的字符
     * @param isPre        是否是前部（在指定长度大于实际长度时表示前填充，否则表示前截取）
     * @param len          填充后总长度
     * @param defaultValue 出错时的默认返回值
     * @return 字符串
     */
    public static String getStr(int value, char fillChar, boolean isPre, int len, String defaultValue) {
        return getStr((long) value, fillChar, isPre, len, defaultValue);
    }

    /**
     * 获取指定长度的字符串
     *
     * @param value    待转换的值
     * @param fillChar 待填充的字符
     * @param isPre    是否是前部（在指定长度大于实际长度时表示前填充，否则表示前截取）
     * @param len      填充后总长度
     * @return 字符串
     */
    public static String getStr(long value, char fillChar, boolean isPre, int len) {
        return getStr(value, fillChar, isPre, len, null);
    }

    /**
     * 获取指定长度的字符串
     *
     * @param value        待转换的值
     * @param fillChar     待填充的字符
     * @param isPre        是否是前部（在指定长度大于实际长度时表示前填充，否则表示前截取）
     * @param len          填充后总长度
     * @param defaultValue 出错时的默认返回值
     * @return 字符串
     */
    public static String getStr(long value, char fillChar, boolean isPre, int len, String defaultValue) {
        return getStr(String.valueOf(value), fillChar, isPre, len, defaultValue);
    }

    /**
     * 获取指定长度的字符串
     *
     * @param value    待转换的字符串
     * @param fillChar 待填充的字符
     * @param isPre    是否是前部（在指定长度大于实际长度时表示前填充，否则表示前截取）
     * @param len      填充后总长度
     * @return 字符串
     */
    public static String getStr(String value, char fillChar, boolean isPre, int len) {
        return getStr(value, fillChar, isPre, len, null);
    }

    /**
     * 获取指定长度的字符串
     *
     * @param value        待转换的字符串
     * @param fillChar     待填充的字符
     * @param isPre        是否是前部（在指定长度大于实际长度时表示前填充，否则表示前截取）
     * @param len          填充后总长度
     * @param defaultValue 出错时的默认返回值
     * @return 字符串
     */
    public static String getStr(String value, char fillChar, boolean isPre, int len, String defaultValue) {
        if (len < 0 || value == null) {
            return defaultValue;
        }
        int realLen = value.length();
        if (len < realLen) {
            return isPre ? value.substring(0, len) : value.substring(realLen - len);
        } else if (len > realLen) {
            realLen = len - realLen;
            StringBuffer sb = isPre ? new StringBuffer() : new StringBuffer(value);
            for (int i = 0; i < realLen; i++) {
                sb.append(fillChar);
            }
            if (isPre) {
                sb.append(value);
            }
            return sb.toString();
        }
        return value;
    }

    /**
     * 获取编码后的url地址
     *
     * @param url 编码前的url地址
     * @return 编码后的url地址
     */
    public static String getEncodedUrl(String url) {
        return getEncodedUrl(url, "UTF-8");
    }

    /**
     * 获取编码后的url地址
     *
     * @param url     编码前的url地址
     * @param charSet 编码格式
     * @return 编码后的url地址
     */
    public static String getEncodedUrl(String url, String charSet) {
        if (isEmpty(url) || isEmpty(charSet)) {
            return null;
        }
        try {
            return URLEncoder.encode(url, charSet).replaceAll("%3a", ":").replaceAll("%2f", "/");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取编码前的url地址
     *
     * @param url 编码后的url地址
     * @return 编码前的url地址
     */
    public static String getDecodedUrl(String url) {
        return getDecodedUrl(url, "UTF-8");
    }

    /**
     * 获取编码前的url地址
     *
     * @param url     编码后的url地址
     * @param charSet 编码格式
     * @return 编码前的url地址
     */
    public static String getDecodedUrl(String url, String charSet) {
        if (isEmpty(url) || isEmpty(charSet)) {
            return null;
        }
        try {
            return URLDecoder.decode(url, charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导演 演员过滤器
     *
     * @param names
     * @return
     */
    public static String nameFilter(String names) {
        if (names != null && !names.equals("0") && !names.equals("未知")) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] nameStrs = names.split("/");
            for (int i = 0; i < nameStrs.length; i++) {
                if (!StringUtil.isEmpty(nameStrs[i])) {
                    if (StringUtil.isEmpty(stringBuilder.toString())) {
                        stringBuilder.append(nameStrs[i]);
                    } else {
                        stringBuilder.append(" / " + nameStrs[i]);
                    }
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }

    public static String formatChannelNo(String channelNo) {
        if (StringUtil.isEmpty(channelNo)) {
            return "000";
        } else {
            String res = "";
            switch (channelNo.length()) {
                case 1:
                    res = "00" + channelNo;
                    break;
                case 2:
                    res = "0" + channelNo;
                    break;
                case 3:
                    res = channelNo;
                    break;
                default:
                    res = channelNo;
                    break;
            }
            return res;
        }
    }

    /**
     * 处理数据上报中预留字段需要传空字段串
     *
     * @param count   目标需要的字符串个数
     * @param strings 传入的原始字符串数组
     * @return
     */
    public static String[] getReserveStrings(int count, String... strings) {
        String[] strings1 = new String[count];
        for (int i = 0; i < count; i++) {
            if (strings != null) {
                if (i < strings.length) {
                    strings1[i] = strings[i];
                } else {
                    strings1[i] = "";
                }
            } else {
                strings1[i] = "";
            }
        }
        return strings1;
    }

    /**
     * 播放时间装换成时分秒
     *
     * @param time 单位：秒
     * @return
     */
    public static String secToTime(int time) {
        if (time < 0) {
            return "";
        }
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 60)
            return time + "秒";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                if (second == 0) {
                    timeStr = minute + "分钟";
                } else {
                    timeStr = minute + "分钟" + second + "秒";
                }
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = hour + "小时" + minute + "分钟" + second + "秒";
            }
        }
        return timeStr;
    }
}
