# README.md

## Starting the application

Mit ``./gradlew run``, danach ist das Controllpanel im Browser unter [localhost:8080](http://localhost:8080) verfügbar.

Output der Application ist leider nicht im Front-end verfügbar, ausschlielich über die Logs im Terminal.

## Design decisions

### Front-end
Damit wir eine einfache intuitivie Steuerung haben und nicht viel Dokumentation für die Interaktion machen mussten.

**Konsequenzen/Nachteile dadurch**:
- Einbindung der Actors in den RestController über statische ActorRef's, da wir uns nich mit Http-Akka herumschlagen wollten.
- Dadurch nicht wirklich Rückmeldung ans Front-End möglich.

### Keine Steuerungs-Actors/Blackboard

