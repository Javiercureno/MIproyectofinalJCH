# Mi proyecto Final es un TODO App "Mis Pendientes"📋

TODO App es una aplicación de lista de tareas que permite a los usuarios agregar, marcar como completadas y eliminar tareas de manera intuitiva. La aplicación incluye una interfaz moderna y minimalista, compatible con modos oscuro y claro.

## Características 🚀

- **Agregar tareas:** Añade nuevas tareas a la lista.
- **Marcar tareas completadas:** Usa un checkbox para marcar tareas como completadas, y el texto se mostrará con una línea tachada.
- **Eliminar tareas:** Elimina tareas de la lista con un botón de eliminación.
- **Filtro de tareas:** Muestra solo las tareas completadas o todas las tareas.
- **Modo Oscuro/Claro:** Alterna entre modo oscuro y claro según las preferencias del usuario.
- **Pantalla de Bienvenida:** Una animación inicial se muestra al abrir la aplicación.

## Componentes Utilizados 🛠️

La aplicación incluye los siguientes componentes de UI básicos, de acuerdo con los requisitos de diseño:

- **TextField**: Para ingresar nuevas tareas.
- **Checkbox**: Para marcar cada tarea como completada.
- **Button**: Para agregar y eliminar tareas.
- **Image**: Imagen relevante para la aplicación.
- **Switch**: Controla el modo oscuro y el filtro de visualización de tareas completadas.

## Tecnologías Utilizadas 💻

- **Kotlin**: Lenguaje de programación principal.
- **Jetpack Compose**: Framework de UI declarativa de Android para crear interfaces de usuario modernas.
- **Material 3**: Utiliza componentes de Material Design para una apariencia moderna y estilizada.

## Estructura de Estado ⚙️

La aplicación gestiona el estado de la interfaz de usuario en tiempo real, utilizando `mutableStateOf` para manejar variables de estado. Esto permite:

- Cambios en la lista de tareas al agregar o eliminar tareas.
- Alternar el modo oscuro o claro.
- Actualizar el estado de las tareas al marcarlas como completadas.

