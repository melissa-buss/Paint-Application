package paintapp;

import java.util.Stack;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color; 
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**Toolbar... is a class that lays out the tool bar located below the menu bar and
 * above the image being manipulated. It gives the user many drawing options and
 * manipulation options regarding what they want to change about the image. The methods
 * give the tool bar functionality in the program. This class is public therefore can
 * interact with the other classes in this package.
 * 
 * @author Melissa Buss
 * @version 5.1
 * @since 9/4/17
 */

public class Toolbar{
    
    /**Creates layout of tool bar
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays the canvas and imageView
     * @param root main formatting tool in the GUI
     * @param scene used to set the scene of the stage, specifically used in this class for move method
     */
    public Toolbar(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack, BorderPane root, Scene scene){
        Rectangle rectangle = new Rectangle();
               
        MenuItem straightline = new MenuItem("Line");
        MenuItem freedraw = new MenuItem("Free Draw");
        MenuItem rect = new MenuItem("Rectangle");
        MenuItem square = new MenuItem("Square");
        MenuItem oval = new MenuItem("Oval");
        MenuItem circle = new MenuItem("Circle");
        MenuButton drawoptions = new MenuButton("Draw options", null, straightline, freedraw, rect, square, oval, circle);
        
        straightline.setOnAction((ActionEvent event) ->{
            straightline(canvas, gc, su, stack);
        });
        
        freedraw.setOnAction((ActionEvent event) ->{
            freedraw(canvas, gc, su, stack);
        });
        
        rect.setOnAction((ActionEvent event) ->{
            rectangle(canvas, gc, su, stack);
        });
        
        square.setOnAction((ActionEvent event) ->{
            square(canvas, gc, su, stack);
        });
        
        oval.setOnAction((ActionEvent event) ->{
            oval(canvas, gc, su, stack);
        });
        
        circle.setOnAction((ActionEvent event) ->{
            circle(canvas, gc, su, stack);
        });
        
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction((ActionEvent event) -> {
            gc.setStroke(colorPicker.getValue());
            gc.setFill(colorPicker.getValue());
        });
        
        Slider slider = new Slider(0, 50, 1);
        Label sliderValue = new Label(String.valueOf((int)slider.getValue()));
        sliderValue.setMinWidth(15);
        sliderValue.setMaxWidth(15);
        slider.setValue(1);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5f);
        slider.valueProperty().addListener(event ->{
            gc.setLineWidth(slider.getValue());
            sliderValue.textProperty().setValue(String.valueOf((int)slider.getValue()));
        });
        
        MenuItem select = new MenuItem("Select");
        MenuItem move = new MenuItem("Move");
        MenuItem eraser = new MenuItem("Eraser");
        MenuItem dropper = new MenuItem("Dropper");
        MenuItem text = new MenuItem("Text");
        MenuButton manipoptions = new MenuButton("Manipulation options", null, select, move, eraser, text, dropper);
        
        select.setOnAction((ActionEvent event) ->{
            select(canvas, gc, su, stack, rectangle, colorPicker, slider);
        });
        
        move.setOnAction((ActionEvent event) ->{
            move(canvas, gc, su, stack, rectangle, scene);
        });
        
        eraser.setOnAction((ActionEvent event) ->{
            eraser(canvas, gc, su, stack, slider);
        });
        
        text.setOnAction((ActionEvent event) ->{
            text(canvas, gc, su, stack, slider);
        });
        
        dropper.setOnAction((ActionEvent event) ->{
            dropper(canvas, gc, stack, colorPicker);
        });
        
        GridPane grid = new GridPane();
        grid.addRow(0, drawoptions, colorPicker, slider, sliderValue, manipoptions);
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 0, 0, 15));
        
        root.setCenter(grid);
    }
        
    /**Saves snapshot of the stack onto a new image that's pushed onto the stack
     * 
     * @param canvas used to draw over the image
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, used to snapshot
     */
    public void snapshot(Canvas canvas, Stack<Image> su, StackPane stack){
        Image image = stack.snapshot(null, null);
        su.push(image);
    }
    
    /**Clears mouse events, created to fix an error with switching between drawing options
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     */
    public void clear(Canvas canvas, GraphicsContext gc){
        canvas.setOnMouseClicked(null);
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
    }
    
    /**Draws straight line on the canvas
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     */
    public void straightline(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack){
        clear(canvas, gc);
        
        canvas.setOnMousePressed((MouseEvent event) -> {
            gc.beginPath();
            gc.lineTo(event.getX(), event.getY());
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
            gc.closePath();
            snapshot(canvas, su, stack);
        });
    }
    
    /**Draws freehand line on the canvas
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     */
    public void freedraw(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack){
        clear(canvas, gc);
        
        canvas.setOnMousePressed((MouseEvent event) ->{
            gc.beginPath();
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
        });
        
        canvas.setOnMouseDragged((MouseEvent event) ->{
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            gc.closePath();
            snapshot(canvas, su, stack);
        });
    }
    
    /**Draws rectangle on the canvas
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     */
    public void rectangle(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack){
        Rectangle rectangle = new Rectangle();
        clear(canvas, gc);
        
        canvas.setOnMousePressed((MouseEvent event) ->{
            gc.beginPath();
            rectangle.setX(event.getX());
            rectangle.setY(event.getY());
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            rectangle.setWidth(event.getX()-rectangle.getX());
            rectangle.setHeight(event.getY()-rectangle.getY());
            gc.strokeRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            gc.closePath();
            snapshot(canvas, su, stack);
        });
    }
    
    /**Draws square on the canvas
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     */
    public void square(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack){
        Rectangle rectangle = new Rectangle();
        clear(canvas, gc);
        
        canvas.setOnMousePressed((MouseEvent event) ->{
            gc.beginPath();
            rectangle.setX(event.getX());
            rectangle.setY(event.getY());
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            rectangle.setWidth(event.getX()-rectangle.getX());
            gc.strokeRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getWidth());
            gc.closePath();
            snapshot(canvas, su, stack);
        });
    }
    
    /**Draws oval on the canvas
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     */
    public void oval(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack){
        Ellipse ellipse = new Ellipse();
        clear(canvas, gc);
        
        canvas.setOnMousePressed((MouseEvent event) ->{
            gc.beginPath();
            ellipse.setCenterX(event.getX());
            ellipse.setCenterY(event.getY());            
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            gc.strokeOval(ellipse.getCenterX(), ellipse.getCenterY(), (event.getX()-ellipse.getCenterX()), event.getY()-ellipse.getCenterY());
            snapshot(canvas, su, stack);
        });
    }
    
    /**Draws circle on the canvas
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     */
    public void circle(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack){
        Ellipse ellipse = new Ellipse();
        clear(canvas, gc);
        
        canvas.setOnMousePressed((MouseEvent event) ->{
            gc.beginPath();
            ellipse.setCenterX(event.getX());
            ellipse.setCenterY(event.getY());            
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            gc.strokeOval(ellipse.getCenterX(), ellipse.getCenterY(), (event.getX()-ellipse.getCenterX()), event.getX()-ellipse.getCenterX());
            snapshot(canvas, su, stack);
        });
    }
        
    /**Selects piece of an image, created so that we can call move()
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     * @param rectangle creates rectangle object, parameter so that the values of rectangle can be used in method()
     * @param colorPicker picks the color being drawn in, parameter so we can switch it back to chosen value when done
     * @param slider picks the line width being drawn in, parameter so we can switch it back to chosen value when done
     */
    public void select(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack, Rectangle rectangle, ColorPicker colorPicker, Slider slider){
        clear(canvas, gc);
        
        canvas.setOnMousePressed((MouseEvent event) ->{
            snapshot(canvas, su, stack);
            gc.beginPath();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(0.5);
            rectangle.setX(event.getX());
            rectangle.setY(event.getY());
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            rectangle.setWidth(event.getX()-rectangle.getX());
            rectangle.setHeight(event.getY()-rectangle.getY());
            gc.strokeRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            gc.closePath();
            gc.setLineWidth(slider.getValue());
            gc.setStroke(colorPicker.getValue());
            snapshot(canvas, su, stack);
        });
    }
    
    /**Moves piece of image selected with select() method, draws it to a new spot on the canvas
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     * @param rectangle creates rectangle object, parameter so that the values of rectangle can be used in method()
     * @param scene used to set the scene of the stage
     */
    public void move(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack, Rectangle rectangle, Scene scene){
        WritableImage wr = new WritableImage((int)rectangle.getWidth(), (int)rectangle.getHeight());
        PixelWriter pw = wr.getPixelWriter();
        clear(canvas, gc);
                
        Image im = stack.snapshot(null, null);
        PixelReader pr = im.getPixelReader();
        
        for (int x=1; x<(int)(rectangle.getWidth()-1); x++){
            for (int y=1; y<(int)(rectangle.getHeight()-1); y++){
                pw.setArgb(x, y, pr.getArgb((int)rectangle.getX()+x, (int)rectangle.getY()+y));
            }
        }
        
        gc.setFill(Color.WHITE);
        gc.fillRect(rectangle.getX()-1, rectangle.getY()-1, rectangle.getWidth()+2, rectangle.getHeight()+2);
        
        Canvas c2 = new Canvas(rectangle.getWidth()+1, rectangle.getHeight()+1);
        GraphicsContext gc2 = c2.getGraphicsContext2D();
        gc2.drawImage(wr, 0, 0);
        stack.getChildren().add(c2);
        
        canvas.setOnMousePressed((MouseEvent event) ->{
            c2.setTranslateX(event.getX()-canvas.getWidth()/2);
            c2.setTranslateY(event.getY()-canvas.getHeight()/2);
        });
        
        canvas.setOnMouseDragged((MouseEvent event) ->{
            c2.setTranslateX(event.getX()-canvas.getWidth()/2);
            c2.setTranslateY(event.getY()-canvas.getHeight()/2);
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            gc.drawImage(wr, event.getX()-(rectangle.getWidth()/2), event.getY()-(rectangle.getHeight()/2));
            stack.getChildren().remove(c2);
            clear(canvas, gc);            
            snapshot(canvas, su, stack);
        });
    }
    
    /**Erases changes to the canvas, keeps image behind in tact
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     * @param slider picks the width of the eraser
     */
    public void eraser(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack, Slider slider){
        clear(canvas, gc);
        
        canvas.setOnMousePressed((MouseEvent event) -> {
            gc.beginPath();
            gc.clearRect(event.getX(), event.getY(), slider.getValue(), slider.getValue());
        });
        
        canvas.setOnMouseDragged((MouseEvent event) ->{
            gc.clearRect(event.getX(), event.getY(), slider.getValue(), slider.getValue());
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            gc.closePath();
            snapshot(canvas, su, stack);
        });
    }
    
    /**Adds text to the canvas, allows user to pick font style and size
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param su stack that holds changes to the image, used for undo
     * @param stack formatting tool that overlays canvas and imageView, parameter so that snapshot() can be called
     * @param slider picks the line width of the text, parameter so we can switch it back to chosen value when done
     */
    public void text(Canvas canvas, GraphicsContext gc, Stack<Image> su, StackPane stack, Slider slider){
        clear(canvas, gc);
        
        canvas.setOnMouseClicked((MouseEvent event) ->{            
            GridPane temp = new GridPane();
            Scene scene = new Scene(temp, 300, 135);
            Stage stage = new Stage();
            stage.setTitle("Text Input");
            stage.setScene(scene);
            stage.show();
            
            Label text = new Label("Your text: ");
            TextField input = new TextField("Hello world");
            
            Label font = new Label("Font size: ");
            TextField size = new TextField("25");
            
            Button ok = new Button("OK");
            
            Label style = new Label("Font Style: ");
            ChoiceBox choicebox = new ChoiceBox();
            choicebox.getItems().addAll(javafx.scene.text.Font.getFamilies());
            choicebox.getSelectionModel().select(216);
            
            temp.setHgap(5);
            temp.setVgap(5);
            temp.setPadding(new Insets(10, 0, 0, 10));
            temp.addColumn(0, text, font, style);
            temp.addColumn(1, input, size, choicebox, ok);
            
            ok.setOnAction((ActionEvent e) ->{
                gc.setFont(new Font((String)choicebox.getValue(), Integer.parseInt(size.getText())));
                gc.setLineWidth(1);
                gc.strokeText(input.getText(), event.getX(), event.getY());
                stage.close();
            });
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            gc.setLineWidth(slider.getValue());
            snapshot(canvas, su, stack);
        });
    }
    
    /**Changes colorPicker value to one found in the image
     * 
     * @param canvas used to draw over the image
     * @param gc gives the user drawing options
     * @param stack formatting tool that overlays canvas and imageView, parameter so that we can snapshot the image
     * @param colorPicker picks the color being drawn in, parameter so we can change the value once a new one is found
     */
    public void dropper(Canvas canvas, GraphicsContext gc, StackPane stack, ColorPicker colorPicker){
        Image im = stack.snapshot(null, null);
        PixelReader pr = im.getPixelReader();
        
        canvas.setOnMousePressed((MouseEvent event) ->{
            colorPicker.setValue(pr.getColor((int)event.getX(), (int)event.getY()));
            gc.setStroke(colorPicker.getValue());
        });
        
        canvas.setOnMouseReleased((MouseEvent event) ->{
            gc.closePath();
            clear(canvas, gc);
        });
    }
}