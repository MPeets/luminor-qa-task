# luminor-qa-task

Take-home for Luminor. Two suites against live systems I dont own:

- API sanity on the public Petstore (`https://petstore.swagger.io/`)
- one UI journey on `https://luminor.lv/en`

Java 21. The gradle wrapper is in the repo, so a clean clone should just work.

## Run

Linux / mac:

```
./gradlew test        # everything
./gradlew apiTest
./gradlew uiTest
```

Windows:

```
gradlew.bat test
gradlew.bat apiTest
gradlew.bat uiTest
```

UI runs headless Chrome by default. To watch it:

```
./gradlew uiTest -Dselenide.headless=false
```

Windows: `gradlew.bat uiTest -Dselenide.headless=false`

After a run, `./gradlew allureServe` opens the report. I pinned Allure to 2.34.1 because Allure 3 showed an empty report even tho the results were sitting there.

`./gradlew test` also picks up a tiny smoke test from when I wired JUnit. `apiTest` / `uiTest` are the ones that matter.

## API

Create, retrieve, update, delete a pet. Plus a GET of an id I never created, which should 404.

A few things that are not obvious from the swagger file:

Petstore is a shared sandbox. If you let the server pick ids you collide with other people's pets, so the factory generates large ids on the client.

Create returns **200**, not 201. That's what the live API does.

You also dont always read what you just wrote. POST can come back 200 and the next GET is still 404. After DELETE, GET can still be 200 for a bit. I poll until the pet is readable / gone instead of sleeping a fixed time. The polling client is quiet on purpose, otherwise Allure fills up with retry noise.

Each test tracks the ids it created and deletes them afterwards. A 404 on cleanup is fine, the pet is already gone.

## UI

One test, as asked: open the English site, hamburger (top right), About Us, Financial reports. The 2026 section should already be open and there should be a report link in it.

Locators are roles and visible text, not CSS class names. There are two "Site menu" buttons (desktop / mobile) so I take the one that's actually visible at 1920x1080.

Cookie banner: a few possible button labels ("I agree", "Accept All Cookies", etc). Wait a few seconds, click if something shows up, move on if it doesnt.

The 2026 block starts with `aria-expanded=false` for a beat, then the page sets it to true. That's just load timing, so "open" means wait until aria says true. Selenide polls (`shouldHave`).

Menu clicks are a bit different: wait until aria says expanded or the next control is visible, in case the panel shows up before the attribute does.

## Other small things

Gradle will happily skip tests if it thinks nothing changed. That's wrong when the other end is a live site, so the test tasks always run.

I didnt add extra tags like `sanity`. The class name is enough. `api` vs `ui` is the split that is actually useful.
