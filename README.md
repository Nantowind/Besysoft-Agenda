# Besysoft-Agenda
Desafio para Besysoft

# Documentacion API
https://documenter.getpostman.com/view/27149453/2s93eSYEZT

Consideraciones:
Dentro de el software existe un archivo

Introducción
El proyecto Agenda Spring Boot es un desafío técnico propuesto por Besysoft, que busca desarrollar una aplicación de agenda utilizando el framework Spring Boot. La aplicación permite almacenar y gestionar información sobre personas, empresas y sus contactos. A continuación, se detallan las funcionalidades básicas y requisitos del proyecto.

Funcionalidades
Almacenamiento de datos sobre personas y empresas, así como los contactos asociados a las empresas.

Posibilidad de agregar un contacto a una empresa, siempre y cuando dicho contacto haya sido previamente agregado a la agenda.

Una empresa puede tener varios contactos.
Implementación de diferentes tipos de buscadores de personas (por nombre, por ciudad, etc.).

Utilización de herramientas de logueo.

Búsqueda de personas en varias ciudades (por ejemplo, todos los 'Juan Pérez' de 'Buenos Aires' y 'Córdoba').

Manejo adecuado de excepciones en la agenda.

Diseño de un Diagrama de Entidad Relación para la solución.

Almacenamiento de datos en una base de datos.

Ejecución de las distintas funciones a través de servicios REST, que puedan ser probados con herramientas como Postman o Swagger.

Requisitos

# Ejecución del proyecto
Este proyecto se puede ejecutar con dos configuraciones diferentes: una directamente con una base de datos y otra a través de H2.

Configuración con una base de datos
Para esta API se usó PostgreSQL, pero puedes modificar el archivo application.properties para usar el motor de base de datos que prefieras.

A continuación se muestran las configuraciones del archivo application.properties para PostgreSQL:
Base de datos PostgreSQL

spring.datasource.url=jdbc:postgresql://localhost:5432/Besysoft_Agenda
spring.datasource.username= [nombre de usuario]
spring.datasource.password= [contraseña]

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
server.port=8080

También se incluye en la carpeta resources el archivo dump.sql que contiene los scripts para crear la estructura de la base de datos.

Configuración con H2
El proyecto ya incluye la configuración para usar H2. Cabe destacar que con esta configuración, los datos se mantendrán solo durante la ejecución del programa, es decir, cada vez que reiniciemos el programa los datos se reiniciarán y no estarán más.

A continuación se muestran las configuraciones del archivo application.properties para H2:

H2 conexión en memoria

spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
server.port=8080


# Login y acceso web
Para acceder a la parte web puedes hacerlo mediante /api/index encontraras como registrarte y logearte. Una vez que estés logueado, puedes acceder al resto de la funcionalidades.



A continuación se muestra el diagrama de relación de base de datos, que representa cómo se relacionan las diferentes entidades entre sí.

![Diagrama de relación de base de datos](https://drive.google.com/uc?id=1OmtQXtARmKA5BeSFCjzxJ_C4igQBVfvv)

En este esquema la relación entre la entidad "Persona" y la entidad "Empresa" no es directa, sino a través de la entidad "Contacto". La entidad "Contacto" actúa como un enlace entre "Persona" y "Empresa".



