package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class SequenceEcritureComptableTest
{
	@Test
	void toString_ShouldReturnString()
	{
		// arrange
		SequenceEcritureComptable sequenceUnderTest = new SequenceEcritureComptable("BQ", 2020, 6);
				
		String expected = "SequenceEcritureComptable{journal=BQ, annee=2020, derniereValeur=6}";
		
		// act
		String actual = sequenceUnderTest.toString();
		
		// assert
		assertThat(actual).isEqualTo(expected);
	}
}
