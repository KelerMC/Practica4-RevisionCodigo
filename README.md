# Proyecto de Admisión - Reporte de SonarLint
### Alumno: Keler Modesto Calixto
 Código: 18200114
 Curso: Verificación y validación de software. 2024-2
## Reporte de problemas
## Archivo: Calificador.java

En este archivo se detectaron 14 problemas, aquí la captura de SonarLint:

![Problemas en Calificador.java](capturas/calificador-problems.jpg)
## 1. Identificación del 1er problema
### Don't use the query "SELECT *" (Lin. 32 col. 115); (Lín. 43, col. 115); (Lín. 80, col. 115)
- **Categoría**: Code Smell
- **Severidad**: Critical
### Muestra del código problemático
- **Antes**:
   ```java
   String query = "SELECT * FROM respuesta";
   String query = "SELECT * FROM clave";
   String query = "SELECT * FROM rango";
## Descripción del problema
- **Violación**: SonarLint detectó que el uso de `SELECT *` en una consulta SQL es un *code smell*, ya que puede afectar el **rendimiento**, **sostenibilidad** y la claridad del código. Se recomienda especificar las columnas necesarias en lugar de utilizar `SELECT *`.

## Explicación de la refactorización 
- **Corrección**: Se especificaron las columnas necesarias en lugar de utilizar `SELECT *`. Esto mejora el rendimiento y la claridad del código.
## Código corregido 
- **Después:**
  ```java
    String query = "SELECT ide_iIndice, res_vcRespuesta FROM respuesta";
    String query = "SELECT cla_iIndice, cla_iPosicion, cla_vcRespuesta FROM clave";
    Stirng query = "SELECT ran_iIndiceMinimo, ran_iIndiceMaximo, cla_iPosicion FROM rango";
Con esto se redujeron 3 problemas con el SELECT*
## 2. Identificación del 2do problema
### 3. Make "respuestas", "clave" and "Rango" transient or serializable.
- **Categoría**: Code Smell
- **Severidad**: Major
## Muestra del código problemático
- **Antes**:
  ```java
  private List<Respuesta> respuestas;
  private List<Clave> claves;
  private List<Rango> rangos;
## Descripción del problema
- **Violación**: SonarLint detectó que el campo `Respuesta` deb ser marcado como `transient`o la clase `Respuesta`debe implementar `Serializable`para asegurar la correca serialización de la clase `Calificador`
## Explicación de la refactorización
- **Correción:** Se decidió hacer que el campo `respuesta`sea `transient`para evitar problema de serialización.
## Código corregido
- **Después:**
  ```java
  private transient List<Respuesta> respuestas;
  private transient List<Clave> claves;
  private transient List<Rango> rangos;
Se corrigió las demás clases. SonarLint indica que la variables que se mostró deberían ser "transient or serializable" y se debe a que se utiliza las clases como parte de un proceso que involucra concurrencia o serialización, y Java necesita asegurarse de que los objetos puedan ser serializados correctamente en esos contextos. Como estos campos no necesitan ser serializados, porque se usarán localmente dentro de la ejecución, se utilizará `transient`. Con esto se redujeron los problemas a 8.

## Archivo: DatabaseConnection.java

En este archivo se detectaron 7 problemas, aquí la captura de SonarLint:

![Problemas en DatabaseConnetion.java](capturas/databaseconnection-problems.jpg)

La mayoría de las advertencias son porque algunas sentencias como los `imports`no se utilizan, pero es porque el entorno original de desarrollo del código es `NetBeans` y se hace uso del `mysql-connector-j-8.4.0.jar`. Sin embargo, hay uno que viola la vulnerabilidad y se presentará a continuación.
## Identificación de problemas
### 3. Revoke and change this password, as it is compromised. (Lin. 32 col. 55)
- **Categoría**: Vulnerability
- **Severidad**: Critical

## Muestra del código problemático
- **Antes**:
  ```java
  public class DatabaseConnection {
      private static final String URL = "jdbc:mysql://localhost:3306/admisión_martes";
      private static final String USER = "root";
      private static final String PASSWORD = "root"; // Contraseña comprometida

      public static Connection getConnection() throws SQLException {
          return DriverManager.getConnection(URL, USER, PASSWORD);
      }
  }
## Descripción del problema
- **Violación**: SonarLint detectó que la contraseña está expuesta en el código fuente, lo cual es una vulnerabilidad crítica. Las contraseñas no deben estar hardcodeadas en el código fuente, ya que pueden ser comprometidas fácilmente.
## Explicación de la refactorización
- **Corrección**: Se cambió la contraseña comprometida por una nueva contraseña segura y se movió la configuración de la base de datos a un archivo de propiedades (`database.properties`) en la carpeta raíz para evitar la exposición de credenciales en el código fuente. Como se muestra en las dos siguientes imágenes.
![database.properties](capturas/database-properties.jpg)
![contenido.database.properties](capturas/contenido-database-properties.jpg)

Además se modifico el .gitignore para que obvie este archivo en el repositorio y solo el autor tenga acceso.
## Código corregido
- **Después:**
  ```java
    private static final String PROPERTIES_FILE = "database.properties";

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            props.load(fis);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }