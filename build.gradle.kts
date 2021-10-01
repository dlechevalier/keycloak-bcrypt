
plugins {
    java
}

group = "com.github.leroyguillaume"
version = "1.5.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.0")
    val bcryptVersion = "0.9.0"
    val jbossLoggingVersion = "3.4.2.Final"
    val keycloakVersion = "15.0.2"
    val bouncyCastleVersion = "1.69"

    // BCrypt
    implementation("at.favre.lib:bcrypt:$bcryptVersion")

    // crypto
    implementation("org.bouncycastle:bcprov-jdk15on:$bouncyCastleVersion")

    // JBoss
    compileOnly("org.jboss.logging:jboss-logging:$jbossLoggingVersion")

    // Keycloak
    compileOnly("org.keycloak:keycloak-common:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-core:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-server-spi:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-server-spi-private:$keycloakVersion")

    testCompileOnly("org.keycloak:keycloak-common:$keycloakVersion")
    testCompileOnly("org.keycloak:keycloak-core:$keycloakVersion")
    testCompileOnly("org.keycloak:keycloak-server-spi:$keycloakVersion")
    testCompileOnly("org.keycloak:keycloak-server-spi-private:$keycloakVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")
}

tasks {
    jar {
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) {
            exclude("META-INF/MANIFEST.MF")
            exclude("META-INF/*.SF")
            exclude("META-INF/*.DSA")
            exclude("META-INF/*.RSA")
        }
    }

    wrapper {
        gradleVersion = "6.4"
    }
}
