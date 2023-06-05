package org.example.shell;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import org.example.shell.annotation.Shell;
import org.example.shell.dispatch.DispatchCenter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class ShellContext {

    public static void run(Class<?> mainClass) {
        Set<Class<?>> shells = ClassUtil.scanPackageByAnnotation(mainClass.getPackageName(), Shell.class);
        DispatchCenter.analysis(shells);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("shell: ");
            String scan = scanner.nextLine();
            String[] split = scan.split(" ");
            if (split.length == 0) {
                System.out.println("请输入命令");
                continue;
            }
              String command = split[0];
            if (CharSequenceUtil.isBlank(command)) {
                System.out.println("请输入命令");
                continue;
            }
            if ("exit".equals(command) || "quit".equals(command)) {
                break;
            }
            Pair<Object, Method> methodPair = DispatchCenter.loadMethodByCommand(command);
            if (Objects.isNull(methodPair)) {
                System.out.println("该命令暂不支持");
                continue;
            }
            Object instance = methodPair.getKey();
            Method method = methodPair.getValue();
            String[] parameters = null;
            Object result;
            if (split.length > 1) {
                parameters = Arrays.copyOfRange(split, 1, split.length);
            }
            try {
                if (Objects.nonNull(parameters)) {
                    result = method.invoke(instance, (Object[]) parameters);
                } else {
                    result = method.invoke(instance);
                }
                if (Objects.nonNull(result)) {
                    System.out.println((String) result);
                }
            } catch (Exception e) {
                System.out.println("该命令暂不支持" + e.getMessage());
            }
        }
    }
}