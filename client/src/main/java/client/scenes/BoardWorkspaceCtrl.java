package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardWorkspaceCtrl implements Initializable {

    @FXML
    private Label lblBoardName;

    @FXML
    private Button btnOpen;

    @FXML
    private Button btnClose;

    private MainCtrl mainCtrl;
    private ServerUtils server;

    private WorkspaceCtrl workspaceCtrl;
    private Board board;

    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //region Button methods

    public void openBoard(){
        mainCtrl.showBoard(board,workspaceCtrl.getUser());
    }

    public void leaveBoard(){
        server.removeBoardFromJoined(user.id, board.id);
        this.workspaceCtrl.refresh();
    }

    //endregion

    //region Getters and setters

    public void setMainCtrlAndServer(MainCtrl mainCtrl, ServerUtils server,
                                     WorkspaceCtrl workspaceCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.workspaceCtrl = workspaceCtrl;
    }

    public Board getBoard(){
        return board;
    }

    public Label getLblBoardName(){ return lblBoardName;}

    public void setBoard(Board board){
        this.board = board;
        lblBoardName.setText(board.title);
    }
    public void setUser(User user){
        this.user = user;
    }

    //endregion
}
