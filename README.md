Mindrobot
=========

Folder:

	./maps - store maps here
	./maps/<mapname>Images/<Custom<FeldChar>|G<GoalNumber>> - store custom images here

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