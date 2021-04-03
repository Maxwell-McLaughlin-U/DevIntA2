package csci2040u.assignment2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.*;
import java.io.FileInputStream;

/**
 * Main class is a child class of Application (an abstract class).
 * This class builds the components and displays the JavaFX application.
 */
public class Main extends Application {
    private TableView<TestFile> table = new TableView<TestFile>();

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
        myGrid.setVgap(10);
        myGrid.setPadding(new javafx.geometry.Insets(25, 25,  25, 25));
        Scene scene = new Scene(new Group(),Color.rgb(110, 115, 119, .99));
        stage.setTitle("Spam Destroyer");
        stage.setWidth(680);  //Setting the width of the JavaFx application
        stage.setHeight(650); //Setting the height of the JavaFx application

        /**
         *DirectoryChooser asks the user to choose a directory immediately when
         * the JavaFX application starts
         */
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(stage);
        String strDirectory = mainDirectory.getPath(); //The path to the directory opened is stored

        /**
         * FileInputStream obtains input bytes from a file in a file system
         * Is used to read in the an image for the header logo for the
         * JavaFX application.
         * The image is resized to fit in appropriately on screen with
         * width 100 and height 400.
         */
        FileInputStream input = new FileInputStream("resources/images/spamLogo.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(400);

        /**
         * Creating Text objects to add onto the grid pane.
         * Text objects are used to display the texts "Accuracy" and "Precision"
         * on JavaFX application.
         */
        Text accuracyText = new Text();
        accuracyText.setText("Accuracy:");
        accuracyText.setFont(Font.font ("Verdana", 12)); //Setting font of text to Verdana and size to 12
        accuracyText.setFill(Color.BLACK);                    //Setting colour of text to black

        Text precisionText = new Text();
        precisionText.setText("Precision:");
        precisionText.setFont(Font.font ("Verdana", 12)); //Setting font of text to Verdana and size to 12
        precisionText.setFill(Color.BLACK);                     //Setting colour of text to black

        /**
         * Creating objects of TextField to display the accuracy and percision
         * of the spam detector once it has read each file word by word in the
         * data/testing directory.
         */
        TextField accuracyField = new TextField();
        accuracyField.setEditable(false);
        TextField precisionField = new TextField();
        precisionField.setEditable(false);

        /**
         * Setting up the columns with appropriate names, sizes and the data to print in each cell.
         * Used to create three columns for File Name, Accuracy Class, and Spam Probability.
         */
        TableColumn Filename = new TableColumn("File Name");
        Filename.setMinWidth(300);
        Filename.setCellValueFactory(new PropertyValueFactory<TestFile, String>("Filename"));


        TableColumn ActualClass = new TableColumn("Actual Class");
        ActualClass.setMinWidth(100);
        ActualClass.setCellValueFactory(new PropertyValueFactory<TestFile, String>("ActualClass"));

        TableColumn SpamProbability = new TableColumn("Spam Probability");
        SpamProbability.setMinWidth(200);
        SpamProbability.setCellValueFactory(new PropertyValueFactory<TestFile, String>("SpamProbRounded"));

        //Creating an object from TestObjects class to access the TestFile objects it created
        TestObjects tester = new TestObjects();

        /**
         *Setting the table with the objects created from TestObjects class
         *and adding the three columns to the table object created with TableColumn class.
         */
        table.setItems(tester.getSpamData(strDirectory));
        table.getColumns().addAll(Filename,ActualClass,SpamProbability);

        //Add the image, text, textField and table components onto the Grid (myGrid) pane
        myGrid.add(imageView, 2,0);
        myGrid.add(accuracyText, 2,50);
        myGrid.add(accuracyField, 3, 50);
        myGrid.add(precisionText, 2,51);
        myGrid.add(precisionField, 3,51);
        myGrid.add(table, 2,1);

        //Displaying the accuracy calculated in TestObjects to the TextField beside text "Accuracy:"
        accuracyField.setText(String.valueOf(tester.accuracy));
        //Displaying the precision calculated in TestObjects to the TextField beside text "Precision:"
        precisionField.setText(String.valueOf(tester.precision));

        /**
         * Displaying the table by creating a padding with
         * and setting a vertical spacing of 5 units. Adding image
         * and table to the padding.
         */
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(15, 0, 0, 10));
        vbox.getChildren().addAll(imageView,table);

        //Adding the Grid Pane and padding to the scene
        ((Group) scene.getRoot()).getChildren().addAll(myGrid, vbox);

        //Setting the scene to the stage
        stage.setScene(scene);
        //Displaying the components
        stage.show();
    }

    /**
     * Prints out "Hello World"
     * and the command line arguments.
     * @param arg A string array containing the command line arguments.
     * @return No return value.
     */
    public static void main(String[] args) {
        launch(args);
    }

}