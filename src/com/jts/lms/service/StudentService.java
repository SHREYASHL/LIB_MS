package com.jts.lms.service;

import com.jts.lms.dao.BookDao;
import com.jts.lms.dao.StudentDao;
import com.jts.lms.dto.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class StudentService {
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

    public void addStudent(Connection conn) throws SQLException {
        System.out.println("Enter the Student name:");
        String studentName = sc.nextLine();
        System.out.println("Enter the reg no");
        String regNo = sc.nextLine();

        StudentDao dao = new StudentDao();
        boolean isStdExist = dao.getStudentByRegno(conn, regNo);

        if(isStdExist) {
            System.out.println("Student details exist into our sentinal.");
            return;
        }
        dao.saveStudent(conn, studentName, regNo);
    }
    public void getAllStudents(Connection conn) throws SQLException {
        StudentDao dao = new StudentDao();
        dao.getAllStudents(conn);




    }

}
