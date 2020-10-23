package com.sample.databasetrialapp.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sample.databasetrialapp.Data.DatabaseHandler;
import com.sample.databasetrialapp.Modal.Grocery;
import com.sample.databasetrialapp.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText itemName,itemUnit;
    private Spinner itemQty;
    private Button saveBtn;
    String unitSelection=null;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=new DatabaseHandler(this);
        byPassActivity();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              popUpDialog();
            }
        });

    }

    private void popUpDialog() {
        builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);
        itemName=(EditText) view.findViewById(R.id.groceryItem);
        itemQty=(Spinner) view.findViewById(R.id.groceryQty);
        itemUnit=(EditText) view.findViewById(R.id.groceryUnit);
        saveBtn=(Button) view.findViewById(R.id.saveButton);
        builder.setView(view);
        dialog=builder.create();
        dialog.show();
        List<String> unit=new ArrayList<>();
        unit.add("Select");
        unit.add("kg");
        unit.add("gms");
        unit.add("lit.");
        unit.add("pkt");
        unit.add("pcs");
        unit.add("box");
        unit.add("dozen");
        itemQty.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,unit);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemQty.setAdapter(adapter);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!itemName.getText().toString().isEmpty() && !itemUnit.getText().toString().isEmpty() && unitSelection!="Select") {
                    saveToDb(v);
                }
            }
        });
    }
    private void saveToDb(View v) {
        Grocery grocery = new Grocery();
        String newGrocery = itemName.getText().toString();

        String newGroceryQuantity =itemUnit.getText().toString()+" "+unitSelection;

        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);

        //Save to DB
        db.addGrocery(grocery);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_SHORT).show();

        // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(Homepage.this, ListActivity.class));
            }
        }, 500); //  1 second.

    }


    public void byPassActivity() {
        //Checks if database is empty; if not, then we just
        //go to ListActivity and show all added items

        if (db.getGroceriesCount() > 0) {
            startActivity(new Intent(Homepage.this, ListActivity.class));
            finish();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unitSelection=parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
