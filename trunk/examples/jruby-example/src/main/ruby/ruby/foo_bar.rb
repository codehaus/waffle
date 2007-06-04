
class FooBar

  def index
    request[:foo] = 'bar'
    session[:bar] = 'foo'
    p session
    begin
      %{
        HELLO WORLD from the index method
        look up from pico: #{find_chicago}
        parameters: #{parameters}
        request: #{request}
        session: #{session}
        servlet_context: #{servlet_context}
        #{session['waffle.session.container']}

        #{session.getServletContext().getRealPath('/WEB-INF/')}

        auto resolve #{foo}
      }
    rescue Exception => e
      "ERROR #{e}"
    end
  end

  def bar
    "HELLO WORLD #{request.local_name} #{request.local_port}"
  end

  def view_jspx
    p "CALLED!"
    @var1 = "this is my variables value from jruby"
    view = render("foobar.jspx")
    p "DONE"
    return view
  end

  def redirect_to_jspx
    @var1 = "this is my variables value from jruby xxx"
    return redirect_to("index.html")
  end

end