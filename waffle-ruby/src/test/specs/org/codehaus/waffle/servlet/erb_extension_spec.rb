require 'erb'
require 'org/codehaus/waffle/erb_extension'
require 'ostruct'

describe Waffle::PartialController do

  it "should expose locals as readable attributes" do
    controller = Waffle::PartialController.new(Object.new, {:foo => 'bar', :hello => 'world'})

    controller.foo.should == 'bar'
    controller.hello.should == 'world'

    # Ensuring attributes do not propogate to subsequent instances
    other = Waffle::PartialController.new(Object.new)
    other.should_not respond_to(:foo)
    other.should_not respond_to(:bar)
  end

  it "should define the same instance variables as the provided delegate controller" do
    delegate = Object.new
    delegate.instance_variable_set("@foo", 54)
    delegate.instance_variable_set("@bar", 99)

    controller = Waffle::PartialController.new(delegate)
    controller.instance_variables.should include("@foo")
    controller.instance_variables.should include("@bar")

    controller.instance_variable_get("@foo").should == 54
    controller.instance_variable_get("@bar").should == 99
  end

end

describe ERB::Util, "partial method" do

  class FakeController
    include ERB::Util  # Mix-in what's under test

    attr :name

    def initialize
      @name = 'waffle'
    end
  end

  it "should locate file and render through ERB" do
    controller = FakeController.new

    File.should_receive(:exist?).with('file path').and_return(true)
    Waffle::ScriptLoader.should_receive(:locate_template).with('file name').and_return("file path")
    File.should_receive(:open).with('file path').and_return([%{Name: <%=@name%> Foo: <%=foo%>}])

    response = controller.send(:partial, 'file name', {:foo => 'bar'})
    response.should == "Name: waffle Foo: bar"
  end

  it "should gracefully handle file does not exist" do
    controller = FakeController.new

    Waffle::ScriptLoader.should_receive(:locate_template).with('bad_file.rhtml').and_return("bad file path")

    response = controller.send(:partial, "bad_file.rhtml")
    response.should == %Q{File Not Found: Unable to render file 'bad file path'.}
  end

end