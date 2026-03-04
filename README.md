# Sistema de Gestión Bibliotecaria SQL Pro

Aplicación de escritorio en Java (Swing) para la gestión del inventario y préstamos de una biblioteca. Implementa SQLite como motor de base de datos embebido y sigue el patrón arquitectónico DAO (Data Access Object) para la separación de la lógica de negocio y el acceso a datos.

## 🚀 Características

* **Autenticación Criptográfica:** Control de acceso mediante algoritmo BCrypt (hashing con *salting* automático).
* **CRUD de Inventario:** Altas, bajas y actualizaciones en tiempo real, operando estrictamente mediante claves primarias (`id`) para mitigar inyecciones SQL y garantizar la integridad referencial.
* **Sistema de Préstamos:** Trazabilidad de estados y registro automático de fechas de salida.
* **Ejecución Autónoma:** Empaquetado en un único artefacto `.jar` que incluye dependencias y motor de base de datos.

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java (JDK 8+)
* **GUI:** Java Swing (UIManager nativo)
* **Base de Datos:** SQLite 3 (Driver JDBC)
* **Seguridad:** jBcrypt 0.4

## ⚙️ Despliegue y Ejecución

### Entorno de Producción (Recomendado)
El sistema está compilado como un ejecutable independiente. No requiere instalación de bases de datos externas.
1. Descargar el archivo `BibliotecaJava.jar`.
2. Ejecutar desde la terminal en el directorio de descarga:
   ```bash
   java -jar BibliotecaJava.jar
Nota: En el primer arranque, el sistema inicializará el esquema SQL de forma automática y creará el usuario administrador por defecto (admin / 1234).

Entorno de Desarrollo
Clonar el repositorio.

Configurar el Build Path del IDE para enlazar los binarios de la carpeta /lib (jbcrypt-0.4.jar y sqlite-jdbc-3.51.2.0.jar).

Ejecutar el punto de entrada principal: Login.java.

🗺️ Roadmap y Próximas Implementaciones (TODO)
El núcleo arquitectónico del sistema está estabilizado. Las próximas iteraciones se centrarán en la expansión de funcionalidades de negocio:

[EN PLANIFICACIÓN] Exportación de Reportes: Generación de informes de inventario y préstamos activos en formato PDF.

[EN PLANIFICACIÓN] Gestión de Usuarios y Roles: Módulo para el alta de bibliotecarios y lectores con diferentes niveles de privilegios.

[EN PLANIFICACIÓN] Sistema de Penalizaciones: Cálculo automático de multas y bloqueos por devoluciones fuera de plazo.