class HelloController

  def index
    @my_instance_var = "Testing instance vars..."
    render("hello.rhtml")
  end

  def test_method
    @value = "Other Value"
    render "other.rhtml"
  end

end