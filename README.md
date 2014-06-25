Adventure Game: Star Village
=======
##Introduction 
This is an adventure game written in Java. I wrote this project in 2006-2007 when I was at university. This game looks like a platform game: The hero can run, jump and climb ladders in a scrolling scene. However, there are RPG-like stories and dialogues. Scenes are switched by entering doors. You can save your progress at cernain scenes. Three types of weapons: sword, ring and the bombers help you beat the enemies.

**Note**: The language in this game is Chinese.

##Play
Use keyboard only:
  * Menu:-----ESC
  * Move:-----Left and Right
  * Jump:-----Z 
  * Climb:----Up and Down, if there is a ladder or a tree at your feet.
  * Talk:-----Starts as the hero reaches the NPC. Press SPACE or ENTER to continue.
  * Door:-----Up to open if the door is closed, otherwise enter the door. 
  * Sword:----Press 1 to equip. X to normal attack, costing 10% energy; C to strong attack, costing 100% energy.
  * ring:-----Press 2 to equip. Hold X and press Left and Right to aim direction. Release X to shoot. Press C to catch.
  * Bomb:-----Press 3 to equip. X to place in current place.

##Requirement
  JDK and JRE 1.5 or higher are required. Tested in Windows, Linux and Mac OS.
  
##Build
	  ./build.sh

##Run
	  ./run.sh
In Windows you can also launch the game using bin/StartGame.exe.

##Editor
There is bin/MapEditor.exe to edit maps. Scripts are also written in Java. You can find all scripts in src/redgame/scripts.

![Screen](http://github.com/redclock/Adv-Game/raw/master/doc/screens/shot1.jpg)
![Screen](http://github.com/redclock/Adv-Game/raw/master/doc/screens/shot3.jpg)
