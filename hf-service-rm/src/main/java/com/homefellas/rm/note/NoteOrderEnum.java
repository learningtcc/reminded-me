package com.homefellas.rm.note;

public enum NoteOrderEnum {

	NOTE_TITLE, CREATED_DATE, TAG;

	public static NoteOrderEnum getNoteOrderEnum(int ordinal)
	{
		for (NoteOrderEnum noteOrderEnum:values())
		{
			if (noteOrderEnum.ordinal()==ordinal)
				return noteOrderEnum;
		}
		return null;
	}
}
