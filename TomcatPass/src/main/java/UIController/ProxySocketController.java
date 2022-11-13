package UIController;

import Util.YamlUtil;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class ProxySocketController implements Initializable {
    public TextField sPort;
    public TextField us;
    public TextField sHost;
    public YamlUtil yml;
    public TextField passwd;
    public String strHost;
    public String strPort;
    public Button closeButton;
    public RadioButton off;
    public RadioButton on;
    public ProxySocketController(){
        this.yml = new YamlUtil();
    }
    public void saveSocket(ActionEvent actionEvent) {
        this.yml.updateYaml("Proxy.Socket.host",this.sHost.getText());
        this.yml.updateYaml("Proxy.Socket.port",this.sPort.getText());
        this.yml.updateYaml("Proxy.Socket.isOn", String.valueOf(this.on.isSelected()));
        System.out.println("设置Socket代理成功："+this.sHost.getText()+":"+this.sPort.getText()+" ; "+
                this.us.getText()+":"+this.passwd.getText());
        this.yml.hitStage("设置Socket代理成功！");
        this.close();
    }
    public boolean socketIsNull(){
        if (this.yml.readYaml("Proxy.Socket.isOn").equals("true")){
            System.out.println("使用代理中。。");
            this.strHost = (String) this.yml.readYaml("Proxy.Socket.host");
            this.strPort = (String) this.yml.readYaml("Proxy.Socket.port");
            return true;
        }
        return false;
    }
    public boolean setIsNull(){
        if (this.yml.readYaml("Proxy.Socket.isOn").equals("true")){
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
        this.sHost.setText((String) this.yml.readYaml("Proxy.Socket.host"));
        this.sPort.setText(this.yml.readYaml("Proxy.Socket.port").toString());
        ToggleGroup group = new ToggleGroup();
        this.off.setToggleGroup(group);
        this.off.setSelected(!this.setIsNull());
        this.on.setToggleGroup(group);
    }
}
