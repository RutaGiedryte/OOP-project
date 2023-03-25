package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Subtask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class CardCtrl extends AnchorPane implements Initializable{
    @FXML
    private Label cardTitle;
    @FXML
    private Button cardEdit;
    @FXML
    private Button cardDelete;
    @FXML
    private ImageView imgDescription;
    @FXML
    private Label lblSubtasks;

    private Card card;

    private ServerUtils server;
    private MainCtrl mainCtrl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /** Sets text of the title of the card
     */
    public void setCardAttributes() {
        cardTitle.setText(card.title);
        if(card.description == null || card.description.isEmpty()) {
            imgDescription.setVisible(false);
        }
        if(card.subtasks == null || card.subtasks.isEmpty()){
            lblSubtasks.setVisible(false);
        }else{
            long total = card.subtasks.size();
            long done = Stream.of(card.subtasks.toArray()).
                filter(subtask->((Subtask)subtask).done).count();
            lblSubtasks.setText("(" + done + "/" + total + "Done)");
        }
    }

    /** This method associates a card to the controller for easy access
     * @param card the card to be associated with the controller
     */
    public void setCard(Card card){
        this.card = card;
    }

    public void deleteCard(){
        server.deleteCard(card.id);
        mainCtrl.deleteCard();
    }

    public void editCard(){
        mainCtrl.showEditCard(card);
    }

    public void setServerAndCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
}