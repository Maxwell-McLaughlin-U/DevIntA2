package csci2040u.assignment2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * Main class is a child class of Application (an abstract class).
 * This class builds the components and displays the JavaFX application.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        /**
         * Setting up a Grid Pane layout to create a "flexible
         * grid of rows and columns in which to lay out controls."
         * (https://docs.oracle.com/javafx/2/get_started/fxml_tutorial.htm)
         * The grid is centered with 10 units horizontal and vertical gaps.
         */
        GridPane myGrid = new GridPane();
        myGrid.setAlignment(Pos.CENTER);
        myGrid.setHgap(10);
        myGrid.setVgap(5);
        myGrid.setPadding(new javafx.geometry.Insets(25, 25,  25, 25));
        Scene scene = new Scene(new Group(),Color.rgb(199, 226, 255, .99));
        stage.setTitle("File Sharer");
        stage.setWidth(580);  //Setting the width of the JavaFx application
        stage.setHeight(600); //Setting the height of the JavaFx application

        /**
         * FileInputStream obtains input bytes from a file in a file system
         * Is used to read in the an image for the header logo for the
         * JavaFX application.
         * The image is resized to fit in appropriately on screen with
         * width 150 and height 270.
         */
        FileInputStream input = new FileInputStream("resources/images/fileSharerLogo.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setFitWidth(270);

        //Setting up a splitPane for the JavaFx application
        SplitPane splitPane = new SplitPane();

        /**
         * Creating a string of ListView objects to display all the files in the
         * client server.
         */
        ListView<String> clientListView = new ListView<>();
        clientListView.getItems().setAll("Client Server Content", "testFileRemote.txt", "localFile2.java", "localFile3.txt");

        /**
         * Setting the CellFactory for the ListView to customize the first cell of the ListView.
         * Used: https://stackoverflow.com/questions/53028313/how-can-i-change-the-background-color-of-only-the-first-cell-in-listview-in-java
         */
        clientListView.setCellFactory(list -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    //Using super on updateItem (a function from abstract class)
                    super.updateItem(item, empty);
                    //Checking to see if cell is empty with no text
                    if (empty) {
                        setGraphic(null);
                    }
                    //If not then get then format the first cell
                    else {
                        //Get the first text in the first cell
                        if (item.equalsIgnoreCase(list.getItems().get(0))) {
                            //Setting background color to grey and the text to black
                            setStyle("-fx-background-color: grey; -fx-text-fill: black");
                            //Setting the font of text to Verdana
                            setFont(Font.font("Verdana"));
                        }
                        //Setting text to the cell
                        setText(item);

                    }
                }
            };
            return cell;
        });

        //Allow for multiple contents to be selected from the list in client server side
        clientListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ListView<String> serverListView = new ListView<>();
        serverListView.getItems().setAll("Shared Server Content", "sharedFile.txt");

        /**
         * Setting the CellFactory for the ListView to customize the first cell of the ListView.
         * Used: https://stackoverflow.com/questions/53028313/how-can-i-change-the-background-color-of-only-the-first-cell-in-listview-in-java
         */
        serverListView.setCellFactory(list -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    //Using super on updateItem (a function from abstract class)
                    super.updateItem(item, empty);
                    //Checking to see if cell is empty with no text
                    if (empty) {
                        setGraphic(null);
                    }
                    //If not then get then format the first cell
                    else {
                        //Get the first text in the first cell
                        if (item.equalsIgnoreCase(list.getItems().get(0))) {
                            //Setting background color to grey and the text to black
                            setStyle("-fx-background-color: grey; -fx-text-fill: black");
                            //Setting the font of text to Verdana
                            setFont(Font.font("Verdana"));
                        }
                        //Setting text to the cell
                        setText(item);

                    }
                }
            };
            return cell;
        });
        //Allow for multiple contents to be selected from the list in shared server server side
        serverListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /*
          Adding the server and client server ListViews to the split pane,
          with the serverList on the left and clientServerList on the right
         */
        splitPane.getItems().addAll(serverListView, clientListView);

        //Making buttons for download and upload and setting their actions
        Button downloadButton = new Button();
        downloadButton.setText("Download");
        downloadButton.setOnAction(e->{
            download();
        });
        Button uploadButton = new Button();
        uploadButton.setText("Upload");
        uploadButton.setOnAction(e->{
            upload();
        });


        //Add the image, splitPane, upload button and download button onto the Grid (myGrid) pane
        myGrid.add(imageView, 2,0);
        myGrid.add(splitPane, 2,14);
        myGrid.add(uploadButton,1,92);
        myGrid.add(downloadButton,2,92);


        /**
         * Displaying the image by creating a vertical box
         * and setting a padding to set location. Adding the
         * image to the vertical box.
         */
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(0, 0, 0, 0));
        vbox.getChildren().addAll(imageView);

        /**
         * Displaying the splitPane by creating a vertical box
         * and setting a padding to set location. Adding the
         * splitPane to the vertical box.
         */
        final VBox vbox2 = new VBox();
        vbox2.setSpacing(5);
        vbox2.setPadding(new Insets(105, 0, 0, 30));
        vbox2.getChildren().addAll(splitPane);

        //Adding the Grid Pane and vertical boxes to the scene
        ((Group) scene.getRoot()).getChildren().addAll(myGrid, vbox, vbox2);

        //Setting the scene to the stage
        stage.setScene(scene);
        //Displaying the components
        stage.show();
    }

    public void download(){

    }
    public void upload(){

    }

    /**
     * Prints out Displays Java Application
     * @param args A string array containing the command line arguments.
     * @return No return value.
     */
    public static void main(String[] args) throws IOException {
        var socket = new Socket("localhost", 8080);
        ClientServer client = new ClientServer(socket);
        launch(args);
    }

}