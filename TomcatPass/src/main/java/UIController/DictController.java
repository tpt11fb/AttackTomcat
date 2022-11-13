package UIController;

import Util.YamlUtil;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class DictController implements Initializable {
    public TextField usernameFilePath;
    public TextField passwordFilePath;
    public YamlUtil yml;
    public Button reset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("读取配置文件，设置字典路径");
//        this.usernameFilePath.setText("dddd");  下边读取yaml文件即可
        this.yml = new YamlUtil();
        this.usernameFilePath.setText((String) this.yml.readYaml("FilePath.username"));
        this.passwordFilePath.setText((String) this.yml.readYaml("FilePath.password"));
    }
    public void setUsernameDict(ActionEvent actionEvent) {
        final String[] file = {null};
        Stage primaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        file[0] = String.valueOf(fileChooser.showOpenDialog(primaryStage));
        System.out.println("获取文件路径："+file[0]);
        this.usernameFilePath.setText(file[0]);
    }
    public void setPasswordDict(ActionEvent passwordDict) {
        final String[] file = {null};
        Stage primaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        file[0] = String.valueOf(fileChooser.showOpenDialog(primaryStage));
        System.out.println("获取文件路径："+file[0]);
        this.passwordFilePath.setText(file[0]);
    }
    public void save(ActionEvent actionEvent) {
        // 保存配置修改yaml文件即可。
        System.out.println("字典路径设置成功："+this.usernameFilePath.getText()+this.passwordFilePath.getText());
        this.yml.updateYaml("FilePath.username",this.usernameFilePath.getText());
        this.yml.updateYaml("FilePath.password",this.passwordFilePath.getText());
        this.yml.hitStage("字典设置保存成功！");
    }
    public void reset(ActionEvent actionEvent) {
        // 调用yaml重置配置文件函数即可。
        this.yml.reset();
        this.yml.hitStage("重置成功！");
        // get a handle to the stage
        Stage stage = (Stage) this.reset.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
