/** Kelas induk untuk semua pengguna sistem (Admin dan Manajer Pemasaran). */
public abstract class Pengguna {
    private final int idPengguna;
    private final String username;
    private String password;
    private final String role;
    private boolean sedangLogin;

    protected Pengguna(int idPengguna, String username, String password, String role) {
        this.idPengguna = idPengguna;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /** Halaman 1: Login. Mengembalikan false bila username atau password salah. */
    public boolean login(String username, String password) {
        sedangLogin = this.username.equals(username) && this.password.equals(password);
        return sedangLogin;
    }

    public void logout() { sedangLogin = false; }

    public boolean isSedangLogin() { return sedangLogin; }
    public int getIdPengguna() { return idPengguna; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public void setPassword(String passwordBaru) { this.password = passwordBaru; }
}
