# TFG Software 2024
# Nombre de la aplicación: Cosmic News
# Nombre del alumno: Pedro Cristino Moreno
# Nombre del Tutor: Óscar Soto
# Fase 0
## Funcionalidades:
### Funcionalidades básicas:
- Cabecera y desplegable: En cualquier parte de la web, el usuario tendrá acceso a la cabecera donde podrá iniciar o cerrar sesión, registrarse, perfil, volver al inicio... Así como podrá abrir un desplegable para acceder a las distintas funcionalidades de la web, como ver las noticias, fotos, el calendario...
- Registrarse / Iniciar sesión
- Sección noticias: Los usuarios podrán ver una lista de noticias que podrán ser ordenadas por fecha de publicación, likes...
- Sección fotos: Los usuarios podrán ver una lista de fotos que podrán ser ordenadas por fecha, likes...
- Perfil: Los usuarios podrán dar like (o quitarlo) a noticias y fotos para que les aparezcan en su perfil.
- Sección videos: Los usuarios podrán ver una lista de vídeos relacionados con la astronomía que serán cargados desde youtube.
- Sección calendario: Los usuarios podrán ver un calendario con eventos futuros y pasados relacionados con la astronomía y podrán marcar días para recibir notificación sobre dichos eventos.
- Sección sistema solar: Los usuarios podrán ver una representación dinámica del sistema solar y podrán aprender sobre los cuerpos celestes que lo componen.
- Sección quizzes: Los usuarios podrán realizar tests sobre temas de astronomía tratados en la web. Podrán ver en qué han fallado y recibirán una insignia, que podrán ver en su perfil, como recompensa al completar al 100% cada test por primera vez.

Los admins serán los encargados de publicar gestionar la creación, modificación y eliminación de noticias, fotos, vídeos, eventos en el calendario y quizzes.

## Aspectos principales de la aplicación:
### Entidades:
- Usuarios
- Noticias
- Fotos
- Videos
- Eventos
- Quizzes

(Planetas)
(Foto relaciones)

### Permisos de usuarios:
- Usuario anónimo: Puede ver los vídeos y el sistema solar. En cuanto a las noticias y las fotos podrá verlas pero no podrá darle like. En cuanto al calendario podrá ver los eventos pero no podrá solicitar notificaciones. En cuanto a los quizzes podrá ver los nombres pero no hacerlos ni ver sus preguntas.
- Usuario registrado: Además de lo anterior puede dar like a noticias y fotos, solicitar notificaciones de eventos y realizar los quizzes.
- Admin: Además de todo lo anterior, podrá gestionar la creación, modificación y eliminación de noticias, fotos, vídeos, eventos en el calendario y quizzes.

### Imágenes:
Tendrán imágenes asociadas los usuarios (foto personal), las noticias, las fotos y los quizzes.

### Gráfico con consulta avanzada:
Se mostrará un grafo que muestre cuantas veces se ha completado al 100% cada quizz (un usuario solo puede completar cada quizz una vez)

### Tecnología complementaria:
- Youtube: Se podrán vídeos de youtube en la propia web o, si quieres, también podrás acceder al enlace del vídeo en youtube
- p5.js: Una librería que se usará para facilitar la creación de un sistema solar dinámico interactivo en la web.

### Wireframe
![wireframe](https://github.com/user-attachments/assets/6287d09a-c8fd-4ec1-af44-1334a0390cf4)

# Fase 1
