package org.codehaus.waffle.taglib.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * A tag that supports iterable collections.
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public abstract class BasicIterationTag<T> extends BodyTagSupport {

	private Iterator<T> iterator;

	private final boolean showIfEmpty;

	private Collection<T> iterable;

	public BasicIterationTag() {
		this(true);
	}

	public BasicIterationTag(boolean showIfEmpty) {
		this.showIfEmpty = showIfEmpty;
	}

	@Override
	public int doStartTag() throws JspException {
		this.iterable = getCollection();
		try {
			if (notEmpty() || showIfEmpty) {
				beginTag();
			}
			if (iterable == null || iterable.isEmpty()) {
				return BodyTag.SKIP_BODY;
			}
			this.iterator = iterable.iterator();
			beforeBodyFor(iterator.next());
			return BodyTag.EVAL_BODY_INCLUDE;
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

	private boolean notEmpty() {
		return iterable != null && !iterable.isEmpty();
	}

	protected abstract void beginTag() throws JspException, IOException;

	public abstract void beforeBodyFor(T current) throws JspException, IOException;

	protected abstract void endTag() throws JspException, IOException;

	protected abstract Collection<T> getCollection();

	@Override
	public int doEndTag() throws JspException {
		try {
			if (notEmpty() || showIfEmpty) {
				endTag();
			}
		} catch (IOException e) {
			throw new JspException(e);
		}
		this.iterable = null;
		this.iterator = null;
		return BodyTag.EVAL_PAGE;
	}

	@Override
	public void doInitBody() throws JspException {
	}

	public int doAfterBody() throws JspException {
		if (!iterator.hasNext()) {
			return BodyTag.EVAL_PAGE;
		}
		try {
			beforeBodyFor(iterator.next());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return BodyTag.EVAL_BODY_AGAIN;
	}

}
