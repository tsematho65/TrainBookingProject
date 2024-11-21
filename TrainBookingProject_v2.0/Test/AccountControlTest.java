package Test;

import junit.framework.TestCase;
import org.junit.Test;
import DAO.UserDAO;



public class AccountControlTest extends TestCase{
  
  @Test
  public void testRegisterSucessful() {
	  UserDAO userDAO = new UserDAO();
	  boolean result = userDAO.register("normal", "test", "test");
	  assertTrue(result);

  }
  
  @Test
  public void testRegisterFail() {
	  UserDAO userDAO = new UserDAO();
	  boolean result = userDAO.register("normal", "q", "q");
	  assertFalse(result);
  }
  
  
  @Test
  public void testLoginSucessful() {
	  UserDAO userDAO = new UserDAO();
	  String userName = userDAO.login("test", "test").getUsername();
	  assertEquals("test", userName);
  }
  
  @Test
  public void testLoginFail() {
	  UserDAO userDAO = new UserDAO();
	  
	  assertEquals( userDAO.login("test", "test1"), null);
  }
   
  
}
