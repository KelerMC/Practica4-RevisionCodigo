# Proyecto de Admisión - Reporte de SonarLint
## Alumno: Keler Modesto Calixto Código: 18200114
## Reporte de problemas
### Archivo: Calificador.java

En este archivo se detectaron 14 problemas, aquí la captura de SonarLint:

![Problemas en Calificador.java](capturas/calificador-problems.jpg)
## Identificación del problema
### 1. Don't use the query "SELECT *"(Lin. 32 col. 115)
## Descripción del problema
- **Violación**: SonarLint detectó que el uso de `SELECT *` en una consulta SQL es un *code smell*, ya que puede afectar el rendimiento y la claridad del código. Se recomienda especificar las columnas necesarias en lugar de utilizar `SELECT *`.
## Muestra del código
- **Antes**:
   ```java
   String query = ""SELECT * FROM respuesta"";
## Explicación de la refactorización 
- **Corrección**: Se especificaron las columnas necesarias en lugar de utilizar `SELECT *`. Esto mejora el rendimiento y la claridad del código.
## Código corregido 
- **Después:**
  ```java
    String query = "SELECT ide_iIndice, res_vcRespuesta FROM respuesta"