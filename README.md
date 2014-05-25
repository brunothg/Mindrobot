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
		? - confusing Field. Invert all commands
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
	
		stop=Stop
		robot_wall_speech=Aua! Dort kann ich nicht lang!
		tp_sel_lng=Weitere Sprachen müssen im Ordner '%s' abgelegt werden.
		syntax_else=Sonst
		robot_out_of_map_speech=Hey! Hier geblieben.
		syntax_while=Solange
		cancel=Abbrechen
		lang=Sprache
		disp_set=Grafikeinstellung
		start_app=Start
		menu_repeat_x=Wiederhole x-mal
		syntax_if=Wenn
		syntax_end=Ende
		menu_branch=Verzweigung
		syntax_then=Dann
		windowed=Fenster
		title=MindRobot
		menu_move_backwards=Einen Schritt rückwärts
		menu_command=Befehl
		qu_blocked=steheVorHindernis
		cmd_backwards=rückwärts
		cmd_right=rechtsDrehen
		menu_move_forwards=Einen Schritt vorwärts
		menu_turn_right=90° im Uhrzeigersinn drehen
		dft=Standart
		exit_question=Wollen Sie das Spiel wirklich verlassen?
		menu_loop=Schleife
		menu=Menü
		play=Start
		edit=Konsole
		menu_while_do=Wiederhole solange wie
		robot_goal_last_speech=Puh! Geschafft.
		exit=Verlassen
		fullscreen=Vollbild
		qu_confused=binVerwirrt
		cmd_forwards=vorwärts
		syntax_repeat=Wiederhole
		robot_goal_x_speech=Yeah! Das %d. Ziel erreicht.
		one_instanz_only_message=Es läuft bereits eine Instanz des Programms.
		start=Starten
		menu_turn_left=90° gegen den Uhrzeigersinn drehen
		cmd_left=linksDrehen
		one_instanz_only_title=Programm wurde bereits gestartet
		menu_question=Frage
		menu_question_confused=Bin ich verwirrt?
		menu_question_obstacle=Stehe ich vor einem Hindernis?
		logging=Logging
		export=Exportieren
		java=Java
		finished=beendet
		number=Zahl
		END