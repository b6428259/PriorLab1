eval $(minikube docker-env)
docker build -t consumer:1.0 -f  Dockerfile-2stage