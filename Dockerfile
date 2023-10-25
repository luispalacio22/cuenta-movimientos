# Utiliza una imagen base con Java 8 preinstalado
FROM openjdk:8-jdk-alpine

# Establece las variables de entorno para Java
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $JAVA_HOME/bin:$PATH

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo pom.xml y el código fuente
COPY pom.xml /app/
COPY src /app/src/

# Ejecuta el comando Maven para compilar la aplicación
RUN apk --no-cache add maven
RUN mvn clean install

# Copia el archivo JAR generado por Maven a la carpeta de la aplicación en el contenedor
COPY target/cuenta-movimiento-0.0.1-SNAPSHOT.jar /app/app.jar

# Exponer el puerto en el que se ejecutará la aplicación (ajusta según tus necesidades)
EXPOSE 8080

# Comando para ejecutar tu aplicación (ajusta según tus necesidades)
CMD ["java", "-jar", "app.jar"]
