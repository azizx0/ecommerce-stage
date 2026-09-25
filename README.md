# E-commerce - Projet de stage

Application e-commerce développée dans le cadre d'un stage ingénieur (validation par soutenance académique).

## Stack technique

**Backend**
- Java 17 / Spring Boot 3.3.4
- Spring Security + JWT (authentification stateless)
- Spring Data JPA / Hibernate
- Base de données H2 (dev) / PostgreSQL (prod)
- Maven

**Frontend**
- Angular (à venir)

## Fonctionnalités

- Authentification (inscription / connexion) avec JWT
- Gestion des rôles : USER / ADMIN
- Catalogue produits (CRUD, filtrage par catégorie, recherche)
- Gestion des catégories (CRUD, réservé ADMIN)
- Commandes : création avec vérification de stock, calcul automatique du total, historique
- Gestion des commandes côté admin (changement de statut)

## Structure du projet
