package com.jts.lms.login;

import com.jts.lms.dao.DatabaseService;
import com.jts.lms.dao.LoginDao;
import com.jts.lms.service.BookService;
import com.jts.lms.service.StudentService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginService {
    Scanner sc = new Scanner(System.in);
    public void login() throws ClassNotFoundException, SQLException {


        System.out.println("Please Login with username and password");
        System.out.println("Username:");
        String userName = sc.nextLine();
        System.out.println("Password:");
        String password = sc.nextLine();

        try (Connection conn = DatabaseService.getConnection()) {
            //VALIDATE
            LoginDao logindao = new LoginDao();
            String userType = logindao.doLogin(conn, userName, password);

            if (userType == null){
                System.out.println("Invalid User!!");
                return;
            }
            System.out.println("U logged in as "+userType+" please select ur role:");
            if (userType.equals("admin")){
                //display admin menu
                displayAdminMenu(conn);

            } else {
                //display student menu
                displayStudentMenu(conn);
            }
        }
    }
    public void displayAdminMenu(Connection conn) throws SQLException {
        int choice;
        BookService bookservice = new BookService();
        StudentService studentService = new StudentService();

        do {
            System.out.println("****************************");
            System.out.println("1> Search a  book");
            System.out.println("2> add new book");
            System.out.println("3> update quantity of book");
            System.out.println("4> show all books");
            System.out.println("5> register student");
            System.out.println("6> show all registered student");
            System.out.println("7> exit ");
            System.out.println("****************************");

            System.out.println("Enter your choice:");
            choice = sc.nextInt();
            switch (choice){
                case 1 :
                    searchBook(conn);
                    break;
                case 2 :
                    bookservice.addBook(conn);
                    break;
                case 3 :
                    bookservice.updateBookQty(conn);
                    break;
                case 4 :
                    bookservice.getAllBooks(conn);
                    break;
                case 5 :
                    studentService.addStudent(conn);
                    break;
                case 6 :
                    studentService.getAllStudents(conn);
                    break;
                case 7 :
                    System.out.println("Thankyou for using LMS");
                    break;
                default:
                    System.out.println("Please select valid option");
            }

        } while (choice != 7);
    }
    public void displayStudentMenu(Connection conn) throws SQLException {
        int choice;
        BookService bookservice = new BookService();
        StudentService studentService = new StudentService();

        do {
            System.out.println("****************************");
            System.out.println("1> Search a  book");
            System.out.println("2> Check out book");
            System.out.println("3> Check in book");
            System.out.println("4> exit ");
            System.out.println("****************************");

            System.out.println("Enter your choice:");
            choice = sc.nextInt();
            switch (choice){
                case 1 :
                    searchBook(conn);
                    break;
                case 2 :
                    bookservice.checkOutBook(conn);
                    break;
                case 3 :
                    bookservice.checkInBook(conn);
                    break;
                case 4 :
                    System.out.println("Thankyou for using LMS");
                    break;
                default:
                    System.out.println("Please select valid option");
            }

        } while (choice != 4);
    }
    private void searchBook(Connection conn) throws SQLException {
        BookService bookservice = new BookService();
        System.out.println("1> search book by serial no.");
        System.out.println("2> search book by author name");
        System.out.println("Enter your choice");
        int choice = sc.nextInt();
        switch (choice){
            case 1:
                bookservice.searchBySrNo(conn);
                break;
            case 2:
                bookservice.searchByAuthorName(conn);
                break;

        }

    }


}
