import java.time.LocalDate;

/** Transaksi pembelian pelanggan, sumber perhitungan Recency, Frequency, Monetary. */
public class Transaksi {
    private final int idTransaksi;
    private final int idPelanggan;
    private final LocalDate tanggal;
    private final double nilai; // total belanja dalam rupiah

    public Transaksi(int idTransaksi, int idPelanggan, LocalDate tanggal, double nilai) {
        this.idTransaksi = idTransaksi;
        this.idPelanggan = idPelanggan;
        this.tanggal = tanggal;
        this.nilai = nilai;
    }

    public int getIdTransaksi() { return idTransaksi; }
    public int getIdPelanggan() { return idPelanggan; }
    public LocalDate getTanggal() { return tanggal; }
    public double getNilai() { return nilai; }
}
