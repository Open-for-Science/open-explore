@echo off
echo Testing Open Explore services...

echo 1. Testing Elasticsearch (port 9200)...
curl -s http://localhost:9200 | findstr "cluster_name" >nul && echo ✅ Elasticsearch is running normally || echo ❌ Elasticsearch connection failed

echo 2. Testing Backend API (port 8088)...
curl -s -o nul -w "%%{http_code}" http://localhost:8088 | findstr "404 200" >nul && echo ✅ Backend service is running normally || echo ❌ Backend service connection failed

echo 3. Testing Frontend (port 80)...
curl -s http://localhost | findstr "html" >nul && echo ✅ Frontend service is running normally || echo ❌ Frontend service connection failed

echo.
echo Service status:
docker-compose ps
pause