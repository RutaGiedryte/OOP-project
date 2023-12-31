package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WorkspaceAdminCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private User user;
    private ObservableList<Board> data;
    @FXML
    private VBox boardsDisplay;


    @Inject
    public WorkspaceAdminCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void subscribeForSocketsWorkspaceAdmin(){
        server.registerForMessages("/topic/boards/removed", Integer.class, boardId -> {
            Platform.runLater(() -> removeBoard(boardId));
        });
        server.registerForMessages("/topic/boards/rename", Board.class, newBoard -> {
            Platform.runLater(() -> { renameBoard(newBoard.id,newBoard.title); });
        });
        server.registerForUpdates(b->{
            Platform.runLater(this::refresh);
        });
    }

    /**
     * Method that sets the user for the workspace
     * @param username - username of the user
     */
    public void setUser(String username){
        this.user = server.getUserByUsername(username);
        if(this.user == null)
            this.user = server.addUser(new User(username));
    }


    //region SOCKET METHODS

    private void renameBoard(int id, String title){
        boardsDisplay.getChildren().stream()
                .filter(e -> ((BoardWorkspaceAdminCtrl)e.getUserData()).getBoard().id==id)
                .findFirst()
                .ifPresent(e -> ((BoardWorkspaceAdminCtrl) e.getUserData())
                        .getLblBoardName().setText(title));
    }

    private void removeBoard(int boardId){
        //We need to check if user is null because we might have
        //never accessed this scene
        if(user!=null && user.hasBoardAlready(boardId))
            boardsDisplay.getChildren().
                    removeIf(e ->
                            ((BoardWorkspaceAdminCtrl)e.getUserData()).getBoard().id==boardId);
    }

    //endregion

    //region Button methods

    public void createBoard(){
        mainCtrl.showNewBoard(this.user);
    }

    /**
     * Method that disconnects the user and redirect to
     * welcome page
     */
    public void disconnect(){
        mainCtrl.showWelcomePage();
    }

    //endregion

    //region Refresh methods

    /**
     * Method that sets controller for the javaFX object
     * that represent joined board
     * @param boardLoader
     * @param board
     * @return
     */
    private BoardWorkspaceAdminCtrl setCtrl(FXMLLoader boardLoader, Board board){
        BoardWorkspaceAdminCtrl ctrl = boardLoader.getController();
        ctrl.setMainCtrlAndServer(mainCtrl, server, this);
        ctrl.setBoard(board);
        ctrl.setUser(this.user);
        return ctrl;
    }

    /**
     * Method that refreshes the preview
     */
    public void refresh(){
        List<Board> boards = server.getBoards();
        data = FXCollections.observableList(boards);
        clearBoards();

        for(Board board: boards){
            FXMLLoader boardWorkspaceAdminLoader = new FXMLLoader(getClass().
                    getResource("BoardWorkspaceAdmin.fxml"));
            try {
                Node boardObject = boardWorkspaceAdminLoader.load();
                boardObject.setUserData(boardWorkspaceAdminLoader.getController());
                setCtrl(boardWorkspaceAdminLoader, board);
                boardsDisplay.getChildren().add(boardObject);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void clearBoards(){
        boardsDisplay.getChildren().clear();
    }

    //endregion

    //region Getters and setters

    public ServerUtils getServer() {
        return server;
    }

    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VBox getBoardsDisplay() {
        return boardsDisplay;
    }

    public void setBoardsDisplay(VBox boardsDisplay) {
        this.boardsDisplay = boardsDisplay;
    }

    //endregion

    /**
     * This method is used to stop the longpolling thread
     */
    public void stop(){
        server.stop();
    }

}
