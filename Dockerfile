##############################################
## Dockerfile para el build del archivo apk ##
## Basado en Ubuntu 14.04                   ##
##############################################

# Seteo la imagen base (Ubuntu oficial, versión 14.04)
FROM ubuntu:14.04

# Mantiene: mart
LABEL maintainer="mart"

# TODO: Usar si hace falta
# Copio los directorios del repositorio
# COPY ./ /home

# TODO: Cambiar al directorio adecuado
# Defino el directorio para correr
# WORKDIR /home

# TODO: Modificar a necesidad
# (Ejemplo) Corro el script de instalación
# RUN chmod 777 instalar_y_correr_cosas.sh && ./instalar_y_correr_cosas.sh

# Defino el comando estándar
CMD ["bash"]
