# Tinkers' Construct — Unofficial 1.21.1 NeoForge Port (WIP / AI-Assisted)

> [!WARNING]
> ### ⚠️ AVISO IMPORTANTE / EXPERIMENTAL (UNOFFICIAL FORK)
> Este proyecto es un **port NO OFICIAL y experimental** de Tinkers' Construct para **Minecraft 1.21.1 (NeoForge)**, adaptado y compilado con asistencia de Inteligencia Artificial (Hermes / DeepSeek).
> 
> * **ESTADO**: **Work In Progress (WIP)**. Se preparó con urgencia ("a las puras") para permitir pruebas en entornos 1.21.1. Puede contener bugs, inconsistencias de balance o funciones en desarrollo.
> * **ESPERA A LA VERSIÓN OFICIAL**: Por favor, **espera a que el creador o los desarrolladores originales ([SlimeKnights](https://github.com/SlimeKnights/TinkersConstruct)) publiquen la versión oficial y pulida**. Este repositorio NO busca reemplazar el trabajo del equipo original, sino servir como puente provisional de desarrollo y prueba.
> * **CRÉDITOS Y ATRIBUCIÓN**: Todos los créditos, derechos, texturas, modelos y código base pertenecen a **SlimeKnights**. Este fork se basa y agradece el trabajo de la comunidad y del repositorio [vancevoj/neotinkers](https://github.com/vancevoj/neotinkers).

---

## 📥 Descargas Directas (.jar listos para jugar)

Haz clic en los enlaces para descargar directamente los archivos e instalarlos en tu carpeta `.minecraft/mods`:

* 📥 [**Descargar NeoTinkers-1.21.1-3.11.2-v1.29.jar** (21.0 MB)](https://github.com/Blackshot32/NeoTinkers-1.21.1/raw/main/downloads/NeoTinkers-1.21.1-3.11.2-v1.29.jar)
* 📥 [**Descargar NeoMantle-1.21.1-1.21.0-v1.25.jar** (1.6 MB)](https://github.com/Blackshot32/NeoTinkers-1.21.1/raw/main/downloads/NeoMantle-1.21.1-1.21.0-v1.25.jar)

> **Requisitos de ejecución**:
> 1. Minecraft **1.21.1**
> 2. **NeoForge** `21.1.233` o superior
> 3. Ambos archivos `.jar` en la carpeta `mods`

---

## 🛠️ Cambios Clave en la Arquitectura 1.21.1

* **Data Components**: Se migró el almacenamiento de herramientas (`ToolStack`, modificadores, materiales y estadísticas) desde NBT libre hacia `DataComponentType<CompoundTag>` (`ToolDataComponents.TOOL_DATA`), adaptándose a la eliminación de NBT y capabilities en `ItemStack` en Minecraft 1.20.5+.
* **NeoForge Block Capabilities**: Sustitución completa de `LazyOptional` por el sistema moderno de `BlockCapability` (`Capabilities.FluidHandler.BLOCK` y `Capabilities.ItemHandler.BLOCK`).
* **Networking**: Migración del canal de paquetes clásico a `CustomPacketPayload` y `StreamCodec<RegistryFriendlyByteBuf, T>`.
* **Renderizado y Animaciones**: Actualización de la cadena de construcción de vértices (`VertexConsumer.addVertex(...)`) y soporte de renderizado dinámico para mesas de fundición, cubetas, canillas y fluidos.
* **Físicas y Drowning**: Configuración de `EntityAttachments` (`eyeHeight`) para slimes personalizados (`SkySlime`, `EnderSlime`, `Terracube`) y control de respiración en fluidos mediante `SlimeFluidType.canDrownIn(...)`.

---

## 🔨 Compilación desde el Código Fuente

Para compilar el mod y su biblioteca en conjunto:

```bash
# Requiere JDK 21
export JAVA_HOME=/ruta/a/tu/jdk-21
export PATH=$JAVA_HOME/bin:$PATH

# Compilar JARs listos para jugar
./gradlew jar
```

Los artefactos se generarán en `build/libs/`.

---

## ⚖️ Licencia
Este proyecto conserva las licencias originales del proyecto upstream (MIT / Licencias de SlimeKnights).
