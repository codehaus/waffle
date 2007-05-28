package org.codehaus.waffle.taglib.internal;

import java.io.IOException;
import java.io.Writer;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.apache.taglibs.standard.tag.common.core.UrlSupport;
import org.apache.taglibs.standard.tag.common.fmt.BundleSupport;
import org.apache.taglibs.standard.tag.common.fmt.MessageSupport;

import org.codehaus.waffle.taglib.form.IterationResult;

/**
 * A basic tag. This class has been created in order to be extended.
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public abstract class BasicTag implements BodyTag {

	private Tag parent;

	protected PageContext pageContext;

	private BodyContent bodyContent;

	/**
	 * If true, the FormElement will be rendered.
	 */
	private boolean rendered;
	
	public BasicTag() {
		release();
	}

	/**
	 * Searchs a parent tag that implements the selected type.
	 * 
	 * @param <T>
	 *            the type to search
	 * @param type
	 *            the class type to search
	 * @return the tag found or null if not found
	 */
	@SuppressWarnings("unchecked")
	protected <T> T findAncestor(Class<T> type) {
		Tag current = parent;
		while (current != null && !type.isAssignableFrom(current.getClass())) {
			current = current.getParent();
		}
		return (T) current;
	}

	public Tag getParent() {
		return parent;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;

	}

	public void release() {
		/*parent = null;
		pageContext = null;*/
		rendered = true;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void setParent(Tag parent) {
		this.parent = parent;
	}

	/**
	 * Returns the i18n message for some key
	 * 
	 * @param key
	 *            the key to search for
	 * @return the i18n message
	 */
	public String getI18NMessage(String key) {
		if (key == null || key.equals("")) {
			return "";
		}
		LocalizationContext locCtxt = getLocalizationContext();

		if (locCtxt != null) {
			ResourceBundle bundle = locCtxt.getResourceBundle();
			if (bundle != null) {
				try {
					return bundle.getString(key);
				} catch (MissingResourceException mre) {
					return MessageSupport.UNDEFINED_KEY + key
							+ MessageSupport.UNDEFINED_KEY;
				}
			}
		}
		return MessageSupport.UNDEFINED_KEY + key
				+ MessageSupport.UNDEFINED_KEY;
	}

	protected LocalizationContext getLocalizationContext() {
		return BundleSupport.getLocalizationContext(pageContext);
	}

	/**
	 * Retrieves an encoded url from a basic one.
	 * 
	 * @param url
	 *            the original url
	 * @return the encoded url
	 * @throws JspException
	 */
	public String getAbsoluteUrl(String url) throws JspException {
		return UrlSupport.resolveUrl(url, null, pageContext);
	}

	/**
	 * Evaluates some expression.
	 */
	protected String evaluate(String expression) throws JspException {
		try {
			return (String) pageContext.getExpressionEvaluator().evaluate(
					expression, String.class,
					pageContext.getVariableResolver(), null);
		} catch (ELException e) {
			throw new JspException(e);
		}
	}

	/**
	 * Evaluates this expression in EL. Returns the same as evaluate("${" +
	 * expression + "}");
	 * 
	 * @param expression
	 *            expression in EL.
	 */
	protected String evaluateEl(String expression) throws JspException {
		return evaluate("${" + expression + "}");
	}

	public void setBodyContent(BodyContent bodyContent) {
		this.bodyContent = bodyContent;
	}

	public void doInitBody() throws JspException {
		// does nothing
	}

	public int doAfterBody() throws JspException {
		try {
			IterationResult result = afterBody(pageContext.getOut());
			if (result == IterationResult.BODY_AGAIN) {
				beforeBody(pageContext.getOut());
			}
			return result.getTagResult();
		} catch (IOException ex) {
			throw new JspException(ex);
		}
	}

	/**
	 * To be implemented by child classes that want to do something after the
	 * body.
	 * 
	 * @param out
	 *            the writer
	 * @throws IOException
	 */
	protected IterationResult afterBody(JspWriter out) throws IOException {
		// does nothing
		return IterationResult.PAGE;
	}

	public int doEndTag() throws JspException {
		if(!rendered) {
			return IterationResult.PAGE.getTagResult();
		}
		try {
			end(pageContext.getOut());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return Tag.EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		if(!rendered) {
			return IterationResult.PAGE.getTagResult();
		}
		try {
			IterationResult result = start(pageContext.getOut());
			if (result == IterationResult.BODY) {
				beforeBody(pageContext.getOut());
			}
			return result.getTagResult();
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

	/**
	 * Starts this tag once.
	 * 
	 * @throws JspException
	 * @throws IOException
	 */
	protected abstract IterationResult start(Writer out) throws JspException,
			IOException;

	/**
	 * Executes something before body evaluation.
	 * 
	 * @throws JspException
	 * @throws IOException
	 */
	protected void beforeBody(Writer out) throws JspException, IOException {
		// does nothing by default
	}

	/**
	 * Ends this tag.
	 * 
	 * @throws JspException
	 * @throws IOException
	 */
	protected void end(Writer out) throws JspException, IOException {
		// does nothing by default
	}
	
	public boolean isRendered() {
		return rendered;
	}

}