package com.xiuchen.org.xiuchen_subbook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvRecord;
    TextView tvTotal;
    List<Record> mData;
    RecordAdapter mAdapter;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    Record editTemp;

    SQLiteDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvRecord = findViewById(R.id.rv_record);
        tvTotal = findViewById(R.id.tv_total);

        mData = new ArrayList<>();
        mAdapter = new RecordAdapter(mData);
        rvRecord.setLayoutManager(new LinearLayoutManager(this));
        rvRecord.setAdapter(mAdapter);

        mDbHelper = new SQLiteDbHelper(this);
        listRecord();
        calculateCharge();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.record_add:
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.setClass(this, AddActivity.class);
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Record record = (Record) data.getSerializableExtra("record");
            mData.add(record);
            mAdapter.notifyDataSetChanged();
            Log.d("MainActivity", mData.size() + " " + mAdapter.getItemCount());
            calculateCharge();
            newRecord(record);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Record record = (Record) data.getSerializableExtra("record");
            if (editTemp != null) {
                for (Record item : mAdapter.mData) {
                    if (item.equals(editTemp)) {
                        item.setName(record.getName());
                        item.setComments(record.getComments());
                        item.setDate(record.getDate());
                        item.setCharge(record.getCharge());
                        item.setId(record.getId());
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
            calculateCharge();
            updateRecord(record);
        }
        listRecord();
    }

    class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {

        List<Record> mData;

        public RecordAdapter(List<Record> mData) {
            this.mData = mData;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.item_record_2, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (mData.get(position).getName() != null)
                holder.tvName.setText("Name: " + mData.get(position).getName());
            if (mData.get(position).getCharge() != null)
                holder.tvCharge.setText("Charge: " + mData.get(position).getCharge().toString());
            if (mData.get(position).getDate() != null)
                holder.tvDate.setText("Date: " + format.format(mData.get(position).getDate()));
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRecord(mData.get(position));
                    mData.remove(position);
                    mAdapter.notifyDataSetChanged();
                    calculateCharge();
                }
            });
            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("type", 2);
                    intent.putExtra("record", mData.get(position));
                    editTemp = mData.get(position);
                    intent.setClass(MainActivity.this, AddActivity.class);
                    startActivityForResult(intent, 2);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvName, tvCharge, tvDate;
            Button ivEdit, ivDelete;

            public MyViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tv_name);
                tvCharge = view.findViewById(R.id.tv_charge);
                tvDate = view.findViewById(R.id.tv_date);
                ivEdit = view.findViewById(R.id.btn_edit);
                ivDelete = view.findViewById(R.id.btn_delete);
            }
        }
    }

    private void calculateCharge() {
        double total = 0;
        for (Record record : mAdapter.mData) {
            total += record.getCharge();
        }
        tvTotal.setText("Total Charge: " + total);
    }

    private void newRecord(Record record) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("name", record.getName());
        value.put("charge", record.getCharge());
        value.put("date", format.format(record.getDate()));
        value.put("comments", record.getComments());
        db.insert("record", null, value);
        db.close();
    }

    private void deleteRecord(Record record) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete("record", "id=?", new String[]{record.getId().toString()});
        db.close();
    }

    private void updateRecord(Record record) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("name", record.getName());
        value.put("charge", record.getCharge());
        value.put("date", format.format(record.getDate()));
        value.put("comments", record.getComments());
        db.update("record", value, "id=?", new String[]{record.getId().toString()});
        db.close();
    }

    private void listRecord() {
        mData.clear();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from record", null);
        while (cursor.moveToNext()) {
            Record record = new Record();
            record.setId(cursor.getInt(cursor.getColumnIndex("id")));
            record.setName(cursor.getString(cursor.getColumnIndex("name")));
            record.setCharge(cursor.getDouble(cursor.getColumnIndex("charge")));
            try {
                record.setDate(format.parse(cursor.getString(cursor.getColumnIndex("date"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("MainActivity", cursor.getString(cursor.getColumnIndex("date")));
            record.setComments(cursor.getString(cursor.getColumnIndex("comments")));
            mData.add(record);
        }
        mAdapter.notifyDataSetChanged();

    }

}