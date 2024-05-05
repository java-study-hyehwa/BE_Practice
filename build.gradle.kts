plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.mysql:mysql-connector-j:8.0.33")
    implementation("com.github.mwiede","jsch","0.2.17")

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

//sourceSets {
//    main {
//        java {
//            srcDirs("src/main/java")
//        }
//        resources {
//            srcDirs("src/main/resources")
//        }
//    }
//}