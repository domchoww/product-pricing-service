plugins {
	java
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.google.cloud.tools.jib") version "3.3.1"
}

group = "com.inpost"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.2.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito:mockito-core:2.1.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib {
	from {
		image = project.property("jib.from.image").toString()
	}
	to {
		image = "domchowit/pricing:latest"
	}
}
