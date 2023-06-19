package itmo.app.shared;

public enum AuthResult {
    LOGGEDIN,
    REGISTERED,
    REJECTED;

    public boolean authorized() {
        return (this.equals(AuthResult.LOGGEDIN) || this.equals(AuthResult.REGISTERED));
    }
}
