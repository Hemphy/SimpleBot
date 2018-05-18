import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.IllegalStateException;
import java.util.Arrays;

/**
 * SimpleBotController is the Controller module of SimpleBot's Model-View-Controller design.
 * The design of SimpleBot is based off of Picobot.
 * <p>
 * SimpleBotController reads the world and program for SimpleBotModel.
 * Both world and program are being read from a scanner and are passed into a SimpleBotModel.
 * </p>
 * <p>
 * SimpleBotController will only handle world and program elements with a maximum size of 1000
    when passed into readWorld(Scanner in) or readProgram(Scanner in) as Scanner in.
 * </p>
 * @author Dennis Dang
 * @version 10 October 2017
 * @see <a href="http://nifty.stanford.edu/2010/dodds-picobot/picobotLocal.html">Picobot Documentation</a>
 */


public class SimpleBotController
{
   private static final int ARRAYSIZE = 1000; 
   private char[][] world; 
   private SimpleBotStatement[] program;

   /**
    * Takes a text representation of SimpleBot's world from a Scanner object
    * and removes whitespace. The text representation is then stored
    * in char[][] array world.
    * 
    * @param in has a text-representation of SimpleBot's world.
    * @throws IllegalArgumentException When the scanner is empty.
    * @throws IllegalArgumentException If more than one robot is detected in world.
    * @throws IllegalArgumentException If an illegal element is detected in world.
    * @throws IllegalArgumentException If the world array would become jagged.
    * @throws IllegalArgumentException If there is no array of at least 1 row and 1 column.
    * @throws IllegalArgumentException If no robot was found.
    */

   public void readWorld(Scanner in)
   {
      if (in == null)
      {
         throw new IllegalArgumentException("Scanner is empty");
      }
      int col = 0;
      int rows = 0;
      int maxCol = 0;
      int robot = 0;
      char[][] arr = new char[ARRAYSIZE][ARRAYSIZE];

      while (in.hasNextLine())
      {
         String temp = in.nextLine();
         if (temp.length() == 0)
         {
            temp = in.nextLine();
         }
         temp = temp.toLowerCase(); // turns string into lowercase to satisfy SimpleBot requirements
         String temp1 = "";
         Scanner lineReader = new Scanner(temp); // Throws a string into a scanner to read
         col = 0; // reset col so that it returns to 0 after scanning a row

         while (lineReader.hasNext()) // Skips whitespaces
         {
            temp1 += lineReader.next();
         }

         for (int i = 0; i < temp1.length(); i++)
         {
            if (temp1.charAt(i) == 'w' || temp1.charAt(i) == 'b' || temp1.charAt(i) == 't' || temp1.charAt(i) == 'r')
            {
               if (temp1.charAt(i) == 'r')
                  robot++;
               if (robot > 1)
                  throw new IllegalArgumentException("More than 1 robot detected");

               arr[rows][col] = temp1.charAt(i);
               col++;
            }
            else
               throw new IllegalArgumentException("Invalid element detected in world");
         }
         
         if ((col > maxCol && rows != 0))
            throw new IllegalArgumentException("Jagged Array detected");
         else if (col > maxCol)
            maxCol = col;
         rows++; // Tracks how many rows Scanner in is reading
      }

      if (col == 0 && rows == 0)
         throw new IllegalArgumentException("World must have at least 1 row and 1 column");

      if (robot == 0)
      {
         throw new IllegalArgumentException("No robot was found!");
      }

      this.world = new char[rows][col];
      for (int rows1 = 0; rows1 < world.length; rows1++)
      {
         for (int col1 = 0; col1 < world[0].length; col1++)
         {
            world[rows1][col1] = arr[rows1][col1];
         }
      }
   }

   /**
    * Takes a text-representation of SimpleBot program statements and comments from a Scanner object.
    * SimpleBot program statements are then captured from the scanner to make an array of type SimpleBotStatement.
    *
    * @param in has a text-representation of SimpleBot's program statements with some comments.
    * @throws IllegalArgumentException If the passed Scanner is empty.   
    */

   public void readProgram(Scanner in)
   {
      if (in == null)
         throw new IllegalArgumentException("Scanner is empty");
      int j = 0;
      String[] arr1 = new String[ARRAYSIZE];

      while (in.hasNextLine())
      {
         String line = in.nextLine();
         Scanner lineReader = new Scanner(line);

         String temp = line;

         for (int y = 0; y < temp.length(); y++)
         {
            if (temp.charAt(y) == '#')
            {
               temp = temp.substring(0,y).trim();
               lineReader.nextLine();
            }
         }
         arr1[j] = temp;
         
         if (!(arr1[j].trim().equals("")))
            j++;
      }

      this.program = new SimpleBotStatement[j];
      for (int i = 0; i < this.program.length; i++)
      {
         this.program[i] = new SimpleBotStatement(arr1[i]);
      }
   }

  /**
   * Passes an char array representing SimpleBot's world and an array of SimpleBotStatement into a constructor, creating a SimpleBotModel object.
   *
   * @throws IllegalStateException If both SimpleBot's world array and program array have not been read at least once.
   *       
   */
   public void start()
   {
      if (this.world == null || this.program == null)
      {
         throw new IllegalStateException("Both world and program have not been read at least once");
      }
      SimpleBotModel model = new SimpleBotModel(world, program);
   }

   /**
    * Returns the text description of the world and program array of SimpleBot's world.
    *
    * @return text description of stored values in SimpleBotController
    */
   public String toString()
   {
      String s = "world: \n" + Arrays.deepToString(this.world);
      s += "\nprogram: \n" + Arrays.toString(this.program);

      return s;
   }
}