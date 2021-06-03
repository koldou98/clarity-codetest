
all:
	./gradlew clean build -q
	@echo "Clean gradle clean build concluded"

clean:
	./gradlew clean -q
	@echo "Gradle files cleaned"

generateInfiniteLog:
	./gradlew :infiniteloggenerator:run

