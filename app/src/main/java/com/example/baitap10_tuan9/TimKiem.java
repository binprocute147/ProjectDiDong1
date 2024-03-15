package com.example.baitap10_tuan9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TimKiem extends AppCompatActivity {

    DBGhiChu dbGhiChu;
    ListView listView;
    EditText editText;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);

        dbGhiChu = new DBGhiChu(this);
        listView = findViewById(R.id.list_view_saved_note_titles);
        editText = findViewById(R.id.txttimkiem);

        // Khởi tạo ImageView và thiết lập sự kiện click
        ImageView addNoteIcon = findViewById(R.id.image_view_exit_icon);
        addNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để mở MainActivity khi nhấn vào icon
                Intent intent = new Intent(TimKiem.this, MainActivity.class);
                startActivity(intent);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (dbGhiChu.checkTitleExists(s.toString())) {
                    ArrayList<GhiChu> ghiChus = new ArrayList<>();
                    ghiChus.add(dbGhiChu.LayDL(s.toString()));

                    // Cập nhật ListView với kết quả tìm kiếm
                    ArrayList<String> titles = new ArrayList<>();
                    for (GhiChu ghiChu : ghiChus) {
                        titles.add(ghiChu.getTieude());
                    }
                    adapter = new ArrayAdapter<>(TimKiem.this, android.R.layout.simple_list_item_1, titles);
                    listView.setAdapter(adapter);
                } else {
                    // Hiển thị thông báo "Không tìm thấy ghi chú" trong ListView
                    ArrayList<String> titles = new ArrayList<>();
                    titles.add("Không tìm thấy ghi chú");
                    adapter = new ArrayAdapter<>(TimKiem.this, android.R.layout.simple_list_item_1, titles);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = (String) parent.getItemAtPosition(position);
                if (!title.equals("Không tìm thấy ghi chú")) {
                    GhiChu ghiChu = dbGhiChu.LayDL(title);
                    Intent intent = new Intent(TimKiem.this, TimKiem_XoaSua.class);
                    intent.putExtra("title", ghiChu.getTieude());
                    intent.putExtra("noteType", ghiChu.getLoaighichu());
                    intent.putExtra("noteContent", ghiChu.getNoidungghichu());
                    startActivity(intent);
                }
            }
        });

    }
}
