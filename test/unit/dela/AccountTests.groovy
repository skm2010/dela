package dela

import dela.utils.DomainFactory

class AccountTests extends AbstractDelaUnitTestCase {

    private DomainFactory domainFactory = new DomainFactory()

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateAccount() {
        mockDomain(Account)
        def account = domainFactory.createAccount()
        boolean result = account.validate()
        assertTrue(account.errors.toString(), result)
    }

    void testLoginConstraints() {
        mockDomain(Account)
        def account = domainFactory.createAccount()
        assert account.save()

        def anotherAccount = domainFactory.createAccount(login: account.login)
        assertFalse(anotherAccount.validate())
        assertEquals('unique', anotherAccount.errors['login'])

        anotherAccount.login = null
        assertFalse(anotherAccount.validate())
        assertEquals('nullable', anotherAccount.errors['login'])

        anotherAccount.login = '12'
        assertFalse(anotherAccount.validate())
        assertEquals('minSize', anotherAccount.errors['login'])

        anotherAccount.login = '123'
        assertTrue(anotherAccount.validate())
    }

    void testPasswordConstraints() {
        mockDomain(Account)
        def account = domainFactory.createAccount(password : '1234')
        assertFalse(account.validate())
        assertEquals('minSize', account.errors['password'])

        account.password = '12345'
        assertTrue(account.validate())
    }

    void testEmailConstraints() {
        mockDomain(Account)
        def account = domainFactory.createAccount(email : 'non-email')
        assertFalse(account.validate())
        assertEquals('email', account.errors['email'])

        account.email = 'theMail@mail.mu'
        assertTrue(account.validate())
    }

    void testRoleConstraints() {
        mockDomain(Account)
        byte wrongRole = 77
        def inList = [Account.ROLE_ANONYMOUS, Account.ROLE_USER, Account.ROLE_ADMIN]
        assert !inList.contains(wrongRole)
        def account = domainFactory.createAccount(role:wrongRole)
        assertFalse(account.validate())
        assertEquals('inList', account.errors['role'])

        account.role = inList[0]
        assertTrue(account.validate())
    }

    void testStateConstraints() {
        mockDomain(Account)
        byte wrongState = 77
        def inList = [Account.STATE_BLOCKED, Account.STATE_CREATING, Account.STATE_ACTIVE]
        assert !inList.contains(wrongState)
        def account = domainFactory.createAccount(state: wrongState)
        assertFalse(account.validate())
        assertEquals('inList', account.errors['state'])

        account.state = inList[0]
        assertTrue(account.validate())
    }

}
