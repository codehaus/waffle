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
      return e
    end
  end
  def bar
    "HELLO WORLD #{request.local_name} #{request.local_port}"
  end
end