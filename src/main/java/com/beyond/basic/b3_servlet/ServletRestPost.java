package com.beyond.basic.b3_servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/servlet/post")
// 서블릿은 사용자의 req를 쉽게 처리하고, 사용자에게 res를 쉽게 조립해주는 기술
// 서블릿에서는 url매핑을 메서드 단위가 아닌 클래스 단위로 지정
public class ServletRestPost extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // url 인코딩방식으로 데이터 전송
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        System.out.println(name);
        System.out.println(email);

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        out.println("ok");
        out.flush();

    }
}
