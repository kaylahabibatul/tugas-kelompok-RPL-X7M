import java.time.LocalDate;
import java.util.*;

/** Pengujian blackbox: hanya memakai input dan memeriksa output, tanpa melihat isi kode. */
public class TestBlackbox {
    static List<String[]> rows = new ArrayList<>();
    static void t(String id, String fitur, String skenario, String input, String harapan, String aktual, boolean ok) {
        rows.add(new String[]{id, fitur, skenario, input, harapan, aktual, ok ? "Sesuai" : "Tidak sesuai"});
    }
    static Pelanggan p(int id, String nama, String hp) { return new Pelanggan(id, nama, "Jl. Contoh " + id, hp, "Perorangan"); }
    static String exc(Runnable r) { try { r.run(); return "tidak ada error"; } catch (Exception e) { return e.getClass().getSimpleName(); } }

    public static void main(String[] a) {
        // ===== Login =====
        Admin ad = new Admin(1, "admin", "admin123");
        boolean r = ad.login("admin", "admin123");
        t("BB-01","Login","Username dan password benar","admin / admin123","Login berhasil (true)",String.valueOf(r),r);
        r = ad.login("admin", "salah");
        t("BB-02","Login","Password salah","admin / salah","Login gagal (false)",String.valueOf(r),!r);
        r = ad.login("orang", "admin123");
        t("BB-03","Login","Username salah","orang / admin123","Login gagal (false)",String.valueOf(r),!r);
        r = ad.login("", "");
        t("BB-04","Login","Username dan password kosong","(kosong) / (kosong)","Login gagal (false)",String.valueOf(r),!r);
        ad.login("admin","admin123"); ad.logout();
        t("BB-05","Login","Logout setelah login","login lalu logout","Status login menjadi false",String.valueOf(ad.isSedangLogin()),!ad.isSedangLogin());

        // ===== Data Pelanggan =====
        Admin a2 = new Admin(1,"admin","admin123");
        r = a2.tambahPelanggan(p(1,"Budi Santoso","081234567890"));
        t("BB-06","Data Pelanggan","Tambah data valid","Budi Santoso, 081234567890","Data tersimpan (true), jumlah 1",r+", jumlah "+a2.getDaftarPelanggan().size(),r&&a2.getDaftarPelanggan().size()==1);
        r = a2.tambahPelanggan(p(2,"","081234567891"));
        t("BB-07","Data Pelanggan","Nama dikosongkan","nama kosong, 081234567891","Ditolak (false)",String.valueOf(r),!r);
        r = a2.tambahPelanggan(p(3,"Sari","08abc12345"));
        t("BB-08","Data Pelanggan","No. HP berisi huruf","08abc12345","Ditolak (false)",String.valueOf(r),!r);
        r = a2.tambahPelanggan(p(4,"Rudi","1234567"));
        t("BB-09","Data Pelanggan","No. HP 7 digit (di bawah batas)","1234567","Ditolak (false)",String.valueOf(r),!r);
        r = a2.tambahPelanggan(p(5,"Dewi","12345678"));
        t("BB-10","Data Pelanggan","No. HP 8 digit (batas bawah)","12345678","Diterima (true)",String.valueOf(r),r);
        r = a2.tambahPelanggan(p(6,"Toni","123456789012345"));
        t("BB-11","Data Pelanggan","No. HP 15 digit (batas atas)","123456789012345","Diterima (true)",String.valueOf(r),r);
        r = a2.tambahPelanggan(p(7,"Lina","1234567890123456"));
        t("BB-12","Data Pelanggan","No. HP 16 digit (di atas batas)","1234567890123456","Ditolak (false)",String.valueOf(r),!r);
        r = a2.tambahPelanggan(p(8,"Budi Santoso","081234567890"));
        t("BB-13","Data Pelanggan","Tambah data duplikat (nama dan HP sama)","Budi Santoso, 081234567890","Ditolak (false)",String.valueOf(r),!r);
        r = a2.ubahPelanggan(1,"Budi S.","Alamat Baru","081999888777","Kontraktor");
        String nm = a2.getDaftarPelanggan().get(0).getNama();
        t("BB-14","Data Pelanggan","Ubah data dengan nilai valid","id 1 -> Budi S., 081999888777","Berhasil (true), nama menjadi Budi S.",r+", nama "+nm,r&&nm.equals("Budi S."));
        r = a2.ubahPelanggan(1,"Nama Rusak","Alamat","abc","Kontraktor");
        nm = a2.getDaftarPelanggan().get(0).getNama();
        t("BB-15","Data Pelanggan","Ubah data dengan No. HP tidak valid","id 1 -> Nama Rusak, HP abc","Ditolak (false) dan data lama tetap Budi S.",r+", nama "+nm,!r&&nm.equals("Budi S."));
        r = a2.hapusPelanggan(5);
        t("BB-16","Data Pelanggan","Hapus pelanggan yang ada","id 5","Terhapus (true)",String.valueOf(r),r);
        r = a2.hapusPelanggan(999);
        t("BB-17","Data Pelanggan","Hapus pelanggan yang tidak ada","id 999","Gagal (false)",String.valueOf(r),!r);
        int n = a2.cariPelanggan("budi").size();
        t("BB-18","Data Pelanggan","Cari kata yang ada (tanpa peduli huruf besar/kecil)","budi","Ditemukan minimal 1 data",n+" data",n>=1);
        n = a2.cariPelanggan("zzzz").size();
        t("BB-19","Data Pelanggan","Cari kata yang tidak ada","zzzz","0 data",n+" data",n==0);

        // ===== Segmentasi =====
        Admin a3 = new Admin(1,"admin","admin123");
        Random rnd = new Random(7); List<Transaksi> trx = new ArrayList<>(); int idT=1;
        for (int i=1;i<=60;i++) {
            a3.tambahPelanggan(new Pelanggan(i,"Pelanggan "+i,"Alamat","0812000"+String.format("%05d",i),"Perorangan"));
            boolean loyal=i<=20; int banyak=loyal?8+rnd.nextInt(5):1+rnd.nextInt(2);
            for(int k=0;k<banyak;k++) trx.add(new Transaksi(idT++,i,LocalDate.now().minusDays(loyal?rnd.nextInt(40):90+rnd.nextInt(200)),loyal?2000000+rnd.nextInt(5000000):100000+rnd.nextInt(600000)));
        }
        a3.tambahPelanggan(new Pelanggan(61,"Tanpa Transaksi","Alamat","081200099999","Perorangan"));
        HasilSegmentasi h = a3.jalankanSegmentasi(trx, 2);
        t("BB-20","Segmentasi","Jalankan K-Means dengan K = 2","60 pelanggan bertransaksi, K=2","2 cluster, total anggota 60",h.getDaftarCluster().size()+" cluster, total "+h.totalPelanggan(),h.getDaftarCluster().size()==2&&h.totalPelanggan()==60);
        String e = exc(() -> a3.jalankanSegmentasi(trx, 100));
        t("BB-21","Segmentasi","K lebih besar dari jumlah data","K=100, data 60","Ditolak dengan pesan error (IllegalArgumentException)",e,e.equals("IllegalArgumentException"));
        e = exc(() -> a3.jalankanSegmentasi(trx, 0));
        t("BB-22","Segmentasi","K = 0 (tidak valid)","K=0","Ditolak dengan pesan error (IllegalArgumentException)",e,e.equals("IllegalArgumentException"));
        e = exc(() -> a3.jalankanSegmentasi(trx, 1));
        t("BB-23","Segmentasi","K = 1 (di bawah batas minimal 2)","K=1","Ditolak dengan pesan error (IllegalArgumentException)",e,e.equals("IllegalArgumentException"));
        Preprocessing pre = new Preprocessing();
        List<DataRFM> d = pre.hitungRFM(a3.getDaftarPelanggan(), trx);
        t("BB-24","Segmentasi","Pelanggan tanpa transaksi dikeluarkan dari data","61 pelanggan, 1 tanpa transaksi","60 data RFM",d.size()+" data RFM",d.size()==60);
        pre.normalisasiMinMax(d); boolean semua=true;
        for (DataRFM x : d) for (double v : x.getTernormalisasi()) if (v<0||v>1) semua=false;
        t("BB-25","Segmentasi","Hasil normalisasi Min-Max","semua nilai R, F, M","Semua nilai di antara 0 dan 1",semua?"Semua dalam rentang 0-1":"Ada di luar rentang",semua);

        // ===== Hasil =====
        int total = h.totalPelanggan();
        int semuaN = h.anggotaCluster(0).size();
        t("BB-26","Hasil Segmentasi","Filter 'Semua' cluster","filter 0","Menampilkan seluruh 60 anggota",semuaN+" anggota",semuaN==total);
        int s = h.anggotaCluster(1).size()+h.anggotaCluster(2).size();
        t("BB-27","Hasil Segmentasi","Jumlah Cluster 1 + Cluster 2","filter 1 dan 2","Sama dengan total (60)",s+" anggota",s==total);
        int z = h.anggotaCluster(9).size();
        t("BB-28","Hasil Segmentasi","Filter cluster yang tidak ada","filter 9","0 anggota",z+" anggota",z==0);

        // ===== Strategi =====
        ManajerPemasaran m = new ManajerPemasaran(2,"manajer","manajer123");
        Cluster c = h.getDaftarCluster().get(0);
        r = m.simpanStrategi(c,"Program Loyalitas","Diskon volume");
        t("BB-29","Strategi Pemasaran","Simpan dengan judul dan rencana terisi","Program Loyalitas / Diskon volume","Tersimpan (true)",String.valueOf(r),r);
        r = m.simpanStrategi(c,"","Diskon volume");
        t("BB-30","Strategi Pemasaran","Judul dikosongkan","judul kosong","Ditolak (false)",String.valueOf(r),!r);
        r = m.simpanStrategi(c,"Judul","");
        t("BB-31","Strategi Pemasaran","Rencana dikosongkan","rencana kosong","Ditolak (false)",String.valueOf(r),!r);
        r = m.simpanStrategi(c,"   ","   ");
        t("BB-32","Strategi Pemasaran","Judul dan rencana hanya spasi","(spasi) / (spasi)","Ditolak (false)",String.valueOf(r),!r);
        int ns = m.getDaftarStrategi().size();
        t("BB-33","Strategi Pemasaran","Jumlah strategi tersimpan setelah 1 simpan berhasil","4 percobaan simpan, 1 valid","1 strategi",ns+" strategi",ns==1);

        for (String[] x : rows) System.out.println(String.join("\t", x));
    }
}
