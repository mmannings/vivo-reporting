package edu.cornell.mannlib.vitro.webapp.dynapi;

import javax.servlet.ServletContext;

import edu.cornell.mannlib.vitro.webapp.dynapi.components.Poolable;

public interface Pool<K, C extends Poolable<K>> {

    public C get(K key);

    public void printKeys();

    public void add(String uri, C component);

    public void remove(K key);
    
    public void load(String uri);
    
    public void unload(String uri);

    public void reload();

    public void init(ServletContext ctx);

    public long obsoleteCount();

    public long count();

}
