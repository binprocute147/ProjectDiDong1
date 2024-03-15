// Them_Ghi_Chu.java
package com.example.baitap10_tuan9;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

// Định nghĩa một lớp Them_Ghi_Chu kế thừa từ AppCompatActivity
public class Them_Ghi_Chu extends AppCompatActivity {

    // Phương thức khởi tạo giao diện
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_ghi_chu);

        // Khởi tạo ImageView và thiết lập sự kiện click
        ImageView addNoteIcon = findViewById(R.id.image_view_exit_icon);
        addNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để mở MainActivity khi nhấn vào icon
                Intent intent = new Intent(Them_Ghi_Chu.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Khởi tạo các trường nhập
        EditText editTextTieuDe = findViewById(R.id.edit_text_title);
        Spinner spinnerLoaiGhiChu = findViewById(R.id.spinner_loai_ghi_chu);
        EditText editTextNoiDungGhiChu = findViewById(R.id.edit_text_note_content);

        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các trường nhập
                String tieude = editTextTieuDe.getText().toString();
                String loaighichu = spinnerLoaiGhiChu.getSelectedItem().toString();
                String noidungghichu = editTextNoiDungGhiChu.getText().toString();

                // Kiểm tra xem tất cả các trường nhập có được điền đầy đủ không
                if (tieude.isEmpty()) {
                    editTextTieuDe.setError("Vui lòng nhập tiêu đề!");
                } else if (loaighichu.isEmpty()) {
                    // Hiển thị cảnh báo nếu không
                    Toast.makeText(Them_Ghi_Chu.this, "Vui lòng chọn loại ghi chú!", Toast.LENGTH_SHORT).show();
                } else if (noidungghichu.isEmpty()) {
                    editTextNoiDungGhiChu.setError("Vui lòng nhập nội dung ghi chú!");
                } else {
                    // Kiểm tra xem tiêu đề đã tồn tại hay chưa
                    DBGhiChu dbGhiChu = new DBGhiChu(Them_Ghi_Chu.this);
                    if(dbGhiChu.checkTitleExists(tieude)) {
                        Toast.makeText(Them_Ghi_Chu.this, "Tiêu đề đã tồn tại!", Toast.LENGTH_SHORT).show();
                        editTextTieuDe.setError("Vui lòng không trùng tên với tiêu đề đã lưu");
                    } else {
                        // Nếu chưa tồn tại, tạo một đối tượng GhiChu mới
                        GhiChu ghiChu = new GhiChu(tieude, loaighichu, noidungghichu);

                        // Lưu đối tượng GhiChu vào cơ sở dữ liệu
                        dbGhiChu.ThemDL(ghiChu);

                        // Hiển thị thông báo thành công
                        Toast.makeText(Them_Ghi_Chu.this, "Thêm ghi chú thành công!", Toast.LENGTH_SHORT).show();

                        // Quay lại MainActivity
                        Intent intent = new Intent(Them_Ghi_Chu.this, MainActivity.class);
                        intent.putExtra("TIEUDE", ghiChu.getTieude());  // Đặt tieude như một extra cho Intent
                        startActivity(intent);
                    }
                }
            }
        });

    }
}
