package net.as93.calculator;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends ActionBarActivity {

    private EditText txtScreen;

    private String errorMessage = "";
    private boolean equalsPressed = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Screen Components */
        txtScreen = (EditText) findViewById(R.id.txtScreen);

        /* Number Buttons*/
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
        Button btn4 = (Button) findViewById(R.id.btn4);
        Button btn5 = (Button) findViewById(R.id.btn5);
        Button btn6 = (Button) findViewById(R.id.btn6);
        Button btn7 = (Button) findViewById(R.id.btn7);
        Button btn8 = (Button) findViewById(R.id.btn8);
        Button btn9 = (Button) findViewById(R.id.btn9);
        Button btn0 = (Button) findViewById(R.id.btn0);

        /* Symbol Buttons */
        Button btnDec   = (Button) findViewById(R.id.btnDecimal);

        /* Operator Buttons */
        Button btnPlus  = (Button) findViewById(R.id.btnPlus);
        Button btnMinus = (Button) findViewById(R.id.btnMinus);
        Button btnMult  = (Button) findViewById(R.id.btnMultiply);
        Button btnDiv   = (Button) findViewById(R.id.btnDivide);

        /* Operation Buttons */
        Button btnEquals = (Button) findViewById(R.id.btnEquals);
        Button btnClearAll = (Button) findViewById(R.id.btnC);
        Button btnDel = (Button) findViewById(R.id.btnDel);




        /* Add text to calculator screen on button press */
        View.OnClickListener buttonPress = new View.OnClickListener() {
            public void onClick(View v) {
                String buttonText = ((Button)v).getText().toString();
                String currentScreenText = txtScreen.getText().toString();
                txtScreen.setText(addDigitsToScreen(buttonText, currentScreenText, equalsPressed));
                equalsPressed = false;
            }
        };

        btn1.setOnClickListener(buttonPress);
        btn2.setOnClickListener(buttonPress);
        btn3.setOnClickListener(buttonPress);
        btn4.setOnClickListener(buttonPress);
        btn5.setOnClickListener(buttonPress);
        btn6.setOnClickListener(buttonPress);
        btn7.setOnClickListener(buttonPress);
        btn8.setOnClickListener(buttonPress);
        btn9.setOnClickListener(buttonPress);
        btn0.setOnClickListener(buttonPress);
        btnPlus.setOnClickListener(buttonPress);
        btnMinus.setOnClickListener(buttonPress);
        btnMult.setOnClickListener(buttonPress);
        btnDiv.setOnClickListener(buttonPress);
        btnDec.setOnClickListener(buttonPress);


         /* Clear screen on C */
        View.OnClickListener clearScreen = new View.OnClickListener() {
            public void onClick(View v) {
                txtScreen.setText("");
            }
        };
        btnClearAll.setOnClickListener(clearScreen);

        /* Delete last character */
        View.OnClickListener clearLast = new View.OnClickListener() {
            public void onClick(View v) {
                if (txtScreen.getText()!=null) {
                    if (txtScreen.getText().length() > 0) {
                        txtScreen.setText(txtScreen.getText().toString().substring(0, txtScreen.getText().toString().length()-1));
                    }
                }
            }
        };
        btnDel.setOnClickListener(clearLast);

        View.OnClickListener equalPress = new View.OnClickListener() {
            public void onClick(View v) {
                equalsPressed = true;
                String sum =  txtScreen.getText().toString();
                String screenAnswer;
                if(checkSumValid(sum)){
                    double theAnswer = calculateAnswer(sum); // The answer
                    if(theAnswer == 0.0 && !errorMessage.equals("")){
                        screenAnswer = sum;
                        Toast.makeText(getApplicationContext(), errorMessage,
                                Toast.LENGTH_LONG).show();} // error :'(

                    else{ screenAnswer = String.valueOf(theAnswer).replaceAll("[0]*$", "").replaceAll(".$", ""); } // no error :D
                }
                else{
                    screenAnswer = sum;
                    Toast.makeText(getApplicationContext(), errorMessage,
                            Toast.LENGTH_LONG).show();
                }
                txtScreen.setText(screenAnswer);
            }
        };

        btnEquals.setOnClickListener(equalPress);

    }

    private String addDigitsToScreen(String buttonText, String currentScreenText, boolean hasEqualsBeenPressed){

        String newScreenText;
        newScreenText = currentScreenText+buttonText;

        if(hasEqualsBeenPressed){
            if(!buttonText.equals("+")&&!buttonText.equals("-")&&!buttonText.equals("*")&&!buttonText.equals("/")){
                newScreenText = buttonText;
            }
        }

        return newScreenText;
    }

    private boolean checkSumValid(String sum){

        /* Check input states with number or minus */
        Matcher m = Pattern.compile("^\\-?\\d").matcher(sum);
        if(!m.find()){
            errorMessage = "Sum must start with a number";
            return false; }

        /* Check input only has one operator */
        int f = 0;
        for (int i = 0; i< sum.length(); i++){
            char c = sum.toCharArray()[i];
            if(c=='+'||c=='/'||c=='*'){ f++; }
        }
        if(f>1){
            errorMessage = "Only one operator may be used";
            return false;
        }

        return true;
    }

    private double calculateAnswer(String sum){
        double answer = 0.0, firstNum  = 0.0, secondNum = 0.0;

        /* Iterate through sum and assign first and second numbers and operator */
        String strFirstNum = "", strSecondNum = "", operator = "";
        char[] charArr =  sum.toCharArray();
        for (int i = 0; i<charArr.length; i++) {
            char ch = charArr[i];
            if(ch >= '0' && ch <= '9'||ch=='.'){
                if(operator.equals("")){strFirstNum+=ch;} // first number
                else{strSecondNum+=ch; }                  // second number
            }
            else if(ch=='-'&&i==0){
               strFirstNum+=ch;
            }
            else if(ch=='-'&&!operator.equals("")){
                strSecondNum+=ch;
            }
            else if(ch=='+'||ch=='-'||ch=='*'||ch=='/'){
                operator = Character.toString(ch);        // operator
            }
        }

        /* Check everything is all cool */
        if(strFirstNum.equals("")){         errorMessage   = "First number not present";    }
        else if(strSecondNum.equals("")){   errorMessage   = "Second number not present";   }
        else if(operator.equals("")){       errorMessage   = "Operator number not present"; }
        try{firstNum = Double.parseDouble(strFirstNum); }
        catch(NumberFormatException e){     errorMessage    = "First number not valid"; }
        try{secondNum = Double.parseDouble(strSecondNum); }
        catch(NumberFormatException e){     errorMessage    = "Second number not valid"; }
        if(!errorMessage.equals("")){return 0.0; } // Something wasn't right :(


        /* Do the calculation */
        if     (operator.equals("+")){ answer = firstNum + secondNum; }
        else if(operator.equals("-")){ answer = firstNum - secondNum; }
        else if(operator.equals("*")){ answer = firstNum * secondNum; }
        else if(operator.equals("/")){ answer = firstNum / secondNum; }

        return answer;
    }




}
