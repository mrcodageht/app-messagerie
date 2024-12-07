plugins {
    id("java")
}

group = "com.mrcodage"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":ServerTcp"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}