package com.xiuchen.org.xiuchen_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    Button btnAdd;
    EditText etName, etCharge, etComment;
    DatePicker dpDate;

    int type;
    Record record;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        etName = findViewById(R.id.et_name);
        etCharge = findViewById(R.id.et_charge);
        etComment = findViewById(R.id.et_comment);
        dpDate = findViewById(R.id.dp_date);
        btnAdd = findViewById(R.id.btn_add);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            btnAdd.setText("Add");
            record = new Record();
        } else if (type == 2) {
            btnAdd.setText("Edit");
            record = (Record) intent.getSerializableExtra("record");
            etName.setText(record.getName());
            etCharge.setText(record.getCharge().toString());
            etComment.setText(record.getComments());
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                try {
                    date = simpleDateFormat.parse((dpDate.getYear() + "-" + dpDate.getMonth() + "-" + dpDate.getDayOfMonth()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                record.setName(etName.getText().toString());
                record.setCharge(Double.valueOf(etCharge.getText().toString()));
                record.setComments(etComment.getText().toString());
                record.setDate(date);
                Intent intent = new Intent();
                intent.putExtra("record", record);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
