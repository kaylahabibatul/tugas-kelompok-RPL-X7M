import java.util.ArrayList;
import java.util.List;

/** Admin: mengelola data pelanggan dan menjalankan proses segmentasi (halaman 3 dan 4). */
public class Admin extends Pengguna {
    private final List<Pelanggan> daftarPelanggan = new ArrayList<>();

    public Admin(int id, String username, String password) {
        super(id, username, password, "Admin");
    }

    /** Halaman 3: tombol "+ Tambah" / "Simpan". Menolak data tidak valid atau duplikat. */
    public boolean tambahPelanggan(Pelanggan p) {
        if (!p.validasi()) return false;
        for (Pelanggan x : daftarPelanggan) {
            if (x.getNama().equalsIgnoreCase(p.getNama()) && x.getNoHp().equals(p.getNoHp())) return false;
        }
        return daftarPelanggan.add(p);
    }

    public boolean ubahPelanggan(int idPelanggan, String nama, String alamat, String noHp, String jenis) {
        for (Pelanggan p : daftarPelanggan) {
            if (p.getIdPelanggan() == idPelanggan) {
                // Validasi dulu pada data sementara agar data lama tidak rusak bila input salah
                Pelanggan uji = new Pelanggan(idPelanggan, nama, alamat, noHp, jenis);
                if (!uji.validasi()) return false;
                p.setNama(nama); p.setAlamat(alamat); p.setNoHp(noHp); p.setJenis(jenis);
                return true;
            }
        }
        return false;
    }

    public boolean hapusPelanggan(int idPelanggan) {
        return daftarPelanggan.removeIf(p -> p.getIdPelanggan() == idPelanggan);
    }

    /** Kotak pencarian pada tabel Data Pelanggan. */
    public List<Pelanggan> cariPelanggan(String kata) {
        List<Pelanggan> hasil = new ArrayList<>();
        for (Pelanggan p : daftarPelanggan) {
            if (p.getNama().toLowerCase().contains(kata.toLowerCase())
                    || p.getJenis().toLowerCase().contains(kata.toLowerCase())) hasil.add(p);
        }
        return hasil;
    }

    /** Halaman 4: tombol "Jalankan" (preprocessing, K, lalu K-Means). */
    public HasilSegmentasi jalankanSegmentasi(List<Transaksi> transaksi, int k) {
        Preprocessing pre = new Preprocessing();
        List<DataRFM> data = pre.hitungRFM(daftarPelanggan, transaksi);
        pre.normalisasiMinMax(data);
        KMeans km = new KMeans(k, 100);
        return km.jalankan(data);
    }

    public List<Pelanggan> getDaftarPelanggan() { return daftarPelanggan; }
}
