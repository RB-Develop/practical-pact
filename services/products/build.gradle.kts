import java.io.ByteArrayOutputStream

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization").version("1.7.0")
    id("au.com.dius.pact").version("4.3.9")
}

group = "com.bulsing"
version = "0.0.1"
application {
    mainClass.set("com.bulsing.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("au.com.dius.pact.consumer:junit5:4.3.9")
    testImplementation("au.com.dius.pact.provider:junit5:4.3.9")
}

fun getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

fun getGitBranch(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

tasks.test {
    useJUnitPlatform()

    if (System.getProperty("pactPublishResults") == "true") {
        systemProperty("pact.provider.version", getGitHash())
        systemProperty("pact.provider.tag", getGitBranch())
        systemProperty("pact.verifier.publishResults", "true")
    }
}

pact {
    publish {
        pactDirectory = "build/pacts"
        pactBrokerUrl = "http://localhost:8000/"
        pactBrokerUsername = System.getProperty("PACT_BROKER_USERNAME", "pact_workshop")
        pactBrokerPassword = System.getProperty("PACT_BROKER_PASSWORD", "pact_workshop")
        tags = listOf(getGitBranch(), "test", "prod")
        consumerVersion = getGitHash()
    }
    broker {
        pactBrokerUrl = "http://localhost:8000/"
        pactBrokerUsername = System.getProperty("PACT_BROKER_USERNAME", "pact_workshop")
        pactBrokerPassword = System.getProperty("PACT_BROKER_PASSWORD", "pact_workshop")
    }
}
