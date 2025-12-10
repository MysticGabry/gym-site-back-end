# 🏋️‍♂️ GymSite – Back-End (Spring Boot)

Back-End RESTful dell’applicazione **GymSite**, una piattaforma e-commerce per la gestione di prodotti fitness, carrello, ordini e autenticazione utenti con JWT.  
Il progetto fornisce tutte le API necessarie al Front-End Angular e segue un'architettura pulita, modulare e facilmente espandibile.

---

## 🚀 Tecnologie utilizzate

- **Java 17+**
- **Spring Boot**
  - Spring Web
  - Spring Security (JWT)
  - Spring Data JPA (Hibernate)
- **MySQL**
- **Maven**
- **Lombok**
- **Model Mapper / DTO Pattern**

---


L’architettura segue questo schema:

Controller → Service → Repository → Database
↓
Security Layer (JWT)

---

# 🔐 Autenticazione & Sicurezza (JWT)

Il progetto utilizza **Spring Security + JWT** con:

### ✔️ `JwtAuthenticationFilter`
- intercetta ogni richiesta
- estrae il token JWT dall’header Authorization
- valida il token
- popola il SecurityContext con l’utente autenticato

### ✔️ `JwtService`
- genera token per login e registrazione
- valida token esistenti
- estrae username, expiration, authorities

### ✔️ `UserDetailsServiceImpl`
- carica l’utente dal database tramite email
- converte il ruolo (`Role.USER`, `Role.ADMIN`) in authorities Spring

### ✔️ `SecurityConfig`
- definisce endpoint pubblici e protetti
- abilita CORS
- registra il filtro JWT nella chain
- protegge le rotte admin

Endpoint pubblici:

/api/auth/login
/api/auth/register
/api/products

Endpoint protetti:

/api/orders/**
/api/users/**

Endpoint admin:

/api/products/admin/**
/api/orders/admin/**

---

# 📦 Funzionalità implementate

## 🧍 Gestione Utenti
- Registrazione (`POST /api/auth/register`)
- Login con JWT (`POST /api/auth/login`)
- Profilo utente
- Ruoli (USER / ADMIN)

---

## 🛒 Prodotti
- GET lista prodotti
- GET dettaglio prodotto
- Operazioni admin:
  - Aggiunta prodotto
  - Modifica prodotto
  - Eliminazione prodotto

Classe chiave: **`ProductService`**  
Repository: **`ProductRepository`**

---

## 🧾 Ordini & Checkout
- Creazione ordine dal carrello
- Registrazione ordini utente
- Storico ordini
- Visualizzazione dettaglio ordine

Classi chiave:
- `OrderService`
- `OrderRepository`
- `OrderItemRepository`

DTO principali:
- `CheckoutDTO`
- `OrderDTO`
- `OrderItemDTO`

---

# 🗂️ Modello dati (Entities)

### ✔️ `User`
- id, username, email, password (hashed)
- role (enum)
- relazione con ordini

### ✔️ `Product`
- id, name, description
- price, stock
- imageUrl

### ✔️ `Order`
- id, user, timestamp
- lista di `OrderItem`

### ✔️ `OrderItem`
- product
- quantity
- linked a un ordine

---

# 🌐 API principali

## 🔑 Autenticazione
POST /api/auth/register
POST /api/auth/login

## 📦 Prodotti
GET /api/products
GET /api/products/{id}
POST /api/products/admin (ADMIN)
PUT /api/products/admin/{id} (ADMIN)
DELETE /api/products/admin/{id} (ADMIN)

## 🧾 Ordini
POST /api/orders/checkout
GET /api/orders
GET /api/orders/{id}


---

# ⚙️ Configurazione

### `src/main/resources/application.properties`
Esempio:

spring.datasource.url=jdbc:mysql://localhost:3306/gym_site_db
spring.datasource.username=root
spring.datasource.password=****
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=INSERISCI_UNA_CHIAVE_SEGRETA
jwt.expiration=86400000

---

# ▶️ Avvio del progetto

### 1️⃣ Configura MySQL
Crea un database:

CREATE DATABASE gym_site_db;

### 2️⃣ Imposta `application.properties`

### 3️⃣ Avvia l’app:
mvn spring-boot:run

Oppure da IDE:
> Run → Application.java

---

# 🧪 Test
Nella cartella `/src/test/java/…` sono presenti test per:

- Integrità del contesto Spring
- Concorrenza nella gestione prodotti (`ProductConcurrencyTest.java`)

--- 
