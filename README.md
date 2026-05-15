# 🏛️ Portal de Cultura Quito
### Sistema de Gestión y Reserva de Visitas a Museos de Quito

**Equipo:** Nexus Core  
**Materia:** Calidad de Software – Escuela Politécnica Nacional  
**Integrantes:**
- Cuasapaz Guilcamaigua Jonathan Franklin *(Product Manager)*
- Guamán Maza Jonathan Steven *(Frontend Developer)*
- Moreira Ramos Jhonny Bryan *(Backend Developer)*
- Sigcha Diaz Christan David *(Tester)*

---

## Descripción

Aplicación web que permite consultar museos disponibles en Quito, visualizar horarios y exposiciones, y gestionar reservas de visitas. Desarrollada con Java 17+, Jakarta EE, Hibernate y SQLite.

## Tecnologías

- **Backend:** Java 17, Jakarta Servlet 6.1, Hibernate ORM 6.5
- **Frontend:** JSP, JSTL
- **Base de datos:** SQLite (embebida, se crea automáticamente)
- **Build:** Maven 3.9
- **Servidor:** Apache Tomcat 11

## Requisitos previos

- JDK 17 o superior
- Apache Tomcat 11.x
- Maven (o usar el wrapper `mvnw` incluido)

## Cómo ejecutar

```bash
# 1. Compilar y empaquetar
mvnw.cmd clean package -DskipTests   # Windows
./mvnw clean package -DskipTests     # Mac/Linux

# 2. Copiar el WAR a Tomcat
# Copiar target/museosquito.war a la carpeta webapps/ de Tomcat

# 3. Iniciar Tomcat y acceder en:
# http://localhost:8080/museosquito
```

## Ejecutar tests

```bash
mvnw.cmd test   # Windows
./mvnw test     # Mac/Linux
```

## Historias de Usuario implementadas

| HU | Descripción | Estado |
|----|-------------|--------|
| HU1 | Consultar información de museos | ✅ Implementada |
| HU2 | Reservar una visita | 🔜 Pendiente |
| HU3 | Cancelar o modificar reserva | 🔜 Pendiente |
| HU4 | Gestionar horarios y aforo | 🔜 Pendiente |

## Estructura del proyecto

```
src/
├── main/
│   ├── java/com/webapp/museosquito/
│   │   ├── controller/visitante/   ← Servlets HU1
│   │   ├── model/                  ← Entidades JPA
│   │   ├── repository/             ← Acceso a datos
│   │   ├── service/                ← Lógica de negocio
│   │   └── util/                   ← Hibernate, DataSeeder
│   ├── resources/
│   │   └── hibernate.cfg.xml
│   └── webapp/
│       ├── WEB-INF/views/visitante/ ← Vistas JSP HU1
│       └── css/
└── test/                            ← Tests unitarios HU1
```
