package info.mvc.experience.model;

import java.util.Map;

public class Transaction {

    private Map<String, String> valuesHolder;

    public Transaction(final Map<String, String> valuesHolder) {
        this.valuesHolder = valuesHolder;
    }

    public Map<String, String> getValuesHolder() {
        return valuesHolder;
    }
}
