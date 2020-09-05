package entities;
// Generated Jul 17, 2020, 10:54:41 AM by Hibernate Tools 4.3.1




public class Taikhoan  implements java.io.Serializable {


     private String username;
     private String password;
     private boolean tructuyen;

    public Taikhoan() {
    }

    public Taikhoan(String username, String password, boolean tructuyen) {
       this.username = username;
       this.password = password;
       this.tructuyen = tructuyen;
    }
   
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isTructuyen() {
        return this.tructuyen;
    }
    
    public void setTructuyen(boolean tructuyen) {
        this.tructuyen = tructuyen;
    }




}


