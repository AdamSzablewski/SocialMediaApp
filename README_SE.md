# Sociala medieapplikation 
- [English](README.md)

## Översikt
Det här är ett projekt som ske presentera min kompetens inom bland annat Java-utveckling och Spring-ramverket. 
Projektet är en backend till en SocialMedia app som ger användare möjlighet att dela video, bild eller textinlägg, engagera sig i innehållet genom att kommentera 
inlägg och andra kommentarer samt gilla innehåll för att påverka dess popularitet. Applikationen tillåter användare att
kommunicera med andra användare genom den inbyggda meddelandefunktionen, som låter användare starta konversationer med en annan användare eller 
en gruppkonversation där användare kan skicka både text, bild och video meddelanden. Applikationen säkerställer även säker AES-256-kryptering för meddelanden.
Vid bortglömt lösenord kan användare även generera ett engångslösenord som skickas till användaren via email.

I det här projektet har jag använt sådana koncept som OOP, AOP, functional programming och asynchronous programming.

## Använda teknologier
- **Java:** Det primära programmeringsspråket.
- **AWS S3 och CloudFront:** För lagring och streaming av videor och bilder.
- **Spring-framework**
- **AOP (Aspektinriktad programmering):** Använder en kombination av aspektinriktad programmering och anpassade annoteringar för att implementera säkerhet på metodnivå, vilket ger stor flexibilitet och enkelhet vid implementering av auktorisation för att begränsad tillstånd där det behövs.
- **JUnit och Mockito:** Använder JUnit och Mockito för grundlig enhetstestning för att säkerställa mikrotjänsternas pålitlighet.
- **MySQL:** Hanterar data, inklusive inlägg, videodata och bildinformation samt användarinformation.

# Nyckelfunktioner

## Sökfunktion
Användare kan söka efter andra användare genom att skriva ett förnamn, efternamn eller båda

![socialApp%20480](https://github.com/AdamSzablewski/SocialMediaApp/assets/114603622/96de5bc0-2646-42a4-9dd3-8dc0f08501d0)


[Klicka här för att se videon](https://youtu.be/LnN93dLWGuQ)

## Inlägg:
- **Typ av inlägg:** Användare kan skapa och dela video, bild eller textbaserade inlägg med sitt nätverk.
- **Data-servering** Bild och Video inlägg serveras direkt till användaren genom Amazon CloudFront och AWS S3, genom sparad
  länk till filen.
- **Övrig information:** Inlägg kan publiceras som offentliga för allmänheten eller privata som låter enbart användarens vänner att
  se inlägget.
- Användare kan se antal vsiningar av inlägget, gillanden och kommentarer samt vem som har gillat-kommenterat inlägget.

![imagepost%20480](https://github.com/AdamSzablewski/SocialMediaApp/assets/114603622/ac6d812f-4564-4ea3-97d6-ed621c54b40e)

[Klicka här för att se videon](https://youtu.be/whWzh4XbZNg)


## Meddelandefunktionalitet:
- **Meddelanden** Applikationen låter användare kommunicera med varandra genom text och bild eller video-meddelanden.
- **Typ av konversationer** En konversation kan hållas mellan 2 eller flera användare som har startat en gruppkonversation.

![messaging480](https://github.com/AdamSzablewski/SocialMediaApp/assets/114603622/4b5d1fa8-f42b-4c0f-ba79-e2fd55862478)

- **Säkerhet** Alla meddelanden är krypterade med AES-256-kryptering.
![Alt text](https://github.com/AdamSzablewski/SocialMediaApp/assets/114603622/0828d164-6e42-4954-975f-54555ba2286c)

### Kommentering och svar:
- **Kommentarer:** Användare kan kommentera inlägg samt svara på andra kommentarer.
- **Popularitet:** Kommentarer för ett inlägg eller de som är svar till en annan kommentar sorteras i den ordning som visar störst
 popularitet genom en kombination av gillanden eller svar under kommentaren.

### Gilla-system:
- **Gillanden** Applikationen inkluderar ett gilla-system för både inlägg och kommentarer, vilket påverkar innehållets popularitet.
- **Övrigt** Andra användare kan se information om de som gillat ett inlägg eller en kommentar

### Vänhantering:
- **Vänner** Användare kan ansluta sig till andra genom att skicka och acceptera vänförfrågningar, vilket utvidgar deras sociala nätverk.

### Email:
- **Bortglömt lösenord** I fall som bortglömt lösenord skickar appen ett engångslösenord till användarens email. 
### Användarregistrering och autentisering:
- **Användarregistrering och autentisering** Alla användare autentiseras med JWT (JSON Web Token).
- **Säker lagring av Lösenord** Läsenord är har krypterats med en SHA-512 algoritm. 
### Enhetsprovning med JUnit och Mockito:** 
- **Enhetsprovning** Över 100 enhetsprovningar ingör i appen för att säkerställa att allt fungerar som det ska.

