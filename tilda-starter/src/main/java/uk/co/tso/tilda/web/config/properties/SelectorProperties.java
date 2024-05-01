package uk.co.tso.tilda.web.config.properties;

import uk.co.tso.tilda.core.util.Optionals;

import java.util.Optional;

public class SelectorProperties {
    private String parent;
    private String select;
    private String where;
    private String orderBy;
    private String filter;
    private String sort;

    public Optional<String> findParent() {
        return Optionals.ofString(parent);
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Optional<String> findSelect() {
        return Optionals.ofString(select);
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public Optional<String> findWhere() {
        return  Optionals.ofString(where);
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public Optional<String> findOrderBy() {
        return Optionals.ofString(orderBy);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Optional<String> findFilter() {
        return Optionals.ofString(filter);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Optional<String> findSort() {
        return Optionals.ofString(sort);
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
