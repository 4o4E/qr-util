import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    `maven-publish`
    `java-library`
}

group = "top.e404"
version = "1.0.0"
fun skiko(module: String, version: String = "0.7.34") = "org.jetbrains.skiko:skiko-awt-runtime-$module:$version"
fun kotlinx(id: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$id:$version"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    // coroutines
    compileOnly(kotlinx("coroutines-core-jvm", "1.6.4"))
    // skiko
    //compileOnly(skiko("windows-x64"))
    compileOnly("top.e404:skiko-util:1.0.0")
    // 二维码
    compileOnly("com.google.zxing:core:3.5.0")
    // test
    testImplementation(kotlin("test", "1.7.20"))
    // coroutines
    testImplementation(kotlinx("coroutines-core-jvm", "1.6.4"))
    // skiko
    //testImplementation(skiko("windows-x64"))
    testImplementation("top.e404:skiko-util:1.0.0")
    // 二维码
    testImplementation("com.google.zxing:core:3.5.0")
}

tasks.test {
    useJUnitPlatform()
    workingDir = projectDir.resolve("run")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    doLast {
        println("==== copy ====")
        for (file in File("build/libs").listFiles() ?: emptyArray()) {
            println("正在复制`${file.path}`")
            file.copyTo(File("jar/${file.name}"), true)
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

afterEvaluate {
    publishing.publications.create<MavenPublication>("java") {
        from(components["kotlin"])
        artifact(tasks.getByName("sourcesJar"))
        artifact(tasks.getByName("javadocJar"))
        artifactId = "qr-util"
        groupId = project.group.toString()
        version = project.version.toString()
    }
}