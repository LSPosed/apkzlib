plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

if (project == rootProject) {
    repositories {
        google()
        mavenCentral()
    }
}

dependencies {
    implementation("com.android.tools.build:apksig:7.1.3")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    compileOnlyApi("com.google.auto.value:auto-value-annotations:1.9")
    annotationProcessor("com.google.auto.value:auto-value:1.9")
}
