# VehixCare 🚗🛠️

VehixCare es una aplicación diseñada para gestionar y visualizar información sobre vehículos, mantenimientos y seguros de una manera intuitiva y eficiente. Con esta herramienta, los usuarios pueden registrar, actualizar y consultar detalles clave relacionados con sus vehículos.

---

## 🚀 Funcionalidades principales

- **Gestión de Vehículos:** Visualiza y administra todos tus vehículos registrados.
- **Control de Mantenimientos:** Lleva un seguimiento de los mantenimientos realizados.
- **Gestión de Seguros:** Consulta y actualiza información sobre seguros asociados a tus vehículos.
- **Navegación simple:** Navega fácilmente entre secciones mediante una barra de navegación intuitiva.

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **Framework de UI:** Jetpack Compose
- **Arquitectura:**
  - `MVVM` (Model-View-ViewModel)
  - Repositorios para la gestión de datos
- **Gestión de datos:**
  - Room Database
  - StateFlow y Flow para la reactividad
- **Otras herramientas:**
  - Material Design 3 (MD3) para componentes visuales
  - Coroutines para operaciones asíncronas

---

## 📂 Estructura del proyecto

```plaintext
.
├── app
│   ├── model          # Clases de datos y modelos
│   ├── repository     # Lógica de acceso a datos
│   ├── view           # Componentes visuales y pantallas
│   ├── viewmodel      # Lógica de presentación y manejo de estado
│   ├── database       # Configuración y acceso a Room
│   ├── Screen         # Configuración de rutas de navegación
│   ├── DAO            # Configuracion de las consultas a la BD

📧 Contacto
Desarrollador: Juan Jose Ruiz
Correo: tu.correo@example.com
GitHub: TuUsuario
