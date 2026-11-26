/** Atribut RFM satu pelanggan, mentah dan setelah normalisasi Min-Max. */
public class DataRFM {
    private final Pelanggan pelanggan;
    private final double recency;   // hari sejak transaksi terakhir
    private final double frequency; // jumlah transaksi
    private final double monetary;  // total belanja
    private double[] ternormalisasi = new double[3];
    private int cluster = -1;
    private double jarakKeCentroid;

    public DataRFM(Pelanggan pelanggan, double recency, double frequency, double monetary) {
        this.pelanggan = pelanggan;
        this.recency = recency;
        this.frequency = frequency;
        this.monetary = monetary;
    }

    public Pelanggan getPelanggan() { return pelanggan; }
    public double getRecency() { return recency; }
    public double getFrequency() { return frequency; }
    public double getMonetary() { return monetary; }
    public double[] getTernormalisasi() { return ternormalisasi; }
    public void setTernormalisasi(double[] v) { this.ternormalisasi = v; }
    public int getCluster() { return cluster; }
    public void setCluster(int cluster) { this.cluster = cluster; }
    public double getJarakKeCentroid() { return jarakKeCentroid; }
    public void setJarakKeCentroid(double j) { this.jarakKeCentroid = j; }
}
