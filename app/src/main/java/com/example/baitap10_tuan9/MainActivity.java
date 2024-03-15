package com.example.baitap10_tuan9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DBGhiChu db;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> tieudeList;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Khởi tạo ImageView và thiết lập sự kiện click
        ImageView addNoteIcon = findViewById(R.id.image_view_add_note_icon);
        addNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để mở Them_Ghi_Chu khi nhấn vào icon
                Intent intent = new Intent(MainActivity.this, Them_Ghi_Chu.class);
                startActivity(intent);  // Khởi chạy Activity mới
            }
        });
        // Khởi tạo ListView
        listView = findViewById(R.id.list_view_saved_note_titles);
        // Khởi tạo ArrayList cho tiêu đề
        tieudeList = new ArrayList<>();

        // Khởi tạo ArrayAdapter
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, tieudeList);
        // Đặt ArrayAdapter cho ListView
        listView.setAdapter(adapter);

        // Thiết lập sự kiện click cho mỗi mục trong ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy tiêu đề từ ListView
                String tieude = (String) parent.getItemAtPosition(position);

                // Tạo Intent để mở Xoa_Sua khi nhấn vào tiêu đề
                Intent intent = new Intent(MainActivity.this, Xoa_Sua.class);
                intent.putExtra("TIEUDE", tieude);  // Đặt tieude như một extra cho Intent
                startActivity(intent);  // Khởi chạy Activity mới
            }
        });
        // Khởi tạo DBGhiChu
        db = new DBGhiChu(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Xóa danh sách tiêu đề cũ
        tieudeList.clear();
        // Lấy dữ liệu từ cơ sở dữ liệu
        ArrayList<GhiChu> danhSachGhiChu = db.LayTatCaDL();
        // Thêm tiêu đề mới vào danh sách
        for (GhiChu ghiChu : danhSachGhiChu) {
            tieudeList.add(ghiChu.getTieude());
        }
        // Thông báo cho adapter về sự thay đổi dữ liệu
        adapter.notifyDataSetChanged();

        setControl();
        setEvent();
    }
    private void setEvent(){
        //tạo nút menu
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //hiện nút
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.mntimkiem)
                {
                    Intent intent = new Intent(MainActivity.this, TimKiem.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Tìm Kiếm Ghi Chú", Toast.LENGTH_SHORT).show();
                }
                if(item.getItemId() == R.id.mnthongke)
                {
                    Intent intent = new Intent(MainActivity.this, ThongKe.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Thống Kê", Toast.LENGTH_SHORT).show();
                }
                if(item.getItemId() == R.id.mnthoat)
                {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Thoát ứng dụng")
                            .setMessage("Bạn có muốn thoát không?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Thoát ứng dụng
                                    finishAffinity();
                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();
                }
                //đóng header v menu sau khi hiện Toast

                drawerLayout.closeDrawers();
                if(fragment!=null)
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();
                }
                return false;
            }
        });
    }
    //khi bấm vào menu(3 gạch) sẽ hiện lên header và menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setControl(){
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navView);
    }
}