module Waffle
    module Controller

        attr_accessor :request, :response, :session

        # Still a work in progress
        def __pico_container=(pico)
            @@__pico_container = pico
        end

        def method_missing(symbol, *args)
            if symbol.to_s =~ /^find_/
                # Pico


            end
        end

        

    end
end