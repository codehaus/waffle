require 'java'

import org.codehaus.waffle.view.View
import org.codehaus.waffle.view.RedirectView
import org.codehaus.waffle.action.ActionMethodInvocationException

# TODO all of this code needs ruby tests!
module Waffle

  # load/require files
  class ScriptLoader

    def ScriptLoader.load_all(prefix, servlet_context)
      @__servlet_context = servlet_context

      if (prefix.gsub!(/^dir:/, ''))
        @__ruby_script_path = prefix
        ScriptLoader.load_from_file_system
      else
        servlet_context.getResourcePaths(prefix).each do |path| # this would be for production!!
          require(path.gsub(%r{#{prefix}\/}, ''))
        end
      end
    end

    def ScriptLoader.load_from_file_system
      path = @__servlet_context.getRealPath('/')
      path = "#{path}#{@__ruby_script_path}"

      Dir.new(path).each do |entry|
        file = "#{path}#{entry}"
        if File.file?(file) # TODO need to recursively search directories
          begin
            load(file) if file =~ /.rb$/
          rescue Exception => e
            p "[WAFFLE] Error loading ruby script: #{e}" # TODO plugin monitoring/logging  
          end
        end
      end
    end

    # Locate RHTML templates
    def self.locate_template(file_name)
      file_name = file_name =~ /.rhtml$/ ? file_name : "#{file_name}.rhtml"
      return @__servlet_context.getRealPath('/') + file_name
    end

  end

  ##
  # This is a generic class for making HttpServletRequest's, HttpSession's and ServletContext's to act like Hash's
  class WebContext < Hash

    def initialize(context)
      @__context = context
      attribute_names = @__context.get_attribute_names

      while (attribute_names.hasMoreElements)
        name = attribute_names.nextElement

        # Store in hash but skip setting back to underlying context
        self.store(name, @__context.get_attribute(name), true)
      end
    end

    def store(key, value, skip=false)
      key = key.to_s # Must be a String
      @__context.set_attribute(key, value) unless skip # add to delegate

      super(key, value)
    end

    def [](key)
      m = Hash.instance_method(:[]).bind(self).call(key.to_s)
    end

    def []=(key, value)
      store(key, value)
    end

    # propogate unknown method calls to the underlying context
    def method_missing(symbol, *args)
      @__context.send(symbol, *args)
    end

    def java_delegate
      return @__context
    end
  end

  module Controller
    attr_reader :parameters, :request, :response, :session, :servlet_context, :errors
    alias_method :params, :parameters
    alias_method :application, :servlet_context

    def __set_all_contexts(request, response)
      @request = WebContext.new(request)
      @session = WebContext.new(@request.getSession(false))
      @servlet_context = WebContext.new(@session.getServletContext());
      @response = response

      # Building up a hash to represent the params
      @parameters = {}
      parameter_names = @request.getParameterNames
      while (parameter_names.hasMoreElements)
        name = parameter_names.nextElement
        @parameters[name] = @request.getParameter(name)
      end

      __process_request_params
    end

    def __pico_container=(pico)
      @__pico_container = pico
    end

    def __argument_resolver=(argument_resolver)
      @__argument_resolver = argument_resolver
    end

    def __errors=(errors)
      @errors = errors
    end

    def locate(type)
      return @__pico_container.getComponentInstanceOfType(type.java_class) if type.is_a? Module

      return @__pico_container.getComponentInstance(type)
    end

    def method_missing(symbol, *args)
      if symbol.to_s =~ /^locate_/
        key = symbol.to_s
        key = key[7..key.length]
        component = @__pico_container.getComponentInstance(key)

        return component unless component.nil?
      else
        value = @__argument_resolver.resolve(@request.java_delegate, "{#{symbol.to_s}}")

        return value unless value.nil?
      end

      super(symbol, *args)
    end

    def render(name)
      return View.new(name, self)
    end

    def redirect_to(name, map = {})
      return RedirectView.new(name, self, map)
    end

    private

    # This method tries to apply parameter values to the controllers instance variables (only if they exist)
    def __process_request_params
      @parameters.each_pair do |key, value|
        parts = key.split('.')
        if self.instance_variable_defined? "@#{key}".to_sym
          begin
            self.instance_eval("@#{key} = '#{value}'")
          rescue Exception => e
            print("ERROR handling request parameter #{key} with value: #{value}\n #{e}\n")
          end
        end
      end
    end

  end
end