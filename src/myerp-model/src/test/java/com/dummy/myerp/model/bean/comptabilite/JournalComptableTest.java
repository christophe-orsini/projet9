package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class JournalComptableTest
{
	@Test
	void getByCode_Found_ShouldReturnBanque()
	{
		// arrange
		JournalComptable expected = new JournalComptable("BQ", "Banque");
		List<JournalComptable> journaux = new ArrayList<JournalComptable>();
		journaux.add(expected);
		
		// act
		JournalComptable actualResult = JournalComptable.getByCode(journaux, "BQ");
		
		// assert
		assertThat(actualResult).isEqualTo(expected);
	}

	@Test
	void getByCode_NotFound_ShouldReturnNull()
	{
		// arrange
		JournalComptable expected = new JournalComptable("BQ", "Banque");
		List<JournalComptable> journaux = new ArrayList<JournalComptable>();
		journaux.add(expected);
		
		// act
		JournalComptable actualResult = JournalComptable.getByCode(journaux, "VT");
		
		// assert
		assertThat(actualResult).isNull();
	}

	@Test
	void getByCode_EmptyListShould_ReturnNull()
	{
		// arrange
		List<JournalComptable> journaux = new ArrayList<JournalComptable>();
		
		// act
		JournalComptable actualResult = JournalComptable.getByCode(journaux, "BQ");
		
		// assert
		assertThat(actualResult).isNull();
	}

	@Test
	void getByCode_ListWithNull_ShouldReturnNull()
	{
		// arrange
		List<JournalComptable> journaux = new ArrayList<JournalComptable>();
		journaux.add(null);
		
		// act
		JournalComptable actualResult = JournalComptable.getByCode(journaux, "BQ");
		
		// assert
		assertThat(actualResult).isNull();
	}
	
	@Test
	void getByCode_JournalWithoutCode_ShouldReturnNull()
	{
		// arrange
		List<JournalComptable> journaux = new ArrayList<JournalComptable>();
		journaux.add(new JournalComptable());
		
		// act
		JournalComptable actualResult = JournalComptable.getByCode(journaux, "VT");
		
		// assert
		assertThat(actualResult).isNull();
	}
}
