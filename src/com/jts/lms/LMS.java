package com.jts.lms;

import com.jts.lms.login.LoginService;

import java.sql.SQLException;

public class LMS {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("********Welcome to Great Sentinal Library*********");

        System.out.println("Please Login below");

        LoginService loginservice = new LoginService();
        loginservice.login();
    }
}
