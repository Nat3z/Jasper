import dev.architectury.pack200.java.Pack200Adapter
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    java
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.8.0"
}
apply(plugin = "com.github.johnrengelman.shadow")

group = "com.nat3z.jasper"
version = "1.0.0"
//var mcDirectory = "${System.getenv("APPDATA")}/.minecraft/mods/"
var mcDirectory = "${System.getenv("APPDATA")}/PrismLauncher/instances/1.8.9 [Test Forge]/.minecraft/mods"

// Toolchains:
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

// Minecraft configuration:
loom {
    launchConfigs {
        "client" {
            // If you don't want mixins, remove these lines
            property("mixin.debug", "true")
            property("asmhelper.verbose", "true")
            property("elementa.dev", "true")
            property("elementa.debug", "true")
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            arg("--mixin", "mixins.jasper.json")
        }
    }
    forge {
        pack200Provider.set(Pack200Adapter())
        // If you don't want mixins, remove this lines
        mixinConfig("mixins.jasper.json")
    }
    // If you don't want mixins, remove these lines
    mixin {
        defaultRefmapName.set("mixins.jasper.refmap.json")
    }
}

sourceSets.main {
    output.setResourcesDir(file("$buildDir/classes/java/main"))
}

// Dependencies:

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
    maven(url = "https://repo.essential.gg/repository/maven-public")
    // If you don't want to log in with your real minecraft account, remove this line
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    // If you don't want mixins, remove these lines
    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    }
    annotationProcessor("org.spongepowered:mixin:0.8.4-SNAPSHOT")

    // If you don't want to log in with your real minecraft account, remove this line
    runtimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.1.0")
    implementation(kotlin("stdlib-jdk8"))

    shadowImpl("gg.essential:loader-launchwrapper:1.1.3") {
        isTransitive = false
    }

    shadowImpl(fileTree("libs/BlendingMC.jar"))

    implementation("gg.essential:essential-1.8.9-forge:11092+gecb85a783")

}

// Tasks:

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.withType(Jar::class) {
    archiveBaseName.set("jasper")
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"

        // If you don't want mixins, remove these lines
        this["TweakClass"] = "gg.essential.loader.stage0.EssentialSetupTweaker"
        this["MixinConfigs"] = "mixins.jasper.json"
    }
}

val remapJar by tasks.named<RemapJarTask>("remapJar") {
    archiveClassifier.set("all")
    from(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)

    doLast {
        println("Copying Mod to Minecraft Mods Folder... Jar: $buildDir/libs/jasper-$version-all.jar to $mcDirectory ")
        copy {
            from("$buildDir/libs/jasper-$version-all.jar")
            into(mcDirectory)
        }

    }
}

tasks.shadowJar {
    archiveClassifier.set("all-dev")
    configurations = listOf(shadowImpl)

    // If you want to include other dependencies and shadow them, you can relocate them in here
    // relocate("gg.essential.vigilance", "com.nat3z.jasper.vigilance")
    // relocate("gg.essential.elementa", "com.nat3z.jasper.elementa")
    // relocate("gg.essential.universal", "com.nat3z.jasper.universal")
    relocate("kotlinx.serialization", "com.nat3z.jasper.ktx-serialization")
    relocate("kotlinx.coroutines", "com.nat3z.jasper.ktx-coroutines")

    doLast {
        configurations.forEach {
            println("Config: ${it.files}")
        }
    }
}

tasks.assemble.get().dependsOn(tasks.remapJar)

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
