package com.novrikurniawan.listapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private ListAdapter mAdapter;
    private EditText mEditTextName;
//    private TextView mTextViewAmount;
//    private int mAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListDBHelper dbHelper = new ListDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ListAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        mEditTextName = findViewById(R.id.edittext_name);
//        mTextViewAmount = findViewById(R.id.textview_amount);

//        Button buttonIncrease = findViewById(R.id.button_increase);
//        Button buttonDecrease = findViewById(R.id.button_decrease);
        Button buttonAdd = findViewById(R.id.button_add);

//        buttonIncrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                increase();
//            }
//        });
//
//        buttonDecrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                decrease();
//            }
//        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

//    private void increase() {
//        mAmount++;
//        mTextViewAmount.setText(String.valueOf(mAmount));
//    }
//
//    private void decrease() {
//        if (mAmount > 0) {
//            mAmount--;
//            mTextViewAmount.setText(String.valueOf(mAmount));
//        }
//    }

    private void addItem() {

//        if (mEditTextName.getText().toString().trim().length() == 0 || mAmount == 0) {
        if (mEditTextName.getText().toString().trim().length() == 0 ) {
            return;
        }

        String name = mEditTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(ListContract.ListEntry.COLUMN_NAME, name);
//        cv.put(ListContract.ListEntry.COLUMN_AMOUNT, mAmount);

        mDatabase.insert(ListContract.ListEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());

        mEditTextName.getText().clear();
    }

    private void removeItem(long id){
        mDatabase.delete(ListContract.ListEntry.TABLE_NAME, ListContract.ListEntry._ID + "="+id,null);
        mAdapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                ListContract.ListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ListContract.ListEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
}
