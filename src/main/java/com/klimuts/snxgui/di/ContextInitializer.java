package com.klimuts.snxgui.di;

import com.klimuts.snxgui.SnxClient;
import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ContextInitializer {

    private static final String SUPERCLASS_NAME = "java.lang.Object";

    public static Context init(SnxClient mainClassBean, String packageName) {
        log.debug("Starting context building");
        Context context = new Context(new HashMap<>());
        context.addBean(context.getClass(), context);
        context.addBean(mainClassBean.getClass(), mainClassBean);

        List<Class<?>> classes = findPackageClasses(packageName);
        Set<Class<?>> components = findManagedComponents(classes);
        log.trace("Found [{}] classes, [{}] managed components", classes.size(), components.size());

        initDependencies(context, components);

        log.debug("Context successfully built");
        return context;
    }

    private static void initDependencies(Context context, Set<Class<?>> components) {
        log.trace("Configuring components dependencies");
        components.forEach(clazz -> {
            try {
                Object component = context.getBean(clazz);
                if (component == null) {
                    context.addBean(clazz, clazz.getConstructor().newInstance());
                }
                Set<Field> fields = findManagedFields(clazz, Autowired.class);
                for (Field field : fields) {
                    field.setAccessible(true);

                    Object dependency = context.getBean(field.getType());
                    if (dependency == null) {
                        dependency = field.getType().getConstructor().newInstance();
                        context.addBean(field.getType(), dependency);
                    }
                    field.set(context.getBean(clazz), dependency);
                    log.trace("[{}] set field: [{}]", clazz.getName(), field.getType());
                    field.setAccessible(false);
                }
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                log.error("Error when configuring components dependencies", e);
            }
        });
    }

    private static List<Class<?>> findPackageClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        ClassGraph.CIRCUMVENT_ENCAPSULATION = ClassGraph.CircumventEncapsulationMethod.JVM_DRIVER;
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(packageName)
                .scan()) {
            ClassInfoList controlClasses = scanResult.getSubclasses(SUPERCLASS_NAME);

            classes.addAll(controlClasses.loadClasses());
        } catch (Exception e) {
            log.error("Error when scanning package classes", e);
        }
        return classes;
    }

    private static Set<Field> findManagedFields(Class<?> clazz, Class<? extends Annotation> annotation) {
        Set<Field> set = new HashSet<>();
        Class<?> c = clazz;
        while (c != null) {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotation)) {
                    set.add(field);
                }
            }
            c = c.getSuperclass();
        }
        return set;
    }

    private static Set<Class<?>> findManagedComponents(List<Class<?>> classes) {
        Set<Class<?>> components = new HashSet<>();
        classes.forEach(clazz -> {
            for (Annotation annotation : clazz.getAnnotations()) {
                if (annotation instanceof Component) {
                    components.add(clazz);
                }
            }
        });
        return components;
    }

}
