include_class 'org.codehaus.waffle.view.View'

class FooBar

  def index
    request[:foo] = 'bar'
    session[:bar] = 'foo'
    p session
    begin
      %{
        HELLO WORLD from the index method
        look up from pico: #{find_chicago}
        request: #{request}
        session: #{session}
        #{session['waffle.session.container']}

        #{session.getServletContext().getRealPath('/WEB-INF/')}
      }
    rescue Exception => e
      "ERROR #{e}"
    end
  end

  def bar
    "HELLO WORLD #{request.local_name} #{request.local_port}"
  end

  def jspx
    @var1 = "this is my variables value from jruby"
    return View.new("foobar.jspx", self)
  end

end