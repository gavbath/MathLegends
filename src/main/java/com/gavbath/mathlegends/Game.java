package com.gavbath.mathlegends;

public class Game {
    public int score;
    public String summary = "";
    public String thisQuestionMsg = ""; //sets the current questions message to empty, but will change later according to what question the user is on.
    public double thisCorrectAns = 0; //sets the current correct answer to 0, but will change per question.
    public boolean isAnsDouble = false; //this just tracks if we need to use any decimal logic.
    
    public void generateAddQuestion(){
        int num1 = (int)(Math.random() * 20); //generates a random number from 0-19.
        int num2 = (int)(Math.random() * 20);
        int actualAnswer = num1+num2;
        thisQuestionMsg = "What is " + num1 + " + " + num2 + " ?";
        thisCorrectAns = actualAnswer;
        isAnsDouble = false;
    }
    
    public void generateSubQuestion(){
        int num1 = (int)(Math.random() * 20);
        int num2 = (int)(Math.random() * 20);
        int small = Math.min(num1,num2); //creates a new integer named small, which takes the min of the 2 numbers the user inputted.
        int large = Math.max(num1, num2); //creates a new integer named large, which takes the max of the 2 numbers the user inputted.
        int actualAnswer = large-small; //subtracts the small number from the large one. this is done to prevent a question appearing where the large number is subtracted from the small one (negative).
        thisQuestionMsg = "What is " + large + " - " + small + " ?";
        thisCorrectAns = actualAnswer;
        isAnsDouble = false;
    }
    
    public void generateDivQuestion(){
        int num1 = (int)(Math.random() * 20 + 1); //changes the usual 0-19 range to 1-20 by entering +1 at the end, and choses a random number. This prevents a divide by zero error.
        int num2 = (int)(Math.random() * 10 + 1); //this limits the numbers from 1-10 for the divisor, chosing a random number in that range.
        
        double actualAnswer = Math.round(((double) num1/num2) * 100.0) / 100.0; //ensures we always get 2 decimal places for the users answer!
        thisQuestionMsg = "What is " + num1 + " / " + num2 + " ?";
        thisCorrectAns = actualAnswer;
        isAnsDouble = true; //here, it sets isAnsDouble to true since division questions require decimal logic.
    }
    
    public void generateMulQuestion(){ //this method is very similar to the addition one, we just swap + with *.
        int num1 = (int)(Math.random() * 20);
        int num2 = (int)(Math.random() * 20);
        int actualAnswer = num1*num2;
        thisQuestionMsg = "What is " + num1 + " * " + num2 + " ?";
        thisCorrectAns = actualAnswer;
        isAnsDouble = false;
    }
    
    public boolean ansCheck(double userAnswer) { //this is a new method for the gui, it checks if the answer from the gui input is correct!
        boolean correct = false;
        
        if (isAnsDouble) {
             //compares the users answer to the actual one, if the absolute value difference between them is < 0.01, it's saved as true.
             boolean closelyRounded = Math.abs(userAnswer - thisCorrectAns) < 0.001;
             summary += "\n" + thisQuestionMsg.replace("What is ", "").replace(" ?", "") + " = " + String.format("%.2f", userAnswer) + " : " + closelyRounded;
             if(closelyRounded) {
                 ++score;
                 correct = true;
             }
        } else {
            summary += "\n" + thisQuestionMsg.replace("What is ", "").replace(" ?", "") + " = " + (int)userAnswer + " : " + ((int)userAnswer == (int)thisCorrectAns);
            if((int)thisCorrectAns == (int)userAnswer){ //if the user's answer is equal to the actal answer, add 1 to their score.
                ++score;
                correct = true;
            }
        }
        return correct;
    }
}