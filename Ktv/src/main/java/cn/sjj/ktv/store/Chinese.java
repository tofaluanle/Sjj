package cn.sjj.ktv.store;

import java.util.ArrayList;
import java.util.List;

import cn.sjj.ktv.bean.Song;

/**
 * @auther 宋疆疆
 * @since 2017/6/16.
 */
public class Chinese {

    public static List<Song> songs;

    static {
        songs = new ArrayList<>();
        songs.add(new Song("", "", ""));
        songs.add(new Song("灰姑娘", "郑钧", ""));
        songs.add(new Song("酒醉的探戈2001", "动力火车", ""));
        songs.add(new Song("突然好想你", "五月天", ""));
        songs.add(new Song("像梦一样自由", "汪峰", ""));
        songs.add(new Song("同桌的你", "老狼", ""));
        songs.add(new Song("最初的梦想", "范玮琪", ""));
        songs.add(new Song("红色高跟鞋", "蔡健雅", ""));
        songs.add(new Song("阿兰", "李志", ""));
        songs.add(new Song("看上她", "黎明", ""));
        songs.add(new Song("画", "G.E.M.邓紫棋", ""));
        songs.add(new Song("a.i.n.y.", "G.E.M.邓紫棋", ""));
        songs.add(new Song("讲真的", "曾惜", ""));
        songs.add(new Song("那些花儿", "朴树", ""));
        songs.add(new Song("在他乡", "水木年华", ""));
        songs.add(new Song("突然好想你", "许飞", ""));
        songs.add(new Song("是否我真的一无所有", "王杰", ""));
        songs.add(new Song("像雾像雨又像风", "梁雁翎", ""));
        songs.add(new Song("你怎么舍得我难过", "黄品源", ""));
        songs.add(new Song("特别的爱给特别的你", "伍思凯", ""));
        songs.add(new Song("一生所爱", "卢冠廷", ""));
    }
}
