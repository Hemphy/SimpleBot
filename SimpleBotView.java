import java.awt.Color;
import java.awt.Graphics;

/**
 * SimpleBotView emulates a robot that displays an environment and updates it upon walking around.
 *
 * @author Dennis Dang
 * @version 20 September 2017
 */

public class SimpleBotView
{
   private Color robot, wall, trail, background;
   private static Graphics g;
   private char[][] initial;
   private int size, robotX, robotY;

   /**
    * Initializes a new SimpleBotView with its environment details.
    *
    * @param initial sets the array of the environment represented in characters.
    * @param robot sets the color representing robot in the environment.
    * @param wall sets the color representing wall in the environment.
    * @param trail sets the color representing trail in the environment.
    * @param background sets the color representing background in the environment.
    * @param size sets the size of each pixel that is represented by each color or element given 
      in the environment.
    */

   public SimpleBotView(char[][] initial, Color robot, Color wall, Color trail, Color background, int size)
   {
      this.robot = robot;
      this.wall = wall;
      this.trail = trail;
      this.background = background;
      this.size = size;
      DrawingPanel canvas = new DrawingPanel(size*initial[0].length, size*initial.length);
      this.g = canvas.getGraphics();
      this.initial = initial;
      draw();
   }

   /**
    * Steps across each element in char representation of environment and draws each of them a
      color on a grid.
    */
   private void draw()
   {
      int x, y = 0;
      for (int row = 0; row < initial.length; row++)
      {
         x = 0;
         for (int col = 0; col < initial[row].length; col++)
         {
            changeColor(initial[row][col], row, col);
            g.fillRect(x,y,size,size);
            x += size;
         }
         y += size;
      }
   }

   /**
    * Changes the color set in element graphics depending on the char representated environment
    * scanned through the for loop in draw(). Also sets location of robot in robotX and robotY, if
      found.
    *
    * @param initial represents current character stepped through a for loop in draw().
    * @param row representation of current row position in the for loop in draw(). This is used to
      set row robot position to robotY.
    * @param col representation of current column position in the for loop in draw(). This is used
      to set column robot position to robotX.
    * @throws IllegalArgumentException If encounters an illegal character that is not represented
      as a wall, trail, robot, or background scanned from draw().
    */
   private void changeColor(char initial, int row, int col)
   {
      if (initial == 'w')
         g.setColor(wall);
      else if (initial == 't')
         g.setColor(trail);
      else if (initial == 'r')
      {
         g.setColor(robot);
         this.robotX = col;
         this.robotY = row;
      }
      else if (initial == 'b')
         g.setColor(background);
      else
         throw new IllegalArgumentException("Not a legal character for color type");
   }

   /**
    * Moves the robot to a given direction over by 1 unit of size, given in the constructor.
    *
    * @param direction sets the direction of where to move the robot
    * @throws IllegalArgumentException if direction is not a legal character matching
              directions (u)p,(d)own,(l)eft,(r)ight.
    */
   public void move(char direction)
   {
      initial[robotY][robotX] = 't';
      g.setColor(trail);
      g.fillRect(size*robotX,size*robotY, size, size);
      g.setColor(robot);
      if (direction == 'u')
      {
         initial[robotY-1][robotX] = 'r';
         robotY -= 1;
      }
      else if (direction == 'd')
      {
         initial[robotY+1][robotX] = 'r';
         robotY += 1;
      }
      else if (direction == 'l')
      {
         initial[robotY][robotX-1] = 'r';
         robotX -= 1;
      }
      else if (direction == 'r')
      {
         initial[robotY][robotX+1] = 'r';
         robotX += 1;
      }
      else
         throw new IllegalArgumentException("Not a legal character for direction");
      g.fillRect(size*robotX, size*robotY, size, size);

   }
}