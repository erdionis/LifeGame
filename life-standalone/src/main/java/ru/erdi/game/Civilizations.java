package ru.erdi.game;

public class Civilizations {

	public static void main(String[] args) {
		if (args.length>0) {
			Form form=Form.lookupByName(args[0].toUpperCase());
			new Empire().seventhDay(form==null?Form.RANDOM:form);	
		} else {
			new Empire().seventhDay(Form.RANDOM);
		}
	}

}
