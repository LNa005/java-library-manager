# Sistema de Gestión Bibliotecaria SQL Pro

Aplicación de escritorio desarrollada en Java (Swing) para la gestión integral del inventario y préstamos de una biblioteca. El sistema utiliza SQLite como motor de base de datos local y está diseñado bajo el patrón arquitectónico DAO (Data Access Object) para asegurar una correcta separación de responsabilidades.

## 🚀 Características Principales

* **Autenticación Segura:** Sistema de acceso restringido con cifrado unidireccional (Hashing) mediante algoritmo BCrypt con salting automático.
* **Gestión de Inventario (CRUD):** Altas, bajas y consultas de libros en tiempo real.
* **Control de Préstamos:** Registro del estado actual del libro y fechas de salida.
* **Persistencia de Datos:** Almacenamiento local mediante una base de datos embebida (`biblioteca.db`).
* **Arquitectura Limpia:** Lógica de negocio (Modelos), acceso a datos (DAO) e interfaz gráfica (Vistas) completamente desacoplados.

## 📂 Estructura del Proyecto

El repositorio está organizado siguiendo estándares clásicos de desarrollo Java:

* `/src`: Código fuente (`.java`).
  * `Modelos:` `Libro.java`
  * `Vistas:` `VentanaBiblioteca.java`, `Login.java`
  * `Data Access:` `LibroDAO.java`, `UsuarioDAO.java`
* `/lib`: Dependencias de terceros (Driver `sqlite-jdbc` y `jBcrypt`).
* `/asset`: Recursos visuales y multimedia de la interfaz.

## 🛠️ Tecnologías y Requisitos

* **Lenguaje:** Java (JDK 8 o superior)
* **Interfaz Gráfica:** Java Swing (UIManager del sistema nativo)
* **Base de Datos:** SQLite 3
* **Seguridad:** jBcrypt 0.4
* **Control de Versiones:** Git

## ⚙️ Instalación y Ejecución

### Entorno de Desarrollo
1. Clonar el repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
Configurar el Build Path en tu IDE para incluir los archivos .jar ubicados en la carpeta /lib.

Compilar el proyecto y ejecutar la clase principal Login.java.

Ejecución en Producción (Autónoma)
El sistema puede ejecutarse de forma independiente sin necesidad de un IDE mediante su artefacto empaquetado (.jar):

Abrir una terminal en el directorio donde se encuentre el archivo ejecutable.

Lanzar la aplicación:

Bash

java -jar BibliotecaJava.jar
Nota: En la primera ejecución, el sistema generará automáticamente la base de datos biblioteca.db y un usuario administrador por defecto (admin / 1234).

⚠️ Notas de Desarrollo
El control de versiones excluye los archivos compilados (.class), los ejecutables empaquetados (.jar) y la base de datos local (.db) mediante .gitignore para mantener el repositorio limpio y proteger los datos en producción.

🚧 Estado del Proyecto (TODO)
[PENDIENTE] Refactorización y auditoría del módulo LibroDAO.java para optimizar las operaciones CRUD del inventario y mitigar posibles inyecciones SQL.