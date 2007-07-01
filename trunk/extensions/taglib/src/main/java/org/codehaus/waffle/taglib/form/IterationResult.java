package org.codehaus.waffle.taglib.form;

import javax.servlet.jsp.tagext.BodyTag;

/**
 * Results for the tags iterating over a body.
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public enum IterationResult {

    /**
     * IterationResult.Body - continue with the body of the tag
     * IterationResult.PAGE - continue with the page (do not eval the body)
     * IterationResult.BODY_AGAIN - do the body again (when in loop tag, for example)  
     * 
     */
    BODY(BodyTag.EVAL_BODY_INCLUDE), BODY_AGAIN(BodyTag.EVAL_BODY_AGAIN), PAGE(BodyTag.EVAL_PAGE), SKIP_BODY(BodyTag.SKIP_BODY);

    private final int tagResult;

    IterationResult(int tagResult) {
	this.tagResult = tagResult;
    }

    public int getTagResult() {
	return tagResult;
    }

}
