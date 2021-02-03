package com.xw.net.http;

import java.util.Map;

/**
 * @author Mr.xw
 * @time 2021/1/11 17:12
 */
public interface HttpItem {

    public enum INType {
        BUFFER,
        XML,
        JSON,
        HTML
    }

    public enum Method {
        GET,
        POST,
    }

    public enum EncodeType {
        GB2312,
        UTF8,
    }

    /**
     * 该类请求标识，请求返回的buffer会根据此标识去区分解析
     * 标识定义为15位整型 每三位标识api_map的一位，最后三位为自定义区分位 如N39_A_15转换出来位39 001 015 000
     */
    public long getIndentify();

    /**
     * 请求返回的buffer会根据buffertype去区分解析为jason或者xml
     */
    public INType getBufferType();

    /**
     * 请求发送方式
     */
    public Method getMethod();

    /**
     * 请求发送编码格式
     */
    public EncodeType getEncodeType();

    public String getServerUrl();

    /**
     * 參數集
     */
    public Map<String, String> getAttrMap();

    /**
     * 是否改接口静态化
     */
    public boolean isStaticTransable();
}
