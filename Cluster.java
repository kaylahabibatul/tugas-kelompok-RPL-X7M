import java.util.ArrayList;
import java.util.List;

/** Satu segmen hasil K-Means beserta centroid dan anggotanya. */
public class Cluster {
    private final int idCluster;
    private String namaSegmen;
    private double[] centroid;
    private final List<DataRFM> anggota = new ArrayList<>();

    public Cluster(int idCluster, double[] centroid) {
        this.idCluster = idCluster;
        this.centroid = centroid.clone();
        this.namaSegmen = "Segmen " + idCluster;
    }

    public void kosongkanAnggota() { anggota.clear(); }
    public void tambahAnggota(DataRFM d) { anggota.add(d); }

    /** Centroid baru = rata-rata anggota. Mengembalikan true bila centroid berubah. */
    public boolean perbaruiCentroid() {
        if (anggota.isEmpty()) return false;
        double[] baru = new double[3];
        for (DataRFM d : anggota) for (int i = 0; i < 3; i++) baru[i] += d.getTernormalisasi()[i];
        boolean berubah = false;
        for (int i = 0; i < 3; i++) {
            baru[i] /= anggota.size();
            if (Math.abs(baru[i] - centroid[i]) > 1e-9) berubah = true;
        }
        centroid = baru;
        return berubah;
    }

    /** Pelanggan Loyal bila F dan M tinggi, R rendah; selain itu Pasif. */
    public void tentukanNama() {
        boolean loyal = centroid[1] > 0.5 && centroid[2] > 0.5 && centroid[0] < 0.5;
        namaSegmen = loyal ? "Pelanggan Loyal" : "Pelanggan Pasif";
    }

    public int getIdCluster() { return idCluster; }
    public String getNamaSegmen() { return namaSegmen; }
    public double[] getCentroid() { return centroid; }
    public List<DataRFM> getAnggota() { return anggota; }
}
