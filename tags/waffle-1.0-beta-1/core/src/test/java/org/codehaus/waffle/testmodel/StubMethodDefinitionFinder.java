package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodDefinition;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StubMethodDefinitionFinder implements MethodDefinitionFinder {
    
    public MethodDefinition find(Object controller,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        throw new UnsupportedOperationException();
    }
}
