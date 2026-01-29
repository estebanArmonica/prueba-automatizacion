.PHONY: build test docker-build docker-run compose-up compose-down k8s-deploy k8s-delete

# Gradle commands
build:
	./gradlew clean build

test:
	./gradlew test --info

# Docker commands
docker-build:
	docker build -t qanova-tests:latest .

docker-run:
	docker run --rm -v $(PWD)/reports:/app/build/reports qanova-tests:latest

# Docker Compose commands
compose-up:
	docker-compose up --build

compose-down:
	docker-compose down -v

# Kubernetes commands
k8s-deploy:
	kubectl apply -f k8s/

k8s-delete:
	kubectl delete -f k8s/

k8s-logs:
	kubectl logs -f deployment/qanova-tests -n qanova-tests

# Reports
open-reports:
	open build/reports/tests/test/index.html || xdg-open build/reports/tests/test/index.html

# CI/CD simulation
ci-local:
	make build test docker-build