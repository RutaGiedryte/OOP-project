package client.scenes;

import client.utils.ServerUtils;
import java.util.*;
import com.google.inject.Inject;
import commons.Board;
import commons.BoardList;
import commons.Card;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;


public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<BoardList> data;

    private Board board;

    @FXML
    private FlowPane mainBoard;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
        board = server.getBoardByID(1);
        //Setting the first board as the main board
        //I tried to get the first boards of all boards but didn't work
    }





    public void addList() {
        mainCtrl.showAddList(board);
    }

    /**
     * @param listLoader the loader to load @FXML contents
     * @param currentList the list to be associated to the controller
     * @return the list controller with the associated boardList
     */
    private ListCtrl createListObject(FXMLLoader listLoader, BoardList currentList){
        ListCtrl listObjectController = listLoader.getController();
        ///Instantiating a new list
        listObjectController.setBoardList(currentList);
        ///Attaching the boardList object to the listObjectController
        listObjectController.setListTitleText(currentList.title);
        //Setting the title of the list
        listObjectController.setServerAndCtrl(server,mainCtrl);
        //Setting the server and  ctrl because I have no idea how to inject it
        return listObjectController;
    }

    /**
     * @param cardLoader the loader to load @FXML contents
     * @param currentCard the card to be associated to the ctrl
     * @return the cardCtrl with the associated card
     */
    private CardCtrl createCardObject(FXMLLoader cardLoader, Card currentCard){
        CardCtrl cardObjectController = cardLoader.getController();
        //Instantiating a new card
        cardObjectController.setCard(currentCard);
        ///Attaching the card object to the cardCtrl
        cardObjectController.setCardTitleText(currentCard.title);
        //Setting the title of the card
        return cardObjectController;
    }

    // Drag&Drop methods

    // method that activates snapshot image being available while dragging the card
    private void addPreview(final FlowPane board, final HBox card){
        ImageView imageView = new ImageView(card.snapshot(null, null));
        imageView.setManaged(false);
        imageView.setMouseTransparent(true);
        board.getChildren().add(imageView);
        board.setUserData(imageView);
        board.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.relocate(event.getX(), event.getY());
            }
        });
    }

    // method that stops showing preview when dragging is finished
    private void removePreview(final FlowPane board){
        board.setOnMouseDragged(null);
        board.getChildren().remove(board.getUserData());
        board.setUserData(null);
    }
    // method that highlights the card when it is dragged and dropped
    private void setDragAndDropEffect(final HBox card){
        String initialStyle = card.getStyle();

        card.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                card.setStyle("-fx-background-color: #ffffa0;");
            }
        });

        card.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                card.setStyle(initialStyle);
            }
        });

    }

    private void adjustCards(final FlowPane board, int indexInitialList, int indexFinalList, int indexCardDragged, int indexCardsDropped){

        BoardList initialList = data.get(indexInitialList);
        BoardList finalList = data.get(indexFinalList);
        Card card = initialList.getCardByIndex(indexCardDragged);

        server.deleteCard(card.getId());
        card.setIndex(indexCardsDropped);

        if(indexInitialList == indexFinalList){
            card.setList(initialList);
            server.addCard(card);
        }else{
            card.setList(finalList);
            server.addCard(card);
        }

        refresh();

    }


    // sets drag and drop feature to cards
    private void addDragAndDrop(final FlowPane board, final VBox list, final HBox card){
        VBox cardsSection = (VBox) list.getChildren().get(2);
        card.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                card.startFullDrag();
                addPreview(board, card);
            }
        });
        // add style effects
        setDragAndDropEffect(card);
        card.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                card.setStyle("");

                removePreview(board);


                Node initial = (Node) event.getGestureSource();
                Node initialCardsSection = initial.getParent();
                Node initialList = initialCardsSection.getParent();
                int indexOfInitialList = board.getChildren().indexOf(initialList);

                Node target = (Node) event.getSource();
                Node targetCardsSection = target.getParent();
                Node targetList = targetCardsSection.getParent();
                int indexOfList = board.getChildren().indexOf(targetList);

                //System.out.println(event.getSource());

                System.out.println("*********************************************");
                System.out.println("Index of List: "+ indexOfList);
                System.out.println("Index of Initial List: "+ indexOfInitialList);
                System.out.println("*********************************************");

                HBox draggedLabel = (HBox) initial;
                VBox draggedCardsSection = (VBox) initialCardsSection;

                VBox droppedCardsSection = (VBox) targetCardsSection;
                int indexOfDraggingNode = draggedCardsSection.getChildren().indexOf(draggedLabel);
                int indexOfDropTarget = droppedCardsSection.getChildren().indexOf(target);

                System.out.println(droppedCardsSection.getChildren().indexOf(target));
                System.out.println("*********************************************");
                System.out.println("Index of card dragged: "+ indexOfDraggingNode);
                System.out.println("Index of card dropped: "+ indexOfDropTarget);
                System.out.println("*********************************************");

                //rotateCards(board, indexOfInitialList, indexOfList, indexOfDraggingNode, indexOfDropTarget);
                adjustCards(board, indexOfInitialList, indexOfList, indexOfDraggingNode, indexOfDropTarget);
                event.consume();
            }
        });

        cardsSection.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                System.out.println("CardsSection");


                Node parent = (Node) event.getGestureSource();
                parent = parent.getParent();
                parent = parent.getParent();
                removePreview(board);

                int indexOfInitialList = board.getChildren().indexOf(parent);
                int indexOfList = board.getChildren().indexOf(list);

                System.out.println("*********************************************");
                System.out.println("Index of List: "+ indexOfList);
                System.out.println("Index of Initial List: "+ indexOfInitialList);
                System.out.println("*********************************************");

                HBox draggedLabel = (HBox) event.getGestureSource();
                VBox draggedCardsSection = (VBox) draggedLabel.getParent();
                int indexOfDraggingNode = draggedCardsSection.getChildren().indexOf(event.getGestureSource());

                System.out.println("*********************************************");
                System.out.println("Index of card dragged: "+ indexOfDraggingNode);
                System.out.println("*********************************************");

                //insertCard(board, indexOfInitialList, indexOfList, indexOfDraggingNode);
                adjustCards(board, indexOfInitialList, indexOfList, indexOfDraggingNode, cardsSection.getChildren().size());
                event.consume();


            }
        });
        list.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                removePreview(board);
                System.out.println("List");

                Node initial = (Node) event.getGestureSource();
                Node initialCardsSection = initial.getParent();
                Node initialList = initialCardsSection.getParent();
                int indexOfInitialList = board.getChildren().indexOf(initialList);

                Node targetList = (Node) event.getSource();
                int indexOfList = board.getChildren().indexOf(targetList);

                HBox draggedLabel = (HBox) initial;
                VBox draggedCardsSection = (VBox) initialCardsSection;
                int indexOfDraggingNode = draggedCardsSection.getChildren().indexOf(draggedLabel);
                event.consume();

                adjustCards(board, indexOfInitialList, indexOfList, indexOfDraggingNode, cardsSection.getChildren().size()-1);


            }
        });

        for(int i = 0;i<list.getChildren().size();i++){
            if(i!=0){
                Node item = list.getChildren().get(i);
                item.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
                    @Override
                    public void handle(MouseDragEvent event) {
                        removePreview(board);
                    }
                });

            }
            else{

                HBox item = (HBox) list.getChildren().get(i);
                for(int j = 0;j<item.getChildren().size();j++){
                    Node itemChild = item.getChildren().get(j);
                    itemChild.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
                        @Override
                        public void handle(MouseDragEvent event) {
                            removePreview(board);
                        }
                    });
                }

            }
        }

        board.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {


            @Override
            public void handle(MouseDragEvent event) {

                removePreview(board);
                System.out.println("Board");

            }
        });


    }

    // end of Drag&Drop

    public void refresh() {
        try {
            mainBoard.getChildren().clear();
            var lists = server.getBoardLists();
            data = FXCollections.observableList(lists);
            for (BoardList currentList : data) {
                FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
                Node listObject = listLoader.load();
                ListCtrl listObjectController = createListObject(listLoader,currentList);
                ObservableList<Card> cardsInList =
                    FXCollections.observableList(server.getCards(currentList.id));

                Collections.sort(cardsInList, (s1, s2) -> { return s1.index-s2.index; });
                currentList.setCards(cardsInList);
                for (Card currentCard : cardsInList) {
                    FXMLLoader cardLoader = new FXMLLoader((getClass().getResource("Card.fxml")));
                    Node cardObject = cardLoader.load();
                    CardCtrl cardObjectController = createCardObject(cardLoader,currentCard);
                    listObjectController.addCardToList(cardObject);
                    //Adding the card to the list
                }
                ///This can be done with the ID of the list
                listObjectController.getListAddCardButton().
                        setOnAction(event -> mainCtrl.showAddCard(currentList));
                mainBoard.getChildren().add(listObject);

                VBox cardsBox = (VBox) ((VBox)listObject ).getChildren().get(2);
                for(int i =0;i<cardsBox.getChildren().size();i++){
                    addDragAndDrop(mainBoard, (VBox) listObject, (HBox) cardsBox.getChildren().get(i));
                }
            }
        } catch (Exception e){
            System.out.print("IO Exception");
        }
    }

    public void disconnectFromServer() {
        mainCtrl.showWelcomePage();
    }
}