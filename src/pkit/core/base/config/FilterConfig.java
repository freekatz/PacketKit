package pkit.core.base.config;

import java.util.Date;

public class FilterConfig implements Config{
    private int id; // 程序中设置的，保存到文件，永久不变
    private String name;
    private String filter;
    private String comment;
    private Date timestamp;


    @Override
    public void Initial() {
        this.timestamp = new Date();
        this.filter = "";
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

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public Date getTimestamp() {
        return this.timestamp;
    }
}
