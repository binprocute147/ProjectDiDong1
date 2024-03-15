package com.example.baitap10_tuan9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class ThongKe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        // Khởi tạo ImageView và thiết lập sự kiện click
        ImageView addNoteIcon = findViewById(R.id.image_view_exit_icon);
        addNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để mở MainActivity khi nhấn vào icon
                Intent intent = new Intent(ThongKe.this, MainActivity.class);
                startActivity(intent);
            }
        });
        DBGhiChu db = new DBGhiChu(this);
        int tongGhiChu = db.DemTatCaGhiChu();
        HashMap<String, Integer> loaiGhiChu = db.DemLoaiGhiChu();

        // Hiển thị thông tin lên giao diện người dùng
        EditText editTextAllNotes = findViewById(R.id.edit_text_all_notes);
        editTextAllNotes.setText("Tổng Ghi Chú: " + tongGhiChu);

        for (Map.Entry<String, Integer> entry : loaiGhiChu.entrySet()) {
            double phanTram = 100.0 * entry.getValue() / tongGhiChu;
            editTextAllNotes.append("\nLoại ghi chú: " + entry.getKey() + " chiếm " + phanTram + "%");
        }


    }
}