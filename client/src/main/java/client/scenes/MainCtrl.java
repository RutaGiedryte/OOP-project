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

import commons.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class MainCtrl {

    private Stage primaryStage;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene board;

    private AddListCtrl addListCtrl;
    private Scene addList;

    private WelcomePageCtrl welcomePageCtrl;
    private Scene welcomePage;

    private EditListCtrl editListCtrl;
    private Scene editList;

    private WorkspaceCtrl workspaceCtrl;
    private Scene workspace;

    private WorkspaceAdminCtrl workspaceAdminCtrl;
    private Scene workspaceAdmin;

    private ChangeBoardTitleCtrl changeBoardTitleCtrl;
    private Scene changeBoardTitle;

    private EditCardCtrl editCardCtrl;
    private Scene editCard;

    private EditTagCtrl editTagCtrl;
    private Scene editTag;

    private AddTagCtrl addTagCtrl;
    private Scene addTag;

    private TagOverviewCtrl tagOverviewCtrl;
    private Scene tagOverview;

    private AddRemoveTagsCtrl addRemoveTagsCtrl;
    private Scene addRemoveTags;

    private HelpCtrl helpCtrl;
    private Scene help;

    private CustomizationCtrl customizationCtrl;
    private Scene customization;

    private AddPresetCtrl addPresetCtrl;
    private Scene addPreset;

    private PasswordSetCtrl passwordSetCtrl;
    private Scene passwordSet;

    private PasswordEnterCtrl passwordEnterCtrl;
    private Scene passwordEnter;


    private String username;
    private boolean isAdmin = false;

    private final String stylePath = "/client.scenes.styles/Default_styles.css";
    List<Scene> sceneArray;

    public void initialize(Stage primaryStage,
                           Pair<AddListCtrl, Parent> addList,
                           Pair<BoardOverviewCtrl, Parent> board,
                           Pair<WelcomePageCtrl, Parent> welcomePage,
                           Pair<EditListCtrl, Parent> editList,
                           Pair<WorkspaceCtrl, Parent> workspace,
                           Pair<WorkspaceAdminCtrl, Parent> workspaceAdmin,
                           Pair<EditCardCtrl, Parent> editCard,
                           Pair<ChangeBoardTitleCtrl, Parent> changeBoardTitle,
                           Pair<EditTagCtrl, Parent> editTag,
                           Pair<AddTagCtrl, Parent> addTag,
                           Pair<TagOverviewCtrl, Parent> tagOverview,
                           Pair<AddRemoveTagsCtrl, Parent> addRemoveTags,
                           Pair<HelpCtrl,Parent> help,
                           Pair<CustomizationCtrl, Parent> customization,
                           Pair<AddPresetCtrl, Parent> addPreset,
                           Pair<PasswordSetCtrl, Parent> passwordSet,
                           Pair<PasswordEnterCtrl, Parent> passwordEnter) {

        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image(
                this.getClass().getResource("/client.images/talioIcon.png").toExternalForm()
        ));

        setControllersAndScenes(addList, board, welcomePage, editList,
                workspace, workspaceAdmin, editCard, changeBoardTitle,
                editTag, addTag, tagOverview, addRemoveTags, help, customization,
                addPreset,passwordSet,passwordEnter);

        showWelcomePage();
        primaryStage.setResizable(false);
        primaryStage.show();
        setStylesheetsAndShortcuts();
    }

    private void setControllersAndScenes(
            Pair<AddListCtrl, Parent> addList,
            Pair<BoardOverviewCtrl, Parent> board,
            Pair<WelcomePageCtrl, Parent> welcomePage,
            Pair<EditListCtrl, Parent> editList,
            Pair<WorkspaceCtrl, Parent> workspace,
            Pair<WorkspaceAdminCtrl, Parent> workspaceAdmin,
            Pair<EditCardCtrl, Parent> editCard,
            Pair<ChangeBoardTitleCtrl, Parent> changeBoardTitle,
            Pair<EditTagCtrl, Parent> editTag,
            Pair<AddTagCtrl, Parent> addTag,
            Pair<TagOverviewCtrl, Parent> tagOverview,
            Pair<AddRemoveTagsCtrl, Parent> addRemoveTags,
            Pair<HelpCtrl,Parent> help,
            Pair<CustomizationCtrl, Parent> customization,
            Pair<AddPresetCtrl, Parent> addPreset,
            Pair<PasswordSetCtrl, Parent> passwordSet,
            Pair<PasswordEnterCtrl, Parent> passwordEnter){
        this.boardOverviewCtrl = board.getKey();
        this.board = new Scene(board.getValue());
        this.addListCtrl = addList.getKey();
        this.addList = new Scene(addList.getValue());
        this.welcomePageCtrl = welcomePage.getKey();
        this.welcomePage = new Scene(welcomePage.getValue());
        this.workspaceCtrl = workspace.getKey();
        this.workspace = new Scene(workspace.getValue());
        this.workspaceAdminCtrl = workspaceAdmin.getKey();
        this.workspaceAdmin = new Scene(workspaceAdmin.getValue());
        this.editListCtrl=editList.getKey();
        this.editList = new Scene(editList.getValue());

        this.changeBoardTitleCtrl = changeBoardTitle.getKey();
        this.changeBoardTitle = new Scene(changeBoardTitle.getValue());

        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

        this.editTagCtrl = editTag.getKey();
        this.editTag = new Scene(editTag.getValue());

        this.addTagCtrl = addTag.getKey();
        this.addTag = new Scene(addTag.getValue());

        this.tagOverviewCtrl = tagOverview.getKey();
        this.tagOverview = new Scene(tagOverview.getValue());

        this.addRemoveTagsCtrl = addRemoveTags.getKey();
        this.addRemoveTags = new Scene(addRemoveTags.getValue());

        this.customizationCtrl = customization.getKey();
        this.customization = new Scene(customization.getValue());

        this.addPresetCtrl = addPreset.getKey();
        this.addPreset = new Scene(addPreset.getValue());

        primaryStage.setOnCloseRequest(e -> workspace.getKey().stop());
        primaryStage.setOnCloseRequest(e -> workspaceAdmin.getKey().stop());

        this.helpCtrl = help.getKey();
        this.help = new Scene(help.getValue());

        this.passwordSetCtrl = passwordSet.getKey();
        this.passwordSet = new Scene(passwordSet.getValue());

        this.passwordEnterCtrl = passwordEnter.getKey();
        this.passwordEnter = new Scene(passwordEnter.getValue());
    }

    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.SLASH,
            KeyCombination.SHIFT_DOWN);

    private void setStylesheetsAndShortcuts() {

        sceneArray = Arrays.asList(addList, board, welcomePage,
                editList, workspace, workspaceAdmin, editCard, changeBoardTitle,
                editTag, addTag, tagOverview, addRemoveTags, help, customization, addPreset,
                passwordSet,passwordEnter);

        for (Scene s : sceneArray) {
            s.getStylesheets().add(this.getClass().getResource(stylePath).toExternalForm());
            s.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler() {
                @Override
                public void handle(Event event) {
                    if(keyComb1.match((KeyEvent) event)){
                        showHelp();
                    }
                }
            });
        }
    }

    /**
     * This method consumes the question mark shortcut, so it doesn't
     * get triggered while in a text box
     * @param text the label to add the handler to
     */
    public void consumeShortcutsTextField(TextInputControl text){
        text.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(keyComb1.match(event)){
                    event.consume();
                }
            }
        });
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void showHelp(){
        if (primaryStage.getScene() == help) return;
        helpCtrl.setPreviousTitle(primaryStage.getTitle());
        helpCtrl.setPreviousScene(primaryStage.getScene());
        primaryStage.setTitle("Help");
        primaryStage.setScene(help);
    }

    public void backFromHelp() {
        primaryStage.setTitle(helpCtrl.getPreviousTitle());
        primaryStage.setScene(helpCtrl.getPreviousScene());
    }

    /**
     * Method that opens new(empty) board for the user
     * @param user - user creating the board
     */
    public void showNewBoard(User user){
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(board);
        Board newBoard = new Board("Board","");
        boardOverviewCtrl.setBoard(newBoard);
        boardOverviewCtrl.saveBoardInDatabase();
        boardOverviewCtrl.setUserViewing(user);
        boardOverviewCtrl.assignToUser(user);
        boardOverviewCtrl.refresh();
    }

    /**
     * Method that sets the current board as the
     * newly joined board and assigns it to the user
     * @param user - user joining the board
     * @param chosenBoard - board that the user is joining
     */
    public void joinBoard(User user, Board chosenBoard) {
        primaryStage.setTitle("Board Overview");
        boardOverviewCtrl.setBoard(chosenBoard);
        boardOverviewCtrl.setUserViewing(user);
        boardOverviewCtrl.assignToUser(user);
        boardOverviewCtrl.refresh();
    }

    /**
     * Method that returns to workspace and deletes the board
     * it was on previously
     */
    public void deleteBoard(){
        workspaceCtrl.clearJoinedBoards();

        if(!isAdmin){
            primaryStage.setTitle("Workspace");
            primaryStage.setScene(workspace);
            workspaceCtrl.refresh();
        } else {
            primaryStage.setTitle("Admin Workspace");
            primaryStage.setScene(workspaceAdmin);
            workspaceAdminCtrl.refresh();
        }
    }

    /**
     * Method that starts the scene showing the board
     */
    public void showBoard(Board showBoard, User userViewing) {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(board);
        boardOverviewCtrl.setBoard(showBoard);
        boardOverviewCtrl.setUserViewing(userViewing);
        boardOverviewCtrl.refresh();

    }

    public void showBoard(){
        primaryStage.setTitle("Board overview");
        primaryStage.setScene(board);
        boardOverviewCtrl.refresh();
    }

    public void showChangeTitle(Board board){
        primaryStage.setTitle("Changing Board Title");
        primaryStage.setScene(changeBoardTitle);
        changeBoardTitleCtrl.setBoard(board);
    }

    /**
     * Method that starts adding list scene
     * @param boardToAddTo - board that you add lists to
     */
    public void showAddList(Board boardToAddTo) {
        primaryStage.setTitle("Adding List");
        primaryStage.setScene(addList);
        addListCtrl.setBoardToAddTo(boardToAddTo);
    }

    /**
     * Method that starts editing list scene
     * @param boardListToEdit - BoardList to be edited
     */
    public void showEditList(BoardList boardListToEdit){
        primaryStage.setTitle("Editing List");
        primaryStage.setScene(editList);
        editListCtrl.setBoardListToEdit(boardListToEdit);
    }

    /**
     * Method that starts welcome page
     */
    public void showWelcomePage() {
        primaryStage.setTitle("Welcome Page");
        primaryStage.setScene(welcomePage);
        welcomePageCtrl.clearPassword();
    }

    /**
     * Method that starts workspace scene for the user
     * @param username - username of the user
     */
    public void showWorkspace(String username) {
        if(isAdmin)
            showAdminWorkspace(username);
        else {
            primaryStage.setTitle("Workspace");
            primaryStage.setScene(workspace);
            workspaceCtrl.setUser(username);
            workspaceCtrl.clearInviteText();

            this.username = username;
            workspaceCtrl.refresh();
        }
    }

    public String getUsername() {
        return username;
    }

    /**
     * Method that starts the editing scene for the card
     * @param cardToEdit - card to be edited
     */
    public void showEditCard(Card cardToEdit) {
        primaryStage.setTitle("Editing Card");
        primaryStage.setScene(editCard);
        editCardCtrl.setCardToEdit(cardToEdit);
    }

    public void showAdminWorkspace(String username) {
        primaryStage.setTitle("Admin Workspace");
        primaryStage.setScene(workspaceAdmin);
        workspaceAdminCtrl.setUser(username);
        this.username = username;

        workspaceAdminCtrl.refresh();
    }

    public void showTagOverview(Board board) {
        primaryStage.setTitle("Tag Overview");
        primaryStage.setScene(tagOverview);
        tagOverviewCtrl.setBoard(board);

        tagOverviewCtrl.refresh();
    }

    public void showAddTag(Board board) {
        primaryStage.setTitle("Adding Tag");
        primaryStage.setScene(addTag);
        addTagCtrl.setBoard(board);
    }

    public void showEditTag(Tag tagToEdit) {
        primaryStage.setTitle("Editing Tag");
        primaryStage.setScene(editTag);
        editTagCtrl.setTagToEdit(tagToEdit);
        editTagCtrl.refresh();
    }

    public void showAddRemoveTags(Card card) {
        primaryStage.setTitle("Choosing Tags");
        primaryStage.setScene(addRemoveTags);
        addRemoveTagsCtrl.setCardToEdit(card);
        addRemoveTagsCtrl.setTags();
    }

    public void showCustomization(Board board) {
        primaryStage.setTitle("Customizing Board");
        primaryStage.setScene(customization);
        customizationCtrl.setBoard(board);
        customizationCtrl.setValues();

    }

    public void showAddPreset(Board board) {
        primaryStage.setTitle("New Task Color");
        primaryStage.setScene(addPreset);
        addPresetCtrl.setBoard(board);
    }

    public void showSetPassword(User viewingUser, Board board){
        primaryStage.setTitle("Change/Set password");
        primaryStage.setScene(passwordSet);
        passwordSetCtrl.setBoardAndUser(viewingUser,board);
    }

    public void showEnterPassword(User viewingUser, Board board) {
        primaryStage.setTitle("Inputting password");
        primaryStage.setScene(passwordEnter);
        passwordEnterCtrl.setBoardAndUser(viewingUser,board);
    }

    public void registerForAllSockets() {
        editCardCtrl.subscribeToSocketsEditCardCtrl();
        workspaceCtrl.subscribeForSocketsWorkspace();
        workspaceAdminCtrl.subscribeForSocketsWorkspaceAdmin();
        boardOverviewCtrl.subscribeToSocketsBoardOverview();
        tagOverviewCtrl.subscribeForSocketsTagOverview();
        customizationCtrl.subscribeToWebsocketsCustomization();
        boardOverviewCtrl.subscribeToSocketsBoardOverview();
    }
}

