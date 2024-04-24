eval $(minikube docker-env)
docker build -t adventureshops:1.0 -f Dockerfile .