/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class WelcomePageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField chosenServer;

    @FXML
    private Label connectionLabel;

    @FXML
    private TextField username;

    @FXML
    private Label userLabel;

    @FXML
    private PasswordField adminPasswordTxt;

    @FXML
    private Label adminErrorLabel;
    @FXML
    private Button helpButton;

    private boolean isAdmin;
    private String adminPassword;

    @Inject
    public WelcomePageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void showHelpPage(){
        mainCtrl.showHelp();
    }

    /**
     * Connects to the server the user inputs in the field "chosenServer"
     */
    public void connectToChosenServer() {
        clearLabels();
        if(testServerConnection()) {
            isAdmin = checkAdminPassword();
            if (isAdmin) {
                if(testUserID()){
                    mainCtrl.showAdminWorkspace(username.getText());
                    server.initializeStompSession(chosenServer.getText());
                    mainCtrl.registerForAllSockets();
                    mainCtrl.setAdmin(true);
                } else
                    setTextAndRemoveAfterDelay(userLabel,
                            "Bad input: Username is empty.");
            } else if(adminPasswordTxt.getText().equals("")){
                mainCtrl.setAdmin(false);
                if(testUserID()) {
                    server.setServer("http://" + chosenServer.getText() + "/");
                    server.initializeStompSession(chosenServer.getText());
                    mainCtrl.registerForAllSockets();
                    mainCtrl.showWorkspace(username.getText());
                } else {
                    setTextAndRemoveAfterDelay(userLabel,
                            "Bad input: Username is empty.");
                }
            } else {
                setTextAndRemoveAfterDelay(adminErrorLabel,
                        "Wrong admin password.");
            }
        }
        else {
            setTextAndRemoveAfterDelay(connectionLabel,
                    "Connection Failed: Server unreachable or wrong input format.");
        }
    }

    private PauseTransition delay;

    private void setTextAndRemoveAfterDelay(Label label,String text){
        label.setText(text);
        if(delay!=null)
            delay.stop();//stop previous delay
        delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            label.setText("");
        });
        delay.play();
    }

    private void clearLabels(){
        connectionLabel.setText("");
        userLabel.setText("");
        adminErrorLabel.setText("");
    }

    public void clearPassword(){
        adminPasswordTxt.clear();
    }

    /**
     * Method which compares the input inside the adminPassword field
     * with the value inside the adminAccess.properties file.
     * @return true if password matches, otherwise false.
     */
    private boolean checkAdminPassword() {
        Properties prop = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("/adminAccess.properties");
        try {
            prop.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        adminPassword = (String) prop.get("adminPassword");
        String input = adminPasswordTxt.getText();

        return input.equals(adminPassword);
    }

    private boolean testUserID(){
        return !username.getText().equals("");
    }

    /** checks if the server address the user inputs is reachable.
     * @return  If yes, then it returns true.
     */
    public boolean testServerConnection() {
        try {
            URL url = new URL("http://" + chosenServer.getText() + "/api/boards/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            int responseCode = connection.getResponseCode();
            connection.disconnect();

            // If the responseCode == 200 => server is reachable
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainCtrl.consumeShortcutsTextField(chosenServer);
        mainCtrl.consumeShortcutsTextField(username);
        mainCtrl.consumeShortcutsTextField(adminPasswordTxt);
        Image test = new Image("/client.images/helpButton.png");
        helpButton.setGraphic(new ImageView(test));
    }
}