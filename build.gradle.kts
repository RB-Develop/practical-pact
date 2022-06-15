val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization").version("1.7.0")
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
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("au.com.dius.pact.provider:junit5:4.3.9")
}

def getGitHash = { ->
    def stdout = new ByteArrayOutputStream()
    exec {
        commandLine 'git', 'rev-parse', '--short', 'HEAD'
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

def getGitBranch = { ->
    def stdout = new ByteArrayOutputStream()
    exec {
        commandLine 'git', 'rev-parse', '--abbrev-ref', 'HEAD'
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

test {
    useJUnitPlatform()

    if (System.getProperty('pactPublishResults') == 'true') {
        systemProperty 'pact.provider.version', getGitHash()
        systemProperty 'pact.provider.tag', getGitBranch()
        systemProperty 'pact.verifier.publishResults', 'true'
    }
}
