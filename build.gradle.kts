import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask
import com.google.protobuf.gradle.proto
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import com.google.protobuf.gradle.testProtobuf

group "com.laeith"
version "1.0-SNAPSHOT"

plugins {
    java
    application
    idea

    id("com.github.davidmc24.gradle.plugin.avro-base") version "1.0.0"
    id("com.google.protobuf") version "0.8.15"
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

val protobufVer = "3.15.1"
val avroVer = "1.10.2"
val sbeVer = "1.21.0"
val jacksonVer = "1.21.0"
val xmlJacksonVer = "2.12.2"
val woodstoxVer = "5.1.0"
val dslJsonVer = "1.9.8"

val agronaVer = "0.9.1"

val junitVer = "5.7.0"
val assertJVer = "3.11.1"
val mockitoVer = "3.3.3"

val findbugsVer = "3.0.1"
val jmhVer = "1.29"
val jolVer = "0.9"


dependencies {
//    Serialization
    implementation("com.google.protobuf:protobuf-java:$protobufVer")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVer")
    implementation("org.apache.avro:avro:$avroVer")
    implementation("uk.co.real-logic:sbe-all:$sbeVer")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVer")
    annotationProcessor("com.dslplatform:dsl-json-java8:$dslJsonVer")
    implementation("com.dslplatform:dsl-json-java8:$dslJsonVer")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$xmlJacksonVer")
    implementation("com.fasterxml.woodstox:woodstox-core:$woodstoxVer")

//    Java structures
    implementation("org.agrona:Agrona:$agronaVer")

//    Testing / utils
    implementation("org.openjdk.jol:jol-core:$jolVer")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:$jmhVer")
    implementation("org.openjdk.jmh:jmh-core:$jmhVer")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitVer")
    testImplementation("org.assertj:assertj-core:$assertJVer")
    testImplementation("org.mockito:mockito-core:$mockitoVer")

    protobuf(files("messages/proto/"))
//    testProtobuf(files("messages/proto/"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

sourceSets {
    main {
        proto {
            srcDir("generated/generatedProtobuf")
        }
        java.srcDir("generated")
    }

    test {
        java.srcDir("generated")
    }
}

tasks.register<GenerateAvroJavaTask>("generateAvro") {
    source("messages/avro")
    setOutputDir(File("generated/generatedAvro"))
}

tasks.named("compileJava") {
    dependsOn("deleteGenerated")
    dependsOn("generateAvro")
    dependsOn("generateSBE")
}

tasks.register<JavaExec>("generateSBE") {
    systemProperties["sbe.output.dir"] = "$projectDir/generated/generatedSBE"
    systemProperties["sbe.target.namespace"] = "com.laeith.playground"

    classpath = sourceSets.main.get().compileClasspath
    main = "uk.co.real_logic.sbe.SbeTool"
    args = listOf("$projectDir/messages/sbe/schema.xml")
}

tasks.register<Delete>("deleteGenerated") {
    delete(files("$projectDir/generated"))
}

protobuf {
    generatedFilesBaseDir = "$projectDir/generated/generatedProtobuf"

    protoc {
        // Will download protoc from a repository and use it
        artifact = "com.google.protobuf:protoc:$protobufVer"
    }
}

