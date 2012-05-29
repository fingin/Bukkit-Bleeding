package org.bukkit.plugin.java;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final ArrayList<String> classes = new ArrayList<String>();

    public PluginClassLoader(final JavaPluginLoader loader, final URL[] urls, final ClassLoader parent) {
        super(urls, parent);

        this.loader = loader;
    }

    @Override
    public void addURL(URL url) { // Override for access level!
        super.addURL(url);
    }

    /**
     * {@inheritDoc}<br><br>
     * This particular implementation will call back to the
     * plugin loader, calling all active class loaders in
     * the order the respective plugins were enabled until
     * the specified class is found.
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = loader.getClassByName(name);
        // The result can be null in some fringe cases
        // where the class loader is still active, but
        // has been dereferenced by the plugin loader
        return result == null ? super.findClass(name) : result;
    }

    /**
     * Should only be called by the plugin loader, when this
     * class loader is still referenced.
     */
    Class<?> grabClass(String name) throws ClassNotFoundException {
        Class<?> result = super.findClass(name);
        loader.setClass(name, result);
        classes.add(name);
        return result;
    }

    /**
     * This method is only provided as a compatibility layer
     * @deprecated Use {@link #findClass(String)} to find a class globally, or {@link #grabClass(String)} to find one locally
     */
    @Deprecated
    protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        return checkGlobal ? findClass(name) : grabClass(name);
    }

    /**
     * This method is only provided as a compatibility layer
     * @deprecated Use {@link #getLoadedClasses()}
     */
    @Deprecated
    public Set<String> getClasses() {
        return new HashSet<String>(classes);
    }

    String[] getLoadedClasses() {
        return classes.toArray(new String[classes.size()]);
    }
}
