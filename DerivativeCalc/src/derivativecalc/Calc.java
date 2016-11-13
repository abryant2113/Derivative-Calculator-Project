/*
 * Austin Bryant
 * 11/12/2016
 * University of Central Florida
 * Derivative Calculator Project
 */

package derivativecalc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Austin
 */
public class Calc {

    private String expression;
    private String derivative;
    private List<String> array;

    public Calc() {
        derivative = "";
        initialize();
    }

    public void initialize() {

        array = new ArrayList();

        System.out.println("Enter the expression to be differentiated:");

        //allows the scanner to scan in the entire line instead of being delimited by the space character
        Scanner in = new Scanner(System.in).useDelimiter("\\n");
        expression = in.next();

        //stores each part of the equation separated by white space into different elements of the list
        array = Arrays.asList(expression.split(" "));

        differentiate();

        System.out.println("The original expression was: " + expression);
        System.out.println("The derivative of that expression is " + derivative);
    }

    public String differentiate() {

        //for each element in the list, we store each section of the list in different variables
        for (String b : array) {
           
            //if string b only contains one variable and no multiplicaton and division sign, we can use the power rule
            if (b.contains("x") && (!b.contains("*") && !b.contains("/") && !b.contains("(") && !b.contains(")"))) {
                powerRule(b);
            } 
            else if (b.contains("(") && b.contains(")")){
                chainRule(b);
            }
            //if the current string is an addition sign, concatenate it to the derivative string
            else if (b.contains("+")) {
                derivative = derivative.concat(" + ");
            } //if the current string is a subtraction sign, concatenate it to the derivative string
            else if (b.contains("-")) {
                derivative = derivative.concat(" - ");
            }
        }
        //returns the derivative
        return derivative;
    }

    private void powerRule(String b) {

        List<String> arr = new ArrayList();

        int num = 1;
        int power;

        String p = "";
        String coefficient = "";
        
        //if the expression passed is simply "x", the derivative is one and we don't need to do anything else, so we break from the method
        if(b.contains("x") && !b.contains("^") && !b.matches(".*[0-9].*")){
            derivative = derivative.concat("1");
            return;
        }
        
        //if the expression contains both an x and a caret, we need to use the product rule 
        if (b.contains("x") && !b.contains("^")) {
            //splits the current expression into individual parts...
            arr = Arrays.asList(b.split("x"));
            //the coefficient is the derivative
            derivative = derivative.concat(arr.get(0));
        } 
        //if the expression involves a caret, we need to use the power rule
        else if (b.contains("^")) {
          
            //no coefficient in front of the x... default value is 1
            if(b.charAt(0) == 'x'){
                //concatenates the power as a new coefficient
                for(int c = 2; c < b.length(); c++){
                    String temp = Character.toString(b.charAt(c));
                    coefficient = coefficient.concat(temp);
                }
                
                //convert the strings containing our needed numbers into integers
                num = Integer.parseInt(coefficient);
                power = Integer.parseInt(coefficient) - 1;
                
                //they are then converted back after some algebraic manipulation in order to be added to the derivative string
                String part1 = Integer.toString(num);
                String part2 = Integer.toString(power);
                
                //concatenates the new part of the derivative onto the other part of the computed derivative
                derivative = derivative.concat(part1 + "x" + "^" + part2);
                
                //once we have completed part of the derivative, we can escape from the method
                return;
            }
            
            //otherwise, we split the string into just numbers
            arr = Arrays.asList(b.split("[x...]+[^...]"));
            
            //grabs the coefficient and the power
            for (int l = 0; l < arr.size(); l++) {

                if (l == (arr.size() - 1)) {
                    //the last number in the string is going to be the power
                    p = arr.get(l);
                } else {
                    //if there is no coefficient, the coefficient is 1...
                    if(arr.get(l) == null){
                        coefficient = coefficient.concat("1");
                    }
                    else{
                        coefficient = coefficient.concat(arr.get(l));
                    }
                }

            }
            //turns the strings into integers...
            num *= Integer.parseInt(coefficient);
            power = Integer.parseInt(p);

            //now that we have all that is needed, we compute the derivative and save it as a string and return it
            String part1 = Integer.toString(num * power);
            String part2 = Integer.toString(power - 1);

            //if the new power of the expression is one, we can omit the exponent
            if ((power - 1) == 1) {
                derivative = derivative.concat(part1 + "x");
            //otherwise, we need to inclde the caret and its corresponding exponent
            } else {
                derivative = derivative.concat(part1 + "x" + "^" + part2);
            }
        }

    }
    
    private void chainRule(String b){
        
        String temp = derivative;
        
        //clears the current derivative variable
        derivative = "";
        //grabs the substring of the chained part of the expression
        String chain = b.substring(b.indexOf("(") + 1, b.indexOf(")")); 
        //we sub the part to be chain in with an x, so that we can call the power rule function with the outer expression
        b = b.replace(chain, "x");
        //replaces the (x) with just x, so that we can pass c into the powerRule function
        String c = b.replace("(x)", "x");
        //calls the powerRule method and computes the derivative of the outer expression
        powerRule(c);
        //sets a new string equal to the derivative member variable so that we can clear the space for the next method call
        String outer = derivative;
        //clears the derivative member variable for the next powerRule call
        derivative = "";
        //calls the powerRule method with the chained expression that we need the derivative of
        powerRule(chain);
        //sub the chained expression back in for x
        c = outer.replace("x", "(" + chain + ")");
        //prints out the complete derivative of the expression
        derivative = temp.concat("(" + derivative + ")" + "*" + c);
        
    }

}
