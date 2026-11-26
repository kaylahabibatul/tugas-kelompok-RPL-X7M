import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Algoritma K-Means (langkah 2 dan 3 halaman Proses Segmentasi). */
public class KMeans {
    private final int k;
    private final int maxIterasi;
    private int iterasi;

    public KMeans(int k, int maxIterasi) {
        this.k = k;
        this.maxIterasi = maxIterasi;
    }

    public static double jarak(double[] a, double[] b) {
        double s = 0;
        for (int i = 0; i < a.length; i++) s += (a[i] - b[i]) * (a[i] - b[i]);
        return Math.sqrt(s);
    }

    public HasilSegmentasi jalankan(List<DataRFM> data) {
        if (k < 2) throw new IllegalArgumentException("K minimal 2");
        if (data.size() < k) throw new IllegalArgumentException("Data lebih sedikit dari K");
        Random rnd = new Random(42);
        List<Cluster> clusters = new ArrayList<>();
        List<Integer> dipakai = new ArrayList<>();
        while (clusters.size() < k) { // centroid awal: k titik acak berbeda
            int idx = rnd.nextInt(data.size());
            if (dipakai.contains(idx)) continue;
            dipakai.add(idx);
            clusters.add(new Cluster(clusters.size() + 1, data.get(idx).getTernormalisasi()));
        }
        iterasi = 0;
        boolean berubah = true;
        while (berubah && iterasi < maxIterasi) {
            iterasi++;
            for (Cluster c : clusters) c.kosongkanAnggota();
            for (DataRFM d : data) {
                int terdekat = 0;
                double min = Double.MAX_VALUE;
                for (int i = 0; i < k; i++) {
                    double j = jarak(d.getTernormalisasi(), clusters.get(i).getCentroid());
                    if (j < min) { min = j; terdekat = i; }
                }
                d.setCluster(terdekat + 1);
                d.setJarakKeCentroid(min);
                clusters.get(terdekat).tambahAnggota(d);
            }
            berubah = false;
            for (Cluster c : clusters) if (c.perbaruiCentroid()) berubah = true;
        }
        for (Cluster c : clusters) c.tentukanNama();
        return new HasilSegmentasi(clusters, hitungSilhouette(data), iterasi);
    }

    /** Rata-rata silhouette seluruh data; nilai mendekati 1 berarti pemisahan cluster baik. */
    public double hitungSilhouette(List<DataRFM> data) {
        double total = 0;
        for (DataRFM d : data) {
            double[] sumJarak = new double[k + 1];
            int[] jumlah = new int[k + 1];
            for (DataRFM o : data) {
                if (o == d) continue;
                sumJarak[o.getCluster()] += jarak(d.getTernormalisasi(), o.getTernormalisasi());
                jumlah[o.getCluster()]++;
            }
            double a = jumlah[d.getCluster()] == 0 ? 0 : sumJarak[d.getCluster()] / jumlah[d.getCluster()];
            double b = Double.MAX_VALUE;
            for (int c = 1; c <= k; c++) {
                if (c == d.getCluster() || jumlah[c] == 0) continue;
                b = Math.min(b, sumJarak[c] / jumlah[c]);
            }
            double m = Math.max(a, b);
            total += (b == Double.MAX_VALUE || m == 0) ? 0 : (b - a) / m;
        }
        return total / data.size();
    }

    /** Inersia (WCSS) untuk grafik Elbow pada pemilihan nilai K. */
    public static double inersia(List<Cluster> clusters) {
        double s = 0;
        for (Cluster c : clusters)
            for (DataRFM d : c.getAnggota()) { double j = jarak(d.getTernormalisasi(), c.getCentroid()); s += j * j; }
        return s;
    }

    public int getK() { return k; }
    public int getIterasi() { return iterasi; }
}
