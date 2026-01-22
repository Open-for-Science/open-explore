package com.os10.common.support;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk.Builder;
import io.searchbox.core.*;
import io.searchbox.indices.Analyze;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.params.Parameters;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author changzhijun 20190624
 *
 */

public class ESHttpUtil {

	public static boolean checkIndex(String ip, int port, List<String> indexs) {
		try {
			/**
			 * 初始化客户端
			 */
//			HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port)
//					.multiThreaded(true).readTimeout(20000).build();
//			JestClientFactory factory = new JestClientFactory();
//			factory.setHttpClientConfig(clientConfig);
//			JestClient jestClient = factory.getObject();
			JestClient jestClient = ESClientManager.getJestClient(ip, port);
			IndicesExists exists = new IndicesExists.Builder(indexs).build();
			JestResult result = jestClient.execute(exists);
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();
			return result.isSucceeded();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static JSONObject search(String ip, int port, String index, String type, String query){
		return search(ip, port, index, type, query,false);
	}


	/**
	 *
	 * @param ip:Elasticsearch的ip
	 * @param port:Elasticsearch的端口号
	 * @param index:要操作的索引名
	 * @param type:要操作的类型名
	 * @param query:检索语句
	 * @return 返回值
	 */
	public static JSONObject search(String ip, int port, String index, String type, String query,boolean isHigh) {
		try {
			/**
			 * 初始化客户端
			 */
//			HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port)
//					.multiThreaded(true).readTimeout(600000).build();
//			JestClientFactory factory = new JestClientFactory();
//			factory.setHttpClientConfig(clientConfig);
//			JestClient jestClient = factory.getObject();
			JestClient jestClient = ESClientManager.getJestClient(ip, port);
			/**
			 * 检索
			 */
			Search.Builder builder = new Search.Builder(query).addIndex(index).addType(type);
			if(!isHigh){
				builder.setParameter("preference", "_primary_first");
			}else{
				builder.setParameter("track_total_hits",true);
			}
			JestResult result = jestClient.execute(builder.build());
			JsonObject json = result.getJsonObject();
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();
			return JSONObject.parseObject(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 *
	 * @param ip:Elasticsearch的ip
	 * @param port:Elasticsearch的端口号
	 * @param index:要操作的索引名
	 * @param type:要操作的类型名
	 * @param texts:文本
	 * @return 返回值
	 */
	public static JSONObject ik(String ip, int port, String index, String type, String... texts) {
		try {
			/**
			 * 初始化客户端
			 */
//			HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port)
//					.multiThreaded(true).readTimeout(20000).build();
//			JestClientFactory factory = new JestClientFactory();
//			factory.setHttpClientConfig(clientConfig);
//			JestClient jestClient = factory.getObject();

			JestClient jestClient = ESClientManager.getJestClient(ip, port);

			Analyze analyze = new Analyze.Builder().index(index).text(Arrays.asList(texts)).tokenizer("ik_max_word").build();
			JestResult result = jestClient.execute(analyze);
			JsonObject json = result.getJsonObject();
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();
			return JSONObject.parseObject(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static JSONArray searchScroll(String ip, int port, String index, String type, String query) {
		try {
			/**
			 * 初始化客户端
			 */
//			HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port)
//					.multiThreaded(true).readTimeout(60000).connTimeout(60000).build();
//			JestClientFactory factory = new JestClientFactory();
//			factory.setHttpClientConfig(clientConfig);
//			JestClient jestClient = factory.getObject();
			JestClient jestClient = ESClientManager.getJestClient(ip, port);
			/**
			 *
			 * 检索
			 */
			Search search = new Search.Builder(query).addIndex(index).addType(type).setParameter(Parameters.SIZE, 2000).setParameter(Parameters.SCROLL, "5m").build();
			JestResult result = jestClient.execute(search);
			JsonObject json = result.getJsonObject();

			JSONObject restJson = JSONObject.parseObject(json.toString());

			JSONObject hitsJson = restJson.getJSONObject("hits");
			Integer total = hitsJson.getInteger("total");
			JSONArray hitsArray = hitsJson.getJSONArray("hits");

			int currentTotal = 0;

			int currentSize = hitsArray.size();

			currentTotal += currentSize;
			String scrollId = restJson.getString("_scroll_id");
			SearchScroll scroll = null;
			do {
				scroll = new SearchScroll.Builder(scrollId, "5m").build();
				result = jestClient.execute(scroll);
				restJson = JSONObject.parseObject(result.getJsonObject().toString());
				hitsJson = restJson.getJSONObject("hits");
				if (CollectionUtil.isNotEmpty(hitsJson.getJSONArray("hits"))) {
					hitsArray.addAll(hitsJson.getJSONArray("hits"));
				}
				scrollId = restJson.getString("_scroll_id");
				currentSize = hitsJson.getJSONArray("hits").size();
				currentTotal += currentSize;
			} while (currentSize != 0);
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();
			System.err.println(currentTotal);
			return hitsArray;
		} catch (Exception e) {
			return null;
		}
	}

	public static JSONObject getMapping(String ip, int port, String index) {

		/**
		 * 初始化客户端
		 */
//		HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port).multiThreaded(true)
//				.readTimeout(20000).build();
//		JestClientFactory factory = new JestClientFactory();
//		factory.setHttpClientConfig(clientConfig);
//		JestClient jestClient = factory.getObject();

		JestClient jestClient = ESClientManager.getJestClient(ip, port);

		try {

			GetMapping getMapping = new GetMapping.Builder().addIndex(index).build();

			JestResult result = jestClient.execute(getMapping);
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return JSONObject.parseObject(result.getJsonObject().toString());

		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return null;
		}
	}

 	public static JSONObject deleteByUuidList(String ip, int port, String index, String type, List<String> uuidList) {

		/**
		 * 初始化客户端
		 */
//		HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port).multiThreaded(true)
//				.readTimeout(20000).build();
//		JestClientFactory factory = new JestClientFactory();
//		factory.setHttpClientConfig(clientConfig);
//		JestClient jestClient = factory.getObject();
		JestClient jestClient = ESClientManager.getJestClient(ip, port);
		/**
		 * 加载的数据
		 */
		Builder bulk = new Builder().defaultIndex(index).defaultType(type).refresh(true);
		for (int i = 0; i < uuidList.size(); i++) {
			bulk.addAction(new Delete.Builder(uuidList.get(i)).build());
		}
		try {
			JestResult result = jestClient.execute(bulk.build());
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return JSONObject.parseObject(result.getJsonObject().toString());

		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return null;
		}

	}
	/**
	 *
	 * @param ip:Elasticsearch的ip
	 * @param port:Elasticsearch的端口号
	 * @param index:要操作的索引名
	 * @param type:要操作的类型名
	 * @param list:索引数据列表
	 * @return 返回值
	 */
	public static JSONObject index(String ip, int port, String index, String type, List<JSONObject> list) {

		/**
		 * 初始化客户端
		 */
//		HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port).multiThreaded(true)
//				.readTimeout(40000).build();
//		JestClientFactory factory = new JestClientFactory();
//		factory.setHttpClientConfig(clientConfig);
//		JestClient jestClient = factory.getObject();

		JestClient jestClient = ESClientManager.getJestClient(ip, port);
		/**
		 * 加载的数据
		 */
		Builder bulk = new Builder().defaultIndex(index).defaultType(type).refresh(true);
		for (int i = 0; i < list.size(); i++) {
			JSONObject elem = list.get(i);
			String uuid = null;
			Index idx = null;
			if (elem.containsKey("uuid")) {
				uuid = elem.getString("uuid");
				idx = new Index.Builder(elem).id(uuid).build();
			} else {
				idx = new Index.Builder(elem).build();
			}
			bulk.addAction(idx);
		}
		try {
			JestResult result = jestClient.execute(bulk.build());

//			/**
//			 * 释放资源
//			 */
//			jestClient.shutdownClient();

			return JSONObject.parseObject(result.getJsonObject().toString());

		} catch (Exception e) {
			e.printStackTrace();
//			/**
//			 * 释放资源
//			 */
//			jestClient.shutdownClient();

			return null;
		}

	}

	public static JSONObject indexNoUUID(String ip, int port, String index, String type, List<JSONObject> list) {

		/**
		 * 初始化客户端
		 */
//		HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port).multiThreaded(true)
//				.readTimeout(20000).build();
//		JestClientFactory factory = new JestClientFactory();
//		factory.setHttpClientConfig(clientConfig);
//		JestClient jestClient = factory.getObject();

		JestClient jestClient = ESClientManager.getJestClient(ip, port);
		/**
		 * 加载的数据
		 */
		Builder bulk = new Builder().defaultIndex(index).defaultType(type).refresh(true);
		for (int i = 0; i < list.size(); i++) {
			JSONObject elem = list.get(i);
			String uuid = null;
			Index idx = null;
			idx = new Index.Builder(elem).build();
			bulk.addAction(idx);
		}
		try {
			JestResult result = jestClient.execute(bulk.build());

			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return JSONObject.parseObject(result.getJsonObject().toString());

		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return null;
		}

	}

	/**
	 *
	 * @param ip:Elasticsearch的ip
	 * @param port:Elasticsearch的端口号
	 * @param index:要操作的索引名
	 * @param type:要操作的类型名
	 * @param list:索引数据列表
	 * @return 返回值
	 */
	public static JSONObject update(String ip, int port, String index, String type, List<JSONObject> list) {

		/**
		 * 初始化客户端
		 */
//		HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port).multiThreaded(true)
//				.readTimeout(20000).build();
//		JestClientFactory factory = new JestClientFactory();
//		factory.setHttpClientConfig(clientConfig);
//		JestClient jestClient = factory.getObject();

		JestClient jestClient = ESClientManager.getJestClient(ip, port);
		/**
		 * 加载的数据
		 */
		Builder bulk = new Builder().defaultIndex(index).defaultType(type).refresh(true);
		for (int i = 0; i < list.size(); i++) {
			JSONObject elem = list.get(i);
			String uuid = null;
			Update idx = null;
			if (elem.containsKey("uuid")) {
				uuid = elem.getString("uuid");
				JSONObject doc = new JSONObject();
				doc.put("doc",elem);
				idx = new Update.Builder(doc).id(uuid).build();
			} else {
				idx = new Update.Builder(elem).build();
			}
			bulk.addAction(idx);
		}
		try {
			JestResult result = jestClient.execute(bulk.build());

			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return JSONObject.parseObject(result.getJsonObject().toString());

		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return null;
		}

	}

	public static JSONObject updateById(String ip, int port, String index, String type, String _id,JSONObject obj) {

		/**
		 * 初始化客户端
		 */
//		HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port).multiThreaded(true)
//				.readTimeout(20000).build();
//		JestClientFactory factory = new JestClientFactory();
//		factory.setHttpClientConfig(clientConfig);
//		JestClient jestClient = factory.getObject();

		JestClient jestClient = ESClientManager.getJestClient(ip, port);
		/**
		 * 加载的数据
		 */
		Builder bulk = new Builder().defaultIndex(index).defaultType(type).refresh(true);
		Update idx = null;
		JSONObject doc = new JSONObject();
		doc.put("doc",obj);
		idx = new Update.Builder(doc).id(_id).build();
		bulk.addAction(idx);
		try {
			JestResult result = jestClient.execute(bulk.build());

			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return JSONObject.parseObject(result.getJsonObject().toString());

		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return null;
		}

	}

	public static JSONObject updateByIds(String ip, int port, String index, String type, List<String> _ids,List<JSONObject> objs) {

		/**
		 * 初始化客户端
		 */
//		HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port).multiThreaded(true)
//				.readTimeout(20000).build();
//		JestClientFactory factory = new JestClientFactory();
//		factory.setHttpClientConfig(clientConfig);
//		JestClient jestClient = factory.getObject();
		JestClient jestClient = ESClientManager.getJestClient(ip, port);
		/**
		 * 加载的数据
		 */
		Builder bulk = new Builder().defaultIndex(index).defaultType(type).refresh(true);
		for (int i = 0; i < _ids.size(); i++){
			JSONObject doc = new JSONObject();
			doc.put("doc",objs.get(i));
			Update idx = new Update.Builder(doc).id(_ids.get(i)).build();
			bulk.addAction(idx);
		}
		try {
			JestResult result = jestClient.execute(bulk.build());

			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return JSONObject.parseObject(result.getJsonObject().toString());

		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return null;
		}

	}

	/**
	 * @param ip:Elasticsearch的ip
	 * @param port:Elasticsearch的端口号
	 * @param index:要操作的索引名
	 * @param type:要操作的类型名
	 * @param uuids:要删除数据的_id
	 * @return 返回值
	 */
	public static JSONObject delete(String ip, int port, String index, String type, List<String> uuids) {
		try {
			/**
			 * 初始化客户端
			 */
//			HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port)
//					.multiThreaded(true).readTimeout(20000).build();
//			JestClientFactory factory = new JestClientFactory();
//			factory.setHttpClientConfig(clientConfig);
//			JestClient jestClient = factory.getObject();
			JestClient jestClient = ESClientManager.getJestClient(ip, port);

			/**
			 * 检索
			 */
			Builder bulk = new Bulk.Builder().defaultIndex(index).defaultType(type).refresh(true);
			for (int i = 0; i < uuids.size(); i++) {
				String uuid = uuids.get(i);
				Delete build = new Delete.Builder(uuid).build();
				bulk.addAction(build);
			}
			JestResult result = jestClient
					.execute(bulk.build());

			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return JSONObject.parseObject(result.getJsonObject().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 *
	 * @param ip:Elasticsearch的ip
	 * @param port:Elasticsearch的端口号
	 * @param index:要操作的索引名
	 * @param type:要操作的类型名
	 * @param _id:要删除数据的_id
	 * @return 返回值
	 */
	public static JSONObject delete(String ip, int port, String index, String type, String _id) {
		try {
			/**
			 * 初始化客户端
			 */
//			HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port)
//					.multiThreaded(true).readTimeout(20000).build();
//			JestClientFactory factory = new JestClientFactory();
//			factory.setHttpClientConfig(clientConfig);
//			JestClient jestClient = factory.getObject();
			JestClient jestClient = ESClientManager.getJestClient(ip, port);

			/**
			 * 检索
			 */
			JestResult result = jestClient
					.execute(new Delete.Builder(_id).index(index).type(type).refresh(true).build());

			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();

			return JSONObject.parseObject(result.getJsonObject().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONObject getFirstScroll(String ip, int port, String index, String type, String query) {
		try {
			/**
			 * 初始化客户端
			 */
//			HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port)
//					.multiThreaded(true).readTimeout(60000).connTimeout(60000).build();
//			JestClientFactory factory = new JestClientFactory();
//			factory.setHttpClientConfig(clientConfig);
//			JestClient jestClient = factory.getObject();
			JestClient jestClient = ESClientManager.getJestClient(ip, port);
			/**
			 * 检索
			 */
			Search search = new Search.Builder(query).addIndex(index).addType(type).setParameter(Parameters.SIZE, 20).setParameter(Parameters.SCROLL, "30m").build();
			JestResult result = jestClient.execute(search);
			JsonObject json = result.getJsonObject();

			JSONObject restJson = JSONObject.parseObject(json.toString());

			JSONObject hitsJson = restJson.getJSONObject("hits");

			JSONArray hitsArray = hitsJson.getJSONArray("hits");

			int currentTotal = 0;

			int currentSize = hitsArray.size();

			currentTotal += currentSize;
			String scrollId = restJson.getString("_scroll_id");
			restJson.put("scrollId",scrollId);
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();
			System.err.println("first___currentTotal："+currentTotal);
			return restJson;
		} catch (Exception e) {
			return null;
		}
	}

	public static JSONObject getScrollNext(String ip, int port,String scrollId) {
		try {
			/**
			 * 初始化客户端
			 */
//			HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://" + ip + ":" + port)
//					.multiThreaded(true).readTimeout(60000).connTimeout(60000).build();
//			JestClientFactory factory = new JestClientFactory();
//			factory.setHttpClientConfig(clientConfig);
//			JestClient jestClient = factory.getObject();
			JestClient jestClient = ESClientManager.getJestClient(ip, port);
			/**
			 * 检索
			 */
			SearchScroll scroll = new SearchScroll.Builder(scrollId, "5m").build();
			JestResult result = jestClient.execute(scroll);
			JSONObject restJson = JSONObject.parseObject(result.getJsonObject().toString());
			//JSONObject hitsJson = restJson.getJSONObject("hits");
			//JSONArray jsonArray = hitsJson.getJSONArray("hits");
			scrollId = restJson.getString("_scroll_id");
			restJson.put("scrollId",scrollId);
			//int currentSize = jsonArray.size();
			/**
			 * 释放资源
			 */
			//			jestClient.shutdownClient();
			//System.err.println("next___currentTotal："+currentSize);
			return restJson;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String arr2str(JSONArray arr){
		String str = "";
		try {
			JSONArray jsonArray = arr.getJSONArray(0);
			str = arr2str(jsonArray);
		}catch (Exception e){
			str = arr.getString(0);
		}
		return str;
	}

	public static JSONArray putArr(Object value,int num){
		JSONArray list = new JSONArray();
		if(num > 0){
			for(int i =0; i < num; i++){
				if(i + 1 == num){
					list.add(value);
				}else {
					JSONArray alist = new JSONArray();
					list.add(alist);
				}
			}
		}else{
			list.add(value);
		}
		return list;
	}

	/*
	 * 转小写去掉特殊字符
	 */
	public static String filter(String character) {
		character = character.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "");
		return character.toLowerCase();
	}

	public static void main(String[] args) {
	}

}
