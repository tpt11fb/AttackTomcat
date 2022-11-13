package UIController;

import Util.YamlUtil;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class ProxyHttpController implements Initializable {
    public TextField hHost;
    public TextField hPort;
    public String strHost;
    public String strPort;
    public Button closeButton;
    public YamlUtil yml;
    public RadioButton off;
    public RadioButton on;

    public ProxyHttpController(){
        this.yml = new YamlUtil();
    }
    public void saveHTTP(ActionEvent actionEvent) {
        this.yml.updateYaml("Proxy.Http.host",this.hHost.getText());
        this.yml.updateYaml("Proxy.Http.port",this.hPort.getText());
        this.yml.updateYaml("Proxy.Http.isOn", String.valueOf(this.on.isSelected()));
        System.out.println("代理切换成功："+this.hHost.getText()+":"+this.hPort.getText());
        this.yml.hitStage("设置HTTP代理成功！");
        this.close();
    }

    public boolean httpIsNull(){
        if (this.yml.readYaml("Proxy.Http.isOn").equals("true")){
            System.out.println("使用代理中。。");
            this.strHost = (String) this.yml.readYaml("Proxy.Http.host");
            this.strPort = (String) this.yml.readYaml("Proxy.Http.port");
            return true;
        }
        return false;
    }
    public boolean setIsNull(){
        if (this.yml.readYaml("Proxy.Http.isOn").equals("true")){
            System.out.println("使用代理中。。");
            this.on.setSelected(true);
            return true;
        }
        return false;
    }
    public void close() {
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.hHost.setText((String) this.yml.readYaml("Proxy.Http.host"));
        this.hPort.setText(this.yml.readYaml("Proxy.Http.port").toString());
        ToggleGroup group = new ToggleGroup();
        this.off.setToggleGroup(group);
        this.off.setSelected(!this.setIsNull());
        this.on.setToggleGroup(group);
    }
}
