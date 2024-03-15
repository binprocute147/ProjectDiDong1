package com.example.baitap10_tuan9;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DBGhiChu extends SQLiteOpenHelper {
    public DBGhiChu(@Nullable Context context){
        super(context , "dbGhiChu" , null , 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "Create table tbGhiChu (tieude text , loaighichu text , noidungghichu text ) ";
        sqLiteDatabase.execSQL(sql);
    }
    public void ThemDL (GhiChu gb)
    {
        String sql = "insert into tbGhiChu values (? , ? , ? )";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL (sql , new String []{gb.getTieude() , gb.getLoaighichu(), gb.getNoidungghichu()});
    }
    public void XoaDL (GhiChu gb)
    {
        String sql = "Delete from tbGhiChu where tieude= ?";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL (sql , new String []{gb.getTieude()});
    }
    public void SuaDL (GhiChu gb)
    {
        String sql = "update tbGhiChu set loaighichu= ? ,noidungghichu = ?  where tieude = ? ";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL (sql , new String []{gb.getLoaighichu() ,gb.getNoidungghichu(),gb.getTieude() });
    }
    public GhiChu LayDL(String tieude) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tbGhiChu", new String[]{"tieude", "loaighichu", "noidungghichu"}, "tieude=?", new String[]{tieude}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        GhiChu ghiChu = new GhiChu(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        return ghiChu;
    }

    public ArrayList<GhiChu> LayTatCaDL() {
        ArrayList<GhiChu> danhSachGhiChu = new ArrayList<>();

        // Lấy cơ sở dữ liệu để đọc
        SQLiteDatabase db = this.getReadableDatabase();

        // Thực hiện truy vấn để lấy tất cả các ghi chú
        Cursor cursor = db.query("tbGhiChu", new String[]{"tieude", "loaighichu", "noidungghichu"}, null, null, null, null, null);

        // Duyệt qua tất cả các hàng trong kết quả truy vấn
        if (cursor.moveToFirst()) {
            do {
                // Tạo một đối tượng GhiChu mới từ dữ liệu trong hàng hiện tại
                GhiChu ghiChu = new GhiChu(cursor.getString(0), cursor.getString(1), cursor.getString(2));

                // Thêm đối tượng GhiChu vào danh sách
                danhSachGhiChu.add(ghiChu);
            } while (cursor.moveToNext());
        }

        // Đảm bảo rằng con trỏ được đóng lại sau khi hoàn tất
        cursor.close();

        return danhSachGhiChu;
    }
    // Phương thức để kiểm tra xem một tiêu đề đã tồn tại trong cơ sở dữ liệu chưa
    public boolean checkTitleExists(String tieude) {
        // Tạo một đối tượng SQLiteDatabase để đọc cơ sở dữ liệu
        SQLiteDatabase db = this.getReadableDatabase();

        // Tạo một truy vấn SQL để tìm các hàng có tiêu đề khớp
        String query = "SELECT * FROM " + "tbGhiChu" + " WHERE " + "tieude" + " = ?";

        // Thực hiện truy vấn và lấy kết quả
        Cursor cursor = db.rawQuery(query, new String[] {tieude});

        // Kiểm tra xem có bất kỳ hàng nào khớp không
        boolean exists = (cursor.getCount() > 0);

        // Đóng con trỏ và cơ sở dữ liệu
        cursor.close();
        db.close();

        // Trả về true nếu tiêu đề tồn tại, false nếu không
        return exists;
    }

    public int DemTatCaGhiChu() {
        String sql = "SELECT COUNT(*) FROM tbGhiChu";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public HashMap<String, Integer> DemLoaiGhiChu() {
        String sql = "SELECT loaighichu, COUNT(*) FROM tbGhiChu GROUP BY loaighichu";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        HashMap<String, Integer> counts = new HashMap<>();
        while (cursor.moveToNext()) {
            counts.put(cursor.getString(0), cursor.getInt(1));
        }
        cursor.close();
        return counts;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
