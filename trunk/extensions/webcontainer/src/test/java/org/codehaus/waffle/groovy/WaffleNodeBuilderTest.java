/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package org.codehaus.waffle.groovy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import junit.framework.TestCase;

import org.mortbay.util.IO;
import org.nanocontainer.script.groovy.GroovyContainerBuilder;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.ObjectReference;
import org.picocontainer.defaults.SimpleReference;

public class WaffleNodeBuilderTest extends TestCase {

    private ObjectReference containerRef = new SimpleReference();
    private ObjectReference parentContainerRef = new SimpleReference();

    private PicoContainer pico;

    protected void tearDown() throws Exception {
        if (pico != null) {
            pico.stop();
        }
    }

    public void testNothing(){
        
    }
    
    public void FIXME_testCanComposeWebContainerWithCompleteWaffleApp() throws InterruptedException, IOException {
        Reader script = new StringReader(
                "nano = builder.container {\n" +
                "    webContainer(port:8080) {\n" +
                "        context(path:'/bar') {\n" +
                "            adapter(nodeBuilder:'org.codehaus.waffle.groovy.WaffleNodeBuilder', viewSuffix:'.jspx') {" +
                "               actionRegistrar(class:org.codehaus.waffle.groovy.FooRegistrar)\n" +
                "               requestFilter(filter:'*.foo')\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n");

        assertPageIsHostedWithContents(script, "hello Fred", "http://localhost:8080/bar/x.foo");
    }

    public void FIXME_testCanComposeWholeWaffleIncludingComposedActions() throws InterruptedException, IOException {
        Reader script = new StringReader(
                "nano = builder.container {\n" +
                "    component(class:org.codehaus.waffle.groovy.FooRegistrar)\n" +
                "    webContainer(port:8080) {\n" +
                "        context(path:'/bar') {\n" +
                "            adapter(nodeBuilder:'org.codehaus.waffle.groovy.WaffleNodeBuilder', viewSuffix:'.jspx') {\n" +
                "                registerActions {\n" +
                "                    application {\n" +
                "                       register(class:org.codehaus.waffle.groovy.FooApplicationAction)\n" +
                "                    }\n" +
//                "                    session {\n" +
//                "                       register(action:'sessionAction', class:org.codehaus.waffle.groovy.FooSessionAction)\n" +
//                "                    }\n" +
                "                }\n" +
                "                requestFilter(filter:'*.htm')\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n");

        assertPageIsHostedWithContents(script, "hello Fred", "http://localhost:8080/bar/foo2");
    }

    private void assertPageIsHostedWithContents(Reader script, String message, String url) throws InterruptedException, IOException {
        pico = buildContainer(script, null, "SOME_SCOPE");
        assertNotNull(pico);

        String actual;
        try {
            actual = getPage(url);
        } catch (FileNotFoundException e) {
            actual = "";
        }
        assertEquals(message, actual);
    }

    private PicoContainer buildContainer(Reader script, PicoContainer parent, Object scope) {
        parentContainerRef.set(parent);
        new GroovyContainerBuilder(script, getClass().getClassLoader()).buildContainer(containerRef, parentContainerRef, scope, true);
        return (PicoContainer) containerRef.get();
    }

    protected String getPage(String url) throws IOException, InterruptedException {
        try {
            return IO.toString(new URL(url).openStream());
        } catch (Exception e) {
            Thread.sleep(2 * 1000);
            try {
                return IO.toString(new URL(url).openStream());
            } catch (Exception e1) {
                return e1.getClass().getName() + ":" + e1.getMessage();
            }
        }
    }


}

