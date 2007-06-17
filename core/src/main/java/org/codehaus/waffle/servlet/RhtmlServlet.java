package org.codehaus.waffle.servlet;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RhtmlServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println(request.getRequestURI()); // TODO determine what the requested rhtml is
        String template = loadRhtml("hello.rhtml");

        Ruby runtime = RequestLevelContainer.get().getComponentInstanceOfType(Ruby.class);

        runtime.evalScript("require 'erb'\n");
        RubyModule module = runtime.getClassFromPath("ERB");

        IRubyObject erb = (IRubyObject) JavaEmbedUtils.invokeMethod(runtime, module, "new",
                new Object[] {JavaEmbedUtils.javaToRuby(runtime, template)}, IRubyObject.class); // cache?

        // Need to include ERB::Util on whats being bound
        // TODO bind the controller ... not self
        runtime.evalScript("self.send :include, ERB::Util");

        String out = (String)JavaEmbedUtils.invokeMethod(runtime, erb, "result",
                new Object[] {(Object) runtime.evalScript("self.send :binding")}, String.class);

        response.getOutputStream().println(out);
        response.getOutputStream().flush();
    }

    private String loadRhtml(String fileName) {
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;

        try {
            inputStream = getServletContext().getResourceAsStream(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder script = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                script.append(line).append("\n");
                line = bufferedReader.readLine();
            }

            return script.toString();
        } catch (IOException e) {
            throw new WaffleException(e);
        } finally {
            try {
                if(inputStream != null) inputStream.close();
                if(bufferedReader != null) bufferedReader.close();
            } catch (IOException ignore) {
                // ignore
            }
        }
    }
}
