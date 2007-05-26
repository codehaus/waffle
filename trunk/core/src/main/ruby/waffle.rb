# This is still a work in progress

module Waffle
    module Controller

        # the setters for these should be hidden (i.e. __request=, __response=, __session=)?
        attr_accessor :request, :response, :session

        def __pico_container=(pico)
            @@__pico_container = pico
        end

        def method_missing(symbol, *args)
            if symbol.to_s =~ /^find_/
                key = symbol.to_s.gsub(/^find_/, '')
                return @@__pico_container.getComponentInstance(key)
            end
        end

    end
end