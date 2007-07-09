require 'org/codehaus/waffle/waffle'
require 'ostruct'

include_class 'java.util.Hashtable'

describe "Waffle::ScriptLoader module" do

  it "should use 'load' when path prefix begins with 'dir:'" do
    servlet_context = mock('servlet_context')

    Waffle::ScriptLoader.should_receive(:load_from_file_system)
    Waffle::ScriptLoader.load_all('dir:/fake/dir/path', servlet_context)

    # Class instance variables should have been set
    Waffle::ScriptLoader.instance_variable_get(:@__servlet_context).should == servlet_context
    Waffle::ScriptLoader.instance_variable_get(:@__ruby_script_path).should == '/fake/dir/path'
  end

  it "should use 'require' when path prefix does NOT beign with 'dir:' (as is the case in production)" do
    paths = ['fake/dir/path/one', 'fake/dir/path/two']

    servlet_context = mock('servlet_context')
    servlet_context.should_receive(:getResourcePaths).with('fake/dir/path').and_return(paths)

    Waffle::ScriptLoader.should_receive(:require).with('one')
    Waffle::ScriptLoader.should_receive(:require).with('two')
    Waffle::ScriptLoader.load_all('fake/dir/path', servlet_context)

    Waffle::ScriptLoader.instance_variable_get(:@__servlet_context).should == servlet_context
  end

end

describe "Waffle::WebContext class" do

  it "initialize() should obtain all attribute name/values and add them to a Ruby Hash" do
    table = Hashtable.new # Using Hashtable because we need to an enumerator for testing
    table.put('foo', "junk")
    table.put(:bar, "junk")

    original_context = mock('original context')
    original_context.should_receive(:get_attribute_names).and_return(table.keys)
    original_context.should_receive(:get_attribute).with('foo').and_return('key was a string')
    original_context.should_receive(:get_attribute).with(:bar).and_return('key was a symbol')

    waffle_context = Waffle::WebContext.new(original_context)

    waffle_context.include?('foo').should == true
    waffle_context.include?(:bar).should == true
  end

  it "should delegate method calls to the underlying instance when method is missing" do
    context = mock('the mock')
    context.should_receive(:get_attribute_names).and_return(OpenStruct.new)
    context.should_receive(:foo).with('bar').and_return('success')

    waffle_context = Waffle::WebContext.new(context)

    waffle_context.foo('bar').should == "success"
  end

end

describe "Waffle::Controller module" do

  it "__set_all_contexts should process request, response, session attributes as well as request parameters" do
    controller = Object.new
    controller.send(:extend, Waffle::Controller)

    servlet_context = mock('servlet context')
    session = mock('session')
    session.should_receive(:getServletContext).and_return(servlet_context)

    # Using Hashtable because we need to an enumerator for testing
    parameters = Hashtable.new
    parameters.put('foo', "junk")

    request = mock('request')
    request.should_receive(:getSession).with(false).and_return(session)
    request.should_receive(:getParameterNames).and_return(parameters.keys)
    request.should_receive(:getParameter).with('foo').and_return('bar')

    response = mock('response')

    Waffle::WebContext.should_receive(:new).with(request).and_return(request) # return same mock to keep test simple
    Waffle::WebContext.should_receive(:new).with(session).and_return(session) # return same mock to keep test simple
    Waffle::WebContext.should_receive(:new).with(servlet_context)

    controller.should_receive(:__process_request_params)

    controller.__set_all_contexts(request, response)

    # Ensure parameters set correctly
    controller.parameters['foo'].should == 'bar'
  end


end