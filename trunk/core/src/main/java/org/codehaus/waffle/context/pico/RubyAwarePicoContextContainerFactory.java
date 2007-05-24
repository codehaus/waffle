package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.context.ContextContainer;
import org.picocontainer.MutablePicoContainer;
import org.jruby.Ruby;

public class RubyAwarePicoContextContainerFactory extends PicoContextContainerFactory {

    public RubyAwarePicoContextContainerFactory(MessageResources messageResources) {
        super(messageResources);
    }

    public ContextContainer buildApplicationContextContainer() {
        ContextContainer contextContainer = super.buildApplicationContextContainer();

        Ruby runtime = Ruby.getDefaultInstance();

        // TODO Ruby ... this needs to be moved to an actual Ruby file and loaded from clas loader
        String script =
            "def String.camelize(lower_case_and_underscored_word, first_letter_in_uppercase = true)\n" +
            "  if first_letter_in_uppercase\n" +
            "    lower_case_and_underscored_word.to_s.gsub(/\\/(.?)/) { \"::\" + $1.upcase }.gsub(/(^|_)(.)/) { $2.upcase }\n" +
            "  else\n" +
            "    lower_case_and_underscored_word.first + camelize(lower_case_and_underscored_word)[1..-1]\n" +
            "  end\n" +
            "end";

        runtime.evalScript(script); // load waffle extensions to Ruby language

        // Register RubyRuntime at Application level
        MutablePicoContainer picoContainer = (MutablePicoContainer) contextContainer.getDelegate();
        picoContainer.registerComponentInstance(Ruby.class, runtime);

        return contextContainer;
    }
    
}
