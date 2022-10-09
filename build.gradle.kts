plugins {
    `java`
    id("java-library")
    id("idea")
    id("io.freefair.lombok") version "6.5.0.2"
    `maven-publish`
}

version = "1.0"
group = "com.github.wisp-capital"


repositories {
    mavenCentral()
}

dependencies {
    implementation(files("lib/TwsApi.jar"))
    implementation("io.projectreactor:reactor-core:3.4.18")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("joda-time:joda-time:2.10.14")
    implementation("org.slf4j:slf4j-simple:2.0.0-alpha0")
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to rootProject.name,
                "Implementation-Version" to project.version
            )
        )
    }
    archiveFileName.set("ib_reactor.jar")
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.wisp-capital"
            artifactId = "ib-reactor"
            version = "0.0.5"

            from(components["java"])
        }
    }
}