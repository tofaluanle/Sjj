package cn.manjuu.searchproject.statics;

/**
 * 用来存放一些固定的静态变量
 * 
 * @author 宋疆疆
 * 
 */
public interface IStatics {

	// public static final String bdKey = "c3RZkv7h5Vr8wPtppwBG16rs";
	public static final String bdKey = "2q19fMGSsgubzn9HnF5pEh25";

	// String URL_SERVER = "http://172.168.1.65:50080/SearchProject/index.php";
	String URL_SERVER = "http://tofaluanle.byethost3.com/SearchProject/index.php";
	// String URL_SERVER = "http://12.1.1.1/SearchProject/index.php";

	// 百度坐标和实际距离的比例
	double SCALE = 0.000011;
	String DBSCALE = "bd:real=0.000011:1米";

	String dirName = "manjuu";

	String ACTION_INSTALL = "search_project_intent_action_INSTALL";

	int TIME_OUT = 15 * 1000;
}
