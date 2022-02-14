package com.cennavi.utils;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by sunpengyan on 2017-7-27.
 */
public class SendUtils {

    public static void main(String[] args) {
        String url = "http://180.153.158.217:8081/api/login";
        String param = "username=sa&password=654321";
        String s = sendPost(url, param);

        String param1 = "{\"cityCode\": \"1307\",\"meshList\": [615417,615416]}";
        String url1 = "http://106.112.139.135:8081/api/get";
        String str = SendUtils.sendPostJson(url1, param1);

        String url2 = "http://106.112.139.135:8081/api/loadAdinfo/1";
        String s2 = sendPostForCookie(url2, null,"JSESSIONID=2C06602FA7CA076E88B2B5A8E5D9536A");
    }

    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        URL realUrl = null;
        try {
            String urlNameString = url;
            realUrl = new URL(urlNameString);
            System.out.println(realUrl);
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(100000);
            connection.setReadTimeout(100000);
            connection.setRequestProperty("connection", "close");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            //e.printStackTrace();
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        System.out.println(result);
        return result;
    }
    public static String sendGet(String url, String... head) {
        String result = "";
        BufferedReader in = null;
        URL realUrl = null;
        try {
            String urlNameString = url;
            realUrl = new URL(urlNameString);
            System.out.println(realUrl);
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(100000);
            connection.setReadTimeout(100000);
            connection.setRequestProperty("connection", "close");
            if(head.length>0) {
                connection.setRequestProperty("Authorization", head[0]);
            }
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            //e.printStackTrace();
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        System.out.println(result);
        return result;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            //conn.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
            //find bugs
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));


            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    //new InputStreamReader(conn.getInputStream()));
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            //System.out.println(conn.getHeaderField("Set-cookie"));
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            // 使用finally块来关闭输出流、输入流
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //System.out.println("url:" + url);
        //System.out.println("POST请求结果：" + result);
        return result;
    }
    public static String sendPostJson(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("accept","application/json");
            if (StringUtils.isNotBlank(param)) {
                byte[] writebytes = param.getBytes();
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(param.getBytes());
                outwritestream.flush();
                outwritestream.close();
                conn.getResponseCode();
            }
            //out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            //out.print(param);
            //out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            // 使用finally块来关闭输出流、输入流
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPostForCookie(String url, String param,String cookie) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            if(StringUtils.isNotBlank(cookie)) {
                conn.setRequestProperty("Cookie", cookie);
            }

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
            //find bugs
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));

            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    //new InputStreamReader(conn.getInputStream()));
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            result +="|"+conn.getHeaderField("Set-cookie");
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            // 使用finally块来关闭输出流、输入流
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
