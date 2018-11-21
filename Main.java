import java.sql.*;
import java.io.*;

import com.mysql.cj.jdbc.Driver;

public class Main {
    public static void main(String args[]) {
        Connection conn = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver"); //want to use the driver from jdbc driver this and statement below are equivalent
            // DriverManager.registerDriver(new Driver());
            //default SSL will be true, Company is the schema name that we are working with
            String url = "jdbc:mysql://localhost:3306/Company?serverTimezone=UTC&useSSL=TRUE"; //this is the connection we have with the database, need timezone in java
            String user, pass;
            user = readEntry("UserId: "); //get the user ID from user
            pass = readEntry("Password: "); //get the PassWord from user
            conn = DriverManager.getConnection(url, user, pass);//where, username, and password these are the parts that the driver manager needs to know about

            boolean done = false;
            do {
                printMenu();
                System.out.print("Type in your option: ");
                System.out.flush();
                String ch = readLine();
                System.out.println();
                switch (ch.charAt(0)) {
                    case '1':
                        citiesMoreThanOneCustomer(conn);
                        break;
                    case '2':
                        idList(conn);
                        break;
                    case '3':
                        filoMix(conn);
                        break;
                    case '4':
                        done = true;
                        break;
                    default:
                        System.out.println(" Not a valid option ");
                }
            }
            while (!done);

        } catch (ClassNotFoundException e) {
            System.out.println("Could not load the driver");
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }


    static String readEntry(String prompt) {
        try {
            StringBuffer buffer = new StringBuffer();
            System.out.print(prompt);
            System.out.flush();
            int c = System.in.read();
            while (c != '\n' && c != -1) {
                buffer.append((char) c);
                c = System.in.read();
            }
            return buffer.toString().trim();
        } catch (IOException e) {
            return "";
        }
    }

    static void printMenu() {
        System.out.println("***********************************************************");
        System.out.println("             CS331- Alex van Zuiden-Rylander               ");
        System.out.println("                      Homework 3                           ");
        System.out.println("***********************************************************");
        System.out.println("     1. Cities for more than one customer resides\n" +
                "             2. ID List for quantity> 5 \n" +
                "      3. Customers names for ‘Filo Mix’ order\n" +
                "                       4. Quit ");


    }

    private static String readLine() {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr, 1);
        String line = "";
        try {
            line = br.readLine();
        } catch (IOException e) {
            System.out.println("Error in SimpleIO.readLine: " + "IOException was thrown");
            System.exit(1);
        }
        return line;
    }

    private static void filoMix(Connection conn) throws SQLException, IOException {
        String query = "select FirstName, LastName\n" +
                "from Customer, Product, Orders, OrderItem\n" +
                "where Customer.Id = Orders.CustomerID\n" +
                "and Orders.Id = OrderItem.OrderID\n" +
                "and OrderItem.ProductID = Product.Id\n" +
                "and ProductName = 'Filo Mix';";
        PreparedStatement p = conn.prepareStatement(query);//call on this connection that we have the connection to and we will run this query on the DBMS
        p.clearParameters();//make sure everything is clear
        ResultSet r = p.executeQuery(); // runs the query on a result set. this is because our values will normally return one, we want a set
        int count = 0;
        System.out.println("Customers Names who Ordered a product called 'Filo Mix'");
        while (r.next() && count < 5) { //as long as there are more, continue reading
            String firstName = r.getString(1);
            String lastName = r.getString(2);
            System.out.println(firstName + " " + lastName);
            count++;
        }
        System.out.println("Press Enter to continue...");
        System.in.read();
        System.out.println();
        p.close();
    }

    private static void idList(Connection conn) throws SQLException, IOException {
        String query = "select ProductID \n" +
                "from OrderItem\n" +
                "where ProductID = ProductID\n" +
                "group by ProductID\n" +
                "having 5 < (select sum(ProductID) from OrderItem);"; 
        PreparedStatement p = conn.prepareStatement(query);//call on this connection that we have the connection to and we will run this query on the DBMS
        p.clearParameters();//make sure everything is clear
        ResultSet r = p.executeQuery(); // runs the query on a result set. this is because our values will normally return one, we want a set
        int count = 0;
        while (r.next() && count < 5) { //as long as there are more, continue reading
            int PID = r.getInt(1); //get the last name to read from database we use getVariable(); varChar = string in java, first index of output
            System.out.println(PID);
            count++;
        }
        System.out.println("Press Enter to continue...");
        System.in.read();
        System.out.println();
        p.close();
    }

    private static void citiesMoreThanOneCustomer(Connection conn) throws SQLException, IOException {
        String query = "select City from Customer group by City having count(*) >1"; //similar to MySQL statements, the question mark is user input
        PreparedStatement p = conn.prepareStatement(query);//call on this connection that we have the connection to and we will run this query on the DBMS
        p.clearParameters();//make sure everything is clear
        ResultSet r = p.executeQuery(); // runs the query on a result set. this is because our values will normally return one, we want a set
        int count = 0;
        while (r.next() && count < 5) { //as long as there are more, continue reading
            String cityName = r.getString(1); //get the last name to read from database we use getVariable(); varChar = string in java, first index of output
            System.out.println(cityName);
            count++;
        }
        System.out.println("Press Enter to continue...");
        System.in.read();
        System.out.println();
        p.close();
    }


}
