#!/bin/bash

echo "Starting deployment of Open Explore project..."

# Stop and remove existing containers
echo "Stopping existing containers..."
docker-compose down

# Clean up old images (optional)
echo "Cleaning up old images..."
docker system prune -f

# Build and start all services
echo "Building and starting services..."
docker-compose up --build -d

# Wait for services to start
echo "Waiting for services to start..."
sleep 30

# Check service status
echo "Checking service status..."
docker-compose ps

echo "Deployment completed!"
echo "Frontend URL: http://localhost"
echo "Backend API URL: http://localhost:8088"
echo "Elasticsearch URL: http://localhost:9200"

# Show logs
echo "Displaying service logs..."
docker-compose logs --tail=50