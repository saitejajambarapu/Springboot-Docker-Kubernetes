📐 Architecture Overview
This project runs a Spring Boot REST API with MySQL on local Kubernetes (Minikube).
The application is containerized using Docker and deployed using Kubernetes primitives.


High-Level Architecture 
 
+---------------------------------------------------+
| Client |
| (Browser / Postman) |
+------------------------+--------------------------+
|
| HTTP (NodePort)
v
+---------------------------------------------------+
| Kubernetes Cluster (Minikube) |
| |
| +---------------- NodePort Service -------------+|
| | docker-kub-demo ||
| | Exposes port 8080 ||
| +--------------------+--------------------------+|
| |
| v
| +----------------------------------+ |
| | Spring Boot Deployment | |
| | (docker-kub-demo Pods) | |
| | | |
| | - REST Controllers | |
| | - Service Layer | |
| | - JPA / Hibernate | |
| | - Runs on port 8080 | |
| +------------------+---------------+ |
| |
| | JDBC
| v
| +----------------------------------+ |
| | MySQL Service (ClusterIP) | |
| | DNS: mysql:3306 | |
| +------------------+---------------+ |
| |
| v
| +----------------------------------+ |
| | MySQL Pod | |
| | (mysql:8.0 image) | |
| | | |
| | - Stores application data | |
| | - Uses PersistentVolumeClaim | |
| +------------------+---------------+ |
| |
| v
| +----------------------------------+ |
| | Persistent Volume (PV) | |
| | Data survives pod restarts | |
| +----------------------------------+ |
| |
+---------------------------------------------------+


Key Design Decisions
Spring Boot application is stateless → deployed as a Deployment
MySQL is stateful → backed by PersistentVolume and PVC
Internal communication uses Kubernetes Service DNS
External access is provided using NodePort
Docker images are built locally inside Minikube

Kubernetes Namespace
All application resources run inside a dedicated namespace:

 
dev


🧱 Components Used
Spring Boot (REST API)
MySQL 8.0
Docker
Kubernetes (Minikube)
Persistent Volumes (PV / PVC)
NodePort & ClusterIP Services

🚀 Commands Used in This Project
1️⃣ Start Minikube
 
minikube start

Check status:

 
minikube status


2️⃣ Set Docker to Use Minikube (Windows – PowerShell)
 
minikube docker-env | Invoke-Expression

Verify:

 
docker info | Select-String Name

Expected:

 
Name: minikube


3️⃣ Build Spring Boot JAR
 
mvn clean package -DskipTests


4️⃣ Build Docker Image Locally for Kubernetes
 
docker build -t docker-kub-demo:0.0.1 .

Verify:

 
docker images


5️⃣ Create Kubernetes Namespace
 
kubectl apply -f namespace.yaml


6️⃣ Create Persistent Storage for MySQL
 
kubectl apply -f pv.yaml
kubectl apply -f pvc.yaml

Verify:

 
kubectl get pv
kubectl get pvc -n dev


7️⃣ Deploy MySQL
 
kubectl apply -f mysql-deployment.yaml
kubectl apply -f mysql-service.yaml

Check:

 
kubectl get pods -n dev


8️⃣ Deploy Spring Boot Application
 
kubectl apply -f app-deployment.yaml
kubectl apply -f app-service.yaml

Check:

 
kubectl get deployments -n dev
kubectl get pods -n dev


9️⃣ Check Application Logs
 
kubectl logs deployment/docker-kub-demo -n dev

Expected logs:

 
Tomcat started on port(s): 8080
Started DockerKubDemoApplication


🔟 Access the Application
 
minikube service docker-kub-demo -n dev

Example output:

 
http://127.0.0.1:<random-port>

Test APIs:

 
GET /api/users
POST /api/users


1️⃣1️⃣ Debugging Commands Used
 
kubectl describe pod <pod-name> -n dev
kubectl get endpoints -n dev
kubectl exec -it <pod-name> -n dev -- sh
kubectl logs <pod-name> -n dev --previous


1️⃣2️⃣ Cleanup Commands
 
kubectl delete deployment docker-kub-demo -n dev
kubectl delete deployment mysql -n dev
kubectl delete svc --all -n dev
kubectl delete pvc --all -n dev
kubectl delete pv --all

----------------------------------------------------------------------------------
This project demonstrates a full local Kubernetes workflow:

Dockerized Spring Boot app
Stateful MySQL with persistent storage
Kubernetes-native networking
Local image usage without Docker Hub
Minikube-based development environment
