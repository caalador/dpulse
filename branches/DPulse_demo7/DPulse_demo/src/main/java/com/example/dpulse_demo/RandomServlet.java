package com.example.dpulse_demo;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/random/*")
public class RandomServlet extends HttpServlet {

	private static final long serialVersionUID = 8397201047220361172L;

	private static int call = 0;

	@Override
	public void service(final ServletRequest arg0, final ServletResponse arg1) throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		switch (call % 6) {
		case 0:
			resp.setStatus(200);
			break;
		case 1:
			try {
				Thread.sleep(new Random(System.currentTimeMillis()).nextInt(1000));
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resp.setStatus(200);
			break;
		case 2:
			resp.setStatus(500);
			break;
		case 3:
			try {
				Thread.sleep(new Random(System.currentTimeMillis()).nextInt(2500));
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resp.setStatus(400);
			break;
		case 4:
			try {
				Thread.sleep(new Random(System.currentTimeMillis()).nextInt(250));
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resp.setStatus(200);
			break;
		case 5:
			try {
				Thread.sleep(new Random(System.currentTimeMillis()).nextInt(750));
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resp.setStatus(200);
			break;
		}
		call += new Random(System.currentTimeMillis()).nextInt(10);
	}
}
