plugins {
    id("java")
}

group = "com.mrcodage"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
}

tasks.test {
    useJUnitPlatform()
}