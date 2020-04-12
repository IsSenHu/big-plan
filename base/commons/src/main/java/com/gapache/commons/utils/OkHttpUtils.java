package com.gapache.commons.utils;

import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * create on 2020/4/11 3:52 下午
 */
public class OkHttpUtils {
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();

    private static final String JSON = "application/json;charset=utf-8";

    /**
     * 异步Get请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 异步回调
     */
    public static void getAsync(String url, Map<String, String> params, Headers headers, Callback callback) {
        url = handleUrlParams(url, params);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        CLIENT.newCall(request).enqueue(callback);
    }

    /**
     * 同步Get请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应body
     */
    public static String getSync(String url, Map<String, String> params) {
        url = handleUrlParams(url, params);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = CLIENT.newCall(request);
        return execAndResp(call);
    }

    /**
     * 异步Post请求
     *
     * @param url     请求地址
     * @param body    请求体
     * @param headers 请求头
     */
    public static void postAsync(String url, String body, Headers headers, Callback callback) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(RequestBody.create(body, MediaType.get(JSON)));
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        CLIENT.newCall(request).enqueue(callback);
    }

    /**
     * 同步Post请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应body
     */
    public static String postSync(String url, Map<String, String> params, Headers headers) {
        FormBody.Builder fb = new FormBody.Builder();
        if (MapUtils.isNotEmpty(params)) {
            params.forEach(fb::add);
        }
        FormBody formBody = fb.build();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(formBody);
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = CLIENT.newCall(request);
        return execAndResp(call);
    }

    /**
     * 同步Delete请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @return 响应body
     */
    public static String deleteSync(String url, Headers headers) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .delete();
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = CLIENT.newCall(request);
        return execAndResp(call);
    }

    /**
     * 同步Put请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @return 响应body
     */
    public static String putSync(String url, Headers headers) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .put(RequestBody.create(StringUtils.EMPTY.getBytes()));
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = CLIENT.newCall(request);
        return execAndResp(call);
    }

    private static String execAndResp(Call call) {
        // 注意资源的释放
        try (Response response = call.execute()) {
            ResponseBody body = response.body();
            if (body == null) {
                return null;
            }
            return body.string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String handleUrlParams(String url, Map<String, String> params) {
        if (MapUtils.isNotEmpty(params)) {
            StringBuilder sb = new StringBuilder(url).append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = sb.substring(0, sb.length() - 1);
        }
        return url;
    }
}
