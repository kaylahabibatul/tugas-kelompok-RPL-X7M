import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/** Langkah 1 halaman Proses Segmentasi: hitung RFM lalu normalisasi Min-Max. */
public class Preprocessing {
    private LocalDate tanggalAcuan = LocalDate.now();

    public void setTanggalAcuan(LocalDate tanggalAcuan) { this.tanggalAcuan = tanggalAcuan; }

    /** Hanya pelanggan yang punya transaksi yang dianggap data bersih. */
    public List<DataRFM> hitungRFM(List<Pelanggan> pelanggan, List<Transaksi> transaksi) {
        List<DataRFM> hasil = new ArrayList<>();
        for (Pelanggan p : pelanggan) {
            LocalDate terakhir = null;
            int frekuensi = 0;
            double total = 0;
            for (Transaksi t : transaksi) {
                if (t.getIdPelanggan() != p.getIdPelanggan()) continue;
                frekuensi++;
                total += t.getNilai();
                if (terakhir == null || t.getTanggal().isAfter(terakhir)) terakhir = t.getTanggal();
            }
            if (frekuensi == 0) continue;
            double recency = ChronoUnit.DAYS.between(terakhir, tanggalAcuan);
            hasil.add(new DataRFM(p, recency, frekuensi, total));
        }
        return hasil;
    }

    /** x' = (x - min) / (max - min) untuk tiap atribut R, F, M. */
    public void normalisasiMinMax(List<DataRFM> data) {
        double[] min = {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
        double[] max = {-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE};
        for (DataRFM d : data) {
            double[] v = {d.getRecency(), d.getFrequency(), d.getMonetary()};
            for (int i = 0; i < 3; i++) { min[i] = Math.min(min[i], v[i]); max[i] = Math.max(max[i], v[i]); }
        }
        for (DataRFM d : data) {
            double[] v = {d.getRecency(), d.getFrequency(), d.getMonetary()};
            double[] n = new double[3];
            for (int i = 0; i < 3; i++) n[i] = max[i] == min[i] ? 0 : (v[i] - min[i]) / (max[i] - min[i]);
            d.setTernormalisasi(n);
        }
    }
}
