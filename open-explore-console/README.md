# 📚 open-explore-console Article Management System

## 📋 Project Introduction

open-explore-console is an article management system based on Spring Boot and Elasticsearch, providing functions such as article CRUD operations, advanced search, and aggregation analysis.

## 🛠️ Technology Stack

| Technology/Framework | Version | Purpose |
|---------------------|---------|---------|
| Java | 1.8 | Development language |
| Spring Boot | 1.5.16.RELEASE | Application framework |
| Elasticsearch | 2.3.4 | Search engine and data storage |
| Jest | 2.0.3 | Elasticsearch Java client |
| Lombok | - | Simplify Java code |
| Hutool | 5.8.25 | Java tool kit |
| Fastjson | 1.2.4 | JSON processing |
| Maven | 3.6+ | Project build tool |

## 📦 Project Information

- **Project Type**: Maven project  
- **Main Language**: Java  
- **Runtime Environment Requirements**:  
  - **JDK**: 1.8 or higher version  
  - **Maven**: 3.6 or higher version  
  - **Elasticsearch**: 2.3.4  

## 🚀 Environment Setup

Before running the project, please ensure the following dependencies are installed:

| Dependency | Version Requirement | Verification Command |
|------------|---------------------|----------------------|
| JDK | ≥ 1.8 | `java -version` |
| Maven | ≥ 3.6 | `mvn -v` |
| Elasticsearch | 2.3.4 | Ensure service is running on configured IP and port |

> 💡 If using IDEA, Eclipse, or other IDEs, Maven dependencies can be automatically recognized and built during project import.

## 🔧 Building the Project

1. **Prepare the project**
   ```bash
   cd open-explore-console
   ```

2. **Build using Maven**
   ```bash
   mvn clean package -DskipTests
   ```

   > After successful build, the executable JAR package will be generated in the `target` directory.  

## ▶️ Running the Project

### Method 1: Direct start with Maven (recommended for development environment)
```bash
mvn spring-boot:run
```

### Method 2: Run the packaged JAR (recommended for production environment)
```bash
java -jar target/article-0.0.1-open-explore-console.jar
```

Default access address:
```
http://localhost:8088
```

## 🧰 Common Maven Commands

| Function | Command |
|----------|---------|
| Clean project | `mvn clean` |
| Compile project | `mvn compile` |
| Run tests | `mvn test` |
| Package executable file | `mvn package` |
| Package with tests skipped | `mvn package -DskipTests` |
| Start project | `mvn spring-boot:run` |

## 📁 Project Structure

```
open-explore-console/
├── src/
│   ├── main/
│   │   ├── java/com/os10/           # Core business code
│   │   │   ├── common/support/             # Elasticsearch support classes
│   │   │   ├── controller/                 # API controllers
│   │   │   ├── dto/                        # Data transfer objects
│   │   │   ├── entity/                     # Entity classes
│   │   │   ├── service/                    # Business service layer
│   │   │   ├── util/                       # Utility classes
│   │   │   └── Os10ArticleApplication.java  # Application entry
│   │   └── resources/                      # Configuration files
│   │       └── application.properties      # Application configuration
│   └── test/                               # Test code
├── pom.xml                                 # Maven build configuration file
└── README.md                               # Project documentation
```

## ⚙️ Configuration Instructions

The project's configuration file is located at:
```
src/main/resources/application.properties
```

Main configuration items:

```properties
# Application name
spring.application.name=open-explore-console
# Server port
server.port=8088
# Log level
logging.level.com.os10.article=debug
# Elasticsearch configuration
es.ip=10.3.11.39
es.port=9201
es.article.index_name=os_10_article_v1
es.article.index_type=os_10_article
```

> ⚠️ Please modify the Elasticsearch IP and port configuration according to your actual environment.

## 📡 API Interfaces

| Interface Path | Method | Function Description | Request Body/Parameters |
|----------------|--------|----------------------|-------------------------|
| `/article/v1/edit` | POST | Create/edit article | ArticleDO object |
| `/article/v1/list` | POST | Article list query | ArticleQuery object |
| `/article/v1/aggregations` | POST | Article aggregation query | ArticleQuery object |
| `/article/v1/getOne/{id}` | GET | Get single article | Path parameter: id |

## 📊 Core Features

### 1. Article Management
- Create and edit article information
- Auto-generate article ID (based on MD5 of title, date, and journal)
- Article duplication validation

### 2. Advanced Search
- Support for multi-field search (title, abstract, keywords, etc.)
- Support for exact phrase matching
- Support for search strategy configuration

### 3. Aggregation Analysis
- Support for multi-dimensional aggregation (discipline, journal, year, collection, etc.)
- Support for aggregation queries with filtering conditions

### 4. Pagination and Sorting
- Support for custom sorting fields and directions
- Support for pagination queries

## 🧩 Common Issues

**Q1: Error "Main class not found" when running**  
👉 Please ensure that `mvn package` has been executed correctly and a `.jar` file has been generated in the `target` directory.

**Q2: Slow dependency downloads**  
👉 You can switch to a domestic Maven mirror, such as Alibaba Cloud mirror:
```xml
<mirror>
  <id>aliyunmaven</id>
  <mirrorOf>*</mirrorOf>
  <name>Aliyun Maven</name>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

**Q3: Elasticsearch connection failure**  
👉 Please check if the Elasticsearch configuration in `application.properties` is correct, and ensure the Elasticsearch service is running.

## 🔍 Extension Notes

The project uses Jest client to interact with Elasticsearch, primarily providing Elasticsearch operation encapsulation through `EsArticleService`. Article data is stored in the index specified in the configuration and supports complex query and aggregation operations.

## 📜 Open Source License

This project follows the corresponding open-source license.
