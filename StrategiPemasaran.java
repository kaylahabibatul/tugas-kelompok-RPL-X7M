/** Strategi pemasaran untuk satu segmen (halaman 6: Strategi Pemasaran). */
public class StrategiPemasaran {
    private final int idStrategi;
    private final Cluster segmen;
    private String judul;
    private String rencana;
    private boolean tersimpan;

    public StrategiPemasaran(int idStrategi, Cluster segmen, String judul, String rencana) {
        this.idStrategi = idStrategi;
        this.segmen = segmen;
        this.judul = judul;
        this.rencana = rencana;
    }

    /** Pesan "Strategi berhasil disimpan" atau minta lengkapi data bila kosong. */
    public boolean simpan() {
        tersimpan = judul != null && !judul.isBlank() && rencana != null && !rencana.isBlank();
        return tersimpan;
    }

    public int getIdStrategi() { return idStrategi; }
    public Cluster getSegmen() { return segmen; }
    public String getJudul() { return judul; }
    public String getRencana() { return rencana; }
    public boolean isTersimpan() { return tersimpan; }
    public void setJudul(String judul) { this.judul = judul; }
    public void setRencana(String rencana) { this.rencana = rencana; }
}
