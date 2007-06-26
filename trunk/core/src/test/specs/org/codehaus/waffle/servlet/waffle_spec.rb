require 'org/codehaus/waffle/waffle'

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

    Waffle::ScriptLoader.should_receive(:require).with('ruby/one')
    Waffle::ScriptLoader.should_receive(:require).with('ruby/two')
    Waffle::ScriptLoader.load_all('fake/dir/path', servlet_context)

    Waffle::ScriptLoader.instance_variable_get(:@__servlet_context).should == servlet_context
  end

end