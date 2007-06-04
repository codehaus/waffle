require 'java'

include_class 'org.codehaus.waffle.view.View'
include_class 'org.codehaus.waffle.view.RedirectView'

module Waffle

  # load/require files
  class ScriptLoader
    def ScriptLoader.load_all(prefix, servlet_context)
      @@__servlet_context = servlet_context

      if (prefix.gsub!(/^dir:/, ''))
        @@_ruby_script_path = prefix
        ScriptLoader.load_from_file_system
      else
        servlet_context.getResourcePaths(prefix).each do |path| # this would be for production!!
          require(path.gsub(Regexp.new("^#{prefix}"), 'ruby/'))
        end
      end
    end

    def ScriptLoader.load_from_file_system
      path = @@__servlet_context.getRealPath('/')
      path = "#{path}#{@@_ruby_script_path}"

      Dir.new(path).each do |entry|
        file = "#{path}#{entry}"
        if File.file?(file) # TODO need to recursively search directories
          load(file) if file =~ /.rb$/
        end
      end
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

        self[name.to_s] = @__context.get_attribute(name) if name.is_a? Symbol
        self[name] = @__context.get_attribute(name) unless name.is_a? Symbol
      end
    end

    def []=(key, value)
      @__context.set_attribute(key.to_s, value) if key.is_a? Symbol
      @__context.set_attribute(key, value) unless key.is_a? Symbol
      super
    end

    def [](key)
      return super(key.to_s) if key.is_a? Symbol

      return super(key)
    end

    def method_missing(symbol, *args)
      @__context.send(symbol, *args)
    end

    def java_delegate
      return @__context
    end
  end

  module Controller
    attr_reader :parameters, :request, :response, :session, :servlet_context

    def __set_all_contexts(request, response)
      @request = WebContext.new(request)
      @parameters = @request.getParameterMap
      @session = WebContext.new(@request.getSession(false))
      @servlet_context = WebContext.new(@session.getServletContext());
      @response = response
    end

    def __pico_container=(pico)
      @@__pico_container = pico
    end

    def __argument_resolver=(argument_resolver)
      @@__argument_resolver = argument_resolver
    end

    def method_missing(symbol, *args)
      if symbol.to_s =~ /^find_/ # todo: I don't like "find_" for this ... sounds to model-ish
        key = symbol.to_s
        key = key[5..key.length]
        component = @@__pico_container.getComponentInstance(key)

        return component unless component.nil?
      else
        value = @@__argument_resolver.resolve(@request.java_delegate, "{#{symbol.to_s}}")

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

  end
end