package UIController;

import Controller.AJPVul;
import Controller.PutVul;
import Controller.ReqController;
import Util.YamlUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public TextArea outLog;
    public YamlUtil yml;
    @FXML
    public TextField url;
    public TextArea readmeArea;
    public TextField ip;
    public TextField port;
    public TextArea readmearea2;
    public TextField path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("初试化配置文件。。。"); // 下面检测一下配置文件是否正确。不正确则重置配置文件
        this.yml = new YamlUtil();
        if (this.yml.checkYaml()!=1){
            this.yml.hitStage("配置文件错误，请点击“重置”按钮重新生成配置文件！");
        }
        this.readmeArea.setText("README" +
                "\nTomcat 漏洞检测工具" +
                "\n\n支持检测漏洞：" +
                "\nCVE-2017-12615 PUT文件上传漏洞" +
                "\ntomcat-pass-getshell 弱认证部署war包" +
                "\nCVE-2020-1938 Tomcat 文件读取/包含" +
                "\n\n使用方式：" +
                "\n[!] 弱口令爆破加上全路径：/manager或/host-manager");
        this.readmearea2.setText("README\n" +
                "\nAJP 文件读取，任意文件包含\n" +
                "影响范围：\n" +
                "Apache Tomcat 9.x < 9.0.31\n" +
                "Apache Tomcat 8.x < 8.5.51\n" +
                "Apache Tomcat 7.x < 7.0.100\n" +
                "Apache Tomcat 6.x\n\n"+
                "默认port：8009,  默认路径：WEB-INF/web.xml\n" +
                "可任意包含ROOT目录下的任意文件，如果执行结果返回500，则指定文件不存在。");
    }
    @FXML
    void settingHttpProxy(ActionEvent httpProxy) throws IOException {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/image/logo.jpg"));
        stage.setTitle("设置HTTP代理");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLLoader.load(getClass().getResource("/setHttpProxy.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    void settingScoketProxy(ActionEvent scoketProxy) throws IOException {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/image/logo.jpg"));
        stage.setTitle("设置Socket代理");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLLoader.load(getClass().getResource("/setSocketProxy.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    public void showUpdata(ActionEvent actionEvent) {

    }
    @FXML
    public void showAbout(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/image/logo.jpg"));
        stage.setTitle("关于");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/about.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    void setDict(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/image/logo.jpg"));
        stage.setTitle("设置字典");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/setDict.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void setReset(ActionEvent actionEvent) {
    }
    public void reqUrl(ActionEvent actionEvent) {
//        System.out.println(this.url.getText()); 获取URL
        this.outLog.appendText("========================正在检测目标："+this.url.getText()+"========================\n");
        ReqController reqController = new ReqController(this.url.getText(), this.yml);
        PutVul putVul = new PutVul(this.url.getText());
        Thread thread = new Thread(() -> {
            String res1 = reqController.outResult();
            String res2 = putVul.outResult();
            Platform.runLater(() -> {
                this.outLog.appendText(res1+"\n");
                this.outLog.appendText(res2+"\n");
            });
        });
        thread.start();
    }

    public void attackAJP(ActionEvent actionEvent) {
        String port = this.port.getText();
        if (port.isEmpty()){
            port = "8009";
        }
        this.outLog.appendText("========================正在检测目标："+this.ip.getText()+":"+port+"========================\n");
        AJPVul ajpVul = new AJPVul(this.path.getText(),this.ip.getText(),this.port.getText());
        Thread thread = new Thread(()->{
            String res3 = ajpVul.outResult();
            Platform.runLater(() -> {
                this.outLog.appendText(res3+"\n");
            });
        });
        thread.start();
    }
}
