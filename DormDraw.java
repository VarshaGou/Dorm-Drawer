//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    Customizable Dorm Visualizer
// Course:   CS 300 Spring 2024
//
// Author:   Varsha Gouraram
// Email:    gouraram@wisc.edu
// Lecturer: Hobbes LeGault
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// Persons:         TA Rahul helped me with my keys pressed code
// Online Sources:  NONE
//
///////////////////////////////////////////////////////////////////////////////
import java.io.File;
import processing.core.PImage;

/**
 * This class uses a GUI to draw a dorm room and allows the user to customize
 * furniture and placement.
 */
public class DormDraw {
    // PImage object that represents the background image
    private static PImage backgroundImage;
    // non-compact perfect size array storing dorm symbols added to the display window
    private static Symbol[] symbols;

    /**
     * Creates and initializes the different data fields defined in your
     * program, and configures the different graphical settings of your application, such as
     * loading the background image, setting the dimensions of the display window, etc.
     */
    public static void setup(){
        // set the background image to the file background.png
        backgroundImage = Utility.loadImage("images" + File.separator + "background.png");
        //creates an array size 12 for the symbols to populate and depopulate
        symbols = new Symbol[12];
    }

    /**
     * Continuously draws the application display window and updates its content
     * with respect to any change or any event which affects its appearance.
     */
    public static void draw(){
        //sets the background color
        Utility.background(Utility.color(150,0,200));
        //Draw the background image at the center of the screen
        Utility.image(backgroundImage, Utility.width()/2, Utility.height()/2);

        //draws the symbols in the array symbols on the screen
        for(int i = 0; i < symbols.length; i++){
            if(symbols[i] != null) {
                symbols[i].draw();
            }
        }
    }

    /**
     * Adds the symbol passed through as a parameter into the array that is also
     * passed through as a parameter into the first null space and then calls
     * the draw method. If array is full, method does nothing.
     */
    public static void addSymbol(Symbol[] symbols, Symbol toAdd) {
        //finds the first null element in the array and sets it to the symbol we
        //want to add. Then calls the draw method to draw symbol on screen.
        for(int i = 0; i < symbols.length; i++){
            if(symbols[i] == null){
                symbols[i] = toAdd;
                return;
            }
        }
    }

    /**
     * Adds a new symbol (bed, chair, desk, dresser, plant, rug, or
     * sofa) based on a specific letter key (not case sensitive) pressed,
     * to a non-full array.
     */
    public static void keyPressed() {
        char userKeyPressed = Utility.key();
        int nullCounter = 0;
        char lowerCaseUserKeyPressed = Character.toLowerCase(userKeyPressed);

        //counts the numbers of null elements in the array
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] == null) {
                nullCounter++;
            }
        }

        //add the symbol that corresponds with each key pressed defined at the
        //position where the user's mouse is if the array is not full.
        if(nullCounter > 0){

            switch (lowerCaseUserKeyPressed){
                case 'b':
                    addSymbol(symbols, new Symbol("bed.png",Utility.mouseX(),Utility.mouseY()));
                    break;

                case 'c':
                    addSymbol(symbols, new Symbol("chair.png",Utility.mouseX(),Utility.mouseY()));
                    break;

                case 'd':
                    addSymbol(symbols, new Symbol("dresser.png",Utility.mouseX(),Utility.mouseY()));
                    break;

                case 'k':
                    addSymbol(symbols, new Symbol("desk.png",Utility.mouseX(),Utility.mouseY()));
                    break;

                case 'f':
                    addSymbol(symbols, new Symbol("sofa.png",Utility.mouseX(),Utility.mouseY()));
                    break;

                case 'g':
                    addSymbol(symbols, new Symbol("rug.png",Utility.mouseX(),Utility.mouseY()));
                    break;

                case 'p':
                    addSymbol(symbols, new Symbol("plant.png",Utility.mouseX(),Utility.mouseY()));
                    break;

                //If the user presses the key r or R and their mouse is over a symbol and there element is
                // not null, then the user can rotate the symbol.
                case 'r':
                for(int i = 0; i<symbols.length; i++) {
                    if(isMouseOver(symbols[i]) && symbols[i] != null) {
                        symbols[i].rotate();
                        break;
                    }
                }
                break;

                //If the user presses the backspace and their mouse is over a symbol and there element is
                // not null, then the user can delete the symbol.
                case Utility.BACKSPACE:
                    for(int i = 0; i<symbols.length; i++) {
                        if(isMouseOver(symbols[i]) && symbols[i] != null) {
                            symbols[i] = null;
                            break;
                        }
                    }
                    break;

                //If the user presses the key s or S  and their mouse is over a symbol and there element is
                // not null, then the user can save their dorm design to a file names dormDraw.png
                case 's':
                    Utility.save("dormDraw.png");
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * Checks the user's mouse is positioned over a symbol.
     *
     * @param symbol - the symbol that we are focusing on
     * @return true if the method is over the input symbol
     */
    public static boolean isMouseOver(Symbol symbol) {
        //check if the symbol inputted exists
        if(symbol == null ){
            return false;
        }

        //variable declarations & initializations
        int xPosSymbol = symbol.x(); //param x
        int yPosSymbol = symbol.y(); //param y
        int symbolWidth = symbol.width(); //image width
        int symbolHeight = symbol.height(); //image height
        double xLowerBound = 0.0;
        double xUpperBound = 0.0;
        double yLowerBound = 0.0;
        double yUpperBound = 0.0;


        //calculate x range of the symbol
        xLowerBound = xPosSymbol - (symbolWidth / 2.0);
        xUpperBound = xPosSymbol + (symbolWidth / 2.0);

        //calculate y range of the symbol
        yLowerBound = yPosSymbol - (symbolHeight / 2.0);
        yUpperBound = yPosSymbol + (symbolHeight / 2.0);

        //check if Mouse's position is between the L/R and Top/Bottom bounds of symbol
        if(Utility.mouseX() > xLowerBound && Utility.mouseX() < xUpperBound &&
                Utility.mouseY() > yLowerBound && Utility.mouseY() < yUpperBound ){
            return true;
        }

        return false;
    }

    /**
     * Callback method called each time the user presses the mouse.
     */
    public static void mousePressed() {
     //Use a loop to traverse the symbols array and when the element that the user
    // clicks on with their mouse is not null then the user can drag the symbol
    //around the dorm.
        for(int i = 0; i<symbols.length; i++){
            if(symbols[i] != null && isMouseOver(symbols[i])){
                symbols[i].startDragging();
                return;
            }
        }
    }

    /**
     * Callback method called each time the mouse is released.
     */
    public static void mouseReleased(){
        //when the mouse released and the element in the array is not null then the
        // item stops dragging.
        for(int j = 0; j<symbols.length; j++){
            if(symbols[j] != null){
                symbols[j].stopDragging();
            }
        }
    }

    /**
     * Has one line of code which starts the application.
     *
     * @param args unused
     */
    public static void main(String[] args){
        Utility.runApplication(); // starts the application
    }
}