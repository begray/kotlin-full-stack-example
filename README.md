## Sample full stack ToDo application in Kotlin

Backend: ktor, exposed
Frontend: kotlin-react, kotlin-js-wrappers
Deployment: docker, helm, kubernetes

## Building and deploying

All the commands below use FISH shell syntax on macOS

### Build and run docker image locally

`$ ./gradlew build`

`$ docker build -t todoapp:v0.0.1 .`

`$ docker run -m512M --cpus 2 -it -p 9090:9090 --rm todoapp:v0.0.1`

### Running inside minikube's VM

`$ eval (minikube docker-env)`

`$ docker run -m512M --cpus 2 -it -p 9090:9090 --rm todoapp:v0.0.1`

`$ open http://(minikube ip):9090/`

### Installing Helm

`$ brew install helm`

`$ helm init`

### Deploy application into Kubernetes cluster

Use `helm` to install.

`$ cd helm-chart`

* Copy PostgreSQL chart into charts

`$ helm update dependencies todoapp`

* Package chart into archive

`$ helm package todoapp`

* Install package into Kubernetes

`$ helm install todoapp-0.0.2.tgz`