# README.md

## Starting the application

Mit ``./gradlew run``, danach ist das Controllpanel im Browser unter [localhost:8080](http://localhost:8080) verfügbar.

Output der Application ist leider nicht im Front-end verfügbar, ausschlielich über die Logs im Terminal.

## Design decisions

Eine Übersicht der Aktoren und den wichtigsten Messages ist [hier](./actorOverview.png)  zu finden.

### Front-end
Damit wir eine einfache intuitivie Steuerung haben und nicht viel Dokumentation für die Interaktion machen mussten.

**Konsequenzen/Nachteile dadurch**:
- Einbindung der Actors in den SpringBoot-RestController über statische ActorRef's, da wir uns nicht mit Http-Akka herumschlagen wollten.
- Dadurch nicht wirklich Rückmeldung ans Front-End möglich.
- Weil es über statische ActorRef's gemacht wurde, heißt es wir können nur eine Instanz von jedem Actor ansprechen, für dieses Beispiel aber ausreichend.

### Keine Steuerungs-Actors/Blackboard
Wollten dass so machen damit jeder Actor für seinen eigenene inneren State verantwortlich ist.
Im Nachhinein wären zumindest kleine Controller sinnvoll gewesen da wir in manchen Actors (Blinds) den letzten State von anderen Actors mitspeichern, einfach um weniger Messages senden zu müssen.

### Kein Temperatur Sensor, Anfrage auf Simulator
Weil bei uns der Airconditioner der Taktgeber für Temperaturmessung ist haben wir bemerkt dass der Sensor eigentlich nur eine Message von der Aircondition an den Simulator weitergeleitet hätte und die Antwort zurückgeschickt.
Weil ein Actor doch relativ viel Tipparbeit ist und wir nicht viel Mehrwert eines reines-Forwarding gesehen haben haben wir den Temperatursensor weggelassen.

