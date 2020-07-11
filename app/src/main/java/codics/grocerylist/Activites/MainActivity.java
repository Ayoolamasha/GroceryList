package codics.grocerylist.Activites;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import codics.grocerylist.Data.DatabaseHandler;
import codics.grocerylist.Model.Grocery;
import codics.grocerylist.R;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem, groceryQuantity;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        byPassActivity();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                createPopupDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPopupDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        groceryItem = (EditText) view.findViewById(R.id.grocery_item);
        groceryQuantity = (EditText) view.findViewById(R.id.grocery_quantity);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: SAVE TO DATABASE
                //TODO: GO TO NEXT SCREEN

                if(!groceryItem.getText().toString().isEmpty() &&
                        !groceryQuantity.getText().toString().isEmpty()){
                    saveGroceryToDb(v);

                }


            }
        });

    }

    private void saveGroceryToDb(View v) {

        Grocery grocery = new Grocery();

        String newGroceryName = groceryItem.getText().toString();
        String newGroceryQty = groceryQuantity.getText().toString();

        grocery.setName(newGroceryName);
        grocery.setQuantity(newGroceryQty);

        // SAVE TO DATABASE
        db.addGrocery(grocery);

        Snackbar.make(v,"Item Saved! " , Snackbar.LENGTH_LONG).show();

        //Log.d("Item Added ID: " , String.valueOf(db.getAllGrocery()));

        // THIS CODE IS TO CAUSE A SECOND DELAY THEN MOVE TO THE NEXT ACTIVITY
        // IN ORDER TO SHOW THE USER THE LIST OF ITEMS THAT HAS BEEN SAVED
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // THIS CODE DISMISSES THE DIALOG
                dialog.dismiss();
                //THIS CODE SENDS THE USER TO THE NEXT SCREEN
                startActivity(new Intent(MainActivity.this, ListActivity.class));

            }
        }, 1000); // 1 SECOND
    }

    public void byPassActivity(){
        // THIS METHOD CHECKS IF WE ALREADY HAVE ITEMS IN OUR DB
        // IF SO THEN WE GO TO THE LIST ACTIVITY DIRECTLY

        if(db.getGroceryCount() > 0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            // THIS IS NEEDED SO WE WON'T GO BACK TO THE MAIN ACTIVITY IF WE PRESS BACK
            finish();
        }
    }

}
