# Cafetería Backend - Sistema de Microservicios

Este repositorio contiene la arquitectura de microservicios para la gestión de una cafetería, desarrollada con **Spring Boot** y **Spring Cloud**.

## 🏗 Arquitectura
* **Discovery Server:** Eureka Server (Puerto: 8761)
* **API Gateway:** Spring Cloud Gateway MVC (Puerto: 8090)
* **Microservicio Catálogo:** (Puerto: 8081)
* **Microservicio Pedidos:** (Puerto: 8082)

---

## 🛠 Requisitos de Instalación

Para levantar este proyecto en tu entorno local, asegúrate de cumplir con los siguientes requisitos previos:

### 1. Entorno de Desarrollo
* **Java Development Kit (JDK) 17 o superior:** Asegúrate de tenerlo instalado y configurado en tus variables de entorno (`JAVA_HOME`).
* **Maven 3.8+:** Para la gestión de dependencias y compilación de los módulos.
* **IDE:** Recomendado **Spring Tool Suite (STS)**, IntelliJ IDEA o Eclipse.

## 2. Herramientas de Infraestructura
* **Servidor de Base de Datos:** PostgreSQL.
* **RabbitMQ:** Asegúrate de que el servidor esté activo en `localhost:15672`.

### Configuración de Bases de Datos
Antes de ejecutar los microservicios, debes crear las bases de datos en tu servidor PostgreSQL. Ejecuta los siguientes comandos en tu cliente SQL (pgAdmin, DBeaver o terminal):

```sql
CREATE DATABASE db_pedidos;
CREATE DATABASE db_catalogo;
```

### 3. Configuración del IDE (STS/Eclipse)
Este proyecto está configurado como un **Monorepo de Maven**. Sigue estos pasos para importar toda la arquitectura de una sola vez:

1. **Clona el repositorio:** `git clone https://github.com/AKUmajinn/cafeteria-backend-monorepo.git`

2. **Importación Única:**
   * En tu IDE (STS o Eclipse), selecciona **File > Import...**
   * Elige **Maven > Existing Maven Projects**.
   * En "Root Directory", selecciona únicamente la carpeta raíz: `cafeteria-backend`.
   * Asegúrate de que el checkbox **"Search for nested projects"** esté activado (esto permite que el IDE detecte automáticamente el `gateway`, `eureka`, `catalogo` y `pedidos`).
   * Haz clic en **Finish**.

3. **Sincronización:**
   * El IDE reconocerá el proyecto padre y todos sus submódulos.
   * Espera a que Maven descargue e indexe todas las dependencias. Una vez finalizado, verás cada microservicio como un proyecto independiente dentro de tu *Package Explorer*.

---

## 🚀 Orden de Ejecución

Es fundamental levantar los servicios en el siguiente orden para asegurar la integridad de la arquitectura:

1. **Infraestructura:** Inicia tus contenedores de **PostgreSQL** (o pgadmin en caso lo uses local) y **RabbitMQ** (vía Docker o servicios locales tambien).
2. **Eureka Server:** Inicia `EurekaApplication`. (Es el primero de los proyectos Java en levantar).
3. **Microservicios:** Inicia `CatalogoApplication` y `PedidosApplication`. 
   * *Nota: Espera unos segundos a que cada uno registre su estado (UP) en el panel de Eureka (`http://localhost:8761`).*
4. **API Gateway:** Inicia `ApiGatewayApplication`. (Es el último, ya que depende de que Eureka tenga el mapa de los servicios registrados).

Una vez iniciados, puedes verificar que todo esté funcionando accediendo al dashboard de Eureka en: `http://localhost:8761`.

---

## 📝 Notas del Proyecto
* Este proyecto está estructurado como un **Monorepo** para facilitar la gestión del ciclo de vida de los microservicios.
* Las rutas de enrutamiento del Gateway están configuradas mediante código Java (filtro `lb()`) para garantizar una integración robusta con el balanceador de carga :D.

![Pochita programando](https://i.pinimg.com/originals/8d/5d/59/8d5d5963f1e30cc2c4d8d1f8ba116ae3.gif)
> Cuando escribí este codigo solo dios y yo sabiamos como levantarlo, ahora solo Dios sabe.