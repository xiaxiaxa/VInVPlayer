package com.xw.net.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xiawei
 * @date: 2021/1/11
 */
public class ApkListHttpItem implements HttpItem {
    String mServer;
    private Map<String, String> map = new HashMap<String, String>();

    public ApkListHttpItem(String server) {
        this.mServer = server;
    }

    @Override
    public long getIndentify() {
        return 0;
    }

    @Override
    public HttpItem.INType getBufferType() {
        return HttpItem.INType.JSON;
    }

    @Override
    public HttpItem.Method getMethod() {
        return HttpItem.Method.GET;
    }

    @Override
    public HttpItem.EncodeType getEncodeType() {
        return HttpItem.EncodeType.UTF8;
    }

    @Override
    public String getServerUrl() {
        return this.mServer;
    }

    @Override
    public Map<String, String> getAttrMap() {
        return null;
    }

    @Override
    public boolean isStaticTransable() {
        return false;
    }

}
