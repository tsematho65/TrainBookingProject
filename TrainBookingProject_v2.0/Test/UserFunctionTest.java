package Test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

import DAO.*;
import DataModel.*;

public class UserFunctionTest {

  @Test
  public void getNormalMemberDiscount() {
      UserDAO userDAO = new UserDAO();
      User user = userDAO.login("admin", "admin");

      
      assertEquals("Normal Member", user.getMemberType());
      assertTrue( user.getDiscount()== 0.1);
  }

  @Test
  public void getPlatinumMemberDiscount() {
      UserDAO userDAO = new UserDAO();
      User user = userDAO.login("q", "q");     
      user.setMember(new PlatinumMember());
      assertTrue(0.2 == user.getDiscount());
      assertEquals("Platinum Member", user.getMemberType());
  }
  
  @Test
  public void changeUserInfo() {
      UserDAO userDAO = new UserDAO();
      User user = userDAO.login("b", "b");     
      user.setUsername("test");
      user.setPassword("test");
      user.setId("test");
      user.setRole("test");
      user.setPoints(100);
      user.setLastSignInDate(LocalDate.now());
      
      
      assertEquals("test", user.getUsername());
      assertEquals("test", user.getPassword());
      assertEquals("test", user.getId());
      assertEquals("test", user.getRole());
      assertEquals(100, user.getPoints());
      assertEquals(LocalDate.now(), user.getLastSignInDate());
      
        }
  
  
}