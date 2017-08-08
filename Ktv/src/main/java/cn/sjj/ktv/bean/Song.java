package cn.sjj.ktv.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @auther 宋疆疆
 * @since 2017/6/16.
 */
public class Song extends BaseBean {

    private String       name;
    private String       author;
    private String       from;
    private List<String> tags;

    public Song(String name, String author, String from) {
        this.name = name;
        this.author = author;
        this.from = from;
        tags = new ArrayList<>();
    }

    public Song(String name, String author, String from, String... tags) {
        this.name = name;
        this.author = author;
        this.from = from;
        this.tags = Arrays.asList(tags);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
