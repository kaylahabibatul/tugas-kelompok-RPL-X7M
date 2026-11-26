import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Demo alur sistem SegmenKu: Login, Data Pelanggan, Proses Segmentasi, Hasil, Strategi. */
public class Main {
    public static void main(String[] args) {
        Admin admin = new Admin(1, "admin", "admin123");
        ManajerPemasaran manajer = new ManajerPemasaran(2, "manajer", "manajer123");

        // 1. Login
        System.out.println("Login salah : " + admin.login("admin", "salah"));
        System.out.println("Login benar : " + admin.login("admin", "admin123"));

        // 3. Data Pelanggan (data contoh/simulasi 120 pelanggan, bukan data asli toko)
        Random rnd = new Random(7);
        List<Transaksi> transaksi = new ArrayList<>();
        int idTrx = 1;
        for (int i = 1; i <= 120; i++) {
            boolean loyal = i <= 38;
            String jenis = loyal ? "Kontraktor" : "Perorangan";
            admin.tambahPelanggan(new Pelanggan(i, "Pelanggan " + i, "Alamat " + i, "0812000" + String.format("%05d", i), jenis));
            int banyak = loyal ? 8 + rnd.nextInt(6) : 1 + rnd.nextInt(2);
            for (int t = 0; t < banyak; t++) {
                int hariLalu = loyal ? rnd.nextInt(40) : 90 + rnd.nextInt(250);
                double nilai = loyal ? 2_000_000 + rnd.nextInt(6_000_000) : 100_000 + rnd.nextInt(700_000);
                transaksi.add(new Transaksi(idTrx++, i, LocalDate.now().minusDays(hariLalu), nilai));
            }
        }
        System.out.println("Pelanggan terdaftar: " + admin.getDaftarPelanggan().size());
        System.out.println("Cari 'kontraktor': " + admin.cariPelanggan("kontraktor").size() + " data");

        // 4. Proses Segmentasi: grafik Elbow untuk memilih K, lalu jalankan K=2
        Preprocessing pre = new Preprocessing();
        List<DataRFM> data = pre.hitungRFM(admin.getDaftarPelanggan(), transaksi);
        pre.normalisasiMinMax(data);
        System.out.println("\nElbow (inersia per K):");
        for (int k = 2; k <= 5; k++) {
            HasilSegmentasi h = new KMeans(k, 100).jalankan(data);
            System.out.printf("  K=%d  inersia=%.3f  silhouette=%.2f%n", k, KMeans.inersia(h.getDaftarCluster()), h.getSilhouette());
        }
        HasilSegmentasi hasil = admin.jalankanSegmentasi(transaksi, 2);

        // 5. Hasil Segmentasi & Analisis (Manajer Pemasaran)
        System.out.println("\n=== Hasil & Analisis ===");
        System.out.println("Login manajer: " + manajer.login("manajer", "manajer123"));
        manajer.lihatHasil(hasil);
        System.out.println("Contoh anggota Cluster 1:");
        hasil.anggotaCluster(1).stream().limit(3).forEach(d -> System.out.printf("  %s | Cluster %d | jarak %.2f%n",
                d.getPelanggan().getNama(), d.getCluster(), d.getJarakKeCentroid()));

        // 6. Strategi Pemasaran
        Cluster loyalCluster = hasil.getDaftarCluster().stream()
                .filter(c -> c.getNamaSegmen().equals("Pelanggan Loyal")).findFirst().orElse(hasil.getDaftarCluster().get(0));
        System.out.println("\nSimpan tanpa judul : " + manajer.simpanStrategi(loyalCluster, "", "Diskon volume"));
        System.out.println("Simpan lengkap     : " + manajer.simpanStrategi(loyalCluster,
                "Program Loyalitas Kontraktor", "Diskon volume & prioritas kirim"));

        manajer.logout();
        admin.logout();
    }
}
