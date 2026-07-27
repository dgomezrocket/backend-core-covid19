# 🏥 CroniApp/CroniWeb — Backend COVID-19

Backend para el registro, monitoreo y seguimiento de pacientes con COVID-19, desarrollado con **Spring Boot**.

El sistema permite administrar pacientes, médicos, hospitales, formularios de síntomas, enfermedades de base y mensajes entre pacientes y profesionales de la salud.

---

## 📋 Tabla de contenidos

- [Características](#-características)
- [Tecnologías](#️-tecnologías)
- [Requisitos previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#️-configuración)
- [Ejecución](#-ejecución)
- [Despliegue](#-despliegue)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [API Endpoints](#-api-endpoints)
- [Contribuciones](#-contribuciones)
- [Licencia](#-licencia)
- [Autores](#-autores)
- [Agradecimientos](#-agradecimientos)
- [Contacto](#-contacto)

---

## ✨ Características

- 🔐 **Autenticación y autorización** mediante JWT.
- 👥 **Gestión de usuarios**, incluyendo:
    - Pacientes.
    - Médicos.
    - Coordinadores.
    - Administradores.
- 🏥 **Administración de hospitales** con información geográfica.
- 📋 **Formularios dinámicos** para registrar síntomas y enfermedades de base.
- 💬 **Sistema de mensajería** entre pacientes y profesionales de la salud.
- 📧 **Envío de correos electrónicos** para:
    - Verificación de cuentas.
    - Recuperación de contraseñas.
- 📊 **Carga masiva de datos** mediante archivos Excel.
- 🗺️ **Geolocalización** para consultar hospitales cercanos.
- 🔍 **Asignación automática de médicos** según la ubicación del paciente.

---

## 🛠️ Tecnologías

El proyecto utiliza las siguientes tecnologías:

- **Java 8**
- **Spring Boot 2.2.5**
    - Spring Web.
    - Spring Data JPA.
    - Spring Security.
    - Spring Boot Starter Mail.
- **PostgreSQL**
- **JWT — JSON Web Tokens**
- **Lombok**
- **Apache POI**
- **Maven**
- **Docker**
- **Docker Compose**

---

## 📦 Requisitos previos

Antes de ejecutar el proyecto, asegurate de tener instaladas las siguientes herramientas:

- [Java JDK 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) o una versión compatible.
- [Maven 3.6 o superior](https://maven.apache.org/download.cgi).
- [PostgreSQL](https://www.postgresql.org/download/).
- [Git](https://git-scm.com/downloads).
- [Docker](https://www.docker.com/get-started), opcional.

Para verificar las versiones instaladas:

```bash
java -version
mvn -version
git --version
docker --version
```

---

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/backend-core-covid19.git
cd backend-core-covid19
```

### 2. Crear la base de datos

Ingresá a PostgreSQL y ejecutá:

```sql
CREATE DATABASE covid19;

CREATE USER covid19
WITH PASSWORD 'covid19';

GRANT ALL PRIVILEGES
ON DATABASE covid19
TO covid19;
```

> [!WARNING]
> Las credenciales anteriores son únicamente un ejemplo para desarrollo local.  
> En producción se debe utilizar una contraseña segura.

### 3. Ejecutar el script de inicialización

El proyecto incluye el archivo `init.sql`, que puede utilizarse para cargar los datos iniciales de la aplicación.

```bash
psql -U covid19 -d covid19 -f init.sql
```

El script puede incluir datos como:

- Roles.
- Estados.
- Usuarios de prueba.
- Formularios.
- Parámetros iniciales del sistema.

---

## ⚙️ Configuración

El proyecto utiliza variables de entorno para administrar información sensible como las credenciales de la base de datos y del servidor de correo.

### Desarrollo local

Creá el siguiente archivo:

```text
src/main/resources/application-local.properties
```

Ejemplo de configuración:

```properties
# =====================================
# SERVIDOR
# =====================================

server.port=${PORT:9900}


# =====================================
# BASE DE DATOS
# =====================================

spring.datasource.url=${DATABASE_URL:jdbc:postgresql://127.0.0.1:5432/covid19}
spring.datasource.username=${DATABASE_USERNAME:covid19}
spring.datasource.password=${DATABASE_PASSWORD:covid19}

spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false


# =====================================
# CORREO ELECTRÓNICO
# =====================================

spring.mail.host=${MAIL_SMTP_HOST:smtp.gmail.com}
spring.mail.port=${MAIL_SMTP_PORT:587}
spring.mail.username=${MAIL_SMTP_USER}
spring.mail.password=${MAIL_SMTP_PASS}

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true


# =====================================
# URLS DE LA APLICACIÓN
# =====================================

app.url.backend=${URL_BACKEND:http://localhost:9900}
app.url.frontend=${URL_FRONTEND:http://localhost:8081}
```

> [!IMPORTANT]
> El archivo `application-local.properties` puede contener información sensible y no debe subirse al repositorio.

Agregalo al archivo `.gitignore`:

```gitignore
src/main/resources/application-local.properties
.env
```

### Variables de entorno

Para ambientes de producción, configurá las siguientes variables:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `PORT` | Puerto utilizado por el servidor | `9900` |
| `DATABASE_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://host:5432/covid19` |
| `DATABASE_USERNAME` | Usuario de PostgreSQL | `covid19` |
| `DATABASE_PASSWORD` | Contraseña de PostgreSQL | `contraseña_segura` |
| `MAIL_SMTP_HOST` | Servidor SMTP | `smtp.gmail.com` |
| `MAIL_SMTP_PORT` | Puerto SMTP | `587` |
| `MAIL_SMTP_USER` | Correo utilizado como remitente | `tu-email@gmail.com` |
| `MAIL_SMTP_PASS` | Contraseña de aplicación del correo | `xxxx xxxx xxxx xxxx` |
| `URL_BACKEND` | URL pública del backend | `https://api.tudominio.com` |
| `URL_FRONTEND` | URL pública del frontend | `https://tudominio.com` |

### Configuración de Gmail

Para enviar correos mediante Gmail:

1. Ingresá a la configuración de tu cuenta de Google.
2. Activá la verificación en dos pasos.
3. Generá una contraseña de aplicación.
4. Seleccioná la opción correspondiente a correo.
5. Utilizá la contraseña generada en la variable `MAIL_SMTP_PASS`.

> [!NOTE]
> No utilices la contraseña normal de tu cuenta de Google dentro de la aplicación.

---

## 🏃 Ejecución

### Ejecución con Maven

```bash
mvn spring-boot:run
```

### Ejecución con Maven Wrapper

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

### Ejecución con el perfil local

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Una vez iniciada, la API estará disponible en:

```text
http://localhost:9900
```

### Generar el archivo JAR

```bash
mvn clean package
```

El archivo generado estará disponible dentro del directorio:

```text
target/
```

Para ejecutarlo:

```bash
java -jar target/backend-core-covid19.jar
```

El nombre exacto del archivo puede variar según la configuración definida en el archivo `pom.xml`.

---

## 🐳 Ejecución con Docker

### Construir la imagen

```bash
docker build -t covid19-backend .
```

### Ejecutar el contenedor

```bash
docker run --name covid19-backend \
  -p 9900:9900 \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/covid19 \
  -e DATABASE_USERNAME=covid19 \
  -e DATABASE_PASSWORD=covid19 \
  -e MAIL_SMTP_USER=tu-email@gmail.com \
  -e MAIL_SMTP_PASS=tu-contraseña-de-aplicacion \
  -e URL_BACKEND=http://localhost:9900 \
  -e URL_FRONTEND=http://localhost:8081 \
  covid19-backend
```

En Windows PowerShell:

```powershell
docker run --name covid19-backend `
  -p 9900:9900 `
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/covid19 `
  -e DATABASE_USERNAME=covid19 `
  -e DATABASE_PASSWORD=covid19 `
  -e MAIL_SMTP_USER=tu-email@gmail.com `
  -e MAIL_SMTP_PASS=tu-contraseña-de-aplicacion `
  -e URL_BACKEND=http://localhost:9900 `
  -e URL_FRONTEND=http://localhost:8081 `
  covid19-backend
```

### Docker Compose

Para iniciar todos los servicios:

```bash
docker compose up -d
```

Para visualizar los registros:

```bash
docker compose logs -f
```

Para detener los servicios:

```bash
docker compose down
```

Para reconstruir las imágenes:

```bash
docker compose up -d --build
```

---

## 🌐 Despliegue

El proyecto puede desplegarse en plataformas compatibles con aplicaciones Java o contenedores Docker.

### Railway

Procedimiento general:

1. Crear un proyecto en Railway.
2. Conectar el repositorio de GitHub.
3. Agregar un servicio PostgreSQL.
4. Configurar las variables de entorno.
5. Verificar que Railway detecte el archivo `Dockerfile`.
6. Configurar el dominio público del backend.
7. Actualizar las variables `URL_BACKEND` y `URL_FRONTEND`.

Variables mínimas recomendadas:

```text
PORT
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD
MAIL_SMTP_HOST
MAIL_SMTP_PORT
MAIL_SMTP_USER
MAIL_SMTP_PASS
URL_BACKEND
URL_FRONTEND
```

> [!IMPORTANT]
> Algunas plataformas proporcionan una URL de PostgreSQL con el formato:
>
> ```text
> postgresql://usuario:contraseña@host:puerto/base_de_datos
> ```
>
> Spring Boot normalmente necesita una URL JDBC:
>
> ```text
> jdbc:postgresql://host:puerto/base_de_datos
> ```

---

## 📁 Estructura del proyecto

```text
backend-core-covid19/
├── src/
│   ├── main/
│   │   ├── java/com/core/covid19/
│   │   │   ├── authentication/       # JWT y filtros de seguridad
│   │   │   ├── controllers/          # Controladores REST
│   │   │   ├── models/
│   │   │   │   ├── entities/         # Entidades JPA
│   │   │   │   ├── requests/         # DTO de entrada
│   │   │   │   └── responses/        # DTO de salida
│   │   │   ├── repos/                # Repositorios JPA
│   │   │   ├── security/             # Configuración de Spring Security
│   │   │   ├── services/             # Lógica de negocio
│   │   │   └── CoreCovid19Application.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── docker/
├── Dockerfile
├── docker-compose.yml
├── init.sql                          # Script de inicialización de la BD
├── pom.xml
└── README.md
```

### Descripción de las principales carpetas

| Carpeta | Descripción |
|---|---|
| `authentication` | Componentes relacionados con JWT y autenticación |
| `controllers` | Endpoints y controladores REST |
| `models/entities` | Entidades persistidas mediante JPA |
| `models/requests` | Objetos utilizados para recibir datos |
| `models/responses` | Objetos utilizados para devolver respuestas |
| `repos` | Interfaces de acceso a datos |
| `security` | Configuración de Spring Security |
| `services` | Reglas y lógica de negocio |
| `resources` | Archivos de configuración y recursos |

---

## 🔌 API Endpoints

La siguiente sección presenta los principales endpoints disponibles en la API.

> [!NOTE]
> Los endpoints protegidos requieren el envío de un token JWT válido en la cabecera `Authorization`.

Ejemplo:

```http
Authorization: Bearer TOKEN_JWT
```

### Autenticación

| Método | Endpoint | Descripción | Autenticación |
|---|---|---|---|
| `POST` | `/authentication/authenticate` | Iniciar sesión | No |
| `POST` | `/accounts/signup` | Registrar un usuario | No |
| `POST` | `/accounts/send-email` | Solicitar recuperación de contraseña | No |
| `GET` | `/accounts/verify?jwt=TOKEN` | Verificar el correo electrónico | No |

### Pacientes

| Método | Endpoint | Descripción | Autenticación |
|---|---|---|---|
| `GET` | `/persons` | Listar pacientes | Sí |
| `POST` | `/persons` | Crear un paciente | Sí |
| `PUT` | `/persons` | Actualizar un paciente | Sí |
| `DELETE` | `/persons` | Eliminar un paciente | Sí |

### Médicos

| Método | Endpoint | Descripción | Autenticación |
|---|---|---|---|
| `POST` | `/accounts/doctor` | Crear un médico | Sí |
| `GET` | `/accounts/doctors` | Listar médicos | Sí |
| `DELETE` | `/accounts/doctor/{id}` | Eliminar un médico | Sí |
| `POST` | `/accounts/doctors/cargar` | Realizar una carga masiva desde Excel | Sí |

### Hospitales

| Método | Endpoint | Descripción | Autenticación |
|---|---|---|---|
| `GET` | `/hospitals` | Listar hospitales | Sí |
| `GET` | `/hospitals/my` | Consultar hospitales cercanos | Sí |
| `POST` | `/hospitals` | Crear un hospital | Sí |
| `DELETE` | `/hospitals/{id}` | Eliminar un hospital | Sí |

### Mensajes

| Método | Endpoint | Descripción | Autenticación |
|---|---|---|---|
| `GET` | `/messages` | Obtener mensajes | Sí |
| `POST` | `/messages` | Enviar un mensaje | Sí |

### Formularios y respuestas

| Método | Endpoint | Descripción | Autenticación |
|---|---|---|---|
| `GET` | `/forms` | Listar formularios | Sí |
| `POST` | `/answers` | Enviar respuestas de un formulario | Sí |
| `GET` | `/answers` | Obtener respuestas registradas | Sí |

### Ejemplo de autenticación

Solicitud:

```bash
curl --request POST \
  --url http://localhost:9900/authentication/authenticate \
  --header "Content-Type: application/json" \
  --data '{
    "email": "usuario@ejemplo.com",
    "password": "contraseña"
  }'
```

Ejemplo de solicitud autenticada:

```bash
curl --request GET \
  --url http://localhost:9900/persons \
  --header "Authorization: Bearer TOKEN_JWT"
```

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas.

Para colaborar con el proyecto:

1. Realizá un fork del repositorio.
2. Creá una rama para tu funcionalidad:

```bash
git checkout -b feature/nueva-funcionalidad
```

3. Realizá los cambios necesarios.
4. Registrá los cambios:

```bash
git add .
git commit -m "Agrega nueva funcionalidad"
```

5. Subí la rama:

```bash
git push origin feature/nueva-funcionalidad
```

6. Abrí un Pull Request.

También pueden utilizarse prefijos como:

```text
feature/   Nueva funcionalidad
fix/       Corrección de errores
docs/      Cambios en documentación
refactor/  Refactorización de código
test/      Incorporación o modificación de pruebas
```

---

## 📄 Licencia

Este proyecto se distribuye bajo la **Licencia MIT**.

Consultá el archivo [`LICENSE`](LICENSE) para obtener más información.

---

## 👥 Autores

- **Jesús Aguilera** — Desarrollo inicial.
- **Derlis Gómez**   — Adecuaciones y mejoras de funcionalidades
- GitHub: [@dgomezrocket]([(https://github.com/dgomezrocket/)])

---

## 🙏 Agradecimientos

- A la **Facultad Politécnica de la Universidad Nacional de Asunción**.
- Al equipo de desarrollo de **CroniWeb**.
- A los profesionales de la salud que colaboraron con el proyecto.
- A todas las personas que participaron en el diseño, desarrollo y evaluación del sistema.

---

## 📞 Contacto

Para consultas, sugerencias o reporte de errores:

- **Correo:** derlisrgomez@pol.una.py
- **GitHub:** [@dgomezrocket]([https://github.com/tu-usuario](https://github.com/dgomezrocket/))
- **Sitio web:** [https://rocketpy.com]([https://rocketpy.com](https://www.rocketpy.com/))

Los errores también pueden reportarse mediante la sección de **Issues** del repositorio.

---

⭐ Si este proyecto te resultó útil, podés apoyar su desarrollo agregándole una estrella en GitHub.