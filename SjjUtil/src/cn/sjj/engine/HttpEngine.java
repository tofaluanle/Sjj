package cn.sjj.engine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpEngine {

	/**
	 * 使用post方式发送请求
	 * @param url
	 * @param params
	 * @return
	 */
	public InputStream doPost(String url, Map<String, String> params) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				postData.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData,
					HTTP.UTF_8);// 过时了?
			post.setEntity(entity);
			// System.out.println(post.toString());
			HttpResponse response = httpClient.execute(post);
			// 若状态码为200 ok
			if (response.getStatusLine().getStatusCode() == 200) {
				// 取出回应字串
				return response.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
