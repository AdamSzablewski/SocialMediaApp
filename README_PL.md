## Aplikacja mediów społecznościowych
- [Angielski](README.md)

## Przegląd
Projekt jest backendem do aplikacji społecznościowej, która umożliwia użytkownikom udostępnianie wideo, zdjęć lub wpisów tekstowych,
pozwala również angażowanie się w treść poprzez komentowanie wpisów i innych komentarzy oraz polubienie treści w celu wpływania na jej popularność. 
Aplikacja pozwala również użytkownikom komunikować się z innymi użytkownikami za pomocą wbudowanej funkcji wiadomości, która umożliwia 
rozpoczęcie konwersacji z innym użytkownikiem lub grupową rozmowę, w której użytkownicy mogą wysyłać zarówno wiadomości tekstowe, zdjęciowe lub wideo. Aplikacja zapewnia także bezpieczne szyfrowanie AES-256 dla wiadomości. 
W przypadku zapomnienia hasła użytkownicy mogą również generować jednorazowe hasło, które zostanie wysłane do nich E-mailem.

## Użyte technologie
- **Java:** Główny język programowania.
- **AWS S3 i CloudFront:** Do przechowywania i streamowania filmów oraz zdjęć.
- **Spring-framework**
- **AOP (Programowanie aspektowe):** Wykorzystuje kombinację programowania aspektowego i własnych adnotacji do implementacji zabezpieczeń na poziomie metod, co zapewnia dużą elastyczność i łatwość w implementacji autoryzacji w celu ograniczenia dostępu tam, gdzie jest to potrzebne.
- **JUnit i Mockito:** Używa JUnit i Mockito do dokładnego testowania jednostkowego.
- **MySQL:**

## Kluczowe funkcje
### Wpisy:
- **Typ wpisów:** Użytkownicy mogą tworzyć i udostępniać wideo, zdjęcia lub wpisy tekstowe.
- **Serwowanie danych:** Wpisy obrazów i wideo są serwowane bezpośrednio do użytkownika za pośrednictwem Amazon CloudFront i AWS S3
- **Inne informacje:** Wpisy mogą być publiczne dla wszystkich lub prywatne, co pozwala tylko znajomym użytkownika zobaczyć wpis.
- Użytkownicy mogą zobaczyć liczbę wyświetleń wpisu, polubień i komentarzy oraz kto polubił/komentował wpis.

### Komentowanie i odpowiadanie:
- **Komentarze:** Użytkownicy mogą komentować wpisy oraz odpowiadać na inne komentarze.
- **Popularność:** Komentarze dla wpisu lub te, które są odpowiedzią na inny komentarz, są sortowane w kolejności prezentującej największą popularność poprzez kombinację polubień lub odpowiedzi pod komentarzem.

### System polubień:
- **Polubienia:** Aplikacja zawiera system polubień zarówno dla wpisów, jak i komentarzy, co wpływa na popularność treści.
- **Inne:** Inni użytkownicy mogą zobaczyć informacje o tych, którzy polubili wpis lub komentarz.

### Zarządzanie znajomymi:
- **Znajomi:** Użytkownicy mogą łączyć się z innymi poprzez wysyłanie i akceptowanie zaproszeń do znajomych, co rozszerza ich sieć społeczną.
### Funkcje wiadomości:
- **Wiadomości:** Aplikacja pozwala użytkownikom komunikować się między sobą za pomocą wiadomości tekstowych, obrazów lub wideo.
- **Rodzaje konwersacji:** Konwersacja może być prowadzona między 2 lub więcej użytkownikami, którzy rozpoczęli rozmowę grupową.
- **Bezpieczeństwo:** Wszystkie wiadomości są szyfrowane za pomocą szyfrowania AES-256.
### Email:
- **Zapomniane hasło:** W przypadku zapomnienia hasła aplikacja wysyła jednorazowe hasło na adres e-mail użytkownika.
### Rejestracja i uwierzytelnianie użytkowników:
- **Rejestracja i uwierzytelnianie użytkowników:** Wszyscy użytkownicy są uwierzytelniani za pomocą JWT (Token JSON Web).
- **Bezpieczne przechowywanie haseł:** Hasła są szyfrowane algorytmem SHA-512.
### Testowanie jednostkowe z JUnit i Mockito:**
- **Testowanie jednostkowe** Ponad 100 testów jednostkowych jest zawartych w aplikacji, aby zapewnić poprawność działania wszystkiego.
