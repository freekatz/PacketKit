package pkit.core.service.config;

import java.util.Date;

public class FilterConfig implements Config{
    private int id;
    private String name;
    private String filter;
    private String comment;
    private Date date;


    @Override
    public void Initial() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getFilter() {
        return this.filter;
    }

    public String getComment() {
        return this.comment;
    }

    public Date getDate() {
        return date;
    }
}
