package com.jts.lms.service;

import com.jts.lms.dao.BookDao;
import com.jts.lms.dao.StudentDao;
import com.jts.lms.dto.Book;
import com.jts.lms.dto.BookingDetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static com.jts.lms.dao.DatabaseService.conn;

public class BookService {
    Scanner sc = new Scanner(System.in);
    public void searchBySrNo(Connection conn) throws SQLException {
        System.out.println("Enter Serial No. of Book: ");
        int srNo = sc.nextInt();

        BookDao dao = new BookDao();
        Book book = dao.getBookBySno(conn, srNo);

        if (book != null){
            System.out.println("***Book Details***");
            System.out.println("SrNo: "+book.getSrNo()+" Book Name: "+book.getBookName()+" AuthorName: "+book.getAuthorName());
        }
        else{
            System.out.println("No Book for SrNo: "+srNo+" found");
        }


    }
    public void searchByAuthorName(Connection conn) throws SQLException {
        System.out.println("Enter Author Name of Book: ");
        String AuthorName = sc.nextLine();

        BookDao dao = new BookDao();
        Book book = dao.getBookByAuthorName(conn, AuthorName);

        if (book != null){
            System.out.println("***Book Details***");
            System.out.println("SrNo:"+book.getSrNo()+"Book Name"+book.getBookName()+"AuthorName"+book.getAuthorName());
        }
        else{
            System.out.println("No Book for AuthorName: "+AuthorName+" found");
        }
    }

    public void addBook(Connection conn) throws SQLException {
        System.out.println("Enter the SrNo of the Book:");
        int srNo = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the name of the Book:");
        String bookName = sc.nextLine();
        System.out.println("Enter the authorname of the Book:");
        String authorName = sc.nextLine();
        System.out.println("Enter qty of the Book:");
        int qty = sc.nextInt();
        BookDao dao = new BookDao();
        Book book = dao.getBookBySnoOrBookName(conn, authorName, srNo);

        if(book != null){
            System.out.println("Books details exist into our sentinal.\nPlease save with other book.");
            return;
        }
        Book input = new Book();
        input.setAuthorName(authorName);
        input.setBookName(bookName);
        input.setBookQty(qty);
        input.setSrNo(srNo);

        dao.saveBook(conn, input);
    }
    public void getAllBooks(Connection conn) throws SQLException {
        BookDao dao = new BookDao();
        List<Book> books = dao.getAllBooks(conn);
        System.out.println("+-------------------------------------------------+");
        System.out.println("|  Book Sr No  |  Name   |  Auth Name |  Book Qty |");
        System.out.println("+--------------------------------------------------+");

        for (Book book : books){

            System.out.printf("| %-14s | %-9s | %-12s | %-10s |\n", book.getSrNo(),book.getBookName(),book.getAuthorName(),book.getBookQty());
//            System.out.println(book.getSrNo()+"     "+book.getBookName()+"     "+book.getAuthorName());
        }


    }
    public void updateBookQty(Connection conn) throws SQLException {
        System.out.println("Enter the SrNo of the Book:");
        int srNo = sc.nextInt();
        sc.nextLine();

        BookDao dao = new BookDao();
        Book book = dao.getBookBySno(conn, srNo);
        if(book == null){
            System.out.println("Book not available");
            return;
        }
        System.out.println("Enter the number of books to be added");
        int qty = sc.nextInt();
        Book input = new Book();
        input.setBookQty(book.getBookQty()+qty);
        input.setSrNo(srNo);

        dao.updateBookQty(conn, input);
    }
    public void checkOutBook(Connection conn) throws SQLException {
        StudentDao dao = new StudentDao();
        System.out.println("Enter Regno");
        String regno = sc.nextLine();
        boolean isExist = dao.getStudentByRegno(conn, regno);

        if (!isExist){
            System.out.println("Student not registered");
            System.out.println("Get registered first");
            return;
        }
        getAllBooks(conn);
        System.out.println("Enter the serial number of the book to be checked out");
        int sNo = sc.nextInt();

        BookDao bookDao = new BookDao();
        Book book = bookDao.getBookBySno(conn, sNo);

        if(book == null){
            System.out.println("Book not available  in the sentinal");
            return;
        }
        book.setBookQty(book.getBookQty() -1);
        int id = dao.getStudentByRegno_(conn, regno);
        dao.saveBookingDetails(conn, id, book.getId(), 1);
        bookDao.updateBookQty(conn, book);
    }
    public void checkInBook(Connection conn) throws SQLException{
        StudentDao dao = new StudentDao();
        System.out.println("Enter Regno");
        String regno = sc.nextLine();
        boolean isExist = dao.getStudentByRegno(conn, regno);

        if (!isExist){
            System.out.println("Student not registered");
            System.out.println("Get registered first");
            return;
        }
        int id = dao.getStudentByRegno_(conn, regno);
        List<BookingDetails> bookingDetails =dao.getBookDetailsId(conn, id);
        bookingDetails.stream().forEach(b -> System.out.println(b.srNo + "\t\t\t" + b.bookName + "\t\t\t" + b.authorName));
        System.out.println("Enter the serial number of the book to be checked in:");
        int sNo = sc.nextInt();
        BookingDetails filterDetails = bookingDetails.stream().filter(b -> b.getSrNo() == sNo).findAny().orElse(null);
        BookDao bookDao = new BookDao();
        Book book = bookDao.getBookBySno(conn, sNo);
        book.setBookQty(book.getBookQty() +1);
        bookDao.updateBookQty(conn, book);
        dao.deleteBookingDetails(conn, filterDetails.getId());
    }



}
