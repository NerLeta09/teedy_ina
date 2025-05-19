set HTTPS_PROXY=https://127.0.0.1:7890

docker run -d -p 8084:8080 --name teedy_manual01 teedy_ina_manual
docker run -d -p 8083:8080 --name teedy_manual02 teedy_ina_manual
docker run -d -p 8082:8080 --name teedy_manual03 teedy_ina_manual