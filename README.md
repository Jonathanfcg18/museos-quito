# 🏛️ Portal de Cultura Quito

### Sistema de Gestión y Reserva de Visitas a Museos de Quito

**Equipo:** Nexus Core
**Materia:** Calidad de Software – Escuela Politécnica Nacional

**Integrantes:**

* Cuasapaz Guilcamaigua Jonathan Franklin *(Product Manager)*
* Guamán Maza Jonathan Steven *(Frontend Developer)*
* Moreira Ramos Jhonny Bryan *(Backend Developer)*
* Sigcha Diaz Christan David *(Tester)*

---

## 📌 Descripción

Aplicación web que permite consultar museos disponibles en Quito, visualizar horarios y exposiciones, y gestionar reservas de visitas.

Desarrollada con Java 17+, Jakarta EE, Hibernate y SQLite para entorno local, y PostgreSQL en producción.

---

## 🛠️ Tecnologías

* **Backend:** Java 17, Jakarta Servlet 6.1, Hibernate ORM 6.5
* **Frontend:** JSP, JSTL
* **Base de datos:**

    * Local: SQLite (embebida, automática)
    * Producción: PostgreSQL (Railway)
* **Build:** Maven 3.9
* **Servidor:** Apache Tomcat 11

---

## 🌐 Demo en producción

👉 https://museos-quito-production-6816.up.railway.app

En producción, Railway inyecta automáticamente la variable `DATABASE_URL`, por lo que no se requiere configuración manual de base de datos.

---

## ⚠️ Limitación en producción (Correos electrónicos)

Las notificaciones por correo (HU-10 y HU-11) **no funcionan en la demo desplegada en Railway**.

### ¿Por qué ocurre esto?

Railway, en sus planes gratuitos (Free / Trial / Hobby), **bloquea completamente el tráfico SMTP saliente**, incluyendo los puertos:

* 25
* 465
* 587
* 2525

Esto significa que cualquier intento de enviar correos mediante **SMTP (por ejemplo, Gmail con Jakarta Mail)** será bloqueado a nivel de red.

### Consecuencia técnica

* La aplicación intenta enviar el correo →
* Railway bloquea la conexión →
* El envío falla (o se queda esperando si no hay timeout)

### Soluciones posibles

1. **Actualizar a plan Pro en Railway** (habilita SMTP saliente)
2. **Usar un servicio de correo vía HTTP** (recomendado), como:

    * Resend
    * SendGrid
    * Mailgun

Estos servicios funcionan sobre HTTPS, el cual **sí está permitido en todos los planes**.

---

## ✅ Requisitos previos

### Opción única: Docker (recomendada)

* Docker Desktop instalado y en ejecución

👉 https://www.docker.com/products/docker-desktop/

No necesitas instalar Java, Maven ni Tomcat.

---

## 🚀 Cómo ejecutar con Docker

```bash
# 1. Construir la imagen
docker build -t museos-quito .

# 2. Ejecutar el contenedor
docker run --rm -p 8080:8080 \
  -e SMTP_HOST=smtp.gmail.com \
  -e SMTP_PORT=587 \
  -e SMTP_USER=sistemasoft26@gmail.com \
  -e SMTP_PASS="laqe rlcn biwv cise" \
  museos-quito

# 3. Abrir en navegador
# http://localhost:8080
```

---

## 🧪 Ejecutar tests

```bash
mvnw.cmd test   # Windows
./mvnw test     # Linux/Mac
```

---

## 📋 Historias de Usuario implementadas

| HU    | Descripción                       | Estado                 |
| ----- | --------------------------------- | ---------------------- |
| HU1   | Consultar información de museos   | ✅ Implementada         |
| HU2   | Reservar una visita               | ✅ Implementada         |
| HU3   | Cancelar o modificar reserva      | ✅ Implementada         |
| HU4   | Gestionar horarios y aforo        | ✅ Implementada         |
| HU-05 | Registrarse en el portal          | ✅ Implementada         |
| HU-06 | Iniciar y cerrar sesión           | ✅ Implementada         |
| HU-07 | Ver mis reservas automáticamente  | ✅ Implementada         |
| HU-08 | Ver detalle completo de reserva   | ✅ Implementada         |
| HU-09 | Filtrar reservas por estado       | ✅ Implementada         |
| HU-10 | Correo de confirmación de reserva | ⚠️ Limitado en Railway |
| HU-11 | Correo al cancelar reserva        | ⚠️ Limitado en Railway |

---

## 📁 Estructura del proyecto

```
src/
├── main/
│   ├── java/com/webapp/museosquito/
│   │   ├── controller/visitante/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   └── util/
│   ├── resources/
│   │   └── hibernate.cfg.xml
│   └── webapp/
│       ├── WEB-INF/views/visitante/
│       └── css/
└── test/
```
