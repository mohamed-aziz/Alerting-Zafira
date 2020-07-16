package utils;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.ant.Groovy;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class GroovyIntegration {
    Binding context;
    URL path;

    private GroovyScriptEngine engine;

    HashMap<String, Object> scripts;

    public GroovyIntegration(Binding context, URL path) {
        this.context = context;
        this.path = path;

        this.engine  = new GroovyScriptEngine(new URL[] {path});
        this.scripts = new HashMap<>();
    }

    public Object runScript(String scriptName) throws ResourceException, ScriptException {
        Object script = this.engine.run(scriptName, this.context);
        scripts.put(script.getClass().getName(), script);
        return script;
    }
}
