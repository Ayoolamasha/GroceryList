package codics.grocerylist.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import codics.grocerylist.R;

public class DetailsActivity extends AppCompatActivity {
    private TextView groceryDetailsName, groceryDetailsQuantity, groceryDetailsDate;
    private int groceryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        groceryDetailsName = (TextView) findViewById(R.id.groceryNameDet);
        groceryDetailsQuantity = (TextView) findViewById(R.id.groceryQuantityDet);
        groceryDetailsDate = (TextView) findViewById(R.id.groceryDateAddedDet);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            groceryDetailsName.setText(bundle.getString("Name "));
            groceryDetailsQuantity.setText(bundle.getString("Quantity "));
            groceryDetailsDate.setText(bundle.getString("Date Item Added "));
            // TO HELP US DELETE
            groceryID = bundle.getInt("ID");
        }
    }
}
