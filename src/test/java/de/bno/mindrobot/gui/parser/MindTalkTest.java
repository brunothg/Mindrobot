package de.bno.mindrobot.gui.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.bno.mindrobot.gui.RobotControl;

public class MindTalkTest
{

	MindTalk interpreter;

	@Before
	public void setUp() throws Exception
	{
		interpreter = new MindTalk();
	}

	@Test
	public void testRunSimpleCommands() throws Exception
	{
		String script = "vorwärts. rückwärts.\t linksDrehen.\n\trechtsDrehen.";
		RobotControlTest ctrl = new RobotControlTest(true, true, false, true, false, false);
		interpreter.run(ctrl, script);

		assertEquals("vrldrd", ctrl.getControlProtokoll());
	}

	@Test
	public void testRunWennFalseDannSonst() throws Exception
	{
		String script = "Wenn steheVorHindernis? Dann linksDrehen. Sonst rechtsDrehen. Ende";
		RobotControlTest ctrl = new RobotControlTest(true, true, false, true, false, false);
		interpreter.run(ctrl, script);

		assertEquals("svhrd", ctrl.getControlProtokoll());
	}

	@Test
	public void testRunWennTrueDannSonst() throws Exception
	{
		String script = "Wenn !binVerwirrt? Dann linksDrehen. Sonst rechtsDrehen. Ende";
		RobotControlTest ctrl = new RobotControlTest(true, true, false, true, false, false);
		interpreter.run(ctrl, script);

		assertEquals("bvld", ctrl.getControlProtokoll());
	}

	@Test
	public void testRunWiederhole() throws Exception
	{
		String script = "Wiederhole 5 vorwärts. rückwärts. Ende";
		RobotControlTest ctrl = new RobotControlTest(true, true, false, true, false, false);
		interpreter.run(ctrl, script);

		assertEquals("vrvrvrvrvr", ctrl.getControlProtokoll());
	}

	@Test
	public void testSplitWennDannSonstBlock() throws Exception
	{

		String divider = "Sonst";
		String starter = "Dann";
		String[] block = new String[] { "Wenn", "steheVorHindernis?", "Dann", "Wenn", "binVerwirrt?", "Dann",
				"vorwärts.", "Sonst", "Ende", "Sonst", "Wenn", "!steheVorHindernis?", "Dann", "linksDrehen.", "Sonst",
				"rechtsDrehen.", "Ende", "Ende" };

		String[] expectedFirstBlock = new String[] { "Wenn", "steheVorHindernis?", "Dann", "Wenn", "binVerwirrt?",
				"Dann", "vorwärts.", "Sonst", "Ende" };
		String[] expectedSecondBlock = new String[] { "Sonst", "Wenn", "!steheVorHindernis?", "Dann", "linksDrehen.",
				"Sonst", "rechtsDrehen.", "Ende", "Ende" };

		String[][] splitBlocks = MindTalk.splitBlock(block, divider, starter);
		String[] firstBlock = splitBlocks[0];
		String[] secondBlock = splitBlocks[1];

		assertEquals(expectedFirstBlock.length, firstBlock.length);
		for (int i = 0; i < firstBlock.length; i++)
		{
			assertEquals(expectedFirstBlock[i], firstBlock[i]);
		}

		assertEquals(expectedSecondBlock.length, secondBlock.length);
		for (int i = 0; i < secondBlock.length; i++)
		{
			assertEquals(expectedSecondBlock[i], secondBlock[i]);
		}
	}

	@Test
	public void testSubBlock() throws Exception
	{

		String[] block = new String[] { "Wiederhole", "2", "vorwärts.", "rückwärts.", "linksDrehen.", "rechtsDrehen.",
				"Wenn", "steheVorHindernis?", "Dann", "linksDrehen.", "Ende", "Ende" };
		String[] expectedSubBlock = new String[] { "rückwärts.", "linksDrehen.", "rechtsDrehen.", "Wenn",
				"steheVorHindernis?" };

		String[] subBlock = MindTalk.subBlock(block, 3, 8);

		assertEquals(expectedSubBlock.length, subBlock.length);
		for (int i = 0; i < subBlock.length; i++)
		{
			assertEquals(expectedSubBlock[i], subBlock[i]);
		}
	}

	class RobotControlTest implements RobotControl
	{

		String protokoll = "";
		private boolean moveforwards;
		private boolean movebackwards;
		private boolean isblockedfieldinfront;
		private boolean isfieldinfrontaccessible;
		private boolean standonconfusingfield;
		private boolean isconfused;

		public RobotControlTest(boolean moveforwards, boolean movebackwards, boolean isblockedfieldinfront,
			boolean isfieldinfrontaccessible, boolean standonconfusingfield, boolean isconfused)
		{
			this.moveforwards = moveforwards;
			this.movebackwards = movebackwards;
			this.isblockedfieldinfront = isblockedfieldinfront;
			this.isfieldinfrontaccessible = isfieldinfrontaccessible;
			this.standonconfusingfield = standonconfusingfield;
			this.isconfused = isconfused;
		}

		@Override
		public void turnLeft()
		{
			protokoll += "ld";
		}

		public String getControlProtokoll()
		{
			return protokoll;
		}

		@Override
		public void turnRight()
		{
			protokoll += "rd";
		}

		@Override
		public boolean moveForwards()
		{
			protokoll += "v";
			return moveforwards;
		}

		@Override
		public boolean moveBackwards()
		{
			protokoll += "r";
			return movebackwards;
		}

		@Override
		public boolean isBlockedFieldInFront()
		{
			protokoll += "ibfif";
			return isblockedfieldinfront;
		}

		@Override
		public boolean isFieldInFrontAccessible()
		{
			protokoll += "svh";
			return isfieldinfrontaccessible;
		}

		@Override
		public boolean standOnConfusingField()
		{
			protokoll += "socf";
			return standonconfusingfield;
		}

		@Override
		public boolean isConfused()
		{
			protokoll += "bv";
			return isconfused;
		}

		@Override
		public int standOnGoalField()
		{
			protokoll += "sogf";
			return -1;
		}

		@Override
		public int getLastGoalNumber()
		{
			protokoll += "glgn";
			return 0;
		}

	}
}
