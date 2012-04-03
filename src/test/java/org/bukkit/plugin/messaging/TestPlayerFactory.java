package org.bukkit.plugin.messaging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.TestPluginFactory;

import com.google.common.collect.ImmutableMap;

public class TestPlayerFactory {
    private interface MethodHandler {
        Object invoke(Object[] args);
    }
    private static final Method equals;
    private static final Method hashCode;
    private static final Method getType;
    private static final Constructor<? extends Player> playerConstructor;
    static {
        try {
            equals = Object.class.getMethod("equals", Object.class);
            hashCode = Object.class.getMethod("hashCode");
            getType = Player.class.getMethod("getType");
            playerConstructor =
                    Proxy.getProxyClass(TestPluginFactory.class.getClassLoader(), Player.class)
                    .asSubclass(Player.class)
                    .getConstructor(InvocationHandler.class);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static Player getPlayer() {
        try {
            return new Object() {
                final Player player = playerConstructor.newInstance(new InvocationHandler() {
                    Map<Method, MethodHandler> handlers = ImmutableMap.<Method, MethodHandler>builder().put(
                            equals, new MethodHandler() {
                                public Object invoke(Object[] args) {
                                    return args[0] == player;
                                }}).put(
                            getType, new MethodHandler() {
                                public Object invoke(Object[] args) {
                                    return EntityType.PLAYER;
                                }}).put(
                            hashCode, new MethodHandler() {
                                public Object invoke(Object[] args) {
                                    return System.identityHashCode(player);
                                }}).build();
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        MethodHandler handler = handlers.get(method);
                        if (handler != null) return handler.invoke(args);
                        throw new UnsupportedOperationException(method + " is unsupported");
                    }});
                }.player;
        } catch (Throwable t) {
            throw new Error(t);
        }
    }
}
