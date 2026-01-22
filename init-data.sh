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