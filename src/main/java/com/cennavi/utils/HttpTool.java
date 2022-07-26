package com.cennavi.utils;




import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.ning.http.client.cookie.Cookie;

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
        try{
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
        }catch (Exception e){
            http.close();
        }
        return null;
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
        try{
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
        }catch (Exception e){
            http.close();
        }
        return null;
    }

    /***
     * post 带 headers
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String post(String url, Map<String, String> params, Map<String, String> headers) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        try{
            AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
            builder.setBodyEncoding(DEFAULT_CHARSET);
            if (params != null && !params.isEmpty()) {
                Set<String> keys = params.keySet();
                for (String key : keys) {
                    builder.addParameter(key, params.get(key));
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
        }catch (Exception e){
            http.close();
        }
        return null;
    }

    /**
     * post  body 为 "application/json 形式 获取数据
     * @param url
     * @param content  不能用map 用JSONObject toString()
     * @param headers
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String post(String url, String content, Map<String, String> headers) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        try{
            AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
            builder.setBodyEncoding(DEFAULT_CHARSET);
            builder.setBody(content);
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
        }catch (Exception e){
            http.close();
        }
        return null;
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
        try{
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
        }catch (Exception e){
            http.close();
        }
        return null;
    }

    /**
     * post 参数 ？asd=asd形式
     * @param url
     * @param s
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String post(String url, String s) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        try{
            AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
            builder.setBodyEncoding(DEFAULT_CHARSET);
            builder.setBody(s);
            Future<Response> f = builder.execute();
            String body = f.get().getResponseBody(DEFAULT_CHARSET);
            http.close();
            return body;
        }catch (Exception e){
            http.close();
        }
        return null;
    }

    /**
     * 获取 body 和本次访问的cookie
     * @param url
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static Map getBodyCookie(String url) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        try{
            AsyncHttpClient.BoundRequestBuilder builder = http.prepareGet(url);
            builder.setBodyEncoding(DEFAULT_CHARSET);
            Future<Response> f = builder.execute();
            List<Cookie> list= f.get().getCookies();
            String body = f.get().getResponseBody(DEFAULT_CHARSET);
            http.close();
            Map map=new HashMap();
            map.put("cookie",list);
            map.put("body",body);
            return map;
        }catch (Exception e){
            http.close();
        }
        return null;
    }


    /**
     * 获取信息 携带head cookie
     * @param url
     * @param params
     * @param headers
     * @param list
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String postByCooike(String url, Map<String, String> params, Map<String, String> headers,List<Cookie> list) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        try{
            AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
            builder.setBodyEncoding(DEFAULT_CHARSET);
            if (params != null && !params.isEmpty()) {
                Set<String> keys = params.keySet();
                for (String key : keys) {
                    builder.addParameter(key, params.get(key));
                }
            }
            if (headers != null && !headers.isEmpty()) {
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    builder.addHeader(key, headers.get(key));
                }
            }
            builder.setCookies(list);
            Future<Response> f = builder.execute();
            String body = f.get().getResponseBody(DEFAULT_CHARSET);
            http.close();
            return body;
        }catch (Exception e){
            System.out.println(e.toString());
            http.close();
        }
        return null;
    }

    /**
     * 获取信息携带cooike
     * @param url
     * @param s
     * @param list
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String postStringByCooike(String url, String s,List<Cookie> list) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        try{
            AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
            builder.setBodyEncoding(DEFAULT_CHARSET);
            builder.setBody(s);
            builder.setCookies(list);
            Future<Response> f = builder.execute();
            String body = f.get().getResponseBody(DEFAULT_CHARSET);
            http.close();
            return body;
        }catch (Exception e){
            http.close();
        }
        return null;
    }


    public  static void main(String[] args) throws Exception{

    }
}