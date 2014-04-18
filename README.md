Mindrobot
=========

Args:

	logfine - set logging level to INFO

Folder:

	./maps - store maps here
	./maps/<mapname>Images/Custom<FeldChar>|G<GoalNumber>|Avatar<Direction|Any> - store custom images here
	./lang - Translations

Maps Format:

	First line holds the MINE-TYPE for Map and has to be "text/mindrobot"
	Second line describes the map size "<x>, <y>:".
	Third line has to be "START". After this sign the mapdata begins. This data is organized in Array noation. Y-rows and X-character.
	
	Allowed Characters (upper-case / case-sensitive):
		
		# - normal Field
		S[direction] - start Field | direction = 1(EAST), 2(SOUTH), 3(WEST), 4(NORTH)
		X - blocked Field
		Z[number] - goal Field | number = 1.. You have to go to goal 1 before you can finish goal 2 and so on.
	
	Example:
	
	1: text/mindrobot
	2: 3, 3:
	3: START
	4: X#S
	5: #?#
	6: Z1#Z2
	7: END
	
Translations:
	
	Filename should have no extension and represent the language name e.g. "German"
	
	Values and example:
	
		exit_question=Wollen Sie das Spiel wirklich verlassen?
		dft=Standart
		stop=Stop
		tp_sel_lng=Weitere Sprachen müssen im Ordner '%s' abgelegt werden.
		edit=Konsole
		play=Start
		cancel=Abbrechen
		lang=Sprache
		start_app=Start
		disp_set=Grafikeinstellung
		exit=Verlassen
		fullscreen=Vollbild
		title=MindRobot
		windowed=Fenster
		one_instanz_only_message=Es läuft bereits eine Instanz des Programms.
		start=Starten
		one_instanz_only_title=Programm wurde bereits gestartet
		menu=Menü
		syntax_end=Ende
		syntax_if=Wenn
		syntax_then=Dann
		syntax_else=Sonst
		syntax_repeat=Wiederhole
		syntax_while=Solange
		robot_wall_speech=Aua! Dort kann ich nicht lang!
		robot_out_of_map_speech=Hey! Hier geblieben.
		robot_goal_x_speech=Yeah! Das %d. Ziel erreicht.
		robot_goal_last_speech=Puh! Geschafft.
		syntax_if=Wenn
		syntax_end=Ende
		syntax_then=Dann
		syntax_else=Sonst
		syntax_while=Solange
		syntax_repeat=Wiederhole
		cmd_backwards=rückwärts
		cmd_right=rechtsDrehen
		qu_confused=binVerwirrt
		cmd_forwards=vorwärts
		qu_blocked=steheVorHindernis
		cmd_left=linksDrehen
		END