package com.englishbookshop.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.englishbookshop.controller.frontend.shoppingcart.ShoppingCart;
import com.englishbookshop.controller.order.OrderDetailServlet;
import com.englishbookshop.dao.CategoryDAO;
import com.englishbookshop.dao.OrderDAO;
import com.englishbookshop.entity.BookOrder;
import com.englishbookshop.entity.Category;
import com.englishbookshop.entity.Customer;
import com.englishbookshop.entity.OrderDetail;
import com.englishbookshop.helper.JspPathHelper;
import com.englishbookshop.helper.ServletHelper;

public class OrderServices extends BaseServices {
	private OrderDAO orderDao;
	private CategoryDAO categoryDao;

	public OrderServices(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		orderDao = new OrderDAO();
		categoryDao = new CategoryDAO();
	}

	public void listAll(String message) throws ServletException, IOException {
		List<BookOrder> listOrders = orderDao.listAll();

		if (message != null) {
			request.setAttribute(ServletHelper.MESSAGE, message);
		}

		request.setAttribute("LIST_ORDERS", listOrders);
		RequestDispatcher rd = request.getRequestDispatcher(JspPathHelper.ORDER_LIST);
		rd.forward(request, response);
	}

	public void showOrderDetail() throws ServletException, IOException {
		int orderId = Integer.parseInt(request.getParameter("id"));

		BookOrder order = orderDao.get(orderId);

		request.setAttribute("ORDER", order);
		RequestDispatcher rd = request.getRequestDispatcher(JspPathHelper.ORDER_DETAIL);
		rd.forward(request, response);

	}

	public void deleteOrder() {

	}

	public void showCheckout() throws ServletException, IOException {
		HttpSession session = request.getSession();

		ShoppingCart spCart = (ShoppingCart) session.getAttribute(ServletHelper.SESSION_CART);
		Customer customer = (Customer) session.getAttribute(ServletHelper.SESSION_LOGGED_CUSTOMER);
		
		if (customer == null) {
			request.setAttribute("MESSAGE_LOGIN", "");
			request.getRequestDispatcher(JspPathHelper.ORDER_CHECKOUT_MESSAGE).forward(request, response);
		} else if (spCart == null || spCart.getTotalItems() == 0) {
			request.setAttribute("MESSAGE_CART", "");
			request.getRequestDispatcher(JspPathHelper.ORDER_CHECKOUT_MESSAGE).forward(request, response);
		} else {

			RequestDispatcher rd = request.getRequestDispatcher(JspPathHelper.ORDER_CHECKOUT);
			rd.forward(request, response);
		}
	}

	public void placeOrder() throws ServletException, IOException {
		String recipientName = request.getParameter("recipient-name");
		String recipientPhone = request.getParameter("recipient-phone");
		String streetAddress = request.getParameter("street-address");
		String city = request.getParameter("city");
		String zipCode = request.getParameter("zip-code");
		String country = request.getParameter("country");
		String shippingAddress = streetAddress + ", " + city + ", " + zipCode + ", " + country;

		HttpSession session = request.getSession();
		Customer loggedCustomer = (Customer) session.getAttribute(ServletHelper.SESSION_LOGGED_CUSTOMER);
		ShoppingCart spCart = (ShoppingCart) session.getAttribute(ServletHelper.SESSION_CART);

		BookOrder order = new BookOrder();
		order.setCustomer(loggedCustomer);
		order.setRecipientName(recipientName);
		order.setRecipientPhone(recipientPhone);
		order.setShippingAddress(shippingAddress);
		order.setTotal(spCart.getTotalAmount());

		Set<OrderDetail> orderDetails = new HashSet<OrderDetail>();

		spCart.getCart().forEach((book, quantity) -> {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setBook(book);
			orderDetail.setBookOrder(order);
			orderDetail.setQuantity(quantity);
			orderDetail.setSubtotal(quantity * book.getPrice());

			orderDetails.add(orderDetail);
		});

		order.setOrderDetails(orderDetails);

		BookOrder createdOrder = orderDao.create(order);

		spCart.clear();

		request.setAttribute("CREATED_ORDER", createdOrder);
		RequestDispatcher rd = request.getRequestDispatcher(JspPathHelper.ORDER_SUCCESS);
		rd.forward(request, response);

	}

	public void listOrdersByCustomer() throws ServletException, IOException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute(ServletHelper.SESSION_LOGGED_CUSTOMER);
		
		List<BookOrder> listOrders = orderDao.listByCustomer(customer.getCustomerId());
		
		request.setAttribute("LIST_ORDERS", listOrders);
		RequestDispatcher rd = request.getRequestDispatcher(JspPathHelper.ORDER_VIEW);
		rd.forward(request, response);
	}

	public void showOrderDetailToCustomer() throws ServletException, IOException {
		int orderId = Integer.parseInt(request.getParameter("id"));
		Customer customer = (Customer) request.getSession().getAttribute(ServletHelper.SESSION_LOGGED_CUSTOMER);
		
		BookOrder order = orderDao.get(orderId, customer.getCustomerId());

		request.setAttribute("ORDER", order);
		RequestDispatcher rd = request.getRequestDispatcher(JspPathHelper.ORDER_VIEW_DETAILS);
		rd.forward(request, response);
	}

	public void showEditFormForAdmin() throws ServletException, IOException {
		int orderId = Integer.parseInt(request.getParameter("id"));
		
		List<Category> listCategories = categoryDao.listAll();
		BookOrder order = orderDao.get(orderId);
		
		request.setAttribute("ORDER", order);
		request.setAttribute("LIST_CATEGORIES", listCategories);
		RequestDispatcher dispatcher = request.getRequestDispatcher(JspPathHelper.ORDER_EDIT);
		dispatcher.forward(request, response);	
	}

}
