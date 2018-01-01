package de.ahahn94.tunerdb;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout background;
    Button buttonSave, buttonSearch, buttonViewDB, buttonResetDB;
    RadioGroup radioGroupBank, radioGroupCell;
    TextView textView;
    EditText editTextEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //Disable keyboard popup at app start
        setContentView(R.layout.activity_main);
        final Database database = new Database(getFilesDir().getPath()+"/database.txt", R.raw.restore, getApplicationContext());

        /*
        Bind Layout to Objects
        */
        background = findViewById(R.id.background);
        buttonSave = findViewById(R.id.buttonSave);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonResetDB = findViewById(R.id.buttonResetDB);
        buttonViewDB = findViewById(R.id.buttonViewDB);
        radioGroupBank = findViewById(R.id.radioGroupBank);
        radioGroupCell = findViewById(R.id.radioGroupCell);
        textView = findViewById(R.id.textviewDB1);
        editTextEntry = findViewById(R.id.edittextEntry);

        /*
        OnClickListeners
         */
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = radioGroupBank.findViewById(radioGroupBank.getCheckedRadioButtonId()).getContentDescription().toString()
                        + "-" + radioGroupCell.findViewById(radioGroupCell.getCheckedRadioButtonId()).getContentDescription().toString();
                String value = editTextEntry.getText().toString();
                database.updateEntry(key, value);
                Toast.makeText(getApplicationContext(), "Entry saved", Toast.LENGTH_SHORT).show();
                if(textView.getText()!=""){
                    buttonViewDB.performClick(); //Update textView if "View All" was previously triggered.
                }
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entryKey = ((Button)radioGroupBank.findViewById(radioGroupBank.getCheckedRadioButtonId())).getContentDescription().toString()
                        + "-" + ((Button)radioGroupCell.findViewById(radioGroupCell.getCheckedRadioButtonId())).getContentDescription().toString();
                Toast.makeText(getApplicationContext(), "Entry loaded", Toast.LENGTH_SHORT).show();
                editTextEntry.setText(database.getDatabase().get(entryKey));
            }
        });

        buttonResetDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a confirm-dialog
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Reset Database?")
                        .setMessage("Do you really want to reset the database? Your changes will be overwritten by the factory defaults!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Reset the database
                                dialogInterface.dismiss();
                                database.restoreDatabase();
                                if(textView.getText()!=""){
                                    buttonViewDB.performClick(); //Update textView if "View All" was previously triggered.
                                }
                                if(editTextEntry.getText().toString() != ""){
                                    buttonSearch.performClick(); //Update editTextEntry if there is any text in it.
                                }
                                Toast.makeText(getApplicationContext(), "Database reset", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        buttonViewDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String databaseToText = "";
                for (Map.Entry<String, String> entry: database.getDatabase().entrySet()){
                    databaseToText = databaseToText + entry.getKey() + ": " + entry.getValue() + "\n";
                }
                textView.setText(databaseToText.substring(0,databaseToText.length() - 1)); //remove new line at the end of the list
            }
        });

        /*
        ToDo: smaller and bigger Displays
         */

    }

}
