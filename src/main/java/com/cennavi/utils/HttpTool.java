package com.cennavi.utils;


import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Z。jj
 */
public class HttpTool {
	private static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * @return 返回类型:
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @description 功能描述: get 请求
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.prepareGet(url);
        builder.setBodyEncoding(DEFAULT_CHARSET);
        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                builder.addQueryParameter(key, params.get(key));
            }
        }

        if (headers != null && !headers.isEmpty()) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                builder.addHeader(key, headers.get(key));
            }
        }
        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }

    /**
     * @return 返回类型:
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @description 功能描述: get 请求
     */
    public static String get(String url) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, IOException, ExecutionException, InterruptedException {
        return get(url, null);
    }

    /**
     * @return 返回类型:
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws UnsupportedEncodingException
     * @description 功能描述: get 请求
     */
    public static String get(String url, Map<String, String> params) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, IOException, ExecutionException, InterruptedException {
        return get(url, params, null);
    }

    /**
     * @return 返回类型:
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @description 功能描述: POST 请求
     */
    public static String post(String url, Map<String, String> params) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.setBodyEncoding(DEFAULT_CHARSET);
        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                builder.addParameter(key, params.get(key));
            }
        }
        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }

    /**
     * 上传媒体文件
     *
     * @param url
     * @param file
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     */
    public static String upload(String url, File file) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.setBodyEncoding(DEFAULT_CHARSET);
        String BOUNDARY = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL"; // 定义数据分隔�?        builder.setHeader("connection", "Keep-Alive");
        builder.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");
        builder.setHeader("Charsert", "UTF-8");
        builder.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义�?��数据分隔�?        builder.setBody(new UploadEntityWriter(end_data, file));

        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }

  

    public static String post(String url, String s) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.setBodyEncoding(DEFAULT_CHARSET);
        builder.setBody(s);
        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }
    
    public  static void main(String[] args) throws Exception{
        //这个工具类 好在处理 https 相关接口 容错更强。 这个工具类 可继续扩展（例如获取访问 sessionid），post带headers
        //带headers 普通get
    	String url="http://www.baidu.com";
    	String json=get(url);
    	System.out.println(json);
    	JSONObject obj=JSONObject.fromObject(json);
    	System.out.println(json);
        //带headers 例子
        String url2="https://10.163.201.158/openapi/devicemanagement/v2/app/shadowDevice/get";
        Map<String, String> params2=new HashMap<>();
        params2.put("deviceName","123456");
        Map<String, String> headers =new HashMap<>();
        headers.put("Authorization","access_token");
        String rel2= get(url2,params2,headers);
        JSONObject device=JSONObject.fromObject(rel2);
    }
}