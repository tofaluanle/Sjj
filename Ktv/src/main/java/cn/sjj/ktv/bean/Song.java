package cn.sjj.ktv.bean;

/**
 * @auther 宋疆疆
 * @since 2017/6/16.
 */
public class Song extends BaseBean {

    private String name;
    private String author;
    private String from;

    public Song(String name, String author, String from) {
        this.name = name;
        this.author = author;
        this.from = from;
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
