# User Management & Promo Code Platform

## Overview

This project is a **multi-platform system** for managing users and promo codes across **mobile, web, and desktop**.

It combines:

* **Mobile app** → FlutterFlow online (drag-and-drop) + Firebase
* **Web backend** → Symfony 5.4 + MySQL
* **Desktop dashboard** → JavaFX
* **Automation** → Symfony custom shell command for **birthday-based promo code generation with automatic email delivery**

The platform ensures **secure authentication**, **real-time synchronization**, and **scalable architecture** simulation across distributed services.

---

## Tech Stack

* **Mobile** → FlutterFlow online builder + Firebase (Auth, Firestore)
* **Web Backend** → Symfony 5.4 (PHP)
* **Database** → MySQL
* **Desktop Dashboard** → JavaFX
* **Automation** → Symfony Console Commands (`php bin/console ...`)
* **Emailing** → Symfony Mailer (SMTP Gmail)
* **Security** → Password hashing (bcrypt/argon2), JWT authentication

---

## Key Features

* **User Management**: secure registration, authentication, and roles.
* **Promo Code System**: creation, validation, expiration rules.
* **Automation with Symfony Commands**:

  * Generate **birthday-based promo codes**.
  * Send **personalized emails** to users with their unique codes.
  * Apply **3-day expiration** logic automatically.
* **Mobile with FlutterFlow**:

  * Built entirely on FlutterFlow online (no local Flutter build).
  * Connected to **Firebase Auth** for login/signup.
  * Linked to **Firestore** for promo codes and user data.
  * Real-time updates across devices.
* **Distributed Architecture Simulation**: system deployed across DB, services, and clients for scalability testing.
* **Admin Dashboard**: built with JavaFX for streamlined operations.

---

## Mobile Platform (FlutterFlow + Firebase)

The mobile app was developed with **FlutterFlow (online drag-and-drop builder)**, directly integrated with **Firebase**.

### Features

* Secure authentication (Firebase Auth).
* Real-time promo code validation via Firestore.
* Instant sync between web and mobile platforms.
* Cross-device responsive UI.

### Setup

* Managed entirely in **FlutterFlow online workspace**.
* Connected to Firebase project with:

  * **Authentication** → Email/password login.
  * **Firestore Collections**:

    * `users` → user profiles.
    * `promo_codes` → codes, expiration dates, and associations.
* No manual mobile build required — FlutterFlow handles deployment.

---

## Web Backend (Symfony 5.4 + MySQL)

* REST API endpoints for users and promo codes.
* MySQL relational schema with Doctrine ORM.
* **JWT-based authentication**.
* **Symfony Console Command** for promo code automation.

### Example Automation Command

```bash
php bin/console app:generate-code-promos
```

This command:

1. Finds users with a birthday today.
2. Generates a **unique alphanumeric promo code** (8 chars).
3. Associates it with the user and sets a **3-day expiration**.
4. Sends an **email notification** with the promo code.

### Snippet (simplified)

```php
$email = (new Email())
    ->from(new Address('anouar.jebri@gmail.com', 'studentors'))
    ->to($utilisateur->getEmail())
    ->subject('🎉 Your Birthday Promo Code')
    ->html('<p>Happy Birthday! Your promo code: <b>' . $uniqueCode . '</b></p>');
    
$this->mailer->send($email);
```

---

## Project Structure

```
User_Promo_Platform/
  ├── mobile/                 # FlutterFlow project (online)
  ├── backend/                # Symfony 5.4 API
  │   ├── src/Command/        # Symfony console automation (promo codes)
  │   ├── src/Entity/         # Doctrine entities (User, PromoCode, etc.)
  │   └── src/Repository/     # Doctrine repositories
  ├── database/               # MySQL schema & migrations
  ├── desktop-dashboard/      # JavaFX admin interface
  └── README.md               # Documentation
```

---

## Achievements

* Improved **cross-platform accessibility** with web + mobile.
* Integrated **secure authentication** (hashing + JWT + Firebase).
* Automated **promo code generation & emailing** with Symfony Commands.
* Real-time sync with Firebase (no manual redeployment).
* Enhanced **admin efficiency** with JavaFX dashboard.
* Boosted scalability testing by simulating **distributed deployment**.

---

## Author

Developed by **Anouar Jebri**
GitHub: [AnouarJebri](https://github.com/AnouarJebri)
