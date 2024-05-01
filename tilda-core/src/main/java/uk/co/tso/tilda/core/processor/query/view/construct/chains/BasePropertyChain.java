package uk.co.tso.tilda.core.processor.query.view.construct.chains;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class BasePropertyChain implements Map<Integer, PropertyChain> {
    private final Map<Integer, PropertyChain> chains = Maps.newHashMap();

    protected void addChain(PropertyChain chain) {
        chains.put(chains.size() + 1, chain);
    }

    public void addChain(String label) {
        if (!exists(label)) {
            var chain = PropertyChain.ofLabel(label);
            addChain(chain);
        }
    }

    protected void addChain(String label, String children) {
        var chain = byLabel(label);
        if (chain != null) {
            chain.children(children);
        } else {
            chain = PropertyChain.ofLabel(label);
            chain.children(children);
            chains.put(chains.size() + 1, chain);
        }
    }

    protected void children(String children) {
        var s = children.split("\\.", 2);
        if (s.length == 0)
            return;
        if (s.length == 1){
            addChain(s[0]);
        }
        if (s.length == 2) {
            addChain(s[0], s[1]);
        }
    }

    private boolean exists(String label) {
        return chains
                .values()
                .stream()
                .anyMatch(pc -> label.equals(pc.label()));
    }

    private PropertyChain byLabel(String label) {
        return chains
                .values()
                .stream()
                .filter(pc -> label.equals(pc.label()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public int size() {
        return chains.size();
    }

    @Override
    public boolean isEmpty() {
        return chains.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return chains.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return chains.containsValue(value);
    }

    @Override
    public PropertyChain get(Object key) {
        return chains.get(key);
    }

    @Override
    public PropertyChain put(Integer key, PropertyChain value) {
        return chains.put(key, value);
    }

    @Override
    public PropertyChain remove(Object key) {
        return chains.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends PropertyChain> m) {
        chains.putAll(m);
    }

    @Override
    public void clear() {
        chains.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return chains.keySet();
    }

    @Override
    public Collection<PropertyChain> values() {
        return chains.values();
    }

    @Override
    public Set<Entry<Integer, PropertyChain>> entrySet() {
        return chains.entrySet();
    }
}
