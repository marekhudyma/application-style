java --enable-preview -jar target/application-style-exec.jar



docker system prune -a

docker build -t application-style .

docker image rm application-style

docker images

Run application as docker image and connect to database installed on localhost (`--network host`)
docker run -e "SPRING_PROFILES_ACTIVE=production" -p 8080:8080 --network host application-style



