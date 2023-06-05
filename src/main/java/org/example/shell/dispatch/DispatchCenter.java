package org.example.shell.dispatch;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import org.example.shell.annotation.ShellMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DispatchCenter {

    private static final Map<String, Pair<Object, Method>> SHELL_METHODS = new ConcurrentHashMap<>();

    public static void analysis(Set<Class<?>> shellClasses) {
        for (Class<?> shellClass : shellClasses) {
            Method[] methods = shellClass.getMethods();
            for (Method method : methods) {
                ShellMethod annotation = method.getAnnotation(ShellMethod.class);
                if (Objects.isNull(annotation)) {
                    continue;
                }
                String command = annotation.command();
                if (CharSequenceUtil.isBlank(command)) {
                    command = method.getName();
                }
                if (SHELL_METHODS.containsKey(command)) {
                    throw new RuntimeException(command + "重复了");
                }
                Object shellInstance = ReflectUtil.newInstance(shellClass);
                Pair<Object, Method> pair = Pair.of(shellInstance, method);
                SHELL_METHODS.put(command, pair);
            }
        }
    }

    public static Pair<Object, Method> loadMethodByCommand(String command) {
        return SHELL_METHODS.get(command);
    }
}
