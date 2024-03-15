package com.example.baitap10_tuan9;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class TimKiem_XoaSua extends AppCompatActivity {

    DBGhiChu dbGhiChu;
    EditText titleEditText;
    Spinner noteTypeSpinner;
    EditText noteContentEditText;
    ListView listView;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem_xoa_sua);

        dbGhiChu = new DBGhiChu(this);
        titleEditText = findViewById(R.id.edit_text_title);
        noteTypeSpinner = findViewById(R.id.spinner_note_type);
        noteContentEditText = findViewById(R.id.edit_text_note_content);
        Button deleteButton = findViewById(R.id.buttondelete);
        Button editButton = findViewById(R.id.buttonedit);
        listView = findViewById(R.id.DanhSach);

        // Khởi tạo ImageView và thiết lập sự kiện click
        ImageView addNoteIcon = findViewById(R.id.image_view_exit_icon);
        addNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để mở MainActivity khi nhấn vào icon
                Intent intent = new Intent(TimKiem_XoaSua.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        String noteType = intent.getStringExtra("noteType");
        String noteContent = intent.getStringExtra("noteContent");

        // Hiển thị dữ liệu lên giao diện
        titleEditText.setText(title);

        // Tạo ArrayAdapter từ mảng các loại ghi chú
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.note_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noteTypeSpinner.setAdapter(adapter);

        // Tìm vị trí của noteType trong mảng và thiết lập vị trí này cho Spinner
        int position = adapter.getPosition(noteType);
        noteTypeSpinner.setSelection(position);

        noteContentEditText.setText(noteContent);

        // Thiết lập sự kiện click cho nút "Xóa"
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TimKiem_XoaSua.this)
                        .setTitle("Xóa Ghi Chú")
                        .setMessage("Bạn có muốn xóa ghi chú này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Xóa ghi chú khỏi cơ sở dữ liệu
                                GhiChu ghiChu = new GhiChu(title, null, null);
                                dbGhiChu.XoaDL(ghiChu);

                                // Quay lại màn hình tìm kiếm
                                Intent intent = new Intent(TimKiem_XoaSua.this, TimKiem.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Không", null)
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
                DBGhiChu dbGhiChu = new DBGhiChu(TimKiem_XoaSua.this);
                dbGhiChu.SuaDL(ghiChu);

                // Hiển thị thông báo sửa thành công
                Toast.makeText(TimKiem_XoaSua.this, "Sửa thành công", Toast.LENGTH_SHORT).show();

                // Cập nhật ListView
                ArrayList<GhiChu> danhSachGhiChu = new ArrayList<>();
                danhSachGhiChu.add(ghiChu);  // Thêm đối tượng GhiChu đã sửa vào danh sách
                ArrayAdapter<GhiChu> adapter = new ArrayAdapter<>(TimKiem_XoaSua.this, android.R.layout.simple_list_item_1, danhSachGhiChu);
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
                    DBGhiChu dbGhiChu = new DBGhiChu(TimKiem_XoaSua.this);
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
