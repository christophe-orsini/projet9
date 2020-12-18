package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CompteComptableTest
{
	@Test
	void getByNumero_Found_ShouldReturnCompte1()
	{
		// arrange
		CompteComptable expected = new CompteComptable(1);
		List<CompteComptable> listCompte = new ArrayList<CompteComptable>();
		listCompte.add(expected);
		
		// act
		CompteComptable actualResult = CompteComptable.getByNumero(listCompte, 1);
		
		// assert
		assertThat(actualResult).isEqualTo(expected);
	}

	@Test
	void getByNumero_NotFound_ShouldReturnNull()
	{
		// arrange
		CompteComptable expected = new CompteComptable(1, "Compte 1");
		List<CompteComptable> listCompte = new ArrayList<CompteComptable>();
		listCompte.add(expected);
		
		// act
		CompteComptable actualResult = CompteComptable.getByNumero(listCompte, 2);
		
		// assert
		assertThat(actualResult).isNull();
	}
	
	@Test
	void getByNumero_EmptyListShould_ReturnNull()
	{
		// arrange
		List<CompteComptable> listCompte = new ArrayList<CompteComptable>();
		
		// act
		CompteComptable actualResult = CompteComptable.getByNumero(listCompte, 3);
		
		// assert
		assertThat(actualResult).isNull();
	}
	
	@Test
	void getByNumero_ListWithNull_ShouldReturnNull()
	{
		// arrange
		List<CompteComptable> listCompte = new ArrayList<CompteComptable>();
		listCompte.add(null);
		
		// act
		CompteComptable actualResult = CompteComptable.getByNumero(listCompte, 3);
		
		// assert
		assertThat(actualResult).isNull();
	}
	
	@Test
	void getByNumero_CompteWithoutNumero_ShouldReturnNull()
	{
		// arrange
		List<CompteComptable> listCompte = new ArrayList<CompteComptable>();
		listCompte.add(new CompteComptable());
		
		// act
		CompteComptable actualResult = CompteComptable.getByNumero(listCompte, 3);
		
		// assert
		assertThat(actualResult).isNull();
	}
	
	@Test
	void toString_ShouldReturnString()
	{
		// arrange
		CompteComptable compteUnderTest = new CompteComptable();
		compteUnderTest.setNumero(9);
		compteUnderTest.setLibelle("Libellé");
		
		String expected = "CompteComptable{numero=9, libelle='Libellé'}";
		
		// act
		String actual = compteUnderTest.toString();
		
		// assert
		assertThat(actual).isEqualTo(expected);
	}
}
