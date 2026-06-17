# Spring Boot Ports & Adapters Generator Plugin

A Gradle plugin that scaffolds a **Ports and Adapters** (Hexagonal Architecture) structure for Spring Boot applications aligned with Domain-Driven Design principles.

## Features

- Generates the base application directory structure (`scaffolding`)
- Generates complete bounded context code per domain model (`scaffoldDomainModel`)
- Repeatable — run `scaffoldDomainModel` once per bounded context
- Supports dry-run preview mode
- Designed for **Spring Boot** applications with MapStruct and Spring Data JPA

## Installation

```groovy
plugins {
    id 'io.github.marksosman.scaffolding' version '1.0.1'
}
```

## Configuration

```groovy
scaffolding {
    rootPackage     = 'ch.sosman'     // required — base Java package
    applicationName = 'myapp'      // optional — appended to rootPackage
    domain          = 'orders'     // optional — appended to applicationName
    subdomain       = 'backorders'       // optional — sub-package under domain/

    // scaffoldDomainModel feature flags (defaults shown)
    withCoreDomain     = true
    withPersistence    = true
    withProvidedPort   = true   // mutually exclusive with withRestController
    withRestController = false  // mutually exclusive with withProvidedPort
    withEventPublisher = false
    withEventListener  = false
    withEventSourcing  = false

    // Shared flags
    dryRun = false   // preview without creating anything
}
```

| Property           | Required | Default | Description                                        |
|--------------------|----------|---------|----------------------------------------------------|
| `rootPackage`      | Yes      | —       | Base Java package                                  |
| `applicationName`  | No       | —       | Appended to `rootPackage`                          |
| `domain`           | No       | —       | Appended after `applicationName`                   |
| `subdomain`        | No       | —       | Sub-package under `domain/`                        |
| `withCoreDomain`   | No       | `true`  | Generate aggregate, value object, facade, repository, provider |
| `withPersistence`  | No       | `true`  | Generate JPA entity, Spring Data repo, mapper, adapter |
| `withProvidedPort`   | No     | `true`  | Generate presenter, request/response mappers, port — mutually exclusive with `withRestController` |
| `withRestController` | No     | `false` | Generate presenter, request/response mappers, REST controller — mutually exclusive with `withProvidedPort` |
| `withEventPublisher` | No     | `false` | Generate publisher interface and implementation    |
| `withEventListener`  | No     | `false` | Generate event facade, provider, listener          |
| `withEventSourcing`  | No     | `false` | Generate event marker, state, projector, repository, adapter |
| `dryRun`             | No     | `false` | Print what would be created without touching files |

---

## Tasks

### `scaffolding` — Generate base directory structure

Run once per application to create the top-level Ports & Adapters packages.

```bash
./gradlew scaffolding
```

#### Output

```text
src/main/java/ch/sosman/myapp/orders/
├── application/
├── crosscutting/
├── domain/
│   └── backorders/          ← created when subdomain is set
└── infrastructure/
```

---

### `scaffoldDomainModel` — Generate a bounded context

Run once per domain model. Requires `-PdomainModelName=Name` (PascalCase).

```bash
./gradlew scaffoldDomainModel -PdomainModelName=Order
```

All flags can also be passed as project properties to override the extension values:

```bash
./gradlew scaffoldDomainModel \
  -PdomainModelName=Order \
  -PwithPersistence=true \
  -PwithEventPublisher=true \
  -PwithEventListener=false \
  -PdryRun=false
```

#### Output

Given `rootPackage=ch.sosman`, `applicationName=myapp`, `subdomain=backorders`, `domainModelName=Order`:

```text
src/main/java/ch/sosman/myapp/domain/backorders/order/
├── core/
│   ├── model/
│   │   ├── Order.java                   ← aggregate root
│   │   └── attribute/
│   │       ├── OrderId.java             ← value object (UUID record)
│   │       └── package-info.java
│   ├── ports/
│   │   ├── incoming/
│   │   │   └── OrderFacade.java         ← use case interface
│   │   └── outgoing/
│   │       └── OrderRepository.java     ← repository interface
│   └── OrderProvider.java               ← use case implementation (@Service)
├── application/
│   └── http/
│       ├── OrderPresenter.java
│       ├── OrderRequestMapper.java
│       ├── OrderResponseMapper.java     ← MapStruct mapper
│       └── OrderProvidedPort.java       ← inbound adapter
├── infrastructure/
│   └── persistence/
│       ├── model/
│       │   └── OrderDbEntity.java       ← JPA entity
│       ├── OrderDb.java                 ← Spring Data JPA repository
│       ├── OrderDbMapper.java           ← MapStruct mapper
│       └── OrderDbAdapter.java          ← outbound adapter
```

With `withEventPublisher=true`:

```text
├── core/ports/outgoing/
│   └── OrderPublisher.java
└── infrastructure/messaging/
    ├── OrderEventMapper.java
    └── OrderEventPublisher.java
```

With `withEventListener=true`:

```text
├── core/ports/incoming/
│   └── OrderEventFacade.java
├── core/
│   └── OrderEventProvider.java
└── application/messaging/
    └── OrderEventListener.java
```

---

## Typical workflow

```bash
# 1. Configure once in build.gradle
#    scaffolding { rootPackage = 'ch.sosman'; applicationName = 'myapp'; subdomain = 'backorders' }

# 2. Bootstrap the application structure
./gradlew scaffolding

# 3. Add a bounded context per domain model
./gradlew scaffoldDomainModel -PdomainModelName=Order
./gradlew scaffoldDomainModel -PdomainModelName=Invoice
./gradlew scaffoldDomainModel -PdomainModelName=Payment -PwithEventPublisher=true

# Preview without writing files
./gradlew scaffoldDomainModel -PdomainModelName=Order -PdryRun=true
```