import com.google.protobuf.gradle.*
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.30"
    id("org.jetbrains.kotlin.kapt") version "1.4.30"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.30"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("io.micronaut.application") version "1.4.2"
    id("com.google.protobuf") version "0.8.13"
}

allOpen{
    annotation("io.micronaut.http.annotation.Controller")
}

version = "0.1"
group = "br.com.zup"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("br.com.zup.*")
    }
}

dependencies {
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("javax.annotation:javax.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    //Test
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.mockito:mockito-core:3.8.0")
    testImplementation("org.mockito:mockito-inline:3.8.0")
    testImplementation("io.micronaut.test:micronaut-test-junit5:2.3.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")


    //Validation
    implementation("io.micronaut:micronaut-validation")
    implementation("org.hibernate:hibernate-validator:6.1.6.Final")

    //GRPC
    implementation("io.micronaut.grpc:micronaut-grpc-client-runtime")
}


application {
    mainClass.set("br.com.zup.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }


}
sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.33.1"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
            }
        }
    }
}
