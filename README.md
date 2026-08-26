# Platmo - Aplicacion movil Android

Aplicacion Android nativa desarrollada en **Kotlin** con **Jetpack Compose**.
Permite registrar usuarios, iniciar sesion, gestionar nodos y escanear codigos QR.

Proyecto academico desarrollado en el contexto de la carrera de Analista
Programador (DUOC UC).

## Stack

- **Kotlin**
- **Jetpack Compose** para la interfaz
- **Room** para persistencia local
- **Retrofit** para el consumo de la API REST
- **CameraX / ML Kit** para el escaneo de codigos QR
- **Arquitectura MVVM**

## Arquitectura

El proyecto sigue el patron **MVVM** (Model-View-ViewModel):

```
view/         -> pantallas en Jetpack Compose (Login, Register, Nodos, QR)
controller/   -> ViewModels con la logica de presentacion
model/        -> modelos de dominio
data/
  dao/        -> acceso a la base de datos local (Room)
  entity/     -> entidades de Room
  database/   -> configuracion de la base de datos
  repository/ -> capa de repositorio
network/      -> configuracion de Retrofit
api/          -> definicion de los endpoints
qr/           -> componente de escaneo de codigos QR
```

## Funcionalidades

- Registro e inicio de sesion de usuarios
- Sesion persistente local (Room)
- Listado y gestion de nodos
- Escaneo de codigos QR con la camara
- Seccion "Quienes somos"

## Backend

Esta aplicacion consume la API REST del repositorio
[back-end-app](https://github.com/pablomzzz/back-end-app) (Spring Boot + MySQL).

## Como ejecutarlo

1. Clona el repositorio y abrelo en **Android Studio**.
2. Levanta el backend (ver su README).
3. Ajusta la URL base de la API en `network/RetrofitProvider.kt` si es necesario.
   - Para el emulador de Android, `localhost` del PC es `10.0.2.2`.
4. Ejecuta la app en un emulador o dispositivo fisico.

## Requisitos

- Android Studio
- JDK 17
- SDK de Android
