package org.codehaus.waffle.example.simple;

import java.util.Map;

import org.codehaus.waffle.monitor.CommonsLoggingMonitor;


public class SimpleMonitor extends CommonsLoggingMonitor {

    /**
     * We want to shouw that we can change all monitor levels to INFO
     */
    @Override
    protected Map<String, Level> monitorLevels(){
        Map<String, Level> levels = super.monitorLevels();
        for ( String key : levels.keySet() ){
            levels.put(key, Level.INFO);
        }
        return levels;       
    }

}
