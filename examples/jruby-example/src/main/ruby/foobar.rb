class FooBar

  def index
    @var1 = "is cool and fast, right?"
    render("foobar.rhtml")
  end

  def hola
    errors # automatically resolves from params => req.attr => session.attr => application.attr
  end

  def ajax
    "<h1>Hello</h1>"
  end

  def index_two
    request[:foo] = 'bar'
    session['bar'] = 'foo'
    session['baz'] = 'foo'
    application[:name] = 'Chicago'
    p session

    %{

        HELLO WORLD from the index method
        parameters: #{parameters}
        params - #{str = ""; params.each_pair{|k,v| str << "k: #{k} v: #{v}"}}
        request: #{request}
        session: #{session}
        application: #{application}
        name: #{name}
        servlet_context: #{servlet_context}
        #{session['waffle.session.container']}

        #{session.getServletContext().getRealPath('/WEB-INF/')}

        auto resolve #{foo}

        component: #{locate(java.util.List)}
        component: #{locate(org.codehaus.waffle.example.jruby.dao.PersonDAO)}
        component: #{locate_the_dao}

        #{cls = Java::JavaClass.for_name('java.util.Vector')}
        #{locate(Java::JavaClass.for_name('java.util.List'))}
    }

  end

  def bar
    "HELLO WORLD #{request.local_name} #{request.local_port}"
  end

  def view
    p "CALLED!"
    @var1 = "this is my variables value from jruby!!!!!!!!"
    view = render("foobar.rhtml")
    p "DONE"
    return view
  end

  def redirect
    @var1 = "this is my variables value from jruby xxx"
    return redirect_to("index.html")
  end

  def request_parameter_example
    return "<h1>FOO: #{params['foo']}</h1>"
  end

  def request_attribute_example
    return "<h1>FOO: #{request['foo']}</h1>"
  end

end