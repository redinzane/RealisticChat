RealisticChat
===========

A Plugin that changes the way Minecraft's chat works to something more realistic.


**Author:** redinzane (with some help and contribution by Thomas Richner. This file is also inspired by his work).

**License:** [GNU General Public License, version 3 (GPL-3.0)](http://opensource.org/licenses/gpl-3.0)

### How to install:

Build it or get a release from here or dev.bukkit, drop into Bukkit's plugin directory.

###Features
- Ranged chat featuring talking, whispering and 4 kinds of yelling, all with configurable ranges and in the case of yelling, configurable hunger cost.
- A cell phone system, allowing players to communicate over long distances using a clock item (optionally with a configurable name), featuring conference calls of configurable size.
- Cell towers (optional), of configurable range and required height, with configurable base blocks.
- Database logging and console output of all chat. Database is laid out as follows: A conversation UUID to identify unique cell conversations, player UUID of the player chatting, their name, the type of chat, everyone who heard, the message and a timestamp.

### How to build a cell tower:


```
                      #   ^
                      #   |
 iron fences    ->    #   |
                      #   | height
                      #   |
                      #   |
                      #   |
                      #   v
 redstone torch ->   \▓   
                      ^
                      |
                base block
                
```
                  
The antenna (iron fences) must have a minimum height (configurable) and have a clear, non-obstructed view of the sky. Iron fences above the maximum height or that are not connected to the antenna count as obstructions. The higher the antenna, the bigger the range. If you successfully built a cell tower, you will see a little effect around the obsidian block.
The antenna can be shut down with Redstone power. 

### How to know you are connected to a tower:
Right-Click with a phone to show whether you are connected or not. It will let you know if you are not connected every time something would happen if you were though.

### Some bonus features:
- Automatically saves towers to a file asynchronously.
- Consistent checks of a tower's validity minimize glitching.
- The order the tower is built does not matter.
- Towers show an effect upon validation, providing instant feedback.
- Signal strength is realistically based on distance to tower (thanks Thomas).
- No external dependencies.
- Colored console output on non-Windows systems.
- Configurability of the color of all messages and the text of 99%.
- All features individually customizable and deactivatable.

### Recommendations:
- Thomas Richner's RadioTowerPlugin to add some radio towers.
- My own PlayerHider to add some realism to Line of Sight.
- Thomas Richner's Bed Nerf, to nerf beds to be used less (based on earlier plugins by him and me).
