import java.sql.*;
import java.util.*;

public class OnlineBookstore {

    // Database connection and statement objects
    static Connection conn = null;
    static Statement stmt = null;

    // Shopping cart instance
    static ShoppingCart shoppingCart = new ShoppingCart();

    public static void main(String args[]) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore?characterEncoding=utf8","root","");
            stmt = conn.createStatement();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                clearScreen();
                // Provide options for user/admin
                System.out.println("\nWelcome to Sneha's Online Bookstore\n");
                System.out.println("1. Admin");
                System.out.println("2. Customer");
                System.out.println("0. Quit");
                System.out.print("\nChoose your role or enter 0 to quit: ");
                int choice = scanner.nextInt();

                if (choice == 0) {
	            System.out.println("Goodbye...");
                    break;
                } 
		else if (choice == 1) {
		     // Admin functionalities
                    adminFunctionality(scanner);
                } 
		else if (choice == 2) {
                    // User functionalities
                    userFunctionality(scanner);
                } 
		else {
                    System.out.println("Invalid choice");
                }
            }
        } 
	catch (SQLException se) {	
	    System.out.println("Error: " + se);
        } 
	catch (Exception e) {
            System.out.println("Error: " + e);
        } 
	finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } 
	    catch (SQLException se) {
                System.out.println("Error: " + se);
            }
        }
    }

    // User functionalities
    static void userFunctionality(Scanner scanner) {
        try {
            while (true) {
                clearScreen();
                System.out.println("User Menu:");
                System.out.println("1. Search for books");
                System.out.println("2. View cart items");
                System.out.println("3. View purchase amount");
                System.out.println("0. Go back");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 0:
                        return;
                    case 1:
                        searchBooksMenu(scanner);
                        break;
                    case 2:
                        viewCartItems();
                        break;
                    case 3:
                        viewPurchaseAmount();
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        } 
	catch (SQLException se) {
            System.out.println("Error: " + se);
        } 
	catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    static void searchBooksMenu(Scanner scanner) throws SQLException {
        clearScreen();
        System.out.println("Search for Books:");
        System.out.print("Enter search keyword: ");
        String keyword = scanner.next();
        List<Book> books = searchBooks(keyword);
        System.out.println("Search Results:");
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.print("Enter book ID to add to cart or '0' to exit: ");
        int bookId = scanner.nextInt();
        if (bookId != 0) {
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            addToCart(bookId, quantity);
        }
    }

    static void viewCartItems() {
        clearScreen();
        System.out.println("Cart Items:");
        List<Book> cartItems = shoppingCart.getItems();
        for (Book item : cartItems) {
            System.out.println("ID: " + item.id + ", Title: " + item.title + ", Author: " + item.author + ", Price: $" + item.price + ", Quantity: " + item.quantity);
        }
        System.out.println();
        System.out.print("Press Enter to continue...");
        new Scanner(System.in).nextLine(); // Wait for Enter key press
    }

    static void viewPurchaseAmount() {
        clearScreen();
        System.out.println("Purchase Amount:");
        List<Book> cartItems = shoppingCart.getItems();
        double total = 0;
        for (Book item : cartItems) {
            System.out.println("Title: " + item.title + ", Price: $" + item.price + ", Quantity: " + item.quantity + ", Total Price: $" + (item.price * item.quantity));
            total += (item.price * item.quantity);
        }
        System.out.println("Total Purchase Amount: $" + total);
	System.out.println("Purchased Successfully! Visit Again :)");
        System.out.println();
        System.out.print("Press Enter to continue...");
        new Scanner(System.in).nextLine(); // Wait for Enter key press
    }

    // Admin functionalities
    static void adminFunctionality(Scanner scanner) {
        try {
            while (true) {
                clearScreen();
                System.out.println("Admin Menu:");
                System.out.println("1. Update book prices");
                System.out.println("2. View books");
                System.out.println("0. Go back");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 0:
                        return;
                    case 1:
                        updateBookPrices(scanner);
                        break;
                    case 2:
                        viewBooks();
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        } 
	catch (SQLException se) {
            System.out.println("Error: " + se);
        } 
	catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    static void updateBookPrices(Scanner scanner) throws SQLException {
        clearScreen();
        System.out.println("Update Book Prices:");
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();

        String sql = "SELECT title, price FROM books WHERE id = " + bookId;
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            String title = rs.getString("title");
            double currentPrice = rs.getDouble("price");
            System.out.println("Book Title: " + title);
            System.out.println("Current Price: $" + currentPrice);

            System.out.print("Do you want to update the price? (Y/N): ");
            char choice = scanner.next().charAt(0);
            if (choice == 'Y' || choice == 'y') {
                System.out.print("Enter new price: ");
                double newPrice = scanner.nextDouble();
                sql = "UPDATE books SET price = " + newPrice + " WHERE id = " + bookId;
                int rowsAffected = stmt.executeUpdate(sql);
                if (rowsAffected > 0) {
                    System.out.println("Price updated successfully");
                } 
	        else {
                    System.out.println("Book ID not found");
                }
            }
        } 
	else {
            System.out.println("Book ID not found");
        }
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine(); // Consume newline
        scanner.nextLine(); // Wait for Enter key press
    }

    static void viewBooks() throws SQLException {
        clearScreen();
        System.out.println("Books List:");
        String sql = "SELECT id, title, price FROM books";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            double price = rs.getDouble("price");
            System.out.println("ID: " + id + ", Title: " + title + ", Price: $" + price);
        }
        System.out.println();
        System.out.print("Press Enter to continue...");
        new Scanner(System.in).nextLine(); // Wait for Enter key press
    }

    // Method to search for books
    static List<Book> searchBooks(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE '%" + keyword + "%'";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            double price = rs.getDouble("price");
            books.add(new Book(id, title, author, price, 0)); // Quantity set to 0 initially
        }
        return books;
    }

    // Method to add book to cart with specified quantity
    static void addToCart(int bookId, int quantity) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = " + bookId;
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            String title = rs.getString("title");
            String author = rs.getString("author");
            double price = rs.getDouble("price");
            shoppingCart.addToCart(new Book(bookId, title, author, price, quantity));
            System.out.println("Book added to cart successfully!");
        } else {
            System.out.println("Book not found!");
        }
        System.out.println();
        System.out.print("Press Enter to continue...");
        new Scanner(System.in).nextLine(); // Wait for Enter key press
    }

    // Book class
    static class Book {
        int id;
        String title;
        String author;
        double price;
        int quantity;

        public Book(int id, String title, String author, double price, int quantity) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.price = price;
            this.quantity = quantity;
        }

        public String toString() {
            return "Book{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    // ShoppingCart class
    static class ShoppingCart {
        private List<Book> items;

        public ShoppingCart() {
            this.items = new ArrayList<>();
        }

        public void addToCart(Book book) {
            items.add(book);
        }

        public List<Book> getItems() {
            return new ArrayList<>(items);
        }

        public double calculateTotal() {
            double total = 0;
            for (Book item : items) {
                total += (item.price * item.quantity);
            }
            return total;
        }

        public void clearCart() {
            items.clear();
        }
    }

    public static void clearScreen() {
        System.out.print("\n");
        System.out.flush();
    }
}
