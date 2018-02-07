package se.umu.cs.ldbn.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface I18NMessages extends Messages {

	String serverErrorCode(String errorCode);

	String sawDecomposeInto(String whatNF);
	String sawImportRelationsConfirm(String whatNF);
	String sawNFDecompositionCheck(String whatNF);
	String sawDecompositionIsInNF(String whatNF);
	String sawDecompositionIsNotInNF(String whatNF);
	String sawFDsNotClosure(String relationName);
	String sawKeyWrong(String relationName);
}

