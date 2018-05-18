import java.awt.*;

/**
 * SimpleBotModel interacts between the user (via SimpleBotMain) and the SimpleBotController.
 * This implementation determines where SimpleBot should expect to move given its sensors for its
 * surrounding area.
 *
 *
 * @author Dennis Dang
 * @version 8 December 2017
 */
public class SimpleBotModel {
   public static char[][] world;
   public static SimpleBotStatement[] program;

   private static SimpleBotView view;
   private static int robotRow, robotCol = 0;
   private static int cellsLeft = 0;
   private static boolean wallFlag = false;
   
   private static int curState;
   private static boolean n, e, w, s;

   /**
    * SimpleBot should move around the world accordingly by its world given and its list of program statements.
    * SimpleBot should update its location and should continue to move until 1 of 3 conditions
    * are met: 
    *         (1) When there are no background cells/elements left in the world.
    *         (2) When the matched SimpleBotStatement from SimpleBot's program according to SimpleBot's surrounding, 
    *              would make SimpleBot run into a wall.
    *         (3) When there are no matches to be found in SimpleBot's program.
    * @param world A text-representation of SimpleBot's world.
    * @param program A list of SimpleBot's program statements to determine where SimpleBot should go in its given world environment.
    */
   public SimpleBotModel(char[][] world, SimpleBotStatement[] program) 
   {
      //Initialize state, world, position, view, ect
      SimpleBotModel.world = world;
      SimpleBotModel.program = program;
      this.view = new SimpleBotView(world, Color.GREEN, Color.ORANGE,
            Color.PINK, Color.YELLOW, 70);
      
      // Initialize robot's position and recount how many cells are left in the world.
      for(int row = 0; row < this.world.length; row++)
      {
         for(int col = 0; col < this.world[row].length; col++)
         {
            if (this.world[row][col] == 'r')
            {
               this.robotRow = row;
               this.robotCol = col;
            }
            if (this.world[row][col] == 'b')
               this.cellsLeft++;
         }
      }
      
      this.curState = 0;             // SimpleBot always start at state 0.
      
      if(this.world[robotRow - 1][robotCol] == 'w')
         this.n = true;
      if(this.world[robotRow][robotCol + 1] == 'w')
         this.e = true;
      if(this.world[robotRow][robotCol - 1] == 'w')
         this.w = true;
      if(this.world[robotRow + 1][robotCol] == 'w')
         this.s = true;
         
      SimpleBotStatement stmt = getStatement();
      while (stmt != null)
      {
         // Reset directional boolean values back to false.
         this.n = false;
         this.e = false;
         this.w = false;
         this.s = false;
         
         // Move SimpleBot Accordingly to the statement recieived and update curState
         try
         {
            moveRobot(stmt.nextAction());
            if (!this.wallFlag)
               this.curState = stmt.nextState();
         }
         catch(ArrayIndexOutOfBoundsException E){System.out.println("Match not found!");}
         
         if(this.world[robotRow - 1][robotCol] == 'w')
            this.n = true;
         if(this.world[robotRow][robotCol + 1] == 'w')
            this.e = true;
         if(this.world[robotRow][robotCol - 1] == 'w')
            this.w = true;
         if(this.world[robotRow + 1][robotCol] == 'w')
            this.s = true;
         try { Thread.sleep(120); } catch(Exception E) { }    
         stmt = getStatement();
      }
   }
   
   /*
      Checks world for if done
      Looks through statements for match
      If done, or no match return null.
   */
   private SimpleBotStatement getStatement()
   {
      if (cellsLeft == 0) // A condition to end the program.
      {
         System.out.println("* * COMPLETE! * *");
         return null;
      }
      boolean foundMatch = false;
      int match = -1; //Initialized to a negative number prevents posibility of being used as an index, if not found.
      int i = 0;
      /* Loops throughout all of the program statements that SimpleBot was given. If it has found
         a match or have run through all of the program statements, then it had not found a match. */
      while(!foundMatch && i < this.program.length)
      {
         if (this.program[i].match(this.curState, n, e, w, s))
         {
            foundMatch = true;
            match = i;
         }   
         i++;
      }
      if (!foundMatch || wallFlag) // 2 conditions to end the program.
      {
         System.out.print("STOPPED: ");
         if(!foundMatch)
            System.out.println("Cannot find matches with Sensors or current state");
         else
            System.out.println("matched statement ran into a wall");   
         return null;      
      }
      else
         return this.program[match]; // Returns 
   }
   
   /* Moves the robot forward to their corresponding direction (n)orth,(e)ast, (w)est, (s)outh.
      If there's a wall in the way, AND if it's not 'x', then a wall flag is placed, 
      which would terminate the program upon getStatement(). */
   private void moveRobot(char nextAction)
   {
      wallFlag = false;
      if(nextAction == 'n')
      {
         if(this.world[robotRow - 1][robotCol] == 'b' || this.world[robotRow - 1][robotCol] == 't')
         {  
            if (this.world[robotRow - 1][robotCol] == 'b')
               cellsLeft -= 1;    
            view.move('u');   
            this.world[robotRow][robotCol] = 't';
            this.robotRow -= 1;
         }   
         else if(this.world[robotRow - 1][robotCol] == 'w')
         {
            if (nextAction != 'x')
               wallFlag = true;
         }   
         
      }   
      else if (nextAction == 'e')
      {
         if (this.world[robotRow][robotCol + 1] == 'b' || this.world[robotRow][robotCol + 1] == 't')
         {
            if(this.world[robotRow][robotCol + 1] == 'b')
               cellsLeft -= 1;   
            view.move('r');
            this.world[robotRow][robotCol] = 't';
            this.robotCol += 1;   
         }   
         else if(this.world[robotRow][robotCol + 1] == 'w')
            if (nextAction != 'x')
               wallFlag = true;
      }  
      else if (nextAction == 'w')
      {
         if(this.world[robotRow][robotCol - 1] == 'b' || this.world[robotRow][robotCol - 1] == 't')
         {
            if(this.world[robotRow][robotCol - 1] == 'b')
            {
               cellsLeft -= 1;
            }
            view.move('l');
            this.world[robotRow][robotCol] = 't';
            this.robotCol -= 1;
         }     
         else if(this.world[robotRow][robotCol - 1] == 'w')
            if (nextAction != 'x')
               wallFlag = true;    
      }  
      else if (nextAction == 's')
      {
         if (this.world[robotRow + 1][robotCol] == 'b' || this.world[robotRow + 1][robotCol] == 't')
         {
            if(this.world[robotRow + 1][robotCol] == 'b')
               cellsLeft -= 1;
            view.move('d');
            this.world[robotRow][robotCol] = 't';
            this.robotRow += 1;      
         }      
         else if (this.world[robotRow + 1][robotCol] == 'w')
            if (nextAction != 'x')
               wallFlag = true;
      }
      
   
   }         
}
