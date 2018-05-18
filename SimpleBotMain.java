import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * Author: Dennis Dang
 * Date: 20 October 2017
 *
 * Description: SimpleBotMain prompts the user twice to for a file name to search for SimpleBot's data for its world
 *              and its program statements. After both files are found and are correct readable file formats, the SimpleBot
 *              begins its simulation.
 */
public class SimpleBotMain{
   public static void main(String[] args)
   {
      boolean foundProgram = false;
      boolean foundWorld = false;
      SimpleBotController ctrl = new SimpleBotController();

      while (!foundProgram || !foundWorld)
      {
         try
         {
            if (!foundProgram)
            {
               ctrl.readProgram(findFile("Program file name: "));
               foundProgram = true;
            }
            else if(!foundWorld)
            {
               ctrl.readWorld(findFile("World file name: "));
               foundWorld = true;
            }
         }
         catch (IllegalArgumentException e)
         {
            System.out.println("Error. That file did not load properly.");
         }
      }

      System.out.print("Starting Simulation...");
      ctrl.start();
   }

   private static Scanner findFile(String prompt)
   {
      boolean found = false;
      Scanner input = new Scanner(System.in);
      Scanner fileScanner = null;

      while(!found)
      {
         try
         {
            System.out.print(prompt);
            File file = new  File(input.nextLine());
            found = true;

            fileScanner = new Scanner(file);
         }
         catch (FileNotFoundException e)
         {
            System.out.println("Error. That file does not exist");
            found = false;
         }
      }
      return fileScanner;
   }
}