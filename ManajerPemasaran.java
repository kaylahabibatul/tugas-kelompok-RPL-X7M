import java.util.ArrayList;
import java.util.List;

/** Manajer Pemasaran: melihat hasil segmentasi dan menyusun strategi (halaman 5 dan 6). */
public class ManajerPemasaran extends Pengguna {
    private final List<StrategiPemasaran> daftarStrategi = new ArrayList<>();

    public ManajerPemasaran(int id, String username, String password) {
        super(id, username, password, "Manajer Pemasaran");
    }

    /** Halaman 5: lihat kartu profil rata-rata RFM tiap segmen. */
    public void lihatHasil(HasilSegmentasi hasil) {
        System.out.println(hasil.ringkasan());
    }

    /** Halaman 6: tombol "Simpan". Mengembalikan false bila judul atau rencana kosong. */
    public boolean simpanStrategi(Cluster segmen, String judul, String rencana) {
        StrategiPemasaran s = new StrategiPemasaran(daftarStrategi.size() + 1, segmen, judul, rencana);
        if (!s.simpan()) return false;
        daftarStrategi.add(s);
        return true;
    }

    public List<StrategiPemasaran> getDaftarStrategi() { return daftarStrategi; }
}
