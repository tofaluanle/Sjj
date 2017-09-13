package cn.sjj.ktv.store;

import java.util.ArrayList;
import java.util.List;

import cn.sjj.ktv.bean.Song;

/**
 * @auther 宋疆疆
 * @since 2017/6/16.
 */
public class English {

    public static List<Song> songs;

    static {
        songs = new ArrayList<>();
        songs.add(new Song("", "", ""));
        songs.add(new Song("Five Hundred Miles", "Justin Timberlake", "500英里", "Justin Timberlake", "Carey Mulligan", "Stark Sands"));
        songs.add(new Song("What Is A Life", "Youth Group", "绯闻女孩S02E10"));
        songs.add(new Song("The Usual Chords", "Slow Runner", "无耻之徒"));
        songs.add(new Song("Only Time", "Enya", ""));
        songs.add(new Song("Teenage Life", "Daz Sampson", ""));
        songs.add(new Song("Scatman", "Scatman John", "约翰.保罗.拉尔金"));
        songs.add(new Song("Unity", "TheFatRat", ""));
        songs.add(new Song("Monody", "TheFatRat", "Laura Brehm"));
        songs.add(new Song("Encore une Fois", "Hélène Ségara", ""));
        songs.add(new Song("The Sound Of Silence", "Simon", ""));
        songs.add(new Song("Viva La Vida", "Coldplay", ""));
        songs.add(new Song("Happy Together", "The Turtles", "乌龟合唱团"));
        songs.add(new Song("Scarborough Fair", "", "斯卡布罗集市"));
        songs.add(new Song("Hotel California", "Eagles", "加州旅馆"));
        songs.add(new Song("Seasons In The Sun", "Westlife", ""));
        songs.add(new Song("Free", "Lenka", ""));
    }
}
