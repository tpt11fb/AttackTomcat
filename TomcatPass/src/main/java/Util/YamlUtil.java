package Util;

import javafx.scene.control.Alert;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
public class YamlUtil {
    private String rootPath;
    private Map<String,Object> config;
    private File file;
    public YamlUtil(){
        this.rootPath = System.getProperty("user.dir");
        this.file = new File(this.rootPath+"/config");
        this.init();
    }
    public void init(){
        if (!file.exists()){
            file.mkdirs();
            File f = new File(this.rootPath+"/config/config.yaml");
            try {
                f.createNewFile();
                this.reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Yaml yml = new Yaml();
        try {
            FileReader reader = new FileReader(this.rootPath+"/config/config.yaml");
            BufferedReader buffer = new BufferedReader(reader);
            this.config = yml.load(buffer);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        System.out.println(this.config);
    }
    public String getRootPath() {
        return rootPath;
    }
    public Object readYaml(String key) {
        String[] separatorKeys  = null;
        if (key.contains(".")){
            separatorKeys = key.split("\\.");
        } else {
            return this.config.get(key);
        }
        Map<String, Map<String, Object>> finalValue = new HashMap<>();
        for (int i = 0; i < separatorKeys.length - 1; i++) {
            if (i == 0) {
                finalValue = (Map) this.config.get(separatorKeys[i]);
                continue;
            }
            if (finalValue == null) {
                break;
            }
            finalValue = (Map) finalValue.get(separatorKeys[i]);
        }
        return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]);
    }
//    {
//      FilePath = {
//          username=[xxx], password=[xxxx]
//      },
//      Proxy = {
//          Http = {
//              host=null, port=null
//          },
//          Socket = {
//              host=null, port=null, username=null, password=null
//          }
//      },
//      Version=[1.0]}

    /**
     *
     * @param key
     * @param value
     * 修改yaml文件。
     */
    public void updateYaml(String key, String value){
        String[] separatorKeys = key.split("\\.");
        Map finlly = this.config;
        for (int i=0; i < separatorKeys.length-1;i++){
            finlly = (Map) finlly.get(separatorKeys[i]);
        }
        finlly.put(separatorKeys[separatorKeys.length-1],value);
        Yaml yml = new Yaml();
        try {
            yml.dump(this.config,new FileWriter(this.rootPath+"/config/config.yaml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int checkYaml(){
        if (this.readYaml("FilePath")!=null && this.readYaml("Proxy") != null){
            return 1;
        }
        return 0;
    }
    public int reset(){
        String str = "FilePath: {username: '"+this.rootPath+"\\config\\username.txt', password: '"+this.rootPath+"\\config\\password.txt'}\n" +
                "Proxy:\n" +
                "  Http: {host: 127.0.0.1, port: '8080', isOn: 'false'}\n" +
                "  Socket: {host: 127.0.0.1, port: '1080', username: null, password: null, isOn: 'false'}\n" +
                "Version: [1.0]\n";
        String str2 = "admin\n" +
                "tomcat\n" +
                "root\n" +
                "manager\n" +
                "test\n" +
                "system\n" +
                "administrator\n" +
                "sys";
        String str3 = "admin\n" +
                "tomcat\n" +
                "root\n" +
                "123456\n" +
                "s3cret\n" +
                "manager\n" +
                "test\n" +
                "123\n" +
                "abc123\n" +
                "111111\n" +
                "sys\n" +
                "system";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.rootPath+"/config/config.yaml"));
            out.write(str);
            out.close();
            BufferedWriter out2 = new BufferedWriter(new FileWriter(this.rootPath+"/config/username.txt"));
            out2.write(str2);
            out2.close();
            BufferedWriter out3 = new BufferedWriter(new FileWriter(this.rootPath+"/config/password.txt"));
            out3.write(str3);
            out3.close();
            return 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void hitStage(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("信息");
        alert.headerTextProperty().set(content);
        alert.showAndWait();
    }
    public static void main(String[] args) throws IOException {
        YamlUtil y = new YamlUtil();
//        y.reset();
//        System.out.println(y.rootPath);
        System.out.println(y.config);
//        System.out.println(y.getRunPath());
    }
}
