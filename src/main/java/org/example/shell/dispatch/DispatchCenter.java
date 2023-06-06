package org.example.shell.dispatch;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import org.example.shell.annotation.ShellMethod;
import org.example.shell.annotation.ShellOption;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DispatchCenter {

    private static final Map<String, Pair<Object, MethodDefinition>> SHELL_METHODS = new ConcurrentHashMap<>();

    public static void analysis(Set<Class<?>> shellClasses) {
        for (Class<?> shellClass : shellClasses) {
            Method[] methods = shellClass.getMethods();
            for (Method method : methods) {
                ShellMethod shellMethod = method.getAnnotation(ShellMethod.class);
                if (Objects.isNull(shellMethod)) {
                    continue;
                }
                MethodDefinition methodDefinition = new MethodDefinition();
                String command = CharSequenceUtil.isBlank(shellMethod.command()) ? method.getName() : shellMethod.command();
                if (SHELL_METHODS.containsKey(command)) {
                    throw new RuntimeException(command + "重复了");
                }
                String description = CharSequenceUtil.isBlank(shellMethod.description()) ? method.getName() : shellMethod.description();
                methodDefinition.setCommand(command).setDescription(description);
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    ShellOption shellOption = parameter.getAnnotation(ShellOption.class);
                    if (Objects.isNull(shellOption)) {
                        methodDefinition.args().put(parameter.getName(), parameter.getName());
                        continue;
                    }
                    String name = CharSequenceUtil.isBlank(shellOption.name()) ? parameter.getName() : shellOption.name();
                    String optionDescription = CharSequenceUtil.isBlank(shellOption.description()) ? parameter.getName() : shellOption.description();
                    methodDefinition.args().put(name, optionDescription);
                }
                methodDefinition.setMethod(method);
                Object shellInstance = ReflectUtil.newInstance(shellClass);
                Pair<Object, MethodDefinition> pair = Pair.of(shellInstance, methodDefinition);
                SHELL_METHODS.put(command, pair);
            }
        }
    }

    public static Pair<Object, MethodDefinition> loadMethodByCommand(String command) {
        return SHELL_METHODS.get(command);
    }
}
