package  paintapp;

import java.io.File;
import java.util.Stack;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * PaintApp... Is the main file for the application. The file interacts with the others
 * in the package. This class is responsible for running the GUI. The class contains
 * variables that come together to layout and build a GUI capable of performing many
 * functions similar to those of Microsoft Paint.
 * 
 * @author Melissa Buss
 * @version 5.1
 * @since 9/4/17
 */

public class PaintApp extends Application {
    File file;
    
    /**
     * Creates the GUI that will be launched
     * 
     * @param primaryStage The stage to be created.
     */
    @Override
    public void start(Stage primaryStage) {
        ImageView imageView = new ImageView();  
        Canvas canvas = new Canvas(600, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        BorderPane root = new BorderPane();
        StackPane stack = new StackPane();        
        Scene scene = new Scene(root, 600, 600);
        Stack<Image> su = new Stack();
        Stack<Image> sr = new Stack();
        
        file = new File("https://i.ytimg.com/vi/eq7Adzo4QAE/maxresdefault.jpg");
        Image image = new Image("https://i.ytimg.com/vi/eq7Adzo4QAE/maxresdefault.jpg");
        su.push(image);
        imageView.setImage(image);
        imageView.setFitHeight(500);
        imageView.setFitWidth(600);
        
        MyMenubar myMenubar = new MyMenubar(canvas, gc, file, imageView, su, sr, stack, root, primaryStage);
        Toolbar toolbar = new Toolbar(canvas, gc, su, stack, root, scene);
        root.setBottom(stack);
        stack.getChildren().addAll(imageView, canvas);
        
        primaryStage.setTitle("Paint");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * 
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}