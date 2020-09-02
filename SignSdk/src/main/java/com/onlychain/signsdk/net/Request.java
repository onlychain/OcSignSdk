package com.onlychain.signsdk.net;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Request {
    public  abstract void success(StringBuffer json);
    public  abstract void fail(Exception e);
    public Request(String urls) {
        get(urls);
    }

    public Request(String post_url,LinkedHashMap parameter) {
        post(post_url,parameter);
    }
    public void get(final String get_urls) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer stringBuffer=new StringBuffer();
                try {
                    URL url = new URL(get_urls);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5*1000);//链接超时
                    urlConnection.setReadTimeout(5*1000);//返回数据超时
                    //getResponseCode (1.200请求成功 2.404请求失败)
                    if(urlConnection.getResponseCode()==200){
                        //获得读取流写入
                        InputStream inputStream = urlConnection.getInputStream();
                        byte[] bytes=new byte[1024];
                        int len=0;
                        while ((len=inputStream.read(bytes))!=-1){
                            stringBuffer.append(new String(bytes,0,len));
                        }
                        success(stringBuffer);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void post(final String post_url, final LinkedHashMap parameter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer stringBuffer=new StringBuffer();
                try {
                    URL url = new URL(post_url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5*1000);
                    urlConnection.setReadTimeout(5*1000);
                    //设置请求方式,大写的POST
                    urlConnection.setRequestMethod("POST");
                    //一定要设置 Content-Type 要不然服务端接收不到参数
                    urlConnection.setRequestProperty("Content-Type", "application/JSON; charset=UTF-8");
                    //设置允许输出
                    urlConnection.setDoOutput(true);
                    urlConnection.setUseCaches(false);

                    OutputStream out = urlConnection.getOutputStream();
                    out.write(hashMapToJson(parameter).getBytes());//请求参数放到请求体
                    out.flush();
                    out.close();
                    if(urlConnection.getResponseCode()==200){
                        String result="";
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
                        while((result=br.readLine()) != null){
                            stringBuffer.append(result+"\n");
                        }
                        br.close();
                        success(stringBuffer);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    fail(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    fail(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail(e);
                }
            }
        }).start();
    }
    public static String hashMapToJson(LinkedHashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            string += "\"" + e.getValue() + "\",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }
}











