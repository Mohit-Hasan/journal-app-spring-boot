# Journal App Spring Boot

## Overview

Journal App is a **basic Spring Boot REST API project** built as a **learning-focused backend application**.  
The goal of this project was not to create a production-grade system, but to **deeply understand modern, scalable backend engineering practices** using Java and Spring Boot.

Although I have long-term experience with **Java and core software engineering foundations**, I had not previously explored a **scalable Java framework** in a structured, hands-on way. After graduation, I intentionally decided to study how **large-scale systems (FAANG-style) are designed, structured, and maintained**, and Spring Boot was the natural choice for this exploration.

This project represents that learning journey.

---

## Learning Objectives

- Understand how **Spring Boot** is used in real-world backend systems
- Apply **advanced OOP concepts** in a modern Java framework
- Learn how **microservices-oriented systems** work conceptually
- Explore **event-driven architecture** patterns
- Practice **engineering quality, testing, and automation**
- Use professional tooling commonly adopted in large-scale systems

---

## Key Concepts Explored

### Backend & Architecture
- REST API development using Spring Boot
- Layered architecture (Controller, Service, Repository)
- Dependency Injection and Inversion of Control
- Configuration-based application design
- Conceptual understanding of microservices-style separation
- Transaction management using Spring configuration

### Caching & Performance
- **Redis** integration for application-level caching
- Caching frequently accessed and third-party API data
- Reducing unnecessary database and external API calls
- Understanding cache usage patterns in scalable systems

### Event-Driven Systems
- **Apache Kafka**
- Kafka consumer implementation for asynchronous processing
- Understanding event-driven communication and decoupled services

### API Documentation
- **Swagger / OpenAPI**
- Automatic API documentation generation
- Clear and structured API contracts

### Security
- **Spring Security** fundamentals
- JWT-based authentication and request filtering
- Public, user, and admin-level API separation
- Basic OAuth (Google login) flow exploration

---

## Testing, Quality & Automation

- Unit and integration testing fundamentals
- Centralized validation and exception handling
- Understanding reliability and maintainability concerns
- **SonarCloud** integration for:
  - Static code analysis
  - Code quality and maintainability metrics
  - Test coverage measurement

### Code Quality Metrics

The project uses **SonarCloud** to measure code quality, reliability, and test coverage through automated CI pipelines.

![SonarCloud Overview](https://raw.githubusercontent.com/Mohit-Hasan/journal-app-spring-boot/refs/heads/main/screenshots/sonarcloud-overview.png)


## CI/CD & DevOps Practices

- Hands-on **CI/CD automation**
- Automated build and test pipelines
- Code quality checks enforced through CI
- Practical experience with quality gates and automated feedback

---

## Containerization & Local Development

- **Docker** for local development and dependency management
- Running services in consistent environments
- Learning how professional teams use containerization locally

---

## Third-Party API Integration

- Integration with external APIs (e.g., weather data)
- Centralized response models
- Caching external API responses to reduce API hits
- Applying a professional, scalable approach to API consumption

---

## Technology Stack

- Java
- Spring Boot
- Spring Web (REST APIs)
- Spring Security
- JWT Authentication
- Redis
- Apache Kafka
- Swagger / OpenAPI
- Docker
- SonarCloud
- CI/CD Pipelines

---

## Project Scope

- Basic REST API implementation
- Learning-focused system design
- Open-source and fully shareable
- Not intended for production use

---

## Learning Reference

This project was initially developed by following the **Spring Boot Mastery: From Basics to Advanced** playlist by **Vipul Tyagi**, and then extended independently through **extensive self-learning**. [YouTube Playlist](https://youtube.com/playlist?list=PLA3GkZPtsafacdBLdd3p1DyRd5FGfr3Ue)


In addition to the core playlist, concepts were deepened by exploring **many supplementary videos, articles, and documentation** related to:
- Spring Framework internals
- Dependency Injection (DI) and Inversion of Control (IoC)
- Spring Security and authentication flows
- Caching strategies
- Messaging and asynchronous processing
- CI/CD fundamentals
- Containerization and deployment
- Code quality and best practices

This project reflects a **hands-on, exploratory learning approach**, where features were added and refactored while gaining deeper understanding of backend engineering concepts.

---

## Summary

Journal App is a **practice-driven Spring Boot project** that demonstrates:
- Strong backend engineering foundations
- Practical understanding of scalable system components
- Exposure to modern backend technologies and workflows
- A disciplined approach to learning, testing, and code quality

---

### Explore Other Projects
I have curated a selection of my work to showcase different architectural patterns and business solutions. Feel free to explore them:

[![GlobalShop](https://img.shields.io/badge/Project-GlobalShop-black?style=for-the-badge&logo=github)](https://github.com/mohit-hasan/globalshop-commerce-platform)
[![Classroom](https://img.shields.io/badge/Project-ClassFlow-black?style=for-the-badge&logo=github)](https://github.com/mohit-hasan/classroom-management-system)
[![WHMCSNexus](https://img.shields.io/badge/Project-WHMCSNexus-black?style=for-the-badge&logo=github)](https://github.com/mohit-hasan/whmcs-nexus-control-plane)
[![JournalAppSpring](https://img.shields.io/badge/Project-JournalApp-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://github.com/mohit-hasan/journal-app-spring-boot)
