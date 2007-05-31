package org.codehaus.waffle.view;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A ResponderView is able to use the servlet response and write directly to it,
 * instead of dispatching it.
 * 
 * @author Paulo Silveira
 */
public abstract class ResponderView extends View {

	public ResponderView() {
		super(null, "");
	}

	/**
	 * Renders the output directly into servlet response
	 * 
	 * @param req
	 *            servler request
	 * @param resp
	 *            http servlet response
	 * @throws IOException
	 */
	public abstract void respond(ServletRequest req, HttpServletResponse resp)
			throws IOException;
}
