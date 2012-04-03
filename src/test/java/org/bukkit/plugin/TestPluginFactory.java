package org.bukkit.plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class TestPluginFactory {
    private interface MethodHandler {
        Object invoke(Object[] args);
    }
    private static final Method equals;
    private static final Method hashCode;
    private static final Method getDescription;
    private static final Constructor<? extends Plugin> pluginConstructor;
    static {
        try {
            equals = Object.class.getMethod("equals", Object.class);
            hashCode = Object.class.getMethod("hashCode");
            getDescription = Plugin.class.getMethod("getDescription");
            pluginConstructor =
                    Proxy.getProxyClass(TestPluginFactory.class.getClassLoader(), Plugin.class)
                    .asSubclass(Plugin.class)
                    .getConstructor(InvocationHandler.class);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static Plugin getPlugin(final String name) {
        try {
            return new Object() {
                final String pluginName = name;
                final Plugin plugin = pluginConstructor.newInstance(new InvocationHandler() {
                    Map<Method, MethodHandler> handlers = ImmutableMap.<Method, MethodHandler>builder().put(
                            equals, new MethodHandler() {
                                public Object invoke(Object[] args) {
                                    if (args[0] == plugin) return true;
                                    if (!(args[0] instanceof Plugin)) return false;
                                    return pluginName.equals(((Plugin) args[0]).getDescription().getName());
                                }}).put(
                            getDescription, new MethodHandler() {
                                public Object invoke(Object[] args) {
                                    return new PluginDescriptionFile(pluginName, "1.0", "test.test");
                                }}).put(
                            hashCode, new MethodHandler() {
                                public Object invoke(Object[] args) {
                                    return pluginName.hashCode();
                                }}).build();
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        MethodHandler handler = handlers.get(method);
                        if (handler != null) return handler.invoke(args);
                        throw new UnsupportedOperationException(method + " is unsupported");
                    }});
                }.plugin;
        } catch (Throwable t) {
            throw new Error(t);
        }
    }
}
