include_class 'java.net.ServerSocket'

describe "jruby example from aslak's wiki" do

  it "should know its own port" do
    server_socket = ServerSocket.new(5678)
    server_socket.localPort.should == 5678


  end

  it "should be able to mock methods on base java classes" do
    list = java.util.ArrayList.new
    list.should_receive(:size).and_return 99

    list.size.should == 99
  end
end