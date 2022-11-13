package Controller;

import UIController.ProxyHttpController;
import UIController.ProxySocketController;
import Util.Mycert;
import Util.UserAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import java.util.Map;

public class PutVul {
    public ProxySocketController setSocketProxy;
    public ProxyHttpController setHttpProxy;
    public String  url;
    private Map<String, List<String>> headers;
    private int respCode;
    private String contentText;
    public Proxy proxy;
    public PutVul(String  url){
        this.url = this.checkUrl(url);
        Mycert mycert = new Mycert();
        this.setHttpProxy = new ProxyHttpController();
        this.setSocketProxy = new ProxySocketController();
        if (this.setHttpProxy.httpIsNull()){
            this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(String.valueOf(this.setHttpProxy.strHost),
                    Integer.parseInt(String.valueOf(this.setHttpProxy.strPort))));
        } else if (this.setSocketProxy.socketIsNull()){
            this.proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(String.valueOf(this.setSocketProxy.strHost),
                    Integer.parseInt(String.valueOf(this.setSocketProxy.strPort))));
        } else {
            this.proxy = null;
        }
    }

    public String  outResult(){
        if (this.checkVul()==1 && (this.respCode==201||this.respCode==204)){
            return "[++] 存在PUT漏洞，可直接上传webshell！";
        } else if (this.respCode!=201){
            return "[-] 不存在PUT上传漏洞";
        } else {
            return "[!] 无法连接URL";
        }
    }
    public int checkVul(){
        URL url = null;
        this.sendPut();
        try {
            url = new URL(this.url+"/1092wxa.txt");
            HttpURLConnection conn;
            if (this.proxy != null){
                conn = (HttpURLConnection) url.openConnection(this.proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000); //请求超时时间
            // 设置headers
            conn.setRequestProperty("Accept","*/*");
            conn.setRequestProperty("Connection","close");
            conn.setRequestProperty("Accept-Language","en");
            conn.setRequestProperty("User-Anget", UserAgent.getRandomUserAgent());
            if (conn.getResponseCode()==200){
                return 1;
            } else {
                return 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String checkUrl(String url){
        if (!url.substring(url.length() - 1).equals("/") && !url.substring(8).contains("/")){
            return url;
        }
        return url.substring(0,url.indexOf('/',8));
    }
    public void sendPut(){
        try {
            URL url = new URL(this.url+"/1092wxa.txt");
            HttpURLConnection conn;
            if (this.proxy != null){
                conn = (HttpURLConnection) url.openConnection(this.proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            // 设置请求方式
            conn.setRequestMethod("PUT");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000); //请求超时时间
            // 设置headers
            conn.setRequestProperty("Accept","*/*");
            conn.setRequestProperty("Connection","close");
            conn.setRequestProperty("Accept-Language","en");
            conn.setRequestProperty("User-Anget", UserAgent.getRandomUserAgent());
            conn.connect();  // 发起请求，建立连接
            this.headers = conn.getHeaderFields();
            this.respCode = conn.getResponseCode();
            if (this.respCode == 200) { // 正常响应
                // 从流中读取响应信息
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) { // 循环从流中读取
                    this.contentText += line + "\n";
                }
                reader.close(); // 关闭流
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
