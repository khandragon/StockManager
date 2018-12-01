package com.dimitar.fe404sleepnotfound.calculator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    public void calculateAndDisplayCreditInfo(View v){
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

        //Calculate the balance left + the total interest paid on a month per month basis (compound interest)
        for(int i=0, j=0; i<numMonths; i++, j++){
            //Calculate compounded interest for that month
            monthlyInterest = balance * (Math.pow((1 + dailyInterest), 30) - 1);

            Log.wtf("INTEREST", ""+monthlyInterest);
            //Remove monthly payment from interest and add to balance
            amountLeft = monthlyPayment - monthlyInterest;
            balance -= amountLeft;

            interestPaidSoFar += monthlyInterest;
        }
        //Set the values
        balanceLeft.setText(Double.toString(balance));
        interestPaid.setText(Double.toString(interestPaidSoFar));
    }

    public void lookupContactEmail(View v){

    }

    public void sendEmailWInfo(View v){

    }
}
