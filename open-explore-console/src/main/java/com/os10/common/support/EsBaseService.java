package com.os10.common.support;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.os10.util.BaseMessage;
import lombok.Data;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class EsBaseService {
    Logger logger = LoggerFactory.getLogger(EsBaseService.class);
    private String ip;
    private int port;
    private String index;
    private String type;

    public ESResult query(Consumer<SearchSourceBuilder> function){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        SearchSourceBuilder builder = new SearchSourceBuilder();
        function.accept(builder);
        JSONObject search = ESHttpUtil.search(this.ip, this.port, this.index, this.type, builder.toString());
        return new ESResult(this.getHits(search),this.getAggs(search),this.getTotal(search),"",null);
    }

    public ESResult query(Function<SearchSourceBuilder,SearchSourceBuilder> function){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        SearchSourceBuilder apply = function.apply(new SearchSourceBuilder());
        if(apply != null){
            JSONObject search = ESHttpUtil.search(this.ip, this.port, this.index, this.type, apply.toString());
            return new ESResult(this.getHits(search),this.getAggs(search),this.getTotal(search),"",null);
        }
        return null;
    }

    public void queryScroll(Function<SearchSourceBuilder,SearchSourceBuilder> function, Consumer<ESResult> consumer){
        SearchSourceBuilder apply = function.apply(new SearchSourceBuilder());
        if(apply != null){
            ESResult esResult1 = this.queryScroll(function);
            String scrollId = esResult1.getScrollId();
            consumer.accept(esResult1);
            while (StrUtil.isNotEmpty(scrollId)){
                ESResult esResult = this.queryScroll(scrollId);
                consumer.accept(esResult);
                if(esResult.getHits().size() > 0){
                    scrollId = esResult.getScrollId();
                }else{
                    scrollId = "";
                }
            }
        }
    }

    public ESResult queryScroll(Function<SearchSourceBuilder,SearchSourceBuilder> function){
        SearchSourceBuilder apply = function.apply(new SearchSourceBuilder());
        if(apply != null){
            return this.queryScroll(apply.toString(),true);
        }
        return null;
    }

    public ESResult queryScroll(String scrollId){
        if(StrUtil.isEmpty(scrollId)){
            return null;
        }
        return this.queryScroll(scrollId,false);
    }

    private ESResult queryScroll(String scrollId,boolean isFirst){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        if(isFirst){
            JSONObject search = ESHttpUtil.getFirstScroll(this.ip, this.port, this.index, this.type, scrollId);
            return new ESResult(this.getHits(search),this.getAggs(search),this.getTotal(search),this.getScrollId(search),null);
        }else{
            JSONObject search = ESHttpUtil.getScrollNext(this.ip, this.port, scrollId);
            return new ESResult(this.getHits(search),this.getAggs(search),this.getTotal(search),this.getScrollId(search),null);
        }
    }
    public ESResult queryEs(String query){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        JSONObject search = ESHttpUtil.search(this.ip, this.port, this.index, this.type, query);
        return new ESResult(this.getHits(search),this.getAggs(search),this.getTotal(search),this.getScrollId(search),null);
    }

    public JSONObject query(String query){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        JSONObject search = ESHttpUtil.search(this.ip, this.port, this.index, this.type, query);
        return search;
    }

    public JSONObject getMapping(){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        JSONObject search = ESHttpUtil.getMapping(this.ip, this.port, this.index);
        return search;
    }

    public JSONObject queryById(String uuid){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return new JSONObject();
        }
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.idsQuery(this.type).ids(uuid));
        JSONObject search = ESHttpUtil.search(this.ip, this.port, this.index, this.type, builder.toString());
        JSONArray hits = this.getHits(search);
        if(hits.size() > 0){
            return hits.getJSONObject(0).getJSONObject("_source");
        }
        return null;
    }

    public JSONArray searchScroll(String query) {
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        JSONArray search = ESHttpUtil.searchScroll(this.ip, this.port, this.index, this.type, query);
        return search;
    }

    public JSONObject index(List<JSONObject> list){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        return ESHttpUtil.index(this.ip,this.port,this.index,this.type,list);
    }

    public JSONObject update(List<JSONObject> list){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        return ESHttpUtil.update(this.ip,this.port,this.index,this.type,list);
    }

    public JSONObject updateById(String _id,JSONObject obj){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        return ESHttpUtil.updateById(this.ip,this.port,this.index,this.type,_id,obj);
    }
    public JSONObject delete(String _id){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        return ESHttpUtil.delete(this.ip,this.port,this.index,this.type,_id);
    }
    public JSONObject delete(List<String> uuids){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        return ESHttpUtil.delete(this.ip,this.port,this.index,this.type,uuids);
    }
    public JSONObject ik(String text){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        return ESHttpUtil.ik(this.ip,this.port,this.index,this.type,text);
    }

    public JSONObject getFirstScroll(String query){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        return ESHttpUtil.getFirstScroll(this.ip,this.port,this.index,this.type,query);
    }

    public JSONObject getNextScroll(String scrollId){
        if(StrUtil.hasEmpty(this.ip,this.port + "",this.index,this.type)){
            return null;
        }
        return ESHttpUtil.getScrollNext(this.ip,this.port,scrollId);
    }

    public JSONArray getHits(JSONObject obj){
        try {
            return obj.getJSONObject("hits").getJSONArray("hits");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONArray();
    }
    public long getTotal(JSONObject obj){
        try {
            return obj.getJSONObject("hits").getLongValue("total");
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0L;
    }

    public JSONObject getAggs(JSONObject obj){
        try {
            return obj.getJSONObject("aggregations");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONObject();
    }
    public String getScrollId(JSONObject obj){
        try {
            return obj.getString("scrollId");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getLastId(ESResult esResult){
        try {
            return esResult.getHits().getJSONObject(esResult.getHits().size() - 1).getString("_id");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public BaseMessage<String> parseError(JSONObject jsonObject){
        Boolean errors = jsonObject.getBoolean("errors");
        if(errors == null){
            JSONObject error = jsonObject.getJSONObject("error");
            logger.error(error.getString("reason"));
            return BaseMessage.error(-1,"修改失败",error.getString("reason"));
        }else if(errors){
            logger.error(jsonObject.getJSONArray("items").toJSONString());
            return BaseMessage.error(-1,"修改失败",jsonObject.getJSONArray("items"));
        }else{
            return BaseMessage.success("修改成功");
        }
    }

    public List<JSONObject> getJsonList(JSONObject hit){
        if (CollectionUtil.isEmpty(hit)) {
            return Collections.emptyList();
        }
        JSONArray hits = hit.getJSONArray("hits");
        if (CollectionUtil.isNotEmpty(hits)) {
            return hits.stream().map(e -> ((JSONObject) e).getJSONObject("_source")).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<JSONObject> getJsonList(JSONArray hits){
        if (CollectionUtil.isNotEmpty(hits)) {
            return hits.stream().map(e -> ((JSONObject) e).getJSONObject("_source")).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<JSONObject> getJsonListInIndex(JSONArray hits){
        if (CollectionUtil.isNotEmpty(hits)) {
            return hits.stream().map(e -> {
                JSONObject dataJson = ((JSONObject) e);
                JSONObject source = dataJson.getJSONObject("_source");
                source.put("_index", dataJson.getString("_index"));
                source.put("_type", dataJson.getString("_type"));
                return source;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
