// Version 2.0

// Points for a cube: 25 25 25 25 25 -25 25 -25 25 -25 25 25 -25 -25 25 -25 25 -25 25 -25 -25 -25 -25 -25

// Edges for a cube: 1 2 1 3 1 4 2 6 2 7 3 5 3 7 4 5 4 6 5 8 6 8 7 8

// Points for a square hourglass: 25 25 25 25 25 -25 -25 25 -25 -25 25 25 10 0 10 10 0 -10 -10 0 -10 -10 0 10 25 -25 25 25 -25 -25 -25 -25 -25 -25 -25 25

// Edges for a square hourglass: 1 2 2 3 3 4 1 4 5 6 6 7 7 8 5 8 9 10 10 11 11 12 9 12 1 5 5 9 2 6 6 10 3 7 7 11 4 8 8 12

// Points for a pyramid: 25 -25 25 25 -25 -25 -25 -25 -25 -25 -25 25 0 25 0

// Edges for a pyramid: 1 2 2 3 3 4 1 4 1 5 2 5 3 5 4 5

// Points for a dodecahedron: -12.361 0 32.36 12.361 0 32.36 20 20 20 32.36 12.361 0 20 20 -20 12.361 0 -32.36 -12.361 0 -32.36 -20 -20 -20 -32.36 -12.361 0 -20 -20 20 -32.36 12.361 0 -20 20 20 0 32.36 12.361 0 32.36 -12.361 -20 20 -20 20 -20 -20 32.36 -12.361 0 0 -32.36 -12.361 0 -32.36 12.361 20 -20 20

// Edges for a dodecahedron: 1 2 2 3 3 4 4 5 5 6 6 7 7 8 8 9 9 10 1 10 1 12 12 13 11 12 9 11 11 15 7 15 14 15 5 14 13 14 10 19 19 20 18 19 8 18 16 18 6 16 16 17 4 17 17 20 2 20 3 13

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;


public class Renderer3D {
    static int playerPos = 0;
    static char blank = '⬜';
    static char filled = '▉';
    static String colorCode;
    static String colorBack;
    final static String colorReset = "\u001B[0m";
    private static String ttyConfig;
    final static int DIM = 100;
    final static int ZOOM = 2;    
    
    // Preset vertice and edge values for all shapes
    static double[] cubeVerts = {25, 25, 25, 25, 25, -25, 25, -25, 25, -25, 25, 25, -25, -25, 25, -25, 25, -25, 25, -25, -25, -25, -25, -25};
    static double[] pyramidVerts = {25, -25, 25, 25, -25, -25, -25, -25, -25, -25, -25, 25, 0, 25, 0};
    static double[] hourglassVerts = {25, 25, 25, 25, 25, -25, -25, 25, -25, -25, 25, 25, 10, 0, 10, 10, 0, -10, -10, 0, -10, -10, 0, 10, 25, -25, 25, 25, -25, -25, -25, -25, -25, -25, -25, 25};
    static double[] dodecahedronVerts = {-12.361, 0, 32.36, 12.361, 0, 32.36, 20, 20, 20, 32.36, 12.361, 0, 20, 20, -20, 12.361, 0, -32.36, -12.361, 0, -32.36, -20, -20, -20, -32.36, -12.361, 0, -20, -20, 20, -32.36, 12.361, 0, -20, 20, 20, 0, 32.36, 12.361, 0, 32.36, -12.361, -20, 20, -20, 20, -20, -20, 32.36, -12.361, 0, 0, -32.36, -12.361, 0, -32.36, 12.361, 20, -20, 20};
    
    static int[] cubeEdges = {1, 2, 1, 3, 1, 4, 2, 6, 2, 7, 3, 5, 3, 7, 4, 5, 4, 6, 5, 8, 6, 8, 7, 8};
    static int[] pyramidEdges = {1, 2, 2, 3, 3, 4, 1, 4, 1, 5, 2, 5, 3, 5, 4, 5};
    static int[] hourglassEdges = {1, 2, 2, 3, 3, 4, 1, 4, 5, 6, 6, 7, 7, 8, 5, 8, 9, 10, 10, 11, 11, 12, 9, 12, 1, 5, 5, 9, 2, 6, 6, 10, 3, 7, 7, 11, 4, 8, 8, 12};
    static int[] dodecahedronEdges = {1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 1, 10, 1, 12, 12, 13, 11, 12, 9, 11, 11, 15, 7, 15, 14, 15, 5, 14, 13, 14, 10, 19, 19, 20, 18, 19, 8, 18, 16, 18, 6, 16, 16, 17, 4, 17, 17, 20, 2, 20, 3, 13};
    
    public static void main(String[] args) throws InterruptedException
    {
        Scanner input = new Scanner(System.in);
        
        char[][] graph = new char[DIM][DIM];
        
        // Creates the graph using all blank tiles
        for(char[] i : graph)
        {
            Arrays.fill(i, blank);
        }
        
        int maxVertices = 21;                                      // |
        final int SENTINEL = DIM + 1;
        double[][] vertices = new double[1][];
        int[][] edges = new int[1][];
        
        String color;
        
        while(true)
        {
            System.out.println("Enter the color name you'd like the graph to be (Black, Red, Blue, Green, Yellow, Purple, Cyan):");
            color = input.nextLine();
            if(color.equals("Black") || color.equals("black") || color.equals("Red") || color.equals("red") || color.equals("Blue") || color.equals("blue") || color.equals("Green") || color.equals("green") || color.equals("Yellow") || color.equals("yellow") || color.equals("Purple") || color.equals("purple") || color.equals("Cyan") || color.equals("cyan") || color.equals(""))
            {
                break;
            }
            System.out.println("Invalid color");
        }
        
        switch(color)
        {
            case "Black":
            case "black":
                colorCode = "\u001B[30m";
                colorBack = "\u001B[40m";
                break;
            case "Red":
            case "red":
                colorCode = "\u001B[31m";
                colorBack = "\u001B[41m";
                break;
            case "Blue":
            case "blue":
                colorCode = "\u001B[34m";
                colorBack = "\u001B[44m";
                break;
            case "Green":
            case "green":
                colorCode = "\u001B[32m";
                colorBack = "\u001B[42m";
                break;
            case "Yellow":
            case "yellow":
                colorCode = "\u001B[33m";
                colorBack = "\u001B[43m";
                break;
            case "Purple":
            case "purple":
                colorCode = "\u001B[35m";
                colorBack = "\u001B[45m";
                break;
            case "Cyan":
            case "cyan":
                colorCode = "\u001B[36m";
                colorBack = "\u001B[46m";
                break;
            default:
                colorCode = "\u001B[30m";
                colorBack = "\u001B[40m";
                break;
        }
        
        String preset;
        
        // Gets the user input for a preset shape and checks if its valid
        while(true)
        {
            System.out.println("Enter a preset shape, or enter \"manual\" to manually enter points (cube, pyramid, dodecahedron, hourglass):");
            preset = input.nextLine();
            if(preset.equals("Manual") || preset.equals("manual") || preset.equals("Cube") || preset.equals("cube") || preset.equals("Pyramid") || preset.equals("pyramid") || preset.equals("Hourglass") || preset.equals("hourglass") || preset.equals("Dodecahedron") || preset.equals("dodecahedron") || preset.equals(""))
            {
                break;
            }
            System.out.println("Invalid shape");
        }
        
        if(preset.equals("manual") || preset.equals("Manual"))
        {
            System.out.println("Enter up to " + (maxVertices - 1) + " points (-" + ((DIM - 1)/2) + " to " + ((DIM - 1)/2) + ") in this format: x y z (Type " + (DIM + 1) + " to finish)");
            
            // Creates an array of vertices based on the user inputted points
            double[][] tempArr = new double[maxVertices][3];
            
            for(int i = 0; i < maxVertices; i++)
            {
                double tempX;
                double tempY;
                double tempZ;
                
                // Checks if X is a valid point and is on the grid
                while (true)
                {
                    try
                    {
                        tempX = input.nextDouble();
                        if(tempX > (DIM - 1)/2 && tempX < 0 - ((DIM - 1)/2))
                        {
                            System.out.println("Invalid point");
                        }
                        else
                        {
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Invalid point");
                        input.nextLine();
                    }
                }
                
                // Checks if the user is complete
                if(tempX == (double) SENTINEL)
                {
                    // Creates a new array that is the exact length of the number of points entered and assigns the given values to it
                    vertices = new double[i][];
                    for(int j = 0; j < vertices.length; j++)
                    {
                        vertices[j] = tempArr[j];
                    }
                    break;
                }
                else
                {
                    // Checks if Y is a valid point and is on the grid
                    while (true)
                    {
                        try
                        {
                            tempY = input.nextDouble();
                            if(tempY > (DIM - 1)/2 && tempY < 0 - ((DIM - 1)/2))
                            {
                                System.out.println("Invalid point");
                            }
                            else
                            {
                                break;
                            }
                        }
                        catch (Exception e)
                        {
                            System.out.println("Invalid point");
                            input.nextLine();
                        }
                    }
                    
                    // Checks if Z is a valid point and is on the grid
                    while (true)
                    {
                        try
                        {
                            tempZ = input.nextDouble();
                            if(tempZ > (DIM - 1)/2 && tempZ < 0 - ((DIM - 1)/2))
                            {
                                System.out.println("Invalid point");
                            }
                            else
                            {
                                break;
                            }
                        }
                        catch (Exception e)
                        {
                            System.out.println("Invalid point");
                            input.nextLine();
                        }
                    }
                    
                    tempArr[i][0] = tempX;
                    tempArr[i][1] = tempY;
                    tempArr[i][2] = tempZ;
                }
            }
            
            System.out.println("Enter a series of points to connect in this format: Point1Number Point2Number (Type " + (DIM + 1) + " to finish):");
            
            // Creates an array of pairs of vertices to connect with lines based on user input
            int[][] tempArray = new int[maxVertices * 5][2];
            
            for(int i = 0; i < tempArray.length; i++)
            {
                int temp1 = input.nextInt();
                
                // Checks if the user is complete
                if(temp1 == SENTINEL)
                {
                    // Creates a new array that is the exact length of the number of pairs entered and assigns the given values to it
                    edges = new int[i][];
                    for(int j = 0; j < edges.length; j++)
                    {
                        edges[j] = tempArray[j];
                    }
                    break;
                }
                else
                {
                    tempArray[i][0] = temp1 - 1;
                    tempArray[i][1] = input.nextInt() - 1;
                }
            }
        }
        else
        {
            // Checks for each preset shape and assigns the given preset values into the arrays
            switch(preset){
                case "Cube":
                case "cube":
                    vertices = new double[cubeVerts.length / 3][3];
                    
                    for(int i = 0; i < vertices.length; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            vertices[i][j] = cubeVerts[(i * 3) + j];
                        }
                    }
                    
                    edges = new int[cubeEdges.length / 2][2];
                    
                    for(int i = 0; i < edges.length; i++)
                    {
                        for(int j = 0; j < 2; j++)
                        {
                            edges[i][j] = cubeEdges[(i * 2) + j] - 1;
                        }
                    }
                    break;
                case "Pyramid":
                case "pyramid":
                    vertices = new double[pyramidVerts.length / 3][3];
                    
                    for(int i = 0; i < vertices.length; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            vertices[i][j] = pyramidVerts[(i * 3) + j];
                        }
                    }
                    
                    edges = new int[pyramidEdges.length / 2][2];
                    
                    for(int i = 0; i < edges.length; i++)
                    {
                        for(int j = 0; j < 2; j++)
                        {
                            edges[i][j] = pyramidEdges[(i * 2) + j] - 1;
                        }
                    }
                    break;
                case "Hourglass":
                case "hourglass":
                    vertices = new double[hourglassVerts.length / 3][3];
                    
                    for(int i = 0; i < vertices.length; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            vertices[i][j] = hourglassVerts[(i * 3) + j];
                        }
                    }
                    
                    edges = new int[hourglassEdges.length / 2][2];
                    
                    for(int i = 0; i < edges.length; i++)
                    {
                        for(int j = 0; j < 2; j++)
                        {
                            edges[i][j] = hourglassEdges[(i * 2) + j] - 1;
                        }
                    }
                    break;
                case "Dodecahedron":
                case "dodecahedron":
                    vertices = new double[dodecahedronVerts.length / 3][3];
                    
                    for(int i = 0; i < vertices.length; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            vertices[i][j] = dodecahedronVerts[(i * 3) + j];
                        }
                    }
                    
                    edges = new int[dodecahedronEdges.length / 2][2];
                    
                    for(int i = 0; i < edges.length; i++)
                    {
                        for(int j = 0; j < 2; j++)
                        {
                            edges[i][j] = dodecahedronEdges[(i * 2) + j] - 1;
                        }
                    }
                    break;
                default:
                    vertices = new double[cubeVerts.length / 3][3];
                    
                    for(int i = 0; i < vertices.length; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            vertices[i][j] = cubeVerts[(i * 3) + j];
                        }
                    }
                    
                    edges = new int[cubeEdges.length / 2][2];
                    
                    for(int i = 0; i < edges.length; i++)
                    {
                        for(int j = 0; j < 2; j++)
                        {
                            edges[i][j] = cubeEdges[(i * 2) + j] - 1;
                        }
                    }
                    break;
            }
        }
        
        //Checks if the input given for focal length is valid
        int focal;
        
        while(true)
        {
            System.out.println("How far away would you like to be from the screen? (Recommended 20-500)");
            
            try
            {
                // Determines focal length (distance from user to screen)
                focal = input.nextInt();
                break;
            }
            catch(Exception e)
            {
                System.out.println("Invalid focal length");
                input.nextLine();
            }
        }
        
        //Checks if the input given for the number of axes is valid
        int numAxes;
        
        while(true)
        {
            System.out.println("How many axes would you like to rotate around? (1-3)");
            
            try
            {
                // Determines the number of axes the user would like to rotate the shape around and by what value
                numAxes = input.nextInt();
                if(numAxes > 3 || numAxes < 1)
                {
                    System.out.println("Invalid number of axes");
                }
                else
                {
                    break;
                }
            }
            catch(Exception e)
            {
                System.out.println("Invalid number of axes");
                input.nextLine();
            }
        }
        
        input.nextLine();

        int[] axes = new int[3];
        double[] deltas = new double[3];
        
        for(int i = 0; i < numAxes; i++)
        {
            String s;

            while(true)
            {
                
                System.out.println("Which axis would you like to rotate the shape around? (x, y, or z)");
                
                s = input.nextLine();
                    
                // Converts the axis to a number for a later if statement
                if(s.startsWith("X"))
                {
                    s = "x";
                }
                else if(s.startsWith("Y"))
                {
                    s = "y";
                }
                else if(s.startsWith("X"))
                {
                    s = "z";
                }
                
                char tempAxis = s.charAt(0);
                if(tempAxis == 'x')
                {
                    axes[i] = 1;
                    break;
                }
                else if(tempAxis == 'y')
                {
                    axes[i] = 2;
                    break;
                }
                else if(tempAxis == 'z')
                {
                    axes[i] = 3;
                    break;
                }
                else
                {
                    System.out.println("Invalid axis");
                }
            }
            
            while(true)
            {
                try
                {
                    System.out.println("How many degrees would you like to rotate by on the previous axis?");
                    deltas[i] = (input.nextDouble() * (Math.PI / 180));
                    break;
                }catch(Exception e)
                {
                    System.out.println("Invalid degree");
                    input.nextLine();
                }
            }
                
            
            input.nextLine();
        }
        
        // Sets the Java console to automatically take input after every character that is entered
        try
        {
            setTerminalToCBreak();
        }
        catch (InterruptedException e)
        {
            System.err.println("InterruptedException");
        }
        catch (IOException e)
        {
            System.err.println("IOException");
        }
        finally
        {
            try
            {
                stty(ttyConfig.trim());
            }
            catch (Exception e)
            {
                System.err.println("Exeption restoring tty config");
            }
        }
        
        byte[] bytes = new byte[50];
        
        // Begins rotating the shape at a set framerate until CodeHS dies
        while(true)
        {
            // Checks for character input in console and adjusts focal length accordingly
            try{
                if(System.in.available() > 0 ) 
                {
                    for(int i = 0; i < System.in.available() + 1; i++)
                    {
                        bytes[i] = (byte)System.in.read();
                    }
                   
                    if(focal > 0)
                    {
                        if(bytes[0] == (byte)'s')
                        {
                            focal -= ZOOM;
                        }
                        else if(bytes[0] == (byte)'w')
                        {
                            focal += ZOOM;
                        }
                    }
                    else
                    {
                        focal = 1;
                    }
                    
                    Arrays.fill(bytes, (byte)0);
                   
                    System.in.skip(System.in.available());
                }
               
            }
            catch (IOException e) 
            {
                System.err.println("IOException");
            }
            finally 
            {
                try 
                {
                    stty( ttyConfig.trim() );
                }
                catch (Exception e) 
                {
                    System.err.println("Exception restoring tty config");
                }
            }
            
            // Rotates the points by the prior values
            for(int i = 0; i < numAxes; i++ )
            {
                vertices = Renderer3D.rotatePoints(vertices, axes[i], deltas[i]);
            }
            
            // Resets the graph array
            for(char[] i : graph)
            {
                Arrays.fill(i, blank);
            }
                
            // Casts the points to the 2d screen from 3d space
            int[][] points = determineCasted(vertices, graph, focal);
            
            // Draws lines betweens any of the given pairs in the edges array
            for(int[] edge : edges)
            {
                int spacer = DIM/2;
                int x1 = points[edge[0]][0] + spacer;
                int y1 = points[edge[0]][1] + spacer;
                int x2 = points[edge[1]][0] + spacer;
                int y2 = points[edge[1]][1] + spacer;
                
                Renderer3D.drawLine(x1, y1, x2, y2, graph);
            }

            // Prints the screen
            Renderer3D.printBoard(graph);
            
            // Delay for computer's sake
            Thread.sleep(100);
        }
    }
    
    private static void drawLine(int x1, int y1, int x2, int y2, char[][] graph)
    {
        int x = x1;
        int y = y1;
        boolean interchange;
        double error;
        double dif1;
        double dif2;
        
        // Calculates change in y, change in x, slope, and whether dx and dy are positive or negative
        double dy = Math.abs(y2 - y1);
        double dx = Math.abs(x2 - x1);
        double slope = (dy / dx);
        int s1 = (int) Math.signum(x2 - x1);
        int s2 = (int) Math.signum(y2 - y1);
        
        // Checks if the slope is greater than 1 or between 1 and 0, and inverts the values if so
        if(dy > dx)
        {
            double temp = dx;
            dx = dy;
            dy = temp;
            interchange = true;
        }
        else
        {
            interchange = false;
        }
        
        // Determines the distance from the point on the line to the nearest pixel, but without the use of division
        error = 2 * dy - dx;
        dif1 = 2 * dy;
        dif2 = (2 * dy) - (2 * dx);
        
        // Fills the initial point
        graph[x][y] = filled;
        
        // Plots all of the points along the line
        for(int i = 1; i <= dx; i++)
        {
            // Calculates whether to fill square on the same line or the one in the direction of the slope
            if(error < 0)
            {
                if(interchange)
                {
                    y += s2;
                }
                else
                {
                    x += s1;
                }
                error += dif1;
            }
            else
            {
                y += s2;
                x += s1;
                error += dif2;
            }
            
            graph[x][y] = filled;
        }
    }
    
    private static void printBoard(char[][] graph)
    {
        String screen = "\u001B[37m" + "\u001B[47m";
        // Prints the array value by value
        for(int i = DIM - 1; i >= 0; i--)
        {
            for(int j = 0; j < DIM; j++)
            {
                if(graph[j][i] == filled)
                {
                    screen += colorCode + colorBack + graph[j][i] + " " + "\u001B[37m" + "\u001B[47m";
                }
                else
                {
                    screen += filled + " ";
                }
            }
            screen += "\n";
        }
        screen += colorReset;
        
        // Clears the screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        System.out.println(screen);
    }
    
    private static int[][] determineCasted(double[][] vertices, char[][] graph, int focal)
    {
        // Creates an array based on the number of vertices
        int[][] points = new int[vertices.length][2];
        
        for(int i = 0; i < vertices.length; i++)
        {
            // Casts the values to where they would appear one the screen
            int xCasted = Renderer3D.determineXCasted(vertices[i][0], vertices[i][2] + DIM/2, focal);
            int yCasted = Renderer3D.determineYCasted(vertices[i][1], vertices[i][2] + DIM/2, focal);
            
            // Sets the pixel that is the casted point to filled
            graph[xCasted + (DIM/2)][yCasted + (DIM/2)] = filled;
            
            // Updates the points array to determine edges later on
            points[i][0] = xCasted;
            points[i][1] = yCasted;
        }
        
        return points;
    }
    
    private static int determineXCasted(double x, double z, int focal)
    {
        // Determines the intersect with the line of sight and the screen between the user and the point in 3d space
        return (int)((focal * x)/(focal + z));
    }
    
    private static int determineYCasted(double y, double z, int focal)
    {
        // Determines the intersect with the line of sight and the screen between the user and the point in 3d space
        return (int)((focal * y)/(focal + z));
    }
    
    private static double[][] rotatePoints(double[][] vertices, int axis, double delta)
    {
        // Based on the axis given to rotate around, the given points are rotated using 2d rotation equations around the given axis, and plugged back into the 3d vertices array
        if(axis == 1)
        {
            for(double[] vertice : vertices)
            {
                double valY = vertice[1];
                double valZ = vertice[2];
                vertice[1] = (valY * Math.cos(delta)) - (valZ * Math.sin(delta));
                vertice[2] = (valY * Math.sin(delta)) + (valZ * Math.cos(delta));
            }
        }
        else if(axis == 2)
        {
            for(double[] vertice : vertices)
            {
                double valX = vertice[0];
                double valZ = vertice[2];
                vertice[0] = (valX * Math.cos(delta)) - (valZ * Math.sin(delta));
                vertice[2] = (valX * Math.sin(delta)) + (valZ * Math.cos(delta));
            }
        }
        else if(axis == 3)
        {
            for(double[] vertice : vertices)
            {
                double valX = vertice[0];
                double valY = vertice[1];
                vertice[0] = (valX * Math.cos(delta)) - (valY * Math.sin(delta));
                vertice[1] = (valX * Math.sin(delta)) + (valY * Math.cos(delta));
            }
        }
        return vertices;
    }
    
    private static void setTerminalToCBreak() throws IOException, InterruptedException 
    {
        // Runs the commands to set the Java console to character break
        
        // set reset ttyConfig value
        ttyConfig = stty("-g");
        
        // set the console to be character-buffered instead of line-buffered
        stty("-icanon min 1");
        
        // disable character echoing
        stty("-echo");
    }
    
    private static String stty(final String args) throws IOException, InterruptedException 
    {
        // Runs each command in the console
        String cmd = "stty " + args + " < /dev/tty";
        
        return exec(new String[] 
        {
            "sh",
            "-c",
            cmd
        });
    }

    private static String exec(final String[] cmd) throws IOException, InterruptedException 
    {
        // Runs the given command
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
       
        Process p = Runtime.getRuntime().exec(cmd);
       
        String result = new String(bout.toByteArray());
        return result;
    }
}
