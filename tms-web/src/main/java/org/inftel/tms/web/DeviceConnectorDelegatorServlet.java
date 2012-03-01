package org.inftel.tms.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.inftel.tms.devices.DeviceConnector;

/**
 * Obtiene los datos de la peticion HTTP y delega el procesado a {@link DeviceConnector}.
 * 
 * FIXME Solo deberia procesar los POST, pero por ahora dejamos POST y GET para facilitar desarrollo
 * 
 * @author ibaca
 */
@WebServlet(name = "DeviceConnectorDelegatorServlet", urlPatterns = { "/connector" })
public class DeviceConnectorDelegatorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private DeviceConnector deviceConnector;

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
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String from = request.getHeader("sender-mobile-number");
			// readLine no es muy seguro, pero paSOS establece que el mensaje esta en la primera
			// linea
			// del contenido del POST, y por tanto deberia ser suficiente
			String message = request.getReader().readLine();
			// Se delega el procesado y se responde con el mensaje devuelto
			out.println(deviceConnector.processAlertMessage(from, message));
		} finally {
			out.close();
		}
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
