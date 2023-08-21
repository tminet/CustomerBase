plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    setSourceCompatibility(libs.versions.java.get())
    setTargetCompatibility(libs.versions.java.get())
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.coroutines.core)

    testImplementation(libs.junit4)
    testImplementation(libs.coroutines.test)
}