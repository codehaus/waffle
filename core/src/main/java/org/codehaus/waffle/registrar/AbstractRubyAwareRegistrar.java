package org.codehaus.waffle.registrar;


public abstract class AbstractRubyAwareRegistrar extends AbstractRegistrar implements RubyAwareRegistrar {
    private final RubyAwareRegistrar rubyAwareRegistrar;

    public AbstractRubyAwareRegistrar(Registrar delegate) {
        super(delegate);
        rubyAwareRegistrar = (RubyAwareRegistrar)delegate;
    }

    public void registerRubyScript(String key, String className) {
        rubyAwareRegistrar.registerRubyScript(key, className);
    }
}
