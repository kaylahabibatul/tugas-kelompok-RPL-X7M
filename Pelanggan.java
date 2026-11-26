/** Data pelanggan toko bangunan (halaman 3: Data Pelanggan). */
public class Pelanggan {
    private final int idPelanggan;
    private String nama;
    private String alamat;
    private String noHp;
    private String jenis; // Perorangan atau Kontraktor

    public Pelanggan(int idPelanggan, String nama, String alamat, String noHp, String jenis) {
        this.idPelanggan = idPelanggan;
        this.nama = nama;
        this.alamat = alamat;
        this.noHp = noHp;
        this.jenis = jenis;
    }

    /** Validasi format: nama wajib, no. HP hanya angka (8-15 digit). */
    public boolean validasi() {
        return nama != null && !nama.isBlank() && noHp != null && noHp.matches("\\d{8,15}");
    }

    public int getIdPelanggan() { return idPelanggan; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
    public String getNoHp() { return noHp; }
    public String getJenis() { return jenis; }
    public void setNama(String nama) { this.nama = nama; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setNoHp(String noHp) { this.noHp = noHp; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    @Override public String toString() { return idPelanggan + " | " + nama + " | " + jenis; }
}
