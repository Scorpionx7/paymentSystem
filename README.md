# 💳 Payment System API

API RESTful construída com **Java 21 + Spring Boot** para gerenciar pagamentos, reembolsos e contas de clientes, com foco em **segurança**, **boas práticas** e **mensageria assíncrona**.

## ⚙️ Funcionalidades

* 🔐 **Segurança com JWT & Roles (`USER`, `ADMIN`)**
* 💸 **Pagamentos:** criar, listar e alterar status (admin)
* 💰 **Contas:** criação automática, saldo, débito/crédito
* 🔁 **Reembolsos:** com validação de valor e histórico
* 📬 **Notificações assíncronas com RabbitMQ**

## 🧰 Tecnologias

Java 21 • Spring Boot 3 • Spring Security • JWT • Spring Data JPA • MySQL • RabbitMQ • Docker • Maven • Lombok

## 🚀 Como Rodar

1. **Clone o projeto**

   ```bash
   git clone https://github.com/Scorpionx7/paymentSystem.git
   cd paymentSystem
   ```

2. **Suba os containers (MySQL + RabbitMQ)**

   ```bash
   docker-compose up -d
   ```

3. **Configure a secret JWT**
   No `application.properties`:

   ```properties
   application.security.jwt.secret-key=SUA_CHAVE_SEGURA_EM_BASE64
   ```

4. **Inicie a aplicação**

   ```bash
   mvn spring-boot:run
   ```

Acesse em `http://localhost:8080`.

## 👤 Usuário Admin Padrão

* **Email:** `admin@payments.com`
* **Senha:** `admin123`

## 📡 Endpoints Principais

### 🔐 Autenticação

| Método | Endpoint             | Acesso  |
| ------ | -------------------- | ------- |
| POST   | `/api/auth/register` | Público |
| POST   | `/api/auth/login`    | Público |

### 💳 Pagamentos

| Método | Endpoint                    | Acesso           |
| ------ | --------------------------- | ---------------- |
| POST   | `/api/payments`             | `USER` / `ADMIN` |
| GET    | `/api/payments`             | `USER` / `ADMIN` |
| PUT    | `/api/payments/{id}/status` | `ADMIN`          |

### 💰 Conta

| Método | Endpoint                | Acesso           |
| ------ | ----------------------- | ---------------- |
| GET    | `/api/accounts/balance` | `USER` / `ADMIN` |

### 🔁 Reembolso

| Método | Endpoint       | Acesso           |
| ------ | -------------- | ---------------- |
| POST   | `/api/refunds` | `USER` / `ADMIN` |

## 📧 Contato

Entre em contato para dúvidas, ideias ou colaborações:

* 🔗 [**LinkedIn**](https://www.linkedin.com/in/estherrezende/)
* 📬 **Email:** [rezendealvesesther@gmail.com](mailto:rezendealvesesther@gmail.com)

---
