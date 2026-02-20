# Sistema de Gestión Bibliotecaria SQL Pro

Aplicación de escritorio desarrollada en Java (Swing) para la gestión integral del inventario y préstamos de una biblioteca. El sistema utiliza SQLite como motor de base de datos local y está diseñado bajo el patrón arquitectónico DAO (Data Access Object) para asegurar una correcta separación de responsabilidades.

## 🚀 Características Principales

* **Autenticación Segura:** Sistema de acceso restringido mediante credenciales.
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
* `/lib`: Dependencias de terceros (Driver `sqlite-jdbc`).
* `/asset`: Recursos visuales y multimedia de la interfaz.

## 🛠️ Tecnologías y Requisitos

* **Lenguaje:** Java (JDK 8 o superior recomendado)
* **Interfaz Gráfica:** Java Swing (UIManager del sistema nativo)
* **Base de Datos:** SQLite 3
* **Control de Versiones:** Git

## ⚙️ Instalación y Ejecución

1. Clonar el repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>

2. Configurar el Build Path o Classpath en tu IDE (Eclipse, IntelliJ, VSCode) para incluir el archivo .jar ubicado en la carpeta /lib.

3. Compilar el proyecto y ejecutar la clase principal Login.java.

4. El sistema generará automáticamente la base de datos biblioteca.db y un usuario administrador por defecto (admin / 1234) en la primera ejecución.

⚠️ Notas de Desarrollo
El control de versiones excluye los archivos compilados (.class), los ejecutables y la base de datos local mediante .gitignore para mantener el repositorio limpio y proteger los datos en producción.

🚧 Estado del Proyecto y Deuda Técnica (TODO)
Este sistema se encuentra en fase de desarrollo iterativo. Actualmente existen mejoras arquitectónicas y parches de seguridad programados para las próximas versiones:

[CRÍTICO] Seguridad de Autenticación: El sistema actual almacena credenciales en texto plano en la base de datos. Está programada la refactorización de la clase UsuarioDAO para implementar cifrado unidireccional (Hashing) mediante BCrypt o SHA-256.

Distribución y Despliegue: Pendiente la configuración del empaquetado del proyecto y sus dependencias (driver de SQLite) en un artefacto ejecutable autónomo (.jar).