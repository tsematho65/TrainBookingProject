package Test;

import junit.framework.TestCase;
import org.junit.Test;
import DAO.UserDAO;
import Main.TrainTicketSystem;



public class HandleCuponTest extends TestCase{
  
  @Test
  public void testGenerateCoupon() {
	  TrainTicketSystem ticketSystem = new TrainTicketSystem();
	  UserDAO userDAO = new UserDAO();
	  
		for (int i = 0; i < 100; i++) {
			ticketSystem.generateRandomCoupon(userDAO.getUserByName("q"));
				
		}
	  
	  assertTrue(userDAO.getUserByName("q").getCouponList().size()-1 >0);
	  
  }	
  
  @Test
	public void testUseCoupon() {
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		UserDAO userDAO = new UserDAO();
		
		int i = 0;
		while(i<100) {
			ticketSystem.generateRandomCoupon(userDAO.getUserByName("q"));
			i++;
		}
		
		double discount = userDAO.useCoupon(100,userDAO.getUserByName("q"));
		assertTrue(discount > 0);
		
	}
   
  
}
