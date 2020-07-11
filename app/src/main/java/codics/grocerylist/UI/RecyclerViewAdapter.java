package codics.grocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import codics.grocerylist.Activites.DetailsActivity;
import codics.grocerylist.Data.DatabaseHandler;
import codics.grocerylist.Model.Grocery;
import codics.grocerylist.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<Grocery> groceryItems;
    // FOR THE CONFIRMATION ALERT DIALOG
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    // WE ARE GOING TO INFLATE OUR LAYOUT
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryItems = groceryList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Grocery grocery = groceryItems.get(position);

        //BINDING TEXT TO HOLDER

        holder.groceryName.setText(grocery.getName());
        holder.groceryQty.setText(grocery.getQuantity());
        holder.groceryDateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groceryName, groceryQty, groceryDateAdded;
        public Button editButton, deleteButton;
        public int id;

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);

            context = ctx;

            groceryName = (TextView) view.findViewById(R.id.groceryNameId);
            groceryQty = (TextView) view.findViewById(R.id.groceryQuantityID);
            groceryDateAdded = (TextView) view.findViewById(R.id.groceryDateAddedID);

            editButton = (Button) view.findViewById(R.id.editButtonID);
            deleteButton = (Button) view.findViewById(R.id.deleteButtonID);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // GO TO THE NEXT SCREEN AFTER CLICKING THE ROW

                    int position = getAdapterPosition();

                    Grocery grocery = groceryItems.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("Name ", grocery.getName());
                    intent.putExtra("Quantity ", grocery.getQuantity());
                    intent.putExtra("ID ", grocery.getId());
                    intent.putExtra("Date Item Added ", grocery.getDateItemAdded());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.editButtonID:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);
                    editItem(grocery);


                    break;

                case R.id.deleteButtonID:
                     position = getAdapterPosition();
                     grocery = groceryItems.get(position);
                    deleteItem(grocery.getId());

                    break;
            }

        }
        public void deleteItem(final int id){
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.confirmation_dialog,null );

            Button noButton = (Button) view.findViewById(R.id.NoButtonID);
            Button yesButton = (Button) view.findViewById(R.id.YesButtonID);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // DISMISS IF NO IS CLICKED
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // DELETE IF YES

                    DatabaseHandler db = new DatabaseHandler(context);
                    // DELETE GROCERY
                    db.deleteGrocery(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                }
            });
    }

        public void editItem(final Grocery grocery){
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.grocery_item);
            final EditText quantity = (EditText) view.findViewById(R.id.grocery_quantity);
            final TextView title = (TextView) view.findViewById(R.id.grocery_text);
            title.setText("Edit Text");
            Button saveItems = (Button) view.findViewById(R.id.saveButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //UPDATE DATABASE
                    DatabaseHandler db = new DatabaseHandler(context);

                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    if(!groceryItem.getText().toString().isEmpty() &&
                            !quantity.getText().toString().isEmpty()){

                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(), grocery);

                    }else{
                        Snackbar.make(view, "Add Grocery and Quantity", Snackbar.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });
        }


    }
}
