FROM elasticsearch:2.3.4

# 创建IK插件目录
RUN mkdir -p /usr/share/elasticsearch/plugins/ik

# 复制IK分词器zip文件并解压
COPY ik.zip /tmp/ik.zip
RUN unzip /tmp/ik.zip -d /usr/share/elasticsearch/plugins/ik/ && rm /tmp/ik.zip

# 创建elasticsearch用户和组（如果不存在）
RUN groupadd -r elasticsearch || true && useradd -r -g elasticsearch elasticsearch || true

# 修改elasticsearch目录权限
RUN chown -R elasticsearch:elasticsearch /usr/share/elasticsearch

# 切换到elasticsearch用户
USER elasticsearch

# 暴露端口
EXPOSE 9200 9300