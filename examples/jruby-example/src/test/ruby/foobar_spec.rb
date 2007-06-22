import org.codehaus.waffle.servlet.WaffleServlet
require 'ruby/person'

describe 'stuff' do

  it "should be able to get classes from jars, compiled directories and *.rb files" do
    servlet = WaffleServlet.new
    p "Servlet #{list}"

    p Person.new
  end
end
