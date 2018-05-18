import java.util.Scanner;

/**
 * This class is an abstraction of a SimpleBot program statement.
 * SimpleBotStatement takes and scans a string to be broken down into 5 parts:
 * The current state, the trigger sensors, the arrow, the nextAction, and nextState.
 * The String can be represented either without whitespace, or in any number or order of whitespace in the string of this format:
 *
 * <p>CurrentState   Surroundings   -&gt;   MoveDirection   NextState
 *
 * <p>During the scanning of the string, data is stored for the 5 components and data is validated.
 *
 * @author Dennis Dang
 * @version 10 October 2017
 */
public class SimpleBotStatement
{
  /**
   * A counter that holds the number of digits there are in the last component of a program statement.
   */
   private int digitFinalCount;
  /**
   * Holds the numeric value that SimpleBot should be in executing its program statement.
   */
   private int nextState;
  /**
   * Holds the action that SimpleBot should do in executing its program statement.
   */
   private char action;  /**
   * Holds the state that SimpleBot should be when executing its program statement.
   */
   private int state = 0;
  /**
   * Holds the program statement string passed in the constructor.
   */
   private String str; 
  /**
   * Holds boolean values for directional trigger (n)orth for SimpleBotStatement. 
   * Holds true if there is a wall in front of SimpleBot at the north.
   */
   private boolean n;
  /**
   * Holds boolean values for directional trigger (e)ast for SimpleBotStatement. 
   * Holds true if there is a wall in front of SimpleBot at the east.
   */
   private boolean e;
  /**
   * Holds boolean values for directional trigger (w)est for SimpleBotStatement. 
   * Holds true if there is a wall in front of SimpleBot at the west.
   */
   private boolean w;
  /**
   * Holds boolean values for directional trigger (s)outh for SimpleBotStatement. 
   * Holds true if there is a wall in front of SimpleBot at the south.
   */
   private boolean s;

  /**
   * Takes a string and removes whitespace.
   * The string is then analyzed into the 5 different components.
   * As the string is scanned through a for loop, index values are stored to locate some components are located.
   *
   *
   * @param s Text-representation of a program statement
   * @throws IllegalArgumentException If found an illegal character that should not be found in any part of the program statement, regardless of which component the scan is at.
   * @throws IllegalArgumentException If the format of the arrow, where the head of the arrow ('&gt;') is before the index of the arrow's tail, '-'. Or, the head is not found at all.
   * @throws IllegalArgumentException If the element at where the index was scanned does not satisfy: if its an integer, a legal character of a trigger component, a valid arrow, and a legal action char.
   * 
   */
   public SimpleBotStatement(String s)
   {
      this.str = s;
      String temp = "";
      Scanner killWhiteSpace = new Scanner (str);
      while (killWhiteSpace.hasNext())
      {
         temp += killWhiteSpace.next();
      }

      for (int i = 0; i < temp.length(); i++)
      {
         // If found a character that is not legal and should not be in a program statement of SimpleBot.
         if (!(temp.charAt(i) == 'n' || temp.charAt(i) == 'e' || temp.charAt(i) == 'w' || temp.charAt(i) == 's' ||
          temp.charAt(i) == '*' || temp.charAt(i) == 'x' || temp.charAt(i) == '-' || temp.charAt(i) == '>' || (Character.isDigit(temp.charAt(i)))))
         { 
            if(Character.isUpperCase(temp.charAt(i)))
               throw new IllegalArgumentException("\'" + temp.charAt(i) + "\' must be lowercase.");
            else   
               throw new IllegalArgumentException("Found illegal element in program statement: \'" + temp.charAt(i) + "\'");
         }      
            
      }
      this.str = temp; // Concatenated string w/o whitespace in between
      int digitInitIdxStart = 0;
      int digitInitIdxEnd = 0; // last location of digit
      int digitInitCount = 0;
      digitFinalCount = 0;

      int surroundIdxStart = 0;
      int surroundIdxEnd = 0;
      int surroundLength = 0;
      int actionLocation = 0;

      int arrow1 = 0;
      int arrow2 = 0;

      // Scan and Find components of program statement
      for (int i = 0; i < temp.length(); i++)
      {
         if (Character.isDigit(str.charAt(i)))
         {
            if (i < str.length()/2) // Count digits on the first half of the string
            {
               if (digitInitCount == 0) // If a digit has not been found before, store the index where it's found
                  digitInitIdxStart = i;
               digitInitCount++;
               digitInitIdxEnd = i;
            }
            else
               this.digitFinalCount++;
         }
         // If the scan sees no more than 4 elements as the valid trigger sensors, and the scan had finished reading the last index of current State.
         else if ((surroundLength < 4) && (i > digitInitIdxEnd) && (str.charAt(i) == 'x' || str.charAt(i) == '*' ||
         str.charAt(i) == 'n' || str.charAt(i) == 'e' || str.charAt(i) == 'w' || str.charAt(i) == 's'))
         {
            if (surroundIdxStart != 0) // If not the first time
            {
               surroundIdxEnd = i;
               surroundLength++;
            }
            else
            {
               surroundLength++; // get index, offsetted by state position
               surroundIdxStart = i;
            }
         }
         else if (str.charAt(i) == '-' || str.charAt(i) == '>')
         {
            if (str.charAt(i) == '-')
               arrow1 = i;
            if (str.charAt(i) == '>')
               arrow2 = i;
            if (arrow2<arrow1 && arrow2 != 0)
               throw new IllegalArgumentException("Found '>' before '-' for arrow element. Off by: " + Math.abs(arrow2-arrow1) + " indexes.");
         }
         // If the scan has gone over half way, is one of the directional values, and is at the nextAction's index value.
         else if ((i > str.length()/2) && (str.charAt(i) == 'x' || str.charAt(i) == 'n' || str.charAt(i) == 'e' || str.charAt(i) == 'w' || str.charAt(i) == 's') && (i==surroundIdxEnd+3))
         {
            actionLocation = surroundIdxEnd+3; // Skip the arrow
            this.action = this.str.charAt(actionLocation);
         }
       else
           throw new IllegalArgumentException("Not legal program Statement: " + this.str);
      }

      // Post-Scan validation
      check(digitInitIdxStart, digitInitCount, surroundIdxStart, surroundIdxEnd, arrow1, arrow2, actionLocation);

   }

  /**
   * Validates and checks if the string scanned from SimpleBotStatement's constructor is correct through the index locations given in the parameters.
   *
   * @param digitInitIdxStart Tracks the first digit's index of the String given in the constructor's parameter.
   * @param digitInitCount Tracks the last digit's index of the String given in the constructor's parameter.
   * @param surroundIdxStart Trackts the starting index of the triggerSensor's first element of the String.
   * @param surroundIdxEnd Tracks the ending index of the triggerSensor's last element of the String.
   * @param arrow1 Tracks the index of the arrow component '-' of the String.
   * @param arrow2 Tracks the index of the arrow component '&gt;' of the String.
   * @param actionLocation Tracks the index of the nextAction component of the String.
   * @throws IllegalArgumentException If the first and last element of the string are valid numbers.
   *
   * @throws IllegalArgumentException If the scanned string in the constructor had not found the end of a complete program statement,
         which is marked by a state. If int digitFinalCount is left at 0, there is no next State.
   * @throws IllegalArgumentException If no next state is found, and is also an incomplete program statement without nextState component.
   * @throws IllegalArgumentException If found a character other than x, *, n, e, w, s as the trigger sensor component of program statement.
   * @throws IllegalArgumentException If both arrow elements '-' and '&gt;" are not found; counters for arrow elements which were initialized at 0 were left at 0.
   * @throws IllegalArgumentException If both arrow elements '-' and '&gt;' are not found next to each other.
   * @throws IllegalArgumentException If the element found at the supposed nextAction index is valid; found a char other than an 'n', 'e', 'w', 's', or 'x'.
   *
   */
   // Post-Scan Validation
   private void check(int digitInitIdxStart, int digitInitCount, int surroundIdxStart, int surroundIdxEnd, int arrow1, int arrow2, int actionLocation)
   {
      // First and last character of program statement should be numeric values
      if (!(Character.isDigit(str.charAt(0)) || Character.isDigit(str.charAt(str.length()-1))))
      {
         throw new IllegalArgumentException("Invalid element of a state. \'" + str.charAt(0) + "\' must be an integer.");
      }

      // If no final state is found
      if (digitFinalCount == 0)
      {
         throw new IllegalArgumentException("Incomplete Program Statement, could not find nextState.");
      }

      // Stores current SimpleBot state
      this.state = toStateInt(str.substring(digitInitIdxStart,digitInitCount));

      // Assign boolean values for Surround from where the surround component starts till the end
      // It is crucial that no invalid data passes this through point.
      for (int i = surroundIdxStart; i <= surroundIdxEnd; i++)
      {
         if (str.charAt(i) == 'x'|| str.charAt(i) == '*')
         {
            if (i == surroundIdxStart)
               this.n = false;
            else if( i == surroundIdxStart+1)
               this.e = false;
            else if(i == surroundIdxStart+2)
               this.w = false;
            else
               this.s = false;
         }
         else if (str.charAt(i) == 'n' || str.charAt(i) == 'e' || str.charAt(i) == 'w' || str.charAt(i) == 's')
         {
            if (i == surroundIdxStart && str.charAt(i) == 'n')
               this.n = true;
            else if( i == surroundIdxStart+1 && str.charAt(i) == 'e')
               this.e = true;
            else if(i == surroundIdxStart+2 && str.charAt(i) == 'w')
               this.w = true;
            else if(i == surroundIdxStart+3 && str.charAt(i) == 's')
               this.s = true;

         }
         else
            throw new IllegalArgumentException("Found invalid trigger sensor: " + str.charAt(i));
      }

      // Check Valid arrow and see if they are next to each other
      if (arrow2 == 0 || arrow1 == 0)
         throw new IllegalArgumentException("Both arrows are not defined");
      if (Math.abs(arrow2-arrow1) != 1)
      {
         throw new IllegalArgumentException("Invalid Arrow. Arrow elements are not next to each other.");
      }

      // Check if nextAction is a valid character value for program statement.
      if (!(str.charAt(actionLocation) == 'n' || str.charAt(actionLocation) == 'e' || str.charAt(actionLocation) == 'w' || str.charAt(actionLocation) == 's' || str.charAt(actionLocation) == 'x'))
      {
         throw new IllegalArgumentException("Invalid element for nextAction component, instead found: \'" + str.charAt(actionLocation) + "\' at index" + actionLocation);
      }
   }

  /**
   * Takes a string and converts it into a numeric value. Though, this will not work on negative values given in String.
   * This method is used to convert Simplebot's string representation of currentState and nextState into its corresponding
   * numeric values.
   *
   * @param numberedString String representation of a int or state.
   * @throws IllegalArgumentException If the given string integer has a negative sign or not a numeric digit.
   * @return The numeric value of the given string.
   */
   private int toStateInt(String numberedString)
   {
      int decimal = 0;
      for (int i = 0; i <= numberedString.length()-1; i++)
      {
         if (!Character.isDigit(numberedString.charAt(i)))
            throw new IllegalArgumentException("\"" + numberedString.charAt(i) + "\" is not a String integer to be converted to integer.");
         decimal += (int) Character.digit(numberedString.charAt(i),10) * Math.pow(10,(numberedString.length()-1)-i);
      }
      return decimal;
   }

  /**
   * Takes a parameter of a state and a directional boolean and compares them with this object's corresponding state and
   * directional booleans. If both states or the corresponding directional booleans do not match, false is returned.
   * Otherwise, false is returned.
   *
   * @param state A state to compare currentState
   * @param n Used to match and compare the current object's boolean of North.
   * @param e Used to match and compare the current object's boolean of East.
   * @param w Used to match and compare the current object's boolean of West.
   * @param s Used to match and compare the current object's boolean of South.
   * @return True, if the current states and current directional boolean values match with the given parameters.
   * If otherwise, return false.
   */
   public boolean match (int state, boolean n, boolean e, boolean w, boolean s)
   {
      if (state != this.state)
         return false;
      else if ((n == this.n) && (e == this.e) && (w == this.w) && (s == this.s))
         return true;
      else
         return false;
   }

  /**
   * Returns the next state as indicated in the last component of string or program statement passed in the constructor.
   *
   * @return The nextState component of the SimpleBot's program statement.
   */
   public int nextState()
   {
      return this.state = toStateInt(str.substring(str.length()-(digitFinalCount)));
   }

  /**
   * Returns what character direction for SimpleBot should navigate to.
   *
   * @return The the direction component of the SimpleBot's program statement.
   */
   public char nextAction()
   {
      return this.action;
   }

  /**
   * Returns the program statement analyzed (in a form of a string) and given in the constructor along with
   * the 4 components of the program statement, excluding the arrow.
   *
   * @return The program statement, the current state, the boolean directional values, the next action, and the next state.
   */
   public String toString()
   {
      String s = "\n\nStatement: \t\t"+this.str;
      s += "\nCurrentState: \t" + this.state;
      s += "\nNorth: \t\t\t"+ this.n ;
      s += "\nEast: \t\t\t"+ this.e;
      s += "\nWest: \t\t\t"+ this.w;
      s += "\nSouth: \t\t\t"+ this.s;
      s += "\nNextAction: \t" + nextAction();
      s += "\nNextState: \t\t" + nextState();
      return s;
   }
}