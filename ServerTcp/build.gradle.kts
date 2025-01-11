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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("javax.xml.bind:jaxb-api:2.1")

}

tasks.test {
    useJUnitPlatform()
}