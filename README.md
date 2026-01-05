# CineValor - Aplicación de Películas 🎬

Aplicación Android desarrollada en Kotlin que consume la API de TMDb (The Movie Database) para mostrar información de películas.

## 📋 Características Implementadas

### ✅ Fase 1: Carga de Películas (COMPLETADA)

- **Integración con TMDb API**: Consumo directo de la API de películas
- **Arquitectura de datos**: Repository pattern con Room Database para caché local
- **Interfaz moderna**: RecyclerView horizontal con cards de películas
- **Categorías de películas**:
  - Películas Populares
  - Recomendadas por la comunidad (Top Rated)
  - En Cartelera (Now Playing)
- **Filtrado por géneros**: Ciencia Ficción, Acción, Comedia, Drama, Terror
- **Búsqueda de películas**: Con debounce de 500ms
- **Detalle de película**: Vista completa con sinopsis, rating y backdrop
- **Sistema de favoritos**: Guardar películas en base de datos local

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI**: Material Design 3, ConstraintLayout, RecyclerView, CardView, Chips
- **Networking**: Retrofit 2.9.0, OkHttp 4.12.0, Gson
- **Base de datos local**: Room 2.6.1
- **Carga de imágenes**: Coil 2.5.0
- **Concurrencia**: Kotlin Coroutines 1.7.3
- **Arquitectura**: MVVM light con Repository pattern
- **View Binding**: Para acceso seguro a vistas

## 📦 Dependencias Agregadas

```kotlin
// Networking
- Retrofit 2.9.0
- Retrofit Gson Converter
- OkHttp Logging Interceptor

// Database
- Room Runtime, KTX y Compiler

// Image Loading
- Coil 2.5.0

// Coroutines
- Kotlinx Coroutines Core y Android

// Lifecycle
- ViewModel KTX, LiveData KTX, Runtime KTX
```

## 🚀 Configuración Inicial

### 1. Obtener API Key de TMDb

1. Ve a [https://www.themoviedb.org/](https://www.themoviedb.org/)
2. Crea una cuenta gratuita o inicia sesión
3. Ve a **Settings > API**
4. Solicita una **API Key** (selecciona "Developer")
5. Llena el formulario simple (uso educativo/personal)
6. Copia tu **API Key (v3 auth)**

### 2. Configurar la API Key en el proyecto

Abre el archivo: `app/src/main/java/com/example/proyecto/data/remote/TMDbApiService.kt`

Busca la línea:
```kotlin
const val API_KEY = "TU_API_KEY_AQUI"
```

Reemplázala con tu API Key:
```kotlin
const val API_KEY = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
```

### 3. Sincronizar el proyecto

En Android Studio:
1. Haz clic en **File > Sync Project with Gradle Files**
2. Espera a que descargue todas las dependencias
3. Compila y ejecuta el proyecto

O desde terminal:
```bash
./gradlew build
```

## 📱 Estructura del Proyecto

```
app/src/main/java/com/example/proyecto/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt          # Room Database
│   │   ├── MovieDao.kt              # Data Access Object
│   │   └── Converters.kt            # Type Converters para Room
│   ├── model/
│   │   ├── Movie.kt                 # Modelo de película
│   │   ├── MovieResponse.kt         # Respuesta de API
│   │   └── Genre.kt                 # Modelo de género
│   ├── remote/
│   │   ├── TMDbApiService.kt        # Interface de Retrofit
│   │   └── RetrofitClient.kt        # Cliente singleton
│   └── repository/
│       └── MovieRepository.kt       # Repositorio de datos
├── ui/
│   └── adapter/
│       └── MovieAdapter.kt          # Adapter para RecyclerView
├── MainActivity.kt                  # Pantalla de bienvenida
├── Login.kt                         # Pantalla de login
├── Registro.kt                      # Pantalla de registro
├── Home.kt                          # Pantalla principal ⭐
├── DetallePelicula.kt               # Detalle de película ⭐
└── Perfil.kt                        # Pantalla de perfil
```

## 🎨 Layouts Actualizados

- ✅ `activity_home.xml` - RecyclerViews horizontales con chips de géneros
- ✅ `item_movie.xml` - Card de película con poster, título y rating
- ✅ `activity_detalle_pelicula.xml` - Detalle completo de película

## 🔄 Flujo de la Aplicación

1. **MainActivity** → Pantalla de bienvenida con botones
2. **Login** → Inicia sesión → **Home**
3. **Home** → Muestra películas en 3 categorías
   - Carga películas desde TMDb API
   - Permite buscar por nombre
   - Filtrar por género (chips)
   - Click en película → **DetallePelicula**
4. **DetallePelicula** → Muestra detalles completos
   - Backdrop de película
   - Sinopsis completa
   - Rating con estrellas
   - Botón de favoritos (guarda en Room)

## ⚙️ Características Técnicas

### Repository Pattern
- Abstrae la fuente de datos (API o BD local)
- Maneja errores con `Result<T>`
- Retorna `Flow` para favoritos (observables)

### Caché Local con Room
- Guarda películas favoritas en SQLite
- Usa TypeConverters para listas
- Operaciones asíncronas con suspend functions

### Networking con Retrofit
- Endpoints configurados con suspending functions
- Logging interceptor para debug
- Timeout de 30 segundos
- Lenguaje configurado a español (es-MX)

### UI Responsive
- RecyclerView con LinearLayoutManager horizontal
- ViewBinding para acceso seguro a vistas
- ProgressBar mientras carga
- Toast para errores
- Material Design 3 components

## 🐛 Solución de Problemas

### Error: "Unresolved reference 'databinding'"
**Solución**: Sincroniza el proyecto con Gradle (File > Sync Project with Gradle Files)

### Error: "API Key inválida"
**Solución**: Verifica que hayas copiado correctamente tu API Key de TMDb en `TMDbApiService.kt`

### Error: "No se cargan las imágenes"
**Solución**: Verifica que tengas permiso de Internet en el `AndroidManifest.xml`

### Error al compilar Room
**Solución**: Asegúrate de que el plugin `kotlin-kapt` esté habilitado en `build.gradle.kts`

## 📝 Próximos Pasos Sugeridos

- [ ] Implementar sistema de autenticación real (Firebase Auth)
- [ ] Agregar navegación con Bottom Navigation Bar
- [ ] Implementar pantalla de Perfil funcional
- [ ] Agregar historial de películas vistas
- [ ] Sistema de reseñas y comentarios
- [ ] Compartir películas en redes sociales
- [ ] Modo offline mejorado
- [ ] Animaciones y transiciones
- [ ] Tests unitarios y de integración

## 📄 Licencia

Este proyecto es para fines educativos.

## 🙏 Créditos

- **API de películas**: [The Movie Database (TMDb)](https://www.themoviedb.org/)
- **Imágenes de películas**: Proporcionadas por TMDb

---

**Nota**: Recuerda que necesitas una conexión a Internet activa para cargar las películas desde la API de TMDb. Los favoritos se guardan localmente y están disponibles offline.

