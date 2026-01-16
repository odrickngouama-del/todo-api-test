Todo Service API

Microservice Spring Boot de gestion de tâches (todo list) développé en Java 17.

Objectif : exposer une API REST simple avec règles métier, tests et documentation.

Technologies

Java 17

Spring Boot 3

Maven

JUnit 5 / Mockito

Cucumber

Swagger (Springdoc OpenAPI)

Lancer l’application
Avec Maven
mvn spring-boot:run
Accès API
http://localhost:8082
Documentation API (Swagger)

Interface Swagger :

http://localhost:8082/swagger-ui/index.html

Spécification OpenAPI :

http://localhost:8082/v3/api-docs
Endpoints principaux

Base URL :

/api/v1/tasks
Créer une tâche

POST /api/v1/tasks

{
"label": "Faire les courses",
"description": "Lait, pain, oeufs"
}
Lister toutes les tâches

GET /api/v1/tasks

Lister les tâches (pagination)

GET /api/v1/tasks

Paramètres :

Paramètre	Description
------------------------------------
page:	page (défaut 0)
size:	taille de page (défaut 10)
todoOnly:	true = uniquement non complétées

Exemple :

/api/v1/tasks?page=0&size=5&todoOnly=true

Réponse :

{
"content": [ { ... } ],
"page": 0,
"size": 5,
"totalElements": 12,
"totalPages": 3
}

Récupérer une tâche par id

GET /api/v1/tasks/{id}

Modifier le statut d’une tâche

PATCH /api/v1/tasks/{id}/status

{ "completed": true }
Règles métier

Label minimum : 5 caractères

Maximum 10 tâches actives

Pas de doublon de label actif (insensible à la casse)

Une tâche est créée avec completed = false

Tests

Lancer tous les tests :

mvn test

Types de tests :

Tests unitaires (services, règles métier)

Tests d’intégration (API REST)

Tests BDD avec Cucumber

Docker (optionnel)
Build
docker build -t todo-service .
Run
docker run -p 8082:8082 todo-service
Structure du projet

domain : modèle et règles métier

application : cas d’usage

exposition : contrôleurs REST

infrastructure : repository en mémoire

commons : DTOs