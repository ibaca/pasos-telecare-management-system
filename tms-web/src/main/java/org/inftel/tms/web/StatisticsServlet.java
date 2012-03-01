/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.chart.PieChartModel;

/**
 * 
 * @author agumpg
 */
@WebServlet(name = "StatisticsServlet", urlPatterns = { "/StatisticsServlet" })
public class StatisticsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static class ChartUserTypeBean implements Serializable {

		private static final long serialVersionUID = 1L;

		private PieChartModel pieModel;

		public void ChartBean() {
			createPieModel();
		}

		public PieChartModel getPieModel() {
			return pieModel;
		}

		private void createPieModel() {
			pieModel = new PieChartModel();

			pieModel.set("Mayor de 65 años", 45);
			pieModel.set("Discapacidad Física", 7);
			pieModel.set("Discapacidad Psíquica", 1);
			pieModel.set("Discapacidad Sensorial", 2);
			pieModel.set("Enfermos crónicos", 4);
			pieModel.set("Violencia de Género", 17);
			pieModel.set("Otros", 24);
		}
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nextJSP = "/statistics.jsp";
		String action = request.getParameter("action");

		if (action.equals("Statistic_UserType")) {
			nextJSP = "/statistics/graphicUserType.jsp";

			ChartUserTypeBean chartUserTypeBean = new ChartUserTypeBean();
			request.setAttribute("chartUserTypeBean", chartUserTypeBean);
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(request, response);

	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
