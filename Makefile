local-start:
	docker compose -f docker/docker-compose.yml up --build --force-recreate -d

local-stop:
	docker compose -f docker/docker-compose.yml down

local-dependencies-start:
	docker compose -f docker/docker-compose.yml up --build --force-recreate -d employee-service

local-dependencies--stop:
	docker compose -f docker/docker-compose.yml down employee-service

build-image:
	docker build -f ./docker/Dockerfile .
