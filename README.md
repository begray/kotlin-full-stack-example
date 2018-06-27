## To Do list application in Kotlin ##

All the commands below use FISH shell syntax

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
