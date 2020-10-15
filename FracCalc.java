/*
Sydney Wan
AP CS A
Period 3
Lab 8: Frac Calc
12/11/19
*/

import java.util.Scanner;

public class FracCalc{

   public static void main(String[] args) {
      Scanner scan = new Scanner(System.in);
      System.out.println("Welcome to the Fraction Calculator.");
      System.out.print("Enter an expression: ");
      String input = scan.nextLine();
      
      while (!input.equalsIgnoreCase("quit")) {
         if (input.equalsIgnoreCase("test")) {
            System.out.println(runTests());
         }
         else {
            System.out.println("\t" + produceAnswer(input) + "\n");
         }
         System.out.print("Enter an expression: ");
         input = scan.nextLine();
     }
     System.out.print("\nGoodbye. Thank you for using the Fraction Calculator.");
   }

/*
method produceAnswer() separates the input of the expression where the spaces occur by calling splitExpression(). 
Then, it further splits each operand into wholes, numerators, and denominators, and returns this info as an array by calling splitNum().
Then it creates a new array with the same values but as int instead of String.
It then uses createImproperFraction() to create an improper fraction for each operand
Then it calls doOperation() to perform the calcuation, returning a single fraction.
It then uses a a variety of removing the denominator, calling reduce() and calling mix() based on the format of the returned fraction to simplify.
*/   
   public static String produceAnswer(String input) { 
      String[] expression = splitExpression(input);
      String firstNum = expression[0];
      String operator = expression[1];
      String secNum = expression[2];
      
      String[] partsofSecNum = splitNum(secNum);
      String[] partsofFirstNum = splitNum(firstNum);
      int[] partsOfSecNum = new int[3];
      int[] partsOfFirstNum = new int[3];
      
      //turns array into an array of int
      for (int k = 0; k <= 2; k++) {
         partsOfFirstNum[k] = Integer.parseInt(partsofFirstNum[k]);
         partsOfSecNum[k] = Integer.parseInt(partsofSecNum[k]);
      }
      
      //turns values into an improper fraction
      partsOfFirstNum = createImproperFraction(partsOfFirstNum);
      partsOfSecNum = createImproperFraction(partsOfSecNum);
      
      int[] answer = new int[3];
      answer = doOperation(partsOfFirstNum, partsOfSecNum, operator);
      
      //removes the denominator if the denominator is 1
      if (answer[2] == 1) {
         return answer[1] + "";
      }
      else if (answer[1] == 0) {
         return answer[0] + "";
      }
      else {
         answer = reduce(answer);
         //creates a mixed fraction if the numerator is greater than the denominator
         if (Math.abs(answer[1]) > answer[2]) {
            answer = mix(answer);
            if (answer[1] == 0) {
               return answer[0] + "";
            }

            return answer[0] + "_" + answer[1] + "/" + answer[2];
         }
      }
      return answer[1] + "/" + answer[2];
   }

/*
method splitExpression() separates the expression into two numbers and an operator by dividing at where the spaces occur, and storing them in an array. 
*/    
   public static String[] splitExpression(String input) {
      String[] array = new String[3];
      
      int indexOfFirstSpace = input.indexOf(" ");
      int indexOfSecSpace = input.indexOf(" ", input.indexOf(" ") + 1);
      
      array[0] = input.substring(0, indexOfFirstSpace);
      array[1] = input.substring(indexOfFirstSpace + 1, indexOfSecSpace);
      array[2] = input.substring(indexOfSecSpace + 1);
      
      return array;
   }

/*
method splitNum() separates each number into its parts: whole, numerator, and denominator. 
*/   
   public static String[] splitNum(String input) {
      String[] numParts = {"0", "0", "1"};
      int wholeNum = 0;
      int fraction = 0;
      
      for (int k = 0; k < input.length(); k++) {  
         if (input.substring(k, k + 1).equals("_")) {
            wholeNum++;
         }
         if (input.substring(k, k + 1).equals("/")) {
            fraction++;
         }
      }
      
      //if there is only a whole
      if (fraction == 0) {
         numParts[0] = input;
      }
      
      else {
         int indexOfSlash = input.indexOf("/");
         numParts[2] = input.substring(indexOfSlash + 1);
         
         //if there is a whole number & fraction
         if (wholeNum == 1) {
            int indexOfUnderscore = input.indexOf("_");
            numParts[0] = input.substring(0, indexOfUnderscore);
            numParts[1] = input.substring(indexOfUnderscore + 1, indexOfSlash);
         }
         
         //if there is only a fraction
         if (wholeNum == 0) {
            numParts[1] = input.substring(0, indexOfSlash);
         }
      }
      
      return numParts; 
   }

/*
method createImproperFraction converts an input into an inproper fraction 
   whole = value of the whole number
   numerator = value of the numerator
   denominator = value of the denominator
the method creates it by multiplying the whole by the denominator and adding it to th existing numerator, and then retaining the original denominator
*/   
   public static int[] createImproperFraction(int[] array) {
      int newNumerator = 0;
      int whole = array[0];
      int numerator = array[1];
      int denominator = array[2];
      //algorithm for creating the correct improper fraction if there are negatives
      if (whole < 0 || numerator < 0) {
         whole = Math.abs(whole);
         numerator = Math.abs(numerator);
         newNumerator = -1 * ((whole * denominator) + numerator);
      }
      //algorithm if there are no negatives
      else {
         newNumerator = ((whole * denominator) + numerator);
      }
      
      int[] fraction = {0, newNumerator, denominator};
      return fraction;
   }

/*
method doOperation applies the actual calculation, depending on which operator the user inputted
   num[0] = 0 currently, because it is an improper fraction
   num[1] = numerator
   num[2] = denominator
answer[] stores the single answer with the same indexes for whole/numerator/denominator as before
*/   
   public static int[] doOperation(int[] num1, int[] num2, String operator) {
      int[] answer = new int[3];
      if (operator.equals("*")) {
         answer[1] = num1[1] * num2[1];
         answer[2] = num1[2] * num2[2];
      }
      else if (operator.equals("/")) {
         answer[1] = num1[1] * num2[2];
         answer[2] = num1[2] * num2[1];
      }
      else if (operator.equals("+")) {
         answer[1] = (num1[1] * num2[2]) + (num2[1] * num1[2]); 
         answer[2] = num1[2] * num2[2];
      }
      else {
         answer[1] = (num1[1] * num2[2]) - (num2[1] * num1[2]); 
         answer[2] = num1[2] * num2[2];
      }
      return answer;      
   }

/*
method reduce reduces the improper fraction answer into its simplest terms, but still returning an improper fraction
   fraction[1] = numerator
   fraction[2] = denominator
the for loop starts at 2 and goes up by 1 up to the greater between the numerator and denominator, finding a value for which both are divisible by
   if both the numerator and denominator are divisible by the same number, both are divided by that value
*/   
   public static int[] reduce(int[] fraction) {
      for (int k = 2; k <= Math.max(fraction[1], fraction[2]); k++) {
         while ((fraction[1] % k == 0) && (fraction[2] % k == 0)) {
            fraction[1] /= k;
            fraction[2] /= k;
         }
      }
      return fraction;
   }

/*
method mix turns an improper fraction into a mixed fraction
   fraction[1] = numerator
   fraction[2] = denominator
it finds the value of the whole number by seeing how many times the numerator can divide by the denominator
it finds the value of the remaining fraction by finding the remainder after the numerator divides by the denominator
*/   
   public static int[] mix(int[] fraction) {
      int whole = fraction[1] / fraction[2];
      int newNumerator = Math.abs(fraction[1] % fraction[2]);
      fraction[0] = whole;
      fraction[1] = newNumerator;
      return fraction;
      
   }
   

/*
method runTests() puts each of the test cases into the testCalc() method and checks that they match the expected answer.
*/   

   public static String runTests() {
      String[] inputs = {"10/4 + 2/2", "3 + 4", "1_3/4 + 2_3/4", "3/4 * -5/6", "-3 + -5", "-2_1/5 + 0", "-1_12/4 * 6/9", "8/4 + 2", "0 / 25_462/543"};
      String[] expected = {"3_1/2", "7", "4_1/2", "-5/8", "-8", "-2_1/5", "-2_2/3", "4", "0" };
            
      int passCount = 0;
      
      for (int k = 0; k < inputs.length; k++) {
         // run each test case
         if (testCalc(inputs[k], expected[k])) {
            passCount++;
         }
      }
      return "\nTests: " + passCount + " of " + inputs.length + " test cases PASSED\n";
   }
   
/*
method testCalc() runs each of the test cases through produceAnswer() and if it passes, it states "test passed" and if it fails, explains why it failed.
*/   

   public static boolean testCalc(String input, String expected) {
      String output = produceAnswer(input);
      
      // compare actual to expected
      boolean pass = output.equals(expected);
      
      if (pass) {
         System.out.println("test passed");
      }
      else {
         System.out.println("test failed");
         System.out.println("   input:    " + input);
         System.out.println("   expected: " + expected);
         System.out.println("   output:   " + output);
      }
         
      return pass;
   }
}