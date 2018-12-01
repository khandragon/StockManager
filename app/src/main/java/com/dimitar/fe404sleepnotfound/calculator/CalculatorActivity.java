package com.dimitar.fe404sleepnotfound.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dimitar.fe404sleepnotfound.R;
import com.dimitar.fe404sleepnotfound.menu.MenuActivity;

public class CalculatorActivity extends MenuActivity {

    private EditText amount;
    private EditText interestRate;
    private EditText monthlyMinimum;
    private EditText years;

    private TextView balanceLeft;
    private TextView interestPaid;
    private TextView yearsLeft;

    private EditText contactName;
    private Button sendToBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        //Acquire references to all necessary views
        amount = findViewById(R.id.amount);
        interestRate = findViewById(R.id.interestRate);
        monthlyMinimum = findViewById(R.id.monthlyMinimum);
        years = findViewById(R.id.yearsPaying);
        balanceLeft = findViewById(R.id.balanceLeft);
        interestPaid = findViewById(R.id.interestPaid);
        yearsLeft = findViewById(R.id.timeLeft);
        contactName = findViewById(R.id.contactName);
        sendToBtn = findViewById(R.id.sendTo);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("balance", balanceLeft.getText().toString());
        savedInstanceState.putString("interest", interestPaid.getText().toString());
        savedInstanceState.putString("yearsLeft", yearsLeft.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        //Restore the values calculated from the Bundle after e.g a screen rotation
        balanceLeft.setText(savedInstanceState.getString("balance", ""));
        interestPaid.setText(savedInstanceState.getString("interest", ""));
        yearsLeft.setText(savedInstanceState.getString("yearsLeft", ""));
    }

    public void calculate(View v){
        //Validate that all input fields have been set
        if(amount.getText().toString().isEmpty() || interestRate.getText().toString().isEmpty() || monthlyMinimum.getText().toString().isEmpty() || years.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.emptyFields), Toast.LENGTH_SHORT).show();
        }
        else{
            calculateAndDisplayCreditInfo();
        }
    }
    private void calculateAndDisplayCreditInfo(){
        //Get all data provided
        double balance = Double.parseDouble(amount.getText().toString());
        double rate = Double.parseDouble(interestRate.getText().toString()) / 100;
        int time = Integer.parseInt(years.getText().toString());
        double monthlyPayment = Double.parseDouble(monthlyMinimum.getText().toString());

        int numMonths = time * 12;
        double interestPaidSoFar = 0;
        double monthlyInterest;
        double amountLeft;
        double dailyInterest = rate / 365;

        int monthsToPayOff ;
        double yearsToPayoff = 0;
        double finalBalance = 0;
        double finalInterest = 0;

        int i;
        //Calculate the balance left + the total interest paid on a month per month basis (compound interest)
        for(i=0; i<1000; i++){
            //Calculate compounded interest for that month
            monthlyInterest = balance * (Math.pow((1 + dailyInterest), 30) - 1);

            //Remove monthly payment from interest and apply what is left to the balance
            amountLeft = monthlyPayment - monthlyInterest;
            balance -= amountLeft;

            //Add the interest paid for that month to the total interest
            interestPaidSoFar += monthlyInterest;

            //When the specified number of months is reached
            if(i == numMonths){
                //If the balance is already paid off at this point
                if(balance < 0) {
                    finalBalance = 0;
                }
                //Otherwise display remaining balance
                else{
                    finalBalance = balance;
                }
                //Display interest paid so far
                finalInterest = interestPaidSoFar;
            }
            //When the number of months required to pay off the balance is reached
            if(balance <= 0){
                //Get the number of months that is left to pay
                monthsToPayOff = i - numMonths;

                //If the balance is already paid off
                if(monthsToPayOff < 0){
                    yearsToPayoff = 0;
                    finalInterest = interestPaidSoFar;
                }
                //If there is still time left to pay off
                else{
                    yearsToPayoff = monthsToPayOff / 12.0;
                }
                break;
            }
        }
        //If the payment is too small for that amount of interest, tell the user to raise their monthly payment
        if(i == 1000){
            balanceLeft.setText("");
            interestPaid.setText("");
            yearsLeft.setText("");
            Toast.makeText(this, getString(R.string.paymentTooSmall), Toast.LENGTH_LONG).show();
        }
        else {
            //Display the values
            balanceLeft.setText(Double.toString(finalBalance));
            interestPaid.setText(Double.toString(finalInterest));
            yearsLeft.setText(Double.toString(yearsToPayoff));
        }
    }

    public void clear(View v){
        amount.getText().clear();
        interestRate.getText().clear();
        monthlyMinimum.getText().clear();
        years.getText().clear();
        balanceLeft.setText("");
        interestPaid.setText("");
        yearsLeft.setText("");
    }

    public void lookupContactEmail(View v){

    }

    public void sendEmailWInfo(View v){

    }
}
