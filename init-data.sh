#!/bin/bash

# Wait for Elasticsearch to start
until curl -s http://localhost:9200 > /dev/null; do
    echo "Waiting for Elasticsearch to start..."
    sleep 5
done

echo "Elasticsearch is running, creating index..."

# Create index
curl -X PUT "http://localhost:9200/os_10_article_v1" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "os_10_article": {
      "dynamic": "strict",
      "properties": {
        "abstracts": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["abstracts_ik"]},
        "abstracts_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "author": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["author_ik"]},
        "author_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "collection": {"type": "string", "index": "not_analyzed", "store": true},
        "date": {"type": "string", "index": "not_analyzed", "store": true},
        "doi": {"type": "string", "index": "not_analyzed", "store": true},
        "end_page": {"type": "string", "index": "not_analyzed", "store": true},
        "funding": {"type": "string", "index": "not_analyzed", "store": true},
        "institution": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["institution_ik"]},
        "institution_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "issn": {"type": "string", "index": "not_analyzed", "store": true},
        "issue": {"type": "string", "index": "not_analyzed", "store": true},
        "journal": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["journal_ik"]},
        "journal_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "keyword": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["keyword_ik"]},
        "keyword_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "license": {"type": "string", "index": "not_analyzed", "store": true},
        "link": {"type": "string", "index": "not_analyzed", "store": true},
        "publisher": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["publisher_ik"]},
        "publisher_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "start_page": {"type": "string", "index": "not_analyzed", "store": true},
        "subject": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["subject_ik"]},
        "subject_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "title": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["title_ik"]},
        "title_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "type": {"type": "string", "index": "not_analyzed", "store": true},
        "uuid": {"type": "string", "index": "not_analyzed", "store": true},
        "volume": {"type": "string", "index": "not_analyzed", "store": true},
        "year": {"type": "string", "index": "not_analyzed", "store": true}
      }
    }
  },
  "settings": {
    "index": {
      "number_of_replicas": "1",
      "number_of_shards": "5"
    }
  }
}'

echo "Index created successfully, inserting sample data..."

# Insert sample data
curl -X POST "http://localhost:9200/os_10_article_v1/os_10_article" -H 'Content-Type: application/json' -d'
{
  "date": "2023-12-15",
  "funding": "National Natural Science Foundation, Project No.: 12345678",
  "issue": "04",
  "abstracts": "This study explores the application of artificial intelligence in sustainable urban development. The research investigates various methodologies including machine learning algorithms and deep neural networks to analyze urban data patterns and optimize resource allocation strategies for smart cities.",
  "year": "2023",
  "author": ["Zhang Wei", "Li Ming", "Wang Hong"],
  "subject": ["Computer Science", "Urban Planning", "Sustainable Development"],
  "link": "https://www.sciencedirect.com/science/article/pii/S123456782300001X",
  "collection": ["SCI", "EI"],
  "title": "Application of Artificial Intelligence in Sustainable Urban Development: A Comprehensive Review",
  "type": "Journal Article",
  "uuid": "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6",
  "volume": "45",
  "institution": ["School of Computer Science and Technology, Tsinghua University", "Department of Urban Planning, Peking University"],
  "license": "CC BY-NC-ND",
  "journal": "Sustainable Cities and Society",
  "issn": "2210-6707",
  "start_page": "101856",
  "publisher": "Elsevier",
  "keyword": ["Artificial Intelligence", "Machine Learning", "Sustainable Urban Development", "Smart Cities", "Data Analysis"],
  "doi": "10.1016/j.scs.2023.101856"
}'

echo "Sample data insertion completed!"

echo "Creating Science Data index..."

# Create Science Data index
curl -X PUT "http://localhost:9200/os_10_sciencedatacenter_v1" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "t_ods_las_nstl_value": {
      "dynamic": "strict",
      "properties": {
        "uuid": {"type": "string", "index": "not_analyzed", "store": true},
        "title": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["title_ik"]},
        "title_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "title-en": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["title_en_ik"]},
        "title_en_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "abstract": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["abstract_ik"]},
        "abstract_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "abstract-en": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["abstract_en_ik"]},
        "abstract_en_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "keyword": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["keyword_ik"]},
        "keyword_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "keyword-en": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["keyword_en_ik"]},
        "keyword_en_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "author": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["author_ik"]},
        "author_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "author-en": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["author_en_ik"]},
        "author_en_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "doi": {"type": "string", "index": "not_analyzed", "store": true},
        "cstr": {"type": "string", "index": "not_analyzed", "store": true},
        "pub-date": {"type": "string", "index": "not_analyzed", "store": true},
        "pub-year": {"type": "string", "index": "not_analyzed", "store": true},
        "dataset-id": {"type": "string", "index": "not_analyzed", "store": true},
        "dataset-type": {"type": "string", "index": "not_analyzed", "store": true},
        "type": {"type": "string", "index": "not_analyzed", "store": true},
        "lang": {"type": "string", "index": "not_analyzed", "store": true},
        "size": {"type": "string", "index": "not_analyzed", "store": true},
        "file-count": {"type": "string", "index": "not_analyzed", "store": true},
        "record-count": {"type": "string", "index": "not_analyzed", "store": true},
        "start-date": {"type": "string", "index": "not_analyzed", "store": true},
        "end-date": {"type": "string", "index": "not_analyzed", "store": true},
        "spatial-dimension": {"type": "string", "index": "not_analyzed", "store": true},
        "facility": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["facility_ik"]},
        "facility_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "group-name": {"type": "string", "index": "not_analyzed", "store": true},
        "provider-name": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["provider_name_ik"]},
        "provider_name_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "correspondent": {"type": "string", "index": "not_analyzed", "store": true},
        "uri": {"type": "string", "index": "not_analyzed", "store": true},
        "url": {"type": "string", "index": "not_analyzed", "store": true},
        "image-url": {"type": "string", "index": "not_analyzed", "store": true},
        "coverUrl": {"type": "string", "index": "not_analyzed", "store": true},
        "rights": {"type": "string", "index": "not_analyzed", "store": true},
        "share": {"type": "string", "index": "not_analyzed", "store": true},
        "reference": {"type": "string", "index": "not_analyzed", "store": true},
        "source-db": {"type": "string", "index": "not_analyzed", "store": true},
        "pid": {"type": "string", "index": "not_analyzed", "store": true},
        "issue": {"type": "string", "index": "not_analyzed", "store": true},
        "version": {"type": "string", "index": "not_analyzed", "store": true},
        "browse_count": {"type": "integer", "store": true},
        "download_count": {"type": "integer", "store": true},
        "insert-date": {"type": "string", "index": "not_analyzed", "store": true},
        "insert-time": {"type": "string", "index": "not_analyzed", "store": true},
        "insert-year": {"type": "string", "index": "not_analyzed", "store": true},
        "update-date": {"type": "string", "index": "not_analyzed", "store": true},
        "update-time": {"type": "string", "index": "not_analyzed", "store": true},
        "update-year": {"type": "string", "index": "not_analyzed", "store": true},
        "operator": {"type": "string", "index": "not_analyzed", "store": true},
        "source": {"type": "string", "index": "not_analyzed", "store": true},
        "institution": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["institution_ik"]},
        "institution_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"},
        "institution-en": {"type": "string", "index": "not_analyzed", "store": true, "copy_to": ["institution_en_ik"]},
        "institution_en_ik": {"type": "string", "store": true, "analyzer": "ik_max_word"}
      }
    }
  },
  "settings": {
    "index": {
      "number_of_replicas": "1",
      "number_of_shards": "5"
    }
  }
}'

echo "Science Data index created successfully, inserting sample data..."

# Insert Science Data sample data
curl -X POST "http://localhost:9200/os_10_sciencedatacenter_v1/t_ods_las_nstl_value" -H 'Content-Type: application/json' -d'
{
  "uuid": "sci-data-001",
  "title": "中国区域高分辨率地面气象驱动数据集",
  "title-en": "High Resolution Ground Meteorological Driving Dataset for China Region",
  "abstract": "本数据集包含中国区域高分辨率地面气象驱动数据，包括温度、降水、湿度、风速等气象要素，时间分辨率为逐小时，空间分辨率为0.1度。数据来源于中国气象局地面观测站网，经过质量控制和空间插值处理。",
  "abstract-en": "This dataset contains high-resolution ground meteorological driving data for China region, including temperature, precipitation, humidity, wind speed and other meteorological elements. The temporal resolution is hourly and the spatial resolution is 0.1 degree. The data is sourced from the China Meteorological Administration surface observation station network and has undergone quality control and spatial interpolation processing.",
  "keyword": ["气象数据", "地面观测", "高分辨率", "中国区域", "驱动数据"],
  "keyword-en": ["Meteorological Data", "Surface Observation", "High Resolution", "China Region", "Driving Data"],
  "author": ["张伟", "李明", "王红"],
  "author-en": ["Zhang Wei", "Li Ming", "Wang Hong"],
  "doi": "10.12345/chen.2023.001",
  "cstr": "CSTR:1234567890",
  "pub-date": "2023-06-15",
  "pub-year": "2023",
  "dataset-id": "DS-2023-001",
  "dataset-type": "地面气象数据",
  "type": "科学数据",
  "lang": "中文",
  "size": "15.6GB",
  "file-count": "365",
  "record-count": "8760",
  "start-date": "2022-01-01",
  "end-date": "2022-12-31",
  "spatial-dimension": "中国区域（73°E-135°E，18°N-54°N）",
  "facility": "中国气象局国家气象信息中心",
  "group-name": "气象数据研究组",
  "provider-name": "中国气象局",
  "correspondent": "张伟",
  "uri": "urn:uuid:sci-data-001",
  "url": "http://data.cma.cn/dataset/001",
  "image-url": "http://data.cma.cn/images/001.jpg",
  "coverUrl": "http://data.cma.cn/covers/001.jpg",
  "rights": "开放获取",
  "share": "允许共享",
  "reference": "张伟, 李明, 王红. 中国区域高分辨率地面气象驱动数据集[J]. 气象学报, 2023, 81(3): 456-470.",
  "source-db": "中国气象科学数据共享服务网",
  "pid": "hdl:12345/001",
  "issue": "3",
  "version": "1.0",
  "browse_count": 1250,
  "download_count": 356,
  "insert-date": "2023-06-20",
  "insert-time": "14:30:00",
  "insert-year": "2023",
  "update-date": "2023-07-15",
  "update-time": "09:15:00",
  "update-year": "2023",
  "operator": "admin",
  "source": "中国气象局",
  "institution": ["中国气象局国家气象信息中心"],
  "institution-en": ["National Meteorological Information Center, CMA"],
  "classification": [{"first": "地球科学", "second": "大气科学"}],
  "contributor": [{"name": "张伟", "role": "主要研究者"}],
  "funding": [{"name": "国家自然科学基金", "number": "42075001"}],
  "project": [{"name": "高分辨率气象数据产品研发", "number": "2022YFB3900000"}],
  "publisher": {"name": "中国气象局国家气象信息中心", "location": "北京"},
  "copyRight": {"holder": "中国气象局", "year": "2023"}
}'

echo "Science Data sample data insertion completed!"