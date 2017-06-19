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
        songs.add(new Song("Scarborough Fair", "2", "斯卡布罗集市"));
        songs.add(new Song("Hotel California", "加州旅馆", "Eagles"));
        songs.add(new Song("Seasons In The Sun", "Westlife", ""));
        songs.add(new Song("Free", "Lenka", ""));
        songs.add(new Song("", "", ""));
    }
}
