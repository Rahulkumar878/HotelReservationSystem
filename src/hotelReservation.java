import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
public class hotelReservation {
	private static final String url="jdbc:mysql://localhost:3306/hoteldatabase";
	private static final String username="root";
	private static final String password="Rahulkumar@123";
	
    public static void main(String arg[]) throws Exception
    {
    	try {
    		
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		Connection con=DriverManager.getConnection(url,username,password);
    		while(true)
    		{
    		  System.out.println();
    		  System.out.println("HOTEL MANAGEMENT SYSTEM");
    		  Scanner sc=new Scanner(System.in);
    		  System.out.println("1. Reserve a room");
    		  System.out.println("2. View Reservations");
    		  System.out.println("3. Get Room Number");
    		  System.out.println("4. Update Reservations");
    		  System.out.println("5. Delete Reservations");
    		  System.out.println("0. Exit");
    		  System.out.println("Choose an option: ");
    		  int choice=sc.nextInt();
    		  switch(choice)
    		  {
    		  case 1:
    			  reservationRoom(con,sc);
    			  break;
    		  case 2:
    			  viewReservations(con);
    			  break;
    		  case 3:
    			  getRoomNumber(con,sc);
    			  break;
    		  case 4:
    			  updateReservation(con,sc);
    			  break;
    		  case 5:
    			  deleteReservation(con,sc);
    			  break;
    		  case 0:
    			  exit();
    			  sc.close();
    			  return;
   		      default:
   		     	 System.out.println("Invalid Choice. Try agian.");
    				  
    		  }
    		  
    		  
    		}
    		
    	}catch(SQLException e)
    	{
    		System.out.println(e.getMessage());
    	}
    	catch(InterruptedException e)
    	{
    		throw new RuntimeException(e);
    	}
    }
    private static void reservationRoom(Connection connection,Scanner scanner) throws Exception
    {
    	
    	try {
    		
            System.out.println("Enter guest name: ");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
         
                // Reading data using readLine
                String guestName = reader.readLine();
           // String guestName = scanner.next();
             // scanner.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            System.out.print("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO  reservation (Guest_Name, Room_Number, Contact_Number) " +
                    "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

            try (Statement statement = connection.createStatement()) {
          
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation successful!");
                } else {
                    System.out.println("Reservation failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void viewReservations(Connection con)
    {
    	String query="select *from  reservation";
    	try(Statement st=con.createStatement();
    		ResultSet rs=st.executeQuery(query)) {
    		
    		System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date         |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            while(rs.next())
            {
            	 int reservationid=rs.getInt("Reservation_Id");
                 String guestname=rs.getString("Guest_Name");
                 int roomnumber=rs.getInt("Room_Number");
                 String contactnumber=rs.getString("Contact_Number");
                 String reservationdate=rs.getTimestamp("Reservation_Date").toString();
                 System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                		 reservationid, guestname, roomnumber, contactnumber, reservationdate);
            }
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
           
    	}catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
    }
    private static void getRoomNumber(Connection connection , Scanner scanner)
    {
    	try {
            System.out.print("Enter reservation ID: ");
            int reservationId = scanner.nextInt();
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();

            String sql = "SELECT Room_Number FROM reservation " +
                    "WHERE Reservation_Id = " + reservationId +
                    " AND Guest_Name = '" + guestName + "'";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationId +
                            " and Guest " + guestName + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    }
    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT Reservation_Id FROM reservation WHERE Reservation_Id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    private static void updateReservation(Connection connection ,Scanner scanner)
    {
    	 try {
             System.out.print("Enter reservation ID to update: ");
             int reservationId = scanner.nextInt();
             scanner.nextLine(); // Consume the newline character

             if (!reservationExists(connection, reservationId)) {
                 System.out.println("Reservation not found for the given ID.");
                 return;
             }

             System.out.print("Enter new guest name: ");
             String newGuestName = scanner.nextLine();
             System.out.print("Enter new room number: ");
             int newRoomNumber = scanner.nextInt();
             System.out.print("Enter new contact number: ");
             String newContactNumber = scanner.next();
             String sql = "UPDATE reservation SET Guest_Name = '" + newGuestName + "', " +
                     "Room_Number = " + newRoomNumber + ", " +
                     "Contact_Number = '" + newContactNumber + "' " +
                     "WHERE Reservation_Id = " + reservationId;

             try (Statement statement = connection.createStatement()) {
                 int affectedRows = statement.executeUpdate(sql);

                 if (affectedRows > 0) {
                     System.out.println("Reservation updated successfully!");
                 } else {
                     System.out.println("Reservation update failed.");
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

    }
    private static void deleteReservation(Connection connection, Scanner scanner)
    {
    	try {
    		System.out.println("Enter Reservation ID to Delete");
    		int reservationid=scanner.nextInt();
    		if(!reservationExists(connection,reservationid)) {
    			 System.out.println("Reservation not found for the given ID.");
                 return;
    		}
    		String sql="delete from reservation where Reservation_Id="+reservationid;
    		try(Statement statement=connection.createStatement())
    		{
    			int affectedrow=statement.executeUpdate(sql);
    			if(affectedrow>0)
    			{
                    System.out.println("Reservation deleted successfully!");

    			} else {
                    System.out.println("Reservation deletion failed.");
                }
    		}
    		
    	}
    	catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void exit() throws InterruptedException
    {
    	 System.out.print("Exiting System");
         int i = 5;
         while(i!=0){
             System.out.print(".");
             Thread.sleep(1000);
             i--;
         }
         System.out.println();
         System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }
}
