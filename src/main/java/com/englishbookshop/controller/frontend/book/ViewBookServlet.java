package com.englishbookshop.controller.frontend.book;

import com.englishbookshop.service.BookServices;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/view-book")
public class ViewBookServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

    public ViewBookServlet() {
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		BookServices bookServices = new BookServices(request, response);
		bookServices.viewBookDetail();
	}

}