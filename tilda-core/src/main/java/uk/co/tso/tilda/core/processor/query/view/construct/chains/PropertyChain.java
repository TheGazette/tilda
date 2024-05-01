package uk.co.tso.tilda.core.processor.query.view.construct.chains;

public final class PropertyChain extends BasePropertyChain {
    private final String label;

    private PropertyChain(String label) {
        this.label = label;
    }

    public static PropertyChain ofLabel(String label) {
        if (label == null || label.isEmpty())
            throw new RuntimeException();
        return new PropertyChain(label);
    }

    public String label() {
        return label;
    }




}
