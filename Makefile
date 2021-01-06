docker-build:
	@ ./gradlew clean build
	@ docker build -t vertx-config-122:1.0.0 .

docker-run:
	@ docker run --rm -p 8888:8888 vertx-config-122:1.0.0

docker-test:
	@ curl http://localhost:8888
