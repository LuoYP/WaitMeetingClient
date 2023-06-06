package org.example.shell.dispatch;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodDefinition {

    private String command;

    private String description;

    private Map<String, String> args = new HashMap<>();

    private Method method;

    public String command() {
        return command;
    }

    public MethodDefinition setCommand(String command) {
        this.command = command;
        return this;
    }

    public String description() {
        return description;
    }

    public MethodDefinition setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, String> args() {
        return args;
    }

    public MethodDefinition setArgs(Map<String, String> args) {
        this.args = args;
        return this;
    }

    public Method method() {
        return method;
    }

    public MethodDefinition setMethod(Method method) {
        this.method = method;
        return this;
    }
}
