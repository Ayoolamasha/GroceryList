package codics.grocerylist.Activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import codics.grocerylist.Data.DatabaseHandler;
import codics.grocerylist.Model.Grocery;
import codics.grocerylist.R;
import codics.grocerylist.UI.RecyclerViewAdapter;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DatabaseHandler db;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private EditText itemName, itemQuantity;
    private Button saveItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                createPopUpDialog();
            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();

        // GET ALL GROCERY
        groceryList = db.getAllGrocery();

        for (Grocery c : groceryList){
            Grocery grocery = new Grocery();

            grocery.setName(c.getName());
            grocery.setQuantity("Qty: " + c.getQuantity() + " kilos");
            grocery.setId(c.getId());
            grocery.setDateItemAdded("Date Added: " + c.getDateItemAdded());

            listItems.add(grocery);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void createPopUpDialog() {

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        itemName = (EditText) view.findViewById(R.id.grocery_item);
        itemQuantity = (EditText) view.findViewById(R.id.grocery_quantity);
        saveItemButton = (Button) view.findViewById(R.id.saveButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        saveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO SAVE TO DB

                if(!itemName.getText().toString().isEmpty() &&
                !itemQuantity.getText().toString().isEmpty()){

                    saveGroceryToDB(v);
                }
            }
        });
    }

    private void saveGroceryToDB(View v) {

        Grocery grocery = new Grocery();

        String newGroceryName = itemName.getText().toString();
        String newGroceryQty = itemQuantity.getText().toString();

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
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();

            }
        }, 1000); // 1 SECOND
    }


}
