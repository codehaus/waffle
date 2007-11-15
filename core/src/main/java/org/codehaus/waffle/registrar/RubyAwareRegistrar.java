package org.codehaus.waffle.registrar;

public interface RubyAwareRegistrar {

    void registerRubyScript(String key, String className);
    
}
