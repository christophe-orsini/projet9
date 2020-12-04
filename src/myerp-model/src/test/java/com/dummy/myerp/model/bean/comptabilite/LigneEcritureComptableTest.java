package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

class LigneEcritureComptableTest
{
	/*
	 * RG_Compta_4
	 * Les montants des lignes d'écriture sont signés et peuvent prendre des valeurs négatives
	 */
	@Test
	void getDebit_MontantPositif()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Débit positif",
				new BigDecimal(100.5),
				null);
		
		// act
		BigDecimal actualResult = ligneEcriture.getDebit();
		
		// assert
		assertThat(actualResult).isPositive();
	}

	@Test
	void getDebit_MontantNegatif()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Débit négatif",
				new BigDecimal(-100.5),
				null);
		
		// act
		BigDecimal actualResult = ligneEcriture.getDebit();
		
		// assert
		assertThat(actualResult).isNegative();
	}
	
	@Test
	void getDebit_MontantZero()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Débit zéro",
				new BigDecimal(-0.0),
				null);
		
		// act
		BigDecimal actualResult = ligneEcriture.getDebit();
		
		// assert
		assertThat(actualResult).isEqualTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
	}
	
	@Test
	void getDebit_MontantNull_ShouldReturnNull()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Débit null",
				null,
				null);
		
		// act
		BigDecimal actualResult = ligneEcriture.getDebit();
		
		// assert
		assertThat(actualResult).isNull();
	}
	
	@Test
	void getCredit_MontantPositif()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Crédit positif",
				null,
				new BigDecimal(100.5));
		
		// act
		BigDecimal actualResult = ligneEcriture.getCredit();
		
		// assert
		assertThat(actualResult).isPositive();
	}

	@Test
	void getCredit_MontantNegatif()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Crédit négatif",
				null,
				new BigDecimal(-100.5));
		
		// act
		BigDecimal actualResult = ligneEcriture.getCredit();
		
		// assert
		assertThat(actualResult).isNegative();
	}
	
	@Test
	void getCredit_MontantZero()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Crédit zéro",
				null,
				new BigDecimal(-0.0));
		
		// act
		BigDecimal actualResult = ligneEcriture.getCredit();
		
		// assert
		assertThat(actualResult).isEqualTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
	}
	
	@Test
	void getCredit_MontantNull_ShouldReturnNull()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Crédit null",
				null,
				null);
		
		// act
		BigDecimal actualResult = ligneEcriture.getCredit();
		
		// assert
		assertThat(actualResult).isNull();
	}
	
	/*
	 * RG_Compta_7
	 * Les montants des lignes d'écritures peuvent comporter 2 chiffres maximum après la virgule
	 */
	@Test
	void getDebit_Precision3_ShouldReturn2()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Débit Scale 3",
				new BigDecimal("123.456"),
				null);
		
		// act
		BigDecimal actualResult = ligneEcriture.getDebit();
		
		// assert
		assertThat(actualResult.scale()).isEqualTo(2);
	}
	
	@Test
	void getCredit_Precision3_ShouldReturn2()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable(
				new CompteComptable(),
				"Crédit Scale 3",
				null,
				new BigDecimal("123.456"));
		
		// act
		BigDecimal actualResult = ligneEcriture.getCredit();
		
		// assert
		assertThat(actualResult.scale()).isEqualTo(2);	
	}
	
	@Test
	void toString_ShouldReturnString()
	{
		// arrange
		LigneEcritureComptable ligneEcriture = new LigneEcritureComptable();
		ligneEcriture.setLibelle("Ligne");
		String expected = "LigneEcritureComptable{compteComptable=null, libelle='Ligne', debit=null, credit=null}";
		
		// act
		String actual = ligneEcriture.toString();
		
		// assert
		assertThat(actual).isEqualTo(expected);
	}	
}
