# Proyecto de Admisión - Reporte de SonarLint
## Alumno: Keler Modesto Calixto Código: 18200114
## Reporte de problemas
### Archivo: Calificador.java

En este archivo se detectaron varios problemas, aquí la captura de SonarLint:

![Problemas en Calificador.java](capturas/calificador-problems.jpg)

### 1. Don't use the query "SELECT *"(Lin. 32 col. 115)

- **Violación**: SonarLint detectó que el uso de `SELECT *` en una consulta SQL es un *code smell*, ya que puede afectar el rendimiento y la claridad del código. Se recomienda especificar las columnas necesarias en lugar de utilizar `SELECT *`.
- **Antes**:
   ```java
   String query = ""SELECT * FROM respuesta"";

