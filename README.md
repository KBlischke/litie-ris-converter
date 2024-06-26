# Konvertierung der Datenbank "Literatur zur Informationserschließung"  

Dieses Projekt zur Konvertierung der Datenbank "Literatur zur Informationserschließung" von einer modifizierten Form des Allegro-Neutralformats in das RIS-Format ist im Kontext einer Bachelorarbeit im Studiengang "Bibliothek und digitale Kommunikation" an der Technischen Hochschule Köln entstanden. Die Struktur und Funktionsweise des Projekts werden in der Arbeit detailliert beschrieben. Die Bachelorarbeit, dem dieses Programm zugrunde liegt kann unter folgender URN abgerufen werden: [urn:nbn:de:hbz:79pbc-opus-24927](https://nbn-resolving.org/urn:nbn:de:hbz:79pbc-opus-24927).  

Das Programm wurde für diese Arbeit in der Programmiersprache Java implementiert und als Maven-Projekt verwaltet. Dieser Ansatz ermöglicht eine effiziente Verwaltung der Abhängigkeiten und eine standardisierte Projektstruktur, was zur einfacheren Entwicklung, Wartung und Skalierbarkeit beiträgt. Die Verwendung von Maven erleichtert auch die Integration von Bibliotheken und externen Ressourcen, was die Entwicklung des Programms erleichtert und die Arbeitsabläufe optimiert.  

Um das Programm zu verwenden, wird eine Java-Laufzeitumgebung der Version 17 oder höher benötigt. Das Programm trägt den Namen `converter-1.0.0.jar` und befindet sich im Dateipfad `converter/target`. Es ist jedoch möglich, die Platzierung des Programms nach Belieben zu ändern.  

Um das Programm auszuführen, kann folgender Befehl in der Kommandozeile verwendet werden: `<Pfad/>java -jar converter-1.0.0.jar <dbm> [<ris>]`.  
Hierbei muss `<Pfad\>` durch den aktuellen Dateipfad zum Programm ersetzt werden. `<dbm>` sollte durch den aktuellen Dateipfad zu der zu übersetzenden DBM-Datei ersetzt werden. Optional kann `[<ris>]` weggelassen oder durch den aktuellen Dateipfad mit dem Namen einer RIS-Datei ersetzt werden, an dem die zu erzeugende RIS-Datei mit dem gewünschten Namen erstellt werden soll.  

Das Programm ist mit der entsprechenden Laufzeitumgebung bereits ausführbar, kann aber auch auf zwei verschiedene Arten selber kompiliert werden:  

1. Wenn Apache Maven mit Version 3.9.6 oder höher installiert ist, kann die Kompilierung mit folgendem Befehl in der Kommandozeile ausgeführt werden: `<Pfad/>mvn install`.  
Apache Maven steht unter der Apache-Lizenz 2.0, die es erlaubt, diese Software frei in jedem Umfeld zu verwenden, zu modifizieren und zu verteilen.  `<Pfad\>` muss durch den aktuellen Dateipfad zum Hauptverzeichnis des Repositoriums ersetzt werden, wobei wichtig ist, dass sich in diesem die Datei `pom.xml` befindet.  

2. Wenn Apache Maven mit Version 3.9.6 oder höher nicht installiert ist, kann die Kompilierung stattdessen durch folgenden Befehl in der Kommandozeile ausgeführt werden: `javac -encoding UTF-8 <Pfad/>Main.java`.  
`<Pfad\>` muss durch den aktuellen Dateipfad zum Hauptverzeichnis des Source-Codes ersetzt werden, wobei wichtig ist, dass sich in diesem die Datei `Main.java` befindet. Dadurch entstehen im Verzeichnis des Source-Codes ausführbare Dateien, die durch folgenden Befehl in der Kommandozeile ausgeführt werden können: `java <Pfad/>Main <dbm> [<ris>]`.  
Hierbei muss `<Pfad\>` durch den aktuellen Dateipfad zum Hauptverzeichnis des Source-Codes ersetzt werden, wobei wichtig ist, dass sich in diesem die Datei `Main.class` befindet.  

Zusätzlich existiert innerhalb des Dateipfades `converter/src/test/java` Test-Code, der dazu dient, die Funktionalität des Programms sicherzustellen. Dabei werden die einzelnen Module des Programms isoliert und unter verschiedenen Szenarien getestet. Diese Tests werden automatisch durch Apache Maven während der Kompilierung durchgeführt. Um die Tests manuell auszuführen, kann der folgende Befehl in der Kommandozeile verwendet werden: `<Pfad/>mvn test`.  
Hierbei muss `<Pfad\>` durch den aktuellen Dateipfad zum Hauptverzeichnis des Test-Codes ersetzt werden. Wenn während der Ausführung keine Fehlermeldungen erscheinen, waren die Tests erfolgreich. Dieser Testprozess trägt dazu bei die Qualität des Programms sicherzustellen und potenzielle Fehler frühzeitig zu erkennen und zu beheben.  

Die durch das implementierte Programm konvertierte Datenbank in das RIS-Format kann im Ordner `data` als `litie34.ris` entnommen werden. Diese Datei enthält die Datenbankinformationen im RIS-Format, die durch das Programm konvertiert wurden. Sie kann für weitere Analysen, Importe oder andere Zwecke verwendet werden, die mit dem RIS-Format kompatibel sind.  
