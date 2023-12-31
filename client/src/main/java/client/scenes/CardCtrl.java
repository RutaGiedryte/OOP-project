package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class CardCtrl extends AnchorPane implements Initializable {
    @FXML
    private Label cardTitle;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField editableTitle;
    @FXML
    private ImageView imgDescription;
    @FXML
    private Label lblSubtasks;
    @FXML
    private FlowPane paneTags;
    @FXML
    private ComboBox quickSelectTags;
    @FXML
    private ComboBox quickSelectPreset;

    private Card card;

    private ServerUtils server;
    private MainCtrl mainCtrl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quickSelectTags.setFocusTraversable(false);
        quickSelectPreset.setFocusTraversable(false);
        handleEditableTitle();
        handleQuickTags();
        handleQuickPreset();
    }

    private boolean hasWriteAccess() {
        Board b = server.getBoardByID(server.getBoardListById(card.listId).boardId);
        return mainCtrl.getIsAdmin() || b.password.equals("") || b.password.equals("NO_PASSWORD") ||
                server.getUserByUsername(mainCtrl.getUsername()).unlockedBoards.
                contains(server.getBoardByID(server.getBoardListById(card.listId).boardId));
    }

    private void handleEditableTitle(){
        editableTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if(!editableTitle.getText().equals("")) {
                    Card newCard = card;
                    newCard.title = editableTitle.getText();
                    this.card = server.editCard(card.id, newCard);
                }
                editableTitle.setVisible(false);
                cardTitle.setVisible(true);
            }
        });
        //When enter is pressed focus is taken away to another node so editing finished
        //Also, event is consumed
        editableTitle.addEventHandler(KeyEvent.KEY_PRESSED, (EventHandler<KeyEvent>) keyEvent -> {
            if(keyEvent.getCode()== KeyCode.ENTER){
                System.out.println("Pressed in text");
                anchorPane.requestFocus();
                keyEvent.consume();
            }
        });
    }

    private void handleQuickTags(){
        // Cell factory
        quickSelectTags.setCellFactory(lv -> new ListCell<Tag>() {
            @Override
            public void updateItem(Tag item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.toString());
                    //setDisable(item.id == cardToEdit.presetId);
                }
            }
        });

        // Add listener to detect when a tag is selected.
        quickSelectTags.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null) {
                Tag t = (Tag) newval;
                card.addTag(t);
                server.editCard(card.id, card);
                quickSelectTags.setVisible(false);
            }
        });

//        quickSelectTags.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue) quickSelectTags.setVisible(false);
//        });
    }

    private void handleQuickPreset(){
        // Cell factory
        quickSelectPreset.setCellFactory(lv -> new ListCell<Preset>() {
            @Override
            public void updateItem(Preset item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setDisable(item.id == card.presetId);
                }
            }
        });

        // Add listener to detect when a preset is selected.
        quickSelectPreset.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null) {
                Preset p = (Preset) newval;
                card.setPreset(p);
                server.editCard(card.id, card);
                quickSelectPreset.setVisible(false);
            }
        });

//        quickSelectPreset.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue) quickSelectPreset.setVisible(false);
//        });
    }

    /**
     * Sets the card to be represented (if it changed)
     * the title and the description
     */
    public void setCardAndAttributes(Card card) {
        this.card = server.getCardById(card.id);
        cardTitle.setText(card.title);
        hideNotNeeded();
        setSmallIcons();
        setQuickTags();
        setQuickPresets();
        setFontAndBackgroundColor();
    }

    /** This method associates a card to the controller for easy access
     * @param card the card to be associated with the controller
     */
    public void setCard(Card card){
        this.card = card;
    }

    private static void throwWriteAlert() {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText("You don't have write access!");
        alert.showAndWait();
    }

    public void deleteCard(){
        if(!hasWriteAccess()){
            throwWriteAlert();
            return;
        }
        server.deleteCard(card.id);
    }

    public void editCard(){
        mainCtrl.showEditCard(card);
    }

    public void setServerAndCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
        mainCtrl.consumeShortcutsTextField(editableTitle);
        editableTitle.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch(event.getCode()) {
                    case DELETE:
                    case T:
                    case ENTER:
                    case BACK_SPACE:
                        event.consume();
                        break;
                }
            }
        });
    }

    public Card getCard() {
        return card;
    }
    public void addTag(Tag tag){
        if(!hasWriteAccess()){
            throwWriteAlert();
            return;
        }
        FXMLLoader tagLoader = new FXMLLoader(getClass().getResource("TagIcon.fxml"));
        try {
            Node tagObject = tagLoader.load();
            TagIconController tagController = tagLoader.getController();
            tagController.setTag(tag);
            paneTags.getChildren().add(tagObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editTitle(){
        if(!hasWriteAccess()){
            throwWriteAlert();
            return;
        }
        cardTitle.setVisible(false);
        editableTitle.setVisible(true);
        editableTitle.requestFocus();
        PauseTransition delay = new PauseTransition(Duration.seconds(0.01));
        delay.setOnFinished(event -> {
            editableTitle.setText(card.title);
        });
        delay.play();
    }

    public void quickAddTag(){
        if(!hasWriteAccess()){
            throwWriteAlert();
            return;
        }
        quickSelectTags.setVisible(true);
    }

    public void quickAddPreset(){
        if(!hasWriteAccess()){
            throwWriteAlert();
            return;
        }
        quickSelectPreset.setVisible(true);
    }

    private void hideNotNeeded(){
        editableTitle.setVisible(false);
        quickSelectTags.setVisible(false);
        quickSelectPreset.setVisible(false);
    }

    private void setQuickTags(){
        ObservableList<Tag> tags = FXCollections.observableArrayList();
        int boardId = server.getBoardListById(card.listId).boardId;
        tags.addAll(server.getTags(boardId));
        quickSelectTags.setItems(tags);
    }

    private void setQuickPresets(){
        ObservableList<Preset> presets = FXCollections.observableArrayList();
        int boardId = server.getBoardListById(card.listId).boardId;
        presets.addAll(server.getAllBoardPresets(boardId));
        quickSelectPreset.setItems(presets);
        quickSelectPreset.setFocusTraversable(false);
    }

    private void setSmallIcons() {
        // description
        if (card.description == null || card.description.isEmpty()) {
            imgDescription.setVisible(false);
        } else {
            imgDescription.setVisible(true);
        }
        // subtasks
        if (card.subtasks == null || card.subtasks.isEmpty()) {
            lblSubtasks.setVisible(false);
        } else {
            long total = card.subtasks.size();
            long done = Stream.of(card.subtasks.toArray()).
                    filter(subtask -> ((Subtask) subtask).done).count();
            lblSubtasks.setText("(" + done + "/" + total + "Done)");
            lblSubtasks.setVisible(true);
        }
        // tags
        paneTags.getChildren().clear();
        if (card.tags != null) {
            for (Tag tag : card.tags) {
                addTag(tag);
            }
        }
    }
    /**
     * Method which sets the fond and backgrounds colors of
     * the CardCtrl based on the Preset the current Card
     * is pointing to.
     */
    public void setFontAndBackgroundColor(){
        Preset preset = server.getPresetById(card.presetId);
        String fontColor = preset.font;
        String backgroundColor = preset.backgroundColor;
        cardTitle.setTextFill(Paint.valueOf(fontColor));
        String style = "-fx-border-color: black; -fx-border-width: 4; -fx-border-radius: 5 5 5 5;" +
                "-fx-background-radius: 5 5 5 5; -fx-background-color:" +
                backgroundColor.replace("0x", "#");
        anchorPane.setStyle(style);
    }


}