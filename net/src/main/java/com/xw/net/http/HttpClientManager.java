package com.xw.net.http;

import android.os.Handler;
import android.os.Looper;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.xw.helper.utils.MLog;
import com.xw.net.NetUtil;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author: xiawei
 * @date: 2021/1/11
 */
public class HttpClientManager {
    private static HttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;
    private List<Call> callList = new ArrayList<>();

    private static final String TAG = "HttpClientManager";
    private int serversLoadTimes = 0;//单次连接次数
    private final int maxLoadTimes = 3;//最大连接次数
    private final int CONNECT_TIMEOUT = 20;//链接超时时间
    private final int READ_TIMEOUT = 20;//读取超时时间

    private HttpClientManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        mOkHttpClient = builder
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
//                .cache(cache)// 设置缓存
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();


        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static HttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (HttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new HttpClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private Call _getAsyn(String url, final HttpClientManager.ResultCallback callback) {
        MLog.e("open URL:get==" + url);
        Call call = null;
        Request request = null;
        try {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Charset", "utf-8")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Connection", "Keep-Alive")
                    .build();
            call = deliveryResult(callback, request);
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(request, e);
            }
            e.printStackTrace();
        }
        return call;
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param map      请求参数   Map<String,String>
     * @param callback
     */
    private Call _postAsyn(String url, Map<String, String> map, final ResultCallback callback) {
        MLog.e("open URL:post==" + url);
        Call call = null;
        Request request = null;
        try {
            request = buildPostRequest(url, map);
            call = deliveryResult(callback, request);
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(request, e);
            }
            e.printStackTrace();
        }
        return call;
    }

    private Request buildPostRequest(String url, Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && value != null) {
                builder.add(key, value);
            }
        }
        RequestBody requestBody = builder.build();
        Request request = null;
        try {
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    private Call _postAsyncWithJsonBody(String url, Object body, final ResultCallback callback) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = mGson.toJson(body);
        Call call = null;
        Request request = null;
        try {
            RequestBody requestBody = RequestBody.create(JSON, jsonBody);
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Charset", "utf-8")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Connection", "Keep-Alive")
                    .post(requestBody)
                    .build();
            call = deliveryResult(callback, request);
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(request, e);
            }
            e.printStackTrace();
        }
        return call;
    }

    private Call deliveryResult(final HttpClientManager.ResultCallback callback, Request request) {
        final Call call1 = mOkHttpClient.newCall(request);
        callList.add(call1);
        serversLoadTimes = 0;
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (serversLoadTimes == maxLoadTimes)//如果超时并未超过指定次数，则重新连接
                {
                    mOkHttpClient.newCall(call.request()).enqueue(this);
                    serversLoadTimes++;
                    MLog.e("HttpClicen链接超时，开始重连:" + serversLoadTimes);
                } else {
                    callList.remove(call);
                    sendFailedStringCallback(call.request(), e, callback);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callList.remove(call);
                try {
                    final String string = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                    MLog.e("IOException");
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    MLog.e("Json解析的错误");
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
        return call1;
    }

    /**
     * 成功回调主线程
     *
     * @param object
     * @param callback
     */
    public void sendSuccessResultCallback(final Object object, final HttpClientManager.ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    /**
     * 失败回调主线程
     *
     * @param request
     * @param e
     * @param callback
     */
    public void sendFailedStringCallback(final Request request, final Exception e, final HttpClientManager.ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(request, e);
                }
            }
        });
    }

    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

    public static Call getAsyn(String url, ResultCallback callback) {
        Call call = getInstance()._getAsyn(url, callback);
        return call;
    }

    public static Call getAsyn(String url, Map<String, String> map, ResultCallback callback) {
        url = NetUtil.addMap2Url(url, map);
        Call call = getInstance()._getAsyn(url, callback);
        return call;
    }

    /**
     * @param isStatic 是否需要静态化
     */
    public static Call getAsyn(String url, boolean isStatic, Map<String, String> map, ResultCallback callback) {
        url = NetUtil.addMap2Url(url, map);
        if (isStatic) {
            url = NetUtil.pseudoInterfaceAddrTrans(url);
        }
        Call call = getInstance()._getAsyn(url, callback);
        return call;
    }

    public static Call postAsyn(String url, Map<String, String> params, ResultCallback callback) {
        Call call = getInstance()._postAsyn(url, params, callback);
        return call;
    }

    public static Call postAsyn(String url, ResultCallback callback) {
        Call call = getInstance()._postAsyn(url, null, callback);
        return call;
    }

    public static Call postAsync(String url, Object body, ResultCallback callback) {
        Call call = getInstance()._postAsyncWithJsonBody(url, body, callback);
        return call;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    //============11-9

    /**
     * 如果有HttpItem请调用此方法： 对接口请求进行相关设置
     *
     * @param httpItem
     * @param callback
     * @return
     */
    public static Call getAsyn(@NonNull HttpItem httpItem, ResultCallback callback) {
        StringBuffer sb = new StringBuffer();
        sb.append(httpItem.getServerUrl());
        if (httpItem.getAttrMap() != null && httpItem.getAttrMap().size() > 0
                && httpItem.getMethod() == HttpItem.Method.GET) {
            sb.append("?");
            sb.append(NetUtil.getParams(httpItem.getAttrMap(), httpItem.getEncodeType()));
        }
        String url = sb.toString();
        if (httpItem.isStaticTransable()) {
            url = NetUtil.pseudoInterfaceAddrTrans(url);
        }
        Call call = getInstance()._getAsyn(url, callback);
        return call;
    }

    /**
     * TODO 只适用于Get请求
     *
     * @param rawUrl   n1_a_1下发原地址
     * @param isStatic 是否需要静态化
     * @param type     请求参数字段转译 默认utf-8
     * @param map      请求参数Map
     * @param callback
     * @return
     */
    public static Call getAsyn(String rawUrl, boolean isStatic, @NonNull HttpItem.EncodeType type, Map<String, String> map, ResultCallback callback) {
        StringBuffer sb = new StringBuffer();
        sb.append(rawUrl);
        if (map != null && map.size() > 0) {
            sb.append("?");
            if (type == null) {
                type = HttpItem.EncodeType.UTF8;
            }
            sb.append(NetUtil.getParams(map, type));
        }
        String url = sb.toString();
        if (isStatic) {
            url = NetUtil.pseudoInterfaceAddrTrans(url);
        }
        Call call = getInstance()._getAsyn(url, callback);
        return call;
    }


    /**
     * 如果有HttpItem请调用此方法： 对接口请求进行相关设置
     *
     * @param httpItem
     * @param callback
     * @return
     */
    public static Call postAsyn(@NonNull HttpItem httpItem, ResultCallback callback) {
        String url = httpItem.getServerUrl();
        Call call = getInstance()._postAsyn(url, httpItem.getAttrMap(), callback);
        return call;
    }

    /**
     * 取消Call 任务
     *
     * @param call
     */
    public static HttpClientManager cancelCall(Call call) {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        return getInstance();
    }

    /**
     * @description 封装apk文件下载器
     * @author Mr.xw
     * @time 2021/1/12 11:22
     */
    /**
     * @param url          下载连接
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称，后面记得拼接后缀，否则手机没法识别文件类型
     * @param listener     下载监听
     */

    public void downloadApk(final String url, final String destFileDir, final String destFileName,
                            final HttpClientManager.OnDownloadListener listener) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**异步请求*/
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /** 下载失败监听回调*/
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                /**储存下载文件的目录*/
                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, destFileName);
                try {

                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        /**下载中更新进度条*/
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    /**下载完成*/
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                    listener.onDownloadFailed(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }
                }
            }
        });
    }

    public interface OnDownloadListener {
        /**
         * 下载成功之后的文件
         */
        void onDownloadSuccess(File file);

        /**
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载异常信息
         */
        void onDownloadFailed(Exception e);
    }
}
