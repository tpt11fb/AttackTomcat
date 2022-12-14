package Controller;

import UIController.ProxyHttpController;
import UIController.ProxySocketController;
import Util.Base64;
import Util.Mycert;
import Util.UserAgent;
import Util.YamlUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ReqController {
    private String url;
    private Map<String, List<String>> headers;
    private int respCode;
    private String contentText;
    private Base64 base64 = new Base64();
    private String usernameFileName;
    private String passwdFileName;
    public YamlUtil yml;
    private ProxyHttpController setHttpProxy;
    private ProxySocketController setSocketProxy;
    public ReqController(String url, YamlUtil yml){
        this.url = url;
        this.yml = yml;
        this.setHttpProxy = new ProxyHttpController();
        this.setSocketProxy = new ProxySocketController();
        this.usernameFileName = (String) yml.readYaml("FilePath.username");
        this.passwdFileName = (String) yml.readYaml("FilePath.password");
    }
    public void sendGET(String base64_content) {
        Mycert mycert = new Mycert();
//        "Authorization","Basic dG9tY2F0OmFkbWlu"
//        "User-Anget","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/20100101 Firefox/105.0"
        try {
            URL url = new URL(this.url);
            HttpURLConnection conn;
            if (this.setHttpProxy.httpIsNull()){
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(String.valueOf(this.setHttpProxy.strHost),
                        Integer.parseInt(String.valueOf(this.setHttpProxy.strPort))));
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else if (this.setSocketProxy.socketIsNull()){
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(String.valueOf(this.setSocketProxy.strHost),
                        Integer.parseInt(String.valueOf(this.setSocketProxy.strPort))));
                conn = (HttpURLConnection) url.openConnection();
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            // ??????????????????
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000); //??????????????????
            // ??????headers
            conn.setRequestProperty("Authorization","Basic "+base64_content);
            conn.setRequestProperty("User-Anget", UserAgent.getRandomUserAgent());
            conn.connect();  // ???????????????????????????
            System.out.println(this.url+"\n"+base64_content);
            this.headers = conn.getHeaderFields();
            this.respCode = conn.getResponseCode();
            if (this.respCode == 200) { // ????????????
                // ???????????????????????????
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) { // ?????????????????????
                    this.contentText += line + "\n";
                }
                reader.close(); // ?????????
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int checlVul(String base64_content){
        this.sendGET(base64_content);
        System.out.println(this.headers);
        System.out.println(this.respCode);
        if (this.respCode == 200 && this.contentText.contains("Tomcat Web Application Manager")){
            return 1;
        } else if (this.respCode == 0 && this.headers == null){
            return -1;
        }
        return 0;
    }
    public String outResult() {
        Base64 b = new Base64();
        int isVul = 0;
        for (String base64_content:b.file_base64(this.usernameFileName,this.passwdFileName)){
            isVul = this.checlVul(base64_content);
            if (isVul==1){
                return "[++] ??????????????? ???????????????"+b.base64_decode(base64_content);
            } else if (isVul==-1){
                return "[!] ????????????URL";
            }
        }
        return "[-] ??????????????????????????????";
    }
//    public static void main(String[] args) {
//        ReqController req = new ReqController("http://127.0.0.1:8081/manager/html");
//        req.outResult("dG9tY2F0OmFkbWlu");
//    }
}
