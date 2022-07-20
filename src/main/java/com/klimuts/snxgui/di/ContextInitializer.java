package com.klimuts.snxgui.di;

import com.klimuts.snxgui.SnxClient;
import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContextInitializer {

    private static final String SUPERCLASS_NAME = "java.lang.Object";

    public static Context init(SnxClient mainClassBean, String packageName) {
        Context context = new Context(new HashMap<>());
        context.addBean(context.getClass(), context);
        context.addBean(mainClassBean.getClass(), mainClassBean);

        List<Class<?>> classes = findPackageClasses(packageName);
        Set<Class<?>> components = findManagedComponents(classes);

        initDependencies(context, components);

        return context;
    }

    private static void initDependencies(Context context, Set<Class<?>> components) {
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
                    field.setAccessible(false);
                }
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        });
    }

    private static List<Class<?>> findPackageClasses(String packageName) {
        List<Class<?>> classes;
        ClassGraph.CIRCUMVENT_ENCAPSULATION = ClassGraph.CircumventEncapsulationMethod.JVM_DRIVER;
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(packageName)
                .scan()) {
            ClassInfoList controlClasses = scanResult.getSubclasses(SUPERCLASS_NAME);

            classes = new ArrayList<>(controlClasses.loadClasses());
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
