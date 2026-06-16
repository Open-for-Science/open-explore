# Open Explore Academic Article & Science Data Management System

## 📋 Project Overview

Open Explore is a Docker-based full-stack academic article and science data management system that includes:
- **Frontend**: Vue.js + Element UI user interface
- **Backend**: Spring Boot + Elasticsearch API services
- **Search Engine**: Elasticsearch 2.3.4 + IK Chinese word segmentation plugin
- **Features**:
  - Academic article search and management
  - Science data search and management (New)
  - Multi-dimensional aggregation filtering
  - Responsive design for multiple devices

## 🏗️ System Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Vue Frontend  │    │  Spring Boot    │    │  Elasticsearch  │
│   (Port: 80)    │───▶│   Backend       │───▶│   + IK Plugin   │
│                 │    │  (Port: 8088)   │    │  (Port: 9200)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
        │                       │                       │
        ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ Article Search  │    │ Science Data    │    │   Indexes:      │
│ Science Data    │    │ API Services    │    │ - article_v1    │
│ Detail Pages    │    │ Article API     │    │ - sciencedata_v1│
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Quick Start

### Prerequisites

Ensure your system has installed:
- Docker (version 20.0+)
- Docker Compose (version 1.29+)

### 1. Clone the Project

```bash
git clone <repository-url>
cd open-explore
```

### 2. Build and Start All Services

#### Option 1: Using Deployment Scripts (Recommended)

**Linux/macOS:**
```bash
chmod +x deploy.sh
./deploy.sh
```

**Windows:**
```cmd
deploy.bat
```

#### Option 2: Manual Docker Compose Execution

```bash
# Build and start all services
docker-compose up --build -d

# Check service status
docker-compose ps

# View service logs
docker-compose logs -f
```

### 3. Initialize Data

After all services are started (approximately 30 seconds), run the initialization script:

**Linux/macOS:**
```bash
chmod +x init-data.sh
./init-data.sh
```

**Windows:**
```cmd
init-data.bat
```

### 4. Access the Application

- **Frontend Interface**: http://localhost
- **Backend API**: http://localhost:8088
- **Elasticsearch**: http://localhost:9200

## 🔧 Docker Service Details

### Service Configuration

| Service Name | Port Mapping | Description |
|-------------|-------------|-------------|
| `open-explore-front` | 80:80 | Vue.js frontend application |
| `open-explore-console` | 8088:8088 | Spring Boot backend service |
| `elasticsearch` | 9200:9200, 9300:9300 | Elasticsearch + IK word segmentation plugin |

### Data Persistence

- Elasticsearch data is stored in Docker volume `es_data`
- Data persists after container restarts

## 📊 Data Initialization Details

The initialization script will automatically:

1. **Create Elasticsearch Index** `os_10_article_v1` with the following fields:
   - Title, abstracts, author
   - Journal, keywords, subject
   - DOI, ISSN, publication date and other metadata
   - Search fields supporting IK Chinese word segmentation

2. **Create Elasticsearch Index** `os_10_sciencedatacenter_v1` with the following fields:
   - Title, abstracts, keywords
   - Author, DOI, CSTR
   - Dataset type, classification
   - Publication date, spatial dimension
   - Provider, facility information
   - Search fields supporting IK Chinese word segmentation

3. **Insert Sample Data**: Contains an academic article about the application of artificial intelligence in sustainable urban development

## 🛠️ Development and Debugging

### View Service Logs

```bash
# View all service logs
docker-compose logs

# View specific service logs
docker-compose logs open-explore-front
docker-compose logs open-explore-console
docker-compose logs elasticsearch
```

### Rebuild Services

```bash
# Rebuild and start specific service
docker-compose up --build -d open-explore-console

# Rebuild all services
docker-compose up --build -d
```

### Enter Container for Debugging

```bash
# Enter backend container
docker exec -it open-explore-console bash

# Enter Elasticsearch container
docker exec -it open-explore-elasticsearch bash
```

## 🧪 Test Services

Run test scripts to verify all services are working properly:

**Linux/macOS:**
```bash
chmod +x test-services.sh
./test-services.sh
```

**Windows:**
```cmd
test-services.bat
```

## 🔬 Science Data API Testing

### API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/sciencedata/v1/list` | POST | List science data with search and pagination |
| `/sciencedata/v1/aggregations` | POST | Get aggregation statistics |
| `/sciencedata/v1/getOne/{id}` | GET | Get single science data by ID |
| `/sciencedata/v1/edit` | POST | Create or update science data |

### Testing Examples

#### 1. List Science Data

```bash
curl -X POST "http://localhost:8088/sciencedata/v1/list" \
  -H "Content-Type: application/json" \
  -d '{"query":"","page":"1","size":"10"}'
```

#### 2. Get Aggregations

```bash
curl -X POST "http://localhost:8088/sciencedata/v1/aggregations" \
  -H "Content-Type: application/json" \
  -d '{"query":"","aggregations":{}}'
```

#### 3. Get Single Science Data

```bash
curl -X GET "http://localhost:8088/sciencedata/v1/getOne/{uuid}"
```

#### 4. Create/Update Science Data

```bash
curl -X POST "http://localhost:8088/sciencedata/v1/edit" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "测试科学数据集",
    "abstracts": "这是一个测试数据集",
    "keyword": ["测试", "科学数据"],
    "pubDate": "2026-06-16"
  }'
```

### Science Data Fields

| Field | Type | Description |
|-------|------|-------------|
| `title` | String | Dataset title |
| `title-en` | String | English title |
| `abstract` | String | Dataset abstract |
| `keyword` | Array | Keywords list |
| `author` | String | Author name |
| `doi` | String | Digital Object Identifier |
| `cstr` | String | China Science and Technology Resource Identifier |
| `pub-date` | String | Publication date |
| `dataset-type` | String | Type of dataset |
| `type` | String | Data type |
| `classification` | Array | Classification hierarchy |
| `provider-name` | String | Data provider |
| `spatial-dimension` | String | Spatial coverage |
| `facility` | String | Research facility |

## 🔄 Common Operations

### Stop All Services

```bash
docker-compose down
```

### Stop and Remove All Data

```bash
docker-compose down -v
```

### Update Services

```bash
# After pulling latest code, rebuild
git pull
docker-compose down
docker-compose up --build -d
```

## 📁 Project Structure

```
open-explore/
├── docker-compose.yml          # Docker Compose configuration
├── deploy.sh / deploy.bat      # Deployment scripts
├── init-data.sh / init-data.bat # Data initialization scripts
├── test-services.sh / test-services.bat # Service testing scripts
├── elasticsearch/              # Elasticsearch configuration
│   ├── Dockerfile
│   └── ik.zip                 # IK Chinese word segmentation plugin
├── open-explore-console/       # Spring Boot backend
│   ├── Dockerfile
│   ├── src/
│   │   ├── main/java/com/os10/
│   │   │   ├── controller/    # API controllers
│   │   │   │   ├── ArticleController.java
│   │   │   │   └── ScienceDataController.java
│   │   │   ├── service/       # Business logic
│   │   │   │   ├── ArticleServiceImpl.java
│   │   │   │   └── ScienceDataServiceImpl.java
│   │   │   ├── entity/        # Data entities
│   │   │   │   ├── ArticleDO.java
│   │   │   │   └── ScienceDataDO.java
│   │   │   └── dto/           # Data transfer objects
│   │   │       ├── ArticleQuery.java
│   │   │       └── ScienceDataQuery.java
│   │   └── resources/
│   │       └── application.properties
│   └── pom.xml
└── open-explore-front/         # Vue.js frontend
    ├── Dockerfile
    ├── src/
    │   ├── views/             # Page components
    │   │   ├── ArticleSearch.vue
    │   │   ├── ArticleDetail.vue
    │   │   ├── ScienceDataSearch.vue
    │   │   └── ScienceDataDetail.vue
    │   ├── components/        # Common components
    │   │   └── TopNav.vue
    │   └── router/
    │       └── index.js
    └── package.json
```

## 🚨 Troubleshooting

### Common Issues

1. **Port Conflicts**: Ensure ports 80, 8088, 9200, 9300 are not occupied
2. **Insufficient Memory**: Elasticsearch requires at least 512MB memory
3. **Permission Issues**: On Linux/macOS, ensure scripts have execution permissions

### Check Service Health Status

```bash
# Check Elasticsearch
curl http://localhost:9200/_health

# Check backend API
curl http://localhost:8088/actuator/health

# Check frontend
curl http://localhost
```

## 📄 License

This project follows the corresponding open source license.

## 🤝 Contributing

Welcome to submit Issues and Pull Requests to improve the project!