##############################################
## Dockerfile para el build del archivo apk ##
## Basado en Ubuntu 14.04                   ##
##############################################

# Seteo la imagen base (Ubuntu oficial, versión 14.04)
FROM ubuntu:14.04

# Mantiene: mart
LABEL maintainer="mart"

# Variables de entorno
ENV SDK_URL="https://dl.google.com/android/repository/sdk-tools-linux-3859397.zip" \
    ANDROID_HOME="/opt" \
    ANDROID_VERSION=26 \
    ANDROID_BUILD_TOOLS_VERSION=26.0.2

# Instalar Java SDK
RUN apt-get update \
    && apt-get install -y software-properties-common \
    && add-apt-repository -y ppa:webupd8team/java \
    && apt-get update \
    && echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections \
    && apt-get install -y oracle-java8-installer \
    && apt-get install oracle-java8-set-default

# Descargar Android SDK y aceptar licencias
RUN cd "$ANDROID_HOME" \
    && apt-get install -y curl \
    && apt-get install -y unzip \
    && curl -o sdk.zip $SDK_URL \
    && unzip sdk.zip \
    && rm sdk.zip \
    && yes | $ANDROID_HOME/tools/bin/sdkmanager --licenses

# Instalar Android SDK
RUN $ANDROID_HOME/tools/bin/sdkmanager --update \
    && $ANDROID_HOME/tools/bin/sdkmanager "build-tools;${ANDROID_BUILD_TOOLS_VERSION}" \
    "platforms;android-${ANDROID_VERSION}" \
    "platform-tools"

# Agrego soporte para librerias i386 
RUN dpkg --add-architecture i386 \
    && apt-get update \
    && apt-get install -y libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386

# Copio los directorios del repositorio
COPY ./ /home

# Inicio el build gradle
RUN cd /home \
    && ./gradlew clean \
    && ./gradlew assembleRelease

# Defino el directorio de trabajo
WORKDIR /home

# Defino el comando estándar
CMD ["bash"]
