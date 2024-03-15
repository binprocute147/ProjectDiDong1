package com.example.baitap10_tuan9;

public class GhiChu {
    String tieude ,  loaighichu , noidungghichu;

    public GhiChu() {
    }

    public GhiChu(String tieude, String loaighichu, String noidungghichu) {
        this.tieude = tieude;
        this.loaighichu = loaighichu;
        this.noidungghichu = noidungghichu;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getLoaighichu() {
        return loaighichu;
    }

    public void setLoaighichu(String loaighichu) {
        this.loaighichu = loaighichu;
    }

    public String getNoidungghichu() {
        return noidungghichu;
    }

    public void setNoidungghichu(String noidungghichu) {
        this.noidungghichu = noidungghichu;
    }

    @Override
    public String toString() {
        return "GhiChu{" +
                "Tiêu đề: ='" + tieude + '\'' +
                ", Loại Ghi Chú='" + loaighichu + '\'' +
                ", Nội Dung Ghi Chú='" + noidungghichu + '\'' +
                '}';
    }

}
