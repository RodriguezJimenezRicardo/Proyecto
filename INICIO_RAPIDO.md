# ⚡ Guía Rápida de Inicio - CineValor

## 🎯 Pasos para ejecutar la aplicación

### 1️⃣ Obtener tu API Key de TMDb (5 minutos)

1. Visita: https://www.themoviedb.org/signup
2. Crea una cuenta gratuita
3. Ve a tu perfil > Settings > API
4. Solicita API Key (elige "Developer")
5. Copia tu **API Key (v3 auth)**

### 2️⃣ Configurar la API Key

Abre: `app/src/main/java/com/example/proyecto/data/remote/TMDbApiService.kt`

Busca la línea 14 y reemplaza:
```kotlin
const val API_KEY = "TU_API_KEY_AQUI"
```

Con tu API Key real:
```kotlin
const val API_KEY = "tu_api_key_copiada_aqui"
```

### 3️⃣ Sincronizar proyecto

En Android Studio:
- Haz clic en el ícono de elefante 🐘 (Sync Project with Gradle Files)
- O: **File > Sync Project with Gradle Files**

Espera a que termine de descargar las dependencias (2-5 minutos la primera vez)

### 4️⃣ Ejecutar la app

- Presiona el botón ▶️ Run
- O: Shift + F10

---

## ✅ Funcionalidades que puedes probar

### En la pantalla Home:
- 📱 Ver películas populares, recomendadas y en cartelera
- 🔍 Buscar películas por nombre
- 🎬 Filtrar por género (Ciencia Ficción, Acción, Comedia, Drama, Terror)
- 👆 Hacer clic en cualquier película para ver detalles

### En detalle de película:
- 📖 Ver sinopsis completa
- ⭐ Ver calificación con estrellas
- ❤️ Agregar/quitar de favoritos

---

## 🐛 Problemas Comunes

### ❌ "Unresolved reference 'databinding'"
**Solución**: Sincroniza el proyecto (paso 3)

### ❌ Imágenes no cargan
**Solución**: Verifica tu conexión a Internet y que la API Key esté correcta

### ❌ "Error 401" al cargar películas
**Solución**: Tu API Key es incorrecta o no está configurada (vuelve al paso 2)

### ❌ Error al compilar
**Solución**: 
1. Build > Clean Project
2. Build > Rebuild Project
3. Sincroniza de nuevo

---

## 📊 Estado de Implementación

✅ **COMPLETADO**
- Integración con TMDb API
- Carga de películas (Populares, Top Rated, Now Playing)
- Búsqueda de películas
- Filtrado por géneros
- Detalle de películas
- Sistema de favoritos con Room Database
- Carga de imágenes con Coil
- UI moderna con Material Design

🔄 **PENDIENTE** (para futuras versiones)
- Autenticación real de usuarios
- Perfil de usuario funcional
- Sistema de reseñas
- Compartir en redes sociales
- Modo oscuro

---

## 💡 Consejos

- **API Gratuita**: TMDb ofrece 60,000 requests por día gratis, más que suficiente para desarrollo
- **Español**: Las películas se cargan en español (es-MX)
- **Offline**: Los favoritos se guardan localmente y funcionan sin Internet
- **Imágenes**: Se cargan automáticamente desde TMDb con alta calidad

---

## 🆘 ¿Necesitas ayuda?

- Documentación TMDb API: https://developers.themoviedb.org/3
- Kotlin Docs: https://kotlinlang.org/docs/home.html
- Android Developers: https://developer.android.com/

---

**¡Disfruta desarrollando! 🚀**

