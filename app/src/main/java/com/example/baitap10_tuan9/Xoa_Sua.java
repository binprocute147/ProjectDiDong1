// Xoa_Sua.java
package com.example.baitap10_tuan9;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

// Định nghĩa một lớp Xoa_Sua kế thừa từ AppCompatActivity
public class Xoa_Sua extends AppCompatActivity {

    // Phương thức khởi tạo giao diện
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xoa_sua);
        // Khởi tạo ImageView và thiết lập sự kiện click
        ImageView addNoteIcon = findViewById(R.id.image_view_exit_icon);
        addNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để mở MainActivity khi nhấn vào icon
                Intent intent = new Intent(Xoa_Sua.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Lấy tiêu đề từ Intent
        Intent intent = getIntent();
        String tieude = intent.getStringExtra("TIEUDE");

        if (tieude != null) {
            // Lấy dữ liệu từ cơ sở dữ liệu
            DBGhiChu dbGhiChu = new DBGhiChu(Xoa_Sua.this);
            GhiChu ghiChu = dbGhiChu.LayDL(tieude);

            // Hiển thị dữ liệu lên các trường nhập
            ((EditText) findViewById(R.id.edit_text_title)).setText(ghiChu.getTieude());
            Spinner spinnerNoteType = findViewById(R.id.spinner_note_type);
            spinnerNoteType.setSelection(getIndex(spinnerNoteType, ghiChu.getLoaighichu()));
            ((EditText) findViewById(R.id.edit_text_note_content)).setText(ghiChu.getNoidungghichu());
        }
        ListView listView = findViewById(R.id.DanhSach);
        Button buttonDelete = findViewById(R.id.buttondelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Xoa_Sua.this)
                        .setTitle("Xóa Ghi Chú")
                        .setMessage("Bạn có muốn xóa ghi chú này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Xóa ghi chú khỏi cơ sở dữ liệu
                                DBGhiChu dbGhiChu = new DBGhiChu(Xoa_Sua.this);
                                GhiChu ghiChu = new GhiChu(tieude, null, null);  // Tạo một đối tượng GhiChu với tieude
                                dbGhiChu.XoaDL(ghiChu);

                                // Hiển thị thông báo xóa thành công
                                Toast.makeText(Xoa_Sua.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                                // Quay lại MainActivity
                                Intent intent = new Intent(Xoa_Sua.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Không", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        EditText editTextTieuDe = findViewById(R.id.edit_text_title);
        editTextTieuDe.setEnabled(false);  // Khóa trường nhập tiêu đề
        Button buttonEdit = findViewById(R.id.buttonedit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các trường nhập
                String tieude = ((EditText) findViewById(R.id.edit_text_title)).getText().toString();
                String loaighichu = ((Spinner) findViewById(R.id.spinner_note_type)).getSelectedItem().toString();
                String noidungghichu = ((EditText) findViewById(R.id.edit_text_note_content)).getText().toString();

                // Tạo một đối tượng GhiChu mới
                GhiChu ghiChu = new GhiChu(tieude, loaighichu, noidungghichu);

                // Lưu đối tượng GhiChu vào cơ sở dữ liệu
                DBGhiChu dbGhiChu = new DBGhiChu(Xoa_Sua.this);
                dbGhiChu.SuaDL(ghiChu);

                // Hiển thị thông báo sửa thành công
                Toast.makeText(Xoa_Sua.this, "Sửa thành công", Toast.LENGTH_SHORT).show();

                // Cập nhật ListView
                ArrayList<GhiChu> danhSachGhiChu = new ArrayList<>();
                danhSachGhiChu.add(ghiChu);  // Thêm đối tượng GhiChu đã sửa vào danh sách
                ArrayAdapter<GhiChu> adapter = new ArrayAdapter<>(Xoa_Sua.this, android.R.layout.simple_list_item_1, danhSachGhiChu);
                listView.setAdapter(adapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy tiêu đề từ ListView
                String tieude = (String) parent.getItemAtPosition(position);
                if (tieude != null) {
                    // Lấy dữ liệu từ cơ sở dữ liệu
                    DBGhiChu dbGhiChu = new DBGhiChu(Xoa_Sua.this);
                    GhiChu ghiChu = dbGhiChu.LayDL(tieude);

                    // Hiển thị dữ liệu lên các trường nhập
                    ((EditText) findViewById(R.id.edit_text_title)).setText(ghiChu.getTieude());
                    Spinner spinnerNoteType = findViewById(R.id.spinner_note_type);
                    spinnerNoteType.setSelection(getIndex(spinnerNoteType, ghiChu.getLoaighichu()));
                    ((EditText) findViewById(R.id.edit_text_note_content)).setText(ghiChu.getNoidungghichu());
                }
            }
        });
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return -1;
    }


}
