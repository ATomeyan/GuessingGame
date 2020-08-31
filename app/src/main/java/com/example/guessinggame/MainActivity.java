package com.example.guessinggame;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText textNumber;
    private TextView textView3;
    private int number;
    private int count;
    private int range = 100;
    private TextView txtRange;

    public void checkGuess(){

        String guessText = textNumber.getText().toString();
        String msg = "";

        try{
            int guess = Integer.parseInt(guessText);
            count++;

            if(guess < number)
                msg = guess + " is to low";
            else if(guess > number)
                msg = guess + " is to high";
            else {
                msg = guess + " is correct! You win after " + count + " tries!";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0) + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("gamesWon", gamesWon);
                editor.apply();
                newGame();
            }
        }catch (Exception e){
            msg = "Enter a whole number between 1 and " + range + ".";
        }finally {
            textView3.setText(msg);
            textNumber.requestFocus();
            textNumber.selectAll();
        }
    }

    @SuppressLint("SetTextI18n")
    public void newGame(){

        number = (int) (Math.random() * range + 1);
        txtRange.setText("Enter a number between 1 and " + range + ".");
        textNumber.setText("" + range / 2);
        textNumber.requestFocus();
        textNumber.selectAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textNumber = (EditText) findViewById(R.id.textNumber);
        Button button = (Button) findViewById(R.id.button);
        textView3 = (TextView) findViewById(R.id.textView3);
        txtRange = (TextView) findViewById(R.id.textView1);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        range = preferences.getInt("range", 100);
        newGame();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGuess();
            }
        });

        textNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                checkGuess();
                return true;
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_settings:
                final CharSequence[] items  = {"1 to 10", "1 to 100", "1 to 1000"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select the range:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                range = 10;
                                storeRange(10);
                                newGame();
                                break;
                            case 1:
                                range = 100;
                                storeRange(100);
                                newGame();
                                break;
                            case 2:
                                range = 1000;
                                storeRange(1000);
                                newGame();
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.action_newgame:
                newGame();
                return true;
            case R.id.action_gamestats:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0);
                AlertDialog statDialog = new AlertDialog.Builder(MainActivity.this).create();
                statDialog.setTitle("Guessing Game Stats");
                statDialog.setMessage("You have won " + gamesWon + " games. Way to go!");
                statDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                statDialog.show();
                return true;
            case R.id.action_about:
                AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
                aboutDialog.setTitle("About Guessing game");
                aboutDialog.setMessage("(C)2020 Artur Tomeyan");
                aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                aboutDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void storeRange(int newRange){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("range", newRange);
        editor.apply();
    }
}