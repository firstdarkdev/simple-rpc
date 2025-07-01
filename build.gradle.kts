import com.hypherionmc.modpublisher.properties.ReleaseType
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("java")
    id("com.hypherionmc.modutils.orion") version "2.0.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.wagyourtail.unimined") version "1.2.9"
    id("com.hypherionmc.modutils.modpublisher") version "2.1.6"
    id("maven-publish")
}

orion {
    setup {
        enableMirrorMaven.set(true)
        enableReleasesMaven.set(true)
        enableSnapshotsMaven.set(true)


        dopplerToken.set(System.getenv("DOPPLER_KEY"))

        versioning {
            var relType = project.properties["releaseType"] ?: orion.getProperty("release_type")
            identifier("$relType")
        }

        tools {
            lombok()
            noLoader()
        }
    }
}

group = orion.getProperty("project_group")

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()

    maven("https://api.modrinth.com/maven") {
        content {
            includeGroup("maven.modrinth")
        }
    }
}

val shade by configurations.creating

configurations {
    getByName("implementation").extendsFrom(shade)
}

dependencies {
    // CraterLib
    compileOnly("com.hypherionmc.craterlib:CraterLib-Common-1.20.4:${orion.getProperty("craterlib")}:dev")

    // Shaded
    shade("org.jodd:jodd-http:6.2.1")
    shade("dev.firstdark.discordrpc:discord-rpc:${orion.getProperty("rpc_version")}")

    // Compat
    compileOnly("maven.modrinth:replaymod:1.20.4-2.6.14")
}

tasks.shadowJar {
    from(sourceSets.main.get().output)
    configurations = listOf(shade)

    relocate("dev.firstdark.rpc", "com.hypherionmc.simplerpc.rpcsdk")
    relocate("jodd", "com.hypherionmc.simplerpc.jodd")
    exclude("logback.xml")

    doLast {
        delete(tasks.jar.get().outputs.files)
    }
}

tasks.jar {
    archiveBaseName.set("slim")
    finalizedBy(tasks.shadowJar)

    manifest {
        attributes(
            mapOf(
                "Specification-Title" to orion.getProperty("mod_name"),
                "Specification-Vendor" to orion.getProperty("mod_author"),
                "Specification-Version" to archiveVersion.get(),
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion.get(),
                "Implementation-Vendor" to orion.getProperty("mod_author"),
                "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
                "Timestamp" to System.currentTimeMillis(),
                "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
                "Built-On-Minecraft" to orion.getProperty("minecraft_version")
            )
        )
    }
}

/**
 * ===============================================================================
 * =       DO NOT EDIT BELOW THIS LINE UNLESS YOU KNOW WHAT YOU ARE DOING        =
 * ===============================================================================
 */

unimined.minecraft {
    version = orion.getProperty("minecraft_version")

    fabric {
        loader(orion.getProperty("fabric_loader"))
    }

    defaultRemapJar = false

    mappings {
        mojmap()
        devNamespace("mojmap")
    }
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
    options.release.set(17)
}

tasks.withType(GenerateModuleMetadata::class) {
    enabled = false
}

tasks.processResources {
    val buildProps = project.properties.toMap()

    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
        expand(buildProps)
    }
}

publisher {
    apiKeys {
        modrinth(orion.getenv("MODRINTH_TOKEN"))
        curseforge(orion.getenv("CURSE_TOKEN"))
        nightbloom(orion.getenv("PLATFORM_KEY"))
    }

    curseID.set(orion.getProperty("curse_id"))
    modrinthID.set(orion.getProperty("modrinth_id"))
    nightbloomID.set("simplerpc")
    setReleaseType(ReleaseType.RELEASE)
    changelog.set(project.rootProject.file("changelog.md"))
    projectVersion.set("${project.version}")
    displayName.set("[1.18.2 - 1.21.5] Simple RPC Universal ${project.version}")
    setGameVersions("1.18.2", "1.19.2", "1.19.4", "1.20", "1.20.1", "1.20.2", "1.20.4", "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7")
    setLoaders("fabric", "forge", "neoforge", "quilt")
    artifact.set(tasks.shadowJar)
    setCurseEnvironment("both")
    isManualRelease.set(true)

    modrinthDepends {
        required("craterlib")
    }

    curseDepends {
        required("craterlib")
    }

    nightbloomDepends {
        required("craterlib")
    }
}

tasks.register("javadocJar", Jar::class) {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}

tasks.register("sourcesJar", Jar::class) {
    from(sourceSets.main.get().allJava)
    archiveClassifier.set("sources")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = base.archivesBaseName
            from(components["java"])

            artifact(tasks.shadowJar.get()) {
                builtBy(tasks.shadowJar)
            }

            artifact(tasks.named("sourcesJar"))
            artifact(tasks.named("javadocJar"))
        }
    }

    repositories {
        maven {
            orion.getPublishingMaven()
        }
    }
}

tasks.javadoc {
    enabled = true
}