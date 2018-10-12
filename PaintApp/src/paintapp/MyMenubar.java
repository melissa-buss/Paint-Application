package paintapp;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * MyMenuBar... is a class that lays out a menu bar located at the top of the GUI
 * that gives the user many options of what they wish to do with the picture. The
 * class is full of methods that give the menu bar functionality in the program.
 * This class is public therefore can interact with the other classes in this package.
 * 
 * @author Melissa Buss
 * @version 5.1
 * @since 9/4/17
 */

public class MyMenubar{
    
    /**Creates layout of menu bar
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param file holds path of the image being worked with
     * @param imageView displays the image in the GUI
     * @param su stack that holds changes to the image, used for undo
     * @param sr stack that holds changes to the image, used for redo
     * @param stack formatting tool that overlays the canvas and imageView
     * @param root main formatting tool in the GUI
     * @param primaryStage stage the menu bar is being added to
     */
    public MyMenubar(Canvas canvas, GraphicsContext gc, File file, ImageView imageView, Stack<Image> su, Stack<Image> sr, StackPane stack, BorderPane root, Stage primaryStage){
        FileChooser fileChooser = new FileChooser();
        
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Menu mfile = new Menu("File");
        Menu medit = new Menu("Edit");
        
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem saveas = new MenuItem("Save as");
        
        open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        open.setOnAction((ActionEvent event) ->{
            open(canvas, file, fileChooser, imageView, su, primaryStage);
        });
        
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        save.setOnAction((ActionEvent event) ->{
            save(canvas, file, stack);
        });
        
        saveas.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        saveas.setOnAction((ActionEvent event) ->{
            saveas(canvas, file, fileChooser, stack, primaryStage);
        });
        
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        MenuItem clear = new MenuItem("Clear Canvas");
        MenuItem fill = new MenuItem("Fill Canvas");
        
        undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        undo.setOnAction((ActionEvent event) ->{
            undo(gc, su, sr);
        });
        
        redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        redo.setOnAction((ActionEvent event) ->{
            redo(gc, su, sr);
        });
        
        clear.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
        clear.setOnAction((ActionEvent event) -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });
        
        fill.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        fill.setOnAction((ActionEvent event) ->{
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });
        
        menuBar.getMenus().addAll(mfile, medit);
        mfile.getItems().addAll(open, save, saveas);
        medit.getItems().addAll(undo, redo, clear, fill);
        
        root.setTop(menuBar);
        
        primaryStage.setOnCloseRequest(WindowEvent ->{
            smartsave(canvas, file, fileChooser, stack, primaryStage, WindowEvent);
        });
    }
    
    /**Allows user to open an image from their library
     * 
     * @param canvas used to draw over the image, called so it be resized to the size of the image
     * @param file holds path of the image being worked with
     * @param fileChooser allows user to pick the file they want to work with
     * @param imageView displays the image in the GUI
     * @param su stack that holds changes to the image, used for undo
     * @param primaryStage stage the menu bar is being added to
     */
    public void open(Canvas canvas, File file, FileChooser fileChooser, ImageView imageView, Stack<Image> su, Stage primaryStage){
        file = fileChooser.showOpenDialog(primaryStage);
        Image image1 = new Image(file.toURI().toString(), canvas.getWidth(), canvas.getHeight(), true, false);
        su.push(image1);
        imageView.setImage(image1);
        imageView.setPreserveRatio(true);
        canvas.setWidth(image1.getWidth());
        canvas.setHeight(image1.getHeight());
    }
    
    /**Saves image using the file path stored
     * 
     * @param canvas used to draw over the image
     * @param file holds path of the image being worked with
     * @param stack overlays canvas and image, used to snapshot what has been drawn on the image
     */
    public void save(Canvas canvas, File file, StackPane stack){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter one = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        try {
            WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
            stack.snapshot(null, writableImage);
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        }
        catch (IOException ex) {
            Logger.getLogger(PaintApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**Allows user to save the image as a new file
     * 
     * @param canvas used to draw over the image
     * @param file holds path of the image being worked with
     * @param fileChooser allows user to pick where they want to store the image
     * @param stack overlays canvas and image, used to snapshot what has been drawn on the image
     * @param primaryStage stage the menu bar is being added to
     */
    public void saveas(Canvas canvas, File file, FileChooser fileChooser, StackPane stack, Stage primaryStage){
        FileChooser.ExtensionFilter one = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        FileChooser.ExtensionFilter two = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter three = new FileChooser.ExtensionFilter("bmp files (*.bmp)", "*.bmp");
        fileChooser.getExtensionFilters().addAll(one, two, three);
        file = fileChooser.showSaveDialog(primaryStage);
        try {
            WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
            stack.snapshot(null, writableImage);
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        }
        catch (IOException ex) {
            Logger.getLogger(PaintApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**Lets user undo the drawing or manipulation functions they have done to the image
     * 
     * @param gc gives the user drawing options, draws image to a new image object
     * @param su stack that holds changes to the image, used for undo
     * @param sr stack that holds changes to the image, used for redo
     */
    public void undo(GraphicsContext gc, Stack<Image> su, Stack<Image> sr){
        if(!su.empty()){
            Image im = su.pop();
            gc.drawImage(im, 0, 0);
            sr.push(im);
        }
    }
    
    /**Lets user redo what they have previously undone
     * 
     * @param gc gives the user drawing options, draws image to a new image object
     * @param su stack that holds changes to the image, used for undo
     * @param sr stack that holds changes to the image, used for redo
     */
    public void redo(GraphicsContext gc, Stack<Image> su, Stack<Image> sr){
        if(!sr.empty()){
            Image im = sr.pop();
            gc.drawImage(im, 0, 0);
            su.push(im);
        }
    }
    
    /**Prompts user to save before their work is lost on close
     * 
     * @param canvas used to draw over the image
     * @param file holds path of the image being worked with
     * @param fileChooser allows user to pick where they want to store the image
     * @param stack overlays canvas and image, used to snapshot what has been drawn on the image
     * @param primaryStage stage the menu bar is being added to
     * @param we used to close the GUI if user desires
     */
    public void smartsave(Canvas canvas, File file, FileChooser fileChooser, StackPane stack, Stage primaryStage, Event we){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Would you like to save your work?");
            
        ButtonType dont = new ButtonType("Don't Save");
        ButtonType smartsave = new ButtonType("Save");
        ButtonType cancel = new ButtonType("Cancel");
            
        alert.getButtonTypes().setAll(dont, smartsave, cancel);
          
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == dont){
            System.exit(0);
        }
        if(result.get() == smartsave){
            FileChooser.ExtensionFilter one = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            FileChooser.ExtensionFilter two = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter three = new FileChooser.ExtensionFilter("bmp files (*.bmp)", "*.bmp");
            fileChooser.getExtensionFilters().addAll(one, two, three);
                
            file = fileChooser.showSaveDialog(primaryStage);
            if (file!= null){
                try{
                    WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                    stack.snapshot(null, writableImage);
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                }
                catch (IOException ex){
                    Logger.getLogger(PaintApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if(result.get() == cancel){
            we.consume();
        }
        else;
    }
}