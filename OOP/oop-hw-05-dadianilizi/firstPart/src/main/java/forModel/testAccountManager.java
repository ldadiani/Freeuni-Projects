package forModel;
import junit.framework.TestCase;
import org.junit.Assert;

public class testAccountManager  extends  TestCase{

    AccountManager manager;
    protected  void setUp() {
        manager=new AccountManager();
    }
    public void test1(){
        assertEquals(true, manager.checkAccount("Patrick"));
        assertEquals(true, manager.checkAccount("Molly"));
        assertEquals(true, manager.checkPassword("Patrick","1234"));
        assertEquals(true, manager.checkPassword("Molly","FloPup"));
    }

    public  void test2(){
        assertEquals(false,manager.checkAccount("Name"));
        assertEquals(false,manager.checkPassword("Name","abc"));
    }

    public void test3(){
        manager.createNewAccount("Name","Pass");
        assertEquals(true, manager.checkAccount("Name"));
        assertEquals(true, manager.checkPassword("Name","Pass"));

    }
}
