package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TagOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board board;
    private ObservableList<Tag> data;
    @FXML
    private VBox tagDisplay;

    @Inject
    public TagOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void subscribeForSocketsTagOverview(){
        server.registerForMessages("/topic/tags", Integer.class, cardId -> {
            Platform.runLater(this::refresh);
        });
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public void createTag(){
        // allow no more than 5 tags (a way to avoid dealing with visual overflow)
        if(board.tags.size() >= 5) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("A board can have at most 5 tags");
            alert.showAndWait();
            return;
        }
        else mainCtrl.showAddTag(this.board);
    }

    public void back() {
        mainCtrl.showBoard();
    }

    private TagCellForOverviewCtrl setCtrl(FXMLLoader tagLoader, Tag tag){
        TagCellForOverviewCtrl ctrl = tagLoader.getController();
        ctrl.setMainCtrlAndServer(mainCtrl, server, this);
        ctrl.setTag(tag);
        return ctrl;
    }

    /**
     * Method that clears the preview and removes all tags
     * from the preview
     */
    public void clearTags(){
        tagDisplay.getChildren().clear();
    }

    /**
     * Method that refreshes the preview
     */
    public void refresh(){
        board = server.getBoardByID(board.id);
        List<Tag> tags = board.tags;
        data = FXCollections.observableList(tags);
        clearTags();

        for(Tag tag: tags){
            tag.board = board;
            FXMLLoader tagDisplayLoader = new FXMLLoader(getClass().
                    getResource("TagCellForOverview.fxml"));
            try {
                Node tagObject = tagDisplayLoader.load();
                setCtrl(tagDisplayLoader, tag);
                tagDisplay.getChildren().add(tagObject);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
