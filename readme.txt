Skład grupy:

Paweł Szapiel
Kamil Łozowski
Łukasz Terpiłowski

Zalecana konfiguracja:

Java 11
Postgresql 14.2

Instrukcja:

    1. Zbuduj projekt:

        mvn package

    2. Utwórz bazę danych psql:

        create database your_db_name

    3. Dodaj konfigurację połączenia do bazy do pliku application.properties

        spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
        spring.datasource.username=your_username
        spring.datasource.password=your_password

    4. Uruchom Spring Boot app używając maven'a:

        mvn spring-boot:run



