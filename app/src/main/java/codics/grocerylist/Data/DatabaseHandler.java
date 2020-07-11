package codics.grocerylist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import codics.grocerylist.Model.Grocery;
import codics.grocerylist.Utils.Util;

public class DatabaseHandler extends SQLiteOpenHelper {


    private Context ctx;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.TABLE_NAME, null, Util.DATABASE_VERSION);

        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "( " +
                Util.KEY_ID + " INTEGER PRIMARY KEY, " + Util.KEY_NAME + " TEXT," +
                Util.KEY_QUANTITY + " TEXT, " +Util.KEY_DATE + " LONG " +  " )";

        db.execSQL(CREATE_GROCERY_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);

        onCreate(db);

    }

    // CRUD CREATE, READ, UPDATE, DELETE

    // ADD GROCERY
    public void addGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, grocery.getName());
        values.put(Util.KEY_QUANTITY, grocery.getQuantity());
        values.put(Util.KEY_DATE, java.lang.System.currentTimeMillis());

        // INSERT INFORMATION INTO DATABASE

        db.insert(Util.TABLE_NAME, null, values);

        Log.d("Saved!!! ", "Saved To DB");


    }

    // GET A GROCERY
    public Grocery getGrocery(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME, new String[] {Util.KEY_ID,
                Util.KEY_NAME, Util.KEY_DATE, Util.KEY_QUANTITY}, Util.KEY_ID + "=?",
        new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

            Grocery grocery = new Grocery();

            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Util.KEY_NAME)));
            grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Util.KEY_QUANTITY)));

            // CONVERT TIMESTAMP TO READABLE
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE)))
            .getTime());

            grocery.setDateItemAdded(formattedDate);

            return grocery;
    }

    // GET ALL GROCERY
        public List<Grocery> getAllGrocery(){
            SQLiteDatabase db = this.getReadableDatabase();

            List<Grocery> groceryList = new ArrayList<>();

            Cursor cursor = db.query(Util.TABLE_NAME, new String [] {Util.KEY_ID, Util.KEY_NAME,
            Util.KEY_QUANTITY, Util.KEY_DATE}, null, null, null, null,
            Util.KEY_DATE + " DESC");

            if(cursor.moveToFirst()){

                do{
                    Grocery grocery = new Grocery();

                    grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
                    grocery.setName(cursor.getString(cursor.getColumnIndex(Util.KEY_NAME)));
                    grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Util.KEY_QUANTITY)));

                    // CONVERT TIME
                    java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                    String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE)))
                    .getTime());

                    grocery.setDateItemAdded(formattedDate);

                    // ADD TO GROCERY LIST
                    groceryList.add(grocery);

                }while (cursor.moveToNext());
            }
            return groceryList;
        }



        // UPDATE GROCERY
    public int updateGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, grocery.getName());
        values.put(Util.KEY_QUANTITY, grocery.getQuantity());
        values.put(Util.KEY_DATE, java.lang.System.currentTimeMillis());

        // UPDATE THE INFORMATION INTO DATABASE

        return db.update(Util.TABLE_NAME, values , Util.KEY_ID + "=?", new String[] {String.valueOf(grocery.getId())});

    }

    // DELETE GROCERY

    public void deleteGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();

    }

    // GET COUNT

    public int getGroceryCount(){
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

}
