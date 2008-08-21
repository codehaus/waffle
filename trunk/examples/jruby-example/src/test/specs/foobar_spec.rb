require 'java'

import org.codehaus.waffle.servlet.WaffleServlet
require 'person'

describe 'stuff' do

  it "should be able to get classes from jars, compiled directories and *.rb files" do
    servlet = WaffleServlet.new
    p "Servlet #{servlet}"

    p Person.new
  end
end
