### Opis
Aplikacja umożliwia przechowywanie zasobów internetowych. Za pomocą usługi REST wysyłane jest żądanie, które następnie trafia na kolejkę Kafki, gdzie plik jest zapisywany w minIO, a dane o pliku w bazie danych mongoDB.
### Wymagania
- gradle
- java 17
- docker-compose
### Uruchomienie
- Dodanie wpisu do pliku /etc/hosts:  
  `127.0.0.1    kafka`
- Start kontenerów:  
  `docker-compose up -d`
- Uruchomienie aplikacji:  
  `./gradlew bootRun`
### Obsługa aplikacji
- ściągnięcie pliku do przestrzeni minIO oraz zapis w bazie mongoDB:  
  `POST localhost:8080/api/file/`  
  body:  
  `{"url" : "string"}`
- pobranie wszystkich rekordów z informacją na temat plików:  
  `GET localhost:8080/api/file/`
- pobranie pliku o danym id z przestrzeni minIO:  
  `GET localhost:8080/api/file/{id}`