# Sociala medieapplikation 
- [English](README.md)

## Översikt
Det här är ett projekt som ske presentera min kompetens inom bland annat Java-utveckling och Spring-ramverket. 
Projektet är en backend till en SocialMedia app som ger användare möjlighet att dela video, bild eller textinlägg, engagera sig i innehållet genom att kommentera 
inlägg och andra kommentarer samt gilla innehåll för att påverka dess popularitet. Applikationen tillåter användare att
kommunicera med andra användare genom den inbyggda meddelandefunktionen.

#### Testa projektet här: [Snart tillgängligt



## Använda teknologier
- **Java:** Det primära programmeringsspråket.
- **AWS S3 och CloudFront:** För lagring och streaming av videor och bilder.
- **Spring-framework**
- **AOP (Aspektinriktad programmering):** Använder en kombination av aspektinriktad programmering och anpassade annoteringar för att implementera säkerhet på metodnivå, vilket ger stor flexibilitet och enkelhet vid implementering av auktorisation för att begränsad tillstånd där det behövs.
- **JUnit och Mockito:** Använder JUnit och Mockito för grundlig enhetstestning för att säkerställa mikrotjänsternas pålitlighet.
- **PostgreSQL:** Hanterar data, inklusive inlägg, videodata och bildinformation samt användarinformation, med PostgreSQL som databashanterningssystem.

## Nyckelfunktioner
- **Inlägg:** Användare kan skapa och dela video, bild eller textbaserade inlägg med sitt nätverk.
- **Videostreaming:** Videoinlägg kan streamas av användare.
- **Kommentering och svar:** Användare kan delta i konversationer genom att kommentera inlägg eller svara på andra kommentarer.
- **Gilla-system:** Applikationen inkluderar ett gilla-system för både inlägg och kommentarer, vilket påverkar innehållets popularitet.
- **Vänhantering:** Användare kan ansluta sig till andra genom att skicka och acceptera vänförfrågningar, vilket utvidgar deras sociala nätverk.
- **Meddelandefunktionalitet:** Realtidsmeddelandefunktionalitet gör att användare kan kommunicera med varandra genom text- och bildmeddelanden.
- **Popularitetsmätare:** Populariteten för inlägg och kommentarer bestäms av antalet gillanden och kommentarer, vilket påverkar deras synlighet.
- **Användarregistrering och autentisering:** Användarregistrering och autentisering med JWT (JSON Web Tokens) för säker användarautentisering.
- **Enhetsprovning med JUnit och Mockito:** Varje mikrotjänst genomgår omfattande enhetsprovning med JUnit och Mockito för att säkerställa funktionalitet och identifiera och åtgärda potentiella problem.
