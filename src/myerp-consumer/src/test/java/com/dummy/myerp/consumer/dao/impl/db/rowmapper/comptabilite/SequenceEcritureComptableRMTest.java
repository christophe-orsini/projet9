package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;

@ExtendWith(MockitoExtension.class)
class SequenceEcritureComptableRMTest
{
	RowMapper<SequenceEcritureComptable> rowMapperUnderTest = new SequenceEcritureComptableRM();
	
	@Mock ResultSet resultSetMock;
	
	@Test
	void mapRow_ShouldReturnSequenceEcritureComptable() throws SQLException
	{
		// arrange
		Mockito.when(resultSetMock.getString("journal_code")).thenReturn("BQ");
		Mockito.when(resultSetMock.getInt("annee")).thenReturn(2020);
		Mockito.when(resultSetMock.getInt("derniere_valeur")).thenReturn(15);
		SequenceEcritureComptable expected = new SequenceEcritureComptable("BQ", 2020, 15);
		
		// act
		SequenceEcritureComptable actual = rowMapperUnderTest.mapRow(resultSetMock, 1);
		
		// assert
		assertThat(actual.getCodeJournal()).isEqualTo(expected.getCodeJournal());
		assertThat(actual.getAnnee()).isEqualTo(expected.getAnnee());
		assertThat(actual.getDerniereValeur()).isEqualTo(expected.getDerniereValeur());
	}

}
