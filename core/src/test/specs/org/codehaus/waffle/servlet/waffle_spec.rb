require 'org/codehaus/waffle/waffle'

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

end