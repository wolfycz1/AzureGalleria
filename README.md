# Azure Galleria

**Azure Galleria** is a text-based adventure game set in the eerie, flooded ruins of a 1980s luxury subterranean shopping center buried beneath the city during a massive flood in 1987.

You play as an Urban Explorer whose rope snaps during a descent, trapping you in the Central Atrium. With the water level slowly rising, you must explore the abandoned shops, interact with strange remnants of the past, and restore power to the emergency Cargo Elevator to escape back to the surface.

---

## How to play (Commands)

The game is controlled by typing text commands into the console. You can type `HELP` (Alias: `?`) at any time to see a list of available commands, or `HELP [command]` to see a detailed manual for a specific action.

* **`GO [destination]`** (Alias: `g,m`) - Moves your character to a connected room (e.g., `go Central Atrium`, `go MH`).
* **`PICKUP [item]`** (Alias: `p`) - Picks up an item from the current room and adds it to your inventory (e.g., `pickup Rusty Key`).
* **`DROP [item]`** (Alias: `d`) - Removes an item from your inventory and leaves it in the current room (e.g., `drop Rusty Key`).
* **`USE [item]`** (Alias: `u`) - Uses an item from your inventory to interact with the environment (e.g., unlocking doors, restoring power).
* **`INTERACT [character]`** (Alias: `i`) - Starts a conversation or initiates a trade with an NPC in the room. You navigate dialogue by typing the number of your choice.
* **`INVESTIGATE`** (Alias: `f`) - Repeats the full description of your current room, listing exits, characters, and items.
* **`HINT`** (Alias: `h`) - Offers a context-sensitive clue on what to do in the current room if you get stuck.
* **`EXIT`** (Alias: `e,q`) - Safely shuts down the game. (Closing the game using ctrl-c might mess up your terminal.)

---

## Game Mechanics

* **Puzzle Solving & Inventory Management:** Progressing through the mall requires finding specific items and figuring out where they belong with only 2 inventory slots.
* **Dynamic Dialogue & Trading:** Interacting with NPCs utilizes a branching dialogue tree. Some characters will only talk if you bring them the correct trade item first.
* **Internationalization (i18n):** The game supports English, Czech and Polish. The language can be selected at startup.
* **Data-Driven World:** The entire map, items, characters, and dialogue trees are loaded dynamically from a JSON file, making modification as easy as swapping a file.

---

## How to run the game

### Prerequisites
* [Java Development Kit (JDK) 21 or higher](https://www.oracle.com/europe/java/technologies/downloads/)
* [Apache Maven installed](https://maven.apache.org/download.cgi) (Only needed for Build)

### Run
[Download the latest release](https://github.com/wolfycz1/AzureGalleria/releases/latest) or [build yourself](#build).
```bash
java -jar AzureGalleria.jar
```

Note:
* On newer Java versions, you might have to include the `--enable-native-access=ALL-UNNAMED` flag for JLine to work properly.
* If running from an IDE, backspace might not work. Use alt-backspace instead.

```bash
java --enable-native-access=ALL-UNNAMED -jar AzureGalleria.jar
```

### Build

Make sure you have [Maven](https://maven.apache.org/download.cgi) installed.

```bash
git clone https://github.com/wolfycz1/AzureGalleria

cd AzureGalleria

mvn clean package

cd target

java -jar AzureGalleria-1.0-SNAPSHOT.jar
```

## Build Tools & Libraries Used
* **Java**
* **Maven**
* **Jackson**
* **JLine**
* **Lombok**
* **SLF4J & Logback**