import java.util.ArrayList;
import java.util.List;

/** Keluaran K-Means yang ditampilkan di Dashboard dan halaman Hasil & Analisis. */
public class HasilSegmentasi {
    private final List<Cluster> daftarCluster;
    private final double silhouette;
    private final int jumlahIterasi;

    public HasilSegmentasi(List<Cluster> daftarCluster, double silhouette, int jumlahIterasi) {
        this.daftarCluster = daftarCluster;
        this.silhouette = silhouette;
        this.jumlahIterasi = jumlahIterasi;
    }

    /** Tabel "Anggota per cluster" dengan filter cluster (0 = semua). */
    public List<DataRFM> anggotaCluster(int idCluster) {
        List<DataRFM> hasil = new ArrayList<>();
        for (Cluster c : daftarCluster)
            if (idCluster == 0 || c.getIdCluster() == idCluster) hasil.addAll(c.getAnggota());
        return hasil;
    }

    public int totalPelanggan() {
        int n = 0;
        for (Cluster c : daftarCluster) n += c.getAnggota().size();
        return n;
    }

    /** Kartu ringkasan segmen (nilai R, F, M adalah centroid ternormalisasi). */
    public String ringkasan() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Pelanggan: %d | K: %d | Silhouette: %.2f | Iterasi: %d%n",
                totalPelanggan(), daftarCluster.size(), silhouette, jumlahIterasi));
        for (Cluster c : daftarCluster) {
            double[] v = c.getCentroid();
            sb.append(String.format("Segmen %d: %s | %d anggota | R %.2f . F %.2f . M %.2f%n",
                    c.getIdCluster(), c.getNamaSegmen(), c.getAnggota().size(), v[0], v[1], v[2]));
        }
        return sb.toString();
    }

    public List<Cluster> getDaftarCluster() { return daftarCluster; }
    public double getSilhouette() { return silhouette; }
    public int getJumlahIterasi() { return jumlahIterasi; }
}
