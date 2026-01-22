# Elasticsearch Index Creation and Document Operation Guide

This document provides detailed instructions for creating academic article indexes and inserting documents in Elasticsearch.

## 1. Create Index

The following command is used to create an Elasticsearch index named `os_10_article_v1`, including index mapping definitions and settings.

```json
PUT /os_10_article_v1
{
  "aliases": {},
  "mappings": {
    "os_10_article": {
      "dynamic": "strict",  // Strict mode, does not allow adding fields not defined in the mapping
      "properties": {        // Define all fields of the document and their mappings
        // Abstract field - Original content, not analyzed
        "abstracts": {
          "type": "string",      // Field type
          "index": "not_analyzed", // Do not perform tokenization indexing
          "store": true,         // Store field values
          "copy_to": [           // Copy values to other fields for analysis
            "abstracts_ik"
          ]
        },
        // Abstract field - Use IK tokenizer for full-text search
        "abstracts_ik": {
          "type": "string",
          "store": true,
          "analyzer": "ik_max_word" // Use IK maximum tokenizer
        },
        
        // Author field - Original content, not analyzed
        "author": {
          "type": "string",
          "index": "not_analyzed",
          "store": true,
          "copy_to": [
            "author_ik"
          ]
        },
        // Author field - Use IK tokenizer for author name search
        "author_ik": {
          "type": "string",
          "store": true,
          "analyzer": "ik_max_word"
        },
        
        // Collection information field
        "collection": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Publication date field
        "date": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // DOI identifier field
        "doi": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // End page field
        "end_page": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Funding information field
        "funding": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Institution field - Original content, not analyzed
        "institution": {
          "type": "string",
          "index": "not_analyzed",
          "store": true,
          "copy_to": [
            "institution_ik"
          ]
        },
        // Institution field - Use IK tokenizer for institution name search
        "institution_ik": {
          "type": "string",
          "store": true,
          "analyzer": "ik_max_word"
        },
        
        // ISSN journal number field
        "issn": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Journal issue field
        "issue": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Journal name field - Original content, not analyzed
        "journal": {
          "type": "string",
          "index": "not_analyzed",
          "store": true,
          "copy_to": [
            "journal_ik"
          ]
        },
        // Journal name field - Use IK tokenizer for journal name search
        "journal_ik": {
          "type": "string",
          "store": true,
          "analyzer": "ik_max_word"
        },
        
        // Keyword field - Original content, not analyzed
        "keyword": {
          "type": "string",
          "index": "not_analyzed",
          "store": true,
          "copy_to": [
            "keyword_ik"
          ]
        },
        // Keyword field - Use IK tokenizer for keyword search
        "keyword_ik": {
          "type": "string",
          "store": true,
          "analyzer": "ik_max_word"
        },
        
        // License type field
        "license": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Article link field
        "link": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Publisher field - Original content, not analyzed
        "publisher": {
          "type": "string",
          "index": "not_analyzed",
          "store": true,
          "copy_to": [
            "publisher_ik"
          ]
        },
        // Publisher field - Use IK tokenizer for publisher search
        "publisher_ik": {
          "type": "string",
          "store": true,
          "analyzer": "ik_max_word"
        },
        
        // Start page field
        "start_page": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Subject classification field - Original content, not analyzed
        "subject": {
          "type": "string",
          "index": "not_analyzed",
          "store": true,
          "copy_to": [
            "subject_ik"
          ]
        },
        // Subject classification field - Use IK tokenizer for subject search
        "subject_ik": {
          "type": "string",
          "store": true,
          "analyzer": "ik_max_word"
        },
        
        // Article title field - Original content, not analyzed
        "title": {
          "type": "string",
          "index": "not_analyzed",
          "store": true,
          "copy_to": [
            "title_ik"
          ]
        },
        // Article title field - Use IK tokenizer for title search
        "title_ik": {
          "type": "string",
          "store": true,
          "analyzer": "ik_max_word"
        },
        
        // Article type field
        "type": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Unique identifier field
        "uuid": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Journal volume field
        "volume": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        },
        
        // Publication year field
        "year": {
          "type": "string",
          "index": "not_analyzed",
          "store": true
        }
      }
    }
  },
  "settings": {      // Index-level configuration settings
    "index": {
      "number_of_replicas": "1",  // Number of replicas, improves availability and read performance
      "number_of_shards": "5",    // Number of shards, controls data distribution and parallel processing capability
    }
  }
}
```

## 2. Insert Document

The following command is used to insert an article document into the created `os_10_article_v1` index.

```json
POST /os_10_article_v1/_doc/os_10_article
{
  "date": "2023-12-15",            // Article publication date
  "funding": "National Natural Science Foundation of China, Grant No. 12345678",       // Article funding information
  "issue": "04",                  // Journal issue number
  "abstracts": "This study investigates the applications of artificial intelligence in sustainable urban development. The research explores various methodologies including machine learning algorithms and deep neural networks to analyze urban data patterns and optimize resource allocation strategies for smart cities.",   // Article abstract
  "year": "2023",                 // Publication year
  "author": [                      // Author list (array type)
    "Zhang Wei",
    "Li Ming",
    "Wang Hong"
  ],
  "subject": [                     // Subject classification list (array type)
    "Computer Science",
    "Urban Planning",
    "Sustainable Development"
  ],
  "link": "https://www.sciencedirect.com/science/article/pii/S123456782300001X",        // Article link
  "collection": [                  // Collection information list (array type)
    "SCI",
    "EI"
  ],
  "title": "Artificial Intelligence Applications in Sustainable Urban Development: A Comprehensive Review",       // Article title
  "type": "Journal Article",              // Article type
  "uuid": "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6", // Article unique identifier
  "volume": "45",                 // Journal volume number
  "institution": [                 // Author institution list (array type)
    "School of Computer Science and Technology, Tsinghua University",
    "Department of Urban Planning, Peking University"
  ],
  "license": "CC BY-NC-ND",             // License type
  "journal": "Journal of Sustainable Cities and Society",              // Journal name
  "issn": "2210-6707",            // ISSN number
  "start_page": "101856",              // Start page number
  "publisher": "Elsevier",          // Publisher
  "keyword": [                     // Keyword list (array type)
    "Artificial Intelligence",
    "Machine Learning",
    "Sustainable Urban Development",
    "Smart Cities",
    "Data Analytics"
  ],
  "doi": "10.1016/j.scs.2023.101856"           // DOI identifier
}
```

## 3. Usage Instructions

1. **Index Mapping Design Description**:
   - This index design adopts a dual-field strategy: one field for exact matching (`not_analyzed`), and another field using IK tokenizer for full-text search
   - The `copy_to` parameter copies original field values to corresponding IK tokenized fields to avoid data duplication
   - Setting `store: true` allows direct retrieval of field values during queries, improving performance

2. **Index Settings Description**:
   - The number of shards is set to 5, suitable for medium to large datasets, which can improve parallel processing capabilities
   - The number of replicas is set to 1, providing data redundancy and high availability

3. **Usage Recommendations**:
   - For exact matching queries (such as exact search by author or journal name), use the original non-analyzed fields
   - For full-text search (such as fuzzy matching of titles, abstracts, and keywords), use the tokenized fields with the `_ik` suffix
   - UUID can be used as a unique identifier for documents, facilitating subsequent update or delete operations

4. **Notes**:
   - Once an index is created, the number of shards cannot be changed; please set it reasonably based on data volume estimates
   - This example uses the `string` type, which may need to be replaced with `text` or `keyword` types in newer versions of Elasticsearch
   - Fields with array values can store multiple values, suitable for representing multiple authors, keywords, etc.