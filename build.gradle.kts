val artifactRegistryMavenSecret: String by project
plugins {
    java
    id("java-library")
    id("idea")
    id("io.freefair.lombok") version "6.5.0.2" //    id("com.google.cloud.artifactregistry.gradle-plugin") version "2.1.5"
    `maven-publish`
}

group = "com.github.wisp-capital"


repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    api(files("lib/TwsApi.jar"))
    api("io.projectreactor:reactor-core:3.4.18")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("joda-time:joda-time:2.10.14")
    implementation("org.slf4j:slf4j-simple:2.0.0-alpha0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


//publishing {
//}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.wisp-capital"
            artifactId = "ib_reactor"
            version = "1.0.5"

            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://us-west1-maven.pkg.dev/iocore/wisp-capital-jvm")
            credentials {
                username = "_json_key_base64"
                password = artifactRegistryMavenSecret
            }

            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}

repositories {
    maven {
        url = uri("https://us-west1-maven.pkg.dev/iocore/wisp-capital-jvm")
        credentials {
            username = "_json_key_base64"
            password = artifactRegistryMavenSecret
        }

        authentication {
            create<BasicAuthentication>("basic")
        }
    }
}

tasks.register("printProps") {
    doLast {
        println(artifactRegistryMavenSecret)
    }
}


