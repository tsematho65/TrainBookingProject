package Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import DAO.CustomerServiceDAO;
import DataModel.CsQuestion;
import Main.TrainTicketSystem;


public class CSTest {
	
	//test CS
	@Test
	public void testLeaveCS() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : Goodbye!", ticketSystem.getAnswer("exit"));
		
	}
	
	@Test
	public void testNoInput() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : Please type your question.", ticketSystem.getAnswer(""));
		
	}
	
	@Test
	public void testNoMatchKeyWord() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : Your question seems to be new. Please ask via email for further assistance.", ticketSystem.getAnswer("Are you free tonight?"));
		
	}
	
	@Test
	public void testAskBookTicket() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : To book a ticket, you should press 1 in the main menu after login, then follow the instructions to do booking.", ticketSystem.getAnswer("book ticket"));
		
	}
	
	 @Test
	public void testAskEditTicket() throws Exception{
			TrainTicketSystem ticketSystem = new TrainTicketSystem();
			CustomerServiceDAO csDAO = new CustomerServiceDAO();
			assertEquals("CSer : To edit a ticket, you should press 2 in the main menu after login, then select a order to edit.", ticketSystem.getAnswer("edit ticket"));
			
	}
    
    @Test
	public void testAskCheckTicket() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : To check a ticket, you should press 2 in the main menu after login, then you can see all ticket you booked and select the view the details.", ticketSystem.getAnswer("check ticket"));
		
	}
    
    @Test
	public void testAskCancelTicket() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : To cancel a ticket, you should press 2 in the main menu after login, then follow the instructions to cancel.", ticketSystem.getAnswer("cancel ticket"));
		
	}
	
	@Test
	public void testAskContact() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : How to find us : email : 123@123.com, phone : 1234567890", ticketSystem.getAnswer("cs contact method ?"));
		
	}
	
	@Test
	public void testSeekHelp() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : If you have any problem, please press 3 to contact customer service.", ticketSystem.getAnswer("help! i am lost!"));
		
	}
	
	@Test
	public void testWithUpperCase() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : If you have any problem, please press 3 to contact customer service.", ticketSystem.getAnswer("i need HELP!"));
		
	}
	
	@Test
	public void testWithMostFrequentKeyWord() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : To book a ticket, you should press 1 in the main menu after login, then follow the instructions to do booking.", ticketSystem.getAnswer("Hi, I want to book a ticket and check the ticket status. Can you teach me how to book tickets first?"));
	}
	
	@Test
	public void testWithNoSpace() throws Exception{
		TrainTicketSystem ticketSystem = new TrainTicketSystem();
		CustomerServiceDAO csDAO = new CustomerServiceDAO();
		assertEquals("CSer : Your question seems to be new. Please ask via email for further assistance.", ticketSystem.getAnswer("Hi, I want to bookticket"));
	}
	
	//test add QA
	
	
	
	
}
