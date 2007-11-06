require 'erb'

module Waffle
  class PartialController

    def initialize(delegate, locals = {})

      def self.metaclass; class << self; self; end; end

      delegate.instance_variables.each do |key|
        self.instance_variable_set(key, delegate.instance_variable_get(key))
      end

      # Add locals to virtual class
      unless locals.empty?
        locals.keys.each do |key|
          metaclass.send(:attr_reader, key)
        end

        locals.each_pair do |key, value|
          self.instance_variable_set("@#{key}", value)
        end
      end

    end
  end
end

class ERB

  module Util

    #
    # renders partial page (rhtml).
    #
    # This will be mixed in to ruby based Waffle Controllers
    #
    def partial(file_name, locals = {})
      controller = Waffle::PartialController.new(self, locals)

      # Locate full path to template (*.rhtml)
      path = Waffle::ScriptLoader.locate_template(file_name)

      return "File Not Found: Unable to render file '#{path}'." unless File.exist?(path)


      # TODO can't we just call open() ??
      template = ''
      File.open(path).each { |line| template << line }

      erb = ERB.new(template)
      return erb.result(controller.send(:binding))
    end

    module_function :partial

  end
  
end
