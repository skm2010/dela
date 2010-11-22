package dela

class Account {

    public static byte ROLE_ANONYMOUS    = 0
    public static byte ROLE_USER         = 50
    public static byte ROLE_ADMIN        = 100

    public static byte STATE_BLOCKED     = 0
    public static byte STATE_CREATING    = 10
    public static byte STATE_ACTIVE      = 100

    static hasOne = [setup : Setup]
    static hasMany = [subjects : Subject]

    String login
    String email
    String password // Before account confirmation the field used for store uuid
    byte role
    byte state

    static constraints = {
        login(unique:true, minSize: 3)
        password(minSize: 5)
        email(email:true)
        setup(nullable:true)
        role(inList: [ROLE_ANONYMOUS, ROLE_USER, ROLE_ADMIN])
        state(inList: [STATE_BLOCKED, STATE_CREATING, STATE_ACTIVE])
    }

    static mapping = {
        subjects lazy: false
        setup lazy: false
    }

    def String toString() {
        return login
    }

    boolean equals(o) {
        if (o == null) return false;
        if (this.is(o)) return true;

        if (getClass() != o.class) return false;

        Account account = (Account) o;

        if (id != account.id) return false;

        return true;
    }

    int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    def isNotAnonymous() {
        this.role != Account.ROLE_ANONYMOUS
    }

    def isAdmin() {
        this.role == Account.ROLE_ADMIN
    }
}
