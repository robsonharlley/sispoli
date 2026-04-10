package br.com.sispoli.model;

public class DbConfig {
    private String url;
    private String user;
    private String pass;
    private boolean encrypt;

    public DbConfig() {}
    
    public DbConfig(String url, String user, String pass, boolean encrypt) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.encrypt = encrypt;
    }

    // Getters & Setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }
    public boolean isEncrypt() { return encrypt; }
    public void setEncrypt(boolean encrypt) { this.encrypt = encrypt; }
    
    @Override public String toString() {
        return "DbConfig{url='" + url + "', user='" + user + "', pass='***', encrypt=" + encrypt + "}";
    }
}